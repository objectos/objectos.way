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

import java.util.Arrays;
import java.util.HexFormat;
import java.util.Objects;
import objectos.html.stream.Event;
import objectos.script.JsAction;
import objectos.internal.Util;
import objectox.html.IntSize;
import objectox.html.markup.HtmlBytes;

class Ctx {

  static final Frame[] EMPTY_STACK = new Frame[0];

  final byte[] code;

  private boolean computed;

  final Object[] objects;

  private Event result;

  private Frame[] stack;

  private int stackIndex = -1;

  Ctx(byte[] code, Object[] objects, Frame[] stack, int stackIndex) {
    this.code = code;

    this.objects = objects;

    this.stack = stack;

    this.stackIndex = stackIndex;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Ctx that
        && Arrays.equals(code, that.code)
        && Arrays.equals(objects, that.objects)
        && Objects.equals(result, that.result)
        && Arrays.equals(stack, 0, stackIndex + 1, that.stack, 0, that.stackIndex + 1);
  }

  public final int frames() {
    return stackIndex + 1;
  }

  public final boolean hasByte() {
    final Frame frame;
    frame = stack[stackIndex];

    return frame.index < code.length;
  }

  public final boolean hasEvent() {
    return result != null;
  }

  public final byte nextByte() {
    final Frame frame;
    frame = stack[stackIndex];

    final int idx;
    idx = frame.index;

    final byte result;
    result = code[idx];

    frame.index = idx + 1;

    return result;
  }

  public final Event nextEvent() {
    final Event res;
    res = result;

    computed = false;

    result = null;

    return res;
  }

  public final int nextInt(IntSize size) {
    final Frame frame;
    frame = stack[stackIndex];

    final int startIndex;
    startIndex = frame.index;

    frame.index = size.skip(startIndex);

    return switch (size) {
      case BIT8 -> toInt(startIndex, 0);

      case BIT16 -> toInt(startIndex, 8) | toInt(startIndex + 1, 0);

      case BIT24 -> toInt(startIndex, 16) | toInt(startIndex + 1, 8) | toInt(startIndex + 2, 0);
    };
  }

