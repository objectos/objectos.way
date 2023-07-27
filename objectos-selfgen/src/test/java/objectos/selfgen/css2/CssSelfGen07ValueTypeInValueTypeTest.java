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

public class CssSelfGen07ValueTypeInValueTypeTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var len = length("em", "px");
        var pct = percentage();

        var lengthPercentage = t("LengthPercentage", len, pct);

        property(
          "line-height",

          sig(t("LineHeightValue", lengthPercentage, k("normal")), "value")
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

      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.PropertyValue;
      import objectos.css.tmpl.PropertyValue.LengthValue;
      import objectos.css.tmpl.PropertyValue.LineHeightValue;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final LineHeightValue normal = StandardName.normal;

        protected final LengthValue em(double value) {
          return length(value, LengthUnit.EM);
        }

        protected final LengthValue em(int value) {
          return length(value, LengthUnit.EM);
        }

        protected final LengthValue px(double value) {
          return length(value, LengthUnit.PX);
        }

        protected final LengthValue px(int value) {
          return length(value, LengthUnit.PX);
        }

        abstract LengthValue length(double value, LengthUnit unit);

        abstract LengthValue length(int value, LengthUnit unit);

        protected final StyleDeclaration lineHeight(LineHeightValue value) {
          Check.notNull(value, "value == null");
          declaration(Property.LINE_HEIGHT, value);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);
      }
      """
    );
  }

  @Test
  public void propertyValue() {
    assertEquals(
      result.get("objectos/css/tmpl/PropertyValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalInstruction;
      import objectos.css.internal.InternalZero;
      import objectos.css.internal.StandardName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface PropertyValue {
        sealed interface LengthPercentage extends LineHeightValue {}

        sealed interface LineHeightValue extends PropertyValue {}

        sealed interface ValueInstruction extends
            LengthPercentage,
            LineHeightValue permits StandardName {}

        sealed interface LengthValue extends LengthPercentage permits InternalInstruction, Zero {}

        sealed interface PercentageValue extends LengthPercentage permits InternalInstruction, Zero {}

        sealed interface Zero extends LengthValue, PercentageValue permits InternalZero {}
      }
      """
    );
  }

}