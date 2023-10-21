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
import selfgen.css.util.AllButFirst;
import selfgen.css.util.CssUtilSelfGen;
import selfgen.css.util.Names;
import selfgen.css.util.Prefix;
import selfgen.css.util.Property1;
import selfgen.css.util.Property2;
import selfgen.css.util.Property2x2;
import selfgen.css.util.PropertyClass;

final class CssUtilSpec extends CssUtilSelfGen {

  private final Set<Prefix> responsive = Prefix.RESPONSIVE;

  public static void main(String[] args) throws IOException {
    CssUtilSpec spec;
    spec = new CssUtilSpec();

    spec.execute(args);
  }

  @Override
  protected final void definition() {
    // A

    alignContent();
    alignItems();
    alignSelf();

    // B

    backgroundColor();
    borderColor();
    borderRadius();
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

    // O
    objectFit();
    opacity();

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

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void backgroundColor() {
    Property1 p;
    p = new Property1("BackgroundColor", "background-color");

    colors(p);

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

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }

      Prefix.HOVER.add(p);
    }
  }

  private void borderRadius() {
    // @formatter:off
    List<PropertyClass> props;
    props = List.of(
      new Property1("BorderRadius", "border-radius"),
      new Property2("BorderTopRadius", "border-top-left-radius", "border-top-right-radius"),
      new Property2("BorderRightRadius", "border-top-right-radius", "border-bottom-right-radius"),
      new Property2("BorderBottomRadius", "border-bottom-left-radius", "border-bottom-right-radius"),
      new Property2("BorderLeftRadius", "border-top-left-radius", "border-bottom-left-radius")
    );
    // @formatter:on

    for (PropertyClass p : props) {
      p.add("PX0", "0px");
      p.add("PX2", "0.125rem");
      p.add("PX4", "0.25rem");
      p.add("PX6", "0.375rem");
      p.add("PX8", "0.5rem");
      p.add("PX12", "0.75rem");
      p.add("PX16", "1rem");
      p.add("PX24", "1.5rem");
      p.add("FULL", "9999px");

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

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void color() {
    Property1 p;
    p = new Property1("TextColor", "color");

    colors(p);

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }

    Prefix.HOVER.add(p);
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

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }

    Prefix.HOVER.add(p);
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

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void flexGrow() {
    Property1 p;
    p = new Property1("FlexGrow", "flex-grow");

    p.add("V1", "1");
    p.add("V0", "0");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void fontSize() {
    Property2x2 p;
    p = new Property2x2("FontSize", "font-size", "line-height");

    p.add("XS", "0.75rem", "1rem");
    p.add("SM", "0.875rem", "1.25rem");
    p.add("BASE", "1rem", "1.5rem");
    p.add("LG", "1.125rem", "1.75rem");
    p.add("XL", "1.25rem", "1.75rem");
    p.add("XL2", "1.5rem", "2rem");
    p.add("XL3", "1.875rem", "2.25rem");
    p.add("XL4", "2.25rem", "2.5rem");
    p.add("XL5", "3rem", "1");
    p.add("XL6", "3.75rem", "1");
    p.add("XL7", "4.5rem", "1");
    p.add("XL8", "6rem", "1");
    p.add("XL9", "8rem", "1");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void fontStyle() {
    Property1 p;
    p = new Property1("FontStyle", "font-style");

    p.add("ITALIC", "italic");
    p.add("NORMAL", "normal");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void fontWeight() {
    Property1 p;
    p = new Property1("FontWeight", "font-weight");

    p.add("THIN", "100");
    p.add("EXTRALIGHT", "200");
    p.add("LIGHT", "300");
    p.add("NORMAL", "400");
    p.add("MEDIUM", "500");
    p.add("SEMIBOLD", "600");
    p.add("BOLD", "700");
    p.add("EXTRABOLD", "800");
    p.add("BLACK", "900");

    for (Prefix prefix : responsive) {
      prefix.add(p);
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

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void lineHeight() {
    Property1 p;
    p = new Property1("LineHeight", "line-height");

    p.add("NONE", "1");
    p.add("TIGHT", "1.25");
    p.add("SNUG", "1.375");
    p.add("NORMAL", "1.5");
    p.add("RELAXED", "1.625");
    p.add("LOOSE", "2");
    p.add("PX12", "0.75rem");
    p.add("PX16", "1rem");
    p.add("PX20", "1.25rem");
    p.add("PX24", "1.5rem");
    p.add("PX28", "1.75rem");
    p.add("PX32", "2rem");
    p.add("PX36", "2.25rem");
    p.add("PX40", "2.5rem");

    for (Prefix prefix : responsive) {
      prefix.add(p);
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
    List<PropertyClass> props;
    props = List.of(
        new Property1("Margin", "margin"),
        new Property1("MarginTop", "margin-top"),
        new Property1("MarginRight", "margin-right"),
        new Property1("MarginBottom", "margin-bottom"),
        new Property1("MarginLeft", "margin-left"),

        new Property2("MarginX", "margin-right", "margin-left"),
        new Property2("MarginY", "margin-top", "margin-bottom")
    );

    for (var p : props) {
      spacing(p);

      p.add("AUTO", "auto");

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void maxWidth() {
    Property1 p;
    p = new Property1("MaxWidth", "max-width");

    p.add("PX0", "0px");
    p.add("NONE", "none");
    p.add("PX320", "20rem");
    p.add("PX384", "24rem");
    p.add("PX448", "28rem");
    p.add("PX512", "32rem");
    p.add("PX576", "36rem");
    p.add("PX672", "42rem");
    p.add("PX768", "48rem");
    p.add("PX896", "56rem");
    p.add("PX1024", "64rem");
    p.add("PX1152", "72rem");
    p.add("PX1280", "80rem");
    p.add("FULL", "100%");
    p.add("MIN", "min-content");
    p.add("MAX", "max-content");
    p.add("FIT", "fit-content");
    p.add("PROSE", "65ch");
    p.add("SCREEN_SMALL", "640px");
    p.add("SCREEN_MEDIUM", "768px");
    p.add("SCREEN_LARGE", "1024px");
    p.add("SCREEN_EXTRA", "1280px");
    p.add("SCREEN_MAX", "1536px");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void minHeight() {
    Property1 p;
    p = new Property1("MinHeight", "min-height");

    p.add("PX0", "0px");
    p.add("FULL", "100%");
    p.add("SCREEN", "100vh");
    p.add("MIN", "min-content");
    p.add("MAX", "max-content");
    p.add("FIT", "fit-content");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void objectFit() {
    Property1 p;
    p = new Property1("ObjectFit", "object-fit");

    p.add("CONTAIN", "contain");
    p.add("COVER", "cover");
    p.add("FILL", "fill");
    p.add("NONE", "none");
    p.add("SCALE_DOWN", "scale-down");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void opacity() {
    Property1 p;
    p = new Property1("Opacity", "opacity");

    p.add("P0", "0");
    p.add("P5", "0.05");
    p.add("P10", "0.1");
    p.add("P20", "0.2");
    p.add("P30", "0.3");
    p.add("P40", "0.4");
    p.add("P50", "0.5");
    p.add("P60", "0.6");
    p.add("P70", "0.7");
    p.add("P80", "0.8");
    p.add("P90", "0.9");
    p.add("P95", "0.95");
    p.add("P100", "1");

    for (Prefix prefix : responsive) {
      prefix.add(p);
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

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void space() {
    List<PropertyClass> props;
    props = List.of(
        new AllButFirst("SpaceX", "margin-left"),
        new AllButFirst("SpaceY", "margin-top")
    );

    for (var p : props) {
      spacing(p);

      for (Prefix prefix : responsive) {
        prefix.add(p);
      }
    }
  }

  private void textAlign() {
    Property1 p;
    p = new Property1("TextAlign", "text-align");

    p.add("LEFT", "left");
    p.add("CENTER", "center");
    p.add("RIGHT", "right");
    p.add("JUSTIFY", "justify");
    p.add("START", "start");
    p.add("END", "end");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }
  }

  private void textDecoration() {
    Property1 p;
    p = new Property1("TextDecoration", "text-decoration");

    p.add("UNDERLINE", "underline");
    p.add("OVERLINE", "overline");
    p.add("LINE_THROUGH", "line-through");
    p.add("NONE", "none");

    for (Prefix prefix : responsive) {
      prefix.add(p);
    }

    Prefix.HOVER.add(p);
  }

  private void textTransform() {
    Property1 p;
    p = new Property1("TextTransform", "text-transform");

    p.add("UPPERCASE", "uppercase");
    p.add("LOWERCASE", "lowercase");
    p.add("CAPITALIZE", "capitalize");
    p.add("NONE", "none");

    for (Prefix prefix : responsive) {
      prefix.add(p);
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