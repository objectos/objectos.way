/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SequencedMap;
import java.util.Set;

/**
 * The <strong>Objectos Util</strong> main class.
 */
public final class Util {

  /**
   * An empty zero-length {@code byte} array instance.
   */
  public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

  /**
   * An empty zero-length {@code int} array instance.
   */
  public static final int[] EMPTY_INT_ARRAY = new int[0];

  /**
   * An empty zero-length {@code Object} array instance.
   */
  public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  static final int DEFAULT_CAPACITY = 10;

  /**
   * From ArraysSupport.SOFT_MAX_ARRAY_LENGTH
   */
  static final int JVM_SOFT_LIMIT = Integer.MAX_VALUE - 8;

  private static final int BYTE_MASK = 0xFF;

  private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

  private static final int HEX_MASK = 0xf;

  private Util() {}

  static <E> List<E> createList() {
    return new UtilList<>();
  }

  static <K, V> Map<K, V> createMap() {
    return new UtilMap<>();
  }

  static <K, V> SequencedMap<K, V> createSequencedMap() {
    return new UtilSequencedMap<>();
  }

  static <E> Set<E> createSet() {
    return new UtilSet<>();
  }

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
   * <li>a new {@code byte} array instance is created. The length of the new
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
   * byte b = readByte();
   * array = ByteArrays.growIfNecessary(array, currentIndex);
   * array[currentIndex++] = b;</pre>
   *
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
  public static byte[] growIfNecessary(byte[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    var length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    var newLength = arrayLength(length, requiredIndex);

    var result = new byte[newLength];

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

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
   * <li>a new {@code int} array instance is created. The length of the new
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
   * int i = computeInt();
   * array = IntArrays.growIfNecessary(array, currentIndex);
   * array[currentIndex++] = i;</pre>
   *
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
  public static int[] growIfNecessary(int[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    var length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    var newLength = arrayLength(length, requiredIndex);

    var result = new int[newLength];

    System.arraycopy(array, 0, result, 0, length);

    return result;
  }

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
   * <li>a new array instance is created. The resulting array is of exactly the
   * same class as the original array. The length of the new
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
   * Foo foo = computeFoo();
   * array = ObjectArrays.growIfNecessary(array, currentIndex);
   * array[currentIndex++] = foo;</pre>
   *
   * @param <T>
   *        the type of the objects in the array
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
  public static <T> T[] growIfNecessary(T[] array, int requiredIndex) {
    Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

    var length = array.length;

    if (requiredIndex < length) {
      return array;
    }

    var newLength = arrayLength(length, requiredIndex);

    return Arrays.copyOf(array, newLength);
  }

  /**
   * Joins the string representation of each of the elements of the specified
   * collection separated by the specified {@code delimiter}.
   *
   * @param coll
   *        the collection containing the elements
   * @param delimiter
   *        the separator to use between each element's string representation
   *
   * @return a new string resulting from joining together the elements
   *         separated by the specified {@code delimiter}
   */
  static String join(Collection<?> coll, String delimiter) {
    if (coll instanceof UtilList list) {
      return list.join(delimiter);
    } else {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Returns a string representation of the array in hexadecimal.
   *
   * <p>
   * The returned string is formed by concatenating the hexadecimal
   * representation of each byte value from the array in order. Therefore,
   * the returned string length is double the length of the array.
   *
   * <p>
   * The following characters are used as hexadecimal digits:
   *
   * <pre>0123456789abcdef</pre>
   *
   * @param array
   *        the array to be converted.
   *
   * @return the string representation of the array in hexadecimal.
   *
   * @throws NullPointerException
   *         if {@code array} is null
   */
  public static String toHexString(byte[] array) {
    Check.notNull(array, "array == null");

    var length = array.length;

    switch (length) {
      case 0:
        return "";
      default:
        return toHexStringNonEmpty(array, 0, length);
    }
  }

  static <E> List<E> toUnmodifiableList(List<E> list) {
    if (list instanceof UtilList<E> impl) {
      return impl.toUnmodifiableList();
    } else {
      throw new UnsupportedOperationException();
    }
  }

  static <K, V> Map<K, V> toUnmodifiableMap(Map<K, V> map) {
    if (map instanceof UtilMap<K, V> impl) {
      return impl.toUnmodifiableMap();
    } else {
      throw new UnsupportedOperationException();
    }
  }

  private static String toHexStringNonEmpty(byte[] array, int offset, int length) {
    var result = new char[length * 2];

    for (int i = 0, j = 0; i < length; i++) {
      var b = array[offset + i] & BYTE_MASK;

      result[j++] = HEX_CHARS[b >>> 4];

      result[j++] = HEX_CHARS[b & HEX_MASK];
    }

    return new String(result);
  }

  static int arrayLength(int length, int requiredIndex) {
    var halfLength = length >> 1;

    var newLength = length + halfLength;

    if (requiredIndex < newLength) {
      return newLength;
    } else {
      return growArrayLength0(requiredIndex);
    }
  }

  static int growBy(int length, int ammount) {
    int half = length >> 1;

    int delta = Math.max(ammount, half);

    int newLength = length + delta;

    return grow0(length, newLength);
  }

  static int growByOne(int length) {
    int half = length >> 1;

    int newLength = length + half;

    return grow0(length, newLength);
  }

  private static int grow0(int length, int newLength) {
    if (newLength > 0 && newLength <= JVM_SOFT_LIMIT) {
      return newLength;
    }

    if (length != JVM_SOFT_LIMIT) {
      return JVM_SOFT_LIMIT;
    }

    throw new OutOfMemoryError(
        """
      Cannot allocate array: exceeds JVM soft limit.

      length = %,14d
      limit  = %,14d
      """.formatted(length + 1, JVM_SOFT_LIMIT)
    );
  }

  private static int growArrayLength0(int requiredIndex) {
    var minLength = requiredIndex + 1;

    var halfLength = minLength >> 1;

    var newLength = minLength + halfLength;

    if (0 < newLength && newLength <= JVM_SOFT_LIMIT) {
      return newLength;
    } else {
      return growArrayLength1(minLength);
    }
  }

  private static int growArrayLength1(int minLength) {
    if (minLength < 0) {
      throw new OutOfMemoryError("Array size already at maximum");
    }

    if (minLength <= JVM_SOFT_LIMIT) {
      return JVM_SOFT_LIMIT;
    }

    return Integer.MAX_VALUE;
  }

  static Map<String, String> parsePropertiesMap(String string) {
    Map<String, String> builder;
    builder = createMap();

    String[] lines;
    lines = string.split("\n");

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      int colon;
      colon = line.indexOf(':');

      if (colon < 0) {
        throw new IllegalArgumentException(
            "The colon character ':' was not found in the line listed below:\n\n" + line + "\n"
        );
      }

      String key;
      key = line.substring(0, colon);

      String value;
      value = line.substring(colon + 1);

      builder.put(key.trim(), value.trim());
    }

    return toUnmodifiableMap(builder);
  }

  /**
   * An {@link java.util.Iterator} implementation that does not support the
   * remove operation.
   *
   * @param <E> the type of the elements in this iterator
   */
  static abstract class UnmodifiableIterator<E> implements Iterator<E> {

    /**
     * Sole constructor.
     */
    protected UnmodifiableIterator() {}

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
    public final void remove() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * An unmodifiable view of a map's keys, values or entries. All of the mutator
   * methods throw an {@link UnsupportedOperationException} when invoked.
   *
   * @param <E> the type of the elements in this view
   */
  static abstract class UnmodifiableView<E> extends UtilBaseCollection<E> implements Set<E> {

    protected UnmodifiableView() {}

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

    /**
     * <p>
     * Compares the specified object with this view for equality. Returns
     * {@code true} if and only if
     *
     * <ul>
     * <li>the specified object is also a {@link Collection};</li>
     * <li>both collections have same size; and</li>
     * <li>each element in this view is also present in the specified
     * collection.</li>
     * </ul>
     *
     * @param obj
     *        the object to be compared for equality with this view
     *
     * @return {@code true} if the specified object is equal to this view
     */
    @Override
    public final boolean equals(Object obj) {
      return obj == this
          || obj instanceof Collection<?> that && equals0(that);
    }

    @Override
    public Object[] toArray() {
      throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public <T> T[] toArray(T[] a) {
      throw new UnsupportedOperationException("Implement me");
    }

    private boolean equals0(Collection<?> that) {
      var size = size();

      if (size != that.size()) {
        return false;
      }

      var iter = iterator();

      while (iter.hasNext()) {
        var o = iter.next();

        if (!that.contains(o)) {
          return false;
        }
      }

      return true;
    }

  }

}
