/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen;

import java.io.IOException;
import objectos.selfgen.css2.CssSelfGen;

public final class CssSpec extends CssSelfGen {

  public static void main(String[] args) throws IOException {
    var spec = new CssSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    selectors(
      // type selectors
      "a",
      "b", "body", "button",
      "code",
      "h1", "h2", "h3", "h4", "h5", "h6", "hr", "html",
      "input",
      "kbd",
      "li",
      "optgroup",
      "pre",
      "samp", "select", "small", "strong", "sub", "sup",
      "table", "textarea",
      "ul",

      // pseudo elements
      "::after", "::before"
    );

    // global keywords

    var globalKeyword = def("GlobalKeyword",
      keywords("inherit", "initial", "unset")
    );

    // color
    var color = def("Color",
      keywords("currentcolor", "transparent"),

      keywords(
        "aqua",
        "black", "blue",
        "fuchsia",
        "gray", "green",
        "maroon",
        "lime",
        "navy",
        "olive",
        "purple",
        "red",
        "silver",
        "teal",
        "white",
        "yellow"
      )
    );

    // length/percentage

    var length = length(
      "ch", "cm",
      "em", "ex",
      "in",
      "mm",
      "pc", "pt", "px",
      "q",
      "rem",
      "vh", "vmax", "vmin", "vw"
    );

    var percentage = percentage();

    var lengthPercentage = def("LengthPercentage", length, percentage);

    // keyword name clashes

    keywordFieldName("double", "_double");
    keywordFieldName("static", "_static");
    keywordFieldName("super", "_super");

    // B

    var borderCollapseVale = def("BorderCollapseValue",
      keywords("collapse", "separate")
    );

    var bottomValue = def("BottomValue",
      kw("auto"),
      lengthPercentage
    );

    var lineWidth = def("LineWidth",
      length,
      keywords("medium", "thick", "thin")
    );

    var lineStyle = def("LineStyle",
      keywords(
        "dashed", "dotted", "double",
        "groove", "hidden",
        "inset",
        "none",
        "outset",
        "ridge", "solid"
      )
    );

    pval("border-bottom-width", globalKeyword);
    pval("border-bottom-width", lineWidth);

    pval("border-collapse", globalKeyword);
    pval("border-collapse", borderCollapseVale);

    pval("border-color", globalKeyword);
    pbox("border-color", color);

    pval("border-left-width", globalKeyword);
    pval("border-left-width", lineWidth);

    pval("border-right-width", globalKeyword);
    pval("border-right-width", lineWidth);

    pval("border-top-width", globalKeyword);
    pval("border-top-width", lineWidth);

    pval("border-width", globalKeyword);
    pbox("border-width", lineWidth);

    pval("bottom", globalKeyword);
    pval("bottom", bottomValue);

    pval("box-sizing", globalKeyword);
    pval("box-sizing", def("BoxSizingValue",
      keywords("border-box", "content-box")
    ));

    pval("border-style", globalKeyword);
    pbox("border-style", lineStyle);

    // C

    pval("color", globalKeyword);
    pval("color", color);

    // F

    var fontFamilyValue = def("FontFamilyValue",
      keywords(
        "cursive",
        "emoji",
        "fangsong", "fantasy",
        "math", "monospace",
        "sans-serif", "serif", "system-ui",
        "ui-monospace",
        "ui-rounded",
        "ui-sans-serif",
        "ui-serif"
      ),
      string()
    );

    var fontSizeValue = def("FontSizeValue",
      keywords(
        "large", "larger",
        "medium",
        "small", "smaller",
        "x-large", "xx-large", "xxx-large", "xx-small", "x-small"
      ),
      lengthPercentage
    );

    var fontWeightValue = def("FontWeightValue",
      keywords(
        "bold", "bolder",
        "lighter",
        "normal"
      )
    );

    pval("font-family", globalKeyword);
    pvar("font-family", fontFamilyValue);

    pval("font-feature-settings", globalKeyword);
    pval("font-feature-settings", def("FontFeatureSettingsValue", kw("normal")));

    pval("font-size", globalKeyword);
    pval("font-size", fontSizeValue);

    pval("font-variation-settings", globalKeyword);
    pval("font-variation-settings", def("FontVariationSettingsValue", kw("normal")));

    pint("font-weight");
    pval("font-weight", globalKeyword);
    pval("font-weight", fontWeightValue);

    // H

    var heightValue = def("HeightValue",
      lengthPercentage,
      keywords("auto", "fit-content", "max-content", "min-content")
    );

    pval("height", globalKeyword);
    pval("height", heightValue);

    // L

    var lineHeightValue = def("LineHeightValue",
      lengthPercentage,
      kw("normal")
    );

    pdbl("line-height");
    pint("line-height");
    pval("line-height", globalKeyword);
    pval("line-height", lineHeightValue);

    // M

    var marginValue = def("MarginValue",
      lengthPercentage,
      kw("auto")
    );

    pval("margin", globalKeyword);
    pbox("margin", marginValue);

    // P

    var positionValue = def("PositionValue",
      keywords("absolute", "fixed", "static", "sticky", "relative")
    );

    pval("padding", globalKeyword);
    pbox("padding", lengthPercentage);

    pval("padding-bottom", globalKeyword);
    pval("padding-bottom", lengthPercentage);

    pval("padding-left", globalKeyword);
    pval("padding-left", lengthPercentage);

    pval("padding-right", globalKeyword);
    pval("padding-right", lengthPercentage);

    pval("padding-top", globalKeyword);
    pval("padding-top", lengthPercentage);

    pval("position", globalKeyword);
    pval("position", positionValue);

    // T

    var textSizeAdjustValue = def("TextSizeAdjustValue",
      keywords("auto", "none"),
      percentage
    );

    var textDecorationLineMultiValue = def("TextDecorationLineMultiValue",
      keywords("blink", "line-through", "overline", "underline")
    );

    var textDecorationLineSingleValue = def("TextDecorationLineSingleValue",
      textDecorationLineMultiValue, kw("none")
    );

    var textDecorationStyleValue = def("TextDecorationStyleValue",
      keywords("dashed", "double", "dotted", "solid", "wavy")
    );

    var textDecorationThicknessValue = def("TextDecorationThicknessValue",
      keywords("auto", "from-font"),
      lengthPercentage
    );

    var textDecorationValue = def("TextDecorationValue",
      color, textDecorationLineSingleValue, textDecorationStyleValue, textDecorationThicknessValue
    );

    var textIndentValue = def("TextIndentValue",
      keywords("each-line", "hanging")
    );

    var textTransformValue = def("TextTransformValue",
      keywords("capitalize", "full-width", "full-size-kana", "lowercase", "none", "uppercase")
    );

    var topValue = def("TopValue",
      kw("auto"),
      lengthPercentage
    );

    pint("-moz-tab-size");
    pval("-moz-tab-size", globalKeyword);
    pval("-moz-tab-size", length);

    pint("tab-size");
    pval("tab-size", globalKeyword);
    pval("tab-size", length);

    pval("text-decoration", globalKeyword);
    pval("text-decoration", textDecorationValue);
    pva2("text-decoration", textDecorationValue);
    pva3("text-decoration", textDecorationValue);
    pva4("text-decoration", textDecorationValue);

    pval("text-decoration-color", globalKeyword);
    pval("text-decoration-color", color);

    pval("text-decoration-line", globalKeyword);
    pval("text-decoration-line", textDecorationLineSingleValue);
    pva2("text-decoration-line", textDecorationLineMultiValue);
    pva3("text-decoration-line", textDecorationLineMultiValue);

    pval("text-decoration-style", globalKeyword);
    pval("text-decoration-style", textDecorationStyleValue);

    pval("text-decoration-thickness", globalKeyword);
    pval("text-decoration-thickness", textDecorationThicknessValue);

    pval("text-indent", globalKeyword);
    pval("text-indent", lengthPercentage);
    pval("text-indent", lengthPercentage, textIndentValue);
    pval("text-indent", lengthPercentage, textIndentValue, textIndentValue);

    pval("-webkit-text-size-adjust", globalKeyword);
    pval("-webkit-text-size-adjust", textSizeAdjustValue);

    pval("text-transform", globalKeyword);
    pval("text-transform", textTransformValue);

    pval("top", globalKeyword);
    pval("top", topValue);

    // V

    var verticalAlignValue = def("VerticalAlignValue",
      keywords(
        "baseline", "bottom",
        "middle",
        "sub", "super",
        "text-bottom", "text-top", "top"
      ),
      lengthPercentage
    );

    pval("vertical-align", globalKeyword);
    pval("vertical-align", verticalAlignValue);
  }

}