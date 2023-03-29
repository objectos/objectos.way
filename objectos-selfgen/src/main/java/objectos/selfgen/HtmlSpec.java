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
package objectos.selfgen;

import java.io.IOException;
import java.util.Set;
import objectos.selfgen.html.CategorySpec;
import objectos.selfgen.html.HtmlSelfGen;
import objectos.util.UnmodifiableSet;

public final class HtmlSpec extends HtmlSelfGen {

  private HtmlSpec() {}

  public static void main(String[] args) throws IOException {
    var spec = new HtmlSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    template()
        .skipAttribute("clipPath")
        .skipAttribute("label")
        .skipAttribute("title")
        .skipText("form");

    rootElement()
        .attribute("accesskey")
        .attribute("class").as("className").classNameType()
        .attribute("contenteditable")
        .attribute("dir")
        .attribute("draggable")
        .attribute("hidden").booleanType()
        .attribute("id").idType()
        .attribute("lang")
        .attribute("spellcheck")
        .attribute("style").as("inlineStyle")
        .attribute("tabindex")
        .attribute("title")
        .attribute("translate")

        .attribute("onclick")
        .attribute("onsubmit")

        .attribute("aria-hidden")
        .attribute("role");

    CategorySpec embedded = category("embedded");
    CategorySpec flow = category("flow");
    CategorySpec formAssociated = category("form-associated");
    CategorySpec heading = category("heading");
    CategorySpec interactive = category("interactive");
    CategorySpec metadata = category("metadata");
    CategorySpec palpable = category("palpable");
    CategorySpec phrasing = category("phrasing");
    CategorySpec sectioning = category("sectioning");

    text()
        .category(flow)
        .category(phrasing)
        .category(palpable);

    element("html")
        .one(el("head"))
        .one(el("body"));

    // 4.2 Document metadata

    element("head")
        .contentModel(metadata);

    element("title")
        .category(metadata)
        .one(text());

    element("link")
        .category(metadata)
        .attribute("href")
        .attribute("crossorigin")
        .attribute("rel")
        .attribute("rev")
        .attribute("media")
        .attribute("type")
        .attribute("referrerpolicy")
        .attribute("sizes")
        .noEndTag();

    element("meta")
        .category(metadata)
        .attribute("name")
        .attribute("http-equiv")
        .attribute("content")
        .attribute("charset")
        .attribute("property")
        .noEndTag();

    // <style> is not a flow stricly speaking, but according to spec:
    // "Contexts in which this element can be used:
    // (...)
    // In the body, where flow content is expected."
    element("style")
        .category(flow)
        .category(metadata)
        .attribute("type")
        .one(text());

    // 4.3 Sections

    element("body")
        .contentModel(flow)
        .attribute("onafterprint")
        .attribute("onbeforeprint")
        .attribute("onbeforeunload")
        .attribute("onhashchange")
        .attribute("onlanguagechange")
        .attribute("onmessage")
        .attribute("onoffline")
        .attribute("ononline")
        .attribute("onpagehide")
        .attribute("onpageshow")
        .attribute("onpopstate")
        .attribute("onrejectionhandled")
        .attribute("onstorage")
        .attribute("onunhandledrejection")
        .attribute("onunload")
        .attributeEnd();

    element("article")
        .category(flow)
        .category(sectioning)
        .category(palpable)
        .contentModel(flow);

    element("section")
        .category(flow)
        .category(sectioning)
        .category(palpable)
        .contentModel(flow);

    element("nav")
        .category(flow)
        .category(sectioning)
        .category(palpable)
        .contentModel(flow);

    for (int i = 1; i <= 6; i++) {
      element("h" + i)
          .simpleName("Heading" + i)
          .category(flow)
          .category(heading)
          .category(palpable)
          .one(text());
    }

    element("hgroup")
        .simpleName("HeadingGroup")
        .category(flow)
        .category(heading)
        .category(palpable)
        .zeroOrMore(el("h1"))
        .zeroOrMore(el("h2"))
        .zeroOrMore(el("h3"))
        .zeroOrMore(el("h4"))
        .zeroOrMore(el("h5"))
        .zeroOrMore(el("h6"));

    element("header")
        .category(flow)
        .category(palpable)
        .contentModel(flow);

    element("footer")
        .category(flow)
        .category(palpable)
        .contentModel(flow);

    // 4.4 Grouping content

    element("p")
        .simpleName("Paragraph")
        .category(flow)
        .category(palpable)
        .one(text());

    element("hr")
        .simpleName("HorizontalRule")
        .category(flow)
        .noEndTag();

    element("pre")
        .category(flow)
        .category(palpable)
        .contentModel(phrasing);

    element("blockquote")
        .category(flow)
        .category(sectioning)
        .category(palpable)
        .attribute("cite")
        .attributeEnd();

    element("ol")
        .simpleName("OrderedList")
        .category(flow)
        .category(palpable)
        .zeroOrMore(el("li"))
        .attribute("reversed").booleanType()
        .attribute("start")
        .attribute("type")
        .attributeEnd();

    element("ul")
        .simpleName("UnorderedList")
        .category(flow)
        .category(palpable)
        .zeroOrMore(el("li"));

    element("menu")
        .category(flow)
        .category(palpable)
        .zeroOrMore(el("li"));

    element("dl")
        .simpleName("DefinitionList")
        .category(flow)
        .zeroOrMore(el("dt"));

    element("dt")
        .simpleName("DefinitionTerm")
        .contentModel(flow);

    element("dd")
        .simpleName("DefinitionDescription")
        .contentModel(flow);

    element("figure")
        .category(flow)
        .category(sectioning)
        .category(palpable);

    element("li")
        .simpleName("ListItem")
        .contentModel(flow);

    element("main")
        .category(flow)
        .category(palpable)
        .contentModel(flow);

    element("div")
        .category(flow)
        .category(palpable)
        .contentModel(flow);

    // 4.5 Text-level semantics

    // TODO: <a> transparent
    element("a")
        .simpleName("Anchor")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        .category(palpable)
        .attribute("href")
        .attribute("target")
        .one(text());

    element("em")
        .simpleName("Emphasis")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("strong")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("small")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("abbr")
        .simpleName("Abbreviation")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("code")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("samp")
        .simpleName("SampleOutput")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("kbd")
        .simpleName("KeyboardInput")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("span")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("br")
        .simpleName("LineBreak")
        .category(flow)
        .category(phrasing)
        .noEndTag();

    element("sub")
        .simpleName("Subscript")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("sup")
        .simpleName("Superscript")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    element("b")
        .simpleName("BringAttentionTo")
        .category(flow)
        .category(phrasing)
        .category(palpable)
        .contentModel(phrasing);

    // 4.7 Embedded content

    element("img")
        .simpleName("Image")
        .category(flow)
        .category(phrasing)
        .category(embedded)
        .category(formAssociated)
        .category(interactive)
        .category(palpable)
        .attribute("alt")
        .attribute("src")
        .attribute("srcset")
        .attribute("width")
        .attribute("height")
        .noEndTag();

    Set<String> svgPresentationProps;
    svgPresentationProps = UnmodifiableSet.of(
      "alignment-baseline",
      "baseline-shift",
      "clip-path",
      "clip-rule",
      "color",
      "color-interpolation",
      "color-interpolation-filters",
      "cursor",
      "direction",
      "display",
      "dominant-baseline",
      "fill",
      "fill-opacity",
      "fill-rule",
      "filter",
      "flood-color",
      "flood-opacity",
      "font-family",
      "font-size",
      "font-size-adjust",
      "font-stretch",
      "font-style",
      "font-variant",
      "font-weight",
      "glyph-orientation-horizontal",
      "glyph-orientation-vertical",
      "image-rendering",
      "letter-spacing",
      "lighting-color",
      "marker-end",
      "marker-mid",
      "marker-start",
      "mask",
      "mask-type",
      "opacity",
      "overflow",
      "paint-order",
      "pointer-events",
      "shape-rendering",
      "stop-color",
      "stop-opacity",
      "stroke",
      "stroke-dasharray",
      "stroke-dashoffset",
      "stroke-linecap",
      "stroke-linejoin",
      "stroke-miterlimit",
      "stroke-opacity",
      "stroke-width",
      "text-anchor",
      "text-decoration",
      "text-overflow",
      "text-rendering",
      "transform-origin",
      "unicode-bidi",
      "vector-effect",
      "visibility",
      "white-space",
      "word-spacing",
      "writing-mode"
    );

    element("svg")
        .category(flow)
        .category(phrasing)
        .category(embedded)
        .category(palpable)

        .attributes(svgPresentationProps)
        .attribute("height")
        .attribute("transform")
        .attribute("viewBox")
        .attribute("xmlns")
        .attribute("width")
        .attributeEnd()
        .zeroOrMore(el("g"))
        .zeroOrMore(el("path"));

    element("clipPath")
        .attributes(svgPresentationProps)
        .attribute("d")
        .attribute("transform")
        .attributeEnd();

    element("defs")
        .attributes(svgPresentationProps)
        .attributeEnd()
        .zeroOrMore(el("clipPath"));

    element("g")
        .attributes(svgPresentationProps)
        .attribute("transform")
        .zeroOrMore(el("g"))
        .zeroOrMore(el("path"));

    element("path")
        .attributes(svgPresentationProps)
        .attribute("d")
        .attribute("transform")
        .attributeEnd();

    // 4.9 Tabular data

    element("table")
        .category(flow)
        .category(palpable)
        .attribute("align")
        .attribute("border")
        .attribute("cellpadding")
        .attribute("cellspacing")
        .attribute("width")
        .one(el("thead"))
        .zeroOrMore(el("tbody"));

    element("thead")
        .simpleName("TableHead")
        .zeroOrMore(el("tr"));

    element("tbody")
        .simpleName("TableBody")
        .zeroOrMore(el("tr"));

    element("tr")
        .simpleName("TableRow")
        .zeroOrMore(el("th"))
        .zeroOrMore(el("td"));

    element("th")
        .simpleName("TableHeader")
        .one(text());

    element("td")
        .simpleName("TableData")
        .zeroOrMore(text())
        .zeroOrMore(el("a"));

    // 4.10 Forms

    element("form")
        .category(flow)
        .category(palpable)
        .contentModel(flow)
        /**/.except(el("form"))
        .attribute("action")
        .attribute("enctype")
        .attribute("method")
        .attribute("name")
        .attribute("target")
        .attributeEnd();

    element("label")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        .category(formAssociated)
        .category(palpable)
        .contentModel(phrasing)
        /**/.except(el("label"))
        .attribute("for").as("forAttr", "forElement")
        .attributeEnd();

    element("input")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        .category(formAssociated)
        .category(palpable)
        .attribute("autofocus").booleanType()
        .attribute("name")
        .attribute("placeholder")
        .attribute("readonly").booleanType()
        .attribute("required").booleanType()
        .attribute("type")
        .attribute("value")
        .noEndTag();

    element("button")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        .category(formAssociated)
        .category(palpable)
        .attribute("type")
        .one(text());

    element("select")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        // Listed, labelable, submittable, resettable, and
        // autocapitalize-inheriting form-associated element.
        .category(palpable)
        .attribute("autocomplete")
        .attribute("disabled").booleanType()
        .attribute("form")
        .attribute("multiple").booleanType()
        .attribute("name")
        .attribute("required").booleanType()
        .attribute("size")
        .zeroOrMore(el("option"))
        .zeroOrMore(el("optgroup"));

    element("optgroup")
        .simpleName("OptionGroup")
        .zeroOrMore(el("option"));

    element("option")
        .attribute("disabled").booleanType()
        .attribute("label")
        .attribute("selected").booleanType()
        .attribute("value")
        .one(text());

    element("textarea")
        .simpleName("TextArea")
        .category(flow)
        .category(phrasing)
        .category(interactive)
        // Listed, labelable, submittable, resettable, and
        // autocapitalize-inheriting form-associated element.
        .category(palpable)
        .attribute("autocomplete")
        .attribute("cols")
        .attribute("dirname")
        .attribute("disabled").booleanType()
        .attribute("form")
        .attribute("maxlength")
        .attribute("minlength")
        .attribute("name")
        .attribute("placeholder")
        .attribute("readonly").booleanType()
        .attribute("required").booleanType()
        .attribute("rows")
        .attribute("wrap")
        .one(text());

    element("progress")
        .category(flow)
        .category(phrasing)
        // .category(labelable)
        .category(palpable)
        .contentModel(phrasing);

    element("fieldset")
        .category(flow)
        .category(sectioning)
        .category(palpable)
        .contentModel(flow);

    element("legend")
        .contentModel(phrasing);

    // 4.11 Interactive elements

    element("details")
        .category(flow)
        .category(sectioning)
        .category(interactive)
        .category(palpable)
        .attribute("open").booleanType()
        .contentModel(flow);

    element("summary")
        .contentModel(phrasing);

    // 4.12 Scripting

    element("script")
        .category(flow)
        .category(metadata)
        .category(phrasing)
        .attribute("src")
        .attribute("type")
        .attribute("nomodule").booleanType()
        .attribute("async").booleanType()
        .attribute("defer").booleanType()
        .attribute("crossorigin")
        .attribute("integrity")
        .attribute("referrerpolicy")
        .one(text());

    element("template")
        .category(metadata)
        .category(flow)
        .category(phrasing);
  }

}