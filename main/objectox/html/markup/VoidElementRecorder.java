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
import objectos.html.markup.VoidElementValue;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class VoidElementRecorder {

  private final HtmlSink sink;

  private final ElementAttributesRecorder attributesRecorder;

  private final ElementPreRecorder elementPreRecorder;

  VoidElementRecorder(HtmlSink sink, ElementAttributesRecorder attributesRecorder, ElementPreRecorder elementPreRecorder) {
    this.sink = sink;

    this.attributesRecorder = attributesRecorder;

    this.elementPreRecorder = elementPreRecorder;
  }

  public final ElementPointer record(String name, VoidElementValue... values) {
    final ElementName elementName;
    elementName = ElementName.getOrCreate(name);

    return record(elementName, values);
  }

  public final ElementPointer record(ElementName name, VoidElementValue... $values) {
    final VoidElementValue[] values;
    values = Objects.requireNonNull($values, "values == null");

    final int savepoint;
    savepoint = sink.codeIndex();

    try {
      return record0(name, values);
    } catch (ObjectAttributeDetected e) {
      sink.codeIndex(savepoint);

      elementPreRecorder.record(values);

      return record0(name, values);
    }
  }

  private ElementPointer record0(ElementName name, VoidElementValue[] values) {
    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final byte opcode;
    opcode = nameSize.toVoidStartTagByte();

    final int startIndex;
    startIndex = sink.addByte(opcode);

    sink.addInt8(nameIndex);

    final IntSize valuesSize;
    valuesSize = sink.codeSize();

    final byte valuesByte;
    valuesByte = valuesSize.toValuesByte();

    sink.addByte(valuesByte);

    final int valuesIndex;
    valuesIndex = sink.addInt(valuesSize, 0);

    final int size;
    size = attributesRecorder.record(name, valuesSize, values);

    sink.setInt(valuesSize, valuesIndex, size);

    return new ElementPointer(startIndex);
  }

}
