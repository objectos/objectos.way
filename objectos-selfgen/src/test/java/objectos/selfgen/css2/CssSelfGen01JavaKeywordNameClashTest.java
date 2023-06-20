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

public class CssSelfGen01JavaKeywordNameClashTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        keywordFieldName("double", "_double");

        var lineStyle = t("LineStyle",
          k("none"), k("hidden"), k("dotted"), k("dashed"),
          k("solid "), k("double"), k("groove"), k("ridge"),
          k("inset"), k("outset")
        );

        pval("border-style", lineStyle);
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
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.LineStyle;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final LineStyle _double = named("double");

        protected static final LineStyle dashed = named("dashed");

        protected static final LineStyle dotted = named("dotted");

        protected static final LineStyle groove = named("groove");

        protected static final LineStyle hidden = named("hidden");

        protected static final LineStyle inset = named("inset");

        protected static final LineStyle none = named("none");

        protected static final LineStyle outset = named("outset");

        protected static final LineStyle ridge = named("ridge");

        protected static final LineStyle solid = named("solid ");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final StyleDeclaration borderStyle(LineStyle value) {
          return new StyleDeclaration1(Property.BORDER_STYLE, value.self());
        }
      }
      """
    );
  }

}