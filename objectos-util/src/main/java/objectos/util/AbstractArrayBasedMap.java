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

import java.util.Map;
import java.util.function.BiFunction;
import objectos.lang.HashCode;
import objectos.lang.ToString;

abstract class AbstractArrayBasedMap<K, V> implements
    Map<K, V>,
    ToString.Formattable {

  static final int MAX_POSITIVE_POWER_OF_TWO = 1 << 30;

  Object[] array = ObjectArrays.empty();

  int hashMask;

  int size = 0;

  /**
   * Returns {@code true} if this map contains a mapping for the specified
   * key.
   *
   * @param key
   *        the key to check
   *
   * @return {@code true} if this map contains a mapping for the specified
   *         key
   */
  @Override
  public final boolean containsKey(Object key) {
    if (key == null) {
      return false;
    }

    if (array == ObjectArrays.empty()) {
      return false;
    }

    int index, marker;
    index = marker = hashIndex(key);

    Object existing;
    existing = array[index];

    if (existing == null) {
      return false;
    }

    else if (existing.equals(key)) {
      return true;
    }

    else {
      index = index + 2;
    }

    while (index < array.length) {
      existing = array[index];

      if (existing == null) {
        return false;
      }

      else if (existing.equals(key)) {
        return true;
      }

      else {
        index = index + 2;
      }
    }

    index = 0;

    while (index < marker) {
      existing = array[index];

      if (existing == null) {
        return false;
      }

      else if (existing.equals(key)) {
        return true;
      }

      else {
        index = index + 2;
      }
    }

    return false;
  }

  /**
   * Returns {@code true} if this map contains at least on key mapped to the
   * specified value.
   *
   * @param value
   *        the value to check
   *
   * @return {@code true} if this map contains at least on key mapped to the
   *         specified value
   */
  @Override
  public final boolean containsValue(Object value) {
    if (value == null) {
      return false;
    }

    for (int i = 1, length = array.length; i < length; i = i + 2) {
      Object o;
      o = array[i];

      if (o == null) {
        continue;
      }

      else if (o.equals(value)) {
        return true;
      }
    }

    return false;
  }

  /**
   * Returns an unmodifiable view of all the entries in this map.
   *
   * <p>
   * If the map is modified while an iteration over the view is in progress
   * the results of the iteration are undefined.
   *
   * @return an unmodifiable view of all the entries in this map
   */
  @Override
  public final UnmodifiableView<Entry<K, V>> entrySet() {
    return new EntrySet();
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this map.
   *
   * <p>
   * The string representation <i>may</i> contain:
   *
   * <ul>
   * <li>the simple name of the map's class; and</li>
   * <li>the key/value pairs in iteration order</li>
   * </ul>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.formatStart(toString, this);

    UnmodifiableIterator<Entry<K, V>> iterator;
    iterator = entryIterator();

    if (iterator.hasNext()) {
      Entry<K, V> entry;
      entry = iterator.next();

      K key;
      key = entry.getKey();

      String name;
      name = key.toString();

      V value = entry.getValue();

      ToString.formatFirstPair(toString, level, name, value);

      while (iterator.hasNext()) {
        key = entry.getKey();

        name = key.toString();

        value = entry.getValue();

        ToString.formatNextPair(toString, level, name, value);
      }
    }

    ToString.formatEnd(toString, level);
  }

  /**
   * Returns the value associated with the specified key or {@code null} if the
   * key is not mapped in this map.
   *
   * @param key
   *        the key for which the value is to be returned
   *
   * @return the value associated with the specified key of {@code null} if the
   *         key is not mapped in this map
   */
  @SuppressWarnings("unchecked")
  @Override
  public final V get(Object key) {
    if (array == ObjectArrays.empty()) {
      return null;
    }

    int index, marker;
    index = marker = hashIndex(key);

    Object existing;

    while (index < array.length) {
      existing = array[index];

      if (existing == null) {
        return null;
      }

      else if (existing.equals(key)) {
        Object existingValue;
        existingValue = array[index + 1];

        return (V) existingValue;
      }

      else {
        index = index + 2;
      }
    }

    index = 0;

    while (index < marker) {
      existing = array[index];

      if (existing == null) {
        return null;
      }

      else if (existing.equals(key)) {
        Object existingValue;
        existingValue = array[index + 1];

        return (V) existingValue;
      }

      else {
        index = index + 2;
      }
    }

    throw new UnsupportedOperationException("Implement me");
  }

  /**
   * Returns {@code true} if this map contains no key/value mappings.
   *
   * @return {@code true} if this map contains no key/value mappings
   */
  @Override
  public final boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns an unmodifiable view of all the keys in this map.
   *
   * <p>
   * If the map is modified while an iteration over the view is in progress
   * the results of the iteration are undefined.
   *
   * @return an unmodifiable view of all the keys in this map
   */
  @Override
  public final UnmodifiableView<K> keySet() {
    return new KeySet();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param key
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V remove(Object key) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param key
   *        ignored (the operation is not supported)
   * @param value
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean remove(Object key, Object value) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param key
   *        ignored (the operation is not supported)
   * @param value
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V replace(K key, V value) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param key
   *        ignored (the operation is not supported)
   * @param oldValue
   *        ignored (the operation is not supported)
   * @param newValue
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean replace(K key, V oldValue, V newValue) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param function
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the size of this map. The size of a map is equal to the number of
   * key/value mappings it contains.
   *
   * @return the size of this map
   */
  @Override
  public final int size() {
    return size;
  }

  /**
   * Returns the string representation of this map as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return a string representation of this map
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * Returns an unmodifiable view of all the values associated with the keys of
   * this map.
   *
   * <p>
   * If the map is modified while an iteration over the view is in progress
   * the results of the iteration are undefined.
   *
   * @return an unmodifiable view of all the values associated with the keys of
   *         this map
   */
  @Override
  public final UnmodifiableView<V> values() {
    return new Values();
  }

  UnmodifiableIterator<Entry<K, V>> entryIterator() {
    return Maps.sparseEntryIterator(array);
  }

  final int hashIndex(Object o) {
    int hashCode;
    hashCode = HashCode.of(o);

    int half;
    half = hashCode & hashMask;

    return half << 1;
  }

  UnmodifiableIterator<K> keyIterator() {
    return Maps.sparseKeyIterator(array);
  }

  UnmodifiableIterator<V> valueIterator() {
    return Maps.sparseValueIterator(array);
  }

  private abstract class AbstractSet<E> extends UnmodifiableView<E> {

    final AbstractArrayBasedMap<K, V> outer = AbstractArrayBasedMap.this;

    @Override
    public final int size() {
      return size;
    }

  }

  private class EntrySet extends AbstractSet<Entry<K, V>> {

    @Override
    public final boolean contains(Object o) {
      if (o instanceof Entry) {
        Entry<?, ?> entry;
        entry = (Entry<?, ?>) o;

        Object key;
        key = entry.getKey();

        Object value;
        value = outer.get(key);

        if (value != null && value.equals(entry.getValue())) {
          return true;
        }
      }

      return false;
    }

    @Override
    public final UnmodifiableIterator<Entry<K, V>> iterator() {
      return outer.entryIterator();
    }

  }

  private class KeySet extends AbstractSet<K> {

    @Override
    public final boolean contains(Object o) {
      return outer.containsKey(o);
    }

    @Override
    public final UnmodifiableIterator<K> iterator() {
      return outer.keyIterator();
    }

  }

  private class Values extends AbstractSet<V> {

    @Override
    public final boolean contains(Object o) {
      return outer.containsValue(o);
    }

    @Override
    public final UnmodifiableIterator<V> iterator() {
      return outer.valueIterator();
    }

  }

}