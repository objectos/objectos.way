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
 * An array-based {@link MutableCollection} and
 * {@link java.util.List} implementation. The main goal of this class is to
 * provide a single mutable list API for Java Multi-Release codebases.
 *
 * <p>
 * Please note that this is not a general-purpose {@link java.util.List}
 * implementation. First, this implementation does not permit {@code null}
 * values. Second, only selected "mutator" operations, specified by either
 * {@link java.util.List} or by {@link java.util.Collection}, are supported.
 * Third, iterators produced by this class will have undefined behaviour if the
 * underlying set is modified during iteration, i.e., the iterators are not
 * <i>fail-fast</i> as defined by
 * {@link java.util.ConcurrentModificationException}.
 *
 * <p>
 * An {@link java.util.ArrayList} should be used if a general-purpose
 * implementation is required.
 *
 * <p>
 * For the specific cases where this class can be used, it should behave in
 * similar ways to an {@link java.util.ArrayList}.
 *
 * @param <E> type of the elements in this list
 *
 * @see BaseCollection
 * @see MutableCollection
 * @see java.util.List
 */
public final class MutableList<E> extends MutableListJava8<E> {

  /**
   * Creates a new {@code MutableList} instance.
   */
  public MutableList() {
    super();
  }

  MutableList(java.lang.Object[] elements) {
    super(elements);
  }

}