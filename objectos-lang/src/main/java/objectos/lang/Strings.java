/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

/**
 * This class provides {@code static} utility methods for operating on
 * {@link java.lang.String} instances.
 *
 * <p>
 * In particular, it provides a method for null string handling and another to
 * extract the Unicode code points from a string.
 *
 * @since 0.2
 */
public final class Strings {

  private static final int[] EMPTY_INT_ARRAY = new int[0];

  private Strings() {}

  /**
   * Returns a new {@code int} array containing all the Unicode code points, in
   * order, of the supplied string. The returned array has a length that is
   * equal to the quantity of code points in the string.
   *
   * @param s
   *        a string
   *
   * @return a new {@code int} array containing code points
   */
  public static int[] toCodePoints(String s) {
    int stringLength;
    stringLength = s.length();

    if (stringLength == 0) {
      return EMPTY_INT_ARRAY;
    }

    if (stringLength == 1) {
      char onlyChar = s.charAt(0);

      return new int[] {onlyChar};
    }

    int[] intArray;
    intArray = new int[stringLength];

    int intIndex;
    intIndex = 0;

    for (int charIndex = 0; charIndex < stringLength; charIndex++) {
      char highOrBmp;
      highOrBmp = s.charAt(charIndex);

      intArray[intIndex] = highOrBmp;

      if (!Character.isHighSurrogate(highOrBmp)) {
        intIndex++;

        continue;
      }

      char low;
      low = s.charAt(charIndex + 1);

      if (!Character.isLowSurrogate(low)) {
        intIndex++;

        continue;
      }

      charIndex++;

      int codePoint;
      codePoint = Character.toCodePoint(highOrBmp, low);

      intArray[intIndex] = codePoint;

      intIndex++;
    }

    if (intIndex == intArray.length) {
      return intArray;
    } else {
      int[] result;
      result = new int[intIndex];

      System.arraycopy(intArray, 0, result, 0, intIndex);

      return result;
    }
  }

  /**
   * Returns {@code null} if the supplied string is {@code null} or if it is
   * empty. Returns the string itself otherwise.
   *
   * @param s
   *        a string
   *
   * @return {@code null} if the string {@code s} is null or if it is empty. The
   *         string itself otherwise.
   */
  static String emptyToNull(String s) {
    if (s == null) {
      return null;
    }

    if (s.isEmpty()) {
      return null;
    }

    return s;
  }

  /**
   * Returns {@code true} if the supplied string is {@code null} or if it is
   * empty. Return {@code false} otherwise.
   *
   * @param s
   *        a string
   *
   * @return {@code true} if the string {@code s} is null or if it is empty.
   *         {@code false} otherwise.
   */
  static boolean isNullOrEmpty(String s) {
    if (s == null) {
      return true;
    }

    if (s.isEmpty()) {
      return true;
    }

    return false;
  }

}