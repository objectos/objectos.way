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

final class HtmlAttributeName implements Html.AttributeName {

  //
  // DATA ATTRS
  //
  static final HtmlAttributeName DATA_FRAME = new HtmlAttributeName(false, 0, "data-frame");
  static final HtmlAttributeName DATA_HIGH = new HtmlAttributeName(false, 1, "data-high");
  static final HtmlAttributeName DATA_LINE = new HtmlAttributeName(false, 2, "data-line");

  //
  // DATA ON
  //
  static final HtmlAttributeName DATA_ON_CLICK = new HtmlAttributeName(false, 3, "data-on-click");
  static final HtmlAttributeName DATA_ON_INPUT = new HtmlAttributeName(false, 4, "data-on-input");
  static final HtmlAttributeName DATA_ON_LOAD = new HtmlAttributeName(false, 5, "data-on-load");
  static final HtmlAttributeName DATA_ON_SUCCESS = new HtmlAttributeName(false, 6, "data-on-success");

  //
  // HTML
  //
  static final HtmlAttributeName ACCESSKEY = new HtmlAttributeName(false, 7, "accesskey");
  static final HtmlAttributeName ACTION = new HtmlAttributeName(false, 8, "action");
  static final HtmlAttributeName ALIGN = new HtmlAttributeName(false, 9, "align");
  static final HtmlAttributeName ALIGNMENT_BASELINE = new HtmlAttributeName(false, 10, "alignment-baseline");
  static final HtmlAttributeName ALT = new HtmlAttributeName(false, 11, "alt");
  static final HtmlAttributeName ARIA_CURRENT = new HtmlAttributeName(false, 12, "aria-current");
  static final HtmlAttributeName ARIA_DISABLED = new HtmlAttributeName(false, 13, "aria-disabled");
  static final HtmlAttributeName ARIA_HIDDEN = new HtmlAttributeName(false, 14, "aria-hidden");
  static final HtmlAttributeName ARIA_INVALID = new HtmlAttributeName(false, 15, "aria-invalid");
  static final HtmlAttributeName ARIA_LABEL = new HtmlAttributeName(false, 16, "aria-label");
  static final HtmlAttributeName ARIA_LABELLED_BY = new HtmlAttributeName(false, 17, "aria-labelledby");
  static final HtmlAttributeName ARIA_MODAL = new HtmlAttributeName(false, 18, "aria-modal");
  static final HtmlAttributeName ARIA_PLACEHOLDER = new HtmlAttributeName(false, 19, "aria-placeholder");
  static final HtmlAttributeName ARIA_READONLY = new HtmlAttributeName(false, 20, "aria-readonly");
  static final HtmlAttributeName ARIA_REQUIRED = new HtmlAttributeName(false, 21, "aria-required");
  static final HtmlAttributeName ARIA_SELECTED = new HtmlAttributeName(false, 22, "aria-selected");
  static final HtmlAttributeName AS = new HtmlAttributeName(false, 23, "as");
  static final HtmlAttributeName ASYNC = new HtmlAttributeName(true, 24, "async");
  static final HtmlAttributeName AUTOCOMPLETE = new HtmlAttributeName(false, 25, "autocomplete");
  static final HtmlAttributeName AUTOFOCUS = new HtmlAttributeName(true, 26, "autofocus");
  static final HtmlAttributeName BASELINE_SHIFT = new HtmlAttributeName(false, 27, "baseline-shift");
  static final HtmlAttributeName BORDER = new HtmlAttributeName(false, 28, "border");
  static final HtmlAttributeName CELLPADDING = new HtmlAttributeName(false, 29, "cellpadding");
  static final HtmlAttributeName CELLSPACING = new HtmlAttributeName(false, 30, "cellspacing");
  static final HtmlAttributeName CHARSET = new HtmlAttributeName(false, 31, "charset");
  static final HtmlAttributeName CHECKED = new HtmlAttributeName(true, 32, "checked");
  static final HtmlAttributeName CITE = new HtmlAttributeName(false, 33, "cite");
  static final HtmlAttributeName CLASS = new HtmlAttributeName(false, 34, "class");
  static final HtmlAttributeName CLIP_PATH = new HtmlAttributeName(false, 35, "clip-path");
  static final HtmlAttributeName CLIP_RULE = new HtmlAttributeName(false, 36, "clip-rule");
  static final HtmlAttributeName CLOSEDBY = new HtmlAttributeName(false, 37, "closedby");
  static final HtmlAttributeName COLOR = new HtmlAttributeName(false, 38, "color");
  static final HtmlAttributeName COLOR_INTERPOLATION = new HtmlAttributeName(false, 39, "color-interpolation");
  static final HtmlAttributeName COLOR_INTERPOLATION_FILTERS = new HtmlAttributeName(false, 40, "color-interpolation-filters");
  static final HtmlAttributeName COLS = new HtmlAttributeName(false, 41, "cols");
  static final HtmlAttributeName CONTENT = new HtmlAttributeName(false, 42, "content");
  static final HtmlAttributeName CONTENTEDITABLE = new HtmlAttributeName(false, 43, "contenteditable");
  static final HtmlAttributeName CROSSORIGIN = new HtmlAttributeName(false, 44, "crossorigin");
  static final HtmlAttributeName CURSOR = new HtmlAttributeName(false, 45, "cursor");
  static final HtmlAttributeName D = new HtmlAttributeName(false, 46, "d");
  static final HtmlAttributeName DEFER = new HtmlAttributeName(true, 47, "defer");
  static final HtmlAttributeName DIR = new HtmlAttributeName(false, 48, "dir");
  static final HtmlAttributeName DIRECTION = new HtmlAttributeName(false, 49, "direction");
  static final HtmlAttributeName DIRNAME = new HtmlAttributeName(false, 50, "dirname");
  static final HtmlAttributeName DISABLED = new HtmlAttributeName(true, 51, "disabled");
  static final HtmlAttributeName DISPLAY = new HtmlAttributeName(false, 52, "display");
  static final HtmlAttributeName DOMINANT_BASELINE = new HtmlAttributeName(false, 53, "dominant-baseline");
  static final HtmlAttributeName DOWNLOAD = new HtmlAttributeName(false, 54, "download");
  static final HtmlAttributeName DRAGGABLE = new HtmlAttributeName(false, 55, "draggable");
  static final HtmlAttributeName ENCTYPE = new HtmlAttributeName(false, 56, "enctype");
  static final HtmlAttributeName FILL = new HtmlAttributeName(false, 57, "fill");
  static final HtmlAttributeName FILL_OPACITY = new HtmlAttributeName(false, 58, "fill-opacity");
  static final HtmlAttributeName FILL_RULE = new HtmlAttributeName(false, 59, "fill-rule");
  static final HtmlAttributeName FILTER = new HtmlAttributeName(false, 60, "filter");
  static final HtmlAttributeName FLOOD_COLOR = new HtmlAttributeName(false, 61, "flood-color");
  static final HtmlAttributeName FLOOD_OPACITY = new HtmlAttributeName(false, 62, "flood-opacity");
  static final HtmlAttributeName FOR = new HtmlAttributeName(false, 63, "for");
  static final HtmlAttributeName FORM = new HtmlAttributeName(false, 64, "form");
  static final HtmlAttributeName GLYPH_ORIENTATION_HORIZONTAL = new HtmlAttributeName(false, 65, "glyph-orientation-horizontal");
  static final HtmlAttributeName GLYPH_ORIENTATION_VERTICAL = new HtmlAttributeName(false, 66, "glyph-orientation-vertical");
  static final HtmlAttributeName HEIGHT = new HtmlAttributeName(false, 67, "height");
  static final HtmlAttributeName HIDDEN = new HtmlAttributeName(true, 68, "hidden");
  static final HtmlAttributeName HREF = new HtmlAttributeName(false, 69, "href");
  static final HtmlAttributeName HTTP_EQUIV = new HtmlAttributeName(false, 70, "http-equiv");
  static final HtmlAttributeName ID = new HtmlAttributeName(false, 71, "id");
  static final HtmlAttributeName IMAGE_RENDERING = new HtmlAttributeName(false, 72, "image-rendering");
  static final HtmlAttributeName INTEGRITY = new HtmlAttributeName(false, 73, "integrity");
  static final HtmlAttributeName LABEL = new HtmlAttributeName(false, 74, "label");
  static final HtmlAttributeName LANG = new HtmlAttributeName(false, 75, "lang");
  static final HtmlAttributeName LETTER_SPACING = new HtmlAttributeName(false, 76, "letter-spacing");
  static final HtmlAttributeName LIGHTING_COLOR = new HtmlAttributeName(false, 77, "lighting-color");
  static final HtmlAttributeName MARKER_END = new HtmlAttributeName(false, 78, "marker-end");
  static final HtmlAttributeName MARKER_MID = new HtmlAttributeName(false, 79, "marker-mid");
  static final HtmlAttributeName MARKER_START = new HtmlAttributeName(false, 80, "marker-start");
  static final HtmlAttributeName MASK = new HtmlAttributeName(false, 81, "mask");
  static final HtmlAttributeName MASK_TYPE = new HtmlAttributeName(false, 82, "mask-type");
  static final HtmlAttributeName MAXLENGTH = new HtmlAttributeName(false, 83, "maxlength");
  static final HtmlAttributeName MEDIA = new HtmlAttributeName(false, 84, "media");
  static final HtmlAttributeName METHOD = new HtmlAttributeName(false, 85, "method");
  static final HtmlAttributeName MINLENGTH = new HtmlAttributeName(false, 86, "minlength");
  static final HtmlAttributeName MULTIPLE = new HtmlAttributeName(true, 87, "multiple");
  static final HtmlAttributeName NAME = new HtmlAttributeName(false, 88, "name");
  static final HtmlAttributeName NOMODULE = new HtmlAttributeName(true, 89, "nomodule");
  static final HtmlAttributeName ONAFTERPRINT = new HtmlAttributeName(false, 90, "onafterprint");
  static final HtmlAttributeName ONBEFOREPRINT = new HtmlAttributeName(false, 91, "onbeforeprint");
  static final HtmlAttributeName ONBEFOREUNLOAD = new HtmlAttributeName(false, 92, "onbeforeunload");
  static final HtmlAttributeName ONCLICK = new HtmlAttributeName(false, 93, "onclick");
  static final HtmlAttributeName ONHASHCHANGE = new HtmlAttributeName(false, 94, "onhashchange");
  static final HtmlAttributeName ONLANGUAGECHANGE = new HtmlAttributeName(false, 95, "onlanguagechange");
  static final HtmlAttributeName ONMESSAGE = new HtmlAttributeName(false, 96, "onmessage");
  static final HtmlAttributeName ONOFFLINE = new HtmlAttributeName(false, 97, "onoffline");
  static final HtmlAttributeName ONONLINE = new HtmlAttributeName(false, 98, "ononline");
  static final HtmlAttributeName ONPAGEHIDE = new HtmlAttributeName(false, 99, "onpagehide");
  static final HtmlAttributeName ONPAGESHOW = new HtmlAttributeName(false, 100, "onpageshow");
  static final HtmlAttributeName ONPOPSTATE = new HtmlAttributeName(false, 101, "onpopstate");
  static final HtmlAttributeName ONREJECTIONHANDLED = new HtmlAttributeName(false, 102, "onrejectionhandled");
  static final HtmlAttributeName ONSTORAGE = new HtmlAttributeName(false, 103, "onstorage");
  static final HtmlAttributeName ONSUBMIT = new HtmlAttributeName(false, 104, "onsubmit");
  static final HtmlAttributeName ONUNHANDLEDREJECTION = new HtmlAttributeName(false, 105, "onunhandledrejection");
  static final HtmlAttributeName ONUNLOAD = new HtmlAttributeName(false, 106, "onunload");
  static final HtmlAttributeName OPACITY = new HtmlAttributeName(false, 107, "opacity");
  static final HtmlAttributeName OPEN = new HtmlAttributeName(true, 108, "open");
  static final HtmlAttributeName OVERFLOW = new HtmlAttributeName(false, 109, "overflow");
  static final HtmlAttributeName PAINT_ORDER = new HtmlAttributeName(false, 110, "paint-order");
  static final HtmlAttributeName PLACEHOLDER = new HtmlAttributeName(false, 111, "placeholder");
  static final HtmlAttributeName POINTER_EVENTS = new HtmlAttributeName(false, 112, "pointer-events");
  static final HtmlAttributeName PROPERTY = new HtmlAttributeName(false, 113, "property");
  static final HtmlAttributeName READONLY = new HtmlAttributeName(true, 114, "readonly");
  static final HtmlAttributeName REFERRERPOLICY = new HtmlAttributeName(false, 115, "referrerpolicy");
  static final HtmlAttributeName REL = new HtmlAttributeName(false, 116, "rel");
  static final HtmlAttributeName REQUIRED = new HtmlAttributeName(true, 117, "required");
  static final HtmlAttributeName REV = new HtmlAttributeName(false, 118, "rev");
  static final HtmlAttributeName REVERSED = new HtmlAttributeName(true, 119, "reversed");
  static final HtmlAttributeName ROLE = new HtmlAttributeName(false, 120, "role");
  static final HtmlAttributeName ROWS = new HtmlAttributeName(false, 121, "rows");
  static final HtmlAttributeName SELECTED = new HtmlAttributeName(true, 122, "selected");
  static final HtmlAttributeName SHAPE_RENDERING = new HtmlAttributeName(false, 123, "shape-rendering");
  static final HtmlAttributeName SIZE = new HtmlAttributeName(false, 124, "size");
  static final HtmlAttributeName SIZES = new HtmlAttributeName(false, 125, "sizes");
  static final HtmlAttributeName SPELLCHECK = new HtmlAttributeName(false, 126, "spellcheck");
  static final HtmlAttributeName SRC = new HtmlAttributeName(false, 127, "src");
  static final HtmlAttributeName SRCSET = new HtmlAttributeName(false, 128, "srcset");
  static final HtmlAttributeName START = new HtmlAttributeName(false, 129, "start");
  static final HtmlAttributeName STOP_COLOR = new HtmlAttributeName(false, 130, "stop-color");
  static final HtmlAttributeName STOP_OPACITY = new HtmlAttributeName(false, 131, "stop-opacity");
  static final HtmlAttributeName STROKE = new HtmlAttributeName(false, 132, "stroke");
  static final HtmlAttributeName STROKE_DASHARRAY = new HtmlAttributeName(false, 133, "stroke-dasharray");
  static final HtmlAttributeName STROKE_DASHOFFSET = new HtmlAttributeName(false, 134, "stroke-dashoffset");
  static final HtmlAttributeName STROKE_LINECAP = new HtmlAttributeName(false, 135, "stroke-linecap");
  static final HtmlAttributeName STROKE_LINEJOIN = new HtmlAttributeName(false, 136, "stroke-linejoin");
  static final HtmlAttributeName STROKE_MITERLIMIT = new HtmlAttributeName(false, 137, "stroke-miterlimit");
  static final HtmlAttributeName STROKE_OPACITY = new HtmlAttributeName(false, 138, "stroke-opacity");
  static final HtmlAttributeName STROKE_WIDTH = new HtmlAttributeName(false, 139, "stroke-width");
  static final HtmlAttributeName STYLE = new HtmlAttributeName(false, 140, "style");
  static final HtmlAttributeName TABINDEX = new HtmlAttributeName(false, 141, "tabindex");
  static final HtmlAttributeName TARGET = new HtmlAttributeName(false, 142, "target");
  static final HtmlAttributeName TEXT_ANCHOR = new HtmlAttributeName(false, 143, "text-anchor");
  static final HtmlAttributeName TEXT_DECORATION = new HtmlAttributeName(false, 144, "text-decoration");
  static final HtmlAttributeName TEXT_OVERFLOW = new HtmlAttributeName(false, 145, "text-overflow");
  static final HtmlAttributeName TEXT_RENDERING = new HtmlAttributeName(false, 146, "text-rendering");
  static final HtmlAttributeName TITLE = new HtmlAttributeName(false, 147, "title");
  static final HtmlAttributeName TRANSFORM = new HtmlAttributeName(false, 148, "transform");
  static final HtmlAttributeName TRANSFORM_ORIGIN = new HtmlAttributeName(false, 149, "transform-origin");
  static final HtmlAttributeName TRANSLATE = new HtmlAttributeName(false, 150, "translate");
  static final HtmlAttributeName TYPE = new HtmlAttributeName(false, 151, "type");
  static final HtmlAttributeName UNICODE_BIDI = new HtmlAttributeName(false, 152, "unicode-bidi");
  static final HtmlAttributeName VALUE = new HtmlAttributeName(false, 153, "value");
  static final HtmlAttributeName VECTOR_EFFECT = new HtmlAttributeName(false, 154, "vector-effect");
  static final HtmlAttributeName VIEWBOX = new HtmlAttributeName(false, 155, "viewBox");
  static final HtmlAttributeName VISIBILITY = new HtmlAttributeName(false, 156, "visibility");
  static final HtmlAttributeName WHITE_SPACE = new HtmlAttributeName(false, 157, "white-space");
  static final HtmlAttributeName WIDTH = new HtmlAttributeName(false, 158, "width");
  static final HtmlAttributeName WORD_SPACING = new HtmlAttributeName(false, 159, "word-spacing");
  static final HtmlAttributeName WRAP = new HtmlAttributeName(false, 160, "wrap");
  static final HtmlAttributeName WRITING_MODE = new HtmlAttributeName(false, 161, "writing-mode");
  static final HtmlAttributeName XMLNS = new HtmlAttributeName(false, 162, "xmlns");

