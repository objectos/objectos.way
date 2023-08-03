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

      import objectos.css.om.Selector;
      import objectos.css.tmpl.Api.LineHeightValue;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.StyleDeclaration;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

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
  public void api() {
    assertEquals(
      result.get("objectos/css/tmpl/Api.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalInstruction;
      import objectos.css.internal.InternalZero;
      import objectos.css.internal.StandardName;
      import objectos.css.util.Percentage;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class Api {
        private Api() {}

        public sealed interface PropertyValue {}

        public sealed interface LineHeightValue extends PropertyValue {}

        public sealed interface ValueInstruction extends
            LineHeightValue permits StandardName {}

        public sealed interface PercentageValue extends LineHeightValue permits InternalInstruction, Percentage, Zero {}

        public sealed interface Zero extends PercentageValue permits InternalZero {}
      }
      """
    );
  }

}