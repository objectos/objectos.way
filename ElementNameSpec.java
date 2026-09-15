/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ElementNameSpec {

  private static final Pattern DEFINITION = Pattern.compile(
      "<dfn[^>]*data-dfn-type=\"element\"[^>]*>(.*?)</dfn>",
      Pattern.DOTALL
  );

  private static final Pattern ENTITY = Pattern.compile(
      "&(?:amp|gt|lt|nbsp|quot|#\\d+|#x[0-9a-fA-F]+);"
  );

  private static final Pattern HEADING = Pattern.compile(
      "<h[1-6]\\b[^>]*\\bid=\"[^\"]*\"[^>]*>(.*?)</h[1-6]>",
      Pattern.DOTALL
  );

  private static final Pattern TAG = Pattern.compile("<[^>]+>");

  private static final Pattern TOKEN = Pattern.compile(
      "<dt[^>]*>(.*?)</dt>|<dd[^>]*>(.*?)</dd>",
      Pattern.DOTALL
  );

  private static final Pattern WHITESPACE = Pattern.compile("\\s+");

  private ElementNameSpec() {}

  public static void main(String[] args) throws Exception {
    final String html;
    html = read(args);

    final String section;
    section = section(html);

    final List<Row> rows;
    rows = parse(section);

    final Path target;
    target = Path.of("work", "html-elements.txt");

    write(target, rows);

    System.out.println("elements: " + rows.size());
  }

  private static String collapse(String raw) {
    final String stripped;
    stripped = TAG.matcher(raw).replaceAll("");

    final String unescaped;
    unescaped = unescape(stripped);

    final String collapsed;
    collapsed = WHITESPACE.matcher(unescaped).replaceAll(" ");

    return collapsed.trim();
  }

  private static Definition definition(String body) {
    final List<String> categories;
    categories = new ArrayList<>();

    final List<String> models;
    models = new ArrayList<>();

    final Matcher matcher;
    matcher = TOKEN.matcher(body);

    int state;
    state = 0;

    while (matcher.find()) {
      final String dt;
      dt = matcher.group(1);

      if (dt != null) {
        state = state(dt);

        continue;
      }

      final String dd;
      dd = matcher.group(2);

      final String text;
      text = collapse(dd);

      if (state == 1) {
        categories.add(text);
      } else if (state == 2) {
        models.add(text);
      }
    }

    final String joinedCategories;
    joinedCategories = String.join(" | ", categories);

    final String joinedModels;
    joinedModels = String.join(" ", models);

    return new Definition(joinedCategories, joinedModels);
  }

  private static List<String> names(String heading) {
    final List<String> result;
    result = new ArrayList<>();

    final Matcher matcher;
    matcher = DEFINITION.matcher(heading);

    while (matcher.find()) {
      final String raw;
      raw = matcher.group(1);

      final String name;
      name = collapse(raw);

      if (!name.isEmpty()) {
        result.add(name);
      }
    }

    return result;
  }

  private static List<Row> parse(String section) {
    final List<Row> rows;
    rows = new ArrayList<>();

    final List<String> current;
    current = new ArrayList<>();

    final Pattern combined;
    combined = Pattern.compile(
        HEADING.pattern() + "|<dl class=\"element\">(.*?)</dl>",
        Pattern.DOTALL
    );

    final Matcher matcher;
    matcher = combined.matcher(section);

    while (matcher.find()) {
      final String heading;
      heading = matcher.group(1);

      if (heading != null) {
        current.clear();

        final List<String> names;
        names = names(heading);

        current.addAll(names);

        continue;
      }

      final String body;
      body = matcher.group(2);

      if (body == null || current.isEmpty()) {
        continue;
      }

      final Definition definition;
      definition = definition(body);

      for (final String name : current) {
        rows.add(new Row(name, definition.categories(), definition.contentModel()));
      }
    }

    return rows;
  }

  private static String read(String[] args) throws Exception {
    if (args.length > 0) {
      final Path source;
      source = Path.of(args[0]);

      return Files.readString(source, StandardCharsets.UTF_8);
    }

    final HttpRequest request;
    request = HttpRequest.newBuilder()
        .uri(URI.create("https://html.spec.whatwg.org/"))
        .GET()
        .build();

    final HttpClient client;
    client = HttpClient.newBuilder()
        .followRedirects(HttpClient.Redirect.NORMAL)
        .build();

    final HttpResponse<String> response;
    response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

    return response.body();
  }

  private static String replacement(String entity) {
    switch (entity) {
      case "&amp;":
        return "&";

      case "&gt;":
        return ">";

      case "&lt;":
        return "<";

      case "&nbsp;":
        return " ";

      case "&quot;":
        return "\"";

      default:
        break;
    }

    if (entity.charAt(1) != '#') {
      return entity;
    }

    final String digits;
    final int radix;

    if (entity.charAt(2) == 'x' || entity.charAt(2) == 'X') {
      digits = entity.substring(3, entity.length() - 1);
      radix = 16;
    } else {
      digits = entity.substring(2, entity.length() - 1);
      radix = 10;
    }

    final int codePoint;
    codePoint = Integer.parseInt(digits, radix);

    return new String(Character.toChars(codePoint));
  }

  private static String section(String html) {
    final int start;
    start = html.indexOf("<h2 id=\"semantics\"");

    final int end;
    end = html.indexOf("<h2 id=\"microdata\"");

    if (start < 0 || end < 0 || end <= start) {
      throw new IllegalStateException("Could not locate section 4");
    }

    return html.substring(start, end);
  }

  private static int state(String dt) {
    if (dt.contains("#concept-element-categories")) {
      return 1;
    }

    if (dt.contains("#concept-element-content-model")) {
      return 2;
    }

    return 0;
  }

  private static String unescape(String raw) {
    final StringBuilder out;
    out = new StringBuilder();

    final Matcher matcher;
    matcher = ENTITY.matcher(raw);

    while (matcher.find()) {
      final String entity;
      entity = matcher.group();

      final String value;
      value = replacement(entity);

      final String quoted;
      quoted = Matcher.quoteReplacement(value);

      matcher.appendReplacement(out, quoted);
    }

    matcher.appendTail(out);

    return out.toString();
  }

  private static void write(Path target, List<Row> rows) throws IOException {
    final List<String> lines;
    lines = new ArrayList<>();

    lines.add("# element\tcategories\tcontent model");

    for (final Row row : rows) {
      final String line;
      line = row.name() + "\t" + row.categories() + "\t" + row.contentModel();

      lines.add(line);
    }

    final Path directory;
    directory = target.getParent();

    Files.createDirectories(directory);

    Files.write(
        target, lines, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
    );
  }

  private record Definition(String categories, String contentModel) {}

  private record Row(String name, String categories, String contentModel) {}

}
