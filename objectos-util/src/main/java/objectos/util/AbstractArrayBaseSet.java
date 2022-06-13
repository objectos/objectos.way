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
import java.util.NoSuchElementException;
import java.util.Set;
import objectos.lang.HashCode;

abstract class AbstractArrayBaseSet<E>
    extends AbstractBaseCollection<E>
    implements Set<E> {

  static final int MAX_POSITIVE_POWER_OF_TWO = 1 << 30;

  Object[] array = ObjectArrays.empty();

  int hashMask;

  int size = 0;

  /**
   * Returns {@code true} if this set contains the specified element. More
   * formally, returns {@code true} if and only if this set contains at least
   * one element {@code e} such that {@code e.equals(o)}.
   *
   * @param o
   *        an element to check for presence in this set
   *
   * @return {@code true} if this set contains the specified value
   */
  @Override
  public final boolean contains(Object o) {
    if (o == null) {
      return false;
    }

    if (isEmpty()) {
      return false;
    }

    int index, marker;
    index = marker = hashIndex(o);

    Object candidate;

    while (index < array.length) {
      candidate = array[index];

      if (candidate == null) {
        return false;
      }

      else if (candidate.equals(o)) {
        return true;
      }

      else {
        index++;
      }
    }

    index = 0;

    while (index < marker) {
      candidate = array[index];

      if (candidate == null) {
        return false;
      }

      else if (candidate.equals(o)) {
        return true;
      }

      else {
        index++;
      }
    }

    return false;
  }

  /**
   * <p>
   * Compares the specified object with this set for equality. Returns
   * {@code true} if and only if
   *
   * <ul>
   * <li>the specified object is also a {@link Set};</li>
   * <li>both sets have same size; and</li>
   * <li>each element in this set is also present in the specified set.</li>
   * </ul>
   *
   * @param obj
   *        the object to be compared for equality with this set
   *
   * @return {@code true} if the specified object is equal to this set
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof Set && equals0((Set<?>) obj);
  }

  /**
   * Returns an iterator over the elements in this set. The elements
   * are returned in no particular order.
   *
   * @return an iterator over the elements in this set
   */
  @Override
  public final UnmodifiableIterator<E> iterator() {
    return new ThisIterator();
  }

  /**
   * Returns the size of this set. The size of a set is equal to the number of
   * elements it contains.
   *
   * @return the size of this set
   */
  @Override
  public final int size() {
    return size;
  }

  /**
   * Returns the only element in this set.
   *
   * @return the only element in this set
   */
  @SuppressWarnings("unchecked")
  @Override
  protected final E getOnlyImpl() {
    for (int i = 0; i < array.length; i++) {
      Object candidate;
      candidate = array[i];

      if (candidate != null) {
        return (E) candidate;
      }
    }

    throw new AssertionError("Should not have happened. This method is called when size == 1");
  }

  final int hashIndex(Object o) {
    int hashCode;
    hashCode = HashCode.of(o);

    return hashCode & hashMask;
  }

  private boolean equals0(Set<?> that) {
    int size;
    size = size();

    if (size != that.size()) {
      return false;
    }

    Iterator<?> iter;
    iter = iterator();

    while (iter.hasNext()) {
      Object o;
      o = iter.next();

      if (!that.contains(o)) {
        return false;
      }
    }

    return true;
  }

  private class ThisIterator extends UnmodifiableIterator<E> {

    private boolean computed;

    private int index;

    private Object next;

    @Override
    public final boolean hasNext() {
      computeIfNecessary();

      return next != null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      } else {
        Object result;
        result = next;

        computed = false;

        next = null;

        return (E) result;
      }
    }

    private void computeIfNecessary() {
      if (!computed) {
        while (index < array.length) {
          next = array[index];

          index++;

          if (next != null) {
            break;
          }
        }

        computed = true;
      }
    }

  }

}