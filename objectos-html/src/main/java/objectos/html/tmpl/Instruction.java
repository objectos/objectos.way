/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.tmpl;

import java.util.Set;
import objectos.html.HtmlTemplate;
import objectos.html.internal.InternalInstruction;

public sealed interface Instruction {
  sealed interface AnchorInstruction extends Instruction {}

  sealed interface AbbreviationInstruction extends Instruction {}

  sealed interface ArticleInstruction extends Instruction {}

  sealed interface BringAttentionToInstruction extends Instruction {}

  sealed interface BlockquoteInstruction extends Instruction {}

  sealed interface BodyInstruction extends Instruction {}

  sealed interface LineBreakInstruction extends Instruction {}

  sealed interface ButtonInstruction extends Instruction {}

  sealed interface ClipPathInstruction extends Instruction {}

  sealed interface CodeInstruction extends Instruction {}

  sealed interface DefinitionDescriptionInstruction extends Instruction {}

  sealed interface DefsInstruction extends Instruction {}

  sealed interface DetailsInstruction extends Instruction {}

  sealed interface DivInstruction extends Instruction {}

  sealed interface DefinitionListInstruction extends Instruction {}

  sealed interface DefinitionTermInstruction extends Instruction {}

  sealed interface EmphasisInstruction extends Instruction {}

  sealed interface FieldsetInstruction extends Instruction {}

  sealed interface FigureInstruction extends Instruction {}

  sealed interface FooterInstruction extends Instruction {}

  sealed interface FormInstruction extends Instruction {}

  sealed interface GInstruction extends Instruction {}

  sealed interface Heading1Instruction extends Instruction {}

  sealed interface Heading2Instruction extends Instruction {}

  sealed interface Heading3Instruction extends Instruction {}

  sealed interface Heading4Instruction extends Instruction {}

  sealed interface Heading5Instruction extends Instruction {}

  sealed interface Heading6Instruction extends Instruction {}

  sealed interface HeadInstruction extends Instruction {}

  sealed interface HeaderInstruction extends Instruction {}

  sealed interface HeadingGroupInstruction extends Instruction {}

  sealed interface HorizontalRuleInstruction extends Instruction {}

  sealed interface HtmlInstruction extends Instruction {}

  sealed interface ImageInstruction extends Instruction {}

  sealed interface InputInstruction extends Instruction {}

  sealed interface KeyboardInputInstruction extends Instruction {}

  sealed interface LabelInstruction extends Instruction {}

  sealed interface LegendInstruction extends Instruction {}

  sealed interface ListItemInstruction extends Instruction {}

  sealed interface LinkInstruction extends Instruction {}

  sealed interface MainInstruction extends Instruction {}

  sealed interface MenuInstruction extends Instruction {}

  sealed interface MetaInstruction extends Instruction {}

  sealed interface NavInstruction extends Instruction {}

  sealed interface OrderedListInstruction extends Instruction {}

  sealed interface OptionGroupInstruction extends Instruction {}

  sealed interface OptionInstruction extends Instruction {}

  sealed interface ParagraphInstruction extends Instruction {}

  sealed interface PathInstruction extends Instruction {}

  sealed interface PreInstruction extends Instruction {}

  sealed interface ProgressInstruction extends Instruction {}

  sealed interface SampleOutputInstruction extends Instruction {}

  sealed interface ScriptInstruction extends Instruction {}

  sealed interface SectionInstruction extends Instruction {}

  sealed interface SelectInstruction extends Instruction {}

  sealed interface SmallInstruction extends Instruction {}

  sealed interface SpanInstruction extends Instruction {}

  sealed interface StrongInstruction extends Instruction {}

  sealed interface StyleInstruction extends Instruction {}

  sealed interface SubscriptInstruction extends Instruction {}

  sealed interface SummaryInstruction extends Instruction {}

  sealed interface SuperscriptInstruction extends Instruction {}

  sealed interface SvgInstruction extends Instruction {}

  sealed interface TableInstruction extends Instruction {}

  sealed interface TableBodyInstruction extends Instruction {}

  sealed interface TableDataInstruction extends Instruction {}

  sealed interface TemplateInstruction extends Instruction {}

  sealed interface TextAreaInstruction extends Instruction {}

  sealed interface TableHeaderInstruction extends Instruction {}

  sealed interface TableHeadInstruction extends Instruction {}

  sealed interface TitleInstruction extends Instruction {}

  sealed interface TableRowInstruction extends Instruction {}

  sealed interface UnorderedListInstruction extends Instruction {}

  sealed interface AlignmentBaselineAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface AutocompleteAttribute extends SelectInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface BaselineShiftAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ClipPathAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ClipRuleAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ColorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ColorInterpolationAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ColorInterpolationFiltersAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface CrossoriginAttribute extends LinkInstruction, ScriptInstruction permits InternalInstruction {}

  sealed interface CursorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface DAttribute extends ClipPathInstruction, PathInstruction permits InternalInstruction {}

  sealed interface DirectionAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface DisabledAttribute extends OptionInstruction, SelectInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface DisplayAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface DominantBaselineAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FillAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FillOpacityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FillRuleAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FilterAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FloodColorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FloodOpacityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontFamilyAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontSizeAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontSizeAdjustAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontStretchAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontStyleAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontVariantAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FontWeightAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface FormAttribute extends SelectInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface GlyphOrientationHorizontalAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface GlyphOrientationVerticalAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface HeightAttribute extends ImageInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface HrefAttribute extends AnchorInstruction, LinkInstruction permits InternalInstruction {}

