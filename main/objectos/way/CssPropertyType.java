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
                       Two borderColorX,
                       Two borderColorY,

                       String borderRadiusTopLeft,
                       String borderRadiusTopRight,
                       String borderRadiusBottomRight,
                       String borderRadiusBottomLeft,
                       Two borderRadiusTop,
                       Two borderRadiusRight,
                       Two borderRadiusBottom,
                       Two borderRadiusLeft,

                       String borderWidthTop,
                       String borderWidthRight,
                       String borderWidthBottom,
                       String borderWidthLeft,
                       Two borderWidthX,
                       Two borderWidthY,

                       String height,

                       String width,
                       String maxWidth,
                       String minWidth) {

  record Two(String first, String second) {}

  static final CssPropertyType PHYSICAL = new CssPropertyType(
      "border-top-color",
      "border-right-color",
      "border-bottom-color",
      "border-left-color",
      two("border-left-color", "border-right-color"),
      two("border-top-color", "border-bottom-color"),

      "border-top-left-radius",
      "border-top-right-radius",
      "border-bottom-right-radius",
      "border-bottom-left-radius",
      two("border-top-left-radius", "border-top-right-radius"),
      two("border-top-right-radius", "border-bottom-right-radius"),
      two("border-bottom-right-radius", "border-bottom-left-radius"),
      two("border-bottom-left-radius", "border-top-left-radius"),

      "border-top-width",
      "border-right-width",
      "border-bottom-width",
      "border-left-width",
      two("border-left-width", "border-right-width"),
      two("border-top-width", "border-bottom-width"),

      "height",

      "width",
      "max-width",
      "min-width"
  );

  static final CssPropertyType LOGICAL = new CssPropertyType(
      "border-block-start-color",
      "border-inline-end-color",
      "border-block-end-color",
      "border-inline-start-color",
      two("border-inline-start-color", "border-inline-end-color"),
      two("border-block-start-color", "border-block-end-color"),

      "border-start-start-radius",
      "border-start-end-radius",
      "border-end-end-radius",
      "border-end-start-radius",
      two("border-start-start-radius", "border-start-end-radius"),
      two("border-start-end-radius", "border-end-end-radius"),
      two("border-end-end-radius", "border-end-start-radius"),
      two("border-end-start-radius", "border-start-start-radius"),

      "border-block-start-width",
      "border-inline-end-width",
      "border-block-end-width",
      "border-inline-start-width",
      two("border-inline-start-width", "border-inline-end-width"),
      two("border-block-start-width", "border-block-end-width"),

      "block-size",

      "inline-size",
      "max-inline-size",
      "min-inline-size"
  );

  private static Two two(String first, String second) {
    return new Two(first, second);
  }

}