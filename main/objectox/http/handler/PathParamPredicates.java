/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.handler;

import java.util.Objects;
import java.util.function.Predicate;
import objectos.http.PathParam;
import objectos.internal.Ascii;

public enum PathParamPredicates implements Predicate<String> {

  TRUE,

  DIGITS;

  public final PathParam forName(String name) {
    var n = Objects.requireNonNull(name, "name == null");

    return new PathParamNamed(n, this);
  }

  @Override
  public final boolean test(String s) {
    return switch (this) {
      case TRUE -> true;

      case DIGITS -> testDigits(s);
    };
  }

  private boolean testDigits(String s) {
    if (s.isEmpty()) {
      return false;
    }

    for (int idx = 0, len = s.length(); idx < len; idx++) {
      final char c;
      c = s.charAt(idx);

      if (!Ascii.isDigit(c)) {
        return false;
      }
    }

    return true;
  }

}