  sealed interface ImageRenderingAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface LetterSpacingAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface LightingColorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface MarkerEndAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface MarkerMidAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface MarkerStartAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface MaskAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface MaskTypeAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface NameAttribute extends FormInstruction, InputInstruction, MetaInstruction, SelectInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface OpacityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface OverflowAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface PaintOrderAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface PlaceholderAttribute extends InputInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface PointerEventsAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ReadonlyAttribute extends InputInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface ReferrerpolicyAttribute extends LinkInstruction, ScriptInstruction permits InternalInstruction {}

  sealed interface RequiredAttribute extends InputInstruction, SelectInstruction, TextAreaInstruction permits InternalInstruction {}

  sealed interface ShapeRenderingAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface SrcAttribute extends ImageInstruction, ScriptInstruction permits InternalInstruction {}

  sealed interface StopColorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StopOpacityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeDasharrayAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeDashoffsetAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeLinecapAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeLinejoinAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeMiterlimitAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeOpacityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface StrokeWidthAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TargetAttribute extends AnchorInstruction, FormInstruction permits InternalInstruction {}

  sealed interface TextAnchorAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TextDecorationAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TextOverflowAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TextRenderingAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TransformAttribute extends ClipPathInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TransformOriginAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface TypeAttribute extends ButtonInstruction, InputInstruction, LinkInstruction, OrderedListInstruction, ScriptInstruction, StyleInstruction permits InternalInstruction {}

  sealed interface UnicodeBidiAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface ValueAttribute extends InputInstruction, OptionInstruction permits InternalInstruction {}

  sealed interface VectorEffectAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface VisibilityAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface WhiteSpaceAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface WidthAttribute extends ImageInstruction, SvgInstruction, TableInstruction permits InternalInstruction {}

  sealed interface WordSpacingAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface WritingModeAttribute extends ClipPathInstruction, DefsInstruction, GInstruction, PathInstruction, SvgInstruction permits InternalInstruction {}

  sealed interface AmbiguousInstruction extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction permits InternalInstruction {}

  sealed interface GlobalAttribute extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction permits ExternalAttribute, InternalInstruction {}

  sealed interface ExternalAttribute extends GlobalAttribute {
    non-sealed interface Id extends ExternalAttribute {
      String value();
    }

    non-sealed interface StyleClass extends ExternalAttribute {
      String value();
    }

    non-sealed interface StyleClassSet extends ExternalAttribute {
      Set<String> value();
    }
  }

  sealed interface ElementContents extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HtmlInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      MainInstruction,
      MenuInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction permits HtmlTemplate, InternalInstruction {}

  @FunctionalInterface
  non-sealed interface Fragment0 extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction {
    void execute();
  }

  @FunctionalInterface
  non-sealed interface Fragment1<T1> extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction {
    void execute(T1 arg1);
  }

  @FunctionalInterface
  non-sealed interface Fragment2<T1, T2> extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction {
    void execute(T1 arg1, T2 arg2);
  }

  sealed interface NoOpInstruction extends
      AnchorInstruction,
      AbbreviationInstruction,
      ArticleInstruction,
      BringAttentionToInstruction,
      BlockquoteInstruction,
      BodyInstruction,
      LineBreakInstruction,
      ButtonInstruction,
      ClipPathInstruction,
      CodeInstruction,
      DefinitionDescriptionInstruction,
      DefsInstruction,
      DetailsInstruction,
      DivInstruction,
      DefinitionListInstruction,
      DefinitionTermInstruction,
      EmphasisInstruction,
      FieldsetInstruction,
      FigureInstruction,
      FooterInstruction,
      FormInstruction,
      GInstruction,
      Heading1Instruction,
      Heading2Instruction,
      Heading3Instruction,
      Heading4Instruction,
      Heading5Instruction,
      Heading6Instruction,
      HeadInstruction,
      HeaderInstruction,
      HeadingGroupInstruction,
      HorizontalRuleInstruction,
      HtmlInstruction,
      ImageInstruction,
      InputInstruction,
      KeyboardInputInstruction,
      LabelInstruction,
      LegendInstruction,
      ListItemInstruction,
      LinkInstruction,
      MainInstruction,
      MenuInstruction,
      MetaInstruction,
      NavInstruction,
      OrderedListInstruction,
      OptionGroupInstruction,
      OptionInstruction,
      ParagraphInstruction,
      PathInstruction,
      PreInstruction,
      ProgressInstruction,
      SampleOutputInstruction,
      ScriptInstruction,
      SectionInstruction,
      SelectInstruction,
      SmallInstruction,
      SpanInstruction,
      StrongInstruction,
      StyleInstruction,
      SubscriptInstruction,
      SummaryInstruction,
      SuperscriptInstruction,
      SvgInstruction,
      TableInstruction,
      TableBodyInstruction,
      TableDataInstruction,
      TemplateInstruction,
      TextAreaInstruction,
      TableHeaderInstruction,
      TableHeadInstruction,
      TitleInstruction,
      TableRowInstruction,
      UnorderedListInstruction permits InternalInstruction {}
}
