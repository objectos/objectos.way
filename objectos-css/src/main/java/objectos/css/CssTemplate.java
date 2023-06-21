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
import objectos.css.internal.InternalAttributeOperator;
import objectos.css.internal.InternalUrl;
import objectos.css.internal.InternalZero;
import objectos.css.internal.NamedElement;
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
import objectos.css.tmpl.Url;
import objectos.css.tmpl.Zero;
import objectos.lang.Check;

public abstract class CssTemplate extends GeneratedCssTemplate {

  protected static final Zero $0 = InternalZero.INSTANCE;

  protected static final AttributeOperator IS = InternalAttributeOperator.EQUALS;

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  public final StyleSheet toStyleSheet() {
    try {
      builder = new StyleSheetBuilder();

      definition();

      return builder.build();
    } finally {
      builder = null;
    }
  }

  protected final Selector attr(String name, AttributeOperator operator, String value) {
    return new NamedElement(
      "[" + name + operator + "\"" + value + "\"]"
    );
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

  protected final void style(StyleRuleElement... elements) {
    Objects.requireNonNull(elements, "elements == null");

    builder.beginStyleRule();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];

      if (element == null) {
        throw new NullPointerException(
          "elements[" + i + "] == null"
        );
      }

      if (element instanceof Selector selector) {
        builder.addSelector(selector);
      } else if (element instanceof StyleDeclaration declaration) {
        builder.addStyleDeclaration(declaration);
      } else {
        throw new IllegalArgumentException(
          "Unsupported element type = " + element.getClass()
        );
      }
    }

    builder.buildStyleRule();
  }

  protected final Selector sel(SelectorElement... elements) {
    builder.beginSelector();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];

      if (element == null) {
        throw new NullPointerException(
          "elements[" + i + "] == null"
        );
      }

      builder.addSelectorElement(element);
    }

    return builder.buildSelector();
  }

  protected final Url url(String value) {
    return InternalUrl.of(value);
  }

  private PropertyValue fontFamilyValue(FontFamilyValue value) {
    if (value instanceof StringLiteral l) {
      return l.asFontFamilyValue();
    } else {
      return value;
    }
  }

}