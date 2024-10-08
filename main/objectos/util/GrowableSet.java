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
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;
import objectos.lang.object.Check;
import objectos.way.Util;
import objectos.way.UtilGrowableCollection;

/**
 * A hash-based {@link Set} and {@link UtilGrowableCollection} implementation.
 *
 * @param <E> type of the elements in this set
 */
public final class GrowableSet<E> extends UtilGrowableCollection<E> implements Set<E> {

  private static final int MAX_POSITIVE_POWER_OF_TWO = 1 << 30;

  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  private static final int FIRST_RESIZE = 4;

  private static final int MAX_ARRAY_LENGTH = MAX_POSITIVE_POWER_OF_TWO;

  private Object[] array = Util.EMPTY_OBJECT_ARRAY;

  private final float loadFactor = DEFAULT_LOAD_FACTOR;

  private int rehashSize;

  private int size = 0;

  /**
   * Creates a new {@code GrowableSet} instance.
   */
  public GrowableSet() {}

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
    Check.notNull(c, "c == null");

    return addAll0(c, "c[");
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

    return addAll0(iterable, "iterable[");
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
    return Sets.containsImpl(array, size, o);
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
    return Sets.equalsImpl(this, obj);
  }

  /**
   * Returns the hash code value of this set.
   *
   * @return the hash code value of this set
   */
  @Override
  public final int hashCode() {
    return Sets.hashCodeImpl(array);
  }

  /**
   * Returns an iterator over the elements in this set. The elements
   * are returned in no particular order.
   *
   * @return an iterator over the elements in this set
   */
  @Override
  public final UnmodifiableIterator<E> iterator() {
    return new Sets.SetIterator<>(array);
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
    return Sets.toArrayImpl(array, size);
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
   *        it is big enough; otherwise, a new array of the same runtime type is
   *        allocated for this purpose.
   *
   * @return an array containing the elements of the set
   */
  @Override
  public final <T> T[] toArray(T[] a) {
    return Sets.toArrayImpl(array, size, a);
  }

  /**
   * Returns an {@link UnmodifiableSet} copy of this set.
   *
   * <p>
   * The returned {@code UnmodifiableSet} will contain all of the elements from
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
   * @return an {@link UnmodifiableSet} copy of this set
   */
  public final UnmodifiableSet<E> toUnmodifiableSet() {
    switch (size) {
      case 0:
        return UnmodifiableSet.of();
      default:
        var copy = Arrays.copyOf(array, array.length);

        return new UnmodifiableSetN<E>(copy, size);
    }
  }

  final boolean addAll0(Iterable<? extends E> iterable, String nullMessageStart) {
    var mod = false;

    if (iterable instanceof RandomAccess && iterable instanceof List<? extends E> list) {
      for (int i = 0, size = list.size(); i < size; i++) {
        var element = list.get(i);

        Check.notNull(element, nullMessageStart, i, "] == null");

        if (addUnchecked(element)) {
          mod = true;
        }
      }
    }

    else {
      int i = 0;

      for (E element : iterable) {
        Check.notNull(element, nullMessageStart, i, "] == null");

        if (addUnchecked(element)) {
          mod = true;
        }

        i++;
      }
    }

    return mod;
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
    if (array == Util.EMPTY_OBJECT_ARRAY) {
      resizeTo(FIRST_RESIZE);
    }
  }

  private int hashIndex(Object e) {
    int hc = e.hashCode();

    int mask = array.length - 1;

    return hc & mask;
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

    var previous = array;

    var newLength = array.length << 1;

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

    rehashSize = (int) (array.length * loadFactor);
  }

}