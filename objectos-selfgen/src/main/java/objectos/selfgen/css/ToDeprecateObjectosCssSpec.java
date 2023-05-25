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
package objectos.selfgen.css;

import objectos.selfgen.css.spec.Prefix;
import objectos.selfgen.css.spec.Source;

public class ToDeprecateObjectosCssSpec extends ToDeprecateCssSpec {

  @Override
  protected final void definition() {
    elementNamesAt("/html-elements.txt");

    defineColors();
    defineKeywordTypes();
    defineLengthUnits();
    defineProperties();
    definePseudoClasses();
    definePseudoElements();
    defineValueTypes();
  }

  private void defineColors() {
    colors(
        "black:#000000",
        "silver:#c0c0c0",
        "gray:#808080",
        "white:#ffffff",
        "maroon:#800000",
        "red:#ff0000",
        "purple:#800080",
        "fuchsia:#ff00ff",
        "green:#008000",
        "lime:#00ff00",
        "olive:#808000",
        "yellow:#ffff00",
        "navy:#000080",
        "blue:#0000ff",
        "teal:#008080",
        "aqua:#00ffff",
        "orange:#ffa500",
        "aliceblue:#f0f8ff",
        "antiquewhite:#faebd7",
        "aquamarine:#7fffd4",
        "azure:#f0ffff",
        "beige:#f5f5dc",
        "bisque:#ffe4c4",
        "blanchedalmond:#ffebcd",
        "blueviolet:#8a2be2",
        "brown:#a52a2a",
        "burlywood:#deb887",
        "cadetblue:#5f9ea0",
        "chartreuse:#7fff00",
        "chocolate:#d2691e",
        "coral:#ff7f50",
        "cornflowerblue:#6495ed",
        "cornsilk:#fff8dc",
        "crimson:#dc143c",
        "cyan:#00ffff",
        "darkblue:#00008b",
        "darkcyan:#008b8b",
        "darkgoldenrod:#b8860b",
        "darkgray:#a9a9a9",
        "darkgreen:#006400",
        "darkgrey:#a9a9a9",
        "darkkhaki:#bdb76b",
        "darkmagenta:#8b008b",
        "darkolivegreen:#556b2f",
        "darkorange:#ff8c00",
        "darkorchid:#9932cc",
        "darkred:#8b0000",
        "darksalmon:#e9967a",
        "darkseagreen:#8fbc8f",
        "darkslateblue:#483d8b",
        "darkslategray:#2f4f4f",
        "darkslategrey:#2f4f4f",
        "darkturquoise:#00ced1",
        "darkviolet:#9400d3",
        "deeppink:#ff1493",
        "deepskyblue:#00bfff",
        "dimgray:#696969",
        "dimgrey:#696969",
        "dodgerblue:#1e90ff",
        "firebrick:#b22222",
        "floralwhite:#fffaf0",
        "forestgreen:#228b22",
        "gainsboro:#dcdcdc",
        "ghostwhite:#f8f8ff",
        "gold:#ffd700",
        "goldenrod:#daa520",
        "greenyellow:#adff2f",
        "grey:#808080",
        "honeydew:#f0fff0",
        "hotpink:#ff69b4",
        "indianred:#cd5c5c",
        "indigo:#4b0082",
        "ivory:#fffff0",
        "khaki:#f0e68c",
        "lavender:#e6e6fa",
        "lavenderblush:#fff0f5",
        "lawngreen:#7cfc00",
        "lemonchiffon:#fffacd",
        "lightblue:#add8e6",
        "lightcoral:#f08080",
        "lightcyan:#e0ffff",
        "lightgoldenrodyellow:#fafad2",
        "lightgray:#d3d3d3",
        "lightgreen:#90ee90",
        "lightgrey:#d3d3d3",
        "lightpink:#ffb6c1",
        "lightsalmon:#ffa07a",
        "lightseagreen:#20b2aa",
        "lightskyblue:#87cefa",
        "lightslategray:#778899",
        "lightslategrey:#778899",
        "lightsteelblue:#b0c4de",
        "lightyellow:#ffffe0",
        "limegreen:#32cd32",
        "linen:#faf0e6",
        "magenta:#ff00ff",
        "mediumaquamarine:#66cdaa",
        "mediumblue:#0000cd",
        "mediumorchid:#ba55d3",
        "mediumpurple:#9370db",
        "mediumseagreen:#3cb371",
        "mediumslateblue:#7b68ee",
        "mediumspringgreen:#00fa9a",
        "mediumturquoise:#48d1cc",
        "mediumvioletred:#c71585",
        "midnightblue:#191970",
        "mintcream:#f5fffa",
        "mistyrose:#ffe4e1",
        "moccasin:#ffe4b5",
        "navajowhite:#ffdead",
        "oldlace:#fdf5e6",
        "olivedrab:#6b8e23",
        "orangered:#ff4500",
        "orchid:#da70d6",
        "palegoldenrod:#eee8aa",
        "palegreen:#98fb98",
        "paleturquoise:#afeeee",
        "palevioletred:#db7093",
        "papayawhip:#ffefd5",
        "peachpuff:#ffdab9",
        "peru:#cd853f",
        "pink:#ffc0cb",
        "plum:#dda0dd",
        "powderblue:#b0e0e6",
        "rosybrown:#bc8f8f",
        "royalblue:#4169e1",
        "saddlebrown:#8b4513",
        "salmon:#fa8072",
        "sandybrown:#f4a460",
        "seagreen:#2e8b57",
        "seashell:#fff5ee",
        "sienna:#a0522d",
        "skyblue:#87ceeb",
        "slateblue:#6a5acd",
        "slategray:#708090",
        "slategrey:#708090",
        "snow:#fffafa",
        "springgreen:#00ff7f",
        "steelblue:#4682b4",
        "tan:#d2b48c",
        "thistle:#d8bfd8",
        "tomato:#ff6347",
        "turquoise:#40e0d0",
        "violet:#ee82ee",
        "wheat:#f5deb3",
        "whitesmoke:#f5f5f5",
        "yellowgreen:#9acd32",
        "rebeccapurple:#663399",

        "transparent",

        "ActiveBorder",
        "ActiveCaption",
        "AppWorkspace",
        "Background",
        "ButtonFace",
        "ButtonHighlight",
        "ButtonShadow",
        "ButtonText",
        "CaptionText",
        "GrayText",
        "Highlight",
        "HighlightText",
        "InactiveBorder",
        "InactiveCaption",
        "InactiveCaptionText",
        "InfoBackground",
        "InfoText",
        "Menu",
        "MenuText",
        "Scrollbar",
        "ThreeDDarkShadow",
        "ThreeDFace",
        "ThreeDHighlight",
        "ThreeDLightShadow",
        "ThreeDShadow",
        "Window",
        "WindowFrame",
        "WindowText"
    );
  }

