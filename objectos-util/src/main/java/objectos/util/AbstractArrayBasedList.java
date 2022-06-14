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
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import objectos.lang.Check;
import objectos.lang.HashCode;
import objectos.lang.ToString;

abstract class AbstractArrayBasedList<E>
    extends AbstractBaseCollection<E>
    implements
    List<E>,
    RandomAccess {

  Object[] array = ObjectArrays.empty();

  int size = 0;

  AbstractArrayBasedList() {}

  /**
   * Returns {@code true} if this list contains the specified element. More
   * formally, returns {@code true} if and only if this list contains at least
   * one element {@code e} such that {@code e.equals(o)}.
   *
   * <p>
   * This method does not throw a {@code ClassCastException}.
   *
   * @param o
   *        an element to check for presence in this list
   *
   * @return {@code true} if this list contains the specified value
   */
  @Override
  public final boolean contains(Object o) {
    if (o == null) {
      return false;
    }

    for (int i = 0; i < size; i++) {
      Object thisElem;
      thisElem = array[i];

      if (thisElem.equals(o)) {
        return true;
      }
    }

    return false;
  }

  /**
   * <p>
   * Compares the specified object with this list for equality. Returns
   * {@code true} if and only if
   *
   * <ul>
   * <li>the specified object is also a {@link List};</li>
   * <li>both lists have same size; and</li>
   * <li>the first element of this list is equal to the first element of the
   * specified list, and the second element of this list is equal to the second
   * element of the specified list, and so on until all elements of both lists
   * are compared and are equal to each other.</li>
   * </ul>
   *
   * @param obj
   *        the object to be compared for equality with this list
   *
   * @return {@code true} if the specified object is equal to this list
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof List && equals0((List<?>) obj);
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this list.
   *
   * <p>
   * The string representation <i>may</i> contain:
   *
   * <ul>
   * <li>the simple name of the list's class; and</li>
   * <li>the string representation of each element paired with its index</li>
   * </ul>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.formatStart(toString, this);

    if (size > 0) {
      int length = sizeDigits();

      ToString.formatFirstPair(
        toString, level,
        indexName(0, length), array[0]
      );

      for (int i = 1; i < size; i++) {
        ToString.formatNextPair(
          toString, level,
          indexName(i, length), array[i]
        );
      }
    }

    ToString.formatEnd(toString, level);
  }

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of the element to return
   *
   * @return the element at the specified position in this list
   *
   * @throws IndexOutOfBoundsException if the index is out of range
   *         ({@code index < 0 || index >= size()})
   */
  @SuppressWarnings("unchecked")
  @Override
  public final E get(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException("Index=" + index + "; Size=" + size);
    }

    return (E) array[index];
  }

  /**
   * Returns the hash code value of this list.
   *
   * @return the hash code value of this list
   */
  @Override
  public final int hashCode() {
    int result;
    result = HashCode.start();

    for (int i = 0; i < size; i++) {
      Object object;
      object = array[i];

      result = HashCode.update(result, object);
    }

    return result;
  }

  /**
   * Returns the index of the first occurrence of the specified element
   * in this list, or -1 if this list does not contain the element.
   *
   * <p>
   * More formally, returns the lowest index {@code i} such that
   * {@code get(i).equals(o)}, or -1 if there is no such index.
   *
   * @param o element to search for
   *
   * @return the index of the first occurrence of the specified element in
   *         this list, or -1 if this list does not contain the element
   */
  @Override
  public final int indexOf(Object o) {
    int result;
    result = -1;

    if (o == null) {
      return result;
    }

    for (int i = 0; i < size; i++) {
      Object element;
      element = array[i];

      if (element.equals(o)) {
        result = i;

        break;
      }
    }

    return result;
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  @Override
  public final UnmodifiableIterator<E> iterator() {
    return new ThisIterator();
  }

  /**
   * Returns a new string by joining together, in order, the string
   * representation of each element in this list.
   *
   * <p>
   * The string representation of each element is obtained by invoking its
   * {@link Object#toString()} method.
   *
   * @return a new string representing this list
   */
  @Override
  public final String join() {
    if (size == 0) {
      return "";
    }

    if (size == 1) {
      Object o;
      o = array[0];

      return o.toString();
    }

    StringBuilder sb;
    sb = new StringBuilder();

    for (int i = 0; i < size; i++) {
      sb.append(array[i]);
    }

    return sb.toString();
  }

  /**
   * Returns a new string by joining together, in order, the string
   * representation of each element in this list separated by the specified
   * delimiter.
   *
   * <p>
   * The string representation of each element is obtained by invoking its
   * {@link Object#toString()} method.
   *
   * @param delimiter
   *        the separator to use
   *
   * @return a new string representing this list
   */
  @Override
  public final String join(String delimiter) {
    Check.notNull(delimiter, "delimiter == null");

    if (size == 0) {
      return "";
    }

    Object o;

    if (size == 1) {
      o = array[0];

      return o.toString();
    }

    StringBuilder sb;
    sb = new StringBuilder();

    o = array[0];

    sb.append(o.toString());

    for (int i = 1; i < size; i++) {
      sb.append(delimiter);

      o = array[i];

      sb.append(o.toString());
    }

    return sb.toString();
  }

  /**
   * Returns a new string by joining together, in order, the string
   * representations of each element in this list separated by the specified
   * {@code delimiter} and with the specified {@code prefix} and {@code suffix}.
   *
   * @param delimiter
   *        the separator to use
   * @param prefix
   *        the first part of the output
   * @param suffix
   *        the last part of the output
   *
   * @return a new string representing this list
   */
  @Override
  public final String join(String delimiter, String prefix, String suffix) {
    Check.notNull(delimiter, "delimiter == null");
    Check.notNull(prefix, "prefix == null");
    Check.notNull(suffix, "suffix == null");

    if (size == 0) {
      return prefix + suffix;
    }

    Object o;

    if (size == 1) {
      o = array[0];

      return prefix + o.toString() + suffix;
    }

    StringBuilder sb;
    sb = new StringBuilder();

    sb.append(prefix);

    o = array[0];

    sb.append(o.toString());

    for (int i = 1; i < size; i++) {
      sb.append(delimiter);

      o = array[i];

      sb.append(o.toString());
    }

    sb.append(suffix);

    return sb.toString();
  }

  /**
   * Returns the index of the last occurrence of the specified element
   * in this list, or -1 if this list does not contain the element.
   *
   * <p>
   * More formally, returns the highest index {@code i} such that
   * {@code get(i).equals(o)}, or -1 if there is no such index.
   *
   * @param o element to search for
   *
   * @return the index of the last occurrence of the specified element in
   *         this list, or -1 if this list does not contain the element
   */
  @Override
  public final int lastIndexOf(Object o) {
    int result;
    result = -1;

    for (int i = size - 1; i >= 0; i--) {
      Object element;
      element = array[i];

      if (element.equals(o)) {
        result = i;

        break;
      }
    }

    return result;
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @return this method does not return as it always throws an exception
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final ListIterator<E> listIterator() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @return this method does not return as it always throws an exception
   *
   * @param index
   *        ignored (this operation in not yet implemented)
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final ListIterator<E> listIterator(int index) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param index
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final E remove(int index) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param index
   *        ignored (this operation is not supported)
   * @param element
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final E set(int index, E element) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the size of this list. The size of a list is equal to the number of
   * elements it contains.
   *
   * @return the size of this list
   */
  @Override
  public final int size() {
    return size;
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @return this method does not return as it always throws an exception
   *
   * @param fromIndex
   *        ignored (this operation in not yet implemented)
   * @param toIndex
   *        ignored (this operation in not yet implemented)
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public final List<E> subList(int fromIndex, int toIndex) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Returns a new array instance containing all of the elements in this
   * list in order. The returned array length is equal to the size of
   * this list.
   *
   * @return a new array instance containing all of the elements in this list
   *
   * @see Arrays#copyOf(Object[], int)
   */
  @Override
  public final Object[] toArray() {
    return Arrays.copyOf(array, size);
  }

  /**
   * Returns an array, either the specified array or a new array instance,
   * containing all of the elements in this list in order.
   *
   * <p>
   * The specified array is used as the return value if it is large enough to
   * hold all of the elements in this list. Additionally, iff the specified
   * array is such that {@code a.length > size()} then the position after the
   * last element is set to {@code null}.
   *
   * <p>
   * If the specified array is not large enough, then a new array is created,
   * with the same runtime type of the specified array, and used as the return
   * value.
   *
   * @param a
   *        the array into which the elements of the list are to be stored, if
   *        it is big enough; otherwise, a new array of the same runtime type is
   *        allocated for this purpose.
   *
   * @return an array containing the elements of the list
   *
   * @see Arrays#copyOf(Object[], int, Class)
   */
  @Override
  @SuppressWarnings("unchecked")
  public final <T> T[] toArray(T[] a) {
    Check.notNull(a, "a == null");

    if (a.length < size) {
      Class<? extends Object[]> newType;
      newType = a.getClass();

      Object[] copy;
      copy = Arrays.copyOf(array, size, newType);

      return (T[]) copy;
    }

    System.arraycopy(array, 0, a, 0, size);

    if (a.length > size) {
      a[size] = null;
    }

    return a;
  }

  /**
   * Returns the only element in this list.
   *
   * @return the only element in this list
   */
  @Override
  @SuppressWarnings("unchecked")
  protected final E getOnlyImpl() {
    return (E) array[0];
  }

  final boolean containsAllIterable0(Iterable<?> iterable) {
    outer: //
    for (Object test : iterable) {
      for (int i = 0; i < size; i++) {
        Object element;
        element = array[i];

        if (element.equals(test)) {
          continue outer;
        }
      }

      return false;
    }

    return true;
  }

  private boolean equals0(List<?> that) {
    int size;
    size = size();

    if (size != that.size()) {
      return false;
    }

    for (int i = 0; i < size; i++) {
      E e;
      e = get(i);

      Object o;
      o = that.get(i);

      // e is guaranteed to be not null
      if (!e.equals(o)) {
        return false;
      }
    }

    return true;
  }

  private class ThisIterator extends UnmodifiableIterator<E> {

    private int index;

    @Override
    public final boolean hasNext() {
      return index < size;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      else {
        Object result;
        result = array[index];

        index++;

        return (E) result;
      }
    }

  }

}