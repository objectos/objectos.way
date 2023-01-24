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

final class MoreArrays {

  /**
   * From ArraysSupport.SOFT_MAX_ARRAY_LENGTH
   */
  static final int JVM_SOFT_LIMIT = Integer.MAX_VALUE - 8;

  private MoreArrays() {}

  static int growArrayLength(int length, int requiredIndex) {
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