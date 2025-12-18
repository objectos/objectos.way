/*
 * Copyright (C) 2015-2025 Objectos Software LTDA.
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

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import objectos.way.Testable.Formatter;

/**
 * The <strong>Objectos HTML</strong> main class.
 */
public final class Html {

  /// An object representing an instruction to render an HTML attribute. These
  /// instructions may be reused, unlike the instructions represented by methods
  /// of the `Markup` or `Template` classes.
  public non-sealed interface AttributeObject extends Instruction.AsObject, Instruction.OfVoid {

    /// Creates an object representing an HTML boolean attribute with the
    /// specified name.
    ///
    /// @param name the boolean attribute name
    ///
    /// @return a newly created object representing an HTML boolean attribute
    static AttributeObject of(AttributeName name) {
      return new DefaultAttributeObject(
          Objects.requireNonNull(name, "name == null"),
          null
      );
    }

    /// Creates an object representing a HTML attribute with the specified name
    /// and value.
    ///
    /// @param name the attribute name
    /// @param value the attribute value
    ///
    /// @return a newly created object representing an HTML attribute
    static AttributeObject of(AttributeName name, String value) {
      return new DefaultAttributeObject(
          Objects.requireNonNull(name, "name == null"),
          Objects.requireNonNull(value, "value == null")
      );
    }

    /// The HTML attribute name.
    ///
    /// @return the HTML attribute name
    AttributeName attrName();

    /// The HTML attribute value, or `null` if this object represents a boolean
    /// HTML attribute.
    ///
    /// @return the HTML attribute value, or `null`
    String attrValue();

  }

  private record DefaultAttributeObject(AttributeName attrName, String attrValue) implements AttributeObject {}

  /// The name of an HTML attribute.
  public sealed interface AttributeName permits HtmlAttributeName {

    // BEGIN generated code

    /// The `accesskey` HTML attribute.
    Html.AttributeName ACCESSKEY = HtmlAttributeName.ACCESSKEY;

    /// The `action` HTML attribute.
    Html.AttributeName ACTION = HtmlAttributeName.ACTION;

    /// The `align` HTML attribute.
    Html.AttributeName ALIGN = HtmlAttributeName.ALIGN;

    /// The `alignment-baseline` HTML attribute.
    Html.AttributeName ALIGNMENT_BASELINE = HtmlAttributeName.ALIGNMENT_BASELINE;

    /// The `alt` HTML attribute.
    Html.AttributeName ALT = HtmlAttributeName.ALT;

    /// The `aria-current` HTML attribute.
    Html.AttributeName ARIA_CURRENT = HtmlAttributeName.ARIA_CURRENT;

    /// The `aria-disabled` HTML attribute.
    Html.AttributeName ARIA_DISABLED = HtmlAttributeName.ARIA_DISABLED;

    /// The `aria-hidden` HTML attribute.
    Html.AttributeName ARIA_HIDDEN = HtmlAttributeName.ARIA_HIDDEN;

    /// The `aria-invalid` HTML attribute.
    Html.AttributeName ARIA_INVALID = HtmlAttributeName.ARIA_INVALID;

    /// The `aria-label` HTML attribute.
    Html.AttributeName ARIA_LABEL = HtmlAttributeName.ARIA_LABEL;

    /// The `aria-labelledby` HTML attribute.
    Html.AttributeName ARIA_LABELLED_BY = HtmlAttributeName.ARIA_LABELLED_BY;

    /// The `aria-modal` HTML attribute.
    Html.AttributeName ARIA_MODAL = HtmlAttributeName.ARIA_MODAL;

    /// The `aria-placeholder` HTML attribute.
    Html.AttributeName ARIA_PLACEHOLDER = HtmlAttributeName.ARIA_PLACEHOLDER;

    /// The `aria-readonly` HTML attribute.
    Html.AttributeName ARIA_READONLY = HtmlAttributeName.ARIA_READONLY;

    /// The `aria-required` HTML attribute.
    Html.AttributeName ARIA_REQUIRED = HtmlAttributeName.ARIA_REQUIRED;

    /// The `aria-selected` HTML attribute.
    Html.AttributeName ARIA_SELECTED = HtmlAttributeName.ARIA_SELECTED;

    /// The `as` HTML attribute.
    Html.AttributeName AS = HtmlAttributeName.AS;

    /// The `async` HTML attribute.
    Html.AttributeName ASYNC = HtmlAttributeName.ASYNC;

    /// The `autocomplete` HTML attribute.
    Html.AttributeName AUTOCOMPLETE = HtmlAttributeName.AUTOCOMPLETE;

    /// The `autofocus` HTML attribute.
    Html.AttributeName AUTOFOCUS = HtmlAttributeName.AUTOFOCUS;

    /// The `baseline-shift` HTML attribute.
    Html.AttributeName BASELINE_SHIFT = HtmlAttributeName.BASELINE_SHIFT;

    /// The `border` HTML attribute.
    Html.AttributeName BORDER = HtmlAttributeName.BORDER;

    /// The `cellpadding` HTML attribute.
    Html.AttributeName CELLPADDING = HtmlAttributeName.CELLPADDING;

    /// The `cellspacing` HTML attribute.
    Html.AttributeName CELLSPACING = HtmlAttributeName.CELLSPACING;

    /// The `charset` HTML attribute.
    Html.AttributeName CHARSET = HtmlAttributeName.CHARSET;

    /// The `checked` HTML attribute.
    Html.AttributeName CHECKED = HtmlAttributeName.CHECKED;

    /// The `cite` HTML attribute.
    Html.AttributeName CITE = HtmlAttributeName.CITE;

    /// The `class` HTML attribute.
    Html.AttributeName CLASS = HtmlAttributeName.CLASS;

    /// The `clip-path` HTML attribute.
    Html.AttributeName CLIP_PATH = HtmlAttributeName.CLIP_PATH;

    /// The `clip-rule` HTML attribute.
    Html.AttributeName CLIP_RULE = HtmlAttributeName.CLIP_RULE;

    /// The `closedby` HTML attribute.
    Html.AttributeName CLOSEDBY = HtmlAttributeName.CLOSEDBY;

    /// The `color` HTML attribute.
    Html.AttributeName COLOR = HtmlAttributeName.COLOR;

    /// The `color-interpolation` HTML attribute.
    Html.AttributeName COLOR_INTERPOLATION = HtmlAttributeName.COLOR_INTERPOLATION;

    /// The `color-interpolation-filters` HTML attribute.
    Html.AttributeName COLOR_INTERPOLATION_FILTERS = HtmlAttributeName.COLOR_INTERPOLATION_FILTERS;

    /// The `cols` HTML attribute.
    Html.AttributeName COLS = HtmlAttributeName.COLS;

    /// The `content` HTML attribute.
    Html.AttributeName CONTENT = HtmlAttributeName.CONTENT;

    /// The `contenteditable` HTML attribute.
    Html.AttributeName CONTENTEDITABLE = HtmlAttributeName.CONTENTEDITABLE;

    /// The `crossorigin` HTML attribute.
    Html.AttributeName CROSSORIGIN = HtmlAttributeName.CROSSORIGIN;

    /// The `cursor` HTML attribute.
    Html.AttributeName CURSOR = HtmlAttributeName.CURSOR;

    /// The `d` HTML attribute.
    Html.AttributeName D = HtmlAttributeName.D;

    /// The `defer` HTML attribute.
    Html.AttributeName DEFER = HtmlAttributeName.DEFER;

    /// The `dir` HTML attribute.
    Html.AttributeName DIR = HtmlAttributeName.DIR;

    /// The `direction` HTML attribute.
    Html.AttributeName DIRECTION = HtmlAttributeName.DIRECTION;

    /// The `dirname` HTML attribute.
    Html.AttributeName DIRNAME = HtmlAttributeName.DIRNAME;

    /// The `disabled` HTML attribute.
    Html.AttributeName DISABLED = HtmlAttributeName.DISABLED;

    /// The `display` HTML attribute.
    Html.AttributeName DISPLAY = HtmlAttributeName.DISPLAY;

    /// The `dominant-baseline` HTML attribute.
    Html.AttributeName DOMINANT_BASELINE = HtmlAttributeName.DOMINANT_BASELINE;

    /// The `download` HTML attribute.
    Html.AttributeName DOWNLOAD = HtmlAttributeName.DOWNLOAD;

    /// The `draggable` HTML attribute.
    Html.AttributeName DRAGGABLE = HtmlAttributeName.DRAGGABLE;

    /// The `enctype` HTML attribute.
    Html.AttributeName ENCTYPE = HtmlAttributeName.ENCTYPE;

    /// The `fill` HTML attribute.
    Html.AttributeName FILL = HtmlAttributeName.FILL;

    /// The `fill-opacity` HTML attribute.
    Html.AttributeName FILL_OPACITY = HtmlAttributeName.FILL_OPACITY;

    /// The `fill-rule` HTML attribute.
    Html.AttributeName FILL_RULE = HtmlAttributeName.FILL_RULE;

    /// The `filter` HTML attribute.
    Html.AttributeName FILTER = HtmlAttributeName.FILTER;

    /// The `flood-color` HTML attribute.
    Html.AttributeName FLOOD_COLOR = HtmlAttributeName.FLOOD_COLOR;

    /// The `flood-opacity` HTML attribute.
    Html.AttributeName FLOOD_OPACITY = HtmlAttributeName.FLOOD_OPACITY;

    /// The `for` HTML attribute.
    Html.AttributeName FOR = HtmlAttributeName.FOR;

    /// The `form` HTML attribute.
    Html.AttributeName FORM = HtmlAttributeName.FORM;

    /// The `glyph-orientation-horizontal` HTML attribute.
    Html.AttributeName GLYPH_ORIENTATION_HORIZONTAL = HtmlAttributeName.GLYPH_ORIENTATION_HORIZONTAL;

    /// The `glyph-orientation-vertical` HTML attribute.
    Html.AttributeName GLYPH_ORIENTATION_VERTICAL = HtmlAttributeName.GLYPH_ORIENTATION_VERTICAL;

    /// The `height` HTML attribute.
    Html.AttributeName HEIGHT = HtmlAttributeName.HEIGHT;

    /// The `hidden` HTML attribute.
    Html.AttributeName HIDDEN = HtmlAttributeName.HIDDEN;

    /// The `href` HTML attribute.
    Html.AttributeName HREF = HtmlAttributeName.HREF;

    /// The `http-equiv` HTML attribute.
    Html.AttributeName HTTP_EQUIV = HtmlAttributeName.HTTP_EQUIV;

    /// The `id` HTML attribute.
    Html.AttributeName ID = HtmlAttributeName.ID;

    /// The `image-rendering` HTML attribute.
    Html.AttributeName IMAGE_RENDERING = HtmlAttributeName.IMAGE_RENDERING;

    /// The `integrity` HTML attribute.
    Html.AttributeName INTEGRITY = HtmlAttributeName.INTEGRITY;

    /// The `label` HTML attribute.
    Html.AttributeName LABEL = HtmlAttributeName.LABEL;

    /// The `lang` HTML attribute.
    Html.AttributeName LANG = HtmlAttributeName.LANG;

    /// The `letter-spacing` HTML attribute.
    Html.AttributeName LETTER_SPACING = HtmlAttributeName.LETTER_SPACING;

    /// The `lighting-color` HTML attribute.
    Html.AttributeName LIGHTING_COLOR = HtmlAttributeName.LIGHTING_COLOR;

    /// The `marker-end` HTML attribute.
    Html.AttributeName MARKER_END = HtmlAttributeName.MARKER_END;

    /// The `marker-mid` HTML attribute.
    Html.AttributeName MARKER_MID = HtmlAttributeName.MARKER_MID;

    /// The `marker-start` HTML attribute.
    Html.AttributeName MARKER_START = HtmlAttributeName.MARKER_START;

    /// The `mask` HTML attribute.
    Html.AttributeName MASK = HtmlAttributeName.MASK;

    /// The `mask-type` HTML attribute.
    Html.AttributeName MASK_TYPE = HtmlAttributeName.MASK_TYPE;

    /// The `maxlength` HTML attribute.
    Html.AttributeName MAXLENGTH = HtmlAttributeName.MAXLENGTH;

    /// The `media` HTML attribute.
    Html.AttributeName MEDIA = HtmlAttributeName.MEDIA;

    /// The `method` HTML attribute.
    Html.AttributeName METHOD = HtmlAttributeName.METHOD;

    /// The `minlength` HTML attribute.
    Html.AttributeName MINLENGTH = HtmlAttributeName.MINLENGTH;

    /// The `multiple` HTML attribute.
    Html.AttributeName MULTIPLE = HtmlAttributeName.MULTIPLE;

    /// The `name` HTML attribute.
    Html.AttributeName NAME = HtmlAttributeName.NAME;

    /// The `nomodule` HTML attribute.
    Html.AttributeName NOMODULE = HtmlAttributeName.NOMODULE;

    /// The `onafterprint` HTML attribute.
    Html.AttributeName ONAFTERPRINT = HtmlAttributeName.ONAFTERPRINT;

    /// The `onbeforeprint` HTML attribute.
    Html.AttributeName ONBEFOREPRINT = HtmlAttributeName.ONBEFOREPRINT;

    /// The `onbeforeunload` HTML attribute.
    Html.AttributeName ONBEFOREUNLOAD = HtmlAttributeName.ONBEFOREUNLOAD;

    /// The `onclick` HTML attribute.
    Html.AttributeName ONCLICK = HtmlAttributeName.ONCLICK;

    /// The `onhashchange` HTML attribute.
    Html.AttributeName ONHASHCHANGE = HtmlAttributeName.ONHASHCHANGE;

    /// The `onlanguagechange` HTML attribute.
    Html.AttributeName ONLANGUAGECHANGE = HtmlAttributeName.ONLANGUAGECHANGE;

