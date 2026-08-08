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
import objectos.html.markup.ElementValue;
import objectox.html.IntSize;
import objectox.html.elem.ElementNamePojo;

final class ElementRecorder {

  private final HtmlSink sink;

  private final ElementAttributesRecorder attributesRecorder;

  private final ElementChildrenRecorder childrenRecorder;

  ElementRecorder(HtmlSink sink, ElementAttributesRecorder attributesRecorder, ElementChildrenRecorder childrenRecorder) {
    this.sink = sink;

    this.attributesRecorder = attributesRecorder;

    this.childrenRecorder = childrenRecorder;
  }

  public final ElementPointer record(ElementName elementName, ElementValue... contents) {
    Objects.requireNonNull(contents, "contents == null");

    final ElementNamePojo name;
    name = (ElementNamePojo) elementName;

    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int startIndex;
    startIndex = startTag(nameIndex, nameSize);

    final int jmpIndex;
    jmpIndex = contents(name, contents);

    end(name, nameIndex, nameSize);

    sink.jmp24(jmpIndex);

    return new ElementPointer(startIndex);
  }

  private int startTag(int nameIndex, IntSize nameSize) {
    return switch (nameSize) {
      case BIT8 -> startTag8(nameIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };
  }

  private int startTag8(int nameIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.STARTTAG8);

    sink.addInt8(nameIndex);

    return startIndex;
  }

  private int contents(ElementNamePojo name, ElementValue[] contents) {
    sink.addByte(HtmlBytes.JMP24);

    final int jmpIndex;
    jmpIndex = sink.addInt24(0);

    attributesRecorder.record(name, contents);

    childrenRecorder.record(name, contents);

    return jmpIndex;
  }

  private void end(ElementNamePojo name, int nameIndex, IntSize nameSize) {
    if (name.endTag()) {
      endTag(nameIndex, nameSize);
    } else {
      sink.addByte(HtmlBytes.VOIDELEM);
    }
  }

  private void endTag(int nameIndex, IntSize nameSize) {
    switch (nameSize) {
      case BIT8 -> endTag8(nameIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    }
  }

  private void endTag8(int nameIndex) {
    sink.addByte(HtmlBytes.ENDTAG8);

    sink.addInt8(nameIndex);
  }

}
