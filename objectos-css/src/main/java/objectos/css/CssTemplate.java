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
import java.util.regex.Pattern;
import objectos.css.internal.NamedSelector;
import objectos.css.internal.Property;
import objectos.css.internal.StyleDeclaration1;
import objectos.css.internal.StyleDeclarationCommaSeparated;
import objectos.css.internal.StyleSheetBuilder;
import objectos.css.om.PropertyValue;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.StyleSheet;
import objectos.lang.Check;

public abstract class CssTemplate {

  // type selectors

  protected static final Selector a = named("a");

  protected static final Selector b = named("b");
  protected static final Selector body = named("body");

  protected static final Selector h1 = named("h1");
  protected static final Selector h2 = named("h2");
  protected static final Selector h3 = named("h3");
  protected static final Selector h4 = named("h4");
  protected static final Selector h5 = named("h5");
  protected static final Selector h6 = named("h6");
  protected static final Selector hr = named("hr");
  protected static final Selector html = named("html");

  protected static final Selector li = named("li");

  protected static final Selector strong = named("strong");

  protected static final Selector ul = named("ul");

  // pseudo element selectors

  protected static final Selector __after = named("::after");

  protected static final Selector __before = named("::before");

  // universal selector

  protected static final Selector any = named("*");

  // keywords

  protected sealed interface GlobalKeyword extends PropertyValue {}

  private record Keyword(String name)
      implements
      GlobalKeyword,

      // A
      AutoKeyword,

      // B
      BoxSizingValue,

      // C
      Color,

      // D
      DashedKeyword,
      DottedKeyword,
      DoubleKeyword,

      // F
      FontFamilyValue,
      FontFeatureSettingsValue,
      FontSizeValue,
      FontVariationSettingsValue,
      FontWeightValue,

      // H
      HeightValue,

      // L
      LineHeightValue,
      LineStyle,
      LineWidth,

      // M
      MarginValue,
      MediumKeyword,

      // N
      NoneKeyword,
      NormalKeyword,

      // S
      SolidKeyword,

      // T
      TextDecorationLineValue,
      TextDecorationStyleValue,
      TextDecorationThicknessValue,
      TextSizeAdjustValue {
    @Override
    public final String toString() { return name; }
  }

  // string

  protected static final class StringLiteral implements FontFamilyValue {
    private static final Pattern FONT_FAMILY = Pattern.compile("-?[a-zA-Z_][a-zA-Z0-9_-]*");

    final String value;

    public StringLiteral(String value) { this.value = value; }

    @Override
    public final String toString() { return "\"" + value + "\""; }

    final PropertyValue asFontFamilyValue() {
      var matcher = FONT_FAMILY.matcher(value);

      if (!matcher.matches()) {
        return this;
      } else {
        return new Keyword(value);
      }
    }
  }

  protected final StringLiteral l(String value) {
    return new StringLiteral(
      Objects.requireNonNull(value, "value == null")
    );
  }

  // zero

  protected static final class Zero implements HeightValue, LineWidth, MarginValue {
    static final Zero INSTANCE = new Zero();

    private Zero() {}

    @Override
    public final String toString() { return "0"; }
  }

  protected static final Zero $0 = Zero.INSTANCE;

  // A
  protected sealed interface AutoKeyword
      extends HeightValue, MarginValue, TextDecorationThicknessValue, TextSizeAdjustValue {}

  protected static final Color aqua = kw("aqua");
  protected static final AutoKeyword auto = kw("auto");

  // B
  protected static final BoxSizingValue borderBox = kw("border-box");
  protected static final Color black = kw("black");
  protected static final TextDecorationLineValue blink = kw("blink");
  protected static final Color blue = kw("blue");
  protected static final FontWeightValue bold = kw("bold");
  protected static final FontWeightValue bolder = kw("bolder");

  // C
  protected static final BoxSizingValue contentBox = kw("content-box");
  protected static final Color currentcolor = kw("currentcolor");
  protected static final FontFamilyValue cursive = kw("cursive");

  // D
  protected sealed interface DashedKeyword extends LineStyle, TextDecorationStyleValue {}
  protected sealed interface DottedKeyword extends LineStyle, TextDecorationStyleValue {}
  protected sealed interface DoubleKeyword extends LineStyle, TextDecorationStyleValue {}

  protected static final DashedKeyword dashed = kw("dashed");
  protected static final DottedKeyword dotted = kw("dotted");
  protected static final DoubleKeyword double$ = kw("double");

