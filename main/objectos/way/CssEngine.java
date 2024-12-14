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
package objectos.way;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.Set;
import objectos.way.Css.Namespace;

final class CssEngine implements Css.StyleSheet.Config, CssGeneratorScanner.Adapter {

  record Notes(
      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound,
      Note.Ref2<Css.Key, String> negativeNotSupported
  ) {

    static Notes get() {
      Class<?> s;
      s = Css.Generator.class;

      return new Notes(
          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO),
          Note.Ref2.create(s, "Does not allow negative values", Note.WARN)
      );
    }

  }

  // ##################################################################
  // # BEGIN: Configuration
  // ##################################################################

  private Set<Class<?>> classesToScan;

  private Set<Path> directoriesToScan;

  private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  @SuppressWarnings("unused")
  private final Notes notes = Notes.get();

  private String theme;

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void scanClass(Class<?> value) {
    Objects.requireNonNull(value, "value == null");

    if (classesToScan == null) {
      classesToScan = Util.createSet();
    }

    classesToScan.add(value);
  }

  @Override
  public final void scanDirectory(Path value) {
    Objects.requireNonNull(value, "value == null");

    if (directoriesToScan == null) {
      directoriesToScan = Util.createSet();
    }

    directoriesToScan.add(value);
  }

  @Override
  public final void theme(String value) {
    Check.state(theme == null, "Theme was already set");

    theme = Objects.requireNonNull(value, "value == null");
  }

  // ##################################################################
  // # END: Configuration
  // ##################################################################

  // ##################################################################
  // # BEGIN: Execution
  // ##################################################################

  public final void execute() {
    // first, we parse the default (built-in) theme
    parse(Css.defaultTheme());

    // if the user provided a theme, we parse it
    if (theme != null) {
      parse(theme);
    }

    // validate theme
    validate();

    // let's apply the spec
    spec();

    // we scan all of the classes
    scan();

    // and we process all of the distinct tokens
    process();
  }

  // ##################################################################
  // # END: Execution
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse
  // ##################################################################

  private final StringBuilder sb = new StringBuilder();

  private final Map<Css.Namespace, List<CssThemeEntry>> themeEntries = new EnumMap<>(Css.Namespace.class);

  private final Map<String, Css.Variant> variants = Util.createMap();

  private class ParseCtx {

    final String text;

    final int length;

    int idx = -1;

    ParseCtx(String text) {
      this.text = text;

      length = text.length();
    }

    final boolean hasNext() {
      idx++;

      return idx < length;
    }

    final boolean hasNext(int max) {
      idx++;

      return idx < max;
    }

    final char next() {
      return text.charAt(idx);
    }

    final String substring(int startIndex) {
      return text.substring(startIndex, idx);
    }

    final void error(String message) {
      throw new IllegalArgumentException(message);
    }

    final int jmp(int value) {
      int returnTo;
      returnTo = idx;

      idx = value - 1;

      return returnTo;
    }

    final void returnTo(int value) {
      idx = value;
    }

  }

  // ##################################################################
  // # BEGIN: Parse :: Top-Level
  // ##################################################################

  private void parse(String text) {
    enum Parser {

      START,
      AT,
      AT_RULE_NAME;

    }

    ParseCtx ctx;
    ctx = new ParseCtx(text);

    Parser parser;
    parser = Parser.START;

    int atIndex = 0;

    while (ctx.hasNext()) {
      char c;
      c = ctx.next();

      switch (parser) {
        case START -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.START;
          }

          else if (c == '@') {
            parser = Parser.AT;

            atIndex = ctx.idx;
          }

          else {
            ctx.error("Only @-rules are currently supported as top-level constructs");
          }
        }

        case AT -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.AT_RULE_NAME;
          }

          else {
            ctx.error("Invalid @-rule name");
          }
        }

        case AT_RULE_NAME -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.AT_RULE_NAME;
          }

          else if (Ascii.isWhitespace(c)) {
            parser = Parser.START;

            String ruleName;
            ruleName = ctx.substring(atIndex);

            switch (ruleName) {
              case "@theme" -> parseTheme(ctx);

              case "@variant" -> parseVariant(ctx);

              default -> ctx.error("Only @theme and @variant are currently supported as top-level constructs");
            }
          }

          else {
            ctx.error("Invalid @-rule name");
          }
        }
      }
    }
  }

  // ##################################################################
  // # END: Parse :: Top-Level
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse :: @theme {}
  // ##################################################################

  private final Map<String, Css.Namespace> namespacePrefixes = namespacePrefixes();

  private Map<String, Css.Namespace> namespacePrefixes() {
    Map<String, Namespace> map;
    map = Util.createMap();

    for (Css.Namespace namespace : Css.Namespace.values()) {
      String name;
      name = namespace.name();

      String prefix;
      prefix = name.toLowerCase(Locale.US);

      map.put(prefix, namespace);
    }

    return map;
  }

  private void parseTheme(ParseCtx ctx) {
    enum Parser {

      START,
      NORMAL,
      HYPHEN1,
      NAMESPACE_1,
      NAMESPACE_N,
      ID_1,
      ID_N,
      OPTIONAL_WS,
      VALUE,
      VALUE_TRIM;

    }

    Parser parser;
    parser = Parser.START;

    int startIndex = 0;

    int auxIndex = 0;

    Css.Namespace namespace = null;

    String name = null, id = null;

    loop: while (ctx.hasNext()) {
      char c;
      c = ctx.next();

      switch (parser) {
        case START -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.START;
          }

          else if (c == '{') {
            parser = Parser.NORMAL;
          }

          else {
            ctx.error("Expected @theme block start");
          }
        }

        case NORMAL -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;
          }

          else if (c == '-') {
            parser = Parser.HYPHEN1;

            startIndex = ctx.idx;
          }

          else if (c == '}') {
            break loop;
          }

          else {
            ctx.error("Expected start of --variable declaration");
          }
        }

        case HYPHEN1 -> {
          if (c == '-') {
            parser = Parser.NAMESPACE_1;
          }

          else {
            ctx.error("Expected start of --variable declaration");
          }
        }

        case NAMESPACE_1 -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.NAMESPACE_N;

            auxIndex = ctx.idx;
          }

          else {
            ctx.error("--variable name must start with a letter");
          }
        }

        case NAMESPACE_N -> {
          if (c == '-') {
            parser = Parser.ID_1;

            String maybeName;
            maybeName = ctx.substring(auxIndex);

            namespace = namespacePrefixes.get(maybeName);

            if (namespace == null) {
              ctx.error("Invalid namespace name=" + maybeName);
            }
          }

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.NAMESPACE_N;
          }

          else {
            ctx.error("CSS variable name with invalid character=" + c);
          }
        }

        case ID_1 -> {
          if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.ID_N;

            auxIndex = ctx.idx;
          }

          else {
            ctx.error("CSS variable name with invalid character=" + c);
          }
        }

        case ID_N -> {
          if (c == ':') {
            parser = Parser.OPTIONAL_WS;

            name = ctx.substring(startIndex);

            id = ctx.substring(auxIndex);
          }

          else if (c == '-') {
            parser = Parser.ID_N;
          }

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.ID_N;
          }

          else {
            ctx.error("CSS variable name with invalid character=" + c);
          }
        }

        case OPTIONAL_WS -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.OPTIONAL_WS;
          }

          else if (c == ';') {
            ctx.error("Empty variable definition");
          }

          else {
            parser = Parser.VALUE;

            sb.setLength(0);

            sb.append(c);
          }
        }

        case VALUE -> {
          if (c == ';') {
            parser = Parser.NORMAL;

            parseThemeAddVar(namespace, name, id);
          }

          else if (Ascii.isWhitespace(c)) {
            parser = Parser.VALUE_TRIM;
          }

          else {
            parser = Parser.VALUE;

            sb.append(c);
          }
        }

        case VALUE_TRIM -> {
          if (c == ';') {
            parser = Parser.NORMAL;

            parseThemeAddVar(namespace, name, id);
          }

          else if (Ascii.isWhitespace(c)) {
            parser = Parser.VALUE_TRIM;
          }

          else {
            parser = Parser.VALUE;

            sb.append(' ');

            sb.append(c);
          }
        }
      }
    }
  }

  private int entryIndex;

  private void parseThemeAddVar(Css.Namespace namespace, String name, String id) {
    String value;
    value = sb.toString();

    List<CssThemeEntry> list;
    list = themeEntries.computeIfAbsent(namespace, ns -> Util.createList());

    CssThemeEntryOfVariable entry;
    entry = new CssThemeEntryOfVariable(entryIndex++, name, value, id);

    list.add(entry);
  }

  // ##################################################################
  // # END: Parse :: @theme {}
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse :: @variant
  // ##################################################################

  private void parseVariant(ParseCtx ctx) {
    enum Parser {

      START,
      NAME,
      BLOCK_OR_PARENS,
      PARENS_START,
      PARENS_DEF,
      PARENS_END;

    }

    Parser parser;
    parser = Parser.START;

    int start = 0, parens = 0;

    String name = null;

    loop: while (ctx.hasNext()) {
      char c;
      c = ctx.next();

      switch (parser) {
        case START -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.START;
          }

          else if (Ascii.isLetter(c)) {
            parser = Parser.NAME;

            start = ctx.idx;
          }

          else {
            ctx.error("Variant name must start with a letter");
          }
        }

        case NAME -> {
          if (Ascii.isLetterOrDigit(c) || c == '-') {
            parser = Parser.NAME;
          }

          else if (Ascii.isWhitespace(c)) {
            parser = Parser.BLOCK_OR_PARENS;

            name = ctx.substring(start);
          }

          else {
            ctx.error("Variant name contains invalid character");
          }
        }

        case BLOCK_OR_PARENS -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.BLOCK_OR_PARENS;
          }

          else if (c == '(') {
            parser = Parser.PARENS_START;

            parens = 1;
          }

          else {
            ctx.error("Invalid variant definition");
          }
        }

        case PARENS_START -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.PARENS_START;
          }

          else if (c == ')' || c == ';') {
            ctx.error("Empty variant definition");
          }

          else {
            parser = Parser.PARENS_DEF;

            start = ctx.idx;
          }
        }

        case PARENS_DEF -> {
          if (c == '(') {
            parens++;
          }

          else if (c == ')') {
            parens--;

            if (parens == 0) {
              parser = Parser.PARENS_END;
            } else {
              parser = Parser.PARENS_DEF;
            }
          }

          else if (c == ';') {
            ctx.error("Invalid variant definition: unclosed parenthesis found");
          }

          else {
            parser = Parser.PARENS_DEF;
          }
        }

        case PARENS_END -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.PARENS_END;
          }

          else if (c == ';') {
            int returnTo;
            returnTo = ctx.jmp(start);

            parseVariantParens(ctx, returnTo, name);

            ctx.returnTo(returnTo);

            break loop;
          }

          else {
            ctx.error("Invalid variant definition: expected the ';' character");
          }
        }
      }
    }
  }

  private void parseVariantParens(ParseCtx ctx, int returnTo, String name) {
    enum Parser {
      START,

      PLACEHOLDER;
    }

    Parser parser;
    parser = Parser.START;

    while (ctx.hasNext(returnTo)) {
      char c;
      c = ctx.next();

      switch (parser) {
        case START -> {
          if (c == '&') {
            parser = Parser.PLACEHOLDER;
          }

          else if (c == '@') {
            throw new UnsupportedOperationException("Implement me :: @media like");
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case PLACEHOLDER -> {
          if (c == '&') {
            ctx.error("Variant must not have more than one placeholder");
          }
        }
      }
    }
  }

  // ##################################################################
  // # END: Parse :: @variant
  // ##################################################################

  // ##################################################################
  // # END: Parse
  // ##################################################################

  // ##################################################################
  // # BEGIN: Theme validation
  // ##################################################################

  private final Map<Css.Namespace, Map<String, String>> namespaces = new EnumMap<>(Css.Namespace.class);

  private void validate() {
    for (Css.Namespace namespace : Css.Namespace.values()) {
      List<CssThemeEntry> namespaceEntries;
      namespaceEntries = themeEntries.get(namespace);

      Map<String, String> mappings;

      if (namespaceEntries == null) {

        mappings = Map.of();

      } else {

        mappings = Util.createMap();

        for (CssThemeEntry entry : namespaceEntries) {
          entry.acceptMappings(mappings);
        }

      }

      namespaces.put(namespace, mappings);
    }
  }

  // ##################################################################
  // # END: Theme validation
  // ##################################################################

  // ##################################################################
  // # BEGIN: Spec
  // ##################################################################

  private final Map<String, Css.Key> prefixes = Util.createMap();

  private void spec() {
    for (Css.Key key : Css.Key.values()) {
      if (!key.engineCompatible) {
        continue;
      }

      String propertyName;
      propertyName = key.propertyName;

      Css.Key maybeExisting;
      maybeExisting = prefixes.put(propertyName, key);

      if (maybeExisting != null) {
        throw new IllegalArgumentException(
            "Prefix " + propertyName + " already mapped to " + maybeExisting
        );
      }
    }
  }

  // ##################################################################
  // # END: Spec
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan
  // ##################################################################

  private String sourceName;

  private final UtilMap<String, Set<String>> tokens = new UtilSequencedMap<>();

  private void scan() {
    CssGeneratorScanner scanner;
    scanner = new CssGeneratorScanner(noteSink);

    if (classesToScan != null) {
      for (Class<?> clazz : classesToScan) {
        scanner.scan(clazz, this);
      }
    }

    if (directoriesToScan != null) {
      for (Path directory : directoriesToScan) {
        scanner.scanDirectory(directory, this);
      }
    }
  }

  @Override
  public final void sourceName(String value) {
    sourceName = value;
  }

  @Override
  public final void processStringConstant(String value) {
    enum Splitter {
      NORMAL,

      WORD,

      TOKEN;
    }

    Splitter parser;
    parser = Splitter.NORMAL;

    for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;
          }

          else {
            parser = Splitter.WORD;

            sb.setLength(0);

            sb.append(c);
          }
        }

        case WORD -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;
          }

          else if (c == ':') {
            parser = Splitter.TOKEN;

            sb.append(c);
          }

          else {
            parser = Splitter.WORD;

            sb.append(c);
          }
        }

        case TOKEN -> {
          if (Character.isWhitespace(c)) {
            parser = Splitter.NORMAL;

            consumeToken();
          }

          else {
            parser = Splitter.TOKEN;

            sb.append(c);
          }
        }
      }
    }

    if (parser == Splitter.TOKEN) {
      consumeToken();
    }
  }

  private void consumeToken() {
    String token;
    token = sb.toString();

    Set<String> sources;
    sources = tokens.get(token);

    if (sources == null) {
      sources = Util.createSet();

      tokens.put(token, sources);
    }

    sources.add(sourceName);
  }

  // ##################################################################
  // # END: Scan
  // ##################################################################

  // ##################################################################
  // # BEGIN: Process
  // ##################################################################

  private final List<String> classNameSlugs = Util.createList();

  private final List<Css.ClassNameFormat> classNameFormats = Util.createList();

  private final List<Css.MediaQuery> mediaQueries = Util.createList();

  private final SequencedMap<String, Css.Rule> rules = Util.createSequencedMap();

  private void process() {
    outer: for (String token : tokens.keySet()) {
      String className;
      className = token;

      // split className on the ':' character
      classNameSlugs.clear();

      int beginIndex;
      beginIndex = 0;

      int colon;
      colon = className.indexOf(':');

      while (colon > 0) {
        String slug;
        slug = className.substring(beginIndex, colon);

        if (slug.isEmpty()) {
          // TODO log invalid className

          continue outer;
        }

        classNameSlugs.add(slug);

        beginIndex = colon + 1;

        colon = className.indexOf(':', beginIndex);
      }

      // last slug = propValue
      String propValue;
      propValue = className.substring(beginIndex);

      // process variants
      classNameFormats.clear();

      mediaQueries.clear();

      int parts;
      parts = classNameSlugs.size();

      if (parts > 1) {

        for (int idx = 0, max = parts - 1; idx < max; idx++) {
          String variantName;
          variantName = classNameSlugs.get(idx);

          Css.Variant variant;
          variant = variants.get(variantName);

          if (variant == null) {
            // TODO log unknown variant name

            continue outer;
          }

          switch (variant) {
            case Css.ClassNameFormat format -> classNameFormats.add(format);

            case Css.MediaQuery query -> mediaQueries.add(query);
          }
        }

      }

      String propName;
      propName = classNameSlugs.get(parts - 1);

      Css.Key key;
      key = prefixes.get(propName);

      if (key == null) {
        // TODO log unknown property name

        continue outer;
      }

      String formatted;
      formatted = formatValue(propValue);

      Css.Modifier modifier;
      modifier = createModifier();

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add(propName, formatted);

      Css.Rule rule;
      rule = new CssUtility(key, className, modifier, properties);

      rules.put(token, rule);
    }
  }

  private Css.Modifier createModifier() {
    if (mediaQueries.isEmpty() && classNameFormats.isEmpty()) {
      return Css.EMPTY_MODIFIER;
    } else {
      return new Css.Modifier(
          Util.toUnmodifiableList(mediaQueries),
          Util.toUnmodifiableList(classNameFormats)
      );
    }
  }

  private enum FormatValue {

    NORMAL,

    KEYWORD,

    INTEGER,

    INTEGER_DOT,

    DECIMAL,

    NUMBER_R,

    RX,

    SPACE,

    UNKNOWN;

  }

  @Lang.VisibleForTesting
  final String formatValue(String value) {
    // index of the first char of the word
    int wordStart;
    wordStart = 0;

    FormatValue parser;
    parser = FormatValue.NORMAL;

    sb.setLength(0);

    for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (c == '_') {
            parser = FormatValue.NORMAL;
          }

          else if (Ascii.isLetter(c)) {
            parser = FormatValue.KEYWORD;

            wordStart = idx;
          }

          else if (Ascii.isDigit(c)) {
            parser = FormatValue.INTEGER;

            wordStart = idx;
          }

          else {
            parser = FormatValue.UNKNOWN;

            wordStart = idx;
          }
        }

        case KEYWORD -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            String word;
            word = value.substring(wordStart, idx);

            String formatted;
            formatted = formatResultKeyword(word);

            sb.append(formatted);
          }

          else if (Ascii.isLetterOrDigit(c) || c == '-') {
            parser = FormatValue.KEYWORD;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case INTEGER -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            String word;
            word = value.substring(wordStart, idx);

            sb.append(word);
          }

          else if (c == 'r') {
            parser = FormatValue.NUMBER_R;
          }

          else if (Ascii.isDigit(c)) {
            parser = FormatValue.INTEGER;
          }

          else if (c == '.') {
            parser = FormatValue.DECIMAL;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case INTEGER_DOT -> {
          if (Ascii.isDigit(c)) {
            parser = FormatValue.DECIMAL;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case DECIMAL -> {
          if (Ascii.isDigit(c)) {
            parser = FormatValue.DECIMAL;
          }

          else if (c == 'r') {
            parser = FormatValue.NUMBER_R;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case NUMBER_R -> {
          if (c == 'x') {
            parser = FormatValue.RX;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case RX -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            formatResultRx(value, wordStart, idx);
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case SPACE -> {
          if (c == '_') {
            parser = FormatValue.SPACE;
          }

          else {
            parser = FormatValue.NORMAL;

            idx--;

            sb.append(' ');
          }
        }

        case UNKNOWN -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            String word;
            word = value.substring(wordStart, idx);

            sb.append(word);
          }
        }
      }
    }

    return switch (parser) {
      case NORMAL -> null; // empty value is invalid

      case KEYWORD -> {
        String keyword;
        keyword = trailer(value, wordStart);

        String formatted;
        formatted = formatResultKeyword(keyword);

        yield formatResult(formatted);
      }

      case INTEGER -> formatResultDefault(value, wordStart);

      case INTEGER_DOT -> value;

      case DECIMAL -> formatResultDefault(value, wordStart);

      case NUMBER_R -> formatResultDefault(value, wordStart);

      case RX -> {
        formatResultRx(value, wordStart, value.length());

        yield sb.toString();
      }

      case SPACE -> formatResultDefault(value, wordStart);

      case UNKNOWN -> formatResultDefault(value, wordStart);
    };

  }

  private String trailer(String value, int wordStart) {
    return wordStart == 0 ? value : value.substring(wordStart);
  }

  private String formatResult(String trailer) {
    if (sb.isEmpty()) {
      return trailer;
    } else {
      sb.append(trailer);

      return sb.toString();
    }
  }

  private String formatResultDefault(String value, int wordStart) {
    String trailer;
    trailer = trailer(value, wordStart);

    return formatResult(trailer);
  }

  private String formatResultKeyword(String keyword) {
    Map<String, String> colors;
    colors = namespaces.get(Css.Namespace.COLOR);

    return colors.getOrDefault(keyword, keyword);
  }

  private void formatResultRx(String value, int wordStart, int wordEnd) {
    int endIndex = wordEnd - 2; // remove the rx unit

    String rx;
    rx = value.substring(wordStart, endIndex);

    sb.append("calc(");

    sb.append(rx);

    sb.append("px / var(--rx) * 1rem)");
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test-only section
  // ##################################################################

  final Set<String> testProcess() {
    Set<String> keys;
    keys = tokens.keySet();

    Set<String> copy;
    copy = Set.copyOf(keys);

    tokens.clear();

    return copy;
  }

  final List<CssThemeEntry> testThemeEntries() {
    parse(theme);

    UtilList<CssThemeEntry> entries;
    entries = new UtilList<>();

    Collection<List<CssThemeEntry>> values;
    values = themeEntries.values();

    for (List<CssThemeEntry> value : values) {
      entries.addAll(value);
    }

    entries.sort(Comparator.naturalOrder());

    return entries.toUnmodifiableList();
  }

  final List<Css.Variant> testThemeVariants() {
    Collection<Css.Variant> values;
    values = variants.values();

    return List.copyOf(values);
  }

  final String testUtilities() {
    CssGeneratorContextOf topLevel;
    topLevel = new CssGeneratorContextOf();

    for (Css.Rule rule : rules.values()) {
      rule.accept(topLevel);
    }

    StringBuilder out;
    out = new StringBuilder();

    Css.Indentation indentation;
    indentation = Css.Indentation.ROOT;

    out.append("@layer utilities {");
    out.append(System.lineSeparator());

    indentation = indentation.increase();

    topLevel.writeTo(out, indentation);

    out.append("}");
    out.append(System.lineSeparator());

    return out.toString();
  }

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

}