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
import objectox.html.IntSize;

final class TestableRecorder {

  private final HtmlSink sink;

  TestableRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final TestableMarkupPointer cell(int width, String value) {
    if (width <= 0) {
      final String msg;
      msg = "width must be > 0";

      throw new IllegalArgumentException(msg);
    }

    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.max(width, valueIndex);

    final byte proto;
    proto = size.toTestableCellByte();

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, width);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer heading(int level, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.of(valueIndex);

    final byte proto;
    proto = size.toTestableHeadingByte();

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addByte((byte) level);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

}
