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

import java.lang.reflect.Array;
import objectos.lang.Check;

/**
 * <p>
 * This class provides {@code static} methods for operating on
 * {@link java.lang.Object} arrays.
 *
 * @since 2
 */
public final class ObjectArrays {

  private static final Object[] EMPTY = new Object[0];

  private ObjectArrays() {}

  /**
   * Copies the values of the array into a larger one (if necessary) so that a
   * value can be inserted at the required index. More formally:
   *
   * <p>
   * If the {@code requiredIndex} is smaller than {@code 0} then an
   * {@link java.lang.IllegalArgumentException} is thrown.
   *
   * <p>
   * If the {@code requiredIndex} is smaller than {@code array.length} then the
   * array is not copied and is returned unchanged.
   *
   * <p>
   * If the {@code requiredIndex} is equal to or is greater than
   * {@code array.length} then:
   *
   * <ol>
   * <li>a new {@code Object} array instance is created. The length of the new
   * array is guaranteed to be greater than {@code requiredIndex};</li>
   * <li>all values from the original array are copied into the new array so
   * that, for all valid indices in the original array, the new array contains
   * an identical value for the same index; and</li>
   * <li>the new array instance is returned.</li>
   * </ol>
   *
   * <p>
   * A typical usage is:
   *
   * <pre>
   * Object o = getValue();
   * array = ObjectArrays.copyIfNecessary(array, currentIndex);
   * array[currentIndex++] = o;</pre>
   *
   * @param <E> type of the elements in the array
   * @param array
   *        the array instance to be copied if necessary
   * @param requiredIndex
   *        the index where a value is to be inserted
   *
   * @return the {@code array} instance itself or a larger copy of the
   *         original
   *
   * @throws IllegalArgumentException
   *         if {@code requiredIndex < 0}
   */
  @SuppressWarnings("unchecked")
  public static <E> E[] copyIfNecessary(E[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    int length;
    length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    int newLength;
    newLength = MoreArrays.growArrayLength(length, requiredIndex);

    E[] result;

    Class<? extends Object[]> arrayType;
    arrayType = array.getClass();

    if (arrayType == Object[].class) {
      result = (E[]) new Object[newLength];
    } else {
      Class<?> componentType;
      componentType = arrayType.getComponentType();

      result = (E[]) Array.newInstance(componentType, newLength);
    }

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

  /**
   * Returns the empty zero-length {@link Object} array instance. Since it is a
   * zero-length instance, values cannot be inserted nor removed from it.
   *
   * @return an empty {@link Object} array
   */
  public static Object[] empty() {
    return EMPTY;
  }

  // Based on checkElementNotNull from:
  // https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ObjectArrays.java
  static <E> E checkNotNull(E[] value, int index, String identifier) {
    E element;
    element = value[index];

    if (element == null) {
      throw new NullPointerException(identifier + "[" + index + "] == null");
    }

    return element;
  }

  static Object[] copyWithNullCheckAsObjectArray(Object[] elements, String identifier) {
    Object[] copy = new Object[elements.length];
    for (int i = 0; i < elements.length; i++) {
      copy[i] = checkNotNull(elements, i, identifier);
    }
    return copy;
  }

  // Based on Builder.expandedCapacity from:
  // https://github.com/google/guava/blob/master/guava/src/com/google/common/collect/ImmutableCollection.java
  static int growArrayLength(int current, int minRequired) {
    if (minRequired < 0) {
      throw new AssertionError("cannot store more than MAX_VALUE elements");
    }

    int result = current + (current >> 1);

    if (result < minRequired) {
      result = Integer.highestOneBit(minRequired - 1) << 1;
    }

    if (result < 0) {
      result = Integer.MAX_VALUE;
    }

    return result;
  }

}