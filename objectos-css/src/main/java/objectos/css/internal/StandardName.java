/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.css.om.Selector;
import objectos.css.tmpl.AppearanceValue;
import objectos.css.tmpl.AutoKeyword;
import objectos.css.tmpl.BackgroundImageValue;
import objectos.css.tmpl.BlockKeyword;
import objectos.css.tmpl.BorderCollapseValue;
import objectos.css.tmpl.BottomValue;
import objectos.css.tmpl.BoxSizingValue;
import objectos.css.tmpl.ButtonKeyword;
import objectos.css.tmpl.CenterKeyword;
import objectos.css.tmpl.ColorValue;
import objectos.css.tmpl.CounterStyleValue;
import objectos.css.tmpl.CursorValue;
import objectos.css.tmpl.DashedKeyword;
import objectos.css.tmpl.DisplayBoxValue;
import objectos.css.tmpl.DisplayInsideValue;
import objectos.css.tmpl.DisplayInternalValue;
import objectos.css.tmpl.DisplayLegacyValue;
import objectos.css.tmpl.DisplayListItemValue;
import objectos.css.tmpl.DisplayOutsideValue;
import objectos.css.tmpl.DottedKeyword;
import objectos.css.tmpl.DoubleKeyword;
import objectos.css.tmpl.EndKeyword;
import objectos.css.tmpl.FitContentKeyword;
import objectos.css.tmpl.FlexDirectionValue;
import objectos.css.tmpl.FlexEndKeyword;
import objectos.css.tmpl.FlexStartKeyword;
import objectos.css.tmpl.FontFamilyValue;
import objectos.css.tmpl.FontFeatureSettingsValue;
import objectos.css.tmpl.FontSizeValue;
import objectos.css.tmpl.FontValue;
import objectos.css.tmpl.FontVariationSettingsValue;
import objectos.css.tmpl.FontWeightValue;
import objectos.css.tmpl.GlobalKeyword;
import objectos.css.tmpl.GrooveKeyword;
import objectos.css.tmpl.HeightOrWidthValue;
import objectos.css.tmpl.InlineKeyword;
import objectos.css.tmpl.InsetKeyword;
import objectos.css.tmpl.JustifyContentPosition;
import objectos.css.tmpl.JustifyContentValue;
import objectos.css.tmpl.LeftKeyword;
import objectos.css.tmpl.LineHeightValue;
import objectos.css.tmpl.LineStyle;
import objectos.css.tmpl.LineWidth;
import objectos.css.tmpl.ListStyleImageValue;
import objectos.css.tmpl.ListStylePositionValue;
import objectos.css.tmpl.ListStyleTypeValue;
import objectos.css.tmpl.MarginValue;
import objectos.css.tmpl.MaxContentKeyword;
import objectos.css.tmpl.MaxHeightOrWidthValue;
import objectos.css.tmpl.MediumKeyword;
import objectos.css.tmpl.MenuKeyword;
import objectos.css.tmpl.MinContentKeyword;
import objectos.css.tmpl.MinHeightOrWidthValue;
import objectos.css.tmpl.NoneKeyword;
import objectos.css.tmpl.NormalKeyword;
import objectos.css.tmpl.OutlineStyleValue;
import objectos.css.tmpl.OutsetKeyword;
import objectos.css.tmpl.OverflowPosition;
import objectos.css.tmpl.PositionValue;
import objectos.css.tmpl.ProgressKeyword;
import objectos.css.tmpl.ResizeValue;
import objectos.css.tmpl.RidgeKeyword;
import objectos.css.tmpl.RightKeyword;
import objectos.css.tmpl.SmallKeyword;
import objectos.css.tmpl.SolidKeyword;
import objectos.css.tmpl.StartKeyword;
import objectos.css.tmpl.SubKeyword;
import objectos.css.tmpl.TableKeyword;
import objectos.css.tmpl.TextDecorationLineMultiValue;
import objectos.css.tmpl.TextDecorationLineSingleValue;
import objectos.css.tmpl.TextDecorationStyleValue;
import objectos.css.tmpl.TextDecorationThicknessValue;
import objectos.css.tmpl.TextIndentValue;
import objectos.css.tmpl.TextSizeAdjustValue;
import objectos.css.tmpl.TextTransformValue;
import objectos.css.tmpl.TextareaKeyword;
import objectos.css.tmpl.TopValue;
import objectos.css.tmpl.VerticalAlignValue;
import objectos.lang.Generated;

