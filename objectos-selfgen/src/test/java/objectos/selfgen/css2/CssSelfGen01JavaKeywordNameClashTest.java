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

        var lineStyle = t(
          "LineStyle",

          k("none"),
          k("hidden"),
          k("dotted"),
          k("dashed"),
          k("solid "),
          k("double"),
          k("groove"),
          k("ridge"),
          k("inset"),
          k("outset")
        );

        property(
          "border-style",

          sig(lineStyle, "value")
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

      import objectos.css.om.PropertyValue;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.LineStyle;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final LineStyle _double = StandardName._double;

        protected static final LineStyle dashed = StandardName.dashed;

        protected static final LineStyle dotted = StandardName.dotted;

        protected static final LineStyle groove = StandardName.groove;

        protected static final LineStyle hidden = StandardName.hidden;

        protected static final LineStyle inset = StandardName.inset;

        protected static final LineStyle none = StandardName.none;

        protected static final LineStyle outset = StandardName.outset;

        protected static final LineStyle ridge = StandardName.ridge;

        protected static final LineStyle solid = StandardName.solid;

        protected final StyleDeclaration borderStyle(LineStyle value) {
          Check.notNull(value, "value == null");
          return declaration(Property.BORDER_STYLE, value);
        }

        abstract StyleDeclaration declaration(Property name, PropertyValue value);

        abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2);

        abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

        abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);

        abstract StyleDeclaration declaration(Property name, int value);

        abstract StyleDeclaration declaration(Property name, double value);

        abstract StyleDeclaration declaration(Property name, String value);
      }
      """
    );
  }

}