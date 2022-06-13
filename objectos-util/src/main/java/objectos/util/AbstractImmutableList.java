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
import java.util.Iterator;
import objectos.lang.Check;

/**
 * An array-based {@link br.com.objectos.core.collection.ImmutableCollection}
 * and {@link java.util.List} implementation.
 *
 * @param <E>
 *        type of the elements in this list
 */
abstract class AbstractImmutableList<E> extends AbstractArrayBaseList<E>
    implements
    ImmutableCollection<E> {

  static final ImmutableList<Object> EMPTY = new ImmutableList<Object>();

  AbstractImmutableList() {}

  AbstractImmutableList(Object[] array) {
    this.array = array;

    size = array.length;
  }

  /**
   * Returns a new {@code ImmutableList} instance containing all of the elements
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
   * @return a new {@code ImmutableList} instance containing all of the elements
   *         in the specified array in order
   *
   * @throws NullPointerException
   *         if the array is {@code null} or any element in the array is
   *         {@code null}
   */
  public static <E> ImmutableList<E> copyOf(E[] array) {
    Check.notNull(array, "array == null");

    switch (array.length) {
      case 0:
        return ImmutableList.of();
      default:
        Object[] copy;
        copy = new Object[array.length];

        for (int i = 0; i < array.length; i++) {
          E e;
          e = array[i];

          copy[i] = Check.notNull(e, "array[", i, "] == null");
        }

        return new ImmutableList<E>(copy);
    }
  }

  /**
   * Returns the specified iterable if it is an {@code ImmutableList}; returns
   * a new {@code ImmutableList} instance containing all of the elements
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
   * @return the specified iterable if it is an {@code ImmutableList} or a new
   *         {@code ImmutableList} instance containing all of the elements
   *         produced by the specified iterable's iterator in order
   *
   * @throws NullPointerException
   *         if the iterable is {@code null} or if any element returned by the
   *         iterable's iterator is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> ImmutableList<E> copyOf(Iterable<? extends E> elements) {
    Check.notNull(elements, "elements == null");

    if (elements instanceof ImmutableList) {
      return (ImmutableList<E>) elements;
    }

    if (elements instanceof MutableList) {
      MutableList<? extends E> list;
      list = (MutableList<? extends E>) elements;

      return (ImmutableList<E>) list.toImmutableList();
    }

    MutableList<E> list;
    list = new MutableList<E>();

    int index;
    index = 0;

    for (E e : elements) {
      Check.notNull(e, "elements[", index, "] == null");

      list.resizeIfNecessaryAndDataAppend(e);

      index++;
    }

    return list.toImmutableList();
  }

  /**
   * Returns a new {@code ImmutableList} instance containing all of the elements
   * returned by the specified iterator or throws a {@link NullPointerException}
   * if any element is {@code null}.
   *
   * @param <E>
   *        type of the elements in this list
   * @param iterator
   *        an {@link Iterator} for which an immutable list copy will be created
   *
   * @return a new {@code ImmutableList} instance containing all of the elements
   *         produced by the specified iterator in order
   *
   * @throws NullPointerException
   *         if the iterator is {@code null} or any element produced by the
   *         iterator is {@code null}
   */
  public static <E> ImmutableList<E> copyOf(Iterator<? extends E> iterator) {
    MutableList<E> list;
    list = new MutableList<E>();

    list.addAll(iterator);

    return list.toImmutableList();
  }

  /**
   * Returns the empty {@code ImmutableList}.
   *
   * @param <E>
   *        type of the elements in this list
   *
   * @return the empty {@code ImmutableList}
   */
  @SuppressWarnings("unchecked")
  public static <E> ImmutableList<E> of() {
    return (ImmutableList<E>) EMPTY;
  }

  /**
   * Returns a new {@code ImmutableList} containing one element or throws a
   * {@link NullPointerException} if the element is {@code null}.
   *
   * @param <E>
   *        type of the elements in this list
   * @param element
   *        the single element
   *
   * @return a new {@code ImmutableList} containing one element
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  public static <E> ImmutableList<E> of(E element) {
    Check.notNull(element, "element == null");

    return new ImmutableList<E>(
      new Object[] {
                    element
      }
    );
  }

  /**
   * Returns a new {@code ImmutableList} containing the all of the especified
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
   * @return a new {@code ImmutableList} containing all of the specified
   *         elements
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  public static <E> ImmutableList<E> of(E first, @SuppressWarnings("unchecked") E... more) {
    Check.notNull(first, "first == null");
    Check.notNull(more, "more == null");

    Object[] elements;
    elements = new Object[more.length + 1];

    elements[0] = first;

    for (int i = 0; i < more.length; i++) {
      E e;
      e = more[i];

      elements[i + 1] = Check.notNull(e, "more[" + i + "] == null");
    }

    return new ImmutableList<E>(elements);
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

}