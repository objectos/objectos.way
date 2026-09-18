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
import objectox.html.elem.ElementName;
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

    final ElementFrame elem;
    elem = f.elementFrame;

    if (elem.hasValue()) {
      return compute0(elem);
    } else {
      final ElementName elementName;
      elementName = elem.elementName;

      final RenderEndTagState state;
      state = new RenderEndTagState(ctx, elementName);

      return state.compute();
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanElementNodeState that
        && ctx.equals(that.ctx);
  }

  private State compute0(ElementFrame elem) {
    final int jmp;
    jmp = elem.nextValue(ctx);

    final Frame frame;
    frame = new Frame(jmp);

    ctx.push(frame);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xELEMSTR8 -> new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8);

      case HtmlBytes.xELEMVOID8 -> new BeginStartTagState(ctx, ElementKind.VOID, IntSize.BIT8);

      case HtmlBytes.xTEXT8 -> new RenderTextState(ctx, IntSize.BIT8);

      case HtmlBytes.xTEXT16 -> new RenderTextState(ctx, IntSize.BIT16);

      case HtmlBytes.xTEXT24 -> new RenderTextState(ctx, IntSize.BIT24);

      case HtmlBytes.xRAW8 -> new RenderRawState(ctx, IntSize.BIT8);

      case HtmlBytes.xRAW16 -> new RenderRawState(ctx, IntSize.BIT16);

      case HtmlBytes.xRAW24 -> new RenderRawState(ctx, IntSize.BIT24);

      default -> throw State.malformed();
    };
  }

}
