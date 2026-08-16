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
import objectox.html.elem.ElementName;

final class TextElementRecorder {

  private final HtmlSink sink;

  TextElementRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final ElementPointer record(String name, String text) {
    final ElementName elementName;
    elementName = ElementName.getOrCreate(name);

    return record(elementName, text);
  }

  public final ElementPointer record(ElementName name, String text) {
    final String value;
    value = Objects.requireNonNull(text, "text == null");

    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final TextEscaper escaper;
    escaper = new TextEscaper(value);

    final String escaped;
    escaped = escaper.escape();

    final int valueIndex;
    valueIndex = sink.addObject(escaped);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> record8(nameIndex, valueIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new ElementPointer(startIndex);
  }

  private int record8(final int nameIndex, final int valueIndex) {
    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    return switch (valueSize) {
      case BIT8 -> record88(nameIndex, valueIndex);

      case BIT16 -> record816(nameIndex, valueIndex);

      case BIT24 -> record824(nameIndex, valueIndex);
    };
  }

  private int record88(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELEMTXT88);

    sink.addInt8(nameIndex);

    sink.addInt8(valueIndex);

    return startIndex;
  }

  private int record816(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELEMTXT816);

    sink.addInt8(nameIndex);

    sink.addInt16(valueIndex);

    return startIndex;
  }

  private int record824(int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELEMTXT824);

    sink.addInt8(nameIndex);

    sink.addInt24(valueIndex);

    return startIndex;
  }

}
