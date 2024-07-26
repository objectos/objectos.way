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

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.util.list.GrowableList;
import objectos.util.map.GrowableMap;

final class CssConfig {

  private NoteSink noteSink = NoOpNoteSink.of();

  private List<Css.Breakpoint> breakpoints = List.of(
      new Css.Breakpoint(0, "sm", "640px"),
      new Css.Breakpoint(1, "md", "768px"),
      new Css.Breakpoint(2, "lg", "1024px"),
      new Css.Breakpoint(3, "xl", "1280px"),
      new Css.Breakpoint(4, "2xl", "1536px")
  );

  private List<String> baseLayer;

  private Set<Class<?>> classes;

  private Map<String, String> components;

  private final Map<Css.Key, CssProperties> overrides = new EnumMap<>(Css.Key.class);

  private final Map<String, Set<Css.Key>> prefixes = new HashMap<>();

  private Css.PropertyType propertyType = Css.PHYSICAL;

  private final Map<Css.Key, CssResolver> resolvers = new EnumMap<>(Css.Key.class);

  private boolean skipReset;

  private final Map<String, Css.StaticUtility> staticUtilities = new GrowableMap<>();

  private Map<String, Css.Variant> variants;

  private boolean variantsInitialized;

  public CssConfig() {
  }

  // testing helper
  CssConfig(Class<?> type) {
    classes = Set.of(type);
  }

  public final void addComponent(String name, String definition) {
    if (components == null) {
      components = new GrowableMap<>();
    }

    String existing;
    existing = components.put(name, definition);

    if (existing != null) {
      throw new IllegalArgumentException(
          "The class name " + name + " is mapped to an existing component."
      );
    }
  }

