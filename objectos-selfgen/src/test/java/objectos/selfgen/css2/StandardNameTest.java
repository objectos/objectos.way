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
package objectos.selfgen.css2;

import static objectos.selfgen.css2.Util.generate;
import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class StandardNameTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        selectors(
          // type selectors
          "a",
          "pre",

          // pseudo elements
          "::after", "::before"
        );

        // global keywords
        var globalKeyword = t("GlobalKeyword",
          k("inherit"), k("initial"), k("unset")
        );

        // color
        var color = color(
          "currentcolor",
          "transparent"
        );

        // B
        property(
          "border-color",

          sig(globalKeyword, "value"),
          sig(color, "value")
        );
      }
    };

    result = generate(spec);
  }

  @Test
  public void all() {
    assertEquals(
      result.get("objectos/css/internal/StandardName.java"),

      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.css.om.Selector;
      import objectos.css.tmpl.ColorValue;
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum StandardName implements Selector, ColorValue,
          PropertyName,
          GlobalKeyword {
        __after("::after"),

        __before("::before"),

        a("a"),

        pre("pre"),

        any("*"),

        currentcolor("currentcolor"),

        transparent("transparent"),

        inherit("inherit"),

        initial("initial"),

        unset("unset"),

        BORDER_COLOR("border-color");

        private static final StandardName[] VALUES = values();

        public final String cssName;

        private StandardName(String cssName) {
          this.cssName = cssName;
        }

        public static StandardName byOrdinal(int ordinal) {
          return VALUES[ordinal];
        }

        @Override
        public final String toString() {
          return cssName;
        }
      }
      """
    );
  }

}