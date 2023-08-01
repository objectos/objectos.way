/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.om.TypeSelector;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public enum StandardTypeSelector implements TypeSelector {
  a("a"),

  article("article"),

  audio("audio"),

  b("b"),

  blockquote("blockquote"),

  body("body"),

  canvas("canvas"),

  code("code"),

  dd("dd"),

  div("div"),

  dl("dl"),

  embed("embed"),

  fieldset("fieldset"),

  figure("figure"),

  form("form"),

  h1("h1"),

  h2("h2"),

  h3("h3"),

  h4("h4"),

  h5("h5"),

  h6("h6"),

  header("header"),

  hr("hr"),

  html("html"),

  iframe("iframe"),

  img("img"),

  input("input"),

  kbd("kbd"),

  label("label"),

  legend("legend"),

  li("li"),

  nav("nav"),

  object("object"),

  ol("ol"),

  optgroup("optgroup"),

  p("p"),

  pre("pre"),

  samp("samp"),

  section("section"),

  select("select"),

  strong("strong"),

  summary("summary"),

  sup("sup"),

  svg("svg"),

  tbody("tbody"),

  td("td"),

  th("th"),

  thead("thead"),

  tr("tr"),

  ul("ul"),

  video("video");

  private static final StandardTypeSelector[] VALUES = values();

  public final String cssName;

  private StandardTypeSelector(String cssName) {
    this.cssName = cssName;
  }

  public static StandardTypeSelector ofOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  @Override
  public final String toString() {
    return cssName;
  }
}
