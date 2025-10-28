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
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

final class CssEngine2 {

  static final class System {
    String base = Css.systemBase();

    String theme = Css.systemTheme();

    Map<String, Variant> variants = Css.systemVariants();
  }

  // ##################################################################
  // # BEGIN: Execute
  // ##################################################################

  private final Config config;

  CssEngine2(Config config) {
    this.config = config;
  }

  public final void generate(Appendable out) throws IOException {
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

    final Map<String, Keyframes> keyframes;
    keyframes = config.keyframes;

    final Map<String, Decl> properties;
    properties = config.properties;

    final List<Section> protoSections;
    protoSections = config.sections;

    final List<Utility> utils;
    utils = proc.utilities;

    final Gen gen;
    gen = new Gen(keyframes, properties, protoSections, utils);

    final Ctx ctx;
    ctx = gen.generate();

    final List<Section> sections;
    sections = ctx.sections;

    final Theme theme;
    theme = new Theme(sections);

    theme.write(out);

    final String baseSource;
    baseSource = config.base;

    final Base base;
    base = new Base(baseSource);

    base.write(out);

    final List<ParsedRule> components;
    components = config.components;

    final Components compWriter;
    compWriter = new Components(components);

    compWriter.write(out);

    final List<Rule> rules;
    rules = ctx.rules;

    final Utilities utilities;
    utilities = new Utilities(rules);

    utilities.write(out);

    final List<List<Decl>> fontFaces;
    fontFaces = config.fontFaces;

    final List<Keyframes> keyframesToWrite;
    keyframesToWrite = ctx.keyframes;

    final Trailer trailer;
    trailer = new Trailer(fontFaces, keyframesToWrite);

    trailer.write(out);
  }

  // ##################################################################
  // # END: Execute
  // ##################################################################

  // ##################################################################
  // # BEGIN: CSS Parser
  // ##################################################################

  static final class Decl implements Stmt {
    private boolean marked;
    private Decl next;
    final String property;
    private boolean replaced;
    final List<Value> values;

    Decl(String property, List<Value> values) {
      this.property = property;

      this.values = values;
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof Decl that
          && marked == that.marked
          && property.equals(that.property)
          && replaced == that.replaced
          && values.equals(that.values);
    }

    @Override
    public final String toString() {
      return property + ": " + values + ";";
    }

    final Decl append(Decl decl) {
      if (next == null) {
        next = decl;
      } else {
        next.append(decl);
      }

      return this;
    }

    final IllegalArgumentException invalid(String msg) {
      return new IllegalArgumentException(msg + "\n\t" + property + ": " + values);
    }

    final void mark() {
      if (!replaced) {
        marked = true;
      }

      if (next != null) {
        next.mark();
      }
    }

    final void mark(Map<String, Decl> properties) {
      marked = true;

      for (Value value : values) {
        value.markProperties(properties);
      }

      if (next != null) {
        next.mark(properties);
      }
    }

    final boolean marked() {
      return marked;
    }

    final Decl replaced() {
      marked = false;

      replaced = true;

      return this;
    }
  }

  static Decl decl(String p, List<Value> v) { return new Decl(p, v); }
  static Decl decl(String p, Value... v) { return new Decl(p, List.of(v)); }

  sealed interface Stmt {}

  /// A top-level CSS statement
  sealed interface Top {}

  private record StyleRule(String selector, List<Stmt> stmts) implements Stmt, Top {}

  static StyleRule styleRule(String sel, Stmt... stmts) { return new StyleRule(sel, List.of(stmts)); }

  sealed interface Value {

    static void formatTo(Appendable out, List<Value> values) throws IOException {
      for (int idx = 0, size = values.size(); idx < size; idx++) {
        final Value value;
        value = values.get(idx);

        switch (value) {
          case
               Fun(String name, List<Value> args) -> {
            if (idx != 0) {
              out.append(' ');
            }

            if ("--rx".equals(name) && args.size() == 1 && args.get(0) instanceof Number(String v)) {
              out.append("calc(");

              out.append(v);

              out.append(" / 16 * 1rem)");
            }

            else {
              out.append(name);

              out.append('(');

              formatTo(out, args);

              out.append(')');
            }
          }

          case
               Number(String v) -> {
            if (idx != 0) {
              out.append(' ');
            }

            out.append(v);
          }

          case Sep.COMMA -> {
            out.append(',');
          }

          case
               Tok(String v) -> {
            if (idx != 0) {
              out.append(' ');
            }

            out.append(v);
          }
        }
      }

    }

    static void formatTo(StringBuilder sb, List<Value> values) {
      try {
        final Appendable out;
        out = sb;

        formatTo(out, values);
      } catch (IOException e) {
        throw new AssertionError("StringBuilder does not throw IOException", e);
      }
    }

    default void markProperties(Map<String, Decl> properties) {
      // noop by default
    }

  }

  private record Fun(String name, List<Value> args) implements Value {
    @Override
    public final void markProperties(Map<String, Decl> properties) {
      for (Value arg : args) {
        arg.markProperties(properties);
      }

      if (!name.equals("var")) {
        return;
      }

      if (args.isEmpty()) {
        return;
      }

      final Value first;
      first = args.get(0);

      if (!(first instanceof Tok(String v))) {
        return;
      }

      final Decl decl;
      decl = properties.get(v);

      if (decl == null) {
        return;
      }

      decl.mark(properties);
    }

  }

  static Fun fun(String name, List<Value> args) { return new Fun(name, args); }
  static Fun fun(String name, Value... args) { return new Fun(name, List.of(args)); }

  private record Number(String v) implements Value {}

  static Number number(String v) { return new Number(v); }

  enum Sep implements Value {
    COMMA;
  }

  /// arbitrary token
  private record Tok(String v) implements Value {}

  static Tok tok(String v) { return new Tok(v); }

