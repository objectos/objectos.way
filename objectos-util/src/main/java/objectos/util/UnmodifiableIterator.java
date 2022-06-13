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

import java.util.Iterator;

/**
 * An {@link java.util.Iterator} implementation that does not support the remove
 * operation.
 *
 * @param <E> the type of the elements in this iterator
 */
public abstract class UnmodifiableIterator<E> implements Iterator<E> {

  /**
   * Sole constructor.
   */
  protected UnmodifiableIterator() {}

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
  public final void remove() {
    throw new UnsupportedOperationException();
  }

}
