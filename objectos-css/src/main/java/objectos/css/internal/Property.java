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

  BORDER_COLOR("border-color"),

  BORDER_LEFT_WIDTH("border-left-width"),

  BORDER_RIGHT_WIDTH("border-right-width"),

  BORDER_STYLE("border-style"),

  BORDER_TOP_WIDTH("border-top-width"),

  BORDER_WIDTH("border-width"),

  BOX_SIZING("box-sizing"),

  COLOR("color"),

  FONT_FAMILY("font-family"),

  FONT_FEATURE_SETTINGS("font-feature-settings"),

  FONT_VARIATION_SETTINGS("font-variation-settings"),

  HEIGHT("height"),

  LINE_HEIGHT("line-height"),

  MARGIN("margin"),

  TAB_SIZE("tab-size"),

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
