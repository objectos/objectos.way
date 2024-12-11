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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.Set;
import objectos.way.Css.Modifier;

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

  private final Notes notes = Notes.get();

  private String theme;

  private final Map<String, Css.Variant> variants = Util.createMap();

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
  // # BEGIN: Theme parsing
  // ##################################################################

  private final Map<Css.Namespace, List<CssThemeEntry>> themeEntries = new EnumMap<>(Css.Namespace.class);

  private enum Parser {

    NORMAL,

    // variables

    HYPHEN1,
    VARNS1,
    VARNSN,
    VARID1,
    VARIDN,
    VAROWS,
    VARVALUE,
    VARTRIM;

  }

  private void parse(String text) {
    Parser parser;
    parser = Parser.NORMAL;

    int entryIndex = 0;

    int line = 1;

    int col = 0;

    int start = 0;

    int aux = 0;

    Css.Namespace namespace = null;

    String variableName = null;

    String variableId = null;

    StringBuilder sb = new StringBuilder();

    for (int idx = 0, len = text.length(); idx < len; idx++) {
      char c;
      c = text.charAt(idx);

      col++;

      switch (parser) {
        case NORMAL -> {
          if (c == '-') {
            parser = Parser.HYPHEN1;

            start = idx;
          }

          else if (Character.isWhitespace(c)) {
            parser = Parser.NORMAL;
          }

          else {
            parseError(line, col, "Expected start of CSS variable declaration");
          }
        }

        case HYPHEN1 -> {
          if (c == '-') {
            parser = Parser.VARNS1;
          }

          else {
            parseError(line, col, "Expected start of CSS variable declaration");
          }
        }

        case VARNS1 -> {
          if (LangChars.isAsciiLetter(c)) {
            parser = Parser.VARNSN;

            aux = idx;
          }

          else {
            parseError(line, col, "CSS variable name must start with a letter");
          }
        }

        case VARNSN -> {
          if (c == '-') {
            parser = Parser.VARID1;

            String sub;
            sub = text.substring(aux, idx);

            String maybeName;
            maybeName = sub.toUpperCase(Locale.US);

            try {
              namespace = Css.Namespace.valueOf(maybeName);
            } catch (IllegalArgumentException iae) {
              parseError(line, col, "Invalid namespace name=" + sub);
            }
          }

          else if (LangChars.isAsciiLetterOrDigit(c)) {
            parser = Parser.VARNSN;
          }

          else {
            parseError(line, col, "CSS variable name with invalid character=" + c);
          }
        }

        case VARID1 -> {
          if (LangChars.isAsciiLetterOrDigit(c)) {
            parser = Parser.VARIDN;

            aux = idx;
          }

          else {
            parseError(line, col, "CSS variable name with invalid character=" + c);
          }
        }

        case VARIDN -> {
          if (c == ':') {
            parser = Parser.VAROWS;

            variableName = text.substring(start, idx);

            variableId = text.substring(aux, idx);
          }

          else if (c == '-') {
            parser = Parser.VARIDN;
          }

          else if (LangChars.isAsciiLetterOrDigit(c)) {
            parser = Parser.VARIDN;
          }

          else {
            parseError(line, col, "CSS variable name with invalid character=" + c);
          }
        }

        case VAROWS -> {
          if (LangChars.isAsciiWhitespace(c)) {
            parser = Parser.VAROWS;

            if (c == '\n') {
              line++;

              col = 0;
            }
          }

          else if (c == ';') {
            parseError(line, col, "Empty variable definition");
          }

          else {
            parser = Parser.VARVALUE;

            sb.setLength(0);

            sb.append(c);
          }
        }

        case VARVALUE -> {
          if (c == ';') {
            parser = Parser.NORMAL;

            String value;
            value = sb.toString();

            CssThemeEntryOfVariable entry;
            entry = new CssThemeEntryOfVariable(entryIndex++, variableName, value, variableId);

            putThemeEntry(namespace, entry);
          }

          else if (LangChars.isAsciiWhitespace(c)) {
            parser = Parser.VARTRIM;

            if (c == '\n') {
              line++;

              col = 0;
            }
          }

          else {
            parser = Parser.VARVALUE;

            sb.append(c);
          }
        }

        case VARTRIM -> {
          if (c == ';') {
            parser = Parser.VARVALUE;

            idx--;
          }

          else if (LangChars.isAsciiWhitespace(c)) {
            parser = Parser.VARTRIM;

            if (c == '\n') {
              line++;

              col = 0;
            }
          }

          else {
            parser = Parser.VARVALUE;

            sb.append(' ');
            sb.append(c);
          }
        }
      }
    }
  }

  private void putThemeEntry(Css.Namespace namespace, CssThemeEntry entry) {
    List<CssThemeEntry> list;
    list = themeEntries.computeIfAbsent(namespace, ns -> Util.createList());

    list.add(entry);
  }

  private void parseError(int line, int col, String message) {
    String formatted;
    formatted = String.format("At %d:%d -> %s", line, col, message);

    throw new IllegalArgumentException(formatted);
  }

  // ##################################################################
  // # END: Theme parsing
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

  private sealed interface UtilitySpec {

    Css.Key key();

    boolean allowsNegative();

    Css.Rule createRule(String className, Modifier modifier, String formatted);

  }

  private record UtilitySpec1(Css.Key key, String propertyName) implements UtilitySpec {
    @Override
    public final boolean allowsNegative() {
      return false;
    }

    @Override
    public final Css.Rule createRule(String className, Modifier modifier, String formatted) {
      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add(propertyName, formatted);

      return new CssUtility(key, className, modifier, properties);
    }
  }

  private final Map<String, Css.StaticUtility> staticUtilities = new HashMap<>();

  private final Map<String, UtilitySpec> utilitySpecs = Util.createMap();

  private void spec() {
    specA();

    specB();
  }

  private void specA() {
    staticUtility(
        Css.Key.ACCESSIBILITY,

        """
        sr-only     | position: absolute
                    | width: 1px
                    | height: 1px
                    | padding: 0
                    | margin: -1px
                    | overflow: hidden
                    | clip: rect(0, 0, 0, 0)
                    | white-space: nowrap
                    | border-width: 0

        not-sr-only | position: static
                    | width: auto
                    | height: auto
                    | padding: 0
                    | margin: 0
                    | overflow: visible
                    | clip: auto
                    | white-space: normal
        """
    );

    funcUtility(Css.Key.ALIGN_CONTENT, "align-content");

    funcUtility(Css.Key.ALIGN_ITEMS, "align-items");

    funcUtility(Css.Key.ALIGN_SELF, "align-self");

    funcUtility(Css.Key.APPEARANCE, "appearance");

    funcUtility(Css.Key.ASPECT_RATIO, "aspect-ratio");
  }

  private void specB() {
    funcUtility(Css.Key.BACKGROUND_COLOR, "background-color");

    funcUtility(Css.Key.BORDER_COLLAPSE, "border-collapse");
  }

  private void funcUtility(Css.Key key, String propertyName) {
    UtilitySpec1 spec = new UtilitySpec1(key, propertyName);

    putUtilitySpec(propertyName, spec);
  }

  private void putUtilitySpec(String prefix, UtilitySpec spec) {
    UtilitySpec maybeExisting;
    maybeExisting = utilitySpecs.put(prefix, spec);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "Prefix " + prefix + " already mapped to " + maybeExisting
      );
    }
  }

  // visible for testing
  final void staticUtility(Css.Key key, String text) {
    Map<String, CssProperties> table;
    table = Css.parseTable(text);

    for (Map.Entry<String, CssProperties> entry : table.entrySet()) {
      String className;
      className = entry.getKey();

      CssProperties properties;
      properties = entry.getValue();

      Css.StaticUtility utility;
      utility = new Css.StaticUtility(key, properties);

      Css.StaticUtility maybeExisting;
      maybeExisting = staticUtilities.put(className, utility);

      if (maybeExisting != null) {
        throw new IllegalArgumentException(
            "Class name " + className + " already mapped to " + maybeExisting
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

  private final StringBuilder sb = new StringBuilder();

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

      WORD;
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

            consumeToken();
          }

          else {
            parser = Splitter.WORD;

            sb.append(c);
          }
        }
      }
    }

    if (parser == Splitter.WORD) {
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

  private final List<Css.ClassNameFormat> classNameFormats = Util.createList();

  private final List<Css.MediaQuery> mediaQueries = Util.createList();

  private final SequencedMap<String, Css.Rule> rules = Util.createSequencedMap();

  private void process() {
    for (String token : tokens.keySet()) {
      Css.Rule newRule;
      newRule = createUtility(token);

      rules.put(token, newRule);
    }
  }

  private Css.Rule createUtility(String className) {
    classNameFormats.clear();

    mediaQueries.clear();

    int beginIndex;
    beginIndex = 0;

    int colon;
    colon = className.indexOf(':', beginIndex);

    while (colon > 0) {
      String variantName;
      variantName = className.substring(beginIndex, colon);

      Css.Variant variant;
      variant = variants.get(variantName);

      if (variant == null) {
        return Css.Rule.NOOP;
      }

      switch (variant) {
        case Css.ClassNameFormat format -> classNameFormats.add(format);

        case Css.MediaQuery query -> mediaQueries.add(query);
      }

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    Css.Modifier modifier;

    if (mediaQueries.isEmpty() && classNameFormats.isEmpty()) {
      modifier = Css.EMPTY_MODIFIER;
    } else {
      modifier = new Css.Modifier(
          Util.toUnmodifiableList(mediaQueries),
          Util.toUnmodifiableList(classNameFormats)
      );
    }

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return createUtility(className, modifier, value);
  }

  private Css.Rule createUtility(String className, Css.Modifier modifier, String value) {
    // 1) static values search
    Css.StaticUtility staticFactory;
    staticFactory = staticUtilities.get(value);

    if (staticFactory != null) {
      return staticFactory.create(className, modifier);
    }

    // 2) by prefix search

    char firstChar;
    firstChar = value.charAt(0);

    // are we dealing with a negative value
    boolean negative;
    negative = false;

    if (firstChar == '-') {
      negative = true;

      value = value.substring(1);
    }

    // search for the CSS Key

    UtilitySpec spec;
    spec = null;

    String suffix;
    suffix = "";

    int fromIndex;
    fromIndex = value.length();

    while (spec == null && fromIndex > 0) {
      int lastDash;
      lastDash = value.lastIndexOf('-', fromIndex);

      if (lastDash == 0) {
        // value starts with a dash and has no other dash
        // => invalid value
        break;
      }

      fromIndex = lastDash - 1;

      String prefix;
      prefix = value;

      suffix = "";

      if (lastDash > 0) {
        prefix = value.substring(0, lastDash);

        suffix = value.substring(lastDash + 1);
      }

      spec = utilitySpecs.get(prefix);
    }

    if (spec == null) {
      noteSink.send(notes.keyNotFound, sourceName, className);

      return Css.Rule.NOOP;
    }

    return createUtility(spec, className, modifier, negative, suffix);
  }

  private Css.Rule createUtility(UtilitySpec spec, String className, Css.Modifier modifier, boolean negative, String value) {
    if (negative && !spec.allowsNegative()) {
      noteSink.send(notes.negativeNotSupported, spec.key(), className);

      return Css.Rule.NOOP;
    }

    String formatted;
    formatted = formatValue(negative, value);

    return spec.createRule(className, modifier, formatted);
  }

  private enum FormatValue {

    NORMAL,

    KEYWORD,

    INTEGER,

    RATIO,

    UNKNOWN;

  }

  @Lang.VisibleForTesting
  final String formatValue(boolean negative, String value) {
    FormatValue parser;
    parser = FormatValue.NORMAL;

    outer: for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (LangChars.isAsciiLetter(c)) {
            parser = FormatValue.KEYWORD;
          }

          else if (LangChars.isAsciiDigit(c)) {
            parser = FormatValue.INTEGER;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case KEYWORD -> {
          if (LangChars.isAsciiLetterOrDigit(c) || c == '-') {
            parser = FormatValue.KEYWORD;
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case INTEGER -> {
          if (LangChars.isAsciiDigit(c)) {
            parser = FormatValue.INTEGER;
          }

          else if (c == '/') {
            parser = FormatValue.RATIO;
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case RATIO -> {
          if (LangChars.isAsciiDigit(c)) {
            parser = FormatValue.RATIO;
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case UNKNOWN -> {
          break outer;
        }
      }
    }

    return switch (parser) {
      case NORMAL -> null; // empty value is invalid

      case KEYWORD -> formatValueKeyword(value);

      case INTEGER -> value;

      case RATIO -> value;

      case UNKNOWN -> value;
    };
  }

  private String formatValueKeyword(String keyword) {
    Map<String, String> colors;
    colors = namespaces.get(Css.Namespace.COLOR);

    return colors.getOrDefault(keyword, keyword);
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test-only section
  // ##################################################################

  final List<CssThemeEntry> testParseTheme() {
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

  final Set<String> testProcess() {
    Set<String> keys;
    keys = tokens.keySet();

    Set<String> copy;
    copy = Set.copyOf(keys);

    tokens.clear();

    return copy;
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