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

  @SuppressWarnings("unused")
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
          if (Ascii.isLetter(c)) {
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

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.VARNSN;
          }

          else {
            parseError(line, col, "CSS variable name with invalid character=" + c);
          }
        }

        case VARID1 -> {
          if (Ascii.isLetterOrDigit(c)) {
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

          else if (Ascii.isLetterOrDigit(c)) {
            parser = Parser.VARIDN;
          }

          else {
            parseError(line, col, "CSS variable name with invalid character=" + c);
          }
        }

        case VAROWS -> {
          if (Ascii.isWhitespace(c)) {
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

          else if (Ascii.isWhitespace(c)) {
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

          else if (Ascii.isWhitespace(c)) {
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

    funcUtility(Css.Key.BORDER, "border");

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
      String className;
      className = token;

      String suffix;
      suffix = processVariants(className);

      if (suffix == null) {
        continue;
      }

      Css.Rule maybeStatic;
      maybeStatic = processStatic(className, suffix);

      if (maybeStatic != null) {
        rules.put(className, maybeStatic);

        continue;
      }

      Css.Rule rule;
      rule = processFunc(className, suffix);

      rules.put(token, rule);
    }
  }

  @Lang.VisibleForTesting
  final String processVariants(String className) {
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
        return null;
      }

      switch (variant) {
        case Css.ClassNameFormat format -> classNameFormats.add(format);

        case Css.MediaQuery query -> mediaQueries.add(query);
      }

      beginIndex = colon + 1;

      colon = className.indexOf(':', beginIndex);
    }

    String value;
    value = className;

    if (beginIndex > 0) {
      value = className.substring(beginIndex);
    }

    return value;
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

  private Css.Rule processStatic(String className, String value) {
    Css.StaticUtility staticFactory;
    staticFactory = staticUtilities.get(value);

    if (staticFactory == null) {
      return null;
    }

    Css.Modifier modifier;
    modifier = createModifier();

    return staticFactory.create(className, modifier);
  }

  private Css.Rule processFunc(String className, String value) {
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
      // TODO note key not found

      return Css.Rule.NOOP;
    }

    if (negative && !spec.allowsNegative()) {
      // TODO note negative not supported

      return Css.Rule.NOOP;
    }

    String formatted;
    formatted = formatValue(negative, suffix);

    if (formatted == null) {
      // TODO note invalid value

      return Css.Rule.NOOP;
    }

    Css.Modifier modifier;
    modifier = createModifier();

    return spec.createRule(className, modifier, formatted);
  }

  private enum FormatValue {

    NORMAL,

    KEYWORD,

    INTEGER,

    INTEGER_DOT,

    DECIMAL,

    RATIO,

    NUMBER_P,

    PIXEL,

    SPACE,

    UNKNOWN;

  }

  @Lang.VisibleForTesting
  final String formatValue(boolean negative, String value) {
    // index of the first char of the word
    int wordStart = 0;

    // accumulate all of the digits here
    long digits = 0L;

    // let's keep track of now many digits before the '.' we have found
    int beforeDot = 0;

    // let's keep track of how many digits after the '.' we have found
    int afterDot = 0;

    FormatValue parser;
    parser = FormatValue.NORMAL;

    sb.setLength(0);

    outer: for (int idx = 0, len = value.length(); idx < len; idx++) {
      char c;
      c = value.charAt(idx);

      switch (parser) {
        case NORMAL -> {
          if (Ascii.isLetter(c)) {
            parser = FormatValue.KEYWORD;
          }

          else if (Ascii.isDigit(c)) {
            parser = FormatValue.INTEGER;

            digits = Ascii.digitToInt(c);

            beforeDot = 1;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case KEYWORD -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            String keyword;
            keyword = value.substring(wordStart, idx);

            String formatted;
            formatted = formatResultKeyword(keyword);

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
          if (Ascii.isDigit(c)) {
            parser = FormatValue.INTEGER;

            digits *= 10;

            digits += Ascii.digitToInt(c);

            beforeDot++;
          }

          else if (c == '.') {
            parser = FormatValue.DECIMAL;

            afterDot = 0;
          }

          else if (c == '/') {
            parser = FormatValue.RATIO;
          }

          else if (c == 'p') {
            parser = FormatValue.NUMBER_P;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case INTEGER_DOT -> {
          if (Ascii.isDigit(c)) {
            parser = FormatValue.DECIMAL;

            digits *= 10;

            digits += Ascii.digitToInt(c);

            afterDot++;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case DECIMAL -> {
          if (Ascii.isDigit(c)) {
            parser = FormatValue.DECIMAL;

            digits *= 10;

            digits += Ascii.digitToInt(c);

            afterDot++;
          }

          else if (c == '/') {
            parser = FormatValue.RATIO;
          }

          else if (c == 'p') {
            parser = FormatValue.NUMBER_P;
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case RATIO -> {
          if (Ascii.isDigit(c)) {
            parser = FormatValue.RATIO;
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case NUMBER_P -> {
          if (c == 'x') {
            parser = FormatValue.PIXEL;
          }

          else {
            throw new UnsupportedOperationException("Implement me");
          }
        }

        case PIXEL -> {
          if (c == '_') {
            parser = FormatValue.SPACE;

            String px;
            px = formatResultPixel(digits, beforeDot, afterDot);

            sb.append(px);
          }

          else {
            parser = FormatValue.UNKNOWN;
          }
        }

        case SPACE -> {
          if (c == '_') {
            parser = FormatValue.UNKNOWN;

            sb.append('_');
            sb.append('_');
          }

          else {
            parser = FormatValue.NORMAL;

            wordStart = idx;

            idx--;

            sb.append(' ');
          }
        }

        case UNKNOWN -> {
          break outer;
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

      case INTEGER -> value;

      case INTEGER_DOT -> value;

      case DECIMAL -> value;

      case RATIO -> value;

      case NUMBER_P -> value;

      case PIXEL -> {
        String px;
        px = formatResultPixel(digits, beforeDot, afterDot);

        yield formatResult(px);
      }

      case SPACE -> value;

      case UNKNOWN -> value;
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

  private String formatResultKeyword(String keyword) {
    Map<String, String> colors;
    colors = namespaces.get(Css.Namespace.COLOR);

    return colors.getOrDefault(keyword, keyword);
  }

  private String formatResultPixel(long digits, int beforeDot, int afterDot) {
    // don't convert 0px
    if (digits == 0) {
      return "0px";
    }

    int scale;
    scale = Math.max(afterDot, 5);

    double dividend;
    dividend = digits;

    if (scale > afterDot) {
      dividend *= Math.pow(10D, scale - afterDot);
    }

    // round up if necessary...
    dividend += 5;

    double multiplier;
    multiplier = Math.pow(10D, scale);

    double divisor;
    divisor = 16L * multiplier;

    double result;
    result = dividend / divisor;

    long unscaled;
    unscaled = (long) (result * multiplier);

    StringBuilder out;
    out = new StringBuilder();

    // print the decimal digits

    boolean append;
    append = false;

    for (int idx = 0; idx < scale; idx++) {
      int thisDigit;
      thisDigit = (int) (unscaled % 10L);

      unscaled /= 10L;

      if (thisDigit != 0) {
        append = true;
      }

      if (!append) {
        continue;
      }

      out.append(thisDigit);
    }

    if (append) {
      out.append('.');
    }

    // print the integer digits

    int nonZeroCount;
    nonZeroCount = 0;

    int zeroCount;
    zeroCount = 0;

    for (int idx = 0, max = beforeDot; idx < max; idx++) {
      int thisDigit;
      thisDigit = (int) (unscaled % 10L);

      if (thisDigit != 0) {
        nonZeroCount++;

        zeroCount = 0;
      } else {
        zeroCount++;
      }

      unscaled /= 10L;

      out.append(thisDigit);
    }

    if (zeroCount > 1 && nonZeroCount == 0) {
      int length;
      length = out.length();

      out.setLength(length - zeroCount + 1);
    }

    else if (zeroCount > 0 && nonZeroCount > 0) {
      int length;
      length = out.length();

      out.setLength(length - zeroCount);
    }

    out.reverse();

    out.append("rem");

    return out.toString();
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