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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import objectos.dev.TestableRowFormatter;

public final class TestableRowFormatterPojo implements TestableRowFormatter {

  final List<String> columns = new ArrayList<>();

  @Override
  public final void cell(boolean value) {
    final String s;
    s = Boolean.toString(value);

    final TestableCellString cell;
    cell = new TestableCellString(s, 5);

    add(cell);
  }

  @Override
  public final void cell(long value, int width) {
    final TestableCellLong cell;
    cell = new TestableCellLong(value, width);

    add(cell);
  }

  @Override
  public final void cell(LocalDate value) {
    final TestableCellLocalDate cell;
    cell = new TestableCellLocalDate(value);

    add(cell);
  }

  @Override
  public final void cell(LocalDateTime value) {
    final TestableCellLocalDateTime cell;
    cell = new TestableCellLocalDateTime(value);

    add(cell);
  }

  @Override
  public final void cell(String value, int width) {
    final TestableCellString cell;
    cell = new TestableCellString(value, width);

    add(cell);
  }

  @Override
  public final String toString() {
    return columns.stream().collect(Collectors.joining(" | ", "| ", " |"));
  }

  private void add(Object o) {
    final String column;
    column = o.toString();

    columns.add(column);
  }

}
