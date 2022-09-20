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
import java.util.List;
import java.util.RandomAccess;
import java.util.function.Predicate;
import objectos.lang.Check;
import objectos.lang.ToString;

/**
 * An array-based {@code abstract} implementation of {@link BaseCollection}.
 *
 * @param <E> type of the elements in this collection
 */
abstract class AbstractBaseCollection<E> implements BaseCollection<E> {

  /**
   * Sole constructor
   */
  AbstractBaseCollection() {}

  /**
   * Returns {@code true} if this collection contains all of the elements in the
   * specified collection.
   *
   * <p>
   * This method does not throw a {@code ClassCastException}.
   *
   * @param c
   *        a collection whose elements are to be checked
   *
   * @return {@code true} if this collection contains all of the elements in the
   *         specified collection
   *
   * @see Collection#contains(Object)
   */
  @Override
  public final boolean containsAll(Collection<?> c) {
    Check.notNull(c, "c == null");

    if (c instanceof RandomAccess && c instanceof List<?> list) {
      for (int i = 0; i < list.size(); i++) {
        var e = list.get(i);

        if (!contains(e)) {
          return false;
        }
      }
    } else {
      for (Object e : c) {
        if (!contains(e)) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Formats and appends to the {@code toString} builder at the specified
   * indentation {@code level} a string representation of this collection.
   *
   * <p>
   * The string representation <i>may</i> contain:
   *
   * <ul>
   * <li>the simple name of the collections's class; and</li>
   * <li>the string representation of each element paired with its iteration
   * index</li>
   * </ul>
   *
   * @param toString
   *        the builder of a {@code toString} method
   * @param level
   *        the indentation level.
   */
  @Override
  public void formatToString(StringBuilder toString, int level) {
    ToString.formatStart(toString, this);

    var iterator = iterator();

    var index = 0;

    if (iterator.hasNext()) {
      var length = sizeDigits();

      var name = indexName(index, length);

      var value = iterator.next();

      ToString.formatFirstPair(toString, level, name, value);

      while (iterator.hasNext()) {
        index++;

        name = indexName(index, length);

        value = iterator.next();

        ToString.formatNextPair(toString, level, name, value);
      }
    }

    ToString.formatEnd(toString, level);
  }

  /**
   * Returns the only element in this collection or throws an exception if the
   * collection is empty or if the collection contains more than one element.
   *
   * @return the only element of this collection
   *
   * @throws IllegalStateException
   *         if the collection is empty or if the collection contains more than
   *         one element
   */
  @Override
  public final E getOnly() {
    switch (size()) {
      case 0:
        throw new IllegalStateException("Could not getOnly: empty.");
      case 1:
        return getOnlyImpl();
      default:
        throw new IllegalStateException("Could not getOnly: more than one element.");
    }
  }

  /**
   * Returns {@code true} if this collection contains no elements.
   *
   * @return {@code true} if this collection contains no elements
   */
  @Override
  public final boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Returns an unmodifiable iterator over the elements in this collection.
   *
   * @return an unmodifiable iterator over the elements in this collection
   */
  @Override
  public abstract UnmodifiableIterator<E> iterator();

  /**
   * Returns a new string by joining together, in iteration order, the
   * string representation of each element in this collection.
   *
   * <p>
   * The string representation of each element is obtained by invoking its
   * {@link Object#toString()} method.
   *
   * @return a new string representing this collection
   */
  @Override
  public String join() {
    if (isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();

    var it = iterator();

    while (it.hasNext()) {
      var element = it.next();

      sb.append(element);
    }

    return sb.toString();
  }

  /**
   * Returns a new string by joining together, in iteration order, the
   * string representation of each element in this collection separated by the
   * specified {@code delimiter}.
   *
   * <p>
   * The string representation of each element is obtained by invoking its
   * {@link Object#toString()} method.
   *
   * @param delimiter
   *        the separator to use
   *
   * @return a new string representing this collection
   */
  @Override
  public String join(String delimiter) {
    Check.notNull(delimiter, "delimiter == null");

    if (isEmpty()) {
      return "";
    }

    var sb = new StringBuilder();

    var it = iterator();

    var element = it.next();

    sb.append(element);

    while (it.hasNext()) {
      sb.append(delimiter);

      element = it.next();

      sb.append(element);
    }

    return sb.toString();
  }

  /**
   * Returns a new string by joining together, in iteration order, the
   * string representation of each element in this collection separated by the
   * specified {@code delimiter} and with the specified {@code prefix} and
   * {@code suffix}.
   *
   * @param delimiter
   *        the separator to use
   * @param prefix
   *        the first part of the output
   * @param suffix
   *        the last part of the output
   *
   * @return a new string representing this collection
   */
  @Override
  public String join(String delimiter, String prefix, String suffix) {
    Check.notNull(delimiter, "delimiter == null");
    Check.notNull(prefix, "prefix == null");
    Check.notNull(suffix, "suffix == null");

    if (isEmpty()) {
      return prefix + suffix;
    }

    var sb = new StringBuilder();

    sb.append(prefix);

    var it = iterator();

    var element = it.next();

    sb.append(element);

    while (it.hasNext()) {
      sb.append(delimiter);

      element = it.next();

      sb.append(element);
    }

    sb.append(suffix);

    return sb.toString();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param o
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean remove(Object o) {
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
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean removeAll(Collection<?> c) {
    throw new UnsupportedOperationException();
  }

  /**
   * This operation is not supported.
   *
   * <p>
   * This method performs no operation other than throw an
   * {@link UnsupportedOperationException}.
   *
   * @param filter
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean removeIf(Predicate<? super E> filter) {
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
   *        ignored (this operation is not supported)
   *
   * @return this method does not return as it always throw an exception
   *
   * @throws UnsupportedOperationException
   *         always
   */
  @Override
  public final boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
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
  public Object[] toArray() {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Not implemented in this release. It might be implemented in a future
   * release.
   *
   * @param a
   *        ignored (this operation in not yet implemented)
   *
   * @return this method does not return as it always throws an exception
   *
   * @throws UnsupportedOperationException
   *         this method may be implemented in a future release
   */
  @Override
  public <T> T[] toArray(T[] a) {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  /**
   * Returns the string representation of this collection as defined by the
   * {@link ToString.Formattable#formatToString(StringBuilder, int)} method.
   *
   * @return a string representation of this collection
   */
  @Override
  public final String toString() {
    return ToString.of(this);
  }

  /**
   * This method is called by {@link #getOnly()} when the size is
   * {@code size == 1}.
   *
   * @return the only element in this collection
   */
  E getOnlyImpl() {
    var it = iterator();

    return it.next();
  }

  /**
   * Returns a name for the specified index having the specified length, result
   * is padded with zero digits if necessary.
   *
   * <p>
   * Auxiliary method for {@link #formatToString(StringBuilder, int)}
   * implementation.
   *
   * <p>
   * Examples:
   *
   * <pre>{@code
   * indexName(1, 3); // returns "001"
   * indexName(37, 3); // returns "037"
   * indexName(912, 3); // returns "912"
   * }</pre>
   *
   * @param index
   *        the index value to format
   * @param length
   *        the length of the result string
   *
   * @return a name for the specified index having the specified length
   */
  final String indexName(int index, int length) {
    var sb = new StringBuilder();

    var s = Integer.toString(index);

    var indexLength = s.length();

    var diff = length - indexLength;

    for (int i = 0; i < diff; i++) {
      sb.append(' ');
    }

    sb.append(s);

    return sb.toString();
  }

  /**
   * Returns the number of digits this collection's size has.
   *
   * <p>
   * Auxiliary method for {@link #formatToString(StringBuilder, int)}
   * implementation.
   *
   * @return the number of digits this collection's size has
   */
  final int sizeDigits() {
    int size = size();

    if (size < 10) {
      return 1;
    }

    else if (size < 100) {
      return 2;
    }

    else {
      double l = Math.log10(size);

      double f = Math.floor(l);

      return (int) f;
    }
  }

}