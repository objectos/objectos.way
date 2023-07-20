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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GeneratedCssTemplateStepTest {

  @Test
  public void borderWidth() {
    test(
      new CssSelfGen() {
        @Override
        protected void definition() {
          var length = length("em", "px");

          var lineWidth = t(
            "LineWidth",

            length,
            k("thin"),
            k("medium"),
            k("thick")
          );

          property(
            "border-width",

            sig(lineWidth, "all"),
            sig(lineWidth, "vertical", lineWidth, "horizontal"),
            sig(lineWidth, "top", lineWidth, "vertical", lineWidth, "bottom"),
            sig(lineWidth, "top", lineWidth, "right", lineWidth, "bottom", lineWidth, "left")
          );
        }
      },

      """
      package objectos.css.internal;

      import objectos.css.om.PropertyValue;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.tmpl.Length;
      import objectos.css.tmpl.LineWidth;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final LineWidth medium = StandardName.medium;

        protected static final LineWidth thick = StandardName.thick;

        protected static final LineWidth thin = StandardName.thin;

        protected final Length em(double value) {
          return length(value, LengthUnit.EM);
        }

        protected final Length em(int value) {
          return length(value, LengthUnit.EM);
        }

        protected final Length px(double value) {
          return length(value, LengthUnit.PX);
        }

        protected final Length px(int value) {
          return length(value, LengthUnit.PX);
        }

        abstract Length length(double value, LengthUnit unit);

        abstract Length length(int value, LengthUnit unit);

        protected final StyleDeclaration borderWidth(LineWidth all) {
          Check.notNull(all, "all == null");
          return declaration(Property.BORDER_WIDTH, all);
        }

        protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
          Check.notNull(vertical, "vertical == null");
          Check.notNull(horizontal, "horizontal == null");
          return declaration(Property.BORDER_WIDTH, vertical, horizontal);
        }

        protected final StyleDeclaration borderWidth(LineWidth top, LineWidth vertical, LineWidth bottom) {
          Check.notNull(top, "top == null");
          Check.notNull(vertical, "vertical == null");
          Check.notNull(bottom, "bottom == null");
          return declaration(Property.BORDER_WIDTH, top, vertical, bottom);
        }

        protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom, LineWidth left) {
          Check.notNull(top, "top == null");
          Check.notNull(right, "right == null");
          Check.notNull(bottom, "bottom == null");
          Check.notNull(left, "left == null");
          return declaration(Property.BORDER_WIDTH, top, right, bottom, left);
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
  public void selectors01() throws IOException {
    test(
      new CssSelfGen() {
        @Override
        protected void definition() {
          selectors(
            SelectorKind.TYPE,

            "a",
            "pre"
          );

          selectors(
            SelectorKind.PSEUDO_ELEMENT,

            "::after",
            "::before"
          );
        }
      },

      """
      package objectos.css.internal;

      import objectos.css.om.PropertyValue;
      import objectos.css.om.PseudoElementSelector;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.om.TypeSelector;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final PseudoElementSelector __after = StandardPseudoElementSelector.__after;

        protected static final PseudoElementSelector __before = StandardPseudoElementSelector.__before;

        protected static final TypeSelector a = StandardTypeSelector.a;

        protected static final TypeSelector pre = StandardTypeSelector.pre;

        protected static final Selector any = StandardName.any;

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
  public void all() {
    test(
      new CssSelfGen() {
        @Override
        protected void definition() {
          length("px", "em");

          selectors(
            SelectorKind.TYPE,

            "a",
            "pre"
          );

          selectors(
            SelectorKind.PSEUDO_CLASS,

            ":checked",
            ":-moz-focusring"
          );

          selectors(
            SelectorKind.PSEUDO_ELEMENT,

            "::after",
            "::-moz-focus-inner"
          );

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
          property(
            "border-color",

            sig(globalKeyword, "value"),
            sig(color, "all")
          );
        }
      },

      """
      package objectos.css.internal;

      import objectos.css.om.PropertyValue;
      import objectos.css.om.PseudoClassSelector;
      import objectos.css.om.PseudoElementSelector;
      import objectos.css.om.Selector;
      import objectos.css.om.StyleDeclaration;
      import objectos.css.om.TypeSelector;
      import objectos.css.tmpl.ColorValue;
      import objectos.css.tmpl.GlobalKeyword;
      import objectos.css.tmpl.Length;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final PseudoElementSelector __after = StandardPseudoElementSelector.__after;

        protected static final PseudoElementSelector __mozFocusInner = StandardPseudoElementSelector.__mozFocusInner;

        protected static final PseudoClassSelector _checked = StandardPseudoClassSelector._checked;

        protected static final PseudoClassSelector _mozFocusring = StandardPseudoClassSelector._mozFocusring;

        protected static final TypeSelector a = StandardTypeSelector.a;

        protected static final TypeSelector pre = StandardTypeSelector.pre;

        protected static final Selector any = StandardName.any;

        protected static final ColorValue currentcolor = StandardName.currentcolor;

        protected static final ColorValue transparent = StandardName.transparent;

        protected static final GlobalKeyword inherit = StandardName.inherit;

        protected static final GlobalKeyword initial = StandardName.initial;

        protected static final GlobalKeyword unset = StandardName.unset;

        protected final Length em(double value) {
          return length(value, LengthUnit.EM);
        }

        protected final Length em(int value) {
          return length(value, LengthUnit.EM);
        }

        protected final Length px(double value) {
          return length(value, LengthUnit.PX);
        }

        protected final Length px(int value) {
          return length(value, LengthUnit.PX);
        }

        abstract Length length(double value, LengthUnit unit);

        abstract Length length(int value, LengthUnit unit);

        protected final StyleDeclaration borderColor(GlobalKeyword value) {
          Check.notNull(value, "value == null");
          return declaration(Property.BORDER_COLOR, value);
        }

        protected final StyleDeclaration borderColor(ColorValue all) {
          Check.notNull(all, "all == null");
          return declaration(Property.BORDER_COLOR, all);
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

  private void test(CssSelfGen spec, String expected) {
    Map<String, String> result;

    try {
      result = Util.generate(spec);
    } catch (IOException e) {
      Assert.fail("Could not generate result", e);

      return;
    }

    String string;
    string = result.get("objectos/css/internal/GeneratedCssTemplate.java");

    assertEquals(string, expected);
  }

}
