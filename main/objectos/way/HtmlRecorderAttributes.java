/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.Objects;

/**
 * Provides methods for rendering HTML attributes.
 */
sealed class HtmlRecorderAttributes extends HtmlRecorderBase permits HtmlRecorderElements {

  HtmlRecorderAttributes() {}

  /**
   * Generates the {@code accesskey} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute accesskey(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACCESSKEY, value);
  }

  /**
   * Generates the {@code action} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute action(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ACTION, value);
  }

  /**
   * Generates the {@code align} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute align(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGN, value);
  }

  /**
   * Generates the {@code alignment-baseline} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute alignmentBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  /**
   * Generates the {@code alt} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute alt(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ALT, value);
  }

  /**
   * Generates the {@code aria-hidden} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ariaHidden(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  /**
   * Generates the {@code aria-label} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ariaLabel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ARIA_LABEL, value);
  }

  /**
   * Generates the {@code async} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute async() {
    return attribute0(HtmlAttributeName.ASYNC);
  }

  /**
   * Generates the {@code autocomplete} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute autocomplete(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  /**
   * Generates the {@code autofocus} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute autofocus() {
    return attribute0(HtmlAttributeName.AUTOFOCUS);
  }

  /**
   * Generates the {@code baseline-shift} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute baselineShift(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  /**
   * Generates the {@code border} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute border(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.BORDER, value);
  }

  /**
   * Generates the {@code cellpadding} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cellpadding(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLPADDING, value);
  }

  /**
   * Generates the {@code cellspacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cellspacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CELLSPACING, value);
  }

  /**
   * Generates the {@code charset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute charset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CHARSET, value);
  }

  /**
   * Generates the {@code cite} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cite(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CITE, value);
  }

  /**
   * Generates the {@code class} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute className(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLASS, value);
  }

  /**
   * Generates the {@code clip-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute clipRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CLIP_RULE, value);
  }

  /**
   * Generates the {@code color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute color(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR, value);
  }

  /**
   * Generates the {@code color-interpolation} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute colorInterpolation(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION, value);
  }

  /**
   * Generates the {@code color-interpolation-filters} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  /**
   * Generates the {@code cols} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cols(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.COLS, value);
  }

  /**
   * Generates the {@code content} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute content(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENT, value);
  }

  /**
   * Generates the {@code contenteditable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute contenteditable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  /**
   * Generates the {@code crossorigin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute crossorigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CROSSORIGIN, value);
  }

  /**
   * Generates the {@code cursor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute cursor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.CURSOR, value);
  }

  /**
   * Generates the {@code d} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute d(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.D, value);
  }

  /**
   * Generates the {@code defer} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute defer() {
    return attribute0(HtmlAttributeName.DEFER);
  }

  /**
   * Generates the {@code dir} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dir(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIR, value);
  }

  /**
   * Generates the {@code direction} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute direction(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRECTION, value);
  }

  /**
   * Generates the {@code dirname} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dirname(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DIRNAME, value);
  }

  /**
   * Generates the {@code disabled} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute disabled() {
    return attribute0(HtmlAttributeName.DISABLED);
  }

  /**
   * Generates the {@code display} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute display(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DISPLAY, value);
  }

  /**
   * Generates the {@code dominant-baseline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute dominantBaseline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  /**
   * Generates the {@code draggable} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute draggable(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.DRAGGABLE, value);
  }

  /**
   * Generates the {@code enctype} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute enctype(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ENCTYPE, value);
  }

  /**
   * Generates the {@code fill} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fill(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL, value);
  }

  /**
   * Generates the {@code fill-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fillOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_OPACITY, value);
  }

  /**
   * Generates the {@code fill-rule} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fillRule(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILL_RULE, value);
  }

  /**
   * Generates the {@code filter} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute filter(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FILTER, value);
  }

  /**
   * Generates the {@code flood-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute floodColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  /**
   * Generates the {@code flood-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute floodOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  /**
   * Generates the {@code font-family} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontFamily(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_FAMILY, value);
  }

  /**
   * Generates the {@code font-size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontSize(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE, value);
  }

  /**
   * Generates the {@code font-size-adjust} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontSizeAdjust(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_SIZE_ADJUST, value);
  }

  /**
   * Generates the {@code font-stretch} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontStretch(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STRETCH, value);
  }

  /**
   * Generates the {@code font-style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_STYLE, value);
  }

  /**
   * Generates the {@code font-variant} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontVariant(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_VARIANT, value);
  }

  /**
   * Generates the {@code font-weight} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute fontWeight(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FONT_WEIGHT, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute forAttr(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Generates the {@code for} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute forElement(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.FOR, value);
  }

  /**
   * Generates the {@code glyph-orientation-horizontal} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  /**
   * Generates the {@code glyph-orientation-vertical} attribute with the
   * specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  /**
   * Generates the {@code height} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute height(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HEIGHT, value);
  }

  /**
   * Generates the {@code hidden} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute hidden() {
    return attribute0(HtmlAttributeName.HIDDEN);
  }

  /**
   * Generates the {@code href} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute href(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HREF, value);
  }

  /**
   * Generates the {@code http-equiv} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute httpEquiv(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  /**
   * Generates the {@code id} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute id(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ID, value);
  }

  /**
   * Generates the {@code image-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute imageRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  /**
   * Generates the {@code integrity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute integrity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.INTEGRITY, value);
  }

  /**
   * Generates the {@code lang} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute lang(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LANG, value);
  }

  /**
   * Generates the {@code letter-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute letterSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LETTER_SPACING, value);
  }

  /**
   * Generates the {@code lighting-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute lightingColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  /**
   * Generates the {@code marker-end} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerEnd(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_END, value);
  }

  /**
   * Generates the {@code marker-mid} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerMid(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_MID, value);
  }

  /**
   * Generates the {@code marker-start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute markerStart(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MARKER_START, value);
  }

  /**
   * Generates the {@code mask} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute mask(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK, value);
  }

  /**
   * Generates the {@code mask-type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute maskType(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MASK_TYPE, value);
  }

  /**
   * Generates the {@code maxlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute maxlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MAXLENGTH, value);
  }

  /**
   * Generates the {@code media} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute media(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MEDIA, value);
  }

  /**
   * Generates the {@code method} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute method(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.METHOD, value);
  }

  /**
   * Generates the {@code minlength} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute minlength(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.MINLENGTH, value);
  }

  /**
   * Generates the {@code multiple} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute multiple() {
    return attribute0(HtmlAttributeName.MULTIPLE);
  }

  /**
   * Generates the {@code name} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute name(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.NAME, value);
  }

  /**
   * Generates the {@code nomodule} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute nomodule() {
    return attribute0(HtmlAttributeName.NOMODULE);
  }

  /**
   * Generates the {@code onafterprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onafterprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  /**
   * Generates the {@code onbeforeprint} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onbeforeprint(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  /**
   * Generates the {@code onbeforeunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onbeforeunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  /**
   * Generates the {@code onclick} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onclick(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONCLICK, value);
  }

  /**
   * Generates the {@code onhashchange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onhashchange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  /**
   * Generates the {@code onlanguagechange} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onlanguagechange(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  /**
   * Generates the {@code onmessage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onmessage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONMESSAGE, value);
  }

  /**
   * Generates the {@code onoffline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onoffline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONOFFLINE, value);
  }

  /**
   * Generates the {@code ononline} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute ononline(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONONLINE, value);
  }

  /**
   * Generates the {@code onpagehide} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpagehide(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  /**
   * Generates the {@code onpageshow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpageshow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPAGESHOW, value);
  }

  /**
   * Generates the {@code onpopstate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onpopstate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONPOPSTATE, value);
  }

  /**
   * Generates the {@code onrejectionhandled} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onrejectionhandled(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  /**
   * Generates the {@code onstorage} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onstorage(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSTORAGE, value);
  }

  /**
   * Generates the {@code onsubmit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onsubmit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONSUBMIT, value);
  }

  /**
   * Generates the {@code onunhandledrejection} attribute with the specified
   * value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onunhandledrejection(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  /**
   * Generates the {@code onunload} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute onunload(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ONUNLOAD, value);
  }

  /**
   * Generates the {@code opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute opacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OPACITY, value);
  }

  /**
   * Generates the {@code open} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute open() {
    return attribute0(HtmlAttributeName.OPEN);
  }

  /**
   * Generates the {@code overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute overflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.OVERFLOW, value);
  }

  /**
   * Generates the {@code paint-order} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute paintOrder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PAINT_ORDER, value);
  }

  /**
   * Generates the {@code placeholder} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute placeholder(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PLACEHOLDER, value);
  }

  /**
   * Generates the {@code pointer-events} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute pointerEvents(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  /**
   * Generates the {@code property} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute property(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.PROPERTY, value);
  }

  /**
   * Generates the {@code readonly} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute readonly() {
    return attribute0(HtmlAttributeName.READONLY);
  }

  /**
   * Generates the {@code referrerpolicy} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute referrerpolicy(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  /**
   * Generates the {@code rel} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rel(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REL, value);
  }

  /**
   * Generates the {@code required} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute required() {
    return attribute0(HtmlAttributeName.REQUIRED);
  }

  /**
   * Generates the {@code rev} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rev(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.REV, value);
  }

  /**
   * Generates the {@code reversed} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute reversed() {
    return attribute0(HtmlAttributeName.REVERSED);
  }

  /**
   * Generates the {@code role} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute role(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROLE, value);
  }

  /**
   * Generates the {@code rows} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute rows(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.ROWS, value);
  }

  /**
   * Generates the {@code selected} boolean attribute.
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute selected() {
    return attribute0(HtmlAttributeName.SELECTED);
  }

  /**
   * Generates the {@code shape-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute shapeRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  /**
   * Generates the {@code size} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute size(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZE, value);
  }

  /**
   * Generates the {@code sizes} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute sizes(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SIZES, value);
  }

  /**
   * Generates the {@code spellcheck} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute spellcheck(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SPELLCHECK, value);
  }

  /**
   * Generates the {@code src} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute src(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRC, value);
  }

  /**
   * Generates the {@code srcset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute srcset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.SRCSET, value);
  }

  /**
   * Generates the {@code start} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute start(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.START, value);
  }

  /**
   * Generates the {@code stop-color} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stopColor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_COLOR, value);
  }

  /**
   * Generates the {@code stop-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stopOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STOP_OPACITY, value);
  }

  /**
   * Generates the {@code stroke} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute stroke(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE, value);
  }

  /**
   * Generates the {@code stroke-dasharray} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeDasharray(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  /**
   * Generates the {@code stroke-dashoffset} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeDashoffset(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  /**
   * Generates the {@code stroke-linecap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeLinecap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  /**
   * Generates the {@code stroke-linejoin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeLinejoin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  /**
   * Generates the {@code stroke-miterlimit} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  /**
   * Generates the {@code stroke-opacity} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeOpacity(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  /**
   * Generates the {@code stroke-width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute strokeWidth(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  /**
   * Generates the {@code style} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute inlineStyle(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.STYLE, value);
  }

  /**
   * Generates the {@code tabindex} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute tabindex(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TABINDEX, value);
  }

  /**
   * Generates the {@code target} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute target(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TARGET, value);
  }

  /**
   * Generates the {@code text-anchor} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textAnchor(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  /**
   * Generates the {@code text-decoration} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textDecoration(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  /**
   * Generates the {@code text-overflow} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textOverflow(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  /**
   * Generates the {@code text-rendering} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute textRendering(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  /**
   * Generates the {@code transform} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute transform(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM, value);
  }

  /**
   * Generates the {@code transform-origin} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute transformOrigin(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  /**
   * Generates the {@code translate} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute translate(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TRANSLATE, value);
  }

  /**
   * Generates the {@code type} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute type(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.TYPE, value);
  }

  /**
   * Generates the {@code unicode-bidi} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute unicodeBidi(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  /**
   * Generates the {@code value} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute value(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VALUE, value);
  }

  /**
   * Generates the {@code vector-effect} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute vectorEffect(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  /**
   * Generates the {@code viewBox} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute viewBox(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VIEWBOX, value);
  }

  /**
   * Generates the {@code visibility} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute visibility(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.VISIBILITY, value);
  }

  /**
   * Generates the {@code white-space} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute whiteSpace(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WHITE_SPACE, value);
  }

  /**
   * Generates the {@code width} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute width(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WIDTH, value);
  }

  /**
   * Generates the {@code word-spacing} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute wordSpacing(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WORD_SPACING, value);
  }

  /**
   * Generates the {@code wrap} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute wrap(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRAP, value);
  }

  /**
   * Generates the {@code writing-mode} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute writingMode(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.WRITING_MODE, value);
  }

  /**
   * Generates the {@code xmlns} attribute with the specified value.
   *
   * @param value
   *        the value of the attribute
   *
   * @return an instruction representing this attribute.
   */
  public final Html.Instruction.OfAttribute xmlns(String value) {
    Objects.requireNonNull(value, "value == null");
    return attribute0(HtmlAttributeName.XMLNS, value);
  }

}