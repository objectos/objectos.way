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

import java.util.Arrays;
import java.util.Comparator;

abstract class MutableListJava6<E> extends AbstractMutableList<E> {

  /**
   * Creates a new {@code MutableList} instance.
   */
  public MutableListJava6() {}

  MutableListJava6(Object[] elements) {
    super(elements);
  }

  /**
   * Sorts the elements of this list according to the specified
   * {@link Comparator} instance.
   *
   * @param c
   *        the comparator defining the order for the elements of this list
   *
   * @see Arrays#sort(Object[], int, int, Comparator)
   */
  @Override
  public final void sort(Comparator<? super E> c) {
    sortImpl(c);
  }

}