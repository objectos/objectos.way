/*
 * Copyright (C) 2015-2026 Objectos Software LTDA.
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
package objectox.html;

import objectos.html.ElementName;
import objectox.html.elem.ElementNamePojo;

public final class HtmlBytes {

  private static final int BYTE_MASK = 0xFF;

  private HtmlBytes() {}

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

  public static byte encodeName(ElementName name) {
    final ElementNamePojo impl;
    impl = (ElementNamePojo) name;

    final int ordinal;
    ordinal = impl.index();

    return encodeInt0(ordinal);
  }

  private static final int VARINT_MAX1 = 0x7F;

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

  public static int decodeCommonEnd(ByteArray buf, int startIndex, int endIndex) {
    int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> buf.get(endIndex);

      case 2 -> decodeVarint(buf.get(endIndex), buf.get(endIndex - 1));

      case 3 -> decodeVarint(buf.get(endIndex), buf.get(endIndex - 1), buf.get(endIndex - 2));

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
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

  public static int decodeVarint(byte b0, byte b1) {
    int int0;
    int0 = decodeVarintValue(b0, 0);

    int int1;
    int1 = decodeVarintValue(b1, 7);

    return int0 | int1;
  }

  public static int decodeVarint(byte b0, byte b1, byte b2) {
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

  public static final int FIXED3_MAX = (1 << 24) - 1;

  public static int decodeLength3(byte b0, byte b1, byte b2) {
    final int int0;
    int0 = toInt(b0, 0);

    final int int1;
    int1 = toInt(b1, 8);

    final int int2;
    int2 = toInt(b2, 16);

    return int0 | int1 | int2;
  }

}