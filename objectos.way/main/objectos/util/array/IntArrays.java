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
package objectos.util.array;

import objectos.lang.object.Check;

/**
 * This class provides {@code static} methods for operating on {@code int}
 * arrays.
 */
public final class IntArrays {

	static final int[] EMPTY = new int[0];

	private IntArrays() {}

	/**
	 * Returns the empty zero-length {@code int} array instance. Since it is a
	 * zero-length instance, values cannot be inserted nor removed from it.
	 *
	 * @return an empty {@code int} array
	 */
	public static int[] empty() {
		return EMPTY;
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

		var newLength = Grow.arrayLength(length, requiredIndex);

		var result = new int[newLength];

		System.arraycopy(array, 0, result, 0, length);

		return result;
	}

	/**
	 * Copies the values of the array into a larger one (if necessary) so that a
	 * {@code int[]} can be inserted at the required index. More formally:
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
	 * <li>a new {@code int[]} array instance is created. The length of the new
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
	 * array = IntArrays.copyIfNecessary(array, currentIndex);
	 * array[currentIndex++] = new int[123];</pre>
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
	public static int[][] growIfNecessary(int[][] array, int requiredIndex) {
		Check.argument(requiredIndex >= 0, "requiredIndex cannot be negative");

		var length = array.length;

		if (requiredIndex < length) {
			return array;
		}

		var newLength = Grow.arrayLength(length, requiredIndex);

		var result = new int[newLength][];

		System.arraycopy(array, 0, result, 0, length);

		return result;
	}

}