  private static final HtmlAttributeName[] VALUES = {
      DATA_FRAME,
      DATA_HIGH,
      DATA_LINE,
      DATA_ON_CLICK,
      DATA_ON_INPUT,
      DATA_ON_LOAD,
      DATA_ON_SUCCESS,
      ACCESSKEY,
      ACTION,
      ALIGN,
      ALIGNMENT_BASELINE,
      ALT,
      ARIA_CURRENT,
      ARIA_DISABLED,
      ARIA_HIDDEN,
      ARIA_INVALID,
      ARIA_LABEL,
      ARIA_LABELLED_BY,
      ARIA_MODAL,
      ARIA_PLACEHOLDER,
      ARIA_READONLY,
      ARIA_REQUIRED,
      ARIA_SELECTED,
      AS,
      ASYNC,
      AUTOCOMPLETE,
      AUTOFOCUS,
      BASELINE_SHIFT,
      BORDER,
      CELLPADDING,
      CELLSPACING,
      CHARSET,
      CHECKED,
      CITE,
      CLASS,
      CLIP_PATH,
      CLIP_RULE,
      CLOSEDBY,
      COLOR,
      COLOR_INTERPOLATION,
      COLOR_INTERPOLATION_FILTERS,
      COLS,
      CONTENT,
      CONTENTEDITABLE,
      CROSSORIGIN,
      CURSOR,
      D,
      DEFER,
      DIR,
      DIRECTION,
      DIRNAME,
      DISABLED,
      DISPLAY,
      DOMINANT_BASELINE,
      DOWNLOAD,
      DRAGGABLE,
      ENCTYPE,
      FILL,
      FILL_OPACITY,
      FILL_RULE,
      FILTER,
      FLOOD_COLOR,
      FLOOD_OPACITY,
      FOR,
      FORM,
      GLYPH_ORIENTATION_HORIZONTAL,
      GLYPH_ORIENTATION_VERTICAL,
      HEIGHT,
      HIDDEN,
      HREF,
      HTTP_EQUIV,
      ID,
      IMAGE_RENDERING,
      INTEGRITY,
      LABEL,
      LANG,
      LETTER_SPACING,
      LIGHTING_COLOR,
      MARKER_END,
      MARKER_MID,
      MARKER_START,
      MASK,
      MASK_TYPE,
      MAXLENGTH,
      MEDIA,
      METHOD,
      MINLENGTH,
      MULTIPLE,
      NAME,
      NOMODULE,
      ONAFTERPRINT,
      ONBEFOREPRINT,
      ONBEFOREUNLOAD,
      ONCLICK,
      ONHASHCHANGE,
      ONLANGUAGECHANGE,
      ONMESSAGE,
      ONOFFLINE,
      ONONLINE,
      ONPAGEHIDE,
      ONPAGESHOW,
      ONPOPSTATE,
      ONREJECTIONHANDLED,
      ONSTORAGE,
      ONSUBMIT,
      ONUNHANDLEDREJECTION,
      ONUNLOAD,
      OPACITY,
      OPEN,
      OVERFLOW,
      PAINT_ORDER,
      PLACEHOLDER,
      POINTER_EVENTS,
      PROPERTY,
      READONLY,
      REFERRERPOLICY,
      REL,
      REQUIRED,
      REV,
      REVERSED,
      ROLE,
      ROWS,
      SELECTED,
      SHAPE_RENDERING,
      SIZE,
      SIZES,
      SPELLCHECK,
      SRC,
      SRCSET,
      START,
      STOP_COLOR,
      STOP_OPACITY,
      STROKE,
      STROKE_DASHARRAY,
      STROKE_DASHOFFSET,
      STROKE_LINECAP,
      STROKE_LINEJOIN,
      STROKE_MITERLIMIT,
      STROKE_OPACITY,
      STROKE_WIDTH,
      STYLE,
      TABINDEX,
      TARGET,
      TEXT_ANCHOR,
      TEXT_DECORATION,
      TEXT_OVERFLOW,
      TEXT_RENDERING,
      TITLE,
      TRANSFORM,
      TRANSFORM_ORIGIN,
      TRANSLATE,
      TYPE,
      UNICODE_BIDI,
      VALUE,
      VECTOR_EFFECT,
      VIEWBOX,
      VISIBILITY,
      WHITE_SPACE,
      WIDTH,
      WORD_SPACING,
      WRAP,
      WRITING_MODE,
      XMLNS
  };

