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
package objectox.html.elem;

import java.util.Set;
import objectos.html.ElementName;
import objectos.internal.Util;

public final class ElementNamePojo implements ElementName {

  public static final ElementNamePojo A = new ElementNamePojo(true, 0, "a");
  public static final ElementNamePojo ABBR = new ElementNamePojo(true, 1, "abbr");
  public static final ElementNamePojo ARTICLE = new ElementNamePojo(true, 2, "article");
  public static final ElementNamePojo ASIDE = new ElementNamePojo(true, 3, "aside");
  public static final ElementNamePojo B = new ElementNamePojo(true, 4, "b");
  public static final ElementNamePojo BLOCKQUOTE = new ElementNamePojo(true, 5, "blockquote");
  public static final ElementNamePojo BODY = new ElementNamePojo(true, 6, "body");
  public static final ElementNamePojo BR = new ElementNamePojo(false, 7, "br");
  public static final ElementNamePojo BUTTON = new ElementNamePojo(true, 8, "button");
  public static final ElementNamePojo CLIPPATH = new ElementNamePojo(true, 9, "clipPath");
  public static final ElementNamePojo CODE = new ElementNamePojo(true, 10, "code");
  public static final ElementNamePojo DD = new ElementNamePojo(true, 11, "dd");
  public static final ElementNamePojo DEFS = new ElementNamePojo(true, 12, "defs");
  public static final ElementNamePojo DETAILS = new ElementNamePojo(true, 13, "details");
  public static final ElementNamePojo DIALOG = new ElementNamePojo(true, 14, "dialog");
  public static final ElementNamePojo DIV = new ElementNamePojo(true, 15, "div");
  public static final ElementNamePojo DL = new ElementNamePojo(true, 16, "dl");
  public static final ElementNamePojo DT = new ElementNamePojo(true, 17, "dt");
  public static final ElementNamePojo EM = new ElementNamePojo(true, 18, "em");
  public static final ElementNamePojo FIELDSET = new ElementNamePojo(true, 19, "fieldset");
  public static final ElementNamePojo FIGURE = new ElementNamePojo(true, 20, "figure");
  public static final ElementNamePojo FOOTER = new ElementNamePojo(true, 21, "footer");
  public static final ElementNamePojo FORM = new ElementNamePojo(true, 22, "form");
  public static final ElementNamePojo G = new ElementNamePojo(true, 23, "g");
  public static final ElementNamePojo H1 = new ElementNamePojo(true, 24, "h1");
  public static final ElementNamePojo H2 = new ElementNamePojo(true, 25, "h2");
  public static final ElementNamePojo H3 = new ElementNamePojo(true, 26, "h3");
  public static final ElementNamePojo H4 = new ElementNamePojo(true, 27, "h4");
  public static final ElementNamePojo H5 = new ElementNamePojo(true, 28, "h5");
  public static final ElementNamePojo H6 = new ElementNamePojo(true, 29, "h6");
  public static final ElementNamePojo HEAD = new ElementNamePojo(true, 30, "head");
  public static final ElementNamePojo HEADER = new ElementNamePojo(true, 31, "header");
  public static final ElementNamePojo HGROUP = new ElementNamePojo(true, 32, "hgroup");
  public static final ElementNamePojo HR = new ElementNamePojo(false, 33, "hr");
  public static final ElementNamePojo HTML = new ElementNamePojo(true, 34, "html");
  public static final ElementNamePojo IMG = new ElementNamePojo(false, 35, "img");
  public static final ElementNamePojo INPUT = new ElementNamePojo(false, 36, "input");
  public static final ElementNamePojo KBD = new ElementNamePojo(true, 37, "kbd");
  public static final ElementNamePojo LABEL = new ElementNamePojo(true, 38, "label");
  public static final ElementNamePojo LEGEND = new ElementNamePojo(true, 39, "legend");
  public static final ElementNamePojo LI = new ElementNamePojo(true, 40, "li");
  public static final ElementNamePojo LINK = new ElementNamePojo(false, 41, "link");
  public static final ElementNamePojo MAIN = new ElementNamePojo(true, 42, "main");
  public static final ElementNamePojo MENU = new ElementNamePojo(true, 43, "menu");
  public static final ElementNamePojo META = new ElementNamePojo(false, 44, "meta");
  public static final ElementNamePojo NAV = new ElementNamePojo(true, 45, "nav");
  public static final ElementNamePojo NOSCRIPT = new ElementNamePojo(true, 46, "noscript");
  public static final ElementNamePojo OL = new ElementNamePojo(true, 47, "ol");
  public static final ElementNamePojo OPTGROUP = new ElementNamePojo(true, 48, "optgroup");
  public static final ElementNamePojo OPTION = new ElementNamePojo(true, 49, "option");
  public static final ElementNamePojo P = new ElementNamePojo(true, 50, "p");
  public static final ElementNamePojo PATH = new ElementNamePojo(true, 51, "path");
  public static final ElementNamePojo PRE = new ElementNamePojo(true, 52, "pre");
  public static final ElementNamePojo PROGRESS = new ElementNamePojo(true, 53, "progress");
  public static final ElementNamePojo SAMP = new ElementNamePojo(true, 54, "samp");
  public static final ElementNamePojo SCRIPT = new ElementNamePojo(true, 55, "script");
  public static final ElementNamePojo SECTION = new ElementNamePojo(true, 56, "section");
  public static final ElementNamePojo SELECT = new ElementNamePojo(true, 57, "select");
  public static final ElementNamePojo SMALL = new ElementNamePojo(true, 58, "small");
  public static final ElementNamePojo SPAN = new ElementNamePojo(true, 59, "span");
  public static final ElementNamePojo STRONG = new ElementNamePojo(true, 60, "strong");
  public static final ElementNamePojo STYLE = new ElementNamePojo(true, 61, "style");
  public static final ElementNamePojo SUB = new ElementNamePojo(true, 62, "sub");
  public static final ElementNamePojo SUMMARY = new ElementNamePojo(true, 63, "summary");
  public static final ElementNamePojo SUP = new ElementNamePojo(true, 64, "sup");
  public static final ElementNamePojo SVG = new ElementNamePojo(true, 65, "svg");
  public static final ElementNamePojo TABLE = new ElementNamePojo(true, 66, "table");
  public static final ElementNamePojo TBODY = new ElementNamePojo(true, 67, "tbody");
  public static final ElementNamePojo TD = new ElementNamePojo(true, 68, "td");
  public static final ElementNamePojo TEMPLATE = new ElementNamePojo(true, 69, "template");
  public static final ElementNamePojo TEXTAREA = new ElementNamePojo(true, 70, "textarea");
  public static final ElementNamePojo TH = new ElementNamePojo(true, 71, "th");
  public static final ElementNamePojo THEAD = new ElementNamePojo(true, 72, "thead");
  public static final ElementNamePojo TITLE = new ElementNamePojo(true, 73, "title");
  public static final ElementNamePojo TR = new ElementNamePojo(true, 74, "tr");
  public static final ElementNamePojo UL = new ElementNamePojo(true, 75, "ul");

  private static final ElementNamePojo[] VALUES = {
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

  private ElementNamePojo(boolean endTag, int index, String name) {
    this.endTag = endTag;

    this.index = index;

    this.name = name;
  }

  public static ElementNamePojo get(int index) {
    return VALUES[index];
  }

  static boolean hasName(String name) {
    return Names.contains(name);
  }

  public static int size() {
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

      for (ElementNamePojo value : VALUES) {
        names.add(value.name);
      }

      return names;
    }

  }

}