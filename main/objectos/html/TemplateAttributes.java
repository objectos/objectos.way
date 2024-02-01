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

/**
 * Provides methods for rendering HTML attributes in templates.
 */
// Generated by objectos.selfgen.HtmlSpec. Do not edit!
public sealed abstract class TemplateAttributes permits TemplateElements {
  Html html;

  TemplateAttributes() {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute accesskey(String value) {
    return html.accesskey(value);
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
    return html.action(value);
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
    return html.align(value);
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
    return html.alignmentBaseline(value);
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
    return html.alt(value);
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
    return html.ariaHidden(value);
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction async() {
    return html.async();
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
    return html.autocomplete(value);
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final InputInstruction autofocus() {
    return html.autofocus();
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
    return html.baselineShift(value);
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
    return html.border(value);
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
    return html.cellpadding(value);
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
    return html.cellspacing(value);
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
    return html.charset(value);
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
    return html.cite(value);
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
    return html.className(value);
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
    return html.clipRule(value);
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
    return html.color(value);
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
    return html.colorInterpolation(value);
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
    return html.colorInterpolationFilters(value);
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
    return html.cols(value);
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
    return html.content(value);
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
    return html.contenteditable(value);
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
    return html.crossorigin(value);
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
    return html.cursor(value);
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
    return html.d(value);
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction defer() {
    return html.defer();
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
    return html.dir(value);
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
    return html.direction(value);
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
    return html.dirname(value);
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final DisabledAttribute disabled() {
    return html.disabled();
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
    return html.display(value);
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
    return html.dominantBaseline(value);
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
    return html.draggable(value);
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
    return html.enctype(value);
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
    return html.fill(value);
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
    return html.fillOpacity(value);
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
    return html.fillRule(value);
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
    return html.filter(value);
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
    return html.floodColor(value);
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
    return html.floodOpacity(value);
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
    return html.fontFamily(value);
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
    return html.fontSize(value);
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
    return html.fontSizeAdjust(value);
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
    return html.fontStretch(value);
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
    return html.fontStyle(value);
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
    return html.fontVariant(value);
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
    return html.fontWeight(value);
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
    return html.forAttr(value);
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
    return html.forElement(value);
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
    return html.glyphOrientationHorizontal(value);
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
    return html.glyphOrientationVertical(value);
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
    return html.height(value);
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final GlobalAttribute hidden() {
    return html.hidden();
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
    return html.href(value);
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
    return html.httpEquiv(value);
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
    return html.id(value);
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
    return html.imageRendering(value);
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
    return html.integrity(value);
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
    return html.lang(value);
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
    return html.letterSpacing(value);
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
    return html.lightingColor(value);
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
    return html.markerEnd(value);
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
    return html.markerMid(value);
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
    return html.markerStart(value);
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
    return html.mask(value);
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
    return html.maskType(value);
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
    return html.maxlength(value);
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
    return html.media(value);
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
    return html.method(value);
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
    return html.minlength(value);
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final SelectInstruction multiple() {
    return html.multiple();
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
    return html.name(value);
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ScriptInstruction nomodule() {
    return html.nomodule();
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
    return html.onafterprint(value);
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
    return html.onbeforeprint(value);
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
    return html.onbeforeunload(value);
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
    return html.onclick(value);
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
    return html.onhashchange(value);
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
    return html.onlanguagechange(value);
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
    return html.onmessage(value);
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
    return html.onoffline(value);
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
    return html.ononline(value);
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
    return html.onpagehide(value);
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
    return html.onpageshow(value);
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
    return html.onpopstate(value);
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
    return html.onrejectionhandled(value);
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
    return html.onstorage(value);
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
    return html.onsubmit(value);
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
    return html.onunhandledrejection(value);
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
    return html.onunload(value);
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
    return html.opacity(value);
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final DetailsInstruction open() {
    return html.open();
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
    return html.overflow(value);
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
    return html.paintOrder(value);
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
    return html.placeholder(value);
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
    return html.pointerEvents(value);
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
    return html.property(value);
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final ReadonlyAttribute readonly() {
    return html.readonly();
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
    return html.referrerpolicy(value);
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
    return html.rel(value);
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final RequiredAttribute required() {
    return html.required();
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
    return html.rev(value);
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final OrderedListInstruction reversed() {
    return html.reversed();
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
    return html.role(value);
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
    return html.rows(value);
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  protected final OptionInstruction selected() {
    return html.selected();
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
    return html.shapeRendering(value);
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
    return html.size(value);
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
    return html.sizes(value);
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
    return html.spellcheck(value);
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
    return html.src(value);
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
    return html.srcset(value);
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
    return html.start(value);
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
    return html.stopColor(value);
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
    return html.stopOpacity(value);
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
    return html.stroke(value);
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
    return html.strokeDasharray(value);
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
    return html.strokeDashoffset(value);
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
    return html.strokeLinecap(value);
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
    return html.strokeLinejoin(value);
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
    return html.strokeMiterlimit(value);
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
    return html.strokeOpacity(value);
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
    return html.strokeWidth(value);
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
    return html.inlineStyle(value);
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
    return html.tabindex(value);
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
    return html.target(value);
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
    return html.textAnchor(value);
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
    return html.textDecoration(value);
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
    return html.textOverflow(value);
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
    return html.textRendering(value);
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
    return html.transform(value);
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
    return html.transformOrigin(value);
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
    return html.translate(value);
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
    return html.type(value);
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
    return html.unicodeBidi(value);
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
    return html.value(value);
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
    return html.vectorEffect(value);
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
    return html.viewBox(value);
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
    return html.visibility(value);
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
    return html.whiteSpace(value);
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
    return html.width(value);
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
    return html.wordSpacing(value);
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
    return html.wrap(value);
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
    return html.writingMode(value);
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
    return html.xmlns(value);
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
    return html.clipPath(value);
  }
}
