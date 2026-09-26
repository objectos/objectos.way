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
    size = IntSize.of(Math.max(width, valueIndex));

    final byte proto;
    proto = switch (size) {
      case BIT8 -> HtmlBytes.TCELL8;

      case BIT16 -> HtmlBytes.TCELL16;

      case BIT24 -> HtmlBytes.TCELL24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, width);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer heading(int level, String value) {
    if (level < 1 || level > 6) {
      final String msg;
      msg = "level must be between 1 and 6";

      throw new IllegalArgumentException(msg);
    }

    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.of(valueIndex);

    final byte proto;
    proto = switch (size) {
      case BIT8 -> HtmlBytes.THEADING8;

      case BIT16 -> HtmlBytes.THEADING16;

      case BIT24 -> HtmlBytes.THEADING24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addByte((byte) level);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer newLine() {
    final int startIndex;
    startIndex = sink.addByte(HtmlBytes.TNEWLINE);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer property(String key, String value) {
    final String k;
    k = Objects.requireNonNull(key, "key == null");

    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int keyIndex;
    keyIndex = sink.addObject(k);

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.of(Math.max(keyIndex, valueIndex));

    final byte proto;
    proto = switch (size) {
      case BIT8 -> HtmlBytes.TPROP8;

      case BIT16 -> HtmlBytes.TPROP16;

      case BIT24 -> HtmlBytes.TPROP24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, keyIndex);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer propKey(String key) {
    final String k;
    k = Objects.requireNonNull(key, "key == null");

    final int keyIndex;
    keyIndex = sink.addObject(k);

    final IntSize size;
    size = IntSize.of(keyIndex);

    final byte proto;
    proto = switch (size) {
      case BIT8 -> HtmlBytes.TPROPKEY8;

      case BIT16 -> HtmlBytes.TPROPKEY16;

      case BIT24 -> HtmlBytes.TPROPKEY24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, keyIndex);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableMarkupPointer propValue(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.of(valueIndex);

    final byte proto;
    proto = switch (size) {
      case BIT8 -> HtmlBytes.TPROPVAL8;

      case BIT16 -> HtmlBytes.TPROPVAL16;

      case BIT24 -> HtmlBytes.TPROPVAL24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, valueIndex);

    return new TestableMarkupPointer(startIndex);
  }

}
