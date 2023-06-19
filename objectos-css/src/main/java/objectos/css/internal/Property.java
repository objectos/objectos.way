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
  BORDER_BOTTOM_WIDTH("border-bottom-width"),

  BORDER_COLLAPSE("border-collapse"),

  BORDER_COLOR("border-color"),

  BORDER_LEFT_WIDTH("border-left-width"),

  BORDER_RIGHT_WIDTH("border-right-width"),

  BORDER_STYLE("border-style"),

  BORDER_TOP_WIDTH("border-top-width"),

  BORDER_WIDTH("border-width"),

  BOTTOM("bottom"),

  BOX_SIZING("box-sizing"),

  COLOR("color"),

  FONT_FAMILY("font-family"),

  FONT_FEATURE_SETTINGS("font-feature-settings"),

  FONT_SIZE("font-size"),

  FONT_VARIATION_SETTINGS("font-variation-settings"),

  FONT_WEIGHT("font-weight"),

  HEIGHT("height"),

  LINE_HEIGHT("line-height"),

  MARGIN("margin"),

  PADDING("padding"),

  PADDING_BOTTOM("padding-bottom"),

  PADDING_LEFT("padding-left"),

  PADDING_RIGHT("padding-right"),

  PADDING_TOP("padding-top"),

  POSITION("position"),

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

  _MOZ_TAB_SIZE("-moz-tab-size"),

  _WEBKIT_TEXT_SIZE_ADJUST("-webkit-text-size-adjust");

  private final String propertyName;

  private Property(String propertyName) {
    this.propertyName = propertyName;
  }

  @Override
  public final String toString() {
    return propertyName;
  }
}
