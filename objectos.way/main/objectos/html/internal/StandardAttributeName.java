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
package objectos.html.internal;

import objectos.util.UnmodifiableMap;

/**
 * TODO
 */
// Generated by selfgen.html.HtmlSpec. Do not edit!
public enum StandardAttributeName implements AttributeName {
  ACCESSKEY(AttributeKind.STRING, "accesskey"),

  ACTION(AttributeKind.STRING, "action"),

  ALIGN(AttributeKind.STRING, "align"),

  ALIGNMENTBASELINE(AttributeKind.STRING, "alignment-baseline"),

  ALT(AttributeKind.STRING, "alt"),

  ARIAHIDDEN(AttributeKind.STRING, "aria-hidden"),

  ASYNC(AttributeKind.BOOLEAN, "async"),

  AUTOCOMPLETE(AttributeKind.STRING, "autocomplete"),

  AUTOFOCUS(AttributeKind.BOOLEAN, "autofocus"),

  BASELINESHIFT(AttributeKind.STRING, "baseline-shift"),

  BORDER(AttributeKind.STRING, "border"),

  CELLPADDING(AttributeKind.STRING, "cellpadding"),

  CELLSPACING(AttributeKind.STRING, "cellspacing"),

  CHARSET(AttributeKind.STRING, "charset"),

  CITE(AttributeKind.STRING, "cite"),

  CLASS(AttributeKind.STRING, "class"),

  CLIPPATH(AttributeKind.STRING, "clip-path"),

  CLIPRULE(AttributeKind.STRING, "clip-rule"),

  COLOR(AttributeKind.STRING, "color"),

  COLORINTERPOLATION(AttributeKind.STRING, "color-interpolation"),

  COLORINTERPOLATIONFILTERS(AttributeKind.STRING, "color-interpolation-filters"),

  COLS(AttributeKind.STRING, "cols"),

  CONTENT(AttributeKind.STRING, "content"),

  CONTENTEDITABLE(AttributeKind.STRING, "contenteditable"),

  CROSSORIGIN(AttributeKind.STRING, "crossorigin"),

  CURSOR(AttributeKind.STRING, "cursor"),

  D(AttributeKind.STRING, "d"),

  DEFER(AttributeKind.BOOLEAN, "defer"),

  DIR(AttributeKind.STRING, "dir"),

  DIRECTION(AttributeKind.STRING, "direction"),

  DIRNAME(AttributeKind.STRING, "dirname"),

  DISABLED(AttributeKind.BOOLEAN, "disabled"),

  DISPLAY(AttributeKind.STRING, "display"),

  DOMINANTBASELINE(AttributeKind.STRING, "dominant-baseline"),

  DRAGGABLE(AttributeKind.STRING, "draggable"),

  ENCTYPE(AttributeKind.STRING, "enctype"),

  FILL(AttributeKind.STRING, "fill"),

  FILLOPACITY(AttributeKind.STRING, "fill-opacity"),

  FILLRULE(AttributeKind.STRING, "fill-rule"),

  FILTER(AttributeKind.STRING, "filter"),

  FLOODCOLOR(AttributeKind.STRING, "flood-color"),

  FLOODOPACITY(AttributeKind.STRING, "flood-opacity"),

  FONTFAMILY(AttributeKind.STRING, "font-family"),

  FONTSIZE(AttributeKind.STRING, "font-size"),

  FONTSIZEADJUST(AttributeKind.STRING, "font-size-adjust"),

  FONTSTRETCH(AttributeKind.STRING, "font-stretch"),

  FONTSTYLE(AttributeKind.STRING, "font-style"),

  FONTVARIANT(AttributeKind.STRING, "font-variant"),

  FONTWEIGHT(AttributeKind.STRING, "font-weight"),

  FOR(AttributeKind.STRING, "for"),

  FORM(AttributeKind.STRING, "form"),

  GLYPHORIENTATIONHORIZONTAL(AttributeKind.STRING, "glyph-orientation-horizontal"),

  GLYPHORIENTATIONVERTICAL(AttributeKind.STRING, "glyph-orientation-vertical"),

  HEIGHT(AttributeKind.STRING, "height"),

  HIDDEN(AttributeKind.BOOLEAN, "hidden"),

  HREF(AttributeKind.STRING, "href"),

  HTTPEQUIV(AttributeKind.STRING, "http-equiv"),

  ID(AttributeKind.STRING, "id"),

  IMAGERENDERING(AttributeKind.STRING, "image-rendering"),

  INTEGRITY(AttributeKind.STRING, "integrity"),

  LABEL(AttributeKind.STRING, "label"),

  LANG(AttributeKind.STRING, "lang"),

  LETTERSPACING(AttributeKind.STRING, "letter-spacing"),

  LIGHTINGCOLOR(AttributeKind.STRING, "lighting-color"),

