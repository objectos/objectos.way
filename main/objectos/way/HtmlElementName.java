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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

final class HtmlElementName implements Html.ElementName {

  private static final class Builder {

    private final List<HtmlElementName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public HtmlElementName[] buildValuesImpl() {
      return standardValues.toArray(HtmlElementName[]::new);
    }

    public final HtmlElementName createNormal(String name) {
      return create(name, true);
    }

    public final HtmlElementName createVoid(String name) {
      return create(name, false);
    }

    private HtmlElementName create(String name, boolean endTag) {
      HtmlElementName result;
      result = new HtmlElementName(index++, name, endTag);

      standardValues.add(result);

      return result;
    }

  }

  private static Builder BUILDER = new Builder();

  public static final Html.ElementName A = BUILDER.createNormal("a");
  public static final Html.ElementName ABBR = BUILDER.createNormal("abbr");
  public static final Html.ElementName ARTICLE = BUILDER.createNormal("article");
  public static final Html.ElementName B = BUILDER.createNormal("b");
  public static final Html.ElementName BLOCKQUOTE = BUILDER.createNormal("blockquote");
  public static final Html.ElementName BODY = BUILDER.createNormal("body");
  public static final Html.ElementName BR = BUILDER.createVoid("br");
  public static final Html.ElementName BUTTON = BUILDER.createNormal("button");
  public static final Html.ElementName CLIPPATH = BUILDER.createNormal("clipPath");
  public static final Html.ElementName CODE = BUILDER.createNormal("code");
  public static final Html.ElementName DD = BUILDER.createNormal("dd");
  public static final Html.ElementName DEFS = BUILDER.createNormal("defs");
  public static final Html.ElementName DETAILS = BUILDER.createNormal("details");
  public static final Html.ElementName DIV = BUILDER.createNormal("div");
  public static final Html.ElementName DL = BUILDER.createNormal("dl");
  public static final Html.ElementName DT = BUILDER.createNormal("dt");
  public static final Html.ElementName EM = BUILDER.createNormal("em");
  public static final Html.ElementName FIELDSET = BUILDER.createNormal("fieldset");
  public static final Html.ElementName FIGURE = BUILDER.createNormal("figure");
  public static final Html.ElementName FOOTER = BUILDER.createNormal("footer");
  public static final Html.ElementName FORM = BUILDER.createNormal("form");
  public static final Html.ElementName G = BUILDER.createNormal("g");
  public static final Html.ElementName H1 = BUILDER.createNormal("h1");
  public static final Html.ElementName H2 = BUILDER.createNormal("h2");
  public static final Html.ElementName H3 = BUILDER.createNormal("h3");
  public static final Html.ElementName H4 = BUILDER.createNormal("h4");
  public static final Html.ElementName H5 = BUILDER.createNormal("h5");
  public static final Html.ElementName H6 = BUILDER.createNormal("h6");
  public static final Html.ElementName HEAD = BUILDER.createNormal("head");
  public static final Html.ElementName HEADER = BUILDER.createNormal("header");
  public static final Html.ElementName HGROUP = BUILDER.createNormal("hgroup");
  public static final Html.ElementName HR = BUILDER.createVoid("hr");
  public static final Html.ElementName HTML = BUILDER.createNormal("html");
  public static final Html.ElementName IMG = BUILDER.createVoid("img");
  public static final Html.ElementName INPUT = BUILDER.createVoid("input");
  public static final Html.ElementName KBD = BUILDER.createNormal("kbd");
  public static final Html.ElementName LABEL = BUILDER.createNormal("label");
  public static final Html.ElementName LEGEND = BUILDER.createNormal("legend");
  public static final Html.ElementName LI = BUILDER.createNormal("li");
  public static final Html.ElementName LINK = BUILDER.createVoid("link");
  public static final Html.ElementName MAIN = BUILDER.createNormal("main");
  public static final Html.ElementName MENU = BUILDER.createNormal("menu");
  public static final Html.ElementName META = BUILDER.createVoid("meta");
  public static final Html.ElementName NAV = BUILDER.createNormal("nav");
  public static final Html.ElementName OL = BUILDER.createNormal("ol");
  public static final Html.ElementName OPTGROUP = BUILDER.createNormal("optgroup");
  public static final Html.ElementName OPTION = BUILDER.createNormal("option");
  public static final Html.ElementName P = BUILDER.createNormal("p");
  public static final Html.ElementName PATH = BUILDER.createNormal("path");
  public static final Html.ElementName PRE = BUILDER.createNormal("pre");
  public static final Html.ElementName PROGRESS = BUILDER.createNormal("progress");
  public static final Html.ElementName SAMP = BUILDER.createNormal("samp");
  public static final Html.ElementName SCRIPT = BUILDER.createNormal("script");
  public static final Html.ElementName SECTION = BUILDER.createNormal("section");
  public static final Html.ElementName SELECT = BUILDER.createNormal("select");
  public static final Html.ElementName SMALL = BUILDER.createNormal("small");
  public static final Html.ElementName SPAN = BUILDER.createNormal("span");
  public static final Html.ElementName STRONG = BUILDER.createNormal("strong");
  public static final Html.ElementName STYLE = BUILDER.createNormal("style");
  public static final Html.ElementName SUB = BUILDER.createNormal("sub");
  public static final Html.ElementName SUMMARY = BUILDER.createNormal("summary");
  public static final Html.ElementName SUP = BUILDER.createNormal("sup");
  public static final Html.ElementName SVG = BUILDER.createNormal("svg");
  public static final Html.ElementName TABLE = BUILDER.createNormal("table");
  public static final Html.ElementName TBODY = BUILDER.createNormal("tbody");
  public static final Html.ElementName TD = BUILDER.createNormal("td");
  public static final Html.ElementName TEMPLATE = BUILDER.createNormal("template");
  public static final Html.ElementName TEXTAREA = BUILDER.createNormal("textarea");
  public static final Html.ElementName TH = BUILDER.createNormal("th");
  public static final Html.ElementName THEAD = BUILDER.createNormal("thead");
  public static final Html.ElementName TITLE = BUILDER.createNormal("title");
  public static final Html.ElementName TR = BUILDER.createNormal("tr");
  public static final Html.ElementName UL = BUILDER.createNormal("ul");

  private static final HtmlElementName[] VALUES = create();

  private static HtmlElementName[] create() {
    HtmlElementName[] result;
    result = BUILDER.buildValuesImpl();

    BUILDER = null;

    return result;
  }

  private final int index;

  private final String name;

  private final boolean endTag;

  private HtmlElementName(int index, String name, boolean endTag) {
    this.index = index;
    this.name = name;
    this.endTag = endTag;
  }

  static HtmlElementName get(int index) {
    return VALUES[index];
  }

  static boolean hasName(String name) {
    return Names.contains(name);
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

  @Override
  public final boolean endTag() {
    return endTag;
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
        names.add(value.name);
      }

      return names;
    }

  }

}