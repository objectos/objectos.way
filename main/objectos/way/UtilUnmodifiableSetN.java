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

import java.util.Set;

final class UtilUnmodifiableSetN<E> extends UtilUnmodifiableSet<E> {
  private final Object[] array;

  private final int size;

  UtilUnmodifiableSetN(Object[] array, int size) {
    this.array = array;

    this.size = size;
  }

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
    return UtilSets.containsImpl(array, size, o);
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
    return UtilSets.equalsImpl(this, obj);
  }

  /**
   * Returns the hash code value of this set.
   *
   * @return the hash code value of this set
   */
  @Override
  public final int hashCode() {
    return UtilSets.hashCodeImpl(array);
  }

  /**
   * Returns an iterator over the elements in this set. The elements
   * are returned in no particular order.
   *
   * @return an iterator over the elements in this set
   */
  @Override
  public final Util.UnmodifiableIterator<E> iterator() {
    return new UtilSets.SetIterator<>(array);
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
   * Returns a new array instance containing all of the elements in this set.
   * The returned array length is equal to the size of this set.
   *
   * @return a new array instance containing all of the elements in this set
   */
  @Override
  public final Object[] toArray() {
    return UtilSets.toArrayImpl(array, size);
  }

  /**
   * Returns an array, either the specified array or a new array instance,
   * containing all of the elements in this set.
   *
   * <p>
   * The specified array is used as the return value if it is large enough to
   * hold all of the elements in this set. Additionally, if the specified
   * array is such that {@code a.length > size()} then the position after the
   * last element is set to {@code null}.
   *
   * <p>
   * If the specified array is not large enough, then a new array is created,
   * with the same runtime type of the specified array, and used as the return
   * value.
   *
   * @param a
   *        the array into which the elements of the set are to be stored, if
   *        it is big enough; otherwise, a new array of the same runtime type
   *        is
   *        allocated for this purpose.
   *
   * @return an array containing the elements of the set
   */
  @Override
  public final <T> T[] toArray(T[] a) {
    return UtilSets.toArrayImpl(array, size, a);
  }

  @Override
  public final String toString() {
    return UtilSets.toStringImpl(this, array);
  }

}