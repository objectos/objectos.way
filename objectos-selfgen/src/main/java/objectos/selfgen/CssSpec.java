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
package objectos.selfgen;

import java.io.IOException;
import java.util.List;
import objectos.selfgen.css2.ColorValue;
import objectos.selfgen.css2.CssSelfGen;
import objectos.selfgen.css2.KeywordName;
import objectos.selfgen.css2.LengthType;
import objectos.selfgen.css2.PercentageType;
import objectos.selfgen.css2.SelectorKind;
import objectos.selfgen.css2.ValueType;

public final class CssSpec extends CssSelfGen {

  private ColorValue color;

  private ValueType globalKeyword;

  private ValueType image;

  private LengthType length;

  private ValueType lengthPercentage;

  private PercentageType percentage;

  private ValueType numberValue;

  public static void main(String[] args) throws IOException {
    CssSpec spec;
    spec = new CssSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    $defineSelectors();
    $defineGlobalKeyword();
    $defineColor();
    $defineImage();
    $defineLength();
    percentage = percentage();
    defineDoubleType();
    defineIntType();
    defineFilterFunction();

    lengthPercentage = t(
      "LengthPercentage",

      length,
      percentage
    );

    numberValue = t(
      "NumberValue",

      doubleType,
      intType
    );

    // keyword name clashes

    keywordFieldName("default", "default_");
    keywordFieldName("double", "double_");
    keywordFieldName("static", "static_");
    keywordFieldName("super", "super_");

    // A

    appearance();

    // B

    backgroundAttachment();
    backgroundClip();
    backgroundColor();
    backgroundImage();
    backgroundPosition();
    backgroundRepeat();
    background();
    borderCollapse();
    borderColor();
    borderRadius();
    borderSpacing();
    borderStyle();
    borderWidth();
    border();
    bottom();
    boxShadow();
    boxSizing();

    // C

    color();
    content();
    cursor();

    // D

    display();

    // F

    fill();
    filter();
    flexDirection();
    flexGrow();
    flexShrink();
    flexWrap();
    font();
    fontFamily();
    fontFeatureSettings();
    fontSize();
    fontStyle();
    fontVariationSettings();
    fontWeight();

    // H

    height();

    // J

    justifyContent();

    // L

    left();
    letterSpacing();
    lineHeight();
    listStyleImage();
    listStylePosition();
    listStyleType();
    listStyle();

    // M

    margin();
    maxHeight();
    maxWidth();
    minHeight();
    minWidth();

    // O

    opacity();
    outlineColor();
    outlineOffset();
    outlineStyle();
    outlineWidth();
    outline();

    // P

    padding();
    paddingBlock();
    paddingInline();
    pointerEvents();
    position();

    // Q

    quotes();

    // R

    resize();

    // T

    tabSize();
    textAlign();
    textDecorationColor();
    textDecorationLine();
    textDecorationStyle();
    textDecorationThickness();
    textDecoration();
    textIndent();
    textSizeAdjust();
    textTransform();
    top();

    // V

    verticalAlign();

    // W

    width();
    wordBreak();
  }

  private void $defineColor() {
    color = color(
      "currentcolor",
      "transparent",

      "aqua",
      "black",
      "blue",
      "fuchsia",
      "gray",
      "green",
      "maroon",
      "lime",
      "navy",
      "olive",
      "purple",
      "red",
      "silver",
      "teal",
      "white",
      "yellow"
    );

    colorPalette();
  }

  private void $defineGlobalKeyword() {
    globalKeyword = t(
      "GlobalKeyword",

      k("inherit"),
      k("initial"),
      k("unset")
    );
  }

  private void $defineImage() {
    image = t(
      "Image",

      url()
    );
  }

  private void $defineLength() {
    length = length(
      "ch", "cm",
      "em", "ex",
      "in",
      "mm",
      "pc", "pt", "px",
      "q",
      "rem",
      "vh", "vmax", "vmin", "vw"
    );
  }

  private void $defineSelectors() {
    selectors(
      SelectorKind.TYPE,

      "a",
      "abbr",
      "acronym",
      "address",
      "article",
      "aside",
      "audio",

      "b",
      "big",
      "body",
      "blockquote",
      "button",

      "canvas",
      "caption",
      "cite",
      "code",

      "dd",
      "del",
      "details",
      "dfn",
      "div",
      "dl",
      "dt",

      "em",
      "embed",

      "fieldset",
      "figcaption",
      "figure",
      "form",
      "footer",

      "h1",
      "h2",
      "h3",
      "h4",
      "h5",
      "h6",
      "header",
      "hgroup",
      "hr",
      "html",

      "iframe",
      "img",
      "input",
      "ins",

      "kbd",

      "label",
      "legend",
      "li",

      "mark",
      "menu",

      "nav",

      "object",
      "ol",
      "optgroup",
      "output",

      "p",
      "pre",
      "progress",

      "q",

      "ruby",

      "samp",
      "section",
      "select",
      "small",
      "span",
      "strike",
      "strong",
      "sub",
      "summary",
      "sup",
      "svg",

      "table",
      "tbody",
      "td",
      "textarea",
      "tfoot",
      "th",
      "thead",
      "time",
      "tr",

      "ul",

      "var",
      "video"
    );

    selectors(
      SelectorKind.PSEUDO_CLASS,

      ":disabled",
      ":first-child",
      ":first-of-type",
      ":focus",
      ":hover",
      ":root",
      ":-moz-focusring",
      ":-moz-ui-invalid"
    );

    selectors(
      SelectorKind.PSEUDO_ELEMENT,

      "::after",
      "::before",
      "::placeholder",
      "::-webkit-file-upload-button",
      "::-webkit-inner-spin-button",
      "::-webkit-outer-spin-button",
      "::-webkit-search-decoration"
    );
  }

