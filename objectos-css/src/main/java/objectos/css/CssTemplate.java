/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css;

import java.util.Objects;
import objectos.css.internal.InternalZero;
import objectos.css.internal.Property;
import objectos.css.internal.StyleDeclaration1;
import objectos.css.internal.StyleDeclarationCommaSeparated;
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.PropertyValue;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.StringLiteral;
import objectos.css.tmpl.Zero;
import objectos.lang.Check;

public abstract class CssTemplate extends GeneratedCssTemplate {

  protected static final Zero $0 = InternalZero.INSTANCE;

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

  public final StyleSheet toStyleSheet() {
    try {
      builder = new StyleSheetBuilder();

      definition();

      return builder.build();
    } finally {
      builder = null;
    }
  }

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  protected abstract void definition();

  @Override
  protected final StyleDeclaration fontFamily(FontFamilyValue... values) {
    Objects.requireNonNull(values, "values == null");

    int length = values.length;

    return switch (length) {
      case 0 -> throw new IllegalArgumentException("The font-family property cannot be empty");

      case 1 -> {
        var source0 = Objects.requireNonNull(values[0], "values[0] == null");

        var value0 = fontFamilyValue(source0);

        yield new StyleDeclaration1(Property.FONT_FAMILY, value0);
      }

      default -> {
        var copy = new PropertyValue[length];

        for (int i = 0; i < length; i++) {
          var source = Check.notNull(values[i], "values[", i, "] == null");

          copy[i] = fontFamilyValue(source);
        }

        yield new StyleDeclarationCommaSeparated(Property.FONT_FAMILY, copy);
      }
    };
  }

  private PropertyValue fontFamilyValue(FontFamilyValue value) {
    if (value instanceof StringLiteral l) {
      return l.asFontFamilyValue();
    } else {
      return value;
    }
  }

  protected final void style(Selector selector,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector, "selector == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector, declarations);
  }

  protected final void style(Selector selector1, Selector selector2,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector1, selector2, declarations);
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(selector1, selector2, selector3, declarations);
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      Selector selector4,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(selector4, "selector4 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(
      selector1, selector2, selector3,
      selector4,
      declarations
    );
  }

  protected final void style(
      Selector selector1, Selector selector2, Selector selector3,
      Selector selector4, Selector selector5, Selector selector6,
      StyleDeclaration... declarations) {
    Objects.requireNonNull(selector1, "selector1 == null");
    Objects.requireNonNull(selector2, "selector2 == null");
    Objects.requireNonNull(selector3, "selector3 == null");
    Objects.requireNonNull(selector4, "selector4 == null");
    Objects.requireNonNull(selector5, "selector5 == null");
    Objects.requireNonNull(selector6, "selector6 == null");
    Objects.requireNonNull(declarations, "declarations == null");

    builder.addStyleRule(
      selector1, selector2, selector3,
      selector4, selector5, selector6,
      declarations
    );
  }

}