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
package objectos.css.util;

import java.util.Random;
import objectos.css.tmpl.Api;
import objectox.lang.Check;

public class Next {

  public static class Builder {

    private Random random;

    private int length = 6;

    private Builder() {}

    public final Next build() {
      if (random == null) {
        random = new Random();
      }

      return new Next(random, length);
    }

    public final Builder nameLength(int value) {
      Check.argument(value > 0, "name length must be > 0");

      length = value;

      return this;
    }

    public final Builder random(Random value) {
      random = Check.notNull(value, "value == null");

      return this;
    }

  }

  private static final char[] DICTIONARY = "abcdefghijklmnopqrstuvwxyz".toCharArray();

  private static final int MASK = 0x1F;

  private final Random random;

  private final int length;

  private Next(Random random, int length) {
    this.random = random;

    this.length = length;
  }

  public static Builder builder() {
    return new Builder();
  }

  public final ClassSelector classSelector() {
    String name;
    name = cssIdentifier();

    return ClassSelector.of(name);
  }

  public final <T extends Api.PropertyValue> CustomProperty<T> customProperty() {
    String name;
    name = "--" + cssIdentifier();

    return CustomProperty.named(name);
  }

  @SuppressWarnings("fallthrough")
  private String cssIdentifier() {
    char[] result;
    result = new char[length];

    int index;
    index = 0;

    int fullIntCount;
    fullIntCount = length / 6;

    for (int i = 0; i < fullIntCount; i++) {
      int value;
      value = random.nextInt();

      result[index++] = DICTIONARY[((value >>> 25) & MASK) % DICTIONARY.length];

      result[index++] = DICTIONARY[((value >>> 20) & MASK) % DICTIONARY.length];

      result[index++] = DICTIONARY[((value >>> 15) & MASK) % DICTIONARY.length];

      result[index++] = DICTIONARY[((value >>> 10) & MASK) % DICTIONARY.length];

      result[index++] = DICTIONARY[((value >>> 5) & MASK) % DICTIONARY.length];

      result[index++] = DICTIONARY[(value & MASK) % DICTIONARY.length];
    }

    int lastIntCount;
    lastIntCount = length % 6;

    if (lastIntCount > 0) {
      int value;
      value = random.nextInt();

      switch (lastIntCount) {
        case 5:
          result[index + 4] = DICTIONARY[((value >>> 5) & MASK) % DICTIONARY.length];
          // fall through
        case 4:
          result[index + 3] = DICTIONARY[((value >>> 10) & MASK) % DICTIONARY.length];
          // fall through
        case 3:
          result[index + 2] = DICTIONARY[((value >>> 15) & MASK) % DICTIONARY.length];
          // fall through
        case 2:
          result[index + 1] = DICTIONARY[((value >>> 20) & MASK) % DICTIONARY.length];
          // fall through
        case 1:
          result[index + 0] = DICTIONARY[((value >>> 25) & MASK) % DICTIONARY.length];
          break;
        default:
          throw new AssertionError("Should not happen");
      }
    }

    return new String(result);
  }

}