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
import objectos.html.BooleanAttribute;
import objectox.html.attr.AttributeName;
import objectox.html.attr.BooleanAttributePojo;

final class BooleanAttributeRecorder {

  private final HtmlSink sink;

  BooleanAttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final AttributePointer record(AttributeName name) {
    final int nameIndex;
    nameIndex = name.index();

    if (nameIndex > HtmlSink.MAX_INT8) {
      final String msg;
      msg = "Attribute name index too large: %d".formatted(nameIndex);

      throw new IllegalArgumentException(msg);
    }

    final int startIndex;
    startIndex = boolean8(nameIndex);

    return new AttributePointer(startIndex);
  }

  public final AttributePointer record(BooleanAttribute value) {
    final BooleanAttribute a;
    a = Objects.requireNonNull(value, "value == null");

    final BooleanAttributePojo impl;
    impl = (BooleanAttributePojo) a;

    final AttributeName attributeName;
    attributeName = impl.attributeName();

    return record(attributeName);
  }

  public final AttributePointer record(String name) {
    final AttributeName attributeName;
    attributeName = AttributeName.getOrCreate(name);

    return record(attributeName);
  }

  private int boolean8(int nameIndex) {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.BOOLEAN8);

    sink.addInt8(nameIndex);

    return startIndex;
  }

}
