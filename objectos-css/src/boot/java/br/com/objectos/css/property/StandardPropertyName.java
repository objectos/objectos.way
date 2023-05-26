package br.com.objectos.css.property;

import objectos.util.GrowableMap;
import objectos.util.UnmodifiableMap;

public enum StandardPropertyName implements PropertyName {
  _MOZ_APPEARANCE("mozAppearance", "-moz-appearance"),

  _MOZ_TAB_SIZE("mozTabSize", "-moz-tab-size"),

  _WEBKIT_APPEARANCE("webkitAppearance", "-webkit-appearance"),

  _WEBKIT_TEXT_SIZE_ADJUST("webkitTextSizeAdjust", "-webkit-text-size-adjust"),

  ALIGN_CONTENT("alignContent", "align-content"),

  ALIGN_ITEMS("alignItems", "align-items"),

  ALIGN_SELF("alignSelf", "align-self"),

  APPEARANCE("appearance", "appearance"),

  BACKGROUND("background", "background"),

  BACKGROUND_ATTACHMENT("backgroundAttachment", "background-attachment"),

  BACKGROUND_CLIP("backgroundClip", "background-clip"),

  BACKGROUND_COLOR("backgroundColor", "background-color"),

  BACKGROUND_IMAGE("backgroundImage", "background-image"),

  BACKGROUND_ORIGIN("backgroundOrigin", "background-origin"),

  BACKGROUND_POSITION("backgroundPosition", "background-position"),

  BACKGROUND_REPEAT("backgroundRepeat", "background-repeat"),

  BACKGROUND_SIZE("backgroundSize", "background-size"),

  BORDER("border", "border"),

  BORDER_BOTTOM("borderBottom", "border-bottom"),

  BORDER_BOTTOM_COLOR("borderBottomColor", "border-bottom-color"),

  BORDER_BOTTOM_LEFT_RADIUS("borderBottomLeftRadius", "border-bottom-left-radius"),

  BORDER_BOTTOM_RIGHT_RADIUS("borderBottomRightRadius", "border-bottom-right-radius"),

  BORDER_BOTTOM_STYLE("borderBottomStyle", "border-bottom-style"),

  BORDER_BOTTOM_WIDTH("borderBottomWidth", "border-bottom-width"),

  BORDER_COLLAPSE("borderCollapse", "border-collapse"),

  BORDER_COLOR("borderColor", "border-color"),

  BORDER_LEFT("borderLeft", "border-left"),

  BORDER_LEFT_COLOR("borderLeftColor", "border-left-color"),

  BORDER_LEFT_STYLE("borderLeftStyle", "border-left-style"),

  BORDER_LEFT_WIDTH("borderLeftWidth", "border-left-width"),

  BORDER_RADIUS("borderRadius", "border-radius"),

  BORDER_RIGHT("borderRight", "border-right"),

  BORDER_RIGHT_COLOR("borderRightColor", "border-right-color"),

  BORDER_RIGHT_STYLE("borderRightStyle", "border-right-style"),

  BORDER_RIGHT_WIDTH("borderRightWidth", "border-right-width"),

  BORDER_STYLE("borderStyle", "border-style"),

  BORDER_TOP("borderTop", "border-top"),

  BORDER_TOP_COLOR("borderTopColor", "border-top-color"),

  BORDER_TOP_LEFT_RADIUS("borderTopLeftRadius", "border-top-left-radius"),

  BORDER_TOP_RIGHT_RADIUS("borderTopRightRadius", "border-top-right-radius"),

  BORDER_TOP_STYLE("borderTopStyle", "border-top-style"),

  BORDER_TOP_WIDTH("borderTopWidth", "border-top-width"),

  BORDER_WIDTH("borderWidth", "border-width"),

  BOTTOM("bottom", "bottom"),

  BOX_SHADOW("boxShadow", "box-shadow"),

  BOX_SIZING("boxSizing", "box-sizing"),

  CLEAR("clear", "clear"),

  COLOR("color", "color"),

  CONTENT("content", "content"),

  CURSOR("cursor", "cursor"),

