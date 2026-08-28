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
import objectox.html.attr.AttributeName;
import objectox.html.markup.HtmlBytes;

final class MergeAttributeState implements State {

  private final Ctx ctx;

  private final IntSize nameSize;

  private final AttributeName attributeName;

  private final String value;

  MergeAttributeState(Ctx ctx, IntSize nameSize, AttributeName attributeName, String value) {
    this.ctx = ctx;

    this.nameSize = nameSize;

    this.attributeName = attributeName;

    this.value = value;
  }

  @Override
  public final State compute() {
    final Frame f;
    f = ctx.peekFrame();

    final AbstractElementAttributeFrame current;
    current = f.as(AbstractElementAttributeFrame.class);

    if (!current.hasNext()) {
      return toScan();
    }

    final int jmpIndex;
    jmpIndex = current.peekInt(ctx);

    final JumpFrame jmp;
    jmp = new JumpFrame(jmpIndex);

    ctx.push(jmp);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xATTRIBUTE88 -> compute0(IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.xATTRIBUTE816 -> compute0(IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.xATTRIBUTE824 -> compute0(IntSize.BIT8, IntSize.BIT24);

      default -> popAndToScan();
    };
  }

  private State compute0(IntSize nextNameSize, IntSize nextValueSize) {
    if (!nextNameSize.equals(nameSize)) {
      return popAndToScan();
    }

    final int nameIndex;
    nameIndex = attributeName.index();

    final int nextNameIndex;
    nextNameIndex = ctx.nextInt(nameSize);

    if (nameIndex != nextNameIndex) {
      return popAndToScan();
    }

    final int valueIndex;
    valueIndex = ctx.nextInt(nextValueSize);

    final String nextValue;
    nextValue = ctx.objString(valueIndex);

    final String merged;
    merged = value + ' ' + nextValue;

    ctx.pop(); // jmp frame

    final Frame f;
    f = ctx.peekFrame();

    final AbstractElementAttributeFrame current;
    current = f.as(AbstractElementAttributeFrame.class);

    current.nextInt(ctx); // skip consumed attribute

    return new MergeAttributeState(ctx, nameSize, attributeName, merged);
  }

  private State popAndToScan() {
    ctx.pop(); // jmp frame

    return toScan();
  }

  private State toScan() {
    final RenderAttributeEvent event;
    event = new RenderAttributeEvent(attributeName, value);

    ctx.result(event);

    final Frame current;
    current = ctx.peekFrame();

    return current.newState(ctx);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof MergeAttributeState that
        && ctx.equals(that.ctx)
        && nameSize.equals(that.nameSize)
        && attributeName.equals(that.attributeName)
        && value.equals(that.value);
  }

}
