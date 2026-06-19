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

final class HeaderParamValueFormatter {

  private static final byte[] HEADER_PARAM_VALUE;

  private static final byte HEADER_PARAM_VALUE_INVALID = 0;

  private static final byte HEADER_PARAM_VALUE_UNQUOTED = 1;

  private static final byte HEADER_PARAM_VALUE_QUOTED = 2;

  private static final byte HEADER_PARAM_VALUE_ESCAPE = 3;

  static {
    final byte[] table;
    table = new byte[128];

    // parameter-value = ( token / quoted-string )

    final String tchar;
    tchar = Rfc.tchar();

    for (int idx = 0, len = tchar.length(); idx < len; idx++) {
      final char c;
      c = tchar.charAt(idx);

      table[c] = HEADER_PARAM_VALUE_UNQUOTED;
    }

    final String vchar;
    vchar = Rfc.vchar();

    for (int idx = 0, len = vchar.length(); idx < len; idx++) {
      final char c;
      c = vchar.charAt(idx);

      if (table[c] == HEADER_PARAM_VALUE_INVALID) {
        table[c] = HEADER_PARAM_VALUE_QUOTED;
      }
    }

    table[' '] = HEADER_PARAM_VALUE_QUOTED;
    table['\t'] = HEADER_PARAM_VALUE_QUOTED;

    table['"'] = HEADER_PARAM_VALUE_ESCAPE;
    table['\\'] = HEADER_PARAM_VALUE_ESCAPE;

    HEADER_PARAM_VALUE = table;
  }

  private char c;

  private int index;

  private final String value;

  HeaderParamValueFormatter(String value) {
    this.value = Objects.requireNonNull(value, "value == null");
  }

  public final String format() {
    if (hasNext()) {
      return format0();
    } else {
      return "\"\"";
    }
  }

  public final String format0() {
    while (hasNext()) {
      final byte flag;
      flag = next();

      if (flag == HEADER_PARAM_VALUE_UNQUOTED) {
        continue;
      }

      return formatQuoted(flag);
    }

    return value;
  }

  private String formatQuoted(byte first) {
    final StringBuilder quoted;
    quoted = new StringBuilder();

    quoted.append('"');

    if (first != HEADER_PARAM_VALUE_ESCAPE) {
      final String prefix;
      prefix = value.substring(0, index);

      quoted.append(prefix);
    } else {
      final String prefix;
      prefix = value.substring(0, index - 1);

      quoted.append(prefix);

      quoted.append('\\');

      quoted.append(c);
    }

    while (hasNext()) {
      final byte flag;
      flag = next();

      if (flag != HEADER_PARAM_VALUE_ESCAPE) {
        quoted.append(c);
      } else {
        quoted.append('\\');

        quoted.append(c);
      }
    }

    quoted.append('"');

    return quoted.toString();
  }

  private boolean hasNext() {
    return index < value.length();
  }

  private byte next() {
    final int idx;
    idx = index++;

    c = value.charAt(idx);

    if (c >= 128) {
      throw invalidChar();
    }

    final byte flag;
    flag = HEADER_PARAM_VALUE[c];

    if (flag == HEADER_PARAM_VALUE_INVALID) {
      throw invalidChar();
    }

    return flag;
  }

  private IllegalArgumentException invalidChar() {
    throw new UnsupportedOperationException("Implement me");
  }

}
