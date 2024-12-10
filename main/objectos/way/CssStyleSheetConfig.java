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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedMap;
import java.util.Set;

final class CssStyleSheetConfig implements Css.StyleSheet.Config, CssGeneratorScanner.Adapter {

  record Notes(
      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound
  ) {

    static Notes get() {
      Class<?> s;
      s = Css.Generator.class;

      return new Notes(
          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO)
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

    // TODO validate theme

    // let's apply the spec
    spec();

    // let's scan all of the classes
    scan();
  }

  // ##################################################################
  // # END: Execution
  // ##################################################################

  // ##################################################################
  // # BEGIN: Theme parsing
  // ##################################################################

  private final Map<Css.Namespace, List<CssThemeEntry>> namespaces = new EnumMap<>(Css.Namespace.class);

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
    list = namespaces.computeIfAbsent(namespace, ns -> Util.createList());

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
  // # BEGIN: Spec
  // ##################################################################

  private static final Set<CssValueType> KEYWORD = EnumSet.of(
      CssValueType.KEYWORD
  );

  private static final CssValueFormatter IDENTITY = new CssValueFormatter() {
    @Override
    public final String format(String value, boolean negative) { return value; }
  };

  private final Map<String, Set<Css.Key>> prefixes = new HashMap<>();

  private final Map<Css.Key, CssResolver> resolvers = new EnumMap<>(Css.Key.class);

  private final Map<String, Css.StaticUtility> staticUtilities = new HashMap<>();

  private void spec() {
    // A

    specA();
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

    funcUtility(
        Css.Key.ALIGN_CONTENT,

        "align-content",

        KEYWORD
    );

    funcUtility(
        Css.Key.ALIGN_ITEMS,

        "align-items",

        KEYWORD
    );

    funcUtility(
        Css.Key.ALIGN_SELF,

        "align-self",

        KEYWORD
    );

    funcUtility(
        Css.Key.APPEARANCE,

        "appearance",

        KEYWORD
    );

    funcUtility(
        Css.Key.ASPECT_RATIO,

        "aspect-ratio",

        EnumSet.of(
            CssValueType.INTEGER,
            CssValueType.DECIMAL,
            CssValueType.RATIO,
            CssValueType.KEYWORD
        )
    );
  }

  private void funcUtility(
      Css.Key key,
      String propertyName,
      Set<CssValueType> supportedTypes) {

    prefix(key, propertyName);

    Map<String, String> values;
    values = Map.of();

    CssResolver resolver;
    resolver = new CssResolverOfProperties(key, values, IDENTITY, supportedTypes, propertyName, null);

    resolver(key, resolver);

  }

  private void prefix(Css.Key key, String prefix) {
    Set<Css.Key> set;
    set = prefixes.computeIfAbsent(prefix, s -> EnumSet.noneOf(Css.Key.class));

    set.add(key);
  }

  private void resolver(Css.Key key, CssResolver resolver) {
    CssResolver maybeExisting;
    maybeExisting = resolvers.put(key, resolver);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "Key " + key + " already mapped to " + maybeExisting
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
  // # BEGIN: Scanning
  // ##################################################################

  private final List<Css.ClassNameFormat> classNameFormats = Util.createList();

  private final List<Css.MediaQuery> mediaQueries = Util.createList();

  private final SequencedMap<String, Css.Rule> rules = Util.createSequencedMap();

  private String sourceName;

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
    String[] parts;
    parts = value.split("\\s+");

    for (String part : parts) {
      if (!part.isBlank()) {
        processToken(part);
      }
    }
  }

  private void processToken(String token) {
    Css.Rule existing;
    existing = rules.get(token);

    if (existing == null) {
      Css.Rule newRule;
      newRule = createRule(token);

      store(token, newRule);
    }

    else {
      consumeExisting(token, existing);
    }
  }

  private Css.Rule createRule(String className) {
    String component;
    component = getComponent(className);

    if (component != null) {
      return createComponent(className, component);
    } else {
      return createUtility(className);
    }
  }

  void consumeExisting(String className, Css.Rule existing) {
    throw new UnsupportedOperationException("Implement me");
  }

  private String getComponent(String className) {
    return null;
  }

  private Css.Rule createComponent(String className, String definition) {
    throw new UnsupportedOperationException("Implement me");
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

    // maybe it is the prefix with an empty value
    // e.g. border-x

    Set<Css.Key> candidates;
    candidates = prefixes.get(value);

    String suffix;
    suffix = "";

    if (candidates == null) {

      int fromIndex;
      fromIndex = value.length();

      while (candidates == null && fromIndex > 0) {
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

        candidates = prefixes.get(prefix);
      }

    }

    if (candidates == null) {
      noteSink.send(notes.keyNotFound, sourceName, className);

      return Css.Rule.NOOP;
    }

    for (Css.Key candidate : candidates) {
      CssResolver resolver;
      resolver = resolvers.get(candidate);

      String resolved;
      resolved = resolver.resolve(suffix);

      if (resolved != null) {
        return resolver.create(className, modifier, negative, resolved);
      }
    }

    CssValueType type;
    type = CssValueType.parse(suffix);

    for (Css.Key candidate : candidates) {
      CssResolver resolver;
      resolver = resolvers.get(candidate);

      String resolved;
      resolved = resolver.resolveWithType(type, suffix);

      if (resolved != null) {
        return resolver.create(className, modifier, negative, resolved);
      }
    }

    noteSink.send(notes.matchNotFound, sourceName, className, candidates);

    return Css.Rule.NOOP;
  }

  void store(String token, Css.Rule value) {
    rules.put(token, value);
  }

  // ##################################################################
  // # END: Scanning
  // ##################################################################

  // ##################################################################
  // # BEGIN: Test-only output
  // ##################################################################

  final List<CssThemeEntry> testParseTheme() {
    parse(theme);

    UtilList<CssThemeEntry> entries;
    entries = new UtilList<>();

    Collection<List<CssThemeEntry>> values;
    values = namespaces.values();

    for (List<CssThemeEntry> value : values) {
      entries.addAll(value);
    }

    entries.sort(Comparator.naturalOrder());

    return entries.toUnmodifiableList();
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

    topLevel.writeTo(out, indentation);

    return out.toString();
  }

  // ##################################################################
  // # END: Test-only output
  // ##################################################################

}