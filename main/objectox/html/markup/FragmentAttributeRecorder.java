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

  private final AmbiguousAttributeRecorder ambiguousAttributeRecorder;

  private final ValueRecorder valueRecorder;

  FragmentAttributeRecorder(HtmlSink sink, AmbiguousAttributeRecorder ambiguousAttributeRecorder, ValueRecorder valueRecorder) {
    this.sink = sink;

    this.ambiguousAttributeRecorder = ambiguousAttributeRecorder;

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

        case HtmlBytes.AMBIGUOUS88,
             HtmlBytes.AMBIGUOUS816,
             HtmlBytes.AMBIGUOUS824 -> sum += ambiguousAttributeRecorder.record(elementName, size, value);

        case HtmlBytes.ATTRIBUTE88,
             HtmlBytes.ATTRIBUTE816,
             HtmlBytes.ATTRIBUTE824,
             HtmlBytes.JSACTION88,
             HtmlBytes.JSACTION816,
             HtmlBytes.JSACTION824,
             HtmlBytes.BOOLEAN8 -> sum += valueRecorder.record(size, value);
      }
    }

    return sum;
  }

}
