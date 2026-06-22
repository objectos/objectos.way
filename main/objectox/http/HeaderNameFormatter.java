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

final class HeaderNameFormatter {

  private int index;

  private final String input;

  HeaderNameFormatter(String input) {
    this.input = input;
  }

  public final String format() {
    while (hasNext()) {
      final char original;
      original = next();

      final byte mapped;
      mapped = HeaderNamePojo.map(original);

      if (mapped < 0) {
        final int idx;
        idx = index - 1;

        final String msg;
        msg = "Invalid header name character '" + original + "' at index " + idx;

        throw new IllegalArgumentException(msg);
      }

      if (mapped != original) {
        return format0(mapped);
      }
    }

    return input;
  }

  private String format0(byte first) {
    final int length;
    length = input.length();

    final StringBuilder sb;
    sb = new StringBuilder(length);

    sb.append(input, 0, index - 1);

    sb.append((char) first);

    while (hasNext()) {
      final char original;
      original = next();

      final byte mapped;
      mapped = HeaderNamePojo.map(original);

      sb.append((char) mapped);
    }

    return sb.toString();
  }

  private boolean hasNext() {
    return index < input.length();
  }

  private char next() {
    return input.charAt(index++);
  }

}
