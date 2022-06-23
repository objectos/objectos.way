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
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.UnaryOperator;
import objectos.lang.Check;

/**
 * An array-based unmodifiable {@link java.util.List} implementation.
 *
 * @param <E>
 *        type of the elements in this list
 */
public final class UnmodifiableList<E> extends AbstractArrayBasedList<E> {

  static final UnmodifiableList<Object> EMPTY = new UnmodifiableList<Object>();

  UnmodifiableList() {
    super();
  }

  UnmodifiableList(java.lang.Object[] array) {
    this.data = array;

    size = array.length;
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
        Object[] copy;
        copy = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
          E e;
          e = array[i];

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

    var list = new GrowableList<E>();

    int index;
    index = 0;

    for (E e : elements) {
      Check.notNull(e, "elements[", index, "] == null");

      list.add0(e);

      index++;
    }

    return list.toUnmodifiableList();
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
    var list = new GrowableList<E>();

    list.addAll(iterator);

    return list.toUnmodifiableList();
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
  public static <E> UnmodifiableList<E> of(E first, @SuppressWarnings("unchecked") E... more) {
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