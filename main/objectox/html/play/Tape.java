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
import objectox.html.rec.ByteArray;
import objectox.html.rec.ObjectArray;

final class Tape {

  private final byte[] main;

  private int mainIndex;

  private final Object[] objects;

  Tape(ByteArray main, ObjectArray objects) {
    this.main = main.unwrap();

    this.objects = objects.unwrap();
  }

  Tape(byte[] main, int mainIndex, Object[] objects) {
    this.main = main;

    this.mainIndex = mainIndex;

    this.objects = objects;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof Tape that
        && Arrays.equals(main, that.main)
        && mainIndex == that.mainIndex
        && Arrays.equals(objects, that.objects);
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

  public final byte peekByte() {
    return main[mainIndex];
  }

  public final Tape push(byte value) {
    final int idx;
    idx = mainIndex++;

    main[idx] = value;

    return new Tape(main, idx, objects);
  }

  public final void skip(int value) {
    mainIndex += value;
  }

  public final void skipInt16() {
    skip(2);
  }

  private int toInt(byte b, int shift) {
    return Byte.toUnsignedInt(b) << shift;
  }

}
