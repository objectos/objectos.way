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

  @Override
  public final State compute() {
    if (ctx.hasByte()) {
      return compute0();
    } else {
      return new StopState(ctx);
    }
  }

  private State compute0() {
    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.DOCTYPE -> doctype();

      case HtmlBytes.STARTTAG8 -> new BeginStartTagState(ctx, IntSize.BIT8);

      default -> throw State.implCode(code);
    };
  }

  private State doctype() {
    ctx.result(RenderDoctypeEvent.of());

    return this;
  }

}
