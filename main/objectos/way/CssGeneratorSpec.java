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

final class CssGeneratorSpec extends CssGeneratorRound implements Css.Generator {

  private static final String DEFAULT_SPACING = """
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

  CssGeneratorSpec(CssGeneratorConfig config) {
    super(config);
  }

  @Override
  final Object execute(CssKey key, CssAction action, Object arg) {
    return switch (key) {
      case _STATIC_TABLE -> _executeStaticTable(key, action, arg);

      case ACCESSIBILITY -> executeAccessibility(key, action, arg);

      case ALIGN_ITEMS -> executeAlignItems(key, action, arg);

      case BACKGROUND_COLOR -> executeBackgroundColor(key, action, arg);

      case BORDER_COLLAPSE -> executeBorderCollapse(key, action, arg);

      case BORDER_COLOR -> executeBorderColor(key, action, "border", "border-color");
      case BORDER_COLOR_BOTTOM -> executeBorderColor(key, action, "border-b", "border-bottom-color");
      case BORDER_COLOR_LEFT -> executeBorderColor(key, action, "border-l", "border-left-color");
      case BORDER_COLOR_RIGHT -> executeBorderColor(key, action, "border-r", "border-right-color");
      case BORDER_COLOR_TOP -> executeBorderColor(key, action, "border-t", "border-top-color");
      case BORDER_COLOR_X -> executeBorderColor(key, action, "border-x", "border-left-color", "border-right-color");
      case BORDER_COLOR_Y -> executeBorderColor(key, action, "border-y", "border-top-color", "border-bottom-color");

      case BORDER_RADIUS -> null;
      case BORDER_RADIUS_B -> null;
      case BORDER_RADIUS_BL -> null;
      case BORDER_RADIUS_BR -> null;
      case BORDER_RADIUS_E -> null;
      case BORDER_RADIUS_EE -> null;
      case BORDER_RADIUS_ES -> null;
      case BORDER_RADIUS_L -> null;
      case BORDER_RADIUS_R -> null;
      case BORDER_RADIUS_S -> null;
      case BORDER_RADIUS_SE -> null;
      case BORDER_RADIUS_SS -> null;
      case BORDER_RADIUS_T -> null;
      case BORDER_RADIUS_TL -> null;
      case BORDER_RADIUS_TR -> null;

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

      case CONTENT -> null;
      case CURSOR -> null;
      case CUSTOM -> null;

      case DISPLAY -> executeDisplay(key, action, arg);

      case END -> executeInset(key, action, "end", "inset-inline-end");

      case FILL -> null;

      case FLEX_DIRECTION -> executeFlexDirection(key, action, arg);

      case FLEX_GROW -> null;
      case FONT_SIZE1 -> null;
      case FONT_SIZE2 -> null;
      case FONT_SIZE3 -> null;
      case FONT_SIZE4 -> null;
      case FONT_WEIGHT -> null;
      case GAP -> null;
      case GAP_X -> null;
      case GAP_Y -> null;
      case GRID_COLUMN -> null;
      case GRID_COLUMN_END -> null;
      case GRID_COLUMN_START -> null;
      case GRID_TEMPLATE_COLUMNS -> null;
      case GRID_TEMPLATE_ROWS -> null;
      case HEIGHT -> null;

      case INSET -> executeInset(key, action, "inset", "inset");
      case INSET_X -> executeInset(key, action, "inset-x", "left", "right");
      case INSET_Y -> executeInset(key, action, "inset-y", "top", "bottom");

      case JUSTIFY_CONTENT -> executeJustifyContent(key, action, arg);

      case LEFT -> executeInset(key, action, "left", "left");

      case LETTER_SPACING -> null;
      case LINE_HEIGHT -> null;
      case MARGIN -> null;
      case MARGIN_BOTTOM -> null;
      case MARGIN_LEFT -> null;
      case MARGIN_RIGHT -> null;
      case MARGIN_TOP -> null;
      case MARGIN_X -> null;
      case MARGIN_Y -> null;
      case MAX_WIDTH -> null;
      case MIN_WIDTH -> null;
      case OPACITY -> null;
      case OUTLINE_COLOR -> null;
      case OUTLINE_OFFSET -> null;
      case OUTLINE_STYLE -> null;
      case OUTLINE_STYLE_NONE -> null;
      case OUTLINE_WIDTH -> null;

      case OVERFLOW -> executeOverflow(key, action, arg);
      case OVERFLOW_X -> executeOverflowX(key, action, arg);
      case OVERFLOW_Y -> executeOverflowY(key, action, arg);

      case PADDING -> null;
      case PADDING_BOTTOM -> null;
      case PADDING_LEFT -> null;
      case PADDING_RIGHT -> null;
      case PADDING_TOP -> null;
      case PADDING_X -> null;
      case PADDING_Y -> null;

      case POINTER_EVENTS -> executePointerEvents(key, action, arg);

      case POSITION -> executePosition(key, action, arg);

      case RIGHT -> executeInset(key, action, "right", "right");

      case SIZE -> null;

      case START -> executeInset(key, action, "start", "inset-inline-start");

      case STROKE -> null;
      case STROKE_WIDTH -> null;

      case TABLE_LAYOUT -> executeTableLayout(key, action, arg);

      case TEXT_ALIGN -> executeTextAlign(key, action, arg);

      case TEXT_COLOR -> null;

      case TEXT_DECORATION -> executeTextDecoration(key, action, arg);

      case TEXT_WRAP -> executeTextWrap(key, action, arg);

      case TOP -> executeInset(key, action, "top", "top");

      case TRANSITION_DURATION -> null;
      case TRANSITION_PROPERTY -> null;

      case USER_SELECT -> executeUserSelect(key, action, arg);

      case VERTICAL_ALIGN -> executeVerticalAlign(key, action, arg);

      case VISIBILITY -> executeVisibility(key, action, arg);

      case WIDTH -> null;
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

      case RESOLVER -> {
        String variableName;
        variableName = "--tw-border-opacity";

        Map<String, String> colors;
        colors = config.colorsAlpha(CssKey.BORDER_COLOR, variableName);

        yield new CssRuleResolver.OfColorAlpha(key, variableName, colors, propertyName1, propertyName2);
      }

      default -> error(key, action, null);
    };
  }

  private Object executeBorderSpacing(CssKey key, CssAction action, String prefix, Function<String, String> valueConverter) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> {
        Map<String, String> borderSpacing;
        borderSpacing = config.values(CssKey.BORDER_SPACING, config::spacing);

        CssValueFormatter formatter;
        formatter = new CssValueFormatter.OfFunction(valueConverter);

        yield new CssRuleResolver.OfProperties(key, borderSpacing, formatter, "border-spacing");
      }

      default -> error(key, action, null);
    };
  }

  private Object executeBorderWidth(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeBorderWidth(key, action, prefix, propertyName1, null);
  }

  private Object executeBorderWidth(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> {
        String defaults = """
        : 1px
        0: 0px
        2: 2px
        4: 4px
        8: 8px
        """;

        Map<String, String> borderWidth;
        borderWidth = config.values(CssKey.BORDER_WIDTH, defaults);

        yield new CssRuleResolver.OfProperties(key, borderWidth, CssValueFormatter.Identity.INSTANCE, propertyName1, propertyName2);
      }

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

  private Object executeFlexDirection(CssKey key, CssAction action, Object arg) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> staticTable()
          .rule(key, "flex-row", "flex-direction: row")
          .rule(key, "flex-row-reverse", "flex-direction: row-reverse")
          .rule(key, "flex-col", "flex-direction: column")
          .rule(key, "flex-col-reverse", "flex-direction: column-reverse");

      default -> error(key, action, arg);
    };
  }

  private static final String DEFAULT_INSET = DEFAULT_SPACING + """
      auto: auto
      1/2: 50%
      1/3: 33.333333%
      2/3: 66.666667%
      1/4: 25%
      2/4: 50%
      3/4: 75%
      full: 100%
      """;

  private Object executeInset(CssKey key, CssAction action, String prefix, String propertyName1) {
    return executeInset(key, action, prefix, propertyName1, null);
  }

  private Object executeInset(CssKey key, CssAction action, String prefix, String propertyName1, String propertyName2) {
    return switch (action) {
      case CONFIG_STATIC_TABLE -> config.prefix(key, prefix);

      case RESOLVER -> {
        Map<String, String> borderWidth;
        borderWidth = config.values(CssKey.INSET, DEFAULT_INSET);

        yield new CssRuleResolver.OfProperties(
            key, borderWidth, CssValueFormatter.NegativeValues.INSTANCE, propertyName1, propertyName2
        );
      }

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