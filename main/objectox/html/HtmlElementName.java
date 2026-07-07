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
package objectox.html;

import java.util.Set;
import objectos.internal.Util;
import objectos.way.Html;

public final class HtmlElementName implements Html.ElementName {

  public static final HtmlElementName A = new HtmlElementName(true, 0, "a");
  public static final HtmlElementName ABBR = new HtmlElementName(true, 1, "abbr");
  public static final HtmlElementName ARTICLE = new HtmlElementName(true, 2, "article");
  public static final HtmlElementName ASIDE = new HtmlElementName(true, 3, "aside");
  public static final HtmlElementName B = new HtmlElementName(true, 4, "b");
  public static final HtmlElementName BLOCKQUOTE = new HtmlElementName(true, 5, "blockquote");
  public static final HtmlElementName BODY = new HtmlElementName(true, 6, "body");
  public static final HtmlElementName BR = new HtmlElementName(false, 7, "br");
  public static final HtmlElementName BUTTON = new HtmlElementName(true, 8, "button");
  public static final HtmlElementName CLIPPATH = new HtmlElementName(true, 9, "clipPath");
  public static final HtmlElementName CODE = new HtmlElementName(true, 10, "code");
  public static final HtmlElementName DD = new HtmlElementName(true, 11, "dd");
  public static final HtmlElementName DEFS = new HtmlElementName(true, 12, "defs");
  public static final HtmlElementName DETAILS = new HtmlElementName(true, 13, "details");
  public static final HtmlElementName DIALOG = new HtmlElementName(true, 14, "dialog");
  public static final HtmlElementName DIV = new HtmlElementName(true, 15, "div");
  public static final HtmlElementName DL = new HtmlElementName(true, 16, "dl");
  public static final HtmlElementName DT = new HtmlElementName(true, 17, "dt");
  public static final HtmlElementName EM = new HtmlElementName(true, 18, "em");
  public static final HtmlElementName FIELDSET = new HtmlElementName(true, 19, "fieldset");
  public static final HtmlElementName FIGURE = new HtmlElementName(true, 20, "figure");
  public static final HtmlElementName FOOTER = new HtmlElementName(true, 21, "footer");
  public static final HtmlElementName FORM = new HtmlElementName(true, 22, "form");
  public static final HtmlElementName G = new HtmlElementName(true, 23, "g");
  public static final HtmlElementName H1 = new HtmlElementName(true, 24, "h1");
  public static final HtmlElementName H2 = new HtmlElementName(true, 25, "h2");
  public static final HtmlElementName H3 = new HtmlElementName(true, 26, "h3");
  public static final HtmlElementName H4 = new HtmlElementName(true, 27, "h4");
  public static final HtmlElementName H5 = new HtmlElementName(true, 28, "h5");
  public static final HtmlElementName H6 = new HtmlElementName(true, 29, "h6");
  public static final HtmlElementName HEAD = new HtmlElementName(true, 30, "head");
  public static final HtmlElementName HEADER = new HtmlElementName(true, 31, "header");
  public static final HtmlElementName HGROUP = new HtmlElementName(true, 32, "hgroup");
  public static final HtmlElementName HR = new HtmlElementName(false, 33, "hr");
  public static final HtmlElementName HTML = new HtmlElementName(true, 34, "html");
  public static final HtmlElementName IMG = new HtmlElementName(false, 35, "img");
  public static final HtmlElementName INPUT = new HtmlElementName(false, 36, "input");
  public static final HtmlElementName KBD = new HtmlElementName(true, 37, "kbd");
  public static final HtmlElementName LABEL = new HtmlElementName(true, 38, "label");
  public static final HtmlElementName LEGEND = new HtmlElementName(true, 39, "legend");
  public static final HtmlElementName LI = new HtmlElementName(true, 40, "li");
  public static final HtmlElementName LINK = new HtmlElementName(false, 41, "link");
  public static final HtmlElementName MAIN = new HtmlElementName(true, 42, "main");
  public static final HtmlElementName MENU = new HtmlElementName(true, 43, "menu");
  public static final HtmlElementName META = new HtmlElementName(false, 44, "meta");
  public static final HtmlElementName NAV = new HtmlElementName(true, 45, "nav");
  public static final HtmlElementName NOSCRIPT = new HtmlElementName(true, 46, "noscript");
  public static final HtmlElementName OL = new HtmlElementName(true, 47, "ol");
  public static final HtmlElementName OPTGROUP = new HtmlElementName(true, 48, "optgroup");
  public static final HtmlElementName OPTION = new HtmlElementName(true, 49, "option");
  public static final HtmlElementName P = new HtmlElementName(true, 50, "p");
  public static final HtmlElementName PATH = new HtmlElementName(true, 51, "path");
  public static final HtmlElementName PRE = new HtmlElementName(true, 52, "pre");
  public static final HtmlElementName PROGRESS = new HtmlElementName(true, 53, "progress");
  public static final HtmlElementName SAMP = new HtmlElementName(true, 54, "samp");
  public static final HtmlElementName SCRIPT = new HtmlElementName(true, 55, "script");
  public static final HtmlElementName SECTION = new HtmlElementName(true, 56, "section");
  public static final HtmlElementName SELECT = new HtmlElementName(true, 57, "select");
  public static final HtmlElementName SMALL = new HtmlElementName(true, 58, "small");
  public static final HtmlElementName SPAN = new HtmlElementName(true, 59, "span");
  public static final HtmlElementName STRONG = new HtmlElementName(true, 60, "strong");
  public static final HtmlElementName STYLE = new HtmlElementName(true, 61, "style");
  public static final HtmlElementName SUB = new HtmlElementName(true, 62, "sub");
  public static final HtmlElementName SUMMARY = new HtmlElementName(true, 63, "summary");
  public static final HtmlElementName SUP = new HtmlElementName(true, 64, "sup");
  public static final HtmlElementName SVG = new HtmlElementName(true, 65, "svg");
  public static final HtmlElementName TABLE = new HtmlElementName(true, 66, "table");
  public static final HtmlElementName TBODY = new HtmlElementName(true, 67, "tbody");
  public static final HtmlElementName TD = new HtmlElementName(true, 68, "td");
  public static final HtmlElementName TEMPLATE = new HtmlElementName(true, 69, "template");
  public static final HtmlElementName TEXTAREA = new HtmlElementName(true, 70, "textarea");
  public static final HtmlElementName TH = new HtmlElementName(true, 71, "th");
  public static final HtmlElementName THEAD = new HtmlElementName(true, 72, "thead");
  public static final HtmlElementName TITLE = new HtmlElementName(true, 73, "title");
  public static final HtmlElementName TR = new HtmlElementName(true, 74, "tr");
  public static final HtmlElementName UL = new HtmlElementName(true, 75, "ul");

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