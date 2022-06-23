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

final class MoreArrays {

  /**
   * From ArraysSupport.SOFT_MAX_ARRAY_LENGTH
   */
  static final int JVM_SOFT_LIMIT = Integer.MAX_VALUE - 8;

  private MoreArrays() {}

  static int growArrayLength(int currentLength, int requiredIndex) {
    int newLength;
    newLength = currentLength + (currentLength >> 1);

    if (requiredIndex < newLength) {
      return newLength;
    }

    int requiredLength;
    requiredLength = requiredIndex + 1;

    newLength = Integer.highestOneBit(requiredLength) << 1;

    if (newLength > 0) {
      return newLength;
    }

    return Integer.MAX_VALUE;
  }

}