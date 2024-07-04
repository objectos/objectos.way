/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.List;

sealed abstract class CssUtility {

  // order is important.

  static final CssUtility CUSTOM = new MultiLine();

  static final CssUtility ACCESSIBILITY = new MultiLine();

  // 672
  static final CssUtility POINTER_EVENTS = new Single("pointer-events");
  static final CssUtility VISIBILITY = new Single("visibility");

  // 687
  static final CssUtility POSITION = new Single("position");

  static final CssUtility INSET = new Single("inset");
  static final CssUtility INSET_X = new Axis("left", "right");
  static final CssUtility INSET_Y = new Axis("top", "bottom");
  static final CssUtility START = new Single("inset-inline-start");
  static final CssUtility END = new Single("inset-inline-start");
  static final CssUtility TOP = new Single("top");
  static final CssUtility RIGHT = new Single("right");
  static final CssUtility BOTTOM = new Single("bottom");
  static final CssUtility LEFT = new Single("left");

  static final CssUtility Z_INDEX = new Single("z-index");

  static final CssUtility GRID_COLUMN = new Single("grid-column");
  static final CssUtility GRID_COLUMN_START = new Single("grid-column-start");
  static final CssUtility GRID_COLUMN_END = new Single("grid-column-end");

  static final CssUtility MARGIN = new Single("margin");
  static final CssUtility MARGIN_X = new Axis("margin-left", "margin-right");
  static final CssUtility MARGIN_Y = new Axis("margin-top", "margin-bottom");
  static final CssUtility MARGIN_TOP = new Single("margin-top");
  static final CssUtility MARGIN_RIGHT = new Single("margin-right");
  static final CssUtility MARGIN_BOTTOM = new Single("margin-bottom");
  static final CssUtility MARGIN_LEFT = new Single("margin-left");

  static final CssUtility DISPLAY = new Single("display");

  // 834
  static final CssUtility SIZE = new Axis("height", "width");
  static final CssUtility HEIGHT = new Single("height");
  static final CssUtility WIDTH = new Single("width");
  static final CssUtility MIN_WIDTH = new Single("min-width");
  static final CssUtility MAX_WIDTH = new Single("max-width");

  static final CssUtility FLEX_GROW = new Single("flex-grow");

  // 878
  static final CssUtility TABLE_LAYOUT = new Single("table-layout");

  static final CssUtility BORDER_COLLAPSE = new Single("border-collapse");
  static final CssUtility BORDER_SPACING = new Single("border-spacing");

  // 1025
  static final CssUtility CURSOR = new Single("cursor");

  // 1078
  static final CssUtility USER_SELECT = new Single("user-select");

  static final CssUtility GRID_TEMPLATE_COLUMNS = new Single("grid-template-columns");
  static final CssUtility GRID_TEMPLATE_ROWS = new Single("grid-template-rows");

  // 1243
  static final CssUtility FLEX_DIRECTION = new Single("flex-direction");
  static final CssUtility ALIGN_ITEMS = new Single("align-items");
  static final CssUtility JUSTIFY_CONTENT = new Single("justify-content");
  static final CssUtility GAP = new Single("gap");
  static final CssUtility GAP_X = new Single("column-gap");
  static final CssUtility GAP_Y = new Single("row-gap");

  // 1495
  static final CssUtility OVERFLOW = new Single("overflow");
  static final CssUtility OVERFLOW_X = new Single("overflow-x");
  static final CssUtility OVERFLOW_Y = new Single("overflow-y");

  static final CssUtility TEXT_WRAP = new Single("text-wrap");

