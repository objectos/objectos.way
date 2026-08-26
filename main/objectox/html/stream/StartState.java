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

import objectox.html.markup.HtmlBytes;

final class StartState implements State {

  private final Ctx ctx;

  StartState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final ScanDocumentNodeState compute() {
    final ScanDocumentNodeFrame start;
    start = new ScanDocumentNodeFrame(0);

    ctx.push(start);

    while (ctx.hasByte()) {
      final byte proto;
      proto = ctx.peekByte();

      switch (proto) {
        case HtmlBytes.xATTRIBUTE88 -> { ctx.skip(1 + 1 + 1); continue; }

        case HtmlBytes.xATTRIBUTE816 -> { ctx.skip(1 + 1 + 2); continue; }

        case HtmlBytes.xATTRIBUTE824 -> { ctx.skip(1 + 1 + 3); continue; }

        case HtmlBytes.xBOOLEAN8 -> { ctx.skip(1 + 1); continue; }
      }

      break;
    }

    return new ScanDocumentNodeState(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof StartState that
        && ctx.equals(that.ctx);
  }

}
