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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Consumer;
import objectos.way.Css.ThemeQueryEntry;

final class CssEngine implements CssEngineScanner.Adapter {

  record Notes(
      Note.Ref1<String> classNotFound,
      Note.Ref2<String, IOException> classIoError,
      Note.Ref1<String> classLoaded,

      Note.Ref2<String, String> keyNotFound,
      Note.Ref3<String, String, Set<Css.Key>> matchNotFound,
      Note.Ref2<Css.Key, String> negativeNotSupported
  ) {

    static Notes get() {
      Class<?> s;
      s = CssEngine.class;

      return new Notes(
          Note.Ref1.create(s, "CNF", Note.WARN),
          Note.Ref2.create(s, "IOE", Note.WARN),
          Note.Ref1.create(s, "REA", Note.DEBUG),

          Note.Ref2.create(s, "Css.Key not found", Note.DEBUG),
          Note.Ref3.create(s, "Match not found", Note.INFO),
          Note.Ref2.create(s, "Does not allow negative values", Note.WARN)
      );
    }

  }

  static final byte $SCAN = 1;
  static final byte $SCAN_CLASS = 2;
  static final byte $SCAN_CLASS_NEXT = 3;
  static final byte $SCAN_DIRECTORY = 4;
  static final byte $SCAN_DIRECTORY_NEXT = 5;
  static final byte $SCAN_BYTES = 6;

  static final byte $OK = 10;
  static final byte $ERROR = 11;

  private static final Notes NOTES = Notes.get();

  @SuppressWarnings("unused")
  private Lang.ClassReader classReader;

  private Iterable<? extends Class<?>> classesToScan;

  private Note.Sink noteSink;

  private Object object0;

  private Object object1;

  private byte state;

  @SuppressWarnings("unused")
  private byte stateNext;

  public final void writeTo(Appendable out) throws IOException {
    state = $SCAN;

    while (state < $OK) {
      state = execute(state);
    }
  }

  // ##################################################################
  // # BEGIN: State Machine
  // ##################################################################

  private byte execute(byte state) {
    return switch (state) {
      case $SCAN -> executeScan();
      case $SCAN_CLASS -> executeScanClass();
      case $SCAN_CLASS_NEXT -> executeScanClassNext();
      case $SCAN_BYTES -> executeScanBytes();

      default -> throw new AssertionError("Unexpected state=" + state);
    };
  }

  // ##################################################################
  // # END: State Machine
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan
  // ##################################################################

  private byte executeScan() {
    object0 = classesToScan.iterator();

    return $SCAN_CLASS;
  }

  private byte executeScanClass() {
    final Iterator<?> iterator;
    iterator = (Iterator<?>) object0;

    if (iterator.hasNext()) {
      object1 = iterator.next();

      return $SCAN_CLASS_NEXT;
    } else {
      return $SCAN_DIRECTORY;
    }
  }

  private byte executeScanClassNext() {
    final Class<?> clazz;
    clazz = (Class<?>) object1;

    final String binaryName;
    binaryName = clazz.getName();

    // 0. load class file

    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    final ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    final InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      noteSink.send(NOTES.classNotFound, binaryName);

      return $SCAN_CLASS;
    }

    try (in) {
      final ByteArrayOutputStream out;
      out = new ByteArrayOutputStream();

      in.transferTo(out);

      object1 = out.toByteArray();

      noteSink.send(notes.classLoaded, binaryName);

      return $SCAN_BYTES;
    } catch (IOException e) {
      noteSink.send(notes.classIoError, binaryName, e);

      return $SCAN_CLASS;
    }
  }

  private byte executeScanBytes() {
    @SuppressWarnings("unused")
    final byte[] bytes;
    bytes = (byte[]) object1;

    throw new UnsupportedOperationException("Implement me");
  }

  // ##################################################################
  // # END: Scan
  // ##################################################################

  private final CssConfiguration config;

  private int entryIndex;

  @SuppressWarnings("unused")
  private final Notes notes = Notes.get();

  private final Map<String, Css.Key> prefixes = Util.createMap();

  private final StringBuilder sb = new StringBuilder();

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
  // # BEGIN: Spec
  // ##################################################################

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
  public final void accept(String value) {
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
    result = config.variant(name);

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

      config.variant(name, descendant);

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
    groupVariant = config.variant(suffix);

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

    config.variant(name, generatedGroupVariant);

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
        keyword = config.keyword(maybe);

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
        keyword = config.keyword(maybe);

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

    UtilList<Css.ThemeEntry> entries;
    entries = new UtilList<>();

    for (Map<String, Css.ThemeEntry> value : config.themeEntries()) {
      Collection<Css.ThemeEntry> thisEntries;
      thisEntries = value.values();

      entries.addAll(thisEntries);
    }

    entries.sort(Comparator.naturalOrder());

    for (Css.ThemeEntry entry : entries) {
      w.indent(2);

      w.write(entry.name());
      w.write(": ");
      w.write(entry.value());
      w.writeln(';');
    }

    w.indent(1);

    w.writeln('}');

    for (Map.Entry<String, List<ThemeQueryEntry>> queryEntry : config.themeQueryEntries()) {
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

  // ##################################################################
  // # END: Test-only section
  // ##################################################################

}