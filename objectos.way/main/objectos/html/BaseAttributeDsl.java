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
package objectos.html;

import objectos.html.internal.HtmlTemplateApi;
import objectos.html.internal.InternalInstruction;
import objectos.html.internal.StandardAttributeName;
import objectos.html.tmpl.Api.AlignmentBaselineAttribute;
import objectos.html.tmpl.Api.AutocompleteAttribute;
import objectos.html.tmpl.Api.BaselineShiftAttribute;
import objectos.html.tmpl.Api.BlockquoteInstruction;
import objectos.html.tmpl.Api.BodyInstruction;
import objectos.html.tmpl.Api.ClipPathAttribute;
import objectos.html.tmpl.Api.ClipRuleAttribute;
import objectos.html.tmpl.Api.ColorAttribute;
import objectos.html.tmpl.Api.ColorInterpolationAttribute;
import objectos.html.tmpl.Api.ColorInterpolationFiltersAttribute;
import objectos.html.tmpl.Api.CrossoriginAttribute;
import objectos.html.tmpl.Api.CursorAttribute;
import objectos.html.tmpl.Api.DAttribute;
import objectos.html.tmpl.Api.DetailsInstruction;
import objectos.html.tmpl.Api.DirectionAttribute;
import objectos.html.tmpl.Api.DisabledAttribute;
import objectos.html.tmpl.Api.DisplayAttribute;
import objectos.html.tmpl.Api.DominantBaselineAttribute;
import objectos.html.tmpl.Api.FillAttribute;
import objectos.html.tmpl.Api.FillOpacityAttribute;
import objectos.html.tmpl.Api.FillRuleAttribute;
import objectos.html.tmpl.Api.FilterAttribute;
import objectos.html.tmpl.Api.FloodColorAttribute;
import objectos.html.tmpl.Api.FloodOpacityAttribute;
import objectos.html.tmpl.Api.FontFamilyAttribute;
import objectos.html.tmpl.Api.FontSizeAdjustAttribute;
import objectos.html.tmpl.Api.FontSizeAttribute;
import objectos.html.tmpl.Api.FontStretchAttribute;
import objectos.html.tmpl.Api.FontStyleAttribute;
import objectos.html.tmpl.Api.FontVariantAttribute;
import objectos.html.tmpl.Api.FontWeightAttribute;
import objectos.html.tmpl.Api.FormInstruction;
import objectos.html.tmpl.Api.GlobalAttribute;
import objectos.html.tmpl.Api.GlyphOrientationHorizontalAttribute;
import objectos.html.tmpl.Api.GlyphOrientationVerticalAttribute;
import objectos.html.tmpl.Api.HeightAttribute;
import objectos.html.tmpl.Api.HrefAttribute;
import objectos.html.tmpl.Api.ImageInstruction;
import objectos.html.tmpl.Api.ImageRenderingAttribute;
import objectos.html.tmpl.Api.InputInstruction;
import objectos.html.tmpl.Api.LabelInstruction;
import objectos.html.tmpl.Api.LetterSpacingAttribute;
import objectos.html.tmpl.Api.LightingColorAttribute;
import objectos.html.tmpl.Api.LinkInstruction;
import objectos.html.tmpl.Api.MarkerEndAttribute;
import objectos.html.tmpl.Api.MarkerMidAttribute;
import objectos.html.tmpl.Api.MarkerStartAttribute;
import objectos.html.tmpl.Api.MaskAttribute;
import objectos.html.tmpl.Api.MaskTypeAttribute;
import objectos.html.tmpl.Api.MetaInstruction;
import objectos.html.tmpl.Api.NameAttribute;
import objectos.html.tmpl.Api.OpacityAttribute;
import objectos.html.tmpl.Api.OptionInstruction;
import objectos.html.tmpl.Api.OrderedListInstruction;
import objectos.html.tmpl.Api.OverflowAttribute;
import objectos.html.tmpl.Api.PaintOrderAttribute;
import objectos.html.tmpl.Api.PlaceholderAttribute;
import objectos.html.tmpl.Api.PointerEventsAttribute;
import objectos.html.tmpl.Api.ReadonlyAttribute;
import objectos.html.tmpl.Api.ReferrerpolicyAttribute;
import objectos.html.tmpl.Api.RequiredAttribute;
import objectos.html.tmpl.Api.ScriptInstruction;
import objectos.html.tmpl.Api.SelectInstruction;
import objectos.html.tmpl.Api.ShapeRenderingAttribute;
import objectos.html.tmpl.Api.SrcAttribute;
import objectos.html.tmpl.Api.StopColorAttribute;
import objectos.html.tmpl.Api.StopOpacityAttribute;
import objectos.html.tmpl.Api.StrokeAttribute;
import objectos.html.tmpl.Api.StrokeDasharrayAttribute;
import objectos.html.tmpl.Api.StrokeDashoffsetAttribute;
import objectos.html.tmpl.Api.StrokeLinecapAttribute;
import objectos.html.tmpl.Api.StrokeLinejoinAttribute;
import objectos.html.tmpl.Api.StrokeMiterlimitAttribute;
import objectos.html.tmpl.Api.StrokeOpacityAttribute;
import objectos.html.tmpl.Api.StrokeWidthAttribute;
import objectos.html.tmpl.Api.SvgInstruction;
import objectos.html.tmpl.Api.TableInstruction;
import objectos.html.tmpl.Api.TargetAttribute;
import objectos.html.tmpl.Api.TextAnchorAttribute;
import objectos.html.tmpl.Api.TextAreaInstruction;
import objectos.html.tmpl.Api.TextDecorationAttribute;
import objectos.html.tmpl.Api.TextOverflowAttribute;
import objectos.html.tmpl.Api.TextRenderingAttribute;
import objectos.html.tmpl.Api.TransformAttribute;
import objectos.html.tmpl.Api.TransformOriginAttribute;
import objectos.html.tmpl.Api.TypeAttribute;
import objectos.html.tmpl.Api.UnicodeBidiAttribute;
import objectos.html.tmpl.Api.ValueAttribute;
import objectos.html.tmpl.Api.VectorEffectAttribute;
import objectos.html.tmpl.Api.VisibilityAttribute;
import objectos.html.tmpl.Api.WhiteSpaceAttribute;
import objectos.html.tmpl.Api.WidthAttribute;
import objectos.html.tmpl.Api.WordSpacingAttribute;
import objectos.html.tmpl.Api.WritingModeAttribute;

