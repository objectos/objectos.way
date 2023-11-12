/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.util;

import objectos.lang.object.Check;

/**
 * <p>
 * This class provides {@code static} methods for operating on {@code char}
 * arrays.
 */
public final class CharArrays {

  static final char[] EMPTY_CHAR_ARRAY = new char[0];

  private CharArrays() {}

  /**
   * Returns the specified array (or a larger copy if necessary)
   * with all characters from the {@code CharSequence} appended at the specified
   * array offset. More formally:
   *
   * <p>
   * If the argument {@code s} is empty (i.e., s.length() returns zero) then the
   * {@code array} is returned unchanged.
   *
   * <p>
   * If the argument {@code s} is not empty and is such that
   * {@code offset + s.length() <= array.length} then the original array
   * is used for the next step. If not, then a copy of the
   * {@code array} is made such that {@code offset + s.length <= copy.length}.
   * The array copy is used for the next step.
   *
   * <p>
   * All characters from {@code s} are then copied into the array coming from
   * the previous step. The character at index 0 of
   * the sequence {@code s} is copied to the array position {@code offset};
   * the character at index 1 of the sequence {@code s} is copied to the array
   * position {@code offset + 1}; and so on until all characters from sequence
   * {@code s} are copied. The array is then returned.
   *
   * @param array
   *        the destination array instance. Characters will copied into this
   *        array (or a larger copy if necessary)
   * @param offset
   *        the starting {@code array} position. Indicates the array index where
   *        the first character will be copied to
   * @param s
   *        the {@code CharSequence} containing the characters to be copied from
   *
   * @return an array containing all the characters from {@code s} starting from
   *         the array position {@code offset}
   *
   * @throws IllegalArgumentException
   *         if {@code offset < 0}
   */
  public static char[] append(char[] array, int offset, CharSequence s) {
    Check.argument(offset >= 0, "offset cannot be negative");

    var length = s.length();

    if (length == 0) {
      return array;
    }

    var requiredIndex = offset + length - 1;

    var result = growIfNecessary(array, requiredIndex);

    for (int i = 0; i < length; i++) {
      result[offset + i] = s.charAt(i);
    }

    return result;
  }

  /**
   * Copies the values of the array into a larger one (if necessary) so that a
   * value can be inserted at the required index. More formally:
   *
   * <p>
   * If the {@code requiredIndex} is smaller than {@code 0} then an
   * {@link java.lang.IllegalArgumentException} is thrown.
   *
   * <p>
   * If the {@code requiredIndex} is smaller than {@code array.length} then the
   * array is not copied and is returned unchanged.
   *
   * <p>
   * If the {@code requiredIndex} is equal to or is greater than
   * {@code array.length} then:
   *
   * <ol>
   * <li>a new {@code char} array instance is created. The length of the new
   * array is guaranteed to be greater than {@code requiredIndex};</li>
   * <li>all values from the original array are copied into the new array so
   * that, for all valid indices in the original array, the new array contains
   * an identical value for the same index; and</li>
   * <li>the new array instance is returned.</li>
   * </ol>
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * char c = readChar();
   * array = CharArrays.growIfNecessary(array, currentIndex);
   * array[currentIndex++] = c;</pre>
   *
   * @param array
   *        the array instance to be copied if necessary
   * @param requiredIndex
   *        the index where a value is to be inserted
   *
   * @return the {@code array} instance itself or a larger copy of the
   *         original
   *
   * @throws IllegalArgumentException
   *         if {@code requiredIndex < 0}
   */
  public static char[] growIfNecessary(char[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    var length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    var newLength = Grow.arrayLength(length, requiredIndex);

    var result = new char[newLength];

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

}