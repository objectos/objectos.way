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

import java.util.Objects;
import objectox.html.Ambiguous;
import objectox.html.IntSize;

final class AmbiguousRecorder {

  private final HtmlSink sink;

  AmbiguousRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AmbiguousPointer record(Ambiguous ambiguous, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int ambiguousIndex;
    ambiguousIndex = ambiguous.code();

    final IntSize ambiguousSize;
    ambiguousSize = IntSize.of(ambiguousIndex);

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    final int startIndex;
    startIndex = switch (ambiguousSize) {
      case BIT8 -> switch (valueSize) {
        case BIT8 -> record88(ambiguousIndex, valueIndex);

        case BIT16 -> record816(ambiguousIndex, valueIndex);

        case BIT24 -> record824(ambiguousIndex, valueIndex);
      };

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new AmbiguousPointer(startIndex);
  }

  private int record88(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.AMBIGUOUS88);

    sink.addInt8(nameIndex);

    sink.addInt8(valueIndex);

    return startIndex;
  }

  private int record816(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.AMBIGUOUS816);

    sink.addInt8(nameIndex);

    sink.addInt16(valueIndex);

    return startIndex;
  }

  private int record824(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.AMBIGUOUS824);

    sink.addInt8(nameIndex);

    sink.addInt24(valueIndex);

    return startIndex;
  }

}
