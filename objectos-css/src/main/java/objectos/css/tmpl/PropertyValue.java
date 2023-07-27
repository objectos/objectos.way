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
package objectos.css.tmpl;

import objectos.css.internal.InternalInstruction;
import objectos.css.internal.InternalZero;
import objectos.css.internal.StandardName;
import objectos.css.om.Selector;
import objectos.css.util.Color;
import objectos.css.util.Length;
import objectos.css.util.Percentage;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public sealed interface PropertyValue {
  sealed interface AppearanceValue extends PropertyValue {}

  sealed interface BackgroundImageValue extends PropertyValue {}

  sealed interface BorderCollapseValue extends PropertyValue {}

  sealed interface BottomValue extends PropertyValue {}

  sealed interface BoxSizingValue extends PropertyValue {}

  sealed interface CounterStyleValue extends ListStyleTypeValue {}

  sealed interface CursorValue extends PropertyValue {}

  sealed interface DisplayBoxValue extends DisplayValue {}

  sealed interface DisplayInsideValue extends DisplayValue, DisplayValue2 {}

  sealed interface DisplayInternalValue extends DisplayValue {}

  sealed interface DisplayLegacyValue extends DisplayValue {}

  sealed interface DisplayListItemValue extends DisplayValue {}

  sealed interface DisplayOutsideValue extends DisplayValue, DisplayValue2 {}

  sealed interface DisplayValue extends PropertyValue {}

  sealed interface DisplayValue2 extends PropertyValue {}

  sealed interface FlexDirectionValue extends PropertyValue {}

  sealed interface FontFamilyValue extends PropertyValue {}

  sealed interface FontFeatureSettingsValue extends PropertyValue {}

  sealed interface FontSizeValue extends PropertyValue {}

  sealed interface FontStyleValue extends PropertyValue {}

  sealed interface FontValue extends PropertyValue {}

  sealed interface FontVariationSettingsValue extends PropertyValue {}

  sealed interface FontWeightValue extends PropertyValue {}

  sealed interface GlobalKeyword extends PropertyValue {}

  sealed interface HeightOrWidthValue extends PropertyValue {}

  sealed interface Image extends ListStyleImageValue {}

  sealed interface JustifyContentPosition extends PropertyValue {}

  sealed interface JustifyContentValue extends PropertyValue {}

  sealed interface LengthPercentage extends BottomValue, FontSizeValue, HeightOrWidthValue, LetterSpacingValue, LineHeightValue, MarginValue, MaxHeightOrWidthValue, TextDecorationThicknessValue, TopValue, VerticalAlignValue {}

  sealed interface LetterSpacingValue extends PropertyValue {}

  sealed interface LineHeightValue extends PropertyValue {}

  sealed interface LineStyle extends PropertyValue {}

  sealed interface LineWidth extends OutlineValue {}

  sealed interface ListStyleImageValue extends ListStyleValue {}

  sealed interface ListStylePositionValue extends ListStyleValue {}

  sealed interface ListStyleTypeValue extends ListStyleValue {}

  sealed interface ListStyleValue extends PropertyValue {}

  sealed interface MarginValue extends PropertyValue {}

  sealed interface MaxHeightOrWidthValue extends PropertyValue {}

  sealed interface MinHeightOrWidthValue extends PropertyValue {}

  sealed interface OutlineStyleValue extends OutlineValue {}

  sealed interface OutlineValue extends PropertyValue {}

  sealed interface OverflowPosition extends PropertyValue {}

  sealed interface PositionValue extends PropertyValue {}

  sealed interface ResizeValue extends PropertyValue {}

  sealed interface TextAlignValue extends PropertyValue {}

  sealed interface TextDecorationLineMultiValue extends TextDecorationLineSingleValue {}

  sealed interface TextDecorationLineSingleValue extends TextDecorationValue {}

  sealed interface TextDecorationStyleValue extends TextDecorationValue {}

  sealed interface TextDecorationThicknessValue extends TextDecorationValue {}

  sealed interface TextDecorationValue extends PropertyValue {}

  sealed interface TextIndentValue extends PropertyValue {}

  sealed interface TextSizeAdjustValue extends PropertyValue {}

  sealed interface TextTransformValue extends PropertyValue {}

  sealed interface TopValue extends PropertyValue {}

  sealed interface VerticalAlignValue extends PropertyValue {}

  sealed interface ValueInstruction extends
      AppearanceValue,
      BackgroundImageValue,
      BorderCollapseValue,
      BottomValue,
      BoxSizingValue,
      CounterStyleValue,
      CursorValue,
      DisplayBoxValue,
      DisplayInsideValue,
      DisplayInternalValue,
      DisplayLegacyValue,
      DisplayListItemValue,
      DisplayOutsideValue,
      DisplayValue,
      DisplayValue2,
      FlexDirectionValue,
      FontFamilyValue,
      FontFeatureSettingsValue,
      FontSizeValue,
      FontStyleValue,
      FontValue,
      FontVariationSettingsValue,
      FontWeightValue,
      GlobalKeyword,
      HeightOrWidthValue,
      Image,
      JustifyContentPosition,
      JustifyContentValue,
      LengthPercentage,
      LetterSpacingValue,
      LineHeightValue,
      LineStyle,
      LineWidth,
      ListStyleImageValue,
      ListStylePositionValue,
      ListStyleTypeValue,
      ListStyleValue,
      MarginValue,
      MaxHeightOrWidthValue,
      MinHeightOrWidthValue,
      OutlineStyleValue,
      OutlineValue,
      OverflowPosition,
      PositionValue,
      ResizeValue,
      TextAlignValue,
      TextDecorationLineMultiValue,
      TextDecorationLineSingleValue,
      TextDecorationStyleValue,
      TextDecorationThicknessValue,
      TextDecorationValue,
      TextIndentValue,
      TextSizeAdjustValue,
      TextTransformValue,
      TopValue,
      VerticalAlignValue permits StandardName {}

  sealed interface AutoKeyword extends AppearanceValue, BottomValue, CursorValue, HeightOrWidthValue, MarginValue, MinHeightOrWidthValue, OutlineStyleValue, TextDecorationThicknessValue, TextSizeAdjustValue, TopValue {}

  sealed interface BlockKeyword extends DisplayOutsideValue, ResizeValue {}

  sealed interface ButtonKeyword extends AppearanceValue, Selector {}

  sealed interface CenterKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  sealed interface DashedKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  sealed interface DottedKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  sealed interface DoubleKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  sealed interface EndKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  sealed interface FitContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  sealed interface FlexEndKeyword extends JustifyContentPosition, JustifyContentValue {}

  sealed interface FlexStartKeyword extends JustifyContentPosition, JustifyContentValue {}

  sealed interface GrooveKeyword extends LineStyle, OutlineStyleValue {}

  sealed interface InlineKeyword extends DisplayOutsideValue, ResizeValue {}

  sealed interface InsetKeyword extends LineStyle, OutlineStyleValue {}

  sealed interface LeftKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  sealed interface MaxContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  sealed interface MediumKeyword extends FontSizeValue, LineWidth {}

  sealed interface MenuKeyword extends FontValue, Selector {}

  sealed interface MinContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  sealed interface NoneKeyword extends AppearanceValue, BackgroundImageValue, CursorValue, DisplayBoxValue, LineStyle, ListStyleImageValue, ListStyleTypeValue, MaxHeightOrWidthValue, OutlineStyleValue, ResizeValue, TextDecorationLineSingleValue, TextSizeAdjustValue, TextTransformValue {}

  sealed interface NormalKeyword extends FontFeatureSettingsValue, FontStyleValue, FontVariationSettingsValue, FontWeightValue, JustifyContentValue, LetterSpacingValue, LineHeightValue {}

  sealed interface OutsetKeyword extends LineStyle, OutlineStyleValue {}

  sealed interface ProgressKeyword extends CursorValue, Selector {}

  sealed interface RidgeKeyword extends LineStyle, OutlineStyleValue {}

  sealed interface RightKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  sealed interface SmallKeyword extends FontSizeValue, Selector {}

  sealed interface SolidKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  sealed interface StartKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  sealed interface SubKeyword extends Selector, VerticalAlignValue {}

  sealed interface TableKeyword extends DisplayInsideValue, Selector {}

  sealed interface TextareaKeyword extends AppearanceValue, Selector {}

  sealed interface KeywordInstruction extends
      AutoKeyword,
      BlockKeyword,
      ButtonKeyword,
      CenterKeyword,
      DashedKeyword,
      DottedKeyword,
      DoubleKeyword,
      EndKeyword,
      FitContentKeyword,
      FlexEndKeyword,
      FlexStartKeyword,
      GrooveKeyword,
      InlineKeyword,
      InsetKeyword,
      LeftKeyword,
      MaxContentKeyword,
      MediumKeyword,
      MenuKeyword,
      MinContentKeyword,
      NoneKeyword,
      NormalKeyword,
      OutsetKeyword,
      ProgressKeyword,
      RidgeKeyword,
      RightKeyword,
      SmallKeyword,
      SolidKeyword,
      StartKeyword,
      SubKeyword,
      TableKeyword,
      TextareaKeyword permits StandardName {}

  sealed interface ColorValue extends OutlineValue, TextDecorationValue permits Color, InternalInstruction, StandardName {}

  sealed interface LengthValue extends LengthPercentage, LineWidth permits InternalInstruction, Length, Zero {}

  sealed interface PercentageValue extends LengthPercentage, TextSizeAdjustValue permits InternalInstruction, Percentage, Zero {}

  sealed interface StringLiteral extends FontFamilyValue, ListStyleTypeValue permits InternalInstruction {}

  sealed interface Url extends Image permits InternalInstruction {}

  sealed interface Zero extends LengthValue, PercentageValue permits InternalZero {}
}
