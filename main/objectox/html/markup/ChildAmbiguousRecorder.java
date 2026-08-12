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
import objectox.html.elem.ElementNamePojo;

final class ChildAmbiguousRecorder {

  private final HtmlSink sink;

  ChildAmbiguousRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final void record(ElementName name, String text) {
    final ElementNamePojo pojo;
    pojo = (ElementNamePojo) name;

    final int nameIndex;
    nameIndex = pojo.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final TextEscaper escaper;
    escaper = new TextEscaper(text);

    final String escaped;
    escaped = escaper.escape();

    final int textIndex;
    textIndex = sink.addObject(escaped);

    switch (nameSize) {
      case BIT8 -> record8(nameIndex, textIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    }
  }

  private void record8(final int nameIndex, final int textIndex) {
    final IntSize textSize;
    textSize = IntSize.of(textIndex);

    switch (textSize) {
      case BIT8 -> record88(nameIndex, textIndex);

      case BIT16 -> record816(nameIndex, textIndex);

      case BIT24 -> record824(nameIndex, textIndex);
    }
  }

  private int record88(int nameIndex, int textIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELTXT88);

    sink.addInt8(nameIndex);

    sink.addInt8(textIndex);

    return startIndex;
  }

  private int record816(int nameIndex, int textIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELTXT816);

    sink.addInt8(nameIndex);

    sink.addInt16(textIndex);

    return startIndex;
  }

  private int record824(int nameIndex, int textIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.ELTXT824);

    sink.addInt8(nameIndex);

    sink.addInt24(textIndex);

    return startIndex;
  }

}