  private void defineKeywordTypes() {
    // cursor property keyword values
    keywordType(
        "CursorKeyword",

        "alias",
        "all-scroll",
        "auto",
        "cell",
        "context-menu",
        "copy",
        "crosshair",
        "default",
        "grab",
        "grabbing",
        "help",
        "move",
        "none",
        "not-allowed",
        "no-drop",
        "pointer",
        "progress",
        "text",
        "vertical-text",
        "wait",
        "zoom-in",
        "zoom-out",

        "e-resize",
        "n-resize",
        "ne-resize",
        "nw-resize",
        "s-resize",
        "se-resize",
        "sw-resize",
        "w-resize",
        "ew-resize",
        "ns-resize",
        "nesw-resize",
        "nwse-resize",
        "col-resize",
        "row-resize"
    );

    // <generic-family> of font-family css property
    keywordType(
        "FontFamilyKeyword",
        "serif", "sans-serif", "cursive", "fantasy", "monospace"
    );

    // text-decoration-line property special keywords
    keywordType(
        "TextDecorationLineKeyword",
        "underline", "overline", "line-through", "blink"
    );
  }

  private void defineLengthUnits() {
    lengthUnits(
        "em",
        "ex",
        "ch",
        "rem",
        "vw",
        "vh",
        "vmin",
        "vmax",
        "cm",
        "mm",
        "q",
        "in",
        "pt",
        "pc",
        "px"
    );
  }