@Generated("objectos.selfgen.CssSpec")
public enum StandardName implements Selector, ColorValue,
    AppearanceValue,
    BackgroundImageValue,
    BorderCollapseValue,
    BottomValue,
    BoxSizingValue,
    CounterStyleValue,
    CursorValue,
    DisplayBoxValue,
    DisplayInsideValue,
    DisplayInternalValue,
    DisplayLegacyValue,
    DisplayListItemValue,
    DisplayOutsideValue,
    FlexDirectionValue,
    FontFamilyValue,
    FontFeatureSettingsValue,
    FontSizeValue,
    FontValue,
    FontVariationSettingsValue,
    FontWeightValue,
    GlobalKeyword,
    HeightOrWidthValue,
    JustifyContentPosition,
    JustifyContentValue,
    LineHeightValue,
    LineStyle,
    LineWidth,
    ListStyleImageValue,
    ListStylePositionValue,
    ListStyleTypeValue,
    MarginValue,
    MaxHeightOrWidthValue,
    MinHeightOrWidthValue,
    OutlineStyleValue,
    OverflowPosition,
    PositionValue,
    ResizeValue,
    TextDecorationLineMultiValue,
    TextDecorationLineSingleValue,
    TextDecorationStyleValue,
    TextDecorationThicknessValue,
    TextIndentValue,
    TextSizeAdjustValue,
    TextTransformValue,
    TopValue,
    VerticalAlignValue,
    AutoKeyword,
    BlockKeyword,
    ButtonKeyword,
    CenterKeyword,
    DashedKeyword,
    DottedKeyword,
    DoubleKeyword,
    EndKeyword,
    FitContentKeyword,
    FlexEndKeyword,
    FlexStartKeyword,
    GrooveKeyword,
    InlineKeyword,
    InsetKeyword,
    LeftKeyword,
    MaxContentKeyword,
    MediumKeyword,
    MenuKeyword,
    MinContentKeyword,
    NoneKeyword,
    NormalKeyword,
    OutsetKeyword,
    ProgressKeyword,
    RidgeKeyword,
    RightKeyword,
    SmallKeyword,
    SolidKeyword,
    StartKeyword,
    SubKeyword,
    TableKeyword,
    TextareaKeyword {
  CH("ch"),

  CM("cm"),

  EM("em"),

  EX("ex"),

  IN("in"),

  MM("mm"),

  PC("pc"),

  PT("pt"),

  PX("px"),

  Q("q"),

  REM("rem"),

  VH("vh"),

  VMAX("vmax"),

  VMIN("vmin"),

  VW("vw"),

  PCT("%"),

  __after("::after"),

  __before("::before"),

  __placeholder("::placeholder"),

  __webkitFileUploadButton("::-webkit-file-upload-button"),

  __webkitInnerSpinButton("::-webkit-inner-spin-button"),

  __webkitOuterSpinButton("::-webkit-outer-spin-button"),

  __webkitSearchDecoration("::-webkit-search-decoration"),

  _disabled(":disabled"),

  _mozFocusring(":-moz-focusring"),

  _mozUiInvalid(":-moz-ui-invalid"),

  a("a"),

  audio("audio"),

  b("b"),

  blockquote("blockquote"),

  body("body"),

  canvas("canvas"),

  code("code"),

  dd("dd"),

  dl("dl"),

  embed("embed"),

  fieldset("fieldset"),

  figure("figure"),

  form("form"),

  h1("h1"),

  h2("h2"),

  h3("h3"),

  h4("h4"),

  h5("h5"),

  h6("h6"),

  hr("hr"),

  html("html"),

  iframe("iframe"),

  img("img"),

  input("input"),

  kbd("kbd"),

  label("label"),

  legend("legend"),

  li("li"),

  object("object"),

  ol("ol"),

  optgroup("optgroup"),

  p("p"),

  pre("pre"),

  samp("samp"),

  select("select"),

  strong("strong"),

  summary("summary"),

  sup("sup"),

  svg("svg"),

  ul("ul"),

  video("video"),

  any("*"),

  aqua("aqua"),

  black("black"),

  blue("blue"),

  currentcolor("currentcolor"),

  fuchsia("fuchsia"),

  gray("gray"),

  green("green"),

  lime("lime"),

  maroon("maroon"),

  navy("navy"),

  olive("olive"),

  purple("purple"),

  red("red"),

  silver("silver"),

  teal("teal"),

  transparent("transparent"),

  white("white"),

  yellow("yellow"),

  _default("default"),

  _double("double"),

  _static("static"),

  _super("super"),

  absolute("absolute"),

  alias("alias"),

  allScroll("all-scroll"),

  arabicIndic("arabic-indic"),

  armenian("armenian"),

  auto("auto"),

  baseline("baseline"),

  bengali("bengali"),

  blink("blink"),

  block("block"),

  bold("bold"),

  bolder("bolder"),

  borderBox("border-box"),

  both("both"),

  bottom("bottom"),

  button("button"),

  cambodian("cambodian"),

  capitalize("capitalize"),

  caption("caption"),

  cell("cell"),

  center("center"),

  checkbox("checkbox"),

  circle("circle"),

  cjkDecimal("cjk-decimal"),

  cjkEarthlyBranch("cjk-earthly-branch"),

  cjkHeavenlyStem("cjk-heavenly-stem"),

  cjkIdeographic("cjk-ideographic"),

  colResize("col-resize"),

  collapse("collapse"),

  column("column"),

  columnReverse("column-reverse"),

  contentBox("content-box"),

  contents("contents"),

  contextMenu("context-menu"),

  copy("copy"),

  crosshair("crosshair"),

  cursive("cursive"),

  dashed("dashed"),

  decimal("decimal"),

  decimalLeadingZero("decimal-leading-zero"),

  devanagari("devanagari"),

  disc("disc"),

  disclosureClosed("disclosure-closed"),

  disclosureOpen("disclosure-open"),

  dotted("dotted"),

  eResize("e-resize"),

  eachLine("each-line"),

  emoji("emoji"),

  end("end"),

  ethiopicNumeric("ethiopic-numeric"),

  ewResize("ew-resize"),

  fangsong("fangsong"),

  fantasy("fantasy"),

  fitContent("fit-content"),

  fixed("fixed"),

  flex("flex"),

  flexEnd("flex-end"),

  flexStart("flex-start"),

  flow("flow"),

  flowRoot("flow-root"),

  fromFont("from-font"),

  fullSizeKana("full-size-kana"),

  fullWidth("full-width"),

  georgian("georgian"),

  grab("grab"),

  grabbing("grabbing"),

  grid("grid"),

  groove("groove"),

  gujarati("gujarati"),

  gurmukhi("gurmukhi"),

  hanging("hanging"),

  hebrew("hebrew"),

  help("help"),

  hidden("hidden"),

  hiragana("hiragana"),

  hiraganaIroha("hiragana-iroha"),

  horizontal("horizontal"),

  icon("icon"),

  inherit("inherit"),

  initial("initial"),

  inline("inline"),

  inlineBlock("inline-block"),

  inlineFlex("inline-flex"),

  inlineGrid("inline-grid"),

  inlineTable("inline-table"),

  inset("inset"),

  inside("inside"),

  japaneseFormal("japanese-formal"),

  japaneseInformal("japanese-informal"),

  kannada("kannada"),

  katakana("katakana"),

  katakanaIroha("katakana-iroha"),

  khmer("khmer"),

  koreanHangulFormal("korean-hangul-formal"),

  koreanHanjaFormal("korean-hanja-formal"),

  koreanHanjaInformal("korean-hanja-informal"),

  lao("lao"),

  large("large"),

  larger("larger"),

  left("left"),

  lighter("lighter"),

  lineThrough("line-through"),

  listItem("list-item"),

  listbox("listbox"),

  lowerAlpha("lower-alpha"),

  lowerArmenian("lower-armenian"),

  lowerGreek("lower-greek"),

  lowerLatin("lower-latin"),

  lowerRoman("lower-roman"),

  lowercase("lowercase"),

  malayalam("malayalam"),

  math("math"),

  maxContent("max-content"),

  medium("medium"),

  menu("menu"),

  menulist("menulist"),

  menulistButton("menulist-button"),

  messageBox("message-box"),

  meter("meter"),

  middle("middle"),

  minContent("min-content"),

  mongolian("mongolian"),

  monospace("monospace"),

  move("move"),

  mozArabicIndic("-moz-arabic-indic"),

  mozBengali("-moz-bengali"),

  mozCjkEarthlyBranch("-moz-cjk-earthly-branch"),

  mozCjkHeavenlyStem("-moz-cjk-heavenly-stem"),

  mozDevanagari("-moz-devanagari"),

  mozGujarati("-moz-gujarati"),

  mozGurmukhi("-moz-gurmukhi"),

  mozKannada("-moz-kannada"),

  mozKhmer("-moz-khmer"),

  mozLao("-moz-lao"),

  mozMalayalam("-moz-malayalam"),

  mozMyanmar("-moz-myanmar"),

  mozOriya("-moz-oriya"),

  mozPersian("-moz-persian"),

  mozTamil("-moz-tamil"),

  mozTelugu("-moz-telugu"),

  mozThai("-moz-thai"),

  myanmar("myanmar"),

  nResize("n-resize"),

  neResize("ne-resize"),

  neswResize("nesw-resize"),

  noDrop("no-drop"),

  none("none"),

  normal("normal"),

  notAllowed("not-allowed"),

  nsResize("ns-resize"),

  nwResize("nw-resize"),

  nwseResize("nwse-resize"),

  oriya("oriya"),

  outset("outset"),

  outside("outside"),

  overline("overline"),

  persian("persian"),

  pointer("pointer"),

  progress("progress"),

  progressBar("progress-bar"),

  pushButton("push-button"),

  radio("radio"),

  relative("relative"),

  ridge("ridge"),

  right("right"),

  row("row"),

  rowResize("row-resize"),

  rowReverse("row-reverse"),

  ruby("ruby"),

  rubyBase("ruby-base"),

  rubyBaseContainer("ruby-base-container"),

  rubyText("ruby-text"),

  rubyTextContainer("ruby-text-container"),

  runIn("runIn"),

  sResize("s-resize"),

  safe("safe"),

  sansSerif("sans-serif"),

  seResize("se-resize"),

  searchfield("searchfield"),

  separate("separate"),

  serif("serif"),

  simpChineseFormal("simp-chinese-formal"),

  simpChineseInformal("simp-chinese-informal"),

  sliderHorizontal("slider-horizontal"),

  small("small"),

  smallCaption("small-caption"),

  smaller("smaller"),

  solid("solid"),

  spaceAround("space-around"),

  spaceBetween("space-between"),

  spaceEvenly("space-evenly"),

  square("square"),

  squareButton("square-button"),

  start("start"),

  statusBar("status-bar"),

  sticky("sticky"),

  stretch("stretch"),

  sub("sub"),

  swResize("sw-resize"),

  systemUi("system-ui"),

  table("table"),

  tableCaption("table-caption"),

  tableCell("table-cell"),

  tableColumn("table-column"),

  tableColumnGroup("table-column-group"),

  tableFooterGroup("table-footer-group"),

  tableHeaderGroup("table-header-group"),

  tableRow("table-row"),

  tableRowGroup("table-row-group"),

  tamil("tamil"),

  telugu("telugu"),

  text("text"),

  textBottom("text-bottom"),

  textTop("text-top"),

  textarea("textarea"),

  textfield("textfield"),

  thai("thai"),

  thick("thick"),

  thin("thin"),

  tibetan("tibetan"),

  top("top"),

  tradChineseFormal("trad-chinese-formal"),

  tradChineseInformal("trad-chinese-informal"),

  uiMonospace("ui-monospace"),

  uiRounded("ui-rounded"),

  uiSansSerif("ui-sans-serif"),

  uiSerif("ui-serif"),

  underline("underline"),

  unsafe("unsafe"),

  unset("unset"),

  upperAlpha("upper-alpha"),

  upperArmenian("upper-armenian"),

  upperLatin("upper-latin"),

  upperRoman("upper-roman"),

  uppercase("uppercase"),

  vertical("vertical"),

  verticalText("vertical-text"),

  wResize("w-resize"),

  wait("wait"),

  wavy("wavy"),

  xLarge("x-large"),

  xSmall("x-small"),

  xxLarge("xx-large"),

  xxSmall("xx-small"),

  xxxLarge("xxx-large"),

  zoomIn("zoom-in"),

  zoomOut("zoom-out");

  private static final StandardName[] VALUES = values();

  public final String cssName;

  private StandardName(String cssName) {
    this.cssName = cssName;
  }

  public static StandardName byOrdinal(int ordinal) {
    return VALUES[ordinal];
  }

  @Override
  public final String toString() {
    return cssName;
  }
}
