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

import java.util.List;

final class HtmlSpec {

  record ElementSpec(String javaName, String htmlName, boolean endTag) {}

  private HtmlSpec() {}

  static List<ElementSpec> elements() {
    return List.of(
        new ElementSpec("A", "a", true),
        new ElementSpec("ABBR", "abbr", true),
        new ElementSpec("ARTICLE", "article", true),
        new ElementSpec("ASIDE", "aside", true),
        new ElementSpec("B", "b", true),
        new ElementSpec("BLOCKQUOTE", "blockquote", true),
        new ElementSpec("BODY", "body", true),
        new ElementSpec("BR", "br", false),
        new ElementSpec("BUTTON", "button", true),
        new ElementSpec("CLIPPATH", "clipPath", true),
        new ElementSpec("CODE", "code", true),
        new ElementSpec("DD", "dd", true),
        new ElementSpec("DEFS", "defs", true),
        new ElementSpec("DETAILS", "details", true),
        new ElementSpec("DIALOG", "dialog", true),
        new ElementSpec("DIV", "div", true),
        new ElementSpec("DL", "dl", true),
        new ElementSpec("DT", "dt", true),
        new ElementSpec("EM", "em", true),
        new ElementSpec("FIELDSET", "fieldset", true),
        new ElementSpec("FIGURE", "figure", true),
        new ElementSpec("FOOTER", "footer", true),
        new ElementSpec("FORM", "form", true),
        new ElementSpec("G", "g", true),
        new ElementSpec("H1", "h1", true),
        new ElementSpec("H2", "h2", true),
        new ElementSpec("H3", "h3", true),
        new ElementSpec("H4", "h4", true),
        new ElementSpec("H5", "h5", true),
        new ElementSpec("H6", "h6", true),
        new ElementSpec("HEAD", "head", true),
        new ElementSpec("HEADER", "header", true),
        new ElementSpec("HGROUP", "hgroup", true),
        new ElementSpec("HR", "hr", false),
        new ElementSpec("HTML", "html", true),
        new ElementSpec("IMG", "img", false),
        new ElementSpec("INPUT", "input", false),
        new ElementSpec("KBD", "kbd", true),
        new ElementSpec("LABEL", "label", true),
        new ElementSpec("LEGEND", "legend", true),
        new ElementSpec("LI", "li", true),
        new ElementSpec("LINK", "link", false),
        new ElementSpec("MAIN", "main", true),
        new ElementSpec("MENU", "menu", true),
        new ElementSpec("META", "meta", false),
        new ElementSpec("NAV", "nav", true),
        new ElementSpec("OL", "ol", true),
        new ElementSpec("OPTGROUP", "optgroup", true),
        new ElementSpec("OPTION", "option", true),
        new ElementSpec("P", "p", true),
        new ElementSpec("PATH", "path", true),
        new ElementSpec("PRE", "pre", true),
        new ElementSpec("PROGRESS", "progress", true),
        new ElementSpec("SAMP", "samp", true),
        new ElementSpec("SCRIPT", "script", true),
        new ElementSpec("SECTION", "section", true),
        new ElementSpec("SELECT", "select", true),
        new ElementSpec("SMALL", "small", true),
        new ElementSpec("SPAN", "span", true),
        new ElementSpec("STRONG", "strong", true),
        new ElementSpec("STYLE", "style", true),
        new ElementSpec("SUB", "sub", true),
        new ElementSpec("SUMMARY", "summary", true),
        new ElementSpec("SUP", "sup", true),
        new ElementSpec("SVG", "svg", true),
        new ElementSpec("TABLE", "table", true),
        new ElementSpec("TBODY", "tbody", true),
        new ElementSpec("TD", "td", true),
        new ElementSpec("TEMPLATE", "template", true),
        new ElementSpec("TEXTAREA", "textarea", true),
        new ElementSpec("TH", "th", true),
        new ElementSpec("THEAD", "thead", true),
        new ElementSpec("TITLE", "title", true),
        new ElementSpec("TR", "tr", true),
        new ElementSpec("UL", "ul", true)
    );
  }

}
