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

final class ScanDocumentNodeState implements State {

  private final Ctx ctx;

  ScanDocumentNodeState(Ctx ctx) {
    this.ctx = ctx;
  }

  public static ScanDocumentNodeState of(Ctx ctx) {
    final Frame frame;
    frame = new Frame(0);

    ctx.push(frame);

    return new ScanDocumentNodeState(ctx);
  }

  @Override
  public final State compute() {
    while (ctx.hasByte()) {
      final byte code;
      code = ctx.nextByte();

      switch (code) {
        case HtmlBytes.DOCTYPE -> { return doctype(); }

        case HtmlBytes.ELEMSTR8 -> { return new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8); }

        case HtmlBytes.ELEMVOID8 -> { return new BeginStartTagState(ctx, ElementKind.VOID, IntSize.BIT8); }

        case HtmlBytes.xATTRIBUTE88 -> ctx.skip(1 + 1);

        case HtmlBytes.xATTRIBUTE816 -> ctx.skip(1 + 2);

        case HtmlBytes.xATTRIBUTE824 -> ctx.skip(1 + 3);

        case HtmlBytes.xBOOLEAN8 -> ctx.skip(1);

        case HtmlBytes.xJSACTION88 -> ctx.skip(1 + 1);

        case HtmlBytes.xJSACTION816 -> ctx.skip(1 + 2);

        case HtmlBytes.xJSACTION824 -> ctx.skip(1 + 3);

        case HtmlBytes.xELEMSTR8 -> {
          final SkipRegularElement skip;
          skip = new SkipRegularElement(ctx, IntSize.BIT8);

          skip.execute();
        }

        case HtmlBytes.xELEMVOID8 -> {
          final SkipVoidElement skip;
          skip = new SkipVoidElement(ctx, IntSize.BIT8);

          skip.execute();
        }

        case HtmlBytes.xTEXT8 -> ctx.skip(1);

        case HtmlBytes.xTEXT16 -> ctx.skip(2);

        case HtmlBytes.xTEXT24 -> ctx.skip(3);

        case HtmlBytes.xRAW8 -> ctx.skip(1);

        case HtmlBytes.xRAW16 -> ctx.skip(2);

        case HtmlBytes.xRAW24 -> ctx.skip(3);

        default -> throw State.implCode(code);
      }
    }

    return new StopState(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanDocumentNodeState that
        && ctx.equals(that.ctx);
  }

  private State doctype() {
    ctx.result(RenderDoctypeEvent.of());

    return this;
  }

}
