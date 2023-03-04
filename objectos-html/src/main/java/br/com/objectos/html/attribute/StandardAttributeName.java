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
package br.com.objectos.html.attribute;

import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;
import objectos.html.tmpl.AValue;
import objectos.html.tmpl.BlockquoteValue;
import objectos.html.tmpl.BodyValue;
import objectos.html.tmpl.ButtonValue;
import objectos.html.tmpl.ClipPathValue;
import objectos.html.tmpl.DefsValue;
import objectos.html.tmpl.DetailsValue;
import objectos.html.tmpl.FormValue;
import objectos.html.tmpl.GValue;
import objectos.html.tmpl.ImgValue;
import objectos.html.tmpl.InputValue;
import objectos.html.tmpl.LabelValue;
import objectos.html.tmpl.LinkValue;
import objectos.html.tmpl.MetaValue;
import objectos.html.tmpl.OlValue;
import objectos.html.tmpl.OptionValue;
import objectos.html.tmpl.PathValue;
import objectos.html.tmpl.ScriptValue;
import objectos.html.tmpl.SelectValue;
import objectos.html.tmpl.StyleValue;
import objectos.html.tmpl.SvgValue;
import objectos.html.tmpl.TableValue;
import objectos.html.tmpl.TextareaValue;
import objectos.html.tmpl.Value;
import objectos.util.UnmodifiableMap;

public abstract class StandardAttributeName implements AttributeName, Value {
  public static final Accesskey ACCESSKEY = new Accesskey();

  public static final Action ACTION = new Action();

  public static final Align ALIGN = new Align();

  public static final AlignmentBaseline ALIGNMENTBASELINE = new AlignmentBaseline();

  public static final Alt ALT = new Alt();

  public static final AriaHidden ARIAHIDDEN = new AriaHidden();

  public static final Async ASYNC = new Async();

  public static final Autocomplete AUTOCOMPLETE = new Autocomplete();

  public static final Autofocus AUTOFOCUS = new Autofocus();

  public static final BaselineShift BASELINESHIFT = new BaselineShift();

  public static final Border BORDER = new Border();

  public static final Cellpadding CELLPADDING = new Cellpadding();

  public static final Cellspacing CELLSPACING = new Cellspacing();

  public static final Charset CHARSET = new Charset();

  public static final Cite CITE = new Cite();

  public static final Class CLASS = new Class();

  public static final ClipPath CLIPPATH = new ClipPath();

  public static final ClipRule CLIPRULE = new ClipRule();

  public static final Color COLOR = new Color();

  public static final ColorInterpolation COLORINTERPOLATION = new ColorInterpolation();

  public static final ColorInterpolationFilters COLORINTERPOLATIONFILTERS = new ColorInterpolationFilters();

  public static final Cols COLS = new Cols();

  public static final Content CONTENT = new Content();

  public static final Contenteditable CONTENTEDITABLE = new Contenteditable();

  public static final Crossorigin CROSSORIGIN = new Crossorigin();

  public static final Cursor CURSOR = new Cursor();

  public static final D D = new D();

  public static final Defer DEFER = new Defer();

  public static final Dir DIR = new Dir();

  public static final Direction DIRECTION = new Direction();

  public static final Dirname DIRNAME = new Dirname();

  public static final Disabled DISABLED = new Disabled();

  public static final Display DISPLAY = new Display();

  public static final DominantBaseline DOMINANTBASELINE = new DominantBaseline();

  public static final Draggable DRAGGABLE = new Draggable();

  public static final Enctype ENCTYPE = new Enctype();

  public static final Fill FILL = new Fill();

  public static final FillOpacity FILLOPACITY = new FillOpacity();

  public static final FillRule FILLRULE = new FillRule();

  public static final Filter FILTER = new Filter();

  public static final FloodColor FLOODCOLOR = new FloodColor();

  public static final FloodOpacity FLOODOPACITY = new FloodOpacity();

  public static final FontFamily FONTFAMILY = new FontFamily();

  public static final FontSize FONTSIZE = new FontSize();

  public static final FontSizeAdjust FONTSIZEADJUST = new FontSizeAdjust();

  public static final FontStretch FONTSTRETCH = new FontStretch();

  public static final FontStyle FONTSTYLE = new FontStyle();

  public static final FontVariant FONTVARIANT = new FontVariant();

  public static final FontWeight FONTWEIGHT = new FontWeight();

  public static final For FOR = new For();

  public static final Form FORM = new Form();

  public static final GlyphOrientationHorizontal GLYPHORIENTATIONHORIZONTAL = new GlyphOrientationHorizontal();

