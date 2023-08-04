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

import objectos.css.tmpl.Api.AppearanceValue;
import objectos.css.tmpl.Api.AutoKeyword;
import objectos.css.tmpl.Api.BackgroundAttachmentValue;
import objectos.css.tmpl.Api.BackgroundClipValue;
import objectos.css.tmpl.Api.BackgroundImageValue;
import objectos.css.tmpl.Api.BackgroundPositionValue;
import objectos.css.tmpl.Api.BackgroundRepeatValue;
import objectos.css.tmpl.Api.BackgroundRepeatValue2;
import objectos.css.tmpl.Api.BackgroundValue;
import objectos.css.tmpl.Api.BlockKeyword;
import objectos.css.tmpl.Api.BorderBoxKeyword;
import objectos.css.tmpl.Api.BorderCollapseValue;
import objectos.css.tmpl.Api.BorderShorthandValue;
import objectos.css.tmpl.Api.BottomKeyword;
import objectos.css.tmpl.Api.BottomValue;
import objectos.css.tmpl.Api.BoxShadowDeclaration;
import objectos.css.tmpl.Api.BoxSizingValue;
import objectos.css.tmpl.Api.ButtonKeyword;
import objectos.css.tmpl.Api.CenterKeyword;
import objectos.css.tmpl.Api.ColorValue;
import objectos.css.tmpl.Api.ContentBoxKeyword;
import objectos.css.tmpl.Api.ContentValue;
import objectos.css.tmpl.Api.CounterStyleValue;
import objectos.css.tmpl.Api.CursorValue;
import objectos.css.tmpl.Api.DashedKeyword;
import objectos.css.tmpl.Api.DisplayBoxValue;
import objectos.css.tmpl.Api.DisplayInsideValue;
import objectos.css.tmpl.Api.DisplayInternalValue;
import objectos.css.tmpl.Api.DisplayLegacyValue;
import objectos.css.tmpl.Api.DisplayListItemValue;
import objectos.css.tmpl.Api.DisplayOutsideValue;
import objectos.css.tmpl.Api.DisplayValue;
import objectos.css.tmpl.Api.DisplayValue2;
import objectos.css.tmpl.Api.DottedKeyword;
import objectos.css.tmpl.Api.DoubleKeyword;
import objectos.css.tmpl.Api.EndKeyword;
import objectos.css.tmpl.Api.FilterValue;
import objectos.css.tmpl.Api.FitContentKeyword;
import objectos.css.tmpl.Api.FixedKeyword;
import objectos.css.tmpl.Api.FlexDirectionValue;
import objectos.css.tmpl.Api.FlexEndKeyword;
import objectos.css.tmpl.Api.FlexStartKeyword;
import objectos.css.tmpl.Api.FlexWrapValue;
import objectos.css.tmpl.Api.FontFamilyValue;
import objectos.css.tmpl.Api.FontFeatureSettingsValue;
import objectos.css.tmpl.Api.FontSizeValue;
import objectos.css.tmpl.Api.FontStyleValue;
import objectos.css.tmpl.Api.FontValue;
import objectos.css.tmpl.Api.FontVariationSettingsValue;
import objectos.css.tmpl.Api.FontWeightValue;
import objectos.css.tmpl.Api.GlobalKeyword;
import objectos.css.tmpl.Api.GrooveKeyword;
import objectos.css.tmpl.Api.HeightOrWidthValue;
import objectos.css.tmpl.Api.InlineKeyword;
import objectos.css.tmpl.Api.InsetKeyword;
import objectos.css.tmpl.Api.IntLiteral;
import objectos.css.tmpl.Api.JustifyContentPosition;
import objectos.css.tmpl.Api.JustifyContentValue;
import objectos.css.tmpl.Api.LeftKeyword;
import objectos.css.tmpl.Api.LeftValue;
import objectos.css.tmpl.Api.LengthPercentage;
import objectos.css.tmpl.Api.LengthValue;
import objectos.css.tmpl.Api.LetterSpacingValue;
import objectos.css.tmpl.Api.LineHeightValue;
import objectos.css.tmpl.Api.LineStyle;
import objectos.css.tmpl.Api.LineWidth;
import objectos.css.tmpl.Api.ListStyleImageValue;
import objectos.css.tmpl.Api.ListStylePositionValue;
import objectos.css.tmpl.Api.ListStyleTypeValue;
import objectos.css.tmpl.Api.ListStyleValue;
import objectos.css.tmpl.Api.MarginValue;
import objectos.css.tmpl.Api.MaxContentKeyword;
import objectos.css.tmpl.Api.MaxHeightOrWidthValue;
import objectos.css.tmpl.Api.MediumKeyword;
import objectos.css.tmpl.Api.MenuKeyword;
import objectos.css.tmpl.Api.MinContentKeyword;
import objectos.css.tmpl.Api.MinHeightOrWidthValue;
import objectos.css.tmpl.Api.NoneKeyword;
import objectos.css.tmpl.Api.NormalKeyword;
import objectos.css.tmpl.Api.NumberValue;
import objectos.css.tmpl.Api.OpacityDeclaration;
import objectos.css.tmpl.Api.OutlineStyleValue;
import objectos.css.tmpl.Api.OutlineValue;
import objectos.css.tmpl.Api.OutsetKeyword;
import objectos.css.tmpl.Api.OverflowPosition;
import objectos.css.tmpl.Api.PercentageValue;
import objectos.css.tmpl.Api.PointerEventsValue;
import objectos.css.tmpl.Api.PositionValue;
import objectos.css.tmpl.Api.ProgressKeyword;
import objectos.css.tmpl.Api.PropertyValue;
import objectos.css.tmpl.Api.ResizeValue;
import objectos.css.tmpl.Api.RidgeKeyword;
import objectos.css.tmpl.Api.RightKeyword;
import objectos.css.tmpl.Api.Selector;
import objectos.css.tmpl.Api.SmallKeyword;
import objectos.css.tmpl.Api.SolidKeyword;
import objectos.css.tmpl.Api.StartKeyword;
import objectos.css.tmpl.Api.StyleDeclaration;
import objectos.css.tmpl.Api.SubKeyword;
import objectos.css.tmpl.Api.TableKeyword;
import objectos.css.tmpl.Api.TextAlignValue;
import objectos.css.tmpl.Api.TextDecorationLineMultiValue;
import objectos.css.tmpl.Api.TextDecorationLineSingleValue;
import objectos.css.tmpl.Api.TextDecorationStyleValue;
import objectos.css.tmpl.Api.TextDecorationThicknessValue;
import objectos.css.tmpl.Api.TextDecorationValue;
import objectos.css.tmpl.Api.TextIndentValue;
import objectos.css.tmpl.Api.TextSizeAdjustValue;
import objectos.css.tmpl.Api.TextTransformValue;
import objectos.css.tmpl.Api.TextareaKeyword;
import objectos.css.tmpl.Api.TopKeyword;
import objectos.css.tmpl.Api.TopValue;
import objectos.css.tmpl.Api.VerticalAlignValue;
import objectos.css.tmpl.Api.WordBreakValue;
import objectos.css.util.Color;
import objectos.lang.Check;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
abstract class GeneratedCssTemplate {
  protected static final Selector __after = StandardPseudoElementSelector.__after;