  private final int index;

  private final String name;

  private HtmlAttributeName(boolean booleanAttribute, int index, String name) {
    this.index = index;

    this.name = name;
  }

  static HtmlAttributeName custom(String name) {
    checkName(name);

    return new HtmlAttributeName(false, -1, name);
  }

  /**
   * We start with a very strict validation. If, with time, the validation
   * proves to be too strict, we'll relax it as needed.
   */
  static void checkName(String name) {
    final int length; // implicit null-check
    length = name.length();

    if (length == 0) {
      throw new IllegalArgumentException("Attribute name must not be empty");
    }

    final char first;
    first = name.charAt(0);

    if (!Character.isLetter(first)) {
      throw new IllegalArgumentException("Attribute name must start with a letter");
    }

    for (int idx = 1; idx < length; idx++) {
      final char c;
      c = name.charAt(idx);

      if (Character.isLetterOrDigit(c)) {
        continue;
      }

      if (c == '-') {
        continue;
      }

      throw new IllegalArgumentException("Attribute name must not contain the character '" + c + "'");
    }
  }

  static int size() {
    return VALUES.length;
  }

  public static HtmlAttributeName get(int index) {
    return VALUES[index];
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof Html.AttributeName that)) {
      return false;
    }

    return index > 0
        ? index == that.index()
        : name.equals(that.name());
  }

  @Override
  public final int hashCode() {
    return index > 0 ? index : name.hashCode();
  }

  @Override
  public final int index() {
    return index;
  }

  @Override
  public final String name() {
    return name;
  }

}