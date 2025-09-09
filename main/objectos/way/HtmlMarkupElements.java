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

sealed abstract class HtmlMarkupElements
    extends HtmlMarkupAttributes
    implements Html.MarkupElements
    permits HtmlMarkup, HtmlMarkupOfTestable {

  HtmlMarkupElements() {}

  abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String value);

  /// Renders the `a` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  /// Renders the `a` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement a(String text) {
    return element(HtmlElementName.A, text);
  }

  /// Renders the `abbr` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  /// Renders the `abbr` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  /// Renders the `article` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  /// Renders the `article` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  /// Renders the `aside` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement aside(Html.Instruction... contents) {
    return element(HtmlElementName.ASIDE, contents);
  }

  /// Renders the `aside` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement aside(String text) {
    return element(HtmlElementName.ASIDE, text);
  }

  /// Renders the `b` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /// Renders the `b` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement b(String text) {
    return element(HtmlElementName.B, text);
  }

  /// Renders the `blockquote` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /// Renders the `blockquote` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /// Renders the `body` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /// Renders the `body` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /// Renders the `br` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /// Renders the `button` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /// Renders the `button` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /// Renders the `clipPath` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
  }

  /// Renders the `clipPath` attribute or element with the specified
  /// text.
  /// @param text the text value of this attribute or element
  /// @return an instruction representing this attribute or element.
  @Override
  public final Html.Instruction.OfAmbiguous clipPath(String text) {
    return ambiguous(HtmlAmbiguous.CLIPPATH, text);
  }

  /// Renders the `code` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /// Renders the `code` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /// Renders the `dd` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /// Renders the `dd` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /// Renders the `defs` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /// Renders the `defs` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /// Renders the `details` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /// Renders the `details` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /// Renders the `dialog` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dialog(Html.Instruction... contents) {
    return element(HtmlElementName.DIALOG, contents);
  }

  /// Renders the `dialog` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dialog(String text) {
    return element(HtmlElementName.DIALOG, text);
  }

  /// Renders the `div` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /// Renders the `div` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /// Renders the `dl` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /// Renders the `dl` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /// Renders the `dt` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /// Renders the `dt` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /// Renders the `em` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /// Renders the `em` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /// Renders the `fieldset` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /// Renders the `fieldset` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /// Renders the `figure` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /// Renders the `figure` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /// Renders the `footer` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /// Renders the `footer` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /// Renders the `form` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /// Renders the `form` attribute or element with the specified text.
  /// @param text the text value of this attribute or element
  /// @return an instruction representing this attribute or element.
  @Override
  public final Html.Instruction.OfAmbiguous form(String text) {
    return ambiguous(HtmlAmbiguous.FORM, text);
  }

  /// Renders the `g` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /// Renders the `g` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement g(String text) {
    return element(HtmlElementName.G, text);
  }

  /// Renders the `h1` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /// Renders the `h1` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /// Renders the `h2` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /// Renders the `h2` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /// Renders the `h3` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /// Renders the `h3` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /// Renders the `h4` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /// Renders the `h4` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /// Renders the `h5` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /// Renders the `h5` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /// Renders the `h6` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /// Renders the `h6` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /// Renders the `head` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /// Renders the `head` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /// Renders the `header` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /// Renders the `header` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /// Renders the `hgroup` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /// Renders the `hgroup` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /// Renders the `hr` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /// Renders the `html` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /// Renders the `html` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /// Renders the `img` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /// Renders the `input` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /// Renders the `kbd` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /// Renders the `kbd` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /// Renders the `label` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /// Renders the `label` attribute or element with the specified text.
  /// @param text the text value of this attribute or element
  /// @return an instruction representing this attribute or element.
  @Override
  public final Html.Instruction.OfAmbiguous label(String text) {
    return ambiguous(HtmlAmbiguous.LABEL, text);
  }

  /// Renders the `legend` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /// Renders the `legend` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /// Renders the `li` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /// Renders the `li` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /// Renders the `link` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /// Renders the `main` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /// Renders the `main` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /// Renders the `menu` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /// Renders the `menu` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /// Renders the `meta` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.META, contents);
  }

  /// Renders the `nav` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /// Renders the `nav` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /// Renders the `ol` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /// Renders the `ol` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /// Renders the `optgroup` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /// Renders the `optgroup` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /// Renders the `option` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /// Renders the `option` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /// Renders the `p` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /// Renders the `p` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement p(String text) {
    return element(HtmlElementName.P, text);
  }

  /// Renders the `path` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /// Renders the `path` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /// Renders the `pre` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /// Renders the `pre` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /// Renders the `progress` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /// Renders the `progress` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /// Renders the `samp` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /// Renders the `samp` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /// Renders the `script` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /// Renders the `script` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /// Renders the `section` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /// Renders the `section` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /// Renders the `select` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /// Renders the `select` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /// Renders the `small` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /// Renders the `small` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /// Renders the `span` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /// Renders the `span` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /// Renders the `strong` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /// Renders the `strong` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /// Renders the `style` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /// Renders the `style` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /// Renders the `sub` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /// Renders the `sub` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /// Renders the `summary` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /// Renders the `summary` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /// Renders the `sup` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /// Renders the `sup` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /// Renders the `svg` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /// Renders the `svg` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /// Renders the `table` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /// Renders the `table` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /// Renders the `tbody` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /// Renders the `tbody` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /// Renders the `td` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /// Renders the `td` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /// Renders the `template` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /// Renders the `template` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /// Renders the `textarea` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /// Renders the `textarea` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /// Renders the `th` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /// Renders the `th` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /// Renders the `thead` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /// Renders the `thead` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /// Renders the `title` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /// Renders the `title` attribute or element with the specified text.
  /// @param text the text value of this attribute or element
  /// @return an instruction representing this attribute or element.
  @Override
  public final Html.Instruction.OfAmbiguous title(String text) {
    return ambiguous(HtmlAmbiguous.TITLE, text);
  }

  /// Renders the `tr` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /// Renders the `tr` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /// Renders the `ul` element with the specified content.
  /// @param contents the attributes and children of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /// Renders the `ul` element with the specified text.
  /// @param text the text value of this element
  /// @return an instruction representing this element.
  @Override
  public final Html.Instruction.OfElement ul(String text) {
    return element(HtmlElementName.UL, text);
  }

}