  MARKEREND(AttributeKind.STRING, "marker-end"),

  MARKERMID(AttributeKind.STRING, "marker-mid"),

  MARKERSTART(AttributeKind.STRING, "marker-start"),

  MASK(AttributeKind.STRING, "mask"),

  MASKTYPE(AttributeKind.STRING, "mask-type"),

  MAXLENGTH(AttributeKind.STRING, "maxlength"),

  MEDIA(AttributeKind.STRING, "media"),

  METHOD(AttributeKind.STRING, "method"),

  MINLENGTH(AttributeKind.STRING, "minlength"),

  MULTIPLE(AttributeKind.BOOLEAN, "multiple"),

  NAME(AttributeKind.STRING, "name"),

  NOMODULE(AttributeKind.BOOLEAN, "nomodule"),

  ONAFTERPRINT(AttributeKind.STRING, "onafterprint"),

  ONBEFOREPRINT(AttributeKind.STRING, "onbeforeprint"),

  ONBEFOREUNLOAD(AttributeKind.STRING, "onbeforeunload"),

  ONCLICK(AttributeKind.STRING, "onclick"),

  ONHASHCHANGE(AttributeKind.STRING, "onhashchange"),

  ONLANGUAGECHANGE(AttributeKind.STRING, "onlanguagechange"),

  ONMESSAGE(AttributeKind.STRING, "onmessage"),

  ONOFFLINE(AttributeKind.STRING, "onoffline"),

  ONONLINE(AttributeKind.STRING, "ononline"),

  ONPAGEHIDE(AttributeKind.STRING, "onpagehide"),

  ONPAGESHOW(AttributeKind.STRING, "onpageshow"),

  ONPOPSTATE(AttributeKind.STRING, "onpopstate"),

  ONREJECTIONHANDLED(AttributeKind.STRING, "onrejectionhandled"),

  ONSTORAGE(AttributeKind.STRING, "onstorage"),

  ONSUBMIT(AttributeKind.STRING, "onsubmit"),

  ONUNHANDLEDREJECTION(AttributeKind.STRING, "onunhandledrejection"),

  ONUNLOAD(AttributeKind.STRING, "onunload"),

  OPACITY(AttributeKind.STRING, "opacity"),

  OPEN(AttributeKind.BOOLEAN, "open"),

  OVERFLOW(AttributeKind.STRING, "overflow"),

  PAINTORDER(AttributeKind.STRING, "paint-order"),

  PLACEHOLDER(AttributeKind.STRING, "placeholder"),

  POINTEREVENTS(AttributeKind.STRING, "pointer-events"),

  PROPERTY(AttributeKind.STRING, "property"),

  READONLY(AttributeKind.BOOLEAN, "readonly"),

  REFERRERPOLICY(AttributeKind.STRING, "referrerpolicy"),

  REL(AttributeKind.STRING, "rel"),

  REQUIRED(AttributeKind.BOOLEAN, "required"),

  REV(AttributeKind.STRING, "rev"),

  REVERSED(AttributeKind.BOOLEAN, "reversed"),

  ROLE(AttributeKind.STRING, "role"),

  ROWS(AttributeKind.STRING, "rows"),

  SELECTED(AttributeKind.BOOLEAN, "selected"),

  SHAPERENDERING(AttributeKind.STRING, "shape-rendering"),

  SIZE(AttributeKind.STRING, "size"),

  SIZES(AttributeKind.STRING, "sizes"),

  SPELLCHECK(AttributeKind.STRING, "spellcheck"),

  SRC(AttributeKind.STRING, "src"),

  SRCSET(AttributeKind.STRING, "srcset"),

  START(AttributeKind.STRING, "start"),

  STOPCOLOR(AttributeKind.STRING, "stop-color"),

  STOPOPACITY(AttributeKind.STRING, "stop-opacity"),

  STROKE(AttributeKind.STRING, "stroke"),

  STROKEDASHARRAY(AttributeKind.STRING, "stroke-dasharray"),

  STROKEDASHOFFSET(AttributeKind.STRING, "stroke-dashoffset"),

  STROKELINECAP(AttributeKind.STRING, "stroke-linecap"),

  STROKELINEJOIN(AttributeKind.STRING, "stroke-linejoin"),

  STROKEMITERLIMIT(AttributeKind.STRING, "stroke-miterlimit"),

  STROKEOPACITY(AttributeKind.STRING, "stroke-opacity"),

  STROKEWIDTH(AttributeKind.STRING, "stroke-width"),

  STYLE(AttributeKind.STRING, "style"),

  TABINDEX(AttributeKind.STRING, "tabindex"),

  TARGET(AttributeKind.STRING, "target"),

  TEXTANCHOR(AttributeKind.STRING, "text-anchor"),

  TEXTDECORATION(AttributeKind.STRING, "text-decoration"),

