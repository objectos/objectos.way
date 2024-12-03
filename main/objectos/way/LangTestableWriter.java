/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

final class LangTestableWriter implements Lang.Testable.Writer {

  private final StringBuilder out = new StringBuilder();

  private final String cellSeparator;

  private final String nullValue = "null";

  private boolean firstCell;

  private int padding;

  LangTestableWriter(String cellSeparator) {
    this.cellSeparator = cellSeparator;

    firstCell = true;
  }

  @Override
  public final void heading(String value) {
    headingNewLineIfRequired();

    out.append("# ");
    out.append(value);

    nextRow();
    nextRow();
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
  public final void nextRow() {
    out.append(System.lineSeparator());

    firstCell = true;
  }

  @Override
  public final void row(Object... values) {
    int idx;
    idx = 0;

    for (;;) {
      Object value;
      value = values[idx++];

      switch (value) {
        case String s -> cell(s, intValue(values[idx++]));

        default -> throw new IllegalArgumentException("Unsupported type=" + value.getClass());
      }

      if (idx == values.length) {
        nextRow();

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
