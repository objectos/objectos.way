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

import objectox.html.IntSize;

final class ScanRegularElementAttributeFrame extends Frame {

  private int current;

  private final int length;

  private final IntSize size;

  ScanRegularElementAttributeFrame(int index, IntSize size, int length) {
    super(index);

    this.size = size;

    this.length = length;
  }

  ScanRegularElementAttributeFrame(int index, IntSize size, int length, int current) {
    super(index);

    this.current = current;

    this.size = size;

    this.length = length;
  }

  @Override
  final boolean equalsImpl(Frame f) {
    return f instanceof ScanRegularElementAttributeFrame that
        && current == that.current
        && length == that.length
        && size == that.size;
  }

  @Override
  final String toStringImpl() {
    return ", " + size + ", " + current + ", " + length;
  }

  public final boolean hasNext() {
    return current < length;
  }

  @Override
  public final State newState(Ctx ctx) {
    return new ScanRegularElementAttributeState(ctx);
  }

  public final int nextInt(Ctx ctx) {
    current += 1;

    return ctx.nextInt(size);
  }

}
