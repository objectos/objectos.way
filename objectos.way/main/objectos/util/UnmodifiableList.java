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
package objectos.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import java.util.function.UnaryOperator;
import objectox.lang.Check;

/**
 * An array-based unmodifiable {@link java.util.List} implementation.
 *
 * @param <E>
 *        type of the elements in this list
 */
public final class UnmodifiableList<E>
    extends UnmodifiableCollection<E>
    implements List<E>, RandomAccess {

  static final UnmodifiableList<Object> EMPTY = new UnmodifiableList<Object>(
    ObjectArrays.EMPTY
  );

  private final Object[] data;

  UnmodifiableList(java.lang.Object[] array) {
    this.data = array;
  }

  /**
   * Returns a new {@code UnmodifiableList} instance containing all of the
   * elements
   * in the specified array in order or throws a {@link NullPointerException} if
   * any element in the array is {@code null}.
   *
   * <p>
   * This method returns a copy in the sense that a modification in the
   * specified array (an element is removed or replaced by another one) does not
   * modify the contents of the returned list.
   *
   * @param <E>
   *        type of the elements in this list
   * @param array
   *        an array for which an immutable list copy will be created
   *
   * @return a new {@code UnmodifiableList} instance containing all of the
   *         elements
   *         in the specified array in order
   *
   * @throws NullPointerException
   *         if the array is {@code null} or any element in the array is
   *         {@code null}
   */
  public static <E> UnmodifiableList<E> copyOf(E[] array) {
    Check.notNull(array, "array == null");

    switch (array.length) {
      case 0:
        return UnmodifiableList.of();
      default:
        var copy = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
          var e = array[i];

          copy[i] = Check.notNull(e, "array[", i, "] == null");
        }

        return new UnmodifiableList<E>(copy);
    }
  }

  /**
   * Returns the specified iterable if it is an {@code UnmodifiableList};
   * returns
   * a new {@code UnmodifiableList} instance containing all of the elements
   * produced by the specified iterable's iterator in the order that they are
   * returned or throws a {@link NullPointerException} if any element
   * returned by the iterator is {@code null}.
   *
   * <p>
   * This method returns a copy in the sense that, if the specified iterable is
   * a mutable collection, then a modification in the specified collection does
   * not modify the contents of the returned list.
   *
   * @param <E>
   *        type of the elements in this list
   * @param elements
   *        an {@link Iterable} for which an immutable list copy will be created
   *
   * @return the specified iterable if it is an {@code UnmodifiableList} or a
   *         new
   *         {@code UnmodifiableList} instance containing all of the elements
   *         produced by the specified iterable's iterator in order
   *
   * @throws NullPointerException
   *         if the iterable is {@code null} or if any element returned by the
   *         iterable's iterator is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableList<E> copyOf(Iterable<? extends E> elements) {
    Check.notNull(elements, "elements == null");

    if (elements instanceof UnmodifiableList) {
      return (UnmodifiableList<E>) elements;
    }

    if (elements instanceof GrowableList<? extends E> list) {
      return (UnmodifiableList<E>) list.toUnmodifiableList();
    }

    if (elements instanceof RandomAccess && elements instanceof List<? extends E> list) {
      var size = list.size();

      var data = new Object[size];

      for (int i = 0; i < size; i++) {
        var e = list.get(i);

        data[i] = Check.notNull(e, "elements[", i, "] == null");
      }

      return new UnmodifiableList<>(data);
    }

    if (elements instanceof Collection<? extends E> coll) {
      var size = coll.size();

      var data = new Object[size];

      int index = 0;

      for (var e : coll) {
        Check.notNull(e, "elements[", index, "] == null");

        data[index++] = e;
      }

      return new UnmodifiableList<>(data);
    }

    var iterator = elements.iterator();

    return copyOf0(iterator);
  }

  /**
   * Returns a new {@code UnmodifiableList} instance containing all of the
   * elements
   * returned by the specified iterator or throws a {@link NullPointerException}
   * if any element is {@code null}.
   *
   * @param <E>
   *        type of the elements in this list
   * @param iterator
   *        an {@link Iterator} for which an immutable list copy will be created
   *
   * @return a new {@code UnmodifiableList} instance containing all of the
   *         elements
   *         produced by the specified iterator in order
   *
   * @throws NullPointerException
   *         if the iterator is {@code null} or any element produced by the
   *         iterator is {@code null}
   */
  public static <E> UnmodifiableList<E> copyOf(Iterator<? extends E> iterator) {
    Check.notNull(iterator, "iterator == null");

    return copyOf0(iterator);
  }

  /**
   * Returns the empty {@code UnmodifiableList}.
   *
   * @param <E>
   *        type of the elements in this list
   *
   * @return the empty {@code UnmodifiableList}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableList<E> of() {
    return (UnmodifiableList<E>) EMPTY;
  }

  /**
   * Returns a new {@code UnmodifiableList} containing one element or throws a
   * {@link NullPointerException} if the element is {@code null}.
   *
   * @param <E>
   *        type of the elements in this list
   * @param element
   *        the single element
   *
   * @return a new {@code UnmodifiableList} containing one element
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  public static <E> UnmodifiableList<E> of(E element) {
    Check.notNull(element, "element == null");

    return new UnmodifiableList<>(
      new Object[] {element}
    );
  }

  /**
   * Returns a new {@code UnmodifiableList} containing the all of the especified
   * elements in order or throws a {@link NullPointerException} if any of the
   * elements is {@code null}.
   *
   * @param <E>
   *        type of the elements in this list
   * @param first
   *        the first element
   * @param more
   *        the additional elements
   *
   * @return a new {@code UnmodifiableList} containing all of the specified
   *         elements
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableList<E> of(E first, E... more) {
    Check.notNull(first, "first == null");
    Check.notNull(more, "more == null");

    var elements = new Object[more.length + 1];

    elements[0] = first;

    for (int i = 0; i < more.length; i++) {
      var e = more[i];

      elements[i + 1] = Check.notNull(e, "more[" + i + "] == null");
    }

    return new UnmodifiableList<E>(elements);
  }

  private static <E> UnmodifiableList<E> copyOf0(Iterator<? extends E> iterator) {
    var list = new GrowableList<E>();

    list.addAll(iterator);

    return list.toUnmodifiableList();
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
   * @param e
   *        ignored (the operation is not supported)
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void add(int index, E e) {
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
    return Lists.containsImpl(data, data.length, o);
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
    return Lists.equalsImpl(this, obj);
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
    Lists.formatToStringImpl(this, data, data.length, toString, level);
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
    return (E) Lists.getImpl(data, data.length, index);
  }

  /**
   * Returns the hash code value of this list.
   *
   * @return the hash code value of this list
   */
  @Override
  public final int hashCode() {
    return Lists.hashCodeImpl(data, data.length);
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
    return Lists.indexOfImpl(data, data.length, o);
  }

  /**
   * Returns an iterator over the elements in this list in proper sequence.
   *
   * @return an iterator over the elements in this list in proper sequence
   */
  @Override
  public final UnmodifiableIterator<E> iterator() {
    return new Lists.SimpleIterator<>(data, data.length);
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
    return Lists.joinImpl(data, data.length);
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
    return Lists.joinImpl(data, data.length, delimiter);
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
    return Lists.joinImpl(data, data.length, delimiter, prefix, suffix);
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
    return Lists.lastIndexOfImpl(data, data.length, o);
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
    return data.length;
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
    return data.clone();
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
    return Lists.toArrayImpl(data, data.length, a);
  }

}