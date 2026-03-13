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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import objectos.internal.Ascii;

final class HttpHeaderValueBuilderImpl implements HttpHeaderValueBuilder {

  private static final byte[] HEADER_PARAM_NAME;

  private static final byte HEADER_PARAM_NAME_INVALID = 0;

  private static final byte HEADER_PARAM_NAME_VALID = 1;

  static {
    final byte[] table;
    table = new byte[128];

    Ascii.fill(table, Http.tchar(), HEADER_PARAM_NAME_VALID);

    HEADER_PARAM_NAME = table;
  }

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
    tchar = Http.tchar();

    for (int idx = 0, len = tchar.length(); idx < len; idx++) {
      final char c;
      c = tchar.charAt(idx);

      table[c] = HEADER_PARAM_VALUE_UNQUOTED;
    }

    final String vchar;
    vchar = Http.vchar();

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

  private final StringBuilder stringBuilder = new StringBuilder();

  @Override
  public final void value(String value) {
    Objects.requireNonNull(value);

    if (!stringBuilder.isEmpty()) {
      stringBuilder.append(", ");
    }

    stringBuilder.append(value);
  }

  @Override
  public final void param(String name, String value) {
    checkParameterName(name);

    final int len; // early implicit null-check
    len = value.length();

    if (stringBuilder.isEmpty()) {
      throw new IllegalStateException("Cannot add a parameter: there's no current value");
    }

    stringBuilder.append(';');
    stringBuilder.append(' ');
    stringBuilder.append(name);
    stringBuilder.append('=');

    if (len == 0) {
      stringBuilder.append("\"\"");

      return;
    }

    // we assume value will be unquoted

    int quotesIndex;
    quotesIndex = stringBuilder.length();

    int valueIndex = 0;

    while (valueIndex < len) {
      final char c;
      c = value.charAt(valueIndex);

      if (c >= 128) {
        throw Http.invalidFieldContent(valueIndex, c);
      }

      final byte flag;
      flag = HEADER_PARAM_VALUE[c];

      if (flag == HEADER_PARAM_VALUE_INVALID) {
        throw Http.invalidFieldContent(valueIndex, c);
      }

      // we're safe so far, char is valid

      valueIndex++;

      if (flag == HEADER_PARAM_VALUE_UNQUOTED) {
        stringBuilder.append(c);

        continue;
      }

      // dquotes needed

      if (quotesIndex > 0) {
        stringBuilder.insert(quotesIndex, '"');

        quotesIndex = -1;
      }

      if (flag == HEADER_PARAM_VALUE_ESCAPE) {
        stringBuilder.append('\\');
      }

      stringBuilder.append(c);
    }

    if (quotesIndex < 0) {
      stringBuilder.append('"');
    }
  }

  @Override
  public final void param(String name, Charset charset, String value) {
    checkParameterName(name);

    if (charset != StandardCharsets.UTF_8) {
      throw new IllegalArgumentException("The UTF-8 charset MUST be used.");
    }

    Objects.requireNonNull(value, "value == null");

    stringBuilder.append(';');
    stringBuilder.append(' ');
    stringBuilder.append(name);
    stringBuilder.append('=');

    final String encoded;
    encoded = Http.rfc8187(value);

    stringBuilder.append(encoded);
  }

  final String build() {
    return stringBuilder.toString();
  }

  private void checkParameterName(String name) {
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
        "Parameter name contains an invalid character at index " + idx + ": '" + c + "'"
    );
  }

}