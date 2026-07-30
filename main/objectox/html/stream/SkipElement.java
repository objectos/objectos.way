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
import objectox.html.rec.HtmlBytes;

final class SkipElement {

  private final Ctx ctx;

  SkipElement(Ctx ctx) {
    this.ctx = ctx;
  }

  public final void execute(IntSize nameSize) {
    final int startTag;
    startTag = 1 + nameSize.bytes();

    ctx.skip(startTag);

    final byte valuesByte;
    valuesByte = ctx.nextByte();

    final IntSize valuesSize;
    valuesSize = switch (valuesByte) {
      case HtmlBytes.VALUES8 -> IntSize.BIT8;

      case HtmlBytes.VALUES16 -> IntSize.BIT16;

      case HtmlBytes.VALUES24 -> IntSize.BIT24;

      default -> throw State.malformed();
    };

    final int length;
    length = ctx.nextInt(valuesSize);

    final int values;
    values = length * valuesSize.bytes();

    ctx.skip(values);

    final byte endTag;
    endTag = ctx.nextByte();

    switch (endTag) {
      case HtmlBytes.VOIDELEM -> {}

      case HtmlBytes.ENDTAG8 -> ctx.skip(1);

      default -> throw State.malformed();
    }
  }

}