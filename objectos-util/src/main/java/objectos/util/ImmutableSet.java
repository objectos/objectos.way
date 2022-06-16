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
import java.util.Set;
import objectos.lang.Check;

/**
 * A hash-based implementation of the {@link Set} and
 * {@link UnmodifiableCollection} interfaces.
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
public final class ImmutableSet<E>
    extends AbstractArrayBasedSet<E>
    implements
    UnmodifiableCollection<E> {

  private static final ImmutableSet<Object> EMPTY = new ImmutableSet<Object>(
    ObjectArrays.empty(), 0
  );

  ImmutableSet(Object[] array, int size) {
    this.array = array;

    this.size = size;

    hashMask = array.length - 1;
  }

  /**
   * Returns a new {@code ImmutableSet} containing all of the distinct elements
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
   * @return a new {@code ImmutableSet} containing all of the distinct
   *         elements in the specified array
   *
   * @throws NullPointerException
   *         if the array is {@code null} or any element in the array is
   *         {@code null}
   */
  public static <E> ImmutableSet<E> copyOf(E[] array) {
    Check.notNull(array, "array == null");

    MutableSet<E> set;
    set = new MutableSet<>();

    E element;

    for (int i = 0, size = array.length; i < size; i++) {
      element = array[i];

      set.addUnchecked(
        Check.notNull(element, "array[", i, "] == null")
      );
    }

    return set.toImmutableSet();
  }

  /**
   * Returns the specified iterable if it is an {@code ImmutableSet}; returns
   * a new {@code ImmutableSet} containing all of the distinct elements returned
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
   * @return the specified iterable if it is an {@code ImmutableSet} or a new
   *         {@code ImmutableSet} containing all of the elements returned by the
   *         specified iterable's iterator
   *
   * @throws NullPointerException
   *         if the iterable is {@code null} or if any element returned by the
   *         iterable's iterator is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> elements) {
    Check.notNull(elements, "elements == null");

    if (elements instanceof ImmutableSet) {
      return (ImmutableSet<E>) elements;
    }

    if (elements instanceof MutableSet) {
      MutableSet<? extends E> set;
      set = (MutableSet<? extends E>) elements;

      return (ImmutableSet<E>) set.toImmutableSet();
    }

    MutableSet<E> set;
    set = new MutableSet<>();

    set.addAllIterable(elements);

    return set.toImmutableSet();
  }

  /**
   * Returns a new {@code ImmutableSet} instance containing all of the distinct
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
   * @return a new {@code ImmutableSet} instance containing all of the distinct
   *         elements produced by the specified iterator
   *
   * @throws NullPointerException
   *         if the iterator is {@code null} or any element produced by the
   *         iterator is {@code null}
   */
  public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> iterator) {
    Check.notNull(iterator, "iterator == null");

    if (!iterator.hasNext()) {
      return ImmutableSet.of();
    }

    MutableSet<E> set;
    set = new MutableSet<>();

    int i;
    i = 0;

    E element;
    element = iterator.next();

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

    return set.toImmutableSet();
  }

  /**
   * Returns the empty {@code ImmutableSet}.
   *
   * @param <E>
   *        type of the elements in this set
   *
   * @return the empty {@code ImmutableSet}
   */
  @SuppressWarnings("unchecked")
  public static <E> ImmutableSet<E> of() {
    return (ImmutableSet<E>) EMPTY;
  }

  /**
   * Returns a new {@code ImmutableSet} containing one element.
   *
   * @param <E>
   *        type of the elements in this set
   * @param element
   *        the single element
   *
   * @return a new {@code ImmutableSet} containing one element
   *
   * @throws NullPointerException
   *         if the specified element is {@code null}
   */
  public static <E> ImmutableSet<E> of(E element) {
    Check.notNull(element, "element == null");

    MutableSet<E> set;
    set = new MutableSet<>();

    set.add(element);

    return set.toImmutableSet();
  }

  /**
   * Returns a new {@code ImmutableSet} containing all of the distinct
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
   * @return a new {@code ImmutableSet} containing all of the distinct specified
   *         elements
   *
   * @throws NullPointerException
   *         if any of the specified elements is {@code null}
   */
  @SuppressWarnings("unchecked")
  public static <E> ImmutableSet<E> of(E first, E... more) {
    Check.notNull(first, "first == null");
    Check.notNull(more, "more == null");

    MutableSet<E> set;
    set = new MutableSet<>();

    set.add(first);

    for (int i = 0; i < more.length; i++) {
      E e;
      e = more[i];

      set.addWithNullMessage(e, "more[", i, "] == null");
    }

    return set.toImmutableSet();
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
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final void clear() {
    throw new UnsupportedOperationException();
  }

}