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
import objectox.html.elem.ElementName;

final class FragmentAttributeRecorder {

  private final HtmlSink sink;

  private final ValueRecorder valueRecorder;

  FragmentAttributeRecorder(HtmlSink sink, ValueRecorder valueRecorder) {
    this.sink = sink;

    this.valueRecorder = valueRecorder;
  }

  public final int record(ElementName elementName, IntSize size, int fragValue) {
    int sum = 0;

    int cursor = fragValue + 4;

    loop: while (sink.hasByte(cursor)) {
      final int value;
      value = cursor++;

      final byte b;
      b = sink.getByte(value);

      switch (b) {
        case HtmlBytes.FRAGEND -> {
          break loop;
        }

        case HtmlBytes.FRAGSTR -> {
          cursor = sink.getInt24(value + 1);

          sum += record(elementName, size, value);
        }

        case HtmlBytes.xFRAGSTR -> {
          cursor = sink.getInt24(value + 1);
        }

        case HtmlBytes.ATTRIBUTE88,
             HtmlBytes.JSACTION88 -> {
          sum += valueRecorder.record(size, value);

          cursor += 1 + 1;
        }

        case HtmlBytes.ATTRIBUTE816,
             HtmlBytes.JSACTION816 -> {
          sum += valueRecorder.record(size, value);

          cursor += 1 + 2;
        }

        case HtmlBytes.ATTRIBUTE824,
             HtmlBytes.JSACTION824 -> {
          sum += valueRecorder.record(size, value);

          cursor += 1 + 3;
        }

        case HtmlBytes.xATTRIBUTE88,
             HtmlBytes.xJSACTION88 -> cursor += 1 + 1;

        case HtmlBytes.xATTRIBUTE816,
             HtmlBytes.xJSACTION816 -> cursor += 1 + 2;

        case HtmlBytes.xATTRIBUTE824,
             HtmlBytes.xJSACTION824 -> cursor += 1 + 3;

        case HtmlBytes.BOOLEAN8 -> {
          sum += valueRecorder.record(size, value);

          cursor += 1;
        }

        case HtmlBytes.xBOOLEAN8 -> cursor += 1;

        case HtmlBytes.ELEMSTR8,
             HtmlBytes.ELEMVOID8,
             HtmlBytes.xELEMSTR8,
             HtmlBytes.xELEMVOID8 -> cursor = skipElement(value);

        case HtmlBytes.RAW8,
             HtmlBytes.xRAW8,
             HtmlBytes.TEXT8,
             HtmlBytes.xTEXT8 -> cursor += 1;

        case HtmlBytes.RAW16,
             HtmlBytes.xRAW16,
             HtmlBytes.TEXT16,
             HtmlBytes.xTEXT16 -> cursor += 2;

        case HtmlBytes.RAW24,
             HtmlBytes.xRAW24,
             HtmlBytes.TEXT24,
             HtmlBytes.xTEXT24 -> cursor += 3;
      }
    }

    return sum;
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

}
