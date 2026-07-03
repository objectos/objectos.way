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

public final class TestableCellLong {

  private final long value;

  private final int width;

  TestableCellLong(long value, int width) {
    if (width <= 0) {
      throw new IllegalArgumentException("Column width must be greater than zero");
    }

    this.value = value;

    this.width = width;
  }

  @Override
  public final String toString() {
    if (value == 0) {
      return "0".repeat(width);
    }

    final StringBuilder out;
    out = new StringBuilder();

    long v = value;

    int d = width;

    if (v < 0) {
      out.append('-');

      v = -v;

      d--;
    }

    final long max;
    max = (long) Math.pow(10, d);

    if (v >= max) {
      final String msg;
      msg = "Formatted value length will exceed the column width of %d".formatted(width);

      throw new IllegalArgumentException(msg);
    }

    long divisor;
    divisor = max / 10;

    while (d > 0) {
      long result;
      result = v / divisor;

      char c;
      c = (char) (result + 48);

      out.append(c);

      v = v % divisor;

      divisor = divisor / 10;

      d--;
    }

    return out.toString();
  }

}