  protected static final Selector __before = StandardPseudoElementSelector.__before;

  protected static final Selector __placeholder = StandardPseudoElementSelector.__placeholder;

  protected static final Selector __webkitFileUploadButton = StandardPseudoElementSelector.__webkitFileUploadButton;

  protected static final Selector __webkitInnerSpinButton = StandardPseudoElementSelector.__webkitInnerSpinButton;

  protected static final Selector __webkitOuterSpinButton = StandardPseudoElementSelector.__webkitOuterSpinButton;

  protected static final Selector __webkitSearchDecoration = StandardPseudoElementSelector.__webkitSearchDecoration;

  protected static final Selector _disabled = StandardPseudoClassSelector._disabled;

  protected static final Selector _firstChild = StandardPseudoClassSelector._firstChild;

  protected static final Selector _firstOfType = StandardPseudoClassSelector._firstOfType;

  protected static final Selector _focus = StandardPseudoClassSelector._focus;

  protected static final Selector _hover = StandardPseudoClassSelector._hover;

  protected static final Selector _mozFocusring = StandardPseudoClassSelector._mozFocusring;

  protected static final Selector _mozUiInvalid = StandardPseudoClassSelector._mozUiInvalid;

  protected static final Selector _root = StandardPseudoClassSelector._root;

  protected static final Selector a = StandardTypeSelector.a;

  protected static final Selector article = StandardTypeSelector.article;

  protected static final Selector audio = StandardTypeSelector.audio;

  protected static final Selector b = StandardTypeSelector.b;

  protected static final Selector blockquote = StandardTypeSelector.blockquote;

  protected static final Selector body = StandardTypeSelector.body;

  protected static final Selector canvas = StandardTypeSelector.canvas;

  protected static final Selector code = StandardTypeSelector.code;

  protected static final Selector dd = StandardTypeSelector.dd;

  protected static final Selector div = StandardTypeSelector.div;

  protected static final Selector dl = StandardTypeSelector.dl;

  protected static final Selector embed = StandardTypeSelector.embed;

  protected static final Selector fieldset = StandardTypeSelector.fieldset;

  protected static final Selector figure = StandardTypeSelector.figure;

  protected static final Selector form = StandardTypeSelector.form;

  protected static final Selector h1 = StandardTypeSelector.h1;

  protected static final Selector h2 = StandardTypeSelector.h2;

  protected static final Selector h3 = StandardTypeSelector.h3;

  protected static final Selector h4 = StandardTypeSelector.h4;

  protected static final Selector h5 = StandardTypeSelector.h5;

  protected static final Selector h6 = StandardTypeSelector.h6;

  protected static final Selector header = StandardTypeSelector.header;

  protected static final Selector hr = StandardTypeSelector.hr;

  protected static final Selector html = StandardTypeSelector.html;

  protected static final Selector iframe = StandardTypeSelector.iframe;

  protected static final Selector img = StandardTypeSelector.img;

  protected static final Selector input = StandardTypeSelector.input;

  protected static final Selector kbd = StandardTypeSelector.kbd;

  protected static final Selector label = StandardTypeSelector.label;

  protected static final Selector legend = StandardTypeSelector.legend;

  protected static final Selector li = StandardTypeSelector.li;

  protected static final Selector nav = StandardTypeSelector.nav;

  protected static final Selector object = StandardTypeSelector.object;

  protected static final Selector ol = StandardTypeSelector.ol;

  protected static final Selector optgroup = StandardTypeSelector.optgroup;

  protected static final Selector p = StandardTypeSelector.p;

  protected static final Selector pre = StandardTypeSelector.pre;

  protected static final Selector samp = StandardTypeSelector.samp;

  protected static final Selector section = StandardTypeSelector.section;

  protected static final Selector select = StandardTypeSelector.select;

  protected static final Selector strong = StandardTypeSelector.strong;

  protected static final Selector summary = StandardTypeSelector.summary;

  protected static final Selector sup = StandardTypeSelector.sup;

  protected static final Selector svg = StandardTypeSelector.svg;

  protected static final Selector tbody = StandardTypeSelector.tbody;

  protected static final Selector td = StandardTypeSelector.td;

  protected static final Selector th = StandardTypeSelector.th;

  protected static final Selector thead = StandardTypeSelector.thead;

  protected static final Selector tr = StandardTypeSelector.tr;

  protected static final Selector ul = StandardTypeSelector.ul;

  protected static final Selector video = StandardTypeSelector.video;

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

  protected static final ColorValue SLATE_050 = Color.ofHex("#f8fafc");

  protected static final ColorValue SLATE_100 = Color.ofHex("#f1f5f9");

  protected static final ColorValue SLATE_200 = Color.ofHex("#e2e8f0");

  protected static final ColorValue SLATE_300 = Color.ofHex("#cbd5e1");

  protected static final ColorValue SLATE_400 = Color.ofHex("#94a3b8");

  protected static final ColorValue SLATE_500 = Color.ofHex("#64748b");

  protected static final ColorValue SLATE_600 = Color.ofHex("#475569");

  protected static final ColorValue SLATE_700 = Color.ofHex("#334155");

  protected static final ColorValue SLATE_800 = Color.ofHex("#1e293b");

  protected static final ColorValue SLATE_900 = Color.ofHex("#0f172a");

  protected static final ColorValue GRAY_050 = Color.ofHex("#f9fafb");

  protected static final ColorValue GRAY_100 = Color.ofHex("#f3f4f6");

  protected static final ColorValue GRAY_200 = Color.ofHex("#e5e7eb");

  protected static final ColorValue GRAY_300 = Color.ofHex("#d1d5db");

  protected static final ColorValue GRAY_400 = Color.ofHex("#9ca3af");

  protected static final ColorValue GRAY_500 = Color.ofHex("#6b7280");

  protected static final ColorValue GRAY_600 = Color.ofHex("#4b5563");

  protected static final ColorValue GRAY_700 = Color.ofHex("#374151");

  protected static final ColorValue GRAY_800 = Color.ofHex("#1f2937");

  protected static final ColorValue GRAY_900 = Color.ofHex("#111827");

  protected static final ColorValue ZINC_050 = Color.ofHex("#fafafa");

  protected static final ColorValue ZINC_100 = Color.ofHex("#f4f4f5");

  protected static final ColorValue ZINC_200 = Color.ofHex("#e4e4e7");

  protected static final ColorValue ZINC_300 = Color.ofHex("#d4d4d8");

  protected static final ColorValue ZINC_400 = Color.ofHex("#a1a1aa");

  protected static final ColorValue ZINC_500 = Color.ofHex("#71717a");

  protected static final ColorValue ZINC_600 = Color.ofHex("#52525b");

  protected static final ColorValue ZINC_700 = Color.ofHex("#3f3f46");

