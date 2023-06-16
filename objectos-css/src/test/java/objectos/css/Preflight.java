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
package objectos.css;

final class Preflight extends CssTemplate {
  @Override
  protected final void definition() {
    style(
      any, __before, __after,

      boxSizing(borderBox),
      borderWidth($0),
      borderStyle(solid),
      borderColor(currentcolor)
    );

    style(
      html,

      lineHeight(1.5),
      webkitTextSizeAdjust(pct(100)),
      mozTabSize(4),
      tabSize(4),
      fontFamily(
        uiSansSerif,
        systemUi,
        l("-apple-system"),
        l("BlinkMacSystemFont"),
        l("Segoe UI"),
        l("Roboto"),
        l("Helvetica Neue"),
        l("Arial"),
        l("Noto Sans"),
        sansSerif,
        l("Apple Color Emoji"),
        l("Segoe UI Emoji"),
        l("Segoe UI Symbol"),
        l("Noto Color Emoji")
      ),
      fontFeatureSettings(normal),
      fontVariationSettings(normal)
    );

    style(
      body,

      margin($0),
      lineHeight(inherit)
    );

    style(
      hr,

      height($0),
      color(inherit),
      borderTopWidth(px(1))
    );

    style(
      h1, h2, h3, h4, h5, h6,

      fontSize(inherit),
      fontWeight(inherit)
    );

    style(
      a,

      color(inherit),
      textDecoration(inherit)
    );
  }
}