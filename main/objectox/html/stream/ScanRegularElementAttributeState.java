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

final class ScanRegularElementAttributeState extends AbstractScanElementAttributeState {

  ScanRegularElementAttributeState(Ctx ctx) {
    super(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof ScanRegularElementAttributeState that
        && ctx.equals(that.ctx);
  }

  @Override
  final State computeNonAttribute(byte code) {
    return switch (code) {
      case HtmlBytes.xELEMSTR8 -> endTag(ElementKind.REGULAR, IntSize.BIT8);

      case HtmlBytes.xELEMVOID8 -> endTag(ElementKind.VOID, IntSize.BIT8);

      default -> throw State.implCode(code);
    };
  }

  @Override
  final EndStartTagKind endTagIsEmpty() {
    return EndStartTagKind.EMPTY_ELEMENT;
  }

  @Override
  final State endTagIsEmptyNextState() {
    return new RenderEndTagState(ctx);
  }

  private State endTag(ElementKind elementKind, IntSize nameSize) {
    final Frame jmp;
    jmp = ctx.pop();

    final Frame f;
    f = ctx.pop();

    final ScanRegularElementAttributeFrame previous;
    previous = f.as(ScanRegularElementAttributeFrame.class);

    final ScanRegularElementNodeFrame next;
    next = new ScanRegularElementNodeFrame(previous);

    ctx.push(next);

    ctx.push(jmp);

    final EndStartTagKind kind;
    kind = EndStartTagKind.ELEMENT;

    final EndStartTagEvent event;
    event = new EndStartTagEvent(kind);

    ctx.result(event);

    return new BeginStartTagState(ctx, elementKind, nameSize);
  }

}
