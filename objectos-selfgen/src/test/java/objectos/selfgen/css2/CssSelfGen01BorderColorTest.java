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

public class CssSelfGen01BorderColorTest {

  private Map<String, String> result;

  @BeforeClass
  public void _generate() throws IOException {
    var spec = new CssSelfGen() {
      @Override
      protected void definition() {
        // global keywords
        var globalKeyword = t("GlobalKeyword",
          k("inherit"), k("initial"), k("unset")
        );

        // color
        var color = color(
          "currentcolor",
          "transparent"
        );

        // B
        pval("border-color", globalKeyword);
        pbox("border-color", color);
      }
    };

    result = generate(spec);
  }

  @Test
  public void colorValue() {
    assertEquals(
      result.get("objectos/css/tmpl/ColorValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.om.PropertyValue;
      import objectos.css.util.Color;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface ColorValue extends
          PropertyValue permits Color {}
      """
    );
  }

  @Test
  public void generatedColor() {
    assertEquals(
      result.get("objectos/css/util/GeneratedColor.java"),

      """
      package objectos.css.util;

      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedColor {
        public static final Color CURRENTCOLOR = Color.named("currentcolor");

        public static final Color TRANSPARENT = Color.named("transparent");
      }
      """
    );
  }

  @Test
  public void generatedCssTemplate() throws IOException {
    assertEquals(
      result.get("objectos/css/GeneratedCssTemplate.java"),

      """
      package objectos.css;

      import objectos.css.internal.NamedElement;
      import objectos.css.internal.Property;
      import objectos.css.internal.StyleDeclaration1;
      import objectos.css.internal.StyleDeclaration2;
      import objectos.css.internal.StyleDeclaration3;
      import objectos.css.internal.StyleDeclaration4;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.ColorValue;
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.css.util.Color;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = named("*");

        protected static final ColorValue currentcolor = Color.CURRENTCOLOR;

        protected static final ColorValue transparent = Color.TRANSPARENT;

        protected static final GlobalKeyword inherit = named("inherit");

        protected static final GlobalKeyword initial = named("initial");

        protected static final GlobalKeyword unset = named("unset");

        private static NamedElement named(String name) {
          return new NamedElement(name);
        }

        protected final StyleDeclaration borderColor(GlobalKeyword value) {
          return new StyleDeclaration1(Property.BORDER_COLOR, value.self());
        }

        protected final StyleDeclaration borderColor(ColorValue all) {
          return new StyleDeclaration1(Property.BORDER_COLOR, all.self());
        }

        protected final StyleDeclaration borderColor(ColorValue vertical, ColorValue horizontal) {
          return new StyleDeclaration2(Property.BORDER_COLOR, vertical.self(), horizontal.self());
        }

        protected final StyleDeclaration borderColor(ColorValue top, ColorValue horizontal, ColorValue bottom) {
          return new StyleDeclaration3(Property.BORDER_COLOR, top.self(), horizontal.self(), bottom.self());
        }

        protected final StyleDeclaration borderColor(ColorValue top, ColorValue right, ColorValue bottom, ColorValue left) {
          return new StyleDeclaration4(Property.BORDER_COLOR, top.self(), right.self(), bottom.self(), left.self());
        }
      }
      """
    );
  }

  @Test
  public void globalKeyword() {
    assertEquals(
      result.get("objectos/css/tmpl/GlobalKeyword.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.NamedElement;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface GlobalKeyword extends PropertyValue permits NamedElement {}
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
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public final class NamedElement implements Selector,
          GlobalKeyword {
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
  public void property() {
    assertEquals(
      result.get("objectos/css/internal/Property.java"),

      """
      package objectos.css.internal;

      import objectos.css.om.PropertyName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public enum Property implements PropertyName {
        BORDER_COLOR("border-color");

        private final String propertyName;

        private Property(String propertyName) {
          this.propertyName = propertyName;
        }

        @Override
        public final String toString() {
          return propertyName;
        }
      }
      """
    );
  }

}