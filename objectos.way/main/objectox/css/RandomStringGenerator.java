/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectox.css;

import java.util.Locale;
import java.util.Random;
import java.util.Set;
import objectos.lang.object.Check;
import objectos.util.GrowableSet;
import objectox.lang.RandomString;

public final class RandomStringGenerator {

  private static final int MAX_TRIES = 20;

  private static final Random RANDOM = new Random();

  private static final RandomString INSTANCE = new RandomString(RANDOM);

  private static final Set<String> NAMES = new GrowableSet<>();

  private RandomStringGenerator() {}

  public static void randomSeed(long seed) {
    RANDOM.setSeed(seed);
  }

  public static String nextName(int length) {
    Check.argument(length > 0, "length must be > 0");

    for (int i = 0; i < MAX_TRIES; i++) {
      String name;
      name = RandomStringGenerator.nextString(length);

      char first;
      first = name.charAt(0);

      if (Character.isDigit(first)) {
        continue;
      }

      name = name.toLowerCase(Locale.US);

      if (NAMES.add(name)) {
        return name;
      }
    }

    throw new IllegalArgumentException(
      "Could not generate distinct name after " + MAX_TRIES + " tries");
  }

  public static String nextString(int length) {
    return INSTANCE.nextString(length);
  }

}