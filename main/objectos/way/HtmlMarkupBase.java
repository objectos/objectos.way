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

abstract class HtmlMarkupBase implements Html.Markup {

  //
  // ELEMENTS
  //

  abstract Html.Instruction.OfElement elem0(Html.ElementName name, Html.Instruction... contents);

  abstract Html.Instruction.OfElement elem0(Html.ElementName name, String text);

  @Override
  public final Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, contents);
  }

  @Override
  public final Html.Instruction.OfElement elem(Html.ElementName name, String text) {
    Objects.requireNonNull(name, "name == null");
    return elem0(name, text);
  }

  @Override
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return elem0(HtmlElementName.A, contents);
  }

  @Override
  public final Html.Instruction.OfElement a(String text) {
    return elem0(HtmlElementName.A, text);
  }

  @Override
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return elem0(HtmlElementName.ABBR, contents);
  }

  @Override
  public final Html.Instruction.OfElement abbr(String text) {
    return elem0(HtmlElementName.ABBR, text);
  }

  @Override
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return elem0(HtmlElementName.ARTICLE, contents);
  }

  @Override
  public final Html.Instruction.OfElement article(String text) {
    return elem0(HtmlElementName.ARTICLE, text);
  }

  @Override
  public final Html.Instruction.OfElement aside(Html.Instruction... contents) {
    return elem0(HtmlElementName.ASIDE, contents);
  }

  @Override
  public final Html.Instruction.OfElement aside(String text) {
    return elem0(HtmlElementName.ASIDE, text);
  }

  @Override
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return elem0(HtmlElementName.B, contents);
  }

  @Override
  public final Html.Instruction.OfElement b(String text) {
    return elem0(HtmlElementName.B, text);
  }

  @Override
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return elem0(HtmlElementName.BLOCKQUOTE, contents);
  }

  @Override
  public final Html.Instruction.OfElement blockquote(String text) {
    return elem0(HtmlElementName.BLOCKQUOTE, text);
  }

  @Override
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return elem0(HtmlElementName.BODY, contents);
  }

  @Override
  public final Html.Instruction.OfElement body(String text) {
    return elem0(HtmlElementName.BODY, text);
  }

  @Override
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.BR, contents);
  }

  @Override
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return elem0(HtmlElementName.BUTTON, contents);
  }

  @Override
  public final Html.Instruction.OfElement button(String text) {
    return elem0(HtmlElementName.BUTTON, text);
  }

  @Override
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return elem0(HtmlElementName.CLIPPATH, contents);
  }

  @Override
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return elem0(HtmlElementName.CODE, contents);
  }

  @Override
  public final Html.Instruction.OfElement code(String text) {
    return elem0(HtmlElementName.CODE, text);
  }

  @Override
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return elem0(HtmlElementName.DD, contents);
  }

  @Override
  public final Html.Instruction.OfElement dd(String text) {
    return elem0(HtmlElementName.DD, text);
  }

  @Override
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return elem0(HtmlElementName.DEFS, contents);
  }

  @Override
  public final Html.Instruction.OfElement defs(String text) {
    return elem0(HtmlElementName.DEFS, text);
  }

  @Override
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return elem0(HtmlElementName.DETAILS, contents);
  }

  @Override
  public final Html.Instruction.OfElement details(String text) {
    return elem0(HtmlElementName.DETAILS, text);
  }

  @Override
  public final Html.Instruction.OfElement dialog(Html.Instruction... contents) {
    return elem0(HtmlElementName.DIALOG, contents);
  }

  @Override
  public final Html.Instruction.OfElement dialog(String text) {
    return elem0(HtmlElementName.DIALOG, text);
  }

  @Override
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return elem0(HtmlElementName.DIV, contents);
  }

  @Override
  public final Html.Instruction.OfElement div(String text) {
    return elem0(HtmlElementName.DIV, text);
  }

  @Override
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return elem0(HtmlElementName.DL, contents);
  }

  @Override
  public final Html.Instruction.OfElement dl(String text) {
    return elem0(HtmlElementName.DL, text);
  }

  @Override
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return elem0(HtmlElementName.DT, contents);
  }

  @Override
  public final Html.Instruction.OfElement dt(String text) {
    return elem0(HtmlElementName.DT, text);
  }

  @Override
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return elem0(HtmlElementName.EM, contents);
  }

  @Override
  public final Html.Instruction.OfElement em(String text) {
    return elem0(HtmlElementName.EM, text);
  }

  @Override
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return elem0(HtmlElementName.FIELDSET, contents);
  }

  @Override
  public final Html.Instruction.OfElement fieldset(String text) {
    return elem0(HtmlElementName.FIELDSET, text);
  }

  @Override
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return elem0(HtmlElementName.FIGURE, contents);
  }

  @Override
  public final Html.Instruction.OfElement figure(String text) {
    return elem0(HtmlElementName.FIGURE, text);
  }

  @Override
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return elem0(HtmlElementName.FOOTER, contents);
  }

  @Override
  public final Html.Instruction.OfElement footer(String text) {
    return elem0(HtmlElementName.FOOTER, text);
  }

  @Override
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return elem0(HtmlElementName.FORM, contents);
  }

  @Override
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return elem0(HtmlElementName.G, contents);
  }

  @Override
  public final Html.Instruction.OfElement g(String text) {
    return elem0(HtmlElementName.G, text);
  }

  @Override
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return elem0(HtmlElementName.H1, contents);
  }

  @Override
  public final Html.Instruction.OfElement h1(String text) {
    return elem0(HtmlElementName.H1, text);
  }

  @Override
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return elem0(HtmlElementName.H2, contents);
  }

  @Override
  public final Html.Instruction.OfElement h2(String text) {
    return elem0(HtmlElementName.H2, text);
  }

  @Override
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return elem0(HtmlElementName.H3, contents);
  }

  @Override
  public final Html.Instruction.OfElement h3(String text) {
    return elem0(HtmlElementName.H3, text);
  }

  @Override
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return elem0(HtmlElementName.H4, contents);
  }

  @Override
  public final Html.Instruction.OfElement h4(String text) {
    return elem0(HtmlElementName.H4, text);
  }

  @Override
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return elem0(HtmlElementName.H5, contents);
  }

  @Override
  public final Html.Instruction.OfElement h5(String text) {
    return elem0(HtmlElementName.H5, text);
  }

  @Override
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return elem0(HtmlElementName.H6, contents);
  }

  @Override
  public final Html.Instruction.OfElement h6(String text) {
    return elem0(HtmlElementName.H6, text);
  }

  @Override
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return elem0(HtmlElementName.HEAD, contents);
  }

  @Override
  public final Html.Instruction.OfElement head(String text) {
    return elem0(HtmlElementName.HEAD, text);
  }

  @Override
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return elem0(HtmlElementName.HEADER, contents);
  }

  @Override
  public final Html.Instruction.OfElement header(String text) {
    return elem0(HtmlElementName.HEADER, text);
  }

  @Override
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return elem0(HtmlElementName.HGROUP, contents);
  }

  @Override
  public final Html.Instruction.OfElement hgroup(String text) {
    return elem0(HtmlElementName.HGROUP, text);
  }

  @Override
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.HR, contents);
  }

  @Override
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return elem0(HtmlElementName.HTML, contents);
  }

  @Override
  public final Html.Instruction.OfElement html(String text) {
    return elem0(HtmlElementName.HTML, text);
  }

  @Override
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.IMG, contents);
  }

  @Override
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.INPUT, contents);
  }

  @Override
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return elem0(HtmlElementName.KBD, contents);
  }

  @Override
  public final Html.Instruction.OfElement kbd(String text) {
    return elem0(HtmlElementName.KBD, text);
  }

  @Override
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return elem0(HtmlElementName.LABEL, contents);
  }

  @Override
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return elem0(HtmlElementName.LEGEND, contents);
  }

  @Override
  public final Html.Instruction.OfElement legend(String text) {
    return elem0(HtmlElementName.LEGEND, text);
  }

  @Override
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return elem0(HtmlElementName.LI, contents);
  }

  @Override
  public final Html.Instruction.OfElement li(String text) {
    return elem0(HtmlElementName.LI, text);
  }

  @Override
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.LINK, contents);
  }

  @Override
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return elem0(HtmlElementName.MAIN, contents);
  }

  @Override
  public final Html.Instruction.OfElement main(String text) {
    return elem0(HtmlElementName.MAIN, text);
  }

  @Override
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return elem0(HtmlElementName.MENU, contents);
  }

  @Override
  public final Html.Instruction.OfElement menu(String text) {
    return elem0(HtmlElementName.MENU, text);
  }

  @Override
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return elem0(HtmlElementName.META, contents);
  }

  @Override
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return elem0(HtmlElementName.NAV, contents);
  }

  @Override
  public final Html.Instruction.OfElement nav(String text) {
    return elem0(HtmlElementName.NAV, text);
  }

  @Override
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return elem0(HtmlElementName.OL, contents);
  }

  @Override
  public final Html.Instruction.OfElement ol(String text) {
    return elem0(HtmlElementName.OL, text);
  }

  @Override
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return elem0(HtmlElementName.OPTGROUP, contents);
  }

  @Override
  public final Html.Instruction.OfElement optgroup(String text) {
    return elem0(HtmlElementName.OPTGROUP, text);
  }

  @Override
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return elem0(HtmlElementName.OPTION, contents);
  }

  @Override
  public final Html.Instruction.OfElement option(String text) {
    return elem0(HtmlElementName.OPTION, text);
  }

  @Override
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return elem0(HtmlElementName.P, contents);
  }

  @Override
  public final Html.Instruction.OfElement p(String text) {
    return elem0(HtmlElementName.P, text);
  }

  @Override
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return elem0(HtmlElementName.PATH, contents);
  }

  @Override
  public final Html.Instruction.OfElement path(String text) {
    return elem0(HtmlElementName.PATH, text);
  }

  @Override
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return elem0(HtmlElementName.PRE, contents);
  }

  @Override
  public final Html.Instruction.OfElement pre(String text) {
    return elem0(HtmlElementName.PRE, text);
  }

  @Override
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return elem0(HtmlElementName.PROGRESS, contents);
  }

  @Override
  public final Html.Instruction.OfElement progress(String text) {
    return elem0(HtmlElementName.PROGRESS, text);
  }

  @Override
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return elem0(HtmlElementName.SAMP, contents);
  }

  @Override
  public final Html.Instruction.OfElement samp(String text) {
    return elem0(HtmlElementName.SAMP, text);
  }

  @Override
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return elem0(HtmlElementName.SCRIPT, contents);
  }

  @Override
  public final Html.Instruction.OfElement script(String text) {
    return elem0(HtmlElementName.SCRIPT, text);
  }

  @Override
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return elem0(HtmlElementName.SECTION, contents);
  }

  @Override
  public final Html.Instruction.OfElement section(String text) {
    return elem0(HtmlElementName.SECTION, text);
  }

  @Override
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return elem0(HtmlElementName.SELECT, contents);
  }

  @Override
  public final Html.Instruction.OfElement select(String text) {
    return elem0(HtmlElementName.SELECT, text);
  }

  @Override
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return elem0(HtmlElementName.SMALL, contents);
  }

  @Override
  public final Html.Instruction.OfElement small(String text) {
    return elem0(HtmlElementName.SMALL, text);
  }

  @Override
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return elem0(HtmlElementName.SPAN, contents);
  }

  @Override
  public final Html.Instruction.OfElement span(String text) {
    return elem0(HtmlElementName.SPAN, text);
  }

  @Override
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return elem0(HtmlElementName.STRONG, contents);
  }

  @Override
  public final Html.Instruction.OfElement strong(String text) {
    return elem0(HtmlElementName.STRONG, text);
  }

  @Override
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return elem0(HtmlElementName.STYLE, contents);
  }

  @Override
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUB, contents);
  }

  @Override
  public final Html.Instruction.OfElement sub(String text) {
    return elem0(HtmlElementName.SUB, text);
  }

  @Override
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUMMARY, contents);
  }

  @Override
  public final Html.Instruction.OfElement summary(String text) {
    return elem0(HtmlElementName.SUMMARY, text);
  }

  @Override
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return elem0(HtmlElementName.SUP, contents);
  }

  @Override
  public final Html.Instruction.OfElement sup(String text) {
    return elem0(HtmlElementName.SUP, text);
  }

  @Override
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return elem0(HtmlElementName.SVG, contents);
  }

  @Override
  public final Html.Instruction.OfElement svg(String text) {
    return elem0(HtmlElementName.SVG, text);
  }

  @Override
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return elem0(HtmlElementName.TABLE, contents);
  }

  @Override
  public final Html.Instruction.OfElement table(String text) {
    return elem0(HtmlElementName.TABLE, text);
  }

  @Override
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return elem0(HtmlElementName.TBODY, contents);
  }

  @Override
  public final Html.Instruction.OfElement tbody(String text) {
    return elem0(HtmlElementName.TBODY, text);
  }

  @Override
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return elem0(HtmlElementName.TD, contents);
  }

  @Override
  public final Html.Instruction.OfElement td(String text) {
    return elem0(HtmlElementName.TD, text);
  }

  @Override
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return elem0(HtmlElementName.TEMPLATE, contents);
  }

  @Override
  public final Html.Instruction.OfElement template(String text) {
    return elem0(HtmlElementName.TEMPLATE, text);
  }

  @Override
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return elem0(HtmlElementName.TEXTAREA, contents);
  }

  @Override
  public final Html.Instruction.OfElement textarea(String text) {
    return elem0(HtmlElementName.TEXTAREA, text);
  }

  @Override
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return elem0(HtmlElementName.TH, contents);
  }

  @Override
  public final Html.Instruction.OfElement th(String text) {
    return elem0(HtmlElementName.TH, text);
  }

  @Override
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return elem0(HtmlElementName.THEAD, contents);
  }

  @Override
  public final Html.Instruction.OfElement thead(String text) {
    return elem0(HtmlElementName.THEAD, text);
  }

  @Override
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return elem0(HtmlElementName.TITLE, contents);
  }

  @Override
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return elem0(HtmlElementName.TR, contents);
  }

  @Override
  public final Html.Instruction.OfElement tr(String text) {
    return elem0(HtmlElementName.TR, text);
  }

  @Override
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return elem0(HtmlElementName.UL, contents);
  }

  @Override
  public final Html.Instruction.OfElement ul(String text) {
    return elem0(HtmlElementName.UL, text);
  }

  //
  // ATTRIBUTES
  //

  abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name);

  abstract Html.Instruction.OfAttribute attr0(Html.AttributeName name, Object value);

  @Override
  public final Html.Instruction.OfAttribute attr(Html.AttributeName name) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name);
  }

  @Override
  public final Html.Instruction.OfAttribute attr(Html.AttributeName name, String value) {
    Objects.requireNonNull(name, "name == null");
    return attr0(name, value);
  }

  @Override
  public final Html.Instruction.OfAttribute accesskey(String value) {
    return attr0(HtmlAttributeName.ACCESSKEY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute action(String value) {
    return attr0(HtmlAttributeName.ACTION, value);
  }

  @Override
  public final Html.Instruction.OfAttribute align(String value) {
    return attr0(HtmlAttributeName.ALIGN, value);
  }

  @Override
  public final Html.Instruction.OfAttribute alignmentBaseline(String value) {
    return attr0(HtmlAttributeName.ALIGNMENT_BASELINE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute alt(String value) {
    return attr0(HtmlAttributeName.ALT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute ariaHidden(String value) {
    return attr0(HtmlAttributeName.ARIA_HIDDEN, value);
  }

  @Override
  public final Html.Instruction.OfAttribute ariaLabel(String value) {
    return attr0(HtmlAttributeName.ARIA_LABEL, value);
  }

  @Override
  public final Html.Instruction.OfAttribute as(String value) {
    return attr0(HtmlAttributeName.AS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute autocomplete(String value) {
    return attr0(HtmlAttributeName.AUTOCOMPLETE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute baselineShift(String value) {
    return attr0(HtmlAttributeName.BASELINE_SHIFT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute border(String value) {
    return attr0(HtmlAttributeName.BORDER, value);
  }

  @Override
  public final Html.Instruction.OfAttribute cellpadding(String value) {
    return attr0(HtmlAttributeName.CELLPADDING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute cellspacing(String value) {
    return attr0(HtmlAttributeName.CELLSPACING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute charset(String value) {
    return attr0(HtmlAttributeName.CHARSET, value);
  }

  @Override
  public final Html.Instruction.OfAttribute cite(String value) {
    return attr0(HtmlAttributeName.CITE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute className(String value) {
    return attr0(HtmlAttributeName.CLASS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute clipRule(String value) {
    return attr0(HtmlAttributeName.CLIP_RULE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute closedby(String value) {
    return attr0(HtmlAttributeName.CLOSEDBY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute color(String value) {
    return attr0(HtmlAttributeName.COLOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute colorInterpolation(String value) {
    return attr0(HtmlAttributeName.COLOR_INTERPOLATION, value);
  }

  @Override
  public final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
    return attr0(HtmlAttributeName.COLOR_INTERPOLATION_FILTERS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute cols(String value) {
    return attr0(HtmlAttributeName.COLS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute content(String value) {
    return attr0(HtmlAttributeName.CONTENT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute contenteditable(String value) {
    return attr0(HtmlAttributeName.CONTENTEDITABLE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute crossorigin(String value) {
    return attr0(HtmlAttributeName.CROSSORIGIN, value);
  }

  @Override
  public final Html.Instruction.OfAttribute cursor(String value) {
    return attr0(HtmlAttributeName.CURSOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute d(String value) {
    return attr0(HtmlAttributeName.D, value);
  }

  @Override
  public final Html.Instruction.OfAttribute dir(String value) {
    return attr0(HtmlAttributeName.DIR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute direction(String value) {
    return attr0(HtmlAttributeName.DIRECTION, value);
  }

  @Override
  public final Html.Instruction.OfAttribute dirname(String value) {
    return attr0(HtmlAttributeName.DIRNAME, value);
  }

  @Override
  public final Html.Instruction.OfAttribute display(String value) {
    return attr0(HtmlAttributeName.DISPLAY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute dominantBaseline(String value) {
    return attr0(HtmlAttributeName.DOMINANT_BASELINE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute draggable(String value) {
    return attr0(HtmlAttributeName.DRAGGABLE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute enctype(String value) {
    return attr0(HtmlAttributeName.ENCTYPE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute fill(String value) {
    return attr0(HtmlAttributeName.FILL, value);
  }

  @Override
  public final Html.Instruction.OfAttribute fillOpacity(String value) {
    return attr0(HtmlAttributeName.FILL_OPACITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute fillRule(String value) {
    return attr0(HtmlAttributeName.FILL_RULE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute filter(String value) {
    return attr0(HtmlAttributeName.FILTER, value);
  }

  @Override
  public final Html.Instruction.OfAttribute floodColor(String value) {
    return attr0(HtmlAttributeName.FLOOD_COLOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute floodOpacity(String value) {
    return attr0(HtmlAttributeName.FLOOD_OPACITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute forId(String value) {
    return attr0(HtmlAttributeName.FOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
    return attr0(HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL, value);
  }

  @Override
  public final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
    return attr0(HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL, value);
  }

  @Override
  public final Html.Instruction.OfAttribute height(String value) {
    return attr0(HtmlAttributeName.HEIGHT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute href(String value) {
    return attr0(HtmlAttributeName.HREF, value);
  }

  @Override
  public final Html.Instruction.OfAttribute httpEquiv(String value) {
    return attr0(HtmlAttributeName.HTTP_EQUIV, value);
  }

  @Override
  public final Html.Instruction.OfAttribute id(String value) {
    return attr0(HtmlAttributeName.ID, value);
  }

  @Override
  public final Html.Instruction.OfAttribute imageRendering(String value) {
    return attr0(HtmlAttributeName.IMAGE_RENDERING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute integrity(String value) {
    return attr0(HtmlAttributeName.INTEGRITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute lang(String value) {
    return attr0(HtmlAttributeName.LANG, value);
  }

  @Override
  public final Html.Instruction.OfAttribute letterSpacing(String value) {
    return attr0(HtmlAttributeName.LETTER_SPACING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute lightingColor(String value) {
    return attr0(HtmlAttributeName.LIGHTING_COLOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute markerEnd(String value) {
    return attr0(HtmlAttributeName.MARKER_END, value);
  }

  @Override
  public final Html.Instruction.OfAttribute markerMid(String value) {
    return attr0(HtmlAttributeName.MARKER_MID, value);
  }

  @Override
  public final Html.Instruction.OfAttribute markerStart(String value) {
    return attr0(HtmlAttributeName.MARKER_START, value);
  }

  @Override
  public final Html.Instruction.OfAttribute mask(String value) {
    return attr0(HtmlAttributeName.MASK, value);
  }

  @Override
  public final Html.Instruction.OfAttribute maskType(String value) {
    return attr0(HtmlAttributeName.MASK_TYPE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute maxlength(String value) {
    return attr0(HtmlAttributeName.MAXLENGTH, value);
  }

  @Override
  public final Html.Instruction.OfAttribute media(String value) {
    return attr0(HtmlAttributeName.MEDIA, value);
  }

  @Override
  public final Html.Instruction.OfAttribute method(String value) {
    return attr0(HtmlAttributeName.METHOD, value);
  }

  @Override
  public final Html.Instruction.OfAttribute minlength(String value) {
    return attr0(HtmlAttributeName.MINLENGTH, value);
  }

  @Override
  public final Html.Instruction.OfAttribute name(String value) {
    return attr0(HtmlAttributeName.NAME, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onafterprint(String value) {
    return attr0(HtmlAttributeName.ONAFTERPRINT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onbeforeprint(String value) {
    return attr0(HtmlAttributeName.ONBEFOREPRINT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onbeforeunload(String value) {
    return attr0(HtmlAttributeName.ONBEFOREUNLOAD, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onclick(String value) {
    return attr0(HtmlAttributeName.ONCLICK, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onhashchange(String value) {
    return attr0(HtmlAttributeName.ONHASHCHANGE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onlanguagechange(String value) {
    return attr0(HtmlAttributeName.ONLANGUAGECHANGE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onmessage(String value) {
    return attr0(HtmlAttributeName.ONMESSAGE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onoffline(String value) {
    return attr0(HtmlAttributeName.ONOFFLINE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute ononline(String value) {
    return attr0(HtmlAttributeName.ONONLINE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onpagehide(String value) {
    return attr0(HtmlAttributeName.ONPAGEHIDE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onpageshow(String value) {
    return attr0(HtmlAttributeName.ONPAGESHOW, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onpopstate(String value) {
    return attr0(HtmlAttributeName.ONPOPSTATE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onrejectionhandled(String value) {
    return attr0(HtmlAttributeName.ONREJECTIONHANDLED, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onstorage(String value) {
    return attr0(HtmlAttributeName.ONSTORAGE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onsubmit(String value) {
    return attr0(HtmlAttributeName.ONSUBMIT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onunhandledrejection(String value) {
    return attr0(HtmlAttributeName.ONUNHANDLEDREJECTION, value);
  }

  @Override
  public final Html.Instruction.OfAttribute onunload(String value) {
    return attr0(HtmlAttributeName.ONUNLOAD, value);
  }

  @Override
  public final Html.Instruction.OfAttribute opacity(String value) {
    return attr0(HtmlAttributeName.OPACITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute overflow(String value) {
    return attr0(HtmlAttributeName.OVERFLOW, value);
  }

  @Override
  public final Html.Instruction.OfAttribute paintOrder(String value) {
    return attr0(HtmlAttributeName.PAINT_ORDER, value);
  }

  @Override
  public final Html.Instruction.OfAttribute placeholder(String value) {
    return attr0(HtmlAttributeName.PLACEHOLDER, value);
  }

  @Override
  public final Html.Instruction.OfAttribute pointerEvents(String value) {
    return attr0(HtmlAttributeName.POINTER_EVENTS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute property(String value) {
    return attr0(HtmlAttributeName.PROPERTY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute referrerpolicy(String value) {
    return attr0(HtmlAttributeName.REFERRERPOLICY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute rel(String value) {
    return attr0(HtmlAttributeName.REL, value);
  }

  @Override
  public final Html.Instruction.OfAttribute rev(String value) {
    return attr0(HtmlAttributeName.REV, value);
  }

  @Override
  public final Html.Instruction.OfAttribute role(String value) {
    return attr0(HtmlAttributeName.ROLE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute rows(String value) {
    return attr0(HtmlAttributeName.ROWS, value);
  }

  @Override
  public final Html.Instruction.OfAttribute shapeRendering(String value) {
    return attr0(HtmlAttributeName.SHAPE_RENDERING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute size(String value) {
    return attr0(HtmlAttributeName.SIZE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute sizes(String value) {
    return attr0(HtmlAttributeName.SIZES, value);
  }

  @Override
  public final Html.Instruction.OfAttribute spellcheck(String value) {
    return attr0(HtmlAttributeName.SPELLCHECK, value);
  }

  @Override
  public final Html.Instruction.OfAttribute src(String value) {
    return attr0(HtmlAttributeName.SRC, value);
  }

  @Override
  public final Html.Instruction.OfAttribute srcset(String value) {
    return attr0(HtmlAttributeName.SRCSET, value);
  }

  @Override
  public final Html.Instruction.OfAttribute start(String value) {
    return attr0(HtmlAttributeName.START, value);
  }

  @Override
  public final Html.Instruction.OfAttribute stopColor(String value) {
    return attr0(HtmlAttributeName.STOP_COLOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute stopOpacity(String value) {
    return attr0(HtmlAttributeName.STOP_OPACITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute stroke(String value) {
    return attr0(HtmlAttributeName.STROKE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeDasharray(String value) {
    return attr0(HtmlAttributeName.STROKE_DASHARRAY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeDashoffset(String value) {
    return attr0(HtmlAttributeName.STROKE_DASHOFFSET, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeLinecap(String value) {
    return attr0(HtmlAttributeName.STROKE_LINECAP, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeLinejoin(String value) {
    return attr0(HtmlAttributeName.STROKE_LINEJOIN, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
    return attr0(HtmlAttributeName.STROKE_MITERLIMIT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeOpacity(String value) {
    return attr0(HtmlAttributeName.STROKE_OPACITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute strokeWidth(String value) {
    return attr0(HtmlAttributeName.STROKE_WIDTH, value);
  }

  @Override
  public final Html.Instruction.OfAttribute tabindex(String value) {
    return attr0(HtmlAttributeName.TABINDEX, value);
  }

  @Override
  public final Html.Instruction.OfAttribute target(String value) {
    return attr0(HtmlAttributeName.TARGET, value);
  }

  @Override
  public final Html.Instruction.OfAttribute textAnchor(String value) {
    return attr0(HtmlAttributeName.TEXT_ANCHOR, value);
  }

  @Override
  public final Html.Instruction.OfAttribute textDecoration(String value) {
    return attr0(HtmlAttributeName.TEXT_DECORATION, value);
  }

  @Override
  public final Html.Instruction.OfAttribute textOverflow(String value) {
    return attr0(HtmlAttributeName.TEXT_OVERFLOW, value);
  }

  @Override
  public final Html.Instruction.OfAttribute textRendering(String value) {
    return attr0(HtmlAttributeName.TEXT_RENDERING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute transform(String value) {
    return attr0(HtmlAttributeName.TRANSFORM, value);
  }

  @Override
  public final Html.Instruction.OfAttribute transformOrigin(String value) {
    return attr0(HtmlAttributeName.TRANSFORM_ORIGIN, value);
  }

  @Override
  public final Html.Instruction.OfAttribute translate(String value) {
    return attr0(HtmlAttributeName.TRANSLATE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute type(String value) {
    return attr0(HtmlAttributeName.TYPE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute unicodeBidi(String value) {
    return attr0(HtmlAttributeName.UNICODE_BIDI, value);
  }

  @Override
  public final Html.Instruction.OfAttribute value(String value) {
    return attr0(HtmlAttributeName.VALUE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute vectorEffect(String value) {
    return attr0(HtmlAttributeName.VECTOR_EFFECT, value);
  }

  @Override
  public final Html.Instruction.OfAttribute viewBox(String value) {
    return attr0(HtmlAttributeName.VIEWBOX, value);
  }

  @Override
  public final Html.Instruction.OfAttribute visibility(String value) {
    return attr0(HtmlAttributeName.VISIBILITY, value);
  }

  @Override
  public final Html.Instruction.OfAttribute whiteSpace(String value) {
    return attr0(HtmlAttributeName.WHITE_SPACE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute width(String value) {
    return attr0(HtmlAttributeName.WIDTH, value);
  }

  @Override
  public final Html.Instruction.OfAttribute wordSpacing(String value) {
    return attr0(HtmlAttributeName.WORD_SPACING, value);
  }

  @Override
  public final Html.Instruction.OfAttribute wrap(String value) {
    return attr0(HtmlAttributeName.WRAP, value);
  }

  @Override
  public final Html.Instruction.OfAttribute writingMode(String value) {
    return attr0(HtmlAttributeName.WRITING_MODE, value);
  }

  @Override
  public final Html.Instruction.OfAttribute xmlns(String value) {
    return attr0(HtmlAttributeName.XMLNS, value);
  }

  //
  // AMBIGUOUS
  //

  abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String text);

  @Override
  public final Html.Instruction.OfAmbiguous clipPath(String text) {
    return ambiguous(HtmlAmbiguous.CLIPPATH, text);
  }

  @Override
  public final Html.Instruction.OfAmbiguous form(String text) {
    return ambiguous(HtmlAmbiguous.FORM, text);
  }

  @Override
  public final Html.Instruction.OfAmbiguous label(String text) {
    return ambiguous(HtmlAmbiguous.LABEL, text);
  }

  @Override
  public final Html.Instruction.OfAmbiguous style(String text) {
    return ambiguous(HtmlAmbiguous.STYLE, text);
  }

  @Override
  public final Html.Instruction.OfAmbiguous title(String text) {
    return ambiguous(HtmlAmbiguous.TITLE, text);
  }

}