  TEXTOVERFLOW(AttributeKind.STRING, "text-overflow"),

  TEXTRENDERING(AttributeKind.STRING, "text-rendering"),

  TITLE(AttributeKind.STRING, "title"),

  TRANSFORM(AttributeKind.STRING, "transform"),

  TRANSFORMORIGIN(AttributeKind.STRING, "transform-origin"),

  TRANSLATE(AttributeKind.STRING, "translate"),

  TYPE(AttributeKind.STRING, "type"),

  UNICODEBIDI(AttributeKind.STRING, "unicode-bidi"),

  VALUE(AttributeKind.STRING, "value"),

  VECTOREFFECT(AttributeKind.STRING, "vector-effect"),

  VIEWBOX(AttributeKind.STRING, "viewBox"),

  VISIBILITY(AttributeKind.STRING, "visibility"),

  WHITESPACE(AttributeKind.STRING, "white-space"),

  WIDTH(AttributeKind.STRING, "width"),

  WORDSPACING(AttributeKind.STRING, "word-spacing"),

  WRAP(AttributeKind.STRING, "wrap"),

  WRITINGMODE(AttributeKind.STRING, "writing-mode"),

  XMLNS(AttributeKind.STRING, "xmlns");

  private static final StandardAttributeName[] ARRAY = StandardAttributeName.values();

  private static final UnmodifiableMap<String, StandardAttributeName> MAP = mapInit();

  private final AttributeKind kind;

  private final String name;

  StandardAttributeName(AttributeKind kind, String name) {
    this.kind = kind;
    this.name = name;
  }

  public static StandardAttributeName getByCode(int code) {
    return ARRAY[code];
  }

  public static StandardAttributeName getByName(String name) {
    return MAP.get(name);
  }

  public static int size() {
    return ARRAY.length;
  }

