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

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * This class provides {@code static} factory methods for the JDK
 * {@link java.util.Map} implementations.
 *
 * <p>
 * This class is mainly provided as a convenience for Java Multi-Release
 * codebases. In particular codebases that must support versions prior to Java
 * 7 and, therefore, cannot use the diamond operator.
 */
final class Maps {

  private static class OrderedEntryIterator<K, V> extends UnmodifiableIterator<Entry<K, V>> {

    private final Object[] array;

    private int index;

    private final int size;

    OrderedEntryIterator(Object[] array, int size) {
      this.array = array;
      this.size = size;
    }

    @Override
    public final boolean hasNext() {
      return index < size;
    }

    @Override
    public final Entry<K, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      else {
        Object key;
        key = array[index];

        index++;

        Object value;
        value = array[index];

        index++;

        return newEntry(key, value);
      }
    }

  }

  private static class OrderedKeyOrValueIterator<K> extends UnmodifiableIterator<K> {

    private final Object[] array;

    private int index;

    private final int size;

    OrderedKeyOrValueIterator(Object[] array, int size, int initialIndex) {
      this.array = array;
      this.size = size;

      index = initialIndex;
    }

    @Override
    public final boolean hasNext() {
      return index < size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      else {
        Object key;
        key = array[index];

        index = index + 2;

        return (K) key;
      }
    }

  }

  private static class SparseEntryIterator<K, V> extends UnmodifiableIterator<Entry<K, V>> {

    private final Object[] array;

    private boolean computed;

    private int index;

    private Entry<K, V> next;

    SparseEntryIterator(Object[] array) {
      this.array = array;
    }

    @Override
    public final boolean hasNext() {
      if (!computed) {
        while (index < array.length) {
          Object key;
          key = array[index];

          index++;

          int valueIndex;
          valueIndex = index;

          index++;

          if (key != null) {
            Object value;
            value = array[valueIndex];

            next = newEntry(key, value);

            break;
          }
        }

        computed = true;
      }

      return next != null;
    }

    @Override
    public final Entry<K, V> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      } else {
        Entry<K, V> result;
        result = next;

        computed = false;

        next = null;

        return result;
      }
    }

  }

  private static class SparseKeyOrValueIterator<K> extends UnmodifiableIterator<K> {

    private final Object[] array;

    private boolean computed;

    private int index;

    private Object next;

    SparseKeyOrValueIterator(Object[] array, int initialIndex) {
      this.array = array;

      index = initialIndex;
    }

    @Override
    public final boolean hasNext() {
      if (!computed) {
        while (index < array.length) {
          Object key;
          key = array[index];

          index = index + 2;

          if (key != null) {
            next = key;

            break;
          }
        }

        computed = true;
      }

      return next != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final K next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      } else {
        Object result;
        result = next;

        computed = false;

        next = null;

        return (K) result;
      }
    }

  }

  private Maps() {}

  static <K, V> UnmodifiableIterator<Entry<K, V>> orderedEntryIterator(Object[] array, int size) {
    return new OrderedEntryIterator<K, V>(array, size);
  }

  static <K> UnmodifiableIterator<K> orderedKeyIterator(Object[] array, int size) {
    return new OrderedKeyOrValueIterator<K>(array, size, 0);
  }

  static <V> UnmodifiableIterator<V> orderedValueIterator(Object[] array, int size) {
    return new OrderedKeyOrValueIterator<V>(array, size, 1);
  }

  static <K, V> UnmodifiableIterator<Entry<K, V>> sparseEntryIterator(Object[] array) {
    return new SparseEntryIterator<K, V>(array);
  }

  static <K> UnmodifiableIterator<K> sparseKeyIterator(Object[] array) {
    return new SparseKeyOrValueIterator<K>(array, 0);
  }

  static <V> UnmodifiableIterator<V> sparseValueIterator(Object[] array) {
    return new SparseKeyOrValueIterator<V>(array, 1);
  }

  @SuppressWarnings("unchecked")
  private static <K, V> SimpleEntry<K, V> newEntry(Object key, Object value) {
    K castKey;
    castKey = (K) key;

    V castValue;
    castValue = (V) value;

    return new AbstractMap.SimpleEntry<K, V>(castKey, castValue);
  }

}
