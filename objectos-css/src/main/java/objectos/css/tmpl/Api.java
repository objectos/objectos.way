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

import objectos.css.internal.Combinator;
import objectos.css.internal.InternalInstruction;
import objectos.css.internal.InternalZero;
import objectos.css.internal.MediaType;
import objectos.css.internal.StandardName;
import objectos.css.internal.StandardPseudoClassSelector;
import objectos.css.internal.StandardPseudoElementSelector;
import objectos.css.internal.StandardTypeSelector;
import objectos.css.util.ClassSelector;
import objectos.css.util.Color;
import objectos.css.util.IdSelector;
import objectos.css.util.Length;
import objectos.css.util.Percentage;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public final class Api {
  private Api() {}

  public sealed interface MediaRuleElement {}

  public sealed interface MediaFeature extends MediaRuleElement {}

  public sealed interface MediaFeatureOrStyleDeclaration extends MediaFeature, StyleDeclaration permits InternalInstruction {}

  public sealed interface MediaQuery extends MediaRuleElement permits MediaType {}

  public sealed interface StyleRule extends MediaRuleElement permits InternalInstruction {}

  public sealed interface StyleRuleElement {}

  public sealed interface StyleDeclaration extends StyleRuleElement {}

  public sealed interface FilterFunction {}

  public sealed interface BoxShadowDeclaration extends StyleDeclaration {}

  public sealed interface BoxShadowHashDeclaration extends StyleDeclaration {}

  public sealed interface OpacityDeclaration extends FilterFunction, StyleDeclaration {}

  public sealed interface StyleDeclarationInstruction extends BoxShadowDeclaration, BoxShadowHashDeclaration, OpacityDeclaration permits InternalInstruction {}

  public sealed interface Selector extends StyleRuleElement {}

  public sealed interface SelectorInstruction extends Selector permits Combinator, InternalInstruction, StandardName, ClassSelector, IdSelector, StandardPseudoClassSelector, StandardPseudoElementSelector, StandardTypeSelector {}

  public sealed interface PropertyValue {}

  public sealed interface AppearanceValue extends PropertyValue {}

  public sealed interface BackgroundAttachmentValue extends BackgroundValue {}

  public sealed interface BackgroundClipValue extends BackgroundValue {}

  public sealed interface BackgroundImageValue extends BackgroundValue {}

  public sealed interface BackgroundPositionValue extends BackgroundValue {}

  public sealed interface BackgroundRepeatValue extends BackgroundValue {}

  public sealed interface BackgroundRepeatValue2 extends BackgroundRepeatValue {}

  public sealed interface BackgroundValue extends PropertyValue {}

  public sealed interface BorderCollapseValue extends PropertyValue {}

  public sealed interface BorderShorthandValue extends PropertyValue {}

  public sealed interface BottomValue extends PropertyValue {}

  public sealed interface BoxSizingValue extends PropertyValue {}

  public sealed interface ContentValue extends PropertyValue {}

  public sealed interface CounterStyleValue extends ListStyleTypeValue {}

  public sealed interface CursorValue extends PropertyValue {}

  public sealed interface DisplayBoxValue extends DisplayValue {}

  public sealed interface DisplayInsideValue extends DisplayValue, DisplayValue2 {}

  public sealed interface DisplayInternalValue extends DisplayValue {}

  public sealed interface DisplayLegacyValue extends DisplayValue {}

  public sealed interface DisplayListItemValue extends DisplayValue {}

  public sealed interface DisplayOutsideValue extends DisplayValue, DisplayValue2 {}

  public sealed interface DisplayValue extends PropertyValue {}

  public sealed interface DisplayValue2 extends PropertyValue {}

  public sealed interface FilterValue extends PropertyValue {}

  public sealed interface FlexDirectionValue extends PropertyValue {}

  public sealed interface FlexWrapValue extends PropertyValue {}

  public sealed interface FontFamilyValue extends PropertyValue {}

  public sealed interface FontFeatureSettingsValue extends PropertyValue {}

  public sealed interface FontSizeValue extends PropertyValue {}

  public sealed interface FontStyleValue extends PropertyValue {}

  public sealed interface FontValue extends PropertyValue {}

  public sealed interface FontVariationSettingsValue extends PropertyValue {}

  public sealed interface FontWeightValue extends PropertyValue {}

  public sealed interface GlobalKeyword extends PropertyValue {}

  public sealed interface HeightOrWidthValue extends PropertyValue {}

  public sealed interface Image extends ListStyleImageValue {}

  public sealed interface JustifyContentPosition extends PropertyValue {}

  public sealed interface JustifyContentValue extends PropertyValue {}

  public sealed interface LeftValue extends PropertyValue {}

  public sealed interface LengthPercentage extends BottomValue, FontSizeValue, HeightOrWidthValue, LeftValue, LetterSpacingValue, LineHeightValue, MarginValue, MaxHeightOrWidthValue, TextDecorationThicknessValue, TopValue, VerticalAlignValue {}

  public sealed interface LetterSpacingValue extends PropertyValue {}

  public sealed interface LineHeightValue extends PropertyValue {}

  public sealed interface LineStyle extends BorderShorthandValue {}

  public sealed interface LineWidth extends BorderShorthandValue, OutlineValue {}

  public sealed interface ListStyleImageValue extends ListStyleValue {}

  public sealed interface ListStylePositionValue extends ListStyleValue {}

  public sealed interface ListStyleTypeValue extends ListStyleValue {}

  public sealed interface ListStyleValue extends PropertyValue {}

  public sealed interface MarginValue extends PropertyValue {}

  public sealed interface MaxHeightOrWidthValue extends PropertyValue {}

  public sealed interface MinHeightOrWidthValue extends PropertyValue {}

  public sealed interface NumberValue extends PropertyValue {}

  public sealed interface OutlineStyleValue extends OutlineValue {}

  public sealed interface OutlineValue extends PropertyValue {}

  public sealed interface OverflowPosition extends PropertyValue {}

  public sealed interface PointerEventsValue extends PropertyValue {}

  public sealed interface PositionValue extends PropertyValue {}

  public sealed interface ResizeValue extends PropertyValue {}

  public sealed interface TextAlignValue extends PropertyValue {}

  public sealed interface TextDecorationLineMultiValue extends TextDecorationLineSingleValue {}

  public sealed interface TextDecorationLineSingleValue extends TextDecorationValue {}

  public sealed interface TextDecorationStyleValue extends TextDecorationValue {}

  public sealed interface TextDecorationThicknessValue extends TextDecorationValue {}

  public sealed interface TextDecorationValue extends PropertyValue {}

  public sealed interface TextIndentValue extends PropertyValue {}

  public sealed interface TextSizeAdjustValue extends PropertyValue {}

  public sealed interface TextTransformValue extends PropertyValue {}

  public sealed interface TopValue extends PropertyValue {}

  public sealed interface VerticalAlignValue extends PropertyValue {}

  public sealed interface WordBreakValue extends PropertyValue {}

  public sealed interface ValueInstruction extends
      AppearanceValue,
      BackgroundAttachmentValue,
      BackgroundClipValue,
      BackgroundImageValue,
      BackgroundPositionValue,
      BackgroundRepeatValue,
      BackgroundRepeatValue2,
      BackgroundValue,
      BorderCollapseValue,
      BorderShorthandValue,
      BottomValue,
      BoxSizingValue,
      ContentValue,
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
      FilterValue,
      FlexDirectionValue,
      FlexWrapValue,
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
      LeftValue,
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
      NumberValue,
      OutlineStyleValue,
      OutlineValue,
      OverflowPosition,
      PointerEventsValue,
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
      VerticalAlignValue,
      WordBreakValue permits StandardName {}

  public sealed interface AutoKeyword extends AppearanceValue, BottomValue, CursorValue, HeightOrWidthValue, LeftValue, MarginValue, MinHeightOrWidthValue, OutlineStyleValue, PointerEventsValue, TextDecorationThicknessValue, TextSizeAdjustValue, TopValue {}

  public sealed interface BlockKeyword extends DisplayOutsideValue, ResizeValue {}

  public sealed interface BorderBoxKeyword extends BackgroundClipValue, BoxSizingValue {}

  public sealed interface BottomKeyword extends BackgroundPositionValue, VerticalAlignValue {}

  public sealed interface ButtonKeyword extends AppearanceValue, Selector {}

  public sealed interface CenterKeyword extends BackgroundPositionValue, JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  public sealed interface ContentBoxKeyword extends BackgroundClipValue, BoxSizingValue {}

  public sealed interface DashedKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  public sealed interface DottedKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  public sealed interface DoubleKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  public sealed interface EndKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  public sealed interface FitContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  public sealed interface FixedKeyword extends BackgroundAttachmentValue, PositionValue {}

  public sealed interface FlexEndKeyword extends JustifyContentPosition, JustifyContentValue {}

  public sealed interface FlexStartKeyword extends JustifyContentPosition, JustifyContentValue {}

  public sealed interface GrooveKeyword extends LineStyle, OutlineStyleValue {}

  public sealed interface InlineKeyword extends DisplayOutsideValue, ResizeValue {}

  public sealed interface InsetKeyword extends LineStyle, OutlineStyleValue {}

  public sealed interface LeftKeyword extends BackgroundPositionValue, JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  public sealed interface MaxContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  public sealed interface MediumKeyword extends FontSizeValue, LineWidth {}

  public sealed interface MenuKeyword extends FontValue, Selector {}

  public sealed interface MinContentKeyword extends HeightOrWidthValue, MaxHeightOrWidthValue, MinHeightOrWidthValue {}

  public sealed interface NoneKeyword extends AppearanceValue, BackgroundImageValue, ContentValue, CursorValue, DisplayBoxValue, FilterValue, LineStyle, ListStyleImageValue, ListStyleTypeValue, MaxHeightOrWidthValue, OutlineStyleValue, PointerEventsValue, ResizeValue, TextDecorationLineSingleValue, TextSizeAdjustValue, TextTransformValue {}

  public sealed interface NormalKeyword extends ContentValue, FontFeatureSettingsValue, FontStyleValue, FontVariationSettingsValue, FontWeightValue, JustifyContentValue, LetterSpacingValue, LineHeightValue, WordBreakValue {}

  public sealed interface OutsetKeyword extends LineStyle, OutlineStyleValue {}

  public sealed interface ProgressKeyword extends CursorValue, Selector {}

  public sealed interface RidgeKeyword extends LineStyle, OutlineStyleValue {}

  public sealed interface RightKeyword extends BackgroundPositionValue, JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  public sealed interface SmallKeyword extends FontSizeValue, Selector {}

  public sealed interface SolidKeyword extends LineStyle, OutlineStyleValue, TextDecorationStyleValue {}

  public sealed interface StartKeyword extends JustifyContentPosition, JustifyContentValue, TextAlignValue {}

  public sealed interface SubKeyword extends Selector, VerticalAlignValue {}

  public sealed interface TableKeyword extends DisplayInsideValue, Selector {}

  public sealed interface TextareaKeyword extends AppearanceValue, Selector {}

  public sealed interface TopKeyword extends BackgroundPositionValue, VerticalAlignValue {}

  public sealed interface KeywordInstruction extends
      AutoKeyword,
      BlockKeyword,
      BorderBoxKeyword,
      BottomKeyword,
      ButtonKeyword,
      CenterKeyword,
      ContentBoxKeyword,
      DashedKeyword,
      DottedKeyword,
      DoubleKeyword,
      EndKeyword,
      FitContentKeyword,
      FixedKeyword,
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
      TextareaKeyword,
      TopKeyword permits StandardName {}

  public sealed interface ColorValue extends BackgroundValue, BorderShorthandValue, OutlineValue, TextDecorationValue permits Color, InternalInstruction, StandardName {}

  public sealed interface LengthValue extends BackgroundPositionValue, LengthPercentage, LineWidth permits InternalInstruction, Length, Zero {}

  public sealed interface PercentageValue extends BackgroundPositionValue, LengthPercentage, TextSizeAdjustValue permits InternalInstruction, Percentage, Zero {}

  public sealed interface StringLiteral extends FontFamilyValue, ListStyleTypeValue permits InternalInstruction {}

  public sealed interface DoubleLiteral extends LineHeightValue, NumberValue permits InternalInstruction {}

  public sealed interface IntLiteral extends FontWeightValue, LineHeightValue, NumberValue permits InternalInstruction {}

  public sealed interface Url extends FilterValue, Image permits InternalInstruction {}

  public sealed interface Zero extends LengthValue, PercentageValue permits InternalZero {}
}
