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
package objectos.util;

import java.util.NoSuchElementException;

final class ArrayIterator<E> extends UnmodifiableIterator<E> {

  private final Object[] array;

  private int index;

  private final int size;

  ArrayIterator(Object[] array) {
    this(array, array.length);
  }

  ArrayIterator(Object[] array, int size) {
    this.array = array;
    this.size = size;
  }

  @Override
  public final boolean hasNext() {
    return index < size;
  }

  @SuppressWarnings("unchecked")
  @Override
  public final E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    else {
      return (E) array[index++];
    }
  }

}