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
  public static final byte END = -2;
  public static final byte MARKED = -3;
  public static final byte MARKED3 = -4;
  public static final byte MARKED4 = -5;
  public static final byte MARKED5 = -6;
  public static final byte MARKED6 = -7;
  public static final byte MARKED7 = -8;
  public static final byte MARKED9 = -9;
  public static final byte MARKED10 = -10;
  public static final byte MARKED11 = -11;
  public static final byte INTERNAL = -12;
  public static final byte INTERNAL4 = -13;
  public static final byte INTERNAL6 = -14;
  public static final byte INTERNAL7 = -15;
  public static final byte INTERNAL9 = -16;
  public static final byte INTERNAL10 = -17;
  public static final byte INTERNAL11 = -18;

  //rules

  public static final byte MEDIA_RULE = -19;
  public static final byte STYLE_RULE = -20;

  //media query

  public static final byte MEDIA_TYPE = -21;

  //selectors

  public static final byte SELECTOR_ATTR = -22;
  public static final byte SELECTOR_ATTR_VALUE = -23;
  public static final byte SELECTOR_CLASS = -24;
  public static final byte SELECTOR_COMBINATOR = -25;
  public static final byte SELECTOR_PSEUDO_CLASS = -26;
  public static final byte SELECTOR_PSEUDO_ELEMENT = -27;
  public static final byte SELECTOR_TYPE = -28;

  //properties

  public static final byte DECLARATION = -29;
  public static final byte PROPERTY_CUSTOM = -30;
  public static final byte PROPERTY_STANDARD = -31;
  public static final byte VAR_FUNCTION = -32;

  //property values

  public static final byte COLOR_HEX = -33;
  public static final byte COMMA = -34;
  public static final byte JAVA_DOUBLE = -35;
  public static final byte JAVA_INT = -36;
  public static final byte JAVA_STRING = -37;
  public static final byte LENGTH_DOUBLE = -38;
  public static final byte LENGTH_INT = -39;
  public static final byte LITERAL_DOUBLE = -40;
  public static final byte LITERAL_INT = -41;
  public static final byte LITERAL_STRING = -42;
  public static final byte PERCENTAGE_DOUBLE = -43;
  public static final byte PERCENTAGE_INT = -44;
  public static final byte RAW = -45;
  public static final byte STANDARD_NAME = -46;
  public static final byte URL = -47;
  public static final byte ZERO = -48;

  private ByteProto() {}

  public static byte markedOf(int length) {
    return switch (length) {
      case 3 -> MARKED3;

      case 4 -> MARKED4;

      case 5 -> MARKED5;

      case 6 -> MARKED6;

      case 7 -> MARKED7;

      case 9 -> MARKED9;

      case 10 -> MARKED10;

      case 11 -> MARKED11;

      default -> throw new UnsupportedOperationException(
        "Implement me :: length=" + length
      );
    };
  }

}