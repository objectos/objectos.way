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
package objectox.html.markup;

final class TextEscaper {

  private final String value;

  TextEscaper(String value) {
    this.value = value;
  }

  public final String escape() {
    final int len;
    len = value.length();

    int idx;
    idx = 0;

    for (; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      if (c == '&' || c == '<' || c == '>') {
        break;
      }
    }

    if (idx == len) {
      return value;
    }

    final StringBuilder out;
    out = new StringBuilder(len + 16);

    out.append(value, 0, idx);

    for (; idx < len; idx++) {
      final char c;
      c = value.charAt(idx);

      switch (c) {
        case '&' -> out.append("&amp;");

        case '<' -> out.append("&lt;");

        case '>' -> out.append("&gt;");

        default -> out.append(c);
      }
    }

    return out.toString();
  }

}
