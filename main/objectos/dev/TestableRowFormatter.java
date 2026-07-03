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
package objectos.dev;

import java.time.LocalDate;
import java.time.LocalDateTime;
import objectox.dev.TestableRowFormatterPojo;

/// Formats a row for the string representation of a testable table.
public sealed interface TestableRowFormatter permits TestableRowFormatterPojo {

  /// Formats the specified boolean value as a table cell value.
  ///
  /// @param value the boolean value
  void cell(boolean value);

  /// Formats the specified long value as a table cell value with the specified
  /// fixed width.
  ///
  /// @param value the long value
  /// @param width the fixed width of the cell.
  void cell(long value, int width);

  /// Formats the specified date as a table cell with the default `yyyy-MM-dd`
  /// format.
  ///
  /// @param value the date value of the cell; may be `null`.
  void cell(LocalDate value);

  /// Formats the specified date-time as a table cell with the default
  /// `yyyy-MM-dd HH:mm:ss` format.
  ///
  /// @param value the date-time value of the cell; may be `null`.
  void cell(LocalDateTime value);

  /// Formats the specified string as a table cell with the specified fixed
  /// width.
  ///
  /// @param value the string value of the cell; may be `null`.
  /// @param width the fixed width of the cell.
  ///
  /// @throws IllegalArgumentException if `length` is negative or if
  ///         `value.length()` exceeds `length`.
  void cell(String value, int width);

}