  public static final GlyphOrientationVertical GLYPHORIENTATIONVERTICAL = new GlyphOrientationVertical();

  public static final Height HEIGHT = new Height();

  public static final Hidden HIDDEN = new Hidden();

  public static final Href HREF = new Href();

  public static final HttpEquiv HTTPEQUIV = new HttpEquiv();

  public static final Id ID = new Id();

  public static final ImageRendering IMAGERENDERING = new ImageRendering();

  public static final Integrity INTEGRITY = new Integrity();

  public static final Label LABEL = new Label();

  public static final Lang LANG = new Lang();

  public static final LetterSpacing LETTERSPACING = new LetterSpacing();

  public static final LightingColor LIGHTINGCOLOR = new LightingColor();

  public static final MarkerEnd MARKEREND = new MarkerEnd();

  public static final MarkerMid MARKERMID = new MarkerMid();

  public static final MarkerStart MARKERSTART = new MarkerStart();

  public static final Mask MASK = new Mask();

  public static final MaskType MASKTYPE = new MaskType();

  public static final Maxlength MAXLENGTH = new Maxlength();

  public static final Media MEDIA = new Media();

  public static final Method METHOD = new Method();

  public static final Minlength MINLENGTH = new Minlength();

  public static final Multiple MULTIPLE = new Multiple();

  public static final Name NAME = new Name();

  public static final Nomodule NOMODULE = new Nomodule();

  public static final Onafterprint ONAFTERPRINT = new Onafterprint();

  public static final Onbeforeprint ONBEFOREPRINT = new Onbeforeprint();

  public static final Onbeforeunload ONBEFOREUNLOAD = new Onbeforeunload();

  public static final Onclick ONCLICK = new Onclick();

  public static final Onhashchange ONHASHCHANGE = new Onhashchange();

  public static final Onlanguagechange ONLANGUAGECHANGE = new Onlanguagechange();

  public static final Onmessage ONMESSAGE = new Onmessage();

  public static final Onoffline ONOFFLINE = new Onoffline();

  public static final Ononline ONONLINE = new Ononline();

  public static final Onpagehide ONPAGEHIDE = new Onpagehide();

  public static final Onpageshow ONPAGESHOW = new Onpageshow();

  public static final Onpopstate ONPOPSTATE = new Onpopstate();

  public static final Onrejectionhandled ONREJECTIONHANDLED = new Onrejectionhandled();

  public static final Onstorage ONSTORAGE = new Onstorage();

  public static final Onsubmit ONSUBMIT = new Onsubmit();

  public static final Onunhandledrejection ONUNHANDLEDREJECTION = new Onunhandledrejection();

  public static final Onunload ONUNLOAD = new Onunload();

  public static final Opacity OPACITY = new Opacity();

  public static final Open OPEN = new Open();

  public static final Overflow OVERFLOW = new Overflow();

  public static final PaintOrder PAINTORDER = new PaintOrder();

  public static final Placeholder PLACEHOLDER = new Placeholder();

  public static final PointerEvents POINTEREVENTS = new PointerEvents();

  public static final Property PROPERTY = new Property();

  public static final Readonly READONLY = new Readonly();

  public static final Referrerpolicy REFERRERPOLICY = new Referrerpolicy();

  public static final Rel REL = new Rel();

  public static final Required REQUIRED = new Required();

  public static final Rev REV = new Rev();

  public static final Reversed REVERSED = new Reversed();

  public static final Role ROLE = new Role();

  public static final Rows ROWS = new Rows();

  public static final Selected SELECTED = new Selected();

  public static final ShapeRendering SHAPERENDERING = new ShapeRendering();

  public static final Size SIZE = new Size();

  public static final Sizes SIZES = new Sizes();

  public static final Spellcheck SPELLCHECK = new Spellcheck();

  public static final Src SRC = new Src();

  public static final Srcset SRCSET = new Srcset();

  public static final Start START = new Start();

  public static final StopColor STOPCOLOR = new StopColor();

  public static final StopOpacity STOPOPACITY = new StopOpacity();

  public static final Stroke STROKE = new Stroke();

  public static final StrokeDasharray STROKEDASHARRAY = new StrokeDasharray();

  public static final StrokeDashoffset STROKEDASHOFFSET = new StrokeDashoffset();

  public static final StrokeLinecap STROKELINECAP = new StrokeLinecap();

  public static final StrokeLinejoin STROKELINEJOIN = new StrokeLinejoin();

  public static final StrokeMiterlimit STROKEMITERLIMIT = new StrokeMiterlimit();

  public static final StrokeOpacity STROKEOPACITY = new StrokeOpacity();

