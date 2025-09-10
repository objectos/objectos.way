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
import java.util.Set;
import java.util.stream.Collectors;

final class HtmlSpec {

  record AmbiguousSpec(String constantName, String methodName, String attributeName, String elementName) {}

  record AttributeSpec(String constantName, String methodName, String htmlName, boolean booleanAttribute)
      implements Comparable<AttributeSpec> {
    @Override
    public int compareTo(AttributeSpec that) {
      return constantName.compareTo(that.constantName);
    }
  }

  record ElementSpec(String javaName, String htmlName, boolean endTag)
      implements Comparable<ElementSpec> {
    @Override
    public int compareTo(ElementSpec that) {
      return javaName.compareTo(that.javaName);
    }
  }

  private HtmlSpec() {}

  static List<AmbiguousSpec> ambiguous() {
    return List.of(
        new AmbiguousSpec("CLIPPATH", "clipPath", "clip-path", "clipPath"),
        new AmbiguousSpec("FORM", "form", "form", "form"),
        new AmbiguousSpec("LABEL", "label", "label", "label"),
        new AmbiguousSpec("TITLE", "title", "title", "title")
    );
  }

  static Set<String> ambiguousAttrNames() {
    return ambiguous().stream().map(AmbiguousSpec::attributeName).collect(Collectors.toUnmodifiableSet());
  }

  static Set<String> ambiguousElemNames() {
    return ambiguous().stream().map(AmbiguousSpec::elementName).collect(Collectors.toUnmodifiableSet());
  }

  static List<AttributeSpec> attributes() {
    return attributes0().stream().sorted().toList();
  }

