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
import objectos.css.internal.StyleDeclarationString;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.tmpl.AppearanceValue;
import objectos.css.tmpl.AutoKeyword;
import objectos.css.tmpl.BackgroundImageValue;
import objectos.css.tmpl.BlockKeyword;
import objectos.css.tmpl.BorderCollapseValue;
import objectos.css.tmpl.BottomValue;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.ButtonKeyword;
import objectos.css.tmpl.Color;
import objectos.css.tmpl.CounterStyleValue;
import objectos.css.tmpl.DashedKeyword;
import objectos.css.tmpl.DisplayBoxValue;
import objectos.css.tmpl.DisplayInsideValue;
import objectos.css.tmpl.DisplayInternalValue;
import objectos.css.tmpl.DisplayLegacyValue;
import objectos.css.tmpl.DisplayListItemValue;
import objectos.css.tmpl.DisplayOutsideValue;
import objectos.css.tmpl.DisplayValue;
import objectos.css.tmpl.DisplayValue2;
import objectos.css.tmpl.DottedKeyword;
import objectos.css.tmpl.DoubleKeyword;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.FontFeatureSettingsValue;
import objectos.css.tmpl.FontSizeValue;
import objectos.css.tmpl.FontValue;
import objectos.css.tmpl.FontVariationSettingsValue;
import objectos.css.tmpl.FontWeightValue;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.GrooveKeyword;
import objectos.css.tmpl.HeightValue;
import objectos.css.tmpl.InlineKeyword;
import objectos.css.tmpl.InsetKeyword;
import objectos.css.tmpl.Length;
import objectos.css.tmpl.LengthPercentage;
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.ListStyleImageValue;
import objectos.css.tmpl.ListStylePositionValue;
import objectos.css.tmpl.ListStyleTypeValue;
import objectos.css.tmpl.ListStyleValue;
import objectos.css.tmpl.MarginValue;
import objectos.css.tmpl.MediumKeyword;
import objectos.css.tmpl.MenuKeyword;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.OutlineStyleValue;
import objectos.css.tmpl.OutlineValue;
import objectos.css.tmpl.OutsetKeyword;
import objectos.css.tmpl.Percentage;
import objectos.css.tmpl.PositionValue;
import objectos.css.tmpl.ResizeValue;
import objectos.css.tmpl.RidgeKeyword;
import objectos.css.tmpl.SmallKeyword;
import objectos.css.tmpl.SolidKeyword;
import objectos.css.tmpl.StringLiteral;
import objectos.css.tmpl.SubKeyword;
import objectos.css.tmpl.TableKeyword;
import objectos.css.tmpl.TextDecorationLineMultiValue;
import objectos.css.tmpl.TextDecorationLineSingleValue;
import objectos.css.tmpl.TextDecorationStyleValue;
import objectos.css.tmpl.TextDecorationThicknessValue;
import objectos.css.tmpl.TextDecorationValue;
import objectos.css.tmpl.TextIndentValue;
import objectos.css.tmpl.TextSizeAdjustValue;
import objectos.css.tmpl.TextTransformValue;
import objectos.css.tmpl.TextareaKeyword;
import objectos.css.tmpl.TopValue;
import objectos.css.tmpl.VerticalAlignValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final Selector __after = named("::after");

  protected static final Selector __before = named("::before");

  protected static final Selector __placeholder = named("::placeholder");

  protected static final Selector __webkitFileUploadButton = named("::-webkit-file-upload-button");

  protected static final Selector __webkitInnerSpinButton = named("::-webkit-inner-spin-button");

  protected static final Selector __webkitOuterSpinButton = named("::-webkit-outer-spin-button");

  protected static final Selector __webkitSearchDecoration = named("::-webkit-search-decoration");

  protected static final Selector _disabled = named(":disabled");

  protected static final Selector _mozFocusring = named(":-moz-focusring");

  protected static final Selector _mozUiInvalid = named(":-moz-ui-invalid");

  protected static final Selector a = named("a");

  protected static final Selector b = named("b");

  protected static final Selector blockquote = named("blockquote");

  protected static final Selector body = named("body");

  protected static final Selector code = named("code");

  protected static final Selector dd = named("dd");

  protected static final Selector dl = named("dl");

  protected static final Selector fieldset = named("fieldset");

  protected static final Selector figure = named("figure");

  protected static final Selector h1 = named("h1");

  protected static final Selector h2 = named("h2");

  protected static final Selector h3 = named("h3");

  protected static final Selector h4 = named("h4");

  protected static final Selector h5 = named("h5");

  protected static final Selector h6 = named("h6");

  protected static final Selector hr = named("hr");

  protected static final Selector html = named("html");

  protected static final Selector input = named("input");

  protected static final Selector kbd = named("kbd");

  protected static final Selector legend = named("legend");

  protected static final Selector li = named("li");

  protected static final Selector ol = named("ol");

  protected static final Selector optgroup = named("optgroup");

  protected static final Selector p = named("p");

  protected static final Selector pre = named("pre");

  protected static final Selector progress = named("progress");

  protected static final Selector samp = named("samp");

  protected static final Selector select = named("select");

  protected static final Selector strong = named("strong");

  protected static final Selector summary = named("summary");

  protected static final Selector sup = named("sup");

  protected static final Selector ul = named("ul");

  protected static final Selector any = named("*");

  protected static final DoubleKeyword _double = named("double");

  protected static final PositionValue _static = named("static");

  protected static final VerticalAlignValue _super = named("super");

  protected static final PositionValue absolute = named("absolute");

  protected static final Color aqua = named("aqua");

  protected static final CounterStyleValue arabicIndic = named("arabic-indic");

  protected static final CounterStyleValue armenian = named("armenian");

  protected static final AutoKeyword auto = named("auto");

  protected static final VerticalAlignValue baseline = named("baseline");

  protected static final CounterStyleValue bengali = named("bengali");

  protected static final Color black = named("black");

  protected static final TextDecorationLineMultiValue blink = named("blink");

  protected static final BlockKeyword block = named("block");

  protected static final Color blue = named("blue");

  protected static final FontWeightValue bold = named("bold");

  protected static final FontWeightValue bolder = named("bolder");

  protected static final BoxSizingValue borderBox = named("border-box");

  protected static final ResizeValue both = named("both");

  protected static final VerticalAlignValue bottom = named("bottom");

  protected static final ButtonKeyword button = named("button");

  protected static final CounterStyleValue cambodian = named("cambodian");

  protected static final TextTransformValue capitalize = named("capitalize");

  protected static final FontValue caption = named("caption");

  protected static final AppearanceValue checkbox = named("checkbox");

  protected static final CounterStyleValue circle = named("circle");

  protected static final CounterStyleValue cjkDecimal = named("cjk-decimal");

  protected static final CounterStyleValue cjkEarthlyBranch = named("cjk-earthly-branch");

  protected static final CounterStyleValue cjkHeavenlyStem = named("cjk-heavenly-stem");

  protected static final CounterStyleValue cjkIdeographic = named("cjk-ideographic");

  protected static final BorderCollapseValue collapse = named("collapse");

  protected static final BoxSizingValue contentBox = named("content-box");

  protected static final DisplayBoxValue contents = named("contents");

  protected static final Color currentcolor = named("currentcolor");

  protected static final FontFamilyValue cursive = named("cursive");

  protected static final DashedKeyword dashed = named("dashed");

  protected static final CounterStyleValue decimal = named("decimal");

  protected static final CounterStyleValue decimalLeadingZero = named("decimal-leading-zero");

  protected static final CounterStyleValue devanagari = named("devanagari");

  protected static final CounterStyleValue disc = named("disc");

  protected static final CounterStyleValue disclosureClosed = named("disclosure-closed");

  protected static final CounterStyleValue disclosureOpen = named("disclosure-open");

  protected static final DottedKeyword dotted = named("dotted");

  protected static final TextIndentValue eachLine = named("each-line");

  protected static final FontFamilyValue emoji = named("emoji");

  protected static final CounterStyleValue ethiopicNumeric = named("ethiopic-numeric");

  protected static final FontFamilyValue fangsong = named("fangsong");

  protected static final FontFamilyValue fantasy = named("fantasy");

  protected static final HeightValue fitContent = named("fit-content");

  protected static final PositionValue fixed = named("fixed");

  protected static final DisplayInsideValue flex = named("flex");

  protected static final DisplayInsideValue flow = named("flow");

  protected static final DisplayInsideValue flowRoot = named("flow-root");

  protected static final TextDecorationThicknessValue fromFont = named("from-font");

  protected static final Color fuchsia = named("fuchsia");

  protected static final TextTransformValue fullSizeKana = named("full-size-kana");

  protected static final TextTransformValue fullWidth = named("full-width");

  protected static final CounterStyleValue georgian = named("georgian");

  protected static final Color gray = named("gray");

  protected static final Color green = named("green");

  protected static final DisplayInsideValue grid = named("grid");

  protected static final GrooveKeyword groove = named("groove");

  protected static final CounterStyleValue gujarati = named("gujarati");

  protected static final CounterStyleValue gurmukhi = named("gurmukhi");

  protected static final TextIndentValue hanging = named("hanging");

  protected static final CounterStyleValue hebrew = named("hebrew");

  protected static final LineStyle hidden = named("hidden");

  protected static final CounterStyleValue hiragana = named("hiragana");

  protected static final CounterStyleValue hiraganaIroha = named("hiragana-iroha");

  protected static final ResizeValue horizontal = named("horizontal");

  protected static final FontValue icon = named("icon");

  protected static final GlobalKeyword inherit = named("inherit");

  protected static final GlobalKeyword initial = named("initial");

  protected static final InlineKeyword inline = named("inline");

  protected static final DisplayLegacyValue inlineBlock = named("inline-block");

  protected static final DisplayLegacyValue inlineFlex = named("inline-flex");

  protected static final DisplayLegacyValue inlineGrid = named("inline-grid");

  protected static final DisplayLegacyValue inlineTable = named("inline-table");

  protected static final InsetKeyword inset = named("inset");

  protected static final ListStylePositionValue inside = named("inside");

  protected static final CounterStyleValue japaneseFormal = named("japanese-formal");

  protected static final CounterStyleValue japaneseInformal = named("japanese-informal");

  protected static final CounterStyleValue kannada = named("kannada");

  protected static final CounterStyleValue katakana = named("katakana");

  protected static final CounterStyleValue katakanaIroha = named("katakana-iroha");

  protected static final CounterStyleValue khmer = named("khmer");

  protected static final CounterStyleValue koreanHangulFormal = named("korean-hangul-formal");

  protected static final CounterStyleValue koreanHanjaFormal = named("korean-hanja-formal");

  protected static final CounterStyleValue koreanHanjaInformal = named("korean-hanja-informal");

  protected static final CounterStyleValue lao = named("lao");

  protected static final FontSizeValue large = named("large");

  protected static final FontSizeValue larger = named("larger");

  protected static final FontWeightValue lighter = named("lighter");

  protected static final Color lime = named("lime");

  protected static final TextDecorationLineMultiValue lineThrough = named("line-through");

  protected static final DisplayListItemValue listItem = named("list-item");

  protected static final AppearanceValue listbox = named("listbox");

  protected static final CounterStyleValue lowerAlpha = named("lower-alpha");

  protected static final CounterStyleValue lowerArmenian = named("lower-armenian");

  protected static final CounterStyleValue lowerGreek = named("lower-greek");

  protected static final CounterStyleValue lowerLatin = named("lower-latin");

  protected static final CounterStyleValue lowerRoman = named("lower-roman");

  protected static final TextTransformValue lowercase = named("lowercase");

  protected static final CounterStyleValue malayalam = named("malayalam");

  protected static final Color maroon = named("maroon");

  protected static final FontFamilyValue math = named("math");

  protected static final HeightValue maxContent = named("max-content");

  protected static final MediumKeyword medium = named("medium");

  protected static final MenuKeyword menu = named("menu");

  protected static final AppearanceValue menulist = named("menulist");

  protected static final AppearanceValue menulistButton = named("menulist-button");

  protected static final FontValue messageBox = named("message-box");

  protected static final AppearanceValue meter = named("meter");

  protected static final VerticalAlignValue middle = named("middle");

  protected static final HeightValue minContent = named("min-content");

  protected static final CounterStyleValue mongolian = named("mongolian");

  protected static final FontFamilyValue monospace = named("monospace");

  protected static final CounterStyleValue mozArabicIndic = named("-moz-arabic-indic");

  protected static final CounterStyleValue mozBengali = named("-moz-bengali");

  protected static final CounterStyleValue mozCjkEarthlyBranch = named("-moz-cjk-earthly-branch");

  protected static final CounterStyleValue mozCjkHeavenlyStem = named("-moz-cjk-heavenly-stem");

  protected static final CounterStyleValue mozDevanagari = named("-moz-devanagari");

  protected static final CounterStyleValue mozGujarati = named("-moz-gujarati");

  protected static final CounterStyleValue mozGurmukhi = named("-moz-gurmukhi");

  protected static final CounterStyleValue mozKannada = named("-moz-kannada");

  protected static final CounterStyleValue mozKhmer = named("-moz-khmer");

  protected static final CounterStyleValue mozLao = named("-moz-lao");

  protected static final CounterStyleValue mozMalayalam = named("-moz-malayalam");

  protected static final CounterStyleValue mozMyanmar = named("-moz-myanmar");

  protected static final CounterStyleValue mozOriya = named("-moz-oriya");

  protected static final CounterStyleValue mozPersian = named("-moz-persian");

  protected static final CounterStyleValue mozTamil = named("-moz-tamil");

  protected static final CounterStyleValue mozTelugu = named("-moz-telugu");

  protected static final CounterStyleValue mozThai = named("-moz-thai");

  protected static final CounterStyleValue myanmar = named("myanmar");

  protected static final Color navy = named("navy");

  protected static final NoneKeyword none = named("none");

  protected static final NormalKeyword normal = named("normal");

  protected static final Color olive = named("olive");

  protected static final CounterStyleValue oriya = named("oriya");

  protected static final OutsetKeyword outset = named("outset");

  protected static final ListStylePositionValue outside = named("outside");

  protected static final TextDecorationLineMultiValue overline = named("overline");

  protected static final CounterStyleValue persian = named("persian");

  protected static final AppearanceValue progressBar = named("progress-bar");

  protected static final Color purple = named("purple");

  protected static final AppearanceValue pushButton = named("push-button");

  protected static final AppearanceValue radio = named("radio");

  protected static final Color red = named("red");

  protected static final PositionValue relative = named("relative");

  protected static final RidgeKeyword ridge = named("ridge");

  protected static final DisplayInsideValue ruby = named("ruby");

  protected static final DisplayInternalValue rubyBase = named("ruby-base");

  protected static final DisplayInternalValue rubyBaseContainer = named("ruby-base-container");

  protected static final DisplayInternalValue rubyText = named("ruby-text");

  protected static final DisplayInternalValue rubyTextContainer = named("ruby-text-container");

  protected static final DisplayOutsideValue runIn = named("runIn");

  protected static final FontFamilyValue sansSerif = named("sans-serif");

  protected static final AppearanceValue searchfield = named("searchfield");

  protected static final BorderCollapseValue separate = named("separate");

  protected static final FontFamilyValue serif = named("serif");

  protected static final Color silver = named("silver");

  protected static final CounterStyleValue simpChineseFormal = named("simp-chinese-formal");

  protected static final CounterStyleValue simpChineseInformal = named("simp-chinese-informal");

  protected static final AppearanceValue sliderHorizontal = named("slider-horizontal");

  protected static final SmallKeyword small = named("small");

  protected static final FontValue smallCaption = named("small-caption");

  protected static final FontSizeValue smaller = named("smaller");

  protected static final SolidKeyword solid = named("solid");

  protected static final CounterStyleValue square = named("square");

  protected static final AppearanceValue squareButton = named("square-button");

  protected static final FontValue statusBar = named("status-bar");

  protected static final PositionValue sticky = named("sticky");

  protected static final SubKeyword sub = named("sub");

  protected static final FontFamilyValue systemUi = named("system-ui");

  protected static final TableKeyword table = named("table");

  protected static final DisplayInternalValue tableCaption = named("table-caption");

  protected static final DisplayInternalValue tableCell = named("table-cell");

  protected static final DisplayInternalValue tableColumn = named("table-column");

  protected static final DisplayInternalValue tableColumnGroup = named("table-column-group");

  protected static final DisplayInternalValue tableFooterGroup = named("table-footer-group");

  protected static final DisplayInternalValue tableHeaderGroup = named("table-header-group");

  protected static final DisplayInternalValue tableRow = named("table-row");

  protected static final DisplayInternalValue tableRowGroup = named("table-row-group");

  protected static final CounterStyleValue tamil = named("tamil");

  protected static final Color teal = named("teal");

  protected static final CounterStyleValue telugu = named("telugu");

  protected static final VerticalAlignValue textBottom = named("text-bottom");

  protected static final VerticalAlignValue textTop = named("text-top");

  protected static final TextareaKeyword textarea = named("textarea");

  protected static final AppearanceValue textfield = named("textfield");

  protected static final CounterStyleValue thai = named("thai");

  protected static final LineWidth thick = named("thick");

  protected static final LineWidth thin = named("thin");

  protected static final CounterStyleValue tibetan = named("tibetan");

  protected static final VerticalAlignValue top = named("top");

  protected static final CounterStyleValue tradChineseFormal = named("trad-chinese-formal");

  protected static final CounterStyleValue tradChineseInformal = named("trad-chinese-informal");

  protected static final Color transparent = named("transparent");

  protected static final FontFamilyValue uiMonospace = named("ui-monospace");

  protected static final FontFamilyValue uiRounded = named("ui-rounded");

  protected static final FontFamilyValue uiSansSerif = named("ui-sans-serif");

  protected static final FontFamilyValue uiSerif = named("ui-serif");

  protected static final TextDecorationLineMultiValue underline = named("underline");

  protected static final GlobalKeyword unset = named("unset");

  protected static final CounterStyleValue upperAlpha = named("upper-alpha");

  protected static final CounterStyleValue upperArmenian = named("upper-armenian");

  protected static final CounterStyleValue upperLatin = named("upper-latin");

  protected static final CounterStyleValue upperRoman = named("upper-roman");

  protected static final TextTransformValue uppercase = named("uppercase");

  protected static final ResizeValue vertical = named("vertical");

  protected static final TextDecorationStyleValue wavy = named("wavy");

  protected static final Color white = named("white");

  protected static final FontSizeValue xLarge = named("x-large");

  protected static final FontSizeValue xSmall = named("x-small");

  protected static final FontSizeValue xxLarge = named("xx-large");

  protected static final FontSizeValue xxSmall = named("xx-small");

  protected static final FontSizeValue xxxLarge = named("xxx-large");

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

  protected final StyleDeclaration appearance(GlobalKeyword value) {
    return new StyleDeclaration1(Property.APPEARANCE, value.self());
  }

  protected final StyleDeclaration appearance(AppearanceValue value) {
    return new StyleDeclaration1(Property.APPEARANCE, value.self());
  }

  protected final StyleDeclaration backgroundColor(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BACKGROUND_COLOR, value.self());
  }

  protected final StyleDeclaration backgroundColor(Color value) {
    return new StyleDeclaration1(Property.BACKGROUND_COLOR, value.self());
  }

  protected final StyleDeclaration backgroundImage(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BACKGROUND_IMAGE, value.self());
  }

  protected final StyleDeclaration backgroundImage(BackgroundImageValue value) {
    return new StyleDeclaration1(Property.BACKGROUND_IMAGE, value.self());
  }

  protected final StyleDeclaration borderBottomWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_BOTTOM_WIDTH, value.self());
  }

  protected final StyleDeclaration borderBottomWidth(LineWidth value) {
    return new StyleDeclaration1(Property.BORDER_BOTTOM_WIDTH, value.self());
  }

  protected final StyleDeclaration borderCollapse(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_COLLAPSE, value.self());
  }

  protected final StyleDeclaration borderCollapse(BorderCollapseValue value) {
    return new StyleDeclaration1(Property.BORDER_COLLAPSE, value.self());
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

  protected final StyleDeclaration borderLeftWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_LEFT_WIDTH, value.self());
  }

  protected final StyleDeclaration borderLeftWidth(LineWidth value) {
    return new StyleDeclaration1(Property.BORDER_LEFT_WIDTH, value.self());
  }

  protected final StyleDeclaration borderRightWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_RIGHT_WIDTH, value.self());
  }

  protected final StyleDeclaration borderRightWidth(LineWidth value) {
    return new StyleDeclaration1(Property.BORDER_RIGHT_WIDTH, value.self());
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

  protected final StyleDeclaration borderTopWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BORDER_TOP_WIDTH, value.self());
  }

  protected final StyleDeclaration borderTopWidth(LineWidth value) {
    return new StyleDeclaration1(Property.BORDER_TOP_WIDTH, value.self());
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

  protected final StyleDeclaration bottom(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BOTTOM, value.self());
  }

  protected final StyleDeclaration bottom(BottomValue value) {
    return new StyleDeclaration1(Property.BOTTOM, value.self());
  }

  protected final StyleDeclaration boxShadow(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BOX_SHADOW, value.self());
  }

  protected final StyleDeclaration boxShadow(NoneKeyword value) {
    return new StyleDeclaration1(Property.BOX_SHADOW, value.self());
  }

  protected final StyleDeclaration boxSizing(GlobalKeyword value) {
    return new StyleDeclaration1(Property.BOX_SIZING, value.self());
  }

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    return new StyleDeclaration1(Property.BOX_SIZING, value.self());
  }

  protected final StyleDeclaration color(GlobalKeyword value) {
    return new StyleDeclaration1(Property.COLOR, value.self());
  }

  protected final StyleDeclaration color(Color value) {
    return new StyleDeclaration1(Property.COLOR, value.self());
  }

  protected final StyleDeclaration display(GlobalKeyword value) {
    return new StyleDeclaration1(Property.DISPLAY, value.self());
  }

  protected final StyleDeclaration display(DisplayValue value) {
    return new StyleDeclaration1(Property.DISPLAY, value.self());
  }

  protected final StyleDeclaration display(DisplayValue value, DisplayValue2 value2) {
    return new StyleDeclaration2(Property.DISPLAY, value.self(), value2.self());
  }

  protected final StyleDeclaration font(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT, value.self());
  }

  protected final StyleDeclaration font(FontValue value) {
    return new StyleDeclaration1(Property.FONT, value.self());
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

  protected final StyleDeclaration fontSize(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_SIZE, value.self());
  }

  protected final StyleDeclaration fontSize(FontSizeValue value) {
    return new StyleDeclaration1(Property.FONT_SIZE, value.self());
  }

  protected final StyleDeclaration fontVariationSettings(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_VARIATION_SETTINGS, value.self());
  }

  protected final StyleDeclaration fontVariationSettings(FontVariationSettingsValue value) {
    return new StyleDeclaration1(Property.FONT_VARIATION_SETTINGS, value.self());
  }

  protected final StyleDeclaration fontWeight(int value) {
    return new StyleDeclarationInt(Property.FONT_WEIGHT, value);
  }

  protected final StyleDeclaration fontWeight(GlobalKeyword value) {
    return new StyleDeclaration1(Property.FONT_WEIGHT, value.self());
  }

  protected final StyleDeclaration fontWeight(FontWeightValue value) {
    return new StyleDeclaration1(Property.FONT_WEIGHT, value.self());
  }

  protected final StyleDeclaration height(GlobalKeyword value) {
    return new StyleDeclaration1(Property.HEIGHT, value.self());
  }

  protected final StyleDeclaration height(HeightValue value) {
    return new StyleDeclaration1(Property.HEIGHT, value.self());
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

  protected final StyleDeclaration listStyle(GlobalKeyword value) {
    return new StyleDeclaration1(Property.LIST_STYLE, value.self());
  }

  protected final StyleDeclaration listStyle(ListStyleValue value) {
    return new StyleDeclaration1(Property.LIST_STYLE, value.self());
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2) {
    return new StyleDeclaration2(Property.LIST_STYLE, value1.self(), value2.self());
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2, ListStyleValue value3) {
    return new StyleDeclaration3(Property.LIST_STYLE, value1.self(), value2.self(), value3.self());
  }

  protected final StyleDeclaration listStyleImage(GlobalKeyword value) {
    return new StyleDeclaration1(Property.LIST_STYLE_IMAGE, value.self());
  }

  protected final StyleDeclaration listStyleImage(ListStyleImageValue value) {
    return new StyleDeclaration1(Property.LIST_STYLE_IMAGE, value.self());
  }

  protected final StyleDeclaration listStylePosition(GlobalKeyword value) {
    return new StyleDeclaration1(Property.LIST_STYLE_POSITION, value.self());
  }

  protected final StyleDeclaration listStylePosition(ListStylePositionValue value) {
    return new StyleDeclaration1(Property.LIST_STYLE_POSITION, value.self());
  }

  protected final StyleDeclaration listStyleType(GlobalKeyword value) {
    return new StyleDeclaration1(Property.LIST_STYLE_TYPE, value.self());
  }

  protected final StyleDeclaration listStyleType(ListStyleTypeValue value) {
    return new StyleDeclaration1(Property.LIST_STYLE_TYPE, value.self());
  }

  protected final StyleDeclaration listStyleType(String value) {
    Objects.requireNonNull(value, "value == null");
    return new StyleDeclarationString(Property.LIST_STYLE_TYPE, value);
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

  protected final StyleDeclaration mozAppearance(GlobalKeyword value) {
    return new StyleDeclaration1(Property._MOZ_APPEARANCE, value.self());
  }

  protected final StyleDeclaration mozAppearance(AppearanceValue value) {
    return new StyleDeclaration1(Property._MOZ_APPEARANCE, value.self());
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

  protected final StyleDeclaration opacity(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OPACITY, value.self());
  }

  protected final StyleDeclaration opacity(Percentage value) {
    return new StyleDeclaration1(Property.OPACITY, value.self());
  }

  protected final StyleDeclaration opacity(double value) {
    return new StyleDeclarationDouble(Property.OPACITY, value);
  }

  protected final StyleDeclaration opacity(int value) {
    return new StyleDeclarationInt(Property.OPACITY, value);
  }

  protected final StyleDeclaration outline(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OUTLINE, value.self());
  }

  protected final StyleDeclaration outline(OutlineValue value) {
    return new StyleDeclaration1(Property.OUTLINE, value.self());
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2) {
    return new StyleDeclaration2(Property.OUTLINE, value1.self(), value2.self());
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2, OutlineValue value3) {
    return new StyleDeclaration3(Property.OUTLINE, value1.self(), value2.self(), value3.self());
  }

  protected final StyleDeclaration outlineColor(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OUTLINE_COLOR, value.self());
  }

  protected final StyleDeclaration outlineColor(Color value) {
    return new StyleDeclaration1(Property.OUTLINE_COLOR, value.self());
  }

  protected final StyleDeclaration outlineOffset(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OUTLINE_OFFSET, value.self());
  }

  protected final StyleDeclaration outlineOffset(Length value) {
    return new StyleDeclaration1(Property.OUTLINE_OFFSET, value.self());
  }

  protected final StyleDeclaration outlineStyle(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OUTLINE_STYLE, value.self());
  }

  protected final StyleDeclaration outlineStyle(OutlineStyleValue value) {
    return new StyleDeclaration1(Property.OUTLINE_STYLE, value.self());
  }

  protected final StyleDeclaration outlineWidth(GlobalKeyword value) {
    return new StyleDeclaration1(Property.OUTLINE_WIDTH, value.self());
  }

  protected final StyleDeclaration outlineWidth(LineWidth value) {
    return new StyleDeclaration1(Property.OUTLINE_WIDTH, value.self());
  }

  protected final StyleDeclaration padding(GlobalKeyword value) {
    return new StyleDeclaration1(Property.PADDING, value.self());
  }

  protected final StyleDeclaration padding(LengthPercentage all) {
    return new StyleDeclaration1(Property.PADDING, all.self());
  }

  protected final StyleDeclaration padding(LengthPercentage vertical, LengthPercentage horizontal) {
    return new StyleDeclaration2(Property.PADDING, vertical.self(), horizontal.self());
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage horizontal, LengthPercentage bottom) {
    return new StyleDeclaration3(Property.PADDING, top.self(), horizontal.self(), bottom.self());
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage right, LengthPercentage bottom, LengthPercentage left) {
    return new StyleDeclaration4(Property.PADDING, top.self(), right.self(), bottom.self(), left.self());
  }

  protected final StyleDeclaration paddingBottom(GlobalKeyword value) {
    return new StyleDeclaration1(Property.PADDING_BOTTOM, value.self());
  }

  protected final StyleDeclaration paddingBottom(LengthPercentage value) {
    return new StyleDeclaration1(Property.PADDING_BOTTOM, value.self());
  }

  protected final StyleDeclaration paddingLeft(GlobalKeyword value) {
    return new StyleDeclaration1(Property.PADDING_LEFT, value.self());
  }

  protected final StyleDeclaration paddingLeft(LengthPercentage value) {
    return new StyleDeclaration1(Property.PADDING_LEFT, value.self());
  }

  protected final StyleDeclaration paddingRight(GlobalKeyword value) {
    return new StyleDeclaration1(Property.PADDING_RIGHT, value.self());
  }

  protected final StyleDeclaration paddingRight(LengthPercentage value) {
    return new StyleDeclaration1(Property.PADDING_RIGHT, value.self());
  }

  protected final StyleDeclaration paddingTop(GlobalKeyword value) {
    return new StyleDeclaration1(Property.PADDING_TOP, value.self());
  }

  protected final StyleDeclaration paddingTop(LengthPercentage value) {
    return new StyleDeclaration1(Property.PADDING_TOP, value.self());
  }

  protected final StyleDeclaration position(GlobalKeyword value) {
    return new StyleDeclaration1(Property.POSITION, value.self());
  }

  protected final StyleDeclaration position(PositionValue value) {
    return new StyleDeclaration1(Property.POSITION, value.self());
  }

  protected final StyleDeclaration resize(GlobalKeyword value) {
    return new StyleDeclaration1(Property.RESIZE, value.self());
  }

  protected final StyleDeclaration resize(ResizeValue value) {
    return new StyleDeclaration1(Property.RESIZE, value.self());
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

  protected final StyleDeclaration textDecoration(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION, value.self());
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION, value.self());
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2) {
    return new StyleDeclaration2(Property.TEXT_DECORATION, value1.self(), value2.self());
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3) {
    return new StyleDeclaration3(Property.TEXT_DECORATION, value1.self(), value2.self(), value3.self());
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3, TextDecorationValue value4) {
    return new StyleDeclaration4(Property.TEXT_DECORATION, value1.self(), value2.self(), value3.self(), value4.self());
  }

  protected final StyleDeclaration textDecorationColor(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_COLOR, value.self());
  }

  protected final StyleDeclaration textDecorationColor(Color value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_COLOR, value.self());
  }

  protected final StyleDeclaration textDecorationLine(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_LINE, value.self());
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineSingleValue value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_LINE, value.self());
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2) {
    return new StyleDeclaration2(Property.TEXT_DECORATION_LINE, value1.self(), value2.self());
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2, TextDecorationLineMultiValue value3) {
    return new StyleDeclaration3(Property.TEXT_DECORATION_LINE, value1.self(), value2.self(), value3.self());
  }

  protected final StyleDeclaration textDecorationStyle(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_STYLE, value.self());
  }

  protected final StyleDeclaration textDecorationStyle(TextDecorationStyleValue value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_STYLE, value.self());
  }

  protected final StyleDeclaration textDecorationThickness(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_THICKNESS, value.self());
  }

  protected final StyleDeclaration textDecorationThickness(TextDecorationThicknessValue value) {
    return new StyleDeclaration1(Property.TEXT_DECORATION_THICKNESS, value.self());
  }

  protected final StyleDeclaration textIndent(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_INDENT, value.self());
  }

  protected final StyleDeclaration textIndent(LengthPercentage value) {
    return new StyleDeclaration1(Property.TEXT_INDENT, value.self());
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2) {
    return new StyleDeclaration2(Property.TEXT_INDENT, value1.self(), value2.self());
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2, TextIndentValue value3) {
    return new StyleDeclaration3(Property.TEXT_INDENT, value1.self(), value2.self(), value3.self());
  }

  protected final StyleDeclaration textTransform(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TEXT_TRANSFORM, value.self());
  }

  protected final StyleDeclaration textTransform(TextTransformValue value) {
    return new StyleDeclaration1(Property.TEXT_TRANSFORM, value.self());
  }

  protected final StyleDeclaration top(GlobalKeyword value) {
    return new StyleDeclaration1(Property.TOP, value.self());
  }

  protected final StyleDeclaration top(TopValue value) {
    return new StyleDeclaration1(Property.TOP, value.self());
  }

  protected final StyleDeclaration verticalAlign(GlobalKeyword value) {
    return new StyleDeclaration1(Property.VERTICAL_ALIGN, value.self());
  }

  protected final StyleDeclaration verticalAlign(VerticalAlignValue value) {
    return new StyleDeclaration1(Property.VERTICAL_ALIGN, value.self());
  }

  protected final StyleDeclaration webkitAppearance(GlobalKeyword value) {
    return new StyleDeclaration1(Property._WEBKIT_APPEARANCE, value.self());
  }

  protected final StyleDeclaration webkitAppearance(AppearanceValue value) {
    return new StyleDeclaration1(Property._WEBKIT_APPEARANCE, value.self());
  }

  protected final StyleDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    return new StyleDeclaration1(Property._WEBKIT_TEXT_SIZE_ADJUST, value.self());
  }

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    return new StyleDeclaration1(Property._WEBKIT_TEXT_SIZE_ADJUST, value.self());
  }
}
