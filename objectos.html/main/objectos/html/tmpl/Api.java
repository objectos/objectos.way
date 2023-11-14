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

import java.util.Iterator;

/**
 * Provides the interfaces of the {@link objectos.html.HtmlTemplate} domain-specific language.
 */
// Generated by selfgen.html.HtmlSpec. Do not edit!
public final class Api {
  public static final Attribute ATTRIBUTE = new Attribute();

  public static final Element ELEMENT = new Element();

  public static final Fragment FRAGMENT = new Fragment();

  public static final NoOp NOOP = new NoOp();

  private Api () {}

  /**
   * Represents an instruction that generates part of the output of an HTML template.
   *
   * <p>
   * Unless noted references to a particular instruction MUST NOT be reused.
   */
  public sealed interface Instruction {}

  /**
   * Allowed as a child of the {@code a} element.
   */
  public sealed interface AnchorInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code abbr} element.
   */
  public sealed interface AbbreviationInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code article} element.
   */
  public sealed interface ArticleInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code b} element.
   */
  public sealed interface BringAttentionToInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code blockquote} element.
   */
  public sealed interface BlockquoteInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code body} element.
   */
  public sealed interface BodyInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code br} element.
   */
  public sealed interface LineBreakInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code button} element.
   */
  public sealed interface ButtonInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code clipPath} element.
   */
  public sealed interface ClipPathInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code code} element.
   */
  public sealed interface CodeInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code dd} element.
   */
  public sealed interface DefinitionDescriptionInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code defs} element.
   */
  public sealed interface DefsInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code details} element.
   */
  public sealed interface DetailsInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code div} element.
   */
  public sealed interface DivInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code dl} element.
   */
  public sealed interface DefinitionListInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code dt} element.
   */
  public sealed interface DefinitionTermInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code em} element.
   */
  public sealed interface EmphasisInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code fieldset} element.
   */
  public sealed interface FieldsetInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code figure} element.
   */
  public sealed interface FigureInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code footer} element.
   */
  public sealed interface FooterInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code form} element.
   */
  public sealed interface FormInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code g} element.
   */
  public sealed interface GInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h1} element.
   */
  public sealed interface Heading1Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h2} element.
   */
  public sealed interface Heading2Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h3} element.
   */
  public sealed interface Heading3Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h4} element.
   */
  public sealed interface Heading4Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h5} element.
   */
  public sealed interface Heading5Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code h6} element.
   */
  public sealed interface Heading6Instruction extends Instruction {}

  /**
   * Allowed as a child of the {@code head} element.
   */
  public sealed interface HeadInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code header} element.
   */
  public sealed interface HeaderInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code hgroup} element.
   */
  public sealed interface HeadingGroupInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code hr} element.
   */
  public sealed interface HorizontalRuleInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code html} element.
   */
  public sealed interface HtmlInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code img} element.
   */
  public sealed interface ImageInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code input} element.
   */
  public sealed interface InputInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code kbd} element.
   */
  public sealed interface KeyboardInputInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code label} element.
   */
  public sealed interface LabelInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code legend} element.
   */
  public sealed interface LegendInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code li} element.
   */
  public sealed interface ListItemInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code link} element.
   */
  public sealed interface LinkInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code main} element.
   */
  public sealed interface MainInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code menu} element.
   */
  public sealed interface MenuInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code meta} element.
   */
  public sealed interface MetaInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code nav} element.
   */
  public sealed interface NavInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code ol} element.
   */
  public sealed interface OrderedListInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code optgroup} element.
   */
  public sealed interface OptionGroupInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code option} element.
   */
  public sealed interface OptionInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code p} element.
   */
  public sealed interface ParagraphInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code path} element.
   */
  public sealed interface PathInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code pre} element.
   */
  public sealed interface PreInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code progress} element.
   */
  public sealed interface ProgressInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code samp} element.
   */
  public sealed interface SampleOutputInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code script} element.
   */
  public sealed interface ScriptInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code section} element.
   */
  public sealed interface SectionInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code select} element.
   */
  public sealed interface SelectInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code small} element.
   */
  public sealed interface SmallInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code span} element.
   */
  public sealed interface SpanInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code strong} element.
   */
  public sealed interface StrongInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code style} element.
   */
  public sealed interface StyleInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code sub} element.
   */
  public sealed interface SubscriptInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code summary} element.
   */
  public sealed interface SummaryInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code sup} element.
   */
  public sealed interface SuperscriptInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code svg} element.
   */
  public sealed interface SvgInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code table} element.
   */
  public sealed interface TableInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code tbody} element.
   */
  public sealed interface TableBodyInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code td} element.
   */
  public sealed interface TableDataInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code template} element.
   */
  public sealed interface TemplateInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code textarea} element.
   */
  public sealed interface TextAreaInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code th} element.
   */
  public sealed interface TableHeaderInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code thead} element.
   */
  public sealed interface TableHeadInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code title} element.
   */
  public sealed interface TitleInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code tr} element.
   */
  public sealed interface TableRowInstruction extends Instruction {}

  /**
   * Allowed as a child of the {@code ul} element.
   */
  public sealed interface UnorderedListInstruction extends Instruction {}


  /**
   * The {@code alignment-baseline} attribute.
   */
  public sealed interface AlignmentBaselineAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code autocomplete} attribute.
   */
  public sealed interface AutocompleteAttribute
      extends
      SelectInstruction,
      TextAreaInstruction {}

  /**
   * The {@code baseline-shift} attribute.
   */
  public sealed interface BaselineShiftAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code clip-path} attribute.
   */
  public sealed interface ClipPathAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code clip-rule} attribute.
   */
  public sealed interface ClipRuleAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code color} attribute.
   */
  public sealed interface ColorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code color-interpolation} attribute.
   */
  public sealed interface ColorInterpolationAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code color-interpolation-filters} attribute.
   */
  public sealed interface ColorInterpolationFiltersAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code crossorigin} attribute.
   */
  public sealed interface CrossoriginAttribute
      extends
      LinkInstruction,
      ScriptInstruction {}

  /**
   * The {@code cursor} attribute.
   */
  public sealed interface CursorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code d} attribute.
   */
  public sealed interface DAttribute
      extends
      ClipPathInstruction,
      PathInstruction {}

  /**
   * The {@code direction} attribute.
   */
  public sealed interface DirectionAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code disabled} attribute.
   */
  public sealed interface DisabledAttribute
      extends
      OptionInstruction,
      SelectInstruction,
      TextAreaInstruction {}

  /**
   * The {@code display} attribute.
   */
  public sealed interface DisplayAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code dominant-baseline} attribute.
   */
  public sealed interface DominantBaselineAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code fill} attribute.
   */
  public sealed interface FillAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code fill-opacity} attribute.
   */
  public sealed interface FillOpacityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code fill-rule} attribute.
   */
  public sealed interface FillRuleAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code filter} attribute.
   */
  public sealed interface FilterAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code flood-color} attribute.
   */
  public sealed interface FloodColorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code flood-opacity} attribute.
   */
  public sealed interface FloodOpacityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-family} attribute.
   */
  public sealed interface FontFamilyAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-size} attribute.
   */
  public sealed interface FontSizeAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-size-adjust} attribute.
   */
  public sealed interface FontSizeAdjustAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-stretch} attribute.
   */
  public sealed interface FontStretchAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-style} attribute.
   */
  public sealed interface FontStyleAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-variant} attribute.
   */
  public sealed interface FontVariantAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code font-weight} attribute.
   */
  public sealed interface FontWeightAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code form} attribute.
   */
  public sealed interface FormAttribute
      extends
      SelectInstruction,
      TextAreaInstruction {}

  /**
   * The {@code glyph-orientation-horizontal} attribute.
   */
  public sealed interface GlyphOrientationHorizontalAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code glyph-orientation-vertical} attribute.
   */
  public sealed interface GlyphOrientationVerticalAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code height} attribute.
   */
  public sealed interface HeightAttribute
      extends
      ImageInstruction,
      SvgInstruction {}

  /**
   * The {@code href} attribute.
   */
  public sealed interface HrefAttribute
      extends
      AnchorInstruction,
      LinkInstruction {}

  /**
   * The {@code image-rendering} attribute.
   */
  public sealed interface ImageRenderingAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code letter-spacing} attribute.
   */
  public sealed interface LetterSpacingAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code lighting-color} attribute.
   */
  public sealed interface LightingColorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code marker-end} attribute.
   */
  public sealed interface MarkerEndAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code marker-mid} attribute.
   */
  public sealed interface MarkerMidAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code marker-start} attribute.
   */
  public sealed interface MarkerStartAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code mask} attribute.
   */
  public sealed interface MaskAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code mask-type} attribute.
   */
  public sealed interface MaskTypeAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code name} attribute.
   */
  public sealed interface NameAttribute
      extends
      FormInstruction,
      InputInstruction,
      MetaInstruction,
      SelectInstruction,
      TextAreaInstruction {}

  /**
   * The {@code opacity} attribute.
   */
  public sealed interface OpacityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code overflow} attribute.
   */
  public sealed interface OverflowAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code paint-order} attribute.
   */
  public sealed interface PaintOrderAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code placeholder} attribute.
   */
  public sealed interface PlaceholderAttribute
      extends
      InputInstruction,
      TextAreaInstruction {}

  /**
   * The {@code pointer-events} attribute.
   */
  public sealed interface PointerEventsAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code readonly} attribute.
   */
  public sealed interface ReadonlyAttribute
      extends
      InputInstruction,
      TextAreaInstruction {}

  /**
   * The {@code referrerpolicy} attribute.
   */
  public sealed interface ReferrerpolicyAttribute
      extends
      LinkInstruction,
      ScriptInstruction {}

  /**
   * The {@code required} attribute.
   */
  public sealed interface RequiredAttribute
      extends
      InputInstruction,
      SelectInstruction,
      TextAreaInstruction {}

  /**
   * The {@code shape-rendering} attribute.
   */
  public sealed interface ShapeRenderingAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code src} attribute.
   */
  public sealed interface SrcAttribute
      extends
      ImageInstruction,
      ScriptInstruction {}

  /**
   * The {@code stop-color} attribute.
   */
  public sealed interface StopColorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stop-opacity} attribute.
   */
  public sealed interface StopOpacityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke} attribute.
   */
  public sealed interface StrokeAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-dasharray} attribute.
   */
  public sealed interface StrokeDasharrayAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-dashoffset} attribute.
   */
  public sealed interface StrokeDashoffsetAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-linecap} attribute.
   */
  public sealed interface StrokeLinecapAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-linejoin} attribute.
   */
  public sealed interface StrokeLinejoinAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-miterlimit} attribute.
   */
  public sealed interface StrokeMiterlimitAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-opacity} attribute.
   */
  public sealed interface StrokeOpacityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code stroke-width} attribute.
   */
  public sealed interface StrokeWidthAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code target} attribute.
   */
  public sealed interface TargetAttribute
      extends
      AnchorInstruction,
      FormInstruction {}

  /**
   * The {@code text-anchor} attribute.
   */
  public sealed interface TextAnchorAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code text-decoration} attribute.
   */
  public sealed interface TextDecorationAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code text-overflow} attribute.
   */
  public sealed interface TextOverflowAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code text-rendering} attribute.
   */
  public sealed interface TextRenderingAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code transform} attribute.
   */
  public sealed interface TransformAttribute
      extends
      ClipPathInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code transform-origin} attribute.
   */
  public sealed interface TransformOriginAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code type} attribute.
   */
  public sealed interface TypeAttribute
      extends
      ButtonInstruction,
      InputInstruction,
      LinkInstruction,
      OrderedListInstruction,
      ScriptInstruction,
      StyleInstruction {}

  /**
   * The {@code unicode-bidi} attribute.
   */
  public sealed interface UnicodeBidiAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code value} attribute.
   */
  public sealed interface ValueAttribute
      extends
      InputInstruction,
      OptionInstruction {}

  /**
   * The {@code vector-effect} attribute.
   */
  public sealed interface VectorEffectAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code visibility} attribute.
   */
  public sealed interface VisibilityAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code white-space} attribute.
   */
  public sealed interface WhiteSpaceAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code width} attribute.
   */
  public sealed interface WidthAttribute
      extends
      ImageInstruction,
      SvgInstruction,
      TableInstruction {}

  /**
   * The {@code word-spacing} attribute.
   */
  public sealed interface WordSpacingAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}

  /**
   * The {@code writing-mode} attribute.
   */
  public sealed interface WritingModeAttribute
      extends
      ClipPathInstruction,
      DefsInstruction,
      GInstruction,
      PathInstruction,
      SvgInstruction {}


  /**
   * Represents an HTML global attribute such as the {@code id} attribute for example.
   */
  public sealed interface GlobalAttribute
      extends
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
      UnorderedListInstruction {}

  /**
   * An instruction for an HTML attribute provided by an external object.
   */
  public sealed interface ExternalAttribute extends GlobalAttribute {

    /**
     * Represents a single {@code id} attribute.
     */
    non-sealed interface Id extends ExternalAttribute {
      /**
       * The value of this {@code id} attribute.
       *
       * @return the value of this {@code id} attribute
       */
      String id();
    }

    /**
     * Represents a single {@code class} attribute.
     */
    non-sealed interface StyleClass extends ExternalAttribute {
      /**
       * The value of this {@code class} attribute.
       *
       * @return the value of this {@code class} attribute
       */
      String className();
    }

    /**
     * Represents a set of {@code class} attributes.
     */
    non-sealed interface StyleClassSet extends ExternalAttribute {
      /**
       * Iterator over the {@code class} attribute values of this set.
       *
       * @return an iterator over the values of this set
       */
      Iterator<String> classNames();
    }
  }

  /**
   * The attribute instruction.
   */
  public static final class Attribute
      implements
      AlignmentBaselineAttribute,
      AutocompleteAttribute,
      BaselineShiftAttribute,
      ClipPathAttribute,
      ClipRuleAttribute,
      ColorAttribute,
      ColorInterpolationAttribute,
      ColorInterpolationFiltersAttribute,
      CrossoriginAttribute,
      CursorAttribute,
      DAttribute,
      DirectionAttribute,
      DisabledAttribute,
      DisplayAttribute,
      DominantBaselineAttribute,
      FillAttribute,
      FillOpacityAttribute,
      FillRuleAttribute,
      FilterAttribute,
      FloodColorAttribute,
      FloodOpacityAttribute,
      FontFamilyAttribute,
      FontSizeAttribute,
      FontSizeAdjustAttribute,
      FontStretchAttribute,
      FontStyleAttribute,
      FontVariantAttribute,
      FontWeightAttribute,
      FormAttribute,
      GlyphOrientationHorizontalAttribute,
      GlyphOrientationVerticalAttribute,
      HeightAttribute,
      HrefAttribute,
      ImageRenderingAttribute,
      LetterSpacingAttribute,
      LightingColorAttribute,
      MarkerEndAttribute,
      MarkerMidAttribute,
      MarkerStartAttribute,
      MaskAttribute,
      MaskTypeAttribute,
      NameAttribute,
      OpacityAttribute,
      OverflowAttribute,
      PaintOrderAttribute,
      PlaceholderAttribute,
      PointerEventsAttribute,
      ReadonlyAttribute,
      ReferrerpolicyAttribute,
      RequiredAttribute,
      ShapeRenderingAttribute,
      SrcAttribute,
      StopColorAttribute,
      StopOpacityAttribute,
      StrokeAttribute,
      StrokeDasharrayAttribute,
      StrokeDashoffsetAttribute,
      StrokeLinecapAttribute,
      StrokeLinejoinAttribute,
      StrokeMiterlimitAttribute,
      StrokeOpacityAttribute,
      StrokeWidthAttribute,
      TargetAttribute,
      TextAnchorAttribute,
      TextDecorationAttribute,
      TextOverflowAttribute,
      TextRenderingAttribute,
      TransformAttribute,
      TransformOriginAttribute,
      TypeAttribute,
      UnicodeBidiAttribute,
      ValueAttribute,
      VectorEffectAttribute,
      VisibilityAttribute,
      WhiteSpaceAttribute,
      WidthAttribute,
      WordSpacingAttribute,
      WritingModeAttribute,
      GlobalAttribute {
    private Attribute() {}
  }

  /**
   * The element instruction.
   */
  public static final class Element
      implements
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
      UnorderedListInstruction {
    private Element() {}
  }

  /**
   * The fragment instruction.
   */
  public static final class Fragment
      implements
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
    private Fragment() {}
  }

  /**
   * The no-op instruction.
   */
  public static final class NoOp
      implements
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
    private NoOp() {}
  }
}
