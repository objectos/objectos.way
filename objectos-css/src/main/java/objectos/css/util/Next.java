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

import java.util.Locale;
import java.util.Random;
import java.util.Set;
import objectos.css.tmpl.Api;
import objectos.lang.Check;
import objectos.lang.RandomString;
import objectos.util.GrowableSet;

public class Next {

  public static class Builder {

    private Random random;

    private int length = 5;

    private Builder() {}

    public final Next build() {
      if (random == null) {
        random = new Random();
      }

      RandomString randomString;
      randomString = new RandomString(random);

      return new Next(randomString, length);
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

  private final RandomString randomString;

  private final int length;

  private final int maxTries = 20;

  private final Set<String> names = new GrowableSet<>();

  private Next(RandomString random, int length) {
    this.randomString = random;
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

  private String cssIdentifier() {
    for (int i = 0; i < maxTries; i++) {
      String name;
      name = randomString.nextString(length);

      char first;
      first = name.charAt(0);

      if (Character.isDigit(first)) {
        continue;
      }

      name = name.toLowerCase(Locale.US);

      if (names.add(name)) {
        return name;
      }
    }

    throw new IllegalArgumentException(
      "Could not generate distinct name after " + maxTries + " tries");
  }

}