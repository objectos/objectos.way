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
package objectox.html.markup;

import objectox.html.IntSize;

final class FragmentChildRecorder {

  private final HtmlSink sink;

  private final ValueRecorder valueRecorder;

  FragmentChildRecorder(
      HtmlSink sink,

      ValueRecorder valueRecorder
  ) {
    this.sink = sink;

    this.valueRecorder = valueRecorder;
  }

  public final int record(IntSize size, int fragValue) {
    final Cursor cursor;
    cursor = new Cursor();

    cursor.value = fragValue + 4;

    int sum = 0;

    while (sink.hasByte(cursor.value)) {
      final int value;
      value = cursor.value++;

      final byte b;
      b = sink.getByte(value);

      if (b == HtmlBytes.FRAGEND) {
        break;
      }

      sum += record0(cursor, size, value, b);
    }

    sink.setByte(fragValue, HtmlBytes.xFRAGSTR);

    return sum;
  }

  private int record0(Cursor cursor, IntSize size, int value, byte b) {
    return switch (b) {
      case HtmlBytes.FRAGSTR -> {
        cursor.value = sink.getInt24(value + 1);

        yield record(size, value);
      }

      case HtmlBytes.xFRAGSTR -> {
        cursor.value = sink.getInt24(value + 1);

        yield 0;
      }

      case HtmlBytes.ELEMSTR8,
           HtmlBytes.ELEMVOID8 -> {
        cursor.value = skipElement(value);

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.xELEMSTR8,
           HtmlBytes.xELEMVOID8 -> {
        cursor.value = skipElement(value);

        yield 0;
      }

      case HtmlBytes.THEADING8 -> {
        cursor.value += 2;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.THEADING16 -> {
        cursor.value += 3;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.THEADING24 -> {
        cursor.value += 4;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.xTHEADING8,
           HtmlBytes.oTHEADING8 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xTHEADING16,
           HtmlBytes.oTHEADING16 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.xTHEADING24,
           HtmlBytes.oTHEADING24 -> {
        cursor.value += 4;

        yield 0;
      }

      case HtmlBytes.TCELL8 -> {
        cursor.value += 1;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TCELL16 -> {
        cursor.value += 2;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TCELL24 -> {
        cursor.value += 3;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.xTCELL8,
           HtmlBytes.oTCELL8 -> {
        cursor.value += 1;

        yield 0;
      }

      case HtmlBytes.xTCELL16,
           HtmlBytes.oTCELL16 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xTCELL24,
           HtmlBytes.oTCELL24 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.TITEM8 -> {
        cursor.value += 1;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TITEM16 -> {
        cursor.value += 2;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TITEM24 -> {
        cursor.value += 3;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.xTITEM8,
           HtmlBytes.oTITEM8 -> {
        cursor.value += 1;

        yield 0;
      }

      case HtmlBytes.xTITEM16,
           HtmlBytes.oTITEM16 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xTITEM24,
           HtmlBytes.oTITEM24 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.TPROP8 -> {
        cursor.value += 1;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TPROP16 -> {
        cursor.value += 2;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.TPROP24 -> {
        cursor.value += 3;

        yield valueRecorder.testableOnly(size, value);
      }

      case HtmlBytes.xTPROP8,
           HtmlBytes.oTPROP8 -> {
        cursor.value += 1;

        yield 0;
      }

      case HtmlBytes.xTPROP16,
           HtmlBytes.oTPROP16 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xTPROP24,
           HtmlBytes.oTPROP24 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.TROW,
           HtmlBytes.TLIST -> valueRecorder.record(size, value);

      case HtmlBytes.xTROW,
           HtmlBytes.xTLIST -> 0;

      case HtmlBytes.TEXT8 -> {
        cursor.value += 1;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.TEXT16 -> {
        cursor.value += 2;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.TEXT24 -> {
        cursor.value += 3;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.xTEXT8 -> {
        cursor.value += 1;

        yield 0;
      }

      case HtmlBytes.xTEXT16 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xTEXT24 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.RAW8 -> {
        cursor.value += 1;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.RAW16 -> {
        cursor.value += 2;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.RAW24 -> {
        cursor.value += 3;

        yield valueRecorder.record(size, value);
      }

      case HtmlBytes.xRAW8 -> {
        cursor.value += 1;

        yield 0;
      }

      case HtmlBytes.xRAW16 -> {
        cursor.value += 2;

        yield 0;
      }

      case HtmlBytes.xRAW24 -> {
        cursor.value += 3;

        yield 0;
      }

      case HtmlBytes.ATTRIBUTE88,
           HtmlBytes.xATTRIBUTE88,
           HtmlBytes.JSACTION88,
           HtmlBytes.xJSACTION88 -> {
        cursor.value += 1 + 1;

        yield 0;
      }

      case HtmlBytes.ATTRIBUTE816,
           HtmlBytes.xATTRIBUTE816,
           HtmlBytes.JSACTION816,
           HtmlBytes.xJSACTION816 -> {
        cursor.value += 1 + 2;

        yield 0;
      }

      case HtmlBytes.ATTRIBUTE824,
           HtmlBytes.xATTRIBUTE824,
           HtmlBytes.JSACTION824,
           HtmlBytes.xJSACTION824 -> {
        cursor.value += 1 + 3;

        yield 0;
      }

      case HtmlBytes.BOOLEAN8,
           HtmlBytes.xBOOLEAN8 -> {
        cursor.value += 1;

        yield 0;
      }

      default -> {
        final String msg;
        msg = "Unexpected opcode: %d at index %d".formatted(b, value);

        throw new IllegalStateException(msg);
      }
    };
  }

  private int skipElement(int elemIndex) {
    final int nameIndex;
    nameIndex = elemIndex + 1;

    final int valuesIndex;
    valuesIndex = nameIndex + 1;

    final byte values;
    values = sink.getByte(valuesIndex);

    final IntSize size;
    size = switch (values) {
      case HtmlBytes.VALUES8 -> IntSize.BIT8;

      case HtmlBytes.VALUES16 -> IntSize.BIT16;

      case HtmlBytes.VALUES24 -> IntSize.BIT24;

      default -> throw new IllegalStateException("Unexpected VALUES marker: " + values);
    };

    final int count;
    count = sink.getInt(size, valuesIndex + 1);

    return valuesIndex + 1 + size.bytes() * (count + 1);
  }

  private static final class Cursor {

    private int value;

  }

}
