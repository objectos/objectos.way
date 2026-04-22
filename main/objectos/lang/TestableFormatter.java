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
package objectos.lang;

import java.time.LocalDate;
import java.time.LocalDateTime;

/// Formats the string representation of a testable component.
public sealed interface TestableFormatter permits TestableFormatter0 {

  /// Creates a new formatter with the default settings.
  ///
  /// @return a new formatter with the default settings.
  static TestableFormatter create() {
    return new TestableFormatter0("|");
  }

  /// Clears the content of this formatter, resetting it to an empty state.
  void clear();

  /// Formats the specified boolean value as a table cell value.
  ///
  /// @param value the boolean value
  void cell(boolean value);

  /// Formats the specified integer value as a table cell value with the
  /// specified fixed width.
  ///
  /// @param value the boolean value
  /// @param width the fixed width of the cell.
  void cell(int value, int width);

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

  /// Formats the specified string a level 1 heading.
  ///
  /// @param value the heading text
  void heading1(String value);

  /// Formats the specified string a level 2 heading.
  ///
  /// @param value the heading text
  void heading2(String value);

  /// Formats the specified string a level 3 heading.
  ///
  /// @param value the heading text
  void heading3(String value);

  /// Formats the specified string a level 4 heading.
  ///
  /// @param value the heading text
  void heading4(String value);

  /// Formats the specified string a level 5 heading.
  ///
  /// @param value the heading text
  void heading5(String value);

  /// Formats the specified string a level 6 heading.
  ///
  /// @param value the heading text
  void heading6(String value);

  /// Formats the specified name and value as a field.
  ///
  /// Using the default field separator, the following invocation:
  ///
  /// ```java
  /// writer.field("name", "Albert");
  /// writer.field("age", "37");
  /// ```
  ///
  /// Produces the following output:
  ///
  /// ```
  /// name: Albert
  /// age: 37
  /// ```
  ///
  /// @param name the field name
  /// @param value the field value
  void field(String name, String value);

  /// Formats the specified name as a field name. More specifically, writes out
  /// the specified name immediately followed by the field separator.
  ///
  /// @param name the field name
  void fieldName(String name);

  /// Formats the specified value as a field value. More specifically, writes
  /// out the value immediately followed by a line separator.
  ///
  /// @param value the field value
  void fieldValue(String value);

  /// Formats a line separator.
  void newLine();

  /// Adds multiple columns to a single row. Columns are specified as
  /// alternating value and length pairs.
  ///
  /// @param values an array where even indices represent column values (strings)
  ///        and odd indices represent their respective lengths (integers).
  ///
  /// @throws IllegalArgumentException if the values array is not structured as
  ///         alternating string and integer pairs or contains unsupported types.
  void row(Object... values);

}