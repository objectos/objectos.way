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
package objectos.util;

final class Iterables {

  private Iterables() {}

  /**
   * Checks if two iterables are equal to each other in a null-safe manner.
   *
   * <p>
   * Returns:
   *
   * <ul>
   * <li>{@code true} if both {@code a} and {@code b} are null;</li>
   * <li>{@code true} if both {@code a} and {@code b} are non-null and the
   * expresssion {@code Equals.iterators(a.iterator(), b.iterator())} evaluates
   * to {@code true}; and</li>
   * <li>{@code false} otherwise</li>
   * </ul>
   *
   * @param a
   *        the first iterable to check for equality
   * @param b
   *        the second iterable to check for equality
   *
   * @return {@code true} if the arguments are equal to each other and
   *         {@code false} otherwise
   */
  public static boolean equals(Iterable<?> a, Iterable<?> b) {
    if (a == null && b == null) {
      return true;
    }

    if (a != null && b != null) {
      return Iterators.equals(a.iterator(), b.iterator());
    }

    return false;
  }

}