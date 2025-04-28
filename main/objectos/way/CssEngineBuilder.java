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
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import objectos.way.Css.Key;
import objectos.way.Css.Layer;
import objectos.way.Css.ThemeQueryEntry;

final class CssEngineBuilder implements Css.StyleSheet.Options {

  private enum Namespace {

    BREAKPOINT,

    COLOR,

    FONT,

    CUSTOM;

  }

  private String base = Css.defaultBase();

  private int entryIndex;

  private final UtilMap<String, String> keywords = new UtilMap<>();

  private final Map<String, Namespace> namespacePrefixes = namespacePrefixes();

  private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

  private final StringBuilder sb = new StringBuilder();

  private Set<Class<?>> scanClasses = UtilUnmodifiableSet.of();

  private Set<Path> scanDirectories = UtilUnmodifiableSet.of();

  private Set<Class<?>> scanJars = UtilUnmodifiableSet.of();

  private Set<Css.Layer> skipLayers = UtilUnmodifiableSet.of();

  private final UtilMap<String, CssVariant> variants = new UtilMap<>();

  private boolean theme;

  private final Map<Namespace, Map<String, Css.ThemeEntry>> themeEntries = new EnumMap<>(Namespace.class);

  private Map<String, List<Css.ThemeQueryEntry>> themeQueryEntries;

  CssEngineBuilder() {
    parseTheme(Css.defaultTheme());

    defaultVariants();
  }

  CssEngineBuilder(boolean test) {
    /* noop */
  }

  // ##################################################################
  // # BEGIN: Public API
  // ##################################################################

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

    if (scanClasses.isEmpty()) {
      scanClasses = Util.createSet();
    }

