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
package objectos.css;

import java.util.Locale;
import java.util.Set;
import objectos.css.select.ClassSelector;
import objectos.css.select.SelectorFactory;
import objectos.lang.Check;
import objectos.util.GrowableSet;

final class RandomClassSelectorGenerator {

  private static final int MAX_TRIES = 20;

  private static final Set<String> NAMES = new GrowableSet<>();

  private RandomClassSelectorGenerator() {}

  public static ClassSelector randomClassSelector(int length) {
    Check.argument(length > 0, "length must be > 0");

    for (int i = 0; i < MAX_TRIES; i++) {
      var className = RandomStringImpl.next(length);

      char first = className.charAt(0);

      if (Character.isDigit(first)) {
        continue;
      }

      className = className.toLowerCase(Locale.US);

      if (NAMES.add(className)) {
        return SelectorFactory.dot(className);
      }
    }

    throw new IllegalArgumentException(
      "Could not generate distinct ClassSelector after " + MAX_TRIES + " tries");
  }

  public static ClassSelector randomDot(int length) {
    return randomClassSelector(length);
  }

}