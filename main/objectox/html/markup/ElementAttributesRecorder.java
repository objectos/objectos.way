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

import objectos.html.markup.ElementMarkup;
import objectos.html.markup.NoOpMarkup;
import objectos.html.markup.RawMarkup;
import objectos.html.markup.TextMarkup;
import objectox.html.AmbiguousName;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class ElementAttributesRecorder {

  private final HtmlSink sink;

  ElementAttributesRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final int record(ElementName name, IntSize size, ElementValue... values) {
    int sum = 0;

    for (ElementValue value : values) {
      sum += record0(name, size, value);
    }

    return sum;
  }

  private int record0(ElementName elementName, IntSize size, ElementValue value) {
    return switch (value) {
      case AmbiguousPointer(int v) -> ambiguous(elementName, size, v);

      case AttributePointer(int v) -> add(size, v);

      case ElementMarkup _ -> 0;

      case FragmentPointer(int v) -> fragment(elementName, size, v);

      case NoOpMarkup _ -> 0;

      case RawMarkup _ -> 0;

      case TextMarkup _ -> 0;
    };
  }

  private int add(IntSize size, int v) {
    sink.consume(v);

    sink.addInt(size, v);

    return 1;
  }

  private int ambiguous(ElementName elementName, IntSize size, int v) {
    final byte b;
    b = sink.getByte(v);

    return switch (b) {
      case HtmlBytes.AMBIGUOUS88,
           HtmlBytes.AMBIGUOUS816,
           HtmlBytes.AMBIGUOUS824 -> {
        final int index;
        index = sink.getInt8(v + 1);

        final AmbiguousName ambiguous;
        ambiguous = AmbiguousName.get(index);

        if (ambiguous.isAttributeOf(elementName)) {
          final byte marked;
          marked = HtmlBytes.attr(b);

          sink.setByte(v, marked);

          sink.addInt(size, v);

          yield 1;
        } else {
          yield 0;
        }
      }

      default -> throw new IllegalStateException();
    };
  }

  private int fragment(ElementName elementName, IntSize size, int v) {
    throw new UnsupportedOperationException("Implement me");
  }

}
