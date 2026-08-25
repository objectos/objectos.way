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

sealed abstract class AbstractScanElementAttributeState
    implements
    State
    permits
    ScanRegularElementAttributeState,
    ScanVoidElementAttributeState {

  final Ctx ctx;

  AbstractScanElementAttributeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final Frame f;
    f = ctx.peekFrame();

    final ScanElementAttributeFrame frame;
    frame = f.as(ScanElementAttributeFrame.class);

    if (frame.hasNext()) {
      return compute0(frame);
    } else {
      return endTag();
    }
  }

  @Override
  public abstract boolean equals(Object obj);

  private State compute0(ScanElementAttributeFrame frame) {
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

      default -> computeNonAttribute(code);
    };
  }

  abstract State computeNonAttribute(byte code);

  private State endTag() {
    final Frame f;
    f = ctx.pop();

    final ScanElementAttributeFrame previous;
    previous = f.as(ScanElementAttributeFrame.class);

    final Frame current;
    current = ctx.peekFrame();

    current.updateIfNecessary(previous);

    final EndStartTagKind kind;
    kind = endTagIsEmpty();

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return endTagIsEmptyNextState();
  }

  abstract EndStartTagKind endTagIsEmpty();

  abstract State endTagIsEmptyNextState();

}
