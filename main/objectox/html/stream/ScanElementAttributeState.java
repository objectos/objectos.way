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
import objectox.html.elem.ElementName;

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
      return compute1Empty(elem);
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

    final ScanOp op;
    op = ctx.nextScanOp();

    final boolean attribute;
    attribute = op.isAttribute();

    if (!attribute) {
      nodes(elem);
    }

    return op.elementAttribute();
  }

  private State compute1Empty(ElementFrame elem) {
    final ElementName elementName;
    elementName = elem.elementName;

    final int level;
    level = ctx.stackIndex();

    final ElementKind elementKind;
    elementKind = elem.elementKind;

    return switch (elementKind) {
      case REGULAR -> {
        final EndStartTagKind kind;
        kind = EndStartTagKind.EMPTY_ELEMENT;

        final EndStartTagEvent event;
        event = new EndStartTagEvent(level, kind);

        ctx.result(event);

        yield new EmitEndTagState(ctx, elementName);
      }

      case VOID -> {
        final EndStartTagKind kind;
        kind = EndStartTagKind.VOID_ELEMENT;

        final EndStartTagEvent event;
        event = new EndStartTagEvent(level, kind);

        ctx.result(event);

        yield new EndElementState(ctx);
      }
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