  protected static final ColorValue ZINC_800 = Color.ofHex("#27272a");

  protected static final ColorValue ZINC_900 = Color.ofHex("#18181b");

  protected static final ColorValue NEUTRAL_050 = Color.ofHex("#fafafa");

  protected static final ColorValue NEUTRAL_100 = Color.ofHex("#f5f5f5");

  protected static final ColorValue NEUTRAL_200 = Color.ofHex("#e5e5e5");

  protected static final ColorValue NEUTRAL_300 = Color.ofHex("#d4d4d4");

  protected static final ColorValue NEUTRAL_400 = Color.ofHex("#a3a3a3");

  protected static final ColorValue NEUTRAL_500 = Color.ofHex("#737373");

  protected static final ColorValue NEUTRAL_600 = Color.ofHex("#525252");

  protected static final ColorValue NEUTRAL_700 = Color.ofHex("#404040");

  protected static final ColorValue NEUTRAL_800 = Color.ofHex("#262626");

  protected static final ColorValue NEUTRAL_900 = Color.ofHex("#171717");

  protected static final ColorValue STONE_050 = Color.ofHex("#fafaf9");

  protected static final ColorValue STONE_100 = Color.ofHex("#f5f5f4");

  protected static final ColorValue STONE_200 = Color.ofHex("#e7e5e4");

  protected static final ColorValue STONE_300 = Color.ofHex("#d6d3d1");

  protected static final ColorValue STONE_400 = Color.ofHex("#a8a29e");

  protected static final ColorValue STONE_500 = Color.ofHex("#78716c");

  protected static final ColorValue STONE_600 = Color.ofHex("#57534e");

  protected static final ColorValue STONE_700 = Color.ofHex("#44403c");

  protected static final ColorValue STONE_800 = Color.ofHex("#292524");

  protected static final ColorValue STONE_900 = Color.ofHex("#1c1917");

  protected static final ColorValue RED_050 = Color.ofHex("#fef2f2");

  protected static final ColorValue RED_100 = Color.ofHex("#fee2e2");

  protected static final ColorValue RED_200 = Color.ofHex("#fecaca");

  protected static final ColorValue RED_300 = Color.ofHex("#fca5a5");

  protected static final ColorValue RED_400 = Color.ofHex("#f87171");

  protected static final ColorValue RED_500 = Color.ofHex("#ef4444");

  protected static final ColorValue RED_600 = Color.ofHex("#dc2626");

  protected static final ColorValue RED_700 = Color.ofHex("#b91c1c");

  protected static final ColorValue RED_800 = Color.ofHex("#991b1b");

  protected static final ColorValue RED_900 = Color.ofHex("#7f1d1d");

  protected static final ColorValue ORANGE_050 = Color.ofHex("#fff7ed");

  protected static final ColorValue ORANGE_100 = Color.ofHex("#ffedd5");

  protected static final ColorValue ORANGE_200 = Color.ofHex("#fed7aa");

  protected static final ColorValue ORANGE_300 = Color.ofHex("#fdba74");

  protected static final ColorValue ORANGE_400 = Color.ofHex("#fb923c");

  protected static final ColorValue ORANGE_500 = Color.ofHex("#f97316");

  protected static final ColorValue ORANGE_600 = Color.ofHex("#ea580c");

  protected static final ColorValue ORANGE_700 = Color.ofHex("#c2410c");

  protected static final ColorValue ORANGE_800 = Color.ofHex("#9a3412");

  protected static final ColorValue ORANGE_900 = Color.ofHex("#7c2d12");

  protected static final ColorValue AMBER_050 = Color.ofHex("#fffbeb");

  protected static final ColorValue AMBER_100 = Color.ofHex("#fef3c7");

  protected static final ColorValue AMBER_200 = Color.ofHex("#fde68a");

  protected static final ColorValue AMBER_300 = Color.ofHex("#fcd34d");

  protected static final ColorValue AMBER_400 = Color.ofHex("#fbbf24");

  protected static final ColorValue AMBER_500 = Color.ofHex("#f59e0b");

  protected static final ColorValue AMBER_600 = Color.ofHex("#d97706");

  protected static final ColorValue AMBER_700 = Color.ofHex("#b45309");

  protected static final ColorValue AMBER_800 = Color.ofHex("#92400e");

  protected static final ColorValue AMBER_900 = Color.ofHex("#78350f");

  protected static final ColorValue YELLOW_050 = Color.ofHex("#fefce8");

  protected static final ColorValue YELLOW_100 = Color.ofHex("#fef9c3");

  protected static final ColorValue YELLOW_200 = Color.ofHex("#fef08a");

  protected static final ColorValue YELLOW_300 = Color.ofHex("#fde047");

  protected static final ColorValue YELLOW_400 = Color.ofHex("#facc15");

  protected static final ColorValue YELLOW_500 = Color.ofHex("#eab308");

  protected static final ColorValue YELLOW_600 = Color.ofHex("#ca8a04");

  protected static final ColorValue YELLOW_700 = Color.ofHex("#a16207");

  protected static final ColorValue YELLOW_800 = Color.ofHex("#854d0e");

  protected static final ColorValue YELLOW_900 = Color.ofHex("#713f12");

  protected static final ColorValue LIME_050 = Color.ofHex("#f7fee7");

  protected static final ColorValue LIME_100 = Color.ofHex("#ecfccb");

  protected static final ColorValue LIME_200 = Color.ofHex("#d9f99d");

  protected static final ColorValue LIME_300 = Color.ofHex("#bef264");

  protected static final ColorValue LIME_400 = Color.ofHex("#a3e635");

  protected static final ColorValue LIME_500 = Color.ofHex("#84cc16");

  protected static final ColorValue LIME_600 = Color.ofHex("#65a30d");

  protected static final ColorValue LIME_700 = Color.ofHex("#4d7c0f");

  protected static final ColorValue LIME_800 = Color.ofHex("#3f6212");

  protected static final ColorValue LIME_900 = Color.ofHex("#365314");

  protected static final ColorValue GREEN_050 = Color.ofHex("#f0fdf4");

  protected static final ColorValue GREEN_100 = Color.ofHex("#dcfce7");

  protected static final ColorValue GREEN_200 = Color.ofHex("#bbf7d0");

  protected static final ColorValue GREEN_300 = Color.ofHex("#86efac");

  protected static final ColorValue GREEN_400 = Color.ofHex("#4ade80");

  protected static final ColorValue GREEN_500 = Color.ofHex("#22c55e");

  protected static final ColorValue GREEN_600 = Color.ofHex("#16a34a");

  protected static final ColorValue GREEN_700 = Color.ofHex("#15803d");

  protected static final ColorValue GREEN_800 = Color.ofHex("#166534");

  protected static final ColorValue GREEN_900 = Color.ofHex("#14532d");

  protected static final ColorValue EMERALD_050 = Color.ofHex("#ecfdf5");

  protected static final ColorValue EMERALD_100 = Color.ofHex("#d1fae5");

