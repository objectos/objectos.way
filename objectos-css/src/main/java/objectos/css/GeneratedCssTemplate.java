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

import objectos.css.internal.InternalLength;
import objectos.css.internal.InternalPercentage;
import objectos.css.internal.InternalStringLiteral;
import objectos.css.internal.NamedElement;
import objectos.css.internal.Property;
import objectos.css.internal.StyleDeclaration1;
import objectos.css.internal.StyleDeclaration2;
import objectos.css.internal.StyleDeclaration3;
import objectos.css.internal.StyleDeclaration4;
import objectos.css.internal.StyleDeclarationDouble;
import objectos.css.internal.StyleDeclarationInt;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.tmpl.AutoKeyword;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.Color;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.FontFeatureSettingsValue;
import objectos.css.tmpl.FontVariationSettingsValue;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.Length;
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.MarginValue;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.Percentage;
import objectos.css.tmpl.StringLiteral;
import objectos.css.tmpl.TextSizeAdjustValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final Selector __after = named("::after");

  protected static final Selector __before = named("::before");

  protected static final Selector a = named("a");

  protected static final Selector b = named("b");

  protected static final Selector body = named("body");

  protected static final Selector code = named("code");

  protected static final Selector h1 = named("h1");

  protected static final Selector h2 = named("h2");

  protected static final Selector h3 = named("h3");

  protected static final Selector h4 = named("h4");

  protected static final Selector h5 = named("h5");

  protected static final Selector h6 = named("h6");

  protected static final Selector hr = named("hr");

  protected static final Selector html = named("html");

  protected static final Selector kbd = named("kbd");

  protected static final Selector li = named("li");

  protected static final Selector pre = named("pre");

  protected static final Selector samp = named("samp");

  protected static final Selector small = named("small");

  protected static final Selector strong = named("strong");

  protected static final Selector sub = named("sub");

  protected static final Selector sup = named("sup");

  protected static final Selector ul = named("ul");

  protected static final Selector any = named("*");

  protected static final LineStyle _double = named("double");

  protected static final Color aqua = named("aqua");

  protected static final AutoKeyword auto = named("auto");

  protected static final Color black = named("black");

  protected static final Color blue = named("blue");

  protected static final BoxSizingValue borderBox = named("border-box");

  protected static final BoxSizingValue contentBox = named("content-box");

  protected static final Color currentcolor = named("currentcolor");

  protected static final FontFamilyValue cursive = named("cursive");

  protected static final LineStyle dashed = named("dashed");

  protected static final LineStyle dotted = named("dotted");

  protected static final FontFamilyValue emoji = named("emoji");

  protected static final FontFamilyValue fangsong = named("fangsong");

  protected static final FontFamilyValue fantasy = named("fantasy");

  protected static final Color fuchsia = named("fuchsia");

  protected static final Color gray = named("gray");

  protected static final Color green = named("green");

  protected static final LineStyle groove = named("groove");

  protected static final LineStyle hidden = named("hidden");

  protected static final GlobalKeyword inherit = named("inherit");

  protected static final GlobalKeyword initial = named("initial");

  protected static final LineStyle inset = named("inset");

  protected static final Color lime = named("lime");

  protected static final Color maroon = named("maroon");

  protected static final FontFamilyValue math = named("math");

  protected static final LineWidth medium = named("medium");

  protected static final FontFamilyValue monospace = named("monospace");

  protected static final Color navy = named("navy");

  protected static final NoneKeyword none = named("none");

  protected static final NormalKeyword normal = named("normal");

  protected static final Color olive = named("olive");

  protected static final LineStyle outset = named("outset");

  protected static final Color purple = named("purple");

  protected static final Color red = named("red");

  protected static final LineStyle ridge = named("ridge");

  protected static final FontFamilyValue sansSerif = named("sans-serif");

  protected static final FontFamilyValue serif = named("serif");

  protected static final Color silver = named("silver");

  protected static final LineStyle solid = named("solid");

  protected static final FontFamilyValue systemUi = named("system-ui");

  protected static final Color teal = named("teal");

  protected static final LineWidth thick = named("thick");

  protected static final LineWidth thin = named("thin");

  protected static final Color transparent = named("transparent");

  protected static final FontFamilyValue uiMonospace = named("ui-monospace");

  protected static final FontFamilyValue uiRounded = named("ui-rounded");

  protected static final FontFamilyValue uiSansSerif = named("ui-sans-serif");

  protected static final FontFamilyValue uiSerif = named("ui-serif");

  protected static final GlobalKeyword unset = named("unset");

  protected static final Color white = named("white");

  protected static final Color yellow = named("yellow");

  private static NamedElement named(String name) {
    return new NamedElement(name);
  }

  protected final Length ch(double value) {
    return InternalLength.of("ch", value);
  }

  protected final Length ch(int value) {
    return InternalLength.of("ch", value);
  }

  protected final Length cm(double value) {
    return InternalLength.of("cm", value);
  }

  protected final Length cm(int value) {
    return InternalLength.of("cm", value);
  }

  protected final Length em(double value) {
    return InternalLength.of("em", value);
  }

  protected final Length em(int value) {
    return InternalLength.of("em", value);
  }

  protected final Length ex(double value) {
    return InternalLength.of("ex", value);
  }

  protected final Length ex(int value) {
    return InternalLength.of("ex", value);
  }

  protected final Length in(double value) {
    return InternalLength.of("in", value);
  }

  protected final Length in(int value) {
    return InternalLength.of("in", value);
  }

  protected final Length mm(double value) {
    return InternalLength.of("mm", value);
  }

  protected final Length mm(int value) {
    return InternalLength.of("mm", value);
  }

  protected final Length pc(double value) {
    return InternalLength.of("pc", value);
  }

  protected final Length pc(int value) {
    return InternalLength.of("pc", value);
  }

  protected final Length pt(double value) {
    return InternalLength.of("pt", value);
  }

  protected final Length pt(int value) {
    return InternalLength.of("pt", value);
  }

  protected final Length px(double value) {
    return InternalLength.of("px", value);
  }

  protected final Length px(int value) {
    return InternalLength.of("px", value);
  }

  protected final Length q(double value) {
    return InternalLength.of("q", value);
  }

  protected final Length q(int value) {
    return InternalLength.of("q", value);
  }

  protected final Length rem(double value) {
    return InternalLength.of("rem", value);
  }

  protected final Length rem(int value) {
    return InternalLength.of("rem", value);
  }

  protected final Length vh(double value) {
    return InternalLength.of("vh", value);
  }

  protected final Length vh(int value) {
    return InternalLength.of("vh", value);
  }

  protected final Length vmax(double value) {
    return InternalLength.of("vmax", value);
  }

  protected final Length vmax(int value) {
    return InternalLength.of("vmax", value);
  }

  protected final Length vmin(double value) {
    return InternalLength.of("vmin", value);
  }

  protected final Length vmin(int value) {
    return InternalLength.of("vmin", value);
  }

  protected final Length vw(double value) {
    return InternalLength.of("vw", value);
  }

  protected final Length vw(int value) {
    return InternalLength.of("vw", value);
  }

  protected final Percentage pct(double value) {
    return InternalPercentage.of(value);
  }

  protected final Percentage pct(int value) {
    return InternalPercentage.of(value);
  }

  protected final StringLiteral l(String value) {
    return InternalStringLiteral.of(value);
  }

  protected final StyleDeclaration borderColor(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_COLOR, value.self());
  }

  protected final StyleDeclaration borderColor(Color all) {
    return new StyleDeclaration1(Property.BORDER_COLOR, all.self());
  }

  protected final StyleDeclaration borderColor(Color vertical, Color horizontal) {
    return new StyleDeclaration2(Property.BORDER_COLOR, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration borderColor(Color top, Color horizontal, Color bottom) {
    return new StyleDeclaration3(Property.BORDER_COLOR, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration borderColor(Color top, Color right, Color bottom, Color left) {
    return new StyleDeclaration4(Property.BORDER_COLOR, top.self(), right.self(), bottom.self(), left.self());
  }

  protected final StyleDeclaration borderStyle(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_STYLE, value.self());
  }

  protected final StyleDeclaration borderStyle(LineStyle all) {
    return new StyleDeclaration1(Property.BORDER_STYLE, all.self());
  }

  protected final StyleDeclaration borderStyle(LineStyle vertical, LineStyle horizontal) {
    return new StyleDeclaration2(Property.BORDER_STYLE, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle horizontal, LineStyle bottom) {
    return new StyleDeclaration3(Property.BORDER_STYLE, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle right, LineStyle bottom, LineStyle left) {
    return new StyleDeclaration4(Property.BORDER_STYLE, top.self(), right.self(), bottom.self(), left.self());
  }

  protected final StyleDeclaration borderWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_WIDTH, value.self());
  }

  protected final StyleDeclaration borderWidth(LineWidth all) {
    return new StyleDeclaration1(Property.BORDER_WIDTH, all.self());
  }

  protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
    return new StyleDeclaration2(Property.BORDER_WIDTH, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth horizontal, LineWidth bottom) {
    return new StyleDeclaration3(Property.BORDER_WIDTH, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom, LineWidth left) {
    return new StyleDeclaration4(Property.BORDER_WIDTH, top.self(), right.self(), bottom.self(), left.self());
  }

  protected final StyleDeclaration boxSizing(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BOX_SIZING, value.self());
  }

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    return new StyleDeclaration1(Property.BOX_SIZING, value.self());
  }

  protected final StyleDeclaration fontFamily(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_FAMILY, value.self());
  }

  protected abstract StyleDeclaration fontFamily(FontFamilyValue... values);

  protected final StyleDeclaration fontFeatureSettings(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_FEATURE_SETTINGS, value.self());
  }

  protected final StyleDeclaration fontFeatureSettings(FontFeatureSettingsValue value) {
    return new StyleDeclaration1(Property.FONT_FEATURE_SETTINGS, value.self());
  }

  protected final StyleDeclaration fontVariationSettings(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_VARIATION_SETTINGS, value.self());
  }

  protected final StyleDeclaration fontVariationSettings(FontVariationSettingsValue value) {
    return new StyleDeclaration1(Property.FONT_VARIATION_SETTINGS, value.self());
  }

  protected final StyleDeclaration lineHeight(double value) {
    return new StyleDeclarationDouble(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration lineHeight(int value) {
    return new StyleDeclarationInt(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration lineHeight(GlobalKeyword value) {
    return new StyleDeclaration1(Property.LINE_HEIGHT, value.self());
  }

  protected final StyleDeclaration lineHeight(LineHeightValue value) {
    return new StyleDeclaration1(Property.LINE_HEIGHT, value.self());
  }

  protected final StyleDeclaration margin(GlobalKeyword value) {
    return new StyleDeclaration1(Property.MARGIN, value.self());
  }

  protected final StyleDeclaration margin(MarginValue all) {
    return new StyleDeclaration1(Property.MARGIN, all.self());
  }

  protected final StyleDeclaration margin(MarginValue vertical, MarginValue horizontal) {
    return new StyleDeclaration2(Property.MARGIN, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue horizontal, MarginValue bottom) {
    return new StyleDeclaration3(Property.MARGIN, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue right, MarginValue bottom, MarginValue left) {
    return new StyleDeclaration4(Property.MARGIN, top.self(), right.self(), bottom.self(), left.self());
  }

  protected final StyleDeclaration mozTabSize(int value) {
    return new StyleDeclarationInt(Property._MOZ_TAB_SIZE, value);
  }

  protected final StyleDeclaration mozTabSize(GlobalKeyword value) {
    return new StyleDeclaration1(Property._MOZ_TAB_SIZE, value.self());
  }

  protected final StyleDeclaration mozTabSize(Length value) {
    return new StyleDeclaration1(Property._MOZ_TAB_SIZE, value.self());
  }

  protected final StyleDeclaration tabSize(int value) {
    return new StyleDeclarationInt(Property.TAB_SIZE, value);
  }

  protected final StyleDeclaration tabSize(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TAB_SIZE, value.self());
  }

  protected final StyleDeclaration tabSize(Length value) {
    return new StyleDeclaration1(Property.TAB_SIZE, value.self());
  }

  protected final StyleDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    return new StyleDeclaration1(Property._WEBKIT_TEXT_SIZE_ADJUST, value.self());
  }

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    return new StyleDeclaration1(Property._WEBKIT_TEXT_SIZE_ADJUST, value.self());
  }
}
