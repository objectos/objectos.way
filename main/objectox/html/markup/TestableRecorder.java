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
import objectos.html.TestableName;
import objectox.html.IntSize;

final class TestableRecorder {

  private final HtmlSink sink;

  TestableRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final TestableMarkupPointer record(TestableName name, String value) {
    final int startIndex;
    startIndex = record0(name, value, HtmlBytes.TEST8, HtmlBytes.TEST16, HtmlBytes.TEST24);

    return new TestableMarkupPointer(startIndex);
  }

  public final TestableOnlyMarkupPointer recordOnly(TestableName name, String value) {
    final int startIndex;
    startIndex = record0(name, value, HtmlBytes.TESTONLY8, HtmlBytes.TESTONLY16, HtmlBytes.TESTONLY24);

    return new TestableOnlyMarkupPointer(startIndex);
  }

  private int record0(TestableName name, String value, byte proto8, byte proto16, byte proto24) {
    final TestableName n;
    n = Objects.requireNonNull(name, "name == null");

    final String v;
    v = Objects.requireNonNull(value, "value == null");

    sink.addObject(n);

    final int valueIndex;
    valueIndex = sink.addObject(v);

    final IntSize size;
    size = IntSize.of(valueIndex);

    final byte proto;
    proto = switch (size) {
      case BIT8 -> proto8;

      case BIT16 -> proto16;

      case BIT24 -> proto24;
    };

    final int startIndex;
    startIndex = sink.addByte(proto);

    sink.addInt(size, valueIndex);

    return startIndex;
  }

}
