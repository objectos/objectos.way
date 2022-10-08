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
import java.util.Set;
import objectos.lang.Check;

/**
 * An unmodifiable implementation of the {@link Set} interface.
 *
 * <p>
 * After creation, instances of this class do not permit adding nor removing of
 * elements. Any mutator method will throw an
 * {@link UnsupportedOperationException} when called. This is true for mutator
 * methods present in this class and for mutator methods present in the returned
 * iterators.
 *
 * @param <E> type of the elements in this set
 */
public abstract sealed class UnmodifiableSet<E>
    extends UnmodifiableCollection<E>
    implements Set<E>permits objectos.util.UnmodifiableSetN<E> {

  private static final UnmodifiableSet<Object> EMPTY = new UnmodifiableSetN<Object>(
    ObjectArrays.empty(), 0
  );

  /**
   * Sole constructor.
   */
  UnmodifiableSet() {}

  /**
   * Returns a new {@code UnmodifiableSet} containing all of the distinct
   * elements
   * in the specified array.
   *
   * <p>
   * Two non-null elements {@code e1} and {@code e2} are distinct if the
   * expression {@code e1.equals(e2)} evaluates to {@code false}.
   *
   * <p>
   * This method returns a copy in the sense that modifications in the
   * specified array are not propagated to the returned set.
   *
   * @param <E>
   *        type of the elements in this set
   *
   * @param array
   *        an array for which an immutable set copy will be created
   *
   * @return a new {@code UnmodifiableSet} containing all of the distinct
   *         elements in the specified array
   *
   * @throws NullPointerException
   *         if the array is {@code null} or any element in the array is
   *         {@code null}
   */
  public static <E> UnmodifiableSet<E> copyOf(E[] array) {
    Check.notNull(array, "array == null");

    var set = new GrowableSet<E>();

    E element;

    for (int i = 0, size = array.length; i < size; i++) {
      element = array[i];

      set.addUnchecked(
        Check.notNull(element, "array[", i, "] == null")
      );
    }

    return set.toUnmodifiableSet();
  }

  /**
   * Returns the specified iterable if it is an {@code UnmodifiableSet}; returns
   * a new {@code UnmodifiableSet} containing all of the distinct elements
   * returned
   * by the specified iterable's iterator.
   *
   * <p>
   * Two non-null elements {@code e1} and {@code e2} are distinct if the
   * expression {@code e1.equals(e2)} evaluates to {@code false}.
   *
   * @param <E>
   *        type of the elements in this set
   * @param elements
   *        an {@link Iterable} for which an immutable set copy will be created
   *
   * @return the specified iterable if it is an {@code UnmodifiableSet} or a new
   *         {@code UnmodifiableSet} containing all of the elements returned by
   *         the
   *         specified iterable's iterator
   *
   * @throws NullPointerException
   *         if the iterable is {@code null} or if any element returned by the
   *         iterable's iterator is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableSet<E> copyOf(Iterable<? extends E> elements) {
    Check.notNull(elements, "elements == null");

    if (elements instanceof UnmodifiableSet) {
      return (UnmodifiableSet<E>) elements;
    }

    if (elements instanceof GrowableSet<? extends E> set) {
      return (UnmodifiableSet<E>) set.toUnmodifiableSet();
    }

    var set = new GrowableSet<E>();

    set.addAll0(elements, "elements[");

    return set.toUnmodifiableSet();
  }

  /**
   * Returns a new {@code UnmodifiableSet} instance containing all of the
   * distinct
   * elements returned by the specified iterator.
   *
   * <p>
   * Two non-null elements {@code e1} and {@code e2} are distinct if the
   * expression {@code e1.equals(e2)} evaluates to {@code false}.
   *
   * @param <E>
   *        type of the elements in this set
   * @param iterator
   *        an {@link Iterator} for which an immutable set copy will be created
   *
   * @return a new {@code UnmodifiableSet} instance containing all of the
   *         distinct
   *         elements produced by the specified iterator
   *
   * @throws NullPointerException
   *         if the iterator is {@code null} or any element produced by the
   *         iterator is {@code null}
   */
  public static <E> UnmodifiableSet<E> copyOf(Iterator<? extends E> iterator) {
    Check.notNull(iterator, "iterator == null");

    if (!iterator.hasNext()) {
      return UnmodifiableSet.of();
    }

    var set = new GrowableSet<E>();

    int i = 0;

    var element = iterator.next();

    if (element == null) {
      throw new NullPointerException("iterator[" + i + "] == null");
    }

    i++;

    set.addUnchecked(element);

    while (iterator.hasNext()) {
      element = iterator.next();

      if (element == null) {
        throw new NullPointerException("iterator[" + i + "] == null");
      }

      i++;

      set.addUnchecked(element);
    }

    return set.toUnmodifiableSet();
  }

  /**
   * Returns the empty {@code UnmodifiableSet}.
   *
   * @param <E>
   *        type of the elements in this set
   *
   * @return the empty {@code UnmodifiableSet}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableSet<E> of() {
    return (UnmodifiableSet<E>) EMPTY;
  }

  /**
   * Returns a new {@code UnmodifiableSet} containing one element.
   *
   * @param <E>
   *        type of the elements in this set
   * @param element
   *        the single element
   *
   * @return a new {@code UnmodifiableSet} containing one element
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  public static <E> UnmodifiableSet<E> of(E element) {
    Check.notNull(element, "element == null");

    var set = new GrowableSet<E>();

    set.add(element);

    return set.toUnmodifiableSet();
  }

  /**
   * Returns a new {@code UnmodifiableSet} containing all of the distinct
   * specified elements.
   *
   * <p>
   * Two non-null elements {@code e1} and {@code e2} are distinct if the
   * expression {@code e1.equals(e2)} evaluates to {@code false}.
   *
   * @param <E>
   *        type of the elements in this set
   * @param first
   *        the first element
   * @param more
   *        the additional elements
   *
   * @return a new {@code UnmodifiableSet} containing all of the distinct
   *         specified
   *         elements
   *
   * @throws NullPointerException
   *         if any of the specified elements is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> UnmodifiableSet<E> of(E first, E... more) {
    Check.notNull(first, "first == null");
    Check.notNull(more, "more == null");

    var set = new GrowableSet<E>();

    set.add(first);

    for (int i = 0; i < more.length; i++) {
      var e = more[i];

      set.addWithNullMessage(e, "more[", i, "] == null");
    }

    return set.toUnmodifiableSet();
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
  public abstract boolean contains(Object o);

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
  public abstract boolean equals(Object obj);

  /**
   * Returns the hash code value of this set.
   *
   * @return the hash code value of this set
   */
  @Override
  public abstract int hashCode();

  /**
   * Returns an iterator over the elements in this set. The elements
   * are returned in no particular order.
   *
   * @return an iterator over the elements in this set
   */
  @Override
  public abstract UnmodifiableIterator<E> iterator();

  /**
   * Returns the size of this set. The size of a set is equal to the number of
   * elements it contains.
   *
   * @return the size of this set
   */
  @Override
  public abstract int size();

  /**
   * Returns a new array instance containing all of the elements in this set.
   * The returned array length is equal to the size of this set.
   *
   * @return a new array instance containing all of the elements in this set
   */
  @Override
  public abstract Object[] toArray();

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
  public abstract <T> T[] toArray(T[] a);

  @Override
  final Object toStringTypeName() { return "UnmodifiableSet"; }

}