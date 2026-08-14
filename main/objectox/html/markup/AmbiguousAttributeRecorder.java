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

import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class AmbiguousAttributeRecorder {

  private final HtmlSink sink;

  AmbiguousAttributeRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final int record(ElementName elementName, IntSize size, int value) {
    final byte b;
    b = sink.getByte(value);

    return switch (b) {
      case HtmlBytes.AMBIGUOUS88,
           HtmlBytes.AMBIGUOUS816,
           HtmlBytes.AMBIGUOUS824 -> record0(elementName, size, value, b);

      default -> 0;
    };
  }

  private int record0(ElementName elementName, IntSize size, int value, byte b) {
    final int index;
    index = sink.getInt8(value + 1);

    final AmbiguousName ambiguous;
    ambiguous = AmbiguousName.get(index);

    if (ambiguous.isAttributeOf(elementName)) {
      final byte marked;
      marked = HtmlBytes.attr(b);

      sink.setByte(value, marked);

      sink.addInt(size, value);

      return 1;
    } else {
      return 0;
    }
  }

}