  // E
  protected static final FontFamilyValue emoji = kw("emoji");

  // F
  protected static final FontFamilyValue fangsong = kw("fangsong");
  protected static final FontFamilyValue fantasy = kw("fantasy");
  protected static final HeightValue fitContent = kw("fit-content");
  protected static final TextDecorationThicknessValue fromFont = kw("from-font");
  protected static final Color fuchsia = kw("fuchsia");

  // G
  protected static final Color gray = kw("gray");
  protected static final Color green = kw("green");
  protected static final LineStyle groove = kw("groove");

  // H
  protected static final LineStyle hidden = kw("hidden");

  // I
  protected static final GlobalKeyword inherit = kw("inherit");
  protected static final GlobalKeyword initial = kw("initial");
  protected static final LineStyle inset = kw("inset");

  // L
  protected static final FontSizeValue large = kw("large");
  protected static final FontSizeValue larger = kw("larger");
  protected static final FontWeightValue lighter = kw("lighter");
  protected static final Color lime = kw("lime");
  protected static final TextDecorationLineValue lineThrough = kw("line-through");

  // M
  protected sealed interface MediumKeyword extends FontSizeValue, LineWidth {}

  protected static final Color maroon = kw("maroon");
  protected static final FontFamilyValue math = kw("math");
  protected static final HeightValue maxContent = kw("max-content");
  protected static final MediumKeyword medium = kw("medium");
  protected static final HeightValue minContent = kw("min-content");
  protected static final FontFamilyValue monospace = kw("monospace");

  // N
  protected sealed interface NoneKeyword
      extends LineStyle, TextDecorationValue, TextSizeAdjustValue {}
  protected sealed interface NormalKeyword
      extends
      FontFeatureSettingsValue,
      FontVariationSettingsValue,
      FontWeightValue,
      LineHeightValue {}

  protected static final Color navy = kw("navy");
  protected static final NoneKeyword none = kw("none");
  protected static final NormalKeyword normal = kw("normal");

  // O
  protected static final Color olive = kw("olive");
  protected static final LineStyle outset = kw("outset");
  protected static final TextDecorationLineValue overline = kw("overline");

  // P
  protected static final Color purple = kw("purple");

  // R
  protected static final Color red = kw("red");
  protected static final LineStyle ridge = kw("ridge");

  // S
  protected sealed interface SolidKeyword extends LineStyle, TextDecorationStyleValue {}

  protected static final FontFamilyValue sansSerif = kw("sans-serif");
  protected static final FontFamilyValue serif = kw("serif");
  protected static final Color silver = kw("silver");
  protected static final FontSizeValue small = kw("small");
  protected static final FontSizeValue smaller = kw("smaller");
  protected static final SolidKeyword solid = kw("solid");
  protected static final FontFamilyValue systemUi = kw("system-ui");

  // T
  protected static final Color teal = kw("teal");
  protected static final LineWidth thick = kw("thick");
  protected static final LineWidth thin = kw("thin");
  protected static final Color transparent = kw("transparent");

  // U
  protected static final FontFamilyValue uiMonospace = kw("ui-monospace");
  protected static final FontFamilyValue uiRounded = kw("ui-rounded");
  protected static final FontFamilyValue uiSansSerif = kw("ui-sans-serif");
  protected static final FontFamilyValue uiSerif = kw("ui-serif");
  protected static final TextDecorationLineValue underline = kw("underline");
  protected static final GlobalKeyword unset = kw("unset");

  // W
  protected static final TextDecorationStyleValue wavy = kw("wavy");
  protected static final Color white = kw("white");

  // X
  protected static final FontSizeValue xLarge = kw("x-large");
  protected static final FontSizeValue xxLarge = kw("xx-large");
  protected static final FontSizeValue xxxLarge = kw("xxx-large");
  protected static final FontSizeValue xSmall = kw("x-small");
  protected static final FontSizeValue xxSmall = kw("xx-small");

  // Y
  protected static final Color yellow = kw("yellow");

  private StyleSheetBuilder builder;

  protected CssTemplate() {}

  private static final NamedSelector named(String name) {
    return new NamedSelector(name);
  }

