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

import objectos.html.Attribute;
import objectos.html.AttributeName;
import objectox.html.IntSize;

final class AttributeRecorder {

  private final HtmlSink sink;

  AttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final void record(Attribute attribute) {
    final AttributeName name;
    name = attribute.attrName();

    if (name == null) {
      final String msg;
      msg = "Attribute provided a null name";

      throw new NullPointerException(msg);
    }

    final String value;
    value = attribute.attrValue();

    if (value == null) {
      final String msg;
      msg = "Attribute provided a null value";

      throw new NullPointerException(msg);
    }

    final Object escaped;
    escaped = escapeIfNecessary(value);

    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int valueIndex;
    valueIndex = sink.addObject(escaped);

    switch (nameSize) {
      case BIT8 -> record8(nameIndex, valueIndex);

      case BIT16, BIT24 -> throw new UnsupportedOperationException();
    }
  }

  private Object escapeIfNecessary(Object v) {
    if (!(v instanceof String s)) {
      return v;
    }

    final AttributeValueEscaper escaper;
    escaper = new AttributeValueEscaper(s);

    return escaper.escape();
  }

  private void record8(final int nameIndex, final int valueIndex) {
    final IntSize valueSize;
    valueSize = IntSize.of(valueIndex);

    switch (valueSize) {
      case BIT8 -> record88(nameIndex, valueIndex);

      case BIT16 -> record816(nameIndex, valueIndex);

      case BIT24 -> record824(nameIndex, valueIndex);
    }
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
