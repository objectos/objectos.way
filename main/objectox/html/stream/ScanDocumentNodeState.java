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

final class ScanDocumentNodeState implements State, ScanOp {

  private final Ctx ctx;

  ScanDocumentNodeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    if (ctx.hasByte()) {
      return compute0();
    } else {
      return new EndDocumentState(ctx, !ctx.emitted());
    }
  }

  @Override
  public final State documentNode() {
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanDocumentNodeState that
        && ctx.equals(that.ctx);
  }

  private State compute0() {
    final ScanOp op;
    op = ctx.nextScanOp();

    return op.documentNode();
  }

}
