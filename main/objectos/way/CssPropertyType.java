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

record CssPropertyType(String borderColorTop,
                       String borderColorRight,
                       String borderColorBottom,
                       String borderColorLeft,

                       String borderRadiusTopLeft,
                       String borderRadiusTopRight,
                       String borderRadiusBottomRight,
                       String borderRadiusBottomLeft,

                       String borderWidthTop,
                       String borderWidthRight,
                       String borderWidthBottom,
                       String borderWidthLeft,

                       String height,

                       String marginTop,
                       String marginRight,
                       String marginBottom,
                       String marginLeft,

                       String paddingTop,
                       String paddingRight,
                       String paddingBottom,
                       String paddingLeft,

                       String width,
                       String maxWidth,
                       String minWidth) {

  static final CssPropertyType PHYSICAL = new CssPropertyType(
      "border-top-color",
      "border-right-color",
      "border-bottom-color",
      "border-left-color",

      "border-top-left-radius",
      "border-top-right-radius",
      "border-bottom-right-radius",
      "border-bottom-left-radius",

      "border-top-width",
      "border-right-width",
      "border-bottom-width",
      "border-left-width",

      "height",

      "margin-top", "margin-right", "margin-bottom", "margin-left",

      "padding-top", "padding-right", "padding-bottom", "padding-left",

      "width",
      "max-width",
      "min-width"
  );

  static final CssPropertyType LOGICAL = new CssPropertyType(
      "border-block-start-color",
      "border-inline-end-color",
      "border-block-end-color",
      "border-inline-start-color",

      "border-start-start-radius",
      "border-start-end-radius",
      "border-end-end-radius",
      "border-end-start-radius",

      "border-block-start-width",
      "border-inline-end-width",
      "border-block-end-width",
      "border-inline-start-width",

      "block-size",

      "margin-block-start", "margin-inline-end", "margin-block-end", "margin-inline-start",

      "padding-block-start", "padding-inline-end", "padding-block-end", "padding-inline-start",

      "inline-size",
      "max-inline-size",
      "min-inline-size"
  );

}