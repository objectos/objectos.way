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

      import objectos.css.tmpl.Api.FontFamilyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final FontFamilyValue monospace = StandardName.monospace;

        protected static final FontFamilyValue sansSerif = StandardName.sansSerif;

        protected abstract StyleDeclaration fontFamily(FontFamilyValue... values);
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

      import objectos.css.internal.Combinator;
      import objectos.css.internal.InternalInstruction;
      import objectos.css.internal.MediaType;
      import objectos.css.internal.StandardName;
      import objectos.css.internal.StandardPseudoClassSelector;
      import objectos.css.internal.StandardPseudoElementSelector;
      import objectos.css.internal.StandardTypeSelector;
      import objectos.css.util.ClassSelector;
      import objectos.css.util.IdSelector;
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

        public sealed interface FontFamilyValue extends PropertyValue {}

        public sealed interface ValueInstruction extends
            FontFamilyValue permits StandardName {}

        public sealed interface StringLiteral extends FontFamilyValue permits InternalInstruction {}
      }
      """
    );
  }

}