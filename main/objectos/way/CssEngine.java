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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

final class CssEngine implements Css.StyleSheet {

  static Css.StyleSheet create0(Consumer<? super Css.StyleSheet.Options> options) {
    final Configuring configuring;
    configuring = new Configuring();

    options.accept(configuring);

    final Config config;
    config = configuring.configure();

    return new CssEngine(config);
  }

  @Override
  public final String contentType() {
    return "text/css; charset=utf-8";
  }

  @Override
  public final Charset charset() {
    return StandardCharsets.UTF_8;
  }

  @Override
  public final String generate() {
    try {
      final StringBuilder out;
      out = new StringBuilder();

      generate(out);

      return out.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder does not throw IOException", e);
    }
  }

  @Override
  public final void writeTo(Appendable out) throws IOException {
    generate(out);
  }

  // ##################################################################
  // # BEGIN: Execute
  // ##################################################################

  private final Config config;

  CssEngine(Config config) {
    this.config = config;
  }

  public final void generate(Appendable out) throws IOException {
    final Set<String> cssProperties;
    cssProperties = config.cssProperties;

    final Note.Sink noteSink;
    noteSink = config.noteSink;

    final Map<String, Variant> variants;
    variants = config.variants;

    final Proc proc;
    proc = new Proc(cssProperties, noteSink, variants);

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

    final Properties properties;
    properties = config.properties;

    final List<Section> protoSections;
    protoSections = config.sections;

    final List<Utility> utils;
    utils = proc.utilities;

    final Gen gen;
    gen = new Gen(keyframes, properties, protoSections);

    final Ctx ctx;
    ctx = gen.generate(utils);

    final List<Section> sections;
    sections = ctx.sections;

    final Theme theme;
    theme = new Theme(sections);

    theme.write(out);

    final List<Top> baseSource;
    baseSource = config.base;

    final Base base;
    base = new Base(baseSource);

    base.write(out);

    final List<Block> components;
    components = config.components;

    final Components compWriter;
    compWriter = new Components(components);

    compWriter.write(out);

    final List<Utility> rules;
    rules = ctx.utilities;

    final Utilities utilities;
    utilities = new Utilities(rules);

    utilities.write(out);

    final List<FontFace> fontFaces;
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
  // # BEGIN: Syntax
  // ##################################################################

  /// Represents an at-rule.
  sealed interface At {
    Stmt asStmt();

    void asTop(List<Top> result);
  }

  static final class Decl implements Stmt {
    private boolean marked;
    private Decl next;
    final String property;
    private boolean replaced;
    final String value;

    Decl(String property, String value) {
      this.property = property;

      this.value = value;
    }

    @Override
    public final boolean equals(Object obj) {
      return obj == this || obj instanceof Decl that
          && marked == that.marked
          && property.equals(that.property)
          && replaced == that.replaced
          && value.equals(that.value);
    }

    @Override
    public final String toString() {
      return property + ": " + value + ";";
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
      return new IllegalArgumentException(msg + "\n\t" + property + ": " + value);
    }

    final Decl mark() {
      if (!replaced) {
        marked = true;

        return this;
      }

      if (next != null) {
        next.mark();
      }

      return this;
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

  static Decl decl(String p, String v) { return new Decl(p, v); }

  sealed interface Stmt {}

  /// A top-level CSS statement
  sealed interface Top {}

  record Block(String selector, List<Stmt> stmts) implements At, Stmt, Top {
    @Override
    public final Stmt asStmt() {
      return this;
    }

    @Override
    public final void asTop(List<Top> result) {
      result.add(this);
    }
  }

  static Block block(String sel, Stmt... stmts) { return new Block(sel, List.of(stmts)); }

  record FontFace(List<Decl> decls) implements At, Top {
    @Override
    public final Stmt asStmt() {
      throw new IllegalArgumentException(
          "Cannot nest a @font-face at-rule"
      );
    }

    @Override
    public final void asTop(List<Top> result) {
      result.add(this);
    }

    final boolean isEmpty() {
      return decls.isEmpty();
    }
  }

  static FontFace fontFace(Decl... decls) { return new FontFace(List.of(decls)); }

  record Keyframes(String name, List<Block> rules) implements At, Top {
    @Override
    public final Stmt asStmt() {
      throw new IllegalArgumentException(
          "Cannot nest a @keyframes at-rule"
      );
    }

    @Override
    public final void asTop(List<Top> result) {
      result.add(this);
    }
  }

  static Keyframes keyframes(String n, Block... rules) { return new Keyframes(n, List.of(rules)); }

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
  private static final byte CSS_LSQUARE = 16;
  private static final byte CSS_REV_SOLIDUS = 17;
  private static final byte CSS_RSQUARE = 18;
  private static final byte CSS_LCURLY = 19;
  private static final byte CSS_RCURLY = 20;
  private static final byte CSS_DIGIT = 21;
  private static final byte CSS_ALPHA = 22;
  private static final byte CSS_UNDERLINE = 23;
  private static final byte CSS_NON_ASCII = 24;
  private static final byte CSS_EOF = 25;
  private static final byte CSS_EXCLAMATION = 26;
  private static final byte CSS_PLUS = 27;
  private static final byte CSS_AMPERSAND = 28;

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
    table['!'] = CSS_EXCLAMATION;
    table['"'] = CSS_DQUOTE;
    table['#'] = CSS_HASH;
    table['%'] = CSS_PERCENT;
    table['&'] = CSS_AMPERSAND;
    table['\''] = CSS_SQUOTE;
    table['('] = CSS_LPARENS;
    table[')'] = CSS_RPARENS;
    table['*'] = CSS_ASTERISK;
    table['+'] = CSS_PLUS;
    table[','] = CSS_COMMA;
    table['-'] = CSS_HYPHEN;
    table['.'] = CSS_DOT;
    table['/'] = CSS_SOLIDUS;
    table[':'] = CSS_COLON;
    table[';'] = CSS_SEMICOLON;
    table['@'] = CSS_AT;
    table['{'] = CSS_LCURLY;
    table['}'] = CSS_RCURLY;
    table['['] = CSS_LSQUARE;
    table['\\'] = CSS_REV_SOLIDUS;
    table[']'] = CSS_RSQUARE;
    table['_'] = CSS_UNDERLINE;

    // alphanumeric
    Ascii.fill(table, Ascii.digit(), CSS_DIGIT);
    Ascii.fill(table, Ascii.alphaLower(), CSS_ALPHA);
    Ascii.fill(table, Ascii.alphaUpper(), CSS_ALPHA);

    CSS = table;
  }

  static final class Syntax {
    private static final String EOF_AT = "EOF while parsing a CSS at-rule";
    private static final String EOF_DECLS = "EOF while parsing rule declarations";
    private static final String EOF_SEL = "EOF while parsing a CSS selector";
    private static final String UNSUPPORTED_ESCAPE = "Escape sequences are currently not supported";
    private static final String UNSUPPORTED_NON_ASCII = "Non ASCII characters are currently not supported";

    private char c;

    private int cursor, idx;

    private String text;

    Syntax() {}

    private enum Dim {
      INT,
      DOT,
      DBL,
      UNIT;
    }

    public final boolean dimension() {
      Dim state = Dim.INT;

      while (hasNext()) {
        final byte next;
        next = next();

        switch (state) {
          case INT -> {
            switch (next) {
              case CSS_DOT -> state = Dim.DOT;

              case CSS_DIGIT -> state = Dim.INT;

              case CSS_ALPHA -> state = Dim.UNIT;

              default -> { return false; }
            }
          }

          case DOT -> {
            switch (next) {
              case CSS_DIGIT -> state = Dim.DBL;

              default -> { return false; }
            }
          }

          case DBL -> {
            switch (next) {
              case CSS_DIGIT -> state = Dim.DBL;

              case CSS_ALPHA -> state = Dim.UNIT;

              default -> { return false; }
            }
          }

          case UNIT -> {
            switch (next) {
              case CSS_ALPHA -> state = Dim.UNIT;

              default -> { return false; }
            }
          }
        }
      }

      return state == Dim.UNIT;
    }

    public final String formatValue() {
      return value(false);
    }

    private String formatValueSemicolon() {
      return value(true);
    }

    public final List<Top> parse() {
      final List<Top> result;
      result = new ArrayList<>();

      while (hasNext()) {
        switch (next()) {
          case CSS_WS -> {}

          case CSS_SOLIDUS -> comment();

          case CSS_AT -> at().asTop(result);

          default -> result.add(
              styleRule()
          );
        }
      }

      return result;
    }

    public final void set(String value) {
      cursor = idx = 0;

      text = value;
    }

    public final Variant variant(int index) {
      final String name;
      name = text;

      set(name + " { {} } }");

      return variantValue(index, name);
    }

    public final List<Variant> variants(int index) {
      final List<Variant> result;
      result = new ArrayList<>();

      while (hasNext()) {
        switch (next()) {
          case CSS_WS -> {}

          case CSS_ALPHA -> result.add(
              variant0(index + result.size())
          );

          default -> throw error("Variant name must start with a letter");
        }
      }

      return result;
    }

    private Variant variant0(int index) {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      final String name;
      name = text.substring(start, idx);

      return switch (next) {
        case CSS_WS -> variantOpen(index, name);

        case CSS_LCURLY -> variantValue(index, name);

        default -> throw error("Invalid variant declaration");
      };
    }

    private Variant variantOpen(int index, String name) {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_LCURLY -> variantValue(index, name);

        default -> throw error("Invalid variant declaration");
      };
    }

    private class VariantParts {
      final List<String> parts = new ArrayList<>();

      final StringBuilder sb = new StringBuilder();

      final void append(String selector) {
        sb.append(selector);
      }

      final void nextPart() {
        final String part;
        part = sb.toString();

        sb.setLength(0);

        parts.add(part);
      }

      final List<String> build() {
        final String part;
        part = sb.toString();

        parts.add(part);

        return parts;
      }
    }

    private Variant variantValue(int index, String name) {
      final VariantParts parts;
      parts = new VariantParts();

      variantParts(parts);

      final List<String> list;
      list = parts.build();

      return new Variant(index, name, list);
    }

    private void variantParts(VariantParts parts) {
      while (hasNext()) {
        switch (next()) {
          case CSS_WS -> {}

          case CSS_LCURLY -> variantParts0(parts);

          case CSS_RCURLY -> {
            return;
          }

          case CSS_AT -> variantAt(parts);

          case CSS_HASH, // id
               CSS_ASTERISK, // universal
               CSS_DOT, // class
               CSS_COLON, // pseudo
               CSS_LSQUARE, // attr
               CSS_ALPHA, // type
               CSS_AMPERSAND // nesting
               -> variantSel(parts);

          default -> throw error("Invalid variant definition");
        }
      }
    }

    private void variantParts0(VariantParts parts) {
      if (!hasNext()) {
        throw error("Unclosed {");
      }

      final byte rcurly;
      rcurly = next();

      if (rcurly == CSS_RCURLY) {
        parts.nextPart();
      } else {
        // re-consume char
        cursor--;

        // nest
        variantParts(parts);
      }
    }

    private void variantAt(VariantParts parts) {
      final int start;
      start = idx;

      whileNext(CSS_HYPHEN, CSS_ALPHA);

      final String name;
      name = text.substring(start, idx);

      switch (name) {
        case "@media" -> varianAtQuery(parts, name);

        default -> throw error("Only @media at-rules are currently supported");
      }
    }

    private void varianAtQuery(VariantParts parts, String name) {
      final String query;
      query = atQuery(name);

      parts.append(query);

      parts.append(" { ");

      variantParts(parts);

      parts.append(" }");
    }

    private void variantSel(VariantParts parts) {
      // re-consume first char;
      cursor--;

      final String selector;
      selector = selector(CSS_LCURLY);

      parts.append(selector);

      parts.append(" { ");

      variantParts(parts);

      parts.append(" }");
    }

    private At at() {
      final int at;
      at = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_ALPHA);

      switch (next) {
        case CSS_WS -> {}

        case CSS_EOF -> throw error(EOF_AT);

        default -> throw error("Invalid at-rule name");
      }

      final String name;
      name = text.substring(at, idx);

      return switch (name) {
        case "@font-face" -> atFontFace();

        case "@keyframes" -> atKeyframes();

        case "@media" -> atBlock(name);

        default -> throw error(name + " at-rule is not supported");
      };
    }

    private Block atBlock(String name) {
      final String query;
      query = atQuery(name);

      final List<Stmt> stmts;
      stmts = stmts();

      return new Block(query, stmts);
    }

    private FontFace atFontFace() {
      final byte lcurly;
      lcurly = whileNext(CSS_WS);

      if (lcurly != CSS_LCURLY) {
        throw error("Invalid @font-face declaration");
      }

      final List<Decl> decls;
      decls = decls();

      return new FontFace(decls);
    }

    private Keyframes atKeyframes() {
      final String name;
      name = atKeyframesName();

      final List<Block> rules;
      rules = atKeyframesRules();

      return new Keyframes(name, rules);
    }

    private String atKeyframesName() {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        case CSS_EOF -> throw error(EOF_AT);

        case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA -> atKeyframesName0();

        default -> throw error("Invalid @keyframes name");
      };
    }

