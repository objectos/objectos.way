/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;

/**
 * An array-based {@link UtilGrowableCollection} and
 * {@link java.util.List} implementation. The main goal of this class is to
 * provide a single mutable list API to be used <em>internally</em> by the
 * Objectos libraries themselves.
 *
 * <p>
 * Please note that this is not a general-purpose {@link java.util.List}
 * implementation. First, this implementation does not permit {@code null}
 * values. Second, only selected "mutator" operations, specified either by
 * {@link java.util.List} or by {@link java.util.Collection}, are supported.
 * Third, iterators produced by this class will have undefined behaviour if the
 * underlying list is modified during iteration, i.e., the iterators are not
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
 * @see UtilGrowableCollection
 * @see java.util.List
 */
final class UtilList<E> extends UtilBaseCollection<E> implements List<E> {

  private Object[] data = Util.EMPTY_OBJECT_ARRAY;

  private int size = 0;

  UtilList() {}

  /**
   * Appends the specified element to this list or throws an exception if the
   * element is {@code null}.
   *
   * @param e
   *        an element to append to this list
   *
   * @return {@code true}
   */
  @Override
  public final boolean add(E e) {
    Check.notNull(e, "e == null");

    return add0(e);
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param index
   *        ignored (the operation is not supported)
   * @param element
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void add(int index, E element) {
    throw new UnsupportedOperationException();
  }

  /**
   * Appends all of the elements in the specified collection to the end of
   * this list, in the order that they are returned by the
   * specified collection's Iterator.
   *
   * <p>
   * The behavior of this operation is undefined if the specified collection is
   * modified while the operation is in progress. (This implies that the
   * behavior of this call is undefined if the specified collection is this
   * list, and this list is nonempty.)
   *
   * @param c
   *        collection containing elements to be added to this list
   *
   * @return {@code true} if this list changed as a result of the call
   */
  @Override
  public final boolean addAll(Collection<? extends E> c) {
    Check.notNull(c, "c == null");

    return addAll0(c, "c[");
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param index
   *        ignored (the operation is not supported)
   * @param c
   *        ignored (the operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean addAll(int index, Collection<? extends E> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * Removes all of the elements from this list.
   */
  @Override
  public final void clear() {
    Arrays.fill(data, null);

    size = 0;
  }

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
    return UtilLists.containsImpl(data, size, o);
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
    return UtilLists.equalsImpl(this, obj);
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
    return (E) UtilLists.getImpl(data, size, index);
  }

  /**
   * Returns the hash code value of this list.
   *
   * @return the hash code value of this list
   */
  @Override
  public final int hashCode() {
    return UtilLists.hashCodeImpl(data, size);
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
    return UtilLists.indexOfImpl(data, size, o);
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  @Override
  public final Util.UnmodifiableIterator<E> iterator() {
    return new UtilLists.SimpleIterator<>(data, size);
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
    return UtilLists.joinImpl(data, size);
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
    return UtilLists.joinImpl(data, size, delimiter);
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
    return UtilLists.joinImpl(data, size, delimiter, prefix, suffix);
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
    return UtilLists.lastIndexOfImpl(data, size, o);
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
    return Arrays.copyOf(data, size);
  }

  /**
   * Returns an array, either the specified array or a new array instance,
   * containing all of the elements in this list in order.
   *
   * <p>
   * The specified array is used as the return value if it is large enough to
   * hold all of the elements in this list. Additionally, if the specified
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
  public final <T> T[] toArray(T[] a) {
    return UtilLists.toArrayImpl(data, size, a);
  }

  /**
   * Returns an {@link UnmodifiableList} copy of this list.
   *
   * <p>
   * The returned {@code UnmodifiableList} will contain all of the elements from
   * this list in order. Therefore, the returned list {@code copy} and this list
   * {@code source} will be such that {@code source.equals(copy)} is
   * {@code true}.
   *
   * <p>
   * The returned list will be a copy in the sense that, after this method
   * returns, modifying this list will have no effect on the returned (copied)
   * one.
   *
   * <p>
   * Note, however, that the behavior of this method is undefined if this list
   * is modified while the copy is being made.
   *
   * @return an {@link UnmodifiableList} copy of this list
   */
  public final UtilUnmodifiableList<E> toUnmodifiableList() {
    switch (size) {
      case 0:
        return UtilUnmodifiableList.of();
      default:
        var copy = new Object[size];

        System.arraycopy(data, 0, copy, 0, size);

        return new UtilUnmodifiableList<>(copy);
    }
  }

  /**
   * Returns a sorted {@link UnmodifiableList} copy of this list while keeping
   * the
   * latter unchanged. This is equivalent to, for this {@code list}:
   *
   * <pre> {@code
   * list.sort(c);
   * return list.toUnmodifiableList();}</pre>
   *
   * except that this list remains unchanged.
   *
   * @param c
   *        the comparator defining the order for the returned list
   *
   * @return a sorted {@link UnmodifiableList} copy of this list
   */
  @SuppressWarnings("unchecked")
  public final UtilUnmodifiableList<E> toUnmodifiableList(Comparator<? super E> c) {
    Check.notNull(c, "c == null");

    switch (size) {
      case 0:
      case 1:
        return toUnmodifiableList();
      default:
        var copy = new Object[size];

        System.arraycopy(data, 0, copy, 0, size);

        Arrays.sort((E[]) copy, 0, size, c);

        return new UtilUnmodifiableList<E>(copy);
    }
  }

  @Override
  public final String toString() {
    return UtilLists.toStringImpl(this, data, size);
  }

  final boolean add0(E e) {
    if (size < data.length) {
      return append0(e);
    }

    if (data == Util.EMPTY_OBJECT_ARRAY) {
      data = new Object[Util.DEFAULT_CAPACITY];
    } else {
      var newLength = Util.growByOne(data.length);

      copyData(newLength);
    }

    return append0(e);
  }

  final boolean addAll(Iterator<? extends E> iterator) {
    return addAll0Iterator(iterator, "iterator[");
  }

  @SuppressWarnings("unchecked")
  final void sortImpl(Comparator<? super E> c) {
    Arrays.sort((E[]) data, 0, size, c);
  }

  private boolean addAll0(Collection<? extends E> c, String nullMessageStart) {
    if (c.isEmpty()) {
      return false;
    }

    if (c instanceof RandomAccess && c instanceof List<? extends E> list) {
      return addAll0List(list, nullMessageStart);
    } else {
      return addAll0Collection(c);
    }
  }

  private boolean addAll0Collection(Collection<? extends E> c) {
    int collSize = c.size();

    if (collSize == 0) {
      return false;
    }

    addAll1Grow(collSize);

    Object[] array = c.toArray();

    for (int i = 0; i < collSize; i++) {
      var element = array[i];

      append0(
          Check.notNull(element, "c[", i, "] == null")
      );
    }

    return true;
  }

  private boolean addAll0Iterator(Iterator<? extends E> iterator, String nullMessageStart) {
    var ret = false;

    var index = 0;

    while (iterator.hasNext()) {
      var element = iterator.next();

      ret = add0(
          Check.notNull(element, nullMessageStart, index, "] == null")
      );

      index++;
    }

    return ret;
  }

  private boolean addAll0List(List<? extends E> list, String nullMessageStart) {
    int listSize = list.size();

    if (listSize == 0) {
      return false;
    }

    addAll1Grow(listSize);

    for (int i = 0; i < listSize; i++) {
      var element = list.get(i);

      append0(
          Check.notNull(element, nullMessageStart, i, "] == null")
      );
    }

    return true;
  }

  private void addAll1Grow(int otherSize) {
    int requiredIndex = size + otherSize;

    if (requiredIndex >= data.length) {
      int newLength;

      if (data == Util.EMPTY_OBJECT_ARRAY && requiredIndex < Util.DEFAULT_CAPACITY) {
        newLength = Util.DEFAULT_CAPACITY;
      } else {
        newLength = Util.growBy(data.length, otherSize);
      }

      copyData(newLength);
    }
  }

  private boolean append0(Object e) {
    data[size++] = e;

    return true;
  }

  private void copyData(int newLength) {
    var copy = new Object[newLength];

    System.arraycopy(data, 0, copy, 0, data.length);

    data = copy;
  }

}