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

public class CssSelfGen09StringLiteralTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var fontFamilyValue = t(
          "FontFamilyValue",

          keywords(
            "sans-serif",
            "monospace"
          ),
          string()
        );

        pvar("font-family", fontFamilyValue);
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
      import objectos.css.tmpl.FontFamilyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final FontFamilyValue monospace = StandardName.monospace;

        protected static final FontFamilyValue sansSerif = StandardName.sansSerif;

        protected abstract StyleDeclaration fontFamily(FontFamilyValue... values);

        abstract void declaration(Property name, PropertyValue value);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);

        abstract void declaration(Property name, int value);

        abstract void declaration(Property name, double value);

        abstract void declaration(Property name, String value);
      }
      """
    );
  }

  @Test
  public void fontFamilyValue() {
    assertEquals(
      result.get("objectos/css/tmpl/FontFamilyValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.StandardName;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface FontFamilyValue extends PropertyValue permits StandardName, StringLiteral {}
      """
    );
  }

  @Test
  public void stringLiteral() {
    assertEquals(
      result.get("objectos/css/tmpl/StringLiteral.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface StringLiteral extends
          FontFamilyValue permits InternalInstruction {}
      """
    );
  }

}