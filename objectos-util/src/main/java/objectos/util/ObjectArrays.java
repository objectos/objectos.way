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

/**
 * <p>
 * This class provides {@code static} methods for operating on
 * {@link java.lang.Object} arrays.
 */
public final class ObjectArrays {

  static final int DEFAULT_CAPACITY = 10;

  static final Object[] EMPTY = new Object[0];

  private ObjectArrays() {}

  /**
   * Returns the empty zero-length {@link Object} array instance. Since it is a
   * zero-length instance, values cannot be inserted nor removed from it.
   *
   * @return an empty {@link Object} array
   */
  public static Object[] empty() {
    return EMPTY;
  }

  static Object[] copyWithNullCheckAsObjectArray(Object[] elements, String identifier) {
    var copy = new Object[elements.length];

    for (int i = 0; i < elements.length; i++) {
      var e = elements[i];

      if (e == null) {
        throw new NullPointerException(identifier + "[" + i + "] == null");
      }

      copy[i] = e;
    }

    return copy;
  }

}