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

  private final AmbiguousChildRecorder ambiguousChildRecorder;

  private final ValueRecorder valueRecorder;

  FragmentChildRecorder(
      HtmlSink sink,

      AmbiguousChildRecorder ambiguousChildRecorder,

      ValueRecorder valueRecorder
  ) {
    this.sink = sink;

    this.ambiguousChildRecorder = ambiguousChildRecorder;

    this.valueRecorder = valueRecorder;
  }

  public final int record(IntSize size, int fragValue) {
    int sum = 0;

    int cursor = fragValue + 3;

    while (sink.hasByte(cursor)) {
      final int value;
      value = cursor++;

      final byte b;
      b = sink.getByte(value);

      final int res;
      res = record0(size, value, b);

      if (res == -1) {
        break;
      }

      sum += res;
    }

    sink.setByte(fragValue, HtmlBytes.xFRAGSTR);

    return sum;
  }

  private int record0(IntSize size, int value, byte b) {
    return switch (b) {
      case HtmlBytes.FRAGEND -> -1;

      case HtmlBytes.AMBIGUOUS88,
           HtmlBytes.AMBIGUOUS816,
           HtmlBytes.AMBIGUOUS824 -> ambiguousChildRecorder.record(size, value);

      case HtmlBytes.ELEMSTR8,
           HtmlBytes.ELEMTXT88,
           HtmlBytes.ELEMTXT816,
           HtmlBytes.ELEMTXT824 -> valueRecorder.record(size, value);

      case HtmlBytes.FRAGSTR -> record(size, value);

      case HtmlBytes.RAW8,
           HtmlBytes.RAW16,
           HtmlBytes.RAW24 -> valueRecorder.record(size, value);

      case HtmlBytes.TEXT8,
           HtmlBytes.TEXT16,
           HtmlBytes.TEXT24 -> valueRecorder.record(size, value);

      default -> 0;
    };
  }

}
