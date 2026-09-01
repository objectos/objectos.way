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

import objectos.html.stream.EndStartTagKind;
import objectox.html.IntSize;
import objectox.html.markup.HtmlBytes;

final class ScanElementAttributeState implements State {

  private final Ctx ctx;

  ScanElementAttributeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final Frame frame;
    frame = ctx.peekFrame();

    final ElementFrame elem;
    elem = frame.elementFrame;

    if (elem.hasValue()) {
      return compute0(elem);
    } else {
      return emptyElement(elem);
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanElementAttributeState that
        && ctx.equals(that.ctx);
  }

  private State compute0(ElementFrame elem) {
    final int jmp;
    jmp = elem.nextValue(ctx);

    final Frame frame;
    frame = new Frame(jmp);

    ctx.push(frame);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xATTRIBUTE88 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.xATTRIBUTE816 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.xATTRIBUTE824 -> new RenderAttributeState(ctx, IntSize.BIT8, IntSize.BIT24);

      case HtmlBytes.xBOOLEAN8 -> new RenderBooleanAttributeState(ctx, IntSize.BIT8);

      case HtmlBytes.xJSACTION88 -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.xJSACTION816 -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.xJSACTION824 -> new RenderJsActionAttributeState(ctx, IntSize.BIT8, IntSize.BIT24);

      default -> compute1NonAttr(elem.elementKind, code);
    };
  }

  private State compute1NonAttr(ElementKind kind, byte code) {
    return switch (kind) {
      case REGULAR -> compute2RegularElem(code);

      case VOID -> throw State.malformed();
    };
  }

  private State compute2RegularElem(byte code) {
    return switch (code) {
      case HtmlBytes.xELEMSTR8 -> compute3Child(ElementKind.REGULAR, IntSize.BIT8);

      case HtmlBytes.xELEMVOID8 -> compute3Child(ElementKind.VOID, IntSize.BIT8);

      default -> throw State.implCode(code);
    };
  }

  private State compute3Child(ElementKind elementKind, IntSize nameSize) {
    final EndStartTagKind kind;
    kind = EndStartTagKind.ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return new BeginStartTagState(ctx, elementKind, nameSize);
  }

  private State emptyElement(ElementFrame elem) {
    final ElementKind kind;
    kind = elem.elementKind;

    return switch (kind) {
      case REGULAR -> emptyElement0Regular();

      case VOID -> endElement1Void();
    };
  }

  private State emptyElement0Regular() {
    final EndStartTagKind kind;
    kind = EndStartTagKind.EMPTY_ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return new RenderEndTagState(ctx);
  }

  private State endElement1Void() {
    final EndStartTagKind kind;
    kind = EndStartTagKind.VOID_ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return new EndElementState(ctx);
  }

}