  // 1607
  static final CssUtility BORDER_RADIUS = new Single("border-radius");
  static final CssUtility BORDER_RADIUS_S = new Axis("border-start-start-radius", "border-end-start-radius");
  static final CssUtility BORDER_RADIUS_E = new Axis("border-start-end-radius", "border-end-end-radius");
  static final CssUtility BORDER_RADIUS_T = new Axis("border-top-left-radius", "border-top-right-radius");
  static final CssUtility BORDER_RADIUS_R = new Axis("border-top-right-radius", "border-bottom-right-radius");
  static final CssUtility BORDER_RADIUS_B = new Axis("border-bottom-right-radius", "border-bottom-left-radius");
  static final CssUtility BORDER_RADIUS_L = new Axis("border-top-left-radius", "border-bottom-left-radius");
  static final CssUtility BORDER_RADIUS_SS = new Single("border-start-start-radius");
  static final CssUtility BORDER_RADIUS_SE = new Single("border-start-end-radius");
  static final CssUtility BORDER_RADIUS_EE = new Single("border-end-end-radius");
  static final CssUtility BORDER_RADIUS_ES = new Single("border-end-start-radius");
  static final CssUtility BORDER_RADIUS_TL = new Single("border-top-left-radius");
  static final CssUtility BORDER_RADIUS_TR = new Single("border-top-right-radius");
  static final CssUtility BORDER_RADIUS_BR = new Single("border-bottom-right-radius");
  static final CssUtility BORDER_RADIUS_BL = new Single("border-bottom-left-radius");

  // 1629
  static final CssUtility BORDER_WIDTH = new Single("border-width");
  static final CssUtility BORDER_WIDTH_X = new Axis("border-left-width", "border-right-width");
  static final CssUtility BORDER_WIDTH_Y = new Axis("border-top-width", "border-bottom-width");
  static final CssUtility BORDER_WIDTH_TOP = new Single("border-top-width");
  static final CssUtility BORDER_WIDTH_RIGHT = new Single("border-right-width");
  static final CssUtility BORDER_WIDTH_BOTTOM = new Single("border-bottom-width");
  static final CssUtility BORDER_WIDTH_LEFT = new Single("border-left-width");

  // 1660
  static final CssUtility BORDER_COLOR = new Single("border-color");
  static final CssUtility BORDER_COLOR_X = new Axis("border-left-color", "border-right-color");
  static final CssUtility BORDER_COLOR_Y = new Axis("border-top-color", "border-bottom-color");
  static final CssUtility BORDER_COLOR_TOP = new Single("border-top-color");
  static final CssUtility BORDER_COLOR_RIGHT = new Single("border-right-color");
  static final CssUtility BORDER_COLOR_BOTTOM = new Single("border-bottom-color");
  static final CssUtility BORDER_COLOR_LEFT = new Single("border-left-color");

  static final CssUtility BACKGROUND_COLOR = new Single("background-color");

  static final CssUtility FILL = new Single("fill");
  static final CssUtility STROKE = new Single("stroke");
  static final CssUtility STROKE_WIDTH = new Single("stroke-width");

  static final CssUtility PADDING = new Single("padding");
  static final CssUtility PADDING_X = new Axis("padding-left", "padding-right");
  static final CssUtility PADDING_Y = new Axis("padding-top", "padding-bottom");
  static final CssUtility PADDING_TOP = new Single("padding-top");
  static final CssUtility PADDING_RIGHT = new Single("padding-right");
  static final CssUtility PADDING_BOTTOM = new Single("padding-bottom");
  static final CssUtility PADDING_LEFT = new Single("padding-left");

  static final CssUtility TEXT_ALIGN = new Single("text-align");
  static final CssUtility VERTICAL_ALIGN = new Single("vertical-align");

  static final CssUtility FONT_SIZE1 = new Single("font-size");
  static final CssUtility.Duo FONT_SIZE2 = new Duo("font-size", "line-height");
  static final CssUtility.Three FONT_SIZE3 = new Three("font-size", "line-height", "letter-spacing");
  static final CssUtility.Four FONT_SIZE4 = new Four("font-size", "line-height", "letter-spacing", "font-weight");
  static final CssUtility FONT_WEIGHT = new Single("font-weight");

  static final CssUtility LINE_HEIGHT = new Single("line-height");

  static final CssUtility LETTER_SPACING = new Single("letter-spacing");

  static final CssUtility TEXT_COLOR = new Single("color");
  static final CssUtility TEXT_DECORATION = new Single("text-decoration-line");

  static final CssUtility OPACITY = new Single("opacity");

