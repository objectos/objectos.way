/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

final class TestableFormatter implements Testable.Formatter {

  private final StringBuilder out = new StringBuilder();

  private final String cellSeparator;

  private final char fieldSeparator = ':';

  private final String nullValue = "null";

  private boolean firstCell;

  private int padding;

  TestableFormatter(String cellSeparator) {
    this.cellSeparator = cellSeparator;

    firstCell = true;
  }

  @Override
  public final void heading1(String value) {
    heading(value, "# ");
  }

  @Override
  public final void heading2(String value) {
    heading(value, "## ");
  }

  @Override
  public final void heading3(String value) {
    heading(value, "### ");
  }

  @Override
  public final void heading4(String value) {
    heading(value, "#### ");
  }

  @Override
  public final void heading5(String value) {
    heading(value, "##### ");
  }

  @Override
  public final void heading6(String value) {
    heading(value, "###### ");
  }

  private void heading(String value, String prefix) {
    headingNewLineIfRequired();

    out.append(prefix);
    out.append(value);

    newLine();
    newLine();
  }

  private void headingNewLineIfRequired() {
    String sep;
    sep = System.lineSeparator();

    int sepLength;
    sepLength = sep.length();

    int endIndex;
    endIndex = out.length();

    int startIndex;
    startIndex = endIndex - sepLength;

    if (startIndex < 0) {
      return;
    }

    String suffix;
    suffix = out.substring(startIndex, endIndex);

    if (!suffix.equals(sep)) {
      out.append(sep);
      out.append(sep);

      return;
    }

    endIndex = startIndex;

    startIndex = endIndex - sepLength;

    if (startIndex < 0) {
      return;
    }

    suffix = out.substring(startIndex, endIndex);

    if (!suffix.equals(sep)) {
      out.append(sep);
    }
  }

  @Override
  public final void field(String name, String value) {
    fieldName(name);
    fieldValue(value);
  }

  @Override
  public final void fieldName(String name) {
    Objects.requireNonNull(name, "name == null");

    out.append(name);
    out.append(fieldSeparator);
  }

  @Override
  public final void fieldValue(String value) {
    if (!"".equals(value)) {

      final int length;
      length = out.length();

      if (length > 0 && out.charAt(length - 1) == fieldSeparator) {
        out.append(' ');
      }

      out.append(value);

    }

    out.append(System.lineSeparator());
  }

  private static final DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private static final DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @Override
  public final void cell(boolean value) {
    cellSeparatorIfRequired();

    if (value) {
      out.append("true");

      padding = 1;
    } else {
      out.append("false");
    }
  }

  @Override
  public final void cell(int value, int width) {
    cell((long) value, width);
  }

  public final void cell(long value, int width) {
    cellSeparatorIfRequired();

    if (value == 0) {
      writeZeros(width);

      return;
    }

    if (value < 0) {
      out.append('-');

      value = -value;

      width--;
    }

    long max;
    max = (long) Math.pow(10, width);

    if (value >= max) {
      throw new IllegalArgumentException("Value must have at most " + width + " digits");
    }

    long current;
    current = value;

    long divisor;
    divisor = max / 10;

    for (int i = width; i > 0; i--) {
      long result;
      result = current / divisor;

      char c;
      c = (char) (result + 48);

      out.append(c);

      current = current % divisor;

      divisor = divisor / 10;
    }
  }

  private void writeZeros(int width) {
    if (width == 0) {
      return;
    }

    if (width < 0) {
      throw new IllegalArgumentException("Width must not be negative");
    }

    for (int i = 0; i < width; i++) {
      out.append('0');
    }
  }

  @Override
  public final void cell(LocalDate value) {
    cellSeparatorIfRequired();

    if (value != null) {
      LOCAL_DATE.formatTo(value, out);
    } else {
      out.append("----------");
    }
  }

  @Override
  public final void cell(LocalDateTime value) {
    cellSeparatorIfRequired();

    if (value != null) {
      LOCAL_DATE_TIME.formatTo(value, out);
    } else {
      out.append("---------- --------");
    }
  }

  @Override
  public final void cell(String value, int length) {
    cellSeparatorIfRequired();

    if (value == null) {
      value = nullValue;
    }

    int actualLength;
    actualLength = value.length();

    padding = length - actualLength;

    if (padding < 0) {
      throw new IllegalArgumentException("String length should not exceed " + length + " characters");
    }

    out.append(value);
  }

  private void cellSeparatorIfRequired() {
    if (firstCell) {
      firstCell = false;

      return;
    }

    if (padding > 0) {
      cellPadding(padding);

      padding = 0;
    }

    out.append(' ');
    out.append(cellSeparator);
    out.append(' ');
  }

  private void cellPadding(int length) {
    if (length == 0) {
      return;
    }

    if (length < 0) {
      throw new IllegalArgumentException("Length must not be negative");
    }

    for (int i = 0; i < length; i++) {
      out.append(' ');
    }
  }

  @Override
  public final void clear() {
    out.setLength(0);
  }

  @Override
  public final void newLine() {
    out.append(System.lineSeparator());

    firstCell = true;

    padding = 0;
  }

  @Override
  public final void row(Object... values) {
    int idx;
    idx = 0;

    for (;;) {
      Object value;
      value = values[idx++];

      switch (value) {
        case Boolean b -> cell(b.booleanValue());

        case Integer i -> cell(i.intValue(), intValue(values[idx++]));

        case LocalDate ld -> cell(ld);

        case LocalDateTime ldt -> cell(ldt);

        case String s -> cell(s, intValue(values[idx++]));

        default -> throw new IllegalArgumentException("Unsupported type=" + value.getClass());
      }

      if (idx == values.length) {
        newLine();

        break;
      }
    }
  }

  private int intValue(Object o) {
    if (o instanceof Integer i) {
      return i.intValue();
    } else {
      throw new IllegalArgumentException("Expected an int value but found " + o.getClass());
    }
  }

  @Override
  public final String toString() {
    return out.toString();
  }

}
