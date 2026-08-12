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

import objectos.script.JsAction;
import objectox.html.IntSize;
import objectox.html.attr.AbstractAttribute;
import objectox.html.attr.AttributeName;
import objectox.html.attr.AttributeValueEscaper;

final class AttributeRecorder {

  private final HtmlSink sink;

  AttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributePointer record(AbstractAttribute attribute) {
    final AttributeName attributeName;
    attributeName = attribute.attributeName;

    final String value;
    value = attribute.value();

    return record(attributeName, value);
  }

  public final AttributePointer record(AttributeName attributeName, String value) {
    final AttributeValueEscaper escaper;
    escaper = new AttributeValueEscaper(value);

    final String escaped;
    escaped = escaper.escape();

    return record0(attributeName, escaped);
  }

  public final AttributePointer record(AttributeName attributeName, JsAction value) {
    return record0(attributeName, value);
  }

  private AttributePointer record0(AttributeName attributeName, Object value) {
    final int nameIndex;
    nameIndex = attributeName.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int valueIndex;
    valueIndex = sink.addObject(value);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> record8(nameIndex, valueIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new AttributePointer(startIndex);
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
