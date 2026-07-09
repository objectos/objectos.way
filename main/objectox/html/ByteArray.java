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

  public final void add(byte b0, byte b1) {
    bytes = Util.growIfNecessary(bytes, index + 1);
    bytes[index++] = b0;
    bytes[index++] = b1;
  }

  public final void add(byte b0, byte b1, byte b2) {
    bytes = Util.growIfNecessary(bytes, index + 2);
    bytes[index++] = b0;
    bytes[index++] = b1;
    bytes[index++] = b2;
  }

  public final void add(byte b0, byte b1, byte b2, byte b3) {
    bytes = Util.growIfNecessary(bytes, index + 3);
    bytes[index++] = b0;
    bytes[index++] = b1;
    bytes[index++] = b2;
    bytes[index++] = b3;
  }

  public final void add(byte b0, byte b1, byte b2, byte b3, byte b4) {
    bytes = Util.growIfNecessary(bytes, index + 4);
    bytes[index++] = b0;
    bytes[index++] = b1;
    bytes[index++] = b2;
    bytes[index++] = b3;
    bytes[index++] = b4;
  }

  public final void add(byte b0, byte b1, byte b2, byte b3, byte b4, byte b5) {
    bytes = Util.growIfNecessary(bytes, index + 5);
    bytes[index++] = b0;
    bytes[index++] = b1;
    bytes[index++] = b2;
    bytes[index++] = b3;
    bytes[index++] = b4;
    bytes[index++] = b5;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ByteArray that
        && Arrays.equals(bytes, 0, index, that.bytes, 0, that.index);
  }

  public final byte get(int idx) {
    return bytes[idx];
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

}
