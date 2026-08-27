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
import objectos.script.JsAction;
import objectox.html.IntSize;
import objectox.html.attr.AttributeObject;
import objectox.html.attr.AttributeName;
import objectox.html.attr.AttributeValueEscaper;

final class AttributeRecorder {

  private final HtmlSink sink;

  AttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributePointer css(String value) {
    final StringBuilder sb;
    sb = new StringBuilder();

    final String formatted;
    formatted = AttributeName.formatAttrValue(value, sb);

    return record(AttributeName.CLASS, formatted);
  }

  public final AttributePointer record(AttributeObject value) {
    final AttributeObject a;
    a = Objects.requireNonNull(value, "value == null");

    final AttributeName attributeName;
    attributeName = a.attributeName;

    final String v;
    v = a.value();

    return record(attributeName, v);
  }

  public final AttributePointer record(AttributeName attributeName, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final AttributeValueEscaper escaper;
    escaper = new AttributeValueEscaper(v);

    final String escaped;
    escaped = escaper.escape();

    return record0(attributeName, false, escaped);
  }

  public final AttributePointer record(String name, String value) {
    final AttributeName attributeName;
    attributeName = AttributeName.getOrCreate(name);

    return record(attributeName, value);
  }

  public final AttributePointer record(AttributeName attributeName, JsAction action) {
    final JsAction a;
    a = Objects.requireNonNull(action, "action == null");

    return record0(attributeName, true, a);
  }

  public final AttributePointer record(String name, JsAction action) {
    final AttributeName attributeName;
    attributeName = AttributeName.getOrCreate(name);

    final JsAction a;
    a = Objects.requireNonNull(action, "action == null");

    return record0(attributeName, true, a);
  }

  private AttributePointer record0(AttributeName attributeName, boolean jsaction, Object value) {
    final int nameIndex;
    nameIndex = attributeName.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int valueIndex;
    valueIndex = sink.addObject(value);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> record8(jsaction, nameIndex, valueIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    };

    return new AttributePointer(startIndex);
  }

  private int record8(boolean jsaction, final int nameIndex, final int valueIndex) {
    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    return switch (valueSize) {
      case BIT8 -> record88(jsaction, nameIndex, valueIndex);

      case BIT16 -> record816(jsaction, nameIndex, valueIndex);

      case BIT24 -> record824(jsaction, nameIndex, valueIndex);
    };
  }

  private int record88(boolean jsaction, int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(jsaction ? HtmlBytes.JSACTION88 : HtmlBytes.ATTRIBUTE88);

    sink.addInt8(nameIndex);

    sink.addInt8(valueIndex);

    return startIndex;
  }

  private int record816(boolean jsaction, int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(jsaction ? HtmlBytes.JSACTION816 : HtmlBytes.ATTRIBUTE816);

    sink.addInt8(nameIndex);

    sink.addInt16(valueIndex);

    return startIndex;
  }

  private int record824(boolean jsaction, int nameIndex, int valueIndex) {
    final int startIndex;
    startIndex = sink.addByte(jsaction ? HtmlBytes.JSACTION824 : HtmlBytes.ATTRIBUTE824);

    sink.addInt8(nameIndex);

    sink.addInt24(valueIndex);

    return startIndex;
  }

}
