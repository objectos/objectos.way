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
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;
import objectos.lang.Check;

/**
 * A hash-based implementation of the {@link Set} and {@link MutableCollection}
 * interfaces.
 *
 * @param <E> type of the elements in this set
 */
public final class MutableSet<E>
    extends AbstractArrayBasedSet<E>
    implements
    MutableCollection<E> {

  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  private static final int FIRST_RESIZE = 4;

  private static final int MAX_ARRAY_LENGTH = MAX_POSITIVE_POWER_OF_TWO;

  private final float loadFactor = DEFAULT_LOAD_FACTOR;

  private int rehashSize;

  /**
   * Creates a new {@code MutableSet} instance.
   */
  public MutableSet() {}

  /**
   * Adds the specified element to this set if it is not already present.
   *
   * @param e
   *        an element to be added to this set
   *
   * @return {@code true} if this set was modified as a result of this
   *         operation. {@code false} if the element was already present in this
   *         set
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  @Override
  public final boolean add(E e) {
    return addWithNullMessage(e, "e == null");
  }

  /**
   * Adds all of the elements in the specified collection to this set if they
   * are not already present.
   *
   * <p>
   * The behavior of this operation is undefined if the specified collection is
   * modified while the operation is in progress. (This implies that the
   * behavior of this call is undefined if the specified collection is this
   * set, and this set is nonempty.)
   *
   * @param c
   *        a collection containing the elements to be added to this set
   *
   * @return {@code true} if this set was modified as a result of this
   *         operation. {@code false} if all of the elements were already
   *         present in this set
   *
   * @throws NullPointerException
   *         if the specified collection is {@code null} or if it contains any
   *         element that is {@code null}
   */
  @Override
  public final boolean addAll(Collection<? extends E> c) {
    return addAllIterable(c);
  }

  /**
   * Adds all of the elements returned by the specified iterable's iterator to
   * this set if they are not already present.
   *
   * <p>
   * The behavior of this operation is undefined if the specified iterable is
   * modified while the operation is in progress. (This implies that the
   * behavior of this call is undefined if the specified iterable is this list,
   * and this list is nonempty.)
   *
   * @param iterable
   *        {@code Iterable} containing elements to be added to this set
   *
   * @return {@code true} if this set was modified as a result of this
   *         operation. {@code false} if all of the elements were already
   *         present in this set
   *
   * @throws NullPointerException
   *         if the specified iterable is {@code null} or if it provides any
   *         element that is {@code null}
   */
  @Override
  public final boolean addAllIterable(Iterable<? extends E> iterable) {
    Check.notNull(iterable, "iterable == null");

    boolean result;
    result = false;

    if (iterable instanceof RandomAccess && iterable instanceof List) {
      List<? extends E> list;
      list = (List<? extends E>) iterable;

      for (int i = 0, size = list.size(); i < size; i++) {
        E element;
        element = list.get(i);

        Check.notNull(element, "iterable[", i, "] == null");

        if (addUnchecked(element)) {
          result = true;
        }
      }
    }

    else {
      int i = 0;

      for (E element : iterable) {
        Check.notNull(element, "elements[", i, "] == null");

        if (addUnchecked(element)) {
          result = true;
        }

        i++;
      }
    }

    return result;
  }

  /**
   * Adds the specified element {@code e} to this set if it is not already
   * present.
   *
   * <p>
   * If a {@code NullPointerException} is to be thrown, the {@code nullMessage}
   * value is used as the exception's message.
   *
   * <p>
   * Typical usage:
   *
   * <pre>
   * set.addWithNullMessage(value, "value == null");</pre>
   *
   * @param e
   *        an element to be added to this set (if it not null)
   * @param nullMessage
   *        the {@code NullPointerException} message
   *
   * @return {@code true} if this set was modified as a result of this
   *         operation. {@code false} if the element was already present in this
   *         set
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   *
   * @see Check#notNull(Object, Object)
   */
  @Override
  public final boolean addWithNullMessage(E e, Object nullMessage) {
    Check.notNull(e, nullMessage);

    return addUnchecked(e);
  }

  /**
   * Adds the specified element {@code e} to this set if it is not already
   * present.
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
   * set.addWithNullMessage(element, "elements[", index, "] == null");</pre>
   *
   * @param e
   *        an element to be added to this set (if it not null)
   * @param nullMessageStart
   *        the first part of the {@code NullPointerException} message
   * @param index
   *        the second part of the {@code NullPointerException} message
   * @param nullMessageEnd
   *        the third part of the {@code NullPointerException} message
   *
   * @return {@code true} if this set was modified as a result of this
   *         operation. {@code false} if the element was already present in this
   *         set
   *
   * @see Check#notNull(Object, Object, int, Object)
   */
  @Override
  public final boolean addWithNullMessage(
      E e, Object nullMessageStart, int index, Object nullMessageEnd) {
    Check.notNull(e, nullMessageStart, index, nullMessageEnd);

    return addUnchecked(e);
  }

  /**
   * Removes all of the elements from this set.
   */
  @Override
  public final void clear() {
    Arrays.fill(array, null);

    size = 0;
  }

  /**
   * Returns an {@link ImmutableSet} copy of this set.
   *
   * <p>
   * The returned {@code ImmutableSet} will contain all of the elements from
   * this set.
   *
   * <p>
   * The returned set will be a copy in the sense that, after this method
   * returns, modifying this set will have no effect on the returned (copied)
   * one.
   *
   * <p>
   * Note, however, that the behaviour of this method is undefined if this set
   * is modified while the copy is being made.
   *
   * @return an {@link ImmutableSet} copy of this set
   */
  public final ImmutableSet<E> toImmutableSet() {
    switch (size) {
      case 0:
        return ImmutableSet.of();
      default:
        Object[] copy;
        copy = Arrays.copyOf(array, array.length);

        return new ImmutableSet<E>(copy, size);
    }
  }

  final boolean addUnchecked(E e) {
    firstResizeIfNecessary();

    int index, marker;
    index = marker = hashIndex(e);

    Object existing;

    while (index < array.length) {
      existing = array[index];

      if (existing == null) {
        insert(index, e);

        return true;
      }

      else if (existing.equals(e)) {
        return false;
      }

      else {
        index++;
      }
    }

    index = 0;

    while (index < marker) {
      existing = array[index];

      if (existing == null) {
        insert(index, e);

        return true;
      }

      else if (existing.equals(e)) {
        return false;
      }

      else {
        index++;
      }
    }

    throw new UnsupportedOperationException("Implement me");
  }

  private void firstResizeIfNecessary() {
    if (array == ObjectArrays.empty()) {
      resizeTo(FIRST_RESIZE);
    }
  }

  private void insert(int index, E e) {
    array[index] = e;

    size++;

    rehashIfNecessary();
  }

  private void rehashIfNecessary() {
    if (size < rehashSize) {
      return;
    }

    if (array.length == MAX_ARRAY_LENGTH) {
      throw new OutOfMemoryError("backing array already at max allowed length");
    }

    Object[] previous;
    previous = array;

    int newLength;
    newLength = array.length << 1;

    if (newLength < 0) {
      newLength = MAX_ARRAY_LENGTH;
    }

    resizeTo(newLength);

    outer: //
    for (Object e : previous) {
      if (e == null) {
        continue;
      }

      int probe, index;
      probe = index = hashIndex(e);

      Object existing;

      while (probe < array.length) {
        existing = array[probe];

        if (existing == null) {
          array[probe] = e;

          continue outer;
        }

        else {
          probe++;
        }
      }

      probe = 0;

      while (probe < index) {
        existing = array[probe];

        if (existing == null) {
          array[probe] = e;

          continue outer;
        }

        else {
          probe++;
        }
      }

      throw new UnsupportedOperationException("Implement me");
    }
  }

  private void resizeTo(int newSize) {
    array = new Object[newSize];

    hashMask = newSize - 1;

    rehashSize = (int) (array.length * loadFactor);
  }

}