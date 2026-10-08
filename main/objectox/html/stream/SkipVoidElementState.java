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

final class SkipVoidElementState implements State, ScanOp {

  private final Ctx ctx;

  private final IntSize nameSize;

  SkipVoidElementState(Ctx ctx, IntSize nameSize) {
    this.ctx = ctx;

    this.nameSize = nameSize;
  }

  @Override
  public final State compute() {
    final int nameBytes;
    nameBytes = nameSize.bytes();

    ctx.skip(nameBytes);

    final IntSize size;
    size = valuesSize();

    final int count;
    count = ctx.nextInt(size);

    final int valueBytes;
    valueBytes = count * size.bytes();

    ctx.skip(valueBytes);

    return new ScanDocumentNodeState(ctx);
  }

  @Override
  public final State documentNode() {
    return this;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof SkipVoidElementState that
        && nameSize.equals(that.nameSize)
        && ctx.equals(that.ctx);
  }

  private IntSize valuesSize() {
    final byte values;
    values = ctx.nextByte();

    return switch (values) {
      case HtmlBytes.VALUES8 -> IntSize.BIT8;

      case HtmlBytes.VALUES16 -> IntSize.BIT16;

      case HtmlBytes.VALUES24 -> IntSize.BIT24;

      default -> throw new IllegalStateException("Unexpected VALUES marker: " + values);
    };
  }

}
