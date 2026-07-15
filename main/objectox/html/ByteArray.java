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
package objectox.html;

import java.util.Arrays;
import java.util.HexFormat;
import objectos.internal.Util;

public final class ByteArray {

  public static final int MAX_INT8 = (1 << 8) - 1;

  public static final int MAX_INT16 = (1 << 16) - 1;

  public static final int MAX_INT24 = (1 << 24) - 1;

  private static final int BYTE_MASK = 0xFF;

  private static final int VARINT_MAX1 = 0x7F;

  static final int VARINT_MAX2 = (1 << 14) - 1;

  private static final int VARINT_MAX3 = (1 << 21) - 1;

  private byte[] bytes;

  private int index;

  public ByteArray(int initialLength) {
    bytes = new byte[initialLength];
  }

  private ByteArray(byte[] bytes) {
    this.bytes = bytes;

    index = bytes.length;
  }

  public static ByteArray of(int... values) {
    final int length;
    length = values.length;

    final byte[] copy;
    copy = new byte[length];

    for (int idx = 0; idx < length; idx++) {
      copy[idx] = (byte) values[idx];
    }

    return new ByteArray(copy);
  }

  public final void add(byte b0) {
    bytes = Util.growIfNecessary(bytes, index + 0);
    bytes[index++] = b0;
  }

  public final int addBytes(ByteArray source, int off, int len) {
    final byte[] s;
    s = source.bytes;

    bytes = Util.growIfNecessary(bytes, index + len);

    System.arraycopy(s, off, bytes, index, len);

    index += len;

    return off + len;
  }

  public final void addInt8(int value) {
    if (value < 0 || value > MAX_INT8) {
      final String msg;
      msg = "Invalid 1-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final byte b;
    b = (byte) value;

    add(b);
  }

  public final void addInt16(int value) {
    if (value < 0 || value > MAX_INT16) {
      final String msg;
      msg = "Invalid 2-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final byte b0;
    b0 = (byte) value;

    final byte b1;
    b1 = (byte) (value >>> 8);

    add(b0, b1);
  }

  public final void addInt24(int value) {
    if (value < 0 || value > MAX_INT24) {
      final String msg;
      msg = "Invalid 3-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final byte b0;
    b0 = (byte) value;

    final byte b1;
    b1 = (byte) (value >>> 8);

    final byte b2;
    b2 = (byte) (value >>> 16);

    add(b0, b1, b2);
  }

  public final void addVarInt(int value) {
    if (value < 0) {
      throw varIntInvalid(value);
    }

    else if (value <= VARINT_MAX1) {
      final byte b1;
      b1 = (byte) value;

      add(b1);
    }

    else if (value <= VARINT_MAX2) {
      final byte b1;
      b1 = varIntHigh(value, 7);

      final byte b2;
      b2 = varInt(value, 0);

      add(b1, b2);
    }

    else if (value <= VARINT_MAX3) {
      final byte b1;
      b1 = varIntHigh(value, 14);

      final byte b2;
      b2 = varInt(value, 7);

      final byte b3;
      b3 = varInt(value, 0);

      add(b1, b2, b3);
    }

    else {
      throw varIntInvalid(value);
    }
  }

  public final void addVarIntLE(int value) {
    if (value < 0) {
      throw varIntInvalid(value);
    }

    else if (value <= VARINT_MAX1) {
      final byte b1;
      b1 = (byte) value;

      add(b1);
    }

    else if (value <= VARINT_MAX2) {
      final byte b1;
      b1 = varInt(value, 0);

      final byte b2;
      b2 = varIntHigh(value, 7);

      add(b1, b2);
    }

    else if (value <= VARINT_MAX3) {
      final byte b1;
      b1 = varInt(value, 0);

      final byte b2;
      b2 = varInt(value, 7);

      final byte b3;
      b3 = varIntHigh(value, 14);

      add(b1, b2, b3);
    }

    else {
      throw varIntInvalid(value);
    }
  }

  private IllegalArgumentException varIntInvalid(int value) {
    final String msg;
    msg = "Invalid varint value: %d".formatted(value);

    return new IllegalArgumentException(msg);
  }

  private byte varIntHigh(int value, int shift) {
    value = value >>> shift;

    return (byte) value;
  }

  private byte varInt(int value, int shift) {
    value = value >>> shift;

    value = value & VARINT_MAX1;

    value = value | 0x80;

    return (byte) value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ByteArray that
        && Arrays.equals(bytes, 0, index, that.bytes, 0, that.index);
  }

  public final byte get(int idx) {
    return bytes[idx];
  }

  public final int getInt16(int idx) {
    final byte b0;
    b0 = bytes[idx + 0];

    final byte b1;
    b1 = bytes[idx + 1];

    return toInt(b0, 0) | toInt(b1, 8);
  }

  public final int getInt24(int idx) {
    final byte b0;
    b0 = bytes[idx + 0];

    final byte b1;
    b1 = bytes[idx + 1];

    final byte b2;
    b2 = bytes[idx + 2];

    return toInt(b0, 0) | toInt(b1, 8) | toInt(b2, 16);
  }

  public final int getVarIntLE(int startIndex, int endIndex) {
    final int length;
    length = endIndex - startIndex;

    return switch (length) {
      case 1 -> get(startIndex);

      case 2 -> decodeVarInt(get(startIndex), 0) | decodeVarInt(get(startIndex + 1), 7);

      case 3 -> decodeVarInt(get(startIndex), 0) | decodeVarInt(get(startIndex + 1), 7) | decodeVarInt(get(startIndex + 2), 14);

      default -> throw new IllegalArgumentException(
          "HtmlTemplate is too large :: length=" + length
      );
    };
  }

  private int decodeVarInt(byte value, int shift) {
    final int intValue;
    intValue = value & VARINT_MAX1;

    return intValue << shift;
  }

  public final void set(int idx, byte value) {
    bytes[idx] = value;
  }

  public final int size() {
    return index;
  }

  @Override
  public final String toString() {
    final HexFormat format;
    format = HexFormat.of();

    return format.formatHex(bytes, 0, index);
  }

  public final byte[] unwrap() {
    return bytes;
  }

  public final int varIntLEEndIndex(int idx) {
    int cursor;
    cursor = idx;

    int aux;

    do {
      aux = get(cursor++);
    } while (aux < 0);

    return cursor;
  }

  private void add(byte b0, byte b1) {
    bytes = Util.growIfNecessary(bytes, index + 1);
    bytes[index++] = b0;
    bytes[index++] = b1;
  }

  private void add(byte b0, byte b1, byte b2) {
    bytes = Util.growIfNecessary(bytes, index + 2);
    bytes[index++] = b0;
    bytes[index++] = b1;
    bytes[index++] = b2;
  }

  private int toInt(byte b, int shift) {
    return (b & BYTE_MASK) << shift;
  }

}
