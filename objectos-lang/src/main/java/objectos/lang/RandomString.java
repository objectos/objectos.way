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
package objectos.lang;

import java.security.SecureRandom;
import java.util.Random;

/**
 * A {@code RandomString} is used to generate random string values
 * from a specified dictionary and a {@link Random} instance.
 *
 * <p>
 * Each random {@link java.lang.String} instance created by the generator
 * will only contain characters from the {@code dictionary}.
 *
 * @since 0.2
 */
public final class RandomString {

  /**
   * The default dictionary consisting of the digits 0 through 9 and all ASCII
   * uppercase and all ASCII lowercase letters.
   */
  public static final String DEFAULT_DICTIONARY
      = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

  private final char[] dictionary;

  private final Random random;

  /**
   * Constructs a random string generator using a newly constructed
   * {@link java.security.SecureRandom} instance and the characters from
   * {@link #DEFAULT_DICTIONARY} as a dictionary.
   */
  public RandomString() {
    this.random = new SecureRandom();

    this.dictionary = DEFAULT_DICTIONARY.toCharArray();
  }

  /**
   * Constructs a new random string generator using the supplied {@code random}
   * instance and the characters from the {@link #DEFAULT_DICTIONARY} as a
   * dictionary.
   *
   * @param random
   *        the random number generator to be used by this instance
   *
   * @throws NullPointerException
   *         if {@code random} is null
   */
  public RandomString(Random random) {
    this.random = Check.notNull(random, "random == null");

    this.dictionary = DEFAULT_DICTIONARY.toCharArray();
  }

  /**
   * Constructs a new random string generator using the supplied {@code random}
   * instance and the characters from the supplied {@code dictionary}.
   *
   * @param random
   *        the random number generator to be used by this instance
   * @param dictionary
   *        characters to use for generating random strings
   *
   * @throws IllegalArgumentException
   *         if {@code dictionary} length is either zero or one
   * @throws NullPointerException
   *         if {@code random} or {@code dictionary} is null
   */
  public RandomString(Random random, String dictionary) {
    Check.notNull(dictionary, "dictionary == null");
    Check.argument(dictionary.length() > 1, "dictionary.length() > 1");

    this.dictionary = dictionary.toCharArray();

    this.random = Check.notNull(random, "random == null");
  }

  /**
   * Returns the next random string having the supplied {@code length}. The
   * generated string will only have characters that also exist in the
   * dictionary.
   *
   * @param length
   *        the length of the generated random string
   *
   * @return the next random string having the supplied {@code length}
   *
   * @throws IllegalArgumentException
   *         if {@code length <= 0}
   */
  public final String nextString(int length) {
    Check.argument(length > 0, "length has to be > 0");

    return randomString0(length);
  }

  private String randomString0(int length) {
    char[] result;
    result = new char[length];

    int index;
    index = 0;

    int fullIntCount;
    fullIntCount = length / 4;

    for (int i = 0; i < fullIntCount; i++) {
      int value;
      value = random.nextInt();

      result[index++] = dictionary[((value >> 12) & 0xFF) % dictionary.length];

      result[index++] = dictionary[((value >> 8) & 0xFF) % dictionary.length];

      result[index++] = dictionary[((value >> 4) & 0xFF) % dictionary.length];

      result[index++] = dictionary[(value & 0xFF) % dictionary.length];
    }

    int lastIntCount;
    lastIntCount = length % 4;

    if (lastIntCount > 0) {
      int value;
      value = random.nextInt();

      switch (lastIntCount) {
        case 3:
          result[index + 2] = dictionary[((value >> 4) & 0xFF) % dictionary.length];
          // fall through
        case 2:
          result[index + 1] = dictionary[((value >> 8) & 0xFF) % dictionary.length];
          // fall through
        case 1:
          result[index + 0] = dictionary[((value >> 12) & 0xFF) % dictionary.length];
          break;
        default:
          throw new AssertionError("Should not happen");
      }
    }

    return new String(result);
  }

}
