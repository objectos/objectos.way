/*
 * Copyright (C) 2016-2025 Objectos Software LTDA.
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

import java.util.Arrays;

final class Bytes {

  public static final byte HTAB = '\t';

  public static final byte LF = '\n';

  public static final byte CR = '\r';

  public static final byte SP = ' ';

  public static final byte COLON = ':';

  public static final byte[] COLON_BYTES = {COLON};

  public static final byte QUESTION_MARK = '?';

  public static final byte SOLIDUS = '/';

  public static final byte DIGIT_0 = '0';

  public static final byte DIGIT_9 = '9';

  public static final byte[] CRLF = {CR, LF};

  public static final byte[] COLONSP = {COLON, SP};

  private Bytes() {}

  private static final byte[] HEX_DIGITS;

  static {
    final byte[] hex;
    hex = new byte[0x80];

    Arrays.fill(hex, (byte) -1);

    hex['0'] = 0;
    hex['1'] = 1;
    hex['2'] = 2;
    hex['3'] = 3;
    hex['4'] = 4;
    hex['5'] = 5;
    hex['6'] = 6;
    hex['7'] = 7;
    hex['8'] = 8;
    hex['9'] = 9;
    hex['a'] = 10;
    hex['b'] = 11;
    hex['c'] = 12;
    hex['d'] = 13;
    hex['e'] = 14;
    hex['f'] = 15;
    hex['A'] = 10;
    hex['B'] = 11;
    hex['C'] = 12;
    hex['D'] = 13;
    hex['E'] = 14;
    hex['F'] = 15;

    HEX_DIGITS = hex;
  }

  public static byte fromHexDigit(byte c) {
    if (c < 0) {
      return -1;
    } else {
      return HEX_DIGITS[c];
    }
  }

  public static boolean isOptionalWhitespace(byte value) {
    return switch (value) {
      case SP, HTAB -> true;

      default -> false;
    };
  }

  public static byte toLowerCase(byte ch) {
    if (ch >= 'A' && ch <= 'Z') {
      return (byte) (ch ^ 0x20);
    }

    return ch;
  }

}