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
import objectox.html.attr.AttributeName;

final class RenderAttributeState implements State, ScanOp {

  private final Ctx ctx;

  private final IntSize nameSize;

  private final IntSize valueSize;

  RenderAttributeState(Ctx ctx, IntSize nameSize, IntSize valueSize) {
    this.ctx = ctx;

    this.nameSize = nameSize;

    this.valueSize = valueSize;
  }

  @Override
  public final State compute() {
    final int nameIndex;
    nameIndex = ctx.nextInt(nameSize);

    final AttributeName attributeName;
    attributeName = AttributeName.get(nameIndex);

    final int valueIndex;
    valueIndex = ctx.nextInt(valueSize);

    final String value;
    value = ctx.objString(valueIndex);

    ctx.pop(); // jmp frame

    return new MergeAttributeState(ctx, nameSize, attributeName, value);
  }

  @Override
  public final State documentNode() {
    final int bytes;
    bytes = valueSize.bytes();

    final int count;
    count = 1 + bytes;

    return new SkipState(ctx, count);
  }

  @Override
  public final State elementAttribute() {
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof RenderAttributeState that
        && ctx.equals(that.ctx)
        && nameSize.equals(that.nameSize)
        && valueSize.equals(that.valueSize);
  }

  @Override
  public final boolean isAttribute() {
    return true;
  }

}