  protected static final ColorValue EMERALD_200 = Color.ofHex("#a7f3d0");

  protected static final ColorValue EMERALD_300 = Color.ofHex("#6ee7b7");

  protected static final ColorValue EMERALD_400 = Color.ofHex("#34d399");

  protected static final ColorValue EMERALD_500 = Color.ofHex("#10b981");

  protected static final ColorValue EMERALD_600 = Color.ofHex("#059669");

  protected static final ColorValue EMERALD_700 = Color.ofHex("#047857");

  protected static final ColorValue EMERALD_800 = Color.ofHex("#065f46");

  protected static final ColorValue EMERALD_900 = Color.ofHex("#064e3b");

  protected static final ColorValue TEAL_050 = Color.ofHex("#f0fdfa");

  protected static final ColorValue TEAL_100 = Color.ofHex("#ccfbf1");

  protected static final ColorValue TEAL_200 = Color.ofHex("#99f6e4");

  protected static final ColorValue TEAL_300 = Color.ofHex("#5eead4");

  protected static final ColorValue TEAL_400 = Color.ofHex("#2dd4bf");

  protected static final ColorValue TEAL_500 = Color.ofHex("#14b8a6");

  protected static final ColorValue TEAL_600 = Color.ofHex("#0d9488");

  protected static final ColorValue TEAL_700 = Color.ofHex("#0f766e");

  protected static final ColorValue TEAL_800 = Color.ofHex("#115e59");

  protected static final ColorValue TEAL_900 = Color.ofHex("#134e4a");

  protected static final ColorValue CYAN_050 = Color.ofHex("#ecfeff");

  protected static final ColorValue CYAN_100 = Color.ofHex("#cffafe");

  protected static final ColorValue CYAN_200 = Color.ofHex("#a5f3fc");

  protected static final ColorValue CYAN_300 = Color.ofHex("#67e8f9");

  protected static final ColorValue CYAN_400 = Color.ofHex("#22d3ee");

  protected static final ColorValue CYAN_500 = Color.ofHex("#06b6d4");

  protected static final ColorValue CYAN_600 = Color.ofHex("#0891b2");

  protected static final ColorValue CYAN_700 = Color.ofHex("#0e7490");

  protected static final ColorValue CYAN_800 = Color.ofHex("#155e75");

  protected static final ColorValue CYAN_900 = Color.ofHex("#164e63");

  protected static final ColorValue SKY_050 = Color.ofHex("#f0f9ff");

  protected static final ColorValue SKY_100 = Color.ofHex("#e0f2fe");

  protected static final ColorValue SKY_200 = Color.ofHex("#bae6fd");

  protected static final ColorValue SKY_300 = Color.ofHex("#7dd3fc");

  protected static final ColorValue SKY_400 = Color.ofHex("#38bdf8");

  protected static final ColorValue SKY_500 = Color.ofHex("#0ea5e9");

  protected static final ColorValue SKY_600 = Color.ofHex("#0284c7");

  protected static final ColorValue SKY_700 = Color.ofHex("#0369a1");

  protected static final ColorValue SKY_800 = Color.ofHex("#075985");

  protected static final ColorValue SKY_900 = Color.ofHex("#0c4a6e");

  protected static final ColorValue BLUE_050 = Color.ofHex("#eff6ff");

  protected static final ColorValue BLUE_100 = Color.ofHex("#dbeafe");

  protected static final ColorValue BLUE_200 = Color.ofHex("#bfdbfe");

  protected static final ColorValue BLUE_300 = Color.ofHex("#93c5fd");

  protected static final ColorValue BLUE_400 = Color.ofHex("#60a5fa");

  protected static final ColorValue BLUE_500 = Color.ofHex("#3b82f6");

  protected static final ColorValue BLUE_600 = Color.ofHex("#2563eb");

  protected static final ColorValue BLUE_700 = Color.ofHex("#1d4ed8");

  protected static final ColorValue BLUE_800 = Color.ofHex("#1e40af");

  protected static final ColorValue BLUE_900 = Color.ofHex("#1e3a8a");

  protected static final ColorValue INDIGO_050 = Color.ofHex("#eef2ff");

  protected static final ColorValue INDIGO_100 = Color.ofHex("#e0e7ff");

  protected static final ColorValue INDIGO_200 = Color.ofHex("#c7d2fe");

  protected static final ColorValue INDIGO_300 = Color.ofHex("#a5b4fc");

  protected static final ColorValue INDIGO_400 = Color.ofHex("#818cf8");

  protected static final ColorValue INDIGO_500 = Color.ofHex("#6366f1");

  protected static final ColorValue INDIGO_600 = Color.ofHex("#4f46e5");

  protected static final ColorValue INDIGO_700 = Color.ofHex("#4338ca");

  protected static final ColorValue INDIGO_800 = Color.ofHex("#3730a3");

  protected static final ColorValue INDIGO_900 = Color.ofHex("#312e81");

  protected static final ColorValue VIOLET_050 = Color.ofHex("#f5f3ff");

  protected static final ColorValue VIOLET_100 = Color.ofHex("#ede9fe");

  protected static final ColorValue VIOLET_200 = Color.ofHex("#ddd6fe");

  protected static final ColorValue VIOLET_300 = Color.ofHex("#c4b5fd");

  protected static final ColorValue VIOLET_400 = Color.ofHex("#a78bfa");

  protected static final ColorValue VIOLET_500 = Color.ofHex("#8b5cf6");

  protected static final ColorValue VIOLET_600 = Color.ofHex("#7c3aed");

  protected static final ColorValue VIOLET_700 = Color.ofHex("#6d28d9");

  protected static final ColorValue VIOLET_800 = Color.ofHex("#5b21b6");

  protected static final ColorValue VIOLET_900 = Color.ofHex("#4c1d95");

  protected static final ColorValue PURPLE_050 = Color.ofHex("#faf5ff");

  protected static final ColorValue PURPLE_100 = Color.ofHex("#f3e8ff");

  protected static final ColorValue PURPLE_200 = Color.ofHex("#e9d5ff");

  protected static final ColorValue PURPLE_300 = Color.ofHex("#d8b4fe");

  protected static final ColorValue PURPLE_400 = Color.ofHex("#c084fc");

  protected static final ColorValue PURPLE_500 = Color.ofHex("#a855f7");

  protected static final ColorValue PURPLE_600 = Color.ofHex("#9333ea");

  protected static final ColorValue PURPLE_700 = Color.ofHex("#7e22ce");

  protected static final ColorValue PURPLE_800 = Color.ofHex("#6b21a8");

  protected static final ColorValue PURPLE_900 = Color.ofHex("#581c87");

  protected static final ColorValue FUCHSIA_050 = Color.ofHex("#fdf4ff");

  protected static final ColorValue FUCHSIA_100 = Color.ofHex("#fae8ff");

  protected static final ColorValue FUCHSIA_200 = Color.ofHex("#f5d0fe");

  protected static final ColorValue FUCHSIA_300 = Color.ofHex("#f0abfc");

