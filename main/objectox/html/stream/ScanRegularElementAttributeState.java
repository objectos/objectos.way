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

import objectos.html.stream.EndStartTagKind;
import objectox.html.IntSize;
import objectox.html.markup.HtmlBytes;

final class ScanRegularElementAttributeState implements State {

  private final Ctx ctx;

  ScanRegularElementAttributeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final Frame f;
    f = ctx.peekFrame();

    final ScanRegularElementAttributeFrame frame;
    frame = f.as(ScanRegularElementAttributeFrame.class);

    if (frame.hasNext()) {
      return compute0(frame);
    } else {
      return endEmpty();
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanRegularElementAttributeState that
        && ctx.equals(that.ctx);
  }

  private State compute0(ScanRegularElementAttributeFrame frame) {
    final int jmpIndex;
    jmpIndex = frame.nextInt(ctx);

    final JumpFrame jmpFrame;
    jmpFrame = new JumpFrame(jmpIndex);

    ctx.push(jmpFrame);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xATTRIBUTE88 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.xATTRIBUTE816 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.xATTRIBUTE824 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24);

      default -> throw State.implCode(code);
    };
  }

  private State endEmpty() {
    final Frame f; // ScanRegularElementAttributeFrame
    f = ctx.pop();

    final ScanRegularElementAttributeFrame previous;
    previous = f.as(ScanRegularElementAttributeFrame.class);

    final Frame current;
    current = ctx.peekFrame();

    current.updateIfNecessary(previous);

    final EndStartTagKind kind;
    kind = EndStartTagKind.EMPTY_ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return new RenderEndTagState(ctx);
  }

  private State skip() {
    ctx.pop(); // jmp frame

    return this;
  }

}
