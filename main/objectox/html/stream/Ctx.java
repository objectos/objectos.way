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
package objectox.html.stream;

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;
import objectos.html.stream.Event;
import objectos.internal.Util;
import objectox.html.IntSize;

class Ctx {

  static final Frame[] EMPTY_STACK = new Frame[0];

  final byte[] code;

  private boolean computed;

  final Object[] objects;

  private Event result;

  private Frame[] stack;

  private int stackIndex = -1;

  Ctx(byte[] code, Object[] objects, Frame[] stack, int stackIndex) {
    this.code = code;

    this.objects = objects;

    this.stack = stack;

    this.stackIndex = stackIndex;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Ctx that
        && Arrays.equals(code, that.code)
        && Arrays.equals(objects, that.objects)
        && Objects.equals(result, that.result)
        && Arrays.equals(stack, 0, stackIndex + 1, that.stack, 0, that.stackIndex + 1);
  }

  public final boolean hasByte() {
    final Frame frame;
    frame = stack[stackIndex];

    return frame.index < code.length;
  }

  public final boolean hasEvent() {
    return result != null;
  }

  public final byte nextByte() {
    final Frame frame;
    frame = stack[stackIndex];

    final int idx;
    idx = frame.index;

    final byte result;
    result = code[idx];

    frame.index = idx + 1;

    return result;
  }

  public final Event nextEvent() {
    final Event res;
    res = result;

    computed = false;

    result = null;

    return res;
  }

  public final int nextInt(IntSize size) {
    final Frame frame;
    frame = stack[stackIndex];

    final int startIndex;
    startIndex = frame.index;

    frame.index = size.skip(startIndex);

    return switch (size) {
      case BIT8 -> toInt(startIndex, 0);

      case BIT16 -> toInt(startIndex, 8) | toInt(startIndex + 1, 0);

      case BIT24 -> toInt(startIndex, 16) | toInt(startIndex + 1, 8) | toInt(startIndex + 2, 0);
    };
  }

  public final String objString(int index) {
    final Object o;
    o = objects[index];

    return (String) o;
  }

  public final byte peekByte() {
    final Frame frame;
    frame = stack[stackIndex];

    final int idx;
    idx = frame.index;

    return code[idx];
  }

  public final Frame peekFrame() {
    return stack[stackIndex];
  }

  public final Frame pop() {
    return stack[stackIndex--];
  }

  public final void push(Frame frame) {
    final int index;
    index = ++stackIndex;

    stack = Util.growIfNecessary(stack, index);

    stack[index] = frame;
  }

  public final void result(Event value) {
    if (computed) {
      final String msg;
      msg = "Result has not been consumed yet";

      throw new IllegalStateException(msg);
    }

    computed = true;

    result = value;
  }

  public final boolean shouldCompute() {
    return !computed;
  }

  public final void skip(int value) {
    final Frame frame;
    frame = stack[stackIndex];

    frame.index += value;
  }

  @Override
  public final String toString() {
    return """
    code    = %s
    objects = %s
    stack   = %s
    """.formatted(
        HexFormat.of().formatHex(code),
        Arrays.toString(objects),
        Arrays.toString(Arrays.copyOf(stack, stackIndex + 1))
    );
  }

  private int toInt(int index, int shift) {
    final byte byteValue;
    byteValue = code[index];

    final int intValue;
    intValue = Byte.toUnsignedInt(byteValue);

    return intValue << shift;
  }

}
