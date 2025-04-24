/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

final class CssEngine implements CssEngineScanner.Adapter {

  record Notes(
      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound,
      Note.Ref2<Css.Key, String> negativeNotSupported
  ) {

    static Notes get() {
      Class<?> s;
      s = CssEngine.class;

      return new Notes(
          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO),
          Note.Ref2.create(s, "Does not allow negative values", Note.WARN)
      );
    }

  }

  private final CssConfiguration config;

  @SuppressWarnings("unused")
  private final Notes notes = Notes.get();

  private final Map<String, CssVariant> variants = new LinkedHashMap<>();

  CssEngine(CssConfiguration config) {
    this.config = config;
  }

  static CssEngine create(Consumer<? super CssConfigurationBuilder> config) {
    final CssConfigurationBuilder builder;
    builder = new CssConfigurationBuilder();

    config.accept(builder);

    return new CssEngine(
        builder.build()
    );
  }

  static String generate(Consumer<? super CssConfigurationBuilder> config) {
    try {
      final CssEngine engine;
      engine = create(config);

      engine.execute();

      final StringBuilder out;
      out = new StringBuilder();

      engine.generate(out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  // ##################################################################
  // # BEGIN: Execution
  // ##################################################################

  public final void execute() {
    // create the default variants
    defaultVariants();

    // parse the default (built-in) theme
    parseTheme(Css.defaultTheme());

    // if the user provided a theme, we parse it
    parseTheme(config.theme());

    for (var entry : config.themeQueries()) {
      parseThemeQuery(entry);
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
    variant("dark", new CssVariant.OfAtRule("@media (prefers-color-scheme: dark)"));

    variant("active", new CssVariant.Suffix(":active"));
    variant("checked", new CssVariant.Suffix(":checked"));
    variant("disabled", new CssVariant.Suffix(":disabled"));
    variant("first-child", new CssVariant.Suffix(":first-child"));
    variant("focus", new CssVariant.Suffix(":focus"));
    variant("focus-visible", new CssVariant.Suffix(":focus-visible"));
    variant("hover", new CssVariant.Suffix(":hover"));
    variant("last-child", new CssVariant.Suffix(":last-child"));
    variant("visited", new CssVariant.Suffix(":visited"));

    variant("after", new CssVariant.Suffix("::after"));
    variant("before", new CssVariant.Suffix("::before"));
    variant("first-letter", new CssVariant.Suffix("::first-letter"));
    variant("first-line", new CssVariant.Suffix("::first-line"));

    variant("*", new CssVariant.Suffix(" > *"));
    variant("**", new CssVariant.Suffix(" *"));
  }

  private void variant(String name, CssVariant variant) {
    CssVariant maybeExisting;
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

  @Lang.VisibleForTesting
  enum Namespace {

    BREAKPOINT,

    COLOR,

    FONT,

    CUSTOM;

  }

  @Lang.VisibleForTesting
  record ThemeEntry(int index, String name, String value, String id) implements Comparable<ThemeEntry> {

    @Override
    public final int compareTo(ThemeEntry o) {
      return Integer.compare(index, o.index);
    }

    public final Object key() {
      return name;
    }

    @Override
    public final String toString() {
      StringBuilder out;
      out = new StringBuilder();

      writeTo(out);

      return out.toString();
    }

    public final void writeTo(StringBuilder out) {
      out.append(name);
      out.append(": ");
      out.append(value);
      out.append(';');
    }

    private boolean shouldClear() {
      return "*".equals(id) && "initial".equals(value);
    }

    private ThemeEntry withValue(ThemeEntry newValue) {
      return new ThemeEntry(index, name, newValue.value, id);
    }

  }

  private final StringBuilder sb = new StringBuilder();

  private final Map<Namespace, Map<String, ThemeEntry>> themeEntries = new EnumMap<>(Namespace.class);

  // ##################################################################
  // # BEGIN: Parse :: @theme {}
  // ##################################################################

  private final Map<String, Namespace> namespacePrefixes = namespacePrefixes();

  private Map<String, Namespace> namespacePrefixes() {
    Map<String, Namespace> map;
    map = Util.createMap();

    for (Namespace namespace : Namespace.values()) {
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
    if (text.isEmpty()) {
      return;
    }

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

    Namespace namespace;
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

            namespace = namespacePrefixes.getOrDefault(maybeName, Namespace.CUSTOM);
          }

          else if (c == ':') {
            parser = Parser.OPTIONAL_WS;

            namespace = Namespace.CUSTOM;

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
          if (Ascii.isLetterOrDigit(c) || c == '*') {
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

  private void parseThemeAddVar(Namespace namespace, String name, String id) {
    String value;
    value = sb.toString();

    Map<String, ThemeEntry> entries;
    entries = themeEntries.computeIfAbsent(namespace, ns -> new HashMap<>());

    ThemeEntry entry;
    entry = new ThemeEntry(entryIndex++, name, value, id);

    if (entry.shouldClear()) {
      entries.clear();

      return;
    }

    entries.merge(entry.name, entry, (oldValue, newValue) -> oldValue.withValue(newValue));
  }

  // ##################################################################
  // # END: Parse :: @theme {}
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse :: @theme w/ query {}
  // ##################################################################

  @Lang.VisibleForTesting
  record ThemeQueryEntry(String name, String value) {
    @Override
    public final String toString() {
      StringBuilder out;
      out = new StringBuilder();

      writeTo(out);

      return out.toString();
    }

    public final void writeTo(StringBuilder out) {
      out.append(name);
      out.append(": ");
      out.append(value);
      out.append(';');
    }
  }

  private Map<String, List<ThemeQueryEntry>> themeQueryEntries;

  private void parseThemeQuery(Map.Entry<String, String> entry) {
    String key;
    key = entry.getKey();

    String query;
    query = parseThemeQueryKey(key);

    String value;
    value = entry.getValue();

    parseThemeQueryValue(query, value);
  }

  private String parseThemeQueryKey(String key) {
    if ("@media (prefers-color-scheme: dark)".equals(key)) {
      return key;
    }

    throw new IllegalArgumentException("Only @media (prefers-color-scheme: dark) is currently supported");
  }

  private void parseThemeQueryValue(String query, String text) {
    enum Parser {

      NORMAL,
      HYPHEN1,
      NAME_1,
      NAME_N,
      OPTIONAL_WS,
      VALUE,
      VALUE_TRIM;

    }

    Parser parser;
    parser = Parser.NORMAL;

    int startIndex;
    startIndex = 0;

    String name = null;

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
            parser = Parser.NAME_1;
          }

          else {
            parseError(text, idx, "Expected start of --variable declaration");
          }
        }

        case NAME_1 -> {
          if (Ascii.isLetter(c)) {
            parser = Parser.NAME_N;
          }

          else {
            parseError(text, idx, "--variable name must start with a letter");
          }
        }

        case NAME_N -> {
          if (Ascii.isLetterOrDigit(c) || c == '-') {
            parser = Parser.NAME_N;
          }

          else if (c == ':') {
            parser = Parser.OPTIONAL_WS;

            name = text.substring(startIndex, idx);
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

            parseThemeQueryAdd(query, name);
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

            parseThemeQueryAdd(query, name);
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

  private void parseThemeQueryAdd(String query, String name) {
    String value;
    value = sb.toString();

    ThemeQueryEntry entry;
    entry = new ThemeQueryEntry(name, value);

    List<ThemeQueryEntry> list;

    if (themeQueryEntries == null) {
      themeQueryEntries = LinkedHashMap.newLinkedHashMap(2);

      list = Util.createList();

      themeQueryEntries.put(query, list);
    } else {
      list = themeQueryEntries.computeIfAbsent(query, k -> Util.createList());
    }

    list.add(entry);
  }

  // ##################################################################
  // # END: Parse :: @theme w/ query {}
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
    for (Map.Entry<Namespace, Map<String, ThemeEntry>> namespaceEntry : themeEntries.entrySet()) {
      Namespace namespace;
      namespace = namespaceEntry.getKey();

      if (namespace == Namespace.CUSTOM) {
        continue;
      }

      Consumer<String> keywordConsumer;
      keywordConsumer = k -> {};

      if (namespace == Namespace.COLOR) {
        keywordConsumer = colorKeywords::add;
      }

      Function<String, String> keywordFunction;
      keywordFunction = Function.identity();

      if (namespace == Namespace.BREAKPOINT) {
        keywordFunction = id -> "screen-" + id;
      }

      Map<String, ThemeEntry> namespaceEntries;
      namespaceEntries = namespaceEntry.getValue();

      for (ThemeEntry entry : namespaceEntries.values()) {
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

    Map<String, ThemeEntry> breakpoints;
    breakpoints = themeEntries.getOrDefault(Namespace.BREAKPOINT, Map.of());

    List<ThemeEntry> sorted;
    sorted = new ArrayList<>(breakpoints.values());

    sorted.sort(Comparator.naturalOrder());

    for (ThemeEntry entry : sorted) {
      String id;
      id = entry.id();

      CssVariant variant;
      variant = new CssVariant.OfAtRule("@media (min-width: " + entry.value() + ")");

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
    CssEngineScanner scanner;
    scanner = new CssEngineScanner(config.noteSink());

    for (Class<?> clazz : config.classesToScan()) {
      scanner.scan(clazz, this);
    }

    for (Path directory : config.directoriesToScan()) {
      scanner.scanDirectory(directory, this);
    }

    for (Class<?> clazz : config.jarFilesToScan()) {
      scanner.scanJarFile(clazz, this);
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

  private final SequencedMap<String, CssUtility> utilities = Util.createSequencedMap();

  private final List<CssVariant.OfAtRule> variantsOfAtRule = Util.createList();

  private final List<CssVariant.OfClassName> variantsOfClassName = Util.createList();

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

      while (colon >= 0) {
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
      variantsOfClassName.clear();

      variantsOfAtRule.clear();

      int parts;
      parts = classNameSlugs.size();

      if (parts > 1) {

        for (int idx = 0, max = parts - 1; idx < max; idx++) {
          String variantName;
          variantName = classNameSlugs.get(idx);

          CssVariant variant;
          variant = variantByName(variantName);

          if (variant == null) {
            // TODO log unknown variant name

            continue outer;
          }

          switch (variant) {
            case CssVariant.OfAtRule ofAtRule -> variantsOfAtRule.add(ofAtRule);

            case CssVariant.OfClassName ofClassName -> variantsOfClassName.add(ofClassName);
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

      CssModifier modifier;
      modifier = createModifier();

      CssProperties.Builder properties;
      properties = new CssProperties.Builder();

      properties.add(propName, formatted);

      CssUtility utility;
      utility = new CssUtility(key, className, modifier, properties);

      utilities.put(token, utility);
    }
  }

  private CssVariant variantByName(String name) {
    CssVariant result;
    result = variants.get(name);

    if (result != null) {
      return result;
    }

    result = variantByNameOfAttribute(name);

    if (result != null) {
      return result;
    }

    result = variantByNameOfElement(name);

    if (result != null) {
      return result;
    }

    result = variantByPseudoClass(name);

    if (result != null) {
      return result;
    }

    return variantByNameOfGroup(name);
  }

  private CssVariant variantByNameOfAttribute(String name) {
    final int length;
    length = name.length();

    if (length == 0) {
      return null;
    }

    final char first;
    first = name.charAt(0);

    if (first != '[') {
      return null;
    }

    final char last;
    last = name.charAt(length - 1);

    if (last != ']') {
      return null;
    }

    return new CssVariant.Suffix(name);
  }

  private CssVariant variantByNameOfElement(String name) {
    if (HtmlElementName.hasName(name)) {
      CssVariant descendant;
      descendant = new CssVariant.Suffix(" " + name);

      variants.put(name, descendant);

      return descendant;
    }

    return null;
  }

  private CssVariant variantByPseudoClass(String name) {
    final int openIndex;
    openIndex = name.indexOf('(');

    if (openIndex < 0) {
      return null;
    }

    final int closeIndex;
    closeIndex = name.lastIndexOf(')');

    if (closeIndex < 0) {
      return null;
    }

    final String maybe;
    maybe = name.substring(0, openIndex);

    final boolean validName;
    validName = switch (maybe) {
      case "nth-child" -> true;

      default -> false;
    };

    if (!validName) {
      return null;
    }

    return new CssVariant.Suffix(":" + name);
  }

  private CssVariant variantByNameOfGroup(String name) {
    final int dash;
    dash = name.indexOf('-');

    if (dash < 0) {
      return null;
    }

    String maybeGroup;
    maybeGroup = name.substring(0, dash);

    if (!maybeGroup.equals("group")) {
      return null;
    }

    int suffixIndex;
    suffixIndex = dash + 1;

    if (suffixIndex >= name.length()) {
      return null;
    }

    String suffix;
    suffix = name.substring(suffixIndex);

    CssVariant groupVariant;
    groupVariant = variants.get(suffix);

    if (groupVariant != null) {
      return variantByNameOfGroup(name, groupVariant);
    }

    groupVariant = variantByNameOfAttribute(suffix);

    if (groupVariant != null) {
      return variantByNameOfGroup(name, groupVariant);
    }

    return null;
  }

  private CssVariant variantByNameOfGroup(String name, CssVariant groupVariant) {
    CssVariant generatedGroupVariant;
    generatedGroupVariant = groupVariant.generateGroup();

    if (generatedGroupVariant == null) {
      return null;
    }

    variants.put(name, generatedGroupVariant);

    return generatedGroupVariant;
  }

  private CssModifier createModifier() {
    if (variantsOfAtRule.isEmpty() && variantsOfClassName.isEmpty()) {
      return CssModifier.EMPTY_MODIFIER;
    } else {
      return new CssModifier(
          Util.toUnmodifiableList(variantsOfAtRule),
          Util.toUnmodifiableList(variantsOfClassName)
      );
    }
  }

  @Lang.VisibleForTesting
  final String formatValue(String value) {
    sb.setLength(0);

    entryIndex = 0;

    int index = 0;

    final int length;
    length = value.length();

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (Ascii.isDigit(c)) {
        index = formatValueNumeric(value, index);
      }

      else if (Ascii.isLetter(c)) {
        index = formatValueKeyword(value, index);
      }

      else if (c == '-') {
        index = formatValueNegative(value, index);
      }

      else if (isWhitespace(c)) {
        index = formatValueWhitespace(value, index);
      }

      else {
        index++;
      }
    }

    if (sb.isEmpty()) {
      return value;
    }

    formatValueNormal(value, length);

    return sb.toString();
  }

  private int formatValueKeyword(String value, int index) {
    final int length;
    length = value.length();

    // where the (possibly) keyword begins
    final int beginIndex;
    beginIndex = index;

    // consume initial char
    index++;

    enum State {

      KEYWORD,

      SLASH,

      OPACITY,

      INVALID;

    }

    State state;
    state = State.KEYWORD;

    int slashIndex;
    slashIndex = length;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (isBoundary(c)) {
        break;
      }

      switch (state) {
        case KEYWORD -> {
          if (c == '/') {
            state = State.SLASH;

            slashIndex = index;
          } else {
            state = State.KEYWORD;
          }
        }

        case SLASH, OPACITY -> {
          if (Ascii.isDigit(c)) {
            state = State.OPACITY;
          } else {
            state = State.INVALID;
          }
        }

        case INVALID -> {
          state = State.INVALID;
        }
      }

      index++;
    }

    switch (state) {
      case KEYWORD -> {
        // extract keyword
        final String maybe;
        maybe = value.substring(beginIndex, index);

        // check for match
        final String keyword;
        keyword = keywords.get(maybe);

        if (keyword == null) {
          break;
        }

        formatValueNormal(value, beginIndex);

        sb.append(keyword);

        entryIndex = index;
      }

      case OPACITY -> {
        // extract keyword
        final String maybe;
        maybe = value.substring(beginIndex, slashIndex);

        // check for match
        final String keyword;
        keyword = keywords.get(maybe);

        if (keyword == null) {
          break;
        }

        formatValueNormal(value, beginIndex);

        final String opacity;
        opacity = value.substring(slashIndex + 1, index);

        sb.append("color-mix(in oklab, ");

        sb.append(keyword);

        sb.append(' ');

        sb.append(opacity);

        sb.append("%, transparent)");

        entryIndex = index;
      }

      case SLASH, INVALID -> {}
    }

    return index;
  }

  private void formatValueNormal(String value, int endIndex) {
    sb.append(value, entryIndex, endIndex);
  }

  private int formatValueNegative(String value, int index) {
    final int length;
    length = value.length();

    // where the number begins
    final int beginIndex;
    beginIndex = index;

    // consume '-'
    index++;

    if (index >= length) {
      // there're no more chars
      return index;
    }

    final char c;
    c = value.charAt(index);

    if (!Ascii.isDigit(c)) {
      return index;
    }

    return formatValueNumeric(value, beginIndex);
  }

  private enum FormatValueNumeric {

    INTEGER,

    DOT,

    DECIMAL;

  }

  private int formatValueNumeric(String value, int index) {
    final int length;
    length = value.length();

    // where the number begins
    final int beginIndex;
    beginIndex = index;

    // consume '-' or initial digit
    index++;

    // initial state
    FormatValueNumeric state;
    state = FormatValueNumeric.INTEGER;

    loop: while (index < length) {
      final char c;
      c = value.charAt(index);

      switch (state) {
        case INTEGER -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.INTEGER;
            index++;
          } else if (c == '.') {
            state = FormatValueNumeric.DOT;
            index++;
          } else {
            break loop;
          }
        }

        case DOT -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.DECIMAL;
            index++;
          } else {
            break loop;
          }
        }

        case DECIMAL -> {
          if (Ascii.isDigit(c)) {
            state = FormatValueNumeric.DECIMAL;
            index++;
          } else {
            break loop;
          }
        }
      }
    }

    // where the (maybe) unit begins
    final int unitIndex;
    unitIndex = index;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (isBoundary(c)) {
        break;
      }

      index++;
    }

    if (state == FormatValueNumeric.DOT) {
      // invalid state
      return index;
    }

    // maybe rx value?

    final int unitLength;
    unitLength = index - unitIndex;

    if (unitLength != 2) {
      return index;
    }

    final char maybeR;
    maybeR = value.charAt(unitIndex);

    if (maybeR != 'r') {
      return index;
    }

    final char maybeX;
    maybeX = value.charAt(unitIndex + 1);

    if (maybeX != 'x') {
      return index;
    }

    // handle rx value

    // 1) emit normal value (if necessary)
    formatValueNormal(value, beginIndex);

    // 2) extract numeric value
    String number;
    number = value.substring(beginIndex, unitIndex);

    // 3) emit value
    sb.append("calc(");

    sb.append(number);

    sb.append(" / var(--rx) * 1rem)");

    // 4) update normal index
    entryIndex = index;

    return index;
  }

  private int formatValueWhitespace(String value, int index) {
    final int length;
    length = value.length();

    final int beginIndex;
    beginIndex = index;

    index++;

    while (index < length) {
      final char c;
      c = value.charAt(index);

      if (!isWhitespace(c)) {
        break;
      }

      index++;
    }

    formatValueNormal(value, beginIndex);

    sb.append(' ');

    entryIndex = index;

    return index;
  }

  private boolean isBoundary(char c) {
    return isSeparator(c) || isWhitespace(c);
  }

  private boolean isSeparator(char c) {
    return switch (c) {
      case ',', '(', ')' -> true;

      default -> false;
    };
  }

  private boolean isWhitespace(char c) {
    return c == '_';
  }

  // ##################################################################
  // # END: Process
  // ##################################################################

  // ##################################################################
  // # BEGIN: CSS Generation
  // ##################################################################

  public final void generate(Appendable out) throws IOException {
    final CssWriter w;
    w = new CssWriter(out, sb);

    if (!config.contains(Css.Layer.THEME)) {
      generateTheme(w);
    }

    if (!config.contains(Css.Layer.BASE)) {
      generateBase(w, config.base());
    }

    if (!config.contains(Css.Layer.UTILITIES)) {
      generateUtilities(w);
    }
  }

  private void generateTheme(CssWriter w) throws IOException {
    w.writeln("@layer theme {");

    w.indent(1);

    w.writeln(":root {");

    UtilList<ThemeEntry> entries;
    entries = new UtilList<>();

    Collection<Map<String, ThemeEntry>> values;
    values = themeEntries.values();

    for (Map<String, ThemeEntry> value : values) {
      Collection<ThemeEntry> thisEntries;
      thisEntries = value.values();

      entries.addAll(thisEntries);
    }

    entries.sort(Comparator.naturalOrder());

    for (ThemeEntry entry : entries) {
      w.indent(2);

      w.write(entry.name());
      w.write(": ");
      w.write(entry.value());
      w.writeln(';');
    }

    w.indent(1);

    w.writeln('}');

    if (themeQueryEntries != null) {
      for (Map.Entry<String, List<ThemeQueryEntry>> queryEntry : themeQueryEntries.entrySet()) {
        w.indent(1);

        String query;
        query = queryEntry.getKey();

        w.write(query);
        w.writeln(" {");

        w.indent(2);

        w.writeln(":root {");

        for (ThemeQueryEntry entry : queryEntry.getValue()) {
          w.indent(3);

          w.write(entry.name());
          w.write(": ");
          w.write(entry.value());
          w.writeln(';');
        }

        w.indent(2);

        w.writeln('}');

        w.indent(1);

        w.writeln('}');
      }
    }

    w.writeln('}');
  }

  // ##################################################################
  // # BEGIN: Base layer
  // ##################################################################

  private void generateBase(CssWriter w, String text) throws IOException {
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

    w.writeln("@layer base {");

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

            w.writeln(c);
          }

          else if (c == '}') {
            parser = Parser.NORMAL;

            indent = true;

            level--;

            w.indent(level);

            w.writeln(c);
          }

          else {
            parser = Parser.TEXT;

            if (indent) {
              w.indent(level);

              indent = false;
            }

            w.write(c);
          }
        }

        case SLASH -> {
          if (c == '*') {
            parser = Parser.COMMENT;
          }

          else {
            parser = Parser.UNKNOWN;

            w.write('/', c);
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

            w.write(' ');
          }

          else if (c == '{') {
            throw new UnsupportedOperationException("Implement me");
          }

          else if (c == ';') {
            parser = Parser.NORMAL;

            indent = true;

            w.writeln(c);
          }

          else {
            parser = Parser.TEXT;

            w.write(c);
          }
        }

        case UNKNOWN -> w.write(c);
      }
    }

    w.writeln('}');
  }

  // ##################################################################
  // # END: Base layer
  // ##################################################################

  private void generateUtilities(CssWriter w) throws IOException {
    CssEngineContextOf topLevel;
    topLevel = new CssEngineContextOf();

    for (CssUtility utility : utilities.values()) {
      utility.accept(topLevel);
    }

    w.writeln("@layer utilities {");

    topLevel.writeTo(w, 1);

    w.writeln('}');
  }

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

  final List<ThemeEntry> testThemeEntries() {
    parseTheme(config.theme());

    UtilList<ThemeEntry> entries;
    entries = new UtilList<>();

    Collection<Map<String, ThemeEntry>> values;
    values = themeEntries.values();

    for (Map<String, ThemeEntry> value : values) {
      Collection<ThemeEntry> thisEntries;
      thisEntries = value.values();

      entries.addAll(thisEntries);
    }

    entries.sort(Comparator.naturalOrder());

    return entries.toUnmodifiableList();
  }

  final List<ThemeQueryEntry> testThemeQueryEntries(String query) {
    for (var entry : config.themeQueries()) {
      parseThemeQuery(entry);
    }

    return themeQueryEntries.get(query);
  }

  final List<CssVariant> testThemeVariants() {
    Collection<CssVariant> values;
    values = variants.values();

    return List.copyOf(values);
  }

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

}