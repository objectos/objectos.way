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

import objectos.html.markup.NoOpMarkup;
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class ElementChildrenRecorder {

  private final HtmlSink sink;

  ElementChildrenRecorder(HtmlSink sink) {
    this.sink = sink;
  }

  public final int record(ElementName elementName, IntSize size, ElementValue... values) {
    int sum = 0;

    for (ElementValue value : values) {
      sum += record0(elementName, size, value);
    }

    return sum;
  }

  private int record0(ElementName elementName, IntSize size, ElementValue value) {
    return switch (value) {
      case AmbiguousPointer(int v) -> ambiguous(elementName, size, v);

      case AttributePointer _ -> 0;

      case ElementPointer(int v) -> add(size, v);

      case FragmentPointer(int v) -> fragment(elementName, size, v);

      case NoOpMarkup _ -> 0;

      case RawPointer(int v) -> add(size, v);

      case TextPointer(int v) -> add(size, v);
    };
  }

  private int add(IntSize size, int v) {
    sink.addInt(size, v);

    return 1;
  }

  private int ambiguous(ElementName elementName, IntSize size, int v) {
    throw new UnsupportedOperationException("Implement me");
  }

  private int fragment(ElementName name, IntSize size, int v) {
    throw new UnsupportedOperationException("Implement me");
  }

}
