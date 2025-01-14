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

import java.util.ArrayList;
import java.util.List;

final class HtmlAttributeName implements Html.AttributeName {

  private static final class Builder {

    private final List<HtmlAttributeName> standardValues = new ArrayList<>();

    private int index;

    private Builder() {}

    public final HtmlAttributeName action(String name) {
      return createImpl(name, false, true, Script.Action.class);
    }

    public final HtmlAttributeName create(String name, boolean booleanAttribute) {
      return createImpl(name, booleanAttribute, false, String.class);
    }

    private HtmlAttributeName createImpl(String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
      HtmlAttributeName result;
      result = new HtmlAttributeName(index++, name, booleanAttribute, singleQuoted, type);

      standardValues.add(result);

      return result;
    }

    public HtmlAttributeName[] buildValuesImpl() {
      return standardValues.toArray(HtmlAttributeName[]::new);
    }

  }

  private static Builder BUILDER = new Builder();

  // custom

  public static final Html.AttributeName DATA_EXECUTE_DEFAULT = BUILDER.create("data-execute-default", true);
  public static final Html.AttributeName DATA_FRAME = BUILDER.create("data-frame", false);
  public static final Html.AttributeName DATA_ON_CLICK = BUILDER.action("data-on-click");
  public static final Html.AttributeName DATA_ON_INPUT = BUILDER.action("data-on-input");

  // standard

