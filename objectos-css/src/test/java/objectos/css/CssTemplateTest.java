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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class CssTemplateTest {

  @Test(description = """
  [#394] Preflight 01

  - border-color
  """)
  public void propertyBorderColor() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            borderColor(currentcolor),
            borderColor(black, white),
            borderColor(blue, red, green),
            borderColor(teal, yellow, aqua, fuchsia),
            borderColor(transparent, inherit, gray, maroon),
            borderColor(navy)
          );
        }
      },

      """
      * {
        border-color: currentcolor;
        border-color: black white;
        border-color: blue red green;
        border-color: teal yellow aqua fuchsia;
        border-color: transparent inherit gray maroon;
        border-color: navy;
      }
      """
    );
  }

  @Test(description = """
  [#394] Preflight 01

  - border-style
  """)
  public void propertyBorderStyle() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            borderStyle(none),
            borderStyle(hidden, dotted),
            borderStyle(dashed, solid, double$),
            borderStyle(groove, ridge, inset, outset),
            borderStyle(inherit)
          );
        }
      },

      """
      * {
        border-style: none;
        border-style: hidden dotted;
        border-style: dashed solid double;
        border-style: groove ridge inset outset;
        border-style: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#394] Preflight 01

  - border-width
  """)
  public void propertyBorderWidth() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            borderWidth($0),
            borderWidth(medium, thick),
            borderWidth(medium, thick, thin),
            borderWidth(medium, thick, thin, unset)
          );
        }
      },

      """
      * {
        border-width: 0;
        border-width: medium thick;
        border-width: medium thick thin;
        border-width: medium thick thin unset;
      }
      """
    );
  }

  @Test(description = """
  [#394] Preflight 01

  - box-sizing
  """)
  public void propertyBoxSizing() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,

            boxSizing(borderBox),
            boxSizing(contentBox),
            boxSizing(inherit),
            boxSizing(initial),
            boxSizing(unset)
          );
        }
      },

      """
      * {
        box-sizing: border-box;
        box-sizing: content-box;
        box-sizing: inherit;
        box-sizing: initial;
        box-sizing: unset;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - line-height
  """)
  public void propertyLineHeight() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            lineHeight(normal),
            lineHeight(1.5),
            lineHeight(2),
            lineHeight(px(14)),
            lineHeight(em(3)),
            lineHeight(pct(40.5)),
            lineHeight(pct(23)),
            lineHeight(inherit)
          );
        }
      },

      """
      html {
        line-height: normal;
        line-height: 1.5;
        line-height: 2;
        line-height: 14px;
        line-height: 3em;
        line-height: 40.5%;
        line-height: 23%;
        line-height: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - tab-size
  - -moz-tab-size
  """)
  public void propertyTabSize() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            tabSize(4),
            tabSize(px(10)),
            mozTabSize(4),
            tabSize(inherit)
          );
        }
      },

      """
      html {
        tab-size: 4;
        tab-size: 10px;
        -moz-tab-size: 4;
        tab-size: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - -webkit-text-size-adjust
  """)
  public void propertyTextSizeAdjust() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            webkitTextSizeAdjust(pct(100)),
            webkitTextSizeAdjust(auto),
            webkitTextSizeAdjust(none),
            webkitTextSizeAdjust(inherit)
          );
        }
      },

      """
      html {
        -webkit-text-size-adjust: 100%;
        -webkit-text-size-adjust: auto;
        -webkit-text-size-adjust: none;
        -webkit-text-size-adjust: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#394] Preflight 01

  - selector list
  """)
  public void selectorList() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any,
            __after,
            __before
          );
        }
      },

      """
      *, ::after, ::before {}
      """
    );
  }

  @Test(description = """
  [#394] Preflight 01

  - universal selector
  """)
  public void selectorUniversal() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            any
          );
        }
      },

      """
      * {}
      """
    );
  }

  private void test(CssTemplate template, String pretty) {
    var sheet = template.toStyleSheet();
    assertEquals(sheet.toString(), pretty);
  }

}