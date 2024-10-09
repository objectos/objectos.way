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

import java.util.Arrays;
import java.util.SequencedMap;
import objectos.util.UnmodifiableIterator;

/**
 * A {@link UtilMap} variant with a predictable iteration order.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
final class UtilSequencedMap<K, V> extends UtilMap<K, V> implements SequencedMap<K, V> {

  private Object[] iteratorArray;

  /**
   * Creates a new {@code MutableOrderedMap} instance.
   */
  public UtilSequencedMap() {
    iteratorArray = Util.EMPTY_OBJECT_ARRAY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void clear() {
    super.clear();

    Arrays.fill(iteratorArray, null);
  }

  @Override
  public final SequencedMap<K, V> reversed() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns an {@link UtilUnmodifiableSequencedMap} copy of this map.
   *
   * <p>
   * The returned {@code ImmutableOrderedMap} will contain all of the entries
   * from this map in the same order.
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
   * @return an {@link UtilUnmodifiableSequencedMap} copy of this set
   */
  @Override
  public final UtilUnmodifiableSequencedMap<K, V> toUnmodifiableMap() {
    switch (size) {
      case 0:
        return UtilUnmodifiableSequencedMap.orderedEmpty();
      default:
        return new UtilUnmodifiableSequencedMap<K, V>(
            Arrays.copyOf(array, array.length),

            size,

            Arrays.copyOf(iteratorArray, size << 1)
        );
    }
  }

  @Override
  final UnmodifiableIterator<Entry<K, V>> entryIterator() {
    return UtilMaps.orderedEntryIterator(iteratorArray, size << 1);
  }

  @Override
  final void insert(int index, Object key, Object value) {
    var keyIndex = size << 1;

    var valueIndex = keyIndex + 1;

    super.insert(index, key, value);

    if (iteratorArray.length != array.length) {
      iteratorArray = Arrays.copyOf(iteratorArray, array.length);
    }

    iteratorArray[keyIndex] = key;

    iteratorArray[valueIndex] = value;
  }

  @Override
  final UnmodifiableIterator<K> keyIterator() {
    return UtilMaps.orderedKeyIterator(iteratorArray, size << 1);
  }

  @Override
  final V replace(int keyIndex, Object key, Object value) {
    for (int i = 0, iteratorSize = size << 1; i < iteratorSize; i = i + 2) {
      var maybeKey = iteratorArray[i];

      if (maybeKey.equals(key)) {
        iteratorArray[i + 1] = value;

        break;
      }
    }

    return super.replace(keyIndex, key, value);
  }

  @Override
  final UnmodifiableIterator<V> valueIterator() {
    return UtilMaps.orderedValueIterator(iteratorArray, size << 1);
  }

}