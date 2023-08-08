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

public class CssSelfGen17GridTemplateColumnsTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var len = length("em", "px");
        var pct = percentage();

        var lengthPercentage = t("LengthPercentage", len, pct);

        function(
          "minmax",

          sig(lengthPercentage, "value1", lengthPercentage, "value2")
        );

        ValueType value = t(
          "GridTemplateColumnsValue",

          f("minmax"),
          k("none")
        );

        property(
          "grid-template-columns",

          sig(value, "value")
        );
      }
    };

    result = generate(spec);
  }

  @Test
  public void api() {
    assertEquals(
      result.get("objectos/css/tmpl/Api.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.Combinator;
      import objectos.css.internal.InternalInstruction;
      import objectos.css.internal.InternalZero;
      import objectos.css.internal.MediaType;
      import objectos.css.internal.StandardName;
      import objectos.css.internal.StandardPseudoClassSelector;
      import objectos.css.internal.StandardPseudoElementSelector;
      import objectos.css.internal.StandardTypeSelector;
      import objectos.css.util.ClassSelector;
      import objectos.css.util.IdSelector;
      import objectos.css.util.Length;
      import objectos.css.util.Percentage;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class Api {
        private Api() {}

        public sealed interface MediaRuleElement {}

        public sealed interface MediaFeature extends MediaRuleElement {}

        public sealed interface MediaFeatureOrStyleDeclaration extends MediaFeature, StyleDeclaration permits InternalInstruction {}

        public sealed interface MediaQuery extends MediaRuleElement permits MediaType {}

        public sealed interface StyleRule extends MediaRuleElement permits InternalInstruction {}

        public sealed interface StyleRuleElement {}

        public sealed interface StyleDeclaration extends StyleRuleElement {}

        public sealed interface StyleDeclarationInstruction permits InternalInstruction {}

        public sealed interface Selector extends StyleRuleElement {}

        public sealed interface SelectorInstruction extends Selector permits Combinator, InternalInstruction, StandardName, ClassSelector, IdSelector, StandardPseudoClassSelector, StandardPseudoElementSelector, StandardTypeSelector {}

        public sealed interface PropertyValue {}

        public sealed interface GridTemplateColumnsValue extends PropertyValue {}

        public sealed interface LengthPercentage extends PropertyValue {}

        public sealed interface ValueInstruction extends
            GridTemplateColumnsValue,
            LengthPercentage permits StandardName {}

        public sealed interface MinmaxFunction extends GridTemplateColumnsValue {}

        public sealed interface FunctionInstruction extends
            MinmaxFunction permits InternalInstruction {}

        public sealed interface LengthValue extends LengthPercentage permits InternalInstruction, Length, Zero {}

        public sealed interface PercentageValue extends LengthPercentage permits InternalInstruction, Percentage, Zero {}

        public sealed interface Zero extends LengthValue, PercentageValue permits InternalZero {}
      }
      """
    );
  }

  @Test
  public void generatedCssTemplate() {
    assertEquals(
      result.get("objectos/css/internal/GeneratedCssTemplate.java"),

      """
      package objectos.css.internal;

      import objectos.css.tmpl.Api.GridTemplateColumnsValue;
      import objectos.css.tmpl.Api.LengthPercentage;
      import objectos.css.tmpl.Api.LengthValue;
      import objectos.css.tmpl.Api.MinmaxFunction;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final GridTemplateColumnsValue none = StandardName.none;

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

        protected final StyleDeclaration gridTemplateColumns(GridTemplateColumnsValue value) {
          Check.notNull(value, "value == null");
          declaration(Property.GRID_TEMPLATE_COLUMNS, value);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);

        protected final MinmaxFunction minmax(LengthPercentage value1, LengthPercentage value2) {
          Check.notNull(value1, "value1 == null");
          Check.notNull(value2, "value2 == null");
          function(Function.MINMAX, value1, value2);
          return InternalInstruction.INSTANCE;
        }

        abstract void function(Function name, PropertyValue value1, PropertyValue value2);
      }
      """
    );
  }

}