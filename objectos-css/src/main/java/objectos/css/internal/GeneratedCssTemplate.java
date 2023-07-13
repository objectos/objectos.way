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

import objectos.css.om.Selector;
import objectos.css.tmpl.AppearanceValue;
import objectos.css.tmpl.AutoKeyword;
import objectos.css.tmpl.BlockKeyword;
import objectos.css.tmpl.BorderCollapseValue;
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
import objectos.css.tmpl.DottedKeyword;
import objectos.css.tmpl.DoubleKeyword;
import objectos.css.tmpl.EndKeyword;
import objectos.css.tmpl.FitContentKeyword;
import objectos.css.tmpl.FlexDirectionValue;
import objectos.css.tmpl.FlexEndKeyword;
import objectos.css.tmpl.FlexStartKeyword;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.FontSizeValue;
import objectos.css.tmpl.FontValue;
import objectos.css.tmpl.FontWeightValue;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.GrooveKeyword;
import objectos.css.tmpl.InlineKeyword;
import objectos.css.tmpl.InsetKeyword;
import objectos.css.tmpl.JustifyContentValue;
import objectos.css.tmpl.LeftKeyword;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.ListStylePositionValue;
import objectos.css.tmpl.MaxContentKeyword;
import objectos.css.tmpl.MediumKeyword;
import objectos.css.tmpl.MenuKeyword;
import objectos.css.tmpl.MinContentKeyword;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.OutsetKeyword;
import objectos.css.tmpl.OverflowPosition;
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
import objectos.css.tmpl.TextDecorationLineMultiValue;
import objectos.css.tmpl.TextDecorationStyleValue;
import objectos.css.tmpl.TextDecorationThicknessValue;
import objectos.css.tmpl.TextIndentValue;
import objectos.css.tmpl.TextTransformValue;
import objectos.css.tmpl.TextareaKeyword;
import objectos.css.tmpl.VerticalAlignValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final Selector __after = StandardName.__after;

  protected static final Selector __before = StandardName.__before;

  protected static final Selector __placeholder = StandardName.__placeholder;

  protected static final Selector __webkitFileUploadButton = StandardName.__webkitFileUploadButton;

  protected static final Selector __webkitInnerSpinButton = StandardName.__webkitInnerSpinButton;

  protected static final Selector __webkitOuterSpinButton = StandardName.__webkitOuterSpinButton;

  protected static final Selector __webkitSearchDecoration = StandardName.__webkitSearchDecoration;

  protected static final Selector _disabled = StandardName._disabled;

  protected static final Selector _mozFocusring = StandardName._mozFocusring;

  protected static final Selector _mozUiInvalid = StandardName._mozUiInvalid;

  protected static final Selector a = StandardName.a;

  protected static final Selector audio = StandardName.audio;

  protected static final Selector b = StandardName.b;

  protected static final Selector blockquote = StandardName.blockquote;

  protected static final Selector body = StandardName.body;

  protected static final Selector canvas = StandardName.canvas;

  protected static final Selector code = StandardName.code;

  protected static final Selector dd = StandardName.dd;

  protected static final Selector dl = StandardName.dl;

  protected static final Selector embed = StandardName.embed;

  protected static final Selector fieldset = StandardName.fieldset;

  protected static final Selector figure = StandardName.figure;

  protected static final Selector form = StandardName.form;

  protected static final Selector h1 = StandardName.h1;

  protected static final Selector h2 = StandardName.h2;

  protected static final Selector h3 = StandardName.h3;

  protected static final Selector h4 = StandardName.h4;

  protected static final Selector h5 = StandardName.h5;

  protected static final Selector h6 = StandardName.h6;

  protected static final Selector hr = StandardName.hr;

  protected static final Selector html = StandardName.html;

  protected static final Selector iframe = StandardName.iframe;

  protected static final Selector img = StandardName.img;

  protected static final Selector input = StandardName.input;

  protected static final Selector kbd = StandardName.kbd;

  protected static final Selector label = StandardName.label;

  protected static final Selector legend = StandardName.legend;

  protected static final Selector li = StandardName.li;

  protected static final Selector object = StandardName.object;

  protected static final Selector ol = StandardName.ol;

  protected static final Selector optgroup = StandardName.optgroup;

  protected static final Selector p = StandardName.p;

  protected static final Selector pre = StandardName.pre;

  protected static final Selector samp = StandardName.samp;

  protected static final Selector select = StandardName.select;

  protected static final Selector strong = StandardName.strong;

  protected static final Selector summary = StandardName.summary;

  protected static final Selector sup = StandardName.sup;

  protected static final Selector svg = StandardName.svg;

  protected static final Selector ul = StandardName.ul;

  protected static final Selector video = StandardName.video;

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

  protected static final CounterStyleValue japaneseFormal = StandardName.japaneseFormal;

  protected static final CounterStyleValue japaneseInformal = StandardName.japaneseInformal;

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
}