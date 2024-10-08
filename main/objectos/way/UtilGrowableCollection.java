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
package objectos.way;

import java.util.Collection;

/**
 * A {@link Collection} that can be modified by adding elements.
 * Except for the {@link #clear()} method, it does not support most of the
 * methods that remove elements.
 *
 * <p>
 * This class extends the {@code BaseCollection} class by providing
 * additional methods for adding elements to the collection.
 *
 * @param <E> type of the elements in this collection
 */
public abstract class UtilGrowableCollection<E>
    extends UtilBaseCollection<E>
    implements Util.GrowableCollection<E> {

  /**
   * Sole constructor
   */
  protected UtilGrowableCollection() {}

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
  @Override
  public abstract boolean addAllIterable(Iterable<? extends E> elements);

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
  @Override
  public abstract boolean addWithNullMessage(E e, Object nullMessage);

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
  @Override
  public abstract boolean addWithNullMessage(
      E e, Object nullMessageStart, int index, Object nullMessageEnd);

}
