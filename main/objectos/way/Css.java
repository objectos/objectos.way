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
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;
import java.util.function.Consumer;

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
  public sealed interface StyleSheet extends Lang.MediaObject permits CssStyleSheet {

    /**
     * Configures the generation of a CSS style sheet.
     */
    sealed interface Config permits CssEngine {

      void noteSink(Note.Sink value);

      void scanClass(Class<?> value);

      void scanDirectory(Path value);

      void theme(String value);

    }

    static StyleSheet generate(Consumer<Config> config) {
      CssEngine engine;
      engine = new CssEngine();

      config.accept(engine);

      engine.execute();

      String css;
      css = engine.generate();

      return new CssStyleSheet(css);
    }

    @Override
    default String contentType() {
      return "text/css; charset=utf-8";
    }

    String css();

  }

  //
  // non-public types
  //

  //
  // A
  //

  record AtRuleVariant(String rule) implements MediaQuery {

    @Override
    public final int compareTo(MediaQuery o) {
      if (o instanceof AtRuleVariant that) {
        return rule.compareTo(that.rule);
      } else {
        return 1;
      }
    }

    @Override
    public final void writeMediaQueryStart(StringBuilder out, Indentation indentation) {
      indentation.writeTo(out);

      out.append(rule);
      out.append(" {");
      out.append(System.lineSeparator());
    }

  }

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

  enum Key {

    ALIGN_CONTENT(true),
    ALIGN_ITEMS(true),
    ALIGN_SELF(true),
    APPEARANCE(true),
    ASPECT_RATIO(true),

    BACKGROUND_COLOR(true),
    BORDER(true),
    /**/BORDER_TOP(true),
    /**/BORDER_RIGHT(true),
    /**/BORDER_BOTTOM(true),
    /**/BORDER_LEFT(true),
    BORDER_COLLAPSE(true),
    BORDER_SPACING(true),
    BORDER_RADIUS(true),
    BOTTOM(true),
    BOX_SHADOW(true),

    CLEAR(true),
    COLOR(true),
    COLUMN_GAP(true),
    CONTENT(true),
    CURSOR(true),

    DISPLAY(true),

    FLEX(true),
    FLEX_BASIS(true),
    FLEX_DIRECTION(true),
    FLEX_GROW(true),
    FLEX_SHRINK(true),
    FLEX_WRAP(true),
    FLOAT(true),
    FILL(true),
    FONT_SIZE(true),
    FONT_STYLE(true),
    FONT_WEIGHT(true),

    GAP(true),
    GRID_COLUMN(true),
    GRID_COLUMN_END(true),
    GRID_COLUMN_START(true),
    GRID_TEMPLATE(true),
    GRID_TEMPLATE_COLUMNS(true),
    GRID_TEMPLATE_ROWS(true),

    HEIGHT(true),

    INSET(true),

    JUSTIFY_CONTENT(true),
    JUSTIFY_ITEMS(true),
    JUSTIFY_SELF(true),

    LEFT(true),
    LETTER_SPACING(true),
    LINE_HEIGHT(true),
    LIST_STYLE_TYPE(true),

    MARGIN(true),
    /**/MARGIN_TOP(true),
    /**/MARGIN_RIGHT(true),
    /**/MARGIN_BOTTOM(true),
    /**/MARGIN_LEFT(true),
    /**/MARGIN_BLOCK(true),
    /**/MARGIN_INLINE(true),
    MAX_HEIGHT(true),
    MAX_WIDTH(true),
    MIN_HEIGHT(true),
    MIN_WIDTH(true),

    OPACITY(true),
    OUTLINE(true),
    OUTLINE_COLOR(true),
    OUTLINE_OFFSET(true),
    OUTLINE_STYLE(true),
    OUTLINE_WIDTH(true),
    OVERFLOW(true),
    OVERFLOW_X(true),
    OVERFLOW_Y(true),

    PADDING(true),
    /**/PADDING_TOP(true),
    /**/PADDING_RIGHT(true),
    /**/PADDING_BOTTOM(true),
    /**/PADDING_LEFT(true),
    POINTER_EVENTS(true),
    POSITION(true),

    RIGHT(true),
    ROTATE(true),
    ROW_GAP(true),

    SCROLL_BEHAVIOR(true),
    STROKE(true),
    STROKE_OPACITY(true),
    STROKE_WIDTH(true),

    TAB_SIZE(true),
    TABLE_LAYOUT(true),
    TEXT_ALIGN(true),
    TEXT_COLOR(true),
    TEXT_DECORATION(true),
    /**/TEXT_DECORATION_COLOR(true),
    /**/TEXT_DECORATION_LINE(true),
    /**/TEXT_DECORATION_STYLE(true),
    /**/TEXT_DECORATION_THICKNESS(true),
    TEXT_OVERFLOW(true),
    TEXT_SHADOW(true),
    TEXT_TRANSFORM(true),
    TEXT_WRAP(true),
    TOP(true),
    TRANSFORM(true),
    TRANSFORM_ORIGIN(true),
    TRANSITION(true),
    TRANSITION_DELAY(true),
    TRANSITION_DURATION(true),
    TRANSITION_PROPERTY(true),
    TRANSITION_TIMING_FUNCTION(true),
    TRANSLATE(true),

    USER_SELECT(true),

    VERTICAL_ALIGN(true),
    VISIBILITY(true),

    WHITE_SPACE(true),
    WIDTH(true),
    WORD_SPACING(true),

    Z_INDEX(true),

    // legacy and to be removed

    _COLORS,

    _SPACING,

    CUSTOM,

    ACCESSIBILITY,

    INSET_X,
    INSET_Y,

    MARGIN_X,
    MARGIN_Y,

    SIZE,

    BORDER_SPACING_X,
    BORDER_SPACING_Y,

    TRANSLATE_X,
    TRANSLATE_Y,

    GAP_X,
    GAP_Y,

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

    PADDING_X,
    PADDING_Y,

    BOX_SHADOW_COLOR,

    RING_WIDTH,
    RING_COLOR,
    RING_OFFSET_WIDTH,
    RING_OFFSET_COLOR,
    RING_INSET;

    final boolean engineCompatible;

    final String propertyName = name().replace('_', '-').toLowerCase(Locale.US);

    private Key() {
      this(false);
    }

    private Key(boolean engineCompatible) {
      this.engineCompatible = engineCompatible;
    }

  }

  //
  // L
  //

  enum Layer {
    THEME,
    BASE,
    COMPONENTS,
    UTILITIES;
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

  enum Namespace {

    BREAKPOINT,

    COLOR,

    CUSTOM;

  }

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

  //
  // V
  //

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

    return new CssStyleSheet(css);
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

  public static Option noteSink(Note.Sink noteSink) {
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
      throw new IllegalArgumentException("Format string must contain exactly one '&' character: " + formatString);
    }

    String before;
    before = formatString.substring(0, amper);

    String after;
    after = formatString.substring(amper + 1);

    int anotherAmper;
    anotherAmper = after.indexOf('&');

    if (anotherAmper > 0) {
      throw new IllegalArgumentException("Format string must contain exactly one '&' character: " + formatString);
    }

    return new ClassNameFormat(before, after);
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

  //
  // current version:
  // https://github.com/tailwindlabs/tailwindcss/blob/961e8da8fd0b2d96c49a5907bd9df70efcacccf7/packages/tailwindcss/preflight.css
  //
  static String defaultBase() {
    return """
    /*
      1. Prevent padding and border from affecting element width. (https://github.com/mozdevs/cssremedy/issues/4)
      2. Remove default margins and padding
      3. Reset all borders.
    */

    *,
    ::after,
    ::before,
    ::backdrop,
    ::file-selector-button {
      box-sizing: border-box; /* 1 */
      margin: 0; /* 2 */
      padding: 0; /* 2 */
      border: 0 solid; /* 3 */
    }

    /*
      1. Use a consistent sensible line-height in all browsers.
      2. Prevent adjustments of font size after orientation changes in iOS.
      3. Use a more readable tab size.
      4. Use the user's configured `sans` font-family by default.
      5. Use the user's configured `sans` font-feature-settings by default.
      6. Use the user's configured `sans` font-variation-settings by default.
      7. Disable tap highlights on iOS.
    */

    html,
    :host {
      line-height: 1.5; /* 1 */
      -webkit-text-size-adjust: 100%; /* 2 */
      tab-size: 4; /* 3 */
      font-family: var(
        --default-font-family,
        ui-sans-serif,
        system-ui,
        sans-serif,
        'Apple Color Emoji',
        'Segoe UI Emoji',
        'Segoe UI Symbol',
        'Noto Color Emoji'
      ); /* 4 */
      font-feature-settings: var(--default-font-feature-settings, normal); /* 5 */
      font-variation-settings: var(--default-font-variation-settings, normal); /* 6 */
      -webkit-tap-highlight-color: transparent; /* 7 */
    }

    /*
      Inherit line-height from `html` so users can set them as a class directly on the `html` element.
    */

    body {
      line-height: inherit;
    }

    /*
      1. Add the correct height in Firefox.
      2. Correct the inheritance of border color in Firefox. (https://bugzilla.mozilla.org/show_bug.cgi?id=190655)
      3. Reset the default border style to a 1px solid border.
    */

    hr {
      height: 0; /* 1 */
      color: inherit; /* 2 */
      border-top-width: 1px; /* 3 */
    }

    /*
      Add the correct text decoration in Chrome, Edge, and Safari.
    */

    abbr:where([title]) {
      -webkit-text-decoration: underline dotted;
      text-decoration: underline dotted;
    }

    /*
      Remove the default font size and weight for headings.
    */

    h1,
    h2,
    h3,
    h4,
    h5,
    h6 {
      font-size: inherit;
      font-weight: inherit;
    }

    /*
      Reset links to optimize for opt-in styling instead of opt-out.
    */

    a {
      color: inherit;
      -webkit-text-decoration: inherit;
      text-decoration: inherit;
    }

    /*
      Add the correct font weight in Edge and Safari.
    */

    b,
    strong {
      font-weight: bolder;
    }

    /*
      1. Use the user's configured `mono` font-family by default.
      2. Use the user's configured `mono` font-feature-settings by default.
      3. Use the user's configured `mono` font-variation-settings by default.
      4. Correct the odd `em` font sizing in all browsers.
    */

    code,
    kbd,
    samp,
    pre {
      font-family: var(
        --default-mono-font-family,
        ui-monospace,
        SFMono-Regular,
        Menlo,
        Monaco,
        Consolas,
        'Liberation Mono',
        'Courier New',
        monospace
      ); /* 4 */
      font-feature-settings: var(--default-mono-font-feature-settings, normal); /* 5 */
      font-variation-settings: var(--default-mono-font-variation-settings, normal); /* 6 */
      font-size: 1em; /* 4 */
    }

    /*
      Add the correct font size in all browsers.
    */

    small {
      font-size: 80%;
    }

    /*
      Prevent `sub` and `sup` elements from affecting the line height in all browsers.
    */

    sub,
    sup {
      font-size: 75%;
      line-height: 0;
      position: relative;
      vertical-align: baseline;
    }

    sub {
      bottom: -0.25em;
    }

    sup {
      top: -0.5em;
    }

    /*
      1. Remove text indentation from table contents in Chrome and Safari. (https://bugs.chromium.org/p/chromium/issues/detail?id=999088, https://bugs.webkit.org/show_bug.cgi?id=201297)
      2. Correct table border color inheritance in all Chrome and Safari. (https://bugs.chromium.org/p/chromium/issues/detail?id=935729, https://bugs.webkit.org/show_bug.cgi?id=195016)
      3. Remove gaps between table borders by default.
    */

    table {
      text-indent: 0; /* 1 */
      border-color: inherit; /* 2 */
      border-collapse: collapse; /* 3 */
    }

    /*
      Use the modern Firefox focus style for all focusable elements.
    */

    :-moz-focusring {
      outline: auto;
    }

    /*
      Add the correct vertical alignment in Chrome and Firefox.
    */

    progress {
      vertical-align: baseline;
    }

    /*
      Add the correct display in Chrome and Safari.
    */

    summary {
      display: list-item;
    }

    /*
      Make lists unstyled by default.
    */

    ol,
    ul,
    menu {
      list-style: none;
    }

    /*
      1. Make replaced elements `display: block` by default. (https://github.com/mozdevs/cssremedy/issues/14)
      2. Add `vertical-align: middle` to align replaced elements more sensibly by default. (https://github.com/jensimmons/cssremedy/issues/14#issuecomment-634934210)
          This can trigger a poorly considered lint error in some tools but is included by design.
    */

    img,
    svg,
    video,
    canvas,
    audio,
    iframe,
    embed,
    object {
      display: block; /* 1 */
      vertical-align: middle; /* 2 */
    }

    /*
      Constrain images and videos to the parent width and preserve their intrinsic aspect ratio. (https://github.com/mozdevs/cssremedy/issues/14)
    */

    img,
    video {
      max-width: 100%;
      height: auto;
    }

    /*
      1. Inherit font styles in all browsers.
      2. Remove border radius in all browsers.
      3. Remove background color in all browsers.
      4. Ensure consistent opacity for disabled states in all browsers.
    */

    button,
    input,
    select,
    optgroup,
    textarea,
    ::file-selector-button {
      font: inherit; /* 1 */
      font-feature-settings: inherit; /* 1 */
      font-variation-settings: inherit; /* 1 */
      letter-spacing: inherit; /* 1 */
      color: inherit; /* 1 */
      border-radius: 0; /* 2 */
      background-color: transparent; /* 3 */
      opacity: 1; /* 4 */
    }

    /*
      Restore default font weight.
    */

    :where(select:is([multiple], [size])) optgroup {
      font-weight: bolder;
    }

    /*
      Restore indentation.
    */

    :where(select:is([multiple], [size])) optgroup option {
      padding-inline-start: 20px;
    }

    /*
      Restore space after button.
    */

    ::file-selector-button {
      margin-inline-end: 4px;
    }

    /*
      1. Reset the default placeholder opacity in Firefox. (https://github.com/tailwindlabs/tailwindcss/issues/3300)
      2. Set the default placeholder color to a semi-transparent version of the current text color.
    */

    ::placeholder {
      opacity: 1; /* 1 */
      color: color-mix(in oklab, currentColor 50%, transparent); /* 2 */
    }

    /*
      Prevent resizing textareas horizontally by default.
    */

    textarea {
      resize: vertical;
    }

    /*
      Remove the inner padding in Chrome and Safari on macOS.
    */

    ::-webkit-search-decoration {
      -webkit-appearance: none;
    }

    /*
      1. Ensure date/time inputs have the same height when empty in iOS Safari.
      2. Ensure text alignment can be changed on date/time inputs in iOS Safari.
    */

    ::-webkit-date-and-time-value {
      min-height: 1lh; /* 1 */
      text-align: inherit; /* 2 */
    }

    /*
      Prevent height from changing on date/time inputs in macOS Safari when the input is set to `display: block`.
    */

    ::-webkit-datetime-edit {
      display: inline-flex;
    }

    /*
      Remove excess padding from pseudo-elements in date/time inputs to ensure consistent height across browsers.
    */

    ::-webkit-datetime-edit-fields-wrapper {
      padding: 0;
    }

    ::-webkit-datetime-edit,
    ::-webkit-datetime-edit-year-field,
    ::-webkit-datetime-edit-month-field,
    ::-webkit-datetime-edit-day-field,
    ::-webkit-datetime-edit-hour-field,
    ::-webkit-datetime-edit-minute-field,
    ::-webkit-datetime-edit-second-field,
    ::-webkit-datetime-edit-millisecond-field,
    ::-webkit-datetime-edit-meridiem-field {
      padding-block: 0;
    }

    /*
      Remove the additional `:invalid` styles in Firefox. (https://github.com/mozilla/gecko-dev/blob/2f9eacd9d3d995c937b4251a5557d95d494c9be1/layout/style/res/forms.css#L728-L737)
    */

    :-moz-ui-invalid {
      box-shadow: none;
    }

    /*
      Correct the inability to style the border radius in iOS Safari.
    */

    button,
    input:where([type='button'], [type='reset'], [type='submit']),
    ::file-selector-button {
      appearance: button;
    }

    /*
      Correct the cursor style of increment and decrement buttons in Safari.
    */

    ::-webkit-inner-spin-button,
    ::-webkit-outer-spin-button {
      height: auto;
    }

    /*
      Make elements with the HTML hidden attribute stay hidden by default.
    */

    [hidden]:where(:not([hidden='until-found'])) {
      display: none !important;
    }
    """;
  }

  //
  // current version:
  // https://github.com/tailwindlabs/tailwindcss/blob/aa15964b28ab9858ac0055082741c2f95f20a920/packages/tailwindcss/theme.css
  //
  static String defaultTheme() {
    return """
    --color-red-50: oklch(0.971 0.013 17.38);
    --color-red-100: oklch(0.936 0.032 17.717);
    --color-red-200: oklch(0.885 0.062 18.334);
    --color-red-300: oklch(0.808 0.114 19.571);
    --color-red-400: oklch(0.704 0.191 22.216);
    --color-red-500: oklch(0.637 0.237 25.331);
    --color-red-600: oklch(0.577 0.245 27.325);
    --color-red-700: oklch(0.505 0.213 27.518);
    --color-red-800: oklch(0.444 0.177 26.899);
    --color-red-900: oklch(0.396 0.141 25.723);
    --color-red-950: oklch(0.258 0.092 26.042);

    --color-orange-50: oklch(0.98 0.016 73.684);
    --color-orange-100: oklch(0.954 0.038 75.164);
    --color-orange-200: oklch(0.901 0.076 70.697);
    --color-orange-300: oklch(0.837 0.128 66.29);
    --color-orange-400: oklch(0.75 0.183 55.934);
    --color-orange-500: oklch(0.705 0.213 47.604);
    --color-orange-600: oklch(0.646 0.222 41.116);
    --color-orange-700: oklch(0.553 0.195 38.402);
    --color-orange-800: oklch(0.47 0.157 37.304);
    --color-orange-900: oklch(0.408 0.123 38.172);
    --color-orange-950: oklch(0.266 0.079 36.259);

    --color-amber-50: oklch(0.987 0.022 95.277);
    --color-amber-100: oklch(0.962 0.059 95.617);
    --color-amber-200: oklch(0.924 0.12 95.746);
    --color-amber-300: oklch(0.879 0.169 91.605);
    --color-amber-400: oklch(0.828 0.189 84.429);
    --color-amber-500: oklch(0.769 0.188 70.08);
    --color-amber-600: oklch(0.666 0.179 58.318);
    --color-amber-700: oklch(0.555 0.163 48.998);
    --color-amber-800: oklch(0.473 0.137 46.201);
    --color-amber-900: oklch(0.414 0.112 45.904);
    --color-amber-950: oklch(0.279 0.077 45.635);

    --color-yellow-50: oklch(0.987 0.026 102.212);
    --color-yellow-100: oklch(0.973 0.071 103.193);
    --color-yellow-200: oklch(0.945 0.129 101.54);
    --color-yellow-300: oklch(0.905 0.182 98.111);
    --color-yellow-400: oklch(0.852 0.199 91.936);
    --color-yellow-500: oklch(0.795 0.184 86.047);
    --color-yellow-600: oklch(0.681 0.162 75.834);
    --color-yellow-700: oklch(0.554 0.135 66.442);
    --color-yellow-800: oklch(0.476 0.114 61.907);
    --color-yellow-900: oklch(0.421 0.095 57.708);
    --color-yellow-950: oklch(0.286 0.066 53.813);

    --color-lime-50: oklch(0.986 0.031 120.757);
    --color-lime-100: oklch(0.967 0.067 122.328);
    --color-lime-200: oklch(0.938 0.127 124.321);
    --color-lime-300: oklch(0.897 0.196 126.665);
    --color-lime-400: oklch(0.841 0.238 128.85);
    --color-lime-500: oklch(0.768 0.233 130.85);
    --color-lime-600: oklch(0.648 0.2 131.684);
    --color-lime-700: oklch(0.532 0.157 131.589);
    --color-lime-800: oklch(0.453 0.124 130.933);
    --color-lime-900: oklch(0.405 0.101 131.063);
    --color-lime-950: oklch(0.274 0.072 132.109);

    --color-green-50: oklch(0.982 0.018 155.826);
    --color-green-100: oklch(0.962 0.044 156.743);
    --color-green-200: oklch(0.925 0.084 155.995);
    --color-green-300: oklch(0.871 0.15 154.449);
    --color-green-400: oklch(0.792 0.209 151.711);
    --color-green-500: oklch(0.723 0.219 149.579);
    --color-green-600: oklch(0.627 0.194 149.214);
    --color-green-700: oklch(0.527 0.154 150.069);
    --color-green-800: oklch(0.448 0.119 151.328);
    --color-green-900: oklch(0.393 0.095 152.535);
    --color-green-950: oklch(0.266 0.065 152.934);

    --color-emerald-50: oklch(0.979 0.021 166.113);
    --color-emerald-100: oklch(0.95 0.052 163.051);
    --color-emerald-200: oklch(0.905 0.093 164.15);
    --color-emerald-300: oklch(0.845 0.143 164.978);
    --color-emerald-400: oklch(0.765 0.177 163.223);
    --color-emerald-500: oklch(0.696 0.17 162.48);
    --color-emerald-600: oklch(0.596 0.145 163.225);
    --color-emerald-700: oklch(0.508 0.118 165.612);
    --color-emerald-800: oklch(0.432 0.095 166.913);
    --color-emerald-900: oklch(0.378 0.077 168.94);
    --color-emerald-950: oklch(0.262 0.051 172.552);

    --color-teal-50: oklch(0.984 0.014 180.72);
    --color-teal-100: oklch(0.953 0.051 180.801);
    --color-teal-200: oklch(0.91 0.096 180.426);
    --color-teal-300: oklch(0.855 0.138 181.071);
    --color-teal-400: oklch(0.777 0.152 181.912);
    --color-teal-500: oklch(0.704 0.14 182.503);
    --color-teal-600: oklch(0.6 0.118 184.704);
    --color-teal-700: oklch(0.511 0.096 186.391);
    --color-teal-800: oklch(0.437 0.078 188.216);
    --color-teal-900: oklch(0.386 0.063 188.416);
    --color-teal-950: oklch(0.277 0.046 192.524);

    --color-cyan-50: oklch(0.984 0.019 200.873);
    --color-cyan-100: oklch(0.956 0.045 203.388);
    --color-cyan-200: oklch(0.917 0.08 205.041);
    --color-cyan-300: oklch(0.865 0.127 207.078);
    --color-cyan-400: oklch(0.789 0.154 211.53);
    --color-cyan-500: oklch(0.715 0.143 215.221);
    --color-cyan-600: oklch(0.609 0.126 221.723);
    --color-cyan-700: oklch(0.52 0.105 223.128);
    --color-cyan-800: oklch(0.45 0.085 224.283);
    --color-cyan-900: oklch(0.398 0.07 227.392);
    --color-cyan-950: oklch(0.302 0.056 229.695);

    --color-sky-50: oklch(0.977 0.013 236.62);
    --color-sky-100: oklch(0.951 0.026 236.824);
    --color-sky-200: oklch(0.901 0.058 230.902);
    --color-sky-300: oklch(0.828 0.111 230.318);
    --color-sky-400: oklch(0.746 0.16 232.661);
    --color-sky-500: oklch(0.685 0.169 237.323);
    --color-sky-600: oklch(0.588 0.158 241.966);
    --color-sky-700: oklch(0.5 0.134 242.749);
    --color-sky-800: oklch(0.443 0.11 240.79);
    --color-sky-900: oklch(0.391 0.09 240.876);
    --color-sky-950: oklch(0.293 0.066 243.157);

    --color-blue-50: oklch(0.97 0.014 254.604);
    --color-blue-100: oklch(0.932 0.032 255.585);
    --color-blue-200: oklch(0.882 0.059 254.128);
    --color-blue-300: oklch(0.809 0.105 251.813);
    --color-blue-400: oklch(0.707 0.165 254.624);
    --color-blue-500: oklch(0.623 0.214 259.815);
    --color-blue-600: oklch(0.546 0.245 262.881);
    --color-blue-700: oklch(0.488 0.243 264.376);
    --color-blue-800: oklch(0.424 0.199 265.638);
    --color-blue-900: oklch(0.379 0.146 265.522);
    --color-blue-950: oklch(0.282 0.091 267.935);

    --color-indigo-50: oklch(0.962 0.018 272.314);
    --color-indigo-100: oklch(0.93 0.034 272.788);
    --color-indigo-200: oklch(0.87 0.065 274.039);
    --color-indigo-300: oklch(0.785 0.115 274.713);
    --color-indigo-400: oklch(0.673 0.182 276.935);
    --color-indigo-500: oklch(0.585 0.233 277.117);
    --color-indigo-600: oklch(0.511 0.262 276.966);
    --color-indigo-700: oklch(0.457 0.24 277.023);
    --color-indigo-800: oklch(0.398 0.195 277.366);
    --color-indigo-900: oklch(0.359 0.144 278.697);
    --color-indigo-950: oklch(0.257 0.09 281.288);

    --color-violet-50: oklch(0.969 0.016 293.756);
    --color-violet-100: oklch(0.943 0.029 294.588);
    --color-violet-200: oklch(0.894 0.057 293.283);
    --color-violet-300: oklch(0.811 0.111 293.571);
    --color-violet-400: oklch(0.702 0.183 293.541);
    --color-violet-500: oklch(0.606 0.25 292.717);
    --color-violet-600: oklch(0.541 0.281 293.009);
    --color-violet-700: oklch(0.491 0.27 292.581);
    --color-violet-800: oklch(0.432 0.232 292.759);
    --color-violet-900: oklch(0.38 0.189 293.745);
    --color-violet-950: oklch(0.283 0.141 291.089);

    --color-purple-50: oklch(0.977 0.014 308.299);
    --color-purple-100: oklch(0.946 0.033 307.174);
    --color-purple-200: oklch(0.902 0.063 306.703);
    --color-purple-300: oklch(0.827 0.119 306.383);
    --color-purple-400: oklch(0.714 0.203 305.504);
    --color-purple-500: oklch(0.627 0.265 303.9);
    --color-purple-600: oklch(0.558 0.288 302.321);
    --color-purple-700: oklch(0.496 0.265 301.924);
    --color-purple-800: oklch(0.438 0.218 303.724);
    --color-purple-900: oklch(0.381 0.176 304.987);
    --color-purple-950: oklch(0.291 0.149 302.717);

    --color-fuchsia-50: oklch(0.977 0.017 320.058);
    --color-fuchsia-100: oklch(0.952 0.037 318.852);
    --color-fuchsia-200: oklch(0.903 0.076 319.62);
    --color-fuchsia-300: oklch(0.833 0.145 321.434);
    --color-fuchsia-400: oklch(0.74 0.238 322.16);
    --color-fuchsia-500: oklch(0.667 0.295 322.15);
    --color-fuchsia-600: oklch(0.591 0.293 322.896);
    --color-fuchsia-700: oklch(0.518 0.253 323.949);
    --color-fuchsia-800: oklch(0.452 0.211 324.591);
    --color-fuchsia-900: oklch(0.401 0.17 325.612);
    --color-fuchsia-950: oklch(0.293 0.136 325.661);

    --color-pink-50: oklch(0.971 0.014 343.198);
    --color-pink-100: oklch(0.948 0.028 342.258);
    --color-pink-200: oklch(0.899 0.061 343.231);
    --color-pink-300: oklch(0.823 0.12 346.018);
    --color-pink-400: oklch(0.718 0.202 349.761);
    --color-pink-500: oklch(0.656 0.241 354.308);
    --color-pink-600: oklch(0.592 0.249 0.584);
    --color-pink-700: oklch(0.525 0.223 3.958);
    --color-pink-800: oklch(0.459 0.187 3.815);
    --color-pink-900: oklch(0.408 0.153 2.432);
    --color-pink-950: oklch(0.284 0.109 3.907);

    --color-rose-50: oklch(0.969 0.015 12.422);
    --color-rose-100: oklch(0.941 0.03 12.58);
    --color-rose-200: oklch(0.892 0.058 10.001);
    --color-rose-300: oklch(0.81 0.117 11.638);
    --color-rose-400: oklch(0.712 0.194 13.428);
    --color-rose-500: oklch(0.645 0.246 16.439);
    --color-rose-600: oklch(0.586 0.253 17.585);
    --color-rose-700: oklch(0.514 0.222 16.935);
    --color-rose-800: oklch(0.455 0.188 13.697);
    --color-rose-900: oklch(0.41 0.159 10.272);
    --color-rose-950: oklch(0.271 0.105 12.094);

    --color-slate-50: oklch(0.984 0.003 247.858);
    --color-slate-100: oklch(0.968 0.007 247.896);
    --color-slate-200: oklch(0.929 0.013 255.508);
    --color-slate-300: oklch(0.869 0.022 252.894);
    --color-slate-400: oklch(0.704 0.04 256.788);
    --color-slate-500: oklch(0.554 0.046 257.417);
    --color-slate-600: oklch(0.446 0.043 257.281);
    --color-slate-700: oklch(0.372 0.044 257.287);
    --color-slate-800: oklch(0.279 0.041 260.031);
    --color-slate-900: oklch(0.208 0.042 265.755);
    --color-slate-950: oklch(0.129 0.042 264.695);

    --color-gray-50: oklch(0.985 0.002 247.839);
    --color-gray-100: oklch(0.967 0.003 264.542);
    --color-gray-200: oklch(0.928 0.006 264.531);
    --color-gray-300: oklch(0.872 0.01 258.338);
    --color-gray-400: oklch(0.707 0.022 261.325);
    --color-gray-500: oklch(0.551 0.027 264.364);
    --color-gray-600: oklch(0.446 0.03 256.802);
    --color-gray-700: oklch(0.373 0.034 259.733);
    --color-gray-800: oklch(0.278 0.033 256.848);
    --color-gray-900: oklch(0.21 0.034 264.665);
    --color-gray-950: oklch(0.13 0.028 261.692);

    --color-zinc-50: oklch(0.985 0 0);
    --color-zinc-100: oklch(0.967 0.001 286.375);
    --color-zinc-200: oklch(0.92 0.004 286.32);
    --color-zinc-300: oklch(0.871 0.006 286.286);
    --color-zinc-400: oklch(0.705 0.015 286.067);
    --color-zinc-500: oklch(0.552 0.016 285.938);
    --color-zinc-600: oklch(0.442 0.017 285.786);
    --color-zinc-700: oklch(0.37 0.013 285.805);
    --color-zinc-800: oklch(0.274 0.006 286.033);
    --color-zinc-900: oklch(0.21 0.006 285.885);
    --color-zinc-950: oklch(0.141 0.005 285.823);

    --color-neutral-50: oklch(0.985 0 0);
    --color-neutral-100: oklch(0.97 0 0);
    --color-neutral-200: oklch(0.922 0 0);
    --color-neutral-300: oklch(0.87 0 0);
    --color-neutral-400: oklch(0.708 0 0);
    --color-neutral-500: oklch(0.556 0 0);
    --color-neutral-600: oklch(0.439 0 0);
    --color-neutral-700: oklch(0.371 0 0);
    --color-neutral-800: oklch(0.269 0 0);
    --color-neutral-900: oklch(0.205 0 0);
    --color-neutral-950: oklch(0.145 0 0);

    --color-stone-50: oklch(0.985 0.001 106.423);
    --color-stone-100: oklch(0.97 0.001 106.424);
    --color-stone-200: oklch(0.923 0.003 48.717);
    --color-stone-300: oklch(0.869 0.005 56.366);
    --color-stone-400: oklch(0.709 0.01 56.259);
    --color-stone-500: oklch(0.553 0.013 58.071);
    --color-stone-600: oklch(0.444 0.011 73.639);
    --color-stone-700: oklch(0.374 0.01 67.558);
    --color-stone-800: oklch(0.268 0.007 34.298);
    --color-stone-900: oklch(0.216 0.006 56.043);
    --color-stone-950: oklch(0.147 0.004 49.25);

    --color-black: #000;
    --color-white: #fff;

    --breakpoint-sm: 40rem;
    --breakpoint-md: 48rem;
    --breakpoint-lg: 64rem;
    --breakpoint-xl: 80rem;
    --breakpoint-2xl: 96rem;

    --rx: 16px;
    """;
  }

}