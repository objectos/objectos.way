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

final class EmitRawState implements State, ScanOp {

  private final Ctx ctx;

  private final ScanLevel scanLevel;

  private final IntSize rawSize;

  EmitRawState(Ctx ctx, ScanLevel scanLevel, IntSize rawSize) {
    this.ctx = ctx;

    this.scanLevel = scanLevel;

    this.rawSize = rawSize;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final int rawIndex;
    rawIndex = ctx.nextInt(rawSize);

    final String raw;
    raw = ctx.objString(rawIndex);

    final EmitRawEvent event;
    event = new EmitRawEvent(level, raw);

    ctx.result(event);

    final Frame top;
    top = ctx.pop();

    return new EndNodeState(ctx, top);
  }

  @Override
  public final State documentNode() {
    return switch (scanLevel) {
      case TOP_LEVEL -> this;

      case NESTED -> {
        final int count;
        count = rawSize.bytes();

        yield new SkipState(ctx, count);
      }
    };
  }

  @Override
  public final State elementAttribute() {
    return switch (scanLevel) {
      case TOP_LEVEL -> ScanOp.super.elementAttribute();

      case NESTED -> this;
    };
  }

  @Override
  public final State elementNode() {
    return switch (scanLevel) {
      case TOP_LEVEL -> ScanOp.super.elementNode();

      case NESTED -> this;
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof EmitRawState that
        && ctx.equals(that.ctx)
        && scanLevel.equals(that.scanLevel)
        && rawSize.equals(that.rawSize);
  }

}
