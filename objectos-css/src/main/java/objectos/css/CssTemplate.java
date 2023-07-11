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
import objectos.css.internal.InternalLength;
import objectos.css.internal.InternalSelector;
import objectos.css.internal.InternalStyleRule;
import objectos.css.internal.InternalStyleSheet;
import objectos.css.internal.InternalUrl;
import objectos.css.internal.InternalZero;
import objectos.css.internal.MediaType;
import objectos.css.internal.NamedElement;
import objectos.css.internal.Property;
import objectos.css.internal.StyleDeclaration1;
import objectos.css.internal.StyleDeclarationCommaSeparated;
import objectos.css.internal.TopLevelElement;
import objectos.css.om.PropertyValue;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleRule;
import objectos.css.om.StyleSheet;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.Length;
import objectos.css.tmpl.MediaRuleElement;
import objectos.css.tmpl.SelectorElement;
import objectos.css.tmpl.StringLiteral;
import objectos.css.tmpl.StyleRuleElement;
import objectos.css.tmpl.Url;
import objectos.css.tmpl.Zero;
import objectos.css.util.Color;
import objectos.lang.Check;
import objectos.util.GrowableList;

/**
 * @since 0.7
 */
public abstract class CssTemplate extends GeneratedCssTemplate {

  protected static final MediaRuleElement screen = MediaType.SCREEN;

  protected static final Zero $0 = InternalZero.INSTANCE;

  protected static final AttributeOperator IS = InternalAttributeOperator.EQUALS;

  protected static final Length PX = InternalLength.of("px", 1);

  protected static final Length V0 = InternalLength.of("px", 0);
  protected static final Length V0_5 = v(0.5);
  protected static final Length V1 = v(1);
  protected static final Length V1_5 = v(1.5);
  protected static final Length V2 = v(2);
  protected static final Length V2_5 = v(2.5);
  protected static final Length V3 = v(3);
  protected static final Length V3_5 = v(3.5);
  protected static final Length V4 = v(4);
  protected static final Length V5 = v(5);
  protected static final Length V6 = v(6);
  protected static final Length V7 = v(7);
  protected static final Length V8 = v(8);
  protected static final Length V9 = v(9);
  protected static final Length V10 = v(10);
  protected static final Length V11 = v(11);
  protected static final Length V12 = v(12);
  protected static final Length V14 = v(14);
  protected static final Length V16 = v(16);
  protected static final Length V20 = v(20);
  protected static final Length V24 = v(24);
  protected static final Length V28 = v(28);
  protected static final Length V32 = v(32);
  protected static final Length V36 = v(36);
  protected static final Length V40 = v(40);
  protected static final Length V44 = v(44);
  protected static final Length V48 = v(48);
  protected static final Length V52 = v(56);
  protected static final Length V60 = v(60);
  protected static final Length V64 = v(64);
  protected static final Length V68 = v(68);
  protected static final Length V72 = v(72);
  protected static final Length V80 = v(80);
  protected static final Length V96 = v(96);

  private GrowableList<TopLevelElement> topLevelElements;

  protected CssTemplate() {}

  private static Length v(double unit) {
    return InternalLength.of("rem", unit * 0.25);
  }

  private static Length v(int unit) {
    return InternalLength.of("rem", unit * 0.25);
  }

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  public final StyleSheet toStyleSheet() {
    try {
      topLevelElements = new GrowableList<>();

      definition();

      return new InternalStyleSheet(
        topLevelElements.toUnmodifiableList()
      );
    } finally {
      topLevelElements = null;
    }
  }

  protected final Selector attr(String name) {
    return new NamedElement(
      "[" + name + "]"
    );
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

  protected final Color hex(String hex) {
    return Color.ofHex(hex);
  }

  protected final Object media(MediaRuleElement... elements) {
    throw new UnsupportedOperationException("Implement me");
  }

  protected final Selector sel(SelectorElement... elements) {
    GrowableList<SelectorElement> safeElements;
    safeElements = new GrowableList<>();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];

      safeElements.addWithNullMessage(element, "elements[", i, "] == null");
    }

    return new InternalSelector(
      safeElements.toUnmodifiableList()
    );
  }

  protected final StyleRule style(StyleRuleElement... elements) {
    GrowableList<Selector> selectors;
    selectors = new GrowableList<>();

    GrowableList<StyleDeclaration> declarations;
    declarations = new GrowableList<>();

    for (int i = 0; i < elements.length; i++) {
      var element = elements[i];

      if (element == null) {
        throw new NullPointerException(
          "elements[" + i + "] == null"
        );
      }

      if (element instanceof Selector selector) {
        selectors.add(selector);

        continue;
      }

      if (element instanceof StyleDeclaration declaration) {
        declarations.add(declaration);

        continue;
      }

      throw new IllegalArgumentException(
        "Unsupported element type = " + element.getClass()
      );
    }

    InternalStyleRule rule;
    rule = new InternalStyleRule(
      selectors.toUnmodifiableList(),
      declarations.toUnmodifiableList()
    );

    topLevelElements.add(rule);

    return rule;
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