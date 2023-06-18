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

public class CssSelfGen08MultiPropertyKeywordTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        var lineStyle = def("LineStyle",
          keywords(
            "inset",
            "none",
            "outset"
          )
        );

        pval("border-style", lineStyle);

        var textSizeAdjustValue = def("TextSizeAdjustValue",
          keywords("auto", "none")
        );

        pval("text-size-adjust", textSizeAdjustValue);
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
      import objectos.css.tmpl.NoneKeyword;
      import objectos.css.tmpl.TextSizeAdjustValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final TextSizeAdjustValue auto = named("auto");

        protected static final LineStyle inset = named("inset");

        protected static final NoneKeyword none = named("none");

        protected static final LineStyle outset = named("outset");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final StyleDeclaration borderStyle(LineStyle value) {
          return new StyleDeclaration1(Property.BORDER_STYLE, value.self());
        }

        protected final StyleDeclaration textSizeAdjust(TextSizeAdjustValue value) {
          return new StyleDeclaration1(Property.TEXT_SIZE_ADJUST, value.self());
        }
      }
      """
    );
  }

  @Test
  public void lineStyle() {
    assertEquals(
      result.get("objectos/css/tmpl/LineStyle.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LineStyle extends PropertyValue permits NamedElement, NoneKeyword {}
      """
    );
  }

  @Test
  public void namedElement() {
    assertEquals(
      result.get("objectos/css/internal/NamedElement.java"),

      """
      package objectos.css.internal;

      import objectos.css.om.Selector;
      import objectos.css.tmpl.LineStyle;
      import objectos.css.tmpl.NoneKeyword;
      import objectos.css.tmpl.TextSizeAdjustValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class NamedElement implements Selector,
          LineStyle,
          TextSizeAdjustValue,
          NoneKeyword {
        private final String name;

        public NamedElement(String name) {
          this.name = name;
        }

        @Override
        public final String toString() {
          return name;
        }
      }
      """
    );
  }

  @Test
  public void noneKeyword() {
    assertEquals(
      result.get("objectos/css/tmpl/NoneKeyword.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface NoneKeyword extends
          LineStyle,
          TextSizeAdjustValue permits NamedElement {}
      """
    );
  }

  @Test
  public void textSizeAdjustValue() {
    assertEquals(
      result.get("objectos/css/tmpl/TextSizeAdjustValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface TextSizeAdjustValue extends PropertyValue permits NamedElement, NoneKeyword {}
      """
    );
  }

}