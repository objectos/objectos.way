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

final class RenderTextState implements State, ScanOp {

  private final Ctx ctx;

  private final ScanOpKind opKind;

  private final IntSize textSize;

  RenderTextState(Ctx ctx, ScanOpKind opKind, IntSize textSize) {
    this.ctx = ctx;

    this.opKind = opKind;

    this.textSize = textSize;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final int textIndex;
    textIndex = ctx.nextInt(textSize);

    final String text;
    text = ctx.objString(textIndex);

    final RenderTextEvent event;
    event = RenderTextEvent.of(level, text);

    ctx.result(event);

    final Frame top;
    top = ctx.pop();

    return new EndNodeState(ctx, top);
  }

  @Override
  public final State documentNode() {
    return switch (opKind) {
      case REGULAR -> this;

      case X -> {
        final int count;
        count = textSize.bytes();

        yield new SkipState(ctx, count);
      }
    };
  }

  @Override
  public final State elementAttribute() {
    return switch (opKind) {
      case REGULAR -> ScanOp.super.elementAttribute();

      case X -> this;
    };
  }

  @Override
  public final State elementNode() {
    return switch (opKind) {
      case REGULAR -> ScanOp.super.elementNode();

      case X -> this;
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderTextState that
        && ctx.equals(that.ctx)
        && opKind.equals(that.opKind)
        && textSize.equals(that.textSize);
  }

}
