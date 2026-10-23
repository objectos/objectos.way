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

final class TestableNewLineState implements State, ScanOp {

  private final Ctx ctx;

  private final ScanLevel scanLevel;

  TestableNewLineState(Ctx ctx, ScanLevel scanLevel) {
    this.ctx = ctx;

    this.scanLevel = scanLevel;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final TestableNewLineEvent event;
    event = new TestableNewLineEvent(level);

    ctx.result(event);

    final Frame top;
    top = ctx.pop();

    return new EndNodeState(ctx, top);
  }

  @Override
  public final State documentNode() {
    return switch (scanLevel) {
      case NESTED -> new ScanDocumentNodeState(ctx);

      case TOP_LEVEL -> this;
    };
  }

  @Override
  public final State elementAttribute() {
    return switch (scanLevel) {
      case NESTED -> this;

      case TOP_LEVEL -> ScanOp.super.elementAttribute();
    };
  }

  @Override
  public final State elementNode() {
    return switch (scanLevel) {
      case NESTED -> this;

      case TOP_LEVEL -> ScanOp.super.elementNode();
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof TestableNewLineState that
        && ctx.equals(that.ctx)
        && scanLevel.equals(that.scanLevel);
  }

}
