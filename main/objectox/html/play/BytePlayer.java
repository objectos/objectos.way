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
package objectox.html.play;

import java.util.Arrays;
import objectox.html.ByteArray;

final class BytePlayer {

  private final byte[] bytes;

  private int cursor;

  private final int size;

  BytePlayer(ByteArray array) {
    bytes = array.unwrap();

    size = array.size();
  }

  BytePlayer(byte[] bytes, int cursor, int size) {
    this.bytes = bytes;

    this.cursor = cursor;

    this.size = size;
  }

  public static BytePlayer of(int... values) {
    final int length;
    length = values.length;

    final byte[] copy;
    copy = new byte[length];

    for (int idx = 0; idx < length; idx++) {
      copy[idx] = (byte) values[idx];
    }

    return new BytePlayer(copy, 0, length);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof BytePlayer that
        && cursor == that.cursor
        && Arrays.equals(bytes, 0, size, that.bytes, 0, that.size);
  }

  public final boolean hasNext() {
    return cursor < size;
  }

  public final byte next() {
    return bytes[cursor++];
  }

  public final int nextInt8() {
    final byte b;
    b = next();

    return Byte.toUnsignedInt(b);
  }

  public final byte peek() {
    return bytes[cursor];
  }

  public final void skip(int value) {
    cursor += value;
  }

  public final void skipInt16() {
    skip(2);
  }

}
