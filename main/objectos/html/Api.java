/*
 * Copyright (C) 2015-2024 Objectos Software LTDA.
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
package objectos.html;

import java.util.Iterator;

/**
 * Defines the types of the {@link Html} domain-specific language.
 */
// Generated by objectos.selfgen.HtmlSpec. Do not edit!
public final class Api {
  static final Attribute ATTRIBUTE = new Attribute();

  static final Element ELEMENT = new Element();

  static final Fragment FRAGMENT = new Fragment();

  static final NoOp NOOP = new NoOp();

  private Api() {}

  /**
   * Represents an instruction that generates part of the output of an HTML template.
   *
   * <p>
   * Unless noted references to a particular instruction MUST NOT be reused.
   */
  public sealed interface Instruction {}

  /**
   * Allowed as an argument to the {@code a} element method.
   */
  public sealed interface AnchorValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code abbr} element method.
   */
  public sealed interface AbbreviationValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code article} element method.
   */
  public sealed interface ArticleValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code b} element method.
   */
  public sealed interface BringAttentionToValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code blockquote} element method.
   */
  public sealed interface BlockquoteValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code body} element method.
   */
  public sealed interface BodyValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code br} element method.
   */
  public sealed interface LineBreakValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code button} element method.
   */
  public sealed interface ButtonValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code clipPath} element method.
   */
  public sealed interface ClipPathValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code code} element method.
   */
  public sealed interface CodeValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code dd} element method.
   */
  public sealed interface DefinitionDescriptionValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code defs} element method.
   */
  public sealed interface DefsValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code details} element method.
   */
  public sealed interface DetailsValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code div} element method.
   */
  public sealed interface DivValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code dl} element method.
   */
  public sealed interface DefinitionListValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code dt} element method.
   */
  public sealed interface DefinitionTermValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code em} element method.
   */
  public sealed interface EmphasisValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code fieldset} element method.
   */
  public sealed interface FieldsetValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code figure} element method.
   */
  public sealed interface FigureValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code footer} element method.
   */
  public sealed interface FooterValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code form} element method.
   */
  public sealed interface FormValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code g} element method.
   */
  public sealed interface GValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code h1} element method.
   */
  public sealed interface Heading1Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code h2} element method.
   */
  public sealed interface Heading2Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code h3} element method.
   */
  public sealed interface Heading3Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code h4} element method.
   */
  public sealed interface Heading4Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code h5} element method.
   */
  public sealed interface Heading5Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code h6} element method.
   */
  public sealed interface Heading6Value extends Instruction {}

  /**
   * Allowed as an argument to the {@code head} element method.
   */
  public sealed interface HeadValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code header} element method.
   */
  public sealed interface HeaderValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code hgroup} element method.
   */
  public sealed interface HeadingGroupValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code hr} element method.
   */
  public sealed interface HorizontalRuleValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code html} element method.
   */
  public sealed interface HtmlValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code img} element method.
   */
  public sealed interface ImageValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code input} element method.
   */
  public sealed interface InputValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code kbd} element method.
   */
  public sealed interface KeyboardInputValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code label} element method.
   */
  public sealed interface LabelValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code legend} element method.
   */
  public sealed interface LegendValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code li} element method.
   */
  public sealed interface ListItemValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code link} element method.
   */
  public sealed interface LinkValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code main} element method.
   */
  public sealed interface MainValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code menu} element method.
   */
  public sealed interface MenuValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code meta} element method.
   */
  public sealed interface MetaValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code nav} element method.
   */
  public sealed interface NavValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code ol} element method.
   */
  public sealed interface OrderedListValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code optgroup} element method.
   */
  public sealed interface OptionGroupValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code option} element method.
   */
  public sealed interface OptionValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code p} element method.
   */
  public sealed interface ParagraphValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code path} element method.
   */
  public sealed interface PathValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code pre} element method.
   */
  public sealed interface PreValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code progress} element method.
   */
  public sealed interface ProgressValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code samp} element method.
   */
  public sealed interface SampleOutputValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code script} element method.
   */
  public sealed interface ScriptValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code section} element method.
   */
  public sealed interface SectionValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code select} element method.
   */
  public sealed interface SelectValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code small} element method.
   */
  public sealed interface SmallValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code span} element method.
   */
  public sealed interface SpanValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code strong} element method.
   */
  public sealed interface StrongValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code style} element method.
   */
  public sealed interface StyleValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code sub} element method.
   */
  public sealed interface SubscriptValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code summary} element method.
   */
  public sealed interface SummaryValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code sup} element method.
   */
  public sealed interface SuperscriptValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code svg} element method.
   */
  public sealed interface SvgValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code table} element method.
   */
  public sealed interface TableValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code tbody} element method.
   */
  public sealed interface TableBodyValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code td} element method.
   */
  public sealed interface TableDataValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code template} element method.
   */
  public sealed interface TemplateValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code textarea} element method.
   */
  public sealed interface TextAreaValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code th} element method.
   */
  public sealed interface TableHeaderValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code thead} element method.
   */
  public sealed interface TableHeadValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code title} element method.
   */
  public sealed interface TitleValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code tr} element method.
   */
  public sealed interface TableRowValue extends Instruction {}

  /**
   * Allowed as an argument to the {@code ul} element method.
   */
  public sealed interface UnorderedListValue extends Instruction {}


  /**
   * The {@code alignment-baseline} attribute.
   */
  public sealed interface AlignmentBaselineAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code autocomplete} attribute.
   */
  public sealed interface AutocompleteAttribute
      extends
      SelectValue,
      TextAreaValue {}

  /**
   * The {@code baseline-shift} attribute.
   */
  public sealed interface BaselineShiftAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code clip-path} attribute.
   */
  public sealed interface ClipPathAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code clip-rule} attribute.
   */
  public sealed interface ClipRuleAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code color} attribute.
   */
  public sealed interface ColorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code color-interpolation} attribute.
   */
  public sealed interface ColorInterpolationAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code color-interpolation-filters} attribute.
   */
  public sealed interface ColorInterpolationFiltersAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code crossorigin} attribute.
   */
  public sealed interface CrossoriginAttribute
      extends
      LinkValue,
      ScriptValue {}

  /**
   * The {@code cursor} attribute.
   */
  public sealed interface CursorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code d} attribute.
   */
  public sealed interface DAttribute
      extends
      ClipPathValue,
      PathValue {}

  /**
   * The {@code direction} attribute.
   */
  public sealed interface DirectionAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code disabled} attribute.
   */
  public sealed interface DisabledAttribute
      extends
      OptionValue,
      SelectValue,
      TextAreaValue {}

  /**
   * The {@code display} attribute.
   */
  public sealed interface DisplayAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code dominant-baseline} attribute.
   */
  public sealed interface DominantBaselineAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code fill} attribute.
   */
  public sealed interface FillAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code fill-opacity} attribute.
   */
  public sealed interface FillOpacityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code fill-rule} attribute.
   */
  public sealed interface FillRuleAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code filter} attribute.
   */
  public sealed interface FilterAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code flood-color} attribute.
   */
  public sealed interface FloodColorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code flood-opacity} attribute.
   */
  public sealed interface FloodOpacityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-family} attribute.
   */
  public sealed interface FontFamilyAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-size} attribute.
   */
  public sealed interface FontSizeAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-size-adjust} attribute.
   */
  public sealed interface FontSizeAdjustAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-stretch} attribute.
   */
  public sealed interface FontStretchAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-style} attribute.
   */
  public sealed interface FontStyleAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-variant} attribute.
   */
  public sealed interface FontVariantAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code font-weight} attribute.
   */
  public sealed interface FontWeightAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code form} attribute.
   */
  public sealed interface FormAttribute
      extends
      SelectValue,
      TextAreaValue {}

  /**
   * The {@code glyph-orientation-horizontal} attribute.
   */
  public sealed interface GlyphOrientationHorizontalAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code glyph-orientation-vertical} attribute.
   */
  public sealed interface GlyphOrientationVerticalAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code height} attribute.
   */
  public sealed interface HeightAttribute
      extends
      ImageValue,
      SvgValue {}

  /**
   * The {@code href} attribute.
   */
  public sealed interface HrefAttribute
      extends
      AnchorValue,
      LinkValue {}

  /**
   * The {@code image-rendering} attribute.
   */
  public sealed interface ImageRenderingAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code letter-spacing} attribute.
   */
  public sealed interface LetterSpacingAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code lighting-color} attribute.
   */
  public sealed interface LightingColorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code marker-end} attribute.
   */
  public sealed interface MarkerEndAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code marker-mid} attribute.
   */
  public sealed interface MarkerMidAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code marker-start} attribute.
   */
  public sealed interface MarkerStartAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code mask} attribute.
   */
  public sealed interface MaskAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code mask-type} attribute.
   */
  public sealed interface MaskTypeAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code name} attribute.
   */
  public sealed interface NameAttribute
      extends
      FormValue,
      InputValue,
      MetaValue,
      SelectValue,
      TextAreaValue {}

  /**
   * The {@code opacity} attribute.
   */
  public sealed interface OpacityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code overflow} attribute.
   */
  public sealed interface OverflowAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code paint-order} attribute.
   */
  public sealed interface PaintOrderAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code placeholder} attribute.
   */
  public sealed interface PlaceholderAttribute
      extends
      InputValue,
      TextAreaValue {}

  /**
   * The {@code pointer-events} attribute.
   */
  public sealed interface PointerEventsAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code readonly} attribute.
   */
  public sealed interface ReadonlyAttribute
      extends
      InputValue,
      TextAreaValue {}

  /**
   * The {@code referrerpolicy} attribute.
   */
  public sealed interface ReferrerpolicyAttribute
      extends
      LinkValue,
      ScriptValue {}

  /**
   * The {@code required} attribute.
   */
  public sealed interface RequiredAttribute
      extends
      InputValue,
      SelectValue,
      TextAreaValue {}

  /**
   * The {@code shape-rendering} attribute.
   */
  public sealed interface ShapeRenderingAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code src} attribute.
   */
  public sealed interface SrcAttribute
      extends
      ImageValue,
      ScriptValue {}

  /**
   * The {@code stop-color} attribute.
   */
  public sealed interface StopColorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stop-opacity} attribute.
   */
  public sealed interface StopOpacityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke} attribute.
   */
  public sealed interface StrokeAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-dasharray} attribute.
   */
  public sealed interface StrokeDasharrayAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-dashoffset} attribute.
   */
  public sealed interface StrokeDashoffsetAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-linecap} attribute.
   */
  public sealed interface StrokeLinecapAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-linejoin} attribute.
   */
  public sealed interface StrokeLinejoinAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-miterlimit} attribute.
   */
  public sealed interface StrokeMiterlimitAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-opacity} attribute.
   */
  public sealed interface StrokeOpacityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code stroke-width} attribute.
   */
  public sealed interface StrokeWidthAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code target} attribute.
   */
  public sealed interface TargetAttribute
      extends
      AnchorValue,
      FormValue {}

  /**
   * The {@code text-anchor} attribute.
   */
  public sealed interface TextAnchorAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code text-decoration} attribute.
   */
  public sealed interface TextDecorationAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code text-overflow} attribute.
   */
  public sealed interface TextOverflowAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code text-rendering} attribute.
   */
  public sealed interface TextRenderingAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code transform} attribute.
   */
  public sealed interface TransformAttribute
      extends
      ClipPathValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code transform-origin} attribute.
   */
  public sealed interface TransformOriginAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code type} attribute.
   */
  public sealed interface TypeAttribute
      extends
      ButtonValue,
      InputValue,
      LinkValue,
      OrderedListValue,
      ScriptValue,
      StyleValue {}

  /**
   * The {@code unicode-bidi} attribute.
   */
  public sealed interface UnicodeBidiAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code value} attribute.
   */
  public sealed interface ValueAttribute
      extends
      InputValue,
      OptionValue {}

  /**
   * The {@code vector-effect} attribute.
   */
  public sealed interface VectorEffectAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code visibility} attribute.
   */
  public sealed interface VisibilityAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code white-space} attribute.
   */
  public sealed interface WhiteSpaceAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code width} attribute.
   */
  public sealed interface WidthAttribute
      extends
      ImageValue,
      SvgValue,
      TableValue {}

  /**
   * The {@code word-spacing} attribute.
   */
  public sealed interface WordSpacingAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}

  /**
   * The {@code writing-mode} attribute.
   */
  public sealed interface WritingModeAttribute
      extends
      ClipPathValue,
      DefsValue,
      GValue,
      PathValue,
      SvgValue {}


  /**
   * Represents an HTML global attribute such as the {@code id} attribute for example.
   */
  public sealed interface GlobalAttribute
      extends
      AnchorValue,
      AbbreviationValue,
      ArticleValue,
      BringAttentionToValue,
      BlockquoteValue,
      BodyValue,
      LineBreakValue,
      ButtonValue,
      ClipPathValue,
      CodeValue,
      DefinitionDescriptionValue,
      DefsValue,
      DetailsValue,
      DivValue,
      DefinitionListValue,
      DefinitionTermValue,
      EmphasisValue,
      FieldsetValue,
      FigureValue,
      FooterValue,
      FormValue,
      GValue,
      Heading1Value,
      Heading2Value,
      Heading3Value,
      Heading4Value,
      Heading5Value,
      Heading6Value,
      HeadValue,
      HeaderValue,
      HeadingGroupValue,
      HorizontalRuleValue,
      HtmlValue,
      ImageValue,
      InputValue,
      KeyboardInputValue,
      LabelValue,
      LegendValue,
      ListItemValue,
      LinkValue,
      MainValue,
      MenuValue,
      MetaValue,
      NavValue,
      OrderedListValue,
      OptionGroupValue,
      OptionValue,
      ParagraphValue,
      PathValue,
      PreValue,
      ProgressValue,
      SampleOutputValue,
      ScriptValue,
      SectionValue,
      SelectValue,
      SmallValue,
      SpanValue,
      StrongValue,
      StyleValue,
      SubscriptValue,
      SummaryValue,
      SuperscriptValue,
      SvgValue,
      TableValue,
      TableBodyValue,
      TableDataValue,
      TemplateValue,
      TextAreaValue,
      TableHeaderValue,
      TableHeadValue,
      TitleValue,
      TableRowValue,
      UnorderedListValue {}

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
  static final class Attribute
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
    Attribute() {}
  }

  /**
   * The element instruction.
   */
  public static final class Element
      implements
      AnchorValue,
      AbbreviationValue,
      ArticleValue,
      BringAttentionToValue,
      BlockquoteValue,
      BodyValue,
      ButtonValue,
      ClipPathValue,
      CodeValue,
      DefinitionDescriptionValue,
      DefsValue,
      DetailsValue,
      DivValue,
      DefinitionListValue,
      DefinitionTermValue,
      EmphasisValue,
      FieldsetValue,
      FigureValue,
      FooterValue,
      FormValue,
      GValue,
      Heading1Value,
      Heading2Value,
      Heading3Value,
      Heading4Value,
      Heading5Value,
      Heading6Value,
      HeadValue,
      HeaderValue,
      HeadingGroupValue,
      HtmlValue,
      KeyboardInputValue,
      LabelValue,
      LegendValue,
      ListItemValue,
      MainValue,
      MenuValue,
      NavValue,
      OrderedListValue,
      OptionGroupValue,
      OptionValue,
      ParagraphValue,
      PathValue,
      PreValue,
      ProgressValue,
      SampleOutputValue,
      ScriptValue,
      SectionValue,
      SelectValue,
      SmallValue,
      SpanValue,
      StrongValue,
      StyleValue,
      SubscriptValue,
      SummaryValue,
      SuperscriptValue,
      SvgValue,
      TableValue,
      TableBodyValue,
      TableDataValue,
      TemplateValue,
      TextAreaValue,
      TableHeaderValue,
      TableHeadValue,
      TitleValue,
      TableRowValue,
      UnorderedListValue {
    Element() {}
  }

  /**
   * The fragment instruction.
   */
  public static final class Fragment
      implements
      AnchorValue,
      AbbreviationValue,
      ArticleValue,
      BringAttentionToValue,
      BlockquoteValue,
      BodyValue,
      LineBreakValue,
      ButtonValue,
      ClipPathValue,
      CodeValue,
      DefinitionDescriptionValue,
      DefsValue,
      DetailsValue,
      DivValue,
      DefinitionListValue,
      DefinitionTermValue,
      EmphasisValue,
      FieldsetValue,
      FigureValue,
      FooterValue,
      FormValue,
      GValue,
      Heading1Value,
      Heading2Value,
      Heading3Value,
      Heading4Value,
      Heading5Value,
      Heading6Value,
      HeadValue,
      HeaderValue,
      HeadingGroupValue,
      HorizontalRuleValue,
      HtmlValue,
      ImageValue,
      InputValue,
      KeyboardInputValue,
      LabelValue,
      LegendValue,
      ListItemValue,
      LinkValue,
      MainValue,
      MenuValue,
      MetaValue,
      NavValue,
      OrderedListValue,
      OptionGroupValue,
      OptionValue,
      ParagraphValue,
      PathValue,
      PreValue,
      ProgressValue,
      SampleOutputValue,
      ScriptValue,
      SectionValue,
      SelectValue,
      SmallValue,
      SpanValue,
      StrongValue,
      StyleValue,
      SubscriptValue,
      SummaryValue,
      SuperscriptValue,
      SvgValue,
      TableValue,
      TableBodyValue,
      TableDataValue,
      TemplateValue,
      TextAreaValue,
      TableHeaderValue,
      TableHeadValue,
      TitleValue,
      TableRowValue,
      UnorderedListValue {
    Fragment() {}
  }

  /**
   * The no-op instruction.
   */
  public static final class NoOp
      implements
      AnchorValue,
      AbbreviationValue,
      ArticleValue,
      BringAttentionToValue,
      BlockquoteValue,
      BodyValue,
      LineBreakValue,
      ButtonValue,
      ClipPathValue,
      CodeValue,
      DefinitionDescriptionValue,
      DefsValue,
      DetailsValue,
      DivValue,
      DefinitionListValue,
      DefinitionTermValue,
      EmphasisValue,
      FieldsetValue,
      FigureValue,
      FooterValue,
      FormValue,
      GValue,
      Heading1Value,
      Heading2Value,
      Heading3Value,
      Heading4Value,
      Heading5Value,
      Heading6Value,
      HeadValue,
      HeaderValue,
      HeadingGroupValue,
      HorizontalRuleValue,
      HtmlValue,
      ImageValue,
      InputValue,
      KeyboardInputValue,
      LabelValue,
      LegendValue,
      ListItemValue,
      LinkValue,
      MainValue,
      MenuValue,
      MetaValue,
      NavValue,
      OrderedListValue,
      OptionGroupValue,
      OptionValue,
      ParagraphValue,
      PathValue,
      PreValue,
      ProgressValue,
      SampleOutputValue,
      ScriptValue,
      SectionValue,
      SelectValue,
      SmallValue,
      SpanValue,
      StrongValue,
      StyleValue,
      SubscriptValue,
      SummaryValue,
      SuperscriptValue,
      SvgValue,
      TableValue,
      TableBodyValue,
      TableDataValue,
      TemplateValue,
      TextAreaValue,
      TableHeaderValue,
      TableHeadValue,
      TitleValue,
      TableRowValue,
      UnorderedListValue {
    private NoOp() {}
  }
}