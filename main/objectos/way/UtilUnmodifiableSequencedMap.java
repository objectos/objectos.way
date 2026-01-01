/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

import java.util.SequencedMap;

/**
 * A {@link UtilUnmodifiableMap} variant with a predictable iteration order.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
final class UtilUnmodifiableSequencedMap<K, V> extends UtilUnmodifiableMap<K, V> implements SequencedMap<K, V> {

  private static final UtilUnmodifiableSequencedMap<Object, Object> EMPTY
      = new UtilUnmodifiableSequencedMap<Object, Object>(
          Util.EMPTY_OBJECT_ARRAY,

          0,

          Util.EMPTY_OBJECT_ARRAY
      );

  private final Object[] iteratorArray;

  UtilUnmodifiableSequencedMap(Object[] array, int size, Object[] iteratorArray) {
    super(array, size);

    this.iteratorArray = iteratorArray;
  }

  @SuppressWarnings("unchecked")
  static <K, V> UtilUnmodifiableSequencedMap<K, V> orderedEmpty() {
    return (UtilUnmodifiableSequencedMap<K, V>) EMPTY;
  }

  @Override
  public final SequencedMap<K, V> reversed() {
    throw new UnsupportedOperationException();
  }

  @Override
  final Util.UnmodifiableIterator<Entry<K, V>> entryIterator() {
    return UtilMaps.orderedEntryIterator(iteratorArray, iteratorArray.length);
  }

  @Override
  final Util.UnmodifiableIterator<K> keyIterator() {
    return UtilMaps.orderedKeyIterator(iteratorArray, iteratorArray.length);
  }

  @Override
  final Util.UnmodifiableIterator<V> valueIterator() {
    return UtilMaps.orderedValueIterator(iteratorArray, iteratorArray.length);
  }

}