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
import objectox.html.elem.ElementName;
import objectox.html.markup.HtmlBytes;

final class ScanElementNodeState implements State {

  private final Ctx ctx;

  ScanElementNodeState(Ctx ctx) {
    this.ctx = ctx;
  }

  @Override
  public final State compute() {
    final Frame f;
    f = ctx.peekFrame();

    final ElementFrame elem;
    elem = f.elementFrame;

    if (elem.hasValue()) {
      return compute0(elem);
    } else {
      final ElementName elementName;
      elementName = elem.elementName;

      final RenderEndTagState state;
      state = new RenderEndTagState(ctx, elementName);

      return state.compute();
    }
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanElementNodeState that
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
      case HtmlBytes.THEADING8,
           HtmlBytes.oTHEADING8 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT8);

      case HtmlBytes.THEADING16,
           HtmlBytes.oTHEADING16 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT16);

      case HtmlBytes.THEADING24,
           HtmlBytes.oTHEADING24 -> new RenderTestableOnlyState(ctx, TestableKind.HEADING, IntSize.BIT24);

      case HtmlBytes.TCELL8,
           HtmlBytes.oTCELL8 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT8);

      case HtmlBytes.TCELL16,
           HtmlBytes.oTCELL16 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT16);

      case HtmlBytes.TCELL24,
           HtmlBytes.oTCELL24 -> new RenderTestableOnlyState(ctx, TestableKind.CELL, IntSize.BIT24);

      case HtmlBytes.TPROP8,
           HtmlBytes.oTPROP8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT8);

      case HtmlBytes.TPROP16,
           HtmlBytes.oTPROP16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT16);

      case HtmlBytes.TPROP24,
           HtmlBytes.oTPROP24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.TPROPKEY8,
           HtmlBytes.oTPROPKEY8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT8);

      case HtmlBytes.TPROPKEY16,
           HtmlBytes.oTPROPKEY16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT16);

      case HtmlBytes.TPROPKEY24,
           HtmlBytes.oTPROPKEY24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.TPROPVAL8,
           HtmlBytes.oTPROPVAL8 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT8);

      case HtmlBytes.TPROPVAL16,
           HtmlBytes.oTPROPVAL16 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT16);

      case HtmlBytes.TPROPVAL24,
           HtmlBytes.oTPROPVAL24 -> new RenderTestableOnlyState(ctx, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.TNEWLINE -> new RenderTestableOnlyState(ctx, TestableKind.NEWLINE, IntSize.BIT8);

      case HtmlBytes.xTHEADING8 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT8);

      case HtmlBytes.xTHEADING16 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT16);

      case HtmlBytes.xTHEADING24 -> new RenderTestableTextState(ctx, TestableKind.HEADING, IntSize.BIT24);

      case HtmlBytes.xTCELL8 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT8);

      case HtmlBytes.xTCELL16 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT16);

      case HtmlBytes.xTCELL24 -> new RenderTestableTextState(ctx, TestableKind.CELL, IntSize.BIT24);

      case HtmlBytes.xTPROP8 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT8);

      case HtmlBytes.xTPROP16 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT16);

      case HtmlBytes.xTPROP24 -> new RenderTestableTextState(ctx, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.xTPROPKEY8 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT8);

      case HtmlBytes.xTPROPKEY16 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT16);

      case HtmlBytes.xTPROPKEY24 -> new RenderTestableTextState(ctx, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.xTPROPVAL8 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT8);

      case HtmlBytes.xTPROPVAL16 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT16);

      case HtmlBytes.xTPROPVAL24 -> new RenderTestableTextState(ctx, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.xTNEWLINE -> new RenderTestableOnlyState(ctx, TestableKind.NEWLINE, IntSize.BIT8);

      case HtmlBytes.xELEMSTR8 -> new BeginStartTagState(ctx, ElementKind.REGULAR, IntSize.BIT8);

      case HtmlBytes.xELEMVOID8 -> new BeginStartTagState(ctx, ElementKind.VOID, IntSize.BIT8);

      case HtmlBytes.xTEXT8 -> new RenderTextState(ctx, IntSize.BIT8);

      case HtmlBytes.xTEXT16 -> new RenderTextState(ctx, IntSize.BIT16);

      case HtmlBytes.xTEXT24 -> new RenderTextState(ctx, IntSize.BIT24);

      case HtmlBytes.xRAW8 -> new RenderRawState(ctx, IntSize.BIT8);

      case HtmlBytes.xRAW16 -> new RenderRawState(ctx, IntSize.BIT16);

      case HtmlBytes.xRAW24 -> new RenderRawState(ctx, IntSize.BIT24);

      default -> throw State.malformed();
    };
  }

}