    scanClasses.add(value);
  }

  @Override
  public final void scanDirectory(Path value) {
    Objects.requireNonNull(value, "value == null");

    if (scanDirectories.isEmpty()) {
      scanDirectories = Util.createSet();
    }

    scanDirectories.add(value);
  }

  @Override
  public final void scanJarFileOf(Class<?> value) {
    Objects.requireNonNull(value, "value == null");

    if (scanJars.isEmpty()) {
      scanJars = Util.createSet();
    }

    scanJars.add(value);
  }

  public final void skipLayer(Css.Layer value) {
    Objects.requireNonNull(value, "value == null");

    if (skipLayers.isEmpty()) {
      skipLayers = Util.createSet();
    }

    skipLayers.add(value);
  }

  @Override
  public final void theme(String value) {
    Check.state(!theme, "Theme was already set");

    parseTheme(value);

    theme = true;
  }

  @Override
  public final void theme(String query, String value) {
    Objects.requireNonNull(query, "query == null");
    Objects.requireNonNull(value, "value == null");

    final String key;
    key = parseThemeQueryKey(query);

    if (themeQueryEntries != null && themeQueryEntries.containsKey(key)) {
      throw new IllegalStateException("Theme was already set for " + key);
    }

    parseThemeQueryValue(query, value);
  }

  // ##################################################################
  // # END: Public API
  // ##################################################################

  // ##################################################################
  // # BEGIN: Default Variants
  // ##################################################################

  private void defaultVariants() {
    variant("dark", CssVariant.atRule("@media (prefers-color-scheme: dark)"));

    variant("active", CssVariant.suffix(":active"));
    variant("checked", CssVariant.suffix(":checked"));
    variant("disabled", CssVariant.suffix(":disabled"));
    variant("first-child", CssVariant.suffix(":first-child"));
    variant("focus", CssVariant.suffix(":focus"));
    variant("focus-visible", CssVariant.suffix(":focus-visible"));
    variant("hover", CssVariant.suffix(":hover"));
    variant("last-child", CssVariant.suffix(":last-child"));
    variant("visited", CssVariant.suffix(":visited"));

    variant("after", CssVariant.suffix("::after"));
    variant("before", CssVariant.suffix("::before"));
    variant("first-letter", CssVariant.suffix("::first-letter"));
    variant("first-line", CssVariant.suffix("::first-line"));

    variant("*", CssVariant.suffix(" > *"));
    variant("**", CssVariant.suffix(" *"));
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
  // # BEGIN: @theme
  // ##################################################################

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

  private void parseThemeAddVar(Namespace namespace, String name, String id) {
    String value;
    value = sb.toString();

    Map<String, Css.ThemeEntry> entries;
    entries = themeEntries.computeIfAbsent(namespace, ns -> new HashMap<>());

    Css.ThemeEntry entry;
    entry = new Css.ThemeEntry(entryIndex++, name, value, id);

    if (entry.shouldClear()) {
      entries.clear();

      return;
    }

    entries.merge(entry.name(), entry, (oldValue, newValue) -> oldValue.withValue(newValue));
  }

  private void validateTheme() {
    for (Map.Entry<Namespace, Map<String, Css.ThemeEntry>> namespaceEntry : themeEntries.entrySet()) {
      Namespace namespace;
      namespace = namespaceEntry.getKey();

      if (namespace == Namespace.CUSTOM) {
        continue;
      }

      Function<String, String> keywordFunction;
      keywordFunction = Function.identity();

      if (namespace == Namespace.BREAKPOINT) {
        keywordFunction = id -> "screen-" + id;
      }

      Map<String, Css.ThemeEntry> namespaceEntries;
      namespaceEntries = namespaceEntry.getValue();

      for (Css.ThemeEntry entry : namespaceEntries.values()) {
        String id;
        id = entry.id();

        String keyword;
        keyword = keywordFunction.apply(id);

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

    Map<String, Css.ThemeEntry> breakpoints;
    breakpoints = themeEntries.getOrDefault(Namespace.BREAKPOINT, Map.of());

    List<Css.ThemeEntry> sorted;
    sorted = new ArrayList<>(breakpoints.values());

    sorted.sort(Comparator.naturalOrder());

    for (Css.ThemeEntry entry : sorted) {
      String id;
      id = entry.id();

      CssVariant variant;
      variant = CssVariant.atRule("@media (min-width: " + entry.value() + ")");

      variant(id, variant);
    }
  }

  // ##################################################################
  // # END: @theme
  // ##################################################################

  // ##################################################################
  // # BEGIN: Parse :: @theme w/ query {}
  // ##################################################################

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

    Css.ThemeQueryEntry entry;
    entry = new Css.ThemeQueryEntry(name, value);

    List<Css.ThemeQueryEntry> list;

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
  // # BEGIN: Test-only section
  // ##################################################################

  final List<Css.ThemeEntry> testThemeEntries() {
    UtilList<Css.ThemeEntry> entries;
    entries = new UtilList<>();

    Collection<Map<String, Css.ThemeEntry>> values;
    values = themeEntries.values();

    for (Map<String, Css.ThemeEntry> value : values) {
      Collection<Css.ThemeEntry> thisEntries;
      thisEntries = value.values();

      entries.addAll(thisEntries);
    }

    entries.sort(Comparator.naturalOrder());

    return entries.toUnmodifiableList();
  }

  final List<ThemeQueryEntry> testThemeQueryEntries(String query) {
    return themeQueryEntries.get(query);
  }

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

  // ##################################################################
  // # BEGIN: Build
  // ##################################################################

  final CssEngine build() {
    validateTheme();

    return new CssEngine(this);
  }

  final String base() {
    return base;
  }

  final Lang.ClassReader classReader() {
    return Lang.createClassReader(noteSink);
  }

  final Map<String, String> keywords() {
    return keywords.toUnmodifiableMap();
  }

  final Note.Sink noteSink() {
    return noteSink;
  }

  final Map<String, Key> prefixes() {
    final UtilMap<String, Key> prefixes;
    prefixes = new UtilMap<>();

    for (Css.Key key : Css.Key.values()) {
      final String propertyName;
      propertyName = key.propertyName;

      final Css.Key maybeExisting;
      maybeExisting = prefixes.put(propertyName, key);

      if (maybeExisting != null) {
        throw new IllegalArgumentException(
            "Prefix " + propertyName + " already mapped to " + maybeExisting
        );
      }
    }

    return prefixes.toUnmodifiableMap();
  }

  final Iterable<? extends Class<?>> scanClasses() {
    return UtilUnmodifiableList.copyOf(scanClasses);
  }

  final Iterable<? extends Path> scanDirectories() {
    return UtilUnmodifiableList.copyOf(scanDirectories);
  }

  final Iterable<? extends Class<?>> scanJars() {
    return UtilUnmodifiableList.copyOf(scanJars);
  }

  final Set<? extends Layer> skipLayers() {
    return skipLayers;
  }

  final Iterable<? extends Css.ThemeEntry> themeEntries() {
    UtilList<Css.ThemeEntry> entries;
    entries = new UtilList<>();

    for (Map<String, Css.ThemeEntry> value : themeEntries.values()) {
      Collection<Css.ThemeEntry> thisEntries;
      thisEntries = value.values();

      entries.addAll(thisEntries);
    }

    entries.sort(Comparator.naturalOrder());

    return entries.toUnmodifiableList();
  }

  final Iterable<? extends Entry<String, List<ThemeQueryEntry>>> themeQueryEntries() {
    return themeQueryEntries != null ? UtilUnmodifiableList.copyOf(themeQueryEntries.entrySet()) : List.of();
  }

  final Map<String, CssVariant> variants() {
    return variants;
  }

  // ##################################################################
  // # END: Build
  // ##################################################################

}