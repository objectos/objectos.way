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
package objectox.html.rec;

import java.util.Arrays;
import java.util.HexFormat;
import objectos.internal.Util;

final class HtmlSink {

  public static final int MAX_INT8 = (1 << 8) - 1;

  public static final int MAX_INT16 = (1 << 16) - 1;

  public static final int MAX_INT24 = (1 << 24) - 1;

  private byte[] code;

  private int codeIndex;

  private Object[] objects;

  private int objectsIndex;

  HtmlSink() {
    code = Util.EMPTY_BYTE_ARRAY;

    objects = Util.EMPTY_OBJECT_ARRAY;
  }

  HtmlSink(byte[] code, Object[] objects) {
    this.code = code;

    codeIndex = code.length;

    this.objects = objects;

    objectsIndex = objects.length;
  }

  public final int addByte(byte value) {
    final int idx;
    idx = codeIndex++;

    code = Util.growIfNecessary(code, idx);

    code[idx] = value;

    return idx;
  }

  public final void addInt8(int value) {
    if (value < 0 || value > MAX_INT8) {
      final String msg;
      msg = "Invalid 1-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final byte b;
    b = (byte) value;

    addByte(b);
  }

  public final void addInt16(int value) {
    if (value < 0 || value > MAX_INT16) {
      final String msg;
      msg = "Invalid 2-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final int idx0;
    idx0 = codeIndex++;

    final int idx1;
    idx1 = codeIndex++;

    code = Util.growIfNecessary(code, idx1);

    code[idx0] = (byte) (value >>> 8);

    code[idx1] = (byte) value;
  }

  public final void addInt24(int value) {
    if (value < 0 || value > MAX_INT24) {
      final String msg;
      msg = "Invalid 3-byte int value: %d".formatted(value);

      throw new IllegalArgumentException(msg);
    }

    final int idx0;
    idx0 = codeIndex++;

    final int idx1;
    idx1 = codeIndex++;

    final int idx2;
    idx2 = codeIndex++;

    code = Util.growIfNecessary(code, idx2);

    code[idx0] = (byte) (value >>> 16);

    code[idx1] = (byte) (value >>> 8);

    code[idx2] = (byte) value;
  }

  public final int addObject(Object value) {
    final int idx;
    idx = objectsIndex++;

    objects = Util.growIfNecessary(objects, idx);

    objects[idx] = value;

    return idx;
  }

  public final void consume(int value) {
    final byte original;
    original = code[value];

    code[value] = HtmlBytes.consume(original);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HtmlSink that
        && Arrays.equals(code, 0, codeIndex, that.code, 0, that.codeIndex)
        && Arrays.equals(objects, 0, objectsIndex, that.objects, 0, that.objectsIndex);
  }

  @Override
  public final String toString() {
    final byte[] $code;
    $code = Arrays.copyOf(code, codeIndex);

    final Object[] $objects;
    $objects = Arrays.copyOf(objects, objectsIndex);

    return """
    code    = %s
    objects = %s
    """.formatted(HexFormat.of().formatHex($code), Arrays.toString($objects));
  }

}
