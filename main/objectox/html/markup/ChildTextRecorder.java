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
import objectox.html.IntSize;

final class ChildTextRecorder {

  private final HtmlSink sink;

  ChildTextRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final void record(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final TextEscaper escaper;
    escaper = new TextEscaper(v);

    final String escaped;
    escaped = escaper.escape();

    final int valueIndex;
    valueIndex = sink.addObject(escaped);

    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    switch (valueSize) {
      case BIT8 -> record8(valueIndex);

      case BIT16 -> record16(valueIndex);

      case BIT24 -> record24(valueIndex);
    }
  }

  private void record8(int valueIndex) {
    sink.addByte(HtmlBytes.TEXT8);

    sink.addInt8(valueIndex);
  }

  private void record16(int valueIndex) {
    sink.addByte(HtmlBytes.TEXT16);

    sink.addInt16(valueIndex);
  }

  private void record24(int valueIndex) {
    sink.addByte(HtmlBytes.TEXT24);

    sink.addInt24(valueIndex);
  }

}
