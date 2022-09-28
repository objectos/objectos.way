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
package objectos.util;

import objectos.lang.Check;

/**
 * <p>
 * This class provides {@code static} methods for operating on {@code byte}
 * arrays.
 */
public final class ByteArrays {

  static final byte[] EMPTY = new byte[0];

  private static final int BYTE_MASK = 0xff;

  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

  private static final int HEX_MASK = 0xf;

  private ByteArrays() {}

  /**
   * Returns the empty zero-length {@code byte} array instance. Since it is a
   * zero-length instance, values cannot be inserted nor removed from it.
   *
   * @return an empty {@code byte} array
   */
  public static byte[] empty() {
    return EMPTY;
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
   * <li>a new {@code byte} array instance is created. The length of the new
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
   * byte b = readByte();
   * array = ByteArrays.growIfNecessary(array, currentIndex);
   * array[currentIndex++] = b;</pre>
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
  public static byte[] growIfNecessary(byte[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    var length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    var newLength = MoreArrays.growArrayLength(length, requiredIndex);

    var result = new byte[newLength];

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

  /**
   * Returns a string representation of the array in hexadecimal.
   *
   * <p>
   * The returned string is formed by concatenating the hexadecimal
   * representation of each byte value from the array in order. Therefore,
   * the returned string length is double the length of the array.
   *
   * <p>
   * The following characters are used as hexadecimal digits:
   *
   * <pre>0123456789abcdef</pre>
   *
   * @param array
   *        the array to be converted.
   *
   * @return the string representation of the array in hexadecimal.
   *
   * @throws NullPointerException
   *         if {@code array} is null
   */
  public static String toHexString(byte[] array) {
    Check.notNull(array, "array == null");

    var length = array.length;

    switch (length) {
      case 0:
        return "";
      default:
        return toHexStringNonEmpty(array, 0, length);
    }
  }

  static long longValue(byte[] array) {
    Check.notNull(array, "array == null");
    Check.argument(array.length == 8, "array.length must be 8");

    return longValueUnchecked(array);
  }

  static long longValueUnchecked(byte[] array) {
    long result = array[0] & BYTE_MASK;
    result = (result << 8) | (array[1] & BYTE_MASK);
    result = (result << 8) | (array[2] & BYTE_MASK);
    result = (result << 8) | (array[3] & BYTE_MASK);
    result = (result << 8) | (array[4] & BYTE_MASK);
    result = (result << 8) | (array[5] & BYTE_MASK);
    result = (result << 8) | (array[6] & BYTE_MASK);
    result = (result << 8) | (array[7] & BYTE_MASK);
    return result;
  }

  private static String toHexStringNonEmpty(byte[] array, int offset, int length) {
    var result = new char[length * 2];

    for (int i = 0, j = 0; i < length; i++) {
      var b = array[offset + i] & BYTE_MASK;

      result[j++] = HEX_CHARS[b >>> 4];

      result[j++] = HEX_CHARS[b & HEX_MASK];
    }

    return new String(result);
  }

}