  private void defineProperties() {
    property(
        "appearance",
        "none | auto | button | textfield | searchfield | textarea | push-button | button-bevel | slider-horizontal | checkbox | radio | square-button | menulist | menulist-button | listbox | meter | progress-bar",
        Source.MANUAL_ENTRY,
        Prefix.WEBKIT, Prefix.MOZILLA
    );
    property(
        "azimuth",
        "<angle> | [[ left-side | far-left | left | center-left | center | center-right | right | far-right | right-side ] || behind ] | leftwards | rightwards | inherit",
        Source.W3C
    );
    // property(nname = "background", "[<background-color> ||
    // <background-image> || <background-repeat> || <background-attachment>
    // || <background-position>] | inherit", Source.W3C);
    property(
        "background",
        "%Background% | inherit",
        Source.W3C
    );
    property(
        "background-attachment",
        "scroll | fixed | inherit",
        Source.W3C
    );
    property(
        "background-color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "background-image",
        "<uri> | none | inherit",
        Source.W3C
    );
    property(
        "background-position",
        "[ [ <percentage> | <length> | left | center | right ] [ <percentage> | <length> | top | center | bottom ]? ] | [ [ left | center | right ] || [ top | center | bottom ] ] | inherit",
        Source.W3C
    );
    property(
        "background-repeat",
        "repeat | repeat-x | repeat-y | no-repeat | inherit",
        Source.W3C
    );
    // based on MDN:
    // https://developer.mozilla.org/en-US/docs/Web/CSS/background-size
    property(
        "background-size",
        "[ <length-or-percentage> | auto ]{1,2} | cover | contain",
        Source.MANUAL_ENTRY
    );
    property(
        "border",
        "[ <border-width> || <border-style> || <border-top-color> ] | inherit",
        Source.W3C
    );
    property(
        "border-bottom",
        "[ <border-width> || <border-style> || <border-top-color> ] | inherit",
        Source.W3C
    );
    property(
        "border-bottom-color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "border-bottom-style",
        "<border-style> | inherit",
        Source.W3C
    );
    property(
        "border-bottom-width",
        "<border-width> | inherit",
        Source.W3C
    );
    property(
        "border-collapse",
        "collapse | separate | inherit",
        Source.W3C
    );
    property(
        "border-color",
        "<color>{1,4} | inherit",
        Source.W3C
    );
    property(
        "border-left",
        "[ <border-width> || <border-style> || <border-top-color> ] | inherit",
        Source.W3C
    );
    property(
        "border-left-color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "border-left-style",
        "<border-style> | inherit",
        Source.W3C
    );
    property(
        "border-left-width",
        "<border-width> | inherit",
        Source.W3C
    );
    property(
        "border-right",
        "[ <border-width> || <border-style> || <border-top-color> ] | inherit",
        Source.W3C
    );
    property(
        "border-right-color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "border-right-style",
        "<border-style> | inherit",
        Source.W3C
    );
    property(
        "border-right-width",
        "<border-width> | inherit",
        Source.W3C
    );
    property(
        "border-spacing",
        "<length> <length>? | inherit",
        Source.W3C
    );
    property(
        "border-style",
        "<border-style>{1,4} | inherit",
        Source.W3C
    );
    property(
        "border-top",
        "[ <border-width> || <border-style> || <border-top-color> ] | inherit",
        Source.W3C
    );
    property(
        "border-top-color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "border-top-style",
        "<border-style> | inherit",
        Source.W3C
    );
    property(
        "border-top-width",
        "<border-width> | inherit",
        Source.W3C
    );
    property(
        "border-width",
        "<border-width>{1,4} | inherit",
        Source.W3C
    );
    property(
        "bottom",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "box-sizing",
        "content-box | border-box",
        Source.W3C
    );
    property(
        "caption-side",
        "top | bottom | inherit",
        Source.W3C
    );
    property(
        "caret-color",
        "auto | <color>",
        Source.W3C
    );
    property(
        "clear",
        "none | left | right | both | inherit",
        Source.W3C
    );
    property(
        "clip",
        "<shape> | auto | inherit",
        Source.W3C
    );
    property(
        "color",
        "<color> | inherit",
        Source.W3C
    );
    property(
        "column-count",
        "auto | <integer>",
        Source.W3C
    );
    property(
        "column-fill",
        "auto | balance | balance-all",
        Source.W3C
    );
    property(
        "column-gap",
        "<length-percentage> | normal",
        Source.W3C
    );
    property(
        "column-rule",
        "<column-rule-width> || <column-rule-style> || <column-rule-color>",
        Source.W3C
    );
    property(
        "column-rule-color",
        "<color>",
        Source.W3C
    );
    property(
        "column-rule-style",
        "<line-style>",
        Source.W3C
    );
    property(
        "column-rule-width",
        "<line-width>",
        Source.W3C
    );
    property(
        "column-span",
        "none | all",
        Source.W3C
    );
    property(
        "column-width",
        "auto | <length>",
        Source.W3C
    );
    property(
        "columns",
        "<column-width> || <column-count>",
        Source.W3C
    );
    // property(name = "content", "normal | none | [ <string> |
    // <uri> | <counter> | attr(<identifier>) | open-quote | close-quote |
    // no-open-quote | no-close-quote ]+ | inherit", Source.W3C);
    property(
        "content",
        "<string>",
        Source.MANUAL_ENTRY
    );
    // property(name = "counter-increment", "[ <identifier>
    // <integer>? ]+ | none | inherit", Source.W3C);
    property(
        "counter-increment",
        "%Counter% | none | inherit",
        Source.W3C
    );
    property(
        "counter-reset",
        "%Counter% | none | inherit",
        Source.W3C
    );
    property(
        "cue",
        "[ <cue-before> || <cue-after> ] | inherit",
        Source.W3C
    );
    property(
        "cue-after",
        "<uri> | none | inherit",
        Source.W3C
    );
    property(
        "cue-before",
        "<uri> | none | inherit",
        Source.W3C
    );
    // property(name = "cursor", "[ [<uri> ,]* [ auto | crosshair
    // | default | pointer | move | e-resize | ne-resize | nw-resize |
    // n-resize | se-resize | sw-resize | s-resize | w-resize | text | wait
    // | help | progress ] ] | inherit", Source.W3C);
    // property(name = "cursor", "[ [<url> [<x> <y>]?,]* [ auto |
    // default | none | context-menu | help | pointer | progress | wait |
    // cell | crosshair | text | vertical-text | alias | copy | move |
    // no-drop | not-allowed | grab | grabbing | e-resize | n-resize |
    // ne-resize | nw-resize | s-resize | se-resize | sw-resize | w-resize |
    // ew-resize | ns-resize | nesw-resize | nwse-resize | col-resize |
    // row-resize | all-scroll | zoom-in | zoom-out ] ]", source =
    // Source.MDN);
    property(
        "cursor",
        "%Cursor% | inherit",
        Source.MANUAL_ENTRY
    );
    property(
        "direction",
        "ltr | rtl | inherit",
        Source.W3C
    );
    property(
        "display",
        "inline | block | list-item | inline-block | table | inline-table | table-row-group | table-header-group | table-footer-group | table-row | table-column-group | table-column | table-cell | table-caption | none | inherit",
        Source.W3C
    );
    property(
        "elevation",
        "<angle> | below | level | above | higher | lower | inherit",
        Source.W3C
    );
    property(
        "empty-cells",
        "show | hide | inherit",
        Source.W3C
    );
    property(
        "float",
        "left | right | none | inherit",
        Source.W3C
    );
    property(
        "font",
        "%Font% | caption | icon | menu | message-box | small-caption | status-bar | inherit",
        Source.MANUAL_ENTRY
    );

    // property(name = "font", "[ [ <font-style> ||
    // <font-variant> || <font-weight> ]? <font-size> [ / <line-height> ]?
    // <font-family> ] | caption | icon | menu | message-box | small-caption
    // | status-bar | inherit", Source.W3C);

    // property(name = "font-family", "[[ <family-name> |
    // <generic-family> ] [, [ <family-name>| <generic-family>] ]* ] |
    // inherit", Source.W3C);

    property(
        "font-size",
        "<absolute-size> | <relative-size> | <length> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "font-style",
        "normal | italic | oblique | inherit",
        Source.W3C
    );
    property(
        "font-variant",
        "normal | small-caps | inherit",
        Source.W3C
    );
    property(
        "font-weight",
        "normal | bold | bolder | lighter | 100 | 200 | 300 | 400 | 500 | 600 | 700 | 800 | 900 | inherit",
        Source.W3C
    );
    property(
        "height",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "left",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "letter-spacing",
        "normal | <length> | inherit",
        Source.W3C
    );
    property(
        "line-height",
        "normal | <number> | <length> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "list-style",
        "[ <list-style-type> || <list-style-position> || <list-style-image> ] | inherit",
        Source.W3C
    );
    property(
        "list-style-image",
        "<uri> | none | inherit",
        Source.W3C
    );
    property(
        "list-style-position",
        "inside | outside | inherit",
        Source.W3C
    );
    property(
        "list-style-type",
        "disc | circle | square | decimal | decimal-leading-zero | lower-roman | upper-roman | lower-greek | lower-latin | upper-latin | armenian | georgian | lower-alpha | upper-alpha | none | inherit",
        Source.W3C
    );
    property(
        "margin",
        "<margin-width>{1,4} | inherit",
        Source.W3C
    );
    property(
        "margin-bottom",
        "<margin-width> | inherit",
        Source.W3C
    );
    property(
        "margin-left",
        "<margin-width> | inherit",
        Source.W3C
    );
    property(
        "margin-right",
        "<margin-width> | inherit",
        Source.W3C
    );
    property(
        "margin-top",
        "<margin-width> | inherit",
        Source.W3C
    );
    property(
        "max-height",
        "<length> | <percentage> | none | inherit",
        Source.W3C
    );
    property(
        "max-width",
        "<length> | <percentage> | none | inherit",
        Source.W3C
    );
    property(
        "min-height",
        "<length> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "min-width",
        "<length> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "orphans",
        "<integer> | inherit",
        Source.W3C
    );
    property(
        "outline",
        "[ <outline-color> || <outline-style> || <outline-width> ] | inherit",
        Source.W3C
    );
    // property(name = "outline", "[ <outline-color> ||
    // <outline-style> || <outline-width> ]", Source.W3C);
    property(
        "outline-color",
        "<color> | invert | inherit",
        Source.W3C
    );
    // property(name = "outline-color", "<color> | invert",
    // Source.W3C);
    property(
        "outline-offset",
        "<length>",
        Source.W3C
    );
    property(
        "outline-style",
        "<border-style> | auto | inherit",
        Source.MANUAL_ENTRY
    );
    // property(name = "outline-style", "<border-style> |
    // inherit", Source.W3C);
    // property(name = "outline-style", "auto | <border-style>",
    // Source.W3C);
    property(
        "outline-width",
        "<border-width> | inherit",
        Source.W3C
    );
    // property(name = "outline-width", "<border-width>", source
    // = Source.W3C);
    property(
        "overflow",
        "visible | hidden | scroll | auto | inherit",
        Source.W3C
    );
    property(
        "padding",
        "<length-or-percentage>{1,4} | inherit",
        Source.W3C
    );
    property(
        "padding-bottom",
        "<length-or-percentage> | inherit",
        Source.W3C
    );
    property(
        "padding-left",
        "<length-or-percentage> | inherit",
        Source.W3C
    );
    property(
        "padding-right",
        "<length-or-percentage> | inherit",
        Source.W3C
    );
    property(
        "padding-top",
        "<length-or-percentage> | inherit",
        Source.W3C
    );
    property(
        "page-break-after",
        "auto | always | avoid | left | right | inherit",
        Source.W3C
    );
    property(
        "page-break-before",
        "auto | always | avoid | left | right | inherit",
        Source.W3C
    );
    property(
        "page-break-inside",
        "avoid | auto | inherit",
        Source.W3C
    );
    property(
        "pause",
        "[ <pause-before> <pause-after>? ] | inherit",
        Source.MANUAL_ENTRY
    );
    // property(name = "pause", "[ [<time> | <percentage>]{1,2} ]
    // | inherit", Source.W3C);
    property(
        "pause-after",
        "<time> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "pause-before",
        "<time> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "pitch",
        "<frequency> | x-low | low | medium | high | x-high | inherit",
        Source.W3C
    );
    property(
        "pitch-range",
        "<number> | inherit",
        Source.W3C
    );
    property(
        "play-during",
        "%PlayDuring% | auto | none | inherit",
        Source.MANUAL_ENTRY
    );
    // property(name = "play-during", "<uri> [ mix || repeat ]? |
    // auto | none | inherit", Source.W3C);
    property(
        "position",
        "static | relative | absolute | fixed | inherit",
        Source.W3C
    );
    property(
        "quotes",
        "%Quotes% | none | inherit",
        Source.W3C
    );
    // property(name = "quotes", "[<string> <string>]+ | none |
    // inherit", Source.W3C);
    property(
        "resize",
        "none | both | horizontal | vertical",
        Source.W3C
    );
    property(
        "richness",
        "<number> | inherit",
        Source.W3C
    );
    property(
        "right",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "speak",
        "normal | none | spell-out | inherit",
        Source.W3C
    );
    property(
        "speak-header",
        "once | always | inherit",
        Source.W3C
    );
    property(
        "speak-numeral",
        "digits | continuous | inherit",
        Source.W3C
    );
    property(
        "speak-punctuation",
        "code | none | inherit",
        Source.W3C
    );
    property(
        "speech-rate",
        "<number> | x-slow | slow | medium | fast | x-fast | faster | slower | inherit",
        Source.W3C
    );
    property(
        "stress",
        "<number> | inherit",
        Source.W3C
    );
    property(
        "table-layout",
        "auto | fixed | inherit",
        Source.W3C
    );
    property(
        "text-align",
        "left | right | center | justify | inherit",
        Source.W3C
    );
    property(
        "text-decoration",
        "<text-decoration-line> || <text-decoration-style> || <text-decoration-color>",
        Source.W3C
    );
    property(
        "text-decoration-color",
        "<color>",
        Source.W3C
    );
    property(
        "text-decoration-line",
        "none | %TextDecorationLineKeyword%{1,4} | inherit",
        Source.W3C
    );
    property(
        "text-decoration-style",
        "solid | double | dotted | dashed | wavy",
        Source.W3C
    );
    property(
        "text-indent",
        "<length> | <percentage> | inherit",
        Source.W3C
    );
    property(
        "text-overflow",
        "clip | ellipsis",
        Source.W3C
    );
    property(
        "text-size-adjust",
        "none | auto | <percentage> | inherit",
        Source.MDN,
        Prefix.WEBKIT, Prefix.MOZILLA, Prefix.MICROSOFT);
    property(
        "text-transform",
        "capitalize | uppercase | lowercase | none | inherit",
        Source.W3C
    );
    property(
        "top",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "unicode-bidi",
        "normal | embed | bidi-override | inherit",
        Source.W3C
    );
    property(
        "vertical-align",
        "baseline | sub | super | top | text-top | middle | bottom | text-bottom | <percentage> | <length> | inherit",
        Source.W3C
    );
    property(
        "visibility",
        "visible | hidden | collapse | inherit",
        Source.W3C
    );
    property(
        "voice-family",
        "%VoiceFamily% | inherit",
        Source.W3C
    );
    property(
        "volume",
        "<number> | <percentage> | silent | x-soft | soft | medium | loud | x-loud | inherit",
        Source.W3C
    );
    property(
        "white-space",
        "normal | pre | nowrap | pre-wrap | pre-line | inherit",
        Source.W3C
    );
    property(
        "widows",
        "<integer> | inherit",
        Source.W3C
    );
    property(
        "width",
        "<length> | <percentage> | auto | inherit",
        Source.W3C
    );
    property(
        "word-spacing",
        "normal | <length> | inherit",
        Source.W3C
    );
    property(
        "z-index",
        "auto | <integer> | inherit",
        Source.W3C
    );
  }

