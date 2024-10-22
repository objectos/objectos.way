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
import java.util.Iterator;
import objectos.util.ArrayBackedIterator;

public final class TestingArrayBackedIterable<E> implements Iterable<E> {

  private final E[] array;

  public TestingArrayBackedIterable(E[] array) {
    this.array = array;
  }

  @SafeVarargs
  @SuppressWarnings("varargs")
  public static <E> TestingArrayBackedIterable<E> of(E... elements) {
    E[] copy = Arrays.copyOf(elements, elements.length);

    return new TestingArrayBackedIterable<>(copy);
  }

  @Override
  public final Iterator<E> iterator() {
    return new ArrayBackedIterator<E>(array);
  }

}