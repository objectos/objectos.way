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
package objectos.css.internal;

import objectos.css.om.PropertyValue;
import objectos.css.om.PseudoClassSelector;
import objectos.css.om.PseudoElementSelector;
import objectos.css.om.Selector;
import objectos.css.om.StyleDeclaration;
import objectos.css.om.TypeSelector;
import objectos.css.tmpl.AppearanceValue;
import objectos.css.tmpl.AutoKeyword;
import objectos.css.tmpl.BackgroundImageValue;
import objectos.css.tmpl.BlockKeyword;
import objectos.css.tmpl.BorderCollapseValue;
import objectos.css.tmpl.BottomValue;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.ButtonKeyword;
import objectos.css.tmpl.CenterKeyword;
import objectos.css.tmpl.ColorValue;
import objectos.css.tmpl.CounterStyleValue;
import objectos.css.tmpl.CursorValue;
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
import objectos.css.tmpl.EndKeyword;
import objectos.css.tmpl.FitContentKeyword;
import objectos.css.tmpl.FlexDirectionValue;
import objectos.css.tmpl.FlexEndKeyword;
import objectos.css.tmpl.FlexStartKeyword;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.FontFeatureSettingsValue;
import objectos.css.tmpl.FontSizeValue;
import objectos.css.tmpl.FontStyleValue;
import objectos.css.tmpl.FontValue;
import objectos.css.tmpl.FontVariationSettingsValue;
import objectos.css.tmpl.FontWeightValue;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.GrooveKeyword;
import objectos.css.tmpl.HeightOrWidthValue;
import objectos.css.tmpl.InlineKeyword;
import objectos.css.tmpl.InsetKeyword;
import objectos.css.tmpl.JustifyContentPosition;
import objectos.css.tmpl.JustifyContentValue;
import objectos.css.tmpl.LeftKeyword;
import objectos.css.tmpl.Length;
import objectos.css.tmpl.LengthPercentage;
import objectos.css.tmpl.LetterSpacingValue;
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.ListStyleImageValue;
import objectos.css.tmpl.ListStylePositionValue;
import objectos.css.tmpl.ListStyleTypeValue;
import objectos.css.tmpl.ListStyleValue;
import objectos.css.tmpl.MarginValue;
import objectos.css.tmpl.MaxContentKeyword;
import objectos.css.tmpl.MaxHeightOrWidthValue;
import objectos.css.tmpl.MediumKeyword;
import objectos.css.tmpl.MenuKeyword;
import objectos.css.tmpl.MinContentKeyword;
import objectos.css.tmpl.MinHeightOrWidthValue;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.OutlineStyleValue;
import objectos.css.tmpl.OutlineValue;
import objectos.css.tmpl.OutsetKeyword;
import objectos.css.tmpl.OverflowPosition;
import objectos.css.tmpl.Percentage;
import objectos.css.tmpl.PositionValue;
import objectos.css.tmpl.ProgressKeyword;
import objectos.css.tmpl.ResizeValue;
import objectos.css.tmpl.RidgeKeyword;
import objectos.css.tmpl.RightKeyword;
import objectos.css.tmpl.SmallKeyword;
import objectos.css.tmpl.SolidKeyword;
import objectos.css.tmpl.StartKeyword;
import objectos.css.tmpl.SubKeyword;
import objectos.css.tmpl.TableKeyword;
import objectos.css.tmpl.TextAlignValue;
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
import objectos.lang.Check;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final PseudoElementSelector __after = StandardPseudoElementSelector.__after;

  protected static final PseudoElementSelector __before = StandardPseudoElementSelector.__before;

  protected static final PseudoElementSelector __placeholder = StandardPseudoElementSelector.__placeholder;

  protected static final PseudoElementSelector __webkitFileUploadButton = StandardPseudoElementSelector.__webkitFileUploadButton;

  protected static final PseudoElementSelector __webkitInnerSpinButton = StandardPseudoElementSelector.__webkitInnerSpinButton;

  protected static final PseudoElementSelector __webkitOuterSpinButton = StandardPseudoElementSelector.__webkitOuterSpinButton;

  protected static final PseudoElementSelector __webkitSearchDecoration = StandardPseudoElementSelector.__webkitSearchDecoration;

  protected static final PseudoClassSelector _disabled = StandardPseudoClassSelector._disabled;

  protected static final PseudoClassSelector _hover = StandardPseudoClassSelector._hover;

  protected static final PseudoClassSelector _mozFocusring = StandardPseudoClassSelector._mozFocusring;

  protected static final PseudoClassSelector _mozUiInvalid = StandardPseudoClassSelector._mozUiInvalid;

  protected static final TypeSelector a = StandardTypeSelector.a;

  protected static final TypeSelector audio = StandardTypeSelector.audio;

  protected static final TypeSelector b = StandardTypeSelector.b;

  protected static final TypeSelector blockquote = StandardTypeSelector.blockquote;

  protected static final TypeSelector body = StandardTypeSelector.body;

  protected static final TypeSelector canvas = StandardTypeSelector.canvas;

  protected static final TypeSelector code = StandardTypeSelector.code;

  protected static final TypeSelector dd = StandardTypeSelector.dd;

  protected static final TypeSelector dl = StandardTypeSelector.dl;

  protected static final TypeSelector embed = StandardTypeSelector.embed;

  protected static final TypeSelector fieldset = StandardTypeSelector.fieldset;

  protected static final TypeSelector figure = StandardTypeSelector.figure;

  protected static final TypeSelector form = StandardTypeSelector.form;

  protected static final TypeSelector h1 = StandardTypeSelector.h1;

  protected static final TypeSelector h2 = StandardTypeSelector.h2;

  protected static final TypeSelector h3 = StandardTypeSelector.h3;

  protected static final TypeSelector h4 = StandardTypeSelector.h4;

  protected static final TypeSelector h5 = StandardTypeSelector.h5;

  protected static final TypeSelector h6 = StandardTypeSelector.h6;

  protected static final TypeSelector hr = StandardTypeSelector.hr;

  protected static final TypeSelector html = StandardTypeSelector.html;

  protected static final TypeSelector iframe = StandardTypeSelector.iframe;

  protected static final TypeSelector img = StandardTypeSelector.img;

  protected static final TypeSelector input = StandardTypeSelector.input;

  protected static final TypeSelector kbd = StandardTypeSelector.kbd;

  protected static final TypeSelector label = StandardTypeSelector.label;

  protected static final TypeSelector legend = StandardTypeSelector.legend;

  protected static final TypeSelector li = StandardTypeSelector.li;

  protected static final TypeSelector object = StandardTypeSelector.object;

  protected static final TypeSelector ol = StandardTypeSelector.ol;

  protected static final TypeSelector optgroup = StandardTypeSelector.optgroup;

  protected static final TypeSelector p = StandardTypeSelector.p;

  protected static final TypeSelector pre = StandardTypeSelector.pre;

  protected static final TypeSelector samp = StandardTypeSelector.samp;

  protected static final TypeSelector select = StandardTypeSelector.select;

  protected static final TypeSelector strong = StandardTypeSelector.strong;

  protected static final TypeSelector summary = StandardTypeSelector.summary;

  protected static final TypeSelector sup = StandardTypeSelector.sup;

  protected static final TypeSelector svg = StandardTypeSelector.svg;

  protected static final TypeSelector ul = StandardTypeSelector.ul;

  protected static final TypeSelector video = StandardTypeSelector.video;

  protected static final Selector any = StandardName.any;

  protected static final ColorValue aqua = StandardName.aqua;

  protected static final ColorValue black = StandardName.black;

  protected static final ColorValue blue = StandardName.blue;

  protected static final ColorValue currentcolor = StandardName.currentcolor;

  protected static final ColorValue fuchsia = StandardName.fuchsia;

  protected static final ColorValue gray = StandardName.gray;

  protected static final ColorValue green = StandardName.green;

  protected static final ColorValue lime = StandardName.lime;

  protected static final ColorValue maroon = StandardName.maroon;

  protected static final ColorValue navy = StandardName.navy;

  protected static final ColorValue olive = StandardName.olive;

  protected static final ColorValue purple = StandardName.purple;

  protected static final ColorValue red = StandardName.red;

  protected static final ColorValue silver = StandardName.silver;

  protected static final ColorValue teal = StandardName.teal;

  protected static final ColorValue transparent = StandardName.transparent;

  protected static final ColorValue white = StandardName.white;

  protected static final ColorValue yellow = StandardName.yellow;

  protected static final CursorValue _default = StandardName._default;

  protected static final DoubleKeyword _double = StandardName._double;

  protected static final PositionValue _static = StandardName._static;

  protected static final VerticalAlignValue _super = StandardName._super;

  protected static final PositionValue absolute = StandardName.absolute;

  protected static final CursorValue alias = StandardName.alias;

  protected static final CursorValue allScroll = StandardName.allScroll;

  protected static final CounterStyleValue arabicIndic = StandardName.arabicIndic;

  protected static final CounterStyleValue armenian = StandardName.armenian;

  protected static final AutoKeyword auto = StandardName.auto;

  protected static final VerticalAlignValue baseline = StandardName.baseline;

  protected static final CounterStyleValue bengali = StandardName.bengali;

  protected static final TextDecorationLineMultiValue blink = StandardName.blink;

  protected static final BlockKeyword block = StandardName.block;

  protected static final FontWeightValue bold = StandardName.bold;

  protected static final FontWeightValue bolder = StandardName.bolder;

  protected static final BoxSizingValue borderBox = StandardName.borderBox;

  protected static final ResizeValue both = StandardName.both;

  protected static final VerticalAlignValue bottom = StandardName.bottom;

  protected static final ButtonKeyword button = StandardName.button;

  protected static final CounterStyleValue cambodian = StandardName.cambodian;

  protected static final TextTransformValue capitalize = StandardName.capitalize;

  protected static final FontValue caption = StandardName.caption;

  protected static final CursorValue cell = StandardName.cell;

  protected static final CenterKeyword center = StandardName.center;

  protected static final AppearanceValue checkbox = StandardName.checkbox;

  protected static final CounterStyleValue circle = StandardName.circle;

  protected static final CounterStyleValue cjkDecimal = StandardName.cjkDecimal;

  protected static final CounterStyleValue cjkEarthlyBranch = StandardName.cjkEarthlyBranch;

  protected static final CounterStyleValue cjkHeavenlyStem = StandardName.cjkHeavenlyStem;

  protected static final CounterStyleValue cjkIdeographic = StandardName.cjkIdeographic;

  protected static final CursorValue colResize = StandardName.colResize;

  protected static final BorderCollapseValue collapse = StandardName.collapse;

  protected static final FlexDirectionValue column = StandardName.column;

  protected static final FlexDirectionValue columnReverse = StandardName.columnReverse;

  protected static final BoxSizingValue contentBox = StandardName.contentBox;

  protected static final DisplayBoxValue contents = StandardName.contents;

  protected static final CursorValue contextMenu = StandardName.contextMenu;

  protected static final CursorValue copy = StandardName.copy;

  protected static final CursorValue crosshair = StandardName.crosshair;

  protected static final FontFamilyValue cursive = StandardName.cursive;

  protected static final DashedKeyword dashed = StandardName.dashed;

  protected static final CounterStyleValue decimal = StandardName.decimal;

  protected static final CounterStyleValue decimalLeadingZero = StandardName.decimalLeadingZero;

  protected static final CounterStyleValue devanagari = StandardName.devanagari;

  protected static final CounterStyleValue disc = StandardName.disc;

  protected static final CounterStyleValue disclosureClosed = StandardName.disclosureClosed;

  protected static final CounterStyleValue disclosureOpen = StandardName.disclosureOpen;

  protected static final DottedKeyword dotted = StandardName.dotted;

  protected static final CursorValue eResize = StandardName.eResize;

  protected static final TextIndentValue eachLine = StandardName.eachLine;

  protected static final FontFamilyValue emoji = StandardName.emoji;

  protected static final EndKeyword end = StandardName.end;

  protected static final CounterStyleValue ethiopicNumeric = StandardName.ethiopicNumeric;

  protected static final CursorValue ewResize = StandardName.ewResize;

  protected static final FontFamilyValue fangsong = StandardName.fangsong;

  protected static final FontFamilyValue fantasy = StandardName.fantasy;

  protected static final FitContentKeyword fitContent = StandardName.fitContent;

  protected static final PositionValue fixed = StandardName.fixed;

  protected static final DisplayInsideValue flex = StandardName.flex;

  protected static final FlexEndKeyword flexEnd = StandardName.flexEnd;

  protected static final FlexStartKeyword flexStart = StandardName.flexStart;

  protected static final DisplayInsideValue flow = StandardName.flow;

  protected static final DisplayInsideValue flowRoot = StandardName.flowRoot;

  protected static final TextDecorationThicknessValue fromFont = StandardName.fromFont;

  protected static final TextTransformValue fullSizeKana = StandardName.fullSizeKana;

  protected static final TextTransformValue fullWidth = StandardName.fullWidth;

  protected static final CounterStyleValue georgian = StandardName.georgian;

  protected static final CursorValue grab = StandardName.grab;

  protected static final CursorValue grabbing = StandardName.grabbing;

  protected static final DisplayInsideValue grid = StandardName.grid;

  protected static final GrooveKeyword groove = StandardName.groove;

  protected static final CounterStyleValue gujarati = StandardName.gujarati;

  protected static final CounterStyleValue gurmukhi = StandardName.gurmukhi;

  protected static final TextIndentValue hanging = StandardName.hanging;

  protected static final CounterStyleValue hebrew = StandardName.hebrew;

  protected static final CursorValue help = StandardName.help;

  protected static final LineStyle hidden = StandardName.hidden;

  protected static final CounterStyleValue hiragana = StandardName.hiragana;

  protected static final CounterStyleValue hiraganaIroha = StandardName.hiraganaIroha;

  protected static final ResizeValue horizontal = StandardName.horizontal;

  protected static final FontValue icon = StandardName.icon;

  protected static final GlobalKeyword inherit = StandardName.inherit;

  protected static final GlobalKeyword initial = StandardName.initial;

  protected static final InlineKeyword inline = StandardName.inline;

  protected static final DisplayLegacyValue inlineBlock = StandardName.inlineBlock;

  protected static final DisplayLegacyValue inlineFlex = StandardName.inlineFlex;

  protected static final DisplayLegacyValue inlineGrid = StandardName.inlineGrid;

  protected static final DisplayLegacyValue inlineTable = StandardName.inlineTable;

  protected static final InsetKeyword inset = StandardName.inset;

  protected static final ListStylePositionValue inside = StandardName.inside;

  protected static final FontStyleValue italic = StandardName.italic;

  protected static final CounterStyleValue japaneseFormal = StandardName.japaneseFormal;

  protected static final CounterStyleValue japaneseInformal = StandardName.japaneseInformal;

  protected static final TextAlignValue justify = StandardName.justify;

  protected static final CounterStyleValue kannada = StandardName.kannada;

  protected static final CounterStyleValue katakana = StandardName.katakana;

  protected static final CounterStyleValue katakanaIroha = StandardName.katakanaIroha;

  protected static final CounterStyleValue khmer = StandardName.khmer;

  protected static final CounterStyleValue koreanHangulFormal = StandardName.koreanHangulFormal;

  protected static final CounterStyleValue koreanHanjaFormal = StandardName.koreanHanjaFormal;

  protected static final CounterStyleValue koreanHanjaInformal = StandardName.koreanHanjaInformal;

  protected static final CounterStyleValue lao = StandardName.lao;

  protected static final FontSizeValue large = StandardName.large;

  protected static final FontSizeValue larger = StandardName.larger;

  protected static final LeftKeyword left = StandardName.left;

  protected static final FontWeightValue lighter = StandardName.lighter;

  protected static final TextDecorationLineMultiValue lineThrough = StandardName.lineThrough;

  protected static final DisplayListItemValue listItem = StandardName.listItem;

  protected static final AppearanceValue listbox = StandardName.listbox;

  protected static final CounterStyleValue lowerAlpha = StandardName.lowerAlpha;

  protected static final CounterStyleValue lowerArmenian = StandardName.lowerArmenian;

  protected static final CounterStyleValue lowerGreek = StandardName.lowerGreek;

  protected static final CounterStyleValue lowerLatin = StandardName.lowerLatin;

  protected static final CounterStyleValue lowerRoman = StandardName.lowerRoman;

  protected static final TextTransformValue lowercase = StandardName.lowercase;

  protected static final CounterStyleValue malayalam = StandardName.malayalam;

  protected static final TextAlignValue matchParent = StandardName.matchParent;

  protected static final FontFamilyValue math = StandardName.math;

  protected static final MaxContentKeyword maxContent = StandardName.maxContent;

  protected static final MediumKeyword medium = StandardName.medium;

  protected static final MenuKeyword menu = StandardName.menu;

  protected static final AppearanceValue menulist = StandardName.menulist;

  protected static final AppearanceValue menulistButton = StandardName.menulistButton;

  protected static final FontValue messageBox = StandardName.messageBox;

  protected static final AppearanceValue meter = StandardName.meter;

  protected static final VerticalAlignValue middle = StandardName.middle;

  protected static final MinContentKeyword minContent = StandardName.minContent;

  protected static final CounterStyleValue mongolian = StandardName.mongolian;

  protected static final FontFamilyValue monospace = StandardName.monospace;

  protected static final CursorValue move = StandardName.move;

  protected static final CounterStyleValue mozArabicIndic = StandardName.mozArabicIndic;

  protected static final CounterStyleValue mozBengali = StandardName.mozBengali;

  protected static final CounterStyleValue mozCjkEarthlyBranch = StandardName.mozCjkEarthlyBranch;

  protected static final CounterStyleValue mozCjkHeavenlyStem = StandardName.mozCjkHeavenlyStem;

  protected static final CounterStyleValue mozDevanagari = StandardName.mozDevanagari;

  protected static final CounterStyleValue mozGujarati = StandardName.mozGujarati;

  protected static final CounterStyleValue mozGurmukhi = StandardName.mozGurmukhi;

  protected static final CounterStyleValue mozKannada = StandardName.mozKannada;

  protected static final CounterStyleValue mozKhmer = StandardName.mozKhmer;

  protected static final CounterStyleValue mozLao = StandardName.mozLao;

  protected static final CounterStyleValue mozMalayalam = StandardName.mozMalayalam;

  protected static final CounterStyleValue mozMyanmar = StandardName.mozMyanmar;

  protected static final CounterStyleValue mozOriya = StandardName.mozOriya;

  protected static final CounterStyleValue mozPersian = StandardName.mozPersian;

  protected static final CounterStyleValue mozTamil = StandardName.mozTamil;

  protected static final CounterStyleValue mozTelugu = StandardName.mozTelugu;

  protected static final CounterStyleValue mozThai = StandardName.mozThai;

  protected static final CounterStyleValue myanmar = StandardName.myanmar;

  protected static final CursorValue nResize = StandardName.nResize;

  protected static final CursorValue neResize = StandardName.neResize;

  protected static final CursorValue neswResize = StandardName.neswResize;

  protected static final CursorValue noDrop = StandardName.noDrop;

  protected static final NoneKeyword none = StandardName.none;

  protected static final NormalKeyword normal = StandardName.normal;

  protected static final CursorValue notAllowed = StandardName.notAllowed;

  protected static final CursorValue nsResize = StandardName.nsResize;

  protected static final CursorValue nwResize = StandardName.nwResize;

  protected static final CursorValue nwseResize = StandardName.nwseResize;

  protected static final FontStyleValue oblique = StandardName.oblique;

  protected static final CounterStyleValue oriya = StandardName.oriya;

  protected static final OutsetKeyword outset = StandardName.outset;

  protected static final ListStylePositionValue outside = StandardName.outside;

  protected static final TextDecorationLineMultiValue overline = StandardName.overline;

  protected static final CounterStyleValue persian = StandardName.persian;

  protected static final CursorValue pointer = StandardName.pointer;

  protected static final ProgressKeyword progress = StandardName.progress;

  protected static final AppearanceValue progressBar = StandardName.progressBar;

  protected static final AppearanceValue pushButton = StandardName.pushButton;

  protected static final AppearanceValue radio = StandardName.radio;

  protected static final PositionValue relative = StandardName.relative;

  protected static final RidgeKeyword ridge = StandardName.ridge;

  protected static final RightKeyword right = StandardName.right;

  protected static final FlexDirectionValue row = StandardName.row;

  protected static final CursorValue rowResize = StandardName.rowResize;

  protected static final FlexDirectionValue rowReverse = StandardName.rowReverse;

  protected static final DisplayInsideValue ruby = StandardName.ruby;

  protected static final DisplayInternalValue rubyBase = StandardName.rubyBase;

  protected static final DisplayInternalValue rubyBaseContainer = StandardName.rubyBaseContainer;

  protected static final DisplayInternalValue rubyText = StandardName.rubyText;

  protected static final DisplayInternalValue rubyTextContainer = StandardName.rubyTextContainer;

  protected static final DisplayOutsideValue runIn = StandardName.runIn;

  protected static final CursorValue sResize = StandardName.sResize;

  protected static final OverflowPosition safe = StandardName.safe;

  protected static final FontFamilyValue sansSerif = StandardName.sansSerif;

  protected static final CursorValue seResize = StandardName.seResize;

  protected static final AppearanceValue searchfield = StandardName.searchfield;

  protected static final BorderCollapseValue separate = StandardName.separate;

  protected static final FontFamilyValue serif = StandardName.serif;

  protected static final CounterStyleValue simpChineseFormal = StandardName.simpChineseFormal;

  protected static final CounterStyleValue simpChineseInformal = StandardName.simpChineseInformal;

  protected static final AppearanceValue sliderHorizontal = StandardName.sliderHorizontal;

  protected static final SmallKeyword small = StandardName.small;

  protected static final FontValue smallCaption = StandardName.smallCaption;

  protected static final FontSizeValue smaller = StandardName.smaller;

  protected static final SolidKeyword solid = StandardName.solid;

  protected static final JustifyContentValue spaceAround = StandardName.spaceAround;

  protected static final JustifyContentValue spaceBetween = StandardName.spaceBetween;

  protected static final JustifyContentValue spaceEvenly = StandardName.spaceEvenly;

  protected static final CounterStyleValue square = StandardName.square;

  protected static final AppearanceValue squareButton = StandardName.squareButton;

  protected static final StartKeyword start = StandardName.start;

  protected static final FontValue statusBar = StandardName.statusBar;

  protected static final PositionValue sticky = StandardName.sticky;

  protected static final JustifyContentValue stretch = StandardName.stretch;

  protected static final SubKeyword sub = StandardName.sub;

  protected static final CursorValue swResize = StandardName.swResize;

  protected static final FontFamilyValue systemUi = StandardName.systemUi;

  protected static final TableKeyword table = StandardName.table;

  protected static final DisplayInternalValue tableCaption = StandardName.tableCaption;

  protected static final DisplayInternalValue tableCell = StandardName.tableCell;

  protected static final DisplayInternalValue tableColumn = StandardName.tableColumn;

  protected static final DisplayInternalValue tableColumnGroup = StandardName.tableColumnGroup;

  protected static final DisplayInternalValue tableFooterGroup = StandardName.tableFooterGroup;

  protected static final DisplayInternalValue tableHeaderGroup = StandardName.tableHeaderGroup;

  protected static final DisplayInternalValue tableRow = StandardName.tableRow;

  protected static final DisplayInternalValue tableRowGroup = StandardName.tableRowGroup;

  protected static final CounterStyleValue tamil = StandardName.tamil;

  protected static final CounterStyleValue telugu = StandardName.telugu;

  protected static final CursorValue text = StandardName.text;

  protected static final VerticalAlignValue textBottom = StandardName.textBottom;

  protected static final VerticalAlignValue textTop = StandardName.textTop;

  protected static final TextareaKeyword textarea = StandardName.textarea;

  protected static final AppearanceValue textfield = StandardName.textfield;

  protected static final CounterStyleValue thai = StandardName.thai;

  protected static final LineWidth thick = StandardName.thick;

  protected static final LineWidth thin = StandardName.thin;

  protected static final CounterStyleValue tibetan = StandardName.tibetan;

  protected static final VerticalAlignValue top = StandardName.top;

  protected static final CounterStyleValue tradChineseFormal = StandardName.tradChineseFormal;

  protected static final CounterStyleValue tradChineseInformal = StandardName.tradChineseInformal;

  protected static final FontFamilyValue uiMonospace = StandardName.uiMonospace;

  protected static final FontFamilyValue uiRounded = StandardName.uiRounded;

  protected static final FontFamilyValue uiSansSerif = StandardName.uiSansSerif;

  protected static final FontFamilyValue uiSerif = StandardName.uiSerif;

  protected static final TextDecorationLineMultiValue underline = StandardName.underline;

  protected static final OverflowPosition unsafe = StandardName.unsafe;

  protected static final GlobalKeyword unset = StandardName.unset;

  protected static final CounterStyleValue upperAlpha = StandardName.upperAlpha;

  protected static final CounterStyleValue upperArmenian = StandardName.upperArmenian;

  protected static final CounterStyleValue upperLatin = StandardName.upperLatin;

  protected static final CounterStyleValue upperRoman = StandardName.upperRoman;

  protected static final TextTransformValue uppercase = StandardName.uppercase;

  protected static final ResizeValue vertical = StandardName.vertical;

  protected static final CursorValue verticalText = StandardName.verticalText;

  protected static final CursorValue wResize = StandardName.wResize;

  protected static final CursorValue wait = StandardName.wait;

  protected static final TextDecorationStyleValue wavy = StandardName.wavy;

  protected static final FontSizeValue xLarge = StandardName.xLarge;

  protected static final FontSizeValue xSmall = StandardName.xSmall;

  protected static final FontSizeValue xxLarge = StandardName.xxLarge;

  protected static final FontSizeValue xxSmall = StandardName.xxSmall;

  protected static final FontSizeValue xxxLarge = StandardName.xxxLarge;

  protected static final CursorValue zoomIn = StandardName.zoomIn;

  protected static final CursorValue zoomOut = StandardName.zoomOut;

  protected final Length ch(double value) {
    return length(value, LengthUnit.CH);
  }

  protected final Length ch(int value) {
    return length(value, LengthUnit.CH);
  }

  protected final Length cm(double value) {
    return length(value, LengthUnit.CM);
  }

  protected final Length cm(int value) {
    return length(value, LengthUnit.CM);
  }

  protected final Length em(double value) {
    return length(value, LengthUnit.EM);
  }

  protected final Length em(int value) {
    return length(value, LengthUnit.EM);
  }

  protected final Length ex(double value) {
    return length(value, LengthUnit.EX);
  }

  protected final Length ex(int value) {
    return length(value, LengthUnit.EX);
  }

  protected final Length in(double value) {
    return length(value, LengthUnit.IN);
  }

  protected final Length in(int value) {
    return length(value, LengthUnit.IN);
  }

  protected final Length mm(double value) {
    return length(value, LengthUnit.MM);
  }

  protected final Length mm(int value) {
    return length(value, LengthUnit.MM);
  }

  protected final Length pc(double value) {
    return length(value, LengthUnit.PC);
  }

  protected final Length pc(int value) {
    return length(value, LengthUnit.PC);
  }

  protected final Length pt(double value) {
    return length(value, LengthUnit.PT);
  }

  protected final Length pt(int value) {
    return length(value, LengthUnit.PT);
  }

  protected final Length px(double value) {
    return length(value, LengthUnit.PX);
  }

  protected final Length px(int value) {
    return length(value, LengthUnit.PX);
  }

  protected final Length q(double value) {
    return length(value, LengthUnit.Q);
  }

  protected final Length q(int value) {
    return length(value, LengthUnit.Q);
  }

  protected final Length rem(double value) {
    return length(value, LengthUnit.REM);
  }

  protected final Length rem(int value) {
    return length(value, LengthUnit.REM);
  }

  protected final Length vh(double value) {
    return length(value, LengthUnit.VH);
  }

  protected final Length vh(int value) {
    return length(value, LengthUnit.VH);
  }

  protected final Length vmax(double value) {
    return length(value, LengthUnit.VMAX);
  }

  protected final Length vmax(int value) {
    return length(value, LengthUnit.VMAX);
  }

  protected final Length vmin(double value) {
    return length(value, LengthUnit.VMIN);
  }

  protected final Length vmin(int value) {
    return length(value, LengthUnit.VMIN);
  }

  protected final Length vw(double value) {
    return length(value, LengthUnit.VW);
  }

  protected final Length vw(int value) {
    return length(value, LengthUnit.VW);
  }

  abstract Length length(double value, LengthUnit unit);

  abstract Length length(int value, LengthUnit unit);

  protected final StyleDeclaration appearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.APPEARANCE, value);
  }

  protected final StyleDeclaration appearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.APPEARANCE, value);
  }

  protected final StyleDeclaration backgroundColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BACKGROUND_COLOR, value);
  }

  protected final StyleDeclaration backgroundColor(ColorValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BACKGROUND_COLOR, value);
  }

  protected final StyleDeclaration backgroundImage(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BACKGROUND_IMAGE, value);
  }

  protected final StyleDeclaration backgroundImage(BackgroundImageValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BACKGROUND_IMAGE, value);
  }

  protected final StyleDeclaration borderBottomWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_BOTTOM_WIDTH, value);
  }

  protected final StyleDeclaration borderBottomWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_BOTTOM_WIDTH, value);
  }

  protected final StyleDeclaration borderCollapse(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_COLLAPSE, value);
  }

  protected final StyleDeclaration borderCollapse(BorderCollapseValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_COLLAPSE, value);
  }

  protected final StyleDeclaration borderColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_COLOR, value);
  }

  protected final StyleDeclaration borderColor(ColorValue all) {
    Check.notNull(all, "all == null");
    return declaration(Property.BORDER_COLOR, all);
  }

  protected final StyleDeclaration borderColor(ColorValue vertical, ColorValue horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    return declaration(Property.BORDER_COLOR, vertical, horizontal);
  }

  protected final StyleDeclaration borderColor(ColorValue top, ColorValue horizontal, ColorValue bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    return declaration(Property.BORDER_COLOR, top, horizontal, bottom);
  }

  protected final StyleDeclaration borderColor(ColorValue top, ColorValue right, ColorValue bottom, ColorValue left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    return declaration(Property.BORDER_COLOR, top, right, bottom, left);
  }

  protected final StyleDeclaration borderLeftWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_LEFT_WIDTH, value);
  }

  protected final StyleDeclaration borderLeftWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_LEFT_WIDTH, value);
  }

  protected final StyleDeclaration borderRightWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_RIGHT_WIDTH, value);
  }

  protected final StyleDeclaration borderRightWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_RIGHT_WIDTH, value);
  }

  protected final StyleDeclaration borderStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_STYLE, value);
  }

  protected final StyleDeclaration borderStyle(LineStyle all) {
    Check.notNull(all, "all == null");
    return declaration(Property.BORDER_STYLE, all);
  }

  protected final StyleDeclaration borderStyle(LineStyle vertical, LineStyle horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    return declaration(Property.BORDER_STYLE, vertical, horizontal);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle horizontal, LineStyle bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    return declaration(Property.BORDER_STYLE, top, horizontal, bottom);
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle right, LineStyle bottom, LineStyle left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    return declaration(Property.BORDER_STYLE, top, right, bottom, left);
  }

  protected final StyleDeclaration borderTopWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_TOP_WIDTH, value);
  }

  protected final StyleDeclaration borderTopWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_TOP_WIDTH, value);
  }

  protected final StyleDeclaration borderWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BORDER_WIDTH, value);
  }

  protected final StyleDeclaration borderWidth(LineWidth all) {
    Check.notNull(all, "all == null");
    return declaration(Property.BORDER_WIDTH, all);
  }

  protected final StyleDeclaration borderWidth(LineWidth vertical, LineWidth horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    return declaration(Property.BORDER_WIDTH, vertical, horizontal);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth horizontal, LineWidth bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    return declaration(Property.BORDER_WIDTH, top, horizontal, bottom);
  }

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth right, LineWidth bottom, LineWidth left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    return declaration(Property.BORDER_WIDTH, top, right, bottom, left);
  }

  protected final StyleDeclaration bottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOTTOM, value);
  }

  protected final StyleDeclaration bottom(BottomValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOTTOM, value);
  }

  protected final StyleDeclaration boxShadow(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOX_SHADOW, value);
  }

  protected final StyleDeclaration boxShadow(NoneKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOX_SHADOW, value);
  }

  protected final StyleDeclaration boxSizing(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOX_SIZING, value);
  }

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.BOX_SIZING, value);
  }

  protected final StyleDeclaration color(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.COLOR, value);
  }

  protected final StyleDeclaration color(ColorValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.COLOR, value);
  }

  protected final StyleDeclaration cursor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.CURSOR, value);
  }

  protected final StyleDeclaration cursor(CursorValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.CURSOR, value);
  }

  protected final StyleDeclaration display(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.DISPLAY, value);
  }

  protected final StyleDeclaration display(DisplayValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.DISPLAY, value);
  }

  protected final StyleDeclaration display(DisplayValue value, DisplayValue2 value2) {
    Check.notNull(value, "value == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.DISPLAY, value, value2);
  }

  protected final StyleDeclaration flexDirection(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FLEX_DIRECTION, value);
  }

  protected final StyleDeclaration flexDirection(FlexDirectionValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FLEX_DIRECTION, value);
  }

  protected final StyleDeclaration font(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT, value);
  }

  protected final StyleDeclaration font(FontValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT, value);
  }

  protected final StyleDeclaration fontFamily(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_FAMILY, value);
  }

  protected abstract StyleDeclaration fontFamily(FontFamilyValue... values);

  protected final StyleDeclaration fontFeatureSettings(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_FEATURE_SETTINGS, value);
  }

  protected final StyleDeclaration fontFeatureSettings(FontFeatureSettingsValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_FEATURE_SETTINGS, value);
  }

  protected final StyleDeclaration fontSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_SIZE, value);
  }

  protected final StyleDeclaration fontSize(FontSizeValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_SIZE, value);
  }

  protected final StyleDeclaration fontStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_STYLE, value);
  }

  protected final StyleDeclaration fontStyle(FontStyleValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_STYLE, value);
  }

  protected final StyleDeclaration fontVariationSettings(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_VARIATION_SETTINGS, value);
  }

  protected final StyleDeclaration fontVariationSettings(FontVariationSettingsValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_VARIATION_SETTINGS, value);
  }

  protected final StyleDeclaration fontWeight(int value) {
    return declaration(Property.FONT_WEIGHT, value);
  }

  protected final StyleDeclaration fontWeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_WEIGHT, value);
  }

  protected final StyleDeclaration fontWeight(FontWeightValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.FONT_WEIGHT, value);
  }

  protected final StyleDeclaration height(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.HEIGHT, value);
  }

  protected final StyleDeclaration height(HeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.HEIGHT, value);
  }

  protected final StyleDeclaration justifyContent(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.JUSTIFY_CONTENT, value);
  }

  protected final StyleDeclaration justifyContent(JustifyContentValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.JUSTIFY_CONTENT, value);
  }

  protected final StyleDeclaration justifyContent(OverflowPosition safeOrUnsafe, JustifyContentPosition position) {
    Check.notNull(safeOrUnsafe, "safeOrUnsafe == null");
    Check.notNull(position, "position == null");
    return declaration(Property.JUSTIFY_CONTENT, safeOrUnsafe, position);
  }

  protected final StyleDeclaration letterSpacing(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LETTER_SPACING, value);
  }

  protected final StyleDeclaration letterSpacing(LetterSpacingValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LETTER_SPACING, value);
  }

  protected final StyleDeclaration lineHeight(double value) {
    return declaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration lineHeight(int value) {
    return declaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration lineHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration lineHeight(LineHeightValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LINE_HEIGHT, value);
  }

  protected final StyleDeclaration listStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE, value);
  }

  protected final StyleDeclaration listStyle(ListStyleValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE, value);
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.LIST_STYLE, value1, value2);
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2, ListStyleValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    return declaration(Property.LIST_STYLE, value1, value2, value3);
  }

  protected final StyleDeclaration listStyleImage(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_IMAGE, value);
  }

  protected final StyleDeclaration listStyleImage(ListStyleImageValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_IMAGE, value);
  }

  protected final StyleDeclaration listStylePosition(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_POSITION, value);
  }

  protected final StyleDeclaration listStylePosition(ListStylePositionValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_POSITION, value);
  }

  protected final StyleDeclaration listStyleType(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_TYPE, value);
  }

  protected final StyleDeclaration listStyleType(ListStyleTypeValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_TYPE, value);
  }

  protected final StyleDeclaration listStyleType(String value) {
    Check.notNull(value, "value == null");
    return declaration(Property.LIST_STYLE_TYPE, value);
  }

  protected final StyleDeclaration margin(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN, value);
  }

  protected final StyleDeclaration margin(MarginValue all) {
    Check.notNull(all, "all == null");
    return declaration(Property.MARGIN, all);
  }

  protected final StyleDeclaration margin(MarginValue vertical, MarginValue horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    return declaration(Property.MARGIN, vertical, horizontal);
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue horizontal, MarginValue bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    return declaration(Property.MARGIN, top, horizontal, bottom);
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue right, MarginValue bottom, MarginValue left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    return declaration(Property.MARGIN, top, right, bottom, left);
  }

  protected final StyleDeclaration marginBottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_BOTTOM, value);
  }

  protected final StyleDeclaration marginBottom(MarginValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_BOTTOM, value);
  }

  protected final StyleDeclaration marginLeft(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_LEFT, value);
  }

  protected final StyleDeclaration marginLeft(MarginValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_LEFT, value);
  }

  protected final StyleDeclaration marginRight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_RIGHT, value);
  }

  protected final StyleDeclaration marginRight(MarginValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_RIGHT, value);
  }

  protected final StyleDeclaration marginTop(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_TOP, value);
  }

  protected final StyleDeclaration marginTop(MarginValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MARGIN_TOP, value);
  }

  protected final StyleDeclaration maxHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MAX_HEIGHT, value);
  }

  protected final StyleDeclaration maxHeight(MaxHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MAX_HEIGHT, value);
  }

  protected final StyleDeclaration maxWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MAX_WIDTH, value);
  }

  protected final StyleDeclaration maxWidth(MaxHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MAX_WIDTH, value);
  }

  protected final StyleDeclaration minHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_HEIGHT, value);
  }

  protected final StyleDeclaration minHeight(MinHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_HEIGHT, value);
  }

  protected final StyleDeclaration minHeight(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_HEIGHT, value);
  }

  protected final StyleDeclaration minWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_WIDTH, value);
  }

  protected final StyleDeclaration minWidth(MinHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_WIDTH, value);
  }

  protected final StyleDeclaration minWidth(Percentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.MIN_WIDTH, value);
  }

  protected final StyleDeclaration mozAppearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property._MOZ_APPEARANCE, value);
  }

  protected final StyleDeclaration mozAppearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property._MOZ_APPEARANCE, value);
  }

  protected final StyleDeclaration mozTabSize(int value) {
    return declaration(Property._MOZ_TAB_SIZE, value);
  }

  protected final StyleDeclaration mozTabSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property._MOZ_TAB_SIZE, value);
  }

  protected final StyleDeclaration mozTabSize(Length value) {
    Check.notNull(value, "value == null");
    return declaration(Property._MOZ_TAB_SIZE, value);
  }

  protected final StyleDeclaration opacity(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OPACITY, value);
  }

  protected final StyleDeclaration opacity(Percentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OPACITY, value);
  }

  protected final StyleDeclaration opacity(double value) {
    return declaration(Property.OPACITY, value);
  }

  protected final StyleDeclaration opacity(int value) {
    return declaration(Property.OPACITY, value);
  }

  protected final StyleDeclaration outline(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE, value);
  }

  protected final StyleDeclaration outline(OutlineValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE, value);
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.OUTLINE, value1, value2);
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2, OutlineValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    return declaration(Property.OUTLINE, value1, value2, value3);
  }

  protected final StyleDeclaration outlineColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_COLOR, value);
  }

  protected final StyleDeclaration outlineColor(ColorValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_COLOR, value);
  }

  protected final StyleDeclaration outlineOffset(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_OFFSET, value);
  }

  protected final StyleDeclaration outlineOffset(Length value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_OFFSET, value);
  }

  protected final StyleDeclaration outlineStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_STYLE, value);
  }

  protected final StyleDeclaration outlineStyle(OutlineStyleValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_STYLE, value);
  }

  protected final StyleDeclaration outlineWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_WIDTH, value);
  }

  protected final StyleDeclaration outlineWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    return declaration(Property.OUTLINE_WIDTH, value);
  }

  protected final StyleDeclaration padding(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING, value);
  }

  protected final StyleDeclaration padding(LengthPercentage all) {
    Check.notNull(all, "all == null");
    return declaration(Property.PADDING, all);
  }

  protected final StyleDeclaration padding(LengthPercentage vertical, LengthPercentage horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    return declaration(Property.PADDING, vertical, horizontal);
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage horizontal, LengthPercentage bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    return declaration(Property.PADDING, top, horizontal, bottom);
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage right, LengthPercentage bottom, LengthPercentage left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    return declaration(Property.PADDING, top, right, bottom, left);
  }

  protected final StyleDeclaration paddingBottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_BOTTOM, value);
  }

  protected final StyleDeclaration paddingBottom(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_BOTTOM, value);
  }

  protected final StyleDeclaration paddingLeft(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_LEFT, value);
  }

  protected final StyleDeclaration paddingLeft(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_LEFT, value);
  }

  protected final StyleDeclaration paddingRight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_RIGHT, value);
  }

  protected final StyleDeclaration paddingRight(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_RIGHT, value);
  }

  protected final StyleDeclaration paddingTop(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_TOP, value);
  }

  protected final StyleDeclaration paddingTop(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.PADDING_TOP, value);
  }

  protected final StyleDeclaration position(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.POSITION, value);
  }

  protected final StyleDeclaration position(PositionValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.POSITION, value);
  }

  protected final StyleDeclaration resize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.RESIZE, value);
  }

  protected final StyleDeclaration resize(ResizeValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.RESIZE, value);
  }

  protected final StyleDeclaration tabSize(int value) {
    return declaration(Property.TAB_SIZE, value);
  }

  protected final StyleDeclaration tabSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TAB_SIZE, value);
  }

  protected final StyleDeclaration tabSize(Length value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TAB_SIZE, value);
  }

  protected final StyleDeclaration textAlign(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_ALIGN, value);
  }

  protected final StyleDeclaration textAlign(TextAlignValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_ALIGN, value);
  }

  protected final StyleDeclaration textDecoration(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION, value);
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION, value);
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.TEXT_DECORATION, value1, value2);
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    return declaration(Property.TEXT_DECORATION, value1, value2, value3);
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3, TextDecorationValue value4) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    Check.notNull(value4, "value4 == null");
    return declaration(Property.TEXT_DECORATION, value1, value2, value3, value4);
  }

  protected final StyleDeclaration textDecorationColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_COLOR, value);
  }

  protected final StyleDeclaration textDecorationColor(ColorValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_COLOR, value);
  }

  protected final StyleDeclaration textDecorationLine(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_LINE, value);
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineSingleValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_LINE, value);
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.TEXT_DECORATION_LINE, value1, value2);
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2, TextDecorationLineMultiValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    return declaration(Property.TEXT_DECORATION_LINE, value1, value2, value3);
  }

  protected final StyleDeclaration textDecorationStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_STYLE, value);
  }

  protected final StyleDeclaration textDecorationStyle(TextDecorationStyleValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_STYLE, value);
  }

  protected final StyleDeclaration textDecorationThickness(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_THICKNESS, value);
  }

  protected final StyleDeclaration textDecorationThickness(TextDecorationThicknessValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_DECORATION_THICKNESS, value);
  }

  protected final StyleDeclaration textIndent(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_INDENT, value);
  }

  protected final StyleDeclaration textIndent(LengthPercentage value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_INDENT, value);
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    return declaration(Property.TEXT_INDENT, value1, value2);
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2, TextIndentValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    return declaration(Property.TEXT_INDENT, value1, value2, value3);
  }

  protected final StyleDeclaration textTransform(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_TRANSFORM, value);
  }

  protected final StyleDeclaration textTransform(TextTransformValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TEXT_TRANSFORM, value);
  }

  protected final StyleDeclaration top(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TOP, value);
  }

  protected final StyleDeclaration top(TopValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.TOP, value);
  }

  protected final StyleDeclaration verticalAlign(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.VERTICAL_ALIGN, value);
  }

  protected final StyleDeclaration verticalAlign(VerticalAlignValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.VERTICAL_ALIGN, value);
  }

  protected final StyleDeclaration webkitAppearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property._WEBKIT_APPEARANCE, value);
  }

  protected final StyleDeclaration webkitAppearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property._WEBKIT_APPEARANCE, value);
  }

  protected final StyleDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property._WEBKIT_TEXT_SIZE_ADJUST, value);
  }

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property._WEBKIT_TEXT_SIZE_ADJUST, value);
  }

  protected final StyleDeclaration width(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    return declaration(Property.WIDTH, value);
  }

  protected final StyleDeclaration width(HeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    return declaration(Property.WIDTH, value);
  }

  abstract StyleDeclaration declaration(Property name, PropertyValue value);

  abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2);

  abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

  abstract StyleDeclaration declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);

  abstract StyleDeclaration declaration(Property name, int value);

  abstract StyleDeclaration declaration(Property name, double value);

  abstract StyleDeclaration declaration(Property name, String value);
}
