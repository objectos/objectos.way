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
import objectos.html.ElementName;
import objectos.html.rec.Instruction;
import objectox.html.HtmlByteProto;
import objectox.html.elem.ElementNamePojo;

final class ElementRecorder {

  private final HtmlSink sink;

  ElementRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final ElementInstruction record(ElementName name, Instruction... contents) {
    final Instruction[] insts;
    insts = Objects.requireNonNull(contents, "contents == null");

    final int startIndex;
    startIndex = sink.addByte(HtmlByteProto.ELEMENT);

    final ElementNamePojo namePojo;
    namePojo = (ElementNamePojo) name;

    final int nameIndex;
    nameIndex = namePojo.index();

    if (nameIndex >= 0) {
      sink.addByte(HtmlByteProto.STANDARD_NAME);

      sink.addInt8(nameIndex);
    } else {
      throw new UnsupportedOperationException();
    }

    final int length;
    length = insts.length;

    sink.addInt16(length);

    for (Instruction inst : insts) {
      final int value;
      value = inst.value();

      sink.addInt24(value);
    }

    return new ElementInstruction(startIndex);
  }

}
