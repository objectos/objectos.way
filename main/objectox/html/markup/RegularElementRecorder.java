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
import objectos.html.markup.RegularElementValue;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class RegularElementRecorder {

  private final HtmlSink sink;

  private final ElementAttributesRecorder attributesRecorder;

  private final ElementAttributesPreRecorder elementAttributesPreRecorder;

  private final ElementChildrenRecorder childrenRecorder;

  RegularElementRecorder(HtmlSink sink, ElementAttributesRecorder attributesRecorder, ElementAttributesPreRecorder elementAttributesPreRecorder, ElementChildrenRecorder childrenRecorder) {
    this.sink = sink;

    this.attributesRecorder = attributesRecorder;

    this.elementAttributesPreRecorder = elementAttributesPreRecorder;

    this.childrenRecorder = childrenRecorder;
  }

  public final ElementPointer record(String name, RegularElementValue... values) {
    final ElementName elementName;
    elementName = ElementName.getOrCreate(name);

    return record(elementName, values);
  }

  public final ElementPointer record(ElementName name, RegularElementValue... $values) {
    final RegularElementValue[] values;
    values = Objects.requireNonNull($values, "values == null");

    final int savepoint;
    savepoint = sink.codeIndex();

    try {
      return record0(name, values);
    } catch (ObjectAttributeDetected e) {
      sink.codeIndex(savepoint);

      elementAttributesPreRecorder.record(values);

      return record0(name, values);
    }
  }

  private ElementPointer record0(ElementName name, RegularElementValue[] values) {
    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int startIndex;
    startIndex = startTag(nameIndex, nameSize);

    final IntSize valuesSize;
    valuesSize = sink.codeSize();

    final int valuesIndex;
    valuesIndex = valuesBytes(valuesSize);

    final int attributes;
    attributes = attributesRecorder.record(name, valuesSize, values);

    final int children;
    children = childrenRecorder.record(valuesSize, values);

    final int size;
    size = attributes + children;

    sink.setInt(valuesSize, valuesIndex, size);

    endTag(nameIndex, nameSize);

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
    startIndex = sink.addByte(HtmlBytes.ELEMSTR8);

    sink.addInt8(nameIndex);

    return startIndex;
  }

  private int valuesBytes(IntSize size) {
    final byte values;
    values = switch (size) {
      case BIT8 -> HtmlBytes.VALUES8;

      case BIT16 -> HtmlBytes.VALUES16;

      case BIT24 -> HtmlBytes.VALUES24;
    };

    sink.addByte(values);

    return sink.addInt(size, 0);
  }

  private void endTag(int nameIndex, IntSize nameSize) {
    switch (nameSize) {
      case BIT8 -> endTag8(nameIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    }
  }

  private void endTag8(int nameIndex) {
    sink.addByte(HtmlBytes.ELEMEND8);

    sink.addInt8(nameIndex);
  }

}
