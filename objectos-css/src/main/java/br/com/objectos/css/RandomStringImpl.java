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
package br.com.objectos.css;

import java.util.Random;
import objectos.lang.RandomString;

final class RandomStringImpl {

  private static final Random RANDOM = new Random();

  private static final RandomString INSTANCE = new RandomString(RANDOM);

  private RandomStringImpl() {}

  public static void randomSeed(long seed) {
    RANDOM.setSeed(seed);
  }

  static String next(int length) {
    return INSTANCE.nextString(length);
  }

}