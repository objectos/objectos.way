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

/**
 * Provides methods for rendering HTML elements.
 */
sealed class HtmlRecorderElements extends HtmlRecorderAttributes permits HtmlRecorder {

  HtmlRecorderElements() {}

  /**
   * Generates the {@code a} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement a(Html.Instruction... contents) {
    return element(HtmlElementName.A, contents);
  }

  /**
   * Generates the {@code a} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement a(String text) {
    return element(HtmlElementName.A, text);
  }

  /**
   * Generates the {@code abbr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
    return element(HtmlElementName.ABBR, contents);
  }

  /**
   * Generates the {@code abbr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement abbr(String text) {
    return element(HtmlElementName.ABBR, text);
  }

  /**
   * Generates the {@code article} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement article(Html.Instruction... contents) {
    return element(HtmlElementName.ARTICLE, contents);
  }

  /**
   * Generates the {@code article} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement article(String text) {
    return element(HtmlElementName.ARTICLE, text);
  }

  /**
   * Generates the {@code b} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement b(Html.Instruction... contents) {
    return element(HtmlElementName.B, contents);
  }

  /**
   * Generates the {@code b} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement b(String text) {
    return element(HtmlElementName.B, text);
  }

  /**
   * Generates the {@code blockquote} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
    return element(HtmlElementName.BLOCKQUOTE, contents);
  }

  /**
   * Generates the {@code blockquote} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement blockquote(String text) {
    return element(HtmlElementName.BLOCKQUOTE, text);
  }

  /**
   * Generates the {@code body} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement body(Html.Instruction... contents) {
    return element(HtmlElementName.BODY, contents);
  }

  /**
   * Generates the {@code body} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement body(String text) {
    return element(HtmlElementName.BODY, text);
  }

  /**
   * Generates the {@code br} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.BR, contents);
  }

  /**
   * Generates the {@code button} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement button(Html.Instruction... contents) {
    return element(HtmlElementName.BUTTON, contents);
  }

  /**
   * Generates the {@code button} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement button(String text) {
    return element(HtmlElementName.BUTTON, text);
  }

  /**
   * Generates the {@code clipPath} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
    return element(HtmlElementName.CLIPPATH, contents);
  }

  /**
   * Generates the {@code clipPath} attribute or element with the specified
   * text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement clipPath(String text) {
    ambiguous(HtmlAmbiguous.CLIPPATH, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code code} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement code(Html.Instruction... contents) {
    return element(HtmlElementName.CODE, contents);
  }

  /**
   * Generates the {@code code} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement code(String text) {
    return element(HtmlElementName.CODE, text);
  }

  /**
   * Generates the {@code dd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dd(Html.Instruction... contents) {
    return element(HtmlElementName.DD, contents);
  }

  /**
   * Generates the {@code dd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dd(String text) {
    return element(HtmlElementName.DD, text);
  }

  /**
   * Generates the {@code defs} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement defs(Html.Instruction... contents) {
    return element(HtmlElementName.DEFS, contents);
  }

  /**
   * Generates the {@code defs} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement defs(String text) {
    return element(HtmlElementName.DEFS, text);
  }

  /**
   * Generates the {@code details} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement details(Html.Instruction... contents) {
    return element(HtmlElementName.DETAILS, contents);
  }

  /**
   * Generates the {@code details} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement details(String text) {
    return element(HtmlElementName.DETAILS, text);
  }

  /**
   * Generates the {@code div} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement div(Html.Instruction... contents) {
    return element(HtmlElementName.DIV, contents);
  }

  /**
   * Generates the {@code div} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement div(String text) {
    return element(HtmlElementName.DIV, text);
  }

  /**
   * Generates the {@code dl} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dl(Html.Instruction... contents) {
    return element(HtmlElementName.DL, contents);
  }

  /**
   * Generates the {@code dl} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dl(String text) {
    return element(HtmlElementName.DL, text);
  }

  /**
   * Generates the {@code dt} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dt(Html.Instruction... contents) {
    return element(HtmlElementName.DT, contents);
  }

  /**
   * Generates the {@code dt} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement dt(String text) {
    return element(HtmlElementName.DT, text);
  }

  /**
   * Generates the {@code em} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement em(Html.Instruction... contents) {
    return element(HtmlElementName.EM, contents);
  }

  /**
   * Generates the {@code em} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement em(String text) {
    return element(HtmlElementName.EM, text);
  }

  /**
   * Generates the {@code fieldset} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
    return element(HtmlElementName.FIELDSET, contents);
  }

  /**
   * Generates the {@code fieldset} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement fieldset(String text) {
    return element(HtmlElementName.FIELDSET, text);
  }

  /**
   * Generates the {@code figure} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement figure(Html.Instruction... contents) {
    return element(HtmlElementName.FIGURE, contents);
  }

  /**
   * Generates the {@code figure} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement figure(String text) {
    return element(HtmlElementName.FIGURE, text);
  }

  /**
   * Generates the {@code footer} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement footer(Html.Instruction... contents) {
    return element(HtmlElementName.FOOTER, contents);
  }

  /**
   * Generates the {@code footer} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement footer(String text) {
    return element(HtmlElementName.FOOTER, text);
  }

  /**
   * Generates the {@code form} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement form(Html.Instruction... contents) {
    return element(HtmlElementName.FORM, contents);
  }

  /**
   * Generates the {@code form} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement form(String text) {
    ambiguous(HtmlAmbiguous.FORM, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code g} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement g(Html.Instruction... contents) {
    return element(HtmlElementName.G, contents);
  }

  /**
   * Generates the {@code g} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement g(String text) {
    return element(HtmlElementName.G, text);
  }

  /**
   * Generates the {@code h1} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h1(Html.Instruction... contents) {
    return element(HtmlElementName.H1, contents);
  }

  /**
   * Generates the {@code h1} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h1(String text) {
    return element(HtmlElementName.H1, text);
  }

  /**
   * Generates the {@code h2} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h2(Html.Instruction... contents) {
    return element(HtmlElementName.H2, contents);
  }

  /**
   * Generates the {@code h2} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h2(String text) {
    return element(HtmlElementName.H2, text);
  }

  /**
   * Generates the {@code h3} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h3(Html.Instruction... contents) {
    return element(HtmlElementName.H3, contents);
  }

  /**
   * Generates the {@code h3} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h3(String text) {
    return element(HtmlElementName.H3, text);
  }

  /**
   * Generates the {@code h4} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h4(Html.Instruction... contents) {
    return element(HtmlElementName.H4, contents);
  }

  /**
   * Generates the {@code h4} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h4(String text) {
    return element(HtmlElementName.H4, text);
  }

  /**
   * Generates the {@code h5} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h5(Html.Instruction... contents) {
    return element(HtmlElementName.H5, contents);
  }

  /**
   * Generates the {@code h5} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h5(String text) {
    return element(HtmlElementName.H5, text);
  }

  /**
   * Generates the {@code h6} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h6(Html.Instruction... contents) {
    return element(HtmlElementName.H6, contents);
  }

  /**
   * Generates the {@code h6} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement h6(String text) {
    return element(HtmlElementName.H6, text);
  }

  /**
   * Generates the {@code head} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement head(Html.Instruction... contents) {
    return element(HtmlElementName.HEAD, contents);
  }

  /**
   * Generates the {@code head} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement head(String text) {
    return element(HtmlElementName.HEAD, text);
  }

  /**
   * Generates the {@code header} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement header(Html.Instruction... contents) {
    return element(HtmlElementName.HEADER, contents);
  }

  /**
   * Generates the {@code header} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement header(String text) {
    return element(HtmlElementName.HEADER, text);
  }

  /**
   * Generates the {@code hgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
    return element(HtmlElementName.HGROUP, contents);
  }

  /**
   * Generates the {@code hgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hgroup(String text) {
    return element(HtmlElementName.HGROUP, text);
  }

  /**
   * Generates the {@code hr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.HR, contents);
  }

  /**
   * Generates the {@code html} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement html(Html.Instruction... contents) {
    return element(HtmlElementName.HTML, contents);
  }

  /**
   * Generates the {@code html} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement html(String text) {
    return element(HtmlElementName.HTML, text);
  }

  /**
   * Generates the {@code img} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.IMG, contents);
  }

  /**
   * Generates the {@code input} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.INPUT, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
    return element(HtmlElementName.KBD, contents);
  }

  /**
   * Generates the {@code kbd} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement kbd(String text) {
    return element(HtmlElementName.KBD, text);
  }

  /**
   * Generates the {@code label} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement label(Html.Instruction... contents) {
    return element(HtmlElementName.LABEL, contents);
  }

  /**
   * Generates the {@code label} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement label(String text) {
    ambiguous(HtmlAmbiguous.LABEL, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code legend} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement legend(Html.Instruction... contents) {
    return element(HtmlElementName.LEGEND, contents);
  }

  /**
   * Generates the {@code legend} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement legend(String text) {
    return element(HtmlElementName.LEGEND, text);
  }

  /**
   * Generates the {@code li} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement li(Html.Instruction... contents) {
    return element(HtmlElementName.LI, contents);
  }

  /**
   * Generates the {@code li} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement li(String text) {
    return element(HtmlElementName.LI, text);
  }

  /**
   * Generates the {@code link} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.LINK, contents);
  }

  /**
   * Generates the {@code main} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement main(Html.Instruction... contents) {
    return element(HtmlElementName.MAIN, contents);
  }

  /**
   * Generates the {@code main} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement main(String text) {
    return element(HtmlElementName.MAIN, text);
  }

  /**
   * Generates the {@code menu} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement menu(Html.Instruction... contents) {
    return element(HtmlElementName.MENU, contents);
  }

  /**
   * Generates the {@code menu} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement menu(String text) {
    return element(HtmlElementName.MENU, text);
  }

  /**
   * Generates the {@code meta} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
    return element(HtmlElementName.META, contents);
  }

  /**
   * Generates the {@code nav} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement nav(Html.Instruction... contents) {
    return element(HtmlElementName.NAV, contents);
  }

  /**
   * Generates the {@code nav} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement nav(String text) {
    return element(HtmlElementName.NAV, text);
  }

  /**
   * Generates the {@code ol} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ol(Html.Instruction... contents) {
    return element(HtmlElementName.OL, contents);
  }

  /**
   * Generates the {@code ol} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ol(String text) {
    return element(HtmlElementName.OL, text);
  }

  /**
   * Generates the {@code optgroup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
    return element(HtmlElementName.OPTGROUP, contents);
  }

  /**
   * Generates the {@code optgroup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement optgroup(String text) {
    return element(HtmlElementName.OPTGROUP, text);
  }

  /**
   * Generates the {@code option} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement option(Html.Instruction... contents) {
    return element(HtmlElementName.OPTION, contents);
  }

  /**
   * Generates the {@code option} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement option(String text) {
    return element(HtmlElementName.OPTION, text);
  }

  /**
   * Generates the {@code p} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement p(Html.Instruction... contents) {
    return element(HtmlElementName.P, contents);
  }

  /**
   * Generates the {@code p} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement p(String text) {
    return element(HtmlElementName.P, text);
  }

  /**
   * Generates the {@code path} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement path(Html.Instruction... contents) {
    return element(HtmlElementName.PATH, contents);
  }

  /**
   * Generates the {@code path} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement path(String text) {
    return element(HtmlElementName.PATH, text);
  }

  /**
   * Generates the {@code pre} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement pre(Html.Instruction... contents) {
    return element(HtmlElementName.PRE, contents);
  }

  /**
   * Generates the {@code pre} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement pre(String text) {
    return element(HtmlElementName.PRE, text);
  }

  /**
   * Generates the {@code progress} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement progress(Html.Instruction... contents) {
    return element(HtmlElementName.PROGRESS, contents);
  }

  /**
   * Generates the {@code progress} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement progress(String text) {
    return element(HtmlElementName.PROGRESS, text);
  }

  /**
   * Generates the {@code samp} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement samp(Html.Instruction... contents) {
    return element(HtmlElementName.SAMP, contents);
  }

  /**
   * Generates the {@code samp} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement samp(String text) {
    return element(HtmlElementName.SAMP, text);
  }

  /**
   * Generates the {@code script} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement script(Html.Instruction... contents) {
    return element(HtmlElementName.SCRIPT, contents);
  }

  /**
   * Generates the {@code script} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement script(String text) {
    return element(HtmlElementName.SCRIPT, text);
  }

  /**
   * Generates the {@code section} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement section(Html.Instruction... contents) {
    return element(HtmlElementName.SECTION, contents);
  }

  /**
   * Generates the {@code section} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement section(String text) {
    return element(HtmlElementName.SECTION, text);
  }

  /**
   * Generates the {@code select} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement select(Html.Instruction... contents) {
    return element(HtmlElementName.SELECT, contents);
  }

  /**
   * Generates the {@code select} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement select(String text) {
    return element(HtmlElementName.SELECT, text);
  }

  /**
   * Generates the {@code small} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement small(Html.Instruction... contents) {
    return element(HtmlElementName.SMALL, contents);
  }

  /**
   * Generates the {@code small} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement small(String text) {
    return element(HtmlElementName.SMALL, text);
  }

  /**
   * Generates the {@code span} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement span(Html.Instruction... contents) {
    return element(HtmlElementName.SPAN, contents);
  }

  /**
   * Generates the {@code span} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement span(String text) {
    return element(HtmlElementName.SPAN, text);
  }

  /**
   * Generates the {@code strong} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement strong(Html.Instruction... contents) {
    return element(HtmlElementName.STRONG, contents);
  }

  /**
   * Generates the {@code strong} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement strong(String text) {
    return element(HtmlElementName.STRONG, text);
  }

  /**
   * Generates the {@code style} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement style(Html.Instruction... contents) {
    return element(HtmlElementName.STYLE, contents);
  }

  /**
   * Generates the {@code style} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement style(String text) {
    return element(HtmlElementName.STYLE, text);
  }

  /**
   * Generates the {@code sub} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sub(Html.Instruction... contents) {
    return element(HtmlElementName.SUB, contents);
  }

  /**
   * Generates the {@code sub} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sub(String text) {
    return element(HtmlElementName.SUB, text);
  }

  /**
   * Generates the {@code summary} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement summary(Html.Instruction... contents) {
    return element(HtmlElementName.SUMMARY, contents);
  }

  /**
   * Generates the {@code summary} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement summary(String text) {
    return element(HtmlElementName.SUMMARY, text);
  }

  /**
   * Generates the {@code sup} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sup(Html.Instruction... contents) {
    return element(HtmlElementName.SUP, contents);
  }

  /**
   * Generates the {@code sup} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement sup(String text) {
    return element(HtmlElementName.SUP, text);
  }

  /**
   * Generates the {@code svg} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement svg(Html.Instruction... contents) {
    return element(HtmlElementName.SVG, contents);
  }

  /**
   * Generates the {@code svg} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement svg(String text) {
    return element(HtmlElementName.SVG, text);
  }

  /**
   * Generates the {@code table} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement table(Html.Instruction... contents) {
    return element(HtmlElementName.TABLE, contents);
  }

  /**
   * Generates the {@code table} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement table(String text) {
    return element(HtmlElementName.TABLE, text);
  }

  /**
   * Generates the {@code tbody} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
    return element(HtmlElementName.TBODY, contents);
  }

  /**
   * Generates the {@code tbody} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tbody(String text) {
    return element(HtmlElementName.TBODY, text);
  }

  /**
   * Generates the {@code td} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement td(Html.Instruction... contents) {
    return element(HtmlElementName.TD, contents);
  }

  /**
   * Generates the {@code td} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement td(String text) {
    return element(HtmlElementName.TD, text);
  }

  /**
   * Generates the {@code template} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement template(Html.Instruction... contents) {
    return element(HtmlElementName.TEMPLATE, contents);
  }

  /**
   * Generates the {@code template} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement template(String text) {
    return element(HtmlElementName.TEMPLATE, text);
  }

  /**
   * Generates the {@code textarea} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
    return element(HtmlElementName.TEXTAREA, contents);
  }

  /**
   * Generates the {@code textarea} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement textarea(String text) {
    return element(HtmlElementName.TEXTAREA, text);
  }

  /**
   * Generates the {@code th} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement th(Html.Instruction... contents) {
    return element(HtmlElementName.TH, contents);
  }

  /**
   * Generates the {@code th} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement th(String text) {
    return element(HtmlElementName.TH, text);
  }

  /**
   * Generates the {@code thead} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement thead(Html.Instruction... contents) {
    return element(HtmlElementName.THEAD, contents);
  }

  /**
   * Generates the {@code thead} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement thead(String text) {
    return element(HtmlElementName.THEAD, text);
  }

  /**
   * Generates the {@code title} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement title(Html.Instruction... contents) {
    return element(HtmlElementName.TITLE, contents);
  }

  /**
   * Generates the {@code title} attribute or element with the specified text.
   *
   * @param text
   *        the text value of this attribute or element
   *
   * @return an instruction representing this attribute or element.
   */
  public final Html.Instruction.OfElement title(String text) {
    ambiguous(HtmlAmbiguous.TITLE, text);
    return Html.ELEMENT;
  }

  /**
   * Generates the {@code tr} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tr(Html.Instruction... contents) {
    return element(HtmlElementName.TR, contents);
  }

  /**
   * Generates the {@code tr} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement tr(String text) {
    return element(HtmlElementName.TR, text);
  }

  /**
   * Generates the {@code ul} element with the specified content.
   *
   * @param contents
   *        the attributes and children of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ul(Html.Instruction... contents) {
    return element(HtmlElementName.UL, contents);
  }

  /**
   * Generates the {@code ul} element with the specified text.
   *
   * @param text
   *        the text value of this element
   *
   * @return an instruction representing this element.
   */
  public final Html.Instruction.OfElement ul(String text) {
    return element(HtmlElementName.UL, text);
  }

}