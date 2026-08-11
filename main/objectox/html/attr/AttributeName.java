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
package objectox.html.attr;

public record AttributeName(int index, String name) {

  private static final AttributeNameUniverse U = new AttributeNameUniverse();

  // BEGIN generated code

  public static final AttributeName ACCESSKEY = U.create("accesskey");
  public static final AttributeName ACTION = U.create("action");
  public static final AttributeName ALIGN = U.create("align");
  public static final AttributeName ALIGNMENT_BASELINE = U.create("alignment-baseline");
  public static final AttributeName ALT = U.create("alt");
  public static final AttributeName ARIA_CURRENT = U.create("aria-current");
  public static final AttributeName ARIA_DISABLED = U.create("aria-disabled");
  public static final AttributeName ARIA_HIDDEN = U.create("aria-hidden");
  public static final AttributeName ARIA_INVALID = U.create("aria-invalid");
  public static final AttributeName ARIA_LABEL = U.create("aria-label");
  public static final AttributeName ARIA_LABELLED_BY = U.create("aria-labelledby");
  public static final AttributeName ARIA_MODAL = U.create("aria-modal");
  public static final AttributeName ARIA_PLACEHOLDER = U.create("aria-placeholder");
  public static final AttributeName ARIA_READONLY = U.create("aria-readonly");
  public static final AttributeName ARIA_REQUIRED = U.create("aria-required");
  public static final AttributeName ARIA_SELECTED = U.create("aria-selected");
  public static final AttributeName AS = U.create("as");
  public static final AttributeName ASYNC = U.create("async");
  public static final AttributeName AUTOCOMPLETE = U.create("autocomplete");
  public static final AttributeName AUTOFOCUS = U.create("autofocus");
  public static final AttributeName BASELINE_SHIFT = U.create("baseline-shift");
  public static final AttributeName BORDER = U.create("border");
  public static final AttributeName CELLPADDING = U.create("cellpadding");
  public static final AttributeName CELLSPACING = U.create("cellspacing");
  public static final AttributeName CHARSET = U.create("charset");
  public static final AttributeName CHECKED = U.create("checked");
  public static final AttributeName CITE = U.create("cite");
  public static final AttributeName CLASS = U.create("class");
  public static final AttributeName CLIP_PATH = U.create("clip-path");
  public static final AttributeName CLIP_RULE = U.create("clip-rule");
  public static final AttributeName CLOSEDBY = U.create("closedby");
  public static final AttributeName COLOR = U.create("color");
  public static final AttributeName COLOR_INTERPOLATION = U.create("color-interpolation");
  public static final AttributeName COLOR_INTERPOLATION_FILTERS = U.create("color-interpolation-filters");
  public static final AttributeName COLS = U.create("cols");
  public static final AttributeName CONTENT = U.create("content");
  public static final AttributeName CONTENTEDITABLE = U.create("contenteditable");
  public static final AttributeName CROSSORIGIN = U.create("crossorigin");
  public static final AttributeName CURSOR = U.create("cursor");
  public static final AttributeName D = U.create("d");
  public static final AttributeName DEFER = U.create("defer");
  public static final AttributeName DIR = U.create("dir");
  public static final AttributeName DIRECTION = U.create("direction");
  public static final AttributeName DIRNAME = U.create("dirname");
  public static final AttributeName DISABLED = U.create("disabled");
  public static final AttributeName DISPLAY = U.create("display");
  public static final AttributeName DOMINANT_BASELINE = U.create("dominant-baseline");
  public static final AttributeName DOWNLOAD = U.create("download");
  public static final AttributeName DRAGGABLE = U.create("draggable");
  public static final AttributeName ENCTYPE = U.create("enctype");
  public static final AttributeName FILL = U.create("fill");
  public static final AttributeName FILL_OPACITY = U.create("fill-opacity");
  public static final AttributeName FILL_RULE = U.create("fill-rule");
  public static final AttributeName FILTER = U.create("filter");
  public static final AttributeName FLOOD_COLOR = U.create("flood-color");
  public static final AttributeName FLOOD_OPACITY = U.create("flood-opacity");
  public static final AttributeName FOR = U.create("for");
  public static final AttributeName FORM = U.create("form");
  public static final AttributeName GLYPH_ORIENTATION_HORIZONTAL = U.create("glyph-orientation-horizontal");
  public static final AttributeName GLYPH_ORIENTATION_VERTICAL = U.create("glyph-orientation-vertical");
  public static final AttributeName HEIGHT = U.create("height");
  public static final AttributeName HIDDEN = U.create("hidden");
  public static final AttributeName HREF = U.create("href");
  public static final AttributeName HTTP_EQUIV = U.create("http-equiv");
  public static final AttributeName ID = U.create("id");
  public static final AttributeName IMAGE_RENDERING = U.create("image-rendering");
  public static final AttributeName INTEGRITY = U.create("integrity");
  public static final AttributeName LABEL = U.create("label");
  public static final AttributeName LANG = U.create("lang");
  public static final AttributeName LETTER_SPACING = U.create("letter-spacing");
  public static final AttributeName LIGHTING_COLOR = U.create("lighting-color");
  public static final AttributeName MARKER_END = U.create("marker-end");
  public static final AttributeName MARKER_MID = U.create("marker-mid");
  public static final AttributeName MARKER_START = U.create("marker-start");
  public static final AttributeName MASK = U.create("mask");
  public static final AttributeName MASK_TYPE = U.create("mask-type");
  public static final AttributeName MAXLENGTH = U.create("maxlength");
  public static final AttributeName MEDIA = U.create("media");
  public static final AttributeName METHOD = U.create("method");
  public static final AttributeName MINLENGTH = U.create("minlength");
  public static final AttributeName MULTIPLE = U.create("multiple");
  public static final AttributeName NAME = U.create("name");
  public static final AttributeName NOMODULE = U.create("nomodule");
  public static final AttributeName ONCLICK = U.create("onclick");
  public static final AttributeName ONLOAD = U.create("onload");
  public static final AttributeName ONPOPSTATE = U.create("onpopstate");
  public static final AttributeName ONSUBMIT = U.create("onsubmit");
  public static final AttributeName OPACITY = U.create("opacity");
  public static final AttributeName OPEN = U.create("open");
  public static final AttributeName OVERFLOW = U.create("overflow");
  public static final AttributeName PAINT_ORDER = U.create("paint-order");
  public static final AttributeName PLACEHOLDER = U.create("placeholder");
  public static final AttributeName POINTER_EVENTS = U.create("pointer-events");
  public static final AttributeName PROPERTY = U.create("property");
  public static final AttributeName READONLY = U.create("readonly");
  public static final AttributeName REFERRERPOLICY = U.create("referrerpolicy");
  public static final AttributeName REL = U.create("rel");
  public static final AttributeName REQUIRED = U.create("required");
  public static final AttributeName REV = U.create("rev");
  public static final AttributeName REVERSED = U.create("reversed");
  public static final AttributeName ROLE = U.create("role");
  public static final AttributeName ROWS = U.create("rows");
  public static final AttributeName SELECTED = U.create("selected");
  public static final AttributeName SHAPE_RENDERING = U.create("shape-rendering");
  public static final AttributeName SIZE = U.create("size");
  public static final AttributeName SIZES = U.create("sizes");
  public static final AttributeName SPELLCHECK = U.create("spellcheck");
  public static final AttributeName SRC = U.create("src");
  public static final AttributeName SRCSET = U.create("srcset");
  public static final AttributeName START = U.create("start");
  public static final AttributeName STOP_COLOR = U.create("stop-color");
  public static final AttributeName STOP_OPACITY = U.create("stop-opacity");
  public static final AttributeName STROKE = U.create("stroke");
  public static final AttributeName STROKE_DASHARRAY = U.create("stroke-dasharray");
  public static final AttributeName STROKE_DASHOFFSET = U.create("stroke-dashoffset");
  public static final AttributeName STROKE_LINECAP = U.create("stroke-linecap");
  public static final AttributeName STROKE_LINEJOIN = U.create("stroke-linejoin");
  public static final AttributeName STROKE_MITERLIMIT = U.create("stroke-miterlimit");
  public static final AttributeName STROKE_OPACITY = U.create("stroke-opacity");
  public static final AttributeName STROKE_WIDTH = U.create("stroke-width");
  public static final AttributeName STYLE = U.create("style");
  public static final AttributeName TABINDEX = U.create("tabindex");
  public static final AttributeName TARGET = U.create("target");
  public static final AttributeName TEXT_ANCHOR = U.create("text-anchor");
  public static final AttributeName TEXT_DECORATION = U.create("text-decoration");
  public static final AttributeName TEXT_OVERFLOW = U.create("text-overflow");
  public static final AttributeName TEXT_RENDERING = U.create("text-rendering");
  public static final AttributeName TITLE = U.create("title");
  public static final AttributeName TRANSFORM = U.create("transform");
  public static final AttributeName TRANSFORM_ORIGIN = U.create("transform-origin");
  public static final AttributeName TRANSLATE = U.create("translate");
  public static final AttributeName TYPE = U.create("type");
  public static final AttributeName UNICODE_BIDI = U.create("unicode-bidi");
  public static final AttributeName VALUE = U.create("value");
  public static final AttributeName VECTOR_EFFECT = U.create("vector-effect");
  public static final AttributeName VIEWBOX = U.create("viewBox");
  public static final AttributeName VISIBILITY = U.create("visibility");
  public static final AttributeName WHITE_SPACE = U.create("white-space");
  public static final AttributeName WIDTH = U.create("width");
  public static final AttributeName WORD_SPACING = U.create("word-spacing");
  public static final AttributeName WRAP = U.create("wrap");
  public static final AttributeName WRITING_MODE = U.create("writing-mode");
  public static final AttributeName XMLNS = U.create("xmlns");

  public static final AttributeName DATA_HIGH = U.create("data-high");
  public static final AttributeName DATA_LINE = U.create("data-line");

  // END generated code

  public static String formatAttrValue(String value, StringBuilder sb) {
    enum Parser {
      START,

      TEXT,

      WS;
    }

    Parser parser;
    parser = Parser.START;

    sb.setLength(0);

    for (int idx = 0, len = value.length(); idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      switch (parser) {
        case START -> {
          if (Character.isWhitespace(c)) {
            parser = Parser.START;
          } else {
            parser = Parser.TEXT;

            sb.append(c);
          }
        }

        case TEXT -> {
          if (Character.isWhitespace(c)) {
            parser = Parser.WS;
          } else {
            parser = Parser.TEXT;

            sb.append(c);
          }
        }

        case WS -> {
          if (Character.isWhitespace(c)) {
            parser = Parser.WS;
          } else {
            parser = Parser.TEXT;

            sb.append(' ');

            sb.append(c);
          }
        }
      }
    }

    return sb.toString();
  }

  public static AttributeName get(int index) {
    return U.get(index);
  }

  public static AttributeName getOrCreate(String name) {
    return U.getOrCreate(name);
  }

}