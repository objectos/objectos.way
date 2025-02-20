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

sealed abstract class HtmlMarkupElements extends HtmlMarkupAttributes implements Html.MarkupElements permits HtmlMarkup, HtmlMarkupTestable {

  HtmlMarkupElements() {}

  abstract Html.Instruction.OfAmbiguous ambiguous(HtmlAmbiguous name, String value);

  abstract Html.Instruction.OfElement element(Html.ElementName name, Html.Instruction... contents);

  abstract Html.Instruction.OfElement element(Html.ElementName name, String text);

  @Override
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  @Override
  public final Html.Instruction.OfElement a(String text) {
    return element(HtmlElementName.A, text);
  }

  @Override
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  @Override
  public final Html.Instruction.OfElement abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  @Override
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  @Override
  public final Html.Instruction.OfElement article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  @Override
  public final Html.Instruction.OfElement aside(Html.Instruction... contents) {
    return element(HtmlElementName.ASIDE, contents);
  }

  @Override
  public final Html.Instruction.OfElement aside(String text) {
    return element(HtmlElementName.ASIDE, text);
  }

  /**
   * Renders the {@code b} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /**
   * Renders the {@code b} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement b(String text) {
    return element(HtmlElementName.B, text);
  }

  /**
   * Renders the {@code blockquote} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /**
   * Renders the {@code blockquote} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /**
   * Renders the {@code body} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /**
   * Renders the {@code body} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /**
   * Renders the {@code br} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /**
   * Renders the {@code button} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /**
   * Renders the {@code button} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /**
   * Renders the {@code clipPath} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
  }

  /**
   * Renders the {@code clipPath} attribute or element with the specified
   * text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfAmbiguous clipPath(String text) {
    return ambiguous(HtmlAmbiguous.CLIPPATH, text);
  }

  /**
   * Renders the {@code code} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /**
   * Renders the {@code code} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /**
   * Renders the {@code dd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /**
   * Renders the {@code dd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /**
   * Renders the {@code defs} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /**
   * Renders the {@code defs} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /**
   * Renders the {@code details} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /**
   * Renders the {@code details} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /**
   * Renders the {@code div} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /**
   * Renders the {@code div} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /**
   * Renders the {@code dl} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /**
   * Renders the {@code dl} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /**
   * Renders the {@code dt} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /**
   * Renders the {@code dt} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /**
   * Renders the {@code em} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /**
   * Renders the {@code em} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /**
   * Renders the {@code fieldset} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /**
   * Renders the {@code fieldset} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /**
   * Renders the {@code figure} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /**
   * Renders the {@code figure} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /**
   * Renders the {@code footer} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /**
   * Renders the {@code footer} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /**
   * Renders the {@code form} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /**
   * Renders the {@code form} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfAmbiguous form(String text) {
    return ambiguous(HtmlAmbiguous.FORM, text);
  }

  /**
   * Renders the {@code g} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /**
   * Renders the {@code g} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement g(String text) {
    return element(HtmlElementName.G, text);
  }

  /**
   * Renders the {@code h1} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /**
   * Renders the {@code h1} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /**
   * Renders the {@code h2} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /**
   * Renders the {@code h2} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /**
   * Renders the {@code h3} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /**
   * Renders the {@code h3} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /**
   * Renders the {@code h4} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /**
   * Renders the {@code h4} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /**
   * Renders the {@code h5} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /**
   * Renders the {@code h5} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /**
   * Renders the {@code h6} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /**
   * Renders the {@code h6} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /**
   * Renders the {@code head} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /**
   * Renders the {@code head} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /**
   * Renders the {@code header} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /**
   * Renders the {@code header} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /**
   * Renders the {@code hgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /**
   * Renders the {@code hgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /**
   * Renders the {@code hr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /**
   * Renders the {@code html} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /**
   * Renders the {@code html} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /**
   * Renders the {@code img} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /**
   * Renders the {@code input} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /**
   * Renders the {@code kbd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /**
   * Renders the {@code kbd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /**
   * Renders the {@code label} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /**
   * Renders the {@code label} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfAmbiguous label(String text) {
    return ambiguous(HtmlAmbiguous.LABEL, text);
  }

  /**
   * Renders the {@code legend} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /**
   * Renders the {@code legend} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /**
   * Renders the {@code li} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /**
   * Renders the {@code li} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /**
   * Renders the {@code link} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /**
   * Renders the {@code main} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /**
   * Renders the {@code main} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /**
   * Renders the {@code menu} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /**
   * Renders the {@code menu} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /**
   * Renders the {@code meta} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.META, contents);
  }

  /**
   * Renders the {@code nav} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /**
   * Renders the {@code nav} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /**
   * Renders the {@code ol} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /**
   * Renders the {@code ol} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /**
   * Renders the {@code optgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /**
   * Renders the {@code optgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /**
   * Renders the {@code option} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /**
   * Renders the {@code option} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /**
   * Renders the {@code p} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /**
   * Renders the {@code p} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement p(String text) {
    return element(HtmlElementName.P, text);
  }

  /**
   * Renders the {@code path} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /**
   * Renders the {@code path} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /**
   * Renders the {@code pre} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /**
   * Renders the {@code pre} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /**
   * Renders the {@code progress} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /**
   * Renders the {@code progress} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /**
   * Renders the {@code samp} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /**
   * Renders the {@code samp} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /**
   * Renders the {@code script} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /**
   * Renders the {@code script} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /**
   * Renders the {@code section} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /**
   * Renders the {@code section} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /**
   * Renders the {@code select} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /**
   * Renders the {@code select} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /**
   * Renders the {@code small} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /**
   * Renders the {@code small} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /**
   * Renders the {@code span} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /**
   * Renders the {@code span} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /**
   * Renders the {@code strong} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /**
   * Renders the {@code strong} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /**
   * Renders the {@code style} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /**
   * Renders the {@code style} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /**
   * Renders the {@code sub} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /**
   * Renders the {@code sub} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /**
   * Renders the {@code summary} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /**
   * Renders the {@code summary} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /**
   * Renders the {@code sup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /**
   * Renders the {@code sup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /**
   * Renders the {@code svg} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /**
   * Renders the {@code svg} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /**
   * Renders the {@code table} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /**
   * Renders the {@code table} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /**
   * Renders the {@code tbody} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /**
   * Renders the {@code tbody} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /**
   * Renders the {@code td} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /**
   * Renders the {@code td} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /**
   * Renders the {@code template} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /**
   * Renders the {@code template} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /**
   * Renders the {@code textarea} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /**
   * Renders the {@code textarea} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /**
   * Renders the {@code th} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /**
   * Renders the {@code th} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /**
   * Renders the {@code thead} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /**
   * Renders the {@code thead} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /**
   * Renders the {@code title} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /**
   * Renders the {@code title} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  @Override
  public final Html.Instruction.OfAmbiguous title(String text) {
    return ambiguous(HtmlAmbiguous.TITLE, text);
  }

  /**
   * Renders the {@code tr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /**
   * Renders the {@code tr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /**
   * Renders the {@code ul} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /**
   * Renders the {@code ul} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  @Override
  public final Html.Instruction.OfElement ul(String text) {
    return element(HtmlElementName.UL, text);
  }

}