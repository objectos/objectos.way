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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.classfile.Annotation;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import java.lang.classfile.constantpool.ConstantPool;
import java.lang.classfile.constantpool.PoolEntry;
import java.lang.classfile.constantpool.StringEntry;
import java.lang.classfile.constantpool.Utf8Entry;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class CssEngine2 implements Css.Engine {

  // ##################################################################
  // # BEGIN: Execute
  // ##################################################################

  private final Configuring configuring = new Configuring();

  public final void generate(Appendable out) throws IOException {
    final Config config;
    config = configuring.configure();

    final Note.Sink noteSink;
    noteSink = config.noteSink;

    final Map<String, Variant> variants;
    variants = config.variants;

    final Proc proc;
    proc = new Proc(noteSink, variants);

    final Tokenizer tokenizer;
    tokenizer = new Tokenizer(proc);

    final Scanner scanner;
    scanner = new Scanner(noteSink, tokenizer);

    final Set<Class<?>> scanClasses;
    scanClasses = config.scanClasses;

    if (!scanClasses.isEmpty()) {
      final Classes classes;
      classes = new Classes(scanner, scanClasses, noteSink);

      classes.scan();
    }

    final Set<Path> scanDirectories;
    scanDirectories = config.scanDirectories;

    if (!scanDirectories.isEmpty()) {
      final Dirs dirs;
      dirs = new Dirs(scanner, scanDirectories, noteSink);

      dirs.scan();
    }

    final Set<Class<?>> scanJars;
    scanJars = config.scanJars;

    if (!scanJars.isEmpty()) {
      final Jars jars;
      jars = new Jars(scanner, scanJars, noteSink);

      jars.scan();
    }

    final Map<String, ThemeProp> keywords;
    keywords = config.keywords;

    final Map<List<String>, List<Value>> themeValues;
    themeValues = config.themeValues;

    final List<Utility> utils;
    utils = proc.utilities;

    final Gen gen;
    gen = new Gen(keywords, themeValues, utils);

    final Ctx ctx;
    ctx = gen.generate();

    final List<ThemeSection> sections;
    sections = ctx.sections;

    final Theme theme;
    theme = new Theme(sections);

    theme.write(out);

    final String baseSource;
    baseSource = config.base;

    final Base base;
    base = new Base(baseSource);

    base.write(out);

    final List<Rule> rules;
    rules = ctx.rules;

    final Utilities utilities;
    utilities = new Utilities(rules);

    utilities.write(out);
  }

  // ##################################################################
  // # END: Execute
  // ##################################################################

  // ##################################################################
  // # BEGIN: Value
  // ##################################################################

  // ##################################################################
  // # BEGIN: Value: Types
  // ##################################################################

  sealed abstract static class Value implements Comparable<Value> {
    final int index;

    Value(int index) {
      this.index = index;
    }

    @Override
    public final int compareTo(Value o) {
      return Integer.compare(index, o.index);
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof Value that
          && index == that.index
          && equals0(that);
    }

    abstract boolean equals0(Value obj);

    String name() {
      throw new UnsupportedOperationException();
    }

    String value() {
      throw new UnsupportedOperationException();
    }

    boolean themeSection() { return false; }
  }

  static final class CustomProp extends Value {

    final String name;
    final String value;

    CustomProp(int index, String name, String value) {
      super(index);
      this.name = name;
      this.value = value;
    }

    @Override
    final boolean equals0(Value obj) {
      return obj instanceof CustomProp that
          && name.equals(that.name)
          && value.equals(that.value);
    }

    @Override
    final String name() { return name; }

    @Override
    final String value() { return value; }

    @Override
    final boolean themeSection() { return true; }

  }

  static final class SystemSkip extends Value {

    final String ns;

    SystemSkip(int index, String ns) {
      super(index);

      this.ns = ns;
    }

    @Override
    final boolean equals0(Value obj) {
      return obj instanceof SystemSkip that
          && ns.equals(that.ns);
    }

  }

  static final class ThemeProp extends Value {

    final String name;
    final String ns;
    final String id;
    final String value;

    boolean marked;

    ThemeProp(int index, String name, String ns, String id, String value) {
      super(index);
      this.name = name;
      this.ns = ns;
      this.id = id;
      this.value = value;
    }

    @Override
    final boolean equals0(Value obj) {
      return obj instanceof ThemeProp that
          && name.equals(that.name)
          && value.equals(that.value);
    }

    @Override
    final String name() { return name; }

    @Override
    final String value() { return value; }

    @Override
    final boolean themeSection() { return marked; }

  }

  static Value customProp(int index, String name, String value) {
    return new CustomProp(index, name, value);
  }

  static Value systemSkip(int index, String ns) {
    return new SystemSkip(index, ns);
  }

  static ThemeProp themeProp(int index, String ns, String id, String value) {
    if (id.isEmpty()) {
      return new ThemeProp(index, "--" + ns, ns, id, value);
    } else {
      return new ThemeProp(index, "--" + ns + "-" + id, ns, id, value);
    }
  }

  // ##################################################################
  // # END: Value: Types
  // ##################################################################

  // ##################################################################
  // # BEGIN: Value: Parser
  // ##################################################################

  private static final byte[] CSS;

  private static final byte CSS_WS = 1;
  private static final byte CSS_ASTERISK = 2;
  private static final byte CSS_HYPHEN = 3;
  private static final byte CSS_COLON = 4;
  private static final byte CSS_SEMICOLON = 5;
  private static final byte CSS_REV_SOLIDUS = 6;
  private static final byte CSS_IDENT_START = 7;
  private static final byte CSS_IDENT = 8;
  private static final byte CSS_NON_ASCII = 9;

  static {
    final byte[] table;
    table = new byte[128];

    // start with invalid

    // https://www.w3.org/TR/2021/CRD-css-syntax-3-20211224/#whitespace
    table['\n'] = CSS_WS;
    table['\t'] = CSS_WS;
    table[' '] = CSS_WS;

    // https://www.w3.org/TR/2021/CRD-css-syntax-3-20211224/#input-preprocessing
    table['\f'] = CSS_WS;
    table['\r'] = CSS_WS;

    // symbols
    table['*'] = CSS_ASTERISK;
    table['-'] = CSS_HYPHEN;
    table[':'] = CSS_COLON;
    table[';'] = CSS_SEMICOLON;
    table['\\'] = CSS_REV_SOLIDUS;

    // ident start
    Ascii.fill(table, Ascii.alphaLower(), CSS_IDENT_START);
    Ascii.fill(table, Ascii.alphaUpper(), CSS_IDENT_START);
    table['_'] = CSS_IDENT_START;

    // ident
    Ascii.fill(table, Ascii.digit(), CSS_IDENT);

    CSS = table;
  }

  private static final Set<String> NAMESPACES = Set.of(
      "breakpoint",
      "color",
      "font"
  );

  static final class ValueParser {
    int cursor, idx, mark0, mark1;

    String id, ns, varName;

    final StringBuilder sb = new StringBuilder();

    final String text;

    int valueIndex;

    final List<Value> values;

    ValueParser(int index, String text, List<Value> values) {
      this.text = text;

      this.valueIndex = index;

      this.values = values;
    }

    static final byte $DECLARATION = 0;

    static final byte $HYPHEN1 = 1;
    static final byte $HYPHEN2 = 2;

    static final byte $VAR_NAME = 3;

    static final byte $COLON = 4;

    static final byte $VALUE = 5;
    static final byte $VALUE_CHAR = 6;
    static final byte $VALUE_WS = 7;

    static final byte $NS_GLOBAL = 8;
    static final byte $NS_VALUE = 9;
    static final byte $NS_VALUE_CHAR = 10;
    static final byte $NS_VALUE_WS = 11;

    final int parse() {
      byte state;
      state = $DECLARATION;

      while (hasNext()) {
        final char c;
        c = next();

        state = switch (state) {
          case $DECLARATION -> parseDeclaration(c);

          case $HYPHEN1 -> parseHyphen1(c);
          case $HYPHEN2 -> parseHyphen2(c);

          case $VAR_NAME -> parseVarName(c);

          case $COLON -> parseColon(c);

          case $VALUE -> parseValue(c);
          case $VALUE_CHAR -> parseValueChar(c);
          case $VALUE_WS -> parseValueWs(c);

          case $NS_GLOBAL -> parseNsGlobal(c);
          case $NS_VALUE -> parseNsValue(c);
          case $NS_VALUE_CHAR -> parseNsValueChar(c);
          case $NS_VALUE_WS -> parseNsValueWs(c);

          default -> throw new AssertionError("Unexpected state=" + state);
        };
      }

      switch (state) {
        case $DECLARATION -> { /* success! */ }

        case $VAR_NAME -> throw error("Unexpected EOF while parsing a custom property name");

        case $COLON -> throw error("Declaration with no ':' colon character");

        case $VALUE, $VALUE_CHAR -> throw error("Unexpected EOF while parsing a declaration value");

        default -> throw error("Unexpected EOF");
      }

      return valueIndex;
    }

    private byte parseDeclaration(char c) {
      return switch (test(c)) {
        case CSS_WS -> $DECLARATION;

        case CSS_HYPHEN -> { mark0 = idx; yield $HYPHEN1; }

        default -> throw error("Expected start of --variable declaration");
      };
    }

    private byte parseHyphen1(char c) {
      return switch (test(c)) {
        case CSS_HYPHEN -> $HYPHEN2;

        default -> throw error("Expected start of --variable declaration");
      };
    }

    private byte parseHyphen2(char c) {
      return switch (test(c)) {
        case CSS_IDENT_START -> { mark1 = idx; yield $VAR_NAME; }

        case CSS_ASTERISK -> { ns = id = "*"; yield $NS_GLOBAL; }

        case CSS_REV_SOLIDUS -> throw error("Escape sequences are currently not supported");

        case CSS_NON_ASCII -> throw error("Non ASCII characters are currently not supported");

        default -> throw error("--variable name must start with a letter");
      };
    }

    private byte parseVarName(char c) {
      return switch (test(c)) {
        case CSS_IDENT_START, CSS_IDENT -> $VAR_NAME;

        case CSS_HYPHEN -> {
          if (ns == null) {
            ns = text.substring(mark1, idx);
          }

          yield $VAR_NAME;
        }

        case CSS_WS -> { parseVarName0(); yield $COLON; }

        case CSS_COLON -> { parseVarName0(); yield $VALUE; }

        default -> throw error("CSS variable name with invalid character=" + c);
      };
    }

    private void parseVarName0() {
      if (ns != null && NAMESPACES.contains(ns)) {
        final int beginIndex;
        beginIndex = mark1 + ns.length() + 1;

        id = text.substring(beginIndex, idx);
      } else {
        varName = text.substring(mark0, idx);

        if ("--rx".equals(varName)) {
          ns = "rx";
          id = "";
        } else {
          ns = id = null;
        }
      }
    }

    private byte parseColon(char c) {
      return switch (test(c)) {
        case CSS_WS -> $COLON;

        case CSS_COLON -> $VALUE;

        default -> throw error("Declaration with no ':' colon character");
      };
    }

    private byte parseValue(char c) {
      return switch (test(c)) {
        case CSS_WS -> $VALUE;

        case CSS_SEMICOLON -> throw error("Declaration with an empty value");

        default -> { sb.setLength(0); sb.append(c); yield $VALUE_CHAR; }
      };
    }

    private byte parseValueChar(char c) {
      return switch (test(c)) {
        case CSS_SEMICOLON -> { parseValue1(); yield $DECLARATION; }

        case CSS_WS -> $VALUE_WS;

        default -> { sb.append(c); yield $VALUE_CHAR; }
      };
    }

    private byte parseValueWs(char c) {
      return switch (test(c)) {
        case CSS_SEMICOLON -> { parseValue1(); yield $DECLARATION; }

        case CSS_WS -> $VALUE_WS;

        default -> { sb.append(' '); sb.append(c); yield $VALUE_CHAR; }
      };
    }

    private void parseValue1() {
      final Value result;

      final String value;
      value = sb.toString();

      if (ns != null) {
        result = themeProp(valueIndex++, ns, id, value);
      } else {
        result = customProp(valueIndex++, varName, value);
      }

      result(result);
    }

    private byte parseNsGlobal(char c) {
      return switch (test(c)) {
        case CSS_WS -> $NS_GLOBAL;

        case CSS_COLON -> $NS_VALUE;

        default -> throw error("Expected the global namespace '--*'");
      };
    }

    private byte parseNsValue(char c) {
      return switch (test(c)) {
        case CSS_WS -> $NS_VALUE;

        case CSS_IDENT_START -> { sb.setLength(0); sb.append(c); yield $NS_VALUE_CHAR; }

        default -> throw error("Expected the keyword 'initial'");
      };
    }

    private byte parseNsValueChar(char c) {
      return switch (test(c)) {
        case CSS_SEMICOLON -> { parseNsValue1(); yield $DECLARATION; }

        case CSS_WS -> { parseNsValue1(); yield $NS_VALUE_WS; }

        case CSS_IDENT_START -> { sb.append(c); yield $NS_VALUE_CHAR; }

        default -> throw error("Expected the keyword 'initial'");
      };
    }

    private byte parseNsValueWs(char c) {
      return switch (test(c)) {
        case CSS_SEMICOLON -> $DECLARATION;

        case CSS_WS -> $NS_VALUE_WS;

        default -> throw error("Expected the keyword 'initial'");
      };
    }

    private void parseNsValue1() {
      final String initial;
      initial = sb.toString();

      if (!"initial".equals(initial)) {
        throw error("Expected the keyword 'initial' but found '" + initial + "'");
      }

      final Value result;
      result = systemSkip(valueIndex++, ns);

      result(result);
    }

    private IllegalArgumentException error(String message) {
      return new IllegalArgumentException(message);
    }

    private boolean hasNext() {
      return cursor < text.length();
    }

    private char next() {
      idx = cursor++;

      return text.charAt(idx);
    }

    private byte test(char c) {
      return c < 128 ? CSS[c] : CSS_NON_ASCII;
    }

    private void result(Value result) {
      values.add(result);

      mark0 = mark1 = 0;

      id = ns = varName = null;
    }
  }

  static int parse(int index, String text, List<Value> values) {
    final ValueParser parser;
    parser = new ValueParser(index, text, values);

    return parser.parse();
  }

  // ##################################################################
  // # END: Value: Parser
  // ##################################################################

  // ##################################################################
  // # END: Value
  // ##################################################################

  // ##################################################################
  // # BEGIN: Variant
  // ##################################################################

  private static final byte[] VAR;

  private static final byte VAR_INVALID = 0;
  private static final byte VAR_CHAR = 1;
  private static final byte VAR_WS = 2;
  private static final byte VAR_SPACED = 3;
  private static final byte VAR_JOINED = 4;

  static {
    final byte[] table;
    table = new byte[128];

    // start with invalid

    // from SP (32) mark as CHAR
    Arrays.fill(table, ' ', table.length, VAR_CHAR);

    // WS
    for (int idx = 0; idx < CSS.length; idx++) {
      if (CSS[idx] == CSS_WS) {
        table[idx] = VAR_WS;
      }
    }

    table['('] = VAR_SPACED;

    table[':'] = VAR_JOINED;

    VAR = table;
  }

  sealed interface Variant {}

  record Simple(String value) implements Variant {}

  static Simple simple(String value) {
    return new Simple(value);
  }

  static final class VariantParser {

    static final byte $NORMAL = 0;
    static final byte $WORD = 1;
    static final byte $WS = 2;

    final StringBuilder sb = new StringBuilder();

    public final Variant parse(String text) {
      byte state;
      state = $NORMAL;

      boolean modified;
      modified = false;

      for (int idx = 0, len = text.length(); idx < len; idx++) {
        final char c;
        c = text.charAt(idx);

        state = switch (state) {
          case $NORMAL -> switch (test(c)) {
            case VAR_INVALID -> throw invalid(c);

            case VAR_WS -> $NORMAL;

            default -> { sb.setLength(0); sb.append(c); yield $WORD; }
          };

          case $WORD -> switch (test(c)) {
            case VAR_INVALID -> throw invalid(c);

            case VAR_WS -> $WS;

            case VAR_SPACED -> { modified = true; sb.append(' '); sb.append(c); yield $WORD; }

            case VAR_JOINED -> { sb.append(c); yield $WS; }

            default -> { sb.append(c); yield $WORD; }
          };

          case $WS -> switch (test(c)) {
            case VAR_INVALID -> throw invalid(c);

            case VAR_WS -> $WS;

            default -> { modified = true; sb.append(' '); sb.append(c); yield $WORD; }
          };

          default -> throw new AssertionError("Unexpected state=" + state);
        };
      }

      if (state == $NORMAL) {
        throw new IllegalArgumentException("Variant with a blank rule");
      }

      final String rule;
      rule = modified ? sb.toString() : text;

      return simple(rule);
    }

    private IllegalArgumentException invalid(char c) {
      return new IllegalArgumentException("Variant with an invalid character: " + c);
    }

    private byte test(char c) {
      return c < 128 ? VAR[c] : VAR_CHAR;
    }

  }

  // ##################################################################
  // # END: Variant
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configuring
  // ##################################################################

  static final List<String> ROOT = List.of(":root");

  static final class Configuring {

    final Note.Ref2<Value, Value> $replaced = Note.Ref2.create(getClass(), "REP", Note.INFO);

    final Map<String, ThemeProp> keywords = new HashMap<>();

    final Map<String, Map<String, Value>> namespaces = new HashMap<>();

    Note.Sink noteSink;

    Set<Class<?>> scanClasses = Set.of();

    Set<Path> scanDirectories = Set.of();

    Set<Class<?>> scanJars = Set.of();

    String systemBase;

    String systemTheme;

    Map<String, Variant> systemVariants;

    final Map<List<String>, List<Value>> themeValues = new LinkedHashMap<>();

    String userTheme = "";

    int valueIndex;

    final Map<String, Variant> variants = new HashMap<>();

    // ##################################################################
    // # BEGIN: Configuring: Public API
    // ##################################################################

    public final void noteSink(Note.Sink value) {
      if (noteSink != null) {
        throw new IllegalStateException("A Note.Sink has already been defined");
      }

      noteSink = Objects.requireNonNull(value, "value == null");
    }

    public final void scanClass(Class<?> value) {
      final Class<?> c;
      c = Objects.requireNonNull(value, "value");

      if (scanClasses.isEmpty()) {
        scanClasses = new HashSet<>();
      }

      scanClasses.add(c);
    }

    public final void scanDirectory(Path value) {
      final Path p;
      p = Objects.requireNonNull(value, "value == null");

      if (scanDirectories.isEmpty()) {
        scanDirectories = new HashSet<>();
      }

      scanDirectories.add(p);
    }

    public final void scanJarFileOf(Class<?> value) {
      final Class<?> c;
      c = Objects.requireNonNull(value, "value == null");

      if (scanJars.isEmpty()) {
        scanJars = new HashSet<>();
      }

      scanJars.add(c);
    }

    public final void theme(String value) {
      userTheme = Objects.requireNonNull(value, "value == null");
    }

    public final Config configure() {
      if (noteSink == null) {
        noteSink = Note.NoOpSink.create();
      }

      systemTheme();

      systemVariants();

      userTheme();

      breakpointVariants();

      themeArtifacts();

      return new Config(
          systemBase == null ? Css.systemBase() : systemBase,

          Map.copyOf(keywords),

          noteSink,

          namespaces.containsKey("rx"),

          Set.copyOf(scanClasses),

          Set.copyOf(scanDirectories),

          Set.copyOf(scanJars),

          Map.copyOf(themeValues),

          Map.copyOf(variants)
      );
    }

    // ##################################################################
    // # END: Configuring: Public API
    // ##################################################################

    private void systemTheme() {
      // parse system theme
      final List<Value> parsed;
      parsed = new ArrayList<>();

      final String source;
      source = systemTheme != null ? systemTheme : Css.systemTheme();

      valueIndex = parse(valueIndex, source, parsed);

      // map to namespace
      for (Value value : parsed) {
        switch (value) {
          case CustomProp prop -> throw new IllegalArgumentException(
              "Arbitrary custom props are not allowed in the system theme"
          );

          case SystemSkip skip -> throw new IllegalArgumentException(
              "The '--<namespace>: initial;' like syntax is not allowed in the system theme"
          );

          case ThemeProp prop -> {
            final String ns;
            ns = prop.ns;

            final Map<String, Value> values;
            values = namespaces.computeIfAbsent(ns, key -> new HashMap<>());

            final Value existing;
            existing = values.put(prop.name, prop);

            if (existing != null) {
              noteSink.send($replaced, existing, prop);
            }
          }
        }
      }

      systemTheme = null;
    }

    private void systemVariants() {
      Map<String, Variant> sv;
      sv = systemVariants;

      if (sv == null) {
        sv = Css.systemVariants();
      } else {
        systemVariants = null;
      }

      variants.putAll(sv);
    }

    private void userTheme() {
      // parse user theme
      final List<Value> parsed;
      parsed = new ArrayList<>();

      valueIndex = parse(valueIndex, userTheme, parsed);

      // process any system skip
      for (Value value : parsed) {
        if (value instanceof SystemSkip skip) {
          final String ns;
          ns = skip.ns;

          if ("*".equals(ns)) {
            namespaces.clear();
          } else {
            namespaces.remove(ns);
          }
        }
      }

      // map to namespace
      for (Value value : parsed) {
        switch (value) {
          case CustomProp prop -> {
            final Map<String, Value> values;
            values = namespaces.computeIfAbsent("custom", key -> new HashMap<>());

            final Value existing;
            existing = values.put(prop.name, prop);

            if (existing != null) {
              noteSink.send($replaced, existing, prop);
            }
          }

          case SystemSkip skip -> {}

          case ThemeProp prop -> {
            final String ns;
            ns = prop.ns;

            final Map<String, Value> values;
            values = namespaces.computeIfAbsent(ns, key -> new HashMap<>());

            final Value existing;
            existing = values.put(prop.name, prop);

            if (existing != null) {
              noteSink.send($replaced, existing, prop);
            }
          }
        }
      }
    }

    private void breakpointVariants() {
      final String ns;
      ns = "breakpoint";

      final Map<String, Value> values;
      values = namespaces.getOrDefault(ns, Map.of());

      for (Value breakpoint : values.values()) {
        final ThemeProp var;
        var = (ThemeProp) breakpoint;

        final String id;
        id = var.id;

        final Variant variant;
        variant = simple("@media (min-width: " + var.value + ")");

        variant(id, variant);
      }
    }

    private void variant(String name, Variant variant) {
      final Variant maybeExisting;
      maybeExisting = variants.put(name, variant);

      if (maybeExisting == null) {
        return;
      }

      // TODO restore existing and log?
    }

    private void themeArtifacts() {
      final List<Value> root;
      root = themeValues.computeIfAbsent(ROOT, key -> new ArrayList<>());

      for (Map<String, Value> map : namespaces.values()) {
        for (Value value : map.values()) {
          switch (value) {
            case CustomProp prop -> root.add(prop);

            case SystemSkip skip -> {}

            case ThemeProp prop -> {
              root.add(prop);

              final String id;
              id = prop.id;

              if (id.isEmpty()) {
                continue;
              }

              final String ns;
              ns = prop.ns;

              final String key;

              if ("breakpoint".equals(ns)) {
                key = "screen-" + id;
              } else {
                key = id;
              }

              final ThemeProp maybeExisting;
              maybeExisting = keywords.put(key, prop);

              if (maybeExisting != null) {
                throw new IllegalArgumentException("Duplicate mapping for " + key + ": " + maybeExisting.value + ", " + prop.value);
              }
            }
          }
        }
      }
    }

  }

  @Override
  public final void noteSink(Note.Sink value) {
    configuring.noteSink(value);
  }

  @Override
  public final void scanClass(Class<?> value) {
    configuring.scanClass(value);
  }

  @Override
  public final void scanDirectory(Path value) {
    configuring.scanDirectory(value);
  }

  @Override
  public final void scanJarFileOf(Class<?> value) {
    configuring.scanJarFileOf(value);
  }

  public final void systemBase(String value) {
    configuring.systemBase = Objects.requireNonNull(value, "value == null");
  }

  public final void systemTheme(String value) {
    configuring.systemTheme = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void theme(String value) {
    configuring.theme(value);
  }

  // ##################################################################
  // # END: Configuring
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configured
  // ##################################################################

  record Config(

      String base,

      Map<String, ThemeProp> keywords,

      Note.Sink noteSink,

      boolean rx,

      Set<Class<?>> scanClasses,

      Set<Path> scanDirectories,

      Set<Class<?>> scanJars,

      Map<List<String>, List<Value>> themeValues,

      Map<String, Variant> variants

  ) {}

  // ##################################################################
  // # END: Configured
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan Classes
  // ##################################################################

  static final class Classes {

    final Note.Ref1<String> $classNotFound = Note.Ref1.create(getClass(), "CNF", Note.WARN);
    final Note.Ref2<String, IOException> $classIoError = Note.Ref2.create(getClass(), "CIX", Note.WARN);
    final Note.Ref2<String, Lang.InvalidClassFileException> $classInvalid = Note.Ref2.create(getClass(), "CFI", Note.WARN);

    final ClassFiles classFiles;

    final Set<Class<?>> classes;

    final Note.Sink noteSink;

    Classes(ClassFiles classFiles, Set<Class<?>> classes, Note.Sink noteSink) {
      this.classFiles = classFiles;

      this.classes = classes;

      this.noteSink = noteSink;
    }

    public final void scan() {
      for (Class<?> next : classes) {
        scan(next);
      }
    }

    private void scan(Class<?> next) {
      final String binaryName;
      binaryName = next.getName();

      String resourceName;
      resourceName = binaryName.replace('.', '/');

      resourceName += ".class";

      final ClassLoader loader;
      loader = next.getClassLoader();

      final InputStream in;
      in = loader.getResourceAsStream(resourceName);

      if (in == null) {
        noteSink.send($classNotFound, binaryName);

        return;
      }

      final byte[] bytes;

      try (in) {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        in.transferTo(out);

        bytes = out.toByteArray();
      } catch (IOException e) {
        noteSink.send($classIoError, binaryName, e);

        return;
      }

      classFiles.scan(binaryName, bytes);
    }

  }

  // ##################################################################
  // # END: Scan Classes
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan Dirs
  // ##################################################################

  static final class Dirs implements FileVisitor<Path> {

    final Note.Ref2<Path, IOException> $directoryDirError = Note.Ref2.create(getClass(), "DDE", Note.WARN);
    final Note.Ref2<Path, IOException> $directoryFileError = Note.Ref2.create(getClass(), "DFE", Note.WARN);

    final ClassFiles classFiles;

    final Set<Path> dirs;

    final Note.Sink noteSink;

    Dirs(ClassFiles classFiles, Set<Path> dirs, Note.Sink noteSink) {
      this.classFiles = classFiles;

      this.dirs = dirs;

      this.noteSink = noteSink;
    }

    public final void scan() {
      for (Path dir : dirs) {
        try {
          Files.walkFileTree(dir, this);
        } catch (IOException e) {
          noteSink.send($directoryDirError, dir, e);
        }
      }
    }

    @Override
    public final FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
      return FileVisitResult.CONTINUE;
    }

    @Override
    public final FileVisitResult postVisitDirectory(Path dir, IOException exc) {
      if (exc != null) {
        noteSink.send($directoryDirError, dir, exc);
      }

      return FileVisitResult.CONTINUE;
    }

    @Override
    public final FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
      final Path fileNamePath;
      fileNamePath = file.getFileName();

      final String fileName;
      fileName = fileNamePath.toString();

      if (!fileName.endsWith(".class")) {
        return FileVisitResult.CONTINUE;
      }

      try {
        final byte[] bytes;
        bytes = Files.readAllBytes(file);

        classFiles.scanIfAnnotated(fileName, bytes);
      } catch (IOException e) {
        noteSink.send($directoryFileError, file, e);
      }

      return FileVisitResult.CONTINUE;
    }

    @Override
    public final FileVisitResult visitFileFailed(Path file, IOException exc) {
      return FileVisitResult.CONTINUE;
    }

  }

  // ##################################################################
  // # END: Scan Dirs
  // ##################################################################

  // ##################################################################
  // # BEGIN: Scan JARs
  // ##################################################################

  static final class Jars {

    final Note.Ref2<Class<?>, Throwable> $jarFileException = Note.Ref2.create(getClass(), "JER", Note.WARN);
    final Note.Ref2<Class<?>, String> $jarFileNull = Note.Ref2.create(getClass(), "JNV", Note.WARN);

    final ClassFiles classFiles;

    final Set<Class<?>> jars;

    final Note.Sink noteSink;

    Jars(ClassFiles classFiles, Set<Class<?>> jars, Note.Sink noteSink) {
      this.classFiles = classFiles;

      this.jars = jars;

      this.noteSink = noteSink;
    }

    public final void scan() {
      for (Class<?> locator : jars) {
        scan(locator);
      }
    }

    private void scan(Class<?> clazz) {
      final ProtectionDomain domain;

      try {
        domain = clazz.getProtectionDomain();
      } catch (SecurityException e) {
        noteSink.send($jarFileException, clazz, e);

        return;
      }

      final CodeSource source;
      source = domain.getCodeSource();

      if (source == null) {
        noteSink.send($jarFileNull, clazz, "CodeSource");

        return;
      }

      final URL location;
      location = source.getLocation();

      if (location == null) {
        noteSink.send($jarFileNull, clazz, "Location");

        return;
      }

      final File file;

      try {
        final URI uri;
        uri = location.toURI();

        file = new File(uri);
      } catch (URISyntaxException e) {
        noteSink.send($jarFileException, clazz, e);

        return;
      }

      try (JarFile jar = new JarFile(file)) {
        final Enumeration<JarEntry> entries;
        entries = jar.entries();

        while (entries.hasMoreElements()) {
          final JarEntry entry;
          entry = entries.nextElement();

          final String entryName;
          entryName = entry.getName();

          if (!entryName.endsWith(".class")) {
            continue;
          }

          final long size;
          size = entry.getSize();

          final int intSize;
          intSize = Math.toIntExact(size);

          final byte[] bytes;
          bytes = new byte[intSize];

          final int bytesRead;

          try (InputStream in = jar.getInputStream(entry)) {
            bytesRead = in.read(bytes, 0, bytes.length);
          }

          if (bytesRead != bytes.length) {
            continue;
          }

          classFiles.scanIfAnnotated(entryName, bytes);
        }
      } catch (ArithmeticException | IOException e) {
        noteSink.send($jarFileException, clazz, e);
      }
    }

  }

  // ##################################################################
  // # END: Scan JARs
  // ##################################################################

  // ##################################################################
  // # BEGIN: ClassFile Scanner
  // ##################################################################

  // scanner abstract for testing
  interface ClassFiles {

    void scan(String name, byte[] bytes);

    void scanIfAnnotated(String name, byte[] bytes);

  }

  static final class Scanner implements ClassFiles {

    final Note.Sink noteSink;

    final Strings strings;

    Scanner(Note.Sink noteSink, Strings strings) {
      this.noteSink = noteSink;

      this.strings = strings;
    }

    @Override
    public final void scan(String name, byte[] bytes) {
      final ClassFile classFile;
      classFile = ClassFile.of();

      final ClassModel classModel;
      classModel = classFile.parse(bytes);

      scan(classModel);
    }

    @Override
    public final void scanIfAnnotated(String name, byte[] bytes) {
      final ClassFile classFile;
      classFile = ClassFile.of();

      final ClassModel classModel;
      classModel = classFile.parse(bytes);

      final Optional<RuntimeInvisibleAnnotationsAttribute> maybe;
      maybe = classModel.findAttribute(Attributes.runtimeInvisibleAnnotations());

      if (maybe.isEmpty()) {
        return;
      }

      final RuntimeInvisibleAnnotationsAttribute attribute;
      attribute = maybe.get();

      final List<Annotation> annotations;
      annotations = attribute.annotations();

      for (Annotation annotation : annotations) {
        final Utf8Entry className;
        className = annotation.className();

        if (className.equalsString("Lobjectos/way/Css$Source;")) {
          scan(classModel);

          break;
        }
      }
    }

    private void scan(ClassModel classModel) {
      final ConstantPool constantPool;
      constantPool = classModel.constantPool();

      for (PoolEntry entry : constantPool) {
        if (!(entry instanceof StringEntry str)) {
          continue;
        }

        final String string;
        string = str.stringValue();

        strings.consume(string);
      }
    }

  }

  // ##################################################################
  // # End: ClassFile Scanner
  // ##################################################################

  // ##################################################################
  // # BEGIN: Tokenizer
  // ##################################################################

  @FunctionalInterface
  interface Strings {
    void consume(String s);
  }

  private static final byte[] TKS;

  private static final byte TKS_WS = 1;
  private static final byte TKS_CHAR = 2;
  private static final byte TKS_SLASH = 3;
  private static final byte TKS_COLON = 4;
  private static final byte TKS_ESCAPE = 5;
  private static final byte TKS_UNDER = 6;

  static {
    // start with invalid
    final byte[] table;
    table = new byte[128];

    // from SP (32) mark as TKS_CHAR
    Arrays.fill(table, ' ', table.length, TKS_CHAR);

    // WS
    for (int idx = 0; idx < CSS.length; idx++) {
      if (CSS[idx] == CSS_WS) {
        table[idx] = TKS_WS;
      }
    }

    // SLASH
    table['/'] = TKS_SLASH;

    // COLON
    table[':'] = TKS_COLON;

    // BACKSLASH
    table['\\'] = TKS_ESCAPE;

    // UNDERSCORE
    table['_'] = TKS_UNDER;

    TKS = table;
  }

  static final class Tokenizer implements Strings {

    static final byte $NORMAL = 0;
    static final byte $NORMAL_COLON = 1;
    static final byte $INVALID = 2;
    static final byte $VALUE = 3;
    static final byte $VALUE_COLON = 4;
    static final byte $VALUE_UNDER = 5;
    static final byte $PROP = 6;
    static final byte $PROP_COLON = 7;
    static final byte $PROP_SLASH = 8;
    static final byte $VAR = 9;
    static final byte $VAR_SLASH = 10;
    static final byte $VAR_UNDER = 11;

    private final List<String> acc = new ArrayList<>();

    private int mark;

    private final StringBuilder sb = new StringBuilder();

    private final Slugs slugs;

    Tokenizer(Slugs slugs) {
      this.slugs = slugs;
    }

    @Override
    public final void consume(String s) {
      byte state;
      state = $NORMAL;

      for (int idx = s.length() - 1; idx >= 0; idx--) {
        final char c;
        c = s.charAt(idx);

        final byte test;
        test = test(c);

        state = switch (state) {
          case $NORMAL -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_COLON -> { acc.clear(); sb.setLength(0); mark = idx; yield $NORMAL_COLON; }

            default -> { acc.clear(); sb.setLength(0); mark = idx; sb.append(c); yield $VALUE; }
          };

          case $NORMAL_COLON -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_COLON -> { sb.append(':'); yield $VALUE; }

            default -> $INVALID;
          };

          case $INVALID -> switch (test) {
            case TKS_WS -> $NORMAL;

            default -> $INVALID;
          };

          case $VALUE -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_COLON -> $VALUE_COLON;

            case TKS_UNDER -> $VALUE_UNDER;

            default -> { sb.append(c); yield $VALUE; }
          };

          case $VALUE_COLON -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_ESCAPE -> { sb.append(':'); yield $VALUE; }

            default -> { acc(); sb.append(c); yield $PROP; }
          };

          case $VALUE_UNDER -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_COLON -> { /*trim sp*/ sb.append(':'); yield $VALUE; }

            case TKS_ESCAPE -> { sb.append('_'); yield $VALUE; }

            default -> { sb.append(' '); sb.append(c); yield $VALUE; }
          };

          case $PROP -> switch (test) {
            case TKS_WS -> { acc(); cons(s, idx + 1); yield $NORMAL; }

            case TKS_COLON -> $INVALID;

            case TKS_SLASH -> { acc(); yield $PROP_SLASH; }

            default -> { sb.append(c); yield $PROP; }
          };

          case $PROP_SLASH -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_SLASH -> $INVALID;

            default -> { sb.append(c); yield $VAR; }
          };

          case $VAR -> switch (test) {
            case TKS_WS -> { acc(); cons(s, idx + 1); yield $NORMAL; }

            case TKS_SLASH -> $VAR_SLASH;

            case TKS_UNDER -> $VAR_UNDER;

            default -> { sb.append(c); yield $VAR; }
          };

          case $VAR_SLASH -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_ESCAPE -> { sb.append('/'); yield $VAR; }

            case TKS_UNDER -> { acc(); yield $VAR_UNDER; }

            default -> { acc(); sb.append(c); yield $VAR; }
          };

          case $VAR_UNDER -> switch (test) {
            case TKS_WS -> $NORMAL;

            case TKS_SLASH -> { /*trim space*/ acc(); yield $VAR; }

            case TKS_ESCAPE -> { sb.append('_'); yield $VAR; }

            default -> { sb.append(' '); sb.append(c); yield $VAR; }
          };

          default -> throw new AssertionError("Unexpected state=" + state);
        };
      }

      if (state == $PROP || state == $VAR) {
        acc();

        cons(s, 0);
      }
    }

    private void acc() {
      sb.reverse();

      final String s;
      s = sb.toString();

      sb.setLength(0);

      acc.add(s);
    }

    private void cons(String s, int idx) {
      final String className;
      className = s.substring(idx, mark + 1);

      slugs.consume(className, acc);
    }

    private byte test(char c) {
      return c < 128 ? TKS[c] : TKS_CHAR;
    }

  }

  // ##################################################################
  // # END: Tokenizer
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utility
  // ##################################################################

  @FunctionalInterface
  interface Slugs {
    void consume(String className, List<String> slugs);
  }

  record Utility(List<Variant> variants, String className, String property, String value)
      implements Comparable<Utility> {
    @Override
    public final int compareTo(Utility o) {
      int result;
      result = property.compareTo(o.property);

      if (result != 0) {
        return result;
      }

      result = value.compareTo(o.value);

      if (result != 0) {
        return result;
      }

      return className.compareTo(o.className);
    }
  }

  static Utility utility(List<Variant> variants, String className, String property, String value) {
    return new Utility(variants, className, property, value);
  }

  static final class Proc implements Slugs {

    final Set<String> distinct = new HashSet<>();

    final Note.Sink noteSink;

    final List<Variant> parsedVars = new ArrayList<>();

    final StringBuilder sb = new StringBuilder();

    final List<Utility> utilities = new ArrayList<>();

    final VariantParser variantParser = new VariantParser();

    final Map<String, Variant> variants;

    Proc(Note.Sink noteSink, Map<String, Variant> variants) {
      this.noteSink = noteSink;

      this.variants = variants;
    }

    @Override
    public final void consume(String className, List<String> slugs) {
      if (!distinct.add(className)) {
        // already seen...
        return;
      }

      final String propValue;
      propValue = slugs.get(0);

      final String propName;
      propName = slugs.get(1);

      // process variants
      parsedVars.clear();

      for (int idx = slugs.size() - 1; idx > 1; idx--) {
        final String variantName;
        variantName = slugs.get(idx);

        final Variant variant;
        variant = variantByName(variantName);

        if (variant == null) {
          // TODO log
          return;
        }

        parsedVars.add(variant);
      }

      final List<Variant> vars;
      vars = List.copyOf(parsedVars);

      final Utility utility;
      utility = new Utility(vars, className, propName, propValue);

      utilities.add(utility);
    }

    private Variant variantByName(String name) {
      final Variant result;
      result = variants.get(name);

      if (result != null) {
        return result;
      }

      try {
        final Variant parsed;
        parsed = variantParser.parse(name);

        variants.put(name, parsed);

        return parsed;
      } catch (IllegalArgumentException expected) {
        // TODO log
        return null;
      }
    }

  }

  // ##################################################################
  // # END: Utility
  // ##################################################################

  // ##################################################################
  // # BEGIN: Gen
  // ##################################################################

  record Rule(String className, List<Variant> variants, String property, String value) {}

  static Rule rule(String className, List<Variant> variants, String property, String value) {
    return new Rule(className, variants, property, value);
  }

  record Ctx(List<Rule> rules, List<ThemeSection> sections) {}

  static final class Gen {

    int entryIndex;

    final Map<String, ThemeProp> keywords;

    final List<Rule> rules = new ArrayList<>();

    final StringBuilder sb = new StringBuilder();

    final Map<List<String>, List<Value>> themeValues;

    final List<Utility> utilities;

    Gen(Map<String, ThemeProp> keywords, Map<List<String>, List<Value>> themeValues, List<Utility> utilities) {
      this.keywords = keywords;

      this.themeValues = themeValues;

      this.utilities = utilities;
    }

    public final Ctx generate() {
      for (Utility utility : utilities) {
        process(utility);
      }

      return new Ctx(
          List.copyOf(rules),

          themeSections()
      );
    }

    private void process(Utility utility) {
      // build class name
      sb.setLength(0);

      sb.append('.');

      Css.serializeIdentifier(sb, utility.className);

      final String className;
      className = sb.toString();

      final List<Variant> variants;
      variants = utility.variants;

      final String property;
      property = utility.property;

      final String value;
      value = formatValue(utility.value);

      final Rule rule;
      rule = rule(className, variants, property, value);

      rules.add(rule);
    }

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
          final ThemeProp kw;
          kw = keywords.get(maybe);

          if (kw == null) {
            break;
          }

          kw.marked = true;

          formatValueNormal(value, beginIndex);

          sb.append(kw.value);

          entryIndex = index;
        }

        case OPACITY -> {
          // extract keyword
          final String maybe;
          maybe = value.substring(beginIndex, slashIndex);

          // check for match
          final ThemeProp kw;
          kw = keywords.get(maybe);

          if (kw == null) {
            break;
          }

          kw.marked = true;

          formatValueNormal(value, beginIndex);

          final String opacity;
          opacity = value.substring(slashIndex + 1, index);

          sb.append("color-mix(in oklab, ");

          sb.append(kw.value);

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

    private List<ThemeSection> themeSections() {
      final List<ThemeSection> sections;
      sections = new ArrayList<>();

      for (Map.Entry<List<String>, List<Value>> entry : themeValues.entrySet()) {
        final List<Value> original;
        original = entry.getValue();

        final List<Value> values;
        values = original.stream()
            .filter(Value::themeSection)
            .sorted()
            .toList();

        if (values.isEmpty()) {
          continue;
        }

        final List<String> selector;
        selector = entry.getKey();

        final ThemeSection section;
        section = new ThemeSection(selector, values);

        sections.add(section);
      }

      return List.copyOf(sections);
    }

    // BEGIN: test-only section

    Gen() {
      keywords = new HashMap<>();

      themeValues = new LinkedHashMap<>();

      utilities = new ArrayList<>();
    }

    final void themeValue(List<String> sel, int idx, String ns, String keyword, String value) {
      final ThemeProp prop;
      prop = themeProp(idx, ns, keyword, value);

      keywords.put(keyword, prop);

      final List<Value> values;
      values = themeValues.computeIfAbsent(sel, key -> new ArrayList<>());

      values.add(prop);
    }

    final void utility(List<Variant> variants, String className, String property, String value) {
      utilities.add(
          CssEngine2.utility(variants, className, property, value)
      );
    }

    // END: test-only section

  }

  // ##################################################################
  // # END: Gen
  // ##################################################################

  // ##################################################################
  // # BEGIN: Writer
  // ##################################################################

  // ##################################################################
  // # END: Writer
  // ##################################################################

  private static abstract class Writer {

    private Appendable out;

    int level;

    public final void write(Appendable out) throws IOException {
      this.out = out;
      write();
      this.out = null;
    }

    abstract void write() throws IOException;

    final void indent() throws IOException {
      for (int i = 0, count = level * 2; i < count; i++) {
        out.append(' ');
      }
    }

    final void w(char c) throws IOException {
      out.append(c);
    }

    final void w(char c0, char c1) throws IOException {
      out.append(c0);
      out.append(c1);
    }

    final void w(CharSequence s) throws IOException {
      out.append(s);
    }

    final void wln() throws IOException {
      out.append('\n');
    }

    final void wln(char c) throws IOException {
      out.append(c);
      out.append('\n');
    }

    final void wln(String s) throws IOException {
      out.append(s);
      out.append('\n');
    }

  }

  // ##################################################################
  // # BEGIN: Theme
  // ##################################################################

  record ThemeSection(List<String> selector, List<Value> values) {}

  static final class Theme extends Writer {

    final List<ThemeSection> sections;

    Theme(List<ThemeSection> sections) {
      this.sections = sections;
    }

    @Override
    final void write() throws IOException {
      if (sections.isEmpty()) {
        return;
      }

      wln("@layer theme {");

      level++;

      for (ThemeSection section : sections) {
        final List<String> selector;
        selector = section.selector;

        for (String part : selector) {
          indent();

          w(part);

          wln(" {");

          level++;
        }

        final List<Value> values;
        values = section.values;

        for (Value value : values) {
          indent();

          w(value.name());

          w(": ");

          w(value.value());

          wln(";");
        }

        for (int idx = 0, size = selector.size(); idx < size; idx++) {
          level--;

          indent();

          wln('}');
        }
      }

      level--;

      wln('}');
    }

  }

  // ##################################################################
  // # END: Theme
  // ##################################################################

  // ##################################################################
  // # BEGIN: Base
  // ##################################################################

  static final class Base extends Writer {

    final String source;

    Base(String source) {
      this.source = source;
    }

    @Override
    final void write() throws IOException {
      if (source == null || source.isBlank()) {
        return;
      }

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

      wln("@layer base {");

      boolean indent;
      indent = true;

      level++;

      for (int idx = 0, len = source.length(); idx < len; idx++) {
        final char c;
        c = source.charAt(idx);

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

              wln(c);
            }

            else if (c == '}') {
              parser = Parser.NORMAL;

              indent = true;

              level--;

              indent();

              wln(c);
            }

            else {
              parser = Parser.TEXT;

              if (indent) {
                indent();

                indent = false;
              }

              w(c);
            }
          }

          case SLASH -> {
            if (c == '*') {
              parser = Parser.COMMENT;
            }

            else {
              parser = Parser.UNKNOWN;

              w('/', c);
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

              w(' ');
            }

            else if (c == '{') {
              throw new UnsupportedOperationException("Implement me");
            }

            else if (c == ';') {
              parser = Parser.NORMAL;

              indent = true;

              wln(c);
            }

            else {
              parser = Parser.TEXT;

              w(c);
            }
          }

          case UNKNOWN -> w(c);
        }
      }

      wln('}');
    }

  }

  // ##################################################################
  // # END: Base
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utilities
  // ##################################################################

  static final class Utilities extends Writer {

    final List<Rule> rules;

    Utilities(List<Rule> rules) {
      this.rules = rules;
    }

    @Override
    final void write() throws IOException {
      if (rules.isEmpty()) {
        return;
      }

      wln("@layer utilities {");

      level++;

      for (Rule rule : rules) {
        indent();

        w(rule.className);

        w(" { ");

        final List<Variant> variants;
        variants = rule.variants;

        for (Variant variant : variants) {
          switch (variant) {
            case Simple s -> {
              w(s.value);

              w(" { ");
            }
          }
        }

        w(rule.property);

        w(": ");

        w(rule.value);

        for (int idx = 0, size = variants.size(); idx < size; idx++) {
          w(" }");
        }

        wln(" }");
      }

      level--;

      wln('}');
    }

  }

  // ##################################################################
  // # END: Utilities
  // ##################################################################

}