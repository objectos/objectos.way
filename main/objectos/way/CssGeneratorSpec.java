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

import java.util.Map;
import java.util.function.Function;
import objectos.util.map.GrowableMap;

final class CssGeneratorSpec extends CssGeneratorRound implements Css.Generator {

  private static final CssValueFormatter IDENTITY = CssValueFormatter.Identity.INSTANCE;

  private static final CssValueFormatter NEGATIVE = CssValueFormatter.NegativeValues.INSTANCE;

  CssGeneratorSpec(CssConfig config) {
    super(config);
  }

  private CssValueFormatter ofFunc(Function<String, String> function) {
    return new CssValueFormatter.OfLambda(function);
  }

  private void config(CssKey key, String prefix, CssResolver resolver) {
    config.customUtility(key, prefix, resolver);
  }

  private void colorUtility(CssKey key, Map<String, String> values, String prefix, String propertyName) {
    colorUtility(key, values, prefix, propertyName, null);
  }

  private void colorUtility(
      CssKey key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    config.colorUtility(key, values, prefix, propertyName1, propertyName2);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values,
      String prefix) {
    funcUtility(key, values, IDENTITY, prefix, prefix, null);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values,
      String prefix, String propertyName) {
    funcUtility(key, values, IDENTITY, prefix, propertyName, null);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values,
      String prefix, String propertyName1, String propertyName2) {
    funcUtility(key, values, IDENTITY, prefix, propertyName1, propertyName2);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values, CssValueFormatter formatter,
      String prefix) {
    funcUtility(key, values, formatter, prefix, prefix, null);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values, CssValueFormatter formatter,
      String prefix, String propertyName) {
    funcUtility(key, values, formatter, prefix, propertyName, null);
  }

  private void funcUtility(
      CssKey key,
      Map<String, String> values, CssValueFormatter formatter,
      String prefix, String propertyName1, String propertyName2) {
    config.funcUtility(key, values, formatter, prefix, propertyName1, propertyName2);
  }

  private void staticUtility(CssKey key, String text) {
    config.staticUtility(key, text);
  }

