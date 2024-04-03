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
package objectos.css;

import java.util.List;

sealed abstract class Utility {

  // order is important.

  static final Utility CUSTOM = new MultiLine();

  static final Utility ACCESSIBILITY = new MultiLine();

  static final Utility POSITION = new Single("position");

  static final Utility INSET = new Single("inset");
  static final Utility INSET_X = new Axis("left", "right");
  static final Utility INSET_Y = new Axis("top", "bottom");
  static final Utility START = new Single("inset-inline-start");
  static final Utility END = new Single("inset-inline-start");
  static final Utility TOP = new Single("top");
  static final Utility RIGHT = new Single("right");
  static final Utility BOTTOM = new Single("bottom");
  static final Utility LEFT = new Single("left");

  static final Utility Z_INDEX = new Single("z-index");

  static final Utility MARGIN = new Single("margin");
  static final Utility MARGIN_X = new Axis("margin-left", "margin-right");
  static final Utility MARGIN_Y = new Axis("margin-top", "margin-bottom");
  static final Utility MARGIN_TOP = new Single("margin-top");
  static final Utility MARGIN_RIGHT = new Single("margin-right");
  static final Utility MARGIN_BOTTOM = new Single("margin-bottom");
  static final Utility MARGIN_LEFT = new Single("margin-left");

  static final Utility DISPLAY = new Single("display");

  static final Utility HEIGHT = new Single("height");
  static final Utility WIDTH = new Single("width");
  
  static final Utility BORDER_COLLAPSE = new Single("border-collapse");
  static final Utility BORDER_SPACING = new Single("border-spacing");

  // 1025
  static final Utility CURSOR = new Single("cursor");
  
  // 1078
  static final Utility USER_SELECT = new Single("user-select");

  static final Utility FLEX_DIRECTION = new Single("flex-direction");
  static final Utility ALIGN_ITEMS = new Single("align-items");
  static final Utility JUSTIFY_CONTENT = new Single("justify-content");

  // 1495
  static final Utility OVERFLOW = new Single("overflow");
  static final Utility OVERFLOW_X = new Single("overflow-x");
  static final Utility OVERFLOW_Y = new Single("overflow-y");
  
  // 1604
  static final Utility BORDER_WIDTH = new Single("border-width");
  static final Utility BORDER_WIDTH_X = new Axis("border-left-width", "border-right-width");
  static final Utility BORDER_WIDTH_Y = new Axis("border-top-width", "border-bottom-width");
  static final Utility BORDER_WIDTH_TOP = new Single("border-top-width");
  static final Utility BORDER_WIDTH_RIGHT = new Single("border-right-width");
  static final Utility BORDER_WIDTH_BOTTOM = new Single("border-bottom-width");
  static final Utility BORDER_WIDTH_LEFT = new Single("border-left-width");

  // 1635
  static final Utility BORDER_COLOR = new Single("border-color");
  static final Utility BORDER_COLOR_X = new Axis("border-left-color", "border-right-color");
  static final Utility BORDER_COLOR_Y = new Axis("border-top-color", "border-bottom-color");
  static final Utility BORDER_COLOR_TOP = new Single("border-top-color");
  static final Utility BORDER_COLOR_RIGHT = new Single("border-right-color");
  static final Utility BORDER_COLOR_BOTTOM = new Single("border-bottom-color");
  static final Utility BORDER_COLOR_LEFT = new Single("border-left-color");

  static final Utility BACKGROUND_COLOR = new Single("background-color");

  static final Utility FILL = new Single("fill");

  static final Utility PADDING = new Single("padding");
  static final Utility PADDING_X = new Axis("padding-left", "padding-right");
  static final Utility PADDING_Y = new Axis("padding-top", "padding-bottom");
  static final Utility PADDING_TOP = new Single("padding-top");
  static final Utility PADDING_RIGHT = new Single("padding-right");
  static final Utility PADDING_BOTTOM = new Single("padding-bottom");
  static final Utility PADDING_LEFT = new Single("padding-left");

  static final Utility TEXT_ALIGN = new Single("text-align");
  static final Utility VERTICAL_ALIGN = new Single("vertical-align");

  static final Utility FONT_SIZE1 = new Single("font-size");
  static final Utility FONT_SIZE2 = new Duo("font-size", "line-height");
  static final Utility FONT_SIZEX = new MultiLine();
  static final Utility FONT_WEIGHT = new Single("font-weight");

  static final Utility LINE_HEIGHT = new Single("line-height");

  static final Utility LETTER_SPACING = new Single("letter-spacing");

  static final Utility TEXT_COLOR = new Single("color");
  static final Utility TEXT_DECORATION = new Single("text-decoration-line");

  static final Utility OPACITY = new Single("opacity");

  static final Utility OUTLINE_STYLE = new Single("outline-style");
  static final Utility OUTLINE_STYLE_NONE = new SingleLine(OUTLINE_STYLE.index);
  static final Utility OUTLINE_WIDTH = new Single("outline-width");
  static final Utility OUTLINE_OFFSET = new Single("outline-offset");
  static final Utility OUTLINE_COLOR = new Single("outline-color");

  static final Utility TRANSITION_PROPERTY = new MultiLine();
  static final Utility TRANSITION_DURATION = new Single("transition-duration");

  static final Utility CONTENT = new Single("content");

  // all instances are created in this class
  private static int COUNTER;

  final int index;

  Utility() {
    index = COUNTER++;
  }

  Utility(int index) {
    this.index = index;
  }

  Rule get(String className, List<Variant> variants, String value) {
    throw new UnsupportedOperationException();
  }

  Rule get(String className, List<Variant> variants, String value1, String value2) {
    throw new UnsupportedOperationException();
  }

  private static final class Single extends Utility {

    private final String property;

    private Single(String property) {
      this.property = property;
    }

    @Override
    final Rule get(String className, List<Variant> variants, String value) {
      return new Rule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property, value);
        }
      };
    }

  }

  private static final class Axis extends Utility {

    private final String property1;

    private final String property2;

    private Axis(String property1, String property2) {
      this.property1 = property1;

      this.property2 = property2;
    }

    @Override
    final Rule get(String className, List<Variant> variants, String value) {
      return new Rule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property1, value);

          out.append("; ");

          writePropertyValue(out, property2, value);
        }
      };
    }

  }

  private static final class Duo extends Utility {

    private final String property1;

    private final String property2;

    public Duo(String property1, String property2) {
      this.property1 = property1;

      this.property2 = property2;
    }

    @Override
    final Rule get(String className, List<Variant> variants, String value1, String value2) {
      return new Rule(index, className, variants) {
        @Override
        final void writeProperties(StringBuilder out) {
          writePropertyValue(out, property1, value1);

          out.append("; ");

          writePropertyValue(out, property2, value2);
        }
      };
    }

  }

  private static final class MultiLine extends Utility {
    @Override
    final Rule get(String className, List<Variant> variants, String value) {
      return new Rule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, Indentation indentation) {
          out.append(" {");

          out.append(System.lineSeparator());

          Indentation next;
          next = indentation.increase();

          out.append(next.indent(value));

          indentation.writeTo(out);

          out.append('}');
        }
      };
    }
  }

  private static final class SingleLine extends Utility {
    public SingleLine(int index) {
      super(index);
    }

    @Override
    final Rule get(String className, List<Variant> variants, String value) {
      return new Rule(index, className, variants) {
        @Override
        final void writeBlock(StringBuilder out, Indentation indentation) {
          out.append(" { ");

          out.append(value);

          out.append(" }");
        }
      };
    }
  }

}