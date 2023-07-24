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
import objectos.selfgen.css2.util.CssUtilSelfGen;
import objectos.selfgen.css2.util.Names;
import objectos.selfgen.css2.util.Prefix;
import objectos.selfgen.css2.util.Prefix.Breakpoint;
import objectos.selfgen.css2.util.PropertyClass;
import objectos.selfgen.css2.util.SelectorKind;
import objectos.selfgen.css2.util.StyleMethod;
import objectos.selfgen.css2.util.Value;

public final class CssUtilSpec extends CssUtilSelfGen {

  private Breakpoint prefixAll;
  private Breakpoint prefixSmall;
  private Breakpoint prefixMedium;
  private Breakpoint prefixLarge;
  private Breakpoint prefixXLarge;
  private Breakpoint prefixXLarge2;

  private List<Prefix> responsive;

  private Names spacing;

  public static void main(String[] args) throws IOException {
    CssUtilSpec spec;
    spec = new CssUtilSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    // breakpoints
    prefixAll = breakpoint("All", 0);
    prefixSmall = breakpoint("Small", 640);
    prefixMedium = breakpoint("Medium", 768);
    prefixLarge = breakpoint("Large", 1024);
    prefixXLarge = breakpoint("XLarge", 1280);
    prefixXLarge2 = breakpoint("XLarge2", 1536);

    responsive = List.of(
      prefixAll,
      prefixSmall,
      prefixMedium,
      prefixLarge,
      prefixXLarge,
      prefixXLarge2
    );

    spacing = names(
      name("PX", px(1)),
      name("V0", zero()),
      name("V0_5", rem(0.125)),
      name("V1", rem(0.25)),
      name("V1_5", rem(0.375)),
      name("V2", rem(0.5)),
      name("V2_5", rem(0.625)),
      name("V3", rem(0.75)),
      name("V3_5", rem(0.875)),
      name("V4", rem(1)),
      name("V5", rem(1.25)),
      name("V6", rem(1.5)),
      name("V7", rem(1.75)),
      name("V8", rem(2)),
      name("V9", rem(2.25)),
      name("V10", rem(2.5)),
      name("V11", rem(2.75)),
      name("V12", rem(3)),
      name("V14", rem(3.5)),
      name("V16", rem(4)),
      name("V20", rem(5)),
      name("V24", rem(6)),
      name("V28", rem(7)),
      name("V32", rem(8)),
      name("V36", rem(9)),
      name("V40", rem(10)),
      name("V44", rem(11)),
      name("V48", rem(12)),
      name("V52", rem(13)),
      name("V56", rem(14)),
      name("V60", rem(15)),
      name("V64", rem(16)),
      name("V68", rem(17)),
      name("V72", rem(18)),
      name("V80", rem(20)),
      name("V96", rem(24))
    );

    // D
    display();

    // F
    flexDirection();
    fontSize();
    fontStyle();
    fontWeight();

    // H
    height();

    // J
    justifyContent();

    // M
    margin();
    maxWidth();
    minHeight();

    // P
    padding();

    // S
    space();

    // W
    width();
  }