  public static final StrokeWidth STROKEWIDTH = new StrokeWidth();

  public static final Style STYLE = new Style();

  public static final Tabindex TABINDEX = new Tabindex();

  public static final Target TARGET = new Target();

  public static final TextAnchor TEXTANCHOR = new TextAnchor();

  public static final TextDecoration TEXTDECORATION = new TextDecoration();

  public static final TextOverflow TEXTOVERFLOW = new TextOverflow();

  public static final TextRendering TEXTRENDERING = new TextRendering();

  public static final Title TITLE = new Title();

  public static final Transform TRANSFORM = new Transform();

  public static final TransformOrigin TRANSFORMORIGIN = new TransformOrigin();

  public static final Translate TRANSLATE = new Translate();

  public static final Type TYPE = new Type();

  public static final UnicodeBidi UNICODEBIDI = new UnicodeBidi();

  public static final br.com.objectos.html.attribute.StandardAttributeName.Value VALUE = new br.com.objectos.html.attribute.StandardAttributeName.Value();

  public static final VectorEffect VECTOREFFECT = new VectorEffect();

  public static final ViewBox VIEWBOX = new ViewBox();

  public static final Visibility VISIBILITY = new Visibility();

  public static final WhiteSpace WHITESPACE = new WhiteSpace();

  public static final Width WIDTH = new Width();

  public static final WordSpacing WORDSPACING = new WordSpacing();

  public static final Wrap WRAP = new Wrap();

  public static final WritingMode WRITINGMODE = new WritingMode();

  public static final Xmlns XMLNS = new Xmlns();

  private static final StandardAttributeName[] ARRAY = {
    ACCESSKEY,
    ACTION,
    ALIGN,
    ALIGNMENTBASELINE,
    ALT,
    ARIAHIDDEN,
    ASYNC,
    AUTOCOMPLETE,
    AUTOFOCUS,
    BASELINESHIFT,
    BORDER,
    CELLPADDING,
    CELLSPACING,
    CHARSET,
    CITE,
    CLASS,
    CLIPPATH,
    CLIPRULE,
    COLOR,
    COLORINTERPOLATION,
    COLORINTERPOLATIONFILTERS,
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
    DOMINANTBASELINE,
    DRAGGABLE,
    ENCTYPE,
    FILL,
    FILLOPACITY,
    FILLRULE,
    FILTER,
    FLOODCOLOR,
    FLOODOPACITY,
    FONTFAMILY,
    FONTSIZE,
    FONTSIZEADJUST,
    FONTSTRETCH,
    FONTSTYLE,
    FONTVARIANT,
    FONTWEIGHT,
    FOR,
    FORM,
    GLYPHORIENTATIONHORIZONTAL,
    GLYPHORIENTATIONVERTICAL,
    HEIGHT,
    HIDDEN,
    HREF,
    HTTPEQUIV,
    ID,
    IMAGERENDERING,
    INTEGRITY,
    LABEL,
    LANG,
    LETTERSPACING,
    LIGHTINGCOLOR,
    MARKEREND,
    MARKERMID,
    MARKERSTART,
    MASK,
    MASKTYPE,
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
    PAINTORDER,
    PLACEHOLDER,
    POINTEREVENTS,
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
    SHAPERENDERING,
    SIZE,
    SIZES,
    SPELLCHECK,
    SRC,
    SRCSET,
    START,
    STOPCOLOR,
    STOPOPACITY,
    STROKE,
    STROKEDASHARRAY,
    STROKEDASHOFFSET,
    STROKELINECAP,
    STROKELINEJOIN,
    STROKEMITERLIMIT,
    STROKEOPACITY,
    STROKEWIDTH,
    STYLE,
    TABINDEX,
    TARGET,
    TEXTANCHOR,
    TEXTDECORATION,
    TEXTOVERFLOW,
    TEXTRENDERING,
    TITLE,
    TRANSFORM,
    TRANSFORMORIGIN,
    TRANSLATE,
    TYPE,
    UNICODEBIDI,
    VALUE,
    VECTOREFFECT,
    VIEWBOX,
    VISIBILITY,
    WHITESPACE,
    WIDTH,
    WORDSPACING,
    WRAP,
    WRITINGMODE,
    XMLNS
  };

  private static final UnmodifiableMap<String, StandardAttributeName> MAP = mapInit();

  private final int code;

  private final AttributeKind kind;

  private final String name;

  StandardAttributeName(int code, AttributeKind kind, String name) {
    this.code = code;
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
    return code;
  }