/**
 * Provides methods for rendering HTML attributes.
 */
// Generated by selfgen.html.HtmlSpec. Do not edit!
public sealed abstract class BaseAttributeDsl permits BaseElementDsl {
  BaseAttributeDsl () {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute accesskey(String value) {
    attribute(StandardAttributeName.ACCESSKEY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FormInstruction action(String value) {
    attribute(StandardAttributeName.ACTION, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TableInstruction align(String value) {
    attribute(StandardAttributeName.ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code alignment-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final AlignmentBaselineAttribute alignmentBaseline(String value) {
    attribute(StandardAttributeName.ALIGNMENTBASELINE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ImageInstruction alt(String value) {
    attribute(StandardAttributeName.ALT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute ariaHidden(String value) {
    attribute(StandardAttributeName.ARIAHIDDEN, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction async() {
    attribute(StandardAttributeName.ASYNC);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final AutocompleteAttribute autocomplete(String value) {
    attribute(StandardAttributeName.AUTOCOMPLETE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final InputInstruction autofocus() {
    attribute(StandardAttributeName.AUTOFOCUS);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BaselineShiftAttribute baselineShift(String value) {
    attribute(StandardAttributeName.BASELINESHIFT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TableInstruction border(String value) {
    attribute(StandardAttributeName.BORDER, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TableInstruction cellpadding(String value) {
    attribute(StandardAttributeName.CELLPADDING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TableInstruction cellspacing(String value) {
    attribute(StandardAttributeName.CELLSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MetaInstruction charset(String value) {
    attribute(StandardAttributeName.CHARSET, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BlockquoteInstruction cite(String value) {
    attribute(StandardAttributeName.CITE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute className(String value) {
    attribute(StandardAttributeName.CLASS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ClipRuleAttribute clipRule(String value) {
    attribute(StandardAttributeName.CLIPRULE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ColorAttribute color(String value) {
    attribute(StandardAttributeName.COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color-interpolation} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ColorInterpolationAttribute colorInterpolation(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATION, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code color-interpolation-filters} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ColorInterpolationFiltersAttribute colorInterpolationFilters(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATIONFILTERS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction cols(String value) {
    attribute(StandardAttributeName.COLS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MetaInstruction content(String value) {
    attribute(StandardAttributeName.CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute contenteditable(String value) {
    attribute(StandardAttributeName.CONTENTEDITABLE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final CrossoriginAttribute crossorigin(String value) {
    attribute(StandardAttributeName.CROSSORIGIN, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final CursorAttribute cursor(String value) {
    attribute(StandardAttributeName.CURSOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final DAttribute d(String value) {
    attribute(StandardAttributeName.D, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction defer() {
    attribute(StandardAttributeName.DEFER);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute dir(String value) {
    attribute(StandardAttributeName.DIR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final DirectionAttribute direction(String value) {
    attribute(StandardAttributeName.DIRECTION, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction dirname(String value) {
    attribute(StandardAttributeName.DIRNAME, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final DisabledAttribute disabled() {
    attribute(StandardAttributeName.DISABLED);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final DisplayAttribute display(String value) {
    attribute(StandardAttributeName.DISPLAY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final DominantBaselineAttribute dominantBaseline(String value) {
    attribute(StandardAttributeName.DOMINANTBASELINE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute draggable(String value) {
    attribute(StandardAttributeName.DRAGGABLE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FormInstruction enctype(String value) {
    attribute(StandardAttributeName.ENCTYPE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FillAttribute fill(String value) {
    attribute(StandardAttributeName.FILL, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FillOpacityAttribute fillOpacity(String value) {
    attribute(StandardAttributeName.FILLOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FillRuleAttribute fillRule(String value) {
    attribute(StandardAttributeName.FILLRULE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FilterAttribute filter(String value) {
    attribute(StandardAttributeName.FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FloodColorAttribute floodColor(String value) {
    attribute(StandardAttributeName.FLOODCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FloodOpacityAttribute floodOpacity(String value) {
    attribute(StandardAttributeName.FLOODOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-family} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontFamilyAttribute fontFamily(String value) {
    attribute(StandardAttributeName.FONTFAMILY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontSizeAttribute fontSize(String value) {
    attribute(StandardAttributeName.FONTSIZE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-size-adjust} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontSizeAdjustAttribute fontSizeAdjust(String value) {
    attribute(StandardAttributeName.FONTSIZEADJUST, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-stretch} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontStretchAttribute fontStretch(String value) {
    attribute(StandardAttributeName.FONTSTRETCH, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontStyleAttribute fontStyle(String value) {
    attribute(StandardAttributeName.FONTSTYLE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-variant} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontVariantAttribute fontVariant(String value) {
    attribute(StandardAttributeName.FONTVARIANT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code font-weight} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FontWeightAttribute fontWeight(String value) {
    attribute(StandardAttributeName.FONTWEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LabelInstruction forAttr(String value) {
    attribute(StandardAttributeName.FOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LabelInstruction forElement(String value) {
    attribute(StandardAttributeName.FOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code glyph-orientation-horizontal} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlyphOrientationHorizontalAttribute glyphOrientationHorizontal(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONHORIZONTAL, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code glyph-orientation-vertical} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlyphOrientationVerticalAttribute glyphOrientationVertical(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONVERTICAL, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final HeightAttribute height(String value) {
    attribute(StandardAttributeName.HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute hidden() {
    attribute(StandardAttributeName.HIDDEN);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final HrefAttribute href(String value) {
    attribute(StandardAttributeName.HREF, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MetaInstruction httpEquiv(String value) {
    attribute(StandardAttributeName.HTTPEQUIV, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute id(String value) {
    attribute(StandardAttributeName.ID, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ImageRenderingAttribute imageRendering(String value) {
    attribute(StandardAttributeName.IMAGERENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction integrity(String value) {
    attribute(StandardAttributeName.INTEGRITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute lang(String value) {
    attribute(StandardAttributeName.LANG, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LetterSpacingAttribute letterSpacing(String value) {
    attribute(StandardAttributeName.LETTERSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LightingColorAttribute lightingColor(String value) {
    attribute(StandardAttributeName.LIGHTINGCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MarkerEndAttribute markerEnd(String value) {
    attribute(StandardAttributeName.MARKEREND, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MarkerMidAttribute markerMid(String value) {
    attribute(StandardAttributeName.MARKERMID, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MarkerStartAttribute markerStart(String value) {
    attribute(StandardAttributeName.MARKERSTART, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MaskAttribute mask(String value) {
    attribute(StandardAttributeName.MASK, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MaskTypeAttribute maskType(String value) {
    attribute(StandardAttributeName.MASKTYPE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction maxlength(String value) {
    attribute(StandardAttributeName.MAXLENGTH, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LinkInstruction media(String value) {
    attribute(StandardAttributeName.MEDIA, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final FormInstruction method(String value) {
    attribute(StandardAttributeName.METHOD, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction minlength(String value) {
    attribute(StandardAttributeName.MINLENGTH, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final SelectInstruction multiple() {
    attribute(StandardAttributeName.MULTIPLE);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final NameAttribute name(String value) {
    attribute(StandardAttributeName.NAME, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction nomodule() {
    attribute(StandardAttributeName.NOMODULE);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onafterprint(String value) {
    attribute(StandardAttributeName.ONAFTERPRINT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onbeforeprint(String value) {
    attribute(StandardAttributeName.ONBEFOREPRINT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onbeforeunload(String value) {
    attribute(StandardAttributeName.ONBEFOREUNLOAD, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute onclick(String value) {
    attribute(StandardAttributeName.ONCLICK, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onhashchange(String value) {
    attribute(StandardAttributeName.ONHASHCHANGE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onlanguagechange(String value) {
    attribute(StandardAttributeName.ONLANGUAGECHANGE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onmessage(String value) {
    attribute(StandardAttributeName.ONMESSAGE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onoffline(String value) {
    attribute(StandardAttributeName.ONOFFLINE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction ononline(String value) {
    attribute(StandardAttributeName.ONONLINE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onpagehide(String value) {
    attribute(StandardAttributeName.ONPAGEHIDE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onpageshow(String value) {
    attribute(StandardAttributeName.ONPAGESHOW, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onpopstate(String value) {
    attribute(StandardAttributeName.ONPOPSTATE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onrejectionhandled} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onrejectionhandled(String value) {
    attribute(StandardAttributeName.ONREJECTIONHANDLED, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onstorage(String value) {
    attribute(StandardAttributeName.ONSTORAGE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute onsubmit(String value) {
    attribute(StandardAttributeName.ONSUBMIT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onunhandledrejection} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onunhandledrejection(String value) {
    attribute(StandardAttributeName.ONUNHANDLEDREJECTION, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final BodyInstruction onunload(String value) {
    attribute(StandardAttributeName.ONUNLOAD, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final OpacityAttribute opacity(String value) {
    attribute(StandardAttributeName.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final DetailsInstruction open() {
    attribute(StandardAttributeName.OPEN);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final OverflowAttribute overflow(String value) {
    attribute(StandardAttributeName.OVERFLOW, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final PaintOrderAttribute paintOrder(String value) {
    attribute(StandardAttributeName.PAINTORDER, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final PlaceholderAttribute placeholder(String value) {
    attribute(StandardAttributeName.PLACEHOLDER, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final PointerEventsAttribute pointerEvents(String value) {
    attribute(StandardAttributeName.POINTEREVENTS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final MetaInstruction property(String value) {
    attribute(StandardAttributeName.PROPERTY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ReadonlyAttribute readonly() {
    attribute(StandardAttributeName.READONLY);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ReferrerpolicyAttribute referrerpolicy(String value) {
    attribute(StandardAttributeName.REFERRERPOLICY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LinkInstruction rel(String value) {
    attribute(StandardAttributeName.REL, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final RequiredAttribute required() {
    attribute(StandardAttributeName.REQUIRED);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LinkInstruction rev(String value) {
    attribute(StandardAttributeName.REV, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final OrderedListInstruction reversed() {
    attribute(StandardAttributeName.REVERSED);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute role(String value) {
    attribute(StandardAttributeName.ROLE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction rows(String value) {
    attribute(StandardAttributeName.ROWS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final OptionInstruction selected() {
    attribute(StandardAttributeName.SELECTED);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ShapeRenderingAttribute shapeRendering(String value) {
    attribute(StandardAttributeName.SHAPERENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final SelectInstruction size(String value) {
    attribute(StandardAttributeName.SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final LinkInstruction sizes(String value) {
    attribute(StandardAttributeName.SIZES, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute spellcheck(String value) {
    attribute(StandardAttributeName.SPELLCHECK, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final SrcAttribute src(String value) {
    attribute(StandardAttributeName.SRC, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ImageInstruction srcset(String value) {
    attribute(StandardAttributeName.SRCSET, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final OrderedListInstruction start(String value) {
    attribute(StandardAttributeName.START, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StopColorAttribute stopColor(String value) {
    attribute(StandardAttributeName.STOPCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StopOpacityAttribute stopOpacity(String value) {
    attribute(StandardAttributeName.STOPOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeAttribute stroke(String value) {
    attribute(StandardAttributeName.STROKE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeDasharrayAttribute strokeDasharray(String value) {
    attribute(StandardAttributeName.STROKEDASHARRAY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeDashoffsetAttribute strokeDashoffset(String value) {
    attribute(StandardAttributeName.STROKEDASHOFFSET, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeLinecapAttribute strokeLinecap(String value) {
    attribute(StandardAttributeName.STROKELINECAP, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeLinejoinAttribute strokeLinejoin(String value) {
    attribute(StandardAttributeName.STROKELINEJOIN, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeMiterlimitAttribute strokeMiterlimit(String value) {
    attribute(StandardAttributeName.STROKEMITERLIMIT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeOpacityAttribute strokeOpacity(String value) {
    attribute(StandardAttributeName.STROKEOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final StrokeWidthAttribute strokeWidth(String value) {
    attribute(StandardAttributeName.STROKEWIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute inlineStyle(String value) {
    attribute(StandardAttributeName.STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute tabindex(String value) {
    attribute(StandardAttributeName.TABINDEX, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TargetAttribute target(String value) {
    attribute(StandardAttributeName.TARGET, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAnchorAttribute textAnchor(String value) {
    attribute(StandardAttributeName.TEXTANCHOR, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextDecorationAttribute textDecoration(String value) {
    attribute(StandardAttributeName.TEXTDECORATION, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextOverflowAttribute textOverflow(String value) {
    attribute(StandardAttributeName.TEXTOVERFLOW, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextRenderingAttribute textRendering(String value) {
    attribute(StandardAttributeName.TEXTRENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TransformAttribute transform(String value) {
    attribute(StandardAttributeName.TRANSFORM, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TransformOriginAttribute transformOrigin(String value) {
    attribute(StandardAttributeName.TRANSFORMORIGIN, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute translate(String value) {
    attribute(StandardAttributeName.TRANSLATE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TypeAttribute type(String value) {
    attribute(StandardAttributeName.TYPE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final UnicodeBidiAttribute unicodeBidi(String value) {
    attribute(StandardAttributeName.UNICODEBIDI, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ValueAttribute value(String value) {
    attribute(StandardAttributeName.VALUE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final VectorEffectAttribute vectorEffect(String value) {
    attribute(StandardAttributeName.VECTOREFFECT, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final SvgInstruction viewBox(String value) {
    attribute(StandardAttributeName.VIEWBOX, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final VisibilityAttribute visibility(String value) {
    attribute(StandardAttributeName.VISIBILITY, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final WhiteSpaceAttribute whiteSpace(String value) {
    attribute(StandardAttributeName.WHITESPACE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final WidthAttribute width(String value) {
    attribute(StandardAttributeName.WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final WordSpacingAttribute wordSpacing(String value) {
    attribute(StandardAttributeName.WORDSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final TextAreaInstruction wrap(String value) {
    attribute(StandardAttributeName.WRAP, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final WritingModeAttribute writingMode(String value) {
    attribute(StandardAttributeName.WRITINGMODE, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final SvgInstruction xmlns(String value) {
    attribute(StandardAttributeName.XMLNS, value);
    return InternalInstruction.INSTANCE;
  }

  /**
   * Generates the {@code clip-path} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final ClipPathAttribute clipPath(String value) {
    api().attribute(StandardAttributeName.CLIPPATH, value);

    return InternalInstruction.INSTANCE;
  }

  abstract HtmlTemplateApi api();

  abstract void attribute(StandardAttributeName name);

  abstract void attribute(StandardAttributeName name, String value);
}