  static List<AttributeSpec> attributes0() {
    return List.of(
        new AttributeSpec("ACCESSKEY", "accesskey", "accesskey", false),
        new AttributeSpec("ACTION", "action", "action", false),
        new AttributeSpec("ALIGN", "align", "align", false),
        new AttributeSpec("ALIGNMENT_BASELINE", "alignmentBaseline", "alignment-baseline", false),
        new AttributeSpec("ALT", "alt", "alt", false),
        new AttributeSpec("ARIA_HIDDEN", "ariaHidden", "aria-hidden", false),
        new AttributeSpec("ARIA_LABEL", "ariaLabel", "aria-label", false),
        new AttributeSpec("AS", "as", "as", false),
        new AttributeSpec("ASYNC", "async", "async", true),
        new AttributeSpec("AUTOCOMPLETE", "autocomplete", "autocomplete", false),
        new AttributeSpec("AUTOFOCUS", "autofocus", "autofocus", true),
        new AttributeSpec("BASELINE_SHIFT", "baselineShift", "baseline-shift", false),
        new AttributeSpec("BORDER", "border", "border", false),
        new AttributeSpec("CELLPADDING", "cellpadding", "cellpadding", false),
        new AttributeSpec("CELLSPACING", "cellspacing", "cellspacing", false),
        new AttributeSpec("CHARSET", "charset", "charset", false),
        new AttributeSpec("CHECKED", "checked", "checked", true),
        new AttributeSpec("CITE", "cite", "cite", false),
        new AttributeSpec("CLASS", "class", "class", false),
        new AttributeSpec("CLIP_PATH", "clipPath", "clip-path", false),
        new AttributeSpec("CLIP_RULE", "clipRule", "clip-rule", false),
        new AttributeSpec("CLOSEDBY", "closedby", "closedby", false),
        new AttributeSpec("COLOR", "color", "color", false),
        new AttributeSpec("COLOR_INTERPOLATION", "colorInterpolation", "color-interpolation", false),
        new AttributeSpec("COLOR_INTERPOLATION_FILTERS", "colorInterpolationFilters", "color-interpolation-filters", false),
        new AttributeSpec("COLS", "cols", "cols", false),
        new AttributeSpec("CONTENT", "content", "content", false),
        new AttributeSpec("CONTENTEDITABLE", "contenteditable", "contenteditable", false),
        new AttributeSpec("CROSSORIGIN", "crossorigin", "crossorigin", false),
        new AttributeSpec("CURSOR", "cursor", "cursor", false),
        new AttributeSpec("D", "d", "d", false),
        new AttributeSpec("DEFER", "defer", "defer", true),
        new AttributeSpec("DIR", "dir", "dir", false),
        new AttributeSpec("DIRECTION", "direction", "direction", false),
        new AttributeSpec("DIRNAME", "dirname", "dirname", false),
        new AttributeSpec("DISABLED", "disabled", "disabled", true),
        new AttributeSpec("DISPLAY", "display", "display", false),
        new AttributeSpec("DOMINANT_BASELINE", "dominantBaseline", "dominant-baseline", false),
        new AttributeSpec("DRAGGABLE", "draggable", "draggable", false),
        new AttributeSpec("ENCTYPE", "enctype", "enctype", false),
        new AttributeSpec("FILL", "fill", "fill", false),
        new AttributeSpec("FILL_OPACITY", "fillOpacity", "fill-opacity", false),
        new AttributeSpec("FILL_RULE", "fillRule", "fill-rule", false),
        new AttributeSpec("FILTER", "filter", "filter", false),
        new AttributeSpec("FLOOD_COLOR", "floodColor", "flood-color", false),
        new AttributeSpec("FLOOD_OPACITY", "floodOpacity", "flood-opacity", false),
        new AttributeSpec("FOR", "for", "for", false),
        new AttributeSpec("FORM", "form", "form", false),
        new AttributeSpec("GLYPH_ORIENTATION_HORIZONTAL", "glyphOrientationHorizontal", "glyph-orientation-horizontal", false),
        new AttributeSpec("GLYPH_ORIENTATION_VERTICAL", "glyphOrientationVertical", "glyph-orientation-vertical", false),
        new AttributeSpec("HEIGHT", "height", "height", false),
        new AttributeSpec("HIDDEN", "hidden", "hidden", true),
        new AttributeSpec("HREF", "href", "href", false),
        new AttributeSpec("HTTP_EQUIV", "httpEquiv", "http-equiv", false),
        new AttributeSpec("ID", "id", "id", false),
        new AttributeSpec("IMAGE_RENDERING", "imageRendering", "image-rendering", false),
        new AttributeSpec("INTEGRITY", "integrity", "integrity", false),
        new AttributeSpec("LABEL", "label", "label", false),
        new AttributeSpec("LANG", "lang", "lang", false),
        new AttributeSpec("LETTER_SPACING", "letterSpacing", "letter-spacing", false),
        new AttributeSpec("LIGHTING_COLOR", "lightingColor", "lighting-color", false),
        new AttributeSpec("MARKER_END", "markerEnd", "marker-end", false),
        new AttributeSpec("MARKER_MID", "markerMid", "marker-mid", false),
        new AttributeSpec("MARKER_START", "markerStart", "marker-start", false),
        new AttributeSpec("MASK", "mask", "mask", false),
        new AttributeSpec("MASK_TYPE", "maskType", "mask-type", false),
        new AttributeSpec("MAXLENGTH", "maxlength", "maxlength", false),
        new AttributeSpec("MEDIA", "media", "media", false),
        new AttributeSpec("METHOD", "method", "method", false),
        new AttributeSpec("MINLENGTH", "minlength", "minlength", false),
        new AttributeSpec("MULTIPLE", "multiple", "multiple", true),
        new AttributeSpec("NAME", "name", "name", false),
        new AttributeSpec("NOMODULE", "nomodule", "nomodule", true),
        new AttributeSpec("ONAFTERPRINT", "onafterprint", "onafterprint", false),
        new AttributeSpec("ONBEFOREPRINT", "onbeforeprint", "onbeforeprint", false),
        new AttributeSpec("ONBEFOREUNLOAD", "onbeforeunload", "onbeforeunload", false),
        new AttributeSpec("ONCLICK", "onclick", "onclick", false),
        new AttributeSpec("ONHASHCHANGE", "onhashchange", "onhashchange", false),
        new AttributeSpec("ONLANGUAGECHANGE", "onlanguagechange", "onlanguagechange", false),
        new AttributeSpec("ONMESSAGE", "onmessage", "onmessage", false),
        new AttributeSpec("ONOFFLINE", "onoffline", "onoffline", false),
        new AttributeSpec("ONONLINE", "ononline", "ononline", false),
        new AttributeSpec("ONPAGEHIDE", "onpagehide", "onpagehide", false),
        new AttributeSpec("ONPAGESHOW", "onpageshow", "onpageshow", false),
        new AttributeSpec("ONPOPSTATE", "onpopstate", "onpopstate", false),
        new AttributeSpec("ONREJECTIONHANDLED", "onrejectionhandled", "onrejectionhandled", false),
        new AttributeSpec("ONSTORAGE", "onstorage", "onstorage", false),
        new AttributeSpec("ONSUBMIT", "onsubmit", "onsubmit", false),
        new AttributeSpec("ONUNHANDLEDREJECTION", "onunhandledrejection", "onunhandledrejection", false),
        new AttributeSpec("ONUNLOAD", "onunload", "onunload", false),
        new AttributeSpec("OPACITY", "opacity", "opacity", false),
        new AttributeSpec("OPEN", "open", "open", true),
        new AttributeSpec("OVERFLOW", "overflow", "overflow", false),
        new AttributeSpec("PAINT_ORDER", "paintOrder", "paint-order", false),
        new AttributeSpec("PLACEHOLDER", "placeholder", "placeholder", false),
        new AttributeSpec("POINTER_EVENTS", "pointerEvents", "pointer-events", false),
        new AttributeSpec("PROPERTY", "property", "property", false),
        new AttributeSpec("READONLY", "readonly", "readonly", true),
        new AttributeSpec("REFERRERPOLICY", "referrerpolicy", "referrerpolicy", false),
        new AttributeSpec("REL", "rel", "rel", false),
        new AttributeSpec("REQUIRED", "required", "required", true),
        new AttributeSpec("REV", "rev", "rev", false),
        new AttributeSpec("REVERSED", "reversed", "reversed", true),
        new AttributeSpec("ROLE", "role", "role", false),
        new AttributeSpec("ROWS", "rows", "rows", false),
        new AttributeSpec("SELECTED", "selected", "selected", true),
        new AttributeSpec("SHAPE_RENDERING", "shapeRendering", "shape-rendering", false),
        new AttributeSpec("SIZE", "size", "size", false),
        new AttributeSpec("SIZES", "sizes", "sizes", false),
        new AttributeSpec("SPELLCHECK", "spellcheck", "spellcheck", false),
        new AttributeSpec("SRC", "src", "src", false),
        new AttributeSpec("SRCSET", "srcset", "srcset", false),
        new AttributeSpec("START", "start", "start", false),
        new AttributeSpec("STOP_COLOR", "stopColor", "stop-color", false),
        new AttributeSpec("STOP_OPACITY", "stopOpacity", "stop-opacity", false),
        new AttributeSpec("STROKE", "stroke", "stroke", false),
        new AttributeSpec("STROKE_DASHARRAY", "strokeDasharray", "stroke-dasharray", false),
        new AttributeSpec("STROKE_DASHOFFSET", "strokeDashoffset", "stroke-dashoffset", false),
        new AttributeSpec("STROKE_LINECAP", "strokeLinecap", "stroke-linecap", false),
        new AttributeSpec("STROKE_LINEJOIN", "strokeLinejoin", "stroke-linejoin", false),
        new AttributeSpec("STROKE_MITERLIMIT", "strokeMiterlimit", "stroke-miterlimit", false),
        new AttributeSpec("STROKE_OPACITY", "strokeOpacity", "stroke-opacity", false),
        new AttributeSpec("STROKE_WIDTH", "strokeWidth", "stroke-width", false),
        new AttributeSpec("STYLE", "style", "style", false),
        new AttributeSpec("TABINDEX", "tabindex", "tabindex", false),
        new AttributeSpec("TARGET", "target", "target", false),
        new AttributeSpec("TEXT_ANCHOR", "textAnchor", "text-anchor", false),
        new AttributeSpec("TEXT_DECORATION", "textDecoration", "text-decoration", false),
        new AttributeSpec("TEXT_OVERFLOW", "textOverflow", "text-overflow", false),
        new AttributeSpec("TEXT_RENDERING", "textRendering", "text-rendering", false),
        new AttributeSpec("TITLE", "title", "title", false),
        new AttributeSpec("TRANSFORM", "transform", "transform", false),
        new AttributeSpec("TRANSFORM_ORIGIN", "transformOrigin", "transform-origin", false),
        new AttributeSpec("TRANSLATE", "translate", "translate", false),
        new AttributeSpec("TYPE", "type", "type", false),
        new AttributeSpec("UNICODE_BIDI", "unicodeBidi", "unicode-bidi", false),
        new AttributeSpec("VALUE", "value", "value", false),
        new AttributeSpec("VECTOR_EFFECT", "vectorEffect", "vector-effect", false),
        new AttributeSpec("VIEWBOX", "viewBox", "viewBox", false),
        new AttributeSpec("VISIBILITY", "visibility", "visibility", false),
        new AttributeSpec("WHITE_SPACE", "whiteSpace", "white-space", false),
        new AttributeSpec("WIDTH", "width", "width", false),
        new AttributeSpec("WORD_SPACING", "wordSpacing", "word-spacing", false),
        new AttributeSpec("WRAP", "wrap", "wrap", false),
        new AttributeSpec("WRITING_MODE", "writingMode", "writing-mode", false),
        new AttributeSpec("XMLNS", "xmlns", "xmlns", false)
    );
  }

  static List<ElementSpec> elements() {
    return elements0().stream().sorted().toList();
  }

  private static List<ElementSpec> elements0() {
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