  public final void addUtility(String className, CssProperties properties) {

    Css.StaticUtility utility;
    utility = new Css.StaticUtility(Css.Key.CUSTOM, properties);

    Css.StaticUtility maybeExisting;
    maybeExisting = staticUtilities.put(className, utility);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "The class name " + className + " is mapped to an existing utility."
      );
    }

  }

  public final void addVariants(CssProperties props) {
    if (variants == null) {
      variants = new GrowableMap<>();
    }

    for (Map.Entry<String, String> entry : props) {
      String variantName;
      variantName = entry.getKey();

      String formatString;
      formatString = entry.getValue();

      Css.Variant variant;
      variant = Css.parseVariant(formatString);

      if (variant instanceof Css.InvalidVariant invalid) {
        throw new IllegalArgumentException("Invalid formatString: " + invalid.reason());
      }

      putVariant(variantName, variant);
    }
  }

  public final void baseLayer(String contents) {
    if (baseLayer == null) {
      baseLayer = new GrowableList<>();
    }

    baseLayer.add(contents);
  }

  public final void breakpoints(CssProperties properties) {
    int index = 0;

    GrowableList<Css.Breakpoint> builder;
    builder = new GrowableList<>();

    for (var entry : properties) {
      String name;
      name = entry.getKey();

      String value;
      value = entry.getValue();

      Css.Breakpoint breakpoint;
      breakpoint = new Css.Breakpoint(index++, name, value);

      builder.add(breakpoint);
    }

    breakpoints = builder.toUnmodifiableList();
  }

  public final void classes(Set<Class<?>> set) {
    classes = set;
  }

  public final void noteSink(NoteSink noteSink) {
    this.noteSink = noteSink;
  }

  public final boolean skipReset() {
    return skipReset;
  }

  public final void skipReset(boolean value) {
    skipReset = value;
  }

  public final CssResolver getResolver(Css.Key key) {
    return resolvers.get(key);
  }

  public final Css.StaticUtility getStatic(String value) {
    return staticUtilities.get(value);
  }

  public final void useLogicalProperties() {
    propertyType = Css.LOGICAL;
  }

  final Set<Css.Key> getCandidates(String prefix) {
    return prefixes.get(prefix);
  }

  final void override(Css.Key key, CssProperties properties) {
    overrides.put(key, properties);
  }

  //

  final List<Css.Breakpoint> breakpoints() {
    return breakpoints;
  }

  final Iterable<String> baseLayer() {
    return baseLayer != null ? baseLayer : List.of();
  }

  final Iterable<Class<?>> classes() {
    return classes != null ? classes : List.of();
  }

  final String getComponent(String value) {
    return components != null ? components.get(value) : null;
  }

  final Css.Variant getVariant(String variantName) {
    return variants().get(variantName);
  }

  final NoteSink noteSink() {
    return noteSink;
  }

  final Css.PropertyType propertyType() {
    return propertyType;
  }

  private Map<String, Css.Variant> variants() {
    if (variants == null) {
      variants = new GrowableMap<>();
    }

    if (!variantsInitialized) {
      for (var breakpoint : breakpoints) {
        putVariant(breakpoint.name(), breakpoint);
      }

      putVariant("focus", new Css.ClassNameSuffix(1, ":focus"));
      putVariant("hover", new Css.ClassNameSuffix(2, ":hover"));
      putVariant("active", new Css.ClassNameSuffix(3, ":active"));
      putVariant("*", new Css.ClassNameSuffix(4, " > *"));

      putVariant("after", new Css.ClassNameSuffix(5, "::after"));
      putVariant("before", new Css.ClassNameSuffix(6, "::before"));

      putVariant("ltr", new Css.ClassNameSuffix(7, ":where([dir=\"ltr\"], [dir=\"ltr\"] *)"));
      putVariant("rtl", new Css.ClassNameSuffix(7, ":where([dir=\"rtl\"], [dir=\"rtl\"] *)"));

      variantsInitialized = true;
    }

    return variants;
  }

  private void putVariant(String name, Css.Variant variant) {
    if (variants.containsKey(name)) {
      throw new IllegalArgumentException("Variant already defined: " + name);
    }

    variants.put(name, variant);
  }

  //
  // ValueFormatter implementations
  //

  private static final Css.ValueFormatter IDENTITY = new Css.ValueFormatter() {
    @Override
    public final String format(String value, boolean negative) {
      return value;
    }
  };

  private static final Css.ValueFormatter NEGATIVE = new Css.ValueFormatter() {
    @Override
    public final String format(String value, boolean negative) {
      return negative ? "-" + value : value;
    }
  };

  private Css.ValueFormatter ofFunc(Function<String, String> function) {
    return (value, negative) -> function.apply(value);
  }

  private Css.ValueFormatter ofFuncNeg(String functionName) {
    return (value, negative) -> negative
        ? functionName + "(-" + value + ")"
        : functionName + "(" + value + ")";
  }

  //
  // SPEC
  //

  final void spec() {
    // be mindful of method size

    var colors = values(Css.Key._COLORS, Css.DEFAULT_COLORS);

    var spacing = values(Css.Key._SPACING, Css.DEFAULT_SPACING);

    // A

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

    staticUtility(
        Css.Key.ALIGN_ITEMS,

        """
        items-start    | align-items: flex-start
        items-end      | align-items: flex-end
        items-center   | align-items: center
        items-baseline | align-items: baseline
        items-stretch  | align-items: stretch
        """
    );

    staticUtility(
        Css.Key.APPEARANCE,

        """
        appearance-none | appearance: none
        appearance-auto | appearance: auto
        """
    );

    // B

    colorUtility(
        Css.Key.BACKGROUND_COLOR,

        values(Css.Key.BACKGROUND_COLOR, colors),

        "bg", "background-color"
    );

    staticUtility(
        Css.Key.BORDER_COLLAPSE,

        """
        border-collapse | border-collapse: collapse
        border-separate | border-collapse: separate
        """
    );

    var borderColor = values(Css.Key.BORDER_COLOR, colors);

    colorUtility(Css.Key.BORDER_COLOR, borderColor, "border", "border-color");
    colorUtility(Css.Key.BORDER_COLOR_TOP, borderColor, "border-t", propertyType.borderColorTop());
    colorUtility(Css.Key.BORDER_COLOR_RIGHT, borderColor, "border-r", propertyType.borderColorRight());
    colorUtility(Css.Key.BORDER_COLOR_BOTTOM, borderColor, "border-b", propertyType.borderColorBottom());
    colorUtility(Css.Key.BORDER_COLOR_LEFT, borderColor, "border-l", propertyType.borderColorLeft());
    colorUtility(Css.Key.BORDER_COLOR_X, borderColor, "border-x", propertyType.borderColorLeft(), propertyType.borderColorRight());
    colorUtility(Css.Key.BORDER_COLOR_Y, borderColor, "border-y", propertyType.borderColorTop(), propertyType.borderColorBottom());

    var rounded = values(
        Css.Key.BORDER_RADIUS,

        """
        none: 0px
        sm: 0.125rem
        : 0.25rem
        md: 0.375rem
        lg: 0.5rem
        xl: 0.75rem
        2xl: 1rem
        3xl: 1.5rem
        full: 9999px
        """
    );

    funcUtility(Css.Key.BORDER_RADIUS, rounded, "rounded", "border-radius");
    funcUtility(Css.Key.BORDER_RADIUS_TL, rounded, "rounded-tl", propertyType.borderRadiusTopLeft());
    funcUtility(Css.Key.BORDER_RADIUS_TR, rounded, "rounded-tr", propertyType.borderRadiusTopRight());
    funcUtility(Css.Key.BORDER_RADIUS_BR, rounded, "rounded-br", propertyType.borderRadiusBottomRight());
    funcUtility(Css.Key.BORDER_RADIUS_BL, rounded, "rounded-bl", propertyType.borderRadiusBottomLeft());
    funcUtility(Css.Key.BORDER_RADIUS_T, rounded, "rounded-t", propertyType.borderRadiusTopLeft(), propertyType.borderRadiusTopRight());
    funcUtility(Css.Key.BORDER_RADIUS_R, rounded, "rounded-r", propertyType.borderRadiusTopRight(), propertyType.borderRadiusBottomRight());
    funcUtility(Css.Key.BORDER_RADIUS_B, rounded, "rounded-b", propertyType.borderRadiusBottomRight(), propertyType.borderRadiusBottomLeft());
    funcUtility(Css.Key.BORDER_RADIUS_L, rounded, "rounded-l", propertyType.borderRadiusBottomLeft(), propertyType.borderRadiusTopLeft());

    var borderSpacing = values(Css.Key.BORDER_SPACING, spacing);

    funcUtility(Css.Key.BORDER_SPACING, borderSpacing, ofFunc(s -> s + " " + s), "border-spacing", "border-spacing");
    funcUtility(Css.Key.BORDER_SPACING_X, borderSpacing, ofFunc(s -> s + " 0"), "border-spacing-x", "border-spacing");
    funcUtility(Css.Key.BORDER_SPACING_Y, borderSpacing, ofFunc(s -> "0 " + s), "border-spacing-y", "border-spacing");

    var borderWidth = values(
        Css.Key.BORDER_WIDTH,

        """
        : 1px
        0: 0px
        2: 2px
        4: 4px
        8: 8px
        """
    );

    funcUtility(Css.Key.BORDER_WIDTH, borderWidth, "border", "border-width");
    funcUtility(Css.Key.BORDER_WIDTH_TOP, borderWidth, "border-t", propertyType.borderWidthTop());
    funcUtility(Css.Key.BORDER_WIDTH_RIGHT, borderWidth, "border-r", propertyType.borderWidthRight());
    funcUtility(Css.Key.BORDER_WIDTH_BOTTOM, borderWidth, "border-b", propertyType.borderWidthBottom());
    funcUtility(Css.Key.BORDER_WIDTH_LEFT, borderWidth, "border-l", propertyType.borderWidthLeft());
    funcUtility(Css.Key.BORDER_WIDTH_X, borderWidth, "border-x", propertyType.borderWidthLeft(), propertyType.borderWidthRight());
    funcUtility(Css.Key.BORDER_WIDTH_Y, borderWidth, "border-y", propertyType.borderWidthTop(), propertyType.borderWidthBottom());

    var inset = values(
        Css.Key.INSET,

        () -> Css.merge(
            """
              auto: auto
              1/2: 50%
              1/3: 33.333333%
              2/3: 66.666667%
              1/4: 25%
              2/4: 50%
              3/4: 75%
              full: 100%
              """,

            spacing
        )
    );

    funcUtility(Css.Key.BOTTOM, inset, NEGATIVE, "bottom", propertyType.bottom());

    // C

    StringBuilder containerDef;
    containerDef = new StringBuilder();

    containerDef.append("w-full");

    for (var breakpoint : breakpoints()) {
      containerDef.append(" ");

      String name;
      name = breakpoint.name();

      containerDef.append(name);

      containerDef.append(':');

      containerDef.append("max-w-screen-");

      containerDef.append(breakpoint.name());
    }

    addComponent("container", containerDef.toString());

    funcUtility(
        Css.Key.CONTENT,

        values(
            Css.Key.CONTENT,

            """
            none: none
            """
        ),

        "content"
    );

    funcUtility(
        Css.Key.CURSOR,

        values(
            Css.Key.CURSOR,

            """
            auto: auto
            default: default
            pointer: pointer
            wait: wait
            text: text
            move: move
            help: help
            not-allowed: not-allowed
            none: none
            context-menu: context-menu
            progress: progress
            cell: cell
            crosshair: crosshair
            vertical-text: vertical-text
            alias: alias
            copy: copy
            no-drop: no-drop
            grab: grab
            grabbing: grabbing
            all-scroll: all-scroll
            col-resize: col-resize
            row-resize: row-resize
            n-resize: n-resize
            e-resize: e-resize
            s-resize: s-resize
            w-resize: w-resize
            ne-resize: ne-resize
            nw-resize: nw-resize
            se-resize: se-resize
            sw-resize: sw-resize
            ew-resize: ew-resize
            ns-resize: ns-resize
            nesw-resize: nesw-resize
            nwse-resize: nwse-resize
            zoom-in: zoom-in
            zoom-out: zoom-out
            """
        ),

        "cursor"
    );

    // D

    staticUtility(
        Css.Key.DISPLAY,

        """
        block              | display: block
        inline-block       | display: inline-block
        inline             | display: inline
        flex               | display: flex
        inline-flex        | display: inline-flex
        table              | display: table
        inline-table       | display: inline-table
        table-caption      | display: table-caption
        table-cell         | display: table-cell
        table-column       | display: table-column
        table-column-group | display: table-column-group
        table-footer-group | display: table-footer-group
        table-header-group | display: table-header-group
        table-row-group    | display: table-row-group
        table-row          | display: table-row
        flow-root          | display: flow-root
        grid               | display: grid
        inline-grid        | display: inline-grid
        contents           | display: contents
        list-item          | display: list-item
        hidden             | display: none
        """
    );

    // F

    funcUtility(
        Css.Key.FILL,

        values(
            Css.Key.FILL,

            () -> Css.merge(
                """
                none: none
                """,

                colors
            )
        ),

        "fill"
    );

    staticUtility(
        Css.Key.FLEX_DIRECTION,

        """
        flex-row         | flex-direction: row
        flex-row-reverse | flex-direction: row-reverse
        flex-col         | flex-direction: column
        flex-col-reverse | flex-direction: column-reverse
        """
    );

    funcUtility(
        Css.Key.FLEX_GROW,

        values(
            Css.Key.FLEX_GROW,

            """
            : 1
            0: 0
            """
        ),

        "grow", "flex-grow"
    );

    var lineHeight = values(
        Css.Key.LINE_HEIGHT,

        """
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
        """
    );

    customUtility(
        Css.Key.FONT_SIZE,

        "text",

        new CssResolver.OfFontSize(
            values(
                Css.Key.FONT_SIZE,

                """
                xs: 0.75rem/1rem
                sm: 0.875rem/1.25rem
                base: 1rem/1.5rem
                lg: 1.125rem/1.75rem
                xl: 1.25rem/1.75rem
                2xl: 1.5rem/2rem
                3xl: 1.875rem/2.25rem
                4xl: 2.25rem/2.5rem
                5xl: 3rem/1
                6xl: 3.75rem/1
                7xl: 4.5rem/1
                8xl: 6rem/1
                9xl: 8rem/1
                """
            ),

            lineHeight
        )
    );

    funcUtility(
        Css.Key.FONT_WEIGHT,

        values(
            Css.Key.FONT_WEIGHT,

            """
            thin: 100
            extralight: 200
            light: 300
            normal: 400
            medium: 500
            semibold: 600
            bold: 700
            extrabold: 800
            black: 900
            """
        ),

        "font", "font-weight"
    );

    // G

    var gap = values(Css.Key.GAP, spacing);

    funcUtility(Css.Key.GAP, gap, "gap", "gap");
    funcUtility(Css.Key.GAP_X, gap, "gap-x", "column-gap");
    funcUtility(Css.Key.GAP_Y, gap, "gap-y", "row-gap");

    funcUtility(
        Css.Key.GRID_COLUMN,

        values(
            Css.Key.GRID_COLUMN,

            """
            auto: auto
            span-1: span 1 / span 1
            span-2: span 2 / span 2
            span-3: span 3 / span 3
            span-4: span 4 / span 4
            span-5: span 5 / span 5
            span-6: span 6 / span 6
            span-7: span 7 / span 7
            span-8: span 8 / span 8
            span-9: span 9 / span 9
            span-10: span 10 / span 10
            span-11: span 11 / span 11
            span-12: span 12 / span 12
            span-full: 1 / -1
            """
        ),

        "col", "grid-column"
    );

    funcUtility(
        Css.Key.GRID_COLUMN_END,

        values(
            Css.Key.GRID_COLUMN_END,

            """
            auto: auto
            1: 1
            2: 2
            3: 3
            4: 4
            5: 5
            6: 6
            7: 7
            8: 8
            9: 9
            10: 10
            11: 11
            12: 12
            13: 13
            """
        ),

        "col-end", "grid-column-end"
    );

    funcUtility(
        Css.Key.GRID_COLUMN_START,

        values(
            Css.Key.GRID_COLUMN_START,

            """
            auto: auto
            1: 1
            2: 2
            3: 3
            4: 4
            5: 5
            6: 6
            7: 7
            8: 8
            9: 9
            10: 10
            11: 11
            12: 12
            13: 13
            """
        ),

        "col-start", "grid-column-start"
    );

    funcUtility(
        Css.Key.GRID_TEMPLATE_COLUMNS,

        values(
            Css.Key.GRID_TEMPLATE_COLUMNS,

            """
            none: none
            subgrid: subgrid
            1: repeat(1, minmax(0, 1fr))
            2: repeat(2, minmax(0, 1fr))
            3: repeat(3, minmax(0, 1fr))
            4: repeat(4, minmax(0, 1fr))
            5: repeat(5, minmax(0, 1fr))
            6: repeat(6, minmax(0, 1fr))
            7: repeat(7, minmax(0, 1fr))
            8: repeat(8, minmax(0, 1fr))
            9: repeat(9, minmax(0, 1fr))
            10: repeat(10, minmax(0, 1fr))
            11: repeat(11, minmax(0, 1fr))
            12: repeat(12, minmax(0, 1fr))
            """
        ),

        "grid-cols", "grid-template-columns"
    );

    funcUtility(
        Css.Key.GRID_TEMPLATE_ROWS,

        values(
            Css.Key.GRID_TEMPLATE_ROWS,

            """
            none: none
            subgrid: subgrid
            1: repeat(1, minmax(0, 1fr))
            2: repeat(2, minmax(0, 1fr))
            3: repeat(3, minmax(0, 1fr))
            4: repeat(4, minmax(0, 1fr))
            5: repeat(5, minmax(0, 1fr))
            6: repeat(6, minmax(0, 1fr))
            7: repeat(7, minmax(0, 1fr))
            8: repeat(8, minmax(0, 1fr))
            9: repeat(9, minmax(0, 1fr))
            10: repeat(10, minmax(0, 1fr))
            11: repeat(11, minmax(0, 1fr))
            12: repeat(12, minmax(0, 1fr))
            """
        ),

        "grid-rows", "grid-template-rows"
    );

    // H

    funcUtility(
        Css.Key.HEIGHT,

        values(
            Css.Key.HEIGHT,

            () -> Css.merge(
                """
                auto: auto
                1/2: 50%
                1/3: 33.333333%
                2/3: 66.666667%
                1/4: 25%
                2/4: 50%
                3/4: 75%
                1/5: 20%
                2/5: 40%
                3/5: 60%
                4/5: 80%
                1/6: 16.666667%
                2/6: 33.333333%
                3/6: 50%
                4/6: 66.666667%
                5/6: 83.333333%
                full: 100%
                screen: 100vh
                svh: 100svh
                lvh: 100lvh
                dvh: 100dvh
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "h",

        propertyType.height()
    );

    // I

    funcUtility(Css.Key.INSET, inset, NEGATIVE, "inset", "inset");
    funcUtility(Css.Key.INSET_X, inset, NEGATIVE, "inset-x", propertyType.left(), propertyType.right());
    funcUtility(Css.Key.INSET_Y, inset, NEGATIVE, "inset-y", propertyType.top(), propertyType.bottom());

    // J

    staticUtility(
        Css.Key.JUSTIFY_CONTENT,

        """
        justify-normal  | justify-content: normal
        justify-start   | justify-content: flex-start
        justify-end     | justify-content: flex-end
        justify-center  | justify-content: center
        justify-between | justify-content: space-between
        justify-around  | justify-content: space-around
        justify-evenly  | justify-content: space-evenly
        justify-stretch | justify-content: stretch
        """
    );

    // L

    funcUtility(Css.Key.LEFT, inset, NEGATIVE, "left", propertyType.left());

    funcUtility(
        Css.Key.LETTER_SPACING,

        values(
            Css.Key.LETTER_SPACING,

            """
            tighter: -0.05em
            tight: -0.025em
            normal: 0em
            wide: 0.025em
            wider: 0.05em
            widest: 0.1em
            """
        ),

        NEGATIVE,

        "tracking", "letter-spacing"
    );

    funcUtility(Css.Key.LINE_HEIGHT, lineHeight, "leading", "line-height");

    // M

    var margin = values(
        Css.Key.MARGIN,

        () -> Css.merge(
            """
            auto: auto
            """,

            spacing
        )
    );

    funcUtility(Css.Key.MARGIN, margin, NEGATIVE, "m", "margin");
    funcUtility(Css.Key.MARGIN_TOP, margin, NEGATIVE, "mt", propertyType.marginTop());
    funcUtility(Css.Key.MARGIN_RIGHT, margin, NEGATIVE, "mr", propertyType.marginRight());
    funcUtility(Css.Key.MARGIN_BOTTOM, margin, NEGATIVE, "mb", propertyType.marginBottom());
    funcUtility(Css.Key.MARGIN_LEFT, margin, NEGATIVE, "ml", propertyType.marginLeft());
    funcUtility(Css.Key.MARGIN_X, margin, NEGATIVE, "mx", propertyType.marginLeft(), propertyType.marginRight());
    funcUtility(Css.Key.MARGIN_Y, margin, NEGATIVE, "my", propertyType.marginTop(), propertyType.marginBottom());

    funcUtility(
        Css.Key.MAX_HEIGHT,

        values(
            Css.Key.MAX_HEIGHT,

            () -> Css.merge(
                """
                none: none
                full: 100%
                screen: 100vh
                svh: 100svh
                lvh: 100lvh
                dvh: 100dvh
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "max-h",

        propertyType.maxHeight()
    );

    funcUtility(
        Css.Key.MAX_WIDTH,

        values(
            Css.Key.MAX_WIDTH,

            () -> {
              GrowableMap<String, String> maxWidth;
              maxWidth = new GrowableMap<>();

              maxWidth.putAll(spacing);

              maxWidth.put("none", "none");
              maxWidth.put("xs", "20rem");
              maxWidth.put("sm", "24rem");
              maxWidth.put("md", "28rem");
              maxWidth.put("lg", "32rem");
              maxWidth.put("xl", "36rem");
              maxWidth.put("2xl", "42rem");
              maxWidth.put("3xl", "48rem");
              maxWidth.put("4xl", "56rem");
              maxWidth.put("5xl", "64rem");
              maxWidth.put("6xl", "72rem");
              maxWidth.put("7xl", "80rem");
              maxWidth.put("full", "100%");
              maxWidth.put("min", "min-content");
              maxWidth.put("max", "max-content");
              maxWidth.put("fit", "fit-content");
              maxWidth.put("prose", "65ch");

              for (var breakpoint : breakpoints) {
                String screen;
                screen = "screen-" + breakpoint.name();

                maxWidth.put(screen, breakpoint.value());
              }

              return maxWidth;
            }
        ),

        "max-w",

        propertyType.maxWidth()
    );

    funcUtility(
        Css.Key.MIN_HEIGHT,

        values(
            Css.Key.MIN_HEIGHT,

            () -> Css.merge(
                """
                full: 100%
                screen: 100vh
                svh: 100svh
                lvh: 100lvh
                dvh: 100dvh
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "min-h",

        propertyType.minHeight()
    );

    funcUtility(
        Css.Key.MIN_WIDTH,

        values(
            Css.Key.MIN_WIDTH,

            () -> Css.merge(
                """
                full: 100%
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "min-w",

        propertyType.minWidth()
    );

    // O

    funcUtility(
        Css.Key.OPACITY,

        values(
            Css.Key.OPACITY,

            """
            0: 0
            5: 0.05
            10: 0.1
            15: 0.15
            20: 0.2
            25: 0.25
            30: 0.3
            35: 0.35
            40: 0.4
            45: 0.45
            50: 0.5
            55: 0.55
            60: 0.6
            65: 0.65
            70: 0.7
            75: 0.75
            80: 0.8
            85: 0.85
            90: 0.9
            95: 0.95
            100: 1
            """
        ),

        "opacity"
    );

    colorUtility(
        Css.Key.OUTLINE_COLOR,

        values(
            Css.Key.OUTLINE_COLOR,

            colors
        ),

        "outline", "outline-color"
    );

    funcUtility(
        Css.Key.OUTLINE_OFFSET,

        values(
            Css.Key.OUTLINE_OFFSET,

            """
            0: 0px
            1: 1px
            2: 2px
            4: 4px
            8: 8px
            """
        ),

        NEGATIVE,

        "outline-offset"
    );

    staticUtility(
        Css.Key.OUTLINE_STYLE,

        """
        outline-none   | outline: 2px solid transparent
                       | outline-offset: 2px
        outline        | outline-style: solid
        outline-dashed | outline-style: dashed
        outline-dotted | outline-style: dotted
        outline-double | outline-style: double
        """
    );

    funcUtility(
        Css.Key.OUTLINE_WIDTH,

        values(
            Css.Key.OUTLINE_WIDTH,

            """
            0: 0px
            1: 1px
            2: 2px
            4: 4px
            8: 8px
            """
        ),

        NEGATIVE,

        "outline", "outline-width"
    );

    staticUtility(
        Css.Key.OVERFLOW,

        """
        overflow-auto    | overflow: auto
        overflow-hidden  | overflow: hidden
        overflow-clip    | overflow: clip
        overflow-visible | overflow: visible
        overflow-scroll  | overflow: scroll
        """
    );

    staticUtility(
        Css.Key.OVERFLOW_X,

        """
        overflow-x-auto    | overflow-x: auto
        overflow-x-hidden  | overflow-x: hidden
        overflow-x-clip    | overflow-x: clip
        overflow-x-visible | overflow-x: visible
        overflow-x-scroll  | overflow-x: scroll
        """
    );

    staticUtility(
        Css.Key.OVERFLOW_Y,

        """
        overflow-y-auto    | overflow-y: auto
        overflow-y-hidden  | overflow-y: hidden
        overflow-y-clip    | overflow-y: clip
        overflow-y-visible | overflow-y: visible
        overflow-y-scroll  | overflow-y: scroll
        """
    );

    // P

    var padding = values(Css.Key.PADDING, spacing);

    funcUtility(Css.Key.PADDING, padding, "p", "padding");
    funcUtility(Css.Key.PADDING_TOP, padding, "pt", propertyType.paddingTop());
    funcUtility(Css.Key.PADDING_RIGHT, padding, "pr", propertyType.paddingRight());
    funcUtility(Css.Key.PADDING_BOTTOM, padding, "pb", propertyType.paddingBottom());
    funcUtility(Css.Key.PADDING_LEFT, padding, "pl", propertyType.paddingLeft());
    funcUtility(Css.Key.PADDING_X, padding, "px", propertyType.paddingLeft(), propertyType.paddingRight());
    funcUtility(Css.Key.PADDING_Y, padding, "py", propertyType.paddingTop(), propertyType.paddingBottom());

    staticUtility(
        Css.Key.POINTER_EVENTS,

        """
        pointer-events-auto | pointer-events: auto
        pointer-events-none | pointer-events: none
        """
    );

    staticUtility(
        Css.Key.POSITION,

        """
        static   | position: static
        fixed    | position: fixed
        absolute | position: absolute
        relative | position: relative
        sticky   | position: sticky
        """
    );

    // R

    funcUtility(Css.Key.RIGHT, inset, NEGATIVE, "right", propertyType.right());

    // S

    funcUtility(
        Css.Key.SIZE,

        values(
            Css.Key.SIZE,

            () -> Css.merge(
                """
                auto: auto
                1/2: 50%
                1/3: 33.333333%
                2/3: 66.666667%
                1/4: 25%
                2/4: 50%
                3/4: 75%
                1/5: 20%
                2/5: 40%
                3/5: 60%
                4/5: 80%
                1/6: 16.666667%
                2/6: 33.333333%
                3/6: 50%
                4/6: 66.666667%
                5/6: 83.333333%
                1/12: 8.333333%
                2/12: 16.666667%
                3/12: 25%
                4/12: 33.333333%
                5/12: 41.666667%
                6/12: 50%
                7/12: 58.333333%
                8/12: 66.666667%
                9/12: 75%
                10/12: 83.333333%
                11/12: 91.666667%
                full: 100%
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "size",

        propertyType.height(),
        propertyType.width()
    );

    colorUtility(
        Css.Key.STROKE,

        values(
            Css.Key.STROKE,

            () -> Css.merge(
                """
                none: none
                """,

                colors
            )
        ),

        "stroke", "stroke"
    );

    funcUtility(
        Css.Key.STROKE_WIDTH,

        values(
            Css.Key.STROKE_WIDTH,

            """
            0: 0
            1: 1
            2: 2
            """
        ),

        "stroke", "stroke-width"
    );

    // T

    staticUtility(
        Css.Key.TABLE_LAYOUT,

        """
        table-auto  | table-layout: auto
        table-fixed | table-layout: fixed
        """
    );

    staticUtility(
        Css.Key.TEXT_ALIGN,

        """
        text-left    | text-align: left
        text-center  | text-align: center
        text-right   | text-align: right
        text-justify | text-align: justify
        text-start   | text-align: start
        text-end     | text-align: end
        """
    );

    colorUtility(
        Css.Key.TEXT_COLOR,

        values(
            Css.Key.TEXT_COLOR,

            colors
        ),

        "text", "color"
    );

    staticUtility(
        Css.Key.TEXT_DECORATION,

        """
        underline    | text-decoration-line: underline
        overline     | text-decoration-line: overline
        line-through | text-decoration-line: line-through
        no-underline | text-decoration-line: none
        """
    );

    staticUtility(
        Css.Key.TEXT_OVERFLOW,

        """
        truncate      | overflow: hidden
                      | text-overflow: ellipsis
                      | white-space: nowrap
        text-ellipsis | text-overflow: ellipsis
        text-clip     | text-overflow: clip
        """
    );

    staticUtility(
        Css.Key.TEXT_WRAP,

        """
        text-wrap    | text-wrap: wrap
        text-nowrap  | text-wrap: nowrap
        text-balance | text-wrap: balance
        text-pretty  | text-wrap: pretty
        """
    );

    funcUtility(Css.Key.TOP, inset, NEGATIVE, "top", propertyType.top());

    staticUtility(
        Css.Key.TRANSFORM,

        """
        transform-none | transform: none
        """
    );

    funcUtility(
        Css.Key.TRANSITION_DURATION,

        values(
            Css.Key.TRANSITION_DURATION,

            """
            0: 0s
            75: 75ms
            100: 100ms
            150: 150ms
            200: 200ms
            300: 300ms
            500: 500ms
            700: 700ms
            1000: 1000ms
            """
        ),

        "duration", "transition-duration"
    );

    customUtility(
        Css.Key.TRANSITION_PROPERTY,

        "transition",

        new CssResolver.OfTransitionProperty(
            values(
                Css.Key.TRANSITION_PROPERTY,

                """
                none: none

                all: all/cubic-bezier(0.4, 0, 0.2, 1)/150ms

                : color, background-color, border-color, text-decoration-color, fill, stroke, opacity, box-shadow, transform, filter, backdrop-filter/cubic-bezier(0.4, 0, 0.2, 1)/150ms

                colors: color, background-color, border-color, text-decoration-color, fill, stroke/cubic-bezier(0.4, 0, 0.2, 1)/150ms

                opacity: opacity/cubic-bezier(0.4, 0, 0.2, 1)/150ms

                shadow: box-shadow/cubic-bezier(0.4, 0, 0.2, 1)/150ms

                transform: transform/cubic-bezier(0.4, 0, 0.2, 1)/150ms
                """
            )
        )
    );

    var translate = values(Css.Key.TRANSLATE, () -> Css.merge(
        """
        1/2: 50%
        1/3: 33.333333%
        2/3: 66.666667%
        1/4: 25%
        2/4: 50%
        3/4: 75%
        full: 100%
        """,

        spacing
    ));

    funcUtility(Css.Key.TRANSLATE_X, translate, ofFuncNeg("translateX"), "translate-x", "transform");
    funcUtility(Css.Key.TRANSLATE_Y, translate, ofFuncNeg("translateY"), "translate-y", "transform");

    // U

    staticUtility(
        Css.Key.USER_SELECT,

        """
        select-none | user-select: none
        select-text | user-select: text
        select-all  | user-select: all
        select-auto | user-select: auto
        """
    );

    // V

    staticUtility(
        Css.Key.VERTICAL_ALIGN,

        """
        align-baseline    | vertical-align: baseline
        align-top         | vertical-align: top
        align-middle      | vertical-align: middle
        align-bottom      | vertical-align: bottom
        align-text-top    | vertical-align: text-top
        align-text-bottom | vertical-align: text-bottom
        align-sub         | vertical-align: sub
        align-super       | vertical-align: super
        """
    );

    staticUtility(
        Css.Key.VISIBILITY,

        """
        visible   | visibility: visible
        invisible | visibility: hidden
        collapse  | visibility: collapse
        """
    );

    // W

    staticUtility(
        Css.Key.WHITESPACE,

        """
        whitespace-normal       | white-space: normal
        whitespace-nowrap       | white-space: nowrap
        whitespace-pre          | white-space: pre
        whitespace-pre-line     | white-space: pre-line
        whitespace-pre-wrap     | white-space: pre-wrap
        whitespace-break-spaces | white-space: break-spaces
        """
    );

    funcUtility(
        Css.Key.WIDTH,

        values(
            Css.Key.WIDTH,

            () -> Css.merge(
                """
                auto: auto
                1/2: 50%
                1/3: 33.333333%
                2/3: 66.666667%
                1/4: 25%
                2/4: 50%
                3/4: 75%
                1/5: 20%
                2/5: 40%
                3/5: 60%
                4/5: 80%
                1/6: 16.666667%
                2/6: 33.333333%
                3/6: 50%
                4/6: 66.666667%
                5/6: 83.333333%
                1/12: 8.333333%
                2/12: 16.666667%
                3/12: 25%
                4/12: 33.333333%
                5/12: 41.666667%
                6/12: 50%
                7/12: 58.333333%
                8/12: 66.666667%
                9/12: 75%
                10/12: 83.333333%
                11/12: 91.666667%
                full: 100%
                screen: 100vw
                svw: 100svw
                lvw: 100lvw
                dvw: 100dvw
                min: min-content
                max: max-content
                fit: fit-content
                """,

                spacing
            )
        ),

        "w",

        propertyType.width()
    );

    // Z

    funcUtility(
        Css.Key.Z_INDEX,

        values(
            Css.Key.Z_INDEX,

            """
            0: 0
            10: 10
            20: 20
            30: 30
            40: 40
            50: 50
            auto: auto
            """
        ),

        "z", "z-index"
    );
  }

  private void colorUtility(Css.Key key, Map<String, String> values, String prefix, String propertyName) {
    colorUtility(key, values, prefix, propertyName, null);
  }

  private void colorUtility(
      Css.Key key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    CssResolver resolver;
    resolver = new CssResolver.OfColorAlpha(key, values, propertyName1, propertyName2);

    customUtility(key, prefix, resolver);
  }

  private void customUtility(Css.Key key, String prefix, CssResolver resolver) {
    prefix(key, prefix);

    CssResolver maybeExisting;
    maybeExisting = resolvers.put(key, resolver);

    if (maybeExisting != null) {
      throw new IllegalArgumentException(
          "Key " + key + " already mapped to " + maybeExisting
      );
    }
  }

  private void prefix(Css.Key key, String prefix) {
    Set<Css.Key> set;
    set = prefixes.computeIfAbsent(prefix, s -> EnumSet.noneOf(Css.Key.class));

    set.add(key);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values,
      String prefix) {
    funcUtility(key, values, IDENTITY, prefix, prefix, null);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values,
      String prefix, String propertyName) {
    funcUtility(key, values, IDENTITY, prefix, propertyName, null);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    funcUtility(key, values, IDENTITY, prefix, propertyName1, propertyName2);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values, Css.ValueFormatter formatter,
      String prefix) {
    funcUtility(key, values, formatter, prefix, prefix, null);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values, Css.ValueFormatter formatter,
      String prefix, String propertyName) {
    funcUtility(key, values, formatter, prefix, propertyName, null);
  }

  private void funcUtility(
      Css.Key key,
      Map<String, String> values, Css.ValueFormatter formatter,
      String prefix, String propertyName1, String propertyName2) {

    CssResolver resolver;
    resolver = new CssResolver.OfProperties(key, values, formatter, propertyName1, propertyName2);

    customUtility(key, prefix, resolver);

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

  private Map<String, String> values(Css.Key key, String defaults) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    CssProperties defaultProperties;
    defaultProperties = Css.parseProperties(defaults);

    return defaultProperties.toMap();
  }

  private Map<String, String> values(Css.Key key, Supplier<Map<String, String>> defaultSupplier) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    return defaultSupplier.get();
  }

  private Map<String, String> values(Css.Key key, Map<String, String> defaults) {
    CssProperties properties;
    properties = overrides.get(key);

    if (properties != null) {
      return properties.toMap();
    }

    return defaults;
  }

}