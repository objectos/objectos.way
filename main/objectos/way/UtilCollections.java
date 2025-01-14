/*
 * Copyright (C) 2022-2025 Objectos Software LTDA.
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

import java.util.Collection;

final class UtilCollections {

  private UtilCollections() {}

  /**
   * Returns a name for the specified index having the specified length, result
   * is padded with zero digits if necessary.
   *
   * <p>
   * Auxiliary method for {@link #formatToString(StringBuilder, int)}
   * implementation.
   *
   * <p>
   * Examples:
   *
   * <pre>{@code
   * indexName(1, 3); // returns "001"
   * indexName(37, 3); // returns "037"
   * indexName(912, 3); // returns "912"
   * }</pre>
   *
   * @param index
   *        the index value to format
   * @param length
   *        the length of the result string
   *
   * @return a name for the specified index having the specified length
   */
  public static String indexName(int index, int length) {
    var sb = new StringBuilder();

    var s = Integer.toString(index);

    var indexLength = s.length();

    var diff = length - indexLength;

    for (int i = 0; i < diff; i++) {
      sb.append(' ');
    }

    sb.append(s);

    return sb.toString();
  }

  /**
   * Returns the number of digits this collection's size has.
   *
   * <p>
   * Auxiliary method for {@link #formatToString(StringBuilder, int)}
   * implementation.
   *
   * @return the number of digits this collection's size has
   */
  public static int sizeDigits(Collection<?> self) {
    int size = self.size();

    if (size < 10) {
      return 1;
    }

    else if (size < 100) {
      return 2;
    }

    else {
      double l = Math.log10(size);

      double f = Math.floor(l);

      return (int) f;
    }
  }

}