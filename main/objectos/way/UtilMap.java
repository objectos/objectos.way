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
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A hash-based implementation of the {@link Map} interface.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
sealed class UtilMap<K, V> extends UtilArrayBasedMap<K, V> permits UtilSequencedMap {

  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  private static final int FIRST_RESIZE = 8;

  private static final int MAX_ARRAY_LENGTH = MAX_POSITIVE_POWER_OF_TWO;

  private final float loadFactor = DEFAULT_LOAD_FACTOR;

  private int rehashSize;

  /**
   * Creates a new {@code GrowableMap} instance.
   */
  public UtilMap() {}

  /**
   * Removes all of the mappings in this map.
   */
  @Override
  public void clear() {
    Arrays.fill(array, null);

    size = 0;
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @param key
   *        ignored (the operation is not implemented)
   * @param remappingFunction
   *        ignored (the operation is not implemented)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @param key
   *        ignored (the operation is not implemented)
   * @param mappingFunction
   *        ignored (the operation is not implemented)
   *
   * @return this method does not return as it always throws an exception
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @param key
   *        ignored (the operation is not implemented)
   * @param remappingFunction
   *        ignored (the operation is not implemented)
   *
   * @return this method does not return as it always throws an exception
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final V computeIfPresent(
      K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Associates the specified value with the specified key in this map. If the
   * map previously contained a mapping for the key, the old value is replaced
   * by the specified value.
   *
   * <p>
   * A map {@code m} is said to contain a mapping for a key {@code k} if and
   * only if {@link #containsKey(Object) m.containsKey(k)} would return
   * {@code true}.)
   *
   * @param key
   *        key with which the specified value is to be associated
   * @param value
   *        value to be associated with the specified key
   *
   * @return the previous value associated with {@code key}, or
   *         {@code null} if there was no mapping for {@code key}.
   *
   * @throws NullPointerException if the specified key or value is null
   */
  @Override
  public final V put(K key, V value) {
    Check.notNull(key, "key == null");
    Check.notNull(value, "value == null");

    return putUnchecked(key, value);
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @param m
   *        ignored (this operation in not yet implemented)
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }

  /**
   * Returns an {@link UtilUnmodifiableMap} copy of this map.
   *
   * <p>
   * The returned {@code UnmodifiableMap} will contain all of the entries from
   * this
   * map.
   *
   * <p>
   * The returned map will be a copy in the sense that, after this method
   * returns, modifying this map will have no effect on the returned (copied)
   * one.
   *
   * <p>
   * Note, however, that the behaviour of this method is undefined if this map
   * is modified while the copy is being made.
   *
   * @return an {@link UtilUnmodifiableMap} copy of this set
   */
  public UtilUnmodifiableMap<K, V> toUnmodifiableMap() {
    switch (size) {
      case 0:
        return UtilUnmodifiableMap.of();
      default:
        var copy = Arrays.copyOf(array, array.length);

        return new UtilUnmodifiableMap<K, V>(copy, size);
    }
  }

  void insert(int index, Object key, Object value) {
    set(index, key, value);

    size++;

    rehashIfNecessary();
  }

  final V putUnchecked(Object key, Object value) {
    firstResizeIfNecessary();

    int index, marker;
    index = marker = hashIndex(key);

    var existing = array[index];

    if (existing == null) {
      insert(index, key, value);

      return null;
    }

    else if (existing.equals(key)) {
      return replace(index, key, value);
    }

    else {
      index = index + 2;
    }

    while (index < array.length) {
      existing = array[index];

      if (existing == null) {
        insert(index, key, value);

        return null;
      }

      else if (existing.equals(key)) {
        return replace(index, key, value);
      }

      else {
        index = index + 2;
      }
    }

    index = 0;

    while (index < marker) {
      existing = array[index];

      if (existing == null) {
        insert(index, key, value);

        return null;
      }

      else if (existing.equals(key)) {
        return replace(index, key, value);
      }

      else {
        index = index + 2;
      }
    }

    throw new UnsupportedOperationException("Implement me");
  }

  @SuppressWarnings("unchecked")
  V replace(int keyIndex, Object key, Object value) {
    int valueIndex;
    valueIndex = keyIndex + 1;

    var existingValue = array[valueIndex];

    array[valueIndex] = value;

    return (V) existingValue;
  }

  private void firstResizeIfNecessary() {
    if (array == Util.EMPTY_OBJECT_ARRAY) {
      resizeTo(FIRST_RESIZE);
    }
  }

  private void rehashIfNecessary() {
    if (size < rehashSize) {
      return;
    }

    if (array.length == MAX_ARRAY_LENGTH) {
      throw new OutOfMemoryError("backing array already at max allowed length");
    }

    var previous = array;

    var newLength = array.length << 1;

    if (newLength < 0) {
      newLength = MAX_ARRAY_LENGTH;
    }

    resizeTo(newLength);

    for (int i = 0, length = previous.length; i < length; i = i + 2) {
      var key = previous[i];

      if (key == null) {
        continue;
      }

      var value = previous[i + 1];

      rehashPut(key, value);
    }
  }

  private void rehashPut(Object key, Object value) {
    int index, marker;
    index = marker = hashIndex(key);

    var existing = array[index];

    if (existing == null) {
      set(index, key, value);

      return;
    }

    else {
      index = index + 2;
    }

    while (index < array.length) {
      existing = array[index];

      if (existing == null) {
        set(index, key, value);

        return;
      }

      else {
        index = index + 2;
      }
    }

    index = 0;

    while (index < marker) {
      existing = array[index];

      if (existing == null) {
        set(index, key, value);

        return;
      }

      else {
        index = index + 2;
      }
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private void resizeTo(int size) {
    array = new Object[size];

    var hashLength = size >> 1;

    hashMask = hashLength - 1;

    rehashSize = (int) (hashLength * loadFactor);
  }

  private void set(int index, Object key, Object value) {
    array[index] = key;

    array[index + 1] = value;
  }

}