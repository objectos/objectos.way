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

    // length

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

    // L

    pdbl("line-height");
    pint("line-height");
  }

}