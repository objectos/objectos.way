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

import objectos.html.BaseTypes.AlignmentBaselineAttribute;
import objectos.html.BaseTypes.AttributeInstruction;
import objectos.html.BaseTypes.AutocompleteAttribute;
import objectos.html.BaseTypes.BaselineShiftAttribute;
import objectos.html.BaseTypes.BlockquoteInstruction;
import objectos.html.BaseTypes.BodyInstruction;
import objectos.html.BaseTypes.ClipPathAttribute;
import objectos.html.BaseTypes.ClipRuleAttribute;
import objectos.html.BaseTypes.ColorAttribute;
import objectos.html.BaseTypes.ColorInterpolationAttribute;
import objectos.html.BaseTypes.ColorInterpolationFiltersAttribute;
import objectos.html.BaseTypes.CrossoriginAttribute;
import objectos.html.BaseTypes.CursorAttribute;
import objectos.html.BaseTypes.DAttribute;
import objectos.html.BaseTypes.DetailsInstruction;
import objectos.html.BaseTypes.DirectionAttribute;
import objectos.html.BaseTypes.DisabledAttribute;
import objectos.html.BaseTypes.DisplayAttribute;
import objectos.html.BaseTypes.DominantBaselineAttribute;
import objectos.html.BaseTypes.FillAttribute;
import objectos.html.BaseTypes.FillOpacityAttribute;
import objectos.html.BaseTypes.FillRuleAttribute;
import objectos.html.BaseTypes.FilterAttribute;
import objectos.html.BaseTypes.FloodColorAttribute;
import objectos.html.BaseTypes.FloodOpacityAttribute;
import objectos.html.BaseTypes.FontFamilyAttribute;
import objectos.html.BaseTypes.FontSizeAdjustAttribute;
import objectos.html.BaseTypes.FontSizeAttribute;
import objectos.html.BaseTypes.FontStretchAttribute;
import objectos.html.BaseTypes.FontStyleAttribute;
import objectos.html.BaseTypes.FontVariantAttribute;
import objectos.html.BaseTypes.FontWeightAttribute;
import objectos.html.BaseTypes.FormInstruction;
import objectos.html.BaseTypes.GlobalAttribute;
import objectos.html.BaseTypes.GlyphOrientationHorizontalAttribute;
import objectos.html.BaseTypes.GlyphOrientationVerticalAttribute;
import objectos.html.BaseTypes.HeightAttribute;
import objectos.html.BaseTypes.HrefAttribute;
import objectos.html.BaseTypes.ImageInstruction;
import objectos.html.BaseTypes.ImageRenderingAttribute;
import objectos.html.BaseTypes.InputInstruction;
import objectos.html.BaseTypes.LabelInstruction;
import objectos.html.BaseTypes.LetterSpacingAttribute;
import objectos.html.BaseTypes.LightingColorAttribute;
import objectos.html.BaseTypes.LinkInstruction;
import objectos.html.BaseTypes.MarkerEndAttribute;
import objectos.html.BaseTypes.MarkerMidAttribute;
import objectos.html.BaseTypes.MarkerStartAttribute;
import objectos.html.BaseTypes.MaskAttribute;
import objectos.html.BaseTypes.MaskTypeAttribute;
import objectos.html.BaseTypes.MetaInstruction;
import objectos.html.BaseTypes.NameAttribute;
import objectos.html.BaseTypes.OpacityAttribute;
import objectos.html.BaseTypes.OptionInstruction;
import objectos.html.BaseTypes.OrderedListInstruction;
import objectos.html.BaseTypes.OverflowAttribute;
import objectos.html.BaseTypes.PaintOrderAttribute;
import objectos.html.BaseTypes.PlaceholderAttribute;
import objectos.html.BaseTypes.PointerEventsAttribute;
import objectos.html.BaseTypes.ReadonlyAttribute;
import objectos.html.BaseTypes.ReferrerpolicyAttribute;
import objectos.html.BaseTypes.RequiredAttribute;
import objectos.html.BaseTypes.ScriptInstruction;
import objectos.html.BaseTypes.SelectInstruction;
import objectos.html.BaseTypes.ShapeRenderingAttribute;
import objectos.html.BaseTypes.SrcAttribute;
import objectos.html.BaseTypes.StopColorAttribute;
import objectos.html.BaseTypes.StopOpacityAttribute;
import objectos.html.BaseTypes.StrokeAttribute;
import objectos.html.BaseTypes.StrokeDasharrayAttribute;
import objectos.html.BaseTypes.StrokeDashoffsetAttribute;
import objectos.html.BaseTypes.StrokeLinecapAttribute;
import objectos.html.BaseTypes.StrokeLinejoinAttribute;
import objectos.html.BaseTypes.StrokeMiterlimitAttribute;
import objectos.html.BaseTypes.StrokeOpacityAttribute;
import objectos.html.BaseTypes.StrokeWidthAttribute;
import objectos.html.BaseTypes.SvgInstruction;
import objectos.html.BaseTypes.TableInstruction;
import objectos.html.BaseTypes.TargetAttribute;
import objectos.html.BaseTypes.TextAnchorAttribute;
import objectos.html.BaseTypes.TextAreaInstruction;
import objectos.html.BaseTypes.TextDecorationAttribute;
import objectos.html.BaseTypes.TextOverflowAttribute;
import objectos.html.BaseTypes.TextRenderingAttribute;
import objectos.html.BaseTypes.TransformAttribute;
import objectos.html.BaseTypes.TransformOriginAttribute;
import objectos.html.BaseTypes.TypeAttribute;
import objectos.html.BaseTypes.UnicodeBidiAttribute;
import objectos.html.BaseTypes.ValueAttribute;
import objectos.html.BaseTypes.VectorEffectAttribute;
import objectos.html.BaseTypes.VisibilityAttribute;
import objectos.html.BaseTypes.WhiteSpaceAttribute;
import objectos.html.BaseTypes.WidthAttribute;
import objectos.html.BaseTypes.WordSpacingAttribute;
import objectos.html.BaseTypes.WritingModeAttribute;
import objectos.html.internal.StandardAttributeName;

