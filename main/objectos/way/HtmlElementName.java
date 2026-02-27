/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.util.Set;

final class HtmlElementName implements Html.ElementName {

  static final HtmlElementName A = new HtmlElementName(true, 0, "a");
  static final HtmlElementName ABBR = new HtmlElementName(true, 1, "abbr");
  static final HtmlElementName ARTICLE = new HtmlElementName(true, 2, "article");
  static final HtmlElementName ASIDE = new HtmlElementName(true, 3, "aside");
  static final HtmlElementName B = new HtmlElementName(true, 4, "b");
  static final HtmlElementName BLOCKQUOTE = new HtmlElementName(true, 5, "blockquote");
  static final HtmlElementName BODY = new HtmlElementName(true, 6, "body");
  static final HtmlElementName BR = new HtmlElementName(false, 7, "br");
  static final HtmlElementName BUTTON = new HtmlElementName(true, 8, "button");
  static final HtmlElementName CLIPPATH = new HtmlElementName(true, 9, "clipPath");
  static final HtmlElementName CODE = new HtmlElementName(true, 10, "code");
  static final HtmlElementName DD = new HtmlElementName(true, 11, "dd");
  static final HtmlElementName DEFS = new HtmlElementName(true, 12, "defs");
  static final HtmlElementName DETAILS = new HtmlElementName(true, 13, "details");
  static final HtmlElementName DIALOG = new HtmlElementName(true, 14, "dialog");
  static final HtmlElementName DIV = new HtmlElementName(true, 15, "div");
  static final HtmlElementName DL = new HtmlElementName(true, 16, "dl");
  static final HtmlElementName DT = new HtmlElementName(true, 17, "dt");
  static final HtmlElementName EM = new HtmlElementName(true, 18, "em");
  static final HtmlElementName FIELDSET = new HtmlElementName(true, 19, "fieldset");
  static final HtmlElementName FIGURE = new HtmlElementName(true, 20, "figure");
  static final HtmlElementName FOOTER = new HtmlElementName(true, 21, "footer");
  static final HtmlElementName FORM = new HtmlElementName(true, 22, "form");
  static final HtmlElementName G = new HtmlElementName(true, 23, "g");
  static final HtmlElementName H1 = new HtmlElementName(true, 24, "h1");
  static final HtmlElementName H2 = new HtmlElementName(true, 25, "h2");
  static final HtmlElementName H3 = new HtmlElementName(true, 26, "h3");
  static final HtmlElementName H4 = new HtmlElementName(true, 27, "h4");
  static final HtmlElementName H5 = new HtmlElementName(true, 28, "h5");
  static final HtmlElementName H6 = new HtmlElementName(true, 29, "h6");
  static final HtmlElementName HEAD = new HtmlElementName(true, 30, "head");
  static final HtmlElementName HEADER = new HtmlElementName(true, 31, "header");
  static final HtmlElementName HGROUP = new HtmlElementName(true, 32, "hgroup");
  static final HtmlElementName HR = new HtmlElementName(false, 33, "hr");
  static final HtmlElementName HTML = new HtmlElementName(true, 34, "html");
  static final HtmlElementName IMG = new HtmlElementName(false, 35, "img");
  static final HtmlElementName INPUT = new HtmlElementName(false, 36, "input");
  static final HtmlElementName KBD = new HtmlElementName(true, 37, "kbd");
  static final HtmlElementName LABEL = new HtmlElementName(true, 38, "label");
  static final HtmlElementName LEGEND = new HtmlElementName(true, 39, "legend");
  static final HtmlElementName LI = new HtmlElementName(true, 40, "li");
  static final HtmlElementName LINK = new HtmlElementName(false, 41, "link");
  static final HtmlElementName MAIN = new HtmlElementName(true, 42, "main");
  static final HtmlElementName MENU = new HtmlElementName(true, 43, "menu");
  static final HtmlElementName META = new HtmlElementName(false, 44, "meta");
  static final HtmlElementName NAV = new HtmlElementName(true, 45, "nav");
  static final HtmlElementName NOSCRIPT = new HtmlElementName(true, 46, "noscript");
  static final HtmlElementName OL = new HtmlElementName(true, 47, "ol");
  static final HtmlElementName OPTGROUP = new HtmlElementName(true, 48, "optgroup");
  static final HtmlElementName OPTION = new HtmlElementName(true, 49, "option");
  static final HtmlElementName P = new HtmlElementName(true, 50, "p");
  static final HtmlElementName PATH = new HtmlElementName(true, 51, "path");
  static final HtmlElementName PRE = new HtmlElementName(true, 52, "pre");
  static final HtmlElementName PROGRESS = new HtmlElementName(true, 53, "progress");
  static final HtmlElementName SAMP = new HtmlElementName(true, 54, "samp");
  static final HtmlElementName SCRIPT = new HtmlElementName(true, 55, "script");
  static final HtmlElementName SECTION = new HtmlElementName(true, 56, "section");
  static final HtmlElementName SELECT = new HtmlElementName(true, 57, "select");
  static final HtmlElementName SMALL = new HtmlElementName(true, 58, "small");
  static final HtmlElementName SPAN = new HtmlElementName(true, 59, "span");
  static final HtmlElementName STRONG = new HtmlElementName(true, 60, "strong");
  static final HtmlElementName STYLE = new HtmlElementName(true, 61, "style");
  static final HtmlElementName SUB = new HtmlElementName(true, 62, "sub");
  static final HtmlElementName SUMMARY = new HtmlElementName(true, 63, "summary");
  static final HtmlElementName SUP = new HtmlElementName(true, 64, "sup");
  static final HtmlElementName SVG = new HtmlElementName(true, 65, "svg");
  static final HtmlElementName TABLE = new HtmlElementName(true, 66, "table");
  static final HtmlElementName TBODY = new HtmlElementName(true, 67, "tbody");
  static final HtmlElementName TD = new HtmlElementName(true, 68, "td");
  static final HtmlElementName TEMPLATE = new HtmlElementName(true, 69, "template");
  static final HtmlElementName TEXTAREA = new HtmlElementName(true, 70, "textarea");
  static final HtmlElementName TH = new HtmlElementName(true, 71, "th");
  static final HtmlElementName THEAD = new HtmlElementName(true, 72, "thead");
  static final HtmlElementName TITLE = new HtmlElementName(true, 73, "title");
  static final HtmlElementName TR = new HtmlElementName(true, 74, "tr");
  static final HtmlElementName UL = new HtmlElementName(true, 75, "ul");

