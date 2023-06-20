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

        pval("line-height", t("LineHeightValue",
          lengthPercentage, k("normal"))
        );
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

      import objectos.css.internal.InternalLength;
      import objectos.css.internal.InternalPercentage;
      import objectos.css.internal.NamedElement;
      import objectos.css.internal.Property;
      import objectos.css.internal.StyleDeclaration1;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.Length;
      import objectos.css.tmpl.LineHeightValue;
      import objectos.css.tmpl.Percentage;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final LineHeightValue normal = named("normal");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final Length em(double value) {
          return InternalLength.of("em", value);
        }

        protected final Length em(int value) {
          return InternalLength.of("em", value);
        }

        protected final Length px(double value) {
          return InternalLength.of("px", value);
        }

        protected final Length px(int value) {
          return InternalLength.of("px", value);
        }

        protected final Percentage pct(double value) {
          return InternalPercentage.of(value);
        }

        protected final Percentage pct(int value) {
          return InternalPercentage.of(value);
        }

        protected final StyleDeclaration lineHeight(LineHeightValue value) {
          return new StyleDeclaration1(Property.LINE_HEIGHT, value.self());
        }
      }
      """
    );
  }

  @Test
  public void length() {
    assertEquals(
      result.get("objectos/css/tmpl/Length.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalLength;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Length extends
          LengthPercentage permits InternalLength, Zero {}
      """
    );
  }

  @Test
  public void lengthPercentage() {
    assertEquals(
      result.get("objectos/css/tmpl/LengthPercentage.java"),

      """
      package objectos.css.tmpl;

      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LengthPercentage extends
          LineHeightValue permits Length, Percentage {}
      """
    );
  }

  @Test
  public void lineHeightValue() {
    assertEquals(
      result.get("objectos/css/tmpl/LineHeightValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LineHeightValue extends PropertyValue permits LengthPercentage, NamedElement {}
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
      import objectos.css.tmpl.LineHeightValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class NamedElement implements Selector,
          LineHeightValue {
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
  public void percentage() {
    assertEquals(
      result.get("objectos/css/tmpl/Percentage.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.InternalPercentage;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface Percentage extends
          LengthPercentage permits InternalPercentage, Zero {}
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
      public sealed interface Zero extends Length, Percentage permits InternalZero {}
      """
    );
  }

}