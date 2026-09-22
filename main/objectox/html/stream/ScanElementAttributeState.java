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
import objectox.html.elem.ElementName;
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
      return compute0Value(elem);
    } else {
      final ElementName elementName;
      elementName = elem.elementName;

      final ElementKind elementKind;
      elementKind = elem.elementKind;

      return elementKind.endStartTagStateEmpty(ctx, elementName);
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanElementAttributeState that
        && ctx.equals(that.ctx);
  }

  private State compute0Value(ElementFrame elem) {
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

      case HtmlBytes.xELEMSTR8 -> { nodes(elem); yield new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8); }

      case HtmlBytes.xELEMVOID8 -> { nodes(elem); yield new BeginStartTagState(ctx, ElementKind.VOID, IntSize.BIT8); }

      case HtmlBytes.xTEXT8 -> { nodes(elem); yield new RenderTextState(ctx, IntSize.BIT8); }

      case HtmlBytes.xTEXT16 -> { nodes(elem); yield new RenderTextState(ctx, IntSize.BIT16); }

      case HtmlBytes.xTEXT24 -> { nodes(elem); yield new RenderTextState(ctx, IntSize.BIT24); }

      case HtmlBytes.xRAW8 -> { nodes(elem); yield new RenderRawState(ctx, IntSize.BIT8); }

      case HtmlBytes.xRAW16 -> { nodes(elem); yield new RenderRawState(ctx, IntSize.BIT16); }

      case HtmlBytes.xRAW24 -> { nodes(elem); yield new RenderRawState(ctx, IntSize.BIT24); }

      case HtmlBytes.xTEST8 -> { nodes(elem); yield new RenderTestableTextState(ctx, IntSize.BIT8); }

      case HtmlBytes.xTEST16 -> { nodes(elem); yield new RenderTestableTextState(ctx, IntSize.BIT16); }

      case HtmlBytes.xTEST24 -> { nodes(elem); yield new RenderTestableTextState(ctx, IntSize.BIT24); }

      case HtmlBytes.xTESTONLY8 -> { nodes(elem); yield new RenderTestableOnlyState(ctx, IntSize.BIT8); }

      case HtmlBytes.xTESTONLY16 -> { nodes(elem); yield new RenderTestableOnlyState(ctx, IntSize.BIT16); }

      case HtmlBytes.xTESTONLY24 -> { nodes(elem); yield new RenderTestableOnlyState(ctx, IntSize.BIT24); }

      default -> throw State.implCode(code);
    };
  }

  private void nodes(ElementFrame elem) {
    final ElementKind elementKind;
    elementKind = elem.elementKind;

    if (elementKind != ElementKind.REGULAR) {
      throw State.malformed();
    }

    // The first child jmp frame is already on top of the stack, so the
    // parent's level is one below the current stack index.
    final int level;
    level = ctx.stackIndex() - 1;

    final EndStartTagKind kind;
    kind = EndStartTagKind.ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(level, kind);

    ctx.result(event);
  }

}
