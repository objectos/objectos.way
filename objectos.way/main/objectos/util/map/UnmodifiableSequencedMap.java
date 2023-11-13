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
package objectos.util.map;

import objectos.util.array.ObjectArrays;
import objectos.util.collection.UnmodifiableIterator;

/**
 * A {@link UnmodifiableMap} variant with a predictable iteration order.
 *
 * @param <K> type of the keys in this map
 * @param <V> type of the values in this map
 */
public final class UnmodifiableSequencedMap<K, V> extends UnmodifiableMap<K, V> {

  private static final UnmodifiableSequencedMap<Object, Object> EMPTY
      = new UnmodifiableSequencedMap<Object, Object>(
        ObjectArrays.empty(),

        0,

        ObjectArrays.empty()
      );

  private final Object[] iteratorArray;

  UnmodifiableSequencedMap(Object[] array, int size, Object[] iteratorArray) {
    super(array, size);

    this.iteratorArray = iteratorArray;
  }

  @SuppressWarnings("unchecked")
  static <K, V> UnmodifiableSequencedMap<K, V> orderedEmpty() {
    return (UnmodifiableSequencedMap<K, V>) EMPTY;
  }

  @Override
  final UnmodifiableIterator<Entry<K, V>> entryIterator() {
    return Maps.orderedEntryIterator(iteratorArray, iteratorArray.length);
  }

  @Override
  final UnmodifiableIterator<K> keyIterator() {
    return Maps.orderedKeyIterator(iteratorArray, iteratorArray.length);
  }

  @Override
  final UnmodifiableIterator<V> valueIterator() {
    return Maps.orderedValueIterator(iteratorArray, iteratorArray.length);
  }

}