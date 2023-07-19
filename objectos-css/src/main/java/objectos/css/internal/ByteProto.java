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

final class ByteProto {

  //internal instructions

  public static final byte NULL = -1;
  public static final byte MARKED = -2;
  public static final byte MARKED3 = -3;
  public static final byte MARKED4 = -4;
  public static final byte MARKED5 = -5;
  public static final byte MARKED6 = -6;
  public static final byte MARKED7 = -7;
  public static final byte MARKED9 = -8;
  public static final byte MARKED10 = -9;
  public static final byte INTERNAL = -10;
  public static final byte INTERNAL4 = -11;
  public static final byte INTERNAL7 = -12;
  public static final byte INTERNAL9 = -13;

  //rules

  public static final byte STYLE_RULE = -14;
  public static final byte STYLE_RULE_END = -15;

  //selectors

  public static final byte SELECTOR_ATTR = -16;
  public static final byte SELECTOR_ATTR_VALUE = -17;
  public static final byte SELECTOR_PSEUDO_CLASS = -18;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -19;
  public static final byte SELECTOR_SEL = -20;
  public static final byte SELECTOR_SEL_END = -21;
  public static final byte SELECTOR_TYPE = -22;

  //properties

  public static final byte DECLARATION = -23;
  public static final byte DECLARATION_END = -24;

  //property values

  public static final byte COLOR_HEX = -25;
  public static final byte COMMA = -26;
  public static final byte JAVA_DOUBLE = -27;
  public static final byte JAVA_INT = -28;
  public static final byte JAVA_STRING = -29;
  public static final byte LENGTH_DOUBLE = -30;
  public static final byte LENGTH_INT = -31;
  public static final byte PERCENTAGE_DOUBLE = -32;
  public static final byte PERCENTAGE_INT = -33;
  public static final byte STANDARD_NAME = -34;
  public static final byte STRING_LITERAL = -35;
  public static final byte URL = -36;
  public static final byte ZERO = -37;

  private ByteProto() {}

  public static byte markedOf(int length) {
    return switch (length) {
      case 3 -> MARKED3;

      case 5 -> MARKED5;

      case 6 -> MARKED6;

      case 9 -> MARKED9;

      case 10 -> MARKED10;

      default -> throw new UnsupportedOperationException(
        "Implement me :: length=" + length
      );
    };
  }

}