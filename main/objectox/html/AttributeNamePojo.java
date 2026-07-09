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
package objectox.html;

import objectos.html.AttributeName;

public final class AttributeNamePojo implements AttributeName {

  //
  // DATA ATTRS
  //
  public static final AttributeNamePojo DATA_HIGH = new AttributeNamePojo(false, 0, "data-high");
  public static final AttributeNamePojo DATA_LINE = new AttributeNamePojo(false, 1, "data-line");

  //
  // HTML
  //
  public static final AttributeNamePojo ACCESSKEY = new AttributeNamePojo(false, 2, "accesskey");
  public static final AttributeNamePojo ACTION = new AttributeNamePojo(false, 3, "action");
  public static final AttributeNamePojo ALIGN = new AttributeNamePojo(false, 4, "align");
  public static final AttributeNamePojo ALIGNMENT_BASELINE = new AttributeNamePojo(false, 5, "alignment-baseline");
  public static final AttributeNamePojo ALT = new AttributeNamePojo(false, 6, "alt");
  public static final AttributeNamePojo ARIA_CURRENT = new AttributeNamePojo(false, 7, "aria-current");
  public static final AttributeNamePojo ARIA_DISABLED = new AttributeNamePojo(false, 8, "aria-disabled");
  public static final AttributeNamePojo ARIA_HIDDEN = new AttributeNamePojo(false, 9, "aria-hidden");
  public static final AttributeNamePojo ARIA_INVALID = new AttributeNamePojo(false, 10, "aria-invalid");
  public static final AttributeNamePojo ARIA_LABEL = new AttributeNamePojo(false, 11, "aria-label");
  public static final AttributeNamePojo ARIA_LABELLED_BY = new AttributeNamePojo(false, 12, "aria-labelledby");
  public static final AttributeNamePojo ARIA_MODAL = new AttributeNamePojo(false, 13, "aria-modal");
  public static final AttributeNamePojo ARIA_PLACEHOLDER = new AttributeNamePojo(false, 14, "aria-placeholder");
  public static final AttributeNamePojo ARIA_READONLY = new AttributeNamePojo(false, 15, "aria-readonly");
  public static final AttributeNamePojo ARIA_REQUIRED = new AttributeNamePojo(false, 16, "aria-required");
  public static final AttributeNamePojo ARIA_SELECTED = new AttributeNamePojo(false, 17, "aria-selected");
  public static final AttributeNamePojo AS = new AttributeNamePojo(false, 18, "as");
  public static final AttributeNamePojo ASYNC = new AttributeNamePojo(true, 19, "async");
  public static final AttributeNamePojo AUTOCOMPLETE = new AttributeNamePojo(false, 20, "autocomplete");
  public static final AttributeNamePojo AUTOFOCUS = new AttributeNamePojo(true, 21, "autofocus");
  public static final AttributeNamePojo BASELINE_SHIFT = new AttributeNamePojo(false, 22, "baseline-shift");
  public static final AttributeNamePojo BORDER = new AttributeNamePojo(false, 23, "border");
  public static final AttributeNamePojo CELLPADDING = new AttributeNamePojo(false, 24, "cellpadding");
  public static final AttributeNamePojo CELLSPACING = new AttributeNamePojo(false, 25, "cellspacing");
  public static final AttributeNamePojo CHARSET = new AttributeNamePojo(false, 26, "charset");
  public static final AttributeNamePojo CHECKED = new AttributeNamePojo(true, 27, "checked");
  public static final AttributeNamePojo CITE = new AttributeNamePojo(false, 28, "cite");
  public static final AttributeNamePojo CLASS = new AttributeNamePojo(false, 29, "class");
  public static final AttributeNamePojo CLIP_PATH = new AttributeNamePojo(false, 30, "clip-path");
  public static final AttributeNamePojo CLIP_RULE = new AttributeNamePojo(false, 31, "clip-rule");
  public static final AttributeNamePojo CLOSEDBY = new AttributeNamePojo(false, 32, "closedby");
  public static final AttributeNamePojo COLOR = new AttributeNamePojo(false, 33, "color");
  public static final AttributeNamePojo COLOR_INTERPOLATION = new AttributeNamePojo(false, 34, "color-interpolation");
  public static final AttributeNamePojo COLOR_INTERPOLATION_FILTERS = new AttributeNamePojo(false, 35, "color-interpolation-filters");
  public static final AttributeNamePojo COLS = new AttributeNamePojo(false, 36, "cols");
  public static final AttributeNamePojo CONTENT = new AttributeNamePojo(false, 37, "content");
  public static final AttributeNamePojo CONTENTEDITABLE = new AttributeNamePojo(false, 38, "contenteditable");
  public static final AttributeNamePojo CROSSORIGIN = new AttributeNamePojo(false, 39, "crossorigin");
  public static final AttributeNamePojo CURSOR = new AttributeNamePojo(false, 40, "cursor");
  public static final AttributeNamePojo D = new AttributeNamePojo(false, 41, "d");
  public static final AttributeNamePojo DEFER = new AttributeNamePojo(true, 42, "defer");
  public static final AttributeNamePojo DIR = new AttributeNamePojo(false, 43, "dir");
  public static final AttributeNamePojo DIRECTION = new AttributeNamePojo(false, 44, "direction");
  public static final AttributeNamePojo DIRNAME = new AttributeNamePojo(false, 45, "dirname");
  public static final AttributeNamePojo DISABLED = new AttributeNamePojo(true, 46, "disabled");
  public static final AttributeNamePojo DISPLAY = new AttributeNamePojo(false, 47, "display");
  public static final AttributeNamePojo DOMINANT_BASELINE = new AttributeNamePojo(false, 48, "dominant-baseline");
  public static final AttributeNamePojo DOWNLOAD = new AttributeNamePojo(false, 49, "download");
  public static final AttributeNamePojo DRAGGABLE = new AttributeNamePojo(false, 50, "draggable");
  public static final AttributeNamePojo ENCTYPE = new AttributeNamePojo(false, 51, "enctype");
  public static final AttributeNamePojo FILL = new AttributeNamePojo(false, 52, "fill");
  public static final AttributeNamePojo FILL_OPACITY = new AttributeNamePojo(false, 53, "fill-opacity");
  public static final AttributeNamePojo FILL_RULE = new AttributeNamePojo(false, 54, "fill-rule");
  public static final AttributeNamePojo FILTER = new AttributeNamePojo(false, 55, "filter");
  public static final AttributeNamePojo FLOOD_COLOR = new AttributeNamePojo(false, 56, "flood-color");
  public static final AttributeNamePojo FLOOD_OPACITY = new AttributeNamePojo(false, 57, "flood-opacity");
  public static final AttributeNamePojo FOR = new AttributeNamePojo(false, 58, "for");
  public static final AttributeNamePojo FORM = new AttributeNamePojo(false, 59, "form");
  public static final AttributeNamePojo GLYPH_ORIENTATION_HORIZONTAL = new AttributeNamePojo(false, 60, "glyph-orientation-horizontal");
  public static final AttributeNamePojo GLYPH_ORIENTATION_VERTICAL = new AttributeNamePojo(false, 61, "glyph-orientation-vertical");
  public static final AttributeNamePojo HEIGHT = new AttributeNamePojo(false, 62, "height");
  public static final AttributeNamePojo HIDDEN = new AttributeNamePojo(true, 63, "hidden");
  public static final AttributeNamePojo HREF = new AttributeNamePojo(false, 64, "href");
  public static final AttributeNamePojo HTTP_EQUIV = new AttributeNamePojo(false, 65, "http-equiv");
  public static final AttributeNamePojo ID = new AttributeNamePojo(false, 66, "id");
  public static final AttributeNamePojo IMAGE_RENDERING = new AttributeNamePojo(false, 67, "image-rendering");
  public static final AttributeNamePojo INTEGRITY = new AttributeNamePojo(false, 68, "integrity");
  public static final AttributeNamePojo LABEL = new AttributeNamePojo(false, 69, "label");
  public static final AttributeNamePojo LANG = new AttributeNamePojo(false, 70, "lang");
  public static final AttributeNamePojo LETTER_SPACING = new AttributeNamePojo(false, 71, "letter-spacing");
  public static final AttributeNamePojo LIGHTING_COLOR = new AttributeNamePojo(false, 72, "lighting-color");
  public static final AttributeNamePojo MARKER_END = new AttributeNamePojo(false, 73, "marker-end");
  public static final AttributeNamePojo MARKER_MID = new AttributeNamePojo(false, 74, "marker-mid");
  public static final AttributeNamePojo MARKER_START = new AttributeNamePojo(false, 75, "marker-start");
  public static final AttributeNamePojo MASK = new AttributeNamePojo(false, 76, "mask");
  public static final AttributeNamePojo MASK_TYPE = new AttributeNamePojo(false, 77, "mask-type");
  public static final AttributeNamePojo MAXLENGTH = new AttributeNamePojo(false, 78, "maxlength");
  public static final AttributeNamePojo MEDIA = new AttributeNamePojo(false, 79, "media");
  public static final AttributeNamePojo METHOD = new AttributeNamePojo(false, 80, "method");
  public static final AttributeNamePojo MINLENGTH = new AttributeNamePojo(false, 81, "minlength");
  public static final AttributeNamePojo MULTIPLE = new AttributeNamePojo(true, 82, "multiple");
  public static final AttributeNamePojo NAME = new AttributeNamePojo(false, 83, "name");
  public static final AttributeNamePojo NOMODULE = new AttributeNamePojo(true, 84, "nomodule");
  public static final AttributeNamePojo ONCLICK = new AttributeNamePojo(false, 85, "onclick");
  public static final AttributeNamePojo ONLOAD = new AttributeNamePojo(false, 86, "onload");
  public static final AttributeNamePojo ONPOPSTATE = new AttributeNamePojo(false, 87, "onpopstate");
  public static final AttributeNamePojo ONSUBMIT = new AttributeNamePojo(false, 88, "onsubmit");
  public static final AttributeNamePojo OPACITY = new AttributeNamePojo(false, 89, "opacity");
  public static final AttributeNamePojo OPEN = new AttributeNamePojo(true, 90, "open");
  public static final AttributeNamePojo OVERFLOW = new AttributeNamePojo(false, 91, "overflow");
  public static final AttributeNamePojo PAINT_ORDER = new AttributeNamePojo(false, 92, "paint-order");
  public static final AttributeNamePojo PLACEHOLDER = new AttributeNamePojo(false, 93, "placeholder");
  public static final AttributeNamePojo POINTER_EVENTS = new AttributeNamePojo(false, 94, "pointer-events");
  public static final AttributeNamePojo PROPERTY = new AttributeNamePojo(false, 95, "property");
  public static final AttributeNamePojo READONLY = new AttributeNamePojo(true, 96, "readonly");
  public static final AttributeNamePojo REFERRERPOLICY = new AttributeNamePojo(false, 97, "referrerpolicy");
  public static final AttributeNamePojo REL = new AttributeNamePojo(false, 98, "rel");
  public static final AttributeNamePojo REQUIRED = new AttributeNamePojo(true, 99, "required");
  public static final AttributeNamePojo REV = new AttributeNamePojo(false, 100, "rev");
  public static final AttributeNamePojo REVERSED = new AttributeNamePojo(true, 101, "reversed");
  public static final AttributeNamePojo ROLE = new AttributeNamePojo(false, 102, "role");
  public static final AttributeNamePojo ROWS = new AttributeNamePojo(false, 103, "rows");
  public static final AttributeNamePojo SELECTED = new AttributeNamePojo(true, 104, "selected");
  public static final AttributeNamePojo SHAPE_RENDERING = new AttributeNamePojo(false, 105, "shape-rendering");
  public static final AttributeNamePojo SIZE = new AttributeNamePojo(false, 106, "size");
  public static final AttributeNamePojo SIZES = new AttributeNamePojo(false, 107, "sizes");
  public static final AttributeNamePojo SPELLCHECK = new AttributeNamePojo(false, 108, "spellcheck");
  public static final AttributeNamePojo SRC = new AttributeNamePojo(false, 109, "src");
  public static final AttributeNamePojo SRCSET = new AttributeNamePojo(false, 110, "srcset");
  public static final AttributeNamePojo START = new AttributeNamePojo(false, 111, "start");
  public static final AttributeNamePojo STOP_COLOR = new AttributeNamePojo(false, 112, "stop-color");
  public static final AttributeNamePojo STOP_OPACITY = new AttributeNamePojo(false, 113, "stop-opacity");
  public static final AttributeNamePojo STROKE = new AttributeNamePojo(false, 114, "stroke");
  public static final AttributeNamePojo STROKE_DASHARRAY = new AttributeNamePojo(false, 115, "stroke-dasharray");
  public static final AttributeNamePojo STROKE_DASHOFFSET = new AttributeNamePojo(false, 116, "stroke-dashoffset");
  public static final AttributeNamePojo STROKE_LINECAP = new AttributeNamePojo(false, 117, "stroke-linecap");
  public static final AttributeNamePojo STROKE_LINEJOIN = new AttributeNamePojo(false, 118, "stroke-linejoin");
  public static final AttributeNamePojo STROKE_MITERLIMIT = new AttributeNamePojo(false, 119, "stroke-miterlimit");
  public static final AttributeNamePojo STROKE_OPACITY = new AttributeNamePojo(false, 120, "stroke-opacity");
  public static final AttributeNamePojo STROKE_WIDTH = new AttributeNamePojo(false, 121, "stroke-width");
  public static final AttributeNamePojo STYLE = new AttributeNamePojo(false, 122, "style");
  public static final AttributeNamePojo TABINDEX = new AttributeNamePojo(false, 123, "tabindex");
  public static final AttributeNamePojo TARGET = new AttributeNamePojo(false, 124, "target");
  public static final AttributeNamePojo TEXT_ANCHOR = new AttributeNamePojo(false, 125, "text-anchor");
  public static final AttributeNamePojo TEXT_DECORATION = new AttributeNamePojo(false, 126, "text-decoration");
  public static final AttributeNamePojo TEXT_OVERFLOW = new AttributeNamePojo(false, 127, "text-overflow");
  public static final AttributeNamePojo TEXT_RENDERING = new AttributeNamePojo(false, 128, "text-rendering");
  public static final AttributeNamePojo TITLE = new AttributeNamePojo(false, 129, "title");
  public static final AttributeNamePojo TRANSFORM = new AttributeNamePojo(false, 130, "transform");
  public static final AttributeNamePojo TRANSFORM_ORIGIN = new AttributeNamePojo(false, 131, "transform-origin");
  public static final AttributeNamePojo TRANSLATE = new AttributeNamePojo(false, 132, "translate");
  public static final AttributeNamePojo TYPE = new AttributeNamePojo(false, 133, "type");
  public static final AttributeNamePojo UNICODE_BIDI = new AttributeNamePojo(false, 134, "unicode-bidi");
  public static final AttributeNamePojo VALUE = new AttributeNamePojo(false, 135, "value");
  public static final AttributeNamePojo VECTOR_EFFECT = new AttributeNamePojo(false, 136, "vector-effect");
  public static final AttributeNamePojo VIEWBOX = new AttributeNamePojo(false, 137, "viewBox");
  public static final AttributeNamePojo VISIBILITY = new AttributeNamePojo(false, 138, "visibility");
  public static final AttributeNamePojo WHITE_SPACE = new AttributeNamePojo(false, 139, "white-space");
  public static final AttributeNamePojo WIDTH = new AttributeNamePojo(false, 140, "width");
  public static final AttributeNamePojo WORD_SPACING = new AttributeNamePojo(false, 141, "word-spacing");
  public static final AttributeNamePojo WRAP = new AttributeNamePojo(false, 142, "wrap");
  public static final AttributeNamePojo WRITING_MODE = new AttributeNamePojo(false, 143, "writing-mode");
  public static final AttributeNamePojo XMLNS = new AttributeNamePojo(false, 144, "xmlns");

  private static final AttributeNamePojo[] VALUES = {
      DATA_HIGH,
      DATA_LINE,
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
      ONCLICK,
      ONLOAD,
      ONPOPSTATE,
      ONSUBMIT,
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

  private AttributeNamePojo(boolean booleanAttribute, int index, String name) {
    this.index = index;

    this.name = name;
  }

  public static AttributeNamePojo custom(String name) {
    checkName(name);

    return new AttributeNamePojo(false, -1, name);
  }

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

  public static AttributeNamePojo get(int index) {
    return VALUES[index];
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (!(obj instanceof AttributeName that)) {
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