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
 *
 * Based on:
 * https://github.com/tailwindlabs/tailwindcss/blob/master/stubs/config.full.js
 *
 * Copyright (c) Adam Wathan <adam.wathan@gmail.com>
 * Copyright (c) Jonathan Reinink <jonathan@reinink.ca>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package objectos.selfgen;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import selfgen.css.util.CssUtilSelfGen;
import selfgen.css.util.Names;
import selfgen.css.util.Prefix;
import selfgen.css.util.Property1;
import selfgen.css.util.Property2;
import selfgen.css.util.PropertyClass;
import selfgen.css.util.Value;

final class CssUtilSpec extends CssUtilSelfGen {

  private final Set<Prefix> responsive = Prefix.RESPONSIVE;

  private Names colors;

  private Names spacing;

  public static void main(String[] args) throws IOException {
    CssUtilSpec spec;
    spec = new CssUtilSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    colors = $colors();

    spacing = $spacing();

    // A

    alignContent();
    alignItems();
    alignSelf();

    // B

    backgroundColor();
    borderColor();
    borderStyle();
    borderWidth();

    // C

    color();
    cursor();

    // D

    display();

    // F

    flex();
    flexDirection();
    flexGrow();
    fontSize();
    fontStyle();
    fontWeight();

    // H

    height();

    // I

    inset();

    // J

    justifyContent();

    // L

    letterSpacing();
    lineHeight();
    listStylePosition();
    listStyleType();

    // M

    margin();
    maxWidth();
    minHeight();

    // P

    padding();
    position();

    // S

    space();

    // T

    textAlign();
    textDecoration();
    textTransform();

    // V

    verticalAlign();

    // W

    width();

    // Z

    zIndex();
  }

