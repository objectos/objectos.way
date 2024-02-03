/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

final class Bytes {

  private static final int BYTE_MASK = 0xFF;

  private Bytes() {}

  public static int decodeInt(byte b0) {
    return toInt(b0, 0);
  }

  public static int decodeInt(byte b0, byte b1) {
    int int0;
    int0 = toInt(b0, 0);

    int int1;
    int1 = toInt(b1, 8);

    return int1 | int0;
  }

  public static byte encodeInt0(int value) {
    return (byte) value;
  }

  public static byte encodeInt1(int value) {
    return (byte) (value >>> 8);
  }

  public static byte encodeInt2(int value) {
    return (byte) (value >>> 16);
  }

  public static byte encodeName(StandardElementName name) {
    int ordinal;
    ordinal = name.ordinal();

    return encodeInt0(ordinal);
  }

  static final int VARINT_MAX1 = 0x7F;

  static final int VARINT_MAX2 = (1 << 14) - 1;

  static final int VARINT_MAX3 = (1 << 21) - 1;

  public static int encodeCommonEnd(byte[] buf, int off, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length has to be >= 0");
    }

    if (length <= VARINT_MAX1) {
      buf[off++] = (byte) length;

      return off;
    }

    if (length <= VARINT_MAX2) {
      buf[off++] = encodeVarintHigh(length, 7);

      buf[off++] = encodeVarint(length, 0);

      return off;
    }

    if (length <= VARINT_MAX3) {
      buf[off++] = encodeVarintHigh(length, 14);

      buf[off++] = encodeVarint(length, 7);

      buf[off++] = encodeVarint(length, 0);

      return off;
    }

    throw new IllegalArgumentException(
        "HtmlTemplate is too large :: length=" + length
    );
  }

  private static byte encodeVarintHigh(int value, int shift) {
    value = value >>> shift;

    return (byte) value;
  }

  private static byte encodeVarint(int value, int shift) {
    value = value >>> shift;

    value = value & VARINT_MAX1;

    value = value | 0x80;

    return (byte) value;
  }

  public static int decodeCommonEnd(byte[] buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf[endIndex];

      case 2 -> decodeVarint(buf[endIndex], buf[endIndex - 1]);

      case 3 -> decodeVarint(buf[endIndex], buf[endIndex - 1], buf[endIndex - 2]);

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  public static int encodeOffset(byte[] buf, int off, int value) {
    if (value < 0) {
      throw new IllegalArgumentException("value has to be >= 0");
    }

    if (value <= VARINT_MAX1) {
      buf[off++] = (byte) value;

      return off;
    }

    if (value <= VARINT_MAX2) {
      buf[off++] = encodeVarint(value, 0);

      buf[off++] = encodeVarintHigh(value, 7);

      return off;
    }

    if (value <= VARINT_MAX3) {
      buf[off++] = encodeVarint(value, 0);

      buf[off++] = encodeVarint(value, 7);

      buf[off++] = encodeVarintHigh(value, 14);

      return off;
    }

    throw new IllegalArgumentException(
        "HtmlTemplate is too large :: value=" + value
    );
  }

  public static int decodeOffset(byte[] buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf[startIndex];

      case 2 -> decodeVarint(buf[startIndex], buf[startIndex + 1]);

      case 3 -> decodeVarint(buf[startIndex], buf[startIndex + 1], buf[startIndex + 2]);

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  public static int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

  private static int decodeVarint(byte b0, byte b1) {
    int int0;
    int0 = decodeVarintValue(b0, 0);

    int int1;
    int1 = decodeVarintValue(b1, 7);

    return int0 | int1;
  }

  private static int decodeVarint(byte b0, byte b1, byte b2) {
    int int0;
    int0 = decodeVarintValue(b0, 0);

    int int1;
    int1 = decodeVarintValue(b1, 7);

    int int2;
    int2 = decodeVarintValue(b2, 14);

    return int0 | int1 | int2;
  }

  private static int decodeVarintValue(byte value, int shift) {
    int intValue;
    intValue = value & VARINT_MAX1;

    return intValue << shift;
  }

  static final int FIXED1_MAX = (1 << 8) - 1;

  static final int FIXED2_MAX = (1 << 16) - 1;

  static final int FIXED3_MAX = (1 << 24) - 1;

  public static int encodeLength3(byte[] buf, int off, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length has to be >= 0");
    }

    if (length <= FIXED3_MAX) {
      buf[off++] = (byte) length;

      length = length >>> 8;

      buf[off++] = (byte) length;

      length = length >>> 8;

      buf[off++] = (byte) length;

      return off;
    }

    throw new IllegalArgumentException(
        "HtmlTemplate is too large :: length=" + length
    );
  }

  public static int decodeLength3(byte b0, byte b1, byte b2) {
    int int0;
    int0 = toInt(b0, 0);

    int int1;
    int1 = toInt(b1, 8);

    int int2;
    int2 = toInt(b2, 16);

    return int0 | int1 | int2;
  }

}