  private static final Keyword kw(String name) {
    return new Keyword(name);
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

  @Override
  public String toString() {
    return toStyleSheet().toString();
  }

  protected abstract void definition();

  // selector methods

  protected final IdSelector id(String id) {
    return IdSelector.of(id);
  }

  // length methods

  protected sealed interface LengthPercentage
      extends
      FontSizeValue,
      HeightValue,
      LineHeightValue,
      MarginValue,
      TextDecorationThicknessValue {}

  protected static final class Length implements LengthPercentage, LineWidth {
    static final Length ZERO = new Length("0");

    final String value;

    public Length(String value) { this.value = value; }

    @Override
    public final String toString() { return value; }
  }

  private enum LengthUnit {
    ch,
    cm,
    em,
    ex,
    in,
    mm,
    pc,
    pt,
    px,
    q,
    rem,
    vh,
    vmax,
    vmin,
    vw;

    final Length of(double value) {
      if (value == 0) {
        return Length.ZERO;
      } else {
        var s = Double.toString(value);

        return new Length(s + name());
      }
    }

    final Length of(int value) {
      if (value == 0) {
        return Length.ZERO;
      } else {
        var s = Integer.toString(value);

        return new Length(s + name());
      }
    }
  }

  protected final Length em(double value) {
    return LengthUnit.em.of(value);
  }

  protected final Length em(int value) {
    return LengthUnit.em.of(value);
  }

  protected final Length px(double value) {
    return LengthUnit.px.of(value);
  }

  protected final Length px(int value) {
    return LengthUnit.px.of(value);
  }

  // percentage method

  protected static final class Percentage implements LengthPercentage, TextSizeAdjustValue {
    static final Percentage ZERO = new Percentage("0");

    final String value;

    public Percentage(String value) { this.value = value; }

    @Override
    public final String toString() { return value; }
  }

  protected final Percentage pct(double value) {
    if (value == 0) {
      return Percentage.ZERO;
    } else {
      var s = Double.toString(value);

      return new Percentage(s + "%");
    }
  }

  protected final Percentage pct(int value) {
    if (value == 0) {
      return Percentage.ZERO;
    } else {
      var s = Integer.toString(value);

      return new Percentage(s + "%");
    }
  }

  // property methods

  // property methods: border-color

  protected sealed interface Color extends TextDecorationValue {}

  protected final StyleDeclaration borderColor(GlobalKeyword value) {
    return Property.BORDER_COLOR.value(value);
  }

  protected final StyleDeclaration borderColor(Color all) {
    return Property.BORDER_COLOR.sides1(all);
  }

  protected final StyleDeclaration borderColor(Color vertical, Color horizontal) {
    return Property.BORDER_COLOR.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderColor(Color top, Color horizontal,
      Color bottom) {
    return Property.BORDER_COLOR.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderColor(Color top, Color right, Color bottom,
      Color left) {
    return Property.BORDER_COLOR.sides4(top, right, bottom, left);
  }

  // property methods: border-style

  protected sealed interface LineStyle extends PropertyValue {}

  protected final StyleDeclaration borderStyle(GlobalKeyword value) {
    return Property.BORDER_STYLE.value(value);
  }

  protected final StyleDeclaration borderStyle(LineStyle all) {
    return Property.BORDER_STYLE.sides1(all);
  }

  protected final StyleDeclaration borderStyle(LineStyle vertical, LineStyle horizontal) {
    return Property.BORDER_STYLE.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle horizontal,
      LineStyle bottom) {
    return Property.BORDER_STYLE.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle right, LineStyle bottom,
      LineStyle left) {
    return Property.BORDER_STYLE.sides4(top, right, bottom, left);
  }

  // property methods: border-width

  protected sealed interface LineWidth extends PropertyValue {}

  protected final StyleDeclaration borderWidth(GlobalKeyword value) {
    return Property.BORDER_WIDTH.value(value);
  }

  protected final StyleDeclaration borderWidth(LineWidth all) {
    return Property.BORDER_WIDTH.sides1(all);
  }

  protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
    return Property.BORDER_WIDTH.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth horizontal,
      LineWidth bottom) {
    return Property.BORDER_WIDTH.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom,
      LineWidth left) {
    return Property.BORDER_WIDTH.sides4(top, right, bottom, left);
  }

  protected final StyleDeclaration borderTopWidth(GlobalKeyword value) {
    return Property.BORDER_TOP_WIDTH.value(value);
  }

  protected final StyleDeclaration borderTopWidth(LineWidth value) {
    return Property.BORDER_TOP_WIDTH.value(value);
  }

  protected final StyleDeclaration borderRightWidth(GlobalKeyword value) {
    return Property.BORDER_RIGHT_WIDTH.value(value);
  }

  protected final StyleDeclaration borderRightWidth(LineWidth value) {
    return Property.BORDER_RIGHT_WIDTH.value(value);
  }

  protected final StyleDeclaration borderBottomWidth(GlobalKeyword value) {
    return Property.BORDER_BOTTOM_WIDTH.value(value);
  }

  protected final StyleDeclaration borderBottomWidth(LineWidth value) {
    return Property.BORDER_BOTTOM_WIDTH.value(value);
  }

  protected final StyleDeclaration borderLeftWidth(GlobalKeyword value) {
    return Property.BORDER_LEFT_WIDTH.value(value);
  }

  protected final StyleDeclaration borderLeftWidth(LineWidth value) {
    return Property.BORDER_LEFT_WIDTH.value(value);
  }

  // property methods: box-sizing

  protected sealed interface BoxSizingValue extends PropertyValue {}

  protected final StyleDeclaration boxSizing(GlobalKeyword value) {
    return Property.BOX_SIZING.value(value);
  }

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    return Property.BOX_SIZING.value(value);
  }

  // property methods: color

  protected final StyleDeclaration color(GlobalKeyword value) {
    return Property.COLOR.value(value);
  }

  protected final StyleDeclaration color(Color value) {
    return Property.COLOR.value(value);
  }

  // property methods: font-family

  protected sealed interface FontFamilyValue extends PropertyValue {}

  @Deprecated
  protected final StyleDeclaration fontFamily() {
    throw new UnsupportedOperationException();
  }

  protected final StyleDeclaration fontFamily(GlobalKeyword value) {
    return Property.FONT_FAMILY.value(value);
  }

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

  // property methods: font-feature-settings

  protected sealed interface FontFeatureSettingsValue extends PropertyValue {}

  protected final StyleDeclaration fontFeatureSettings(GlobalKeyword value) {
    return Property.FONT_FEATURE_SETTINGS.value(value);
  }

  protected final StyleDeclaration fontFeatureSettings(FontFeatureSettingsValue value) {
    return Property.FONT_FEATURE_SETTINGS.value(value);
  }

  // property methods: font-size

  protected sealed interface FontSizeValue extends PropertyValue {}

  protected final StyleDeclaration fontSize(GlobalKeyword value) {
    return Property.FONT_SIZE.value(value);
  }

  protected final StyleDeclaration fontSize(FontSizeValue value) {
    return Property.FONT_SIZE.value(value);
  }

  // property methods: font-variation-settings

  protected sealed interface FontVariationSettingsValue extends PropertyValue {}

  protected final StyleDeclaration fontVariationSettings(FontVariationSettingsValue value) {
    return Property.FONT_VARIATION_SETTINGS.value(value);
  }

  protected final StyleDeclaration fontVariationSettings(GlobalKeyword value) {
    return Property.FONT_VARIATION_SETTINGS.value(value);
  }

  // property methods: font-weight

  protected sealed interface FontWeightValue extends PropertyValue {}

  protected final StyleDeclaration fontWeight(int value) {
    return Property.FONT_WEIGHT.value(value);
  }

  protected final StyleDeclaration fontWeight(FontWeightValue value) {
    return Property.FONT_WEIGHT.value(value);
  }

  protected final StyleDeclaration fontWeight(GlobalKeyword value) {
    return Property.FONT_WEIGHT.value(value);
  }

  // property methods: height

  protected sealed interface HeightValue extends PropertyValue {}

  protected final StyleDeclaration height(HeightValue value) {
    return Property.HEIGHT.value(value);
  }

  protected final StyleDeclaration height(GlobalKeyword value) {
    return Property.HEIGHT.value(value);
  }

  // property methods: line-height

  protected sealed interface LineHeightValue extends PropertyValue {}

  protected final StyleDeclaration lineHeight(int value) {
    return Property.LINE_HEIGHT.value(value);
  }

  protected final StyleDeclaration lineHeight(double value) {
    return Property.LINE_HEIGHT.value(value);
  }

  protected final StyleDeclaration lineHeight(LineHeightValue value) {
    return Property.LINE_HEIGHT.value(value);
  }

  protected final StyleDeclaration lineHeight(GlobalKeyword value) {
    return Property.LINE_HEIGHT.value(value);
  }

  // property methods: margin

  protected sealed interface MarginValue extends PropertyValue {}

  protected final StyleDeclaration margin(MarginValue all) {
    return Property.MARGIN.sides1(all);
  }

  protected final StyleDeclaration margin(MarginValue vertical, MarginValue horizontal) {
    return Property.MARGIN.sides2(vertical, horizontal);
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue horizontal,
      MarginValue bottom) {
    return Property.MARGIN.sides3(top, horizontal, bottom);
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue right, MarginValue bottom,
      MarginValue left) {
    return Property.MARGIN.sides4(top, right, bottom, left);
  }

  protected final StyleDeclaration margin(GlobalKeyword value) {
    return Property.MARGIN.value(value);
  }

  // property methods: tab-size

  protected final StyleDeclaration tabSize(int value) {
    return Property.TAB_SIZE.value(value);
  }

  protected final StyleDeclaration tabSize(Length value) {
    return Property.TAB_SIZE.value(value);
  }

  protected final StyleDeclaration tabSize(GlobalKeyword value) {
    return Property.TAB_SIZE.value(value);
  }

  protected final StyleDeclaration mozTabSize(int value) {
    return Property._MOZ_TAB_SIZE.value(value);
  }

  protected final StyleDeclaration mozTabSize(Length value) {
    return Property._MOZ_TAB_SIZE.value(value);
  }

  protected final StyleDeclaration mozTabSize(GlobalKeyword value) {
    return Property._MOZ_TAB_SIZE.value(value);
  }

  // property methods: text-decoration

  protected sealed interface TextDecorationValue extends PropertyValue {}

  protected final StyleDeclaration textDecoration(GlobalKeyword value) {
    return Property.TEXT_DECORATION.value(value);
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value) {
    return Property.TEXT_DECORATION.value(value);
  }

  protected final StyleDeclaration textDecoration(
      TextDecorationValue value1, TextDecorationValue value2) {
    return Property.TEXT_DECORATION.value(value1, value2);
  }

  protected final StyleDeclaration textDecoration(
      TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3) {
    return Property.TEXT_DECORATION.value(value1, value2, value3);
  }

  protected final StyleDeclaration textDecoration(
      TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3,
      TextDecorationValue value4) {
    return Property.TEXT_DECORATION.value(value1, value2, value3, value4);
  }

  // property methods: text-decoration-color

  protected final StyleDeclaration textDecorationColor(GlobalKeyword value) {
    return Property.TEXT_DECORATION_COLOR.value(value);
  }

  protected final StyleDeclaration textDecorationColor(Color value) {
    return Property.TEXT_DECORATION_COLOR.value(value);
  }

  // property methods: text-decoration-line

  protected sealed interface TextDecorationLineValue extends TextDecorationValue {}

  protected final StyleDeclaration textDecorationLine(GlobalKeyword value) {
    return Property.TEXT_DECORATION_LINE.value(value);
  }

  protected final StyleDeclaration textDecorationLine(NoneKeyword value) {
    return Property.TEXT_DECORATION_LINE.value(value);
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineValue value) {
    return Property.TEXT_DECORATION_LINE.value(value);
  }

  protected final StyleDeclaration textDecorationLine(
      TextDecorationLineValue value1, TextDecorationLineValue value2) {
    return Property.TEXT_DECORATION_LINE.value(value1, value2);
  }

  protected final StyleDeclaration textDecorationLine(
      TextDecorationLineValue value1,
      TextDecorationLineValue value2, TextDecorationLineValue value3) {
    return Property.TEXT_DECORATION_LINE.value(value1, value2, value3);
  }

  // property methods: text-decoration-style

  protected sealed interface TextDecorationStyleValue extends TextDecorationValue {}

  protected final StyleDeclaration textDecorationStyle(TextDecorationStyleValue value) {
    return Property.TEXT_DECORATION_STYLE.value(value);
  }

  protected final StyleDeclaration textDecorationStyle(GlobalKeyword value) {
    return Property.TEXT_DECORATION_STYLE.value(value);
  }

  // property methods: text-decoration-thickness

  protected sealed interface TextDecorationThicknessValue extends TextDecorationValue {}

  protected final StyleDeclaration textDecorationThickness(TextDecorationThicknessValue value) {
    return Property.TEXT_DECORATION_THICKNESS.value(value);
  }

  protected final StyleDeclaration textDecorationThickness(GlobalKeyword value) {
    return Property.TEXT_DECORATION_THICKNESS.value(value);
  }

  // property methods: text-size-adjust

  protected sealed interface TextSizeAdjustValue extends PropertyValue {}

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    return Property._WEBKIT_TEXT_SIZE_ADJUST.value(value);
  }

  protected final StyleDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    return Property._WEBKIT_TEXT_SIZE_ADJUST.value(value);
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