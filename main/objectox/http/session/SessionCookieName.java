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
package objectox.http.session;

import java.util.Objects;
import objectox.http.Rfc;

final class SessionCookieName {

  private final String name;

  SessionCookieName(String name) {
    this.name = Objects.requireNonNull(name, "name == null");
  }

  public final String validate() {
    if (name.isBlank()) {
      throw new IllegalArgumentException("Cookie name must not be blank");
    }

    // we don't store the table in a static field and, instead,
    // recreate it every time so it can be GCed afterwards
    final boolean[] validTable;
    validTable = new boolean[128];

    final String tchar;
    tchar = Rfc.tchar();

    for (int idx = 0, len = tchar.length(); idx < len; idx++) {
      final char c;
      c = tchar.charAt(idx);

      validTable[c] = true;
    }

    for (int idx = 0, len = name.length(); idx < len; idx++) {
      final char c;
      c = name.charAt(idx);

      if (c < 128 && validTable[c]) {
        continue;
      }

      throw new IllegalArgumentException("""
      Cookie name must only contain the following characters:
      \t"!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
      \tDIGIT (US-ASCII) / ALPHA (US-ASCII)
      """);
    }

    return name;
  }

}
