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

import java.util.Random;
import objectox.lang.RandomString;

/**
 * Provides {@code static} methods for generating random byte arrays, string and
 * primitive values. Actual pseudo number generation is delegated to the
 * {@link Random} class. Random string generation is delegated to the
 * {@link RandomString} class.
 *
 * <p>
 * Please not that this class is designed to be used in testing environments
 * only. It is not designed to be used in production.
 */
public final class Next {

  private static final Random RANDOM;

  private static final RandomString STRING;

  static {
    Random r;
    r = new Random();

    RANDOM = r;

    RandomString stringGenerator;
    stringGenerator = new RandomString(r);

    STRING = stringGenerator;
  }

  private Next() {}

  /**
   * Fills the specified byte array with next random byte values.
   *
   * @param bytes
   *        the array to be filled with next random byte values
   *
   * @see Random#nextBytes(byte[])
   */
  public static void bytes(byte[] bytes) {
    RANDOM.nextBytes(bytes);
  }

  /**
   * Creates and returns a new byte array with the specified length filled with
   * next random byte values.
   *
   * @param length
   *        the length of the array to be created
   *
   * @return a new byte array with the specified length filled with next random
   *         byte values
   *
   * @see Random#nextBytes(byte[])
   */
  public static byte[] bytes(int length) {
    byte[] bytes;
    bytes = new byte[length];

    RANDOM.nextBytes(bytes);

    return bytes;
  }

  /**
   * Returns the next random double value.
   *
   * @return the next random double value
   *
   * @see Random#nextDouble()
   */
  public static double doubleValue() {
    return RANDOM.nextDouble();
  }

  /**
   * Returns the next random int value.
   *
   * @return the next random int value
   *
   * @see Random#nextInt()
   */
  public static int intValue() {
    return RANDOM.nextInt();
  }

  /**
   * Returns the next random int value between zero (inclusive) and the
   * specified value (exclusive).
   *
   * @return the next random int value between zero (inclusive) and the
   *         specified value (exclusive)
   *
   * @see Random#nextInt(int)
   */
  public static int intValue(int bound) {
    return RANDOM.nextInt(bound);
  }

  /**
   * Returns the next random string with the specified length.
   *
   * @return he next random string with the specified length
   *
   * @see RandomString#nextString(int)
   */
  public static String string(int length) {
    return STRING.nextString(length);
  }

}