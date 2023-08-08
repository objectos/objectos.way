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
        var textIndentValue = t("TextIndentValue",
          keywords("each-line", "hanging")
        );

        property(
          "text-indent",

          sig(percentage(), "value"),
          sig(percentage(), "value1", textIndentValue, "value2"),
          sig(percentage(), "value1", textIndentValue, "value2", textIndentValue, "value3")
        );
      }
    };

    result = generate(spec);
  }

  @Test
  public void generatedCssTemplate() {
    assertEquals(
      result.get("objectos/css/internal/GeneratedCssTemplate.java"),

      """
      package objectos.css.internal;

      import objectos.css.tmpl.Api.PercentageValue;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectos.css.tmpl.Api.TextIndentValue;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final TextIndentValue eachLine = StandardName.eachLine;

        protected static final TextIndentValue hanging = StandardName.hanging;

        protected final StyleDeclaration textIndent(PercentageValue value) {
          Check.notNull(value, "value == null");
          declaration(Property.TEXT_INDENT, value);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration textIndent(PercentageValue value1, TextIndentValue value2) {
          Check.notNull(value1, "value1 == null");
          Check.notNull(value2, "value2 == null");
          declaration(Property.TEXT_INDENT, value1, value2);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration textIndent(PercentageValue value1, TextIndentValue value2, TextIndentValue value3) {
          Check.notNull(value1, "value1 == null");
          Check.notNull(value2, "value2 == null");
          Check.notNull(value3, "value3 == null");
          declaration(Property.TEXT_INDENT, value1, value2, value3);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);
      }
      """
    );
  }

}