  protected static final ColorValue FUCHSIA_400 = Color.ofHex("#e879f9");

  protected static final ColorValue FUCHSIA_500 = Color.ofHex("#d946ef");

  protected static final ColorValue FUCHSIA_600 = Color.ofHex("#c026d3");

  protected static final ColorValue FUCHSIA_700 = Color.ofHex("#a21caf");

  protected static final ColorValue FUCHSIA_800 = Color.ofHex("#86198f");

  protected static final ColorValue FUCHSIA_900 = Color.ofHex("#701a75");

  protected static final ColorValue PINK_050 = Color.ofHex("#fdf2f8");

  protected static final ColorValue PINK_100 = Color.ofHex("#fce7f3");

  protected static final ColorValue PINK_200 = Color.ofHex("#fbcfe8");

  protected static final ColorValue PINK_300 = Color.ofHex("#f9a8d4");

  protected static final ColorValue PINK_400 = Color.ofHex("#f472b6");

  protected static final ColorValue PINK_500 = Color.ofHex("#ec4899");

  protected static final ColorValue PINK_600 = Color.ofHex("#db2777");

  protected static final ColorValue PINK_700 = Color.ofHex("#be185d");

  protected static final ColorValue PINK_800 = Color.ofHex("#9d174d");

  protected static final ColorValue PINK_900 = Color.ofHex("#831843");

  protected static final ColorValue ROSE_050 = Color.ofHex("#fff1f2");

  protected static final ColorValue ROSE_100 = Color.ofHex("#ffe4e6");

  protected static final ColorValue ROSE_200 = Color.ofHex("#fecdd3");

  protected static final ColorValue ROSE_300 = Color.ofHex("#fda4af");

  protected static final ColorValue ROSE_400 = Color.ofHex("#fb7185");

  protected static final ColorValue ROSE_500 = Color.ofHex("#f43f5e");

  protected static final ColorValue ROSE_600 = Color.ofHex("#e11d48");

  protected static final ColorValue ROSE_700 = Color.ofHex("#be123c");

  protected static final ColorValue ROSE_800 = Color.ofHex("#9f1239");

  protected static final ColorValue ROSE_900 = Color.ofHex("#881337");

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

  protected static final BorderBoxKeyword borderBox = StandardName.borderBox;

  protected static final ResizeValue both = StandardName.both;

  protected static final BottomKeyword bottom = StandardName.bottom;

  protected static final WordBreakValue breakAll = StandardName.breakAll;

  protected static final WordBreakValue breakWord = StandardName.breakWord;

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

  protected static final ContentBoxKeyword contentBox = StandardName.contentBox;

  protected static final DisplayBoxValue contents = StandardName.contents;

  protected static final CursorValue contextMenu = StandardName.contextMenu;

  protected static final CursorValue copy = StandardName.copy;

  protected static final CursorValue crosshair = StandardName.crosshair;

  protected static final FontFamilyValue cursive = StandardName.cursive;

  protected static final DashedKeyword dashed = StandardName.dashed;

  protected static final CounterStyleValue decimal = StandardName.decimal;

  protected static final CounterStyleValue decimalLeadingZero = StandardName.decimalLeadingZero;

  protected static final CursorValue default_ = StandardName.default_;

  protected static final CounterStyleValue devanagari = StandardName.devanagari;

  protected static final CounterStyleValue disc = StandardName.disc;

  protected static final CounterStyleValue disclosureClosed = StandardName.disclosureClosed;

  protected static final CounterStyleValue disclosureOpen = StandardName.disclosureOpen;

  protected static final DottedKeyword dotted = StandardName.dotted;

  protected static final DoubleKeyword double_ = StandardName.double_;

  protected static final CursorValue eResize = StandardName.eResize;

  protected static final TextIndentValue eachLine = StandardName.eachLine;

  protected static final FontFamilyValue emoji = StandardName.emoji;

  protected static final EndKeyword end = StandardName.end;

  protected static final CounterStyleValue ethiopicNumeric = StandardName.ethiopicNumeric;

  protected static final CursorValue ewResize = StandardName.ewResize;

  protected static final FontFamilyValue fangsong = StandardName.fangsong;

  protected static final FontFamilyValue fantasy = StandardName.fantasy;

  protected static final FitContentKeyword fitContent = StandardName.fitContent;

  protected static final FixedKeyword fixed = StandardName.fixed;

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

  protected static final WordBreakValue keepAll = StandardName.keepAll;

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

  protected static final BackgroundAttachmentValue local = StandardName.local;

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

  protected static final BackgroundRepeatValue2 noRepeat = StandardName.noRepeat;

  protected static final NoneKeyword none = StandardName.none;

  protected static final NormalKeyword normal = StandardName.normal;

  protected static final CursorValue notAllowed = StandardName.notAllowed;

  protected static final FlexWrapValue nowrap = StandardName.nowrap;

  protected static final CursorValue nsResize = StandardName.nsResize;

  protected static final CursorValue nwResize = StandardName.nwResize;

  protected static final CursorValue nwseResize = StandardName.nwseResize;

  protected static final FontStyleValue oblique = StandardName.oblique;

  protected static final CounterStyleValue oriya = StandardName.oriya;

  protected static final OutsetKeyword outset = StandardName.outset;

  protected static final ListStylePositionValue outside = StandardName.outside;

  protected static final TextDecorationLineMultiValue overline = StandardName.overline;

  protected static final BackgroundClipValue paddingBox = StandardName.paddingBox;

  protected static final CounterStyleValue persian = StandardName.persian;

  protected static final CursorValue pointer = StandardName.pointer;

  protected static final ProgressKeyword progress = StandardName.progress;

  protected static final AppearanceValue progressBar = StandardName.progressBar;

  protected static final AppearanceValue pushButton = StandardName.pushButton;

  protected static final AppearanceValue radio = StandardName.radio;

  protected static final PositionValue relative = StandardName.relative;

  protected static final BackgroundRepeatValue2 repeat = StandardName.repeat;

  protected static final BackgroundRepeatValue repeatX = StandardName.repeatX;

  protected static final BackgroundRepeatValue repeatY = StandardName.repeatY;

  protected static final RidgeKeyword ridge = StandardName.ridge;

  protected static final RightKeyword right = StandardName.right;

  protected static final BackgroundRepeatValue2 round = StandardName.round;

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

  protected static final BackgroundAttachmentValue scroll = StandardName.scroll;

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

  protected static final BackgroundRepeatValue2 space = StandardName.space;

  protected static final JustifyContentValue spaceAround = StandardName.spaceAround;

  protected static final JustifyContentValue spaceBetween = StandardName.spaceBetween;

  protected static final JustifyContentValue spaceEvenly = StandardName.spaceEvenly;

  protected static final CounterStyleValue square = StandardName.square;

  protected static final AppearanceValue squareButton = StandardName.squareButton;

  protected static final StartKeyword start = StandardName.start;

  protected static final PositionValue static_ = StandardName.static_;

