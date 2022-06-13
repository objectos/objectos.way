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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * This class provides {@code static} factory methods for the JDK
 * {@link java.util.Set} implementations.
 *
 * <p>
 * This class is mainly provided as a convenience for Java Multi-Release
 * codebases. In particular codebases that must support versions prior to Java
 * 7 and, therefore, cannot use the diamond operator.
 */
public final class Sets {

  private Sets() {}

  /**
   * Returns a new {@link HashSet} instance by invoking the constructor that
   * takes no arguments.
   *
   * @param <E> type of the elements in the set
   *
   * @return a new {@link HashSet} instance
   *
   * @see HashSet#HashSet()
   */
  public static <E> HashSet<E> newHashSet() {
    return new HashSet<E>();
  }

  /**
   * Returns a new {@link HashSet} instance containing all of the distinct
   * elements of the specified collection.
   *
   * @param <E> type of the elements in the set
   * @param c
   *        construct the new {@code HashSet} with all of the elements in this
   *        collection
   *
   * @return a new {@link HashSet} instance
   *
   * @see HashSet#HashSet(Collection)
   */
  public static <E> HashSet<E> newHashSet(Collection<? extends E> c) {
    return new HashSet<E>(c);
  }

  /**
   * Returns a new {@link HashSet} instance with the specified capacity.
   *
   * @param <E> type of the elements in the set
   *
   * @param initialCapacity
   *        the initial capacity of the set's hash table
   *
   * @return a new {@link HashSet} instance
   *
   * @see HashSet#HashSet(int)
   */
  public static <E> HashSet<E> newHashSetWithCapacity(int initialCapacity) {
    return new HashSet<E>(initialCapacity);
  }

  /**
   * Returns a new {@link LinkedHashSet} instance by invoking the constructor
   * that takes no arguments.
   *
   * @param <E> type of the elements in the set
   *
   * @return a new {@link LinkedHashSet} instance
   *
   * @see LinkedHashSet#LinkedHashSet()
   */
  public static <E> LinkedHashSet<E> newLinkedHashSet() {
    return new LinkedHashSet<E>();
  }

  /**
   * Returns a new {@link LinkedHashSet} instance containing all of the distinct
   * elements of the specified collection.
   *
   * @param <E> type of the elements in the set
   * @param c
   *        construct the new {@code LinkedHashSet} with all of the elements in
   *        this collection
   *
   * @return a new {@link LinkedHashSet} instance
   *
   * @see LinkedHashSet#LinkedHashSet(Collection)
   */
  public static <E> LinkedHashSet<E> newLinkedHashSet(Collection<? extends E> c) {
    return new LinkedHashSet<E>(c);
  }

  /**
   * Returns a new {@link LinkedHashSet} instance with the specified capacity.
   *
   * @param <E> type of the elements in the set
   *
   * @param initialCapacity
   *        the initial capacity of the set's hash table
   *
   * @return a new {@link LinkedHashSet} instance
   *
   * @see LinkedHashSet#LinkedHashSet(int)
   */
  public static <E> LinkedHashSet<E> newLinkedHashSetWithCapacity(int initialCapacity) {
    return new LinkedHashSet<E>(initialCapacity);
  }

  /**
   * Returns a new {@link TreeSet} instance by invoking the constructor that
   * takes no arguments.
   *
   * @param <E> type of the elements in the set
   *
   * @return a new {@link TreeSet} instance
   *
   * @see TreeSet#TreeSet()
   */
  public static <E> TreeSet<E> newTreeSet() {
    return new TreeSet<E>();
  }

  /**
   * Returns a new {@link TreeSet} instance containing all of the distinct
   * elements of the specified collection.
   *
   * @param <E> type of the elements in the set
   * @param c
   *        construct the new {@code TreeSet} with all of the elements in
   *        this collection
   *
   * @return a new {@link TreeSet} instance
   *
   * @see TreeSet#TreeSet(Collection)
   */
  public static <E> TreeSet<E> newTreeSet(Collection<? extends E> c) {
    return new TreeSet<E>(c);
  }

}