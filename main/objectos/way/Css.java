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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import objectos.notes.NoteSink;

/**
 * The <strong>Objectos CSS</strong> main class.
 */
public final class Css {

  //
  // public API
  //

  /**
   * Generates a style sheet by scanning Java class files for predefined CSS
   * utility class names.
   */
  sealed interface Generator permits CssGenerator {}

  /**
   * A style sheet generation option.
   */
  public sealed interface Option {}

  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.TYPE)
  public @interface Source {}

  /**
   * A CSS style sheet.
   */
  public sealed interface StyleSheet {

    default String contentType() {
      return "text/css; charset=utf-8";
    }

    String css();

    byte[] toByteArray();

  }

  //
  // non-public types
  //

  //
  // B
  //

  record Breakpoint(int index, String name, String value) implements MediaQuery {
    @Override
    public final int compareTo(MediaQuery o) {
      if (o instanceof Breakpoint that) {
        return Integer.compare(index, that.index);
      } else {
        return -1;
      }
    }

    @Override
    public final void writeMediaQueryStart(StringBuilder out, Indentation indentation) {
      indentation.writeTo(out);

      out.append("@media (min-width: ");
      out.append(value);
      out.append(") {");
      out.append(System.lineSeparator());
    }
  }

  //
  // C
  //

  record ClassNameFormat(String before, String after) implements Variant {
    public final void writeClassName(StringBuilder out, int startIndex) {
      String original;
      original = out.substring(startIndex, out.length());

      out.setLength(startIndex);

      out.append(before);
      out.append(original);
      out.append(after);
    }

    final int length() {
      return before.length() + after.length();
    }
  }

  /**
   * CSS generation context.
   */
  static abstract class Context {

    final List<Rule> rules = Util.createList();

    Context() {
    }

    public final void add(Rule rule) {
      rules.add(rule);
    }

    public abstract void addComponent(CssComponent component);

    public abstract Context contextOf(Css.Modifier modifier);

    public final void writeTo(StringBuilder out, Indentation indentation) {
      rules.sort(Comparator.naturalOrder());

      write(out, indentation);
    }

    abstract void write(StringBuilder out, Indentation indentation);

  }

  private non-sealed static abstract class GeneratorOption implements Option {

    GeneratorOption() {}

    public static GeneratorOption cast(Option o) {
      // this cast is safe as Css.Option is sealed
      return (GeneratorOption) o;
    }

    abstract void acceptCssConfig(CssConfig config);

  }

  static final class Indentation {

    static final Indentation ROOT = new Indentation(0);

    private final int level;

    private Indentation(int level) {
      this.level = level;
    }

    public final String indent(String value) {
      return value.indent(level * 2);
    }

    public final Indentation increase() {
      return new Indentation(level + 1);
    }

    public final void writeTo(StringBuilder out) {
      for (int i = 0, count = level * 2; i < count; i++) {
        out.append(' ');
      }
    }

  }

  record InvalidVariant(String formatString, String reason) implements Variant {}

  enum Key {

    _COLORS,

    _SPACING,

    // utilities
    // order is important!

    CUSTOM,

    ACCESSIBILITY,

    POINTER_EVENTS,
    VISIBILITY,

    POSITION,

    INSET,
    INSET_X,
    INSET_Y,
    TOP,
    RIGHT,
    BOTTOM,
    LEFT,

    Z_INDEX,

    GRID_COLUMN,
    GRID_COLUMN_START,
    GRID_COLUMN_END,

    FLOAT,
    CLEAR,

    MARGIN,
    MARGIN_X,
    MARGIN_Y,
    MARGIN_TOP,
    MARGIN_RIGHT,
    MARGIN_BOTTOM,
    MARGIN_LEFT,

    DISPLAY,

    ASPECT_RATIO,
    SIZE,
    HEIGHT,
    MAX_HEIGHT,
    MIN_HEIGHT,
    WIDTH,
    MAX_WIDTH,
    MIN_WIDTH,

    FLEX,
    FLEX_GROW,
    FLEX_SHRINK,
    FLEX_BASIS,

    TABLE_LAYOUT,

    BORDER_COLLAPSE,
    BORDER_SPACING,
    BORDER_SPACING_X,
    BORDER_SPACING_Y,

    TRANSLATE,
    TRANSLATE_X,
    TRANSLATE_Y,
    TRANSFORM,

    CURSOR,

    USER_SELECT,

    LIST_STYLE_TYPE,

    APPEARANCE,

    GRID_TEMPLATE_COLUMNS,
    GRID_TEMPLATE_ROWS,

    FLEX_DIRECTION,
    FLEX_WRAP,
    ALIGN_CONTENT,
    ALIGN_ITEMS,
    JUSTIFY_CONTENT,
    GAP,
    GAP_X,
    GAP_Y,

    ALIGN_SELF,

    OVERFLOW,
    OVERFLOW_X,
    OVERFLOW_Y,

    TEXT_OVERFLOW,
    WHITESPACE,
    TEXT_WRAP,

    BORDER_RADIUS,
    BORDER_RADIUS_T,
    BORDER_RADIUS_R,
    BORDER_RADIUS_B,
    BORDER_RADIUS_L,
    BORDER_RADIUS_TL,
    BORDER_RADIUS_TR,
    BORDER_RADIUS_BR,
    BORDER_RADIUS_BL,

    BORDER_WIDTH,
    BORDER_WIDTH_X,
    BORDER_WIDTH_Y,
    BORDER_WIDTH_TOP,
    BORDER_WIDTH_RIGHT,
    BORDER_WIDTH_BOTTOM,
    BORDER_WIDTH_LEFT,

    BORDER_STYLE,

    BORDER_COLOR,
    BORDER_COLOR_X,
    BORDER_COLOR_Y,
    BORDER_COLOR_TOP,
    BORDER_COLOR_RIGHT,
    BORDER_COLOR_BOTTOM,
    BORDER_COLOR_LEFT,

    BACKGROUND_COLOR,

    FILL,
    STROKE,
    STROKE_WIDTH,

    PADDING,
    PADDING_X,
    PADDING_Y,
    PADDING_TOP,
    PADDING_RIGHT,
    PADDING_BOTTOM,
    PADDING_LEFT,

    TEXT_ALIGN,
    VERTICAL_ALIGN,

    FONT_SIZE,
    FONT_WEIGHT,
    FONT_STYLE,

    LINE_HEIGHT,

    LETTER_SPACING,

    TEXT_COLOR,
    TEXT_DECORATION,

    OPACITY,

    BOX_SHADOW,
    BOX_SHADOW_COLOR,

    OUTLINE_STYLE,
    OUTLINE_WIDTH,
    OUTLINE_OFFSET,
    OUTLINE_COLOR,

    RING_WIDTH,
    RING_COLOR,
    RING_OFFSET_WIDTH,
    RING_OFFSET_COLOR,
    RING_INSET,

    TRANSITION_PROPERTY,
    TRANSITION_DURATION,

    CONTENT;

  }

  //
  // M
  //

  sealed interface MediaQuery extends Comparable<MediaQuery>, Variant {

    void writeMediaQueryStart(StringBuilder out, Indentation indentation);

  }

  record Modifier(List<MediaQuery> mediaQueries, List<ClassNameFormat> classNameFormats) implements Comparable<Modifier> {

    @Override
    public final int compareTo(Modifier o) {
      return Integer.compare(length(), o.length());
    }

    public final void writeClassName(StringBuilder out, String className) {
      int startIndex;
      startIndex = out.length();

      Css.writeClassName(out, className);

      for (var format : classNameFormats) {
        format.writeClassName(out, startIndex);
      }
    }

    final int length() {
      int result = 0;

      for (var format : classNameFormats) {
        result += format.length();
      }

      return result;
    }

  }

  static final Modifier EMPTY_MODIFIER = new Modifier(List.of(), List.of());

  //
  // N
  //

  private static final class NoOpRule implements Rule {

    static final NoOpRule INSTANCE = new NoOpRule();

    private NoOpRule() {}

    @Override
    public final void accept(Css.Context ctx) {
      // noop
    }

    @Override
    public final int compareSameKind(Rule o) {
      return 0;
    }

    @Override
    public final int kind() {
      return 0;
    }

    @Override
    public final void writeTo(StringBuilder out, Css.Indentation indentation) {
      // noop
    }

    @Override
    public final void writeProps(StringBuilder out, Css.Indentation indentation) {
      // noop
    }

  }

  //
  // P
  //

  record PropertyType(String borderColorTop,
      String borderColorRight,
      String borderColorBottom,
      String borderColorLeft,

      String borderRadiusTopLeft,
      String borderRadiusTopRight,
      String borderRadiusBottomRight,
      String borderRadiusBottomLeft,

      String borderWidthTop,
      String borderWidthRight,
      String borderWidthBottom,
      String borderWidthLeft,

      String marginTop,
      String marginRight,
      String marginBottom,
      String marginLeft,

      String paddingTop,
      String paddingRight,
      String paddingBottom,
      String paddingLeft,

      String height,
      String maxHeight,
      String minHeight,

      String width,
      String maxWidth,
      String minWidth,

      String top,
      String right,
      String bottom,
      String left) {}

  static final PropertyType PHYSICAL = new PropertyType(
      "border-top-color",
      "border-right-color",
      "border-bottom-color",
      "border-left-color",

      "border-top-left-radius",
      "border-top-right-radius",
      "border-bottom-right-radius",
      "border-bottom-left-radius",

      "border-top-width",
      "border-right-width",
      "border-bottom-width",
      "border-left-width",

      "margin-top",
      "margin-right",
      "margin-bottom",
      "margin-left",

      "padding-top",
      "padding-right",
      "padding-bottom",
      "padding-left",

      "height",
      "max-height",
      "min-height",

      "width",
      "max-width",
      "min-width",

      "top",
      "right",
      "bottom",
      "left"
  );

  static final PropertyType LOGICAL = new PropertyType(
      "border-block-start-color",
      "border-inline-end-color",
      "border-block-end-color",
      "border-inline-start-color",

      "border-start-start-radius",
      "border-start-end-radius",
      "border-end-end-radius",
      "border-end-start-radius",

      "border-block-start-width",
      "border-inline-end-width",
      "border-block-end-width",
      "border-inline-start-width",

      "margin-block-start",
      "margin-inline-end",
      "margin-block-end",
      "margin-inline-start",

      "padding-block-start",
      "padding-inline-end",
      "padding-block-end",
      "padding-inline-start",

      "block-size",
      "max-block-size",
      "min-block-size",

      "inline-size",
      "max-inline-size",
      "min-inline-size",

      "inset-block-start",
      "inset-inline-end",
      "inset-block-end",
      "inset-inline-start"
  );

  sealed interface Repository permits CssComponent, CssGenerator {

    void cycleCheck(String className);

    void consumeRule(String className, Rule existing);

    void putRule(String className, Rule rule);

  }

  sealed interface Rule extends Comparable<Rule> permits CssComponent, NoOpRule, CssUtility {

    Rule NOOP = NoOpRule.INSTANCE;

    void accept(Css.Context ctx);

    @Override
    default int compareTo(Rule o) {
      int thisKind;
      thisKind = kind();

      int thatKind;
      thatKind = o.kind();

      if (thisKind == thatKind) {
        return compareSameKind(o);
      } else if (thisKind < thatKind) {
        return -1;
      } else {
        return 1;
      }
    }

    int compareSameKind(Rule o);

    int kind();

    void writeTo(StringBuilder out, Css.Indentation indentation);

    void writeProps(StringBuilder out, Css.Indentation indentation);

  }

  record StaticUtility(Css.Key key, CssProperties properties) {

    public final Css.Rule create(String className, Css.Modifier modifier) {
      return new CssUtility(key, className, modifier, properties);
    }

  }

  private record ThisStyleSheet(String css) implements StyleSheet {

    @Override
    public final byte[] toByteArray() {
      return css.getBytes(StandardCharsets.UTF_8);
    }

  }

  //
  // V
  //

  @FunctionalInterface
  interface ValueFormatter {

    String format(String value, boolean negative);

  }

  enum ValueType {
    // non-arbitrary
    STANDARD,

    // arbitrary
    ZERO,

    LENGTH,
    LENGTH_NEGATIVE,

    PERCENTAGE,
    PERCENTAGE_NEGATIVE,

    STRING {
      @Override
      final String get(String value) {
        return super.get(value).replace('_', ' ');
      }
    },

    INTEGER,
    INTEGER_NEGATIVE,

    DECIMAL,
    DECIMAL_NEGATIVE;

    String get(String value) {
      return value.substring(1, value.length() - 1);
    }
  }

  sealed interface Variant {}

  private Css() {}

  public static String generateCss(Option... options) {
    int len;
    len = options.length; // implicit null-check

    CssConfig config;
    config = new CssConfig();

    for (int i = 0; i < len; i++) {
      Option o;
      o = Check.notNull(options[i], "options[", i, "] == null");

      GeneratorOption option;
      option = GeneratorOption.cast(o);

      option.acceptCssConfig(config);
    }

    config.spec();

    CssGenerator round;
    round = new CssGenerator(config);

    return round.generate();
  }

  public static StyleSheet generateStyleSheet(Option... options) {
    String css;
    css = generateCss(options);

    return new ThisStyleSheet(css);
  }

  // options

  public static Option baseLayer(String contents) {
    Check.notNull(contents, "contents == null");

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.baseLayer(contents);
      }
    };
  }

  public static Option breakpoints(String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.breakpoints(properties);
      }
    };
  }

  public static Option classes(Class<?>... values) {
    Set<Class<?>> set;
    set = Set.of(values);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.classes(set);
      }
    };
  }

  public static Option classes(Set<Class<?>> values) {
    Set<Class<?>> set;
    set = Set.copyOf(values);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.classes(set);
      }
    };
  }

  public static Option components(String text) {
    Map<String, String> components;
    components = parseComponents(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        for (var entry : components.entrySet()) {
          String name;
          name = entry.getKey();

          String definition;
          definition = entry.getValue();

          config.addComponent(name, definition);
        }
      }
    };
  }

  /**
   * Option: extends the built-in or the overridden colors with the values
   * obtained from parsing the specified key-value configuration text.
   *
   * @param text
   *        the key-value configuration text
   *
   * @return a new configuration option
   */
  public static Option extendColors(String text) {
    return extend(Key._COLORS, text);
  }

  /**
   * Option: extends the built-in or the overridden spacing with the values
   * obtained from parsing the specified key-value configuration text.
   *
   * @param text
   *        the key-value configuration text
   *
   * @return a new configuration option
   */
  public static Option extendSpacing(String text) {
    return extend(Key._SPACING, text);
  }

  /**
   * Option: extends the built-in or the overridden z-index with the values
   * obtained from parsing the specified key-value configuration text.
   *
   * @param text
   *        the key-value configuration text
   *
   * @return a new configuration option
   */
  public static Option extendZIndex(String text) {
    return extend(Key.Z_INDEX, text);
  }

  private static Option extend(Key key, String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.extend(key, properties);
      }
    };
  }

  public static Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.noteSink(noteSink);
      }
    };
  }

  public static Option overrideBackgroundColor(String text) {
    return override(Key.BACKGROUND_COLOR, text);
  }

  public static Option overrideBorderColor(String text) {
    return override(Key.BORDER_COLOR, text);
  }

  public static Option overrideBorderWidth(String text) {
    return override(Key.BORDER_WIDTH, text);
  }

  /**
   * Option: overrides the built-in colors with the values obtained from parsing
   * the specified key-value configuration text.
   *
   * @param text
   *        the key-value configuration text
   *
   * @return a new configuration option
   */
  public static Option overrideColors(String text) {
    return override(Key._COLORS, text);
  }

  public static Option overrideContent(String text) {
    return override(Key.CONTENT, text);
  }

  public static Option overrideFill(String text) {
    return override(Key.FILL, text);
  }

  public static Option overrideFontSize(String text) {
    return override(Key.FONT_SIZE, text);
  }

  public static Option overrideFontWeight(String text) {
    return override(Key.FONT_WEIGHT, text);
  }

  public static Option overrideGridColumn(String text) {
    return override(Key.GRID_COLUMN, text);
  }

  public static Option overrideGridColumnEnd(String text) {
    return override(Key.GRID_COLUMN_END, text);
  }

  public static Option overrideGridColumnStart(String text) {
    return override(Key.GRID_COLUMN_START, text);
  }

  public static Option overrideGridTemplateColumns(String text) {
    return override(Key.GRID_TEMPLATE_COLUMNS, text);
  }

  public static Option overrideGridTemplateRows(String text) {
    return override(Key.GRID_TEMPLATE_ROWS, text);
  }

  public static Option overrideLetterSpacing(String text) {
    return override(Key.LETTER_SPACING, text);
  }

  public static Option overrideLineHeight(String text) {
    return override(Key.LINE_HEIGHT, text);
  }

  public static Option overrideOutlineColor(String text) {
    return override(Key.OUTLINE_COLOR, text);
  }

  /**
   * Option: overrides the built-in spacing with the values obtained from
   * parsing the specified key-value configuration text.
   *
   * @param text
   *        the key-value configuration text
   *
   * @return a new configuration option
   */
  public static Option overrideSpacing(String text) {
    return override(Key._SPACING, text);
  }

  public static Option overrideTextColor(String text) {
    return override(Key.TEXT_COLOR, text);
  }

  public static Option overrideZIndex(String text) {
    return override(Key.Z_INDEX, text);
  }

  private static Option override(Key key, String text) {
    CssProperties properties;
    properties = parseProperties(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.override(key, properties);
      }
    };
  }

  public static Option scanDirectory(Path directory) {
    Check.notNull(directory, "directory == null");

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.addDirectory(directory);
      }
    };
  }

  /**
   * Option: do not emit the CSS reset.
   *
   * @return a new CSS generation option
   */
  public static Option skipReset() {
    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.skipReset = true;
      }
    };
  }

  public static Option useLogicalProperties() {
    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.useLogicalProperties();
      }
    };
  }

  public static Option utility(String className, String text) {
    Check.notNull(className, "className == null");

    CssProperties properties;
    properties = parseProperties(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.addUtility(className, properties);
      }
    };
  }

  public static Option variants(String text) {
    CssProperties props;
    props = parseProperties(text);

    return new GeneratorOption() {
      @Override
      final void acceptCssConfig(CssConfig config) {
        config.addVariants(props);
      }
    };
  }

  // private stuff

  static Map<String, String> parseComponents(String text) {
    SequencedMap<String, String> map;
    map = Util.createSequencedMap();

    String name;
    name = "";

    List<String> definition;
    definition = Util.createList();

    String[] lines;
    lines = text.split("\n");

    for (int number = 0; number < lines.length; number++) {
      String line = lines[number];

      if (line.isBlank()) {
        continue;
      }

      char first;
      first = line.charAt(0);

      if (first != '#') {
        definition.add(line);

        continue;
      }

      if (!definition.isEmpty()) {
        String value;
        value = Util.join(definition, " ");

        map.put(name, value);

        definition.clear();
      }

      name = line.substring(1).trim();

      if (name.isBlank()) {
        throw new IllegalArgumentException("No component name defined @ line " + (number + 1));
      }
    }

    if (!definition.isEmpty()) {
      String value;
      value = Util.join(definition, " ");

      map.put(name, value);
    }

    return Util.toUnmodifiableMap(map);
  }

  static CssProperties parseProperties(String text) {
    CssProperties.Builder builder;
    builder = new CssProperties.Builder();

    String[] lines;
    lines = text.split("\n");

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int colon;
      colon = line.indexOf(':');

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(0, colon);

      String value;
      value = line.substring(colon + 1);

      builder.add(key.trim(), value.trim());
    }

    return builder.build();
  }

  static Map<String, CssProperties> parseTable(String text) {
    Map<String, CssProperties> map;
    map = Util.createMap();

    String[] lines;
    lines = text.split("\n");

    String className;
    className = null;

    CssProperties.Builder props;
    props = null;

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int pipe;
      pipe = line.indexOf('|');

      if (pipe < 0) {
        throw new IllegalArgumentException(
            "The vertical bar character '|' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String maybeClass;
      maybeClass = line.substring(0, pipe);

      maybeClass = maybeClass.trim();

      if (!maybeClass.isEmpty()) {
        if (className != null) {
          map.put(className, props.build());
        }

        className = maybeClass;

        props = new CssProperties.Builder();
      }

      int colon;
      colon = line.indexOf(':', pipe);

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(pipe + 1, colon);

      String value;
      value = line.substring(colon + 1);

      props.add(key.trim(), value.trim());
    }

    if (className != null) {
      map.put(className, props.build());
    }

    return Util.toUnmodifiableMap(map);
  }

  static Map<String, String> merge(String text, Map<String, String> more) {
    CssProperties properties;
    properties = parseProperties(text);

    return properties.toMap(more);
  }

  static Variant parseVariant(String formatString) {
    int amper;
    amper = formatString.indexOf('&');

    if (amper < 0) {
      return new InvalidVariant(formatString, "Format string must contain exactly one '&' character");
    }

    String before;
    before = formatString.substring(0, amper);

    String after;
    after = formatString.substring(amper + 1);

    int anotherAmper;
    anotherAmper = after.indexOf('&');

    if (anotherAmper > 0) {
      return new InvalidVariant(formatString, "Format string must contain exactly one '&' character");
    }

    return new ClassNameFormat(before, after);
  }

  private enum Parser {

    START,
    STOP,

    NEGATIVE_OR_PROPERTY,

    PROPERTY,

    ZERO,

    NUMERIC,
    NUMERIC_DOT,

    LENGTH,

    PERCENTAGE,

    FLOAT;

  }

  static ValueType typeOf(String s) {
    int length;
    length = s.length();

    if (length < 2) {
      return ValueType.STANDARD;
    }

    if (s.charAt(0) != '[' || s.charAt(length - 1) != ']') {
      return ValueType.STANDARD;
    }

    Parser parser;
    parser = Parser.START;

    boolean negative;
    negative = false;

    for (int i = 1, end = length - 1; i < end; i++) {
      char c;
      c = s.charAt(i);

      parser = switch (parser) {
        case START -> {
          if (c == '0') {
            yield Parser.ZERO;
          }

          if (isDigit(c)) {
            yield Parser.NUMERIC;
          }

          if (c == '-') {
            yield Parser.NEGATIVE_OR_PROPERTY;
          }

          yield Parser.STOP;
        }

        case ZERO -> {
          if (c == '.') {
            throw new UnsupportedOperationException("Implement me");
          }

          if (isDigit(c)) {
            yield Parser.NUMERIC;
          }

          yield Parser.STOP;
        }

        case NEGATIVE_OR_PROPERTY -> {
          if (isDigit(c)) {
            negative = true;

            yield Parser.NUMERIC;
          }

          if (c == '-') {
            yield Parser.PROPERTY;
          }

          yield Parser.STOP;
        }

        case PROPERTY -> throw new UnsupportedOperationException("Implement me");

        case NUMERIC -> {
          if (isDigit(c)) {
            yield Parser.NUMERIC;
          }

          if (isLetter(c)) {
            yield Parser.LENGTH;
          }

          if (c == '.') {
            yield Parser.NUMERIC_DOT;
          }

          if (c == '%') {
            yield Parser.PERCENTAGE;
          }

          yield Parser.STOP;
        }

        case NUMERIC_DOT -> {
          if (isDigit(c)) {
            yield Parser.FLOAT;
          }

          yield Parser.STOP;
        }

        case FLOAT -> {
          if (isDigit(c)) {
            yield Parser.FLOAT;
          }

          if (isLetter(c)) {
            yield Parser.LENGTH;
          }

          if (c == '%') {
            yield Parser.PERCENTAGE;
          }

          yield Parser.STOP;
        }

        case LENGTH -> {
          if (isLetter(c)) {
            yield parser;
          }

          yield Parser.STOP;
        }

        case PERCENTAGE -> Parser.STOP;

        case STOP -> throw new AssertionError();
      };

      if (parser == Parser.STOP) {
        break;
      }
    }

    return switch (parser) {
      case ZERO -> ValueType.ZERO;

      case NUMERIC -> negative ? ValueType.INTEGER_NEGATIVE : ValueType.INTEGER;

      case FLOAT -> negative ? ValueType.DECIMAL_NEGATIVE : ValueType.DECIMAL;

      case LENGTH -> negative ? ValueType.LENGTH_NEGATIVE : ValueType.LENGTH;

      case PERCENTAGE -> negative ? ValueType.PERCENTAGE_NEGATIVE : ValueType.PERCENTAGE;

      default -> ValueType.STRING;
    };
  }

  private static boolean isDigit(char c) {
    return '0' <= c && c <= '9';
  }

  private static boolean isLetter(char c) {
    return 'A' <= c && c <= 'Z'
        || 'a' <= c && c <= 'z';
  }

  static void writeClassName(StringBuilder out, String className) {
    int length;
    length = className.length();

    if (length == 0) {
      return;
    }

    out.append('.');

    int index;
    index = 0;

    boolean escaped;
    escaped = false;

    char first;
    first = className.charAt(index);

    if (0x30 <= first && first <= 0x39) {
      out.append("\\3");
      out.append(first);

      index++;

      escaped = true;
    }

    for (; index < length; index++) {
      char c;
      c = className.charAt(index);

      switch (c) {
        case ',' -> {
          out.append("\\");

          out.append(Integer.toHexString(c));

          out.append(' ');

          escaped = false;
        }

        case ' ', '.', '/', ':', '@', '(', ')', '[', ']', '*', '%' -> {
          out.append("\\");

          out.append(c);

          escaped = false;
        }

        case 'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
          if (escaped) {
            out.append(' ');
          }

          out.append(c);

          escaped = false;
        }

        default -> out.append(c);
      }
    }
  }

  static final String DEFAULT_COLORS = """
      inherit: inherit
      current: currentColor
      transparent: transparent

      black: #000000
      white: #ffffff

      slate-50: #f8fafc
      slate-100: #f1f5f9
      slate-200: #e2e8f0
      slate-300: #cbd5e1
      slate-400: #94a3b8
      slate-500: #64748b
      slate-600: #475569
      slate-700: #334155
      slate-800: #1e293b
      slate-900: #0f172a
      slate-950: #020617

      gray-50: #f9fafb
      gray-100: #f3f4f6
      gray-200: #e5e7eb
      gray-300: #d1d5db
      gray-400: #9ca3af
      gray-500: #6b7280
      gray-600: #4b5563
      gray-700: #374151
      gray-800: #1f2937
      gray-900: #111827
      gray-950: #030712

      zinc-50: #fafafa
      zinc-100: #f4f4f5
      zinc-200: #e4e4e7
      zinc-300: #d4d4d8
      zinc-400: #a1a1aa
      zinc-500: #71717a
      zinc-600: #52525b
      zinc-700: #3f3f46
      zinc-800: #27272a
      zinc-900: #18181b
      zinc-950: #09090b

      neutral-50: #fafafa
      neutral-100: #f5f5f5
      neutral-200: #e5e5e5
      neutral-300: #d4d4d4
      neutral-400: #a3a3a3
      neutral-500: #737373
      neutral-600: #525252
      neutral-700: #404040
      neutral-800: #262626
      neutral-900: #171717
      neutral-950: #0a0a0a

      stone-50: #fafaf9
      stone-100: #f5f5f4
      stone-200: #e7e5e4
      stone-300: #d6d3d1
      stone-400: #a8a29e
      stone-500: #78716c
      stone-600: #57534e
      stone-700: #44403c
      stone-800: #292524
      stone-900: #1c1917
      stone-950: #0c0a09

      red-50: #fef2f2
      red-100: #fee2e2
      red-200: #fecaca
      red-300: #fca5a5
      red-400: #f87171
      red-500: #ef4444
      red-600: #dc2626
      red-700: #b91c1c
      red-800: #991b1b
      red-900: #7f1d1d
      red-950: #450a0a

      orange-50: #fff7ed
      orange-100: #ffedd5
      orange-200: #fed7aa
      orange-300: #fdba74
      orange-400: #fb923c
      orange-500: #f97316
      orange-600: #ea580c
      orange-700: #c2410c
      orange-800: #9a3412
      orange-900: #7c2d12
      orange-950: #431407

      amber-50: #fffbeb
      amber-100: #fef3c7
      amber-200: #fde68a
      amber-300: #fcd34d
      amber-400: #fbbf24
      amber-500: #f59e0b
      amber-600: #d97706
      amber-700: #b45309
      amber-800: #92400e
      amber-900: #78350f
      amber-950: #451a03

      yellow-50: #fefce8
      yellow-100: #fef9c3
      yellow-200: #fef08a
      yellow-300: #fde047
      yellow-400: #facc15
      yellow-500: #eab308
      yellow-600: #ca8a04
      yellow-700: #a16207
      yellow-800: #854d0e
      yellow-900: #713f12
      yellow-950: #422006

      lime-50: #f7fee7
      lime-100: #ecfccb
      lime-200: #d9f99d
      lime-300: #bef264
      lime-400: #a3e635
      lime-500: #84cc16
      lime-600: #65a30d
      lime-700: #4d7c0f
      lime-800: #3f6212
      lime-900: #365314
      lime-950: #1a2e05

      green-50: #f0fdf4
      green-100: #dcfce7
      green-200: #bbf7d0
      green-300: #86efac
      green-400: #4ade80
      green-500: #22c55e
      green-600: #16a34a
      green-700: #15803d
      green-800: #166534
      green-900: #14532d
      green-950: #052e16

      emerald-50: #ecfdf5
      emerald-100: #d1fae5
      emerald-200: #a7f3d0
      emerald-300: #6ee7b7
      emerald-400: #34d399
      emerald-500: #10b981
      emerald-600: #059669
      emerald-700: #047857
      emerald-800: #065f46
      emerald-900: #064e3b
      emerald-950: #022c22

      teal-50: #f0fdfa
      teal-100: #ccfbf1
      teal-200: #99f6e4
      teal-300: #5eead4
      teal-400: #2dd4bf
      teal-500: #14b8a6
      teal-600: #0d9488
      teal-700: #0f766e
      teal-800: #115e59
      teal-900: #134e4a
      teal-950: #042f2e

      cyan-50: #ecfeff
      cyan-100: #cffafe
      cyan-200: #a5f3fc
      cyan-300: #67e8f9
      cyan-400: #22d3ee
      cyan-500: #06b6d4
      cyan-600: #0891b2
      cyan-700: #0e7490
      cyan-800: #155e75
      cyan-900: #164e63
      cyan-950: #083344

      sky-50: #f0f9ff
      sky-100: #e0f2fe
      sky-200: #bae6fd
      sky-300: #7dd3fc
      sky-400: #38bdf8
      sky-500: #0ea5e9
      sky-600: #0284c7
      sky-700: #0369a1
      sky-800: #075985
      sky-900: #0c4a6e
      sky-950: #082f49

      blue-50: #eff6ff
      blue-100: #dbeafe
      blue-200: #bfdbfe
      blue-300: #93c5fd
      blue-400: #60a5fa
      blue-500: #3b82f6
      blue-600: #2563eb
      blue-700: #1d4ed8
      blue-800: #1e40af
      blue-900: #1e3a8a
      blue-950: #172554

      indigo-50: #eef2ff
      indigo-100: #e0e7ff
      indigo-200: #c7d2fe
      indigo-300: #a5b4fc
      indigo-400: #818cf8
      indigo-500: #6366f1
      indigo-600: #4f46e5
      indigo-700: #4338ca
      indigo-800: #3730a3
      indigo-900: #312e81
      indigo-950: #1e1b4b

      violet-50: #f5f3ff
      violet-100: #ede9fe
      violet-200: #ddd6fe
      violet-300: #c4b5fd
      violet-400: #a78bfa
      violet-500: #8b5cf6
      violet-600: #7c3aed
      violet-700: #6d28d9
      violet-800: #5b21b6
      violet-900: #4c1d95
      violet-950: #2e1065

      purple-50: #faf5ff
      purple-100: #f3e8ff
      purple-200: #e9d5ff
      purple-300: #d8b4fe
      purple-400: #c084fc
      purple-500: #a855f7
      purple-600: #9333ea
      purple-700: #7e22ce
      purple-800: #6b21a8
      purple-900: #581c87
      purple-950: #3b0764

      fuchsia-50: #fdf4ff
      fuchsia-100: #fae8ff
      fuchsia-200: #f5d0fe
      fuchsia-300: #f0abfc
      fuchsia-400: #e879f9
      fuchsia-500: #d946ef
      fuchsia-600: #c026d3
      fuchsia-700: #a21caf
      fuchsia-800: #86198f
      fuchsia-900: #701a75
      fuchsia-950: #4a044e

      pink-50: #fdf2f8
      pink-100: #fce7f3
      pink-200: #fbcfe8
      pink-300: #f9a8d4
      pink-400: #f472b6
      pink-500: #ec4899
      pink-600: #db2777
      pink-700: #be185d
      pink-800: #9d174d
      pink-900: #831843
      pink-950: #500724

      rose-50: #fff1f2
      rose-100: #ffe4e6
      rose-200: #fecdd3
      rose-300: #fda4af
      rose-400: #fb7185
      rose-500: #f43f5e
      rose-600: #e11d48
      rose-700: #be123c
      rose-800: #9f1239
      rose-900: #881337
      rose-950: #4c0519
      """;

  static final String DEFAULT_LINE_HEIGHT = """
      3: 0.75rem
      4: 1rem
      5: 1.25rem
      6: 1.5rem
      7: 1.75rem
      8: 2rem
      9: 2.25rem
      10: 2.5rem
      none: 1
      tight: 1.25
      snug: 1.375
      normal: 1.5
      relaxed: 1.625
      loose: 2
      """;

  static final String DEFAULT_SPACING = """
      px: 1px
      0: 0px
      0.5: 0.125rem
      1: 0.25rem
      1.5: 0.375rem
      2: 0.5rem
      2.5: 0.625rem
      3: 0.75rem
      3.5: 0.875rem
      4: 1rem
      5: 1.25rem
      6: 1.5rem
      7: 1.75rem
      8: 2rem
      9: 2.25rem
      10: 2.5rem
      11: 2.75rem
      12: 3rem
      14: 3.5rem
      16: 4rem
      20: 5rem
      24: 6rem
      28: 7rem
      32: 8rem
      36: 9rem
      40: 10rem
      44: 11rem
      48: 12rem
      52: 13rem
      56: 14rem
      60: 15rem
      64: 16rem
      72: 18rem
      80: 20rem
      96: 24rem
      """;

}