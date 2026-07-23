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
import objectos.html.AttributeName;

final class AttributeRecorder {

  private final HtmlSink sink;

  AttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributeInstruction record(AttributeName name, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> switch (valueSize) {
        case BIT8 -> record88(nameIndex, valueIndex);

        case BIT16 -> record816(nameIndex, valueIndex);

        case BIT24 -> record824(nameIndex, valueIndex);
      };

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new AttributeInstruction(startIndex);
  }

  private int record88(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ATTRIBUTE88);

    sink.addInt8(nameIndex);

    sink.addInt8(valueIndex);

    return startIndex;
  }

  private int record816(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ATTRIBUTE816);

    sink.addInt8(nameIndex);

    sink.addInt16(valueIndex);

    return startIndex;
  }

  private int record824(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ATTRIBUTE824);

    sink.addInt8(nameIndex);

    sink.addInt24(valueIndex);

    return startIndex;
  }

}