    /// The `onmessage` HTML attribute.
    Html.AttributeName ONMESSAGE = HtmlAttributeName.ONMESSAGE;

    /// The `onoffline` HTML attribute.
    Html.AttributeName ONOFFLINE = HtmlAttributeName.ONOFFLINE;

    /// The `ononline` HTML attribute.
    Html.AttributeName ONONLINE = HtmlAttributeName.ONONLINE;

    /// The `onpagehide` HTML attribute.
    Html.AttributeName ONPAGEHIDE = HtmlAttributeName.ONPAGEHIDE;

    /// The `onpageshow` HTML attribute.
    Html.AttributeName ONPAGESHOW = HtmlAttributeName.ONPAGESHOW;

    /// The `onpopstate` HTML attribute.
    Html.AttributeName ONPOPSTATE = HtmlAttributeName.ONPOPSTATE;

    /// The `onrejectionhandled` HTML attribute.
    Html.AttributeName ONREJECTIONHANDLED = HtmlAttributeName.ONREJECTIONHANDLED;

    /// The `onstorage` HTML attribute.
    Html.AttributeName ONSTORAGE = HtmlAttributeName.ONSTORAGE;

    /// The `onsubmit` HTML attribute.
    Html.AttributeName ONSUBMIT = HtmlAttributeName.ONSUBMIT;

    /// The `onunhandledrejection` HTML attribute.
    Html.AttributeName ONUNHANDLEDREJECTION = HtmlAttributeName.ONUNHANDLEDREJECTION;

    /// The `onunload` HTML attribute.
    Html.AttributeName ONUNLOAD = HtmlAttributeName.ONUNLOAD;

    /// The `opacity` HTML attribute.
    Html.AttributeName OPACITY = HtmlAttributeName.OPACITY;

    /// The `open` HTML attribute.
    Html.AttributeName OPEN = HtmlAttributeName.OPEN;

    /// The `overflow` HTML attribute.
    Html.AttributeName OVERFLOW = HtmlAttributeName.OVERFLOW;

    /// The `paint-order` HTML attribute.
    Html.AttributeName PAINT_ORDER = HtmlAttributeName.PAINT_ORDER;

    /// The `placeholder` HTML attribute.
    Html.AttributeName PLACEHOLDER = HtmlAttributeName.PLACEHOLDER;

    /// The `pointer-events` HTML attribute.
    Html.AttributeName POINTER_EVENTS = HtmlAttributeName.POINTER_EVENTS;

    /// The `property` HTML attribute.
    Html.AttributeName PROPERTY = HtmlAttributeName.PROPERTY;

    /// The `readonly` HTML attribute.
    Html.AttributeName READONLY = HtmlAttributeName.READONLY;

    /// The `referrerpolicy` HTML attribute.
    Html.AttributeName REFERRERPOLICY = HtmlAttributeName.REFERRERPOLICY;

    /// The `rel` HTML attribute.
    Html.AttributeName REL = HtmlAttributeName.REL;

    /// The `required` HTML attribute.
    Html.AttributeName REQUIRED = HtmlAttributeName.REQUIRED;

    /// The `rev` HTML attribute.
    Html.AttributeName REV = HtmlAttributeName.REV;

    /// The `reversed` HTML attribute.
    Html.AttributeName REVERSED = HtmlAttributeName.REVERSED;

    /// The `role` HTML attribute.
    Html.AttributeName ROLE = HtmlAttributeName.ROLE;

    /// The `rows` HTML attribute.
    Html.AttributeName ROWS = HtmlAttributeName.ROWS;

    /// The `selected` HTML attribute.
    Html.AttributeName SELECTED = HtmlAttributeName.SELECTED;

    /// The `shape-rendering` HTML attribute.
    Html.AttributeName SHAPE_RENDERING = HtmlAttributeName.SHAPE_RENDERING;

    /// The `size` HTML attribute.
    Html.AttributeName SIZE = HtmlAttributeName.SIZE;

    /// The `sizes` HTML attribute.
    Html.AttributeName SIZES = HtmlAttributeName.SIZES;

    /// The `spellcheck` HTML attribute.
    Html.AttributeName SPELLCHECK = HtmlAttributeName.SPELLCHECK;

    /// The `src` HTML attribute.
    Html.AttributeName SRC = HtmlAttributeName.SRC;

    /// The `srcset` HTML attribute.
    Html.AttributeName SRCSET = HtmlAttributeName.SRCSET;

    /// The `start` HTML attribute.
    Html.AttributeName START = HtmlAttributeName.START;

    /// The `stop-color` HTML attribute.
    Html.AttributeName STOP_COLOR = HtmlAttributeName.STOP_COLOR;

    /// The `stop-opacity` HTML attribute.
    Html.AttributeName STOP_OPACITY = HtmlAttributeName.STOP_OPACITY;

    /// The `stroke` HTML attribute.
    Html.AttributeName STROKE = HtmlAttributeName.STROKE;

    /// The `stroke-dasharray` HTML attribute.
    Html.AttributeName STROKE_DASHARRAY = HtmlAttributeName.STROKE_DASHARRAY;

    /// The `stroke-dashoffset` HTML attribute.
    Html.AttributeName STROKE_DASHOFFSET = HtmlAttributeName.STROKE_DASHOFFSET;

    /// The `stroke-linecap` HTML attribute.
    Html.AttributeName STROKE_LINECAP = HtmlAttributeName.STROKE_LINECAP;

    /// The `stroke-linejoin` HTML attribute.
    Html.AttributeName STROKE_LINEJOIN = HtmlAttributeName.STROKE_LINEJOIN;

    /// The `stroke-miterlimit` HTML attribute.
    Html.AttributeName STROKE_MITERLIMIT = HtmlAttributeName.STROKE_MITERLIMIT;

    /// The `stroke-opacity` HTML attribute.
    Html.AttributeName STROKE_OPACITY = HtmlAttributeName.STROKE_OPACITY;

    /// The `stroke-width` HTML attribute.
    Html.AttributeName STROKE_WIDTH = HtmlAttributeName.STROKE_WIDTH;

    /// The `style` HTML attribute.
    Html.AttributeName STYLE = HtmlAttributeName.STYLE;

    /// The `tabindex` HTML attribute.
    Html.AttributeName TABINDEX = HtmlAttributeName.TABINDEX;

    /// The `target` HTML attribute.
    Html.AttributeName TARGET = HtmlAttributeName.TARGET;

    /// The `text-anchor` HTML attribute.
    Html.AttributeName TEXT_ANCHOR = HtmlAttributeName.TEXT_ANCHOR;

    /// The `text-decoration` HTML attribute.
    Html.AttributeName TEXT_DECORATION = HtmlAttributeName.TEXT_DECORATION;

    /// The `text-overflow` HTML attribute.
    Html.AttributeName TEXT_OVERFLOW = HtmlAttributeName.TEXT_OVERFLOW;

    /// The `text-rendering` HTML attribute.
    Html.AttributeName TEXT_RENDERING = HtmlAttributeName.TEXT_RENDERING;

    /// The `title` HTML attribute.
    Html.AttributeName TITLE = HtmlAttributeName.TITLE;

    /// The `transform` HTML attribute.
    Html.AttributeName TRANSFORM = HtmlAttributeName.TRANSFORM;

    /// The `transform-origin` HTML attribute.
    Html.AttributeName TRANSFORM_ORIGIN = HtmlAttributeName.TRANSFORM_ORIGIN;

    /// The `translate` HTML attribute.
    Html.AttributeName TRANSLATE = HtmlAttributeName.TRANSLATE;

    /// The `type` HTML attribute.
    Html.AttributeName TYPE = HtmlAttributeName.TYPE;

    /// The `unicode-bidi` HTML attribute.
    Html.AttributeName UNICODE_BIDI = HtmlAttributeName.UNICODE_BIDI;

    /// The `value` HTML attribute.
    Html.AttributeName VALUE = HtmlAttributeName.VALUE;

    /// The `vector-effect` HTML attribute.
    Html.AttributeName VECTOR_EFFECT = HtmlAttributeName.VECTOR_EFFECT;

    /// The `viewBox` HTML attribute.
    Html.AttributeName VIEWBOX = HtmlAttributeName.VIEWBOX;

    /// The `visibility` HTML attribute.
    Html.AttributeName VISIBILITY = HtmlAttributeName.VISIBILITY;

    /// The `white-space` HTML attribute.
    Html.AttributeName WHITE_SPACE = HtmlAttributeName.WHITE_SPACE;

    /// The `width` HTML attribute.
    Html.AttributeName WIDTH = HtmlAttributeName.WIDTH;

    /// The `word-spacing` HTML attribute.
    Html.AttributeName WORD_SPACING = HtmlAttributeName.WORD_SPACING;

    /// The `wrap` HTML attribute.
    Html.AttributeName WRAP = HtmlAttributeName.WRAP;

    /// The `writing-mode` HTML attribute.
    Html.AttributeName WRITING_MODE = HtmlAttributeName.WRITING_MODE;

    /// The `xmlns` HTML attribute.
    Html.AttributeName XMLNS = HtmlAttributeName.XMLNS;

    // END generated code

    /// Creates a new HTML attribute name.
    ///
    /// @param name the name of the attribute
    ///
    /// @return the HTML attribute name instance
    static AttributeName of(String name) {
      return HtmlAttributeName.custom(name);
    }

    /// Index of this attribute.
    ///
    /// @return index of this attribute.
    int index();

    /// Name of the attribute.
    ///
    /// @return name of the attribute
    String name();

  }

  /// Represents an HTML {@code class} attribute and its value.
  public sealed interface ClassName extends AttributeObject {

    /**
     * Creates a new {@code ClassName} instance whose value is the result of
     * joining the value of each of the specified {@code ClassName} instances
     * around the space character.
     *
     * @param values
     *        the {@code ClassName} instances to be joined into a single value
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static ClassName of(ClassName... values) {
      StringBuilder sb;
      sb = new StringBuilder();

      for (int i = 0, len = values.length; i < len; i++) {
        if (i != 0) {
          sb.append(' ');
        }

        ClassName cn;
        cn = values[i];

        String value;
        value = cn.attrValue();

        sb.append(value);
      }

      String value;
      value = sb.toString();

      return new DefaultClassName(value);
    }

    /**
     * Creates a new {@code ClassName} instance with the specified value.
     *
     * @param value
     *        the value of this HTML {@code class} attribute
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static ClassName of(String value) {
      Objects.requireNonNull(value, "value == null");

      return new DefaultClassName(value);
    }

    /**
     * Creates a new {@code ClassName} instance by processing the specified
     * value.
     *
     * <p>
     * This method is designed to work with Java text blocks. It first removes
     * any leading and trailing whitespace. Additionally, any sequence of
     * consecutive whitespace characters is replaced by a single space
     * character.
     *
     * <p>
     * For example, creating an instance like the following:
     *
     * <pre>{@code
     * Html.ClassName.ofText("""
     *     first \tsecond
     *       third\r
     *
     *     fourth
     *     """);
     * }</pre>
     *
     * <p>
     * Produces the same result as creating an instance with the
     * {@code "first second third fourth"} string literal.
     *
     * @param value
     *        the text block containing class names, possibly spread across
     *        multiple lines
     *
     * @return a newly constructed {@code ClassName} instance
     */
    static Html.ClassName ofText(String value) {
      String result;
      result = Html.formatAttrValue(value);

      return new DefaultClassName(result);
    }

    /**
     * The {@code class} attribute name.
     *
     * @return the {@code class} attribute name
     */
    @Override
    default AttributeName attrName() {
      return HtmlAttributeName.CLASS;
    }

    /**
     * The {@code class} value.
     *
     * @return the {@code class} value
     */
    @Override
    String attrValue();

  }

  private record DefaultClassName(String attrValue) implements ClassName {}

  /// Represents the name of an HTML element.
  public sealed interface ElementName permits HtmlElementName {

    /// The `a` element.
    Html.ElementName A = HtmlElementName.A;

    /// The `abbr` element.
    Html.ElementName ABBR = HtmlElementName.ABBR;

    /// The `article` element.
    Html.ElementName ARTICLE = HtmlElementName.ARTICLE;

    /// The `b` element.
    Html.ElementName B = HtmlElementName.B;

    /// The `blockquote` element.
    Html.ElementName BLOCKQUOTE = HtmlElementName.BLOCKQUOTE;

    /// The `body` element.
    Html.ElementName BODY = HtmlElementName.BODY;

    /// The `br` element.
    Html.ElementName BR = HtmlElementName.BR;

    /// The `button` element.
    Html.ElementName BUTTON = HtmlElementName.BUTTON;

    /// The `clipPath` element.
    Html.ElementName CLIPPATH = HtmlElementName.CLIPPATH;

    /// The `code` element.
    Html.ElementName CODE = HtmlElementName.CODE;

    /// The `dd` element.
    Html.ElementName DD = HtmlElementName.DD;

    /// The `defs` element.
    Html.ElementName DEFS = HtmlElementName.DEFS;

    /// The `details` element.
    Html.ElementName DETAILS = HtmlElementName.DETAILS;

    /// The `div` element.
    Html.ElementName DIV = HtmlElementName.DIV;

    /// The `dl` element.
    Html.ElementName DL = HtmlElementName.DL;

    /// The `dt` element.
    Html.ElementName DT = HtmlElementName.DT;

    /// The `em` element.
    Html.ElementName EM = HtmlElementName.EM;

    /// The `fieldset` element.
    Html.ElementName FIELDSET = HtmlElementName.FIELDSET;

    /// The `figure` element.
    Html.ElementName FIGURE = HtmlElementName.FIGURE;

    /// The `footer` element.
    Html.ElementName FOOTER = HtmlElementName.FOOTER;

    /// The `form` element.
    Html.ElementName FORM = HtmlElementName.FORM;

