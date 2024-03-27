/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.css;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import objectos.css.Variant.MediaQuery;
import objectos.util.list.GrowableList;

final class WayStyleGenRound extends WayStyleGenParser {

  static class Context {

    private final MediaQuery query;

    private final GrowableList<Rule> rules = new GrowableList<>();

    private Map<MediaQuery, Context> mediaQueries;

    private Context(MediaQuery query) {
      this.query = query;
    }

    final void add(Rule rule) {
      rules.add(rule);
    }

    final Context contextOf(MediaQuery child) {
      if (mediaQueries == null) {
        mediaQueries = new TreeMap<>();
      }

      return mediaQueries.computeIfAbsent(child, Context::new);
    }

    final void writeTo(StringBuilder out, Indentation indentation) {
      query.writeMediaQueryStart(out, indentation);

      rules.sort(Comparator.naturalOrder());

      Indentation blockIndentation;
      blockIndentation = indentation.increase();

      for (Rule rule : rules) {
        rule.writeTo(out, blockIndentation);
      }

      if (mediaQueries != null) {
        for (Context child : mediaQueries.values()) {
          if (!out.isEmpty()) {
            out.append(System.lineSeparator());
          }

          child.writeTo(out, blockIndentation);
        }
      }

      indentation.writeTo(out);

      out.append('}');

      out.append(System.lineSeparator());
    }

  }

  private GrowableList<Rule> topLevel;

  private Map<MediaQuery, Context> mediaQueries;

  WayStyleGenRound(WayStyleGenConfig config) {
    super(config);
  }

  public final String generate() {
    topLevel = new GrowableList<>();

    for (Rule rule : rules.values()) {
      if (rule == Rule.NOOP) {
        continue;
      }

      rule.accept(this);
    }

    StringBuilder out;
    out = new StringBuilder();

    Indentation indentation;
    indentation = Indentation.ROOT;

    Map<String, String> rules;
    rules = config.rules();

    for (var entry : rules.entrySet()) {
      String selector;
      selector = entry.getKey();

      out.append(selector);

      out.append(" {");
      out.append(System.lineSeparator());

      Indentation one;
      one = indentation.increase();

      String body;
      body = entry.getValue();

      String indented;
      indented = one.indent(body);

      out.append(indented);

      out.append("}");
      out.append(System.lineSeparator());

      if (!topLevel.isEmpty()) {
        out.append(System.lineSeparator());
      }
    }

    topLevel.sort(Comparator.naturalOrder());

    for (Rule rule : topLevel) {
      rule.writeTo(out, indentation);
    }

    if (mediaQueries != null) {
      for (Context ctx : mediaQueries.values()) {
        if (!out.isEmpty()) {
          out.append(System.lineSeparator());
        }

        ctx.writeTo(out, indentation);
      }
    }

    return out.toString();
  }

  final Context contextOf(MediaQuery query) {
    if (mediaQueries == null) {
      mediaQueries = new TreeMap<>();
    }

    return mediaQueries.computeIfAbsent(query, Context::new);
  }

  final void topLevel(Rule rule) {
    topLevel.add(rule);
  }

}