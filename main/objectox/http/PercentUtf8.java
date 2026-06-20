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

final class PercentUtf8 {

  private char highSurrogate;

  private final PercentEncoder parent;

  private final PercentString string;

  PercentUtf8(PercentEncoder parent, PercentString string) {
    this.parent = parent;

    this.string = string;
  }

  public final String encode() {
    while (parent.hasNext()) {
      final char c;
      c = parent.next();

      if (parent.test(c)) {
        highSurrogate = ensureZero(highSurrogate);

        string.append(c);
      }

      else if (c <= 0x7F) {
        highSurrogate = ensureZero(highSurrogate);

        string.encode(c);
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        string.encode(byte0, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        string.encode(byte0, byte1, byte2, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        string.encode(byte0, byte1, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    return string.toString();
  }

  private char ensureZero(char highSurrogate) {
    if (highSurrogate != 0) {
      throw new IllegalArgumentException("High surrogate \\u" + Integer.toHexString(highSurrogate) + " must be followed by a low surrogate.");
    }

    return 0;
  }

}