  private static final byte[] CSS;

  private static final byte CSS_WS = 1;
  private static final byte CSS_DQUOTE = 2;
  private static final byte CSS_HASH = 3;
  private static final byte CSS_PERCENT = 4;
  private static final byte CSS_SQUOTE = 5;
  private static final byte CSS_LPARENS = 6;
  private static final byte CSS_RPARENS = 7;
  private static final byte CSS_ASTERISK = 8;
  private static final byte CSS_COMMA = 9;
  private static final byte CSS_HYPHEN = 10;
  private static final byte CSS_DOT = 11;
  private static final byte CSS_SOLIDUS = 12;
  private static final byte CSS_COLON = 13;
  private static final byte CSS_SEMICOLON = 14;
  private static final byte CSS_AT = 15;
  private static final byte CSS_REV_SOLIDUS = 16;
  private static final byte CSS_LCURLY = 17;
  private static final byte CSS_RCURLY = 18;
  private static final byte CSS_DIGIT = 19;
  private static final byte CSS_ALPHA = 20;
  private static final byte CSS_UNDERLINE = 21;
  private static final byte CSS_NON_ASCII = 22;
  private static final byte CSS_EOF = 23;

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
    table['"'] = CSS_DQUOTE;
    table['#'] = CSS_HASH;
    table['%'] = CSS_PERCENT;
    table['\''] = CSS_SQUOTE;
    table['('] = CSS_LPARENS;
    table[')'] = CSS_RPARENS;
    table['*'] = CSS_ASTERISK;
    table[','] = CSS_COMMA;
    table['-'] = CSS_HYPHEN;
    table['.'] = CSS_DOT;
    table['/'] = CSS_SOLIDUS;
    table[':'] = CSS_COLON;
    table[';'] = CSS_SEMICOLON;
    table['@'] = CSS_AT;
    table['{'] = CSS_LCURLY;
    table['}'] = CSS_RCURLY;
    table['\\'] = CSS_REV_SOLIDUS;
    table['_'] = CSS_UNDERLINE;

    // alphanumeric
    Ascii.fill(table, Ascii.digit(), CSS_DIGIT);
    Ascii.fill(table, Ascii.alphaLower(), CSS_ALPHA);
    Ascii.fill(table, Ascii.alphaUpper(), CSS_ALPHA);