  private Names $colors() {
    return names(
      name("TRANSPARENT", k("transparent")),

      name("BLACK", hex("#000")),
      name("WHITE", hex("#ffffff")),

      name("SLATE_050", hex("#f8fafc")),
      name("SLATE_100", hex("#f1f5f9")),
      name("SLATE_200", hex("#e2e8f0")),
      name("SLATE_300", hex("#cbd5e1")),
      name("SLATE_400", hex("#94a3b8")),
      name("SLATE_500", hex("#64748b")),
      name("SLATE_600", hex("#475569")),
      name("SLATE_700", hex("#334155")),
      name("SLATE_800", hex("#1e293b")),
      name("SLATE_900", hex("#0f172a")),

      name("GRAY_050", hex("#f9fafb")),
      name("GRAY_100", hex("#f3f4f6")),
      name("GRAY_200", hex("#e5e7eb")),
      name("GRAY_300", hex("#d1d5db")),
      name("GRAY_400", hex("#9ca3af")),
      name("GRAY_500", hex("#6b7280")),
      name("GRAY_600", hex("#4b5563")),
      name("GRAY_700", hex("#374151")),
      name("GRAY_800", hex("#1f2937")),
      name("GRAY_900", hex("#111827")),

      name("ZINC_050", hex("#fafafa")),
      name("ZINC_100", hex("#f4f4f5")),
      name("ZINC_200", hex("#e4e4e7")),
      name("ZINC_300", hex("#d4d4d8")),
      name("ZINC_400", hex("#a1a1aa")),
      name("ZINC_500", hex("#71717a")),
      name("ZINC_600", hex("#52525b")),
      name("ZINC_700", hex("#3f3f46")),
      name("ZINC_800", hex("#27272a")),
      name("ZINC_900", hex("#18181b")),

      name("NEUTRAL_050", hex("#fafafa")),
      name("NEUTRAL_100", hex("#f5f5f5")),
      name("NEUTRAL_200", hex("#e5e5e5")),
      name("NEUTRAL_300", hex("#d4d4d4")),
      name("NEUTRAL_400", hex("#a3a3a3")),
      name("NEUTRAL_500", hex("#737373")),
      name("NEUTRAL_600", hex("#525252")),
      name("NEUTRAL_700", hex("#404040")),
      name("NEUTRAL_800", hex("#262626")),
      name("NEUTRAL_900", hex("#171717")),

      name("STONE_050", hex("#fafaf9")),
      name("STONE_100", hex("#f5f5f4")),
      name("STONE_200", hex("#e7e5e4")),
      name("STONE_300", hex("#d6d3d1")),
      name("STONE_400", hex("#a8a29e")),
      name("STONE_500", hex("#78716c")),
      name("STONE_600", hex("#57534e")),
      name("STONE_700", hex("#44403c")),
      name("STONE_800", hex("#292524")),
      name("STONE_900", hex("#1c1917")),

      name("RED_050", hex("#fef2f2")),
      name("RED_100", hex("#fee2e2")),
      name("RED_200", hex("#fecaca")),
      name("RED_300", hex("#fca5a5")),
      name("RED_400", hex("#f87171")),
      name("RED_500", hex("#ef4444")),
      name("RED_600", hex("#dc2626")),
      name("RED_700", hex("#b91c1c")),
      name("RED_800", hex("#991b1b")),
      name("RED_900", hex("#7f1d1d")),

      name("ORANGE_050", hex("#fff7ed")),
      name("ORANGE_100", hex("#ffedd5")),
      name("ORANGE_200", hex("#fed7aa")),
      name("ORANGE_300", hex("#fdba74")),
      name("ORANGE_400", hex("#fb923c")),
      name("ORANGE_500", hex("#f97316")),
      name("ORANGE_600", hex("#ea580c")),
      name("ORANGE_700", hex("#c2410c")),
      name("ORANGE_800", hex("#9a3412")),
      name("ORANGE_900", hex("#7c2d12")),

      name("AMBER_050", hex("#fffbeb")),
      name("AMBER_100", hex("#fef3c7")),
      name("AMBER_200", hex("#fde68a")),
      name("AMBER_300", hex("#fcd34d")),
      name("AMBER_400", hex("#fbbf24")),
      name("AMBER_500", hex("#f59e0b")),
      name("AMBER_600", hex("#d97706")),
      name("AMBER_700", hex("#b45309")),
      name("AMBER_800", hex("#92400e")),
      name("AMBER_900", hex("#78350f")),

      name("YELLOW_050", hex("#fefce8")),
      name("YELLOW_100", hex("#fef9c3")),
      name("YELLOW_200", hex("#fef08a")),
      name("YELLOW_300", hex("#fde047")),
      name("YELLOW_400", hex("#facc15")),
      name("YELLOW_500", hex("#eab308")),
      name("YELLOW_600", hex("#ca8a04")),
      name("YELLOW_700", hex("#a16207")),
      name("YELLOW_800", hex("#854d0e")),
      name("YELLOW_900", hex("#713f12")),

      name("LIME_050", hex("#f7fee7")),
      name("LIME_100", hex("#ecfccb")),
      name("LIME_200", hex("#d9f99d")),
      name("LIME_300", hex("#bef264")),
      name("LIME_400", hex("#a3e635")),
      name("LIME_500", hex("#84cc16")),
      name("LIME_600", hex("#65a30d")),
      name("LIME_700", hex("#4d7c0f")),
      name("LIME_800", hex("#3f6212")),
      name("LIME_900", hex("#365314")),

      name("GREEN_050", hex("#f0fdf4")),
      name("GREEN_100", hex("#dcfce7")),
      name("GREEN_200", hex("#bbf7d0")),
      name("GREEN_300", hex("#86efac")),
      name("GREEN_400", hex("#4ade80")),
      name("GREEN_500", hex("#22c55e")),
      name("GREEN_600", hex("#16a34a")),
      name("GREEN_700", hex("#15803d")),
      name("GREEN_800", hex("#166534")),
      name("GREEN_900", hex("#14532d")),

      name("EMERALD_050", hex("#ecfdf5")),
      name("EMERALD_100", hex("#d1fae5")),
      name("EMERALD_200", hex("#a7f3d0")),
      name("EMERALD_300", hex("#6ee7b7")),
      name("EMERALD_400", hex("#34d399")),
      name("EMERALD_500", hex("#10b981")),
      name("EMERALD_600", hex("#059669")),
      name("EMERALD_700", hex("#047857")),
      name("EMERALD_800", hex("#065f46")),
      name("EMERALD_900", hex("#064e3b")),

      name("TEAL_050", hex("#f0fdfa")),
      name("TEAL_100", hex("#ccfbf1")),
      name("TEAL_200", hex("#99f6e4")),
      name("TEAL_300", hex("#5eead4")),
      name("TEAL_400", hex("#2dd4bf")),
      name("TEAL_500", hex("#14b8a6")),
      name("TEAL_600", hex("#0d9488")),
      name("TEAL_700", hex("#0f766e")),
      name("TEAL_800", hex("#115e59")),
      name("TEAL_900", hex("#134e4a")),

      name("CYAN_050", hex("#ecfeff")),
      name("CYAN_100", hex("#cffafe")),
      name("CYAN_200", hex("#a5f3fc")),
      name("CYAN_300", hex("#67e8f9")),
      name("CYAN_400", hex("#22d3ee")),
      name("CYAN_500", hex("#06b6d4")),
      name("CYAN_600", hex("#0891b2")),
      name("CYAN_700", hex("#0e7490")),
      name("CYAN_800", hex("#155e75")),
      name("CYAN_900", hex("#164e63")),

      name("SKY_050", hex("#f0f9ff")),
      name("SKY_100", hex("#e0f2fe")),
      name("SKY_200", hex("#bae6fd")),
      name("SKY_300", hex("#7dd3fc")),
      name("SKY_400", hex("#38bdf8")),
      name("SKY_500", hex("#0ea5e9")),
      name("SKY_600", hex("#0284c7")),
      name("SKY_700", hex("#0369a1")),
      name("SKY_800", hex("#075985")),
      name("SKY_900", hex("#0c4a6e")),

      name("BLUE_050", hex("#eff6ff")),
      name("BLUE_100", hex("#dbeafe")),
      name("BLUE_200", hex("#bfdbfe")),
      name("BLUE_300", hex("#93c5fd")),
      name("BLUE_400", hex("#60a5fa")),
      name("BLUE_500", hex("#3b82f6")),
      name("BLUE_600", hex("#2563eb")),
      name("BLUE_700", hex("#1d4ed8")),
      name("BLUE_800", hex("#1e40af")),
      name("BLUE_900", hex("#1e3a8a")),

      name("INDIGO_050", hex("#eef2ff")),
      name("INDIGO_100", hex("#e0e7ff")),
      name("INDIGO_200", hex("#c7d2fe")),
      name("INDIGO_300", hex("#a5b4fc")),
      name("INDIGO_400", hex("#818cf8")),
      name("INDIGO_500", hex("#6366f1")),
      name("INDIGO_600", hex("#4f46e5")),
      name("INDIGO_700", hex("#4338ca")),
      name("INDIGO_800", hex("#3730a3")),
      name("INDIGO_900", hex("#312e81")),

      name("VIOLET_050", hex("#f5f3ff")),
      name("VIOLET_100", hex("#ede9fe")),
      name("VIOLET_200", hex("#ddd6fe")),
      name("VIOLET_300", hex("#c4b5fd")),
      name("VIOLET_400", hex("#a78bfa")),
      name("VIOLET_500", hex("#8b5cf6")),
      name("VIOLET_600", hex("#7c3aed")),
      name("VIOLET_700", hex("#6d28d9")),
      name("VIOLET_800", hex("#5b21b6")),
      name("VIOLET_900", hex("#4c1d95")),

      name("PURPLE_050", hex("#faf5ff")),
      name("PURPLE_100", hex("#f3e8ff")),
      name("PURPLE_200", hex("#e9d5ff")),
      name("PURPLE_300", hex("#d8b4fe")),
      name("PURPLE_400", hex("#c084fc")),
      name("PURPLE_500", hex("#a855f7")),
      name("PURPLE_600", hex("#9333ea")),
      name("PURPLE_700", hex("#7e22ce")),
      name("PURPLE_800", hex("#6b21a8")),
      name("PURPLE_900", hex("#581c87")),

      name("FUCHSIA_050", hex("#fdf4ff")),
      name("FUCHSIA_100", hex("#fae8ff")),
      name("FUCHSIA_200", hex("#f5d0fe")),
      name("FUCHSIA_300", hex("#f0abfc")),
      name("FUCHSIA_400", hex("#e879f9")),
      name("FUCHSIA_500", hex("#d946ef")),
      name("FUCHSIA_600", hex("#c026d3")),
      name("FUCHSIA_700", hex("#a21caf")),
      name("FUCHSIA_800", hex("#86198f")),
      name("FUCHSIA_900", hex("#701a75")),

      name("PINK_050", hex("#fdf2f8")),
      name("PINK_100", hex("#fce7f3")),
      name("PINK_200", hex("#fbcfe8")),
      name("PINK_300", hex("#f9a8d4")),
      name("PINK_400", hex("#f472b6")),
      name("PINK_500", hex("#ec4899")),
      name("PINK_600", hex("#db2777")),
      name("PINK_700", hex("#be185d")),
      name("PINK_800", hex("#9d174d")),
      name("PINK_900", hex("#831843")),

      name("ROSE_050", hex("#fff1f2")),
      name("ROSE_100", hex("#ffe4e6")),
      name("ROSE_200", hex("#fecdd3")),
      name("ROSE_300", hex("#fda4af")),
      name("ROSE_400", hex("#fb7185")),
      name("ROSE_500", hex("#f43f5e")),
      name("ROSE_600", hex("#e11d48")),
      name("ROSE_700", hex("#be123c")),
      name("ROSE_800", hex("#9f1239")),
      name("ROSE_900", hex("#881337"))
    );
  }

