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
package objectox.dev;

import java.util.Objects;

public final class TestableCellString {

  private final String value;

  private final int width;

  TestableCellString(String value, int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("Column width must be greater than zero");
    }

    this.value = value;

    this.width = width;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof TestableCellString that
        && Objects.equals(value, that.value)
        && width == that.width;
  }

  @Override
  public final String toString() {
    final String s;
    s = value != null ? value : "null";

    final int length;
    length = s.length();

    final int padding;
    padding = width - length;

    if (padding < 0) {
      final String msg;
      msg = "Formatted value length will exceed the column width of %d".formatted(width);

      throw new IllegalArgumentException(msg);
    }

    else if (padding == 0) {
      return s;
    }

    else {
      return s + " ".repeat(padding);
    }
  }

}
