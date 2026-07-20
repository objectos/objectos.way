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

import objectos.internal.Util;
import objectox.html.rec.ByteArray;
import objectox.html.rec.ObjectArray;

final class Tape {

  private final byte[] main;

  private int mainIndex;

  private final Object[] objects;

  private Frame[] frames = new Frame[10];

  private int framesIndex;

  Tape(ByteArray main, ObjectArray objects) {
    this.main = main.unwrap();

    this.objects = objects.unwrap();
  }

  Tape(byte[] main, int mainIndex, Object[] objects, Frame[] frames, int framesIndex) {
    this.main = main;

    this.mainIndex = mainIndex;

    this.objects = objects;

    this.frames = frames;

    this.framesIndex = framesIndex;
  }

  public final boolean hasByte() {
    return mainIndex < main.length;
  }

  public final byte nextByte() {
    return main[mainIndex++];
  }

  public final int nextInt8() {
    final byte b;
    b = nextByte();

    return Byte.toUnsignedInt(b);
  }

  public final int nextInt16() {
    final byte b0;
    b0 = nextByte();

    final byte b1;
    b1 = nextByte();

    return toInt(b0, 0) | toInt(b1, 8);
  }

  public final int nextVarIntLE() {
    final int startIndex;
    startIndex = mainIndex;

    byte aux;

    do {
      aux = nextByte();
    } while (aux < 0);

    final int length;
    length = mainIndex - startIndex;

    assert 0 < length && length <= 3;

    int result;
    result = 0;

    for (int iter = 0; iter < length; iter++) {
      final int idx;
      idx = startIndex + iter;

      final byte value;
      value = main[idx];

      final int intValue;
      intValue = value & 0x7F;

      final int shift;
      shift = 7 * iter;

      result |= intValue << shift;
    }

    return result;
  }

  public final byte peekByte() {
    return main[mainIndex];
  }

  public final FrameKind pop() {
    framesIndex--;

    final Frame f;
    f = frames[framesIndex];

    mainIndex = f.index();

    return f.kind();
  }

  public final void push(FrameKind kind) {
    push(kind, 0);
  }

  public final void push(FrameKind kind, int offset) {
    final int value;
    value = mainIndex + offset;

    final int idx;
    idx = framesIndex++;

    frames = Util.growIfNecessary(frames, idx);

    frames[idx] = new Frame(kind, value);
  }

  public final void skip(int value) {
    mainIndex += value;
  }

  public final void skipByte() {
    skip(1);
  }

  public final void skipInt16() {
    skip(2);
  }

  public final void skipVarIntLE() {
    byte aux;

    do {
      aux = nextByte();
    } while (aux < 0);
  }

  public final String string(int idx) {
    return (String) objects[idx];
  }

  private int toInt(byte b, int shift) {
    return Byte.toUnsignedInt(b) << shift;
  }

}
