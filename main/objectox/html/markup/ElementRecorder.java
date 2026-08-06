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
import objectos.html.ElementName;
import objectos.html.markup.ElementContent;
import objectos.html.markup.Instruction;
import objectox.html.IntSize;
import objectox.html.elem.ElementNamePojo;

final class ElementRecorder {

  private final HtmlSink sink;

  private final ContentsPrecorder contentsPrecorder;

  private final ContentsRecorder contentsRecorder;

  ElementRecorder(HtmlSink sink, ContentsPrecorder contentsPrecorder, ContentsRecorder contentsRecorder) {
    this.sink = sink;

    this.contentsPrecorder = contentsPrecorder;

    this.contentsRecorder = contentsRecorder;
  }

  public final ElementPointer record(ElementName name, ElementContent... contents) {
    final Instruction[] instructions;
    instructions = Objects.requireNonNull(contents, "contents == null");

    final Instruction[] precorded;
    precorded = contentsPrecorder.precord(instructions);

    final ElementNamePojo namePojo;
    namePojo = (ElementNamePojo) name;

    final int nameIndex;
    nameIndex = namePojo.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> record8(nameIndex, precorded, namePojo);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new ElementPointer(startIndex);
  }

  private int record8(int nameIndex, Instruction[] instructions, ElementNamePojo namePojo) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTTAG8);

    sink.addInt8(nameIndex);

    contentsRecorder.record(instructions);

    if (namePojo.endTag()) {
      sink.addByte(HtmlBytes.ENDTAG8);

      sink.addInt8(nameIndex);
    } else {
      sink.addByte(HtmlBytes.VOIDELEM);
    }

    return startIndex;
  }

}
