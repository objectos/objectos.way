/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

sealed abstract class HtmlMarkup permits HtmlMarkupOfHtml, HtmlMarkupOfTestable {

  //
  // ELEMENTS
  //

  abstract Html.Instruction.OfElement elem0(Html.ElementName name, Html.Instruction... contents);

  abstract Html.Instruction.OfElement elem0(Html.ElementName name, String text);

  public final Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, contents);
  }

  public final Html.Instruction.OfElement elem(Html.ElementName name, String text) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, text);
  }

  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return elem0(HtmlElementName.A, contents);
  }

  public final Html.Instruction.OfElement a(String text) {
    return elem0(HtmlElementName.A, text);
  }

  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return elem0(HtmlElementName.ABBR, contents);
  }

  public final Html.Instruction.OfElement abbr(String text) {
    return elem0(HtmlElementName.ABBR, text);
  }

  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return elem0(HtmlElementName.ARTICLE, contents);
  }

  public final Html.Instruction.OfElement article(String text) {
    return elem0(HtmlElementName.ARTICLE, text);
  }

  public final Html.Instruction.OfElement aside(Html.Instruction... contents) {
    return elem0(HtmlElementName.ASIDE, contents);
  }

  public final Html.Instruction.OfElement aside(String text) {
    return elem0(HtmlElementName.ASIDE, text);
  }

  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return elem0(HtmlElementName.B, contents);
  }

  public final Html.Instruction.OfElement b(String text) {
    return elem0(HtmlElementName.B, text);
  }

  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return elem0(HtmlElementName.BLOCKQUOTE, contents);
  }

  public final Html.Instruction.OfElement blockquote(String text) {
    return elem0(HtmlElementName.BLOCKQUOTE, text);
  }

  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return elem0(HtmlElementName.BODY, contents);
  }

  public final Html.Instruction.OfElement body(String text) {
    return elem0(HtmlElementName.BODY, text);
  }

  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.BR, contents);
  }

  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return elem0(HtmlElementName.BUTTON, contents);
  }

  public final Html.Instruction.OfElement button(String text) {
    return elem0(HtmlElementName.BUTTON, text);
  }

  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return elem0(HtmlElementName.CLIPPATH, contents);
  }

  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return elem0(HtmlElementName.CODE, contents);
  }

  public final Html.Instruction.OfElement code(String text) {
    return elem0(HtmlElementName.CODE, text);
  }

  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return elem0(HtmlElementName.DD, contents);
  }

  public final Html.Instruction.OfElement dd(String text) {
    return elem0(HtmlElementName.DD, text);
  }

  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return elem0(HtmlElementName.DEFS, contents);
  }

  public final Html.Instruction.OfElement defs(String text) {
    return elem0(HtmlElementName.DEFS, text);
  }

  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return elem0(HtmlElementName.DETAILS, contents);
  }

  public final Html.Instruction.OfElement details(String text) {
    return elem0(HtmlElementName.DETAILS, text);
  }

  public final Html.Instruction.OfElement dialog(Html.Instruction... contents) {
    return elem0(HtmlElementName.DIALOG, contents);
  }

  public final Html.Instruction.OfElement dialog(String text) {
    return elem0(HtmlElementName.DIALOG, text);
  }

  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return elem0(HtmlElementName.DIV, contents);
  }

  public final Html.Instruction.OfElement div(String text) {
    return elem0(HtmlElementName.DIV, text);
  }

  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return elem0(HtmlElementName.DL, contents);
  }

  public final Html.Instruction.OfElement dl(String text) {
    return elem0(HtmlElementName.DL, text);
  }

  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return elem0(HtmlElementName.DT, contents);
  }

  public final Html.Instruction.OfElement dt(String text) {
    return elem0(HtmlElementName.DT, text);
  }

  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return elem0(HtmlElementName.EM, contents);
  }

  public final Html.Instruction.OfElement em(String text) {
    return elem0(HtmlElementName.EM, text);
  }

  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return elem0(HtmlElementName.FIELDSET, contents);
  }

  public final Html.Instruction.OfElement fieldset(String text) {
    return elem0(HtmlElementName.FIELDSET, text);
  }

  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return elem0(HtmlElementName.FIGURE, contents);
  }

  public final Html.Instruction.OfElement figure(String text) {
    return elem0(HtmlElementName.FIGURE, text);
  }

  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return elem0(HtmlElementName.FOOTER, contents);
  }

  public final Html.Instruction.OfElement footer(String text) {
    return elem0(HtmlElementName.FOOTER, text);
  }

  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return elem0(HtmlElementName.FORM, contents);
  }

  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return elem0(HtmlElementName.G, contents);
  }

  public final Html.Instruction.OfElement g(String text) {
    return elem0(HtmlElementName.G, text);
  }

  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return elem0(HtmlElementName.H1, contents);
  }

  public final Html.Instruction.OfElement h1(String text) {
    return elem0(HtmlElementName.H1, text);
  }

  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return elem0(HtmlElementName.H2, contents);
  }

  public final Html.Instruction.OfElement h2(String text) {
    return elem0(HtmlElementName.H2, text);
  }

  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return elem0(HtmlElementName.H3, contents);
  }

  public final Html.Instruction.OfElement h3(String text) {
    return elem0(HtmlElementName.H3, text);
  }

  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return elem0(HtmlElementName.H4, contents);
  }

  public final Html.Instruction.OfElement h4(String text) {
    return elem0(HtmlElementName.H4, text);
  }

  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return elem0(HtmlElementName.H5, contents);
  }

  public final Html.Instruction.OfElement h5(String text) {
    return elem0(HtmlElementName.H5, text);
  }

  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return elem0(HtmlElementName.H6, contents);
  }

  public final Html.Instruction.OfElement h6(String text) {
    return elem0(HtmlElementName.H6, text);
  }

  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return elem0(HtmlElementName.HEAD, contents);
  }

  public final Html.Instruction.OfElement head(String text) {
    return elem0(HtmlElementName.HEAD, text);
  }

  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return elem0(HtmlElementName.HEADER, contents);
  }

  public final Html.Instruction.OfElement header(String text) {
    return elem0(HtmlElementName.HEADER, text);
  }

  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return elem0(HtmlElementName.HGROUP, contents);
  }

  public final Html.Instruction.OfElement hgroup(String text) {
    return elem0(HtmlElementName.HGROUP, text);
  }

  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.HR, contents);
  }

  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return elem0(HtmlElementName.HTML, contents);
  }

  public final Html.Instruction.OfElement html(String text) {
    return elem0(HtmlElementName.HTML, text);
  }

  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.IMG, contents);
  }

  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.INPUT, contents);
  }

  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return elem0(HtmlElementName.KBD, contents);
  }

  public final Html.Instruction.OfElement kbd(String text) {
    return elem0(HtmlElementName.KBD, text);
  }

  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return elem0(HtmlElementName.LABEL, contents);
  }

  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return elem0(HtmlElementName.LEGEND, contents);
  }

  public final Html.Instruction.OfElement legend(String text) {
    return elem0(HtmlElementName.LEGEND, text);
  }

  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return elem0(HtmlElementName.LI, contents);
  }

  public final Html.Instruction.OfElement li(String text) {
    return elem0(HtmlElementName.LI, text);
  }

  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.LINK, contents);
  }

  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return elem0(HtmlElementName.MAIN, contents);
  }

  public final Html.Instruction.OfElement main(String text) {
    return elem0(HtmlElementName.MAIN, text);
  }

  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return elem0(HtmlElementName.MENU, contents);
  }

  public final Html.Instruction.OfElement menu(String text) {
    return elem0(HtmlElementName.MENU, text);
  }

  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.META, contents);
  }

  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return elem0(HtmlElementName.NAV, contents);
  }

  public final Html.Instruction.OfElement nav(String text) {
    return elem0(HtmlElementName.NAV, text);
  }

  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return elem0(HtmlElementName.OL, contents);
  }

  public final Html.Instruction.OfElement ol(String text) {
    return elem0(HtmlElementName.OL, text);
  }

  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return elem0(HtmlElementName.OPTGROUP, contents);
  }

  public final Html.Instruction.OfElement optgroup(String text) {
    return elem0(HtmlElementName.OPTGROUP, text);
  }

  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return elem0(HtmlElementName.OPTION, contents);
  }

  public final Html.Instruction.OfElement option(String text) {
    return elem0(HtmlElementName.OPTION, text);
  }

  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return elem0(HtmlElementName.P, contents);
  }

  public final Html.Instruction.OfElement p(String text) {
    return elem0(HtmlElementName.P, text);
  }

  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return elem0(HtmlElementName.PATH, contents);
  }

  public final Html.Instruction.OfElement path(String text) {
    return elem0(HtmlElementName.PATH, text);
  }

  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return elem0(HtmlElementName.PRE, contents);
  }

  public final Html.Instruction.OfElement pre(String text) {
    return elem0(HtmlElementName.PRE, text);
  }

  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return elem0(HtmlElementName.PROGRESS, contents);
  }

  public final Html.Instruction.OfElement progress(String text) {
    return elem0(HtmlElementName.PROGRESS, text);
  }

  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return elem0(HtmlElementName.SAMP, contents);
  }

  public final Html.Instruction.OfElement samp(String text) {
    return elem0(HtmlElementName.SAMP, text);
  }

  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return elem0(HtmlElementName.SCRIPT, contents);
  }

  public final Html.Instruction.OfElement script(String text) {
    return elem0(HtmlElementName.SCRIPT, text);
  }

  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return elem0(HtmlElementName.SECTION, contents);
  }

  public final Html.Instruction.OfElement section(String text) {
    return elem0(HtmlElementName.SECTION, text);
  }

  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return elem0(HtmlElementName.SELECT, contents);
  }

  public final Html.Instruction.OfElement select(String text) {
    return elem0(HtmlElementName.SELECT, text);
  }

  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return elem0(HtmlElementName.SMALL, contents);
  }

  public final Html.Instruction.OfElement small(String text) {
    return elem0(HtmlElementName.SMALL, text);
  }

  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return elem0(HtmlElementName.SPAN, contents);
  }

  public final Html.Instruction.OfElement span(String text) {
    return elem0(HtmlElementName.SPAN, text);
  }

  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return elem0(HtmlElementName.STRONG, contents);
  }

  public final Html.Instruction.OfElement strong(String text) {
    return elem0(HtmlElementName.STRONG, text);
  }

  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return elem0(HtmlElementName.STYLE, contents);
  }

  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUB, contents);
  }

  public final Html.Instruction.OfElement sub(String text) {
    return elem0(HtmlElementName.SUB, text);
  }

  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUMMARY, contents);
  }

  public final Html.Instruction.OfElement summary(String text) {
    return elem0(HtmlElementName.SUMMARY, text);
  }

  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUP, contents);
  }

  public final Html.Instruction.OfElement sup(String text) {
    return elem0(HtmlElementName.SUP, text);
  }

  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return elem0(HtmlElementName.SVG, contents);
  }

  public final Html.Instruction.OfElement svg(String text) {
    return elem0(HtmlElementName.SVG, text);
  }

  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return elem0(HtmlElementName.TABLE, contents);
  }

  public final Html.Instruction.OfElement table(String text) {
    return elem0(HtmlElementName.TABLE, text);
  }

  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return elem0(HtmlElementName.TBODY, contents);
  }

  public final Html.Instruction.OfElement tbody(String text) {
    return elem0(HtmlElementName.TBODY, text);
  }

  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return elem0(HtmlElementName.TD, contents);
  }

  public final Html.Instruction.OfElement td(String text) {
    return elem0(HtmlElementName.TD, text);
  }

  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return elem0(HtmlElementName.TEMPLATE, contents);
  }

  public final Html.Instruction.OfElement template(String text) {
    return elem0(HtmlElementName.TEMPLATE, text);
  }

  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return elem0(HtmlElementName.TEXTAREA, contents);
  }

  public final Html.Instruction.OfElement textarea(String text) {
    return elem0(HtmlElementName.TEXTAREA, text);
  }

  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return elem0(HtmlElementName.TH, contents);
  }

  public final Html.Instruction.OfElement th(String text) {
    return elem0(HtmlElementName.TH, text);
  }

  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return elem0(HtmlElementName.THEAD, contents);
  }

  public final Html.Instruction.OfElement thead(String text) {
    return elem0(HtmlElementName.THEAD, text);
  }

  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return elem0(HtmlElementName.TITLE, contents);
  }

  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return elem0(HtmlElementName.TR, contents);
  }

  public final Html.Instruction.OfElement tr(String text) {
    return elem0(HtmlElementName.TR, text);
  }

  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return elem0(HtmlElementName.UL, contents);
  }

  public final Html.Instruction.OfElement ul(String text) {
    return elem0(HtmlElementName.UL, text);
  }

  //
  // ATTRIBUTES
  //

  abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name);

  abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name, Object value);

  public final Html.Instruction.OfAttribute attr(Html.AttributeName name) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name);
  }

  public final Html.Instruction.OfAttribute attr(Html.AttributeName name, String value) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name, value);
  }

  public final Html.Instruction.OfAttribute accesskey(String value) {
    return attr0(HtmlAttributeName.ACCESSKEY, value);
  }

  public final Html.Instruction.OfAttribute action(String value) {
    return attr0(HtmlAttributeName.ACTION, value);
  }

  public final Html.Instruction.OfAttribute align(String value) {
    return attr0(HtmlAttributeName.ALIGN, value);
  }

  public final Html.Instruction.OfAttribute alignmentBaseline(String value) {
    return attr0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  public final Html.Instruction.OfAttribute alt(String value) {
    return attr0(HtmlAttributeName.ALT, value);
  }

  public final Html.Instruction.OfAttribute ariaHidden(String value) {
    return attr0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  public final Html.Instruction.OfAttribute ariaLabel(String value) {
    return attr0(HtmlAttributeName.ARIA_LABEL, value);
  }

  public final Html.Instruction.OfAttribute as(String value) {
    return attr0(HtmlAttributeName.AS, value);
  }

  public final Html.Instruction.OfAttribute autocomplete(String value) {
    return attr0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  public final Html.Instruction.OfAttribute baselineShift(String value) {
    return attr0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  public final Html.Instruction.OfAttribute border(String value) {
    return attr0(HtmlAttributeName.BORDER, value);
  }

  public final Html.Instruction.OfAttribute cellpadding(String value) {
    return attr0(HtmlAttributeName.CELLPADDING, value);
  }

  public final Html.Instruction.OfAttribute cellspacing(String value) {
    return attr0(HtmlAttributeName.CELLSPACING, value);
  }

  public final Html.Instruction.OfAttribute charset(String value) {
    return attr0(HtmlAttributeName.CHARSET, value);
  }

  public final Html.Instruction.OfAttribute cite(String value) {
    return attr0(HtmlAttributeName.CITE, value);
  }

  public final Html.Instruction.OfAttribute className(String value) {
    return attr0(HtmlAttributeName.CLASS, value);
  }

  public final Html.Instruction.OfAttribute clipRule(String value) {
    return attr0(HtmlAttributeName.CLIP_RULE, value);
  }

  public final Html.Instruction.OfAttribute closedby(String value) {
    return attr0(HtmlAttributeName.CLOSEDBY, value);
  }

  public final Html.Instruction.OfAttribute color(String value) {
    return attr0(HtmlAttributeName.COLOR, value);
  }

  public final Html.Instruction.OfAttribute colorInterpolation(String value) {
    return attr0(HtmlAttributeName.COLOR_INTERPOLATION, value);
  }

  public final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
    return attr0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  public final Html.Instruction.OfAttribute cols(String value) {
    return attr0(HtmlAttributeName.COLS, value);
  }

  public final Html.Instruction.OfAttribute content(String value) {
    return attr0(HtmlAttributeName.CONTENT, value);
  }

  public final Html.Instruction.OfAttribute contenteditable(String value) {
    return attr0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  public final Html.Instruction.OfAttribute crossorigin(String value) {
    return attr0(HtmlAttributeName.CROSSORIGIN, value);
  }

  public final Html.Instruction.OfAttribute cursor(String value) {
    return attr0(HtmlAttributeName.CURSOR, value);
  }

  public final Html.Instruction.OfAttribute d(String value) {
    return attr0(HtmlAttributeName.D, value);
  }

  public final Html.Instruction.OfAttribute dir(String value) {
    return attr0(HtmlAttributeName.DIR, value);
  }

  public final Html.Instruction.OfAttribute direction(String value) {
    return attr0(HtmlAttributeName.DIRECTION, value);
  }

  public final Html.Instruction.OfAttribute dirname(String value) {
    return attr0(HtmlAttributeName.DIRNAME, value);
  }

  public final Html.Instruction.OfAttribute display(String value) {
    return attr0(HtmlAttributeName.DISPLAY, value);
  }

  public final Html.Instruction.OfAttribute dominantBaseline(String value) {
    return attr0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  public final Html.Instruction.OfAttribute draggable(String value) {
    return attr0(HtmlAttributeName.DRAGGABLE, value);
  }

  public final Html.Instruction.OfAttribute enctype(String value) {
    return attr0(HtmlAttributeName.ENCTYPE, value);
  }

  public final Html.Instruction.OfAttribute fill(String value) {
    return attr0(HtmlAttributeName.FILL, value);
  }

  public final Html.Instruction.OfAttribute fillOpacity(String value) {
    return attr0(HtmlAttributeName.FILL_OPACITY, value);
  }

  public final Html.Instruction.OfAttribute fillRule(String value) {
    return attr0(HtmlAttributeName.FILL_RULE, value);
  }

  public final Html.Instruction.OfAttribute filter(String value) {
    return attr0(HtmlAttributeName.FILTER, value);
  }

  public final Html.Instruction.OfAttribute floodColor(String value) {
    return attr0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  public final Html.Instruction.OfAttribute floodOpacity(String value) {
    return attr0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  public final Html.Instruction.OfAttribute forId(String value) {
    return attr0(HtmlAttributeName.FOR, value);
  }

  public final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
    return attr0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  public final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
    return attr0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  public final Html.Instruction.OfAttribute height(String value) {
    return attr0(HtmlAttributeName.HEIGHT, value);
  }

  public final Html.Instruction.OfAttribute href(String value) {
    return attr0(HtmlAttributeName.HREF, value);
  }

  public final Html.Instruction.OfAttribute httpEquiv(String value) {
    return attr0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  public final Html.Instruction.OfAttribute id(String value) {
    return attr0(HtmlAttributeName.ID, value);
  }

  public final Html.Instruction.OfAttribute imageRendering(String value) {
    return attr0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  public final Html.Instruction.OfAttribute integrity(String value) {
    return attr0(HtmlAttributeName.INTEGRITY, value);
  }

  public final Html.Instruction.OfAttribute lang(String value) {
    return attr0(HtmlAttributeName.LANG, value);
  }

  public final Html.Instruction.OfAttribute letterSpacing(String value) {
    return attr0(HtmlAttributeName.LETTER_SPACING, value);
  }

  public final Html.Instruction.OfAttribute lightingColor(String value) {
    return attr0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  public final Html.Instruction.OfAttribute markerEnd(String value) {
    return attr0(HtmlAttributeName.MARKER_END, value);
  }

  public final Html.Instruction.OfAttribute markerMid(String value) {
    return attr0(HtmlAttributeName.MARKER_MID, value);
  }

  public final Html.Instruction.OfAttribute markerStart(String value) {
    return attr0(HtmlAttributeName.MARKER_START, value);
  }

  public final Html.Instruction.OfAttribute mask(String value) {
    return attr0(HtmlAttributeName.MASK, value);
  }

  public final Html.Instruction.OfAttribute maskType(String value) {
    return attr0(HtmlAttributeName.MASK_TYPE, value);
  }

  public final Html.Instruction.OfAttribute maxlength(String value) {
    return attr0(HtmlAttributeName.MAXLENGTH, value);
  }

  public final Html.Instruction.OfAttribute media(String value) {
    return attr0(HtmlAttributeName.MEDIA, value);
  }

  public final Html.Instruction.OfAttribute method(String value) {
    return attr0(HtmlAttributeName.METHOD, value);
  }

  public final Html.Instruction.OfAttribute minlength(String value) {
    return attr0(HtmlAttributeName.MINLENGTH, value);
  }

  public final Html.Instruction.OfAttribute name(String value) {
    return attr0(HtmlAttributeName.NAME, value);
  }

  public final Html.Instruction.OfAttribute onafterprint(String value) {
    return attr0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  public final Html.Instruction.OfAttribute onbeforeprint(String value) {
    return attr0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  public final Html.Instruction.OfAttribute onbeforeunload(String value) {
    return attr0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  public final Html.Instruction.OfAttribute onclick(String value) {
    return attr0(HtmlAttributeName.ONCLICK, value);
  }

  public final Html.Instruction.OfAttribute onhashchange(String value) {
    return attr0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  public final Html.Instruction.OfAttribute onlanguagechange(String value) {
    return attr0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  public final Html.Instruction.OfAttribute onmessage(String value) {
    return attr0(HtmlAttributeName.ONMESSAGE, value);
  }

  public final Html.Instruction.OfAttribute onoffline(String value) {
    return attr0(HtmlAttributeName.ONOFFLINE, value);
  }

  public final Html.Instruction.OfAttribute ononline(String value) {
    return attr0(HtmlAttributeName.ONONLINE, value);
  }

  public final Html.Instruction.OfAttribute onpagehide(String value) {
    return attr0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  public final Html.Instruction.OfAttribute onpageshow(String value) {
    return attr0(HtmlAttributeName.ONPAGESHOW, value);
  }

  public final Html.Instruction.OfAttribute onpopstate(String value) {
    return attr0(HtmlAttributeName.ONPOPSTATE, value);
  }

  public final Html.Instruction.OfAttribute onrejectionhandled(String value) {
    return attr0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  public final Html.Instruction.OfAttribute onstorage(String value) {
    return attr0(HtmlAttributeName.ONSTORAGE, value);
  }

  public final Html.Instruction.OfAttribute onsubmit(String value) {
    return attr0(HtmlAttributeName.ONSUBMIT, value);
  }

  public final Html.Instruction.OfAttribute onunhandledrejection(String value) {
    return attr0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  public final Html.Instruction.OfAttribute onunload(String value) {
    return attr0(HtmlAttributeName.ONUNLOAD, value);
  }

  public final Html.Instruction.OfAttribute opacity(String value) {
    return attr0(HtmlAttributeName.OPACITY, value);
  }

  public final Html.Instruction.OfAttribute overflow(String value) {
    return attr0(HtmlAttributeName.OVERFLOW, value);
  }

  public final Html.Instruction.OfAttribute paintOrder(String value) {
    return attr0(HtmlAttributeName.PAINT_ORDER, value);
  }

  public final Html.Instruction.OfAttribute placeholder(String value) {
    return attr0(HtmlAttributeName.PLACEHOLDER, value);
  }

  public final Html.Instruction.OfAttribute pointerEvents(String value) {
    return attr0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  public final Html.Instruction.OfAttribute property(String value) {
    return attr0(HtmlAttributeName.PROPERTY, value);
  }

  public final Html.Instruction.OfAttribute referrerpolicy(String value) {
    return attr0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  public final Html.Instruction.OfAttribute rel(String value) {
    return attr0(HtmlAttributeName.REL, value);
  }

  public final Html.Instruction.OfAttribute rev(String value) {
    return attr0(HtmlAttributeName.REV, value);
  }

  public final Html.Instruction.OfAttribute role(String value) {
    return attr0(HtmlAttributeName.ROLE, value);
  }

  public final Html.Instruction.OfAttribute rows(String value) {
    return attr0(HtmlAttributeName.ROWS, value);
  }

  public final Html.Instruction.OfAttribute shapeRendering(String value) {
    return attr0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  public final Html.Instruction.OfAttribute size(String value) {
    return attr0(HtmlAttributeName.SIZE, value);
  }

  public final Html.Instruction.OfAttribute sizes(String value) {
    return attr0(HtmlAttributeName.SIZES, value);
  }

  public final Html.Instruction.OfAttribute spellcheck(String value) {
    return attr0(HtmlAttributeName.SPELLCHECK, value);
  }

  public final Html.Instruction.OfAttribute src(String value) {
    return attr0(HtmlAttributeName.SRC, value);
  }

  public final Html.Instruction.OfAttribute srcset(String value) {
    return attr0(HtmlAttributeName.SRCSET, value);
  }

  public final Html.Instruction.OfAttribute start(String value) {
    return attr0(HtmlAttributeName.START, value);
  }

  public final Html.Instruction.OfAttribute stopColor(String value) {
    return attr0(HtmlAttributeName.STOP_COLOR, value);
  }

  public final Html.Instruction.OfAttribute stopOpacity(String value) {
    return attr0(HtmlAttributeName.STOP_OPACITY, value);
  }

  public final Html.Instruction.OfAttribute stroke(String value) {
    return attr0(HtmlAttributeName.STROKE, value);
  }

  public final Html.Instruction.OfAttribute strokeDasharray(String value) {
    return attr0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  public final Html.Instruction.OfAttribute strokeDashoffset(String value) {
    return attr0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  public final Html.Instruction.OfAttribute strokeLinecap(String value) {
    return attr0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  public final Html.Instruction.OfAttribute strokeLinejoin(String value) {
    return attr0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  public final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
    return attr0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  public final Html.Instruction.OfAttribute strokeOpacity(String value) {
    return attr0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  public final Html.Instruction.OfAttribute strokeWidth(String value) {
    return attr0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  public final Html.Instruction.OfAttribute tabindex(String value) {
    return attr0(HtmlAttributeName.TABINDEX, value);
  }

  public final Html.Instruction.OfAttribute target(String value) {
    return attr0(HtmlAttributeName.TARGET, value);
  }

  public final Html.Instruction.OfAttribute textAnchor(String value) {
    return attr0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  public final Html.Instruction.OfAttribute textDecoration(String value) {
    return attr0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  public final Html.Instruction.OfAttribute textOverflow(String value) {
    return attr0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  public final Html.Instruction.OfAttribute textRendering(String value) {
    return attr0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  public final Html.Instruction.OfAttribute transform(String value) {
    return attr0(HtmlAttributeName.TRANSFORM, value);
  }

  public final Html.Instruction.OfAttribute transformOrigin(String value) {
    return attr0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  public final Html.Instruction.OfAttribute translate(String value) {
    return attr0(HtmlAttributeName.TRANSLATE, value);
  }

  public final Html.Instruction.OfAttribute type(String value) {
    return attr0(HtmlAttributeName.TYPE, value);
  }

  public final Html.Instruction.OfAttribute unicodeBidi(String value) {
    return attr0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  public final Html.Instruction.OfAttribute value(String value) {
    return attr0(HtmlAttributeName.VALUE, value);
  }

  public final Html.Instruction.OfAttribute vectorEffect(String value) {
    return attr0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  public final Html.Instruction.OfAttribute viewBox(String value) {
    return attr0(HtmlAttributeName.VIEWBOX, value);
  }

  public final Html.Instruction.OfAttribute visibility(String value) {
    return attr0(HtmlAttributeName.VISIBILITY, value);
  }

  public final Html.Instruction.OfAttribute whiteSpace(String value) {
    return attr0(HtmlAttributeName.WHITE_SPACE, value);
  }

  public final Html.Instruction.OfAttribute width(String value) {
    return attr0(HtmlAttributeName.WIDTH, value);
  }

  public final Html.Instruction.OfAttribute wordSpacing(String value) {
    return attr0(HtmlAttributeName.WORD_SPACING, value);
  }

  public final Html.Instruction.OfAttribute wrap(String value) {
    return attr0(HtmlAttributeName.WRAP, value);
  }

  public final Html.Instruction.OfAttribute writingMode(String value) {
    return attr0(HtmlAttributeName.WRITING_MODE, value);
  }

  public final Html.Instruction.OfAttribute xmlns(String value) {
    return attr0(HtmlAttributeName.XMLNS, value);
  }

  //
  // AMBIGUOUS
  //

  abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String text);

  public final Html.Instruction.OfAmbiguous clipPath(String text) {
    return ambiguous(HtmlAmbiguous.CLIPPATH, text);
  }

  public final Html.Instruction.OfAmbiguous form(String text) {
    return ambiguous(HtmlAmbiguous.FORM, text);
  }

  public final Html.Instruction.OfAmbiguous label(String text) {
    return ambiguous(HtmlAmbiguous.LABEL, text);
  }

  public final Html.Instruction.OfAmbiguous style(String text) {
    return ambiguous(HtmlAmbiguous.STYLE, text);
  }

  public final Html.Instruction.OfAmbiguous title(String text) {
    return ambiguous(HtmlAmbiguous.TITLE, text);
  }

}
