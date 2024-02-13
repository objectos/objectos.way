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
package objectos.http;

import java.nio.charset.StandardCharsets;

final class Bytes {

  public static final byte HTAB = '\t';

  public static final byte LF = '\n';

  public static final byte CR = '\r';

  public static final byte SP = ' ';

  public static final byte COLON = ':';

  public static final byte QUESTION_MARK = '?';

  public static final byte SOLIDUS = '/';

  public static final byte DIGIT_0 = '0';

  public static final byte DIGIT_9 = '9';

  public static final byte[] CRLF = {CR, LF};

  public static final byte[] COLONSP = {COLON, SP};

  public static final byte[] CLOSE = utf8("close");

  public static final byte[] FORM_URLENCODED = utf8("application/x-www-form-urlencoded");

  public static final byte[] KEEP_ALIVE = utf8("keep-alive");

  private Bytes() {}

  public static boolean isDigit(byte value) {
    return DIGIT_0 <= value && value <= DIGIT_9;
  }

  public static boolean isHexDigit(byte value) {
    return isDigit(value)
        || 'a' <= value && value <= 'f'
        || 'A' <= value && value <= 'F';
  }

  public static boolean isOptionalWhitespace(byte value) {
    return switch (value) {
      case SP, HTAB -> true;

      default -> false;
    };
  }

  public static int toInt(byte b) {
    return b & 0xFF;
  }

  public static byte toLowerCase(byte ch) {
    if (ch >= 'A' && ch <= 'Z') {
      return (byte) (ch ^ 0x20);
    }

    return ch;
  }

  public static byte[] utf8(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

  public static int parseHexDigit(byte value) {
    return switch (value) {
      case '0' -> 0;
      case '1' -> 1;
      case '2' -> 2;
      case '3' -> 3;
      case '4' -> 4;
      case '5' -> 5;
      case '6' -> 6;
      case '7' -> 7;
      case '8' -> 8;
      case '9' -> 9;
      case 'a', 'A' -> 10;
      case 'b', 'B' -> 11;
      case 'c', 'C' -> 12;
      case 'd', 'D' -> 13;
      case 'e', 'E' -> 14;
      case 'f', 'F' -> 15;

      default -> throw new IllegalArgumentException(
          "Illegal hex char= " + (char) toInt(value)
      );
    };
  }

}