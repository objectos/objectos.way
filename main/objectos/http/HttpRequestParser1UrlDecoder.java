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

  @SuppressWarnings("serial")
  static final class DecodeException extends Exception {}

  private final HttpRequestParser0Input input;

  HttpRequestParser1UrlDecoder(HttpRequestParser0Input input) {
    this.input = input;
  }

  public final int decode() throws DecodeException, IOException {
    final byte high1;
    high1 = readPerc();

    return switch (high1) {
      // 0yyyzzzz
      case 0b0000, 0b0001,
           0b0010, 0b0011,
           0b0100, 0b0101, 0b0110, 0b0111 -> decodePerc1(high1);

      // 110xxxyy 10yyzzzz
      case 0b1100, 0b1101 -> decodePerc2(high1);

      // 1110wwww 10xxxxyy 10yyzzzz
      case 0b1110 -> decodePerc3(high1);

      // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
      case 0b1111 -> decodePerc4(high1);

      default -> throw new DecodeException();
    };
  }

  private int decodePerc1(byte high1) throws DecodeException, IOException {
    final byte low1;
    low1 = readPerc();

    return decodePerc(high1, low1);
  }

  private int decodePerc2(byte high1) throws DecodeException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int c;
    c = (perc1 & 0b1_1111) << 6 | (perc2 & 0b11_1111);

    if (c < 0x80 || c > 0x7FF) {
      throw new DecodeException();
    }

    return c;
  }

  private int decodePerc3(byte high1) throws DecodeException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int perc3;
    perc3 = decodePercNext();

    final int c;
    c = (perc1 & 0b1111) << 12 | (perc2 & 0b11_1111) << 6 | (perc3 & 0b11_1111);

    if (c < 0x800 || c > 0xFFFF || Character.isSurrogate((char) c)) {
      throw new DecodeException();
    }

    return c;
  }

  private int decodePerc4(byte high1) throws DecodeException, IOException {
    final byte low1;
    low1 = readPerc();

    final int perc1;
    perc1 = decodePerc(high1, low1);

    final int perc2;
    perc2 = decodePercNext();

    final int perc3;
    perc3 = decodePercNext();

    final int perc4;
    perc4 = decodePercNext();

    final int c;
    c = (perc1 & 0b111) << 18 | (perc2 & 0b11_1111) << 12 | (perc3 & 0b11_1111) << 6 | (perc4 & 0b11_1111);

    if (c < 0x1_0000 || !Character.isValidCodePoint(c)) {
      throw new DecodeException();
    }

    return c;
  }

  private int decodePerc(byte high, byte low) {
    return (high << 4) | low;
  }

  private int decodePercNext() throws DecodeException, IOException {
    readPercSep();

    final byte high;
    high = readPerc();

    final byte low;
    low = readPerc();

    final int perc;
    perc = decodePerc(high, low);

    if (!utf8Byte(perc)) {
      throw new DecodeException();
    }

    return perc;
  }

  private byte readPerc() throws DecodeException, IOException {
    final byte b;
    b = input.readByte();

    final byte perc;
    perc = Bytes.fromHexDigit(b);

    if (perc < 0) {
      throw new DecodeException();
    }

    return perc;
  }

  private void readPercSep() throws DecodeException, IOException {
    final byte b;
    b = input.readByte();

    if (b != '%') {
      throw new DecodeException();
    }
  }

  private boolean utf8Byte(int utf8) {
    final int topTwoBits;
    topTwoBits = utf8 & 0b1100_0000;

    return topTwoBits == 0b1000_0000;
  }

}
