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
package objectos.http;

import java.util.function.Predicate;
import objectos.internal.Ascii;

/// Provides the standard path parameter predicate implementations.
public final class PathParams {

  private PathParams() {}

  /// The path parameter value must only contain ASCII digits.
  ///
  /// @return a predicate that checks if the string contain only ASCII digits.
  public static Predicate<String> digits() {
    return s -> {
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
    };
  }

}