  public final ScanOp nextScanOp() {
    final byte code;
    code = nextByte();

    return switch (code) {
      case HtmlBytes.DOCTYPE -> new RenderDoctypeState(this);

      case HtmlBytes.ELEMSTR8 -> new BeginStartTagState(this, ScanLevel.TOP_LEVEL, ElementKind.REGULAR, IntSize.BIT8);
      case HtmlBytes.ELEMVOID8 -> new BeginStartTagState(this, ScanLevel.TOP_LEVEL, ElementKind.VOID, IntSize.BIT8);

      case HtmlBytes.FRAGSTR -> new SkipState(this, 3);
      case HtmlBytes.FRAGEND -> new ScanDocumentNodeState(this);

      case HtmlBytes.TEXT8 -> new RenderTextState(this, ScanLevel.TOP_LEVEL, IntSize.BIT8);
      case HtmlBytes.TEXT16 -> new RenderTextState(this, ScanLevel.TOP_LEVEL, IntSize.BIT16);
      case HtmlBytes.TEXT24 -> new RenderTextState(this, ScanLevel.TOP_LEVEL, IntSize.BIT24);

      case HtmlBytes.RAW8 -> new RenderRawState(this, ScanLevel.TOP_LEVEL, IntSize.BIT8);
      case HtmlBytes.RAW16 -> new RenderRawState(this, ScanLevel.TOP_LEVEL, IntSize.BIT16);
      case HtmlBytes.RAW24 -> new RenderRawState(this, ScanLevel.TOP_LEVEL, IntSize.BIT24);

      case HtmlBytes.THEADING8 -> new TestableHeadingState(this, TestableLevel.TOP_LEVEL, IntSize.BIT8);
      case HtmlBytes.THEADING16 -> new TestableHeadingState(this, TestableLevel.TOP_LEVEL, IntSize.BIT16);
      case HtmlBytes.THEADING24 -> new TestableHeadingState(this, TestableLevel.TOP_LEVEL, IntSize.BIT24);

      case HtmlBytes.TCELL8 -> new TestableCellState(this, TestableLevel.TOP_LEVEL, IntSize.BIT8);
      case HtmlBytes.TCELL16 -> new TestableCellState(this, TestableLevel.TOP_LEVEL, IntSize.BIT16);
      case HtmlBytes.TCELL24 -> new TestableCellState(this, TestableLevel.TOP_LEVEL, IntSize.BIT24);

      case HtmlBytes.TPROP8 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT8);
      case HtmlBytes.TPROP16 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT16);
      case HtmlBytes.TPROP24 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.TPROPKEY8 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT8);
      case HtmlBytes.TPROPKEY16 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT16);
      case HtmlBytes.TPROPKEY24 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.TPROPVAL8 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT8);
      case HtmlBytes.TPROPVAL16 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT16);
      case HtmlBytes.TPROPVAL24 -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.TNEWLINE -> new RenderTestableOnlyState(this, ScanLevel.TOP_LEVEL, TestableKind.NEWLINE, IntSize.BIT8);

      case HtmlBytes.xELEMSTR8 -> new BeginStartTagState(this, ScanLevel.NESTED, ElementKind.REGULAR, IntSize.BIT8);
      case HtmlBytes.xELEMVOID8 -> new BeginStartTagState(this, ScanLevel.NESTED, ElementKind.VOID, IntSize.BIT8);

      case HtmlBytes.xATTRIBUTE88 -> new RenderAttributeState(this, IntSize.BIT8, IntSize.BIT8);
      case HtmlBytes.xATTRIBUTE816 -> new RenderAttributeState(this, IntSize.BIT8, IntSize.BIT16);
      case HtmlBytes.xATTRIBUTE824 -> new RenderAttributeState(this, IntSize.BIT8, IntSize.BIT24);

      case HtmlBytes.xBOOLEAN8 -> new RenderBooleanAttributeState(this, IntSize.BIT8);

      case HtmlBytes.xJSACTION88 -> new RenderJsActionAttributeState(this, IntSize.BIT8, IntSize.BIT8);
      case HtmlBytes.xJSACTION816 -> new RenderJsActionAttributeState(this, IntSize.BIT8, IntSize.BIT16);
      case HtmlBytes.xJSACTION824 -> new RenderJsActionAttributeState(this, IntSize.BIT8, IntSize.BIT24);

      case HtmlBytes.xFRAGSTR -> new SkipFragmentState(this);

      case HtmlBytes.xTEXT8 -> new RenderTextState(this, ScanLevel.NESTED, IntSize.BIT8);
      case HtmlBytes.xTEXT16 -> new RenderTextState(this, ScanLevel.NESTED, IntSize.BIT16);
      case HtmlBytes.xTEXT24 -> new RenderTextState(this, ScanLevel.NESTED, IntSize.BIT24);

      case HtmlBytes.xRAW8 -> new RenderRawState(this, ScanLevel.NESTED, IntSize.BIT8);
      case HtmlBytes.xRAW16 -> new RenderRawState(this, ScanLevel.NESTED, IntSize.BIT16);
      case HtmlBytes.xRAW24 -> new RenderRawState(this, ScanLevel.NESTED, IntSize.BIT24);

      case HtmlBytes.xTHEADING8 -> new TestableHeadingState(this, TestableLevel.REGULAR, IntSize.BIT8);
      case HtmlBytes.xTHEADING16 -> new TestableHeadingState(this, TestableLevel.REGULAR, IntSize.BIT16);
      case HtmlBytes.xTHEADING24 -> new TestableHeadingState(this, TestableLevel.REGULAR, IntSize.BIT24);

      case HtmlBytes.xTCELL8 -> new TestableCellState(this, TestableLevel.REGULAR, IntSize.BIT8);
      case HtmlBytes.xTCELL16 -> new TestableCellState(this, TestableLevel.REGULAR, IntSize.BIT16);
      case HtmlBytes.xTCELL24 -> new TestableCellState(this, TestableLevel.REGULAR, IntSize.BIT24);

      case HtmlBytes.xTPROP8 -> new RenderTestableTextState(this, TestableKind.PROP, IntSize.BIT8);
      case HtmlBytes.xTPROP16 -> new RenderTestableTextState(this, TestableKind.PROP, IntSize.BIT16);
      case HtmlBytes.xTPROP24 -> new RenderTestableTextState(this, TestableKind.PROP, IntSize.BIT24);

      case HtmlBytes.xTPROPKEY8 -> new RenderTestableTextState(this, TestableKind.PROP_KEY, IntSize.BIT8);
      case HtmlBytes.xTPROPKEY16 -> new RenderTestableTextState(this, TestableKind.PROP_KEY, IntSize.BIT16);
      case HtmlBytes.xTPROPKEY24 -> new RenderTestableTextState(this, TestableKind.PROP_KEY, IntSize.BIT24);

      case HtmlBytes.xTPROPVAL8 -> new RenderTestableTextState(this, TestableKind.PROP_VALUE, IntSize.BIT8);
      case HtmlBytes.xTPROPVAL16 -> new RenderTestableTextState(this, TestableKind.PROP_VALUE, IntSize.BIT16);
      case HtmlBytes.xTPROPVAL24 -> new RenderTestableTextState(this, TestableKind.PROP_VALUE, IntSize.BIT24);

      case HtmlBytes.xTNEWLINE -> new RenderTestableOnlyState(this, ScanLevel.NESTED, TestableKind.NEWLINE, IntSize.BIT8);

      case HtmlBytes.oTHEADING8 -> new TestableHeadingState(this, TestableLevel.TEST_ONLY, IntSize.BIT8);
      case HtmlBytes.oTHEADING16 -> new TestableHeadingState(this, TestableLevel.TEST_ONLY, IntSize.BIT16);
      case HtmlBytes.oTHEADING24 -> new TestableHeadingState(this, TestableLevel.TEST_ONLY, IntSize.BIT24);

      case HtmlBytes.oTCELL8 -> new TestableCellState(this, TestableLevel.TEST_ONLY, IntSize.BIT8);
      case HtmlBytes.oTCELL16 -> new TestableCellState(this, TestableLevel.TEST_ONLY, IntSize.BIT16);
      case HtmlBytes.oTCELL24 -> new TestableCellState(this, TestableLevel.TEST_ONLY, IntSize.BIT24);

      default -> throw State.malformed();
    };
  }

  public final JsAction objJsAction(int index) {
    final Object o;
    o = objects[index];

    return (JsAction) o;
  }

  public final String objString(int index) {
    final Object o;
    o = objects[index];

    return (String) o;
  }

  public final byte peekByte() {
    final Frame frame;
    frame = stack[stackIndex];

    final int idx;
    idx = frame.index;

    return code[idx];
  }

  public final Frame peekFrame() {
    return stack[stackIndex];
  }

  public final int peekInt(IntSize size) {
    final Frame frame;
    frame = stack[stackIndex];

    final int startIndex;
    startIndex = frame.index;

    return switch (size) {
      case BIT8 -> toInt(startIndex, 0);

      case BIT16 -> toInt(startIndex, 8) | toInt(startIndex + 1, 0);

      case BIT24 -> toInt(startIndex, 16) | toInt(startIndex + 1, 8) | toInt(startIndex + 2, 0);
    };
  }

  public final Frame pop() {
    return stack[stackIndex--];
  }

  public final void push(Frame frame) {
    final int index;
    index = ++stackIndex;

    stack = Util.growIfNecessary(stack, index);

    stack[index] = frame;
  }

  public final void result(Event value) {
    if (computed) {
      final String msg;
      msg = "Result has not been consumed yet";

      throw new IllegalStateException(msg);
    }

    computed = true;

    result = value;
  }

  public final boolean shouldCompute() {
    return !computed;
  }

  public final void skip(int value) {
    final Frame frame;
    frame = stack[stackIndex];

    frame.index += value;
  }

  public final int stackIndex() {
    return stackIndex;
  }

  @Override
  public final String toString() {
    return """
    code    = %s
    objects = %s
    stack   = %s
    """.formatted(
        HexFormat.of().formatHex(code),
        Arrays.toString(objects),
        Arrays.toString(Arrays.copyOf(stack, stackIndex + 1))
    );
  }

  private int toInt(int index, int shift) {
    final byte byteValue;
    byteValue = code[index];

    final int intValue;
    intValue = Byte.toUnsignedInt(byteValue);

    return intValue << shift;
  }

}
