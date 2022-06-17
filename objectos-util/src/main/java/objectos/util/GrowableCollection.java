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

/**
 * A {@link Collection} that can be modified by adding elements; it does not
 * support removal of elements
 *
 * <p>
 * This interface extends the {@code BaseCollection} interface by providing
 * additional methods for adding elements to the collection.
 *
 * @param <E> type of the elements in this collection
 */
public interface GrowableCollection<E> extends BaseCollection<E> {

  /**
   * Adds all elements of the given {@link Iterable} to this collection.
   *
   * <p>
   * Elements are added in the order they are returned by the given iterable's
   * iterator. If the iterator returns a {@code null} element then this method
   * does not return and throws a {@link NullPointerException} instead.
   *
   * @param elements
   *        the elements to be added to this collection
   *
   * @return {@code true} if this collection changed as a result of this
   *         operation
   */
  boolean addAllIterable(Iterable<? extends E> elements);

  /**
   * Adds the specified element {@code e} to this collection or throws a
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
   * coll.addWithNullMessage(value, "value == null");</pre>
   *
   * @param e
   *        an element to be added to this collection
   * @param nullMessage
   *        the {@code NullPointerException} message
   *
   * @return {@code true} if this collection changed as a result of this
   *         operation
   */
  boolean addWithNullMessage(E e, Object nullMessage);

  /**
   * Adds the specified element {@code e} to this collection or throws a
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
   * coll.addWithNullMessage(element, "elements[", index, "] == null");</pre>
   *
   * @param e
   *        an element to be added to this collection
   * @param nullMessageStart
   *        the first part of the {@code NullPointerException} message
   * @param index
   *        the second part of the {@code NullPointerException} message
   * @param nullMessageEnd
   *        the third part of the {@code NullPointerException} message
   *
   * @return {@code true} if this collection changed as a result of this
   *         operation
   */
  boolean addWithNullMessage(E e, Object nullMessageStart, int index, Object nullMessageEnd);

}