  @Override
  final void spec() {
    // be mindful of method size

    var colors = config.values(CssKey._COLORS, Css.DEFAULT_COLORS);

    var spacing = config.values(CssKey._SPACING, Css.DEFAULT_SPACING);

    var propertyType = config.propertyType();

    // A

    staticUtility(
        CssKey.ACCESSIBILITY,

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
        CssKey.ALIGN_ITEMS,

        """
        items-start    | align-items: flex-start
        items-end      | align-items: flex-end
        items-center   | align-items: center
        items-baseline | align-items: baseline
        items-stretch  | align-items: stretch
        """
    );

    staticUtility(
        CssKey.APPEARANCE,

        """
        appearance-none | appearance: none
        appearance-auto | appearance: auto
        """
    );

    // B

    colorUtility(
        CssKey.BACKGROUND_COLOR,

        config.values(CssKey.BACKGROUND_COLOR, colors),

        "bg", "background-color"
    );

    staticUtility(
        CssKey.BORDER_COLLAPSE,

        """
        border-collapse | border-collapse: collapse
        border-separate | border-collapse: separate
        """
    );

    var borderColor = config.values(CssKey.BORDER_COLOR, colors);

    colorUtility(CssKey.BORDER_COLOR, borderColor, "border", "border-color");
    colorUtility(CssKey.BORDER_COLOR_TOP, borderColor, "border-t", propertyType.borderColorTop());
    colorUtility(CssKey.BORDER_COLOR_RIGHT, borderColor, "border-r", propertyType.borderColorRight());
    colorUtility(CssKey.BORDER_COLOR_BOTTOM, borderColor, "border-b", propertyType.borderColorBottom());
    colorUtility(CssKey.BORDER_COLOR_LEFT, borderColor, "border-l", propertyType.borderColorLeft());
    colorUtility(CssKey.BORDER_COLOR_X, borderColor, "border-x", propertyType.borderColorLeft(), propertyType.borderColorRight());
    colorUtility(CssKey.BORDER_COLOR_Y, borderColor, "border-y", propertyType.borderColorTop(), propertyType.borderColorBottom());

    var rounded = config.values(
        CssKey.BORDER_RADIUS,

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

    funcUtility(CssKey.BORDER_RADIUS, rounded, "rounded", "border-radius");
    funcUtility(CssKey.BORDER_RADIUS_TL, rounded, "rounded-tl", propertyType.borderRadiusTopLeft());
    funcUtility(CssKey.BORDER_RADIUS_TR, rounded, "rounded-tr", propertyType.borderRadiusTopRight());
    funcUtility(CssKey.BORDER_RADIUS_BR, rounded, "rounded-br", propertyType.borderRadiusBottomRight());
    funcUtility(CssKey.BORDER_RADIUS_BL, rounded, "rounded-bl", propertyType.borderRadiusBottomLeft());
    funcUtility(CssKey.BORDER_RADIUS_T, rounded, "rounded-t", propertyType.borderRadiusTopLeft(), propertyType.borderRadiusTopRight());
    funcUtility(CssKey.BORDER_RADIUS_R, rounded, "rounded-r", propertyType.borderRadiusTopRight(), propertyType.borderRadiusBottomRight());
    funcUtility(CssKey.BORDER_RADIUS_B, rounded, "rounded-b", propertyType.borderRadiusBottomRight(), propertyType.borderRadiusBottomLeft());
    funcUtility(CssKey.BORDER_RADIUS_L, rounded, "rounded-l", propertyType.borderRadiusBottomLeft(), propertyType.borderRadiusTopLeft());

    var borderSpacing = config.values(CssKey.BORDER_SPACING, spacing);

    funcUtility(CssKey.BORDER_SPACING, borderSpacing, ofFunc(s -> s + " " + s), "border-spacing", "border-spacing");
    funcUtility(CssKey.BORDER_SPACING_X, borderSpacing, ofFunc(s -> s + " 0"), "border-spacing-x", "border-spacing");
    funcUtility(CssKey.BORDER_SPACING_Y, borderSpacing, ofFunc(s -> "0 " + s), "border-spacing-y", "border-spacing");

    var borderWidth = config.values(
        CssKey.BORDER_WIDTH,

        """
        : 1px
        0: 0px
        2: 2px
        4: 4px
        8: 8px
        """
    );

    funcUtility(CssKey.BORDER_WIDTH, borderWidth, "border", "border-width");
    funcUtility(CssKey.BORDER_WIDTH_TOP, borderWidth, "border-t", propertyType.borderWidthTop());
    funcUtility(CssKey.BORDER_WIDTH_RIGHT, borderWidth, "border-r", propertyType.borderWidthRight());
    funcUtility(CssKey.BORDER_WIDTH_BOTTOM, borderWidth, "border-b", propertyType.borderWidthBottom());
    funcUtility(CssKey.BORDER_WIDTH_LEFT, borderWidth, "border-l", propertyType.borderWidthLeft());
    funcUtility(CssKey.BORDER_WIDTH_X, borderWidth, "border-x", propertyType.borderWidthLeft(), propertyType.borderWidthRight());
    funcUtility(CssKey.BORDER_WIDTH_Y, borderWidth, "border-y", propertyType.borderWidthTop(), propertyType.borderWidthBottom());

    var inset = config.values(
        CssKey.INSET,

        c -> Css.merge(
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

    funcUtility(CssKey.BOTTOM, inset, NEGATIVE, "bottom", propertyType.bottom());

    // C

    funcUtility(
        CssKey.CONTENT,

        config.values(
            CssKey.CONTENT,

            """
            none: none
            """
        ),

        "content"
    );

    funcUtility(
        CssKey.CURSOR,

        config.values(
            CssKey.CURSOR,

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
        CssKey.DISPLAY,

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
        CssKey.FILL,

        config.values(
            CssKey.FILL,

            c -> Css.merge(
                """
                none: none
                """,

                colors
            )
        ),

        "fill"
    );

    staticUtility(
        CssKey.FLEX_DIRECTION,

        """
        flex-row         | flex-direction: row
        flex-row-reverse | flex-direction: row-reverse
        flex-col         | flex-direction: column
        flex-col-reverse | flex-direction: column-reverse
        """
    );

    funcUtility(
        CssKey.FLEX_GROW,

        config.values(
            CssKey.FLEX_GROW,

            """
            : 1
            0: 0
            """
        ),

        "grow", "flex-grow"
    );

    var lineHeight = config.values(
        CssKey.LINE_HEIGHT,

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

    config(
        CssKey.FONT_SIZE,

        "text",

        new CssResolver.OfFontSize(
            config.values(
                CssKey.FONT_SIZE,

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
        CssKey.FONT_WEIGHT,

        config.values(
            CssKey.FONT_WEIGHT,

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

    var gap = config.values(CssKey.GAP, spacing);

    funcUtility(CssKey.GAP, gap, "gap", "gap");
    funcUtility(CssKey.GAP_X, gap, "gap-x", "column-gap");
    funcUtility(CssKey.GAP_Y, gap, "gap-y", "row-gap");

    funcUtility(
        CssKey.GRID_COLUMN,

        config.values(
            CssKey.GRID_COLUMN,

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
        CssKey.GRID_COLUMN_END,

        config.values(
            CssKey.GRID_COLUMN_END,

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
        CssKey.GRID_COLUMN_START,

        config.values(
            CssKey.GRID_COLUMN_START,

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
        CssKey.GRID_TEMPLATE_COLUMNS,

        config.values(
            CssKey.GRID_TEMPLATE_COLUMNS,

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
        CssKey.GRID_TEMPLATE_ROWS,

        config.values(
            CssKey.GRID_TEMPLATE_ROWS,

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
        CssKey.HEIGHT,

        config.values(
            CssKey.HEIGHT,

            c -> Css.merge(
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

    funcUtility(CssKey.INSET, inset, NEGATIVE, "inset", "inset");
    funcUtility(CssKey.INSET_X, inset, NEGATIVE, "inset-x", propertyType.left(), propertyType.right());
    funcUtility(CssKey.INSET_Y, inset, NEGATIVE, "inset-y", propertyType.top(), propertyType.bottom());

    // J

    staticUtility(
        CssKey.JUSTIFY_CONTENT,

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

    funcUtility(CssKey.LEFT, inset, NEGATIVE, "left", propertyType.left());

    funcUtility(
        CssKey.LETTER_SPACING,

        config.values(
            CssKey.LETTER_SPACING,

            """
            tighter: -0.05em
            tight: -0.025em
            normal: 0em
            wide: 0.025em
            wider: 0.05em
            widest: 0.1em
            """
        ),

        "tracking", "letter-spacing"
    );

    funcUtility(CssKey.LINE_HEIGHT, lineHeight, "leading", "line-height");

    // M

    var margin = config.values(
        CssKey.MARGIN,

        c -> Css.merge(
            """
            auto: auto
            """,

            spacing
        )
    );

    funcUtility(CssKey.MARGIN, margin, NEGATIVE, "m", "margin");
    funcUtility(CssKey.MARGIN_TOP, margin, NEGATIVE, "mt", propertyType.marginTop());
    funcUtility(CssKey.MARGIN_RIGHT, margin, NEGATIVE, "mr", propertyType.marginRight());
    funcUtility(CssKey.MARGIN_BOTTOM, margin, NEGATIVE, "mb", propertyType.marginBottom());
    funcUtility(CssKey.MARGIN_LEFT, margin, NEGATIVE, "ml", propertyType.marginLeft());
    funcUtility(CssKey.MARGIN_X, margin, NEGATIVE, "mx", propertyType.marginLeft(), propertyType.marginRight());
    funcUtility(CssKey.MARGIN_Y, margin, NEGATIVE, "my", propertyType.marginTop(), propertyType.marginBottom());

    funcUtility(
        CssKey.MAX_HEIGHT,

        config.values(
            CssKey.MAX_HEIGHT,

            c -> Css.merge(
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
        CssKey.MAX_WIDTH,

        config.values(
            CssKey.MAX_WIDTH,

            c -> {
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

              for (var breakpoint : c.breakpoints()) {
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
        CssKey.MIN_HEIGHT,

        config.values(
            CssKey.MIN_HEIGHT,

            c -> Css.merge(
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
        CssKey.MIN_WIDTH,

        config.values(
            CssKey.MIN_WIDTH,

            c -> Css.merge(
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
        CssKey.OPACITY,

        config.values(
            CssKey.OPACITY,

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
        CssKey.OUTLINE_COLOR,

        config.values(
            CssKey.OUTLINE_COLOR,

            colors
        ),

        "outline", "outline-color"
    );

    funcUtility(
        CssKey.OUTLINE_OFFSET,

        config.values(
            CssKey.OUTLINE_OFFSET,

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
        CssKey.OUTLINE_STYLE,

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
        CssKey.OUTLINE_WIDTH,

        config.values(
            CssKey.OUTLINE_WIDTH,

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
        CssKey.OVERFLOW,

        """
        overflow-auto    | overflow: auto
        overflow-hidden  | overflow: hidden
        overflow-clip    | overflow: clip
        overflow-visible | overflow: visible
        overflow-scroll  | overflow: scroll
        """
    );

    staticUtility(
        CssKey.OVERFLOW_X,

        """
        overflow-x-auto    | overflow-x: auto
        overflow-x-hidden  | overflow-x: hidden
        overflow-x-clip    | overflow-x: clip
        overflow-x-visible | overflow-x: visible
        overflow-x-scroll  | overflow-x: scroll
        """
    );

    staticUtility(
        CssKey.OVERFLOW_Y,

        """
        overflow-y-auto    | overflow-y: auto
        overflow-y-hidden  | overflow-y: hidden
        overflow-y-clip    | overflow-y: clip
        overflow-y-visible | overflow-y: visible
        overflow-y-scroll  | overflow-y: scroll
        """
    );

    // P

    var padding = config.values(CssKey.PADDING, spacing);

    funcUtility(CssKey.PADDING, padding, "p", "padding");
    funcUtility(CssKey.PADDING_TOP, padding, "pt", propertyType.paddingTop());
    funcUtility(CssKey.PADDING_RIGHT, padding, "pr", propertyType.paddingRight());
    funcUtility(CssKey.PADDING_BOTTOM, padding, "pb", propertyType.paddingBottom());
    funcUtility(CssKey.PADDING_LEFT, padding, "pl", propertyType.paddingLeft());
    funcUtility(CssKey.PADDING_X, padding, "px", propertyType.paddingLeft(), propertyType.paddingRight());
    funcUtility(CssKey.PADDING_Y, padding, "py", propertyType.paddingTop(), propertyType.paddingBottom());

    staticUtility(
        CssKey.POINTER_EVENTS,

        """
        pointer-events-auto | pointer-events: auto
        pointer-events-none | pointer-events: none
        """
    );

    staticUtility(
        CssKey.POSITION,

        """
        static   | position: static
        fixed    | position: fixed
        absolute | position: absolute
        relative | position: relative
        sticky   | position: sticky
        """
    );

    // R

    funcUtility(CssKey.RIGHT, inset, NEGATIVE, "right", propertyType.right());

    // S

    funcUtility(
        CssKey.SIZE,

        config.values(
            CssKey.SIZE,

            c -> Css.merge(
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
        CssKey.STROKE,

        config.values(
            CssKey.STROKE,

            c -> Css.merge(
                """
                none: none
                """,

                colors
            )
        ),

        "stroke", "stroke"
    );

    funcUtility(
        CssKey.STROKE_WIDTH,

        config.values(
            CssKey.STROKE_WIDTH,

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
        CssKey.TABLE_LAYOUT,

        """
        table-auto  | table-layout: auto
        table-fixed | table-layout: fixed
        """
    );

    staticUtility(
        CssKey.TEXT_ALIGN,

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
        CssKey.TEXT_COLOR,

        config.values(
            CssKey.TEXT_COLOR,

            colors
        ),

        "text", "color"
    );

    staticUtility(
        CssKey.TEXT_DECORATION,

        """
        underline    | text-decoration-line: underline
        overline     | text-decoration-line: overline
        line-through | text-decoration-line: line-through
        no-underline | text-decoration-line: none
        """
    );

    staticUtility(
        CssKey.TEXT_WRAP,

        """
        text-wrap    | text-wrap: wrap
        text-nowrap  | text-wrap: nowrap
        text-balance | text-wrap: balance
        text-pretty  | text-wrap: pretty
        """
    );

    funcUtility(CssKey.TOP, inset, NEGATIVE, "top", propertyType.top());

    staticUtility(
        CssKey.TRANSFORM,

        """
        transform-none | transform: none
        """
    );

    funcUtility(
        CssKey.TRANSITION_DURATION,

        config.values(
            CssKey.TRANSITION_DURATION,

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

    config(
        CssKey.TRANSITION_PROPERTY,

        "transition",

        new CssResolver.OfTransitionProperty(
            config.values(
                CssKey.TRANSITION_PROPERTY,

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

    var translate = config.values(CssKey.TRANSLATE, c -> Css.merge(
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

    funcUtility(CssKey.TRANSLATE_X, translate, new CssValueFormatter.OfFunctionNeg("translateX"), "translate-x", "transform");
    funcUtility(CssKey.TRANSLATE_Y, translate, new CssValueFormatter.OfFunctionNeg("translateY"), "translate-y", "transform");

    // U

    staticUtility(
        CssKey.USER_SELECT,

        """
        select-none | user-select: none
        select-text | user-select: text
        select-all  | user-select: all
        select-auto | user-select: auto
        """
    );

    // V

    staticUtility(
        CssKey.VERTICAL_ALIGN,

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
        CssKey.VISIBILITY,

        """
        visible   | visibility: visible
        invisible | visibility: hidden
        collapse  | visibility: collapse
        """
    );

    // W

    staticUtility(
        CssKey.WHITESPACE,

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
        CssKey.WIDTH,

        config.values(
            CssKey.WIDTH,

            c -> Css.merge(
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
        CssKey.Z_INDEX,

        config.values(
            CssKey.Z_INDEX,

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

}