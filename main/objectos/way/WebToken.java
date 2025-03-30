/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.Arrays;
import java.util.random.RandomGenerator;

final class WebToken implements Web.Token {

  static final class ParseException extends Exception {

    private static final long serialVersionUID = -7205302637303864948L;

    ParseException(String message) {
      super(message);
    }

  }

  private static final char[] BASE64_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();

  private static final long BASE64_MASK = 0x3F;

  private static final byte[] BASE64_VALUES;

  static {
    final byte[] bytes;
    bytes = new byte[128];

    Arrays.fill(bytes, (byte) -1);

    for (byte value = 0; value < BASE64_CHARS.length; value++) {
      final char idx;
      idx = BASE64_CHARS[value];

      bytes[idx] = value;
    }

    BASE64_VALUES = bytes;
  }

  final long l0;
  final long l1;
  final long l2;
  final long l3;

  private int hashCode;

  private WebToken(long l0, long l1, long l2, long l3) {
    this.l0 = l0;
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;
  }

  public static WebToken of(RandomGenerator random, int bitLength) {
    return switch (bitLength) {
      case 32 -> of32(random);

      default -> throw new IllegalArgumentException("Invalid bitLength=" + bitLength);
    };
  }

  public static WebToken of32(long l0, long l1, long l2, long l3) {
    return new WebToken(l0, l1, l2, l3);
  }

  public static WebToken of32(RandomGenerator random) {
    return new WebToken(
        random.nextLong(),
        random.nextLong(),
        random.nextLong(),
        random.nextLong()
    );
  }

  public static WebToken parse(CharSequence s, int bitLength) throws ParseException {
    return switch (bitLength) {
      case 32 -> parse32(s);

      default -> throw new IllegalArgumentException("Invalid bitLength=" + bitLength);
    };
  }

  private static WebToken parse32(CharSequence s) throws ParseException {
    final int length;
    length = s.length();

    if (length != 44) {
      throw new ParseException("Token length");
    }

    int iter;
    iter = 0;

    final long l0;
    l0 = decode(s, iter++, 0, 4); // iter=0, 0 + 60 + 4

    final long l1;
    l1 = decode(s, iter++, 2, 2); // iter=1, 2 + 60 + 2

    final long l2;
    l2 = decode(s, iter++, 4, 0); // iter=2, 4 + 60 + 0

    final long l3;
    l3 = decode(s, iter++, 6, -2); // iter=3, 6 + 58 + -2

    return new WebToken(l0, l1, l2, l3);
  }

  private static long decode(CharSequence s, int iter, int firstBits, int lastBits) throws ParseException {
    long result;
    result = 0L;

    int index;
    index = iter * 11;

    int bits;
    bits = 64;

    if (firstBits > 0) {
      // there are leftovers bits from previous last char
      index--;

      bits -= firstBits;
    } else {
      // no leftover bits
      bits -= 6;
    }

    while (bits >= 0) {
      final long decode;
      decode = decode(s, index++);

      result |= decode << bits;

      bits -= 6;
    }

    if (bits < 0) {
      int abs;
      abs = Math.abs(bits);

      final long decode;
      decode = decode(s, index++);

      result |= decode >>> abs;
    }

    return result;
  }

  private static long decode(CharSequence s, int index) throws ParseException {
    final char c;
    c = s.charAt(index);

    if (c >= BASE64_VALUES.length) {
      throw new ParseException("Token character");
    }

    final byte value;
    value = BASE64_VALUES[c];

    if (value < 0) {
      throw new ParseException("Token character");
    }

    return value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof WebToken that
        && l0 == that.l0 && l1 == that.l1 && l2 == that.l2 && l3 == that.l3;
  }

  @Override
  public final int hashCode() {
    int hc = hashCode;

    if (hc == 0) {
      hc = 17;

      hc = 31 * hc + Long.hashCode(l0);
      hc = 31 * hc + Long.hashCode(l1);
      hc = 31 * hc + Long.hashCode(l2);
      hc = 31 * hc + Long.hashCode(l3);

      hashCode = hc;
    }

    return hc;
  }

  public final byte[] toByteArray() {
    final byte[] bytes;
    bytes = new byte[32];

    toByteArray(bytes, 0, l0);
    toByteArray(bytes, 8, l1);
    toByteArray(bytes, 16, l2);
    toByteArray(bytes, 24, l3);

    return bytes;
  }

  private void toByteArray(byte[] bytes, int offset, long value) {
    bytes[offset + 0] = (byte) (value >>> 56);
    bytes[offset + 1] = (byte) (value >>> 48);
    bytes[offset + 2] = (byte) (value >>> 40);
    bytes[offset + 3] = (byte) (value >>> 32);
    bytes[offset + 4] = (byte) (value >>> 24);
    bytes[offset + 5] = (byte) (value >>> 16);
    bytes[offset + 6] = (byte) (value >>> 8);
    bytes[offset + 7] = (byte) value;
  }

  @Override
  public final String toString() {
    final StringBuilder sb;
    sb = new StringBuilder(44);

    int prevBits;
    prevBits = 0;

    prevBits = encode(sb, prevBits, 0L, l0);
    prevBits = encode(sb, prevBits, l0, l1);
    prevBits = encode(sb, prevBits, l1, l2);
    prevBits = encode(sb, prevBits, l2, l3);

    if (prevBits > 0) {
      encodePrev(sb, prevBits, l3, 0L);
    }

    final int length;
    length = sb.length();

    final int mod;
    mod = length % 4;

    if (mod > 0) {
      final int paddingNeeeded;
      paddingNeeeded = (4 - mod);

      for (int i = 0; i < paddingNeeeded; i++) {
        sb.append('=');
      }
    }

    return sb.toString();
  }

  private int encode(StringBuilder sb, int prevBits, long prev, long value) {
    int bits;
    bits = 64 - 6;

    if (prevBits > 0) {
      final int valueBits;
      valueBits = encodePrev(sb, prevBits, prev, value);

      bits -= valueBits;
    }

    int lastBits;
    lastBits = bits;

    while (bits >= 0) {
      long index;
      index = (value >>> bits) & BASE64_MASK;

      final char c;
      c = BASE64_CHARS[(int) index];

      sb.append(c);

      lastBits = bits;

      bits -= 6;
    }

    return lastBits;
  }

  private int encodePrev(StringBuilder sb, int prevBits, long prev, long value) {
    final long prevMask;
    prevMask = mask(prevBits);

    long index;
    index = prev & prevMask;

    final int valueBits;
    valueBits = 6 - prevBits;

    index <<= valueBits;

    final int bits;
    bits = 64 - valueBits;

    final long valueMask;
    valueMask = mask(valueBits);

    index |= (value >>> bits) & valueMask;

    final char c;
    c = BASE64_CHARS[(int) index];

    sb.append(c);

    return valueBits;
  }

  private static long mask(int bits) {
    return (1L << bits) - 1;
  }

}