  DISPLAY("display", "display"),

  FLEX("flex", "flex"),

  FLEX_BASIS("flexBasis", "flex-basis"),

  FLEX_DIRECTION("flexDirection", "flex-direction"),

  FLEX_FLOW("flexFlow", "flex-flow"),

  FLEX_GROW("flexGrow", "flex-grow"),

  FLEX_SHRINK("flexShrink", "flex-shrink"),

  FLEX_WRAP("flexWrap", "flex-wrap"),

  FLOAT("floatTo", "float"),

  FONT("font", "font"),

  FONT_FAMILY("fontFamily", "font-family"),

  FONT_SIZE("fontSize", "font-size"),

  FONT_STYLE("fontStyle", "font-style"),

  FONT_WEIGHT("fontWeight", "font-weight"),

  HEIGHT("height", "height"),

  JUSTIFY_CONTENT("justifyContent", "justify-content"),

  JUSTIFY_ITEMS("justifyItems", "justify-items"),

  JUSTIFY_SELF("justifySelf", "justify-self"),

  LEFT("left", "left"),

  LETTER_SPACING("letterSpacing", "letter-spacing"),

  LINE_HEIGHT("lineHeight", "line-height"),

  LIST_STYLE("listStyle", "list-style"),

  LIST_STYLE_IMAGE("listStyleImage", "list-style-image"),

  LIST_STYLE_POSITION("listStylePosition", "list-style-position"),

  LIST_STYLE_TYPE("listStyleType", "list-style-type"),

  MARGIN("margin", "margin"),

  MARGIN_BOTTOM("marginBottom", "margin-bottom"),

  MARGIN_LEFT("marginLeft", "margin-left"),

  MARGIN_RIGHT("marginRight", "margin-right"),

  MARGIN_TOP("marginTop", "margin-top"),

  MAX_HEIGHT("maxHeight", "max-height"),

  MAX_WIDTH("maxWidth", "max-width"),

  MIN_HEIGHT("minHeight", "min-height"),

  MIN_WIDTH("minWidth", "min-width"),

  OBJECT_FIT("objectFit", "object-fit"),

  OPACITY("opacity", "opacity"),

  OUTLINE("outline", "outline"),

  OUTLINE_COLOR("outlineColor", "outline-color"),

  OUTLINE_OFFSET("outlineOffset", "outline-offset"),

  OUTLINE_STYLE("outlineStyle", "outline-style"),

  OUTLINE_WIDTH("outlineWidth", "outline-width"),

  OVERFLOW("overflow", "overflow"),

  OVERFLOW_BLOCK("overflowBlock", "overflow-block"),

  OVERFLOW_INLINE("overflowInline", "overflow-inline"),

  OVERFLOW_X("overflowX", "overflow-x"),

  OVERFLOW_Y("overflowY", "overflow-y"),

  PADDING("padding", "padding"),

  PADDING_BOTTOM("paddingBottom", "padding-bottom"),

  PADDING_LEFT("paddingLeft", "padding-left"),

  PADDING_RIGHT("paddingRight", "padding-right"),

  PADDING_TOP("paddingTop", "padding-top"),

  POSITION("position", "position"),

  RESIZE("resize", "resize"),

  RIGHT("right", "right"),

  TAB_SIZE("tabSize", "tab-size"),

  TEXT_ALIGN("textAlign", "text-align"),

  TEXT_DECORATION("textDecoration", "text-decoration"),

  TEXT_DECORATION_COLOR("textDecorationColor", "text-decoration-color"),

  TEXT_DECORATION_LINE("textDecorationLine", "text-decoration-line"),

  TEXT_DECORATION_STYLE("textDecorationStyle", "text-decoration-style"),

  TEXT_DECORATION_THICKNESS("textDecorationThickness", "text-decoration-thickness"),

  TEXT_INDENT("textIndent", "text-indent"),

  TEXT_SHADOW("textShadow", "text-shadow"),

  TEXT_SIZE_ADJUST("textSizeAdjust", "text-size-adjust"),

  TEXT_TRANSFORM("textTransform", "text-transform"),

