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
import objectos.way.Http.CsrfToken;

final class HttpToken implements Http.CsrfToken {

  static final class ParseException extends Exception {

    private static final long serialVersionUID = -7205302637303864948L;

    ParseException(String message) {
      super(message);
    }

  }

  private static final char[] BASE64_CHARS = """
      ABCDEFGHIJKLMNOPQRSTUVWXYZ\
      abcdefghijklmnopqrstuvwxyz\
      0123456789\
      -_\
      """.toCharArray();

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

  final long l0, l1, l2, l3;

  private int hashCode;

  private HttpToken(long l0, long l1, long l2, long l3) {
    this.l0 = l0;
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;
  }

  public static HttpToken of(RandomGenerator random, int bytesLength) {
    return switch (bytesLength) {
      case 32 -> of32(random);

      default -> throw new IllegalArgumentException("Invalid bytesLength=" + bytesLength);
    };
  }

  public static HttpToken of32(long l0, long l1, long l2, long l3) {
    return new HttpToken(l0, l1, l2, l3);
  }

  public static HttpToken of32(RandomGenerator random) {
    return new HttpToken(
        random.nextLong(),
        random.nextLong(),
        random.nextLong(),
        random.nextLong()
    );
  }

  public static HttpToken parse(CharSequence s, int bytesLength) throws ParseException {
    return switch (bytesLength) {
      case 32 -> parse32(s);

      default -> throw new IllegalArgumentException("Invalid bytesLength=" + bytesLength);
    };
  }

  private static HttpToken parse32(CharSequence s) throws ParseException {
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

    return new HttpToken(l0, l1, l2, l3);
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
  public final boolean checkCsrfToken(CsrfToken other) {
    final HttpToken impl;
    impl = (HttpToken) other;

    return equalsConstantTime(impl);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HttpToken that
        && l0 == that.l0 && l1 == that.l1 && l2 == that.l2 && l3 == that.l3;
  }

  public final boolean equalsConstantTime(HttpToken obj) {
    final long diff0 = l0 ^ obj.l0;
    final long diff1 = l1 ^ obj.l1;
    final long diff2 = l2 ^ obj.l2;
    final long diff3 = l3 ^ obj.l3;

    final long diff = diff0 | diff1 | diff2 | diff3;

    return diff == 0;
  }

  @Override
  public final int hashCode() {
    int hc;
    hc = hashCode;

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
    bytes[offset + 7] = (byte) (value >>> 0);
  }

  @Override
  public final String toString() {
    final StringBuilder sb;
    sb = new StringBuilder(44);

    int bits;
    bits = 0;

    bits = encode(sb, bits, l0);
    bits = encode(sb, bits, l0, l1);
    bits = encode(sb, bits, l1);
    bits = encode(sb, bits, l1, l2);
    bits = encode(sb, bits, l2);
    bits = encode(sb, bits, l2, l3);
    bits = encode(sb, bits, l3);
    bits = encode(sb, bits, l3, 0L);

    sb.append('=');

    return sb.toString();
  }

  private int encode(StringBuilder sb, int bitsUsed, long value) {
    int bits;
    bits = 64 - bitsUsed;

    do {
      bits -= 6;

      final long shifted;
      shifted = (value >>> bits);

      final long sextet;
      sextet = shifted & 0x3F;

      final char c;
      c = BASE64_CHARS[(int) sextet];

      sb.append(c);
    } while (bits >= 6);

    return bits;
  }

  private int encode(StringBuilder sb, int bits, long prev, long next) {
    final long prevMask;
    prevMask = mask(bits);

    long index;
    index = prev & prevMask;

    final int nextBits;
    nextBits = 6 - bits;

    index <<= nextBits;

    final int shift;
    shift = 64 - nextBits;

    final long valueMask;
    valueMask = mask(nextBits);

    index |= (next >>> shift) & valueMask;

    final char c;
    c = BASE64_CHARS[(int) index];

    sb.append(c);

    return nextBits;
  }

  private static long mask(int bits) {
    return (1L << bits) - 1;
  }

}