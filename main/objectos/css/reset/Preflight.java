/*
 * Based on Tailwind CSS Preflight
 *
 * MIT License
 *
 * Copyright (c) Nicolas Gallagher
 * Copyright (c) Jonathan Neal
 * Copyright (c) Sindre Sorhus <sindresorhus@gmail.com> (sindresorhus.com)
 * Copyright (c) Adam Wathan
 * Copyright (c) Jonathan Reinink
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * Modifications:
 *
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.css.reset;

import objectos.css.CssTemplate;

public final class Preflight extends CssTemplate {
  public Preflight() {}

  @Override
  protected final void definition() {
    style(
      any, OR,
      __before, OR,
      __after,

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
      h1, OR,
      h2, OR,
      h3, OR,
      h4, OR,
      h5, OR,
      h6,

      fontSize(inherit),
      fontWeight(inherit)
    );

    style(
      a,

      color(inherit),
      textDecoration(inherit)
    );

    style(
      b, OR,
      strong,

      fontWeight(bolder)
    );

    style(
      code, OR,
      kbd, OR,
      samp, OR,
      pre,

      fontFamily(
        uiMonospace,
        l("SFMono-Regular"),
        l("Menlo"),
        l("Monaco"),
        l("Consolas"),
        l("Liberation Mono"),
        l("Courier New"),
        monospace
      ),
      fontSize(em(1))
    );

    style(
      small,

      fontSize(pct(80))
    );

    style(
      sub, OR,
      sup,

      fontSize(pct(75)),
      lineHeight(0),
      position(relative),
      verticalAlign(baseline)
    );

    style(
      sub,

      bottom(em(-0.25))
    );

    style(
      sup,

      top(em(-0.5))
    );

    style(
      table,

      textIndent($0),
      borderColor(inherit),
      borderCollapse(collapse)
    );

    style(
      button, OR,
      input, OR,
      optgroup, OR,
      select, OR,
      textarea,

      fontFamily(inherit),
      fontFeatureSettings(inherit),
      fontVariationSettings(inherit),
      fontSize(pct(100)),
      fontWeight(inherit),
      lineHeight(inherit),
      color(inherit),
      margin($0),
      padding($0)
    );

    style(
      button, OR,
      select,

      textTransform(none)
    );

    style(
      button, OR,
      attr("type", EQ, "button"), OR,
      attr("type", EQ, "reset"), OR,
      attr("type", EQ, "submit"),

      webkitAppearance(button),
      backgroundColor(transparent),
      backgroundImage(none)
    );

    style(
      _mozFocusring,

      outline(auto)
    );

    style(
      _mozUiInvalid,

      boxShadow(none)
    );

    style(
      progress,

      verticalAlign(baseline)
    );

    style(
      __webkitInnerSpinButton, OR,
      __webkitOuterSpinButton,

      height(auto)
    );

    style(
      attr("type", EQ, "search"),

      outlineOffset(px(-2))
    );

    style(
      __webkitSearchDecoration,

      webkitAppearance(none)
    );

    style(
      __webkitFileUploadButton,

      webkitAppearance(button),
      font(inherit)
    );

    style(
      summary,

      display(listItem)
    );

    style(
      blockquote, OR,
      dl, OR,
      dd, OR,
      h1, OR,
      h2, OR,
      h3, OR,
      h4, OR,
      h5, OR,
      h6, OR,
      hr, OR,
      figure, OR,
      p, OR,
      pre,

      margin($0)
    );

    style(
      fieldset,

      margin($0),
      padding($0)
    );

    style(
      legend,

      padding($0)
    );

    style(
      ol, OR,
      ul, OR,
      menu,

      listStyle(none),
      margin($0),
      padding($0)
    );

    style(
      textarea,

      resize(vertical)
    );

    style(
      input, __placeholder, OR,
      textarea, __placeholder,

      opacity(1),
      color(hex("#9ca3af"))
    );

    style(
      button, OR,
      attr("role", EQ, "button"),

      cursor(pointer)
    );

    style(
      _disabled,

      cursor(default_)
    );

    style(
      img, OR,
      svg, OR,
      video, OR,
      canvas, OR,
      audio, OR,
      iframe, OR,
      embed, OR,
      object,

      display(block),
      verticalAlign(middle)
    );

    style(
      img, OR,
      video,

      maxWidth(pct(100)),
      height(auto)
    );

    style(
      attr("hidden"),

      display(none)
    );
  }
}