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

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import objectos.lang.object.Check;

/**
 * A hash-based unmodifiable implementation of the {@link Map} interface.
 *
 * <p>
 * After creation, instances of this class do not permit adding nor removing of
 * elements. Any mutator method will throw an
 * {@link UnsupportedOperationException} when called. This is true for mutator
 * methods present in this class and for mutator methods present in the returned
 * iterators.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
class UtilUnmodifiableMap<K, V> extends UtilArrayBasedMap<K, V> {

  private static final UtilUnmodifiableMap<Object, Object> EMPTY
      = new UtilUnmodifiableMap<Object, Object>();

  UtilUnmodifiableMap(Object[] array, int size) {
    this.array = array;

    this.size = size;

    hashMask = (array.length >> 1) - 1;
  }

  private UtilUnmodifiableMap() {}

  @SuppressWarnings("unchecked")
  static <K, V> UtilUnmodifiableMap<K, V> of() {
    return (UtilUnmodifiableMap<K, V>) EMPTY;
  }

  static <K, V> UtilUnmodifiableMap<K, V> of(K key, V value) {
    Check.notNull(key, "key == null");
    Check.notNull(value, "value == null");

    var map = new UtilMap<K, V>();

    map.put(key, value);

    return map.toUnmodifiableMap();
  }

  static <K, V> UtilUnmodifiableMap<K, V> of(K k1, V v1, K k2, V v2) {
    Check.notNull(k1, "k1 == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(k2, "k2 == null");
    Check.notNull(v2, "v2 == null");

    var map = new UtilMap<K, V>();

    map.put(k1, v1);
    map.put(k2, v2);

    return map.toUnmodifiableMap();
  }

  static <K, V> UtilUnmodifiableMap<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
    Check.notNull(k1, "k1 == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(k2, "k2 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(k3, "k3 == null");
    Check.notNull(v3, "v3 == null");

    var map = new UtilMap<K, V>();

    map.put(k1, v1);
    map.put(k2, v2);
    map.put(k3, v3);

    return map.toUnmodifiableMap();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void clear() {
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
   * @param remappingFunction
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
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
   * @param mappingFunction
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
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
   * @param remappingFunction
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final V computeIfPresent(
      K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
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
  public final V put(K key, V value) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param m
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void putAll(Map<? extends K, ? extends V> m) {
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
  public final V putIfAbsent(K key, V value) {
    throw new UnsupportedOperationException();
  }

}