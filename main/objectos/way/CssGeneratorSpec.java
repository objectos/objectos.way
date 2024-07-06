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

  CssGeneratorSpec(CssGeneratorConfig config) {
    super(config);
  }

  @Override
  final Object execute(CssKey key, CssAction action, Object arg) {
    return switch (key) {
      case _STATIC_TABLE -> _executeStaticTable(key, action, arg);

      // A

      case ACCESSIBILITY -> executeAccessibility(key, action, arg);

      case ALIGN_ITEMS -> executeAlignItems(key, action, arg);

      // B

      case BACKGROUND_COLOR -> executeBackgroundColor(key, action, arg);

      case BORDER_COLLAPSE -> executeBorderCollapse(key, action, arg);

      case BORDER_COLOR -> executeBorderColor(key, action, "border", "border-color");
      case BORDER_COLOR_BOTTOM -> executeBorderColor(key, action, "border-b", "border-bottom-color");
      case BORDER_COLOR_LEFT -> executeBorderColor(key, action, "border-l", "border-left-color");
      case BORDER_COLOR_RIGHT -> executeBorderColor(key, action, "border-r", "border-right-color");
      case BORDER_COLOR_TOP -> executeBorderColor(key, action, "border-t", "border-top-color");
      case BORDER_COLOR_X -> executeBorderColor(key, action, "border-x", "border-left-color", "border-right-color");
      case BORDER_COLOR_Y -> executeBorderColor(key, action, "border-y", "border-top-color", "border-bottom-color");

      case BORDER_RADIUS -> executeBorderRadius(key, action, "rounded", "border-radius");
      case BORDER_RADIUS_B -> executeBorderRadius(key, action, "rounded-b", "border-bottom-right-radius", "border-bottom-left-radius");
      case BORDER_RADIUS_BL -> executeBorderRadius(key, action, "rounded-bl", "border-bottom-left-radius");
      case BORDER_RADIUS_BR -> executeBorderRadius(key, action, "rounded-br", "border-bottom-right-radius");
      case BORDER_RADIUS_E -> executeBorderRadius(key, action, "rounded-e", "border-start-end-radius", "border-end-end-radius");
      case BORDER_RADIUS_EE -> executeBorderRadius(key, action, "rounded-ee", "border-end-end-radius");
      case BORDER_RADIUS_ES -> executeBorderRadius(key, action, "rounded-es", "border-end-start-radius");
      case BORDER_RADIUS_L -> executeBorderRadius(key, action, "rounded-l", "border-top-left-radius", "border-bottom-left-radius");
      case BORDER_RADIUS_R -> executeBorderRadius(key, action, "rounded-r", "border-top-right-radius", "border-bottom-right-radius");
      case BORDER_RADIUS_S -> executeBorderRadius(key, action, "rounded-s", "border-start-start-radius", "border-end-start-radius");
      case BORDER_RADIUS_SE -> executeBorderRadius(key, action, "rounded-se", "border-start-end-radius");
      case BORDER_RADIUS_SS -> executeBorderRadius(key, action, "rounded-ss", "border-start-start-radius");
      case BORDER_RADIUS_T -> executeBorderRadius(key, action, "rounded-t", "border-top-left-radius", "border-top-right-radius");
      case BORDER_RADIUS_TL -> executeBorderRadius(key, action, "rounded-tl", "border-top-left-radius");
      case BORDER_RADIUS_TR -> executeBorderRadius(key, action, "rounded-tr", "border-top-right-radius");

      case BORDER_SPACING -> executeBorderSpacing(key, action, "border-spacing", s -> s + " " + s);
      case BORDER_SPACING_X -> executeBorderSpacing(key, action, "border-spacing-x", s -> s + " 0");
      case BORDER_SPACING_Y -> executeBorderSpacing(key, action, "border-spacing-y", s -> "0 " + s);

      case BORDER_WIDTH -> executeBorderWidth(key, action, "border", "border-width");
      case BORDER_WIDTH_BOTTOM -> executeBorderWidth(key, action, "border-b", "border-bottom-width");
      case BORDER_WIDTH_LEFT -> executeBorderWidth(key, action, "border-l", "border-left-width");
      case BORDER_WIDTH_RIGHT -> executeBorderWidth(key, action, "border-r", "border-right-width");
      case BORDER_WIDTH_TOP -> executeBorderWidth(key, action, "border-t", "border-top-width");
      case BORDER_WIDTH_X -> executeBorderWidth(key, action, "border-x", "border-left-width", "border-right-width");
      case BORDER_WIDTH_Y -> executeBorderWidth(key, action, "border-y", "border-top-width", "border-bottom-width");

      case BOTTOM -> executeInset(key, action, "bottom", "bottom");

      // C

      case CONTENT -> executeContent(key, action);

      case CURSOR -> executeCursor(key, action);

      case CUSTOM -> null;

      // D

      case DISPLAY -> executeDisplay(key, action, arg);

      case END -> executeInset(key, action, "end", "inset-inline-end");

      case FILL -> executeFill(key, action);

      case FLEX_DIRECTION -> executeFlexDirection(key, action);

      case FLEX_GROW -> executeFlexGrow(key, action);

      case FONT_SIZE -> executeFontSize(key, action);

      case FONT_WEIGHT -> executeFontWeight(key, action);

      case GAP -> executeGap(key, action, "gap", "gap");
      case GAP_X -> executeGap(key, action, "gap-x", "column-gap");
      case GAP_Y -> executeGap(key, action, "gap-y", "row-gap");

      case GRID_COLUMN -> executeGridColumn(key, action);

      case GRID_COLUMN_END -> executeGridColumnEnd(key, action);

      case GRID_COLUMN_START -> executeGridColumnStart(key, action);

      case GRID_TEMPLATE_COLUMNS -> executeGridTemplateColumns(key, action);

      case GRID_TEMPLATE_ROWS -> executeGridTemplateRows(key, action);

      case HEIGHT -> executeHeight(key, action);

      case INSET -> executeInset(key, action, "inset", "inset");
      case INSET_X -> executeInset(key, action, "inset-x", "left", "right");
      case INSET_Y -> executeInset(key, action, "inset-y", "top", "bottom");

      case JUSTIFY_CONTENT -> executeJustifyContent(key, action, arg);

      case LEFT -> executeInset(key, action, "left", "left");

      case LETTER_SPACING -> executeLetterSpacing(key, action);

      case LINE_HEIGHT -> executeLineHeight(key, action);

      // M

      case MARGIN -> executeMargin(key, action, "m", "margin");
      case MARGIN_BOTTOM -> executeMargin(key, action, "mb", "margin-bottom");
      case MARGIN_LEFT -> executeMargin(key, action, "ml", "margin-left");
      case MARGIN_RIGHT -> executeMargin(key, action, "mr", "margin-right");
      case MARGIN_TOP -> executeMargin(key, action, "mt", "margin-top");
      case MARGIN_X -> executeMargin(key, action, "mx", "margin-left", "margin-right");
      case MARGIN_Y -> executeMargin(key, action, "my", "margin-top", "margin-bottom");

      case MAX_WIDTH -> executeMaxWidth(key, action);
      case MIN_WIDTH -> executeMinWidth(key, action);

      // N

      // O

      case OPACITY -> executeOpacity(key, action);

      case OUTLINE_COLOR -> executeOutlineColor(key, action);

      case OUTLINE_OFFSET -> executeOutlineOffset(key, action);

      case OUTLINE_STYLE -> executeOutlineStyle(key, action);

      case OUTLINE_WIDTH -> executeOutlineWidth(key, action);

      case OVERFLOW -> executeOverflow(key, action, arg);
      case OVERFLOW_X -> executeOverflowX(key, action, arg);
      case OVERFLOW_Y -> executeOverflowY(key, action, arg);

      // P

      case PADDING -> executePadding(key, action, "p", "padding");
      case PADDING_BOTTOM -> executePadding(key, action, "pb", "padding-bottom");
      case PADDING_LEFT -> executePadding(key, action, "pl", "padding-left");
      case PADDING_RIGHT -> executePadding(key, action, "pr", "padding-right");
      case PADDING_TOP -> executePadding(key, action, "pt", "padding-top");
      case PADDING_X -> executePadding(key, action, "px", "padding-left", "padding-right");
      case PADDING_Y -> executePadding(key, action, "py", "padding-top", "padding-bottom");

      case POINTER_EVENTS -> executePointerEvents(key, action, arg);

      case POSITION -> executePosition(key, action, arg);

      // R

      case RIGHT -> executeInset(key, action, "right", "right");

      // S

      case SIZE -> executeSize(key, action);

      case START -> executeInset(key, action, "start", "inset-inline-start");

      case STROKE -> executeStroke(key, action);

      case STROKE_WIDTH -> executeStrokeWidth(key, action);

      // T

      case TABLE_LAYOUT -> executeTableLayout(key, action, arg);

      case TEXT_ALIGN -> executeTextAlign(key, action, arg);

      case TEXT_COLOR -> null;

      case TEXT_DECORATION -> executeTextDecoration(key, action, arg);

      case TEXT_WRAP -> executeTextWrap(key, action, arg);

      case TOP -> executeInset(key, action, "top", "top");

      case TRANSITION_DURATION -> null;
      case TRANSITION_PROPERTY -> null;

      // U

      case USER_SELECT -> executeUserSelect(key, action, arg);

      // V

      case VERTICAL_ALIGN -> executeVerticalAlign(key, action, arg);

      case VISIBILITY -> executeVisibility(key, action, arg);

      // W

      case WIDTH -> null;

      // Z

      case Z_INDEX -> null;
    };
  }

  private Object _executeStaticTable(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> {
        config.put(key, new CssStaticTable());

        yield null;
      }

      default -> error(key, action, arg);
    };
  }

  private Object executeAccessibility(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "sr-only", """
          position: absolute
          width: 1px
          height: 1px
          padding: 0
          margin: -1px
          overflow: hidden
          clip: rect(0, 0, 0, 0)
          white-space: nowrap
          border-width: 0
          """)
          .rule(key, "not-sr-only", """
          position: static
          width: auto
          height: auto
          padding: 0
          margin: 0
          overflow: visible
          clip: auto
          white-space: normal
          """);

      default -> error(key, action, arg);
    };
  }

  private Object executeAlignItems(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "items-start", "align-items: flex-start")
          .rule(key, "items-end", "align-items: flex-end")
          .rule(key, "items-center", "align-items: center")
          .rule(key, "items-baseline", "align-items: baseline")
          .rule(key, "items-stretch", "align-items: stretch");

      default -> error(key, action, arg);
    };
  }

  private Object executeBackgroundColor(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "bg");

      case RESOLVER -> {
        String variableName;
        variableName = "--tw-bg-opacity";

        Map<String, String> colors;
        colors = config.colorsAlpha(CssKey.BACKGROUND_COLOR, variableName);

        String propertyName;
        propertyName = "background-color";

        yield new CssRuleResolver.OfColorAlpha(key, variableName, colors, propertyName);
      }

      default -> error(key, action, arg);
    };
  }

  private Object executeBorderCollapse(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "border-collapse", "border-collapse: collapse")
          .rule(key, "border-separate", "border-collapse: separate");

      default -> error(key, action, arg);
    };
  }

  private Object executeBorderColor(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeBorderColor(key, action, prefix, propertyName1, null);
  }

  private Object executeBorderColor(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfColorAlpha(
          key,

          "--tw-border-opacity",

          config.colorsAlpha(
              CssKey.BORDER_COLOR,

              "--tw-border-opacity"
          ),

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executeBorderRadius(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeBorderRadius(key, action, prefix, propertyName1, null);
  }

  private Object executeBorderRadius(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
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
          ),

          IDENTITY,

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executeBorderSpacing(CssKey key, CssAction action, String prefix, Function<String, String> valueConverter) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              CssKey.BORDER_SPACING,

              CssGeneratorConfig::spacing
          ),

          new CssValueFormatter.OfFunction(valueConverter),

          "border-spacing"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeBorderWidth(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeBorderWidth(key, action, prefix, propertyName1, null);
  }

  private Object executeBorderWidth(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              CssKey.BORDER_WIDTH,

              """
              : 1px
              0: 0px
              2: 2px
              4: 4px
              8: 8px
              """
          ),

          IDENTITY,

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executeContent(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "content");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              """
              none: none
              """
          ),

          IDENTITY,

          "content"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeCursor(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "cursor");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "cursor"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeDisplay(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "block", "display: block")
          .rule(key, "inline-block", "display: inline-block")
          .rule(key, "inline", "display: inline")
          .rule(key, "flex", "display: flex")
          .rule(key, "inline-flex", "display: inline-flex")
          .rule(key, "table", "display: table")
          .rule(key, "inline-table", "display: inline-table")
          .rule(key, "table-caption", "display: table-caption")
          .rule(key, "table-cell", "display: table-cell")
          .rule(key, "table-column", "display: table-column")
          .rule(key, "table-column-group", "display: table-column-group")
          .rule(key, "table-footer-group", "display: table-footer-group")
          .rule(key, "table-header-group", "display: table-header-group")
          .rule(key, "table-row-group", "display: table-row-group")
          .rule(key, "table-row", "display: table-row")
          .rule(key, "flow-root", "display: flow-root")
          .rule(key, "grid", "display: grid")
          .rule(key, "inline-grid", "display: inline-grid")
          .rule(key, "contents", "display: contents")
          .rule(key, "list-item", "display: list-item")
          .rule(key, "hidden", "display: none");

      default -> error(key, action, arg);
    };
  }

  private Object executeFill(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "fill");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              c -> Css.merge(
                  """
                  none: none
                  """,

                  c.colors()
              )
          ),

          IDENTITY,

          "fill"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeFlexDirection(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "flex-row", "flex-direction: row")
          .rule(key, "flex-row-reverse", "flex-direction: row-reverse")
          .rule(key, "flex-col", "flex-direction: column")
          .rule(key, "flex-col-reverse", "flex-direction: column-reverse");

      default -> error(key, action, null);
    };
  }

  private Object executeFlexGrow(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "grow");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              """
              : 1
              0: 0
              """
          ),

          IDENTITY,

          "flex-grow"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeFontSize(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "text");

      case RESOLVER -> new CssRuleResolver.OfFontSize(
          key,

          config.values(
              key,

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

          config.lineHeight()
      );

      default -> error(key, action, null);
    };
  }

  private Object executeFontWeight(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "font");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "font-weight"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGap(CssKey key, CssAction action, String prefix, String propertyName) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.spacing(),

          IDENTITY,

          propertyName
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGridColumn(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "col");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "grid-column"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGridColumnEnd(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "col-end");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "grid-column-end"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGridColumnStart(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "col-start");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "grid-column-start"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGridTemplateColumns(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "grid-cols");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "grid-template-columns"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeGridTemplateRows(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "grid-rows");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "grid-template-rows"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeHeight(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "h");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

                  c.spacing()
              )
          ),

          IDENTITY,

          "height"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeInset(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeInset(key, action, prefix, propertyName1, null);
  }

  private Object executeInset(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
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

                  config.spacing()
              )
          ),

          NEGATIVE,

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executeLetterSpacing(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "tracking");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              """
              tighter: -0.05em
              tight: -0.025em
              normal: 0em
              wide: 0.025em
              wider: 0.05em
              widest: 0.1em
              """
          ),

          IDENTITY,

          "letter-spacing"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeLineHeight(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "leading");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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
          ),

          IDENTITY,

          "line-height"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeMargin(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeMargin(key, action, prefix, propertyName1, null);
  }

  private Object executeMargin(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              c -> Css.merge(
                  """
                  auto: auto
                  """,

                  c.spacing()
              )
          ),

          NEGATIVE,

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executeMaxWidth(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "max-w");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              c -> {
                GrowableMap<String, String> maxWidth;
                maxWidth = new GrowableMap<>();

                maxWidth.putAll(c.spacing());

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

          IDENTITY,

          "max-width"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeMinWidth(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "min-w");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              c -> Css.merge(
                  """
                  full: 100%
                  min: min-content
                  max: max-content
                  fit: fit-content
                  """,

                  c.spacing()
              )
          ),

          IDENTITY,

          "min-width"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeJustifyContent(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "justify-normal", "justify-content: normal")
          .rule(key, "justify-start", "justify-content: flex-start")
          .rule(key, "justify-end", "justify-content: flex-end")
          .rule(key, "justify-center", "justify-content: center")
          .rule(key, "justify-between", "justify-content: space-between")
          .rule(key, "justify-around", "justify-content: space-around")
          .rule(key, "justify-evenly", "justify-content: space-evenly")
          .rule(key, "justify-stretch", "justify-content: stretch");

      default -> error(key, action, arg);
    };
  }

  private Object executeOpacity(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "opacity");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

          IDENTITY,

          "opacity"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeOutlineColor(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "outline");

      case RESOLVER -> new CssRuleResolver.OfColorAlpha(
          key,

          "--tw-outline-opacity",

          config.colorsAlpha(
              CssKey.OUTLINE_COLOR,

              "--tw-outline-opacity"
          ),

          "outline-color"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeOutlineOffset(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "outline-offset");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

      default -> error(key, action, null);
    };
  }

  private Object executeOutlineStyle(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "outline-none", "outline: 2px solid transparent\noutline-offset: 2px")
          .rule(key, "outline", "outline-style: solid")
          .rule(key, "outline-dashed", "outline-style: dashed")
          .rule(key, "outline-dotted", "outline-style: dotted")
          .rule(key, "outline-double", "outline-style: double");

      default -> error(key, action, null);
    };
  }

  private Object executeOutlineWidth(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "outline");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              """
              0: 0px
              1: 1px
              2: 2px
              4: 4px
              8: 8px
              """
          ),

          NEGATIVE,

          "outline-width"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeOverflow(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "overflow-auto", "overflow: auto")
          .rule(key, "overflow-hidden", "overflow: hidden")
          .rule(key, "overflow-clip", "overflow: clip")
          .rule(key, "overflow-visible", "overflow: visible")
          .rule(key, "overflow-scroll", "overflow: scroll");

      default -> error(key, action, arg);
    };
  }

  private Object executeOverflowX(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "overflow-x-auto", "overflow-x: auto")
          .rule(key, "overflow-x-hidden", "overflow-x: hidden")
          .rule(key, "overflow-x-clip", "overflow-x: clip")
          .rule(key, "overflow-x-visible", "overflow-x: visible")
          .rule(key, "overflow-x-scroll", "overflow-x: scroll");

      default -> error(key, action, arg);
    };
  }

  private Object executeOverflowY(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "overflow-y-auto", "overflow-y: auto")
          .rule(key, "overflow-y-hidden", "overflow-y: hidden")
          .rule(key, "overflow-y-clip", "overflow-y: clip")
          .rule(key, "overflow-y-visible", "overflow-y: visible")
          .rule(key, "overflow-y-scroll", "overflow-y: scroll");

      default -> error(key, action, arg);
    };
  }

  private Object executePadding(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executePadding(key, action, prefix, propertyName1, null);
  }

  private Object executePadding(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              CssGeneratorConfig::spacing
          ),

          IDENTITY,

          propertyName1,
          propertyName2
      );

      default -> error(key, action, null);
    };
  }

  private Object executePointerEvents(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "pointer-events-auto", "pointer-events: auto")
          .rule(key, "pointer-events-none", "pointer-events: none");

      default -> error(key, action, arg);
    };
  }

  private Object executePosition(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "static", "position: static")
          .rule(key, "fixed", "position: fixed")
          .rule(key, "absolute", "position: absolute")
          .rule(key, "relative", "position: relative")
          .rule(key, "sticky", "position: sticky");

      default -> error(key, action, arg);
    };
  }

  private Object executeSize(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "size");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

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

                  c.spacing()
              )
          ),

          IDENTITY,

          "height", "width"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeStroke(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "stroke");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              c -> Css.merge(
                  """
                  none: none
                  """,

                  c.colors()
              )
          ),

          IDENTITY,

          "stroke"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeStrokeWidth(CssKey key, CssAction action) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, "stroke");

      case RESOLVER -> new CssRuleResolver.OfProperties(
          key,

          config.values(
              key,

              """
              0: 0
              1: 1
              2: 2
              """
          ),

          IDENTITY,

          "stroke-width"
      );

      default -> error(key, action, null);
    };
  }

  private Object executeTableLayout(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "table-auto", "table-layout: auto")
          .rule(key, "table-fixed", "table-layout: fixed");

      default -> error(key, action, arg);
    };
  }

  private Object executeTextAlign(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "text-left", "text-align: left")
          .rule(key, "text-center", "text-align: center")
          .rule(key, "text-right", "text-align: right")
          .rule(key, "text-justify", "text-align: justify")
          .rule(key, "text-start", "text-align: start")
          .rule(key, "text-end", "text-align: end");

      default -> error(key, action, arg);
    };
  }

  private Object executeTextDecoration(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "underline", "text-decoration-line: underline")
          .rule(key, "overline", "text-decoration-line: overline")
          .rule(key, "line-through", "text-decoration-line: line-through")
          .rule(key, "no-underline", "text-decoration-line: none");

      default -> error(key, action, arg);
    };
  }

  private Object executeTextWrap(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "text-wrap", "text-wrap: wrap")
          .rule(key, "text-nowrap", "text-wrap: nowrap")
          .rule(key, "text-balance", "text-wrap: balance")
          .rule(key, "text-pretty", "text-wrap: pretty");

      default -> error(key, action, arg);
    };
  }

  private Object executeUserSelect(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "select-none", "user-select: none")
          .rule(key, "select-text", "user-select: text")
          .rule(key, "select-all", "user-select: all")
          .rule(key, "select-auto", "user-select: auto");

      default -> error(key, action, arg);
    };
  }

  private Object executeVerticalAlign(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "align-baseline", "vertical-align: baseline")
          .rule(key, "align-top", "vertical-align: top")
          .rule(key, "align-middle", "vertical-align: middle")
          .rule(key, "align-bottom", "vertical-align: bottom")
          .rule(key, "align-text-top", "vertical-align: text-top")
          .rule(key, "align-text-bottom", "vertical-align: text-bottom")
          .rule(key, "align-sub", "vertical-align: sub")
          .rule(key, "align-super", "vertical-align: super");

      default -> error(key, action, arg);
    };
  }

  private Object executeVisibility(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "visible", "visibility: visible")
          .rule(key, "invisible", "visibility: hidden")
          .rule(key, "collapse", "visibility: collapse");

      default -> error(key, action, arg);
    };
  }

  // util

  private Object error(CssKey key, CssAction action, Object arg) {
    throw new AssertionError(
        "Unexpected action " + action + " for key " + key + ". Arg=" + arg
    );
  }

  private CssStaticTable staticTable() {
    return (CssStaticTable) config.get(CssKey._STATIC_TABLE);
  }

}