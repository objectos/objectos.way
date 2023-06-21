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
import objectos.css.tmpl.BackgroundImageValue;
import objectos.css.tmpl.BlockKeyword;
import objectos.css.tmpl.BorderCollapseValue;
import objectos.css.tmpl.BottomValue;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.ButtonKeyword;
import objectos.css.tmpl.CounterStyleValue;
import objectos.css.tmpl.DashedKeyword;
import objectos.css.tmpl.DisplayBoxValue;
import objectos.css.tmpl.DisplayInsideValue;
import objectos.css.tmpl.DisplayInternalValue;
import objectos.css.tmpl.DisplayLegacyValue;
import objectos.css.tmpl.DisplayListItemValue;
import objectos.css.tmpl.DisplayOutsideValue;
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
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.ListStyleImageValue;
import objectos.css.tmpl.ListStylePositionValue;
import objectos.css.tmpl.ListStyleTypeValue;
import objectos.css.tmpl.MarginValue;
import objectos.css.tmpl.MediumKeyword;
import objectos.css.tmpl.MenuKeyword;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.OutlineStyleValue;
import objectos.css.tmpl.OutsetKeyword;
import objectos.css.tmpl.PositionValue;
import objectos.css.tmpl.ResizeValue;
import objectos.css.tmpl.RidgeKeyword;
import objectos.css.tmpl.SmallKeyword;
import objectos.css.tmpl.SolidKeyword;
import objectos.css.tmpl.SubKeyword;
import objectos.css.tmpl.TableKeyword;
import objectos.css.tmpl.TextDecorationLineMultiValue;
import objectos.css.tmpl.TextDecorationLineSingleValue;
import objectos.css.tmpl.TextDecorationStyleValue;
import objectos.css.tmpl.TextDecorationThicknessValue;
import objectos.css.tmpl.TextIndentValue;
import objectos.css.tmpl.TextSizeAdjustValue;
import objectos.css.tmpl.TextTransformValue;
import objectos.css.tmpl.TextareaKeyword;
import objectos.css.tmpl.TopValue;
import objectos.css.tmpl.VerticalAlignValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public final class NamedElement implements Selector,
    AppearanceValue,
    BackgroundImageValue,
    BorderCollapseValue,
    BottomValue,
    BoxSizingValue,
    CounterStyleValue,
    DisplayBoxValue,
    DisplayInsideValue,
    DisplayInternalValue,
    DisplayLegacyValue,
    DisplayListItemValue,
    DisplayOutsideValue,
    FontFamilyValue,
    FontFeatureSettingsValue,
    FontSizeValue,
    FontValue,
    FontVariationSettingsValue,
    FontWeightValue,
    GlobalKeyword,
    HeightValue,
    LineHeightValue,
    LineStyle,
    LineWidth,
    ListStyleImageValue,
    ListStylePositionValue,
    ListStyleTypeValue,
    MarginValue,
    OutlineStyleValue,
    PositionValue,
    ResizeValue,
    TextDecorationLineMultiValue,
    TextDecorationLineSingleValue,
    TextDecorationStyleValue,
    TextDecorationThicknessValue,
    TextIndentValue,
    TextSizeAdjustValue,
    TextTransformValue,
    TopValue,
    VerticalAlignValue,
    AutoKeyword,
    BlockKeyword,
    ButtonKeyword,
    DashedKeyword,
    DottedKeyword,
    DoubleKeyword,
    GrooveKeyword,
    InlineKeyword,
    InsetKeyword,
    MediumKeyword,
    MenuKeyword,
    NoneKeyword,
    NormalKeyword,
    OutsetKeyword,
    RidgeKeyword,
    SmallKeyword,
    SolidKeyword,
    SubKeyword,
    TableKeyword,
    TextareaKeyword {
  private final String name;

  public NamedElement(String name) {
    this.name = name;
  }

  @Override
  public final String toString() {
    return name;
  }
}
