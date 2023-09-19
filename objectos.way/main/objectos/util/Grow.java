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

final class Grow {

  public static final int DEFAULT_CAPACITY = 10;

  /**
   * From ArraysSupport.SOFT_MAX_ARRAY_LENGTH
   */
  public static final int JVM_SOFT_LIMIT = Integer.MAX_VALUE - 8;

  private Grow() {}

  public static int growBy(int length, int ammount) {
    int half = length >> 1;

    int delta = Math.max(ammount, half);

    int newLength = length + delta;

    return grow0(length, newLength);
  }

  public static int growByOne(int length) {
    int half = length >> 1;

    int newLength = length + half;

    return grow0(length, newLength);
  }

  private static int grow0(int length, int newLength) {
    if (newLength > 0 && newLength <= Grow.JVM_SOFT_LIMIT) {
      return newLength;
    }

    if (length != Grow.JVM_SOFT_LIMIT) {
      return Grow.JVM_SOFT_LIMIT;
    }

    throw new OutOfMemoryError(
      """
      Cannot allocate array: exceeds JVM soft limit.

      length = %,14d
      limit  = %,14d
      """.formatted(length + 1, Grow.JVM_SOFT_LIMIT)
    );
  }

  public static int arrayLength(int length, int requiredIndex) {
    var halfLength = length >> 1;

    var newLength = length + halfLength;

    if (requiredIndex < newLength) {
      return newLength;
    } else {
      return growArrayLength0(requiredIndex);
    }
  }

  private static int growArrayLength0(int requiredIndex) {
    var minLength = requiredIndex + 1;

    var halfLength = minLength >> 1;

    var newLength = minLength + halfLength;

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