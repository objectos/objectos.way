/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html;

import java.util.Arrays;

/// Array utilities for the Objectos HTML implementation.
public final class MoreArrays {

  /// An empty zero-length `byte` array instance.
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  /// An empty zero-length `Object` array instance.
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  /// From ArraysSupport.SOFT_MAX_ARRAY_LENGTH
  private static final int JVM_SOFT_LIMIT = Integer.MAX_VALUE - 8;

  private MoreArrays() {}

  /// Copies the values of the array into a larger one (if necessary) so that a
  /// value can be inserted at the required index. More formally:
  ///
  /// If the `requiredIndex` is smaller than `0` then an
  /// `IllegalArgumentException` is thrown.
  ///
  /// If the `requiredIndex` is smaller than `array.length` then the array is
  /// not copied and is returned unchanged.
  ///
  /// If the `requiredIndex` is equal to or is greater than `array.length` then:
  ///
  /// 1. a new `byte` array instance is created. The length of the new array is
  ///    guaranteed to be greater than `requiredIndex`;
  /// 2. all values from the original array are copied into the new array so
  ///    that, for all valid indices in the original array, the new array
  ///    contains an identical value for the same index; and
  /// 3. the new array instance is returned.
  ///
  /// @param array the array instance to be copied if necessary
  /// @param requiredIndex the index where a value is to be inserted
  ///
  /// @return the `array` instance itself or a larger copy of the original
  ///
  /// @throws IllegalArgumentException if `requiredIndex < 0`
  public static byte[] growIfNecessary(byte[] array, int requiredIndex) {
    if (requiredIndex < 0) {
      throw new IllegalArgumentException("requiredIndex cannot be negative");
    }

    final int length;
    length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    final int newLength;
    newLength = arrayLength(length, requiredIndex);

    final byte[] result;
    result = new byte[newLength];

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

  /// Copies the values of the array into a larger one (if necessary) so that a
  /// value can be inserted at the required index. More formally:
  ///
  /// If the `requiredIndex` is smaller than `0` then an
  /// `IllegalArgumentException` is thrown.
  ///
  /// If the `requiredIndex` is smaller than `array.length` then the array is
  /// not copied and is returned unchanged.
  ///
  /// If the `requiredIndex` is equal to or is greater than `array.length` then:
  ///
  /// 1. a new array instance is created. The resulting array is of exactly the
  ///    same class as the original array. The length of the new array is
  ///    guaranteed to be greater than `requiredIndex`;
  /// 2. all values from the original array are copied into the new array so
  ///    that, for all valid indices in the original array, the new array
  ///    contains an identical value for the same index; and
  /// 3. the new array instance is returned.
  ///
  /// @param <T> the type of the objects in the array
  /// @param array the array instance to be copied if necessary
  /// @param requiredIndex the index where a value is to be inserted
  ///
  /// @return the `array` instance itself or a larger copy of the original
  ///
  /// @throws IllegalArgumentException if `requiredIndex < 0`
  public static <T> T[] growIfNecessary(T[] array, int requiredIndex) {
    if (requiredIndex < 0) {
      throw new IllegalArgumentException("requiredIndex cannot be negative");
    }

    final int length;
    length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    final int newLength;
    newLength = arrayLength(length, requiredIndex);

    return Arrays.copyOf(array, newLength);
  }

  private static int arrayLength(int length, int requiredIndex) {
    final int halfLength;
    halfLength = length >> 1;

    final int newLength;
    newLength = length + halfLength;

    if (requiredIndex < newLength) {
      return newLength;
    } else {
      return growArrayLength0(requiredIndex);
    }
  }

  private static int growArrayLength0(int requiredIndex) {
    final int minLength;
    minLength = requiredIndex + 1;

    final int halfLength;
    halfLength = minLength >> 1;

    final int newLength;
    newLength = minLength + halfLength;

    if (0 < newLength && newLength <= JVM_SOFT_LIMIT) {
      return newLength;
    } else {
      return growArrayLength1(minLength);
    }
  }

  private static int growArrayLength1(int minLength) {
    if (minLength < 0) {
      throw new OutOfMemoryError("Array size already at maximum");
    }

    if (minLength <= JVM_SOFT_LIMIT) {
      return JVM_SOFT_LIMIT;
    }

    return Integer.MAX_VALUE;
  }

}
