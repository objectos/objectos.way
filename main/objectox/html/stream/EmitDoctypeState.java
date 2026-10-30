/*
 * Copyright (C) 2022-2026 Objectos Software LTDA.
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

final class EmitDoctypeState implements State, ScanOp {

  private final Ctx ctx;

  EmitDoctypeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final EmitDoctypeEvent event;
    event = new EmitDoctypeEvent(level);

    ctx.result(event);

    return new ScanDocumentNodeState(ctx);
  }

  @Override
  public final State documentNode() {
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof EmitDoctypeState that
        && ctx.equals(that.ctx);
  }

}
