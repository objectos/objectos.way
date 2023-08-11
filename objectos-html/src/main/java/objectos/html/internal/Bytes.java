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
package objectos.html.internal;

import objectos.html.tmpl.StandardElementName;

public final class Bytes {

  public static final int VARINT_MAX1 = 0x7F;

  public static final int VARINT_MAX2 = 0x7F00 | 0x00FF;

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

  public static int decodeVarInt(byte int0, byte int1) {
    int value;
    value = int0 & VARINT_MAX1;

    value |= int1 << 7;

    return value;
  }

  public static byte encodeInt0(int value) {
    return (byte) value;
  }

  public static byte encodeInt1(int value) {
    return (byte) (value >>> 8);
  }

  public static byte encodeName(StandardElementName name) {
    int ordinal;
    ordinal = name.ordinal();

    return encodeInt0(ordinal);
  }

  public static int encodeVarInt(byte[] buf, int off, int value) {
    if (value < 0) {
      throw new IllegalArgumentException("value has to be >= 0");
    }

    if (value <= VARINT_MAX1) {
      buf[off++] = (byte) value;

      return off;
    }

    if (value <= VARINT_MAX2) {
      buf[off++] = encodeVarInt0(value);

      buf[off++] = encodeVarInt1(value);

      return off;
    }

    throw new IllegalArgumentException(
      "HtmlTemplate is too large"
    );
  }

  public static byte encodeVarInt0(int value) {
    byte b0;
    b0 = (byte) (value & 0x7F);

    b0 |= 0x80;

    return b0;
  }

  public static byte encodeVarInt1(int value) {
    value = value >>> 7;

    return (byte) value;
  }

  public static int encodeVarIntR(byte[] buf, int off, int length) {
    if (length < 0) {
      throw new IllegalArgumentException("Length has to be >= 0");
    }

    if (length <= VARINT_MAX1) {
      buf[off++] = (byte) length;

      return off;
    }

    if (length <= VARINT_MAX2) {
      buf[off++] = encodeVarInt1(length);

      buf[off++] = encodeVarInt0(length);

      return off;
    }

    throw new IllegalArgumentException(
      "HtmlTemplate is too large"
    );
  }

  public static int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

}