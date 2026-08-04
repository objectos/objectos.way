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

final class StartState implements State {

  private final Ctx ctx;

  private SkipElement skipElement;

  StartState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final ScanDocumentNodeState compute() {
    ctx.push(ScanDocumentNodeFrame.of(0));

    while (ctx.hasByte()) {
      final byte proto;
      proto = ctx.peekByte();

      switch (proto) {
        case HtmlBytes.xSTARTTAG8 -> { skipElement().execute(IntSize.BIT8); continue; }

        case HtmlBytes.xSTARTFLAT -> throw new UnsupportedOperationException("Implement me");

        case HtmlBytes.xSTARTFRAG -> throw new UnsupportedOperationException("Implement me");

        case HtmlBytes.xAMBIGUOUS88, HtmlBytes.xATTRIBUTE88 -> { ctx.skip(1 + 1 + 1); continue; }

        case HtmlBytes.xAMBIGUOUS816, HtmlBytes.xATTRIBUTE816 -> { ctx.skip(1 + 1 + 2); continue; }

        case HtmlBytes.xAMBIGUOUS824, HtmlBytes.xATTRIBUTE824 -> { ctx.skip(1 + 1 + 3); continue; }

        case HtmlBytes.xBOOLEAN8 -> { ctx.skip(1 + 1); continue; }

        case HtmlBytes.xRAW8, HtmlBytes.xTEXT8 -> { ctx.skip(1 + 1); continue; }

        case HtmlBytes.xRAW16, HtmlBytes.xTEXT16 -> { ctx.skip(1 + 2); continue; }

        case HtmlBytes.xRAW24, HtmlBytes.xTEXT24 -> { ctx.skip(1 + 3); continue; }
      }

      break;
    }

    ctx.result(BeginDocumentEvent.INSTANCE);

    return new ScanDocumentNodeState(ctx);
  }

  private SkipElement skipElement() {
    if (skipElement == null) {
      skipElement = new SkipElement(ctx);
    }

    return skipElement;
  }

}