  public static final Html.AttributeName ACCESSKEY = BUILDER.create("accesskey", false);
  public static final Html.AttributeName ACTION = BUILDER.create("action", false);
  public static final Html.AttributeName ALIGN = BUILDER.create("align", false);
  public static final Html.AttributeName ALIGNMENT_BASELINE = BUILDER.create("alignment-baseline", false);
  public static final Html.AttributeName ALT = BUILDER.create("alt", false);
  public static final Html.AttributeName ARIA_HIDDEN = BUILDER.create("aria-hidden", false);
  public static final Html.AttributeName ARIA_LABEL = BUILDER.create("aria-label", false);
  public static final Html.AttributeName AS = BUILDER.create("as", false);
  public static final Html.AttributeName ASYNC = BUILDER.create("async", true);
  public static final Html.AttributeName AUTOCOMPLETE = BUILDER.create("autocomplete", false);
  public static final Html.AttributeName AUTOFOCUS = BUILDER.create("autofocus", true);
  public static final Html.AttributeName BASELINE_SHIFT = BUILDER.create("baseline-shift", false);
  public static final Html.AttributeName BORDER = BUILDER.create("border", false);
  public static final Html.AttributeName CELLPADDING = BUILDER.create("cellpadding", false);
  public static final Html.AttributeName CELLSPACING = BUILDER.create("cellspacing", false);
  public static final Html.AttributeName CHARSET = BUILDER.create("charset", false);
  public static final Html.AttributeName CITE = BUILDER.create("cite", false);
  public static final Html.AttributeName CLASS = BUILDER.create("class", false);
  public static final Html.AttributeName CLIP_PATH = BUILDER.create("clip-path", false);
  public static final Html.AttributeName CLIP_RULE = BUILDER.create("clip-rule", false);
  public static final Html.AttributeName COLOR = BUILDER.create("color", false);
  public static final Html.AttributeName COLOR_INTERPOLATION = BUILDER.create("color-interpolation", false);
  public static final Html.AttributeName COLOR_INTERPOLATION_FILTERS = BUILDER.create("color-interpolation-filters", false);
  public static final Html.AttributeName COLS = BUILDER.create("cols", false);
  public static final Html.AttributeName CONTENT = BUILDER.create("content", false);
  public static final Html.AttributeName CONTENTEDITABLE = BUILDER.create("contenteditable", false);
  public static final Html.AttributeName CROSSORIGIN = BUILDER.create("crossorigin", false);
  public static final Html.AttributeName CURSOR = BUILDER.create("cursor", false);
  public static final Html.AttributeName D = BUILDER.create("d", false);
  public static final Html.AttributeName DEFER = BUILDER.create("defer", true);
  public static final Html.AttributeName DIR = BUILDER.create("dir", false);
  public static final Html.AttributeName DIRECTION = BUILDER.create("direction", false);
  public static final Html.AttributeName DIRNAME = BUILDER.create("dirname", false);
  public static final Html.AttributeName DISABLED = BUILDER.create("disabled", true);
  public static final Html.AttributeName DISPLAY = BUILDER.create("display", false);
  public static final Html.AttributeName DOMINANT_BASELINE = BUILDER.create("dominant-baseline", false);
  public static final Html.AttributeName DRAGGABLE = BUILDER.create("draggable", false);
  public static final Html.AttributeName ENCTYPE = BUILDER.create("enctype", false);
  public static final Html.AttributeName FILL = BUILDER.create("fill", false);
  public static final Html.AttributeName FILL_OPACITY = BUILDER.create("fill-opacity", false);
  public static final Html.AttributeName FILL_RULE = BUILDER.create("fill-rule", false);
  public static final Html.AttributeName FILTER = BUILDER.create("filter", false);
  public static final Html.AttributeName FLOOD_COLOR = BUILDER.create("flood-color", false);
  public static final Html.AttributeName FLOOD_OPACITY = BUILDER.create("flood-opacity", false);
  public static final Html.AttributeName FOR = BUILDER.create("for", false);
  public static final Html.AttributeName FORM = BUILDER.create("form", false);
  public static final Html.AttributeName GLYPH_ORIENTATION_HORIZONTAL = BUILDER.create("glyph-orientation-horizontal", false);
  public static final Html.AttributeName GLYPH_ORIENTATION_VERTICAL = BUILDER.create("glyph-orientation-vertical", false);
  public static final Html.AttributeName HEIGHT = BUILDER.create("height", false);
  public static final Html.AttributeName HIDDEN = BUILDER.create("hidden", true);
  public static final Html.AttributeName HREF = BUILDER.create("href", false);
  public static final Html.AttributeName HTTP_EQUIV = BUILDER.create("http-equiv", false);
  public static final Html.AttributeName ID = BUILDER.create("id", false);
  public static final Html.AttributeName IMAGE_RENDERING = BUILDER.create("image-rendering", false);
  public static final Html.AttributeName INTEGRITY = BUILDER.create("integrity", false);
  public static final Html.AttributeName LABEL = BUILDER.create("label", false);
  public static final Html.AttributeName LANG = BUILDER.create("lang", false);
  public static final Html.AttributeName LETTER_SPACING = BUILDER.create("letter-spacing", false);
  public static final Html.AttributeName LIGHTING_COLOR = BUILDER.create("lighting-color", false);
  public static final Html.AttributeName MARKER_END = BUILDER.create("marker-end", false);
  public static final Html.AttributeName MARKER_MID = BUILDER.create("marker-mid", false);
  public static final Html.AttributeName MARKER_START = BUILDER.create("marker-start", false);
  public static final Html.AttributeName MASK = BUILDER.create("mask", false);
  public static final Html.AttributeName MASK_TYPE = BUILDER.create("mask-type", false);
  public static final Html.AttributeName MAXLENGTH = BUILDER.create("maxlength", false);
  public static final Html.AttributeName MEDIA = BUILDER.create("media", false);
  public static final Html.AttributeName METHOD = BUILDER.create("method", false);
  public static final Html.AttributeName MINLENGTH = BUILDER.create("minlength", false);
  public static final Html.AttributeName MULTIPLE = BUILDER.create("multiple", true);
  public static final Html.AttributeName NAME = BUILDER.create("name", false);
  public static final Html.AttributeName NOMODULE = BUILDER.create("nomodule", true);
  public static final Html.AttributeName ONAFTERPRINT = BUILDER.create("onafterprint", false);
  public static final Html.AttributeName ONBEFOREPRINT = BUILDER.create("onbeforeprint", false);
  public static final Html.AttributeName ONBEFOREUNLOAD = BUILDER.create("onbeforeunload", false);
  public static final Html.AttributeName ONCLICK = BUILDER.create("onclick", false);
  public static final Html.AttributeName ONHASHCHANGE = BUILDER.create("onhashchange", false);
  public static final Html.AttributeName ONLANGUAGECHANGE = BUILDER.create("onlanguagechange", false);
  public static final Html.AttributeName ONMESSAGE = BUILDER.create("onmessage", false);
  public static final Html.AttributeName ONOFFLINE = BUILDER.create("onoffline", false);
  public static final Html.AttributeName ONONLINE = BUILDER.create("ononline", false);
  public static final Html.AttributeName ONPAGEHIDE = BUILDER.create("onpagehide", false);
  public static final Html.AttributeName ONPAGESHOW = BUILDER.create("onpageshow", false);
  public static final Html.AttributeName ONPOPSTATE = BUILDER.create("onpopstate", false);
  public static final Html.AttributeName ONREJECTIONHANDLED = BUILDER.create("onrejectionhandled", false);
  public static final Html.AttributeName ONSTORAGE = BUILDER.create("onstorage", false);
  public static final Html.AttributeName ONSUBMIT = BUILDER.create("onsubmit", false);
  public static final Html.AttributeName ONUNHANDLEDREJECTION = BUILDER.create("onunhandledrejection", false);
  public static final Html.AttributeName ONUNLOAD = BUILDER.create("onunload", false);
  public static final Html.AttributeName OPACITY = BUILDER.create("opacity", false);
  public static final Html.AttributeName OPEN = BUILDER.create("open", true);
  public static final Html.AttributeName OVERFLOW = BUILDER.create("overflow", false);
  public static final Html.AttributeName PAINT_ORDER = BUILDER.create("paint-order", false);
  public static final Html.AttributeName PLACEHOLDER = BUILDER.create("placeholder", false);
  public static final Html.AttributeName POINTER_EVENTS = BUILDER.create("pointer-events", false);
  public static final Html.AttributeName PROPERTY = BUILDER.create("property", false);
  public static final Html.AttributeName READONLY = BUILDER.create("readonly", true);
  public static final Html.AttributeName REFERRERPOLICY = BUILDER.create("referrerpolicy", false);
  public static final Html.AttributeName REL = BUILDER.create("rel", false);
  public static final Html.AttributeName REQUIRED = BUILDER.create("required", true);
  public static final Html.AttributeName REV = BUILDER.create("rev", false);
  public static final Html.AttributeName REVERSED = BUILDER.create("reversed", true);
  public static final Html.AttributeName ROLE = BUILDER.create("role", false);
  public static final Html.AttributeName ROWS = BUILDER.create("rows", false);
  public static final Html.AttributeName SELECTED = BUILDER.create("selected", true);
  public static final Html.AttributeName SHAPE_RENDERING = BUILDER.create("shape-rendering", false);
  public static final Html.AttributeName SIZE = BUILDER.create("size", false);
  public static final Html.AttributeName SIZES = BUILDER.create("sizes", false);
  public static final Html.AttributeName SPELLCHECK = BUILDER.create("spellcheck", false);
  public static final Html.AttributeName SRC = BUILDER.create("src", false);
  public static final Html.AttributeName SRCSET = BUILDER.create("srcset", false);
  public static final Html.AttributeName START = BUILDER.create("start", false);
  public static final Html.AttributeName STOP_COLOR = BUILDER.create("stop-color", false);
  public static final Html.AttributeName STOP_OPACITY = BUILDER.create("stop-opacity", false);
  public static final Html.AttributeName STROKE = BUILDER.create("stroke", false);
  public static final Html.AttributeName STROKE_DASHARRAY = BUILDER.create("stroke-dasharray", false);
  public static final Html.AttributeName STROKE_DASHOFFSET = BUILDER.create("stroke-dashoffset", false);
  public static final Html.AttributeName STROKE_LINECAP = BUILDER.create("stroke-linecap", false);
  public static final Html.AttributeName STROKE_LINEJOIN = BUILDER.create("stroke-linejoin", false);
  public static final Html.AttributeName STROKE_MITERLIMIT = BUILDER.create("stroke-miterlimit", false);
  public static final Html.AttributeName STROKE_OPACITY = BUILDER.create("stroke-opacity", false);
  public static final Html.AttributeName STROKE_WIDTH = BUILDER.create("stroke-width", false);
  public static final Html.AttributeName STYLE = BUILDER.create("style", false);
  public static final Html.AttributeName TABINDEX = BUILDER.create("tabindex", false);
  public static final Html.AttributeName TARGET = BUILDER.create("target", false);
  public static final Html.AttributeName TEXT_ANCHOR = BUILDER.create("text-anchor", false);
  public static final Html.AttributeName TEXT_DECORATION = BUILDER.create("text-decoration", false);
  public static final Html.AttributeName TEXT_OVERFLOW = BUILDER.create("text-overflow", false);
  public static final Html.AttributeName TEXT_RENDERING = BUILDER.create("text-rendering", false);
  public static final Html.AttributeName TITLE = BUILDER.create("title", false);
  public static final Html.AttributeName TRANSFORM = BUILDER.create("transform", false);
  public static final Html.AttributeName TRANSFORM_ORIGIN = BUILDER.create("transform-origin", false);
  public static final Html.AttributeName TRANSLATE = BUILDER.create("translate", false);
  public static final Html.AttributeName TYPE = BUILDER.create("type", false);
  public static final Html.AttributeName UNICODE_BIDI = BUILDER.create("unicode-bidi", false);
  public static final Html.AttributeName VALUE = BUILDER.create("value", false);
  public static final Html.AttributeName VECTOR_EFFECT = BUILDER.create("vector-effect", false);
  public static final Html.AttributeName VIEWBOX = BUILDER.create("viewBox", false);
  public static final Html.AttributeName VISIBILITY = BUILDER.create("visibility", false);
  public static final Html.AttributeName WHITE_SPACE = BUILDER.create("white-space", false);
  public static final Html.AttributeName WIDTH = BUILDER.create("width", false);
  public static final Html.AttributeName WORD_SPACING = BUILDER.create("word-spacing", false);
  public static final Html.AttributeName WRAP = BUILDER.create("wrap", false);
  public static final Html.AttributeName WRITING_MODE = BUILDER.create("writing-mode", false);
  public static final Html.AttributeName XMLNS = BUILDER.create("xmlns", false);

  static HtmlAttributeName[] VALUES = create();

  private static HtmlAttributeName[] create() {
    HtmlAttributeName[] result;
    result = BUILDER.buildValuesImpl();

    BUILDER = null;

    return result;
  }

  private final int index;

  private final String name;

  private final boolean booleanAttribute;

  private final boolean singleQuoted;

  private final Class<?> type;

  public HtmlAttributeName(int index, String name, boolean booleanAttribute, boolean singleQuoted, Class<?> type) {
    this.index = index;
    this.name = name;
    this.booleanAttribute = booleanAttribute;
    this.singleQuoted = singleQuoted;
    this.type = type;
  }

  static int size() {
    return VALUES.length;
  }

  public static HtmlAttributeName get(int index) {
    return VALUES[index];
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
  public final boolean booleanAttribute() {
    return booleanAttribute;
  }

  @Override
  public final boolean singleQuoted() {
    return singleQuoted;
  }

  public final Class<?> type() {
    return type;
  }

}