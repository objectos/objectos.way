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
package objectos.way;

import java.lang.reflect.Array;
import java.util.NoSuchElementException;
import java.util.Set;

final class UtilSets {

  static class SetIterator<E> extends Util.UnmodifiableIterator<E> {

    private final Object[] array;

    private boolean computed;

    private int index;

    private Object next;

    public SetIterator(Object[] array) {
      this.array = array;
    }

    @Override
    public final boolean hasNext() {
      computeIfNecessary();

      return next != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      } else {
        var result = next;

        computed = false;

        next = null;

        return (E) result;
      }
    }

    private void computeIfNecessary() {
      if (!computed) {
        while (index < array.length) {
          next = array[index];

          index++;

          if (next != null) {
            break;
          }
        }

        computed = true;
      }
    }

  }

  private UtilSets() {}

  public static boolean equals(Set<?> self, Set<?> that) {
    var size = self.size();

    if (size != that.size()) {
      return false;
    }

    var iter = self.iterator();

    while (iter.hasNext()) {
      var o = iter.next();

      if (!that.contains(o)) {
        return false;
      }
    }

    return true;
  }

  static boolean containsImpl(Object[] array, int size, Object o) {
    if (o == null) {
      return false;
    }

    if (size == 0) {
      return false;
    }

    int index, marker;
    index = marker = hashIndex(array, o);

    Object candidate;

    while (index < array.length) {
      candidate = array[index];

      if (candidate == null) {
        return false;
      }

      else if (candidate.equals(o)) {
        return true;
      }

      else {
        index++;
      }
    }

    index = 0;

    while (index < marker) {
      candidate = array[index];

      if (candidate == null) {
        return false;
      }

      else if (candidate.equals(o)) {
        return true;
      }

      else {
        index++;
      }
    }

    return false;
  }

  static boolean equalsImpl(Set<?> self, Object obj) {
    return obj == self
        || obj instanceof Set<?> that && equals(self, that);
  }

  static int hashCodeImpl(Object[] array) {
    var hashCode = 0;

    for (int i = 0; i < array.length; i++) {
      var e = array[i];

      if (e != null) {
        hashCode += +e.hashCode();
      }
    }

    return hashCode;
  }

  static Object[] toArrayImpl(Object[] array, int size) {
    var a = new Object[size];

    fillToArray(array, a);

    return a;
  }

  @SuppressWarnings("unchecked")
  static <T> T[] toArrayImpl(Object[] array, int size, T[] a) {
    Check.notNull(a, "a == null");

    Object[] target = a;

    if (a.length < size) {
      var arrayType = a.getClass();

      var componentType = arrayType.getComponentType();

      target = (Object[]) Array.newInstance(componentType, size);
    }

    fillToArray(array, target);

    if (a.length > size) {
      a[size] = null;
    }

    return (T[]) target;
  }

  private static void fillToArray(Object[] array, Object[] a) {
    var index = 0;

    for (int i = 0, len = array.length; i < len; i++) {
      var maybe = array[i];

      if (maybe != null) {
        a[index++] = maybe;
      }
    }
  }

  private static int hashIndex(Object[] array, Object o) {
    int hc = o.hashCode();

    int mask = array.length - 1;

    return hc & mask;
  }

}