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
  [#419] Preflight 13

  - appearance
  """)
  public void propertyAppearance() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            button,
            attr("type", IS, "button"),
            attr("type", IS, "reset"),
            attr("type", IS, "submit"),

            appearance(none),
            appearance(auto),
            appearance(menulistButton),
            appearance(textfield),

            appearance(searchfield),
            appearance(textarea),
            appearance(pushButton),
            appearance(sliderHorizontal),
            appearance(checkbox),
            appearance(radio),
            appearance(squareButton),
            appearance(menulist),
            appearance(listbox),
            appearance(meter),
            appearance(progressBar),

            webkitAppearance(button),
            appearance(inherit)
          );
        }
      },

      """
      button, [type="button"], [type="reset"], [type="submit"] {
        appearance: none;
        appearance: auto;
        appearance: menulist-button;
        appearance: textfield;
        appearance: searchfield;
        appearance: textarea;
        appearance: push-button;
        appearance: slider-horizontal;
        appearance: checkbox;
        appearance: radio;
        appearance: square-button;
        appearance: menulist;
        appearance: listbox;
        appearance: meter;
        appearance: progress-bar;
        -webkit-appearance: button;
        appearance: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#419] Preflight 13

  - background-image
  """)
  public void propertyBackgroundImage() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            button,
            attr("type", IS, "button"),
            attr("type", IS, "reset"),
            attr("type", IS, "submit"),

            backgroundImage(none),
            backgroundImage(inherit)
          );
        }
      },

      """
      button, [type="button"], [type="reset"], [type="submit"] {
        background-image: none;
        background-image: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#417] Preflight 11

  - border-collapse
  """)
  public void propertyBorderCollapse() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            table,

            borderCollapse(collapse),
            borderCollapse(separate),
            borderCollapse(inherit)
          );
        }
      },

      """
      table {
        border-collapse: collapse;
        border-collapse: separate;
        border-collapse: inherit;
      }
      """
    );
  }

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
            borderColor(transparent, navy, gray, maroon),
            borderColor(inherit)
          );
        }
      },

      """
      * {
        border-color: currentcolor;
        border-color: black white;
        border-color: blue red green;
        border-color: teal yellow aqua fuchsia;
        border-color: transparent navy gray maroon;
        border-color: inherit;
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
            borderTopWidth(unset),
            borderRightWidth(px(20)),
            borderRightWidth(initial),
            borderBottomWidth(medium),
            borderBottomWidth(inherit),
            borderLeftWidth(em(1)),
            borderLeftWidth(inherit)
          );
        }
      },

      """
      hr {
        border-top-width: 0;
        border-top-width: unset;
        border-right-width: 20px;
        border-right-width: initial;
        border-bottom-width: medium;
        border-bottom-width: inherit;
        border-left-width: 1em;
        border-left-width: inherit;
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
            borderStyle(dashed, solid, _double),
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
            borderWidth(medium, thick, thin, $0),
            borderWidth(unset)
          );
        }
      },

      """
      * {
        border-width: 0;
        border-width: medium thick;
        border-width: medium thick thin;
        border-width: medium thick thin 0;
        border-width: unset;
      }
      """
    );
  }

  @Test(description = """
  [#404] Preflight 10

  - bottom
  """)
  public void propertyBottom() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            sub, sup,

            bottom(auto),
            bottom(px(3)),
            bottom(em(2.4)),
            bottom(pct(10)),
            bottom(inherit)
          );
        }
      },

      """
      sub, sup {
        bottom: auto;
        bottom: 3px;
        bottom: 2.4em;
        bottom: 10%;
        bottom: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#420] Preflight 14

  - box-shadow
  """)
  public void propertyBoxShadow() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            _mozUiInvalid,

            boxShadow(none),
            boxShadow(inherit)
          );
        }
      },

      """
      :-moz-ui-invalid {
        box-shadow: none;
        box-shadow: inherit;
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
  [#420] Preflight 14

  - outline
  """)
  public void propertyOutline() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            _mozFocusring,

            outline(auto),
            outline(solid),
            outline(red, dashed),
            outline(inset, thick),
            outline(green, solid, px(3)),
            outline(inherit)
          );
        }
      },

      """
      :-moz-focusring {
        outline: auto;
        outline: solid;
        outline: red dashed;
        outline: inset thick;
        outline: green solid 3px;
        outline: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#420] Preflight 14

  - outline-color
  """)
  public void propertyOutlineColor() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            _mozFocusring,

            outlineColor(blue),
            outlineColor(inherit)
          );
        }
      },

      """
      :-moz-focusring {
        outline-color: blue;
        outline-color: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#420] Preflight 14

  - outline-style
  """)
  public void propertyOutlineStyle() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            _mozFocusring,

            outlineStyle(auto),
            outlineStyle(none),
            outlineStyle(dotted),
            outlineStyle(dashed),
            outlineStyle(solid),
            outlineStyle(_double),
            outlineStyle(groove),
            outlineStyle(ridge),
            outlineStyle(inset),
            outlineStyle(outset),
            outlineStyle(inherit)
          );
        }
      },

      """
      :-moz-focusring {
        outline-style: auto;
        outline-style: none;
        outline-style: dotted;
        outline-style: dashed;
        outline-style: solid;
        outline-style: double;
        outline-style: groove;
        outline-style: ridge;
        outline-style: inset;
        outline-style: outset;
        outline-style: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#420] Preflight 14

  - outline-width
  """)
  public void propertyOutlineWidth() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            _mozFocusring,

            outlineWidth(thin),
            outlineWidth(medium),
            outlineWidth(thick),
            outlineWidth(px(1)),
            outlineWidth(em(0.1)),
            outlineWidth(inherit)
          );
        }
      },

      """
      :-moz-focusring {
        outline-width: thin;
        outline-width: medium;
        outline-width: thick;
        outline-width: 1px;
        outline-width: 0.1em;
        outline-width: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#418] Preflight 12

  - padding
  """)
  public void propertyPadding() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            button,
            input,
            optgroup,
            select,
            textarea,

            padding($0),
            padding(em(1)),
            padding(pct(5), pct(10)),
            padding(em(1), em(2), em(2)),
            padding(px(5), em(1), $0, em(2)),
            padding(inherit)
          );
        }
      },

      """
      button, input, optgroup, select, textarea {
        padding: 0;
        padding: 1em;
        padding: 5% 10%;
        padding: 1em 2em 2em;
        padding: 5px 1em 0 2em;
        padding: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#404] Preflight 10

  - position
  """)
  public void propertyPosition() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            sub, sup,

            position(_static),
            position(relative),
            position(absolute),
            position(fixed),
            position(sticky),
            position(inherit)
          );
        }
      },

      """
      sub, sup {
        position: static;
        position: relative;
        position: absolute;
        position: fixed;
        position: sticky;
        position: inherit;
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

  - text-decoration
  """)
  public void propertyTextDecoration() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            a,

            textDecoration(blue),
            textDecoration(underline),
            textDecoration(underline, dotted),
            textDecoration(underline, dotted, red),
            textDecoration(underline, overline, green),
            textDecoration(none),
            textDecoration(inherit)
          );
        }
      },

      """
      a {
        text-decoration: blue;
        text-decoration: underline;
        text-decoration: underline dotted;
        text-decoration: underline dotted red;
        text-decoration: underline overline green;
        text-decoration: none;
        text-decoration: inherit;
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
            textDecorationStyle(_double),
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
  [#399] Preflight 06

  - text-decoration-thickness
  """)
  public void propertyTextDecorationThickness() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            a,

            textDecorationThickness(auto),
            textDecorationThickness(fromFont),
            textDecorationThickness(em(0.1)),
            textDecorationThickness(px(3)),
            textDecorationThickness(pct(10)),
            textDecorationThickness(inherit)
          );
        }
      },

      """
      a {
        text-decoration-thickness: auto;
        text-decoration-thickness: from-font;
        text-decoration-thickness: 0.1em;
        text-decoration-thickness: 3px;
        text-decoration-thickness: 10%;
        text-decoration-thickness: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#417] Preflight 11

  - text-indent
  """)
  public void propertyTextIndent() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            table,

            textIndent($0),
            textIndent(mm(3)),
            textIndent(px(40)),
            textIndent(pct(15)),

            textIndent(em(5), eachLine),
            textIndent(em(5), hanging),
            textIndent(em(5), hanging, eachLine),

            textIndent(inherit)
          );
        }
      },

      """
      table {
        text-indent: 0;
        text-indent: 3mm;
        text-indent: 40px;
        text-indent: 15%;
        text-indent: 5em each-line;
        text-indent: 5em hanging;
        text-indent: 5em hanging each-line;
        text-indent: inherit;
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
            webkitTextSizeAdjust($0),
            webkitTextSizeAdjust(auto),
            webkitTextSizeAdjust(none),
            webkitTextSizeAdjust(inherit)
          );
        }
      },

      """
      html {
        -webkit-text-size-adjust: 100%;
        -webkit-text-size-adjust: 0;
        -webkit-text-size-adjust: auto;
        -webkit-text-size-adjust: none;
        -webkit-text-size-adjust: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#418] Preflight 12

  - text-transform
  """)
  public void propertyTextTransform() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            button,
            select,

            textTransform(none),
            textTransform(capitalize),
            textTransform(uppercase),
            textTransform(lowercase),
            textTransform(fullWidth),
            textTransform(fullSizeKana),
            textTransform(inherit)
          );
        }
      },

      """
      button, select {
        text-transform: none;
        text-transform: capitalize;
        text-transform: uppercase;
        text-transform: lowercase;
        text-transform: full-width;
        text-transform: full-size-kana;
        text-transform: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#404] Preflight 10

  - top
  """)
  public void propertyTop() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            sub, sup,

            top(auto),
            top(px(3)),
            top(em(2.4)),
            top(pct(10)),
            top(inherit)
          );
        }
      },

      """
      sub, sup {
        top: auto;
        top: 3px;
        top: 2.4em;
        top: 10%;
        top: inherit;
      }
      """
    );
  }

  @Test(description = """
  [#404] Preflight 10

  - vertical-align
  """)
  public void propertyVerticalAlign() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            sub, sup,

            verticalAlign(baseline),
            verticalAlign(sub),
            verticalAlign(_super),
            verticalAlign(textTop),
            verticalAlign(textBottom),
            verticalAlign(middle),
            verticalAlign(top),
            verticalAlign(bottom),
            verticalAlign(em(10)),
            verticalAlign(px(4)),
            verticalAlign(pct(20)),
            verticalAlign(inherit)
          );
        }
      },

      """
      sub, sup {
        vertical-align: baseline;
        vertical-align: sub;
        vertical-align: super;
        vertical-align: text-top;
        vertical-align: text-bottom;
        vertical-align: middle;
        vertical-align: top;
        vertical-align: bottom;
        vertical-align: 10em;
        vertical-align: 4px;
        vertical-align: 20%;
        vertical-align: inherit;
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
  [#401] Preflight 07
  [#402] Preflight 08
  [#403] Preflight 09

  - type selectors
  """)
  public void selectorTypes() {
    test(
      new CssTemplate() {
        @Override
        protected void definition() {
          style(
            b, strong
          );

          style(
            code, kbd, samp, pre
          );

          style(
            small
          );
        }
      },

      """
      b, strong {}
      code, kbd, samp, pre {}
      small {}
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