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
import objectos.selfgen.css2.CssSelfGen;
import objectos.selfgen.css2.ValueType;

public final class CssSpec extends CssSelfGen {

  private ValueType color;

  private ValueType globalKeyword;

  private ValueType image;

  private ValueType lengthPercentage;

  public static void main(String[] args) throws IOException {
    var spec = new CssSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    selectors(
      // type selectors
      "a",

      "b",
      "body",
      "blockquote",
      "button",

      "code",

      "dd",
      "dl",

      "h1",
      "h2",
      "h3",
      "h4",
      "h5",
      "h6",
      "hr",
      "html",

      "fieldset",
      "figure",

      "input",

      "kbd",

      "legend",
      "li",

      "menu",

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

      "table",
      "textarea",

      "ul",

      // pseudo classes
      ":-moz-focusring",
      ":-moz-ui-invalid",

      // pseudo elements
      "::after",
      "::before",
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
    color = t(
      "Color",

      k("currentcolor"),
      k("transparent"),

      keywords(
        "aqua",
        "black", "blue",
        "fuchsia",
        "gray", "green",
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
      )
    );

    // image

    image = t(
      "Image",

      url()
    );

    // length/percentage

    var length = length(
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

    var lineWidth = t("LineWidth",
      length,
      keywords("medium", "thick", "thin")
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

    pval("border-bottom-width", globalKeyword);
    pval("border-bottom-width", lineWidth);

    pval("border-collapse", globalKeyword);
    pval("border-collapse", borderCollapseVale);

    pval("border-color", globalKeyword);
    pbox("border-color", color);

    pval("border-left-width", globalKeyword);
    pval("border-left-width", lineWidth);

    pval("border-right-width", globalKeyword);
    pval("border-right-width", lineWidth);

    pval("border-top-width", globalKeyword);
    pval("border-top-width", lineWidth);

    pval("border-width", globalKeyword);
    pbox("border-width", lineWidth);

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

    // D

    display();

    // F

    var fontValue = t("FontValue",
      keywords(
        "caption",
        "icon",
        "menu", "message-box",
        "small-caption", "status-bar"
      )
    );

    var fontFamilyValue = t("FontFamilyValue",
      keywords(
        "cursive",
        "emoji",
        "fangsong", "fantasy",
        "math", "monospace",
        "sans-serif", "serif", "system-ui",
        "ui-monospace",
        "ui-rounded",
        "ui-sans-serif",
        "ui-serif"
      ),
      string()
    );

    var fontSizeValue = t("FontSizeValue",
      keywords(
        "large", "larger",
        "medium",
        "small", "smaller",
        "x-large", "xx-large", "xxx-large", "xx-small", "x-small"
      ),
      lengthPercentage
    );

    var fontWeightValue = t("FontWeightValue",
      keywords(
        "bold", "bolder",
        "lighter",
        "normal"
      )
    );

    pval("font", globalKeyword);
    pval("font", fontValue);

    pval("font-family", globalKeyword);
    pvar("font-family", fontFamilyValue);

    pval("font-feature-settings", globalKeyword);
    pval("font-feature-settings", t("FontFeatureSettingsValue", k("normal")));

    pval("font-size", globalKeyword);
    pval("font-size", fontSizeValue);

    pval("font-variation-settings", globalKeyword);
    pval("font-variation-settings", t("FontVariationSettingsValue", k("normal")));

    pint("font-weight");
    pval("font-weight", globalKeyword);
    pval("font-weight", fontWeightValue);

    // H

    var heightValue = t("HeightValue",
      lengthPercentage,
      keywords("auto", "fit-content", "max-content", "min-content")
    );

    pval("height", globalKeyword);
    pval("height", heightValue);

    // L

    lineHeight();
    listStyleImage();
    listStylePosition();
    listStyleType();
    listStyle();

    // M

    var marginValue = t("MarginValue",
      lengthPercentage,
      k("auto")
    );

    pval("margin", globalKeyword);
    pbox("margin", marginValue);

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

    // O

    var outlineStyleValue = t("OutlineStyleValue",
      keywords(
        "auto",
        "dashed", "dotted", "double",
        "inset",
        "groove",
        "none",
        "outset",
        "ridge",
        "solid"
      )
    );

    var outlineValue = t("OutlineValue",
      color,
      lineWidth,
      outlineStyleValue
    );

    pval("outline", globalKeyword);
    pval("outline", outlineValue);
    pva2("outline", outlineValue);
    pva3("outline", outlineValue);

    pval("outline-color", globalKeyword);
    pval("outline-color", color);

    pval("outline-offset", globalKeyword);
    pval("outline-offset", length);

    pval("outline-style", globalKeyword);
    pval("outline-style", outlineStyleValue);

    pval("outline-width", globalKeyword);
    pval("outline-width", lineWidth);

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

  private void color() {
    property(
      "color",

      sig(globalKeyword, "value"),
      sig(color, "value")
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

}