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

import module java.base;
import objectos.internal.Bytes;

final class HttpRequestParser1UrlDecoder {

  private final InputStream input;

  HttpRequestParser1UrlDecoder(InputStream input) {
    this.input = input;
  }

  public final int decode(HttpRequestParserException.Kind kind) throws IOException {
    final byte high1;
    high1 = readPerc(kind);

    final byte low1;
    low1 = readPerc(kind);

    final int perc1;
    perc1 = decodePerc(high1, low1);

    return switch (high1) {
      // 0yyyzzzz
      case 0b0000, 0b0001,
           0b0010, 0b0011,
           0b0100, 0b0101, 0b0110, 0b0111 -> perc1;

      // 110xxxyy 10yyzzzz
      case 0b1100, 0b1101 -> decodePerc2(kind, perc1);

      // 1110wwww 10xxxxyy 10yyzzzz
      case 0b1110 -> decodePerc3(kind, perc1);

      // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
      case 0b1111 -> decodePerc4(kind, perc1);

      default -> {
        final String msg;
        msg = "Invalid percent-encoded value: 0x%02X is not a valid UTF-8 1-byte sequence".formatted(perc1);

        throw new HttpRequestParserException(msg, kind);
      }
    };
  }

  private int decodePerc2(HttpRequestParserException.Kind kind, int perc1) throws IOException {
    final int perc2;
    perc2 = decodePercNext(kind, 2, 2);

    final int c;
    c = (perc1 & 0b1_1111) << 6 | (perc2 & 0b11_1111);

    if (c < 0x80 || c > 0x7FF) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X 0x%02X is not a valid UTF-8 2-byte sequence".formatted(perc1, perc2);

      throw new HttpRequestParserException(msg, kind);
    }

    return c;
  }

  private int decodePerc3(HttpRequestParserException.Kind kind, int perc1) throws IOException {
    final int perc2;
    perc2 = decodePercNext(kind, 2, 3);

    final int perc3;
    perc3 = decodePercNext(kind, 3, 3);

    final int c;
    c = (perc1 & 0b1111) << 12 | (perc2 & 0b11_1111) << 6 | (perc3 & 0b11_1111);

    if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X 0x%02X 0x%02X is not a valid UTF-8 3-byte sequence".formatted(perc1, perc2, perc3);

      throw new HttpRequestParserException(msg, kind);
    }

    return c;
  }

  private int decodePerc4(HttpRequestParserException.Kind kind, int perc1) throws IOException {
    final int perc2;
    perc2 = decodePercNext(kind, 2, 4);

    final int perc3;
    perc3 = decodePercNext(kind, 3, 4);

    final int perc4;
    perc4 = decodePercNext(kind, 4, 4);

    final int c;
    c = (perc1 & 0b111) << 18 | (perc2 & 0b11_1111) << 12 | (perc3 & 0b11_1111) << 6 | (perc4 & 0b11_1111);

    if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X 0x%02X 0x%02X 0x%02X is not a valid UTF-8 4-byte sequence".formatted(perc1, perc2, perc3, perc4);

      throw new HttpRequestParserException(msg, kind);
    }

    return c;
  }

  private int decodePerc(byte high, byte low) {
    return (high << 4) | low;
  }

  private int decodePercNext(HttpRequestParserException.Kind kind, int current, int total) throws IOException {
    readPercSep(kind, current, total);

    final byte high;
    high = readPerc(kind);

    final byte low;
    low = readPerc(kind);

    final int perc;
    perc = decodePerc(high, low);

    if (!utf8Byte(perc)) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X is not a valid UTF-8 byte".formatted(perc);

      throw new HttpRequestParserException(msg, kind);
    }

    return perc;
  }

  private byte readPerc(HttpRequestParserException.Kind kind) throws IOException {
    final int c;
    c = input.read();

    if (c < 0) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X is not an US-ASCII char".formatted(c);

      throw new HttpRequestParserException(msg, kind);
    }

    final byte b;
    b = (byte) c;

    final byte perc;
    perc = Bytes.fromHexDigit(b);

    if (perc < 0) {
      final String msg;
      msg = "Invalid percent-encoded value: 0x%02X is not an US-ASCII digit char".formatted(c);

      throw new HttpRequestParserException(msg, kind);
    }

    return perc;
  }

  private void readPercSep(HttpRequestParserException.Kind kind, int current, int total) throws IOException {
    final int c;
    c = input.read();

    if (c != '%') {
      final String msg;
      msg = "Invalid percent-encoded value: got 0x%02X instead of start of byte %d of a %d-byte UTF-8 code point".formatted(c, current, total);

      throw new HttpRequestParserException(msg, kind);
    }
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

}
