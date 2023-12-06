/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

final class Utf8 {

  static final byte DIGIT_0 = UsAscii.DIGIT_0;

  static final byte DIGIT_1 = UsAscii.DIGIT_1;

  static final byte DIGIT_2 = UsAscii.DIGIT_2;

  static final byte DIGIT_3 = UsAscii.DIGIT_3;

  static final byte DIGIT_4 = UsAscii.DIGIT_4;

  static final byte DIGIT_5 = UsAscii.DIGIT_5;

  static final byte DIGIT_6 = UsAscii.DIGIT_6;

  static final byte DIGIT_7 = UsAscii.DIGIT_7;

  static final byte DIGIT_8 = UsAscii.DIGIT_8;

  static final byte DIGIT_9 = UsAscii.DIGIT_9;

  private Utf8() {}

  public static int parseInt(byte utf8) {
    if (!isDigit(utf8)) {
      throw new NumberFormatException();
    }

    return utf8 - DIGIT_0;
  }

  private static boolean isDigit(byte utf8) {
    if (utf8 < DIGIT_0) {
      return false;
    }

    if (utf8 > DIGIT_9) {
      return false;
    }

    return true;
  }

}