  private Names $spacing() {
    return names(
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
  }

  private void alignContent() {
    Property1 p;
    p = new Property1("AlignContent", "align-content");

    p.add("NORMAL", "normal");
    p.add("CENTER", "center");
    p.add("START", "flex-start");
    p.add("END", "flex-end");
    p.add("BETWEEN", "space-between");
    p.add("AROUND", "space-around");
    p.add("EVENLY", "space-evenly");
    p.add("BASELINE", "baseline");
    p.add("STRETCH", "stretch");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void alignItems() {
    Property1 p;
    p = new Property1("AlignItems", "align-items");

    p.add("START", "flex-start");
    p.add("END", "flex-end");
    p.add("CENTER", "center");
    p.add("BASELINE", "baseline");
    p.add("STRETCH", "stretch");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void alignSelf() {
    Property1 p;
    p = new Property1("AlignSelf", "align-self");

    p.add("AUTO", "auto");
    p.add("START", "flex-start");
    p.add("END", "flex-end");
    p.add("CENTER", "center");
    p.add("STRETCH", "stretch");
    p.add("BASELINE", "baseline");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void backgroundColor() {
    Property1 p;
    p = new Property1("BackgroundColor", "background-color");

    colors(p);

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }

    Prefix.HOVER.add(p);
  }

  private void borderColor() {
    List<PropertyClass> props;
    props = List.of(
      new Property1("BorderColor", "border-color"),
      new Property1("BorderTopColor", "border-top-color"),
      new Property1("BorderRightColor", "border-right-color"),
      new Property1("BorderBottomColor", "border-bottom-color"),
      new Property1("BorderLeftColor", "border-left-color"),
      new Property2("BorderXColor", "border-right-color", "border-left-color"),
      new Property2("BorderYColor", "border-top-color", "border-bottom-color")
    );

    for (var p : props) {
      colors(p);

      add(p);

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void borderStyle() {
    Property1 p;
    p = new Property1("BorderStyle", "border-style");

    p.add("SOLID", "solid");
    p.add("DASHED", "dashed");
    p.add("DOTTED", "dotted");
    p.add("DOUBLE", "double");
    p.add("HIDDEN", "hidden");
    p.add("NONE", "none");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void borderWidth() {
    List<PropertyClass> props;
    props = List.of(
      new Property1("BorderWidth", "border-width"),
      new Property1("BorderTopWidth", "border-top-width"),
      new Property1("BorderRightWidth", "border-right-width"),
      new Property1("BorderBottomWidth", "border-bottom-width"),
      new Property1("BorderLeftWidth", "border-left-width"),
      new Property2("BorderXWidth", "border-right-width", "border-left-width"),
      new Property2("BorderYWidth", "border-top-width", "border-bottom-width")
    );

    for (var p : props) {
      p.add("PX0", "0px");
      p.add("PX1", "1px");
      p.add("PX2", "2px");
      p.add("PX4", "4px");
      p.add("PX8", "8px");

      add(p);

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void color() {
    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("Color"), methods("color"), colors);
    }

    // generateHover(hover, simpleName("Color"), methods("color"), colors);
  }

  private void cursor() {
    Property1 p;
    p = new Property1("Cursor", "cursor");

    p.add("AUTO", "auto");
    p.add("DEFAULT", "default");
    p.add("POINTER", "pointer");
    p.add("WAIT", "wait");
    p.add("TEXT", "text");
    p.add("MOVE", "move");
    p.add("HELP", "help");
    p.add("NOT_ALLOWED", "not-allowed");
    p.add("NONE", "none");
    p.add("CONTEXT_MENU", "context-menu");
    p.add("PROGRESS", "progress");
    p.add("CELL", "cell");
    p.add("CROSSHAIR", "crosshair");
    p.add("VERTICAL_TEXT", "vertical-text");
    p.add("ALIAS", "alias");
    p.add("COPY", "copy");
    p.add("NO_DROP", "no-drop");
    p.add("GRAB", "grab");
    p.add("GRABBING", "grabbing");
    p.add("ALL_SCROLL", "all-scroll");
    p.add("COL_RESIZE", "col-resize");
    p.add("ROW_RESIZE", "row-resize");
    p.add("N_RESIZE", "n-resize");
    p.add("E_RESIZE", "e-resize");
    p.add("S_RESIZE", "s-resize");
    p.add("W_RESIZE", "w-resize");
    p.add("NE_RESIZE", "ne-resize");
    p.add("NW_RESIZE", "nw-resize");
    p.add("SE_RESIZE", "se-resize");
    p.add("SW_RESIZE", "sw-resize");
    p.add("EW_RESIZE", "ew-resize");
    p.add("NS_RESIZE", "ns-resize");
    p.add("NESW_RESIZE", "nesw-resize");
    p.add("NWSE_RESIZE", "nwse-resize");
    p.add("ZOOM_IN", "zoom-in");
    p.add("ZOOM_OUT", "zoom-out");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void display() {
    Property1 p;
    p = new Property1("Display", "display");

    p.add("NONE", "none");
    p.add("BLOCK", "block");
    p.add("FLOW_ROOT", "flow-root");
    p.add("INLINE_BLOCK", "inline-block");
    p.add("INLINE", "inline");
    p.add("FLEX", "flex");
    p.add("INLINE_FLEX", "inline-flex");
    p.add("GRID", "grid");
    p.add("INLINE_GRID", "inline-grid");
    p.add("TABLE", "table");
    p.add("TABLE_CAPTION", "table-caption");
    p.add("TABLE_CELL", "table-cell");
    p.add("TABLE_COLUMN", "table-column");
    p.add("TABLE_COLUMN_GROUP", "table-column-group");
    p.add("TABLE_FOOTER_GROUP", "table-footer-group");
    p.add("TABLE_HEADER_GROUP", "table-header-group");
    p.add("TABLE_ROW_GROUP", "table-row-group");
    p.add("TABLE_ROW", "table-row");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void flex() {
    Property1 p;
    p = new Property1("Flex", "flex");

    p.add("ONE", "1 1 0%");
    p.add("AUTO", "1 1 auto");
    p.add("INITIAL", "0 1 auto");
    p.add("NONE", "none");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void flexDirection() {
    Property1 p;
    p = new Property1("FlexDirection", "flex-direction");

    p.add("ROW", "row");
    p.add("ROW_REVERSE", "row-reverse");
    p.add("COLUMN", "column");
    p.add("COLUMN_REVERSE", "column-reverse");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void flexGrow() {
    Property1 p;
    p = new Property1("FlexGrow", "flex-grow");

    p.add("V1", "1");
    p.add("V0", "0");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  @SuppressWarnings("unused")
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
    Property1 p;
    p = new Property1("Height", "height");

    spacing(p);

    p.add("AUTO", "auto");
    p.add("HALF", "50%");
    p.add("THIRD1", "33.333333%");
    p.add("THIRD2", "66.666667%");
    p.add("QUARTER1", "25%");
    p.add("QUARTER2", "50%");
    p.add("QUARTER3", "75%");
    p.add("FIFTH1", "20%");
    p.add("FIFTH2", "40%");
    p.add("FIFTH3", "60%");
    p.add("FIFTH4", "80%");
    p.add("SIXTH1", "16.666667%");
    p.add("SIXTH2", "33.333333%");
    p.add("SIXTH3", "50%");
    p.add("SIXTH4", "66.666667%");
    p.add("SIXTH5", "83.333333%");
    p.add("FULL", "100%");
    p.add("SCREEN", "100vh");
    p.add("MIN", "min-content");
    p.add("MAX", "max-content");
    p.add("FIT", "fit-content");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void inset() {
    List<PropertyClass> props;
    props = List.of(
      new Property1("Top", "top"),
      new Property1("Right", "right"),
      new Property1("Bottom", "bottom"),
      new Property1("Left", "left"),
      new Property1("Inset", "inset"),

      new Property2("InsetX", "left", "right"),
      new Property2("InsetY", "top", "bottom")
    );

    for (var p : props) {
      spacing(p);

      p.add("AUTO", "auto");
      p.add("HALF", "50%");
      p.add("THIRD1", "33.333333%");
      p.add("THIRD2", "66.666667%");
      p.add("QUARTER1", "25%");
      p.add("QUARTER2", "50%");
      p.add("QUARTER3", "75%");
      p.add("FULL", "100%");

      add(p);

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void justifyContent() {
    Property1 p;
    p = new Property1("JustifyContent", "justify-content");

    p.add("NORMAL", "normal");
    p.add("START", "flex-start");
    p.add("END", "flex-end");
    p.add("CENTER", "center");
    p.add("BETWEEN", "space-between");
    p.add("AROUND", "space-around");
    p.add("EVENLY", "space-evenly");
    p.add("STRETCH", "stretch");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void letterSpacing() {
    Property1 p;
    p = new Property1("LetterSpacing", "letter-spacing");

    p.add("TIGHTER", "-0.05em");
    p.add("TIGHT", "-0.025em");
    p.add("NORMAL", "0em");
    p.add("WIDE", "0.025em");
    p.add("WIDER", "0.05em");
    p.add("WIDEST", "0.1em");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void lineHeight() {
    Names names = names(
      name("NONE", l(1)),
      name("TIGHT", l(1.25)),
      name("SNUG", l(1.375)),
      name("NORMAL", l(1.5)),
      name("RELAXED", l(1.625)),
      name("LOOSE", l(2)),
      name("V3", rem(0.75)),
      name("V4", rem(1)),
      name("V5", rem(1.25)),
      name("V6", rem(1.5)),
      name("V7", rem(1.75)),
      name("V8", rem(2)),
      name("V9", rem(2.25)),
      name("V10", rem(2.5))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("LineHeight"), methods("lineHeight"), names);
    }
  }

  private void listStylePosition() {
    Names names;
    names = names(
      name("INSIDE", k("inside")),
      name("OUTSIDE", k("outside"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("ListStylePosition"), methods("listStylePosition"), names);
    }
  }

  private void listStyleType() {
    Names names;
    names = names(
      name("NONE", k("none")),
      name("DISC", k("disc")),
      name("DECIMAL", k("decimal"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("ListStyleType"), methods("listStyleType"), names);
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
      name("PROSE", ch(65))
    //name("SCREEN_SMALL", px(prefixSmall.length)),
    //name("SCREEN_MEDIUM", px(prefixMedium.length)),
    //name("SCREEN_LARGE", px(prefixLarge.length)),
    //name("SCREEN_X_LARGE", px(prefixExtra.length)),
    //name("SCREEN_X_LARGE2", px(prefixMax.length))
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
    List<PropertyClass> props;
    props = List.of(
      new Property1("Padding", "padding"),
      new Property1("PaddingTop", "padding-top"),
      new Property1("PaddingRight", "padding-right"),
      new Property1("PaddingBottom", "padding-bottom"),
      new Property1("PaddingLeft", "padding-left"),
      new Property2("PaddingX", "padding-right", "padding-left"),
      new Property2("PaddingY", "padding-top", "padding-bottom")
    );

    for (var p : props) {
      spacing(p);

      add(p);

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void position() {
    Property1 p;
    p = new Property1("Position", "position");

    p.add("STATIC", "static");
    p.add("FIXED", "fixed");
    p.add("ABSOLUTE", "absolute");
    p.add("RELATIVE", "relative");
    p.add("STICKY", "sticky");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void space() {
    for (Prefix prefix : responsive) {
      generateAllButFirst(prefix, simpleName("SpaceX"), methods("marginLeft"), spacing);

      generateAllButFirst(prefix, simpleName("SpaceY"), methods("marginTop"), spacing);
    }
  }

  private void textAlign() {
    Names names = names(
      name("LEFT", k("left")),
      name("CENTER", k("center")),
      name("RIGHT", k("right")),
      name("JUSTIFY", k("justify"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("TextAlign"), methods("textAlign"), names);
    }
  }

  private void textDecoration() {
    Names names;
    names = names(
      name("UNDERLINE", k("underline")),
      name("LINE_THROUGH", k("lineThrough")),
      name("STRIKE", k("lineThrough")),
      name("NONE", k("none"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("TextDecoration"), methods("textDecoration"), names);
    }

    //generateHover(hover, simpleName("TextDecoration"), methods("textDecoration"), names);
  }

  private void textTransform() {
    Names names;
    names = names(
      name("UPPERCASE", k("uppercase")),
      name("LOWERCASE", k("lowercase")),
      name("CAPITALIZE", k("capitalize")),
      name("NONE", k("none"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("TextTransform"), methods("textTransform"), names);
    }
  }

  private void verticalAlign() {
    Names names;
    names = names(
      name("BASELINE", k("baseline")),
      name("TOP", k("top")),
      name("MIDDLE", k("middle")),
      name("BOTTOM", k("bottom")),
      name("TEXT_TOP", k("textTop")),
      name("TEXT_BOTTOM", k("textBottom")),
      name("SUB_ALIGN", k("sub")),
      name("SUPER_ALIGN", k("super_"))
    );

    for (Prefix prefix : responsive) {
      generate(prefix, simpleName("VerticalAlign"), methods("verticalAlign"), names);
    }
  }

  private void width() {
    Property1 p;
    p = new Property1("Width", "width");

    spacing(p);

    p.add("AUTO", "auto");
    p.add("HALF", "50%");
    p.add("THIRD1", "33.333333%");
    p.add("THIRD2", "66.666667%");
    p.add("QUARTER1", "25%");
    p.add("QUARTER2", "50%");
    p.add("QUARTER3", "75%");
    p.add("FIFTH1", "20%");
    p.add("FIFTH2", "40%");
    p.add("FIFTH3", "60%");
    p.add("FIFTH4", "80%");
    p.add("SIXTH1", "16.666667%");
    p.add("SIXTH2", "33.333333%");
    p.add("SIXTH3", "50%");
    p.add("SIXTH4", "66.666667%");
    p.add("SIXTH5", "83.333333%");
    p.add("TWELFTH1", "8.333333%");
    p.add("TWELFTH2", "16.666667%");
    p.add("TWELFTH3", "25%");
    p.add("TWELFTH4", "33.333333%");
    p.add("TWELFTH5", "41.666667%");
    p.add("TWELFTH6", "50%");
    p.add("TWELFTH7", "58.333333%");
    p.add("TWELFTH8", "66.666667%");
    p.add("TWELFTH9", "75%");
    p.add("TWELFTH10", "83.333333%");
    p.add("TWELFTH11", "91.666667%");
    p.add("FULL", "100%");
    p.add("SCREEN", "100vh");
    p.add("MIN", "min-content");
    p.add("MAX", "max-content");
    p.add("FIT", "fit-content");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void zIndex() {
    Property1 p;
    p = new Property1("ZIndex", "z-index");

    p.add("V0", "0");
    p.add("V10", "10");
    p.add("V20", "20");
    p.add("V30", "30");
    p.add("V40", "40");
    p.add("V50", "50");
    p.add("AUTO", "auto");

    add(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void colors(PropertyClass p) {
    p.add("INHERIT", "inherit");
    p.add("CURRENT", "currentColor");
    p.add("TRANSPARENT", "transparent");

    p.add("BLACK", rgb("#000000"));
    p.add("WHITE", rgb("#ffffff"));

    p.add("SLATE_050", rgb("#f8fafc"));
    p.add("SLATE_100", rgb("#f1f5f9"));
    p.add("SLATE_200", rgb("#e2e8f0"));
    p.add("SLATE_300", rgb("#cbd5e1"));
    p.add("SLATE_400", rgb("#94a3b8"));
    p.add("SLATE_500", rgb("#64748b"));
    p.add("SLATE_600", rgb("#475569"));
    p.add("SLATE_700", rgb("#334155"));
    p.add("SLATE_800", rgb("#1e293b"));
    p.add("SLATE_900", rgb("#0f172a"));

    p.add("GRAY_050", rgb("#f9fafb"));
    p.add("GRAY_100", rgb("#f3f4f6"));
    p.add("GRAY_200", rgb("#e5e7eb"));
    p.add("GRAY_300", rgb("#d1d5db"));
    p.add("GRAY_400", rgb("#9ca3af"));
    p.add("GRAY_500", rgb("#6b7280"));
    p.add("GRAY_600", rgb("#4b5563"));
    p.add("GRAY_700", rgb("#374151"));
    p.add("GRAY_800", rgb("#1f2937"));
    p.add("GRAY_900", rgb("#111827"));

    p.add("ZINC_050", rgb("#fafafa"));
    p.add("ZINC_100", rgb("#f4f4f5"));
    p.add("ZINC_200", rgb("#e4e4e7"));
    p.add("ZINC_300", rgb("#d4d4d8"));
    p.add("ZINC_400", rgb("#a1a1aa"));
    p.add("ZINC_500", rgb("#71717a"));
    p.add("ZINC_600", rgb("#52525b"));
    p.add("ZINC_700", rgb("#3f3f46"));
    p.add("ZINC_800", rgb("#27272a"));
    p.add("ZINC_900", rgb("#18181b"));

    p.add("NEUTRAL_050", rgb("#fafafa"));
    p.add("NEUTRAL_100", rgb("#f5f5f5"));
    p.add("NEUTRAL_200", rgb("#e5e5e5"));
    p.add("NEUTRAL_300", rgb("#d4d4d4"));
    p.add("NEUTRAL_400", rgb("#a3a3a3"));
    p.add("NEUTRAL_500", rgb("#737373"));
    p.add("NEUTRAL_600", rgb("#525252"));
    p.add("NEUTRAL_700", rgb("#404040"));
    p.add("NEUTRAL_800", rgb("#262626"));
    p.add("NEUTRAL_900", rgb("#171717"));

    p.add("STONE_050", rgb("#fafaf9"));
    p.add("STONE_100", rgb("#f5f5f4"));
    p.add("STONE_200", rgb("#e7e5e4"));
    p.add("STONE_300", rgb("#d6d3d1"));
    p.add("STONE_400", rgb("#a8a29e"));
    p.add("STONE_500", rgb("#78716c"));
    p.add("STONE_600", rgb("#57534e"));
    p.add("STONE_700", rgb("#44403c"));
    p.add("STONE_800", rgb("#292524"));
    p.add("STONE_900", rgb("#1c1917"));

    p.add("RED_050", rgb("#fef2f2"));
    p.add("RED_100", rgb("#fee2e2"));
    p.add("RED_200", rgb("#fecaca"));
    p.add("RED_300", rgb("#fca5a5"));
    p.add("RED_400", rgb("#f87171"));
    p.add("RED_500", rgb("#ef4444"));
    p.add("RED_600", rgb("#dc2626"));
    p.add("RED_700", rgb("#b91c1c"));
    p.add("RED_800", rgb("#991b1b"));
    p.add("RED_900", rgb("#7f1d1d"));

    p.add("ORANGE_050", rgb("#fff7ed"));
    p.add("ORANGE_100", rgb("#ffedd5"));
    p.add("ORANGE_200", rgb("#fed7aa"));
    p.add("ORANGE_300", rgb("#fdba74"));
    p.add("ORANGE_400", rgb("#fb923c"));
    p.add("ORANGE_500", rgb("#f97316"));
    p.add("ORANGE_600", rgb("#ea580c"));
    p.add("ORANGE_700", rgb("#c2410c"));
    p.add("ORANGE_800", rgb("#9a3412"));
    p.add("ORANGE_900", rgb("#7c2d12"));

    p.add("AMBER_050", rgb("#fffbeb"));
    p.add("AMBER_100", rgb("#fef3c7"));
    p.add("AMBER_200", rgb("#fde68a"));
    p.add("AMBER_300", rgb("#fcd34d"));
    p.add("AMBER_400", rgb("#fbbf24"));
    p.add("AMBER_500", rgb("#f59e0b"));
    p.add("AMBER_600", rgb("#d97706"));
    p.add("AMBER_700", rgb("#b45309"));
    p.add("AMBER_800", rgb("#92400e"));
    p.add("AMBER_900", rgb("#78350f"));

    p.add("YELLOW_050", rgb("#fefce8"));
    p.add("YELLOW_100", rgb("#fef9c3"));
    p.add("YELLOW_200", rgb("#fef08a"));
    p.add("YELLOW_300", rgb("#fde047"));
    p.add("YELLOW_400", rgb("#facc15"));
    p.add("YELLOW_500", rgb("#eab308"));
    p.add("YELLOW_600", rgb("#ca8a04"));
    p.add("YELLOW_700", rgb("#a16207"));
    p.add("YELLOW_800", rgb("#854d0e"));
    p.add("YELLOW_900", rgb("#713f12"));

    p.add("LIME_050", rgb("#f7fee7"));
    p.add("LIME_100", rgb("#ecfccb"));
    p.add("LIME_200", rgb("#d9f99d"));
    p.add("LIME_300", rgb("#bef264"));
    p.add("LIME_400", rgb("#a3e635"));
    p.add("LIME_500", rgb("#84cc16"));
    p.add("LIME_600", rgb("#65a30d"));
    p.add("LIME_700", rgb("#4d7c0f"));
    p.add("LIME_800", rgb("#3f6212"));
    p.add("LIME_900", rgb("#365314"));

    p.add("GREEN_050", rgb("#f0fdf4"));
    p.add("GREEN_100", rgb("#dcfce7"));
    p.add("GREEN_200", rgb("#bbf7d0"));
    p.add("GREEN_300", rgb("#86efac"));
    p.add("GREEN_400", rgb("#4ade80"));
    p.add("GREEN_500", rgb("#22c55e"));
    p.add("GREEN_600", rgb("#16a34a"));
    p.add("GREEN_700", rgb("#15803d"));
    p.add("GREEN_800", rgb("#166534"));
    p.add("GREEN_900", rgb("#14532d"));

    p.add("EMERALD_050", rgb("#ecfdf5"));
    p.add("EMERALD_100", rgb("#d1fae5"));
    p.add("EMERALD_200", rgb("#a7f3d0"));
    p.add("EMERALD_300", rgb("#6ee7b7"));
    p.add("EMERALD_400", rgb("#34d399"));
    p.add("EMERALD_500", rgb("#10b981"));
    p.add("EMERALD_600", rgb("#059669"));
    p.add("EMERALD_700", rgb("#047857"));
    p.add("EMERALD_800", rgb("#065f46"));
    p.add("EMERALD_900", rgb("#064e3b"));

    p.add("TEAL_050", rgb("#f0fdfa"));
    p.add("TEAL_100", rgb("#ccfbf1"));
    p.add("TEAL_200", rgb("#99f6e4"));
    p.add("TEAL_300", rgb("#5eead4"));
    p.add("TEAL_400", rgb("#2dd4bf"));
    p.add("TEAL_500", rgb("#14b8a6"));
    p.add("TEAL_600", rgb("#0d9488"));
    p.add("TEAL_700", rgb("#0f766e"));
    p.add("TEAL_800", rgb("#115e59"));
    p.add("TEAL_900", rgb("#134e4a"));

    p.add("CYAN_050", rgb("#ecfeff"));
    p.add("CYAN_100", rgb("#cffafe"));
    p.add("CYAN_200", rgb("#a5f3fc"));
    p.add("CYAN_300", rgb("#67e8f9"));
    p.add("CYAN_400", rgb("#22d3ee"));
    p.add("CYAN_500", rgb("#06b6d4"));
    p.add("CYAN_600", rgb("#0891b2"));
    p.add("CYAN_700", rgb("#0e7490"));
    p.add("CYAN_800", rgb("#155e75"));
    p.add("CYAN_900", rgb("#164e63"));

    p.add("SKY_050", rgb("#f0f9ff"));
    p.add("SKY_100", rgb("#e0f2fe"));
    p.add("SKY_200", rgb("#bae6fd"));
    p.add("SKY_300", rgb("#7dd3fc"));
    p.add("SKY_400", rgb("#38bdf8"));
    p.add("SKY_500", rgb("#0ea5e9"));
    p.add("SKY_600", rgb("#0284c7"));
    p.add("SKY_700", rgb("#0369a1"));
    p.add("SKY_800", rgb("#075985"));
    p.add("SKY_900", rgb("#0c4a6e"));

    p.add("BLUE_050", rgb("#eff6ff"));
    p.add("BLUE_100", rgb("#dbeafe"));
    p.add("BLUE_200", rgb("#bfdbfe"));
    p.add("BLUE_300", rgb("#93c5fd"));
    p.add("BLUE_400", rgb("#60a5fa"));
    p.add("BLUE_500", rgb("#3b82f6"));
    p.add("BLUE_600", rgb("#2563eb"));
    p.add("BLUE_700", rgb("#1d4ed8"));
    p.add("BLUE_800", rgb("#1e40af"));
    p.add("BLUE_900", rgb("#1e3a8a"));

    p.add("INDIGO_050", rgb("#eef2ff"));
    p.add("INDIGO_100", rgb("#e0e7ff"));
    p.add("INDIGO_200", rgb("#c7d2fe"));
    p.add("INDIGO_300", rgb("#a5b4fc"));
    p.add("INDIGO_400", rgb("#818cf8"));
    p.add("INDIGO_500", rgb("#6366f1"));
    p.add("INDIGO_600", rgb("#4f46e5"));
    p.add("INDIGO_700", rgb("#4338ca"));
    p.add("INDIGO_800", rgb("#3730a3"));
    p.add("INDIGO_900", rgb("#312e81"));

    p.add("VIOLET_050", rgb("#f5f3ff"));
    p.add("VIOLET_100", rgb("#ede9fe"));
    p.add("VIOLET_200", rgb("#ddd6fe"));
    p.add("VIOLET_300", rgb("#c4b5fd"));
    p.add("VIOLET_400", rgb("#a78bfa"));
    p.add("VIOLET_500", rgb("#8b5cf6"));
    p.add("VIOLET_600", rgb("#7c3aed"));
    p.add("VIOLET_700", rgb("#6d28d9"));
    p.add("VIOLET_800", rgb("#5b21b6"));
    p.add("VIOLET_900", rgb("#4c1d95"));

    p.add("PURPLE_050", rgb("#faf5ff"));
    p.add("PURPLE_100", rgb("#f3e8ff"));
    p.add("PURPLE_200", rgb("#e9d5ff"));
    p.add("PURPLE_300", rgb("#d8b4fe"));
    p.add("PURPLE_400", rgb("#c084fc"));
    p.add("PURPLE_500", rgb("#a855f7"));
    p.add("PURPLE_600", rgb("#9333ea"));
    p.add("PURPLE_700", rgb("#7e22ce"));
    p.add("PURPLE_800", rgb("#6b21a8"));
    p.add("PURPLE_900", rgb("#581c87"));

    p.add("FUCHSIA_050", rgb("#fdf4ff"));
    p.add("FUCHSIA_100", rgb("#fae8ff"));
    p.add("FUCHSIA_200", rgb("#f5d0fe"));
    p.add("FUCHSIA_300", rgb("#f0abfc"));
    p.add("FUCHSIA_400", rgb("#e879f9"));
    p.add("FUCHSIA_500", rgb("#d946ef"));
    p.add("FUCHSIA_600", rgb("#c026d3"));
    p.add("FUCHSIA_700", rgb("#a21caf"));
    p.add("FUCHSIA_800", rgb("#86198f"));
    p.add("FUCHSIA_900", rgb("#701a75"));

    p.add("PINK_050", rgb("#fdf2f8"));
    p.add("PINK_100", rgb("#fce7f3"));
    p.add("PINK_200", rgb("#fbcfe8"));
    p.add("PINK_300", rgb("#f9a8d4"));
    p.add("PINK_400", rgb("#f472b6"));
    p.add("PINK_500", rgb("#ec4899"));
    p.add("PINK_600", rgb("#db2777"));
    p.add("PINK_700", rgb("#be185d"));
    p.add("PINK_800", rgb("#9d174d"));
    p.add("PINK_900", rgb("#831843"));

    p.add("ROSE_050", rgb("#fff1f2"));
    p.add("ROSE_100", rgb("#ffe4e6"));
    p.add("ROSE_200", rgb("#fecdd3"));
    p.add("ROSE_300", rgb("#fda4af"));
    p.add("ROSE_400", rgb("#fb7185"));
    p.add("ROSE_500", rgb("#f43f5e"));
    p.add("ROSE_600", rgb("#e11d48"));
    p.add("ROSE_700", rgb("#be123c"));
    p.add("ROSE_800", rgb("#9f1239"));
    p.add("ROSE_900", rgb("#881337"));
  }

  private String rgb(String hex) {
    int r;
    r = rgb0(hex.charAt(1), hex.charAt(2));

    int g;
    g = rgb0(hex.charAt(3), hex.charAt(4));

    int b;
    b = rgb0(hex.charAt(5), hex.charAt(6));

    return "rgb(" + r + " " + g + " " + b + ")";
  }

  private int rgb0(char h, char l) {
    int high;
    high = Character.digit(h, 16);

    int low;
    low = Character.digit(l, 16);

    return (high << 4) | low;
  }

  private void spacing(PropertyClass p) {
    p.add("PX0", "0px");
    p.add("PX1", "1px");
    p.add("PX2", "0.125rem");
    p.add("PX4", "0.25rem");
    p.add("PX6", "0.375rem");
    p.add("PX8", "0.5rem");
    p.add("PX10", "0.625rem");
    p.add("PX12", "0.75rem");
    p.add("PX14", "0.875rem");
    p.add("PX16", "1rem");
    p.add("PX20", "1.25rem");
    p.add("PX24", "1.5rem");
    p.add("PX28", "1.75rem");
    p.add("PX32", "2rem");
    p.add("PX36", "2.25rem");
    p.add("PX40", "2.5rem");
    p.add("PX44", "2.75rem");
    p.add("PX48", "3rem");
    p.add("PX56", "3.5rem");
    p.add("PX64", "4rem");
    p.add("PX80", "5rem");
    p.add("PX96", "6rem");
    p.add("PX112", "7rem");
    p.add("PX128", "8rem");
    p.add("PX144", "9rem");
    p.add("PX160", "10rem");
    p.add("PX176", "11rem");
    p.add("PX192", "12rem");
    p.add("PX208", "13rem");
    p.add("PX224", "14rem");
    p.add("PX240", "15rem");
    p.add("PX256", "16rem");
    p.add("PX288", "18rem");
    p.add("PX320", "20rem");
    p.add("PX384", "24rem");
  }

}