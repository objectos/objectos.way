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
import objectox.html.markup.HtmlBytes;

sealed abstract class AbstractBeginElementContentsState
    implements
    State
    permits
    BeginRegularElementContentsState,
    BeginVoidElementContentsState {

  final Ctx ctx;

  AbstractBeginElementContentsState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.VALUES8 -> compute(IntSize.BIT8);

      case HtmlBytes.VALUES16 -> compute(IntSize.BIT16);

      case HtmlBytes.VALUES24 -> compute(IntSize.BIT24);

      default -> throw State.malformed();
    };
  }

  @Override
  public abstract boolean equals(Object obj);

  private State compute(IntSize size) {
    final int length;
    length = ctx.nextInt(size);

    final Frame currentFrame;
    currentFrame = ctx.peekFrame();

    final int startIndex;
    startIndex = currentFrame.index;

    final AbstractElementFrame nextFrame;
    nextFrame = newFrame(startIndex, size, length);

    ctx.push(nextFrame);

    return nextState();
  }

  abstract AbstractElementFrame newFrame(int startIndex, IntSize size, int length);

  abstract State nextState();

}
