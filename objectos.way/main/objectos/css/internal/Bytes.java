/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import objectos.lang.Check;

final class Bytes {

  private static final int BYTE_MASK = 0xFF;

  private static final int MAX2_INDEX = 1 << 16 - 1;

  public static final int VARINT_MAX1 = 0x7F;

  public static final int VARINT_MAX2 = 0x7F00 | 0x00FF;

  private Bytes() {}

  public static int decodeFixedLength(byte len0, byte len1) {
    int length0;
    length0 = toInt(len0, 0);

    int length1;
    length1 = toInt(len1, 8);

    return length1 | length0;
  }

  public static int decodeIndex2(byte b0, byte b1) {
    int index0;
    index0 = toInt(b0, 0);

    int index1;
    index1 = toInt(b1, 8);

    return index1 | index0;
  }

  public static double doubleValue(
      byte b0, byte b1, byte b2, byte b3, byte b4, byte b5, byte b6, byte b7) {
    long v0 = toLong(b0, 0);
    long v1 = toLong(b1, 8);
    long v2 = toLong(b2, 16);
    long v3 = toLong(b3, 24);
    long v4 = toLong(b4, 32);
    long v5 = toLong(b5, 40);
    long v6 = toLong(b6, 48);
    long v7 = toLong(b7, 56);

    long bits;
    bits = v7 | v6 | v5 | v4 | v3 | v2 | v1 | v0;

    return Double.longBitsToDouble(bits);
  }

  public static byte int0(int value) {
    return (byte) value;
  }

  public static byte int1(int value) {
    return (byte) (value >>> 8);
  }

  public static byte int2(int value) {
    return (byte) (value >>> 16);
  }

  public static byte int3(int value) {
    return (byte) (value >>> 24);
  }

  public static int intValue(byte b0, byte b1) {
    int v0;
    v0 = toInt(b0, 0);

    int v1;
    v1 = toInt(b1, 8);

    return v1 | v0;
  }

  public static int intValue(byte b0, byte b1, byte b2, byte b3) {
    int v0 = toInt(b0, 0);
    int v1 = toInt(b1, 8);
    int v2 = toInt(b2, 16);
    int v3 = toInt(b3, 24);

    return v3 | v2 | v1 | v0;
  }

  public static byte len0(int index) {
    Check.argument(index <= MAX2_INDEX, "Template or element is too large.");

    return (byte) index;
  }

  public static byte len1(int index) {
    return (byte) (index >>> 8);
  }

  public static byte long0(long value) {
    return (byte) (value >>> 0);
  }

  public static byte long1(long value) {
    return (byte) (value >>> 8);
  }

  public static byte long2(long value) {
    return (byte) (value >>> 16);
  }

  public static byte long3(long value) {
    return (byte) (value >>> 24);
  }

  public static byte long4(long value) {
    return (byte) (value >>> 32);
  }

  public static byte long5(long value) {
    return (byte) (value >>> 40);
  }

  public static byte long6(long value) {
    return (byte) (value >>> 48);
  }

  public static byte long7(long value) {
    return (byte) (value >>> 56);
  }

  // we use 2 bytes for the StandardName enum
  public static byte name0(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) ordinal;
  }

  // we use 2 bytes for the StandardName enum
  public static byte name1(StandardName name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) (ordinal >>> 8);
  }

  // we use 2 bytes for the Property enum
  public static byte prop0(Enum<?> name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) ordinal;
  }

  // we use 2 bytes for the Property enum
  public static byte prop1(Enum<?> name) {
    int ordinal;
    ordinal = name.ordinal();

    return (byte) (ordinal >>> 8);
  }

  public static Property property(byte b0, byte b1) {
    int ordinal0;
    ordinal0 = Bytes.toInt(b0, 0);

    int ordinal1;
    ordinal1 = Bytes.toInt(b1, 8);

    int ordinal = ordinal1 | ordinal0;

    return Property.byOrdinal(ordinal);
  }

  public static String propertyName(byte b0, byte b1) {
    Property property;
    property = property(b0, b1);

    return property.cssName;
  }

  public static String standardNameValue(byte b0, byte b1) {
    int ordinal0 = Bytes.toInt(b0, 0);
    int ordinal1 = Bytes.toInt(b1, 8);

    int ordinal = ordinal1 | ordinal0;

    StandardName name;
    name = StandardName.byOrdinal(ordinal);

    return name.cssName;
  }

  public static int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

  public static long toLong(byte b, int shift) {
    return (b & (long) BYTE_MASK) << shift;
  }

  public static int toVarInt(byte b0, byte b1) {
    int intValue;
    intValue = b0 & 0x7F;

    intValue |= b1 << 7;

    return intValue;
  }

  public static byte two0(int index) {
    Check.argument(index <= MAX2_INDEX, "CssTemplate is too large.");

    return (byte) index;
  }

  public static byte two1(int index) {
    return (byte) (index >>> 8);
  }

  public static byte unit(LengthUnit unit) {
    return int0(unit.ordinal());
  }

  public static byte var0(int value) {
    byte b0;
    b0 = (byte) (value & 0x7F);

    b0 |= (byte) 0x80;

    return b0;
  }

  public static byte var1(int value) {
    value = value >>> 7;

    return (byte) value;
  }

  public static int varInt(byte[] buf, int off, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length has to be >= 0");
    }

    if (length <= VARINT_MAX1) {
      buf[off++] = (byte) length;

      return off;
    }

    if (length <= VARINT_MAX2) {
      buf[off++] = var0(length);

      buf[off++] = var1(length);

      return off;
    }

    throw new IllegalArgumentException(
      "CssTemplate is too large"
    );
  }

  public static int varIntR(byte[] buf, int off, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length has to be >= 0");
    }

    if (length <= VARINT_MAX1) {
      buf[off++] = (byte) length;

      return off;
    }

    if (length <= VARINT_MAX2) {
      buf[off++] = var1(length);

      buf[off++] = var0(length);

      return off;
    }

    throw new IllegalArgumentException(
      "CssTemplate is too large"
    );
  }

}