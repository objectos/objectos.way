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

import java.util.EnumSet;
import java.util.Set;

enum CssKey {

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
  START,
  END,
  TOP,
  RIGHT,
  BOTTOM,
  LEFT,

  Z_INDEX,

  GRID_COLUMN,
  GRID_COLUMN_START,
  GRID_COLUMN_END,

  MARGIN,
  MARGIN_X,
  MARGIN_Y,
  MARGIN_TOP,
  MARGIN_RIGHT,
  MARGIN_BOTTOM,
  MARGIN_LEFT,

  DISPLAY,

  SIZE,
  HEIGHT,
  WIDTH,
  MIN_WIDTH,
  MAX_WIDTH,

  FLEX_GROW,

  TABLE_LAYOUT,

  BORDER_COLLAPSE,
  BORDER_SPACING,
  BORDER_SPACING_X,
  BORDER_SPACING_Y,

  CURSOR,

  USER_SELECT,

  GRID_TEMPLATE_COLUMNS,
  GRID_TEMPLATE_ROWS,

  FLEX_DIRECTION,
  ALIGN_ITEMS,
  JUSTIFY_CONTENT,
  GAP,
  GAP_X,
  GAP_Y,

  OVERFLOW,
  OVERFLOW_X,
  OVERFLOW_Y,

  TEXT_WRAP,

  BORDER_RADIUS,
  BORDER_RADIUS_S,
  BORDER_RADIUS_E,
  BORDER_RADIUS_T,
  BORDER_RADIUS_R,
  BORDER_RADIUS_B,
  BORDER_RADIUS_L,
  BORDER_RADIUS_SS,
  BORDER_RADIUS_SE,
  BORDER_RADIUS_EE,
  BORDER_RADIUS_ES,
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

  LINE_HEIGHT,

  LETTER_SPACING,

  TEXT_COLOR,
  TEXT_DECORATION,

  OPACITY,

  OUTLINE_STYLE,
  OUTLINE_WIDTH,
  OUTLINE_OFFSET,
  OUTLINE_COLOR,

  TRANSITION_PROPERTY,
  TRANSITION_DURATION,

  CONTENT;

  static final Set<CssKey> UNIVERSE = EnumSet.allOf(CssKey.class);

}