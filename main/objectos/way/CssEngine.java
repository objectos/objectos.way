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
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import objectos.way.Css.ClassNameFormat;
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

  private final StringBuilder css = new StringBuilder();

  private String base = Css.defaultBase();

  private Set<Class<?>> classesToScan;

  private Set<Path> directoriesToScan;

  private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  @SuppressWarnings("unused")
  private final Notes notes = Notes.get();

  private final Set<Css.Layer> skipLayer = EnumSet.noneOf(Css.Layer.class);

  private String theme;

  private final Map<String, Css.Variant> variants = new LinkedHashMap<>();

  public final void base(String value) {
    base = Objects.requireNonNull(value, "value == null");
  }

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

  public final void skipLayer(Css.Layer layer) {
    skipLayer.add(layer);
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
    // create the default variants
    defaultVariants();

    // parse the default (built-in) theme
    parseTheme(Css.defaultTheme());

    // if the user provided a theme, we parse it
    if (theme != null) {
      parseTheme(theme);
    }

    // validate configuration
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
  // # BEGIN: Default Variants
  // ##################################################################

  private void defaultVariants() {
    variant("dark", new Css.AtRuleVariant("@media (prefers-color-scheme: dark)"));

    variant("focus", new Css.ClassNameFormat("", ":focus"));
    variant("hover", new Css.ClassNameFormat("", ":hover"));
    variant("active", new Css.ClassNameFormat("", ":active"));
    variant("visited", new Css.ClassNameFormat("", ":visited"));

    variant("after", new Css.ClassNameFormat("", "::after"));
    variant("before", new Css.ClassNameFormat("", "::before"));

    variant("*", new Css.ClassNameFormat("", " > *"));
    variant("**", new Css.ClassNameFormat("", " *"));
  }

  private void variant(String name, Css.Variant variant) {
    Css.Variant maybeExisting;
    maybeExisting = variants.put(name, variant);

    if (maybeExisting == null) {
      return;
    }

    // TODO restore existing and log?
  }

  // ##################################################################
  // # END: Default Variants
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse
  // ##################################################################

  private final StringBuilder sb = new StringBuilder();

  private final Map<Css.Namespace, List<CssThemeEntry>> themeEntries = new EnumMap<>(Css.Namespace.class);

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

  private void parseError(String text, int idx, String message) {
    throw new IllegalArgumentException(message);
  }

  private void parseTheme(String text) {
    enum Parser {

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
    parser = Parser.NORMAL;

    int startIndex;
    startIndex = 0;

    int auxIndex;
    auxIndex = 0;

    Css.Namespace namespace;
    namespace = null;

    String name = null, id = null;

    for (int idx = 0, len = text.length(); idx < len; idx++) {
      char c;
      c = text.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;
          }

          else if (c == '-') {
            parser = Parser.HYPHEN1;

            startIndex = idx;
          }

          else {
            parseError(text, idx, "Expected start of --variable declaration");
          }
        }

        case HYPHEN1 -> {
          if (c == '-') {
            parser = Parser.NAMESPACE_1;
          }

          else {
            parseError(text, idx, "Expected start of --variable declaration");
          }
        }

        case NAMESPACE_1 -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.NAMESPACE_N;

            auxIndex = idx;
          }

          else {
            parseError(text, idx, "--variable name must start with a letter");
          }
        }

        case NAMESPACE_N -> {
          if (c == '-') {
            parser = Parser.ID_1;

            String maybeName;
            maybeName = text.substring(auxIndex, idx);

            namespace = namespacePrefixes.get(maybeName);

            if (namespace == null) {
              parseError(text, idx, "Invalid namespace name=" + maybeName);
            }
          }

          else if (c == ':') {
            parser = Parser.OPTIONAL_WS;

            namespace = Css.Namespace.CUSTOM;

            name = text.substring(startIndex, idx);

            id = null;
          }

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.NAMESPACE_N;
          }

          else {
            parseError(text, idx, "CSS variable name with invalid character=" + c);
          }
        }

        case ID_1 -> {
          if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.ID_N;

            auxIndex = idx;
          }

          else {
            parseError(text, idx, "CSS variable name with invalid character=" + c);
          }
        }

        case ID_N -> {
          if (c == ':') {
            parser = Parser.OPTIONAL_WS;

            name = text.substring(startIndex, idx);

            id = text.substring(auxIndex, idx);
          }

          else if (c == '-') {
            parser = Parser.ID_N;
          }

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.ID_N;
          }

          else {
            parseError(text, idx, "CSS variable name with invalid character=" + c);
          }
        }

        case OPTIONAL_WS -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.OPTIONAL_WS;
          }

          else if (c == ';') {
            parseError(text, idx, "Empty variable definition");
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

    if (parser != Parser.NORMAL) {
      parseError(text, text.length(), "Unexpected end of theme");
    }
  }

  private int entryIndex;

  private void parseThemeAddVar(Css.Namespace namespace, String name, String id) {
    String value;
    value = sb.toString();

    List<CssThemeEntry> list;
    list = themeEntries.computeIfAbsent(namespace, ns -> Util.createList());

    CssThemeEntry entry;
    entry = new CssThemeEntry(entryIndex++, name, value, id);

    list.add(entry);
  }

  // ##################################################################
  // # END: Parse :: @theme {}
  // ##################################################################

  // ##################################################################
  // # END: Parse
  // ##################################################################

  // ##################################################################
  // # BEGIN: Theme validation
  // ##################################################################

  private final Set<String> colorKeywords = Util.createSet();

  private final Map<String, String> keywords = Util.createMap();

  private void validate() {
    for (Css.Namespace namespace : Css.Namespace.values()) {
      if (namespace == Css.Namespace.CUSTOM) {
        continue;
      }

      Consumer<String> keywordConsumer;
      keywordConsumer = k -> {};

      if (namespace == Css.Namespace.COLOR) {
        keywordConsumer = colorKeywords::add;
      }

      Function<String, String> keywordFunction;
      keywordFunction = Function.identity();

      if (namespace == Css.Namespace.BREAKPOINT) {
        keywordFunction = id -> "screen-" + id;
      }

      List<CssThemeEntry> namespaceEntries;
      namespaceEntries = themeEntries.getOrDefault(namespace, List.of());

      for (CssThemeEntry entry : namespaceEntries) {
        String id;
        id = entry.id();

        String keyword;
        keyword = keywordFunction.apply(id);

        keywordConsumer.accept(keyword);

        String mappingValue;
        mappingValue = "var(" + entry.name() + ")";

        String maybeExisting;
        maybeExisting = keywords.put(keyword, mappingValue);

        if (maybeExisting != null) {
          throw new IllegalArgumentException("Duplicate mapping for " + entry.name() + ": " + entry.value());
        }
      }
    }

    // generate breakpoint variants

    List<CssThemeEntry> breakpoints;
    breakpoints = themeEntries.getOrDefault(Css.Namespace.BREAKPOINT, List.of());

    for (CssThemeEntry entry : breakpoints) {
      String id;
      id = entry.id();

      Css.Variant variant;
      variant = new Css.AtRuleVariant("@media (min-width: " + entry.value() + ")");

      variant(id, variant);
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
          variant = variantByName(variantName);

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

  private Css.Variant variantByName(String name) {
    Css.Variant variant;
    variant = variants.get(name);

    if (variant != null) {
      return variant;
    }

    if (!HtmlElementName.hasName(name)) {
      return null;
    }

    ClassNameFormat descendant;
    descendant = new Css.ClassNameFormat("", " " + name);

    variants.put(name, descendant);

    return descendant;
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

  @Lang.VisibleForTesting
  final String formatValue(String value) {
    enum FormatValue {

      NORMAL,

      KEYWORD,

      COLOR_SLASH,

      COLOR_OPACITY,

      INTEGER,

      INTEGER_DOT,

      DECIMAL,

      NUMBER_R,

      RX,

      SPACE,

      UNKNOWN;

    }

    // index of the first char of the word
    int wordStart;
    wordStart = 0;

    FormatValue parser;
    parser = FormatValue.NORMAL;

    String colorName;
    colorName = null;

    int opacity;
    opacity = 0;

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

          else if (c == '/') {
            String word;
            word = value.substring(wordStart, idx);

            if (colorKeywords.contains(word)) {
              parser = FormatValue.COLOR_SLASH;

              colorName = word;

              opacity = 0;
            } else {
              parser = FormatValue.KEYWORD;
            }
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case COLOR_SLASH -> {
          if (c == '_') {
            parser = FormatValue.UNKNOWN;

            String word;
            word = value.substring(wordStart, idx);

            sb.append(word);
          }

          else if (Ascii.isDigit(c)) {
            parser = FormatValue.COLOR_OPACITY;

            int digit;
            digit = Ascii.digitToInt(c);

            opacity += digit;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case COLOR_OPACITY -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            formatResultColorOpacity(colorName, opacity);
          }

          else if (Ascii.isDigit(c)) {
            parser = FormatValue.COLOR_OPACITY;

            opacity *= 10;

            int digit;
            digit = Ascii.digitToInt(c);

            opacity += digit;
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

      case COLOR_SLASH -> formatResultDefault(value, wordStart);

      case COLOR_OPACITY -> {
        formatResultColorOpacity(colorName, opacity);

        yield sb.toString();
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

  private void formatResultColorOpacity(String colorName, int opacity) {
    sb.append("color-mix(in oklab, ");

    String colorValue;
    colorValue = keywords.get(colorName);

    sb.append(colorValue);

    sb.append(' ');

    sb.append(opacity);

    sb.append("%, transparent)");
  }

  private String formatResultKeyword(String keyword) {
    return keywords.getOrDefault(keyword, keyword);
  }

  private void formatResultRx(String value, int wordStart, int wordEnd) {
    int endIndex;
    endIndex = wordEnd - 2; // remove the rx unit

    String rx;
    rx = value.substring(wordStart, endIndex);

    sb.append("calc(");

    sb.append(rx);

    sb.append(" / var(--rx) * 1rem)");
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: CSS Generation
  // ##################################################################

  public final String generate() {
    if (!skipLayer.contains(Css.Layer.THEME)) {
      generateTheme();
    }

    if (!skipLayer.contains(Css.Layer.BASE)) {
      generateBase(base);
    }

    if (!skipLayer.contains(Css.Layer.UTILITIES)) {
      generateUtilities();
    }

    return css.toString();
  }

  private void generateTheme() {
    writeln("@layer theme {");

    indent(1);

    writeln(":root {");

    UtilList<CssThemeEntry> entries;
    entries = new UtilList<>();

    Collection<List<CssThemeEntry>> values;
    values = themeEntries.values();

    for (List<CssThemeEntry> value : values) {
      entries.addAll(value);
    }

    entries.sort(Comparator.naturalOrder());

    for (CssThemeEntry entry : entries) {
      indent(2);

      write(entry.name());
      write(": ");
      write(entry.value());
      writeln(';');
    }

    indent(1);

    writeln('}');

    writeln('}');
  }

  // ##################################################################
  // # BEGIN: Base layer
  // ##################################################################

  private void generateBase(String text) {
    enum Parser {
      NORMAL,

      SLASH,

      COMMENT,
      COMMENT_STAR,

      TEXT,

      UNKNOWN;
    }

    Parser parser;
    parser = Parser.NORMAL;

    writeln("@layer base {");

    boolean indent;
    indent = true;

    int level;
    level = 1;

    for (int idx = 0, len = text.length(); idx < len; idx++) {
      char c = text.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;
          }

          else if (c == '/') {
            parser = Parser.SLASH;
          }

          else if (c == '{') {
            parser = Parser.NORMAL;

            indent = true;

            level++;

            writeln(c);
          }

          else if (c == '}') {
            parser = Parser.NORMAL;

            indent = true;

            level--;

            indent(level);

            writeln(c);
          }

          else {
            parser = Parser.TEXT;

            if (indent) {
              indent(level);

              indent = false;
            }

            write(c);
          }
        }

        case SLASH -> {
          if (c == '*') {
            parser = Parser.COMMENT;
          }

          else {
            parser = Parser.UNKNOWN;

            write('/', c);
          }
        }

        case COMMENT -> {
          if (c == '*') {
            parser = Parser.COMMENT_STAR;
          }
        }

        case COMMENT_STAR -> {
          if (c == '*') {
            parser = Parser.COMMENT_STAR;
          }

          else if (c == '/') {
            parser = Parser.NORMAL;
          }

          else {
            parser = Parser.COMMENT;
          }
        }

        case TEXT -> {
          if (Ascii.isWhitespace(c)) {
            parser = Parser.NORMAL;

            write(' ');
          }

          else if (c == '{') {
            throw new UnsupportedOperationException("Implement me");
          }

          else if (c == ';') {
            parser = Parser.NORMAL;

            indent = true;

            writeln(c);
          }

          else {
            parser = Parser.TEXT;

            write(c);
          }
        }

        case UNKNOWN -> css.append(c);
      }
    }

    css.append("}\n");
  }

  // ##################################################################
  // # END: Base layer
  // ##################################################################

  private void generateUtilities() {
    CssGeneratorContextOf topLevel;
    topLevel = new CssGeneratorContextOf();

    for (Css.Rule rule : rules.values()) {
      rule.accept(topLevel);
    }

    Css.Indentation indentation;
    indentation = Css.Indentation.ROOT;

    writeln("@layer utilities {");

    indentation = indentation.increase();

    topLevel.writeTo(css, indentation);

    writeln('}');
  }

  // ##################################################################
  // # BEGIN: output writing section
  // ##################################################################

  private void indent(int level) {
    for (int i = 0, count = level * 2; i < count; i++) {
      css.append(' ');
    }
  }

  private void write(char c) {
    css.append(c);
  }

  private void write(char c1, char c2) {
    css.append(c1);
    css.append(c2);
  }

  private void write(String s) {
    css.append(s);
  }

  private void writeln(char c) {
    css.append(c);
    css.append('\n');
  }

  private void writeln(String s) {
    css.append(s);
    css.append('\n');
  }

  // ##################################################################
  // # END: output writing section
  // ##################################################################

  // ##################################################################
  // # END: CSS Generation
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
    parseTheme(theme);

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

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

}