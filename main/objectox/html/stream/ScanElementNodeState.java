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

import objectox.html.elem.ElementName;

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

      final EmitEndTagState state;
      state = new EmitEndTagState(ctx, elementName);

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

    final ScanOp op;
    op = ctx.nextScanOp();

    return op.elementNode();
  }

}
