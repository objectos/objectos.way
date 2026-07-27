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
package objectox.html.play;

import objectos.html.ElementName;
import objectox.html.IntSize;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;

final class RenderEndTagState implements State {

  private final Ctx ctx;

  RenderEndTagState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.ENDTAG8 -> renderEndTag(IntSize.BIT8);

      case HtmlBytes.VOIDELEM -> renderVoidElem();

      default -> throw State.malformed();
    };
  }

  private State renderEndTag(IntSize size) {
    final int nameIndex;
    nameIndex = ctx.nextInt(size);

    final ElementName elementName;
    elementName = ElementNamePojo.get(nameIndex);

    final RenderEndTagEvent event;
    event = new RenderEndTagEvent(elementName);

    ctx.result(event);

    return new EndElementState(ctx);
  }

  private State renderVoidElem() {
    throw new UnsupportedOperationException("Implement me");
  }

}