  TOP("top", "top"),

  TRANSFORM("transform", "transform"),

  VERTICAL_ALIGN("verticalAlign", "vertical-align"),

  WHITE_SPACE("whiteSpace", "white-space"),

  WIDTH("width", "width"),

  Z_INDEX("zIndex", "z-index");

  private static final StandardPropertyName[] ARRAY = StandardPropertyName.values();

  private static final UnmodifiableMap<String, StandardPropertyName> MAP = buildMap();

  private final String javaName;

  private final String name;

  private StandardPropertyName(String javaName, String name) {
    this.javaName = javaName;
    this.name = name;
  }

  public static StandardPropertyName getByCode(int code) {
    return ARRAY[code];
  }

  public static StandardPropertyName getByName(String name) {
    return MAP.get(name);
  }

  private static UnmodifiableMap<String, StandardPropertyName> buildMap() {
    var m = new GrowableMap<String, StandardPropertyName>();
    m.put("-moz-appearance", _MOZ_APPEARANCE);
    m.put("-moz-tab-size", _MOZ_TAB_SIZE);
    m.put("-webkit-appearance", _WEBKIT_APPEARANCE);
    m.put("-webkit-text-size-adjust", _WEBKIT_TEXT_SIZE_ADJUST);
    m.put("align-content", ALIGN_CONTENT);
    m.put("align-items", ALIGN_ITEMS);
    m.put("align-self", ALIGN_SELF);
    m.put("appearance", APPEARANCE);
    m.put("background", BACKGROUND);
    m.put("background-attachment", BACKGROUND_ATTACHMENT);
    m.put("background-clip", BACKGROUND_CLIP);
    m.put("background-color", BACKGROUND_COLOR);
    m.put("background-image", BACKGROUND_IMAGE);
    m.put("background-origin", BACKGROUND_ORIGIN);
    m.put("background-position", BACKGROUND_POSITION);
    m.put("background-repeat", BACKGROUND_REPEAT);
    m.put("background-size", BACKGROUND_SIZE);
    m.put("border", BORDER);
    m.put("border-bottom", BORDER_BOTTOM);
    m.put("border-bottom-color", BORDER_BOTTOM_COLOR);
    m.put("border-bottom-left-radius", BORDER_BOTTOM_LEFT_RADIUS);
    m.put("border-bottom-right-radius", BORDER_BOTTOM_RIGHT_RADIUS);
    m.put("border-bottom-style", BORDER_BOTTOM_STYLE);
    m.put("border-bottom-width", BORDER_BOTTOM_WIDTH);
    m.put("border-collapse", BORDER_COLLAPSE);
    m.put("border-color", BORDER_COLOR);
    m.put("border-left", BORDER_LEFT);
    m.put("border-left-color", BORDER_LEFT_COLOR);
    m.put("border-left-style", BORDER_LEFT_STYLE);
    m.put("border-left-width", BORDER_LEFT_WIDTH);
    m.put("border-radius", BORDER_RADIUS);
    m.put("border-right", BORDER_RIGHT);
    m.put("border-right-color", BORDER_RIGHT_COLOR);
    m.put("border-right-style", BORDER_RIGHT_STYLE);
    m.put("border-right-width", BORDER_RIGHT_WIDTH);
    m.put("border-style", BORDER_STYLE);
    m.put("border-top", BORDER_TOP);
    m.put("border-top-color", BORDER_TOP_COLOR);
    m.put("border-top-left-radius", BORDER_TOP_LEFT_RADIUS);
    m.put("border-top-right-radius", BORDER_TOP_RIGHT_RADIUS);
    m.put("border-top-style", BORDER_TOP_STYLE);
    m.put("border-top-width", BORDER_TOP_WIDTH);
    m.put("border-width", BORDER_WIDTH);
    m.put("bottom", BOTTOM);
    m.put("box-shadow", BOX_SHADOW);
    m.put("box-sizing", BOX_SIZING);
    m.put("clear", CLEAR);
    m.put("color", COLOR);
    m.put("content", CONTENT);
    m.put("cursor", CURSOR);
    m.put("display", DISPLAY);
    m.put("flex", FLEX);
    m.put("flex-basis", FLEX_BASIS);
    m.put("flex-direction", FLEX_DIRECTION);
    m.put("flex-flow", FLEX_FLOW);
    m.put("flex-grow", FLEX_GROW);
    m.put("flex-shrink", FLEX_SHRINK);
    m.put("flex-wrap", FLEX_WRAP);
    m.put("float", FLOAT);
    m.put("font", FONT);
    m.put("font-family", FONT_FAMILY);
    m.put("font-size", FONT_SIZE);
    m.put("font-style", FONT_STYLE);
    m.put("font-weight", FONT_WEIGHT);
    m.put("height", HEIGHT);
    m.put("justify-content", JUSTIFY_CONTENT);
    m.put("justify-items", JUSTIFY_ITEMS);
    m.put("justify-self", JUSTIFY_SELF);
    m.put("left", LEFT);
    m.put("letter-spacing", LETTER_SPACING);
    m.put("line-height", LINE_HEIGHT);
    m.put("list-style", LIST_STYLE);
    m.put("list-style-image", LIST_STYLE_IMAGE);
    m.put("list-style-position", LIST_STYLE_POSITION);
    m.put("list-style-type", LIST_STYLE_TYPE);
    m.put("margin", MARGIN);
    m.put("margin-bottom", MARGIN_BOTTOM);
    m.put("margin-left", MARGIN_LEFT);
    m.put("margin-right", MARGIN_RIGHT);
    m.put("margin-top", MARGIN_TOP);
    m.put("max-height", MAX_HEIGHT);
    m.put("max-width", MAX_WIDTH);
    m.put("min-height", MIN_HEIGHT);
    m.put("min-width", MIN_WIDTH);
    m.put("object-fit", OBJECT_FIT);
    m.put("opacity", OPACITY);
    m.put("outline", OUTLINE);
    m.put("outline-color", OUTLINE_COLOR);
    m.put("outline-offset", OUTLINE_OFFSET);
    m.put("outline-style", OUTLINE_STYLE);
    m.put("outline-width", OUTLINE_WIDTH);
    m.put("overflow", OVERFLOW);
    m.put("overflow-block", OVERFLOW_BLOCK);
    m.put("overflow-inline", OVERFLOW_INLINE);
    m.put("overflow-x", OVERFLOW_X);
    m.put("overflow-y", OVERFLOW_Y);
    m.put("padding", PADDING);
    m.put("padding-bottom", PADDING_BOTTOM);
    m.put("padding-left", PADDING_LEFT);
    m.put("padding-right", PADDING_RIGHT);
    m.put("padding-top", PADDING_TOP);
    m.put("position", POSITION);
    m.put("resize", RESIZE);
    m.put("right", RIGHT);
    m.put("tab-size", TAB_SIZE);
    m.put("text-align", TEXT_ALIGN);
    m.put("text-decoration", TEXT_DECORATION);
    m.put("text-decoration-color", TEXT_DECORATION_COLOR);
    m.put("text-decoration-line", TEXT_DECORATION_LINE);
    m.put("text-decoration-style", TEXT_DECORATION_STYLE);
    m.put("text-decoration-thickness", TEXT_DECORATION_THICKNESS);
    m.put("text-indent", TEXT_INDENT);
    m.put("text-shadow", TEXT_SHADOW);
    m.put("text-size-adjust", TEXT_SIZE_ADJUST);
    m.put("text-transform", TEXT_TRANSFORM);
    m.put("top", TOP);
    m.put("transform", TRANSFORM);
    m.put("vertical-align", VERTICAL_ALIGN);
    m.put("white-space", WHITE_SPACE);
    m.put("width", WIDTH);
    m.put("z-index", Z_INDEX);
    return m.toUnmodifiableMap();
  }

  public static int size() {
    return ARRAY.length;
  }

  @Override
  public final int getCode() {
    return ordinal();
  }

  public final String getJavaName() {
    return javaName;
  }

  @Override
  public final String getName() {
    return name;
  }
}