  @Override
  public final AttributeKind getKind() {
    return kind;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final void mark(Marker marker) {
    marker.markAttribute();
  }

  @Override
  public final void render(Renderer renderer) {}

  public static class Accesskey extends StandardAttributeName implements GlobalAttributeName {
    private Accesskey() {
      super(0, AttributeKind.STRING, "accesskey");
    }
  }

  public static class Action extends StandardAttributeName implements FormValue {
    private Action() {
      super(1, AttributeKind.STRING, "action");
    }
  }

  public static class Align extends StandardAttributeName implements TableValue {
    private Align() {
      super(2, AttributeKind.STRING, "align");
    }
  }

  public static class AlignmentBaseline extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private AlignmentBaseline() {
      super(3, AttributeKind.STRING, "alignment-baseline");
    }
  }

  public static class Alt extends StandardAttributeName implements ImgValue {
    private Alt() {
      super(4, AttributeKind.STRING, "alt");
    }
  }

  public static class AriaHidden extends StandardAttributeName implements GlobalAttributeName {
    private AriaHidden() {
      super(5, AttributeKind.STRING, "aria-hidden");
    }
  }

  public static class Async extends StandardAttributeName implements ScriptValue {
    private Async() {
      super(6, AttributeKind.BOOLEAN, "async");
    }
  }

  public static class Autocomplete extends StandardAttributeName implements SelectValue, TextareaValue {
    private Autocomplete() {
      super(7, AttributeKind.STRING, "autocomplete");
    }
  }

  public static class Autofocus extends StandardAttributeName implements InputValue {
    private Autofocus() {
      super(8, AttributeKind.BOOLEAN, "autofocus");
    }
  }

