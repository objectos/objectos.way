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

final class CssGeneratorSpec extends CssGeneratorRound implements Css.Generator {

  CssGeneratorSpec(CssGeneratorConfig config) {
    super(config);
  }

  @Override
  final Object execute(CssKey key, CssAction action, Object arg) {
    return switch (key) {
      case $MARKER_UTILITIES_END -> marker();
      case $MARKER_UTILITIES_START -> marker();
      case _STATIC_TABLE -> _executeStaticTable(key, action, arg);
      case ACCESSIBILITY -> executeAccessibility(key, action, arg);
      case ALIGN_ITEMS -> null;
      case BACKGROUND_COLOR -> null;
      case BORDER_COLLAPSE -> null;
      case BORDER_COLOR -> null;
      case BORDER_COLOR_BOTTOM -> null;
      case BORDER_COLOR_LEFT -> null;
      case BORDER_COLOR_RIGHT -> null;
      case BORDER_COLOR_TOP -> null;
      case BORDER_COLOR_X -> null;
      case BORDER_COLOR_Y -> null;
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
      case BORDER_SPACING -> null;
      case BORDER_WIDTH -> null;
      case BORDER_WIDTH_BOTTOM -> null;
      case BORDER_WIDTH_LEFT -> null;
      case BORDER_WIDTH_RIGHT -> null;
      case BORDER_WIDTH_TOP -> null;
      case BORDER_WIDTH_X -> null;
      case BORDER_WIDTH_Y -> null;
      case BOTTOM -> null;
      case CONTENT -> null;
      case CURSOR -> null;
      case CUSTOM -> null;
      case DISPLAY -> null;
      case END -> null;
      case FILL -> null;
      case FLEX_DIRECTION -> null;
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
      case INSET -> null;
      case INSET_X -> null;
      case INSET_Y -> null;
      case JUSTIFY_CONTENT -> null;
      case LEFT -> null;
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
      case OVERFLOW -> null;
      case OVERFLOW_X -> null;
      case OVERFLOW_Y -> null;
      case PADDING -> null;
      case PADDING_BOTTOM -> null;
      case PADDING_LEFT -> null;
      case PADDING_RIGHT -> null;
      case PADDING_TOP -> null;
      case PADDING_X -> null;
      case PADDING_Y -> null;
      case POINTER_EVENTS -> null;
      case POSITION -> null;
      case RIGHT -> null;
      case SIZE -> null;
      case START -> null;
      case STROKE -> null;
      case STROKE_WIDTH -> null;
      case TABLE_LAYOUT -> null;
      case TEXT_ALIGN -> null;
      case TEXT_COLOR -> null;
      case TEXT_DECORATION -> null;
      case TEXT_WRAP -> null;
      case TOP -> null;
      case TRANSITION_DURATION -> null;
      case TRANSITION_PROPERTY -> null;
      case USER_SELECT -> null;
      case VERTICAL_ALIGN -> null;
      case VISIBILITY -> null;
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

  // util

  private Object error(CssKey key, CssAction action, Object arg) {
    throw new AssertionError(
        "Unexpected action " + action + " for key " + key + ". Arg=" + arg
    );
  }

  private Object marker() {
    return null;
  }

  private CssStaticTable staticTable() {
    return (CssStaticTable) config.get(CssKey._STATIC_TABLE);
  }

}