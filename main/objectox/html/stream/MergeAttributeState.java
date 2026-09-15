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

import objectox.html.AmbiguousName;
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

    final ElementFrame elem;
    elem = f.elementFrame;

    if (!elem.hasValue()) {
      return toScan();
    }

    final int jmpIndex;
    jmpIndex = elem.peekValue(ctx);

    final Frame jmp;
    jmp = new Frame(jmpIndex);

    ctx.push(jmp);

    final byte code;
    code = ctx.nextByte();

    return switch (code) {
      case HtmlBytes.xATTRIBUTE88 -> compute0Attr(IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.xATTRIBUTE816 -> compute0Attr(IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.xATTRIBUTE824 -> compute0Attr(IntSize.BIT8, IntSize.BIT24);

      case HtmlBytes.aAMBIGUOUS88 -> compute1Ambi(IntSize.BIT8, IntSize.BIT8);

      case HtmlBytes.aAMBIGUOUS816 -> compute1Ambi(IntSize.BIT8, IntSize.BIT16);

      case HtmlBytes.aAMBIGUOUS824 -> compute1Ambi(IntSize.BIT8, IntSize.BIT24);

      default -> popAndToScan();
    };
  }

  private State compute0Attr(IntSize nextNameSize, IntSize nextValueSize) {
    if (!nextNameSize.equals(nameSize)) {
      return popAndToScan();
    }

    final int nextNameIndex;
    nextNameIndex = ctx.nextInt(nameSize);

    return computeCommon(nextNameIndex, nextValueSize);
  }

  private State compute1Ambi(IntSize ambiSize, IntSize nextValueSize) {
    final int nameIndex;
    nameIndex = ctx.nextInt(ambiSize);

    final AmbiguousName ambiName;
    ambiName = AmbiguousName.get(nameIndex);

    final AttributeName nextName;
    nextName = ambiName.attribute;

    final int nextNameIndex;
    nextNameIndex = nextName.index();

    return computeCommon(nextNameIndex, nextValueSize);
  }

  private State computeCommon(int nextNameIndex, IntSize nextValueSize) {
    final int nameIndex;
    nameIndex = attributeName.index();

    if (nextNameIndex != nameIndex) {
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

    final ElementFrame elem;
    elem = f.elementFrame;

    elem.nextValue(ctx); // skip consumed attribute

    return new MergeAttributeState(ctx, nameSize, attributeName, merged);
  }

  private State popAndToScan() {
    ctx.pop(); // jmp frame

    return toScan();
  }

  private State toScan() {
    final int level;
    level = ctx.stackIndex();

    final RenderAttributeEvent event;
    event = new RenderAttributeEvent(level, attributeName, value);

    ctx.result(event);

    return new ScanElementAttributeState(ctx);
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
