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

import java.util.Arrays;
import java.util.Map;
import objectos.lang.Check;

/**
 * A hash-based implementation of the {@link Map} interface.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
public class MutableMap<K, V> extends AbstractArrayBasedMap<K, V> {

  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  private static final int FIRST_RESIZE = 8;

  private static final int MAX_ARRAY_LENGTH = MAX_POSITIVE_POWER_OF_TWO;

  private final float loadFactor = DEFAULT_LOAD_FACTOR;

  private int rehashSize;

  /**
   * Creates a new {@code MutableMap} instance.
   */
  public MutableMap() {}

  /**
   * Creates and returns a new {@code MutableMap} instance.
   *
   * <p>
   * This method is mainly provided as a convenience for Java Multi-Release
   * codebases. In particular codebases that must support versions prior to Java
   * 7 and, therefore, cannot use the diamond operator.
   *
   * @param <K> type of the keys in this map
   * @param <V> type of the values in this map
   *
   * @return a new {@code MutableMap} instance
   */
  public static <K, V> MutableMap<K, V> create() {
    return new MutableMap<K, V>();
  }

  /**
   * Removes all of the mappings in this map.
   */
  @Override
  public void clear() {
    Arrays.fill(array, null);

    size = 0;
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
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Returns an {@link ImmutableMap} copy of this map.
   *
   * <p>
   * The returned {@code ImmutableMap} will contain all of the entries from this
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
   * @return an {@link ImmutableMap} copy of this set
   */
  public ImmutableMap<K, V> toImmutableMap() {
    switch (size) {
      case 0:
        return ImmutableMap.empty();
      default:
        Object[] copy;
        copy = Arrays.copyOf(array, array.length);

        return new ImmutableMap<K, V>(copy, size);
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

    Object existing;
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

    Object existingValue;
    existingValue = array[valueIndex];

    array[valueIndex] = value;

    return (V) existingValue;
  }

  private void firstResizeIfNecessary() {
    if (array == ObjectArrays.empty()) {
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

    Object[] previous;
    previous = array;

    int newLength;
    newLength = array.length << 1;

    if (newLength < 0) {
      newLength = MAX_ARRAY_LENGTH;
    }

    resizeTo(newLength);

    for (int i = 0, length = previous.length; i < length; i = i + 2) {
      Object key;
      key = previous[i];

      if (key == null) {
        continue;
      }

      Object value;
      value = previous[i + 1];

      rehashPut(key, value);
    }
  }

  private void rehashPut(Object key, Object value) {
    int index, marker;
    index = marker = hashIndex(key);

    Object existing;
    existing = array[index];

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

    int hashLength;
    hashLength = size >> 1;

    hashMask = hashLength - 1;

    rehashSize = (int) (hashLength * loadFactor);
  }

  private void set(int index, Object key, Object value) {
    array[index] = key;

    array[index + 1] = value;
  }

}