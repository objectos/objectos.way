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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public final class TestableRow {

  private final String cellSeparator = " | ";

  private int index;

  private final Object[] values;

  public TestableRow(Object... values) {
    this.values = Objects.requireNonNull(values, "values == null");
  }

  public final String format() {
    final StringBuilder out;
    out = new StringBuilder();

    while (hasNext()) {
      if (index != 0) {
        out.append(cellSeparator);
      }

      final Object next;
      next = next();

      final String formatted;
      formatted = switch (next) {
        case Boolean b -> cellString(b.toString(), 5);

        case Integer i -> cellLong(i.longValue());

        case LocalDate value -> cellLocalDate(value);

        case LocalDateTime value -> cellLocalDateTime(value);

        case Long l -> cellLong(l.longValue());

        case String value -> cellString(value);

        default -> throw new IllegalArgumentException("Unsupported type=" + next.getClass());
      };

      out.append(formatted);
    }

    return out.toString();
  }

  private String cellLocalDate(LocalDate value) {
    final TestableCellLocalDate cell;
    cell = new TestableCellLocalDate(value);

    return cell.format();
  }

  private String cellLocalDateTime(LocalDateTime value) {
    final TestableCellLocalDateTime cell;
    cell = new TestableCellLocalDateTime(value);

    return cell.format();
  }

  private String cellLong(long value) {
    final int width;
    width = width();

    final TestableCellLong cell;
    cell = new TestableCellLong(value, width);

    return cell.format();
  }

  private String cellString(String value) {
    final int width;
    width = width();

    return cellString(value, width);
  }

  private String cellString(String value, int width) {
    final TestableCellString cell;
    cell = new TestableCellString(value, width);

    final boolean last;
    last = !hasNext();

    return cell.format(last);
  }

  private boolean hasNext() {
    return index < values.length;
  }

  private Object next() {
    return values[index++];
  }

  private int width() {
    if (!hasNext()) {
      throw noColumnWidth(1);
    }

    final Object next;
    next = next();

    if (!(next instanceof Integer width)) {
      throw noColumnWidth(2);
    }

    return width.intValue();
  }

  private IllegalArgumentException noColumnWidth(int offset) {
    final Object previous;
    previous = values[index - offset];

    final String msg;
    msg = "No column width after '%s'".formatted(previous);

    throw new IllegalArgumentException(msg);
  }

}