/**
 * Provides methods for rendering HTML attributes.
 */
// Generated by objectos.selfgen.HtmlSpec. Do not edit!
public sealed abstract class BaseAttributes extends Recorder permits BaseElements {
  BaseAttributes() {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute accesskey(String value) {
    attribute(StandardAttributeName.ACCESSKEY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FormInstruction action(String value) {
    attribute(StandardAttributeName.ACTION, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TableInstruction align(String value) {
    attribute(StandardAttributeName.ALIGN, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code alignment-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final AlignmentBaselineAttribute alignmentBaseline(String value) {
    attribute(StandardAttributeName.ALIGNMENTBASELINE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ImageInstruction alt(String value) {
    attribute(StandardAttributeName.ALT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute ariaHidden(String value) {
    attribute(StandardAttributeName.ARIAHIDDEN, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final ScriptInstruction async() {
    attribute(StandardAttributeName.ASYNC);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final AutocompleteAttribute autocomplete(String value) {
    attribute(StandardAttributeName.AUTOCOMPLETE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final InputInstruction autofocus() {
    attribute(StandardAttributeName.AUTOFOCUS);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BaselineShiftAttribute baselineShift(String value) {
    attribute(StandardAttributeName.BASELINESHIFT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TableInstruction border(String value) {
    attribute(StandardAttributeName.BORDER, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TableInstruction cellpadding(String value) {
    attribute(StandardAttributeName.CELLPADDING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TableInstruction cellspacing(String value) {
    attribute(StandardAttributeName.CELLSPACING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MetaInstruction charset(String value) {
    attribute(StandardAttributeName.CHARSET, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BlockquoteInstruction cite(String value) {
    attribute(StandardAttributeName.CITE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute className(String value) {
    attribute(StandardAttributeName.CLASS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ClipRuleAttribute clipRule(String value) {
    attribute(StandardAttributeName.CLIPRULE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ColorAttribute color(String value) {
    attribute(StandardAttributeName.COLOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color-interpolation} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ColorInterpolationAttribute colorInterpolation(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATION, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color-interpolation-filters} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ColorInterpolationFiltersAttribute colorInterpolationFilters(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATIONFILTERS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction cols(String value) {
    attribute(StandardAttributeName.COLS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MetaInstruction content(String value) {
    attribute(StandardAttributeName.CONTENT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute contenteditable(String value) {
    attribute(StandardAttributeName.CONTENTEDITABLE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final CrossoriginAttribute crossorigin(String value) {
    attribute(StandardAttributeName.CROSSORIGIN, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final CursorAttribute cursor(String value) {
    attribute(StandardAttributeName.CURSOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final DAttribute d(String value) {
    attribute(StandardAttributeName.D, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final ScriptInstruction defer() {
    attribute(StandardAttributeName.DEFER);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute dir(String value) {
    attribute(StandardAttributeName.DIR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final DirectionAttribute direction(String value) {
    attribute(StandardAttributeName.DIRECTION, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction dirname(String value) {
    attribute(StandardAttributeName.DIRNAME, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final DisabledAttribute disabled() {
    attribute(StandardAttributeName.DISABLED);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final DisplayAttribute display(String value) {
    attribute(StandardAttributeName.DISPLAY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final DominantBaselineAttribute dominantBaseline(String value) {
    attribute(StandardAttributeName.DOMINANTBASELINE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute draggable(String value) {
    attribute(StandardAttributeName.DRAGGABLE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FormInstruction enctype(String value) {
    attribute(StandardAttributeName.ENCTYPE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FillAttribute fill(String value) {
    attribute(StandardAttributeName.FILL, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FillOpacityAttribute fillOpacity(String value) {
    attribute(StandardAttributeName.FILLOPACITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FillRuleAttribute fillRule(String value) {
    attribute(StandardAttributeName.FILLRULE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FilterAttribute filter(String value) {
    attribute(StandardAttributeName.FILTER, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FloodColorAttribute floodColor(String value) {
    attribute(StandardAttributeName.FLOODCOLOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FloodOpacityAttribute floodOpacity(String value) {
    attribute(StandardAttributeName.FLOODOPACITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-family} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontFamilyAttribute fontFamily(String value) {
    attribute(StandardAttributeName.FONTFAMILY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontSizeAttribute fontSize(String value) {
    attribute(StandardAttributeName.FONTSIZE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-size-adjust} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontSizeAdjustAttribute fontSizeAdjust(String value) {
    attribute(StandardAttributeName.FONTSIZEADJUST, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-stretch} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontStretchAttribute fontStretch(String value) {
    attribute(StandardAttributeName.FONTSTRETCH, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontStyleAttribute fontStyle(String value) {
    attribute(StandardAttributeName.FONTSTYLE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-variant} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontVariantAttribute fontVariant(String value) {
    attribute(StandardAttributeName.FONTVARIANT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-weight} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FontWeightAttribute fontWeight(String value) {
    attribute(StandardAttributeName.FONTWEIGHT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LabelInstruction forAttr(String value) {
    attribute(StandardAttributeName.FOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LabelInstruction forElement(String value) {
    attribute(StandardAttributeName.FOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code glyph-orientation-horizontal} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlyphOrientationHorizontalAttribute glyphOrientationHorizontal(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONHORIZONTAL, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code glyph-orientation-vertical} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlyphOrientationVerticalAttribute glyphOrientationVertical(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONVERTICAL, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final HeightAttribute height(String value) {
    attribute(StandardAttributeName.HEIGHT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute hidden() {
    attribute(StandardAttributeName.HIDDEN);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final HrefAttribute href(String value) {
    attribute(StandardAttributeName.HREF, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MetaInstruction httpEquiv(String value) {
    attribute(StandardAttributeName.HTTPEQUIV, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute id(String value) {
    attribute(StandardAttributeName.ID, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ImageRenderingAttribute imageRendering(String value) {
    attribute(StandardAttributeName.IMAGERENDERING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ScriptInstruction integrity(String value) {
    attribute(StandardAttributeName.INTEGRITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute lang(String value) {
    attribute(StandardAttributeName.LANG, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LetterSpacingAttribute letterSpacing(String value) {
    attribute(StandardAttributeName.LETTERSPACING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LightingColorAttribute lightingColor(String value) {
    attribute(StandardAttributeName.LIGHTINGCOLOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MarkerEndAttribute markerEnd(String value) {
    attribute(StandardAttributeName.MARKEREND, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MarkerMidAttribute markerMid(String value) {
    attribute(StandardAttributeName.MARKERMID, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MarkerStartAttribute markerStart(String value) {
    attribute(StandardAttributeName.MARKERSTART, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MaskAttribute mask(String value) {
    attribute(StandardAttributeName.MASK, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MaskTypeAttribute maskType(String value) {
    attribute(StandardAttributeName.MASKTYPE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction maxlength(String value) {
    attribute(StandardAttributeName.MAXLENGTH, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LinkInstruction media(String value) {
    attribute(StandardAttributeName.MEDIA, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final FormInstruction method(String value) {
    attribute(StandardAttributeName.METHOD, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction minlength(String value) {
    attribute(StandardAttributeName.MINLENGTH, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final SelectInstruction multiple() {
    attribute(StandardAttributeName.MULTIPLE);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final NameAttribute name(String value) {
    attribute(StandardAttributeName.NAME, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final ScriptInstruction nomodule() {
    attribute(StandardAttributeName.NOMODULE);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onafterprint(String value) {
    attribute(StandardAttributeName.ONAFTERPRINT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onbeforeprint(String value) {
    attribute(StandardAttributeName.ONBEFOREPRINT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onbeforeunload(String value) {
    attribute(StandardAttributeName.ONBEFOREUNLOAD, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute onclick(String value) {
    attribute(StandardAttributeName.ONCLICK, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onhashchange(String value) {
    attribute(StandardAttributeName.ONHASHCHANGE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onlanguagechange(String value) {
    attribute(StandardAttributeName.ONLANGUAGECHANGE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onmessage(String value) {
    attribute(StandardAttributeName.ONMESSAGE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onoffline(String value) {
    attribute(StandardAttributeName.ONOFFLINE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction ononline(String value) {
    attribute(StandardAttributeName.ONONLINE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onpagehide(String value) {
    attribute(StandardAttributeName.ONPAGEHIDE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onpageshow(String value) {
    attribute(StandardAttributeName.ONPAGESHOW, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onpopstate(String value) {
    attribute(StandardAttributeName.ONPOPSTATE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onrejectionhandled} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onrejectionhandled(String value) {
    attribute(StandardAttributeName.ONREJECTIONHANDLED, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onstorage(String value) {
    attribute(StandardAttributeName.ONSTORAGE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute onsubmit(String value) {
    attribute(StandardAttributeName.ONSUBMIT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onunhandledrejection} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onunhandledrejection(String value) {
    attribute(StandardAttributeName.ONUNHANDLEDREJECTION, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final BodyInstruction onunload(String value) {
    attribute(StandardAttributeName.ONUNLOAD, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final OpacityAttribute opacity(String value) {
    attribute(StandardAttributeName.OPACITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final DetailsInstruction open() {
    attribute(StandardAttributeName.OPEN);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final OverflowAttribute overflow(String value) {
    attribute(StandardAttributeName.OVERFLOW, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final PaintOrderAttribute paintOrder(String value) {
    attribute(StandardAttributeName.PAINTORDER, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final PlaceholderAttribute placeholder(String value) {
    attribute(StandardAttributeName.PLACEHOLDER, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final PointerEventsAttribute pointerEvents(String value) {
    attribute(StandardAttributeName.POINTEREVENTS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final MetaInstruction property(String value) {
    attribute(StandardAttributeName.PROPERTY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final ReadonlyAttribute readonly() {
    attribute(StandardAttributeName.READONLY);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ReferrerpolicyAttribute referrerpolicy(String value) {
    attribute(StandardAttributeName.REFERRERPOLICY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LinkInstruction rel(String value) {
    attribute(StandardAttributeName.REL, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final RequiredAttribute required() {
    attribute(StandardAttributeName.REQUIRED);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LinkInstruction rev(String value) {
    attribute(StandardAttributeName.REV, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final OrderedListInstruction reversed() {
    attribute(StandardAttributeName.REVERSED);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute role(String value) {
    attribute(StandardAttributeName.ROLE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction rows(String value) {
    attribute(StandardAttributeName.ROWS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final OptionInstruction selected() {
    attribute(StandardAttributeName.SELECTED);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ShapeRenderingAttribute shapeRendering(String value) {
    attribute(StandardAttributeName.SHAPERENDERING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final SelectInstruction size(String value) {
    attribute(StandardAttributeName.SIZE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final LinkInstruction sizes(String value) {
    attribute(StandardAttributeName.SIZES, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute spellcheck(String value) {
    attribute(StandardAttributeName.SPELLCHECK, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final SrcAttribute src(String value) {
    attribute(StandardAttributeName.SRC, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ImageInstruction srcset(String value) {
    attribute(StandardAttributeName.SRCSET, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final OrderedListInstruction start(String value) {
    attribute(StandardAttributeName.START, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StopColorAttribute stopColor(String value) {
    attribute(StandardAttributeName.STOPCOLOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StopOpacityAttribute stopOpacity(String value) {
    attribute(StandardAttributeName.STOPOPACITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeAttribute stroke(String value) {
    attribute(StandardAttributeName.STROKE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeDasharrayAttribute strokeDasharray(String value) {
    attribute(StandardAttributeName.STROKEDASHARRAY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeDashoffsetAttribute strokeDashoffset(String value) {
    attribute(StandardAttributeName.STROKEDASHOFFSET, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeLinecapAttribute strokeLinecap(String value) {
    attribute(StandardAttributeName.STROKELINECAP, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeLinejoinAttribute strokeLinejoin(String value) {
    attribute(StandardAttributeName.STROKELINEJOIN, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeMiterlimitAttribute strokeMiterlimit(String value) {
    attribute(StandardAttributeName.STROKEMITERLIMIT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeOpacityAttribute strokeOpacity(String value) {
    attribute(StandardAttributeName.STROKEOPACITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final StrokeWidthAttribute strokeWidth(String value) {
    attribute(StandardAttributeName.STROKEWIDTH, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute inlineStyle(String value) {
    attribute(StandardAttributeName.STYLE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute tabindex(String value) {
    attribute(StandardAttributeName.TABINDEX, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TargetAttribute target(String value) {
    attribute(StandardAttributeName.TARGET, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAnchorAttribute textAnchor(String value) {
    attribute(StandardAttributeName.TEXTANCHOR, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextDecorationAttribute textDecoration(String value) {
    attribute(StandardAttributeName.TEXTDECORATION, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextOverflowAttribute textOverflow(String value) {
    attribute(StandardAttributeName.TEXTOVERFLOW, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextRenderingAttribute textRendering(String value) {
    attribute(StandardAttributeName.TEXTRENDERING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TransformAttribute transform(String value) {
    attribute(StandardAttributeName.TRANSFORM, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TransformOriginAttribute transformOrigin(String value) {
    attribute(StandardAttributeName.TRANSFORMORIGIN, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final GlobalAttribute translate(String value) {
    attribute(StandardAttributeName.TRANSLATE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TypeAttribute type(String value) {
    attribute(StandardAttributeName.TYPE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final UnicodeBidiAttribute unicodeBidi(String value) {
    attribute(StandardAttributeName.UNICODEBIDI, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ValueAttribute value(String value) {
    attribute(StandardAttributeName.VALUE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final VectorEffectAttribute vectorEffect(String value) {
    attribute(StandardAttributeName.VECTOREFFECT, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final SvgInstruction viewBox(String value) {
    attribute(StandardAttributeName.VIEWBOX, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final VisibilityAttribute visibility(String value) {
    attribute(StandardAttributeName.VISIBILITY, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final WhiteSpaceAttribute whiteSpace(String value) {
    attribute(StandardAttributeName.WHITESPACE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final WidthAttribute width(String value) {
    attribute(StandardAttributeName.WIDTH, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final WordSpacingAttribute wordSpacing(String value) {
    attribute(StandardAttributeName.WORDSPACING, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final TextAreaInstruction wrap(String value) {
    attribute(StandardAttributeName.WRAP, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final WritingModeAttribute writingMode(String value) {
    attribute(StandardAttributeName.WRITINGMODE, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final SvgInstruction xmlns(String value) {
    attribute(StandardAttributeName.XMLNS, value);
    return AttributeInstruction.INSTANCE;
  }

  /**
   * Generates the {@code clip-path} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final ClipPathAttribute clipPath(String value) {
    attribute(StandardAttributeName.CLIPPATH, value);
    return AttributeInstruction.INSTANCE;
  }
}