    /// The `g` element.
    Html.ElementName G = HtmlElementName.G;

    /// The `h1` element.
    Html.ElementName H1 = HtmlElementName.H1;

    /// The `h2` element.
    Html.ElementName H2 = HtmlElementName.H2;

    /// The `h3` element.
    Html.ElementName H3 = HtmlElementName.H3;

    /// The `h4` element.
    Html.ElementName H4 = HtmlElementName.H4;

    /// The `h5` element.
    Html.ElementName H5 = HtmlElementName.H5;

    /// The `h6` element.
    Html.ElementName H6 = HtmlElementName.H6;

    /// The `head` element.
    Html.ElementName HEAD = HtmlElementName.HEAD;

    /// The `header` element.
    Html.ElementName HEADER = HtmlElementName.HEADER;

    /// The `hgroup` element.
    Html.ElementName HGROUP = HtmlElementName.HGROUP;

    /// The `hr` element.
    Html.ElementName HR = HtmlElementName.HR;

    /// The `html` element.
    Html.ElementName HTML = HtmlElementName.HTML;

    /// The `img` element.
    Html.ElementName IMG = HtmlElementName.IMG;

    /// The `input` element.
    Html.ElementName INPUT = HtmlElementName.INPUT;

    /// The `kbd` element.
    Html.ElementName KBD = HtmlElementName.KBD;

    /// The `label` element.
    Html.ElementName LABEL = HtmlElementName.LABEL;

    /// The `legend` element.
    Html.ElementName LEGEND = HtmlElementName.LEGEND;

    /// The `li` element.
    Html.ElementName LI = HtmlElementName.LI;

    /// The `link` element.
    Html.ElementName LINK = HtmlElementName.LINK;

    /// The `main` element.
    Html.ElementName MAIN = HtmlElementName.MAIN;

    /// The `menu` element.
    Html.ElementName MENU = HtmlElementName.MENU;

    /// The `meta` element.
    Html.ElementName META = HtmlElementName.META;

    /// The `nav` element.
    Html.ElementName NAV = HtmlElementName.NAV;

    /// The `ol` element.
    Html.ElementName OL = HtmlElementName.OL;

    /// The `optgroup` element.
    Html.ElementName OPTGROUP = HtmlElementName.OPTGROUP;

    /// The `option` element.
    Html.ElementName OPTION = HtmlElementName.OPTION;

    /// The `p` element.
    Html.ElementName P = HtmlElementName.P;

    /// The `path` element.
    Html.ElementName PATH = HtmlElementName.PATH;

    /// The `pre` element.
    Html.ElementName PRE = HtmlElementName.PRE;

    /// The `progress` element.
    Html.ElementName PROGRESS = HtmlElementName.PROGRESS;

    /// The `samp` element.
    Html.ElementName SAMP = HtmlElementName.SAMP;

    /// The `script` element.
    Html.ElementName SCRIPT = HtmlElementName.SCRIPT;

    /// The `section` element.
    Html.ElementName SECTION = HtmlElementName.SECTION;

    /// The `select` element.
    Html.ElementName SELECT = HtmlElementName.SELECT;

    /// The `small` element.
    Html.ElementName SMALL = HtmlElementName.SMALL;

    /// The `span` element.
    Html.ElementName SPAN = HtmlElementName.SPAN;

    /// The `strong` element.
    Html.ElementName STRONG = HtmlElementName.STRONG;

    /// The `style` element.
    Html.ElementName STYLE = HtmlElementName.STYLE;

    /// The `sub` element.
    Html.ElementName SUB = HtmlElementName.SUB;

    /// The `summary` element.
    Html.ElementName SUMMARY = HtmlElementName.SUMMARY;

    /// The `sup` element.
    Html.ElementName SUP = HtmlElementName.SUP;

    /// The `svg` element.
    Html.ElementName SVG = HtmlElementName.SVG;

    /// The `table` element.
    Html.ElementName TABLE = HtmlElementName.TABLE;

    /// The `tbody` element.
    Html.ElementName TBODY = HtmlElementName.TBODY;

    /// The `td` element.
    Html.ElementName TD = HtmlElementName.TD;

    /// The `template` element.
    Html.ElementName TEMPLATE = HtmlElementName.TEMPLATE;

    /// The `textarea` element.
    Html.ElementName TEXTAREA = HtmlElementName.TEXTAREA;

    /// The `th` element.
    Html.ElementName TH = HtmlElementName.TH;

    /// The `thead` element.
    Html.ElementName THEAD = HtmlElementName.THEAD;

    /// The `title` element.
    Html.ElementName TITLE = HtmlElementName.TITLE;

    /// The `tr` element.
    Html.ElementName TR = HtmlElementName.TR;

    /// The `ul` element.
    Html.ElementName UL = HtmlElementName.UL;

  }

  /// A fragment represents a set of markup or template instructions to be
  /// lazily applied.
  ///
  /// The set of instructions MUST be of the same markup or template instance
  /// where this fragment will be included.
  public sealed interface Fragment {

    /// A fragment that takes no arguments.
    @FunctionalInterface
    non-sealed interface Of0 extends Fragment {

      /// Invokes this set of instructions.
      void invoke();

    }

    /// A fragment that takes one argument.
    ///
    /// @param <T1> the type of the argument
    @FunctionalInterface
    non-sealed interface Of1<T1> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the argument
      void invoke(T1 arg1);

    }