    private String atKeyframesName0() {
      final int start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      return switch (next) {
        case CSS_EOF -> throw error(EOF_AT);

        case CSS_WS -> atKeyframesNameTrim(start);

        case CSS_LCURLY -> text.substring(start, idx);

        default -> throw error("Invalid @keyframes name");
      };
    }

    private String atKeyframesNameTrim(int start) {
      final int end;
      end = idx;

      final byte next;
      next = whileNext(CSS_WS);

      if (next != CSS_LCURLY) {
        throw error("Invalid @keyframes name");
      }

      return text.substring(start, end);
    }

    private List<Block> atKeyframesRules() {
      final List<Block> rules;
      rules = new ArrayList<>();

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_WS) {
          continue;
        }

        if (next == CSS_RCURLY) {
          break;
        }

        rules.add(
            atKeyframesRule()
        );
      }

      return rules;
    }

    private Block atKeyframesRule() {
      final String selector;
      selector = atKeyframesSelector();

      final List<Stmt> stmts;
      stmts = stmts();

      return new Block(selector, stmts);
    }

    private String atKeyframesSelector() {
      final StringBuilder sb;
      sb = new StringBuilder();

      // re-consume first char
      cursor--;

      int ws;
      ws = 0;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_LCURLY) {
          if (sb.isEmpty()) {
            throw error("empty @keyframes selector");
          }

          return sb.toString();
        }

        if (next == CSS_WS) {
          ws++;

          continue;
        }

        if (ws > 0) {
          sb.append(' ');

          ws = 0;
        }

        final String sel;
        sel = switch (next) {
          case CSS_ALPHA -> atKeyframesSelectorKw();

          case CSS_DIGIT -> atKeyframesSelectorPerc();

          default -> throw error("Invalid @keyframes selector :: next=" + next);
        };

        sb.append(sel);
      }

      throw error(EOF_SEL);
    }

    private String atKeyframesSelectorKw() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_ALPHA);

      return selEnd(start, next);
    }

    private String atKeyframesSelectorPerc() {
      final int start;
      start = idx;

      final byte perc;
      perc = whileNext(CSS_DIGIT);

      if (perc != CSS_PERCENT) {
        throw error("Invalid @keyframes selector");
      }

      final byte next;
      next = nextEof();

      return selEnd(start, next);
    }

    private String atQuery(String name) {
      final StringBuilder sb;
      sb = new StringBuilder();

      sb.append(name);

      int ws;
      ws = 0;

      // re-consume CSS_WS
      cursor--;

      while (hasNext()) {
        switch (next()) {
          case CSS_LCURLY -> {
            return sb.toString();
          }

          case CSS_WS -> ws++;

          case CSS_COMMA, CSS_COLON -> { sb.append(c); ws++; }

          case CSS_LPARENS -> { sb.append(' '); sb.append(c); ws = 0; }

          case CSS_RPARENS -> { sb.append(c); ws = 0; }

          default -> { if (ws > 0) { sb.append(' '); ws = 0; } sb.append(c); }
        }
      }

      throw error(EOF_AT);
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

    private List<Decl> decls() {
      final List<Decl> decls;
      decls = new ArrayList<>();

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_RCURLY) {
          break;
        }

        if (next == CSS_WS) {
          continue;
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

        case CSS_ASTERISK -> {
          final String name;
          name = text.substring(start, cursor);

          yield declColon(name);
        }

        case CSS_COLON -> {
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
        case CSS_SEMICOLON, CSS_RCURLY -> CssEngine.decl(name, "");

        case CSS_EOF -> throw error(EOF_DECLS);

        default -> {
          cursor--;

          final String value;
          value = formatValueSemicolon();

          yield CssEngine.decl(name, value);
        }
      };
    }

    private void malformed(int start) {
      throw new UnsupportedOperationException("Implement me");
    }

    private String selector(byte stop) {
      final StringBuilder sb;
      sb = new StringBuilder();

      int ws;
      ws = 0;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_RPARENS) {
          // end of function

          if (next == stop) {
            return sb.toString();
          } else {
            throw error("Invalid selector");
          }
        }

        if (next == CSS_LCURLY) {
          // start of rule body

          if (next == stop) {
            return sb.toString();
          } else {
            throw error("Invalid selector");
          }
        }

        if (next == CSS_WS) {
          // trim or combinator
          ws++;

          continue;
        }

        if (ws > 0) {
          // combinator
          sb.append(' ');

          ws = 0;
        }

        final String sel;
        sel = switch (next) {
          case CSS_HASH -> selId();

          case CSS_ASTERISK -> selUniversal();

          case CSS_COMMA -> selList();

          case CSS_DOT -> selClass();

          case CSS_COLON -> selPseudo();

          case CSS_LSQUARE -> selAttr();

          case CSS_ALPHA -> selType();

          case CSS_AMPERSAND -> selNesting();

          default -> throw error("Invalid selector");
        };

        sb.append(sel);
      }

      throw error(EOF_SEL);
    }

    private String selAttr() {
      final int start;
      start = idx;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_RSQUARE) {
          return text.substring(start, cursor);
        }
      }

      throw error(EOF_SEL);
    }

    private String selClass() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      return selEnd(start, next);
    }

    private String selEnd(int start, byte next) {
      return switch (next) {
        case CSS_EOF -> throw error(EOF_SEL);

        case CSS_WS,
             // attr
             CSS_LSQUARE,
             // list
             CSS_COMMA,
             // pseudo
             CSS_COLON -> text.substring(start, --cursor);

        default -> throw error("Invalid selector");
      };
    }

    private String selId() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      return selEnd(start, next);
    }

    private String selList() {
      final byte next;
      next = whileNext(CSS_WS);

      return switch (next) {
        // universal
        case CSS_ASTERISK,
             // nesting
             CSS_AMPERSAND,
             // attr
             CSS_LSQUARE,
             // class
             CSS_DOT,
             // id
             CSS_HASH,
             // pseudo
             CSS_COLON,
             // type
             CSS_HYPHEN, CSS_ALPHA -> { cursor--; yield ", "; }

        default -> throw error("Invalid selector");
      };
    }

    private String selNesting() {
      final int start;
      start = idx;

      return selEnd(start, nextEof());
    }

    private String selPseudo() {
      final int start;
      start = idx;

      if (!hasNext()) {
        throw error(EOF_SEL);
      }

      return switch (next()) {
        case CSS_HYPHEN, CSS_ALPHA -> selPseudo0(start);

        case CSS_COLON -> selPseudo0(start);

        default -> throw error("Invalid pseudo CSS selector");
      };
    }

    private String selPseudo0(int start) {
      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      if (next == CSS_LPARENS) {
        final String sel;
        sel = text.substring(start, cursor);

        return sel + selector(CSS_RPARENS) + ")";
      } else {
        return selEnd(start, next);
      }
    }

    private String selType() {
      final int start;
      start = idx;

      final byte next;
      next = whileNext(CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT);

      return selEnd(start, next);
    }

    private String selUniversal() {
      final int start;
      start = idx;

      return selEnd(start, nextEof());
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

        case CSS_AT -> at().asStmt();

        case CSS_ALPHA -> stmtSep();

        default -> throw new UnsupportedOperationException("Implement me :: next=" + next);
      };
    }

    private Stmt stmtSep() {
      final int start;
      start = idx;

      while (hasNext()) {
        switch (next()) {
          case CSS_SEMICOLON -> {
            idx = start;

            cursor = idx + 1;

            return decl();
          }

          case CSS_LCURLY -> {
            idx = start;

            cursor = idx + 1;

            return styleRule();
          }
        }
      }

      throw new UnsupportedOperationException("Implement me");
    }

    private Block styleRule() {
      // re-consume first char
      cursor--;

      final String selector;
      selector = selector(CSS_LCURLY);

      final List<Stmt> stmts;
      stmts = stmts();

      return new Block(selector, stmts);
    }

    private enum Value {
      NORMAL,
      HYPHEN1,
      HYPHEN2,
      CUSTOM,

      RX,
      RX_INT,
      RX_DOT,
      RX_DOUBLE,

      THEME,
      THEME_WS,
      THEME_COMMA;
    }

    private String value(boolean semi) {
      int hyphen;
      hyphen = 0;

      final StringBuilder sb;
      sb = new StringBuilder();

      Value state;
      state = Value.NORMAL;

      while (hasNext()) {
        final byte next;
        next = next();

        if (next == CSS_SEMICOLON && semi) {
          return sb.toString();
        }

        switch (state) {
          case NORMAL -> {
            switch (next) {
              case CSS_HYPHEN -> { hyphen = idx; state = Value.HYPHEN1; }

              default -> state = Value.NORMAL;
            }
          }

          case HYPHEN1 -> {
            switch (next) {
              case CSS_HYPHEN -> state = Value.HYPHEN2;

              default -> state = Value.NORMAL;
            }
          }

          case HYPHEN2 -> {
            switch (next) {
              case CSS_ALPHA -> state = Value.CUSTOM;

              default -> state = Value.NORMAL;
            }
          }

          case CUSTOM -> {
            switch (next) {
              case CSS_ALPHA -> state = Value.CUSTOM;

              case CSS_LPARENS -> {
                final String name;
                name = text.substring(hyphen, idx);

                switch (name) {
                  case "--rx" -> state = Value.RX;

                  case "--theme" -> {
                    int length;
                    length = sb.length();

                    sb.setLength(length - "--theme".length());

                    sb.append("var");

                    state = Value.THEME;
                  }

                  default -> state = Value.NORMAL;
                }
              }

              default -> state = Value.NORMAL;
            }
          }

          case RX -> {
            switch (next) {
              case CSS_DOT -> state = Value.RX_DOUBLE;

              case CSS_DIGIT -> state = Value.RX_INT;

              default -> state = Value.NORMAL;
            }
          }

          case RX_INT -> {
            switch (next) {
              case CSS_RPARENS -> { state = valueRx(sb, hyphen); continue; }

              case CSS_DOT -> state = Value.RX_DOT;

              case CSS_DIGIT -> state = Value.RX_INT;

              default -> state = Value.NORMAL;
            }
          }

          case RX_DOT -> {
            switch (next) {
              case CSS_DIGIT -> state = Value.RX_DOUBLE;

              default -> state = Value.NORMAL;
            }
          }

          case RX_DOUBLE -> {
            switch (next) {
              case CSS_RPARENS -> { state = valueRx(sb, hyphen); continue; }

              case CSS_DIGIT -> state = Value.RX_DOUBLE;

              default -> state = Value.NORMAL;
            }
          }

          case THEME -> {
            switch (next) {
              case CSS_RPARENS -> state = Value.NORMAL;

              case CSS_WS -> { state = Value.THEME_WS; continue; }

              case CSS_COMMA -> state = Value.THEME_COMMA;

              default -> state = Value.THEME;
            }
          }

          case THEME_WS -> {
            switch (next) {
              case CSS_RPARENS -> state = Value.NORMAL;

              case CSS_WS -> { state = Value.THEME_WS; continue; }

              default -> state = Value.THEME;
            }
          }

          case THEME_COMMA -> {
            switch (next) {
              case CSS_RPARENS -> state = Value.NORMAL;

              case CSS_WS -> { state = Value.THEME_COMMA; continue; }

              default -> { sb.append(' '); state = Value.THEME; }
            }
          }
        }

        sb.append(c);
      }

      if (semi) {
        throw error("Invalid CSS declaration value");
      }

      return sb.toString();
    }

    private Value valueRx(StringBuilder sb, int hyphen) {
      final int start;
      start = hyphen + 5; // '--rx('

      final int end;
      end = idx;

      final String value;
      value = text.substring(start, end);

      int length;
      length = sb.length();

      sb.setLength(length - (idx - hyphen));

      sb.append("calc(");

      sb.append(value);

      sb.append(" / 16 * 1rem)");

      return Value.NORMAL;
    }

    // utils

    private IllegalArgumentException error(String message) {
      int start;
      start = text.lastIndexOf('\n', cursor);

      if (start < 0) {
        start = 0;
      } else {
        start += 1;
      }

      int end;
      end = text.indexOf('\n', cursor);

      if (end < 0) {
        end = text.length();
      }

      final String line;
      line = text.substring(start, end);

      return new IllegalArgumentException("%s\n\t%s".formatted(message, line));
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

    private byte whileNext(byte c0, byte c1) {
      while (hasNext()) {
        final byte next;
        next = next();

        if (next == c0) {
          continue;
        }

        if (next == c1) {
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
  // # END: Syntax
  // ##################################################################

  // ##################################################################
  // # BEGIN: Variant
  // ##################################################################

  record Variant(int index, String name, List<String> parts) implements Comparable<Variant> {
    Variant {
      if (parts.size() < 2) {
        throw new IllegalArgumentException(
            "A variant must contain at least 2 parts"
        );
      }
    }

    @Override
    public final int compareTo(Variant o) {
      return Integer.compare(index, o.index);
    }

    final Variant withIndex(int newIndex) { return new Variant(newIndex, name, parts); }
  }

  static Variant variant(int i, String n, List<String> parts) { return new Variant(i, n, parts); }
  static Variant variant(int i, String n, String... parts) { return new Variant(i, n, List.of(parts)); }

  // ##################################################################
  // # END: Variant
  // ##################################################################

  // ##################################################################
  // # BEGIN: Properties
  // ##################################################################

  static final class Properties {

    private final Map<String, Decl> properties = new LinkedHashMap<>();

    private enum Props {
      NORMAL,
      LPARENS,
      HYPHEN1,
      HYPHEN2,
      PROP;
    }

    public final void accept(Decl decl) {
      decl.mark();

      final String value;
      value = decl.value;

      accept(value);

      final Decl next;
      next = decl.next;

      if (next == null) {
        return;
      }

      accept(next);
    }

    public final void accept(String text) {
      int hyphen;
      hyphen = 0;

      Props state;
      state = Props.NORMAL;

      for (int idx = 0, len = text.length(); idx < len; idx++) {
        final char c;
        c = text.charAt(idx);

        final byte next;
        next = c < 128 ? CSS[c] : CSS_NON_ASCII;

        switch (state) {
          case NORMAL -> {
            switch (next) {
              case CSS_LPARENS -> state = Props.LPARENS;

              default -> state = Props.NORMAL;
            }
          }

          case LPARENS -> {
            switch (next) {
              case CSS_HYPHEN -> { hyphen = idx; state = Props.HYPHEN1; }

              default -> state = Props.NORMAL;
            }
          }

          case HYPHEN1 -> {
            switch (next) {
              case CSS_HYPHEN -> state = Props.HYPHEN2;

              default -> state = Props.NORMAL;
            }
          }

          case HYPHEN2 -> {
            switch (next) {
              case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT -> state = Props.PROP;

              default -> state = Props.NORMAL;
            }
          }

          case PROP -> {
            switch (next) {
              case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT -> state = Props.PROP;

              case CSS_RPARENS, CSS_COMMA -> {
                final String name;
                name = text.substring(hyphen, idx);

                consume(name);

                state = Props.NORMAL;
              }

              default -> state = Props.NORMAL;
            }
          }
        }
      }
    }

    final void merge(String propName, Decl decl) {
      properties.merge(propName, decl, (oldValue, value) -> oldValue.append(value));
    }

    private void consume(String name) {
      final Decl decl;
      decl = properties.get(name);

      if (decl == null) {
        return;
      }

      accept(decl);
    }

  }

  // ##################################################################
  // # END: Marker
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configuring
  // ##################################################################

  static final class Configuring implements Css.StyleSheet.Options {

    private final Note.Ref2<Decl, Decl> $replaced = Note.Ref2.create(getClass(), "REP", Note.INFO);

    private final List<Block> components = new ArrayList<>();

    private Note.Sink noteSink = Note.NoOpSink.INSTANCE;

    private Set<Class<?>> scanClasses = Set.of();

    private Set<Path> scanDirectories = Set.of();

    private Set<Class<?>> scanJars = Set.of();

    private final Syntax syntax = new Syntax();

    private List<Top> systemBase;

    private List<Top> systemTheme;

    private List<Variant> systemVariants;

    private Set<String> userCssProperties = Set.of();

    private List<Top> userTheme = List.of();

    private List<Variant> userVariants = List.of();

    Configuring() {
      // system base
      syntax.set(Css.systemBase());

      systemBase = syntax.parse();

      // system theme
      syntax.set(Css.systemTheme());

      final List<Top> _systemTheme;
      _systemTheme = syntax.parse();

      systemTheme = validateSystemTheme(_systemTheme);

      // system variants
      syntax.set(Css.systemVariants());

      final List<Variant> variants;
      variants = syntax.variants(0);

      systemVariants = validateSystemVariants(variants);
    }

    public final void generate(Appendable out) throws IOException {
      final Config config;
      config = configure();

      final CssEngine engine;
      engine = new CssEngine(config);

      engine.generate(out);
    }

    // ##################################################################
    // # BEGIN: Configuring: Public API
    // ##################################################################

    @Override
    public final void cssPropertyNames(String... values) {
      for (int idx = 0; idx < values.length; idx++) {
        final String name;
        name = Check.notNull(values[idx], "values[", idx, "] == null");

        if (userCssProperties.isEmpty()) {
          userCssProperties = new HashSet<>();
        }

        userCssProperties.add(name);
      }
    }

    @Override
    public final void include(Css.Library value) {
      value.configure(this);
    }

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
    public final void scanClasses(Class<?>... values) {
      final Class<?>[] classes;
      classes = Objects.requireNonNull(values, "values == null");

      if (scanClasses.isEmpty()) {
        scanClasses = new HashSet<>();
      }

      for (int idx = 0; idx < classes.length; idx++) {
        final Class<?> c;
        c = Check.notNull(classes[idx], "values[", idx, "] == null");

        scanClasses.add(c);
      }
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

      if (userTheme.isEmpty()) {
        userTheme = new ArrayList<>();
      }

      syntax.set(text);

      final List<Top> parsed;
      parsed = syntax.parse();

      final List<Top> validated;
      validated = validateUserTheme(parsed);

      userTheme.addAll(validated);
    }

    @Override
    public final void systemTheme(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      syntax.set(text);

      final List<Top> _systemTheme;
      _systemTheme = syntax.parse();

      systemTheme = validateSystemTheme(_systemTheme);
    }

    @Override
    public final void systemBase(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      syntax.set(text);

      systemBase = syntax.parse();
    }

    @Override
    public final void systemVariants(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      syntax.set(text);

      final List<Variant> variants;
      variants = syntax.variants(0);

      systemVariants = validateSystemVariants(variants);
    }

    @Override
    public final void variants(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      syntax.set(text);

      if (userVariants.isEmpty()) {
        userVariants = new ArrayList<>();
      }

      final int startIndex;
      startIndex = userVariants.size();

      final List<Variant> parsed;
      parsed = syntax.variants(startIndex);

      userVariants.addAll(parsed);
    }

    @Override
    public final void components(String value) {
      final String text;
      text = Objects.requireNonNull(value, "value == null");

      final Syntax syntax;
      syntax = new Syntax();

      syntax.set(text);

      final List<Top> parsed;
      parsed = syntax.parse();

      for (Top top : parsed) {
        switch (top) {
          case Block rule -> components.add(rule);

          case FontFace ff -> throw new IllegalArgumentException(
              "@font-face declarations are not allowed in the components layer"
          );

          case Keyframes kf -> throw new IllegalArgumentException(
              "@keyframes declarations are not allowed in the components layer"
          );
        }
      }
    }

    private List<Top> validateSystemTheme(List<Top> parsed) {
      final Set<String> kfNames;
      kfNames = new HashSet<>();

      final Set<String> propNames;
      propNames = new HashSet<>();

      for (Top top : parsed) {
        switch (top) {
          case Block(String selector, List<Stmt> stmts) -> {
            if (!":root".equals(selector)) {
              throw new IllegalArgumentException(
                  "The system theme must only contain the :root selector"
              );
            }

            for (Stmt stmt : stmts) {
              switch (stmt) {
                case Decl decl -> {
                  final String property;
                  property = decl.property;

                  if (!property.startsWith("--")) {
                    throw decl.invalid("The system theme must only contain custom properties");
                  }

                  if (!propNames.add(property)) {
                    decl.invalid("Duplicate property definition");
                  }
                }

                case Block nested -> throw new IllegalArgumentException(
                    "The system theme must not contain nested statements"
                );
              }
            }
          }

          case FontFace ff -> throw new IllegalArgumentException(
              "The system theme must not contain @font-face declarations"
          );

          case Keyframes kf -> {
            final String name;
            name = kf.name;

            if (!kfNames.add(name)) {
              throw new IllegalArgumentException(
                  "Duplicate @keyframes definition: " + name
              );
            }
          }
        }
      }

      return parsed;
    }

    private List<Variant> validateSystemVariants(List<Variant> parsed) {
      final Set<String> names;
      names = new HashSet<>();

      for (Variant v : parsed) {
        final String name;
        name = v.name;

        if (!names.add(name)) {
          throw new IllegalArgumentException(
              "Duplicate variant name " + name
          );
        }
      }

      return parsed;
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

    private List<Top> validateUserTheme(List<Top> parsed) {
      for (Top top : parsed) {
        switch (top) {
          case FontFace ff -> {
            for (Decl decl : ff.decls) {
              final String property;
              property = decl.property;

              if (!FONT_FACE_PROPS.contains(property)) {
                decl.invalid("Invalid @font-face property");
              }
            }
          }

          default -> {}
        }
      }

      return parsed;
    }

    final class Helper {
      final List<FontFace> fontFaces = new ArrayList<>();

      final Map<String, CssEngine.Keyframes> keyframes = new HashMap<>();

      final Syntax parser = new Syntax();

      final Properties properties = new Properties();

      final List<Section> sections = new ArrayList<>();

      final Map<List<String>, List<Decl>> themeSections = new LinkedHashMap<>();

      final Map<List<String>, Map<String, Decl>> themeProps = new LinkedHashMap<>();

      final Map<String, Variant> variants = new HashMap<>();

      //

      final void systemTheme() {
        for (Top top : systemTheme) {
          switch (top) {
            case Block(String selector, List<Stmt> stmts) -> {
              final List<String> root;
              root = List.of(selector);

              final List<Decl> section;
              section = themeSections.computeIfAbsent(root, key -> new ArrayList<>());

              for (Stmt stmt : stmts) {
                switch (stmt) {
                  case Decl decl -> {
                    section.add(decl);

                    final String property;
                    property = decl.property;

                    final Map<String, Decl> values;
                    values = themeProps.computeIfAbsent(root, key -> new LinkedHashMap<>());

                    values.put(property, decl);
                  }

                  case Block nested -> throw new IllegalArgumentException(
                      "The system theme must not contain nested statements"
                  );
                }
              }
            }

            case FontFace ff -> throw new IllegalArgumentException(
                "The system theme must not contain @font-face declarations"
            );

            case Keyframes kf -> {
              final String name;
              name = kf.name;

              keyframes.put(name, kf);
            }
          }
        }
      }

      final void userTheme() {
        for (Top top : userTheme) {
          switch (top) {
            case Block rule -> userTheme(List.of(), rule);

            case FontFace ff -> fontFaces.add(ff);

            case Keyframes kf -> {
              final String name;
              name = kf.name;

              final Keyframes existing;
              existing = keyframes.put(name, kf);

              if (existing != null) {
                // TODO log replaced
              }
            }
          }
        }
      }

      private void userTheme(List<String> previous, Block rule) {
        final List<String> list;
        list = new ArrayList<>(previous);

        list.add(rule.selector);

        final List<String> selector;
        selector = List.copyOf(list);

        final List<Decl> section;
        section = themeSections.computeIfAbsent(selector, key -> new ArrayList<>());

        final Map<String, Decl> props;
        props = themeProps.computeIfAbsent(selector, key -> new LinkedHashMap<>());

        for (Stmt stmt : rule.stmts) {
          switch (stmt) {
            case Decl decl -> {
              section.add(decl);

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

            case Block nested -> userTheme(selector, nested);
          }
        }
      }

      final void collectProperties() {
        for (Map<String, Decl> map : themeProps.values()) {
          for (Map.Entry<String, Decl> inner : map.entrySet()) {
            final String propName;
            propName = inner.getKey();

            final Decl decl;
            decl = inner.getValue();

            properties.merge(propName, decl);
          }
        }
      }

      final void collectSections() {
        for (Map.Entry<List<String>, List<Decl>> entry : themeSections.entrySet()) {
          final List<String> selector;
          selector = entry.getKey();

          final List<Decl> decls;
          decls = entry.getValue();

          final Section section;
          section = new Section(selector, decls);

          sections.add(section);
        }
      }

      final void collectVariants() {
        final List<Variant> system;

        if (!userVariants.isEmpty()) {
          system = new ArrayList<>();

          final int startIndex;
          startIndex = userVariants.size();

          for (Variant old : systemVariants) {
            final int newIndex;
            newIndex = startIndex + system.size();

            final Variant v;
            v = old.withIndex(newIndex);

            system.add(v);
          }
        } else {
          system = systemVariants;
        }

        for (Variant v : system) {
          final String name;
          name = v.name;

          variants.put(name, v);
        }

        for (Variant v : userVariants) {
          final String name;
          name = v.name;

          final Variant existing;
          existing = variants.put(name, v);

          if (existing != null) {
            // TODO log replaced
          }
        }
      }

      final void baseProperties() {
        for (Top top : systemBase) {
          switch (top) {
            case Block rule -> baseStyleRule(rule);

            case FontFace ff -> {/* leave it as it is */}

            case Keyframes kf -> { /* leave it as it is */ }
          }
        }
      }

      private void baseStyleRule(Block rule) {
        for (Stmt stmt : rule.stmts) {
          switch (stmt) {
            case Decl decl -> properties.accept(decl);

            case Block nested -> baseStyleRule(nested);
          }
        }
      }

      final void componentsProperties() {
        for (Block component : components) {
          baseStyleRule(component);
        }
      }

      final Config build(Set<String> cssProperties) {
        return new Config(
            systemBase,

            components,

            cssProperties,

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

    }

    public final Config configure() {
      final Helper helper;
      helper = new Helper();

      final Set<String> systemCssProperties;
      systemCssProperties = CssProps.get();

      final Set<String> cssProperties;

      if (userCssProperties.isEmpty()) {
        cssProperties = systemCssProperties;
      } else {
        cssProperties = userCssProperties;

        userCssProperties.addAll(systemCssProperties);
      }

      helper.systemTheme();

      helper.userTheme();

      helper.collectProperties();

      helper.collectSections();

      helper.collectVariants();

      helper.baseProperties();

      helper.componentsProperties();

      return helper.build(cssProperties);
    }

    // ##################################################################
    // # END: Configuring: Public API
    // ##################################################################

  }

  // ##################################################################
  // # END: Configuring
  // ##################################################################

  // ##################################################################
  // # BEGIN: Configured
  // ##################################################################

  record Config(

      List<Top> base,

      List<Block> components,

      Set<String> cssProperties,

      List<FontFace> fontFaces,

      Map<String, Keyframes> keyframes,

      Note.Sink noteSink,

      Properties properties,

      Set<Class<?>> scanClasses,

      Set<Path> scanDirectories,

      Set<Class<?>> scanJars,

      List<Section> sections,

      Map<String, Variant> variants

  ) {

    final Map<String, Decl> propertiesMap() {
      return properties.properties;
    }

  }

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

  record Utility(List<Variant> variants, String className, String property, String value)
      implements Comparable<Utility> {

    @Override
    public final int compareTo(Utility o) {
      final int thisVariants;
      thisVariants = variants.size();

      final int thatVariants;
      thatVariants = o.variants.size();

      if (thisVariants != thatVariants) {
        return Integer.compare(thisVariants, thatVariants);
      }

      for (int idx = 0; idx < thisVariants; idx++) {
        final Variant thisVar;
        thisVar = variants.get(idx);

        final Variant thatVar;
        thatVar = o.variants.get(idx);

        final int x;
        x = thisVar.compareTo(thatVar);

        if (x != 0) {
          return x;
        }
      }

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

  static Utility utility(List<Variant> variants, String className, String property, String value) {
    return new Utility(variants, className, property, value);
  }

  static final class Proc implements Slugs {

    private final ClassNameFormat classNameFormat = new ClassNameFormat();

    private final Set<String> cssProperties;

    private final Set<String> distinct = new HashSet<>();

    @SuppressWarnings("unused")
    private final Note.Sink noteSink;

    private final List<Variant> parsedVars = new ArrayList<>();

    private final Syntax syntax = new Syntax();

    private final List<Utility> utilities = new ArrayList<>();

    private final Map<String, Variant> variants;

    Proc(Set<String> cssProperties, Note.Sink noteSink, Map<String, Variant> variants) {
      this.cssProperties = cssProperties;

      this.noteSink = noteSink;

      this.variants = variants;
    }

    @Override
    public final void consume(String className, List<String> slugs) {
      if (!distinct.add(className)) {
        // already seen...
        return;
      }

      final String propName;
      propName = slugs.get(1);

      if (!cssProperties.contains(propName)) {
        return;
      }

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

      final String propValue;
      propValue = slugs.get(0);

      syntax.set(propValue);

      final String valueFormatted;
      valueFormatted = syntax.formatValue();

      final Utility utility;
      utility = new Utility(vars, classNameFormatted, propName, valueFormatted);

      utilities.add(utility);
    }

    public final List<Utility> result() {
      return utilities;
    }

    private Variant variantByName(String name) {
      final Variant result;
      result = variants.get(name);

      if (result != null) {
        return result;
      }

      try {
        syntax.set(name);

        final int index;
        index = variants.size();

        final Variant parsed;
        parsed = syntax.variant(index);

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

  record Ctx(
      List<Keyframes> keyframes,
      List<Section> sections,
      List<Utility> utilities
  ) {}

  static final class Gen {

    private static final Set<String> KEYFRAMES_PROPERTIES = Set.of(
        "animation",
        "animation-name"
    );

    private final Map<String, Keyframes> keyframes;

    private final Map<String, Keyframes> keyframesMarked = new HashMap<>();

    private final Properties properties;

    private final List<Section> sections;

    Gen(Map<String, Keyframes> keyframes, Properties properties, List<Section> sections) {
      this.keyframes = keyframes;

      this.properties = properties;

      this.sections = sections;
    }

    public final Ctx generate(List<Utility> utilities) {
      for (Utility utility : utilities) {
        process(utility);
      }

      utilities.sort(Comparator.naturalOrder());

      return new Ctx(
          keyframesMarked.values().stream().sorted().toList(),

          themeSections(),

          utilities
      );
    }

    private enum Kf {
      NORMAL,

      IDEN;
    }

    private void process(Utility utility) {
      final String value;
      value = utility.value;

      properties.accept(value);

      final String property;
      property = utility.property;

      final boolean keyframesProp;
      keyframesProp = KEYFRAMES_PROPERTIES.contains(property);

      if (!keyframesProp) {
        return;
      }

      int iden;
      iden = 0;

      Kf state;
      state = Kf.NORMAL;

      for (int idx = 0, len = value.length(); idx < len; idx++) {
        final char c;
        c = value.charAt(idx);

        final byte next;
        next = c < 128 ? CSS[c] : CSS_NON_ASCII;

        switch (state) {
          case NORMAL -> {
            switch (next) {
              case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA -> { iden = idx; state = Kf.IDEN; }

              default -> state = Kf.NORMAL;
            }
          }

          case IDEN -> {
            switch (next) {
              case CSS_HYPHEN, CSS_UNDERLINE, CSS_ALPHA, CSS_DIGIT -> state = Kf.IDEN;

              default -> {
                final String kf;
                kf = value.substring(iden, idx);

                kf(kf);

                state = Kf.NORMAL;
              }
            }
          }
        }
      }

      if (state == Kf.IDEN) {
        final int end;
        end = value.length();

        final String kf;
        kf = value.substring(iden, end);

        kf(kf);
      }
    }

    private void kf(String value) {
      if (keyframesMarked.containsKey(value)) {
        return;
      }

      final Keyframes kf;
      kf = keyframes.get(value);

      if (kf == null) {
        return;
      }

      keyframesMarked.put(value, kf);
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

    final void wblock(Block block) throws IOException {
      indent();

      w(block.selector);

      wln(" {");

      level++;

      for (Stmt stmt : block.stmts) {
        switch (stmt) {
          case Decl decl -> wdecl(decl);

          case Block nested -> wblock(nested);
        }
      }

      level--;

      indent();

      wln('}');
    }

    final void wdecl(Decl decl) throws IOException {
      indent();

      w(decl.property);

      w(": ");

      w(decl.value);

      wln(";");
    }

    final void wdecls(List<Decl> decls) throws IOException {
      for (Decl decl : decls) {
        wdecl(decl);
      }
    }

    final void wfontface(FontFace ff) throws IOException {
      if (ff.isEmpty()) {
        return;
      }

      wln("@font-face {");

      level++;

      wdecls(ff.decls);

      level--;

      wln('}');
    }

    final void wkeyframes(Keyframes kf) throws IOException {
      w("@keyframes ");

      w(kf.name);

      wln(" {");

      level++;

      final List<Block> rules;
      rules = kf.rules();

      for (Block rule : rules) {
        wblock(rule);
      }

      level--;

      wln('}');
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

  record Section(List<String> selector, List<Decl> decls) {}

  static Section section(List<String> s, List<Decl> decls) { return new Section(s, decls); }
  static Section section(List<String> s, Decl... decls) { return new Section(s, List.of(decls)); }

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

        wdecls(decls);

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

    final List<Top> source;

    Base(List<Top> source) {
      this.source = source;
    }

    @Override
    final void write() throws IOException {
      if (source.isEmpty()) {
        return;
      }

      wln("@layer base {");

      level++;

      for (Top top : source) {
        switch (top) {
          case Block block -> wblock(block);

          case FontFace ff -> wfontface(ff);

          case Keyframes kf -> wkeyframes(kf);
        }
      }

      level--;

      wln('}');
    }

  }

  // ##################################################################
  // # END: Base
  // ##################################################################

  // ##################################################################
  // # BEGIN: Components
  // ##################################################################

  static final class Components extends Writer {

    final List<Block> components;

    Components(List<Block> components) {
      this.components = components;
    }

    @Override
    final void write() throws IOException {
      if (components.isEmpty()) {
        return;
      }

      wln("@layer components {");

      level++;

      for (Block component : components) {
        wblock(component);
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

    private final StringBuilder sb = new StringBuilder();

    private final List<Utility> values;

    Utilities(List<Utility> values) {
      this.values = values;
    }

    @Override
    final void write() throws IOException {
      if (values.isEmpty()) {
        return;
      }

      wln("@layer utilities {");

      level++;

      for (Utility u : values) {
        indent();

        w(u.className);

        w(" { ");

        final List<Variant> variants;
        variants = u.variants;

        final int size;
        size = variants.size();

        if (size == 0) {
          w(u.property);

          w(": ");

          w(u.value);
        } else {
          sb.setLength(0);

          sb.append(u.property);

          sb.append(": ");

          sb.append(u.value);

          for (int idx = size - 1; idx >= 0; idx--) {
            final String val;
            val = sb.toString();

            sb.setLength(0);

            final Variant variant;
            variant = variants.get(idx);

            final List<String> parts;
            parts = variant.parts;

            final Iterator<String> iterator;
            iterator = parts.iterator();

            final String first;
            first = iterator.next();

            sb.append(first);

            while (iterator.hasNext()) {
              sb.append(val);

              final String next;
              next = iterator.next();

              sb.append(next);
            }
          }

          w(sb.toString());
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

    private final List<FontFace> fontFaces;
    private final List<Keyframes> keyframes;

    Trailer(List<FontFace> fontFaces, List<Keyframes> keyframes) {
      this.fontFaces = fontFaces;

      this.keyframes = keyframes;
    }

    @Override
    final void write() throws IOException {
      for (Keyframes kf : keyframes) {
        wkeyframes(kf);
      }

      for (FontFace face : fontFaces) {
        wfontface(face);
      }
    }

  }

  // ##################################################################
  // # END: Trailer
  // ##################################################################

}