  private static UnmodifiableMap<String, StandardAttributeName> mapInit() {
    var builder = new NamesBuilder();
    builder.put("accesskey", ACCESSKEY);
    builder.put("action", ACTION);
    builder.put("align", ALIGN);
    builder.put("alignment-baseline", ALIGNMENTBASELINE);
    builder.put("alt", ALT);
    builder.put("aria-hidden", ARIAHIDDEN);
    builder.put("async", ASYNC);
    builder.put("autocomplete", AUTOCOMPLETE);
    builder.put("autofocus", AUTOFOCUS);
    builder.put("baseline-shift", BASELINESHIFT);
    builder.put("border", BORDER);
    builder.put("cellpadding", CELLPADDING);
    builder.put("cellspacing", CELLSPACING);
    builder.put("charset", CHARSET);
    builder.put("cite", CITE);
    builder.put("class", CLASS);
    builder.put("clip-path", CLIPPATH);
    builder.put("clip-rule", CLIPRULE);
    builder.put("color", COLOR);
    builder.put("color-interpolation", COLORINTERPOLATION);
    builder.put("color-interpolation-filters", COLORINTERPOLATIONFILTERS);
    builder.put("cols", COLS);
    builder.put("content", CONTENT);
    builder.put("contenteditable", CONTENTEDITABLE);
    builder.put("crossorigin", CROSSORIGIN);
    builder.put("cursor", CURSOR);
    builder.put("d", D);
    builder.put("defer", DEFER);
    builder.put("dir", DIR);
    builder.put("direction", DIRECTION);
    builder.put("dirname", DIRNAME);
    builder.put("disabled", DISABLED);
    builder.put("display", DISPLAY);
    builder.put("dominant-baseline", DOMINANTBASELINE);
    builder.put("draggable", DRAGGABLE);
    builder.put("enctype", ENCTYPE);
    builder.put("fill", FILL);
    builder.put("fill-opacity", FILLOPACITY);
    builder.put("fill-rule", FILLRULE);
    builder.put("filter", FILTER);
    builder.put("flood-color", FLOODCOLOR);
    builder.put("flood-opacity", FLOODOPACITY);
    builder.put("font-family", FONTFAMILY);
    builder.put("font-size", FONTSIZE);
    builder.put("font-size-adjust", FONTSIZEADJUST);
    builder.put("font-stretch", FONTSTRETCH);
    builder.put("font-style", FONTSTYLE);
    builder.put("font-variant", FONTVARIANT);
    builder.put("font-weight", FONTWEIGHT);
    builder.put("for", FOR);
    builder.put("form", FORM);
    builder.put("glyph-orientation-horizontal", GLYPHORIENTATIONHORIZONTAL);
    builder.put("glyph-orientation-vertical", GLYPHORIENTATIONVERTICAL);
    builder.put("height", HEIGHT);
    builder.put("hidden", HIDDEN);
    builder.put("href", HREF);
    builder.put("http-equiv", HTTPEQUIV);
    builder.put("id", ID);
    builder.put("image-rendering", IMAGERENDERING);
    builder.put("integrity", INTEGRITY);
    builder.put("label", LABEL);
    builder.put("lang", LANG);
    builder.put("letter-spacing", LETTERSPACING);
    builder.put("lighting-color", LIGHTINGCOLOR);
    builder.put("marker-end", MARKEREND);
    builder.put("marker-mid", MARKERMID);
    builder.put("marker-start", MARKERSTART);
    builder.put("mask", MASK);
    builder.put("mask-type", MASKTYPE);
    builder.put("maxlength", MAXLENGTH);
    builder.put("media", MEDIA);
    builder.put("method", METHOD);
    builder.put("minlength", MINLENGTH);
    builder.put("multiple", MULTIPLE);
    builder.put("name", NAME);
    builder.put("nomodule", NOMODULE);
    builder.put("onafterprint", ONAFTERPRINT);
    builder.put("onbeforeprint", ONBEFOREPRINT);
    builder.put("onbeforeunload", ONBEFOREUNLOAD);
    builder.put("onclick", ONCLICK);
    builder.put("onhashchange", ONHASHCHANGE);
    builder.put("onlanguagechange", ONLANGUAGECHANGE);
    builder.put("onmessage", ONMESSAGE);
    builder.put("onoffline", ONOFFLINE);
    builder.put("ononline", ONONLINE);
    builder.put("onpagehide", ONPAGEHIDE);
    builder.put("onpageshow", ONPAGESHOW);
    builder.put("onpopstate", ONPOPSTATE);
    builder.put("onrejectionhandled", ONREJECTIONHANDLED);
    builder.put("onstorage", ONSTORAGE);
    builder.put("onsubmit", ONSUBMIT);
    builder.put("onunhandledrejection", ONUNHANDLEDREJECTION);
    builder.put("onunload", ONUNLOAD);
    builder.put("opacity", OPACITY);
    builder.put("open", OPEN);
    builder.put("overflow", OVERFLOW);
    builder.put("paint-order", PAINTORDER);
    builder.put("placeholder", PLACEHOLDER);
    builder.put("pointer-events", POINTEREVENTS);
    builder.put("property", PROPERTY);
    builder.put("readonly", READONLY);
    builder.put("referrerpolicy", REFERRERPOLICY);
    builder.put("rel", REL);
    builder.put("required", REQUIRED);
    builder.put("rev", REV);
    builder.put("reversed", REVERSED);
    builder.put("role", ROLE);
    builder.put("rows", ROWS);
    builder.put("selected", SELECTED);
    builder.put("shape-rendering", SHAPERENDERING);
    builder.put("size", SIZE);
    builder.put("sizes", SIZES);
    builder.put("spellcheck", SPELLCHECK);
    builder.put("src", SRC);
    builder.put("srcset", SRCSET);
    builder.put("start", START);
    builder.put("stop-color", STOPCOLOR);
    builder.put("stop-opacity", STOPOPACITY);
    builder.put("stroke", STROKE);
    builder.put("stroke-dasharray", STROKEDASHARRAY);
    builder.put("stroke-dashoffset", STROKEDASHOFFSET);
    builder.put("stroke-linecap", STROKELINECAP);
    builder.put("stroke-linejoin", STROKELINEJOIN);
    builder.put("stroke-miterlimit", STROKEMITERLIMIT);
    builder.put("stroke-opacity", STROKEOPACITY);
    builder.put("stroke-width", STROKEWIDTH);
    builder.put("style", STYLE);
    builder.put("tabindex", TABINDEX);
    builder.put("target", TARGET);
    builder.put("text-anchor", TEXTANCHOR);
    builder.put("text-decoration", TEXTDECORATION);
    builder.put("text-overflow", TEXTOVERFLOW);
    builder.put("text-rendering", TEXTRENDERING);
    builder.put("title", TITLE);
    builder.put("transform", TRANSFORM);
    builder.put("transform-origin", TRANSFORMORIGIN);
    builder.put("translate", TRANSLATE);
    builder.put("type", TYPE);
    builder.put("unicode-bidi", UNICODEBIDI);
    builder.put("value", VALUE);
    builder.put("vector-effect", VECTOREFFECT);
    builder.put("viewBox", VIEWBOX);
    builder.put("visibility", VISIBILITY);
    builder.put("white-space", WHITESPACE);
    builder.put("width", WIDTH);
    builder.put("word-spacing", WORDSPACING);
    builder.put("wrap", WRAP);
    builder.put("writing-mode", WRITINGMODE);
    builder.put("xmlns", XMLNS);

    return builder.build();
  }

  @Override
  public final int getCode() {
    return ordinal();
  }

  @Override
  public final AttributeKind getKind() {
    return kind;
  }

  @Override
  public final String getName() {
    return name;
  }
}