    /// A fragment that takes two arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    @FunctionalInterface
    non-sealed interface Of2<T1, T2> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      void invoke(T1 arg1, T2 arg2);

    }

    /// A fragment that takes three arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    @FunctionalInterface
    non-sealed interface Of3<T1, T2, T3> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      /// @param arg3 the third argument
      void invoke(T1 arg1, T2 arg2, T3 arg3);

    }

    /// A fragment that takes four arguments.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param <T4> the type of the fourth argument
    @FunctionalInterface
    non-sealed interface Of4<T1, T2, T3, T4> extends Fragment {

      /// Invokes this set of instructions.
      ///
      /// @param arg1 the first argument
      /// @param arg2 the second argument
      /// @param arg3 the third argument
      /// @param arg4 the fourth argument
      void invoke(T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    }

  }

  /**
   * Represents an HTML {@code id} attribute and its value.
   */
  public sealed interface Id extends AttributeObject {

    /**
     * Creates a new {@code Id} instance with the specified value.
     *
     * @param value
     *        the value of this HTML {@code id} attribute.
     *
     * @return a newly constructed {@code Id} instance
     */
    static Id of(String value) {
      Objects.requireNonNull(value, "value == null");

      return new DefaultId(value);
    }

    /**
     * The {@code id} attribute name.
     *
     * @return the {@code id} attribute name
     */
    @Override
    default AttributeName attrName() {
      return HtmlAttributeName.ID;
    }

    /**
     * The {@code id} value.
     *
     * @return the {@code id} value
     */
    @Override
    String attrValue();

  }

  private record DefaultId(String attrValue) implements Id {}

  /**
   * Represents an instruction that generates part of the output of an HTML
   * template.
   */
  public sealed interface Instruction {

    /**
     * Class of instructions that are represented by object instances.
     *
     * <p>
     * Instances of this interface can be safely reused in multiple templates.
     */
    sealed interface AsObject extends Instruction {}

    /**
     * Class of instructions that are represented by methods of the
     * {@link Html.Template} class.
     *
     * <p>
     * Instances of this interface MUST NOT be reused in a template.
     */
    sealed interface AsMethod extends Instruction {}

    /**
     * An instruction to generate an ambiguous element in a template.
     */
    sealed interface OfAmbiguous extends OfAttribute, OfElement {}

    /**
     * An instruction to generate an HTML attribute in template.
     */
    sealed interface OfAttribute extends AsMethod, OfVoid {}

    /**
     * An instruction to generate a {@code data-on-*} HTML attribute in a
     * template.
     */
    sealed interface OfDataOn extends AsMethod, OfVoid {}

    /**
     * An instruction to generate an HTML element in a template.
     */
    sealed interface OfElement extends AsMethod {}

    /**
     * An instruction to include an HTML fragment to a template.
     */
    sealed interface OfFragment extends AsMethod, OfVoid {}

    /**
     * Class of instructions that are allowed as arguments to template
     * methods that represent void elements.
     */
    sealed interface OfVoid extends Instruction {}

    /// The no-op instruction.
    sealed interface NoOp extends AsMethod, OfVoid {}

    /// Returns the no-op instruction.
    ///
    /// @return the no-op instruction
    static NoOp noop() {
      return Html.NOOP;
    }

  }

  sealed interface AttributeOrNoOp extends Instruction.OfAttribute, Instruction.OfDataOn, Instruction.NoOp {}

  private static final class HtmlInstruction
      implements
      AttributeOrNoOp,
      Html.Instruction.OfAmbiguous,
      Html.Instruction.OfElement,
      Html.Instruction.OfFragment {}

  static final Html.AttributeOrNoOp ATTRIBUTE = new HtmlInstruction();
  static final HtmlInstruction ELEMENT = new HtmlInstruction();
  static final Html.Instruction.OfFragment FRAGMENT = new HtmlInstruction();
  static final Html.AttributeOrNoOp NOOP = new HtmlInstruction();

  /// An object that renders HTML on its own or as part of a larger
  /// HTML document.
  @FunctionalInterface
  public interface Component extends Media.Text {

    /// Returns `text/html; charset=utf-8`.
    ///
    /// @return `text/html; charset=utf-8`
    @Override
    default String contentType() {
      return "text/html; charset=utf-8";
    }

    /// Returns `StandardCharsets.UTF_8`.
    ///
    /// @return `StandardCharsets.UTF_8`
    @Override
    default Charset charset() {
      return StandardCharsets.UTF_8;
    }

    /// Returns the markup instance to be used by this component.
    ///
    /// @return the markup instance to be used by this component
    default Html.Markup newHtmlMarkup() {
      return new Html.Markup.OfHtml();
    }

    /// Renders the HTML of this component.
    ///
    /// @param m the markup object to use
    void renderHtml(Html.Markup m);

    /// Returns the HTML generated by this component.
    ///
    /// @return the HTML generated by this component
    default String toHtml() {
      final Html.Markup html;
      html = newHtmlMarkup();

      renderHtml(html);

      return html.toHtml();
    }

    /// Writes out the HTML generated by this component.
    ///
    /// @param out where to write out the HTML
    ///
    /// @throws IOException if an I/O error occurs
    @Override
    default void writeTo(Appendable out) throws IOException {
      Objects.requireNonNull(out, "out == null");

      final Html.Markup html;
      html = newHtmlMarkup();

      renderHtml(html);

      html.writeTo(out);
    }

  }

  /// Declares the structure of an HTML document using pure Java.
  public sealed interface Markup {

    /// Markup implementation for generating HTML.
    non-sealed class OfHtml extends HtmlMarkupOfHtml implements Markup {

      /// Sole constructor.
      protected OfHtml() {}

      /// {@inheritDoc}
      @Override
      public final Html.Instruction.OfFragment c(Html.Component... components) {
        int index;
        index = fragmentBegin();

        for (int idx = 0, len = components.length; idx < len; idx++) {
          final Html.Component c;
          c = Check.notNull(components[idx], "components[", idx, "] == null");

          c.renderHtml(this);
        }

        fragmentEnd(index);

        return Html.FRAGMENT;
      }

      /// {@inheritDoc}
      @Override
      public final Html.Instruction.OfFragment c(Iterable<? extends Html.Component> components) {
        int index;
        index = fragmentBegin();

        for (Html.Component c : components) {
          c.renderHtml(this);
        }

        fragmentEnd(index);

        return Html.FRAGMENT;
      }

    }

    /// Markup implementation for formatting testable objects.
    final class OfTestable extends HtmlMarkupOfTestable implements Markup {

      OfTestable(Formatter formatter) {
        super(formatter);
      }

      /// {@inheritDoc}
      @Override
      public final Html.Instruction.OfFragment c(Component... components) {
        for (int idx = 0, len = components.length; idx < len; idx++) {
          final Html.Component c;
          c = Check.notNull(components[idx], "components[", idx, "] == null");

          c.renderHtml(this);
        }

        return Html.FRAGMENT;
      }

      /// {@inheritDoc}
      @Override
      public final Html.Instruction.OfFragment c(Iterable<? extends Component> components) {
        for (Html.Component c : components) {
          c.renderHtml(this);
        }

        return Html.FRAGMENT;
      }

    }

    /// Renders the specified attribute at the root of a document or fragment.
    ///
    /// @param object the attribute
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute attr(AttributeObject object);

    /// Generates and returns the HTML represented by this markup instance.
    ///
    /// @return the HTML represented by this markup instance.
    String toHtml();

    /// Writes out the HTML generated by this markup instance. This method may
    /// be invoked at most once per markup instance.
    ///
    /// @param out where to write out the HTML
    ///
    /// @throws IOException if an I/O error occurs
    void writeTo(Appendable out) throws IOException;

    // START generated code
    /// The `async` boolean attribute.
    Html.AttributeObject async = Html.AttributeObject.of(Html.AttributeName.ASYNC);

    /// The `autofocus` boolean attribute.
    Html.AttributeObject autofocus = Html.AttributeObject.of(Html.AttributeName.AUTOFOCUS);

    /// The `checked` boolean attribute.
    Html.AttributeObject checked = Html.AttributeObject.of(Html.AttributeName.CHECKED);

    /// The `defer` boolean attribute.
    Html.AttributeObject defer = Html.AttributeObject.of(Html.AttributeName.DEFER);

    /// The `disabled` boolean attribute.
    Html.AttributeObject disabled = Html.AttributeObject.of(Html.AttributeName.DISABLED);

    /// The `hidden` boolean attribute.
    Html.AttributeObject hidden = Html.AttributeObject.of(Html.AttributeName.HIDDEN);

    /// The `multiple` boolean attribute.
    Html.AttributeObject multiple = Html.AttributeObject.of(Html.AttributeName.MULTIPLE);

    /// The `nomodule` boolean attribute.
    Html.AttributeObject nomodule = Html.AttributeObject.of(Html.AttributeName.NOMODULE);

    /// The `open` boolean attribute.
    Html.AttributeObject open = Html.AttributeObject.of(Html.AttributeName.OPEN);

    /// The `readonly` boolean attribute.
    Html.AttributeObject readonly = Html.AttributeObject.of(Html.AttributeName.READONLY);

    /// The `required` boolean attribute.
    Html.AttributeObject required = Html.AttributeObject.of(Html.AttributeName.REQUIRED);

    /// The `reversed` boolean attribute.
    Html.AttributeObject reversed = Html.AttributeObject.of(Html.AttributeName.REVERSED);

    /// The `selected` boolean attribute.
    Html.AttributeObject selected = Html.AttributeObject.of(Html.AttributeName.SELECTED);

    //
    // WAY
    //

    /// Renders the `data-on-click` attribute with the specified script.
    ///
    /// @param script the script to be executed
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfDataOn dataOnClick(Script.JsAction script);

    /// Renders the `data-on-input` attribute with the specified script.
    ///
    /// @param script the script to be executed
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfDataOn dataOnInput(Script.JsAction script);

    /// Renders the `data-on-load` attribute with the specified script.
    ///
    /// @param script the script to be executed
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfDataOn dataOnLoad(Script.JsAction script);

    /// Renders the `data-on-success` attribute with the specified script.
    ///
    /// @param script the script to be executed
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfDataOn dataOnSuccess(Script.JsAction script);

    /// Renders the `data-frame` attribute for a frame with the specified name.
    ///
    /// @param name the name of the frame
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute dataFrame(String name);

    /// Renders the `data-frame` attribute for a frame with the specified name
    /// and value.
    ///
    /// @param name the name of the frame
    /// @param value the value of the frame
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute dataFrame(String name, String value);

    /// Renders the `class` attribute by processing the specified value.
    ///
    /// This method is designed to work with Java text blocks. It first removes
    /// any leading and trailing whitespace. Additionally, any sequence of
    /// consecutive whitespace characters is replaced by a single space
    /// character.
    ///
    /// For example, the following invocation:
    ///
    /// ```java css(""" display:inline-flex justify-content:center
    ///
    /// background-color:blue-500 """); ```
    ///
    /// Produces the same result as invoking `className("display:inline-flex
    /// justify-content:center background-color:blue-500")`.
    ///
    /// @param value the text block containing class names, possibly spread
    /// across multiple lines
    ///
    /// @return an instruction representing this attribute.
    Html.Instruction.OfAttribute css(String value);

    /// Renders the specified components in order as part of this document.
    ///
    /// @param components the components to be rendered as part of this document
    ///
    /// @return an instruction representing the rendered components.
    Html.Instruction.OfFragment c(Html.Component... components);

    /// Renders the specified components in order as part of this document.
    ///
    /// @param components the components to be rendered as part of this document
    ///
    /// @return an instruction representing the rendered components.
    Html.Instruction.OfFragment c(Iterable<? extends Html.Component> components);

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f0"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html
    /// <ul>
    /// <li>Mon</li>
    /// <li>Wed</li>
    /// <li>Fri</li>
    /// </ul>
    /// ```
    ///
    /// @param fragment the fragment to include
    ///
    /// @return an instruction representing the fragment
    Html.Instruction.OfFragment f(Html.Fragment.Of0 fragment);

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f1"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html
    /// <ul>
    /// <li>Mon</li>
    /// <li>Wed</li>
    /// <li>Fri</li>
    /// </ul>
    /// ```
    ///
    /// @param <T1> the type of the first argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    ///
    /// @return an instruction representing the fragment
    <T1> Html.Instruction.OfFragment f(Html.Fragment.Of1<T1> fragment, T1 arg1);

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f2"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html <div><button>OK</button><button>Cancel</button></div> ```
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    ///
    /// @return an instruction representing the fragment
    <T1, T2> Html.Instruction.OfFragment f(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2);

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f3"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html <div>
    /// <p>
    /// City<span>Tokyo</span>
    /// </p>
    /// <p>
    /// Country<span>Japan</span>
    /// </p>
    /// </div> ```
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    /// @param arg3 the third argument
    ///
    /// @return an instruction representing the fragment
    <T1, T2, T3> Html.Instruction.OfFragment f(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3);

    /// Renders the specified fragment as part of this document.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param <T4> the type of the fourth argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    /// @param arg3 the third argument
    /// @param arg4 the fourth argument
    ///
    /// @return an instruction representing the fragment
    <T1, T2, T3, T4> Html.Instruction.OfFragment f(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4);

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    ///
    /// @param contents the instructions to be flattened
    ///
    /// @return an instruction representing this flatten operation
    Html.Instruction.OfElement flatten(Html.Instruction... contents);

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    ///
    /// @param contents the instructions to be flattened
    ///
    /// @return an instruction representing this flatten operation
    Html.Instruction.OfElement flatten(Iterable<? extends Html.Instruction> contents);

    /// The no-op instruction.
    ///
    /// @return the no-op instruction.
    Html.Instruction.NoOp noop();

    //
    // TESTABLE
    //

    /// Formats the specified value as a testable table cell with the specified
    /// fixed width (optional operation).
    ///
    /// @param value the cell value
    /// @param width the fixed width of the cell
    ///
    /// @return always the cell value
    String testableCell(String value, int width);

    /// Formats the specified name and value as a testable field (optional
    /// operation).
    ///
    /// @param name the field name
    /// @param value the field value
    ///
    /// @return always the field value
    String testableField(String name, String value);

    /// Formats the specified name as a testable field name (optional
    /// operation).
    ///
    /// @param name the field name
    ///
    /// @return the specified field name
    String testableFieldName(String name);

    /// Formats the specified value as a testable field value (optional
    /// operation).
    ///
    /// @param value the field value
    ///
    /// @return the specified field value
    String testableFieldValue(String value);

    /// Formats the specified value as a testable heading level 1 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH1(String value);

    /// Formats the specified value as a testable heading level 2 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH2(String value);

    /// Formats the specified value as a testable heading level 3 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH3(String value);

    /// Formats the specified value as a testable heading level 4 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH4(String value);

    /// Formats the specified value as a testable heading level 5 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH5(String value);

    /// Formats the specified value as a testable heading level 6 (optional
    /// operation).
    ///
    /// @param value the heading value
    ///
    /// @return the specified value
    String testableH6(String value);

    /// Formats a line separator at the testable output exclusively (optional
    /// operation).
    ///
    /// @return a no-op instruction
    Html.Instruction.NoOp testableNewLine();

    //
    // ELEMENTS
    //

    /// Renders an HTML element with the specified name and contents.
    ///
    /// @param name the element name
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element
    Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents);

    /// Renders an HTML element with the specified name and text.
    ///
    /// @param name the element name
    /// @param text the text value of this element
    ///
    /// @return an instruction representing the element
    Html.Instruction.OfElement elem(Html.ElementName name, String text);

    /// Renders the `<!DOCTYPE html>` doctype.
    void doctype();

    /// Renders the `a` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement a(Html.Instruction... contents);

    /// Renders the `a` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement a(String text);

    /// Renders the `abbr` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement abbr(Html.Instruction... contents);

    /// Renders the `abbr` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement abbr(String text);

    /// Renders the `article` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement article(Html.Instruction... contents);

    /// Renders the `article` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement article(String text);

    /// Renders the `aside` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement aside(Html.Instruction... contents);

    /// Renders the `aside` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement aside(String text);

    /// Renders the `b` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement b(Html.Instruction... contents);

    /// Renders the `b` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement b(String text);

    /// Renders the `blockquote` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement blockquote(Html.Instruction... contents);

    /// Renders the `blockquote` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement blockquote(String text);

    /// Renders the `body` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement body(Html.Instruction... contents);

    /// Renders the `body` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement body(String text);

    /// Renders the `br` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents);

    /// Renders the `button` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement button(Html.Instruction... contents);

    /// Renders the `button` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement button(String text);

    /// Renders the `clipPath` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement clipPath(Html.Instruction... contents);

    /// Renders the `code` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement code(Html.Instruction... contents);

    /// Renders the `code` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement code(String text);

    /// Renders the `dd` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dd(Html.Instruction... contents);

    /// Renders the `dd` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dd(String text);

    /// Renders the `defs` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement defs(Html.Instruction... contents);

    /// Renders the `defs` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement defs(String text);

    /// Renders the `details` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement details(Html.Instruction... contents);

    /// Renders the `details` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement details(String text);

    /// Renders the `dialog` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dialog(Html.Instruction... contents);

    /// Renders the `dialog` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dialog(String text);

    /// Renders the `div` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement div(Html.Instruction... contents);

    /// Renders the `div` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement div(String text);

    /// Renders the `dl` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dl(Html.Instruction... contents);

    /// Renders the `dl` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dl(String text);

    /// Renders the `dt` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dt(Html.Instruction... contents);

    /// Renders the `dt` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement dt(String text);

    /// Renders the `em` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement em(Html.Instruction... contents);

    /// Renders the `em` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement em(String text);

    /// Renders the `fieldset` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement fieldset(Html.Instruction... contents);

    /// Renders the `fieldset` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement fieldset(String text);

    /// Renders the `figure` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement figure(Html.Instruction... contents);

    /// Renders the `figure` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement figure(String text);

    /// Renders the `footer` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement footer(Html.Instruction... contents);

    /// Renders the `footer` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement footer(String text);

    /// Renders the `form` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement form(Html.Instruction... contents);

    /// Renders the `g` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement g(Html.Instruction... contents);

    /// Renders the `g` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement g(String text);

    /// Renders the `h1` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h1(Html.Instruction... contents);

    /// Renders the `h1` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h1(String text);

    /// Renders the `h2` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h2(Html.Instruction... contents);

    /// Renders the `h2` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h2(String text);

    /// Renders the `h3` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h3(Html.Instruction... contents);

    /// Renders the `h3` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h3(String text);

    /// Renders the `h4` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h4(Html.Instruction... contents);

    /// Renders the `h4` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h4(String text);

    /// Renders the `h5` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h5(Html.Instruction... contents);

    /// Renders the `h5` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h5(String text);

    /// Renders the `h6` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h6(Html.Instruction... contents);

    /// Renders the `h6` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement h6(String text);

    /// Renders the `head` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement head(Html.Instruction... contents);

    /// Renders the `head` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement head(String text);

    /// Renders the `header` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement header(Html.Instruction... contents);

    /// Renders the `header` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement header(String text);

    /// Renders the `hgroup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement hgroup(Html.Instruction... contents);

    /// Renders the `hgroup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement hgroup(String text);

    /// Renders the `hr` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents);

    /// Renders the `html` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement html(Html.Instruction... contents);

    /// Renders the `html` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement html(String text);

    /// Renders the `img` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents);

    /// Renders the `input` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents);

    /// Renders the `kbd` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement kbd(Html.Instruction... contents);

    /// Renders the `kbd` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement kbd(String text);

    /// Renders the `label` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement label(Html.Instruction... contents);

    /// Renders the `legend` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement legend(Html.Instruction... contents);

    /// Renders the `legend` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement legend(String text);

    /// Renders the `li` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement li(Html.Instruction... contents);

    /// Renders the `li` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement li(String text);

    /// Renders the `link` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents);

    /// Renders the `main` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement main(Html.Instruction... contents);

    /// Renders the `main` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement main(String text);

    /// Renders the `menu` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement menu(Html.Instruction... contents);

    /// Renders the `menu` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement menu(String text);

    /// Renders the `meta` element with the specified content.
    ///
    /// @param contents the attributes of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents);

    /// Renders the `nav` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement nav(Html.Instruction... contents);

    /// Renders the `nav` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement nav(String text);

    /// Renders the `ol` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement ol(Html.Instruction... contents);

    /// Renders the `ol` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement ol(String text);

    /// Renders the `optgroup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement optgroup(Html.Instruction... contents);

    /// Renders the `optgroup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement optgroup(String text);

    /// Renders the `option` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement option(Html.Instruction... contents);

    /// Renders the `option` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement option(String text);

    /// Renders the `p` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement p(Html.Instruction... contents);

    /// Renders the `p` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement p(String text);

    /// Renders the `path` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement path(Html.Instruction... contents);

    /// Renders the `path` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement path(String text);

    /// Renders the `pre` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement pre(Html.Instruction... contents);

    /// Renders the `pre` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement pre(String text);

    /// Renders the `progress` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement progress(Html.Instruction... contents);

    /// Renders the `progress` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement progress(String text);

    /// Renders the `samp` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement samp(Html.Instruction... contents);

    /// Renders the `samp` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement samp(String text);

    /// Renders the `script` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement script(Html.Instruction... contents);

    /// Renders the `script` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement script(String text);

    /// Renders the `section` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement section(Html.Instruction... contents);

    /// Renders the `section` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement section(String text);

    /// Renders the `select` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement select(Html.Instruction... contents);

    /// Renders the `select` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement select(String text);

    /// Renders the `small` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement small(Html.Instruction... contents);

    /// Renders the `small` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement small(String text);

    /// Renders the `span` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement span(Html.Instruction... contents);

    /// Renders the `span` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement span(String text);

    /// Renders the `strong` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement strong(Html.Instruction... contents);

    /// Renders the `strong` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement strong(String text);

    /// Renders the `style` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement style(Html.Instruction... contents);

    /// Renders the `sub` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement sub(Html.Instruction... contents);

    /// Renders the `sub` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement sub(String text);

    /// Renders the `summary` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement summary(Html.Instruction... contents);

    /// Renders the `summary` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement summary(String text);

    /// Renders the `sup` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement sup(Html.Instruction... contents);

    /// Renders the `sup` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement sup(String text);

    /// Renders the `svg` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement svg(Html.Instruction... contents);

    /// Renders the `svg` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement svg(String text);

    /// Renders the `table` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement table(Html.Instruction... contents);

    /// Renders the `table` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement table(String text);

    /// Renders the `tbody` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement tbody(Html.Instruction... contents);

    /// Renders the `tbody` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement tbody(String text);

    /// Renders the `td` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement td(Html.Instruction... contents);

    /// Renders the `td` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement td(String text);

    /// Renders the `template` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement template(Html.Instruction... contents);

    /// Renders the `template` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement template(String text);

    /// Renders the `textarea` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement textarea(Html.Instruction... contents);

    /// Renders the `textarea` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement textarea(String text);

    /// Renders the `th` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement th(Html.Instruction... contents);

    /// Renders the `th` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement th(String text);

    /// Renders the `thead` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement thead(Html.Instruction... contents);

    /// Renders the `thead` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement thead(String text);

    /// Renders the `title` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement title(Html.Instruction... contents);

    /// Renders the `tr` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement tr(Html.Instruction... contents);

    /// Renders the `tr` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement tr(String text);

    /// Renders the `ul` element with the specified content.
    ///
    /// @param contents the attributes and children of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement ul(Html.Instruction... contents);

    /// Renders the `ul` element with the specified text.
    ///
    /// @param text the text value of the element
    ///
    /// @return an instruction representing the element.
    Html.Instruction.OfElement ul(String text);

    //
    // ATTRIBUTES
    //

    /// Renders an attribute with the specified name.
    ///
    /// @param name the attribute name
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute attr(Html.AttributeName name);

    /// Renders an attribute with the specified name and value.
    ///
    /// @param name the attribute name
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute attr(Html.AttributeName name, String value);

    /// Renders the `accesskey` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute accesskey(String value);

    /// Renders the `action` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute action(String value);

    /// Renders the `align` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute align(String value);

    /// Renders the `alignment-baseline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute alignmentBaseline(String value);

    /// Renders the `alt` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute alt(String value);

    /// Renders the `aria-current` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaCurrent(String value);

    /// Renders the `aria-disabled` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaDisabled(String value);

    /// Renders the `aria-hidden` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaHidden(String value);

    /// Renders the `aria-invalid` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaInvalid(String value);

    /// Renders the `aria-label` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaLabel(String value);

    /// Renders the `aria-labelledby` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaLabelledBy(String value);

    /// Renders the `aria-modal` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaModal(String value);

    /// Renders the `aria-placeholder` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaPlaceholder(String value);

    /// Renders the `aria-readonly` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaReadonly(String value);

    /// Renders the `aria-required` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaRequired(String value);

    /// Renders the `aria-selected` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ariaSelected(String value);

    /// Renders the `as` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute as(String value);

    /// Renders the `autocomplete` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute autocomplete(String value);

    /// Renders the `baseline-shift` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute baselineShift(String value);

    /// Renders the `border` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute border(String value);

    /// Renders the `cellpadding` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute cellpadding(String value);

    /// Renders the `cellspacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute cellspacing(String value);

    /// Renders the `charset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute charset(String value);

    /// Renders the `cite` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute cite(String value);

    /// Renders the `class` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute className(String value);

    /// Renders the `clip-rule` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute clipRule(String value);

    /// Renders the `closedby` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute closedby(String value);

    /// Renders the `color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute color(String value);

    /// Renders the `color-interpolation` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute colorInterpolation(String value);

    /// Renders the `color-interpolation-filters` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute colorInterpolationFilters(String value);

    /// Renders the `cols` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute cols(String value);

    /// Renders the `content` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute content(String value);

    /// Renders the `contenteditable` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute contenteditable(String value);

    /// Renders the `crossorigin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute crossorigin(String value);

    /// Renders the `cursor` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute cursor(String value);

    /// Renders the `d` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute d(String value);

    /// Renders the `dir` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute dir(String value);

    /// Renders the `direction` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute direction(String value);

    /// Renders the `dirname` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute dirname(String value);

    /// Renders the `display` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute display(String value);

    /// Renders the `dominant-baseline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute dominantBaseline(String value);

    /// Renders the `download` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute download(String value);

    /// Renders the `draggable` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute draggable(String value);

    /// Renders the `enctype` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute enctype(String value);

    /// Renders the `fill` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute fill(String value);

    /// Renders the `fill-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute fillOpacity(String value);

    /// Renders the `fill-rule` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute fillRule(String value);

    /// Renders the `filter` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute filter(String value);

    /// Renders the `flood-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute floodColor(String value);

    /// Renders the `flood-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute floodOpacity(String value);

    /// Renders the `for` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute forId(String value);

    /// Renders the `glyph-orientation-horizontal` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute glyphOrientationHorizontal(String value);

    /// Renders the `glyph-orientation-vertical` attribute with the specified
    /// value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute glyphOrientationVertical(String value);

    /// Renders the `height` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute height(String value);

    /// Renders the `href` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute href(String value);

    /// Renders the `http-equiv` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute httpEquiv(String value);

    /// Renders the `id` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute id(String value);

    /// Renders the `image-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute imageRendering(String value);

    /// Renders the `integrity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute integrity(String value);

    /// Renders the `lang` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute lang(String value);

    /// Renders the `letter-spacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute letterSpacing(String value);

    /// Renders the `lighting-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute lightingColor(String value);

    /// Renders the `marker-end` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute markerEnd(String value);

    /// Renders the `marker-mid` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute markerMid(String value);

    /// Renders the `marker-start` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute markerStart(String value);

    /// Renders the `mask` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute mask(String value);

    /// Renders the `mask-type` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute maskType(String value);

    /// Renders the `maxlength` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute maxlength(String value);

    /// Renders the `media` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute media(String value);

    /// Renders the `method` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute method(String value);

    /// Renders the `minlength` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute minlength(String value);

    /// Renders the `name` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute name(String value);

    /// Renders the `onafterprint` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onafterprint(String value);

    /// Renders the `onbeforeprint` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onbeforeprint(String value);

    /// Renders the `onbeforeunload` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onbeforeunload(String value);

    /// Renders the `onclick` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onclick(String value);

    /// Renders the `onhashchange` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onhashchange(String value);

    /// Renders the `onlanguagechange` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onlanguagechange(String value);

    /// Renders the `onmessage` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onmessage(String value);

    /// Renders the `onoffline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onoffline(String value);

    /// Renders the `ononline` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute ononline(String value);

    /// Renders the `onpagehide` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onpagehide(String value);

    /// Renders the `onpageshow` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onpageshow(String value);

    /// Renders the `onpopstate` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onpopstate(String value);

    /// Renders the `onrejectionhandled` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onrejectionhandled(String value);

    /// Renders the `onstorage` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onstorage(String value);

    /// Renders the `onsubmit` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onsubmit(String value);

    /// Renders the `onunhandledrejection` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onunhandledrejection(String value);

    /// Renders the `onunload` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute onunload(String value);

    /// Renders the `opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute opacity(String value);

    /// Renders the `overflow` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute overflow(String value);

    /// Renders the `paint-order` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute paintOrder(String value);

    /// Renders the `placeholder` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute placeholder(String value);

    /// Renders the `pointer-events` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute pointerEvents(String value);

    /// Renders the `property` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute property(String value);

    /// Renders the `referrerpolicy` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute referrerpolicy(String value);

    /// Renders the `rel` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute rel(String value);

    /// Renders the `rev` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute rev(String value);

    /// Renders the `role` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute role(String value);

    /// Renders the `rows` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute rows(String value);

    /// Renders the `shape-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute shapeRendering(String value);

    /// Renders the `size` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute size(String value);

    /// Renders the `sizes` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute sizes(String value);

    /// Renders the `spellcheck` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute spellcheck(String value);

    /// Renders the `src` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute src(String value);

    /// Renders the `srcset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute srcset(String value);

    /// Renders the `start` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute start(String value);

    /// Renders the `stop-color` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute stopColor(String value);

    /// Renders the `stop-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute stopOpacity(String value);

    /// Renders the `stroke` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute stroke(String value);

    /// Renders the `stroke-dasharray` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeDasharray(String value);

    /// Renders the `stroke-dashoffset` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeDashoffset(String value);

    /// Renders the `stroke-linecap` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeLinecap(String value);

    /// Renders the `stroke-linejoin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeLinejoin(String value);

    /// Renders the `stroke-miterlimit` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeMiterlimit(String value);

    /// Renders the `stroke-opacity` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeOpacity(String value);

    /// Renders the `stroke-width` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute strokeWidth(String value);

    /// Renders the `tabindex` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute tabindex(String value);

    /// Renders the `target` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute target(String value);

    /// Renders the `text-anchor` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute textAnchor(String value);

    /// Renders the `text-decoration` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute textDecoration(String value);

    /// Renders the `text-overflow` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute textOverflow(String value);

    /// Renders the `text-rendering` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute textRendering(String value);

    /// Renders the `transform` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute transform(String value);

    /// Renders the `transform-origin` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute transformOrigin(String value);

    /// Renders the `translate` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute translate(String value);

    /// Renders the `type` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute type(String value);

    /// Renders the `unicode-bidi` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute unicodeBidi(String value);

    /// Renders the `value` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute value(String value);

    /// Renders the `vector-effect` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute vectorEffect(String value);

    /// Renders the `viewBox` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute viewBox(String value);

    /// Renders the `visibility` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute visibility(String value);

    /// Renders the `white-space` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute whiteSpace(String value);

    /// Renders the `width` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute width(String value);

    /// Renders the `word-spacing` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute wordSpacing(String value);

    /// Renders the `wrap` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute wrap(String value);

    /// Renders the `writing-mode` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute writingMode(String value);

    /// Renders the `xmlns` attribute with the specified value.
    ///
    /// @param value the attribute value
    ///
    /// @return an instruction representing the attribute
    Html.Instruction.OfAttribute xmlns(String value);

    //
    // AMBIGUOUS
    //

    /// Renders the `clip-path` attribute or the `clipPath` element with the
    /// specified text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    Html.Instruction.OfAmbiguous clipPath(String text);

    /// Renders the `form` attribute or the `form` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    Html.Instruction.OfAmbiguous form(String text);

    /// Renders the `label` attribute or the `label` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    Html.Instruction.OfAmbiguous label(String text);

    /// Renders the `style` attribute or the `style` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    Html.Instruction.OfAmbiguous style(String text);

    /// Renders the `title` attribute or the `title` element with the specified
    /// text.
    ///
    /// @param text the attribute value or the text content of the element
    ///
    /// @return an instruction representing the attribute or the element
    Html.Instruction.OfAmbiguous title(String text);

    //
    // TEXT
    //

    /// Renders the non-breaking space `&nbsp;` HTML character entity.
    ///
    /// @return an instruction representing the non-breaking space
    ///         character entity.
    Html.Instruction.OfElement nbsp();

    /// Renders the specified value as raw HTML.
    ///
    /// @param value the raw HTML value
    ///
    /// @return a raw HTML instruction
    Html.Instruction.OfElement raw(String value);

    /// Renders a text node with the specified value. The text value is escaped
    /// before being emitted to the output.
    ///
    /// @param value the text value
    ///
    /// @return an instruction representing the text node
    Html.Instruction.OfElement text(String value);

    // END generated code

  }

  /**
   * A template in pure Java for generating HTML.
   *
   * <p>
   * This class provides methods for representing HTML code in a Java class. An
   * instance of the class can then be used to generate the represented HTML
   * code.
   */
  public static abstract class Template implements Component, Testable {

    Html.Markup html;

    /**
     * Sole constructor.
     */
    protected Template() {}

    /**
     * Returns {@code text/html; charset=utf-8}.
     *
     * @return always {@code text/html; charset=utf-8}
     */
    @Override
    public final String contentType() {
      return "text/html; charset=utf-8";
    }

    @Override
    public final void formatTestable(Testable.Formatter formatter) {
      final Html.Markup html;
      html = new Html.Markup.OfTestable(formatter);

      renderHtml(html);
    }

    @Override
    public final void renderHtml(Html.Markup m) {
      Check.state(html == null, "Concurrent evalution of a HtmlTemplate is not supported");

      try {
        html = m;

        render();
      } finally {
        html = null;
      }
    }

    /// Returns the HTML generated by this template suited to be used in JSON.
    ///
    /// @return the HTML generated by this template suited to be used in JSON
    public final String toJsonString() {
      final Html.Markup html;
      html = new Html.Markup.OfHtml();

      renderHtml(html);

      final HtmlMarkupOfHtml impl;
      impl = (HtmlMarkupOfHtml) html;

      return impl.toJsonString();
    }

    /**
     * Returns the HTML generated by this template.
     *
     * @return the HTML generated by this template
     */
    @Override
    public final String toString() {
      return toHtml();
    }

    /**
     * Defines the HTML code to be generated by this template.
     */
    protected abstract void render();

    private Html.Markup $html() {
      Check.state(html != null, "html not set");

      return html;
    }

    /// Renders the specified attribute at the root of this template or
    /// fragment.
    ///
    /// @param object the attribute
    ///
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(Html.AttributeObject object) {
      return $html().attr(object);
    }

    // START generated code

    /// The `async` boolean attribute.
    protected static final Html.AttributeObject async = Html.Markup.async;

    /// The `autofocus` boolean attribute.
    protected static final Html.AttributeObject autofocus = Html.Markup.autofocus;

    /// The `checked` boolean attribute.
    protected static final Html.AttributeObject checked = Html.Markup.checked;

    /// The `defer` boolean attribute.
    protected static final Html.AttributeObject defer = Html.Markup.defer;

    /// The `disabled` boolean attribute.
    protected static final Html.AttributeObject disabled = Html.Markup.disabled;

    /// The `hidden` boolean attribute.
    protected static final Html.AttributeObject hidden = Html.Markup.hidden;

    /// The `multiple` boolean attribute.
    protected static final Html.AttributeObject multiple = Html.Markup.multiple;

    /// The `nomodule` boolean attribute.
    protected static final Html.AttributeObject nomodule = Html.Markup.nomodule;

    /// The `open` boolean attribute.
    protected static final Html.AttributeObject open = Html.Markup.open;

    /// The `readonly` boolean attribute.
    protected static final Html.AttributeObject readonly = Html.Markup.readonly;

    /// The `required` boolean attribute.
    protected static final Html.AttributeObject required = Html.Markup.required;

    /// The `reversed` boolean attribute.
    protected static final Html.AttributeObject reversed = Html.Markup.reversed;

    /// The `selected` boolean attribute.
    protected static final Html.AttributeObject selected = Html.Markup.selected;

    //
    // WAY
    //

    /// Renders the `data-on-click` attribute with the specified script.
    /// 
    /// @param script the script to be executed
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfDataOn dataOnClick(Script.JsAction script) {
      return $html().dataOnClick(script);
    }

    /// Renders the `data-on-input` attribute with the specified script.
    /// 
    /// @param script the script to be executed
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfDataOn dataOnInput(Script.JsAction script) {
      return $html().dataOnInput(script);
    }

    /// Renders the `data-on-load` attribute with the specified script.
    /// 
    /// @param script the script to be executed
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfDataOn dataOnLoad(Script.JsAction script) {
      return $html().dataOnLoad(script);
    }

    /// Renders the `data-on-success` attribute with the specified script.
    /// 
    /// @param script the script to be executed
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfDataOn dataOnSuccess(Script.JsAction script) {
      return $html().dataOnSuccess(script);
    }

    /// Renders the `data-frame` attribute for a frame with the specified name.
    /// 
    /// @param name the name of the frame
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dataFrame(String name) {
      return $html().dataFrame(name);
    }

    /// Renders the `data-frame` attribute for a frame with the specified name
    /// and value.
    /// 
    /// @param name the name of the frame
    /// @param value the value of the frame
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dataFrame(String name, String value) {
      return $html().dataFrame(name, value);
    }

    /// Renders the `class` attribute by processing the specified value.
    ///
    /// This method is designed to work with Java text blocks. It first removes
    /// any leading and trailing whitespace. Additionally, any sequence of
    /// consecutive whitespace characters is replaced by a single space
    /// character.
    ///
    /// For example, the following invocation:
    ///
    /// ```java css(""" display:inline-flex justify-content:center
    ///
    /// background-color:blue-500 """); ```
    ///
    /// Produces the same result as invoking `className("display:inline-flex
    /// justify-content:center background-color:blue-500")`.
    ///
    /// @param value the text block containing class names, possibly spread
    ///        across multiple lines
    /// 
    /// @return an instruction representing this attribute.
    protected final Html.Instruction.OfAttribute css(String value) {
      return $html().css(value);
    }

    /// Renders the specified components in order as part of this document.
    /// 
    /// @param components the components to be rendered as part of this document
    /// 
    /// @return an instruction representing the rendered components.
    protected final Html.Instruction.OfFragment c(Html.Component... components) {
      return $html().c(components);
    }

    /// Renders the specified components in order as part of this document.
    /// 
    /// @param components the components to be rendered as part of this document
    /// 
    /// @return an instruction representing the rendered components.
    protected final Html.Instruction.OfFragment c(Iterable<? extends Html.Component> components) {
      return $html().c(components);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f0"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html
    /// <ul>
    /// <li>Mon</li>
    /// <li>Wed</li>
    /// <li>Fri</li>
    /// </ul>
    /// ```
    ///
    /// @param fragment the fragment to include
    /// 
    /// @return an instruction representing the fragment
    protected final Html.Instruction.OfFragment f(Html.Fragment.Of0 fragment) {
      return $html().f(fragment);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f1"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html
    /// <ul>
    /// <li>Mon</li>
    /// <li>Wed</li>
    /// <li>Fri</li>
    /// </ul>
    /// ```
    ///
    /// @param <T1> the type of the first argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// 
    /// @return an instruction representing the fragment
    protected final <T1> Html.Instruction.OfFragment f(Html.Fragment.Of1<T1> fragment, T1 arg1) {
      return $html().f(fragment, arg1);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f2"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html <div><button>OK</button><button>Cancel</button></div> ```
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    /// 
    /// @return an instruction representing the fragment
    protected final <T1, T2> Html.Instruction.OfFragment f(Html.Fragment.Of2<T1, T2> fragment, T1 arg1, T2 arg2) {
      return $html().f(fragment, arg1, arg2);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// The following Objectos HTML component:
    ///
    /// {@snippet file = "objectos/way/HtmlMarkupJavadoc.java" region = "f3"}
    ///
    /// Generates the following HTML:
    ///
    /// ```html <div>
    /// <p>
    /// City<span>Tokyo</span>
    /// </p>
    /// <p>
    /// Country<span>Japan</span>
    /// </p>
    /// </div> ```
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    /// @param arg3 the third argument
    /// 
    /// @return an instruction representing the fragment
    protected final <T1, T2, T3> Html.Instruction.OfFragment f(Html.Fragment.Of3<T1, T2, T3> fragment, T1 arg1, T2 arg2, T3 arg3) {
      return $html().f(fragment, arg1, arg2, arg3);
    }

    /// Renders the specified fragment as part of this document.
    ///
    /// @param <T1> the type of the first argument
    /// @param <T2> the type of the second argument
    /// @param <T3> the type of the third argument
    /// @param <T4> the type of the fourth argument
    /// @param fragment the fragment to include
    /// @param arg1 the first argument
    /// @param arg2 the second argument
    /// @param arg3 the third argument
    /// @param arg4 the fourth argument
    /// 
    /// @return an instruction representing the fragment
    protected final <T1, T2, T3, T4> Html.Instruction.OfFragment f(Html.Fragment.Of4<T1, T2, T3, T4> fragment, T1 arg1, T2 arg2, T3 arg3, T4 arg4) {
      return $html().f(fragment, arg1, arg2, arg3, arg4);
    }

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    /// 
    /// @param contents the instructions to be flattened
    /// 
    /// @return an instruction representing this flatten operation
    protected final Html.Instruction.OfElement flatten(Html.Instruction... contents) {
      return $html().flatten(contents);
    }

    /// Flattens the specified instructions so that each of the specified
    /// instructions is individually added, in order, to a receiving element.
    /// 
    /// @param contents the instructions to be flattened
    /// 
    /// @return an instruction representing this flatten operation
    protected final Html.Instruction.OfElement flatten(Iterable<? extends Html.Instruction> contents) {
      return $html().flatten(contents);
    }

    /// The no-op instruction.
    /// 
    /// @return the no-op instruction.
    protected final Html.Instruction.NoOp noop() {
      return $html().noop();
    }

    //
    // TESTABLE
    //

    /// Formats the specified value as a testable table cell with the specified
    /// fixed width.
    /// 
    /// @param value the cell value
    /// @param width the fixed width of the cell
    /// 
    /// @return always the cell value
    protected final String testableCell(String value, int width) {
      return $html().testableCell(value, width);
    }

    /// Formats the specified name and value as a testable field.
    /// 
    /// @param name the field name
    /// @param value the field value
    /// 
    /// @return always the field value
    protected final String testableField(String name, String value) {
      return $html().testableField(name, value);
    }

    /// Formats the specified name as a testable field name.
    /// 
    /// @param name the field name
    /// 
    /// @return the specified field name
    protected final String testableFieldName(String name) {
      return $html().testableFieldName(name);
    }

    /// Formats the specified value as a testable field value.
    /// 
    /// @param value the field value
    /// 
    /// @return the specified field value
    protected final String testableFieldValue(String value) {
      return $html().testableFieldValue(value);
    }

    /// Formats the specified value as a testable heading level 1.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH1(String value) {
      return $html().testableH1(value);
    }

    /// Formats the specified value as a testable heading level 2.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH2(String value) {
      return $html().testableH2(value);
    }

    /// Formats the specified value as a testable heading level 3.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH3(String value) {
      return $html().testableH3(value);
    }

    /// Formats the specified value as a testable heading level 4.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH4(String value) {
      return $html().testableH4(value);
    }

    /// Formats the specified value as a testable heading level 5.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH5(String value) {
      return $html().testableH5(value);
    }

    /// Formats the specified value as a testable heading level 6.
    /// 
    /// @param value the heading value
    /// 
    /// @return the specified value
    protected final String testableH6(String value) {
      return $html().testableH6(value);
    }

    /// Formats a line separator at the testable output exclusively.
    /// 
    /// @return a no-op instruction
    protected final Html.Instruction.NoOp testableNewLine() {
      return $html().testableNewLine();
    }

    //
    // ELEMENTS
    //

    /// Renders an HTML element with the specified name and contents.
    /// 
    /// @param name the element name
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element
    protected final Html.Instruction.OfElement elem(Html.ElementName name, Html.Instruction... contents) {
      return $html().elem(name, contents);
    }

    /// Renders an HTML element with the specified name and text.
    /// 
    /// @param name the element name
    /// @param text the text value of this element
    /// 
    /// @return an instruction representing the element
    protected final Html.Instruction.OfElement elem(Html.ElementName name, String text) {
      return $html().elem(name, text);
    }

    /// Renders the `<!DOCTYPE html>` doctype.
    protected final void doctype() {
      $html().doctype();
    }

    /// Renders the `a` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement a(Html.Instruction... contents) {
      return $html().a(contents);
    }

    /// Renders the `a` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement a(String text) {
      return $html().a(text);
    }

    /// Renders the `abbr` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement abbr(Html.Instruction... contents) {
      return $html().abbr(contents);
    }

    /// Renders the `abbr` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement abbr(String text) {
      return $html().abbr(text);
    }

    /// Renders the `article` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement article(Html.Instruction... contents) {
      return $html().article(contents);
    }

    /// Renders the `article` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement article(String text) {
      return $html().article(text);
    }

    /// Renders the `aside` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement aside(Html.Instruction... contents) {
      return $html().aside(contents);
    }

    /// Renders the `aside` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement aside(String text) {
      return $html().aside(text);
    }

    /// Renders the `b` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement b(Html.Instruction... contents) {
      return $html().b(contents);
    }

    /// Renders the `b` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement b(String text) {
      return $html().b(text);
    }

    /// Renders the `blockquote` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement blockquote(Html.Instruction... contents) {
      return $html().blockquote(contents);
    }

    /// Renders the `blockquote` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement blockquote(String text) {
      return $html().blockquote(text);
    }

    /// Renders the `body` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement body(Html.Instruction... contents) {
      return $html().body(contents);
    }

    /// Renders the `body` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement body(String text) {
      return $html().body(text);
    }

    /// Renders the `br` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement br(Html.Instruction.OfVoid... contents) {
      return $html().br(contents);
    }

    /// Renders the `button` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement button(Html.Instruction... contents) {
      return $html().button(contents);
    }

    /// Renders the `button` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement button(String text) {
      return $html().button(text);
    }

    /// Renders the `clipPath` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement clipPath(Html.Instruction... contents) {
      return $html().clipPath(contents);
    }

    /// Renders the `code` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement code(Html.Instruction... contents) {
      return $html().code(contents);
    }

    /// Renders the `code` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement code(String text) {
      return $html().code(text);
    }

    /// Renders the `dd` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dd(Html.Instruction... contents) {
      return $html().dd(contents);
    }

    /// Renders the `dd` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dd(String text) {
      return $html().dd(text);
    }

    /// Renders the `defs` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement defs(Html.Instruction... contents) {
      return $html().defs(contents);
    }

    /// Renders the `defs` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement defs(String text) {
      return $html().defs(text);
    }

    /// Renders the `details` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement details(Html.Instruction... contents) {
      return $html().details(contents);
    }

    /// Renders the `details` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement details(String text) {
      return $html().details(text);
    }

    /// Renders the `dialog` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dialog(Html.Instruction... contents) {
      return $html().dialog(contents);
    }

    /// Renders the `dialog` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dialog(String text) {
      return $html().dialog(text);
    }

    /// Renders the `div` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement div(Html.Instruction... contents) {
      return $html().div(contents);
    }

    /// Renders the `div` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement div(String text) {
      return $html().div(text);
    }

    /// Renders the `dl` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dl(Html.Instruction... contents) {
      return $html().dl(contents);
    }

    /// Renders the `dl` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dl(String text) {
      return $html().dl(text);
    }

    /// Renders the `dt` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dt(Html.Instruction... contents) {
      return $html().dt(contents);
    }

    /// Renders the `dt` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement dt(String text) {
      return $html().dt(text);
    }

    /// Renders the `em` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement em(Html.Instruction... contents) {
      return $html().em(contents);
    }

    /// Renders the `em` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement em(String text) {
      return $html().em(text);
    }

    /// Renders the `fieldset` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement fieldset(Html.Instruction... contents) {
      return $html().fieldset(contents);
    }

    /// Renders the `fieldset` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement fieldset(String text) {
      return $html().fieldset(text);
    }

    /// Renders the `figure` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement figure(Html.Instruction... contents) {
      return $html().figure(contents);
    }

    /// Renders the `figure` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement figure(String text) {
      return $html().figure(text);
    }

    /// Renders the `footer` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement footer(Html.Instruction... contents) {
      return $html().footer(contents);
    }

    /// Renders the `footer` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement footer(String text) {
      return $html().footer(text);
    }

    /// Renders the `form` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement form(Html.Instruction... contents) {
      return $html().form(contents);
    }

    /// Renders the `g` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement g(Html.Instruction... contents) {
      return $html().g(contents);
    }

    /// Renders the `g` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement g(String text) {
      return $html().g(text);
    }

    /// Renders the `h1` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h1(Html.Instruction... contents) {
      return $html().h1(contents);
    }

    /// Renders the `h1` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h1(String text) {
      return $html().h1(text);
    }

    /// Renders the `h2` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h2(Html.Instruction... contents) {
      return $html().h2(contents);
    }

    /// Renders the `h2` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h2(String text) {
      return $html().h2(text);
    }

    /// Renders the `h3` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h3(Html.Instruction... contents) {
      return $html().h3(contents);
    }

    /// Renders the `h3` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h3(String text) {
      return $html().h3(text);
    }

    /// Renders the `h4` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h4(Html.Instruction... contents) {
      return $html().h4(contents);
    }

    /// Renders the `h4` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h4(String text) {
      return $html().h4(text);
    }

    /// Renders the `h5` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h5(Html.Instruction... contents) {
      return $html().h5(contents);
    }

    /// Renders the `h5` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h5(String text) {
      return $html().h5(text);
    }

    /// Renders the `h6` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h6(Html.Instruction... contents) {
      return $html().h6(contents);
    }

    /// Renders the `h6` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement h6(String text) {
      return $html().h6(text);
    }

    /// Renders the `head` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement head(Html.Instruction... contents) {
      return $html().head(contents);
    }

    /// Renders the `head` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement head(String text) {
      return $html().head(text);
    }

    /// Renders the `header` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement header(Html.Instruction... contents) {
      return $html().header(contents);
    }

    /// Renders the `header` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement header(String text) {
      return $html().header(text);
    }

    /// Renders the `hgroup` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hgroup(Html.Instruction... contents) {
      return $html().hgroup(contents);
    }

    /// Renders the `hgroup` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hgroup(String text) {
      return $html().hgroup(text);
    }

    /// Renders the `hr` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement hr(Html.Instruction.OfVoid... contents) {
      return $html().hr(contents);
    }

    /// Renders the `html` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement html(Html.Instruction... contents) {
      return $html().html(contents);
    }

    /// Renders the `html` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement html(String text) {
      return $html().html(text);
    }

    /// Renders the `img` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement img(Html.Instruction.OfVoid... contents) {
      return $html().img(contents);
    }

    /// Renders the `input` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement input(Html.Instruction.OfVoid... contents) {
      return $html().input(contents);
    }

    /// Renders the `kbd` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement kbd(Html.Instruction... contents) {
      return $html().kbd(contents);
    }

    /// Renders the `kbd` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement kbd(String text) {
      return $html().kbd(text);
    }

    /// Renders the `label` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement label(Html.Instruction... contents) {
      return $html().label(contents);
    }

    /// Renders the `legend` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement legend(Html.Instruction... contents) {
      return $html().legend(contents);
    }

    /// Renders the `legend` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement legend(String text) {
      return $html().legend(text);
    }

    /// Renders the `li` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement li(Html.Instruction... contents) {
      return $html().li(contents);
    }

    /// Renders the `li` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement li(String text) {
      return $html().li(text);
    }

    /// Renders the `link` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement link(Html.Instruction.OfVoid... contents) {
      return $html().link(contents);
    }

    /// Renders the `main` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement main(Html.Instruction... contents) {
      return $html().main(contents);
    }

    /// Renders the `main` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement main(String text) {
      return $html().main(text);
    }

    /// Renders the `menu` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement menu(Html.Instruction... contents) {
      return $html().menu(contents);
    }

    /// Renders the `menu` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement menu(String text) {
      return $html().menu(text);
    }

    /// Renders the `meta` element with the specified content.
    /// 
    /// @param contents the attributes of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement meta(Html.Instruction.OfVoid... contents) {
      return $html().meta(contents);
    }

    /// Renders the `nav` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement nav(Html.Instruction... contents) {
      return $html().nav(contents);
    }

    /// Renders the `nav` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement nav(String text) {
      return $html().nav(text);
    }

    /// Renders the `ol` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ol(Html.Instruction... contents) {
      return $html().ol(contents);
    }

    /// Renders the `ol` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ol(String text) {
      return $html().ol(text);
    }

    /// Renders the `optgroup` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement optgroup(Html.Instruction... contents) {
      return $html().optgroup(contents);
    }

    /// Renders the `optgroup` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement optgroup(String text) {
      return $html().optgroup(text);
    }

    /// Renders the `option` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement option(Html.Instruction... contents) {
      return $html().option(contents);
    }

    /// Renders the `option` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement option(String text) {
      return $html().option(text);
    }

    /// Renders the `p` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement p(Html.Instruction... contents) {
      return $html().p(contents);
    }

    /// Renders the `p` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement p(String text) {
      return $html().p(text);
    }

    /// Renders the `path` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement path(Html.Instruction... contents) {
      return $html().path(contents);
    }

    /// Renders the `path` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement path(String text) {
      return $html().path(text);
    }

    /// Renders the `pre` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement pre(Html.Instruction... contents) {
      return $html().pre(contents);
    }

    /// Renders the `pre` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement pre(String text) {
      return $html().pre(text);
    }

    /// Renders the `progress` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement progress(Html.Instruction... contents) {
      return $html().progress(contents);
    }

    /// Renders the `progress` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement progress(String text) {
      return $html().progress(text);
    }

    /// Renders the `samp` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement samp(Html.Instruction... contents) {
      return $html().samp(contents);
    }

    /// Renders the `samp` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement samp(String text) {
      return $html().samp(text);
    }

    /// Renders the `script` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement script(Html.Instruction... contents) {
      return $html().script(contents);
    }

    /// Renders the `script` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement script(String text) {
      return $html().script(text);
    }

    /// Renders the `section` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement section(Html.Instruction... contents) {
      return $html().section(contents);
    }

    /// Renders the `section` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement section(String text) {
      return $html().section(text);
    }

    /// Renders the `select` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement select(Html.Instruction... contents) {
      return $html().select(contents);
    }

    /// Renders the `select` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement select(String text) {
      return $html().select(text);
    }

    /// Renders the `small` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement small(Html.Instruction... contents) {
      return $html().small(contents);
    }

    /// Renders the `small` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement small(String text) {
      return $html().small(text);
    }

    /// Renders the `span` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement span(Html.Instruction... contents) {
      return $html().span(contents);
    }

    /// Renders the `span` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement span(String text) {
      return $html().span(text);
    }

    /// Renders the `strong` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement strong(Html.Instruction... contents) {
      return $html().strong(contents);
    }

    /// Renders the `strong` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement strong(String text) {
      return $html().strong(text);
    }

    /// Renders the `style` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement style(Html.Instruction... contents) {
      return $html().style(contents);
    }

    /// Renders the `sub` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sub(Html.Instruction... contents) {
      return $html().sub(contents);
    }

    /// Renders the `sub` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sub(String text) {
      return $html().sub(text);
    }

    /// Renders the `summary` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement summary(Html.Instruction... contents) {
      return $html().summary(contents);
    }

    /// Renders the `summary` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement summary(String text) {
      return $html().summary(text);
    }

    /// Renders the `sup` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sup(Html.Instruction... contents) {
      return $html().sup(contents);
    }

    /// Renders the `sup` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement sup(String text) {
      return $html().sup(text);
    }

    /// Renders the `svg` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement svg(Html.Instruction... contents) {
      return $html().svg(contents);
    }

    /// Renders the `svg` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement svg(String text) {
      return $html().svg(text);
    }

    /// Renders the `table` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement table(Html.Instruction... contents) {
      return $html().table(contents);
    }

    /// Renders the `table` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement table(String text) {
      return $html().table(text);
    }

    /// Renders the `tbody` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tbody(Html.Instruction... contents) {
      return $html().tbody(contents);
    }

    /// Renders the `tbody` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tbody(String text) {
      return $html().tbody(text);
    }

    /// Renders the `td` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement td(Html.Instruction... contents) {
      return $html().td(contents);
    }

    /// Renders the `td` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement td(String text) {
      return $html().td(text);
    }

    /// Renders the `template` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement template(Html.Instruction... contents) {
      return $html().template(contents);
    }

    /// Renders the `template` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement template(String text) {
      return $html().template(text);
    }

    /// Renders the `textarea` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement textarea(Html.Instruction... contents) {
      return $html().textarea(contents);
    }

    /// Renders the `textarea` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement textarea(String text) {
      return $html().textarea(text);
    }

    /// Renders the `th` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement th(Html.Instruction... contents) {
      return $html().th(contents);
    }

    /// Renders the `th` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement th(String text) {
      return $html().th(text);
    }

    /// Renders the `thead` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement thead(Html.Instruction... contents) {
      return $html().thead(contents);
    }

    /// Renders the `thead` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement thead(String text) {
      return $html().thead(text);
    }

    /// Renders the `title` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement title(Html.Instruction... contents) {
      return $html().title(contents);
    }

    /// Renders the `tr` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tr(Html.Instruction... contents) {
      return $html().tr(contents);
    }

    /// Renders the `tr` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement tr(String text) {
      return $html().tr(text);
    }

    /// Renders the `ul` element with the specified content.
    /// 
    /// @param contents the attributes and children of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ul(Html.Instruction... contents) {
      return $html().ul(contents);
    }

    /// Renders the `ul` element with the specified text.
    /// 
    /// @param text the text value of the element
    /// 
    /// @return an instruction representing the element.
    protected final Html.Instruction.OfElement ul(String text) {
      return $html().ul(text);
    }

    //
    // ATTRIBUTES
    //

    /// Renders an attribute with the specified name.
    /// 
    /// @param name the attribute name
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(Html.AttributeName name) {
      return $html().attr(name);
    }

    /// Renders an attribute with the specified name and value.
    /// 
    /// @param name the attribute name
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute attr(Html.AttributeName name, String value) {
      return $html().attr(name, value);
    }

    /// Renders the `accesskey` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute accesskey(String value) {
      return $html().accesskey(value);
    }

    /// Renders the `action` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute action(String value) {
      return $html().action(value);
    }

    /// Renders the `align` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute align(String value) {
      return $html().align(value);
    }

    /// Renders the `alignment-baseline` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute alignmentBaseline(String value) {
      return $html().alignmentBaseline(value);
    }

    /// Renders the `alt` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute alt(String value) {
      return $html().alt(value);
    }

    /// Renders the `aria-current` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaCurrent(String value) {
      return $html().ariaCurrent(value);
    }

    /// Renders the `aria-disabled` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaDisabled(String value) {
      return $html().ariaDisabled(value);
    }

    /// Renders the `aria-hidden` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaHidden(String value) {
      return $html().ariaHidden(value);
    }

    /// Renders the `aria-invalid` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaInvalid(String value) {
      return $html().ariaInvalid(value);
    }

    /// Renders the `aria-label` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaLabel(String value) {
      return $html().ariaLabel(value);
    }

    /// Renders the `aria-labelledby` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaLabelledBy(String value) {
      return $html().ariaLabelledBy(value);
    }

    /// Renders the `aria-modal` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaModal(String value) {
      return $html().ariaModal(value);
    }

    /// Renders the `aria-placeholder` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaPlaceholder(String value) {
      return $html().ariaPlaceholder(value);
    }

    /// Renders the `aria-readonly` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaReadonly(String value) {
      return $html().ariaReadonly(value);
    }

    /// Renders the `aria-required` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaRequired(String value) {
      return $html().ariaRequired(value);
    }

    /// Renders the `aria-selected` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ariaSelected(String value) {
      return $html().ariaSelected(value);
    }

    /// Renders the `as` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute as(String value) {
      return $html().as(value);
    }

    /// Renders the `autocomplete` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute autocomplete(String value) {
      return $html().autocomplete(value);
    }

    /// Renders the `baseline-shift` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute baselineShift(String value) {
      return $html().baselineShift(value);
    }

    /// Renders the `border` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute border(String value) {
      return $html().border(value);
    }

    /// Renders the `cellpadding` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cellpadding(String value) {
      return $html().cellpadding(value);
    }

    /// Renders the `cellspacing` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cellspacing(String value) {
      return $html().cellspacing(value);
    }

    /// Renders the `charset` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute charset(String value) {
      return $html().charset(value);
    }

    /// Renders the `cite` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cite(String value) {
      return $html().cite(value);
    }

    /// Renders the `class` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute className(String value) {
      return $html().className(value);
    }

    /// Renders the `clip-rule` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute clipRule(String value) {
      return $html().clipRule(value);
    }

    /// Renders the `closedby` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute closedby(String value) {
      return $html().closedby(value);
    }

    /// Renders the `color` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute color(String value) {
      return $html().color(value);
    }

    /// Renders the `color-interpolation` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute colorInterpolation(String value) {
      return $html().colorInterpolation(value);
    }

    /// Renders the `color-interpolation-filters` attribute with the specified
    /// value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute colorInterpolationFilters(String value) {
      return $html().colorInterpolationFilters(value);
    }

    /// Renders the `cols` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cols(String value) {
      return $html().cols(value);
    }

    /// Renders the `content` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute content(String value) {
      return $html().content(value);
    }

    /// Renders the `contenteditable` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute contenteditable(String value) {
      return $html().contenteditable(value);
    }

    /// Renders the `crossorigin` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute crossorigin(String value) {
      return $html().crossorigin(value);
    }

    /// Renders the `cursor` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute cursor(String value) {
      return $html().cursor(value);
    }

    /// Renders the `d` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute d(String value) {
      return $html().d(value);
    }

    /// Renders the `dir` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dir(String value) {
      return $html().dir(value);
    }

    /// Renders the `direction` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute direction(String value) {
      return $html().direction(value);
    }

    /// Renders the `dirname` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dirname(String value) {
      return $html().dirname(value);
    }

    /// Renders the `display` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute display(String value) {
      return $html().display(value);
    }

    /// Renders the `dominant-baseline` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute dominantBaseline(String value) {
      return $html().dominantBaseline(value);
    }

    /// Renders the `download` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute download(String value) {
      return $html().download(value);
    }

    /// Renders the `draggable` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute draggable(String value) {
      return $html().draggable(value);
    }

    /// Renders the `enctype` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute enctype(String value) {
      return $html().enctype(value);
    }

    /// Renders the `fill` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fill(String value) {
      return $html().fill(value);
    }

    /// Renders the `fill-opacity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fillOpacity(String value) {
      return $html().fillOpacity(value);
    }

    /// Renders the `fill-rule` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute fillRule(String value) {
      return $html().fillRule(value);
    }

    /// Renders the `filter` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute filter(String value) {
      return $html().filter(value);
    }

    /// Renders the `flood-color` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute floodColor(String value) {
      return $html().floodColor(value);
    }

    /// Renders the `flood-opacity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute floodOpacity(String value) {
      return $html().floodOpacity(value);
    }

    /// Renders the `for` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute forId(String value) {
      return $html().forId(value);
    }

    /// Renders the `glyph-orientation-horizontal` attribute with the specified
    /// value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute glyphOrientationHorizontal(String value) {
      return $html().glyphOrientationHorizontal(value);
    }

    /// Renders the `glyph-orientation-vertical` attribute with the specified
    /// value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute glyphOrientationVertical(String value) {
      return $html().glyphOrientationVertical(value);
    }

    /// Renders the `height` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute height(String value) {
      return $html().height(value);
    }

    /// Renders the `href` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute href(String value) {
      return $html().href(value);
    }

    /// Renders the `http-equiv` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute httpEquiv(String value) {
      return $html().httpEquiv(value);
    }

    /// Renders the `id` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute id(String value) {
      return $html().id(value);
    }

    /// Renders the `image-rendering` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute imageRendering(String value) {
      return $html().imageRendering(value);
    }

    /// Renders the `integrity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute integrity(String value) {
      return $html().integrity(value);
    }

    /// Renders the `lang` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute lang(String value) {
      return $html().lang(value);
    }

    /// Renders the `letter-spacing` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute letterSpacing(String value) {
      return $html().letterSpacing(value);
    }

    /// Renders the `lighting-color` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute lightingColor(String value) {
      return $html().lightingColor(value);
    }

    /// Renders the `marker-end` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerEnd(String value) {
      return $html().markerEnd(value);
    }

    /// Renders the `marker-mid` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerMid(String value) {
      return $html().markerMid(value);
    }

    /// Renders the `marker-start` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute markerStart(String value) {
      return $html().markerStart(value);
    }

    /// Renders the `mask` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute mask(String value) {
      return $html().mask(value);
    }

    /// Renders the `mask-type` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute maskType(String value) {
      return $html().maskType(value);
    }

    /// Renders the `maxlength` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute maxlength(String value) {
      return $html().maxlength(value);
    }

    /// Renders the `media` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute media(String value) {
      return $html().media(value);
    }

    /// Renders the `method` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute method(String value) {
      return $html().method(value);
    }

    /// Renders the `minlength` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute minlength(String value) {
      return $html().minlength(value);
    }

    /// Renders the `name` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute name(String value) {
      return $html().name(value);
    }

    /// Renders the `onafterprint` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onafterprint(String value) {
      return $html().onafterprint(value);
    }

    /// Renders the `onbeforeprint` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onbeforeprint(String value) {
      return $html().onbeforeprint(value);
    }

    /// Renders the `onbeforeunload` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onbeforeunload(String value) {
      return $html().onbeforeunload(value);
    }

    /// Renders the `onclick` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onclick(String value) {
      return $html().onclick(value);
    }

    /// Renders the `onhashchange` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onhashchange(String value) {
      return $html().onhashchange(value);
    }

    /// Renders the `onlanguagechange` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onlanguagechange(String value) {
      return $html().onlanguagechange(value);
    }

    /// Renders the `onmessage` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onmessage(String value) {
      return $html().onmessage(value);
    }

    /// Renders the `onoffline` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onoffline(String value) {
      return $html().onoffline(value);
    }

    /// Renders the `ononline` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute ononline(String value) {
      return $html().ononline(value);
    }

    /// Renders the `onpagehide` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onpagehide(String value) {
      return $html().onpagehide(value);
    }

    /// Renders the `onpageshow` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onpageshow(String value) {
      return $html().onpageshow(value);
    }

    /// Renders the `onpopstate` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onpopstate(String value) {
      return $html().onpopstate(value);
    }

    /// Renders the `onrejectionhandled` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onrejectionhandled(String value) {
      return $html().onrejectionhandled(value);
    }

    /// Renders the `onstorage` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onstorage(String value) {
      return $html().onstorage(value);
    }

    /// Renders the `onsubmit` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onsubmit(String value) {
      return $html().onsubmit(value);
    }

    /// Renders the `onunhandledrejection` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onunhandledrejection(String value) {
      return $html().onunhandledrejection(value);
    }

    /// Renders the `onunload` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute onunload(String value) {
      return $html().onunload(value);
    }

    /// Renders the `opacity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute opacity(String value) {
      return $html().opacity(value);
    }

    /// Renders the `overflow` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute overflow(String value) {
      return $html().overflow(value);
    }

    /// Renders the `paint-order` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute paintOrder(String value) {
      return $html().paintOrder(value);
    }

    /// Renders the `placeholder` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute placeholder(String value) {
      return $html().placeholder(value);
    }

    /// Renders the `pointer-events` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute pointerEvents(String value) {
      return $html().pointerEvents(value);
    }

    /// Renders the `property` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute property(String value) {
      return $html().property(value);
    }

    /// Renders the `referrerpolicy` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute referrerpolicy(String value) {
      return $html().referrerpolicy(value);
    }

    /// Renders the `rel` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rel(String value) {
      return $html().rel(value);
    }

    /// Renders the `rev` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rev(String value) {
      return $html().rev(value);
    }

    /// Renders the `role` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute role(String value) {
      return $html().role(value);
    }

    /// Renders the `rows` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute rows(String value) {
      return $html().rows(value);
    }

    /// Renders the `shape-rendering` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute shapeRendering(String value) {
      return $html().shapeRendering(value);
    }

    /// Renders the `size` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute size(String value) {
      return $html().size(value);
    }

    /// Renders the `sizes` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute sizes(String value) {
      return $html().sizes(value);
    }

    /// Renders the `spellcheck` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute spellcheck(String value) {
      return $html().spellcheck(value);
    }

    /// Renders the `src` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute src(String value) {
      return $html().src(value);
    }

    /// Renders the `srcset` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute srcset(String value) {
      return $html().srcset(value);
    }

    /// Renders the `start` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute start(String value) {
      return $html().start(value);
    }

    /// Renders the `stop-color` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stopColor(String value) {
      return $html().stopColor(value);
    }

    /// Renders the `stop-opacity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stopOpacity(String value) {
      return $html().stopOpacity(value);
    }

    /// Renders the `stroke` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute stroke(String value) {
      return $html().stroke(value);
    }

    /// Renders the `stroke-dasharray` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeDasharray(String value) {
      return $html().strokeDasharray(value);
    }

    /// Renders the `stroke-dashoffset` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeDashoffset(String value) {
      return $html().strokeDashoffset(value);
    }

    /// Renders the `stroke-linecap` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeLinecap(String value) {
      return $html().strokeLinecap(value);
    }

    /// Renders the `stroke-linejoin` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeLinejoin(String value) {
      return $html().strokeLinejoin(value);
    }

    /// Renders the `stroke-miterlimit` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeMiterlimit(String value) {
      return $html().strokeMiterlimit(value);
    }

    /// Renders the `stroke-opacity` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeOpacity(String value) {
      return $html().strokeOpacity(value);
    }

    /// Renders the `stroke-width` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute strokeWidth(String value) {
      return $html().strokeWidth(value);
    }

    /// Renders the `tabindex` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute tabindex(String value) {
      return $html().tabindex(value);
    }

    /// Renders the `target` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute target(String value) {
      return $html().target(value);
    }

    /// Renders the `text-anchor` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textAnchor(String value) {
      return $html().textAnchor(value);
    }

    /// Renders the `text-decoration` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textDecoration(String value) {
      return $html().textDecoration(value);
    }

    /// Renders the `text-overflow` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textOverflow(String value) {
      return $html().textOverflow(value);
    }

    /// Renders the `text-rendering` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute textRendering(String value) {
      return $html().textRendering(value);
    }

    /// Renders the `transform` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute transform(String value) {
      return $html().transform(value);
    }

    /// Renders the `transform-origin` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute transformOrigin(String value) {
      return $html().transformOrigin(value);
    }

    /// Renders the `translate` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute translate(String value) {
      return $html().translate(value);
    }

    /// Renders the `type` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute type(String value) {
      return $html().type(value);
    }

    /// Renders the `unicode-bidi` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute unicodeBidi(String value) {
      return $html().unicodeBidi(value);
    }

    /// Renders the `value` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute value(String value) {
      return $html().value(value);
    }

    /// Renders the `vector-effect` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute vectorEffect(String value) {
      return $html().vectorEffect(value);
    }

    /// Renders the `viewBox` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute viewBox(String value) {
      return $html().viewBox(value);
    }

    /// Renders the `visibility` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute visibility(String value) {
      return $html().visibility(value);
    }

    /// Renders the `white-space` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute whiteSpace(String value) {
      return $html().whiteSpace(value);
    }

    /// Renders the `width` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute width(String value) {
      return $html().width(value);
    }

    /// Renders the `word-spacing` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute wordSpacing(String value) {
      return $html().wordSpacing(value);
    }

    /// Renders the `wrap` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute wrap(String value) {
      return $html().wrap(value);
    }

    /// Renders the `writing-mode` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute writingMode(String value) {
      return $html().writingMode(value);
    }

    /// Renders the `xmlns` attribute with the specified value.
    /// 
    /// @param value the attribute value
    /// 
    /// @return an instruction representing the attribute
    protected final Html.Instruction.OfAttribute xmlns(String value) {
      return $html().xmlns(value);
    }

    //
    // AMBIGUOUS
    //

    /// Renders the `clip-path` attribute or the `clipPath` element with the
    /// specified text.
    /// 
    /// @param text the attribute value or the text content of the element
    /// 
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous clipPath(String text) {
      return $html().clipPath(text);
    }

    /// Renders the `form` attribute or the `form` element with the specified
    /// text.
    /// 
    /// @param text the attribute value or the text content of the element
    /// 
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous form(String text) {
      return $html().form(text);
    }

    /// Renders the `label` attribute or the `label` element with the specified
    /// text.
    /// 
    /// @param text the attribute value or the text content of the element
    /// 
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous label(String text) {
      return $html().label(text);
    }

    /// Renders the `style` attribute or the `style` element with the specified
    /// text.
    /// 
    /// @param text the attribute value or the text content of the element
    /// 
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous style(String text) {
      return $html().style(text);
    }

    /// Renders the `title` attribute or the `title` element with the specified
    /// text.
    /// 
    /// @param text the attribute value or the text content of the element
    /// 
    /// @return an instruction representing the attribute or the element
    protected final Html.Instruction.OfAmbiguous title(String text) {
      return $html().title(text);
    }

    //
    // TEXT
    //

    /// Renders the non-breaking space `&nbsp;` HTML character entity.
    /// 
    /// @return an instruction representing the non-breaking space
    ///         character entity.
    protected final Html.Instruction.OfElement nbsp() {
      return $html().nbsp();
    }

    /// Renders the specified value as raw HTML.
    /// 
    /// @param value the raw HTML value
    /// 
    /// @return a raw HTML instruction
    protected final Html.Instruction.OfElement raw(String value) {
      return $html().raw(value);
    }

    /// Renders a text node with the specified value. The text value is escaped
    /// before being emitted to the output.
    /// 
    /// @param value the text value
    /// 
    /// @return an instruction representing the text node
    protected final Html.Instruction.OfElement text(String value) {
      return $html().text(value);
    }

    // END generated code

  }

  private Html() {}

  /// Formats the specified string to be used as an HTML attribute value. More
  /// specifically, this method returns a string whose value is the specified
  /// string:
  ///
  /// - With all leading and trailing [white space][Character#isWhitespace(char)]
  /// removed. - All other [white space][Character#isWhitespace(char)] characters
  /// are replaced by the space character (`U+0020`). - All sequences of more
  /// than one consecutive space characters are normalized to a single space
  /// character.
  ///
  /// @param value the string to be formatted
  ///
  /// @return the formatted string
  public static String formatAttrValue(String value) {
    Objects.requireNonNull(value, "value == null");

    return formatAttrValue(value, new StringBuilder());
  }

  static String formatAttrValue(String value, StringBuilder sb) {
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

}
