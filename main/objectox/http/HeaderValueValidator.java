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

import java.util.Objects;

final class HeaderValueValidator {

  private int index;

  private final String value;

  HeaderValueValidator(String value) {
    this.value = Objects.requireNonNull(value, "value == null");
  }

  public final void validate() {
    if (!hasNext()) {
      // empty is valid
      return;
    }

    validateLeading();

    final boolean ws;
    ws = validateContents();

    if (ws) {
      final String msg;
      msg = "Invalid header value: trailing SPACE or HTAB characters are not allowed";

      throw new IllegalArgumentException(msg);
    }
  }

  private void validateLeading() {
    final byte first;
    first = next();

    if (first == Rfc.HEADER_VALUE_VALID) {
      return;
    }

    if (first == Rfc.HEADER_VALUE_WS) {
      final String msg;
      msg = "Invalid header value: leading SPACE or HTAB characters are not allowed";

      throw new IllegalArgumentException(msg);
    }

    throw invalidChar();
  }

  private boolean validateContents() {
    boolean ws;
    ws = false;

    while (hasNext()) {
      final byte flag;
      flag = next();

      if (flag == Rfc.HEADER_VALUE_VALID) {
        ws = false;

        continue;
      }

      if (flag == Rfc.HEADER_VALUE_WS) {
        ws = true;

        continue;
      }

      throw invalidChar();
    }

    return ws;
  }

  private boolean hasNext() {
    return index < value.length();
  }

  private byte next() {
    final int idx;
    idx = index++;

    final char c;
    c = value.charAt(idx);

    if (c >= 128) {
      throw invalidChar();
    }

    return Rfc.HEADER_VALUE_TABLE[c];
  }

  private IllegalArgumentException invalidChar() {
    final int idx;
    idx = index - 1;

    final char c;
    c = value.charAt(idx);

    final String msg;
    msg = "Invalid header value: character '%c' at index %d is not allowed".formatted(c, idx);

    throw new IllegalArgumentException(msg);
  }

}
