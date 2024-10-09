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

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class SingletonIterator<E> implements Iterator<E> {

  private boolean consumed;

  private final E element;

  public SingletonIterator(E element) {
    this.element = element;
  }

  @Override
  public final boolean hasNext() {
    return !consumed;
  }

  @Override
  public final E next() {
    if (consumed) {
      throw new NoSuchElementException();
    }

    else {
      consumed = true;

      return element;
    }
  }

  @Override
  public final void remove() {
    throw new UnsupportedOperationException();
  }

}
