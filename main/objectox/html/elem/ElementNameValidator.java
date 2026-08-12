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
package objectox.html.elem;

import java.util.Objects;

final class ElementNameValidator {

  private final String name;

  ElementNameValidator(String name) {
    this.name = Objects.requireNonNull(name, "name == null");
  }

  public final String validate() {
    final int length;
    length = name.length();

    if (length == 0) {
      final String msg;
      msg = "Invalid element name: it must not be empty";

      throw new IllegalArgumentException(msg);
    }

    final char first;
    first = name.charAt(0);

    if (!Character.isLetter(first)) {
      final String msg;
      msg = "Invalid element name: it must start with a letter";

      throw new IllegalArgumentException(msg);
    }

    for (int idx = 1; idx < length; idx++) {
      final char c;
      c = name.charAt(idx);

      if (Character.isLetterOrDigit(c)) {
        continue;
      }

      if (c == '-') {
        continue;
      }

      final String msg;
      msg = "Invalid element name: it must not contain the character '%c'".formatted(c);

      throw new IllegalArgumentException(msg);
    }

    return name;
  }

}
