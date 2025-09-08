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

import java.util.Locale;
import java.util.Set;

enum HtmlElementName implements Html.ElementName {

  A(true),
  ABBR(true),
  ARTICLE(true),
  ASIDE(true),
  B(true),
  BLOCKQUOTE(true),
  BODY(true),
  BR(false),
  BUTTON(true),
  CLIPPATH(true, "clipPath"),
  CODE(true),
  DD(true),
  DEFS(true),
  DETAILS(true),
  DIALOG(true),
  DIV(true),
  DL(true),
  DT(true),
  EM(true),
  FIELDSET(true),
  FIGURE(true),
  FOOTER(true),
  FORM(true),
  G(true),
  H1(true),
  H2(true),
  H3(true),
  H4(true),
  H5(true),
  H6(true),
  HEAD(true),
  HEADER(true),
  HGROUP(true),
  HR(false),
  HTML(true),
  IMG(false),
  INPUT(false),
  KBD(true),
  LABEL(true),
  LEGEND(true),
  LI(true),
  LINK(false),
  MAIN(true),
  MENU(true),
  META(false),
  NAV(true),
  OL(true),
  OPTGROUP(true),
  OPTION(true),
  P(true),
  PATH(true),
  PRE(true),
  PROGRESS(true),
  SAMP(true),
  SCRIPT(true),
  SECTION(true),
  SELECT(true),
  SMALL(true),
  SPAN(true),
  STRONG(true),
  STYLE(true),
  SUB(true),
  SUMMARY(true),
  SUP(true),
  SVG(true),
  TABLE(true),
  TBODY(true),
  TD(true),
  TEMPLATE(true),
  TEXTAREA(true),
  TH(true),
  THEAD(true),
  TITLE(true),
  TR(true),
  UL(true);

  private static final HtmlElementName[] VALUES = values();

  final boolean endTag;

  final String lowerCase;

  private HtmlElementName(boolean endTag) {
    this.endTag = endTag;

    lowerCase = name().toLowerCase(Locale.US);
  }

  private HtmlElementName(boolean endTag, String lowerCase) {
    this.endTag = endTag;

    this.lowerCase = lowerCase;
  }

  static HtmlElementName get(int index) {
    return VALUES[index];
  }

  static boolean hasName(String name) {
    return Names.contains(name);
  }

  static int size() {
    return VALUES.length;
  }

  private static final class Names {

    private static final Set<String> NAMES = create();

    public static boolean contains(String name) {
      return NAMES.contains(name);
    }

    private static Set<String> create() {
      Set<String> names;
      names = Util.createSet();

      for (HtmlElementName value : VALUES) {
        names.add(value.lowerCase);
      }

      return names;
    }

  }

}