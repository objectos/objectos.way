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
package objectos.util;

public final class ArrayBackedIterator<E> extends AbstractIterator<E> {

  private int index;

  private final E[] values;

  public ArrayBackedIterator(E[] values) {
    this.values = values;
  }

  @Override
  public final boolean hasNext() {
    return index < values.length;
  }

  @Override
  public final E next() {
    return values[index++];
  }

  public final void reset() {
    index = 0;
  }

}