    CSS = table;
  }

  static final class CssParser {
    private static final String EOF_DECLS = "EOF while parsing rule declarations";
    private static final String EOF_SEL = "EOF while parsing a CSS selector";
    private static final String UNSUPPORTED_ESCAPE = "Escape sequences are currently not supported";
    private static final String UNSUPPORTED_NON_ASCII = "Non ASCII characters are currently not supported";

    private char c;

    private int cursor, idx;

    private List<String> selectors;

    private String text;

    CssParser() {}

    CssParser(String text) {
      this.text = text;
    }

    public final void set(String value) {
      cursor = idx = 0;

      text = value;
    }

    public final List<Top> parse() {
      final List<Top> result;
      result = new ArrayList<>();

      while (hasNext()) {
        final byte next;
        next = next();

        switch (next) {
          case CSS_WS -> {}

          case CSS_SOLIDUS -> comment();

          case CSS_AT -> result.add(
              at()
          );

          default -> result.add(
              styleRule(next)
          );
        }
      }

      return result;
    }

    private Top at() {
      throw new UnsupportedOperationException("Implement me");
    }

    private void comment() {
      final int start;
      start = idx;

      if (next(CSS_ASTERISK)) {
        comment0(start);
      } else {
        malformed(start);
      }
    }

    private void comment0(int start) {
      boolean asterisk;
      asterisk = false;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_ASTERISK) {
          asterisk = true;

          break;
        }
      }

      if (asterisk) {
        comment1(start);
      } else {
        malformed(start);
      }
    }

    private void comment1(int start) {
      if (next(CSS_SOLIDUS)) {
        return;
      } else {
        cursor--;

        comment0(start);
      }
    }

    private void malformed(int start) {
      throw new UnsupportedOperationException("Implement me");
    }

    private String selector(byte next) {
      if (selectors == null) {
        selectors = new ArrayList<>();
      } else {
        selectors.clear();
      }

      final String first;
      first = sel(next);

      selectors.add(first);

      return selector0(first);
    }

    private String selector0(String first) {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_EOF -> throw error(EOF_SEL);

        case CSS_LCURLY -> selectors.stream().collect(Collectors.joining(", "));

        default -> throw new UnsupportedOperationException("Implement me");
      };
    }

    private String sel(byte next) {
      final int start;
      start = idx;

      return switch (next) {
        case CSS_COLON -> selPseudo(start);

        case CSS_ALPHA -> selElement(start);

        default -> throw new UnsupportedOperationException("Implement me");
      };
    }

    private String selElement(int start) {
      throw new UnsupportedOperationException("Implement me");
    }

    private String selPseudo(int start) {
      return switch (nextEof()) {
        case CSS_HYPHEN, CSS_ALPHA -> selPseudoClass(start);

        case CSS_COLON -> selPseudoElemen(start);

        default -> throw error("Invalid pseudo CSS selector");
      };
    }

    private String selPseudoClass(int start) {
      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      return switch (next) {
        case CSS_EOF -> throw error(EOF_SEL);

        case CSS_WS -> text.substring(start, idx);

        case CSS_COMMA, CSS_LCURLY -> text.substring(start, cursor--);

        default -> throw error("Invalid CSS selector");
      };
    }

    private String selPseudoElemen(int start) {
      throw new UnsupportedOperationException("Implement me");
    }

    private List<Stmt> stmts() {
      final List<Stmt> stmts;
      stmts = new ArrayList<>();

      loop: while (hasNext()) {
        final byte next;
        next = next();

        switch (next) {
          case CSS_WS -> {}

          case CSS_SOLIDUS -> comment();

          case CSS_RCURLY -> {
            break loop;
          }

          default -> stmts.add(
              stmt(next)
          );
        }
      }

      return stmts;
    }

    private Stmt stmt(byte next) {
      return switch (next) {
        case CSS_HYPHEN -> decl();

        default -> throw new UnsupportedOperationException("Implement me :: next=" + next);
      };
    }

    private StyleRule styleRule(byte next) {
      final String selector;
      selector = selector(next);

      final List<Stmt> stmts;
      stmts = stmts();

      return new StyleRule(selector, stmts);
    }

    public final List<Decl> parseDecls() {
      final List<Decl> decls;
      decls = new ArrayList<>();

      while (true) {
        final byte next;
        next = whileNext(CSS_WS);

        if (next == CSS_EOF) {
          break;
        }

        final Decl decl;
        decl = switch (next) {
          case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA -> decl();

          default -> throw error("Expected a CSS property declaration");
        };

        decls.add(decl);
      }

      return decls;
    }

    private Decl decl() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_DIGIT, CSS_ALPHA);

      return switch (next) {
        case CSS_WS -> {
          final String name;
          name = text.substring(start, idx);

          yield declColon(name);
        }

        case CSS_ASTERISK ->

        {
          final String name;
          name = text.substring(start, cursor);

          yield declColon(name);
        }

        case CSS_COLON ->

        {
          final String name;
          name = text.substring(start, idx);

          yield declValue(name);
        }

        case CSS_REV_SOLIDUS -> throw error(UNSUPPORTED_ESCAPE);

        case CSS_NON_ASCII -> throw error(UNSUPPORTED_NON_ASCII);

        case CSS_EOF -> throw error(EOF_DECLS);

        default -> throw error("Invalid CSS property name");
      };
    }

    private Decl declColon(String name) {
      final byte next;
      next = whileNext(CSS_WS);

      if (next != CSS_COLON) {
        throw error("Expected ':' after a CSS property name");
      }

      return declValue(name);
    }

    private Decl declValue(String name) {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_SEMICOLON, CSS_RCURLY -> CssEngine2.decl(name, List.of());

        case CSS_EOF -> throw error(EOF_DECLS);

        default -> {
          cursor--;
          yield declValue0(name);
        }

      };
    }

    private Decl declValue0(String name) {
      final List<Value> values;
      values = new ArrayList<>();

      values(values, CSS_SEMICOLON);

      return CssEngine2.decl(name, values);
    }

    public final String parseIden() {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_UNDERLINE, CSS_ALPHA -> iden();

        case CSS_REV_SOLIDUS -> throw error(UNSUPPORTED_ESCAPE);

        case CSS_NON_ASCII -> throw error(UNSUPPORTED_NON_ASCII);

        case CSS_EOF -> throw error("Empty or blank identifier");

        default -> throw error("Invalid CSS identifier");
      };
    }

    private String iden() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_DIGIT, CSS_ALPHA);

      return switch (next) {
        case CSS_EOF -> iden(start, cursor);

        case CSS_WS -> idenTrim(start);

        case CSS_REV_SOLIDUS -> throw error(UNSUPPORTED_ESCAPE);

        case CSS_NON_ASCII -> throw error(UNSUPPORTED_NON_ASCII);

        default -> throw error("Invalid CSS identifier");
      };
    }

    private String iden(int idx0, int idx1) {
      return text.substring(idx0, idx1);
    }

    private String idenTrim(int start) {
      final int end;
      end = idx;

      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_EOF -> iden(start, end);

        default -> throw error("Invalid CSS identifier");
      };
    }

    public final List<Value> parseValues() {
      final List<Value> result;
      result = new ArrayList<>();

      values(result, CSS_EOF);

      return result;
    }

    private void values(List<Value> result, byte stopIf) {
      while (true) {
        final byte next;
        next = whileNext(CSS_WS);

        if (next == stopIf) {
          return;
        }

        result.add(
            value(next)
        );
      }
    }

    private Value value(byte next) {
      return switch (next) {
        case CSS_DQUOTE, CSS_SQUOTE -> valueString(next);

        case CSS_HASH -> valueHexColor();

        case CSS_COMMA -> valueSep(Sep.COMMA);

        case CSS_HYPHEN -> valueHyphen();

        case CSS_DOT -> valueDot();

        case CSS_ALPHA, CSS_UNDERLINE -> valueIden(idx);

        case CSS_DIGIT -> valueDigit(idx);

        default -> throw error("Invalid CSS declaration value");
      };
    }

    private Value valueDigit(final int start) {
      final byte next;
      next = whileNext(CSS_DIGIT);

      return switch (next) {
        case CSS_EOF -> valueInteger(start, cursor);

        case CSS_WS -> valueInteger(start, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valueInteger(start, --cursor);

        case CSS_PERCENT -> valuePerc(start);

        case CSS_DOT -> valueDouble(start);

        case CSS_ALPHA -> valueLength(start);

        default -> throw error("Expected a CSS numeric value");
      };
    }

    private Value valueDot() {
      final int dot;
      dot = idx;

      return switch (nextEof()) {
        case CSS_DIGIT -> valueDouble(dot);

        default -> throw error("Expected a CSS fractional value");
      };
    }

    private Value valueDouble(int start) {
      final byte next;
      next = whileNext(CSS_DIGIT);

      return switch (next) {
        case CSS_EOF -> valueDouble(start, cursor);

        case CSS_WS -> valueDouble(start, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valueDouble(start, --cursor);

        case CSS_PERCENT -> valuePerc(start);

        case CSS_ALPHA -> valueLength(start);

        default -> throw error("Expected a CSS <double> value");
      };
    }

    private Value valueDouble(int idx0, int idx1) {
      final String s;
      s = text.substring(idx0, idx1);

      return new Number(s);
    }

    private Value valueHexColor() {
      final int start;
      start = idx;

      final byte next;
      next = whileHexDigit();

      return switch (next) {
        case CSS_EOF -> valueHexColor(start, cursor);

        case CSS_WS -> valueHexColor(start, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valueHexColor(start, --cursor);

        default -> throw error("Expected a CSS <hex-color> value");
      };
    }

    private Value valueFun(String name) {
      final List<Value> args;
      args = new ArrayList<>();

      values(args, CSS_RPARENS);

      return new Fun(name, args);
    }

    private Value valueHexColor(int idx0, int idx1) {
      final String v;
      v = text.substring(idx0, idx1);

      return new Tok(v);
    }

    private Value valueIden(final int start) {
      final byte next;
      next = whileNext(CSS_ALPHA, CSS_DIGIT, CSS_HYPHEN, CSS_UNDERLINE);

      return switch (next) {
        case CSS_EOF -> valueKeyword(start, cursor);

        case CSS_WS -> valueKeyword(start, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valueKeyword(start, --cursor);

        case CSS_LPARENS -> {
          final String name;
          name = text.substring(start, idx);

          yield valueFun(name);
        }

        default -> throw error("Expected CSS identifier");
      };

    }

    private Value valueInteger(int idx0, int idx1) {
      final String s;
      s = text.substring(idx0, idx1);

      return new Number(s);
    }

    private Value valueHyphen() {
      final int hyphen;
      hyphen = idx;

      return switch (nextEof()) {
        case CSS_DIGIT -> valueDigit(hyphen);

        case CSS_ALPHA, CSS_HYPHEN, CSS_UNDERLINE -> valueIden(hyphen);

        default -> throw error("Expected a CSS <iden> or a negative numeric value");
      };
    }

    private Value valueKeyword(int idx0, int idx1) {
      final String s;
      s = text.substring(idx0, idx1);

      return new Tok(s);
    }

    private Value valueLength(int start) {
      final int unit;
      unit = idx;

      final byte next;
      next = whileNext(CSS_ALPHA);

      return switch (next) {
        case CSS_EOF -> valueLength(start, unit, cursor);

        case CSS_WS -> valueLength(start, unit, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valueLength(start, unit, --cursor);

        default -> throw error("Expected a CSS <length> value");
      };
    }

    private Value valueLength(int number, int unit, int end) {
      final String s;
      s = text.substring(number, end);

      return new Tok(s);
    }

    private Value valuePerc(int first) {
      return switch (nextEof()) {
        case CSS_EOF -> valuePerc(first, cursor);

        case CSS_WS -> valuePerc(first, idx);

        case CSS_COMMA, CSS_SEMICOLON, CSS_RPARENS -> valuePerc(first, --cursor);

        default -> throw error("Expected a CSS <percentage> value");
      };
    }

    private Value valuePerc(int idx0, int idx1) {
      final String s;
      s = text.substring(idx0, idx1);

      return new Tok(s);
    }

    private Value valueSep(Sep v) {
      whileNext(CSS_WS);

      cursor--;

      return v;
    }

    private Value valueString(byte quote) {
      final int start;
      start = idx;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next != quote) {
          continue;
        }

        final char prev;
        prev = text.charAt(idx - 1);

        if (prev == '\\') {
          continue;
        }

        return valueString(start, cursor);
      }

      throw error("Unclosed string");
    }

    private Value valueString(int idx0, int idx1) {
      final String s;
      s = text.substring(idx0, idx1);

      return new Tok(s);
    }

    private IllegalArgumentException error(String message) {
      return new IllegalArgumentException(message);
    }

    private boolean hasNext() {
      return cursor < text.length();
    }

    private byte next() {
      idx = cursor++;

      c = text.charAt(idx);

      return c < 128 ? CSS[c] : CSS_NON_ASCII;
    }

    private boolean next(byte test) {
      if (hasNext()) {
        return next() == test;
      } else {
        return CSS_EOF == test;
      }
    }

    private byte nextEof() {
      if (hasNext()) {
        return next();
      } else {
        return CSS_EOF;
      }
    }

    private byte whileHexDigit() {
      while (hasNext()) {
        final byte next;
        next = next();

        if (HexFormat.isHexDigit(c)) {
          continue;
        }

        return next;
      }

      return CSS_EOF;
    }

    private byte whileNext(byte c0) {
      while (hasNext()) {
        final byte next;
        next = next();

        if (next == c0) {
          continue;
        }

        return next;
      }

      return CSS_EOF;
    }

    private byte whileNext(byte c0, byte c1, byte c2, byte c3) {
      while (hasNext()) {
        final byte next;
        next = next();

        if (next == c0) {
          continue;
        }

        if (next == c1) {
          continue;
        }

        if (next == c2) {
          continue;
        }

        if (next == c3) {
          continue;
        }

        return next;
      }

      return CSS_EOF;
    }

  }

  // ##################################################################
  // # END: CSS Parser
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
  // # BEGIN: Keyframes
  // ##################################################################

  record Keyframes(String name, List<ParsedRule> rules) implements Comparable<Keyframes> {
    @Override
    public final int compareTo(Keyframes o) {
      return name.compareTo(o.name);
    }
  }

  static Keyframes keyframes(String name, List<ParsedRule> rules) {
    return new Keyframes(name, rules);
  }

  record ParsedRule(String selector, List<Decl> decls) {}

  static ParsedRule parsedRule(String s, List<Decl> d) {
    return new ParsedRule(s, d);
  }

  static final class KeyframesBuilder implements Css.Engine.Keyframes {

    private final String name;

    private final List<ParsedRule> rules = new ArrayList<>();

    KeyframesBuilder(String name) {
      this.name = name;
    }

    @Override
    public final void from(String value) {
      add("from", value);
    }

    @Override
    public final void to(String value) {
      add("to", value);
    }

    @Override
    public final void perc(int offset, String value) {
      if (offset < 0 || offset > 100) {
        throw new IllegalArgumentException("Percentage offset value must be 0% <= value <= 100%");
      }

      add(offset + "%", value);
    }

    private void add(String selector, String value) {
      value = Objects.requireNonNull(value, "value == null");

      final CssParser declsParser;
      declsParser = new CssParser(value);

      final List<Decl> decls;
      decls = declsParser.parseDecls();

      final ParsedRule rule;
      rule = new ParsedRule(selector, decls);

      rules.add(rule);
    }

    final Keyframes build() {
      return new Keyframes(name, rules);
    }

  }

  // ##################################################################
  // # END: Keyframes
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configuring
  // ##################################################################

  static final class Configuring implements Css.Engine {

    private final Note.Ref2<Decl, Decl> $replaced = Note.Ref2.create(getClass(), "REP", Note.INFO);

    private final List<ParsedRule> components = new ArrayList<>();

    private final List<List<Decl>> fontFaces = new ArrayList<>();

    private final Map<String, CssEngine2.Keyframes> keyframes = new HashMap<>();

    private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

    private Set<Class<?>> scanClasses = Set.of();

    private Set<Path> scanDirectories = Set.of();

    private Set<Class<?>> scanJars = Set.of();

    private final Map<List<String>, List<Stmt>> sections = new LinkedHashMap<>();

    private final String systemBase;

    private final Map<List<String>, Map<String, Decl>> themeProps = new LinkedHashMap<>();

    private final Map<String, Variant> variants = new HashMap<>();

    Configuring(System system) {
      this.systemBase = system.base;

      // parse system theme
      final CssParser parser;
      parser = new CssParser(system.theme);

      final List<Top> parsed;
      parsed = parser.parse();

      for (Top top : parsed) {
        switch (top) {
          case StyleRule(String selector, List<Stmt> stmts) -> {
            if (!":root".equals(selector)) {
              throw new IllegalArgumentException(
                  "The system theme must only contain the :root selector"
              );
            }

            final List<String> root;
            root = List.of(selector);

            final List<Stmt> section;
            section = sections.computeIfAbsent(root, key -> new ArrayList<>());

            for (Stmt stmt : stmts) {
              section.add(stmt);

              switch (stmt) {
                case Decl decl -> {
                  final String property;
                  property = decl.property;

                  if (!property.startsWith("--")) {
                    throw decl.invalid("The system theme must only contain custom properties");
                  }

                  final Map<String, Decl> values;
                  values = themeProps.computeIfAbsent(root, key -> new LinkedHashMap<>());

                  final Decl existing;
                  existing = values.put(property, decl);

                  if (existing != null) {
                    decl.invalid("Duplicate property definition");
                  }
                }

                case StyleRule nested -> throw new IllegalArgumentException(
                    "The system theme must not contain nested statements"
                );
              }
            }
          }
        }
      }

      variants.putAll(system.variants);
    }

    public final void generate(Appendable out) throws IOException {
      final Config config;
      config = configure();

      final CssEngine2 engine;
      engine = new CssEngine2(config);

      engine.generate(out);
    }

    // ##################################################################
    // # BEGIN: Configuring: Public API
    // ##################################################################

    @Override
    public final void noteSink(Note.Sink value) {
      noteSink = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void scanClass(Class<?> value) {
      final Class<?> c;
      c = Objects.requireNonNull(value, "value");

      if (scanClasses.isEmpty()) {
        scanClasses = new HashSet<>();
      }

      scanClasses.add(c);
    }

    @Override
    public final void scanDirectory(Path value) {
      final Path p;
      p = Objects.requireNonNull(value, "value == null");

      if (scanDirectories.isEmpty()) {
        scanDirectories = new HashSet<>();
      }

      scanDirectories.add(p);
    }

    @Override
    public final void scanJarFileOf(Class<?> value) {
      final Class<?> c;
      c = Objects.requireNonNull(value, "value == null");

      if (scanJars.isEmpty()) {
        scanJars = new HashSet<>();
      }

      scanJars.add(c);
    }

    @Override
    public final void theme(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      final CssParser parser;
      parser = new CssParser(text);

      final List<Top> parsed;
      parsed = parser.parse();

      for (Top top : parsed) {
        switch (top) {
          case StyleRule rule -> theme(List.of(), rule);
        }
      }
    }

    private void theme(List<String> previous, StyleRule rule) {
      final List<String> list;
      list = new ArrayList<>(previous);

      list.add(rule.selector);

      final List<String> selector;
      selector = List.copyOf(list);

      final List<Stmt> section;
      section = sections.computeIfAbsent(selector, key -> new ArrayList<>());

      final Map<String, Decl> props;
      props = themeProps.computeIfAbsent(selector, key -> new LinkedHashMap<>());

      for (Stmt stmt : rule.stmts) {
        section.add(stmt);

        switch (stmt) {
          case Decl decl -> {
            final String property;
            property = decl.property;

            if (!property.startsWith("--")) {
              // always emit a non-custom-prop
              decl.mark();

              continue;
            }

            final Decl existing;
            existing = props.put(property, decl);

            if (existing != null) {
              existing.replaced();

              noteSink.send($replaced, existing, decl);
            }
          }

          case StyleRule nested -> theme(selector, nested);
        }
      }
    }

    @Override
    public final void component(String selector, String value) {
      selector = Objects.requireNonNull(selector, "selector == null");
      selector = selector.strip();

      value = Objects.requireNonNull(value, "value == null");

      final CssParser declsParser;
      declsParser = new CssParser(value);

      final List<Decl> decls;
      decls = declsParser.parseDecls();

      final ParsedRule rule;
      rule = new ParsedRule(selector, decls);

      components.add(rule);
    }

    @Override
    public final void keyframes(String name, Consumer<? super Css.Engine.Keyframes> frames) {
      name = Objects.requireNonNull(name, "name == null");

      final CssParser nameParser;
      nameParser = new CssParser(name);

      name = nameParser.parseIden();

      final KeyframesBuilder builder;
      builder = new KeyframesBuilder(name);

      frames.accept(builder);

      final CssEngine2.Keyframes kf;
      kf = builder.build();

      final CssEngine2.Keyframes existing;
      existing = keyframes.put(kf.name, kf);

      if (existing != null) {
        throw new IllegalArgumentException(
            "Duplicate @keyframes named " + kf.name
        );
      }
    }

    private static final Set<String> FONT_FACE_PROPS = Set.of(
        "ascent-override",
        "descent-override",
        "font-display",
        "font-family",
        "font-stretch",
        "font-style",
        "font-weight",
        "font-feature-settings",
        "font-variant-settings",
        "line-gap-override",
        "size-adjust",
        "src",
        "unicode-range"
    );

    @Override
    public final void fontFace(String value) {
      value = Objects.requireNonNull(value, "value == null");

      final CssParser declsParser;
      declsParser = new CssParser(value);

      final List<Decl> decls;
      decls = declsParser.parseDecls();

      for (Decl decl : decls) {
        final String property;
        property = decl.property;

        if (!FONT_FACE_PROPS.contains(property)) {
          decl.invalid("Invalid @font-face property");
        }
      }

      fontFaces.add(decls);
    }

    public final Config configure() {
      // collect all props
      final Map<String, Decl> properties;
      properties = new LinkedHashMap<>();

      for (Map.Entry<List<String>, Map<String, Decl>> outer : themeProps.entrySet()) {
        final List<String> selector;
        selector = outer.getKey();

        final Map<String, Decl> props;
        props = outer.getValue();

        final List<Decl> decls;
        decls = new ArrayList<>();

        for (Map.Entry<String, Decl> inner : props.entrySet()) {
          final String propName;
          propName = inner.getKey();

          final Decl decl;
          decl = inner.getValue();

          properties.merge(propName, decl, (oldValue, value) -> oldValue.append(value));

          decls.add(decl);
        }
      }

      // collect breakpoints
      final List<String> root;
      root = List.of(":root");

      final Map<String, Decl> rootProps;
      rootProps = themeProps.getOrDefault(root, Map.of());

      for (Decl decl : rootProps.values()) {
        final String property;
        property = decl.property;

        final String prefix;
        prefix = "--breakpoint-";

        if (!property.startsWith(prefix)) {
          continue;
        }

        final int beginIndex;
        beginIndex = prefix.length();

        final String id;
        id = property.substring(beginIndex);

        final List<Value> values;
        values = decl.values;

        final int size;
        size = values.size();

        if (size != 1) {
          // TODO log
          continue;
        }

        final Value value;
        value = values.get(0);

        if (!(value instanceof Tok tok)) {
          // TODO log
          continue;
        }

        final Variant variant;
        variant = simple("@media (min-width: " + tok.v + ")");

        variant(id, variant);
      }

      // collect all sections
      final List<Section> sections;
      sections = new ArrayList<>();

      for (Map.Entry<List<String>, List<Stmt>> entry : this.sections.entrySet()) {
        final List<String> selector;
        selector = entry.getKey();

        final List<Stmt> stmts;
        stmts = entry.getValue();

        final Section section;
        section = new Section(selector, stmts);

        sections.add(section);
      }

      // mark all props found in base
      final CssParser baseParser;
      baseParser = new CssParser(systemBase);

      final List<Top> base;
      base = baseParser.parse();

      for (Top top : base) {
        switch (top) {
          case StyleRule rule -> baseStyleRule(properties, rule);
        }
      }

      return new Config(
          systemBase,

          components,

          fontFaces,

          keyframes,

          noteSink,

          properties,

          scanClasses,

          scanDirectories,

          scanJars,

          sections,

          variants
      );
    }

    private void baseStyleRule(Map<String, Decl> properties, StyleRule rule) {
      for (Stmt stmt : rule.stmts) {
        switch (stmt) {
          case Decl decl -> {
            final String baseProp;
            baseProp = decl.property;

            final Decl maybe;
            maybe = properties.get(baseProp);

            if (maybe != null) {
              maybe.mark(properties);
            }
          }

          case StyleRule nested -> baseStyleRule(properties, nested);
        }
      }
    }

    // ##################################################################
    // # END: Configuring: Public API
    // ##################################################################

    private void variant(String name, Variant variant) {
      final Variant maybeExisting;
      maybeExisting = variants.put(name, variant);

      if (maybeExisting == null) {
        return;
      }

      // TODO restore existing and log?
    }

  }

  // ##################################################################
  // # END: Configuring
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configured
  // ##################################################################

  record Config(

      String base,

      List<ParsedRule> components,

      List<List<Decl>> fontFaces,

      Map<String, Keyframes> keyframes,

      Note.Sink noteSink,

      Map<String, Decl> properties,

      Set<Class<?>> scanClasses,

      Set<Path> scanDirectories,

      Set<Class<?>> scanJars,

      List<Section> sections,

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
  // # BEGIN: ClassNameFormat
  // ##################################################################

  private static final byte[] CNF;

  private static final byte CNF_ASCII = 0;
  private static final byte CNF_NULL = 1;
  private static final byte CNF_CONTROL = 2;
  private static final byte CNF_HYPHEN = 3;
  private static final byte CNF_DIGIT = 4;
  private static final byte CNF_ALPHA = 5;
  private static final byte CNF_UNDERLINE = 6;
  private static final byte CNF_NON_ASCII = 7;
  private static final byte CNF_EOF = 8;

  static {
    final byte[] table;
    table = new byte[128];

    // start with ASCII

    table[0] = CNF_NULL;

    for (int idx = '\u0001'; idx <= '\u001F'; idx++) {
      table[idx] = CNF_CONTROL;
    }

    table['-'] = CNF_HYPHEN;

    Ascii.fill(table, Ascii.digit(), CNF_DIGIT);

    Ascii.fill(table, Ascii.alphaUpper(), CNF_ALPHA);

    table['_'] = CNF_UNDERLINE;

    Ascii.fill(table, Ascii.alphaLower(), CNF_ALPHA);

    table['\u007F'] = CNF_CONTROL;

    CNF = table;
  }

  static final class ClassNameFormat {

    private char c;

    private int cursor;

    private final StringBuilder sb = new StringBuilder();

    private String src;

    public final String format(String source) {
      src = Objects.requireNonNull(source, "source == null");

      cursor = 0;

      sb.setLength(0);

      sb.append('.');

      final byte first;
      first = nextTest();

      return switch (first) {
        case CNF_EOF -> throw new IllegalArgumentException("Cannot format an empty string");

        case CNF_DIGIT -> formatStartsWithDigit();

        case CNF_HYPHEN -> formatStartsWithHyphen();

        default -> formatRest(first);
      };
    }

    private String formatStartsWithDigit() {
      escapeAsCodePoint();

      return formatRest(nextTest());
    }

    private String formatStartsWithHyphen() {
      final byte test;
      test = nextTest();

      return switch (test) {
        case CNF_EOF -> {
          sb.append('\\');

          sb.append('-');

          yield sb.toString();
        }

        case CNF_DIGIT -> {
          sb.append('-');
          escapeAsCodePoint();
          yield formatRest(nextTest());
        }

        default ->

        {
          sb.append('-');

          yield formatRest(test);
        }
      };

    }

    private String formatRest(byte test) {
      while (true) {
        switch (test) {
          case CNF_EOF -> {
            return sb.toString();
          }

          case CNF_NULL -> sb.append('\uFFFD');

          case CNF_CONTROL -> escapeAsCodePoint();

          case CNF_ASCII -> { sb.append('\\'); sb.append(c); }

          default -> sb.append(c);
        }

        test = nextTest();
      }
    }

    private void escapeAsCodePoint() {
      sb.append("\\");

      sb.append(Integer.toHexString(c));

      sb.append(' ');
    }

    private byte nextTest() {
      if (cursor < src.length()) {
        c = src.charAt(cursor++);

        return c < 128 ? CNF[c] : CNF_NON_ASCII;
      } else {
        return CNF_EOF;
      }
    }

  }

  // ##################################################################
  // # END: ClassNameFormat
  // ##################################################################

  // ##################################################################
  // # BEGIN: Utility
  // ##################################################################

  @FunctionalInterface
  interface Slugs {
    void consume(String className, List<String> slugs);
  }

  record Utility(List<Variant> variants, String className, String property, List<Value> values) {}

  static Utility utility(List<Variant> variants, String className, String property, List<Value> values) {
    return new Utility(variants, className, property, values);
  }
  static Utility utility(List<Variant> variants, String className, String property, Value... values) {
    return new Utility(variants, className, property, List.of(values));
  }

  static final class Proc implements Slugs {

    final ClassNameFormat classNameFormat = new ClassNameFormat();

    final CssParser cssParser = new CssParser();

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

      final String classNameFormatted;
      classNameFormatted = classNameFormat.format(className);

      cssParser.set(propValue);

      final List<Value> values;
      values = cssParser.parseValues();

      final Utility utility;
      utility = new Utility(vars, classNameFormatted, propName, values);

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

  record Rule(String className, List<Variant> variants, String property, String value)
      implements
      Comparable<Rule> {

    @Override
    public final int compareTo(Rule o) {
      final int p;
      p = property.compareTo(o.property);

      if (p != 0) {
        return p;
      }

      final int v;
      v = value.compareTo(o.value);

      if (v != 0) {
        return v;
      }

      return className.compareTo(o.className);
    }

  }

  static Rule rule(String className, List<Variant> variants, String property, String value) {
    return new Rule(className, variants, property, value);
  }

  record Ctx(
      List<Keyframes> keyframes,
      List<Rule> rules,
      List<Section> sections
  ) {}

  static final class Gen {

    static final Set<String> KEYFRAMES_PROPERTIES = Set.of(
        "animation",
        "animation-name"
    );

    final Map<String, Keyframes> keyframes;

    final Map<String, Keyframes> keyframesMarked = new HashMap<>();

    final Map<String, Decl> properties;

    final List<Rule> rules = new ArrayList<>();

    final List<Section> sections;

    private final StringBuilder sb = new StringBuilder();

    final List<Utility> utilities;

    Gen(Map<String, Keyframes> keyframes, Map<String, Decl> properties, List<Section> sections, List<Utility> utilities) {
      this.keyframes = keyframes;

      this.properties = properties;

      this.sections = sections;

      this.utilities = utilities;
    }

    public final Ctx generate() {
      for (Utility utility : utilities) {
        process(utility);
      }

      rules.sort(Comparator.naturalOrder());

      return new Ctx(
          keyframesMarked.values().stream().sorted().toList(),

          List.copyOf(rules),

          themeSections()
      );
    }

    private void process(Utility utility) {
      final String className;
      className = utility.className;

      final List<Variant> variants;
      variants = utility.variants;

      final String property;
      property = utility.property;

      final List<Value> values;
      values = utility.values;

      final boolean keyframesProp;
      keyframesProp = KEYFRAMES_PROPERTIES.contains(property);

      for (Value value : values) {
        value.markProperties(properties);

        if (keyframesProp &&
            value instanceof Tok(String v) &&
            !keyframesMarked.containsKey(v)) {
          final Keyframes kf;
          kf = keyframes.get(v);

          if (kf != null) {
            keyframesMarked.put(v, kf);
          }
        }
      }

      sb.setLength(0);

      Value.formatTo(sb, values);

      final String value;
      value = sb.toString();

      final Rule rule;
      rule =

          rule(className, variants, property, value);

      rules.add(rule);
    }

    private List<Section> themeSections() {
      final List<Section> result;
      result = new ArrayList<>();

      for (Section section : sections) {
        final List<Decl> decls;
        decls = new ArrayList<>();

        final List<Decl> original;
        original = section.decls;

        for (Decl proto : original) {
          if (!proto.marked) {
            continue;
          }

          decls.add(proto);
        }

        if (decls.isEmpty()) {
          continue;
        }

        final List<String> selector;
        selector = section.selector;

        result.add(
            new Section(selector, decls)
        );
      }

      return result;
    }

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

    final void w(List<Value> values) throws IOException {
      Value.formatTo(out, values);
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

  record Section(List<String> selector, List<Stmt> stmts) {}

  static Section section(List<String> s, Stmt... stmts) { return new Section(s, List.of(stmts)); }

  static final class Theme extends Writer {

    final List<Section> sections;

    Theme(List<Section> sections) {
      this.sections = sections;
    }

    @Override
    final void write() throws IOException {
      if (sections.isEmpty()) {
        return;
      }

      wln("@layer theme {");

      level++;

      for (Section section : sections) {
        final List<String> selector;
        selector = section.selector;

        for (String part : selector) {
          indent();

          w(part);

          wln(" {");

          level++;
        }

        final List<Decl> decls;
        decls = section.decls;

        for (Decl decl : decls) {
          indent();

          w(decl.property);

          w(": ");

          w(decl.values);

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

    int cursor;

    int idx;

    final String source;

    Base(String source) {
      this.source = source;
    }

    @Override
    final void write() throws IOException {
      if (source == null || source.isBlank()) {
        return;
      }

      wln("@layer base {");

      level++;

      while (hasNext()) {
        switch (next()) {
          case CSS_WS -> {}

          case CSS_SOLIDUS -> comment();

          default -> statement();
        }
      }

      level--;

      wln('}');
    }

    private void comment() {
      final int start;
      start = idx;

      if (next(CSS_ASTERISK)) {
        comment0(start);
      } else {
        malformed(start);
      }
    }

    private void comment0(int start) {
      boolean asterisk;
      asterisk = false;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_ASTERISK) {
          asterisk = true;

          break;
        }
      }

      if (asterisk) {
        comment1(start);
      } else {
        malformed(start);
      }
    }

    private void comment1(int start) {
      if (next(CSS_SOLIDUS)) {
        return;
      } else {
        cursor--;

        comment0(start);
      }
    }

    private void malformed(int start) {
      throw new UnsupportedOperationException("Implement me");
    }

    private void statement() {
      throw new UnsupportedOperationException("Implement me");
    }

    private boolean hasNext() {
      return cursor < source.length();
    }

    private byte next() {
      idx = cursor++;

      final char c;
      c = source.charAt(idx);

      return c < 128 ? CSS[c] : CSS_NON_ASCII;
    }

    private boolean next(byte test) {
      if (hasNext()) {
        return next() == test;
      } else {
        return false;
      }
    }

    private byte whileNext(byte c0) {
      while (hasNext()) {
        final byte next;
        next = next();

        if (next == c0) {
          continue;
        }

        return next;
      }

      return CSS_EOF;
    }

  }

  // ##################################################################
  // # END: Base
  // ##################################################################

  // ##################################################################
  // # BEGIN: Components
  // ##################################################################

  static final class Components extends Writer {

    final List<ParsedRule> components;

    Components(List<ParsedRule> components) {
      this.components = components;
    }

    @Override
    final void write() throws IOException {
      if (components.isEmpty()) {
        return;
      }

      wln("@layer components {");

      level++;

      for (ParsedRule component : components) {
        indent();

        w(component.selector);

        wln(" {");

        level++;

        final List<Decl> decls;
        decls = component.decls;

        for (Decl decl : decls) {
          indent();

          w(decl.property);

          w(": ");

          w(decl.values);

          wln(';');
        }

        level--;

        indent();

        wln('}');
      }

      level--;

      wln('}');
    }

  }

  // ##################################################################
  // # END: Components
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

  // ##################################################################
  // # BEGIN: Trailer
  // ##################################################################

  static final class Trailer extends Writer {

    private final List<List<Decl>> fontFaces;
    private final List<Keyframes> keyframes;

    Trailer(List<List<Decl>> fontFaces, List<Keyframes> keyframes) {
      this.fontFaces = fontFaces;
      this.keyframes = keyframes;
    }

    @Override
    final void write() throws IOException {
      for (Keyframes kf : keyframes) {
        w("@keyframes ");

        w(kf.name);

        wln(" {");

        level++;

        final List<ParsedRule> rules;
        rules = kf.rules;

        for (ParsedRule rule : rules) {
          indent();

          w(rule.selector);

          wln(" {");

          level++;

          final List<Decl> decls;
          decls = rule.decls;

          for (Decl decl : decls) {
            indent();

            w(decl.property);

            w(": ");

            w(decl.values);

            wln(';');
          }

          level--;

          indent();

          wln('}');
        }

        level--;

        wln('}');
      }

      for (List<Decl> face : fontFaces) {
        if (face.isEmpty()) {
          continue;
        }

        wln("@font-face {");

        level++;

        for (Decl decl : face) {
          indent();

          w(decl.property);

          w(": ");

          w(decl.values);

          wln(';');
        }

        level--;

        wln('}');
      }
    }

  }

  // ##################################################################
  // # END: Trailer
  // ##################################################################

}