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

      import objectos.css.tmpl.Api.LengthValue;
      import objectos.css.tmpl.Api.LineWidth;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector any = StandardName.any;

        protected static final LineWidth medium = StandardName.medium;

        protected static final LineWidth thick = StandardName.thick;

        protected static final LineWidth thin = StandardName.thin;

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

        protected final StyleDeclaration borderWidth(LineWidth all) {
          Check.notNull(all, "all == null");
          declaration(Property.BORDER_WIDTH, all);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
          Check.notNull(vertical, "vertical == null");
          Check.notNull(horizontal, "horizontal == null");
          declaration(Property.BORDER_WIDTH, vertical, horizontal);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderWidth(LineWidth top, LineWidth vertical, LineWidth bottom) {
          Check.notNull(top, "top == null");
          Check.notNull(vertical, "vertical == null");
          Check.notNull(bottom, "bottom == null");
          declaration(Property.BORDER_WIDTH, top, vertical, bottom);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom, LineWidth left) {
          Check.notNull(top, "top == null");
          Check.notNull(right, "right == null");
          Check.notNull(bottom, "bottom == null");
          Check.notNull(left, "left == null");
          declaration(Property.BORDER_WIDTH, top, right, bottom, left);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

        abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);
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

      import objectos.css.tmpl.Api.Selector;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector __after = StandardPseudoElementSelector.__after;

        protected static final Selector __before = StandardPseudoElementSelector.__before;

        protected static final Selector a = StandardTypeSelector.a;

        protected static final Selector pre = StandardTypeSelector.pre;

        protected static final Selector any = StandardName.any;
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

      import objectos.css.tmpl.Api.ColorValue;
      import objectos.css.tmpl.Api.GlobalKeyword;
      import objectos.css.tmpl.Api.LengthValue;
      import objectos.css.tmpl.Api.PropertyValue;
      import objectos.css.tmpl.Api.Selector;
      import objectos.css.tmpl.Api.StyleDeclaration;
      import objectos.lang.Check;
      import objectos.lang.Generated;

      @Generated("objectos.selfgen.CssSpec")
      abstract class GeneratedCssTemplate {
        protected static final Selector __after = StandardPseudoElementSelector.__after;

        protected static final Selector __mozFocusInner = StandardPseudoElementSelector.__mozFocusInner;

        protected static final Selector _checked = StandardPseudoClassSelector._checked;

        protected static final Selector _mozFocusring = StandardPseudoClassSelector._mozFocusring;

        protected static final Selector a = StandardTypeSelector.a;

        protected static final Selector pre = StandardTypeSelector.pre;

        protected static final Selector any = StandardName.any;

        protected static final ColorValue currentcolor = StandardName.currentcolor;

        protected static final ColorValue transparent = StandardName.transparent;

        protected static final GlobalKeyword inherit = StandardName.inherit;

        protected static final GlobalKeyword initial = StandardName.initial;

        protected static final GlobalKeyword unset = StandardName.unset;

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

        protected final StyleDeclaration borderColor(GlobalKeyword value) {
          Check.notNull(value, "value == null");
          declaration(Property.BORDER_COLOR, value);
          return InternalInstruction.INSTANCE;
        }

        protected final StyleDeclaration borderColor(ColorValue all) {
          Check.notNull(all, "all == null");
          declaration(Property.BORDER_COLOR, all);
          return InternalInstruction.INSTANCE;
        }

        abstract void declaration(Property name, PropertyValue value);
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