  private void appearance() {
    var appearanceValue = t(
      "AppearanceValue",

      k("auto"),
      k("menulist-button"),
      k("none"),
      k("textfield"),

      k("button"),
      k("checkbox"),
      k("listbox"),
      k("menulist"),
      k("meter"),
      k("progress-bar"),
      k("push-button"),
      k("radio"),
      k("searchfield"),
      k("slider-horizontal"),
      k("square-button"),
      k("textarea")
    );

    var names = List.of("appearance", "-moz-appearance", "-webkit-appearance");

    for (var name : names) {
      property(
        name,

        sig(globalKeyword, "value"),
        sig(appearanceValue, "value")
      );
    }
  }

  private void background() {
    var shorthand = t(
      "BackgroundValue",

      color,
      t("BackgroundAttachmentValue"),
      t("BackgroundClipValue"),
      t("BackgroundImageValue"),
      t("BackgroundPositionValue"),
      t("BackgroundRepeatValue")
    );

    property(
      "background",

      sig(globalKeyword, "value"),
      sig(shorthand, "value"),
      sig(shorthand, "value1", shorthand, "value2"),
      sig(shorthand, "value1", shorthand, "value2", shorthand, "value3"),
      sig(shorthand, "value1", shorthand, "value2", shorthand, "value3", shorthand, "value4"),
      sig(
        shorthand, "value1",
        shorthand, "value2",
        shorthand, "value3",
        shorthand, "value4",
        shorthand, "value5"
      )
    );
  }

