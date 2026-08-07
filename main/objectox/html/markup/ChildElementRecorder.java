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

final class ChildElementRecorder {

  private final HtmlSink sink;

  ChildElementRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final void record(ElementPointer child) {
    final int value;
    value = child.value();

    final IntSize size;
    size = IntSize.of(value);

    switch (size) {
      case BIT8 -> record8(value);

      case BIT16 -> record16(value);

      case BIT24 -> record24(value);
    }
  }

  private void record8(int value) {
    sink.addByte(HtmlBytes.ELEM8);

    sink.addInt8(value);
  }

  private void record16(int value) {
    sink.addByte(HtmlBytes.ELEM16);

    sink.addInt16(value);
  }

  private void record24(int value) {
    sink.addByte(HtmlBytes.ELEM24);

    sink.addInt24(value);
  }

}
