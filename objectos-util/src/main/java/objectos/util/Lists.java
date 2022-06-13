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

import java.util.Comparator;
import java.util.List;

/**
 * This class provides {@code static} utility methods for {@link java.util.List}
 * classes and objects.
 */
public final class Lists {

  private Lists() {}

  /**
   * Returns the only element of the specified list or throws an exception if
   * the list is empty or if the list contains more than one element.
   *
   * @param <T>
   *        the class of the objects in the list
   * @param list
   *        the list whose only element is to be returned
   *
   * @return the only element of this list
   *
   * @throws IllegalStateException
   *         if the list is empty or if the list contains more than one element
   */
  public static <T> T getOnly(List<T> list) {
    switch (list.size()) {
      case 0:
        throw new IllegalStateException("Could not getOnly: empty.");
      case 1:
        return list.get(0);
      default:
        throw new IllegalStateException("Could not getOnly: more than one element.");
    }
  }

  /**
   * Returns a comparator that compares {@link Comparable} objects in natural
   * order.
   *
   * @param <E>
   *        the {@link Comparable} type of element to be compared
   *
   * @return a comparator that imposes the <i>natural ordering</i> on {@code
   *         Comparable} objects.
   *
   * @see Comparable
   */
  public static <E> Comparator<E> naturalOrder() {
    return Comparators.naturalOrder();
  }

}