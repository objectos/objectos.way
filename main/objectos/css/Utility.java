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

  static final Utility FLEX_DIRECTION = new Single("flex-direction");
  static final Utility ALIGN_ITEMS = new Single("align-items");
  static final Utility JUSTIFY_CONTENT = new Single("justify-content");

  static final Utility BORDER_WIDTH = new Single("border-width");
  static final Utility BORDER_WIDTH_X = new Axis("border-left-width", "border-right-width");
  static final Utility BORDER_WIDTH_Y = new Axis("border-top-width", "border-bottom-width");
  static final Utility BORDER_WIDTH_TOP = new Single("border-top-width");
  static final Utility BORDER_WIDTH_RIGHT = new Single("border-right-width");
  static final Utility BORDER_WIDTH_BOTTOM = new Single("border-bottom-width");
  static final Utility BORDER_WIDTH_LEFT = new Single("border-left-width");

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

  static final Utility FONT_SIZE = new Duo("font-size", "line-height");

  static final Utility LINE_HEIGHT = new Single("line-height");

  static final Utility LETTER_SPACING = new Single("letter-spacing");

  static final Utility TEXT_COLOR = new Single("color");

  // all instances are created in this class
  private static int COUNTER;

  final int index = COUNTER++;

  Utility() {}

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

}