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

final class RenderTestableOnlyState implements State, ScanOp {

  private final Ctx ctx;

  private final ScanLevel scanLevel;

  private final TestableKind kind;

  private final IntSize size;

  RenderTestableOnlyState(Ctx ctx, ScanLevel scanLevel, TestableKind kind, IntSize size) {
    this.ctx = ctx;

    this.scanLevel = scanLevel;

    this.kind = kind;

    this.size = size;
  }

  @Override
  public final State compute() {
    final TestableNode node;
    node = TestableNode.parse(ctx, kind, size);

    return new RenderTestableNodeState(ctx, node);
  }

  @Override
  public final State documentNode() {
    return switch (scanLevel) {
      case TOP_LEVEL -> this;

      case NESTED -> switch (kind) {
        case NEWLINE -> new ScanDocumentNodeState(ctx);

        case CELL, HEADING, PROP, PROP_KEY, PROP_VALUE -> this;
      };
    };
  }

  @Override
  public final State elementAttribute() {
    return this;
  }

  @Override
  public final State elementNode() {
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderTestableOnlyState that
        && ctx.equals(that.ctx)
        && scanLevel.equals(that.scanLevel)
        && kind.equals(that.kind)
        && size.equals(that.size);
  }

}
