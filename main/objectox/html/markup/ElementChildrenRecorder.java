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

import objectos.html.ClassName;
import objectos.html.Component;
import objectos.html.Id;
import objectos.html.markup.BooleanAttribute;
import objectos.html.markup.NoOpMarkup;
import objectox.html.IntSize;

final class ElementChildrenRecorder {

  private final FragmentChildRecorder fragmentChildRecorder;

  private final ValueRecorder valueRecorder;

  ElementChildrenRecorder(
      FragmentChildRecorder fragmentChildRecorder,

      ValueRecorder valueRecorder
  ) {
    this.fragmentChildRecorder = fragmentChildRecorder;

    this.valueRecorder = valueRecorder;
  }

  public final int record(IntSize size, ElementValue... values) {
    int sum = 0;

    for (ElementValue value : values) {
      sum += record0(size, value);
    }

    return sum;
  }

  private int record0(IntSize size, ElementValue value) {
    return switch (value) {
      case AttributePointer _ -> 0;

      case BooleanAttribute _ -> 0;

      case ClassName _ -> 0;

      case Component _ -> 0;

      case ElementPointer(int v) -> valueRecorder.record(size, v);

      case FragmentPointer(int v) -> fragmentChildRecorder.record(size, v);

      case Id _ -> 0;

      case NoOpMarkup _ -> 0;

      case RawPointer(int v) -> valueRecorder.record(size, v);

      case TestableMarkupPointer(int v) -> valueRecorder.record(size, v);

      case TextPointer(int v) -> valueRecorder.record(size, v);
    };
  }

}
