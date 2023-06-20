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

public class CssSelfGen11Value2AndValue3SigsTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var textDecorationLineMultiValue = def("TextDecorationLineMultiValue",
          keywords("blink", "line-through", "overline", "underline")
        );

        var textDecorationLineSingleValue = def("TextDecorationLineSingleValue",
          textDecorationLineMultiValue, kw("none")
        );

        pval("text-decoration-line", textDecorationLineSingleValue);
        pva2("text-decoration-line", textDecorationLineMultiValue);
        pva3("text-decoration-line", textDecorationLineMultiValue);
      }
    };

    result = generate(spec);
  }

  @Test
  public void generatedCssTemplate() {
    assertEquals(
      result.get("objectos/css/GeneratedCssTemplate.java"),

      """
      package objectos.css;

      import objectos.css.internal.NamedElement;
      import objectos.css.internal.Property;
      import objectos.css.internal.StyleDeclaration1;
      import objectos.css.internal.StyleDeclaration2;
      import objectos.css.internal.StyleDeclaration3;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.TextDecorationLineMultiValue;
      import objectos.css.tmpl.TextDecorationLineSingleValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final TextDecorationLineMultiValue blink = named("blink");

        protected static final TextDecorationLineMultiValue lineThrough = named("line-through");

        protected static final TextDecorationLineSingleValue none = named("none");

        protected static final TextDecorationLineMultiValue overline = named("overline");

        protected static final TextDecorationLineMultiValue underline = named("underline");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final StyleDeclaration textDecorationLine(TextDecorationLineSingleValue value) {
          return new StyleDeclaration1(Property.TEXT_DECORATION_LINE, value.self());
        }

        protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2) {
          return new StyleDeclaration2(Property.TEXT_DECORATION_LINE, value1.self(), value2.self());
        }

        protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2, TextDecorationLineMultiValue value3) {
          return new StyleDeclaration3(Property.TEXT_DECORATION_LINE, value1.self(), value2.self(), value3.self());
        }
      }
      """
    );
  }

}