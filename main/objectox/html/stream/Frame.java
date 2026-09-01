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
package objectox.html.stream;

import java.util.Objects;

final class Frame {

  ElementFrame elementFrame;

  int index;

  Frame(int index) {
    this.index = index;
  }

  Frame(int index, ElementFrame elementFrame) {
    this.index = index;

    this.elementFrame = elementFrame;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Frame that
        && Objects.equals(elementFrame, that.elementFrame)
        && index == that.index;
  }

  public final void set(ElementFrame value) {
    if (elementFrame != null) {
      final String msg;
      msg = "ElementFrame already set";

      throw new IllegalStateException(msg);
    }

    elementFrame = value;
  }

  @Override
  public final String toString() {
    final Class<? extends Frame> c;
    c = getClass();

    final String simpleName;
    simpleName = c.getSimpleName();

    return simpleName + "(" + elementFrame + ", " + index + ")";
  }

}
