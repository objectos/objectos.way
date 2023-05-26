/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css;

import java.util.Iterator;

final class Iterators {

  private Iterators() {}

  /**
   * Checks if two iterators are equal to each other in a null-safe manner.
   *
   * <p>
   * Two iterators are considered equal if they both return the same number of
   * elements and each returned element from {@code a} is equal to the
   * corresponding returned element from {@code b}.
   *
   * <p>
   * If both iterators {@code a} and {@code b} are null this method returns
   * {@code true}.
   *
   * @param a
   *        the first iterator to check for equality
   * @param b
   *        the second iterator to check for equality
   *
   * @return {@code true} if the arguments are equal to each other and
   *         {@code false} otherwise
   */
  public static boolean equals(Iterator<?> a, Iterator<?> b) {
    if (a == null && b == null) {
      return true;
    }

    Object an, bn;

    if (a != null && b != null) {
      while (a.hasNext()) {
        if (!b.hasNext()) {
          return false;
        }

        an = a.next();

        bn = b.next();

        if (!objectos.lang.Equals.of(an, bn)) {
          return false;
        }
      }

      return !b.hasNext();
    }

    return false;
  }

}