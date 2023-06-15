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

import java.util.Locale;
import java.util.Objects;
import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.om.StyleDeclaration;

public enum Property implements PropertyName {

  // B
  BACKGROUND_COLOR,
  BORDER,
  BORDER_COLOR,
  BORDER_STYLE,
  BORDER_WIDTH,
  BOX_SIZING,

  CONTENT,

  DISPLAY,

  // F
  FONT_FAMILY,
  FONT_FEATURE_SETTINGS,
  FONT_VARIATION_SETTINGS,

  // H
  HEIGHT,

  LINE_HEIGHT,

  // M
  MARGIN,
  MIN_HEIGHT,

  // T
  TAB_SIZE,
  _MOZ_TAB_SIZE,
  _WEBKIT_TEXT_SIZE_ADJUST,

  Z_INDEX;

  private final String propertyName;

  private Property() {
    propertyName = name().toLowerCase(Locale.US).replace('_', '-');
  }

  public final StyleDeclaration sides1(PropertyValue all) {
    Objects.requireNonNull(all, "all == null");

    return new StyleDeclaration1(this, all);
  }

  public final StyleDeclaration sides2(PropertyValue vertical, PropertyValue horizontal) {
    Objects.requireNonNull(vertical, "vertical == null");
    Objects.requireNonNull(horizontal, "horizontal == null");

    return new StyleDeclaration2(this, vertical, horizontal);
  }

  public final StyleDeclaration sides3(
      PropertyValue top, PropertyValue horizontal, PropertyValue bottom) {
    Objects.requireNonNull(top, "top == null");
    Objects.requireNonNull(horizontal, "horizontal == null");
    Objects.requireNonNull(bottom, "bottom == null");

    return new StyleDeclaration3(this, top, horizontal, bottom);
  }

  public final StyleDeclaration sides4(
      PropertyValue top, PropertyValue right, PropertyValue bottom, PropertyValue left) {
    Objects.requireNonNull(top, "top == null");
    Objects.requireNonNull(right, "right == null");
    Objects.requireNonNull(bottom, "bottom == null");
    Objects.requireNonNull(left, "left == null");

    return new StyleDeclaration4(this, top, right, bottom, left);
  }

  @Override
  public final String toString() {
    return propertyName;
  }

  public final StyleDeclaration value(PropertyValue value) {
    Objects.requireNonNull(value, "value == null");

    return new StyleDeclaration1(this, value);
  }

  public final StyleDeclaration value(int value) {
    return new StyleDeclarationInt(this, value);
  }

  public final StyleDeclaration value(double value) {
    return new StyleDeclarationDouble(this, value);
  }

}