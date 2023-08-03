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

public class StandardSelectorStepTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        selectors(
          SelectorKind.TYPE,

          "a",
          "pre"
        );

        selectors(
          SelectorKind.PSEUDO_CLASS,

          ":checked",
          ":-moz-focusring"
        );

        selectors(
          SelectorKind.PSEUDO_ELEMENT,

          "::after",
          "::-moz-focus-inner"
        );
      }
    };

    result = generate(spec);
  }

  @Test
  public void type() {
    assertEquals(
      result.get("objectos/css/internal/StandardTypeSelector.java"),

      """
      package objectos.css.internal;

      import objectos.css.tmpl.Api.SelectorInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum StandardTypeSelector implements SelectorInstruction {
        a("a"),

        pre("pre");

        private static final StandardTypeSelector[] VALUES = values();

        public final String cssName;

        private StandardTypeSelector(String cssName) {
          this.cssName = cssName;
        }

        public static StandardTypeSelector ofOrdinal(int ordinal) {
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

  @Test
  public void pseudoClass() {
    assertEquals(
      result.get("objectos/css/internal/StandardPseudoClassSelector.java"),

      """
      package objectos.css.internal;

      import objectos.css.tmpl.Api.SelectorInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum StandardPseudoClassSelector implements SelectorInstruction {
        _checked(":checked"),

        _mozFocusring(":-moz-focusring");

        private static final StandardPseudoClassSelector[] VALUES = values();

        public final String cssName;

        private StandardPseudoClassSelector(String cssName) {
          this.cssName = cssName;
        }

        public static StandardPseudoClassSelector ofOrdinal(int ordinal) {
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

  @Test
  public void pseudoElement() {
    assertEquals(
      result.get("objectos/css/internal/StandardPseudoElementSelector.java"),

      """
      package objectos.css.internal;

      import objectos.css.tmpl.Api.SelectorInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum StandardPseudoElementSelector implements SelectorInstruction {
        __after("::after"),

        __mozFocusInner("::-moz-focus-inner");

        private static final StandardPseudoElementSelector[] VALUES = values();

        public final String cssName;

        private StandardPseudoElementSelector(String cssName) {
          this.cssName = cssName;
        }

        public static StandardPseudoElementSelector ofOrdinal(int ordinal) {
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