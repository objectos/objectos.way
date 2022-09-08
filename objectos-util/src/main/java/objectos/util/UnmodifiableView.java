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

import java.util.Collection;
import java.util.Set;

/**
 * An unmodifiable view of a map's keys, values or entries. All of the mutator
 * methods throw an {@link UnsupportedOperationException} when invoked.
 *
 * @param <E> the type of the elements in this view
 */
public abstract class UnmodifiableView<E> extends AbstractBaseCollection<E> implements Set<E> {

  UnmodifiableView() {}

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param e
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean add(E e) {
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
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean addAll(Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

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
  public final void clear() {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>
   * Compares the specified object with this view for equality. Returns
   * {@code true} if and only if
   *
   * <ul>
   * <li>the specified object is also a {@link Collection};</li>
   * <li>both collections have same size; and</li>
   * <li>each element in this view is also present in the specified
   * collection.</li>
   * </ul>
   *
   * @param obj
   *        the object to be compared for equality with this view
   *
   * @return {@code true} if the specified object is equal to this view
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof Collection<?> that && equals0(that);
  }

  @Override
  public Object[] toArray() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public <T> T[] toArray(T[] a) {
    throw new UnsupportedOperationException("Implement me");
  }

  private boolean equals0(Collection<?> that) {
    var size = size();

    if (size != that.size()) {
      return false;
    }

    var iter = iterator();

    while (iter.hasNext()) {
      var o = iter.next();

      if (!that.contains(o)) {
        return false;
      }
    }

    return true;
  }

}