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

abstract class AbstractState implements State {

  final byte[] main;

  int mainIndex;

  final Object[] objects;

  AbstractState(byte[] main, int mainIndex, Object[] objects) {
    this.main = main;

    this.mainIndex = mainIndex;

    this.objects = objects;
  }

  final boolean hasMain() {
    return mainIndex < main.length;
  }

  final UnsupportedOperationException implMe(byte proto) {
    final String msg;
    msg = "Implement me :: proto=%d".formatted(proto);

    throw new UnsupportedOperationException(msg);
  }

  final byte nextByte() {
    return main[mainIndex++];
  }

  final int nextInt8() {
    final byte b;
    b = nextByte();

    return Byte.toUnsignedInt(b);
  }

  final int nextInt16() {
    final byte b0;
    b0 = nextByte();

    final byte b1;
    b1 = nextByte();

    return toInt(b0, 0) | toInt(b1, 8);
  }

  final byte peekByte() {
    return peekByte(mainIndex);
  }

  final byte peekByte(int index) {
    return main[index];
  }

  final int set(byte value) {
    final int idx;
    idx = mainIndex++;

    main[idx] = value;

    return idx;
  }

  final void skip(int value) {
    mainIndex += value;
  }

  final void skipInt16() {
    skip(2);
  }

  private int toInt(byte b, int shift) {
    return Byte.toUnsignedInt(b) << shift;
  }

}
