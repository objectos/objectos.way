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
      "b", "body",
      "code",
      "h1", "h2", "h3", "h4", "h5", "h6", "hr", "html",
      "kbd",
      "li",
      "pre",
      "samp", "small", "strong", "sub", "sup",
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

    // B

    pval("border-color", globalKeyword);
    pbox("border-color", color);

    var lineWidth = def("LineWidth",
      length,
      keywords("medium", "thick", "thin")
    );

    pval("border-width", globalKeyword);
    pbox("border-width", lineWidth);

    pval("box-sizing", globalKeyword);
    pval("box-sizing", def("BoxSizingValue",
      keywords("border-box", "content-box")
    ));

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

    pval("border-style", globalKeyword);
    pbox("border-style", lineStyle);

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

    pval("font-family", globalKeyword);
    pvar("font-family", fontFamilyValue);

    pval("font-feature-settings", globalKeyword);
    pval("font-feature-settings", def("FontFeatureSettingsValue", kw("normal")));

    pval("font-variation-settings", globalKeyword);
    pval("font-variation-settings", def("FontVariationSettingsValue", kw("normal")));

    // L

    pdbl("line-height");
    pint("line-height");
    pval("line-height", globalKeyword);
    pval("line-height", def("LineHeightValue",
      lengthPercentage,
      kw("normal")
    ));

    // T

    pint("-moz-tab-size");
    pval("-moz-tab-size", globalKeyword);
    pval("-moz-tab-size", length);

    pint("tab-size");
    pval("tab-size", globalKeyword);
    pval("tab-size", length);

    var textSizeAdjustValue = def("TextSizeAdjustValue",
      keywords("auto", "none"),
      percentage
    );

    pval("-webkit-text-size-adjust", globalKeyword);
    pval("-webkit-text-size-adjust", textSizeAdjustValue);
  }

}