  protected static final FontValue statusBar = StandardName.statusBar;

  protected static final PositionValue sticky = StandardName.sticky;

  protected static final JustifyContentValue stretch = StandardName.stretch;

  protected static final SubKeyword sub = StandardName.sub;

  protected static final VerticalAlignValue super_ = StandardName.super_;

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

  protected static final TopKeyword top = StandardName.top;

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

  protected static final FlexWrapValue wrap = StandardName.wrap;

  protected static final FlexWrapValue wrapReverse = StandardName.wrapReverse;

  protected static final FontSizeValue xLarge = StandardName.xLarge;

  protected static final FontSizeValue xSmall = StandardName.xSmall;

  protected static final FontSizeValue xxLarge = StandardName.xxLarge;

  protected static final FontSizeValue xxSmall = StandardName.xxSmall;

  protected static final FontSizeValue xxxLarge = StandardName.xxxLarge;

  protected static final CursorValue zoomIn = StandardName.zoomIn;

  protected static final CursorValue zoomOut = StandardName.zoomOut;

  protected final LengthValue ch(double value) {
    return length(value, LengthUnit.CH);
  }

  protected final LengthValue ch(int value) {
    return length(value, LengthUnit.CH);
  }

  protected final LengthValue cm(double value) {
    return length(value, LengthUnit.CM);
  }

  protected final LengthValue cm(int value) {
    return length(value, LengthUnit.CM);
  }

  protected final LengthValue em(double value) {
    return length(value, LengthUnit.EM);
  }

  protected final LengthValue em(int value) {
    return length(value, LengthUnit.EM);
  }

  protected final LengthValue ex(double value) {
    return length(value, LengthUnit.EX);
  }

  protected final LengthValue ex(int value) {
    return length(value, LengthUnit.EX);
  }

  protected final LengthValue in(double value) {
    return length(value, LengthUnit.IN);
  }

  protected final LengthValue in(int value) {
    return length(value, LengthUnit.IN);
  }

  protected final LengthValue mm(double value) {
    return length(value, LengthUnit.MM);
  }

  protected final LengthValue mm(int value) {
    return length(value, LengthUnit.MM);
  }

  protected final LengthValue pc(double value) {
    return length(value, LengthUnit.PC);
  }

  protected final LengthValue pc(int value) {
    return length(value, LengthUnit.PC);
  }

  protected final LengthValue pt(double value) {
    return length(value, LengthUnit.PT);
  }

  protected final LengthValue pt(int value) {
    return length(value, LengthUnit.PT);
  }

  protected final LengthValue px(double value) {
    return length(value, LengthUnit.PX);
  }

  protected final LengthValue px(int value) {
    return length(value, LengthUnit.PX);
  }

  protected final LengthValue q(double value) {
    return length(value, LengthUnit.Q);
  }

  protected final LengthValue q(int value) {
    return length(value, LengthUnit.Q);
  }

  protected final LengthValue rem(double value) {
    return length(value, LengthUnit.REM);
  }

  protected final LengthValue rem(int value) {
    return length(value, LengthUnit.REM);
  }

  protected final LengthValue vh(double value) {
    return length(value, LengthUnit.VH);
  }

  protected final LengthValue vh(int value) {
    return length(value, LengthUnit.VH);
  }

  protected final LengthValue vmax(double value) {
    return length(value, LengthUnit.VMAX);
  }

  protected final LengthValue vmax(int value) {
    return length(value, LengthUnit.VMAX);
  }

  protected final LengthValue vmin(double value) {
    return length(value, LengthUnit.VMIN);
  }

  protected final LengthValue vmin(int value) {
    return length(value, LengthUnit.VMIN);
  }

  protected final LengthValue vw(double value) {
    return length(value, LengthUnit.VW);
  }

  protected final LengthValue vw(int value) {
    return length(value, LengthUnit.VW);
  }

  abstract LengthValue length(double value, LengthUnit unit);

  abstract LengthValue length(int value, LengthUnit unit);

