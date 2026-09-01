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

final class ScanElementNodeState implements State {

  private final Ctx ctx;

  ScanElementNodeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final Frame f;
    f = ctx.peekFrame();

    final ScanRegularElementNodeFrame frame;
    frame = f.as(ScanRegularElementNodeFrame.class);

    if (frame.hasNext()) {
      return compute0(frame);
    } else {
      return endTag();
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanElementNodeState that
        && ctx.equals(that.ctx);
  }

  private State compute0(ScanRegularElementNodeFrame frame) {
    final int jmpIndex;
    jmpIndex = frame.nextInt(ctx);

    final JumpFrame jmpFrame;
    jmpFrame = new JumpFrame(jmpIndex);

    ctx.push(jmpFrame);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xTEXT8 -> new RenderTextState(ctx, IntSize.BIT8);

      case HtmlBytes.xTEXT16 -> new RenderTextState(ctx, IntSize.BIT16);

      case HtmlBytes.xTEXT24 -> new RenderTextState(ctx, IntSize.BIT24);

      case HtmlBytes.xATTRIBUTE88,
           HtmlBytes.xBOOLEAN8 -> skip();

      default -> throw State.implCode(code);
    };
  }

  private State skip() {
    ctx.pop(); // jmp frame

    return this;
  }

  private State endTag() {
    final Frame f;
    f = ctx.pop();

    final ScanRegularElementNodeFrame previous;
    previous = f.as(ScanRegularElementNodeFrame.class);

    final Frame current;
    current = ctx.peekFrame();

    current.updateIfNecessary(previous);

    return new RenderEndTagState(ctx);
  }

}
