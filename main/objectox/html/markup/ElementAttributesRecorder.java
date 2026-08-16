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
import objectox.html.IntSize;
import objectox.html.elem.ElementName;

final class ElementAttributesRecorder {

  private final AmbiguousAttributeRecorder ambiguousAttributeRecorder;

  private final FragmentAttributeRecorder fragmentAttributeRecorder;

  private final ValueRecorder valueRecorder;

  ElementAttributesRecorder(
      AmbiguousAttributeRecorder ambiguousAttributeRecorder,

      FragmentAttributeRecorder fragmentAttributeRecorder,

      ValueRecorder valueRecorder
  ) {
    this.ambiguousAttributeRecorder = ambiguousAttributeRecorder;

    this.fragmentAttributeRecorder = fragmentAttributeRecorder;

    this.valueRecorder = valueRecorder;
  }

  public final int record(ElementName name, IntSize size, ElementValue... values) {
    int sum = 0;

    for (int idx = 0; idx < values.length; idx++) {
      final ElementValue value;
      value = values[idx];

      if (value == null) {
        final String msg;
        msg = "values[%d] == null".formatted(idx);

        throw new NullPointerException(msg);
      }

      sum += record0(name, size, value);
    }

    return sum;
  }

  private int record0(ElementName elementName, IntSize size, ElementValue value) {
    return switch (value) {
      case AmbiguousPointer(int v) -> ambiguousAttributeRecorder.record(elementName, size, v);

      case AttributePointer(int v) -> valueRecorder.record(size, v);

      case ElementMarkup _ -> 0;

      case FragmentPointer(int v) -> fragmentAttributeRecorder.record(elementName, size, v);

      case NoOpMarkup _ -> 0;

      case RawMarkup _ -> 0;

      case TextMarkup _ -> 0;
    };
  }

}
