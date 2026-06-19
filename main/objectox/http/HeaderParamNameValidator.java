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
import objectos.internal.Ascii;

final class HeaderParamNameValidator {

  private static final byte[] HEADER_PARAM_NAME;

  private static final byte HEADER_PARAM_NAME_INVALID = 0;

  private static final byte HEADER_PARAM_NAME_VALID = 1;

  static {
    final byte[] table;
    table = new byte[128];

    Ascii.fill(table, Rfc.tchar(), HEADER_PARAM_NAME_VALID);

    HEADER_PARAM_NAME = table;
  }

  private final String name;

  HeaderParamNameValidator(String name) {
    this.name = Objects.requireNonNull(name, "name == null");
  }

  public final void validate() {
    final int len;
    len = name.length();

    for (int idx = 0; idx < len; idx++) {
      final char c;
      c = name.charAt(idx);

      if (c >= 128) {
        throw invalidParameterName(idx, c);
      }

      final byte flag;
      flag = HEADER_PARAM_NAME[c];

      if (flag == HEADER_PARAM_NAME_INVALID) {
        throw invalidParameterName(idx, c);
      }
    }
  }

  private IllegalArgumentException invalidParameterName(int idx, char c) {
    return new IllegalArgumentException(
        "Invalid parameter name: character '%c' at index %d is not allowed".formatted(c, idx)
    );
  }

}
