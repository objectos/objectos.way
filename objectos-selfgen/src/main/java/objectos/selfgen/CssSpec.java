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
import objectos.selfgen.css2.LengthType;
import objectos.selfgen.css2.SelectorKind;
import objectos.selfgen.css2.ValueType;

public final class CssSpec extends CssSelfGen {

  private ColorValue color;

  private ValueType globalKeyword;

  private ValueType image;

  private LengthType length;

  private ValueType lengthPercentage;

  public static void main(String[] args) throws IOException {
    var spec = new CssSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    selectors(
      SelectorKind.TYPE,

      "a",
      "audio",

      "b",
      "body",
      "blockquote",
      "button",

      "canvas",
      "code",

      "dd",
      "dl",

      "embed",

      "fieldset",
      "figure",
      "form",

      "h1",
      "h2",
      "h3",
      "h4",
      "h5",
      "h6",
      "hr",
      "html",

      "iframe",
      "img",
      "input",

      "kbd",

      "label",
      "legend",
      "li",

      "menu",

      "object",
      "ol",
      "optgroup",

      "p",
      "pre",
      "progress",

      "samp",
      "select",
      "small",
      "strong",
      "sub",
      "summary",
      "sup",
      "svg",

      "table",
      "textarea",

      "ul",

      "video"
    );

    selectors(
      SelectorKind.PSEUDO_CLASS,

      ":disabled",
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

    // global keywords

    globalKeyword = t(
      "GlobalKeyword",

      k("inherit"),
      k("initial"),
      k("unset")
    );

    // color

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

    // image

    image = t(
      "Image",

      url()
    );

    // length/percentage

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

    var percentage = percentage();

    lengthPercentage = t("LengthPercentage", length, percentage);

    // keyword name clashes

    keywordFieldName("default", "_default");
    keywordFieldName("double", "_double");
    keywordFieldName("static", "_static");
    keywordFieldName("super", "_super");

    // A

    appearance();

    // B

    var borderCollapseVale = t("BorderCollapseValue",
      keywords("collapse", "separate")
    );

    var bottomValue = t("BottomValue",
      k("auto"),
      lengthPercentage
    );

    var lineStyle = t("LineStyle",
      keywords(
        "dashed", "dotted", "double",
        "groove", "hidden",
        "inset",
        "none",
        "outset",
        "ridge", "solid"
      )
    );

    backgroundColor();

    backgroundImage();

    borderWidth();

    pval("border-collapse", globalKeyword);
    pval("border-collapse", borderCollapseVale);

    pval("border-color", globalKeyword);
    pbox("border-color", color);

    pval("bottom", globalKeyword);
    pval("bottom", bottomValue);

    pval("box-shadow", globalKeyword);
    pval("box-shadow", k("none"));

    pval("box-sizing", globalKeyword);
    pval("box-sizing", t("BoxSizingValue",
      keywords("border-box", "content-box")
    ));

    pval("border-style", globalKeyword);
    pbox("border-style", lineStyle);

    // C

    color();
    cursor();

    // D

    display();

    // F

    flexDirection();
    font();
    fontFamily();
    fontFeatureSettings();
    fontSize();
    fontVariationSettings();
    fontWeight();

    // H

    height();

    // J

    justifyContent();

    // L

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

    var positionValue = t("PositionValue",
      keywords("absolute", "fixed", "static", "sticky", "relative")
    );

    pval("padding", globalKeyword);
    pbox("padding", lengthPercentage);

    pval("padding-bottom", globalKeyword);
    pval("padding-bottom", lengthPercentage);

    pval("padding-left", globalKeyword);
    pval("padding-left", lengthPercentage);

    pval("padding-right", globalKeyword);
    pval("padding-right", lengthPercentage);

    pval("padding-top", globalKeyword);
    pval("padding-top", lengthPercentage);

    pval("position", globalKeyword);
    pval("position", positionValue);

    // R

    resize();

    // T

    var textSizeAdjustValue = t("TextSizeAdjustValue",
      keywords("auto", "none"),
      percentage
    );

    var textDecorationLineMultiValue = t("TextDecorationLineMultiValue",
      keywords("blink", "line-through", "overline", "underline")
    );

    var textDecorationLineSingleValue = t("TextDecorationLineSingleValue",
      textDecorationLineMultiValue, k("none")
    );

    var textDecorationStyleValue = t("TextDecorationStyleValue",
      keywords("dashed", "double", "dotted", "solid", "wavy")
    );

    var textDecorationThicknessValue = t("TextDecorationThicknessValue",
      keywords("auto", "from-font"),
      lengthPercentage
    );

    var textDecorationValue = t("TextDecorationValue",
      color, textDecorationLineSingleValue, textDecorationStyleValue, textDecorationThicknessValue
    );

    var textIndentValue = t("TextIndentValue",
      keywords("each-line", "hanging")
    );

    var textTransformValue = t("TextTransformValue",
      keywords("capitalize", "full-width", "full-size-kana", "lowercase", "none", "uppercase")
    );

    var topValue = t("TopValue",
      k("auto"),
      lengthPercentage
    );

    pint("-moz-tab-size");
    pval("-moz-tab-size", globalKeyword);
    pval("-moz-tab-size", length);

    pint("tab-size");
    pval("tab-size", globalKeyword);
    pval("tab-size", length);

    pval("text-decoration", globalKeyword);
    pval("text-decoration", textDecorationValue);
    pva2("text-decoration", textDecorationValue);
    pva3("text-decoration", textDecorationValue);
    pva4("text-decoration", textDecorationValue);

    pval("text-decoration-color", globalKeyword);
    pval("text-decoration-color", color);

    pval("text-decoration-line", globalKeyword);
    pval("text-decoration-line", textDecorationLineSingleValue);
    pva2("text-decoration-line", textDecorationLineMultiValue);
    pva3("text-decoration-line", textDecorationLineMultiValue);

    pval("text-decoration-style", globalKeyword);
    pval("text-decoration-style", textDecorationStyleValue);

    pval("text-decoration-thickness", globalKeyword);
    pval("text-decoration-thickness", textDecorationThicknessValue);

    pval("text-indent", globalKeyword);
    pval("text-indent", lengthPercentage);
    pval("text-indent", lengthPercentage, textIndentValue);
    pval("text-indent", lengthPercentage, textIndentValue, textIndentValue);

    pval("-webkit-text-size-adjust", globalKeyword);
    pval("-webkit-text-size-adjust", textSizeAdjustValue);

    pval("text-transform", globalKeyword);
    pval("text-transform", textTransformValue);

    pval("top", globalKeyword);
    pval("top", topValue);

    // V

    verticalAlign();

    // W

    width();
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

  private void color() {
    property(
      "color",

      sig(globalKeyword, "value"),
      sig(color, "value")
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

      keywords(
        "bold", "bolder",
        "lighter",
        "normal"
      )
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

  private void lineHeight() {
    var lineHeightValue = t(
      "LineHeightValue",

      k("normal"),

      lengthPercentage
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
    var marginValue = t(
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
    var value = t(
      "MinHeightOrWidthValue",

      k("auto"),
      k("fit-content"),
      k("max-content"),
      k("min-content"),

      lengthPercentage
    );

    property(
      "min-height",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void minWidth() {
    var value = t("MinHeightOrWidthValue");

    property(
      "min-width",

      sig(globalKeyword, "value"),
      sig(value, "value")
    );
  }

  private void opacity() {
    property(
      "opacity",

      sig(globalKeyword, "value"),
      sig(percentage(), "value"),
      sig(DOUBLE, "value"),
      sig(INT, "value")
    );
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

}