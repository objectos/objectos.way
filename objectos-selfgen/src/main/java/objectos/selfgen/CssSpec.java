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
      kw("inherit"), kw("initial"), kw("unset")
    );

    // color
    var color = def("Color",
      kw("currentcolor"), kw("transparent"),

      kw("black"), kw("silver"), kw("gray"), kw("white"), kw("maroon"), kw("red"), kw("purple"),
      kw("fuchsia"), kw("green"), kw("lime"), kw("olive"), kw("yellow"), kw("navy"), kw("blue"),
      kw("teal"), kw("aqua")
    );

    // B
    pval("border-color", globalKeyword);
    pbox("border-color", color);

    pval("box-sizing", globalKeyword);
    pval("box-sizing", def("BoxSizingValue",
      kw("border-box"), kw("content-box")
    ));
  }

}