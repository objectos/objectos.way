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

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class provides {@code static} utility methods for {@link java.util.List}
 * classes and objects.
 */
final class UtilLists {

  static class SimpleIterator<E> extends Util.UnmodifiableIterator<E> {
    private final Object[] data;

    private final int size;

    private int index;

    public SimpleIterator(Object[] data, int size) {
      this.data = data;
      this.size = size;
    }

    @Override
    public final boolean hasNext() {
      return index < size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      else {
        var result = data[index];

        index++;

        return (E) result;
      }
    }
  }

  private UtilLists() {}

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

  static boolean containsImpl(Object[] data, int size, Object o) {
    if (o == null) {
      return false;
    }

    for (int i = 0; i < size; i++) {
      var thisElem = data[i];

      if (thisElem.equals(o)) {
        return true;
      }
    }

    return false;
  }

  static boolean equals0(List<?> self, List<?> that) {
    var size = self.size();

    if (size != that.size()) {
      return false;
    }

    for (int i = 0; i < size; i++) {
      var e = self.get(i);

      var o = that.get(i);

      // e is guaranteed to be not null
      if (!e.equals(o)) {
        return false;
      }
    }

    return true;
  }

  static boolean equalsImpl(List<?> self, Object obj) {
    return obj == self
        || obj instanceof List<?> that && equals0(self, that);
  }

  static Object getImpl(Object[] data, int size, int index) {
    Objects.checkIndex(index, size);

    return data[index];
  }

  static int hashCodeImpl(Object[] data, int size) {
    var hashCode = 1;

    for (int i = 0; i < size; i++) {
      var e = data[i];

      // e is guaranteed to be not null
      hashCode = 31 * hashCode + e.hashCode();
    }

    return hashCode;
  }

  static int indexOfImpl(Object[] data, int size, Object o) {
    var result = -1;

    if (o == null) {
      return result;
    }

    for (int i = 0; i < size; i++) {
      var element = data[i];

      if (element.equals(o)) {
        result = i;

        break;
      }
    }

    return result;
  }

  static String joinImpl(Object[] data, int size) {
    if (size == 0) {
      return "";
    }

    if (size == 1) {
      var o = data[0];

      return o.toString();
    }

    var sb = new StringBuilder();

    for (int i = 0; i < size; i++) {
      sb.append(data[i]);
    }

    return sb.toString();
  }

  static String joinImpl(Object[] data, int size, String delimiter) {
    Check.notNull(delimiter, "delimiter == null");

    if (size == 0) {
      return "";
    }

    Object o;

    if (size == 1) {
      o = data[0];

      return o.toString();
    }

    StringBuilder sb;
    sb = new StringBuilder();

    o = data[0];

    sb.append(o.toString());

    for (int i = 1; i < size; i++) {
      sb.append(delimiter);

      o = data[i];

      sb.append(o.toString());
    }

    return sb.toString();
  }

  static String joinImpl(Object[] data, int size, String delimiter, String prefix, String suffix) {
    Check.notNull(delimiter, "delimiter == null");
    Check.notNull(prefix, "prefix == null");
    Check.notNull(suffix, "suffix == null");

    if (size == 0) {
      return prefix + suffix;
    }

    Object o;

    if (size == 1) {
      o = data[0];

      return prefix + o.toString() + suffix;
    }

    var sb = new StringBuilder();

    sb.append(prefix);

    o = data[0];

    sb.append(o.toString());

    for (int i = 1; i < size; i++) {
      sb.append(delimiter);

      o = data[i];

      sb.append(o.toString());
    }

    sb.append(suffix);

    return sb.toString();
  }

  static int lastIndexOfImpl(Object[] data, int size, Object o) {
    var result = -1;

    for (int i = size - 1; i >= 0; i--) {
      var element = data[i];

      if (element.equals(o)) {
        result = i;

        break;
      }
    }

    return result;
  }

  @SuppressWarnings("unchecked")
  static <T> T[] toArrayImpl(Object[] data, int size, T[] a) {
    Check.notNull(a, "a == null");

    if (a.length < size) {
      Class<? extends Object[]> newType;
      newType = a.getClass();

      Object[] copy;
      copy = Arrays.copyOf(data, size, newType);

      return (T[]) copy;
    }

    System.arraycopy(data, 0, a, 0, size);

    if (a.length > size) {
      a[size] = null;
    }

    return a;
  }

  static String toStringImpl(Object self, Object[] data, int size) {
    if (size == 0) {
      return "[]";
    }

    StringBuilder sb;
    sb = new StringBuilder();

    sb.append('[');

    for (int idx = 0; idx < size; idx++) {
      if (idx > 0) {
        sb.append(',');
        sb.append(' ');
      }

      Object o;
      o = data[idx];

      if (o == self) {
        sb.append("(this Collection)");
      } else {
        sb.append(o);
      }
    }

    sb.append(']');

    return sb.toString();
  }

}