  public static class BaselineShift extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private BaselineShift() {
      super(9, AttributeKind.STRING, "baseline-shift");
    }
  }

  public static class Border extends StandardAttributeName implements TableValue {
    private Border() {
      super(10, AttributeKind.STRING, "border");
    }
  }

  public static class Cellpadding extends StandardAttributeName implements TableValue {
    private Cellpadding() {
      super(11, AttributeKind.STRING, "cellpadding");
    }
  }

  public static class Cellspacing extends StandardAttributeName implements TableValue {
    private Cellspacing() {
      super(12, AttributeKind.STRING, "cellspacing");
    }
  }

  public static class Charset extends StandardAttributeName implements MetaValue {
    private Charset() {
      super(13, AttributeKind.STRING, "charset");
    }
  }

  public static class Cite extends StandardAttributeName implements BlockquoteValue {
    private Cite() {
      super(14, AttributeKind.STRING, "cite");
    }
  }

  public static class Class extends StandardAttributeName implements GlobalAttributeName {
    private Class() {
      super(15, AttributeKind.STRING, "class");
    }
  }

  public static class ClipPath extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ClipPath() {
      super(16, AttributeKind.STRING, "clip-path");
    }
  }

  public static class ClipRule extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ClipRule() {
      super(17, AttributeKind.STRING, "clip-rule");
    }
  }

  public static class Color extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Color() {
      super(18, AttributeKind.STRING, "color");
    }
  }

  public static class ColorInterpolation extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ColorInterpolation() {
      super(19, AttributeKind.STRING, "color-interpolation");
    }
  }

  public static class ColorInterpolationFilters extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ColorInterpolationFilters() {
      super(20, AttributeKind.STRING, "color-interpolation-filters");
    }
  }

  public static class Cols extends StandardAttributeName implements TextareaValue {
    private Cols() {
      super(21, AttributeKind.STRING, "cols");
    }
  }

  public static class Content extends StandardAttributeName implements MetaValue {
    private Content() {
      super(22, AttributeKind.STRING, "content");
    }
  }

  public static class Contenteditable extends StandardAttributeName implements GlobalAttributeName {
    private Contenteditable() {
      super(23, AttributeKind.STRING, "contenteditable");
    }
  }

  public static class Crossorigin extends StandardAttributeName implements LinkValue, ScriptValue {
    private Crossorigin() {
      super(24, AttributeKind.STRING, "crossorigin");
    }
  }

  public static class Cursor extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Cursor() {
      super(25, AttributeKind.STRING, "cursor");
    }
  }

  public static class D extends StandardAttributeName implements ClipPathValue, PathValue {
    private D() {
      super(26, AttributeKind.STRING, "d");
    }
  }

  public static class Defer extends StandardAttributeName implements ScriptValue {
    private Defer() {
      super(27, AttributeKind.BOOLEAN, "defer");
    }
  }

  public static class Dir extends StandardAttributeName implements GlobalAttributeName {
    private Dir() {
      super(28, AttributeKind.STRING, "dir");
    }
  }

  public static class Direction extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Direction() {
      super(29, AttributeKind.STRING, "direction");
    }
  }

  public static class Dirname extends StandardAttributeName implements TextareaValue {
    private Dirname() {
      super(30, AttributeKind.STRING, "dirname");
    }
  }

  public static class Disabled extends StandardAttributeName implements OptionValue, SelectValue, TextareaValue {
    private Disabled() {
      super(31, AttributeKind.BOOLEAN, "disabled");
    }
  }

  public static class Display extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Display() {
      super(32, AttributeKind.STRING, "display");
    }
  }

  public static class DominantBaseline extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private DominantBaseline() {
      super(33, AttributeKind.STRING, "dominant-baseline");
    }
  }

  public static class Draggable extends StandardAttributeName implements GlobalAttributeName {
    private Draggable() {
      super(34, AttributeKind.STRING, "draggable");
    }
  }

  public static class Enctype extends StandardAttributeName implements FormValue {
    private Enctype() {
      super(35, AttributeKind.STRING, "enctype");
    }
  }

  public static class Fill extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Fill() {
      super(36, AttributeKind.STRING, "fill");
    }
  }

  public static class FillOpacity extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FillOpacity() {
      super(37, AttributeKind.STRING, "fill-opacity");
    }
  }

  public static class FillRule extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FillRule() {
      super(38, AttributeKind.STRING, "fill-rule");
    }
  }

  public static class Filter extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Filter() {
      super(39, AttributeKind.STRING, "filter");
    }
  }

  public static class FloodColor extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FloodColor() {
      super(40, AttributeKind.STRING, "flood-color");
    }
  }

  public static class FloodOpacity extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FloodOpacity() {
      super(41, AttributeKind.STRING, "flood-opacity");
    }
  }

  public static class FontFamily extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontFamily() {
      super(42, AttributeKind.STRING, "font-family");
    }
  }

  public static class FontSize extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontSize() {
      super(43, AttributeKind.STRING, "font-size");
    }
  }

  public static class FontSizeAdjust extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontSizeAdjust() {
      super(44, AttributeKind.STRING, "font-size-adjust");
    }
  }

  public static class FontStretch extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontStretch() {
      super(45, AttributeKind.STRING, "font-stretch");
    }
  }

  public static class FontStyle extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontStyle() {
      super(46, AttributeKind.STRING, "font-style");
    }
  }

  public static class FontVariant extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontVariant() {
      super(47, AttributeKind.STRING, "font-variant");
    }
  }

  public static class FontWeight extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private FontWeight() {
      super(48, AttributeKind.STRING, "font-weight");
    }
  }

  public static class For extends StandardAttributeName implements LabelValue {
    private For() {
      super(49, AttributeKind.STRING, "for");
    }
  }

  public static class Form extends StandardAttributeName implements SelectValue, TextareaValue {
    private Form() {
      super(50, AttributeKind.STRING, "form");
    }
  }

  public static class GlyphOrientationHorizontal extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private GlyphOrientationHorizontal() {
      super(51, AttributeKind.STRING, "glyph-orientation-horizontal");
    }
  }

  public static class GlyphOrientationVertical extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private GlyphOrientationVertical() {
      super(52, AttributeKind.STRING, "glyph-orientation-vertical");
    }
  }

  public static class Height extends StandardAttributeName implements ImgValue, SvgValue {
    private Height() {
      super(53, AttributeKind.STRING, "height");
    }
  }

  public static class Hidden extends StandardAttributeName implements GlobalAttributeName {
    private Hidden() {
      super(54, AttributeKind.BOOLEAN, "hidden");
    }
  }

  public static class Href extends StandardAttributeName implements AValue, LinkValue {
    private Href() {
      super(55, AttributeKind.STRING, "href");
    }
  }

  public static class HttpEquiv extends StandardAttributeName implements MetaValue {
    private HttpEquiv() {
      super(56, AttributeKind.STRING, "http-equiv");
    }
  }

  public static class Id extends StandardAttributeName implements GlobalAttributeName {
    private Id() {
      super(57, AttributeKind.STRING, "id");
    }
  }

  public static class ImageRendering extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ImageRendering() {
      super(58, AttributeKind.STRING, "image-rendering");
    }
  }

  public static class Integrity extends StandardAttributeName implements ScriptValue {
    private Integrity() {
      super(59, AttributeKind.STRING, "integrity");
    }
  }

  public static class Label extends StandardAttributeName implements OptionValue {
    private Label() {
      super(60, AttributeKind.STRING, "label");
    }
  }

  public static class Lang extends StandardAttributeName implements GlobalAttributeName {
    private Lang() {
      super(61, AttributeKind.STRING, "lang");
    }
  }

  public static class LetterSpacing extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private LetterSpacing() {
      super(62, AttributeKind.STRING, "letter-spacing");
    }
  }

  public static class LightingColor extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private LightingColor() {
      super(63, AttributeKind.STRING, "lighting-color");
    }
  }

  public static class MarkerEnd extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private MarkerEnd() {
      super(64, AttributeKind.STRING, "marker-end");
    }
  }

  public static class MarkerMid extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private MarkerMid() {
      super(65, AttributeKind.STRING, "marker-mid");
    }
  }

  public static class MarkerStart extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private MarkerStart() {
      super(66, AttributeKind.STRING, "marker-start");
    }
  }

  public static class Mask extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Mask() {
      super(67, AttributeKind.STRING, "mask");
    }
  }

  public static class MaskType extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private MaskType() {
      super(68, AttributeKind.STRING, "mask-type");
    }
  }

  public static class Maxlength extends StandardAttributeName implements TextareaValue {
    private Maxlength() {
      super(69, AttributeKind.STRING, "maxlength");
    }
  }

  public static class Media extends StandardAttributeName implements LinkValue {
    private Media() {
      super(70, AttributeKind.STRING, "media");
    }
  }

  public static class Method extends StandardAttributeName implements FormValue {
    private Method() {
      super(71, AttributeKind.STRING, "method");
    }
  }

  public static class Minlength extends StandardAttributeName implements TextareaValue {
    private Minlength() {
      super(72, AttributeKind.STRING, "minlength");
    }
  }

  public static class Multiple extends StandardAttributeName implements SelectValue {
    private Multiple() {
      super(73, AttributeKind.BOOLEAN, "multiple");
    }
  }

  public static class Name extends StandardAttributeName implements FormValue, InputValue, MetaValue, SelectValue, TextareaValue {
    private Name() {
      super(74, AttributeKind.STRING, "name");
    }
  }

  public static class Nomodule extends StandardAttributeName implements ScriptValue {
    private Nomodule() {
      super(75, AttributeKind.BOOLEAN, "nomodule");
    }
  }

  public static class Onafterprint extends StandardAttributeName implements BodyValue {
    private Onafterprint() {
      super(76, AttributeKind.STRING, "onafterprint");
    }
  }

  public static class Onbeforeprint extends StandardAttributeName implements BodyValue {
    private Onbeforeprint() {
      super(77, AttributeKind.STRING, "onbeforeprint");
    }
  }

  public static class Onbeforeunload extends StandardAttributeName implements BodyValue {
    private Onbeforeunload() {
      super(78, AttributeKind.STRING, "onbeforeunload");
    }
  }

  public static class Onclick extends StandardAttributeName implements GlobalAttributeName {
    private Onclick() {
      super(79, AttributeKind.STRING, "onclick");
    }
  }

  public static class Onhashchange extends StandardAttributeName implements BodyValue {
    private Onhashchange() {
      super(80, AttributeKind.STRING, "onhashchange");
    }
  }

  public static class Onlanguagechange extends StandardAttributeName implements BodyValue {
    private Onlanguagechange() {
      super(81, AttributeKind.STRING, "onlanguagechange");
    }
  }

  public static class Onmessage extends StandardAttributeName implements BodyValue {
    private Onmessage() {
      super(82, AttributeKind.STRING, "onmessage");
    }
  }

  public static class Onoffline extends StandardAttributeName implements BodyValue {
    private Onoffline() {
      super(83, AttributeKind.STRING, "onoffline");
    }
  }

  public static class Ononline extends StandardAttributeName implements BodyValue {
    private Ononline() {
      super(84, AttributeKind.STRING, "ononline");
    }
  }

  public static class Onpagehide extends StandardAttributeName implements BodyValue {
    private Onpagehide() {
      super(85, AttributeKind.STRING, "onpagehide");
    }
  }

  public static class Onpageshow extends StandardAttributeName implements BodyValue {
    private Onpageshow() {
      super(86, AttributeKind.STRING, "onpageshow");
    }
  }

  public static class Onpopstate extends StandardAttributeName implements BodyValue {
    private Onpopstate() {
      super(87, AttributeKind.STRING, "onpopstate");
    }
  }

  public static class Onrejectionhandled extends StandardAttributeName implements BodyValue {
    private Onrejectionhandled() {
      super(88, AttributeKind.STRING, "onrejectionhandled");
    }
  }

  public static class Onstorage extends StandardAttributeName implements BodyValue {
    private Onstorage() {
      super(89, AttributeKind.STRING, "onstorage");
    }
  }

  public static class Onsubmit extends StandardAttributeName implements GlobalAttributeName {
    private Onsubmit() {
      super(90, AttributeKind.STRING, "onsubmit");
    }
  }

  public static class Onunhandledrejection extends StandardAttributeName implements BodyValue {
    private Onunhandledrejection() {
      super(91, AttributeKind.STRING, "onunhandledrejection");
    }
  }

  public static class Onunload extends StandardAttributeName implements BodyValue {
    private Onunload() {
      super(92, AttributeKind.STRING, "onunload");
    }
  }

  public static class Opacity extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Opacity() {
      super(93, AttributeKind.STRING, "opacity");
    }
  }

  public static class Open extends StandardAttributeName implements DetailsValue {
    private Open() {
      super(94, AttributeKind.BOOLEAN, "open");
    }
  }

  public static class Overflow extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Overflow() {
      super(95, AttributeKind.STRING, "overflow");
    }
  }

  public static class PaintOrder extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private PaintOrder() {
      super(96, AttributeKind.STRING, "paint-order");
    }
  }

  public static class Placeholder extends StandardAttributeName implements InputValue, TextareaValue {
    private Placeholder() {
      super(97, AttributeKind.STRING, "placeholder");
    }
  }

  public static class PointerEvents extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private PointerEvents() {
      super(98, AttributeKind.STRING, "pointer-events");
    }
  }

  public static class Property extends StandardAttributeName implements MetaValue {
    private Property() {
      super(99, AttributeKind.STRING, "property");
    }
  }

  public static class Readonly extends StandardAttributeName implements InputValue, TextareaValue {
    private Readonly() {
      super(100, AttributeKind.BOOLEAN, "readonly");
    }
  }

  public static class Referrerpolicy extends StandardAttributeName implements LinkValue, ScriptValue {
    private Referrerpolicy() {
      super(101, AttributeKind.STRING, "referrerpolicy");
    }
  }

  public static class Rel extends StandardAttributeName implements LinkValue {
    private Rel() {
      super(102, AttributeKind.STRING, "rel");
    }
  }

  public static class Required extends StandardAttributeName implements InputValue, SelectValue, TextareaValue {
    private Required() {
      super(103, AttributeKind.BOOLEAN, "required");
    }
  }

  public static class Rev extends StandardAttributeName implements LinkValue {
    private Rev() {
      super(104, AttributeKind.STRING, "rev");
    }
  }

  public static class Reversed extends StandardAttributeName implements OlValue {
    private Reversed() {
      super(105, AttributeKind.BOOLEAN, "reversed");
    }
  }

  public static class Role extends StandardAttributeName implements GlobalAttributeName {
    private Role() {
      super(106, AttributeKind.STRING, "role");
    }
  }

  public static class Rows extends StandardAttributeName implements TextareaValue {
    private Rows() {
      super(107, AttributeKind.STRING, "rows");
    }
  }

  public static class Selected extends StandardAttributeName implements OptionValue {
    private Selected() {
      super(108, AttributeKind.BOOLEAN, "selected");
    }
  }

  public static class ShapeRendering extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private ShapeRendering() {
      super(109, AttributeKind.STRING, "shape-rendering");
    }
  }

  public static class Size extends StandardAttributeName implements SelectValue {
    private Size() {
      super(110, AttributeKind.STRING, "size");
    }
  }

  public static class Sizes extends StandardAttributeName implements LinkValue {
    private Sizes() {
      super(111, AttributeKind.STRING, "sizes");
    }
  }

  public static class Spellcheck extends StandardAttributeName implements GlobalAttributeName {
    private Spellcheck() {
      super(112, AttributeKind.STRING, "spellcheck");
    }
  }

  public static class Src extends StandardAttributeName implements ImgValue, ScriptValue {
    private Src() {
      super(113, AttributeKind.STRING, "src");
    }
  }

  public static class Srcset extends StandardAttributeName implements ImgValue {
    private Srcset() {
      super(114, AttributeKind.STRING, "srcset");
    }
  }

  public static class Start extends StandardAttributeName implements OlValue {
    private Start() {
      super(115, AttributeKind.STRING, "start");
    }
  }

  public static class StopColor extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StopColor() {
      super(116, AttributeKind.STRING, "stop-color");
    }
  }

  public static class StopOpacity extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StopOpacity() {
      super(117, AttributeKind.STRING, "stop-opacity");
    }
  }

  public static class Stroke extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Stroke() {
      super(118, AttributeKind.STRING, "stroke");
    }
  }

  public static class StrokeDasharray extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeDasharray() {
      super(119, AttributeKind.STRING, "stroke-dasharray");
    }
  }

  public static class StrokeDashoffset extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeDashoffset() {
      super(120, AttributeKind.STRING, "stroke-dashoffset");
    }
  }

  public static class StrokeLinecap extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeLinecap() {
      super(121, AttributeKind.STRING, "stroke-linecap");
    }
  }

  public static class StrokeLinejoin extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeLinejoin() {
      super(122, AttributeKind.STRING, "stroke-linejoin");
    }
  }

  public static class StrokeMiterlimit extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeMiterlimit() {
      super(123, AttributeKind.STRING, "stroke-miterlimit");
    }
  }

  public static class StrokeOpacity extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeOpacity() {
      super(124, AttributeKind.STRING, "stroke-opacity");
    }
  }

  public static class StrokeWidth extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private StrokeWidth() {
      super(125, AttributeKind.STRING, "stroke-width");
    }
  }

  public static class Style extends StandardAttributeName implements GlobalAttributeName {
    private Style() {
      super(126, AttributeKind.STRING, "style");
    }
  }

  public static class Tabindex extends StandardAttributeName implements GlobalAttributeName {
    private Tabindex() {
      super(127, AttributeKind.STRING, "tabindex");
    }
  }

  public static class Target extends StandardAttributeName implements AValue, FormValue {
    private Target() {
      super(128, AttributeKind.STRING, "target");
    }
  }

  public static class TextAnchor extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private TextAnchor() {
      super(129, AttributeKind.STRING, "text-anchor");
    }
  }

  public static class TextDecoration extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private TextDecoration() {
      super(130, AttributeKind.STRING, "text-decoration");
    }
  }

  public static class TextOverflow extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private TextOverflow() {
      super(131, AttributeKind.STRING, "text-overflow");
    }
  }

  public static class TextRendering extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private TextRendering() {
      super(132, AttributeKind.STRING, "text-rendering");
    }
  }

  public static class Title extends StandardAttributeName implements GlobalAttributeName {
    private Title() {
      super(133, AttributeKind.STRING, "title");
    }
  }

  public static class Transform extends StandardAttributeName implements ClipPathValue, GValue, PathValue, SvgValue {
    private Transform() {
      super(134, AttributeKind.STRING, "transform");
    }
  }

  public static class TransformOrigin extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private TransformOrigin() {
      super(135, AttributeKind.STRING, "transform-origin");
    }
  }

  public static class Translate extends StandardAttributeName implements GlobalAttributeName {
    private Translate() {
      super(136, AttributeKind.STRING, "translate");
    }
  }

  public static class Type extends StandardAttributeName implements ButtonValue, InputValue, LinkValue, OlValue, ScriptValue, StyleValue {
    private Type() {
      super(137, AttributeKind.STRING, "type");
    }
  }

  public static class UnicodeBidi extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private UnicodeBidi() {
      super(138, AttributeKind.STRING, "unicode-bidi");
    }
  }

  public static class Value extends StandardAttributeName implements InputValue, OptionValue {
    private Value() {
      super(139, AttributeKind.STRING, "value");
    }
  }

  public static class VectorEffect extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private VectorEffect() {
      super(140, AttributeKind.STRING, "vector-effect");
    }
  }

  public static class ViewBox extends StandardAttributeName implements SvgValue {
    private ViewBox() {
      super(141, AttributeKind.STRING, "viewBox");
    }
  }

  public static class Visibility extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private Visibility() {
      super(142, AttributeKind.STRING, "visibility");
    }
  }

  public static class WhiteSpace extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private WhiteSpace() {
      super(143, AttributeKind.STRING, "white-space");
    }
  }

  public static class Width extends StandardAttributeName implements ImgValue, SvgValue, TableValue {
    private Width() {
      super(144, AttributeKind.STRING, "width");
    }
  }

  public static class WordSpacing extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private WordSpacing() {
      super(145, AttributeKind.STRING, "word-spacing");
    }
  }

  public static class Wrap extends StandardAttributeName implements TextareaValue {
    private Wrap() {
      super(146, AttributeKind.STRING, "wrap");
    }
  }

  public static class WritingMode extends StandardAttributeName implements ClipPathValue, DefsValue, GValue, PathValue, SvgValue {
    private WritingMode() {
      super(147, AttributeKind.STRING, "writing-mode");
    }
  }

  public static class Xmlns extends StandardAttributeName implements SvgValue {
    private Xmlns() {
      super(148, AttributeKind.STRING, "xmlns");
    }
  }
}
