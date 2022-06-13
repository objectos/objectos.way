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

import java.util.Comparator;
import java.util.function.UnaryOperator;

abstract class ImmutableListJava8<E> extends AbstractImmutableList<E> {

  ImmutableListJava8() {}

  ImmutableListJava8(Object[] array) {
    super(array);
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param operator
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void replaceAll(UnaryOperator<E> operator) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param c
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void sort(Comparator<? super E> c) {
    throw new UnsupportedOperationException();
  }

}