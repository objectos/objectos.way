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

enum Utility {

  NOOP(""),

  // order is important.

  MARGIN("margin"),
  MARGIN_X("margin-left", "margin-right"),
  MARGIN_Y("margin-top", "margin-bottom"),
  MARGIN_TOP("margin-top"),
  MARGIN_RIGHT("margin-right"),
  MARGIN_BOTTOM("margin-bottom"),
  MARGIN_LEFT("margin-left"),

  DISPLAY("display"),

  HEIGHT("height"),

  FLEX_DIRECTION("flex-direction"),

  ALIGN_ITEMS("align-items"),

  BACKGROUND_COLOR("background-color"),

  PADDING("padding"),
  PADDING_X("padding-left", "padding-right"),
  PADDING_Y("padding-top", "padding-bottom"),
  PADDING_TOP("padding-top"),
  PADDING_RIGHT("padding-right"),
  PADDING_BOTTOM("padding-bottom"),
  PADDING_LEFT("padding-left"),

  LINE_HEIGHT("line-height"),
  LETTER_SPACING("letter-spacing");

  private final String property1;

  private final String property2;

  private Utility(String property1) {
    this(property1, null);
  }

  private Utility(String property1, String property2) {
    this.property1 = property1;
    this.property2 = property2;
  }

  final RuleFactory factory(String value) {
    return new RuleFactory(this, value);
  }

  final Rule nameValue(List<Variant> variants, String className, String value) {
    return new WithNameAndValue(this, variants, className, value);
  }

  private static final class WithNameAndValue extends Rule {
    private final String className;
    private final String value;

    public WithNameAndValue(Utility utility, List<Variant> variants, String className, String value) {
      super(utility, variants);
      this.className = className;
      this.value = value;
    }

    @Override
    final void writeTo(StringBuilder out) {
      writeClassName(out, className);

      out.append(" { ");

      String p1;
      p1 = utility.property1;

      writePropertyValue(out, p1, value);

      String p2;
      p2 = utility.property2;

      if (p2 != null) {
        out.append("; ");

        writePropertyValue(out, p2, value);
      }

      out.append(" }");

      out.append(System.lineSeparator());
    }

  }

  static void writeClassName(StringBuilder out, String className) {
    int length;
    length = className.length();

    if (length == 0) {
      return;
    }

    out.append('.');

    int index;
    index = 0;

    boolean escaped;
    escaped = false;

    char first;
    first = className.charAt(index);

    if (0x30 <= first && first <= 0x39) {
      out.append("\\3");
      out.append(first);

      index++;

      escaped = true;
    }

    for (; index < length; index++) {
      char c;
      c = className.charAt(index);

      switch (c) {
        case ' ', ',', '.', '/', ':', '@', '[', ']' -> {
          out.append("\\");

          out.append(c);

          escaped = false;
        }

        case 'a', 'b', 'c', 'd', 'e', 'f',
             'A', 'B', 'C', 'D', 'E', 'F',
             '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> {
          if (escaped) {
            out.append(' ');
          }

          out.append(c);

          escaped = false;
        }

        default -> out.append(c);
      }
    }
  }

  static void writePropertyValue(StringBuilder out, String property, String value) {
    out.append(property);

    out.append(": ");

    out.append(value);
  }

}