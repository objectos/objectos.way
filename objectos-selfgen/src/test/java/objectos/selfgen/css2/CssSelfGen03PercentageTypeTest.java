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

public class CssSelfGen03PercentageTypeTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var pct = percentage();

        property(
          "line-height",

          sig(t("LineHeightValue", pct), "value")
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
      import objectos.css.tmpl.LineHeightValue;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected final StyleDeclaration lineHeight(LineHeightValue value) {
          Check.notNull(value, "value == null");
          return declaration(Property.LINE_HEIGHT, value);
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

  @Test
  public void percentage() {
    assertEquals(
      result.get("objectos/css/tmpl/Percentage.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalInstruction;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Percentage extends
          LineHeightValue permits InternalInstruction, Zero {}
      """
    );
  }

  @Test
  public void zero() {
    assertEquals(
      result.get("objectos/css/tmpl/Zero.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalZero;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Zero extends Percentage permits InternalZero {}
      """
    );
  }

}