  private void backgroundAttachment() {
    var value = t(
      "BackgroundAttachmentValue",

      k("fixed"),
      k("local"),
      k("scroll")
    );

    property(
      "background-attachment",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void backgroundClip() {
    var value = t(
      "BackgroundClipValue",

      k("border-box"),
      k("content-box"),
      k("padding-box")
    );

    property(
      "background-clip",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void backgroundColor() {
    property(
      "background-color",

      sig(globalKeyword, "value"),
      sig(color, "value")
    );
  }

  private void backgroundImage() {
    var backgroundImageValue = t(
      "BackgroundImageValue",

      k("none")
    );

    property(
      "background-image",

      sig(globalKeyword, "value"),
      sig(backgroundImageValue, "value")
    );
  }

  private void backgroundPosition() {
    ValueType bgPosition = t(
      "BackgroundPositionValue",
      k("center"),
      k("top"),
      k("left"),
      k("bottom"),
      k("right"),
      length,
      percentage
    );

    property(
      "background-position",

      sig(globalKeyword, "value"),
      sig(bgPosition, "value"),
      sig(bgPosition, "value1", bgPosition, "value2"),
      sig(bgPosition, "value1", bgPosition, "value2", bgPosition, "value3"),
      sig(bgPosition, "value1", bgPosition, "value2", bgPosition, "value3", bgPosition, "value4")
    );
  }

  private void backgroundRepeat() {
    ValueType arity2;
    arity2 = t(
      "BackgroundRepeatValue2",

      k("repeat"),
      k("space"),
      k("round"),
      k("no-repeat")
    );

    ValueType arity1;
    arity1 = t(
      "BackgroundRepeatValue",

      k("repeat-x"),
      k("repeat-y"),
      arity2
    );

    property(
      "background-repeat",

      sig(globalKeyword, "value"),
      sig(arity1, "value"),
      sig(arity2, "value1", arity2, "value2")
    );
  }

  private void border() {
    ValueType value;
    value = t(
      "BorderShorthandValue",

      t("LineWidth"),
      t("LineStyle"),
      color
    );

    List<String> names;
    names = List.of("border", "border-top", "border-right", "border-bottom", "border-left");

    for (var name : names) {
      property(
        name,

        sig(globalKeyword, "value"),
        sig(value, "value1"),
        sig(value, "value1", value, "value2"),
        sig(value, "value1", value, "value2", value, "value3")
      );
    }
  }

  private void borderCollapse() {
    var borderCollapseValue = t(
      "BorderCollapseValue",

      k("collapse"),
      k("separate")
    );

    property(
      "border-collapse",

      sig(globalKeyword, "value"),
      sig(borderCollapseValue, "value")
    );
  }

  private void borderColor() {
    property(
      "border-color",

      sig(globalKeyword, "value"),
      sig(color, "all"),
      sig(color, "vertical", color, "horizontal"),
      sig(color, "top", color, "horizontal", color, "bottom"),
      sig(color, "top", color, "right", color, "bottom", color, "left")
    );
  }

  private void borderRadius() {
    ValueType value;
    value = lengthPercentage;

    property(
      "border-radius",

      sig(globalKeyword, "value"),
      sig(
        value, "all"
      ),
      sig(
        value, "topLeftBottomRight",
        value, "topRightBottomLeft"
      ),
      sig(
        value, "topLeft",
        value, "topRightBottomLeft",
        value, "bottomRight"
      ),
      sig(
        value, "topLeft",
        value, "topRight",
        value, "bottomRight",
        value, "bottomLeft"
      )
    );

    var names = List.of(
      "border-top-left-radius",
      "border-top-right-radius",
      "border-bottom-right-radius",
      "border-bottom-left-radius"
    );

    for (var name : names) {
      property(
        name,

        sig(globalKeyword, "value"),
        sig(value, "value"),
        sig(value, "horizontal", value, "vertical")
      );
    }
  }

  private void borderSpacing() {
    property(
      "border-spacing",

      sig(globalKeyword, "value"),
      sig(length, "value"),
      sig(length, "horizontal", length, "vertical")
    );
  }

  private void borderStyle() {
    var lineStyle = t(
      "LineStyle",

      k("dashed"),
      k("dotted"),
      k("double"),
      k("groove"),
      k("hidden"),
      k("inset"),
      k("none"),
      k("outset"),
      k("ridge"),
      k("solid")
    );

    property(
      "border-style",

      sig(globalKeyword, "value"),
      sig(lineStyle, "all"),
      sig(lineStyle, "vertical", lineStyle, "horizontal"),
      sig(lineStyle, "top", lineStyle, "horizontal", lineStyle, "bottom"),
      sig(lineStyle, "top", lineStyle, "right", lineStyle, "bottom", lineStyle, "left")
    );
  }

  private void borderWidth() {
    var lineWidth = t(
      "LineWidth",

      length,

      k("medium"),
      k("thick"),
      k("thin")
    );

    var names = List.of(
      "border-top-width",
      "border-right-width",
      "border-bottom-width",
      "border-left-width"
    );

    for (var name : names) {
      property(
        name,

        sig(globalKeyword, "value"),
        sig(lineWidth, "value")
      );
    }

    property(
      "border-width",

      sig(globalKeyword, "value"),
      sig(lineWidth, "all"),
      sig(lineWidth, "vertical", lineWidth, "horizontal"),
      sig(lineWidth, "top", lineWidth, "horizontal", lineWidth, "bottom"),
      sig(lineWidth, "top", lineWidth, "right", lineWidth, "bottom", lineWidth, "left")
    );
  }

  private void bottom() {
    var bottomValue = t(
      "BottomValue",
      k("auto"),
      lengthPercentage
    );

    property(
      "bottom",

      sig(globalKeyword, "value"),
      sig(bottomValue, "value")
    );
  }

  private void boxShadow() {
    KeywordName inset;
    inset = k("inset");

    property(
      "box-shadow",

      sig(globalKeyword, "value"),

      sig(k("none"), "value"),

      sig(
        length, "offsetX",
        length, "offsetY",
        color, "color"
      ),

      sig(
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        color, "color"
      ),

      sig(
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        length, "spreadRadius",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        color, "color"
      ),

      sig(
        inset, "inset",
        length, "offsetX",
        length, "offsetY",
        length, "blurRadius",
        length, "spreadRadius",
        color, "color"
      )
    ).asHashProperty();
  }

  private void boxSizing() {
    ValueType boxSizingValue = t(
      "BoxSizingValue",

      k("border-box"),
      k("content-box")
    );

    property(
      "box-sizing",

      sig(globalKeyword, "value"),
      sig(boxSizingValue, "value")
    );
  }

  private void color() {
    property(
      "color",

      sig(globalKeyword, "value"),
      sig(color, "value")
    );
  }

  private void colorPalette() {
    color.add("SLATE_050", "#f8fafc");
    color.add("SLATE_100", "#f1f5f9");
    color.add("SLATE_200", "#e2e8f0");
    color.add("SLATE_300", "#cbd5e1");
    color.add("SLATE_400", "#94a3b8");
    color.add("SLATE_500", "#64748b");
    color.add("SLATE_600", "#475569");
    color.add("SLATE_700", "#334155");
    color.add("SLATE_800", "#1e293b");
    color.add("SLATE_900", "#0f172a");

    color.add("GRAY_050", "#f9fafb");
    color.add("GRAY_100", "#f3f4f6");
    color.add("GRAY_200", "#e5e7eb");
    color.add("GRAY_300", "#d1d5db");
    color.add("GRAY_400", "#9ca3af");
    color.add("GRAY_500", "#6b7280");
    color.add("GRAY_600", "#4b5563");
    color.add("GRAY_700", "#374151");
    color.add("GRAY_800", "#1f2937");
    color.add("GRAY_900", "#111827");

    color.add("ZINC_050", "#fafafa");
    color.add("ZINC_100", "#f4f4f5");
    color.add("ZINC_200", "#e4e4e7");
    color.add("ZINC_300", "#d4d4d8");
    color.add("ZINC_400", "#a1a1aa");
    color.add("ZINC_500", "#71717a");
    color.add("ZINC_600", "#52525b");
    color.add("ZINC_700", "#3f3f46");
    color.add("ZINC_800", "#27272a");
    color.add("ZINC_900", "#18181b");

    color.add("NEUTRAL_050", "#fafafa");
    color.add("NEUTRAL_100", "#f5f5f5");
    color.add("NEUTRAL_200", "#e5e5e5");
    color.add("NEUTRAL_300", "#d4d4d4");
    color.add("NEUTRAL_400", "#a3a3a3");
    color.add("NEUTRAL_500", "#737373");
    color.add("NEUTRAL_600", "#525252");
    color.add("NEUTRAL_700", "#404040");
    color.add("NEUTRAL_800", "#262626");
    color.add("NEUTRAL_900", "#171717");

    color.add("STONE_050", "#fafaf9");
    color.add("STONE_100", "#f5f5f4");
    color.add("STONE_200", "#e7e5e4");
    color.add("STONE_300", "#d6d3d1");
    color.add("STONE_400", "#a8a29e");
    color.add("STONE_500", "#78716c");
    color.add("STONE_600", "#57534e");
    color.add("STONE_700", "#44403c");
    color.add("STONE_800", "#292524");
    color.add("STONE_900", "#1c1917");

    color.add("RED_050", "#fef2f2");
    color.add("RED_100", "#fee2e2");
    color.add("RED_200", "#fecaca");
    color.add("RED_300", "#fca5a5");
    color.add("RED_400", "#f87171");
    color.add("RED_500", "#ef4444");
    color.add("RED_600", "#dc2626");
    color.add("RED_700", "#b91c1c");
    color.add("RED_800", "#991b1b");
    color.add("RED_900", "#7f1d1d");

    color.add("ORANGE_050", "#fff7ed");
    color.add("ORANGE_100", "#ffedd5");
    color.add("ORANGE_200", "#fed7aa");
    color.add("ORANGE_300", "#fdba74");
    color.add("ORANGE_400", "#fb923c");
    color.add("ORANGE_500", "#f97316");
    color.add("ORANGE_600", "#ea580c");
    color.add("ORANGE_700", "#c2410c");
    color.add("ORANGE_800", "#9a3412");
    color.add("ORANGE_900", "#7c2d12");

    color.add("AMBER_050", "#fffbeb");
    color.add("AMBER_100", "#fef3c7");
    color.add("AMBER_200", "#fde68a");
    color.add("AMBER_300", "#fcd34d");
    color.add("AMBER_400", "#fbbf24");
    color.add("AMBER_500", "#f59e0b");
    color.add("AMBER_600", "#d97706");
    color.add("AMBER_700", "#b45309");
    color.add("AMBER_800", "#92400e");
    color.add("AMBER_900", "#78350f");

    color.add("YELLOW_050", "#fefce8");
    color.add("YELLOW_100", "#fef9c3");
    color.add("YELLOW_200", "#fef08a");
    color.add("YELLOW_300", "#fde047");
    color.add("YELLOW_400", "#facc15");
    color.add("YELLOW_500", "#eab308");
    color.add("YELLOW_600", "#ca8a04");
    color.add("YELLOW_700", "#a16207");
    color.add("YELLOW_800", "#854d0e");
    color.add("YELLOW_900", "#713f12");

    color.add("LIME_050", "#f7fee7");
    color.add("LIME_100", "#ecfccb");
    color.add("LIME_200", "#d9f99d");
    color.add("LIME_300", "#bef264");
    color.add("LIME_400", "#a3e635");
    color.add("LIME_500", "#84cc16");
    color.add("LIME_600", "#65a30d");
    color.add("LIME_700", "#4d7c0f");
    color.add("LIME_800", "#3f6212");
    color.add("LIME_900", "#365314");

    color.add("GREEN_050", "#f0fdf4");
    color.add("GREEN_100", "#dcfce7");
    color.add("GREEN_200", "#bbf7d0");
    color.add("GREEN_300", "#86efac");
    color.add("GREEN_400", "#4ade80");
    color.add("GREEN_500", "#22c55e");
    color.add("GREEN_600", "#16a34a");
    color.add("GREEN_700", "#15803d");
    color.add("GREEN_800", "#166534");
    color.add("GREEN_900", "#14532d");

    color.add("EMERALD_050", "#ecfdf5");
    color.add("EMERALD_100", "#d1fae5");
    color.add("EMERALD_200", "#a7f3d0");
    color.add("EMERALD_300", "#6ee7b7");
    color.add("EMERALD_400", "#34d399");
    color.add("EMERALD_500", "#10b981");
    color.add("EMERALD_600", "#059669");
    color.add("EMERALD_700", "#047857");
    color.add("EMERALD_800", "#065f46");
    color.add("EMERALD_900", "#064e3b");

    color.add("TEAL_050", "#f0fdfa");
    color.add("TEAL_100", "#ccfbf1");
    color.add("TEAL_200", "#99f6e4");
    color.add("TEAL_300", "#5eead4");
    color.add("TEAL_400", "#2dd4bf");
    color.add("TEAL_500", "#14b8a6");
    color.add("TEAL_600", "#0d9488");
    color.add("TEAL_700", "#0f766e");
    color.add("TEAL_800", "#115e59");
    color.add("TEAL_900", "#134e4a");

    color.add("CYAN_050", "#ecfeff");
    color.add("CYAN_100", "#cffafe");
    color.add("CYAN_200", "#a5f3fc");
    color.add("CYAN_300", "#67e8f9");
    color.add("CYAN_400", "#22d3ee");
    color.add("CYAN_500", "#06b6d4");
    color.add("CYAN_600", "#0891b2");
    color.add("CYAN_700", "#0e7490");
    color.add("CYAN_800", "#155e75");
    color.add("CYAN_900", "#164e63");

    color.add("SKY_050", "#f0f9ff");
    color.add("SKY_100", "#e0f2fe");
    color.add("SKY_200", "#bae6fd");
    color.add("SKY_300", "#7dd3fc");
    color.add("SKY_400", "#38bdf8");
    color.add("SKY_500", "#0ea5e9");
    color.add("SKY_600", "#0284c7");
    color.add("SKY_700", "#0369a1");
    color.add("SKY_800", "#075985");
    color.add("SKY_900", "#0c4a6e");

    color.add("BLUE_050", "#eff6ff");
    color.add("BLUE_100", "#dbeafe");
    color.add("BLUE_200", "#bfdbfe");
    color.add("BLUE_300", "#93c5fd");
    color.add("BLUE_400", "#60a5fa");
    color.add("BLUE_500", "#3b82f6");
    color.add("BLUE_600", "#2563eb");
    color.add("BLUE_700", "#1d4ed8");
    color.add("BLUE_800", "#1e40af");
    color.add("BLUE_900", "#1e3a8a");

    color.add("INDIGO_050", "#eef2ff");
    color.add("INDIGO_100", "#e0e7ff");
    color.add("INDIGO_200", "#c7d2fe");
    color.add("INDIGO_300", "#a5b4fc");
    color.add("INDIGO_400", "#818cf8");
    color.add("INDIGO_500", "#6366f1");
    color.add("INDIGO_600", "#4f46e5");
    color.add("INDIGO_700", "#4338ca");
    color.add("INDIGO_800", "#3730a3");
    color.add("INDIGO_900", "#312e81");

    color.add("VIOLET_050", "#f5f3ff");
    color.add("VIOLET_100", "#ede9fe");
    color.add("VIOLET_200", "#ddd6fe");
    color.add("VIOLET_300", "#c4b5fd");
    color.add("VIOLET_400", "#a78bfa");
    color.add("VIOLET_500", "#8b5cf6");
    color.add("VIOLET_600", "#7c3aed");
    color.add("VIOLET_700", "#6d28d9");
    color.add("VIOLET_800", "#5b21b6");
    color.add("VIOLET_900", "#4c1d95");

    color.add("PURPLE_050", "#faf5ff");
    color.add("PURPLE_100", "#f3e8ff");
    color.add("PURPLE_200", "#e9d5ff");
    color.add("PURPLE_300", "#d8b4fe");
    color.add("PURPLE_400", "#c084fc");
    color.add("PURPLE_500", "#a855f7");
    color.add("PURPLE_600", "#9333ea");
    color.add("PURPLE_700", "#7e22ce");
    color.add("PURPLE_800", "#6b21a8");
    color.add("PURPLE_900", "#581c87");

    color.add("FUCHSIA_050", "#fdf4ff");
    color.add("FUCHSIA_100", "#fae8ff");
    color.add("FUCHSIA_200", "#f5d0fe");
    color.add("FUCHSIA_300", "#f0abfc");
    color.add("FUCHSIA_400", "#e879f9");
    color.add("FUCHSIA_500", "#d946ef");
    color.add("FUCHSIA_600", "#c026d3");
    color.add("FUCHSIA_700", "#a21caf");
    color.add("FUCHSIA_800", "#86198f");
    color.add("FUCHSIA_900", "#701a75");

    color.add("PINK_050", "#fdf2f8");
    color.add("PINK_100", "#fce7f3");
    color.add("PINK_200", "#fbcfe8");
    color.add("PINK_300", "#f9a8d4");
    color.add("PINK_400", "#f472b6");
    color.add("PINK_500", "#ec4899");
    color.add("PINK_600", "#db2777");
    color.add("PINK_700", "#be185d");
    color.add("PINK_800", "#9d174d");
    color.add("PINK_900", "#831843");

    color.add("ROSE_050", "#fff1f2");
    color.add("ROSE_100", "#ffe4e6");
    color.add("ROSE_200", "#fecdd3");
    color.add("ROSE_300", "#fda4af");
    color.add("ROSE_400", "#fb7185");
    color.add("ROSE_500", "#f43f5e");
    color.add("ROSE_600", "#e11d48");
    color.add("ROSE_700", "#be123c");
    color.add("ROSE_800", "#9f1239");
    color.add("ROSE_900", "#881337");
  }

  private void content() {
    var contentValue = t(
      "ContentValue",

      k("none"),
      k("normal")
    );

    property(
      "content",

      sig(globalKeyword, "value"),
      sig(contentValue, "value"),
      sig(STRING, "value")
    );
  }

  private void cursor() {
    var cursor = t(
      "CursorValue",

      k("alias"),
      k("all-scroll"),
      k("auto"),
      k("cell"),
      k("col-resize"),
      k("context-menu"),
      k("copy"),
      k("crosshair"),
      k("default"),
      k("e-resize"),
      k("ew-resize"),
      k("grab"),
      k("grabbing"),
      k("help"),
      k("move"),
      k("n-resize"),
      k("ne-resize"),
      k("nesw-resize"),
      k("no-drop"),
      k("none"),
      k("not-allowed"),
      k("ns-resize"),
      k("nw-resize"),
      k("nwse-resize"),
      k("pointer"),
      k("progress"),
      k("row-resize"),
      k("s-resize"),
      k("se-resize"),
      k("sw-resize"),
      k("text"),
      k("vertical-text"),
      k("w-resize"),
      k("wait"),
      k("zoom-in"),
      k("zoom-out")
    );

    property(
      "cursor",

      sig(globalKeyword, "value"),
      sig(cursor, "value")
    );
  }

  private void display() {
    var displayOutside = t(
      "DisplayOutsideValue",
      k("block"),
      k("inline"),
      k("runIn")
    );

    var displayInside = t(
      "DisplayInsideValue",
      k("flow"),
      k("flow-root"),
      k("table"),
      k("flex"),
      k("grid"),
      k("ruby")
    );

    var displayListItem = t(
      "DisplayListItemValue",
      k("list-item")
    );

    var displayInternal = t(
      "DisplayInternalValue",
      k("table-row-group"),
      k("table-header-group"),
      k("table-footer-group"),
      k("table-row"),
      k("table-cell"),
      k("table-column-group"),
      k("table-column"),
      k("table-caption"),
      k("ruby-base"),
      k("ruby-text"),
      k("ruby-base-container"),
      k("ruby-text-container")
    );

    var displayBox = t(
      "DisplayBoxValue",
      k("contents"),
      k("none")
    );

    var displayLegacy = t(
      "DisplayLegacyValue",
      k("inline-block"),
      k("inline-table"),
      k("inline-flex"),
      k("inline-grid")
    );

    var displayValue = t(
      "DisplayValue",
      displayOutside,
      displayInside,
      displayListItem,
      displayInternal,
      displayBox,
      displayLegacy
    );

    var displayValue2 = t(
      "DisplayValue2",
      displayOutside,
      displayInside
    );

    property(
      "display",
      sig(globalKeyword, "value"),
      sig(displayValue, "value"),
      sig(displayValue, "value", displayValue2, "value2")
    );
  }

  private void fill() {
    property(
      "fill",

      sig(color, "color")
    );
  }

  private void filter() {
    var filterValue = t(
      "FilterValue",

      k("none"),
      url()
    );

    List<String> names;
    names = List.of("filter", "-webkit-filter");

    for (var name : names) {
      property(
        name,

        sig(globalKeyword, "value"),
        sig(filterValue, "value")
      );
    }
  }

  private void flexDirection() {
    var flexDirectionValue = t(
      "FlexDirectionValue",

      k("row"),
      k("row-reverse"),
      k("column"),
      k("column-reverse")
    );

    property(
      "flex-direction",

      sig(globalKeyword, "value"),
      sig(flexDirectionValue, "value")
    );
  }

  private void flexGrow() {
    property(
      "flex-grow",

      sig(globalKeyword, "value"),
      sig(DOUBLE, "value"),
      sig(INT, "value"),
      sig(numberValue, "value")
    );
  }

  private void flexShrink() {
    property(
      "flex-shrink",

      sig(globalKeyword, "value"),
      sig(DOUBLE, "value"),
      sig(INT, "value"),
      sig(numberValue, "value")
    );
  }

  private void flexWrap() {
    ValueType flexWrapValue;
    flexWrapValue = t(
      "FlexWrapValue",

      k("nowrap"),
      k("wrap"),
      k("wrapReverse")
    );

    property(
      "flex-wrap",

      sig(globalKeyword, "value"),
      sig(flexWrapValue, "value")
    );
  }

  private void font() {
    var fontValue = t(
      "FontValue",

      k("caption"),
      k("icon"),
      k("menu"),
      k("message-box"),
      k("small-caption"),
      k("status-bar")
    );

    property(
      "font",
      sig(globalKeyword, "value"),
      sig(fontValue, "value")
    );
  }

  private void fontFamily() {
    var fontFamilyValue = t(
      "FontFamilyValue",

      k("cursive"),
      k("emoji"),
      k("fangsong"),
      k("fantasy"),
      k("math"),
      k("monospace"),
      k("sans-serif"),
      k("serif"),
      k("system-ui"),
      k("ui-monospace"),
      k("ui-rounded"),
      k("ui-sans-serif"),
      k("ui-serif"),

      string()
    );

    property(
      "font-family",

      sig(globalKeyword, "value"),
      sigVar(fontFamilyValue, "values")
    );
  }

  private void fontFeatureSettings() {
    var value = t(
      "FontFeatureSettingsValue",

      k("normal")
    );

    property(
      "font-feature-settings",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void fontSize() {
    var fontSizeValue = t(
      "FontSizeValue",

      k("large"),
      k("larger"),
      k("medium"),
      k("small"),
      k("smaller"),
      k("x-large"),
      k("xx-large"),
      k("xxx-large"),
      k("xx-small"),
      k("x-small"),

      lengthPercentage
    );

    property(
      "font-size",

      sig(globalKeyword, "value"),
      sig(fontSizeValue, "value")
    );
  }

  private void fontStyle() {
    var value = t(
      "FontStyleValue",

      k("italic"),
      k("normal"),
      k("oblique")
    );

    property(
      "font-style",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void fontVariationSettings() {
    var value = t(
      "FontVariationSettingsValue",

      k("normal")
    );

    property(
      "font-variation-settings",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void fontWeight() {
    var fontWeightValue = t(
      "FontWeightValue",

      k("bold"),
      k("bolder"),
      k("lighter"),
      k("normal"),

      intType
    );

    property(
      "font-weight",

      sig(INT, "value"),
      sig(globalKeyword, "value"),
      sig(fontWeightValue, "value")
    );
  }

  private void height() {
    var heightOrWidthValue = t(
      "HeightOrWidthValue",

      k("auto"),
      k("fit-content"),
      k("max-content"),
      k("min-content"),

      lengthPercentage
    );

    property(
      "height",

      sig(globalKeyword, "value"),
      sig(heightOrWidthValue, "value")
    );
  }

  private void justifyContent() {
    var justifyContent = t(
      "JustifyContentValue",

      k("normal"),

      k("space-between"),
      k("space-around"),
      k("space-evenly"),
      k("stretch"),

      k("center"),
      k("start"),
      k("end"),
      k("flex-start"),
      k("flex-end"),

      k("left"),
      k("right")
    );

    var overflow = t(
      "OverflowPosition",

      k("safe"),
      k("unsafe")
    );

    var justifyContentPosition = t(
      "JustifyContentPosition",

      k("center"),
      k("start"),
      k("end"),
      k("flex-start"),
      k("flex-end"),

      k("left"),
      k("right")
    );

    property(
      "justify-content",

      sig(globalKeyword, "value"),
      sig(justifyContent, "value"),
      sig(overflow, "safeOrUnsafe", justifyContentPosition, "position")
    );
  }

  private void left() {
    var leftValue = t(
      "LeftValue",

      k("auto"),
      lengthPercentage
    );

    property(
      "left",

      sig(globalKeyword, "value"),
      sig(leftValue, "value")
    );
  }

  private void letterSpacing() {
    var letterSpacingValue = t(
      "LetterSpacingValue",

      k("normal"),

      lengthPercentage
    );

    property(
      "letter-spacing",

      sig(globalKeyword, "value"),
      sig(letterSpacingValue, "value")
    );
  }

  private void lineHeight() {
    var lineHeightValue = t(
      "LineHeightValue",

      k("normal"),

      lengthPercentage,
      doubleType,
      intType
    );

    property(
      "line-height",

      sig(DOUBLE, "value"),
      sig(INT, "value"),
      sig(globalKeyword, "value"),
      sig(lineHeightValue, "value")
    );
  }

  private void listStyle() {
    var shorthand = t(
      "ListStyleValue",
      t("ListStyleTypeValue"),
      t("ListStylePositionValue"),
      t("ListStyleImageValue")
    );

    property(
      "list-style",

      sig(globalKeyword, "value"),
      sig(shorthand, "value"),
      sig(shorthand, "value1", shorthand, "value2"),
      sig(shorthand, "value1", shorthand, "value2", shorthand, "value3")
    );
  }

  private void listStyleImage() {
    var listStyleImageValue = t(
      "ListStyleImageValue",

      k("none"),

      image
    );

    property(
      "list-style-image",

      sig(globalKeyword, "value"),
      sig(listStyleImageValue, "value")
    );
  }

  private void listStylePosition() {
    var listStylePositionValue = t(
      "ListStylePositionValue",

      k("inside"),
      k("outside")
    );

    property(
      "list-style-position",

      sig(globalKeyword, "value"),
      sig(listStylePositionValue, "value")
    );
  }

  private void listStyleType() {
    var counterStyleValue = t(
      "CounterStyleValue",

      k("disc"),
      k("circle"),
      k("square"),
      k("decimal"),
      k("cjk-decimal"),
      k("decimal-leading-zero"),
      k("lower-roman"),
      k("upper-roman"),
      k("lower-greek"),
      k("lower-alpha"),
      k("lower-latin"),
      k("upper-alpha"),
      k("upper-latin"),
      k("arabic-indic"),
      k("-moz-arabic-indic"),
      k("armenian"),
      k("bengali"),
      k("-moz-bengali"),
      k("cambodian"),
      k("cjk-earthly-branch"),
      k("-moz-cjk-earthly-branch"),
      k("cjk-heavenly-stem"),
      k("-moz-cjk-heavenly-stem"),
      k("cjk-ideographic"),
      k("devanagari"),
      k("-moz-devanagari"),
      k("ethiopic-numeric"),
      k("georgian"),
      k("gujarati"),
      k("-moz-gujarati"),
      k("gurmukhi"),
      k("-moz-gurmukhi"),
      k("hebrew"),
      k("hiragana"),
      k("hiragana-iroha"),
      k("japanese-formal"),
      k("japanese-informal"),
      k("kannada"),
      k("-moz-kannada"),
      k("katakana"),
      k("katakana-iroha"),
      k("khmer"),
      k("-moz-khmer"),
      k("korean-hangul-formal"),
      k("korean-hanja-formal"),
      k("korean-hanja-informal"),
      k("lao"),
      k("-moz-lao"),
      k("lower-armenian"),
      k("malayalam"),
      k("-moz-malayalam"),
      k("mongolian"),
      k("myanmar"),
      k("-moz-myanmar"),
      k("oriya"),
      k("-moz-oriya"),
      k("persian"),
      k("-moz-persian"),
      k("simp-chinese-formal"),
      k("simp-chinese-informal"),
      k("tamil"),
      k("-moz-tamil"),
      k("telugu"),
      k("-moz-telugu"),
      k("thai"),
      k("-moz-thai"),
      k("tibetan"),
      k("trad-chinese-formal"),
      k("trad-chinese-informal"),
      k("upper-armenian"),
      k("disclosure-open"),
      k("disclosure-closed")
    );

    var listStyleTypeValue = t(
      "ListStyleTypeValue",

      counterStyleValue,
      k("none"),
      string()
    );

    property(
      "list-style-type",

      sig(globalKeyword, "value"),
      sig(listStyleTypeValue, "value"),
      sig(STRING, "value")
    );
  }

  private void margin() {
    ValueType marginValue;
    marginValue = t(
      "MarginValue",

      k("auto"),
      lengthPercentage
    );

    property(
      "margin",

      sig(globalKeyword, "value"),
      sig(marginValue, "all"),
      sig(marginValue, "vertical", marginValue, "horizontal"),
      sig(marginValue, "top", marginValue, "horizontal", marginValue, "bottom"),
      sig(marginValue, "top", marginValue, "right", marginValue, "bottom", marginValue, "left")
    );

    List<String> names;
    names = List.of("margin-top", "margin-right", "margin-bottom", "margin-left");

    for (var name : names) {
      property(
        name,
        sig(globalKeyword, "value"),
        sig(marginValue, "value")
      );
    }
  }

  private void maxHeight() {
    var value = t(
      "MaxHeightOrWidthValue",

      k("none"),
      k("fit-content"),
      k("max-content"),
      k("min-content"),

      lengthPercentage
    );

    property(
      "max-height",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void maxWidth() {
    var value = t("MaxHeightOrWidthValue");

    property(
      "max-width",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void minHeight() {
    ValueType value;
    value = t(
      "MinHeightOrWidthValue",

      k("auto"),
      k("fit-content"),
      k("max-content"),
      k("min-content")
    );

    property(
      "min-height",

      sig(globalKeyword, "value"),
      sig(value, "value"),
      sig(lengthPercentage, "value")
    );
  }

  private void minWidth() {
    ValueType value;
    value = t("MinHeightOrWidthValue");

    // we leave out the length type as
    // min-width(length) is also a media feature

    property(
      "min-width",

      sig(globalKeyword, "value"),
      sig(value, "value"),
      sig(percentage, "value")
    );
  }

  private void opacity() {
    property(
      "opacity",

      sig(globalKeyword, "value"),
      sig(percentage(), "value"),
      sig(DOUBLE, "value"),
      sig(INT, "value"),
      sig(numberValue, "value")
    ).asFilterFunction();
  }

  private void outline() {
    var outlineValue = t(
      "OutlineValue",

      color,

      t("LineWidth"),
      t("OutlineStyleValue")
    );

    property(
      "outline",

      sig(globalKeyword, "value"),
      sig(outlineValue, "value"),
      sig(outlineValue, "value1", outlineValue, "value2"),
      sig(outlineValue, "value1", outlineValue, "value2", outlineValue, "value3")
    );
  }

  private void outlineColor() {
    property(
      "outline-color",

      sig(globalKeyword, "value"),
      sig(color, "value")
    );
  }

  private void outlineOffset() {
    property(
      "outline-offset",

      sig(globalKeyword, "value"),
      sig(length, "value")
    );
  }

  private void outlineStyle() {
    var outlineStyleValue = t(
      "OutlineStyleValue",

      k("auto"),
      k("dashed"),
      k("dotted"),
      k("double"),
      k("inset"),
      k("groove"),
      k("none"),
      k("outset"),
      k("ridge"),
      k("solid")
    );

    property(
      "outline-style",

      sig(globalKeyword, "value"),
      sig(outlineStyleValue, "value")
    );
  }

  private void outlineWidth() {
    var lineWidth = t("LineWidth");

    property(
      "outline-width",

      sig(globalKeyword, "value"),
      sig(lineWidth, "value")
    );
  }

  private void padding() {
    ValueType padValue;
    padValue = lengthPercentage;

    property(
      "padding",

      sig(globalKeyword, "value"),
      sig(padValue, "all"),
      sig(padValue, "vertical", padValue, "horizontal"),
      sig(padValue, "top", padValue, "horizontal", padValue, "bottom"),
      sig(padValue, "top", padValue, "right", padValue, "bottom", padValue, "left")
    );

    List<String> names;
    names = List.of("padding-top", "padding-right", "padding-bottom", "padding-left");

    for (var name : names) {
      property(
        name,
        sig(globalKeyword, "value"),
        sig(padValue, "value")
      );
    }
  }

  private void paddingBlock() {
    property(
      "padding-block",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "both"),
      sig(lengthPercentage, "start", lengthPercentage, "end")
    );

    property(
      "padding-block-end",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "value")
    );

    property(
      "padding-block-start",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "value")
    );
  }

  private void paddingInline() {
    property(
      "padding-inline",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "both"),
      sig(lengthPercentage, "start", lengthPercentage, "end")
    );

    property(
      "padding-inline-end",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "value")
    );

    property(
      "padding-inline-start",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "value")
    );
  }

  private void pointerEvents() {
    var pointerEventsValue = t(
      "PointerEventsValue",

      k("auto"),
      k("none")
    );

    property(
      "pointer-events",

      sig(globalKeyword, "value"),
      sig(pointerEventsValue, "value")
    );
  }

  private void position() {
    var positionValue = t(
      "PositionValue",

      k("absolute"),
      k("fixed"),
      k("static"),
      k("sticky"),
      k("relative")
    );

    property(
      "position",

      sig(globalKeyword, "value"),
      sig(positionValue, "value")
    );
  }

  private void quotes() {
    ValueType value = t(
      "QuotesValue",

      k("none"),
      k("auto")
    );

    property(
      "quotes",

      sig(globalKeyword, "value"),
      sig(value, "value"),
      sig(string(), "open", string(), "close")
    );
  }

  private void resize() {
    var resizeValue = t(
      "ResizeValue",

      k("block"),
      k("both"),
      k("horizontal"),
      k("inline"),
      k("none"),
      k("vertical")
    );

    property(
      "resize",

      sig(globalKeyword, "value"),
      sig(resizeValue, "value")
    );
  }

  private void tabSize() {
    List<String> names;
    names = List.of("-moz-tab-size", "tab-size");

    for (var name : names) {
      property(
        name,

        sig(INT, "value"),
        sig(intType, "value"),
        sig(globalKeyword, "value"),
        sig(length, "value")
      );
    }
  }

  private void textAlign() {
    ValueType textAlignValue;
    textAlignValue = t(
      "TextAlignValue",

      k("center"),
      k("end"),
      k("justify"),
      k("left"),
      k("match-parent"),
      k("right"),
      k("start")
    );

    property(
      "text-align",

      sig(globalKeyword, "value"),
      sig(textAlignValue, "value")
    );
  }

  private void textDecoration() {
    var value = t("TextDecorationValue",
      color,

      t("TextDecorationLineSingleValue"),
      t("TextDecorationStyleValue"),
      t("TextDecorationThicknessValue")
    );

    property(
      "text-decoration",

      sig(globalKeyword, "value"),
      sig(value, "value"),
      sig(value, "value1", value, "value2"),
      sig(value, "value1", value, "value2", value, "value3"),
      sig(value, "value1", value, "value2", value, "value3", value, "value4")
    );
  }

  private void textDecorationColor() {
    property(
      "text-decoration-color",

      sig(globalKeyword, "value"),
      sig(color, "value")
    );
  }

  private void textDecorationLine() {
    var multiValue = t(
      "TextDecorationLineMultiValue",

      k("blink"),
      k("line-through"),
      k("overline"),
      k("underline")
    );

    var singleValue = t(
      "TextDecorationLineSingleValue",

      multiValue,
      k("none")
    );

    property(
      "text-decoration-line",

      sig(globalKeyword, "value"),
      sig(singleValue, "value"),
      sig(multiValue, "value1", multiValue, "value2"),
      sig(multiValue, "value1", multiValue, "value2", multiValue, "value3")
    );
  }

  private void textDecorationStyle() {
    var textDecorationStyleValue = t(
      "TextDecorationStyleValue",

      k("dashed"),
      k("double"),
      k("dotted"),
      k("solid"),
      k("wavy")
    );

    property(
      "text-decoration-style",

      sig(globalKeyword, "value"),
      sig(textDecorationStyleValue, "value")
    );
  }

  private void textDecorationThickness() {
    var textDecorationThicknessValue = t(
      "TextDecorationThicknessValue",

      k("auto"),
      k("from-font"),
      lengthPercentage
    );

    property(
      "text-decoration-thickness",

      sig(globalKeyword, "value"),
      sig(textDecorationThicknessValue, "value")
    );
  }

  private void textIndent() {
    var textIndentValue = t(
      "TextIndentValue",

      k("each-line"),
      k("hanging")
    );

    property(
      "text-indent",

      sig(globalKeyword, "value"),
      sig(lengthPercentage, "value"),
      sig(lengthPercentage, "value1", textIndentValue, "value2"),
      sig(lengthPercentage, "value1", textIndentValue, "value2", textIndentValue, "value3")
    );
  }

  private void textSizeAdjust() {
    var textSizeAdjustValue = t(
      "TextSizeAdjustValue",

      k("auto"),
      k("none"),
      percentage
    );

    property(
      "-webkit-text-size-adjust",

      sig(globalKeyword, "value"),
      sig(textSizeAdjustValue, "value")
    );
  }

  private void textTransform() {
    var textTransformValue = t(
      "TextTransformValue",

      k("capitalize"),
      k("full-width"),
      k("full-size-kana"),
      k("lowercase"),
      k("none"),
      k("uppercase")
    );

    property(
      "text-transform",

      sig(globalKeyword, "value"),
      sig(textTransformValue, "value")
    );
  }

  private void top() {
    var topValue = t(
      "TopValue",

      k("auto"),
      lengthPercentage
    );

    property(
      "top",

      sig(globalKeyword, "value"),
      sig(topValue, "value")
    );
  }

  private void verticalAlign() {
    var verticalAlignValue = t(
      "VerticalAlignValue",

      k("baseline"),
      k("bottom"),
      k("middle"),
      k("sub"),
      k("super"),
      k("text-bottom"),
      k("text-top"),
      k("top"),

      lengthPercentage
    );

    property(
      "vertical-align",

      sig(globalKeyword, "value"),
      sig(verticalAlignValue, "value")
    );
  }

  private void width() {
    var value = t("HeightOrWidthValue");

    property(
      "width",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void wordBreak() {
    var wordBreakValue = t(
      "WordBreakValue",

      k("normal"),
      k("keep-all"),
      k("break-all"),
      k("break-word")
    );

    property(
      "word-break",

      sig(globalKeyword, "value"),
      sig(wordBreakValue, "value")
    );
  }

}