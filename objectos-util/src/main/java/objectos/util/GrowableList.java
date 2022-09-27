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
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import objectos.lang.Check;

/**
 * An array-based {@link GrowableCollection} and
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
 * @see GrowableCollection
 * @see java.util.List
 */
public class GrowableList<E>
    extends AbstractArrayBasedList<E>
    implements GrowableCollection<E> {

  static final int DEFAULT_CAPACITY = 10;

  /**
   * Creates a new {@code GrowableList} instance.
   */
  public GrowableList() {
    super();
  }

  GrowableList(Object[] elements) {
    data = elements;

    size = elements.length;
  }

  // static so easier to test
  static int growBy(int length, int ammount) {
    int half = length >> 1;

    int delta = Math.max(ammount, half);

    int newLength = length + delta;

    return grow0(length, newLength);
  }

  // static so easier to test
  static int growByOne(int length) {
    int half = length >> 1;

    int newLength = length + half;

    return grow0(length, newLength);
  }

  private static int grow0(int length, int newLength) {
    if (newLength > 0 && newLength <= MoreArrays.JVM_SOFT_LIMIT) {
      return newLength;
    }

    if (length != MoreArrays.JVM_SOFT_LIMIT) {
      return MoreArrays.JVM_SOFT_LIMIT;
    }

    throw new OutOfMemoryError(
      """
      Cannot allocate array: exceeds JVM soft limit.

      length = %,14d
      limit  = %,14d
      """.formatted(length + 1, MoreArrays.JVM_SOFT_LIMIT)
    );
  }

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
   * Appends all of the elements in the specified iterable to the end of this
   * list, in the order that they are returned by the specified iterable's
   * iterator.
   *
   * <p>
   * The behavior of this operation is undefined if the specified iterable is
   * modified while the operation is in progress. (This implies that the
   * behavior of this call is undefined if the specified iterable is this list,
   * and this list is nonempty.)
   *
   * @param iterable
   *        {@code Iterable} containing elements to be added to this list
   *
   * @return {@code true} if this list changed as a result of the call
   */
  @Override
  public final boolean addAllIterable(Iterable<? extends E> iterable) {
    Check.notNull(iterable, "iterable == null");

    if (iterable instanceof Collection<? extends E> coll) {
      return addAll0(coll, "iterable[");
    }

    else {
      var iterator = iterable.iterator();

      return addAll0Iterator(iterator, "iterable[");
    }
  }

  /**
   * Appends the specified element {@code e} to this list or throws a
   * {@code NullPointerException} if the element is {@code null}.
   *
   * <p>
   * If a {@code NullPointerException} is to be thrown, the {@code nullMessage}
   * value is used as the exception's message.
   *
   * <p>
   * Typical usage:
   *
   * <pre>
   * list.addWithNullMessage(value, "value == null");</pre>
   *
   * @param e
   *        an element to be added to this list (if it not null)
   * @param nullMessage
   *        the {@code NullPointerException} message
   *
   * @return {@code true}
   */
  @Override
  public final boolean addWithNullMessage(E e, Object nullMessage) {
    Check.notNull(e, nullMessage);

    return add0(e);
  }

  /**
   * Appends the specified element {@code e} to this list or throws a
   * {@code NullPointerException} if the element is {@code null}.
   *
   * <p>
   * If a {@code NullPointerException} is to be thrown, the concatenation of
   * {@code nullMessageStart}, {@code index} and {@code nullMessageEnd} is
   * used as the exception's message.
   *
   * <p>
   * Typical usage:
   *
   * <pre>
   * list.addWithNullMessage(element, "elements[", index, "] == null");</pre>
   *
   * @param e
   *        an element to be added to this list (if it not null)
   * @param nullMessageStart
   *        the first part of the {@code NullPointerException} message
   * @param index
   *        the second part of the {@code NullPointerException} message
   * @param nullMessageEnd
   *        the third part of the {@code NullPointerException} message
   *
   * @return {@code true}
   */
  @Override
  public final boolean addWithNullMessage(
      E e, Object nullMessageStart, int index, Object nullMessageEnd) {
    Check.notNull(e, nullMessageStart, index, nullMessageEnd);

    return add0(e);
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
  public final UnmodifiableList<E> toUnmodifiableList() {
    switch (size) {
      case 0:
        return UnmodifiableList.of();
      default:
        var copy = new Object[size];

        System.arraycopy(data, 0, copy, 0, size);

        return new UnmodifiableList<>(copy);
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
  public final UnmodifiableList<E> toUnmodifiableList(Comparator<? super E> c) {
    Check.notNull(c, "c == null");

    switch (size) {
      case 0:
      case 1:
        return toUnmodifiableList();
      default:
        var copy = new Object[size];

        System.arraycopy(data, 0, copy, 0, size);

        Arrays.sort((E[]) copy, 0, size, c);

        return new UnmodifiableList<E>(copy);
    }
  }

  final boolean add0(E e) {
    if (size < data.length) {
      return append0(e);
    }

    if (data == ObjectArrays.EMPTY) {
      data = new Object[DEFAULT_CAPACITY];
    } else {
      var newLength = growByOne(data.length);

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

  /**
   * Truncates this list to the specified size.
   *
   * <p>
   * More formally, removes all of the elements from list having indices
   * {@code i} such that {@code i >= newSize}. If specified size is equal to or
   * greater than the current size then the list is left unmodified.
   *
   * @param newSize
   *        the new size to set for this list
   *
   * @throws IllegalArgumentException
   *         if {@code newSize < 0}
   */
  final void truncate(int newSize) {
    Check.argument(newSize >= 0, "newSize must not be negative");

    if (newSize >= size) {
      return;
    }

    Arrays.fill(data, newSize, size, null);

    size = newSize;
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

      if (data == ObjectArrays.EMPTY && requiredIndex < DEFAULT_CAPACITY) {
        newLength = DEFAULT_CAPACITY;
      } else {
        newLength = growBy(data.length, otherSize);
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