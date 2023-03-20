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
package objectos.html.tmpl;

import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;

public enum StandardElementName implements ElementName {
  A(ElementKind.NORMAL, "a"),

  ABBR(ElementKind.NORMAL, "abbr"),

  ARTICLE(ElementKind.NORMAL, "article"),

  B(ElementKind.NORMAL, "b"),

  BLOCKQUOTE(ElementKind.NORMAL, "blockquote"),

  BODY(ElementKind.NORMAL, "body"),

  BR(ElementKind.VOID, "br"),

  BUTTON(ElementKind.NORMAL, "button"),

  CLIPPATH(ElementKind.NORMAL, "clipPath"),

  CODE(ElementKind.NORMAL, "code"),

  DD(ElementKind.NORMAL, "dd"),

  DEFS(ElementKind.NORMAL, "defs"),

  DETAILS(ElementKind.NORMAL, "details"),

  DIV(ElementKind.NORMAL, "div"),

  DL(ElementKind.NORMAL, "dl"),

  DT(ElementKind.NORMAL, "dt"),

  EM(ElementKind.NORMAL, "em"),

  FIELDSET(ElementKind.NORMAL, "fieldset"),

  FIGURE(ElementKind.NORMAL, "figure"),

  FOOTER(ElementKind.NORMAL, "footer"),

  FORM(ElementKind.NORMAL, "form"),

  G(ElementKind.NORMAL, "g"),

  H1(ElementKind.NORMAL, "h1"),

  H2(ElementKind.NORMAL, "h2"),

  H3(ElementKind.NORMAL, "h3"),

  H4(ElementKind.NORMAL, "h4"),

  H5(ElementKind.NORMAL, "h5"),

  H6(ElementKind.NORMAL, "h6"),

  HEAD(ElementKind.NORMAL, "head"),

  HEADER(ElementKind.NORMAL, "header"),

  HGROUP(ElementKind.NORMAL, "hgroup"),

  HR(ElementKind.VOID, "hr"),

  HTML(ElementKind.NORMAL, "html"),

  IMG(ElementKind.VOID, "img"),

  INPUT(ElementKind.VOID, "input"),

  KBD(ElementKind.NORMAL, "kbd"),

  LABEL(ElementKind.NORMAL, "label"),

  LEGEND(ElementKind.NORMAL, "legend"),

  LI(ElementKind.NORMAL, "li"),

  LINK(ElementKind.VOID, "link"),

  MAIN(ElementKind.NORMAL, "main"),

  MENU(ElementKind.NORMAL, "menu"),

  META(ElementKind.VOID, "meta"),

  NAV(ElementKind.NORMAL, "nav"),

  OL(ElementKind.NORMAL, "ol"),

  OPTGROUP(ElementKind.NORMAL, "optgroup"),

  OPTION(ElementKind.NORMAL, "option"),

  P(ElementKind.NORMAL, "p"),

  PATH(ElementKind.NORMAL, "path"),

  PRE(ElementKind.NORMAL, "pre"),

  PROGRESS(ElementKind.NORMAL, "progress"),

  SAMP(ElementKind.NORMAL, "samp"),

  SCRIPT(ElementKind.NORMAL, "script"),

  SECTION(ElementKind.NORMAL, "section"),

  SELECT(ElementKind.NORMAL, "select"),

  SMALL(ElementKind.NORMAL, "small"),

  SPAN(ElementKind.NORMAL, "span"),

  STRONG(ElementKind.NORMAL, "strong"),

  STYLE(ElementKind.NORMAL, "style"),

  SUB(ElementKind.NORMAL, "sub"),

  SUMMARY(ElementKind.NORMAL, "summary"),

  SUP(ElementKind.NORMAL, "sup"),

  SVG(ElementKind.NORMAL, "svg"),

  TABLE(ElementKind.NORMAL, "table"),

  TBODY(ElementKind.NORMAL, "tbody"),

  TD(ElementKind.NORMAL, "td"),

  TEMPLATE(ElementKind.NORMAL, "template"),

  TEXTAREA(ElementKind.NORMAL, "textarea"),

  TH(ElementKind.NORMAL, "th"),

  THEAD(ElementKind.NORMAL, "thead"),

  TITLE(ElementKind.NORMAL, "title"),

  TR(ElementKind.NORMAL, "tr"),

  UL(ElementKind.NORMAL, "ul");

  private static final StandardElementName[] ARRAY = StandardElementName.values();

  private final ElementKind kind;

  private final String name;

  private StandardElementName(ElementKind kind, String name) {
    this.kind = kind;
    this.name = name;
  }

  public static StandardElementName getByCode(int code) {
    return ARRAY[code];
  }

  public static int size() {
    return ARRAY.length;
  }

  @Override
  public final int getCode() {
    return ordinal();
  }

  @Override
  public final ElementKind getKind() {
    return kind;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final void mark(Marker marker) {}

  @Override
  public final void render(Renderer renderer) {}
}
