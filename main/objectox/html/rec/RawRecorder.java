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
package objectox.html.rec;

import java.util.Objects;

final class RawRecorder {

  private final HtmlSink sink;

  RawRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final RawPointer record(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    final int startIndex;
    startIndex = switch (valueSize) {
      case BIT8 -> record8(valueIndex);

      case BIT16 -> record16(valueIndex);

      case BIT24 -> record24(valueIndex);
    };

    return new RawPointer(startIndex);
  }

  private int record8(int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.RAW8);

    sink.addInt8(valueIndex);

    return startIndex;
  }

  private int record16(int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.RAW16);

    sink.addInt16(valueIndex);

    return startIndex;
  }

  private int record24(int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.RAW24);

    sink.addInt24(valueIndex);

    return startIndex;
  }

}
