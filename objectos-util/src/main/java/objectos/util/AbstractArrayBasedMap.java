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

import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.BiFunction;
import objectos.lang.Check;
import objectos.lang.HashCode;
import objectos.lang.ToString;

abstract class AbstractArrayBasedMap<K, V> implements Map<K, V>, ToString.Formattable {

  private abstract class AbstractSet<E> extends UnmodifiableView<E> {

    final AbstractArrayBasedMap<K, V> outer = AbstractArrayBasedMap.this;

    @Override
    public final int size() {
      return size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final <T> T[] toArray(T[] a) {
      Check.notNull(a, "a == null");

      Object[] target = a;

      if (a.length < size) {
        var arrayType = a.getClass();

        var componentType = arrayType.getComponentType();

        target = (Object[]) Array.newInstance(componentType, size);
      }

      var iterator = iterator();

      for (int i = 0; i < size; i++) {
        target[i] = iterator.next();
      }

      if (a.length > size) {
        a[size] = null;
      }

      return (T[]) target;
    }

  }

  private class EntrySet extends AbstractSet<Entry<K, V>> {

    @Override
    public final boolean contains(Object o) {
      if (o instanceof Entry<?, ?> entry) {
        var key = entry.getKey();

        var value = outer.get(key);

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

    var existing = array[index];

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
      var o = array[i];

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
   * <p>
   * Compares the specified object with this map for equality. Returns
   * {@code true} if and only if
   *
   * <ul>
   * <li>the specified object is also a {@link Map};</li>
   * <li>both maps have same size; and</li>
   * <li>each key-value pair in this map is also present in the specified
   * map.</li>
   * </ul>
   *
   * @param obj
   *        the object to be compared for equality with this map
   *
   * @return {@code true} if the specified object is equal to this map
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof Map<?, ?> that && equals0(that);
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

    var iterator = entryIterator();

    if (iterator.hasNext()) {
      var entry = iterator.next();

      var key = entry.getKey();

      var name = key.toString();

      var value = entry.getValue();

      ToString.formatFirstPair(toString, level, name, value);

      while (iterator.hasNext()) {
        entry = iterator.next();

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
        var existingValue = array[index + 1];

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
        var existingValue = array[index + 1];

        return (V) existingValue;
      }

      else {
        index = index + 2;
      }
    }

    throw new UnsupportedOperationException("Implement me");
  }

  /**
   * Returns the hash code value of this map.
   *
   * @return the hash code value of this map
   */
  @Override
  public final int hashCode() {
    int h = 0;

    for (Entry<K, V> entry : entrySet()) {
      h += entry.hashCode();
    }

    return h;
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
   * @param value
   *        ignored (the operation is not supported)
   * @param remappingFunction
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V merge(K key, V value,
      BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
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
    var hashCode = HashCode.of(o);

    var half = hashCode & hashMask;

    return half << 1;
  }

  UnmodifiableIterator<K> keyIterator() {
    return Maps.sparseKeyIterator(array);
  }

  UnmodifiableIterator<V> valueIterator() {
    return Maps.sparseValueIterator(array);
  }

  private boolean equals0(Map<?, ?> that) {
    var size = size();

    if (size != that.size()) {
      return false;
    }

    for (var entry : entrySet()) {
      K key = entry.getKey();

      V value = entry.getValue();

      Object thatValue = that.get(key);

      // value is guaranteed to be non-null
      if (!value.equals(thatValue)) {
        return false;
      }
    }

    return true;
  }

}