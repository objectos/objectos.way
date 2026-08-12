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
import objectox.html.attr.AttributeName;
import objectox.html.attr.BooleanAttributePojo;

final class BooleanAttributeRecorder {

  private final HtmlSink sink;

  BooleanAttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributePointer record(BooleanAttributePojo attribute) {
    final AttributeName name;
    name = attribute.attributeName();

    final int nameIndex;
    nameIndex = name.index();

    final IntSize nameSize;
    nameSize = IntSize.of(nameIndex);

    final int startIndex;
    startIndex = switch (nameSize) {
      case BIT8 -> boolean8(nameIndex);

      case BIT16 -> boolean16(nameIndex);

      case BIT24 -> boolean24(nameIndex);
    };

    return new AttributePointer(startIndex);
  }

  private int boolean8(int nameIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.BOOLEAN8);

    sink.addInt8(nameIndex);

    return startIndex;
  }

  private int boolean16(int nameIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.BOOLEAN16);

    sink.addInt16(nameIndex);

    return startIndex;
  }

  private int boolean24(int nameIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.BOOLEAN24);

    sink.addInt24(nameIndex);

    return startIndex;
  }

}