  protected final StyleDeclaration appearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration appearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(BackgroundValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(BackgroundValue value1, BackgroundValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BACKGROUND, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BACKGROUND, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3, BackgroundValue value4) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    Check.notNull(value4, "value4 == null");
    declaration(Property.BACKGROUND, value1, value2, value3, value4);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration background(BackgroundValue value1, BackgroundValue value2, BackgroundValue value3, BackgroundValue value4, BackgroundValue value5) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    Check.notNull(value4, "value4 == null");
    Check.notNull(value5, "value5 == null");
    declaration(Property.BACKGROUND, value1, value2, value3, value4, value5);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundAttachment(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_ATTACHMENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundAttachment(BackgroundAttachmentValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_ATTACHMENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundClip(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_CLIP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundClip(BackgroundClipValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_CLIP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundColor(ColorValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundImage(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_IMAGE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundImage(BackgroundImageValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_IMAGE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundPosition(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundPosition(BackgroundPositionValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BACKGROUND_POSITION, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2, BackgroundPositionValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BACKGROUND_POSITION, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundPosition(BackgroundPositionValue value1, BackgroundPositionValue value2, BackgroundPositionValue value3, BackgroundPositionValue value4) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    Check.notNull(value4, "value4 == null");
    declaration(Property.BACKGROUND_POSITION, value1, value2, value3, value4);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundRepeat(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_REPEAT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundRepeat(BackgroundRepeatValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BACKGROUND_REPEAT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration backgroundRepeat(BackgroundRepeatValue2 value1, BackgroundRepeatValue2 value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BACKGROUND_REPEAT, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration border(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration border(BorderShorthandValue value1) {
    Check.notNull(value1, "value1 == null");
    declaration(Property.BORDER, value1);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration border(BorderShorthandValue value1, BorderShorthandValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BORDER, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration border(BorderShorthandValue value1, BorderShorthandValue value2, BorderShorthandValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BORDER, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottom(BorderShorthandValue value1) {
    Check.notNull(value1, "value1 == null");
    declaration(Property.BORDER_BOTTOM, value1);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottom(BorderShorthandValue value1, BorderShorthandValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BORDER_BOTTOM, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottom(BorderShorthandValue value1, BorderShorthandValue value2, BorderShorthandValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BORDER_BOTTOM, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottomWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_BOTTOM_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderBottomWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_BOTTOM_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderCollapse(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_COLLAPSE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderCollapse(BorderCollapseValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_COLLAPSE, value);
    return InternalInstruction.INSTANCE;
  }

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

  protected final StyleDeclaration borderColor(ColorValue vertical, ColorValue horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    declaration(Property.BORDER_COLOR, vertical, horizontal);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderColor(ColorValue top, ColorValue horizontal, ColorValue bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    declaration(Property.BORDER_COLOR, top, horizontal, bottom);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderColor(ColorValue top, ColorValue right, ColorValue bottom, ColorValue left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    declaration(Property.BORDER_COLOR, top, right, bottom, left);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeft(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeft(BorderShorthandValue value1) {
    Check.notNull(value1, "value1 == null");
    declaration(Property.BORDER_LEFT, value1);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeft(BorderShorthandValue value1, BorderShorthandValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BORDER_LEFT, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeft(BorderShorthandValue value1, BorderShorthandValue value2, BorderShorthandValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BORDER_LEFT, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeftWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_LEFT_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderLeftWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_LEFT_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_RIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRight(BorderShorthandValue value1) {
    Check.notNull(value1, "value1 == null");
    declaration(Property.BORDER_RIGHT, value1);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRight(BorderShorthandValue value1, BorderShorthandValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BORDER_RIGHT, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRight(BorderShorthandValue value1, BorderShorthandValue value2, BorderShorthandValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BORDER_RIGHT, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRightWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_RIGHT_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderRightWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_RIGHT_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderStyle(LineStyle all) {
    Check.notNull(all, "all == null");
    declaration(Property.BORDER_STYLE, all);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderStyle(LineStyle vertical, LineStyle horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    declaration(Property.BORDER_STYLE, vertical, horizontal);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle horizontal, LineStyle bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    declaration(Property.BORDER_STYLE, top, horizontal, bottom);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderStyle(LineStyle top, LineStyle right, LineStyle bottom, LineStyle left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    declaration(Property.BORDER_STYLE, top, right, bottom, left);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTop(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTop(BorderShorthandValue value1) {
    Check.notNull(value1, "value1 == null");
    declaration(Property.BORDER_TOP, value1);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTop(BorderShorthandValue value1, BorderShorthandValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.BORDER_TOP, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTop(BorderShorthandValue value1, BorderShorthandValue value2, BorderShorthandValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.BORDER_TOP, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTopWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_TOP_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderTopWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_TOP_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration borderWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BORDER_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

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

  protected final StyleDeclaration borderWidth(LineWidth top, LineWidth horizontal, LineWidth bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    declaration(Property.BORDER_WIDTH, top, horizontal, bottom);
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

  protected final StyleDeclaration bottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration bottom(BottomValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOX_SHADOW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(NoneKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOX_SHADOW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(LengthValue offsetX, LengthValue offsetY, ColorValue color) {
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, offsetX, offsetY, color);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(LengthValue offsetX, LengthValue offsetY, LengthValue blurRadius, ColorValue color) {
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(blurRadius, "blurRadius == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, offsetX, offsetY, blurRadius, color);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(LengthValue offsetX, LengthValue offsetY, LengthValue blurRadius, LengthValue spreadRadius, ColorValue color) {
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(blurRadius, "blurRadius == null");
    Check.notNull(spreadRadius, "spreadRadius == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, offsetX, offsetY, blurRadius, spreadRadius, color);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(InsetKeyword inset, LengthValue offsetX, LengthValue offsetY, ColorValue color) {
    Check.notNull(inset, "inset == null");
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, inset, offsetX, offsetY, color);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(InsetKeyword inset, LengthValue offsetX, LengthValue offsetY, LengthValue blurRadius, ColorValue color) {
    Check.notNull(inset, "inset == null");
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(blurRadius, "blurRadius == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, inset, offsetX, offsetY, blurRadius, color);
    return InternalInstruction.INSTANCE;
  }

  protected final BoxShadowDeclaration boxShadow(InsetKeyword inset, LengthValue offsetX, LengthValue offsetY, LengthValue blurRadius, LengthValue spreadRadius, ColorValue color) {
    Check.notNull(inset, "inset == null");
    Check.notNull(offsetX, "offsetX == null");
    Check.notNull(offsetY, "offsetY == null");
    Check.notNull(blurRadius, "blurRadius == null");
    Check.notNull(spreadRadius, "spreadRadius == null");
    Check.notNull(color, "color == null");
    declaration(Property.BOX_SHADOW, inset, offsetX, offsetY, blurRadius, spreadRadius, color);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration boxSizing(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOX_SIZING, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration boxSizing(BoxSizingValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.BOX_SIZING, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration color(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration color(ColorValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration content(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration content(ContentValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration content(String value) {
    Check.notNull(value, "value == null");
    declaration(Property.CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration cursor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.CURSOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration cursor(CursorValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.CURSOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration display(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.DISPLAY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration display(DisplayValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.DISPLAY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration display(DisplayValue value, DisplayValue2 value2) {
    Check.notNull(value, "value == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.DISPLAY, value, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fill(ColorValue color) {
    Check.notNull(color, "color == null");
    declaration(Property.FILL, color);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration filter(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration filter(FilterValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexDirection(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_DIRECTION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexDirection(FlexDirectionValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_DIRECTION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexGrow(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_GROW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexGrow(double value) {
    declaration(Property.FLEX_GROW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexGrow(int value) {
    declaration(Property.FLEX_GROW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexGrow(NumberValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_GROW, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexShrink(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_SHRINK, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexShrink(double value) {
    declaration(Property.FLEX_SHRINK, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexShrink(int value) {
    declaration(Property.FLEX_SHRINK, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexShrink(NumberValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_SHRINK, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexWrap(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_WRAP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration flexWrap(FlexWrapValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FLEX_WRAP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration font(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration font(FontValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontFamily(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_FAMILY, value);
    return InternalInstruction.INSTANCE;
  }

  protected abstract StyleDeclaration fontFamily(FontFamilyValue... values);

  protected final StyleDeclaration fontFeatureSettings(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_FEATURE_SETTINGS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontFeatureSettings(FontFeatureSettingsValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_FEATURE_SETTINGS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontSize(FontSizeValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontStyle(FontStyleValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontVariationSettings(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_VARIATION_SETTINGS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontVariationSettings(FontVariationSettingsValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_VARIATION_SETTINGS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontWeight(int value) {
    declaration(Property.FONT_WEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontWeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_WEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration fontWeight(FontWeightValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.FONT_WEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration height(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration height(HeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration justifyContent(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.JUSTIFY_CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration justifyContent(JustifyContentValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.JUSTIFY_CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration justifyContent(OverflowPosition safeOrUnsafe, JustifyContentPosition position) {
    Check.notNull(safeOrUnsafe, "safeOrUnsafe == null");
    Check.notNull(position, "position == null");
    declaration(Property.JUSTIFY_CONTENT, safeOrUnsafe, position);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration left(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration left(LeftValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration letterSpacing(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LETTER_SPACING, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration letterSpacing(LetterSpacingValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LETTER_SPACING, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration lineHeight(double value) {
    declaration(Property.LINE_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration lineHeight(int value) {
    declaration(Property.LINE_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration lineHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LINE_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration lineHeight(LineHeightValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LINE_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyle(ListStyleValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.LIST_STYLE, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyle(ListStyleValue value1, ListStyleValue value2, ListStyleValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.LIST_STYLE, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyleImage(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_IMAGE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyleImage(ListStyleImageValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_IMAGE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStylePosition(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStylePosition(ListStylePositionValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyleType(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_TYPE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyleType(ListStyleTypeValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_TYPE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration listStyleType(String value) {
    Check.notNull(value, "value == null");
    declaration(Property.LIST_STYLE_TYPE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration margin(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration margin(MarginValue all) {
    Check.notNull(all, "all == null");
    declaration(Property.MARGIN, all);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration margin(MarginValue vertical, MarginValue horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    declaration(Property.MARGIN, vertical, horizontal);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue horizontal, MarginValue bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    declaration(Property.MARGIN, top, horizontal, bottom);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration margin(MarginValue top, MarginValue right, MarginValue bottom, MarginValue left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    declaration(Property.MARGIN, top, right, bottom, left);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginBottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginBottom(MarginValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginLeft(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginLeft(MarginValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginRight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_RIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginRight(MarginValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_RIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginTop(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration marginTop(MarginValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MARGIN_TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration maxHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MAX_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration maxHeight(MaxHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MAX_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration maxWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MAX_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration maxWidth(MaxHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MAX_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minHeight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minHeight(MinHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minHeight(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minWidth(MinHeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration minWidth(PercentageValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.MIN_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozAppearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property._MOZ_APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozAppearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    declaration(Property._MOZ_APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozTabSize(int value) {
    declaration(Property._MOZ_TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozTabSize(IntLiteral value) {
    Check.notNull(value, "value == null");
    declaration(Property._MOZ_TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozTabSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property._MOZ_TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration mozTabSize(LengthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property._MOZ_TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final OpacityDeclaration opacity(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final OpacityDeclaration opacity(PercentageValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final OpacityDeclaration opacity(double value) {
    declaration(Property.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final OpacityDeclaration opacity(int value) {
    declaration(Property.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final OpacityDeclaration opacity(NumberValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outline(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outline(OutlineValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.OUTLINE, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outline(OutlineValue value1, OutlineValue value2, OutlineValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.OUTLINE, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineColor(ColorValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineOffset(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_OFFSET, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineOffset(LengthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_OFFSET, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineStyle(OutlineStyleValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineWidth(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration outlineWidth(LineWidth value) {
    Check.notNull(value, "value == null");
    declaration(Property.OUTLINE_WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration padding(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration padding(LengthPercentage all) {
    Check.notNull(all, "all == null");
    declaration(Property.PADDING, all);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration padding(LengthPercentage vertical, LengthPercentage horizontal) {
    Check.notNull(vertical, "vertical == null");
    Check.notNull(horizontal, "horizontal == null");
    declaration(Property.PADDING, vertical, horizontal);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage horizontal, LengthPercentage bottom) {
    Check.notNull(top, "top == null");
    Check.notNull(horizontal, "horizontal == null");
    Check.notNull(bottom, "bottom == null");
    declaration(Property.PADDING, top, horizontal, bottom);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration padding(LengthPercentage top, LengthPercentage right, LengthPercentage bottom, LengthPercentage left) {
    Check.notNull(top, "top == null");
    Check.notNull(right, "right == null");
    Check.notNull(bottom, "bottom == null");
    Check.notNull(left, "left == null");
    declaration(Property.PADDING, top, right, bottom, left);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingBottom(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingBottom(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_BOTTOM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingLeft(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingLeft(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_LEFT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingRight(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_RIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingRight(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_RIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingTop(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration paddingTop(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.PADDING_TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration pointerEvents(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.POINTER_EVENTS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration pointerEvents(PointerEventsValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.POINTER_EVENTS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration position(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration position(PositionValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.POSITION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration resize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.RESIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration resize(ResizeValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.RESIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration tabSize(int value) {
    declaration(Property.TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration tabSize(IntLiteral value) {
    Check.notNull(value, "value == null");
    declaration(Property.TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration tabSize(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration tabSize(LengthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TAB_SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textAlign(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textAlign(TextAlignValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecoration(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.TEXT_DECORATION, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.TEXT_DECORATION, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecoration(TextDecorationValue value1, TextDecorationValue value2, TextDecorationValue value3, TextDecorationValue value4) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    Check.notNull(value4, "value4 == null");
    declaration(Property.TEXT_DECORATION, value1, value2, value3, value4);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationColor(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationColor(ColorValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationLine(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_LINE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineSingleValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_LINE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.TEXT_DECORATION_LINE, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationLine(TextDecorationLineMultiValue value1, TextDecorationLineMultiValue value2, TextDecorationLineMultiValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.TEXT_DECORATION_LINE, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationStyle(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationStyle(TextDecorationStyleValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationThickness(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_THICKNESS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textDecorationThickness(TextDecorationThicknessValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_DECORATION_THICKNESS, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textIndent(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_INDENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textIndent(LengthPercentage value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_INDENT, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    declaration(Property.TEXT_INDENT, value1, value2);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textIndent(LengthPercentage value1, TextIndentValue value2, TextIndentValue value3) {
    Check.notNull(value1, "value1 == null");
    Check.notNull(value2, "value2 == null");
    Check.notNull(value3, "value3 == null");
    declaration(Property.TEXT_INDENT, value1, value2, value3);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textTransform(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_TRANSFORM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration textTransform(TextTransformValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TEXT_TRANSFORM, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration top(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration top(TopValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.TOP, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration verticalAlign(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.VERTICAL_ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration verticalAlign(VerticalAlignValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.VERTICAL_ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitAppearance(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitAppearance(AppearanceValue value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_APPEARANCE, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitFilter(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitFilter(FilterValue value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitTextSizeAdjust(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_TEXT_SIZE_ADJUST, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration webkitTextSizeAdjust(TextSizeAdjustValue value) {
    Check.notNull(value, "value == null");
    declaration(Property._WEBKIT_TEXT_SIZE_ADJUST, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration width(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration width(HeightOrWidthValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration wordBreak(GlobalKeyword value) {
    Check.notNull(value, "value == null");
    declaration(Property.WORD_BREAK, value);
    return InternalInstruction.INSTANCE;
  }

  protected final StyleDeclaration wordBreak(WordBreakValue value) {
    Check.notNull(value, "value == null");
    declaration(Property.WORD_BREAK, value);
    return InternalInstruction.INSTANCE;
  }

  abstract void declaration(Property name, PropertyValue value);

  abstract void declaration(Property name, PropertyValue value1, PropertyValue value2);

  abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3);

  abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4);

  abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4, PropertyValue value5);

  abstract void declaration(Property name, PropertyValue value1, PropertyValue value2, PropertyValue value3, PropertyValue value4, PropertyValue value5, PropertyValue value6);

  abstract void declaration(Property name, int value);

  abstract void declaration(Property name, double value);

  abstract void declaration(Property name, String value);
}