  private void definePseudoClasses() {
    pseudoClasses(
        "active",
        "any-link",
        "blank",
        "checked",
        "current",
        "default",
        "defined",
        "disabled",
        "drop",
        "empty",
        "enabled",
        "first",
        "first-child",
        "first-of-type",
        "fullscreen",
        "future",
        "focus",
        "focus-visible",
        "focus-within",
        "host",
        "hover",
        "indeterminate",
        "in-range",
        "invalid",
        "last-child",
        "last-of-type",
        "left",
        "link",
        "local-link",
        "only-child",
        "only-of-type",
        "optional",
        "out-of-range",
        "past",
        "placeholder-shown",
        "read-only",
        "read-write",
        "required",
        "right",
        "root",
        "scope",
        "target",
        "target-within",
        "user-invalid",
        "valid",
        "visited",

        "-moz-focusring"
    );
  }

  private void definePseudoElements() {
    pseudoElements(
        "after",
        "backdrop",
        "before",
        "cue",
        "first-letter",
        "first-line",
        "grammar-error",
        "marker",
        "placeholder",
        "selection",
        "spelling-error",

        "-moz-focus-inner",
        "-webkit-inner-spin-button",
        "-webkit-outer-spin-button",
        "-webkit-search-decoration",
        "-webkit-file-upload-button"
    );
  }

  private void defineValueTypes() {
    valueType(
        "border-style",
        "none | hidden | dotted | dashed | solid | double | groove | ridge | inset | outset"
    );
    valueType(
        "border-width",
        "thin | medium | thick | <length>"
    );
    valueType(
        "margin-width",
        "<length> | <percentage> | auto"
    );
    valueType(
        "length-or-percentage",
        "<length> | <percentage>"
    );
    // @ValueType(name = "Note.", "<time> | <percentage>", source =
    // Source.W3C)
  }

}
