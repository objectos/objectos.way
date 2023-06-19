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

public class CssSelfGen12CustomSignatureTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var textIndentValue = def("TextIndentValue",
          keywords("each-line", "hanging")
        );

        pval("text-indent", percentage());
        pval("text-indent", percentage(), textIndentValue);
        pval("text-indent", percentage(), textIndentValue, textIndentValue);
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

      import objectos.css.internal.InternalPercentage;
      import objectos.css.internal.NamedElement;
      import objectos.css.internal.Property;
      import objectos.css.internal.StyleDeclaration1;
      import objectos.css.internal.StyleDeclaration2;
      import objectos.css.internal.StyleDeclaration3;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.Percentage;
      import objectos.css.tmpl.TextIndentValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final TextIndentValue eachLine = named("each-line");

        protected static final TextIndentValue hanging = named("hanging");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final Percentage pct(double value) {
          return InternalPercentage.of(value);
        }

        protected final Percentage pct(int value) {
          return InternalPercentage.of(value);
        }

        protected final StyleDeclaration textIndent(Percentage value) {
          return new StyleDeclaration1(Property.TEXT_INDENT, value.self());
        }

        protected final StyleDeclaration textIndent(Percentage value1, TextIndentValue value2) {
          return new StyleDeclaration2(Property.TEXT_INDENT, value1.self(), value2.self());
        }

        protected final StyleDeclaration textIndent(Percentage value1, TextIndentValue value2, TextIndentValue value3) {
          return new StyleDeclaration3(Property.TEXT_INDENT, value1.self(), value2.self(), value3.self());
        }
      }
      """
    );
  }

}