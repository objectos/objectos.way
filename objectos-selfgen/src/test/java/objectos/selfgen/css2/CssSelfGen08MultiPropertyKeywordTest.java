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
        var lineStyle = t(
          "LineStyle",

          keywords(
            "inset",
            "none",
            "outset"
          )
        );

        property(
          "border-style",

          sig(lineStyle, "value")
        );

        var textSizeAdjustValue = t("TextSizeAdjustValue",
          keywords("auto", "none")
        );

        property(
          "text-size-adjust",

          sig(textSizeAdjustValue, "value")
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
      import objectos.css.tmpl.NoneKeyword;
      import objectos.css.tmpl.TextSizeAdjustValue;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final TextSizeAdjustValue auto = StandardName.auto;

        protected static final LineStyle inset = StandardName.inset;

        protected static final NoneKeyword none = StandardName.none;

        protected static final LineStyle outset = StandardName.outset;

        protected final StyleDeclaration borderStyle(LineStyle value) {
          Check.notNull(value, "value == null");
          declaration(Property.BORDER_STYLE, value);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration textSizeAdjust(TextSizeAdjustValue value) {
          Check.notNull(value, "value == null");
          declaration(Property.TEXT_SIZE_ADJUST, value);
          return InternalInstruction.INSTANCE;
        }

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
  public void lineStyle() {
    assertEquals(
      result.get("objectos/css/tmpl/LineStyle.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.StandardName;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface LineStyle extends PropertyValue permits NoneKeyword, StandardName {}
      """
    );
  }

  @Test
  public void noneKeyword() {
    assertEquals(
      result.get("objectos/css/tmpl/NoneKeyword.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.StandardName;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface NoneKeyword extends
          LineStyle,
          TextSizeAdjustValue permits StandardName {}
      """
    );
  }

  @Test
  public void textSizeAdjustValue() {
    assertEquals(
      result.get("objectos/css/tmpl/TextSizeAdjustValue.java"),

      """
      package objectos.css.tmpl;

      import objectos.css.internal.StandardName;
      import objectos.css.om.PropertyValue;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      public sealed interface TextSizeAdjustValue extends PropertyValue permits NoneKeyword, StandardName {}
      """
    );
  }

}