  private static final HtmlElementName[] VALUES = {
      A,
      ABBR,
      ARTICLE,
      ASIDE,
      B,
      BLOCKQUOTE,
      BODY,
      BR,
      BUTTON,
      CLIPPATH,
      CODE,
      DD,
      DEFS,
      DETAILS,
      DIALOG,
      DIV,
      DL,
      DT,
      EM,
      FIELDSET,
      FIGURE,
      FOOTER,
      FORM,
      G,
      H1,
      H2,
      H3,
      H4,
      H5,
      H6,
      HEAD,
      HEADER,
      HGROUP,
      HR,
      HTML,
      IMG,
      INPUT,
      KBD,
      LABEL,
      LEGEND,
      LI,
      LINK,
      MAIN,
      MENU,
      META,
      NAV,
      NOSCRIPT,
      OL,
      OPTGROUP,
      OPTION,
      P,
      PATH,
      PRE,
      PROGRESS,
      SAMP,
      SCRIPT,
      SECTION,
      SELECT,
      SMALL,
      SPAN,
      STRONG,
      STYLE,
      SUB,
      SUMMARY,
      SUP,
      SVG,
      TABLE,
      TBODY,
      TD,
      TEMPLATE,
      TEXTAREA,
      TH,
      THEAD,
      TITLE,
      TR,
      UL
  };

  private final boolean endTag;

  private final int index;

  private final String name;

  private HtmlElementName(boolean endTag, int index, String name) {
    this.endTag = endTag;

    this.index = index;

    this.name = name;
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

  public final boolean endTag() { return endTag; }

  public final int index() { return index; }

  public final String name() { return name; }

  private static final class Names {

    private static final Set<String> NAMES = create();

    public static boolean contains(String name) {
      return NAMES.contains(name);
    }

    private static Set<String> create() {
      Set<String> names;
      names = Util.createSet();

      for (HtmlElementName value : VALUES) {
        names.add(value.name);
      }

      return names;
    }

  }

}