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
package objectox.http;

import objectos.internal.Ascii;
import objectos.internal.VisibleForTesting;

final class PercentDictionary {

  private final boolean[] dictionary;

  PercentDictionary(String input) {
    final boolean[] valid;
    valid = new boolean[128];

    for (int idx = 0, len = input.length(); idx < len; idx++) {
      final char c;
      c = input.charAt(idx);

      if (c >= 128) {
        final String msg;
        msg = "Invalid dictionary string: only US-ASCII characters are allowed, but found '%c'".formatted(c);

        throw new IllegalArgumentException(msg);
      }

      valid[c] = true;
    }

    dictionary = valid;
  }

  public static PercentDictionary forUrlEncoder() {
    return new PercentDictionary(
        Ascii.visible()
    );
  }

  public final boolean test(char c) {
    return c < dictionary.length ? dictionary[c] : false;
  }

  @VisibleForTesting
  final int length() {
    return dictionary.length;
  }

}
