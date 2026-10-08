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

final class BeginStartTagState implements State, ScanOp {

  private final Ctx ctx;

  private final ScanOpKind opKind;

  private final ElementKind elementKind;

  private final IntSize nameSize;

  BeginStartTagState(Ctx ctx, ScanOpKind opKind, ElementKind elementKind, IntSize nameSize) {
    this.ctx = ctx;

    this.opKind = opKind;

    this.elementKind = elementKind;

    this.nameSize = nameSize;
  }

  @Override
  public final State compute() {
    final int level;
    level = ctx.stackIndex();

    final int nameIndex;
    nameIndex = ctx.nextInt(nameSize);

    final ElementName elementName;
    elementName = ElementName.get(nameIndex);

    final BeginStartTagEvent event;
    event = new BeginStartTagEvent(level, elementName);

    ctx.result(event);

    final byte values;
    values = ctx.nextByte();

    final IntSize valueSize;
    valueSize = valueSize(values);

    final int valueLength;
    valueLength = ctx.nextInt(valueSize);

    final ElementFrame elem;
    elem = new ElementFrame(elementKind, elementName, valueLength, valueSize);

    final Frame current;
    current = ctx.peekFrame();

    current.set(elem);

    return new ScanElementAttributeState(ctx);
  }

  @Override
  public final State documentNode() {
    return switch (opKind) {
      case REGULAR -> this;

      case X -> skipState();
    };
  }

  @Override
  public final State elementAttribute() {
    return switch (opKind) {
      case REGULAR -> ScanOp.super.elementAttribute();

      case X -> this;
    };
  }

  @Override
  public final State elementNode() {
    return switch (opKind) {
      case REGULAR -> ScanOp.super.elementNode();

      case X -> this;
    };
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof BeginStartTagState that
        && ctx.equals(that.ctx)
        && opKind.equals(that.opKind)
        && elementKind.equals(that.elementKind)
        && nameSize.equals(that.nameSize);
  }

  private State skipState() {
    return switch (elementKind) {
      case REGULAR -> new SkipRegularElementState(ctx, nameSize);

      case VOID -> new SkipVoidElementState(ctx, nameSize);
    };
  }

  private IntSize valueSize(byte values) {
    return switch (values) {
      case HtmlBytes.VALUES8 -> IntSize.BIT8;

      case HtmlBytes.VALUES16 -> IntSize.BIT16;

      case HtmlBytes.VALUES24 -> IntSize.BIT24;

      default -> throw State.malformed();
    };
  }

}
