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

public record ElementName(int index, String name) {

  private static final ElementNameUniverse U = new ElementNameUniverse();

  // BEGIN generated code

  public static final ElementName A = U.create("a");
  public static final ElementName ABBR = U.create("abbr");
  public static final ElementName ARTICLE = U.create("article");
  public static final ElementName ASIDE = U.create("aside");
  public static final ElementName B = U.create("b");
  public static final ElementName BLOCKQUOTE = U.create("blockquote");
  public static final ElementName BODY = U.create("body");
  public static final ElementName BR = U.create("br");
  public static final ElementName BUTTON = U.create("button");
  public static final ElementName CLIPPATH = U.create("clipPath");
  public static final ElementName CODE = U.create("code");
  public static final ElementName DD = U.create("dd");
  public static final ElementName DEFS = U.create("defs");
  public static final ElementName DETAILS = U.create("details");
  public static final ElementName DIALOG = U.create("dialog");
  public static final ElementName DIV = U.create("div");
  public static final ElementName DL = U.create("dl");
  public static final ElementName DT = U.create("dt");
  public static final ElementName EM = U.create("em");
  public static final ElementName FIELDSET = U.create("fieldset");
  public static final ElementName FIGURE = U.create("figure");
  public static final ElementName FOOTER = U.create("footer");
  public static final ElementName FORM = U.create("form");
  public static final ElementName G = U.create("g");
  public static final ElementName H1 = U.create("h1");
  public static final ElementName H2 = U.create("h2");
  public static final ElementName H3 = U.create("h3");
  public static final ElementName H4 = U.create("h4");
  public static final ElementName H5 = U.create("h5");
  public static final ElementName H6 = U.create("h6");
  public static final ElementName HEAD = U.create("head");
  public static final ElementName HEADER = U.create("header");
  public static final ElementName HGROUP = U.create("hgroup");
  public static final ElementName HR = U.create("hr");
  public static final ElementName HTML = U.create("html");
  public static final ElementName IMG = U.create("img");
  public static final ElementName INPUT = U.create("input");
  public static final ElementName KBD = U.create("kbd");
  public static final ElementName LABEL = U.create("label");
  public static final ElementName LEGEND = U.create("legend");
  public static final ElementName LI = U.create("li");
  public static final ElementName LINK = U.create("link");
  public static final ElementName MAIN = U.create("main");
  public static final ElementName MENU = U.create("menu");
  public static final ElementName META = U.create("meta");
  public static final ElementName NAV = U.create("nav");
  public static final ElementName NOSCRIPT = U.create("noscript");
  public static final ElementName OL = U.create("ol");
  public static final ElementName OPTGROUP = U.create("optgroup");
  public static final ElementName OPTION = U.create("option");
  public static final ElementName P = U.create("p");
  public static final ElementName PATH = U.create("path");
  public static final ElementName PRE = U.create("pre");
  public static final ElementName PROGRESS = U.create("progress");
  public static final ElementName SAMP = U.create("samp");
  public static final ElementName SCRIPT = U.create("script");
  public static final ElementName SECTION = U.create("section");
  public static final ElementName SELECT = U.create("select");
  public static final ElementName SMALL = U.create("small");
  public static final ElementName SPAN = U.create("span");
  public static final ElementName STRONG = U.create("strong");
  public static final ElementName STYLE = U.create("style");
  public static final ElementName SUB = U.create("sub");
  public static final ElementName SUMMARY = U.create("summary");
  public static final ElementName SUP = U.create("sup");
  public static final ElementName SVG = U.create("svg");
  public static final ElementName TABLE = U.create("table");
  public static final ElementName TBODY = U.create("tbody");
  public static final ElementName TD = U.create("td");
  public static final ElementName TEMPLATE = U.create("template");
  public static final ElementName TEXTAREA = U.create("textarea");
  public static final ElementName TH = U.create("th");
  public static final ElementName THEAD = U.create("thead");
  public static final ElementName TITLE = U.create("title");
  public static final ElementName TR = U.create("tr");
  public static final ElementName UL = U.create("ul");

  // END generated code

  public static ElementName getOrCreate(String name) {
    return U.getOrCreate(name);
  }

}