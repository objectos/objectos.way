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

final class SkipRegularElement {

  private final Ctx ctx;

  SkipRegularElement(Ctx ctx) {
    this.ctx = ctx;
  }

  public final void execute() {
    ctx.skip(1 + 1);

    final IntSize size;
    size = valuesSize();

    final int count;
    count = ctx.nextInt(size);

    ctx.skip(count * size.bytes());

    ctx.skip(1 + 1);
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