  static final CssUtility OUTLINE_STYLE = new Single("outline-style");
  static final CssUtility OUTLINE_STYLE_NONE = new SingleLine(OUTLINE_STYLE.index);
  static final CssUtility OUTLINE_WIDTH = new Single("outline-width");
  static final CssUtility OUTLINE_OFFSET = new Single("outline-offset");
  static final CssUtility OUTLINE_COLOR = new Single("outline-color");

  static final CssUtility TRANSITION_PROPERTY = new MultiLine();
  static final CssUtility TRANSITION_DURATION = new Single("transition-duration");

  static final CssUtility CONTENT = new Single("content");

  // all instances are created in this class
  private static int COUNTER;

  final int index;

  CssUtility() {
    index = COUNTER++;
  }

  CssUtility(int index) {
    this.index = index;
  }

  CssRule get(String className, List<CssVariant> variants, String value) {
    throw new UnsupportedOperationException();
  }

  private static final class Single extends CssUtility {

    private final String property;

    private Single(String property) {
      this.property = property;
    }

    @Override
    final CssRule get(String className, List<CssVariant> variants, String value) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property, value);
        }
      };
    }

  }

  private static final class Axis extends CssUtility {

    private final String property1;

    private final String property2;

    private Axis(String property1, String property2) {
      this.property1 = property1;

      this.property2 = property2;
    }

    @Override
    final CssRule get(String className, List<CssVariant> variants, String value) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property1, value);

          out.append("; ");

          writePropertyValue(out, property2, value);
        }
      };
    }

  }

  static final class Duo extends CssUtility {

    private final String property1;

    private final String property2;

    public Duo(String property1, String property2) {
      this.property1 = property1;

      this.property2 = property2;
    }

    final CssRule get(String className, List<CssVariant> variants, String value1, String value2) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property1, value1);

          out.append("; ");

          writePropertyValue(out, property2, value2);
        }
      };
    }

  }

  static final class Three extends CssUtility {

    private final String property1;

    private final String property2;

    private final String property3;

    public Three(String property1, String property2, String property3) {
      this.property1 = property1;
      this.property2 = property2;
      this.property3 = property3;
    }

    final CssRule get(String className, List<CssVariant> variants, String value1, String value2, String value3) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, CssIndentation indentation) {
          CssIndentation next;
          next = indentation.increase();

          out.append(" {");

          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property1, value1);

          out.append(";");
          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property2, value2);

          out.append(";");
          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property3, value3);

          out.append(";");
          out.append(System.lineSeparator());

          indentation.writeTo(out);

          out.append('}');
        }
      };
    }

  }

  static final class Four extends CssUtility {

    private final String property1;

    private final String property2;

    private final String property3;

    private final String property4;

    public Four(String property1, String property2, String property3, String property4) {
      this.property1 = property1;
      this.property2 = property2;
      this.property3 = property3;
      this.property4 = property4;
    }

    final CssRule get(String className, List<CssVariant> variants, String value1, String value2, String value3, String value4) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, CssIndentation indentation) {
          CssIndentation next;
          next = indentation.increase();

          out.append(" {");

          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property1, value1);

          out.append(";");
          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property2, value2);

          out.append(";");
          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property3, value3);

          out.append(";");
          out.append(System.lineSeparator());

          next.writeTo(out);
          writePropertyValue(out, property4, value4);

          out.append(";");
          out.append(System.lineSeparator());

          indentation.writeTo(out);

          out.append('}');
        }
      };
    }

  }

  private static final class MultiLine extends CssUtility {
    @Override
    final CssRule get(String className, List<CssVariant> variants, String value) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, CssIndentation indentation) {
          out.append(" {");

          out.append(System.lineSeparator());

          CssIndentation next;
          next = indentation.increase();

          out.append(next.indent(value));

          indentation.writeTo(out);

          out.append('}');
        }
      };
    }
  }

  private static final class SingleLine extends CssUtility {
    public SingleLine(int index) {
      super(index);
    }

    @Override
    final CssRule get(String className, List<CssVariant> variants, String value) {
      return new CssRule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, CssIndentation indentation) {
          out.append(" { ");

          out.append(value);

          out.append(" }");
        }
      };
    }
  }

}