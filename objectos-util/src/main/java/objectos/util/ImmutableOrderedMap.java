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

/**
 * A {@link ImmutableMap} variant with a predictable iteration order.
 */
public final class ImmutableOrderedMap<K, V> extends ImmutableMap<K, V> {

  private static final ImmutableOrderedMap<Object, Object> EMPTY
      = new ImmutableOrderedMap<Object, Object>(
        ObjectArrays.empty(),

        0,

        ObjectArrays.empty()
      );

  private final Object[] iteratorArray;

  ImmutableOrderedMap(Object[] array, int size, Object[] iteratorArray) {
    super(array, size);

    this.iteratorArray = iteratorArray;
  }

  @SuppressWarnings("unchecked")
  static <K, V> ImmutableOrderedMap<K, V> orderedEmpty() {
    return (ImmutableOrderedMap<K, V>) EMPTY;
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