  private void display() {
    Names names;
    names = names(
      name("HIDDEN", k("none")),
      name("BLOCK", k("block")),
      name("FLOW_ROOT", k("flowRoot")),
      name("INLINE_BLOCK", k("inlineBlock")),
      name("INLINE", k("inline")),
      name("FLEX", k("flex")),
      name("INLINE_FLEX", k("inlineFlex")),
      name("GRID", k("grid")),
      name("INLINE_GRID", k("inlineGrid")),
      name("TABLE", k("table")),
      name("TABLE_CAPTION", k("tableCaption")),
      name("TABLE_CELL", k("tableCell")),
      name("TABLE_COLUMN", k("tableColumn")),
      name("TABLE_COLUMN_GROUP", k("tableColumnGroup")),
      name("TABLE_FOOTER_GROUP", k("tableFooterGroup")),
      name("TABLE_HEADER_GROUP", k("tableHeaderGroup")),
      name("TABLE_ROW_GROUP", k("tableRowGroup")),
      name("TABLE_ROW", k("tableRow"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Display"), methods("display"), names);
    }
  }

  private void flexDirection() {
    Names names;
    names = names(
      name("ROW", k("row")),
      name("ROW_REVERSE", k("rowReverse")),
      name("COLUMN", k("column")),
      name("COLUMN_REVERSE", k("columnReverse"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("FlexDirection"), methods("flexDirection"), names);
    }
  }

  private void fontSize() {
    record FontSize(String name, Value fontSize, Value lineHeight) {}

    Value one;
    one = new Value.LiteralInt(1);

    List<FontSize> values = List.of(
      new FontSize("X_SMALL", rem(0.75), rem(1)),
      new FontSize("SMALL", rem(0.875), rem(1.25)),
      new FontSize("BASE", rem(1), rem(1.5)),
      new FontSize("LARGE", rem(1.125), rem(1.75)),
      new FontSize("X_LARGE", rem(1.25), rem(1.75)),
      new FontSize("X_LARGE2", rem(1.5), rem(2)),
      new FontSize("X_LARGE3", rem(1.875), rem(2.25)),
      new FontSize("X_LARGE4", rem(2.25), rem(2.5)),
      new FontSize("X_LARGE5", rem(3), one),
      new FontSize("X_LARGE6", rem(3.75), one),
      new FontSize("X_LARGE7", rem(4.5), one),
      new FontSize("X_LARGE8", rem(6), one),
      new FontSize("X_LARGE9", rem(8), one)
    );

    for (Prefix prefix : responsive) {
      PropertyClass propertyClass;
      propertyClass = prefix.propertyClass("FontSize");

      for (FontSize value : values) {
        StyleMethod styleMethod;
        styleMethod = propertyClass.style(SelectorKind.STANDARD, value.name);

        styleMethod.addDeclaration("fontSize", value.fontSize);

        styleMethod.addDeclaration("lineHeight", value.lineHeight);
      }
    }
  }

  private void fontStyle() {
    Names names;
    names = names(
      name("ITALIC", k("italic")),
      name("NORMAL", k("normal"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("FontStyle"), methods("fontStyle"), names);
    }
  }

  private void fontWeight() {
    Names names;
    names = names(
      name("THIN", l(100)),
      name("EXTRALIGHT", l(200)),
      name("LIGHT", l(300)),
      name("NORMAL", l(400)),
      name("MEDIUM", l(500)),
      name("SEMIBOLD", l(600)),
      name("BOLD", l(700)),
      name("EXTRABOLD", l(800)),
      name("BLOCK", l(900))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("FontWeight"), methods("fontWeight"), names);
    }
  }

  private void height() {
    Names names;
    names = names(
      spacing,
      name("AUTO", k("auto")),
      name("P1_2", pct(50)),
      name("P1_3", pct(33.333333)),
      name("P2_3", pct(66.666667)),
      name("P1_4", pct(25)),
      name("P2_4", pct(50)),
      name("P3_4", pct(75)),
      name("P1_5", pct(20)),
      name("P2_5", pct(40)),
      name("P3_5", pct(60)),
      name("P4_5", pct(80)),
      name("P1_6", pct(16.666667)),
      name("P2_6", pct(33.333333)),
      name("P3_6", pct(50)),
      name("P4_6", pct(66.666667)),
      name("P5_6", pct(83.333333)),
      name("FULL", pct(100)),
      name("SCREEN", vh(100)),
      name("MIN", k("minContent")),
      name("MAX", k("maxContent")),
      name("FIT", k("fitContent"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Height"), methods("height"), names);
    }
  }

  private void justifyContent() {
    Names names;
    names = names(
      name("START", k("flexStart")),
      name("CENTER", k("center")),
      name("END", k("flexEnd")),
      name("BETWEEN", k("spaceBetween")),
      name("AROUND", k("spaceAround"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("JustifyContent"), methods("justifyContent"), names);
    }
  }

  private void margin() {
    Names names;
    names = names(
      name("AUTO", k("auto")),
      spacing
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Margin"), methods("margin"), names);

      generate(prefix, simpleName("MarginX"), methods("marginRight", "marginLeft"), names);

      generate(prefix, simpleName("MarginY"), methods("marginTop", "marginBottom"), names);

      generate(prefix, simpleName("MarginTop"), methods("marginTop"), names);

      generate(prefix, simpleName("MarginRight"), methods("marginRight"), names);

      generate(prefix, simpleName("MarginBottom"), methods("marginBottom"), names);

      generate(prefix, simpleName("MarginLeft"), methods("marginLeft"), names);
    }
  }

  private void maxWidth() {
    Names names;
    names = names(
      name("V0", zero()),
      name("NONE", k("none")),
      name("X_SMALL", rem(20)),
      name("SMALL", rem(24)),
      name("MEDIUM", rem(28)),
      name("LARGE", rem(32)),
      name("X_LARGE", rem(36)),
      name("X_LARGE2", rem(42)),
      name("X_LARGE3", rem(48)),
      name("X_LARGE4", rem(56)),
      name("X_LARGE5", rem(64)),
      name("X_LARGE6", rem(72)),
      name("X_LARGE7", rem(80)),
      name("FULL", pct(100)),
      name("MIN", k("minContent")),
      name("MAX", k("maxContent")),
      name("FIT", k("fitContent")),
      name("PROSE", ch(65)),
      name("SCREEN_SMALL", px(prefixSmall.length)),
      name("SCREEN_MEDIUM", px(prefixMedium.length)),
      name("SCREEN_LARGE", px(prefixLarge.length)),
      name("SCREEN_X_LARGE", px(prefixXLarge.length)),
      name("SCREEN_X_LARGE2", px(prefixXLarge2.length))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("MaxWidth"), methods("maxWidth"), names);
    }
  }

  private void minHeight() {
    Names names;
    names = names(
      name("V0", zero()),
      name("FULL", pct(100)),
      name("SCREEN", vh(100))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("MinHeight"), methods("minHeight"), names);
    }
  }

  private void padding() {
    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Padding"), methods("padding"), spacing);

      generate(prefix, simpleName("PaddingX"), methods("paddingRight", "paddingLeft"), spacing);

      generate(prefix, simpleName("PaddingY"), methods("paddingTop", "paddingBottom"), spacing);

      generate(prefix, simpleName("PaddingTop"), methods("paddingTop"), spacing);

      generate(prefix, simpleName("PaddingRight"), methods("paddingRight"), spacing);

      generate(prefix, simpleName("PaddingBottom"), methods("paddingBottom"), spacing);

      generate(prefix, simpleName("PaddingLeft"), methods("paddingLeft"), spacing);
    }
  }

  private void space() {
    for (Prefix prefix : responsive) {
      generateAllButFirst(prefix, simpleName("SpaceX"), methods("marginLeft"), spacing);

      generateAllButFirst(prefix, simpleName("SpaceY"), methods("marginTop"), spacing);
    }
  }

  private void width() {
    Names names;
    names = names(
      spacing,
      name("AUTO", k("auto")),
      name("P1_2", pct(50)),
      name("P1_3", pct(33.333333)),
      name("P2_3", pct(66.666667)),
      name("P1_4", pct(25)),
      name("P2_4", pct(50)),
      name("P3_4", pct(75)),
      name("P1_5", pct(20)),
      name("P2_5", pct(40)),
      name("P3_5", pct(60)),
      name("P4_5", pct(80)),
      name("P1_6", pct(16.666667)),
      name("P2_6", pct(33.333333)),
      name("P3_6", pct(50)),
      name("P4_6", pct(66.666667)),
      name("P5_6", pct(83.333333)),
      name("P1_12", pct(8.333333)),
      name("P2_12", pct(16.666667)),
      name("P3_12", pct(25)),
      name("P4_12", pct(33.333333)),
      name("P5_12", pct(41.666667)),
      name("P6_12", pct(50)),
      name("P7_12", pct(58.333333)),
      name("P8_12", pct(66.666667)),
      name("P9_12", pct(75)),
      name("P10_12", pct(83.333333)),
      name("P11_12", pct(91.666667)),
      name("FULL", pct(100)),
      name("SCREEN", vw(100)),
      name("MIN", k("minContent")),
      name("MAX", k("maxContent")),
      name("FIT", k("fitContent"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Width"), methods("width"), names);
    }
  }

}