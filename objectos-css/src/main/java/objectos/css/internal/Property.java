/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.om.PropertyName;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public enum Property implements PropertyName {
  APPEARANCE("appearance"),

  BACKGROUND_COLOR("background-color"),

  BACKGROUND_IMAGE("background-image"),

  BORDER_BOTTOM_WIDTH("border-bottom-width"),

  BORDER_COLLAPSE("border-collapse"),

  BORDER_COLOR("border-color"),

  BORDER_LEFT_WIDTH("border-left-width"),

  BORDER_RIGHT_WIDTH("border-right-width"),

  BORDER_STYLE("border-style"),

  BORDER_TOP_WIDTH("border-top-width"),

  BORDER_WIDTH("border-width"),

  BOTTOM("bottom"),

  BOX_SHADOW("box-shadow"),

  BOX_SIZING("box-sizing"),

  COLOR("color"),

  CURSOR("cursor"),

  DISPLAY("display"),

  FLEX_DIRECTION("flex-direction"),

  FONT("font"),

  FONT_FAMILY("font-family"),

  FONT_FEATURE_SETTINGS("font-feature-settings"),

  FONT_SIZE("font-size"),

  FONT_VARIATION_SETTINGS("font-variation-settings"),

  FONT_WEIGHT("font-weight"),

  HEIGHT("height"),

  JUSTIFY_CONTENT("justify-content"),

  LINE_HEIGHT("line-height"),

  LIST_STYLE("list-style"),

  LIST_STYLE_IMAGE("list-style-image"),

  LIST_STYLE_POSITION("list-style-position"),

  LIST_STYLE_TYPE("list-style-type"),

  MARGIN("margin"),

  MARGIN_BOTTOM("margin-bottom"),

  MARGIN_LEFT("margin-left"),

  MARGIN_RIGHT("margin-right"),

  MARGIN_TOP("margin-top"),

  MAX_HEIGHT("max-height"),

  MAX_WIDTH("max-width"),

  MIN_HEIGHT("min-height"),

  MIN_WIDTH("min-width"),

  OPACITY("opacity"),

  OUTLINE("outline"),

  OUTLINE_COLOR("outline-color"),

  OUTLINE_OFFSET("outline-offset"),

  OUTLINE_STYLE("outline-style"),

  OUTLINE_WIDTH("outline-width"),

  PADDING("padding"),

  PADDING_BOTTOM("padding-bottom"),

  PADDING_LEFT("padding-left"),

  PADDING_RIGHT("padding-right"),

  PADDING_TOP("padding-top"),

  POSITION("position"),

  RESIZE("resize"),

  TAB_SIZE("tab-size"),

  TEXT_DECORATION("text-decoration"),

  TEXT_DECORATION_COLOR("text-decoration-color"),

  TEXT_DECORATION_LINE("text-decoration-line"),

  TEXT_DECORATION_STYLE("text-decoration-style"),

  TEXT_DECORATION_THICKNESS("text-decoration-thickness"),

  TEXT_INDENT("text-indent"),

  TEXT_TRANSFORM("text-transform"),

  TOP("top"),

  VERTICAL_ALIGN("vertical-align"),

  WIDTH("width"),

  _MOZ_APPEARANCE("-moz-appearance"),

  _MOZ_TAB_SIZE("-moz-tab-size"),

  _WEBKIT_APPEARANCE("-webkit-appearance"),

  _WEBKIT_TEXT_SIZE_ADJUST("-webkit-text-size-adjust");

  private static final Property[] VALUES = values();

  public final String cssName;

  private Property(String cssName) {
    this.cssName = cssName;
  }

  public static Property byOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  @Override
  public final String toString() {
    return cssName;
  }
}
