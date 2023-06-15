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
  [#397] Preflight 04

  - border-[side]-width
  """)
  public void propertyBorderSideWidth() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            hr,

            borderTopWidth($0),
            borderRightWidth(px(20)),
            borderBottomWidth(medium),
            borderLeftWidth(inherit)
          );
        }
      },

      """
      hr {
        border-top-width: 0;
        border-right-width: 20px;
        border-bottom-width: medium;
        border-left-width: inherit;
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
  [#397] Preflight 04

  - color
  """)
  public void propertyColor() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            hr,

            color(red),
            color(inherit)
          );
        }
      },

      """
      hr {
        color: red;
        color: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - font-family
  """)
  public void propertyFontFamily() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            fontFamily(l("Gill Sans Extrabold"), sansSerif),
            fontFamily(serif),
            fontFamily(l("-apple-system")),
            fontFamily(l("Arial"), l("Roboto"), l("Noto Sans")),
            fontFamily(l("Red/Black")),
            fontFamily(inherit)
          );
        }
      },

      """
      html {
        font-family: "Gill Sans Extrabold", sans-serif;
        font-family: serif;
        font-family: -apple-system;
        font-family: Arial, Roboto, "Noto Sans";
        font-family: "Red/Black";
        font-family: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - font-feature-settings
  """)
  public void propertyFontFeatureSettings() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            fontFeatureSettings(normal),
            fontFeatureSettings(unset)
          );
        }
      },

      """
      html {
        font-feature-settings: normal;
        font-feature-settings: unset;
      }
      """
    );
  }

  @Test(description = """
  [#398] Preflight 06

  - font-size
  """)
  public void propertyFontSize() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            h1, h2, h3, h4, h5, h6,

            fontSize(xxSmall),
            fontSize(xSmall),
            fontSize(small),
            fontSize(medium),
            fontSize(large),
            fontSize(xLarge),
            fontSize(xxLarge),
            fontSize(xxxLarge),

            fontSize(smaller),
            fontSize(larger),

            fontSize(px(12)),
            fontSize(em(0.8)),
            fontSize(pct(80)),

            fontSize(inherit)
          );
        }
      },

      """
      h1, h2, h3, h4, h5, h6 {
        font-size: xx-small;
        font-size: x-small;
        font-size: small;
        font-size: medium;
        font-size: large;
        font-size: x-large;
        font-size: xx-large;
        font-size: xxx-large;
        font-size: smaller;
        font-size: larger;
        font-size: 12px;
        font-size: 0.8em;
        font-size: 80%;
        font-size: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#398] Preflight 06

  - font-weight
  """)
  public void propertyFontWeight() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            h1, h2, h3, h4, h5, h6,

            fontWeight(normal),
            fontWeight(bold),
            fontWeight(lighter),
            fontWeight(bolder),

            fontWeight(100),
            fontWeight(400),

            fontWeight(inherit)
          );
        }
      },

      """
      h1, h2, h3, h4, h5, h6 {
        font-weight: normal;
        font-weight: bold;
        font-weight: lighter;
        font-weight: bolder;
        font-weight: 100;
        font-weight: 400;
        font-weight: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#395] Preflight 02

  - font-variation-settings
  """)
  public void propertyFontVariationSettings() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            html,

            fontVariationSettings(normal),
            fontVariationSettings(inherit)
          );
        }
      },

      """
      html {
        font-variation-settings: normal;
        font-variation-settings: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#397] Preflight 04

  - height
  """)
  public void propertyHeight() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            hr,

            height($0),
            height(px(120)),
            height(pct(75)),
            height(maxContent),
            height(minContent),
            height(fitContent),
            height(auto),
            height(inherit)
          );
        }
      },

      """
      hr {
        height: 0;
        height: 120px;
        height: 75%;
        height: max-content;
        height: min-content;
        height: fit-content;
        height: auto;
        height: inherit;
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
  [#396] Preflight 03

  - margin
  """)
  public void propertyMargin() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            body,

            margin($0),
            margin(em(1)),
            margin(px(-3)),
            margin(pct(5), auto),
            margin(em(1), auto, em(2)),
            margin(px(2), em(1), $0, auto),
            margin(inherit)
          );
        }
      },

      """
      body {
        margin: 0;
        margin: 1em;
        margin: -3px;
        margin: 5% auto;
        margin: 1em auto 2em;
        margin: 2px 1em 0 auto;
        margin: inherit;
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
  [#399] Preflight 06

  - text-decoration-color
  """)
  public void propertyTextDecorationColor() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            a,

            textDecorationColor(blue),
            textDecorationColor(inherit)
          );
        }
      },

      """
      a {
        text-decoration-color: blue;
        text-decoration-color: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#399] Preflight 06

  - text-decoration-line
  """)
  public void propertyTextDecorationLine() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            a,

            textDecorationLine(none),
            textDecorationLine(underline),
            textDecorationLine(overline),
            textDecorationLine(lineThrough),
            textDecorationLine(blink),
            textDecorationLine(underline, overline),
            textDecorationLine(overline, underline, lineThrough),
            textDecorationLine(inherit)
          );
        }
      },

      """
      a {
        text-decoration-line: none;
        text-decoration-line: underline;
        text-decoration-line: overline;
        text-decoration-line: line-through;
        text-decoration-line: blink;
        text-decoration-line: underline overline;
        text-decoration-line: overline underline line-through;
        text-decoration-line: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#399] Preflight 06

  - text-decoration-style
  """)
  public void propertyTextDecorationStyle() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            a,

            textDecorationStyle(solid),
            textDecorationStyle(double$),
            textDecorationStyle(dotted),
            textDecorationStyle(dashed),
            textDecorationStyle(wavy),
            textDecorationStyle(inherit)
          );
        }
      },

      """
      a {
        text-decoration-style: solid;
        text-decoration-style: double;
        text-decoration-style: dotted;
        text-decoration-style: dashed;
        text-decoration-style: wavy;
        text-decoration-style: inherit;
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