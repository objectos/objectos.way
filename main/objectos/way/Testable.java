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

/**
 * The <strong>Objectos Testable</strong> main interface. It represents an
 * object that can create a string representation suitable for testing.
 */
public interface Testable {

  /**
   * Formats the string representation of a testable component.
   */
  public sealed interface Formatter permits TestableFormatter {

    /**
     * Creates a new formatter with the default settings.
     *
     * @return a new formatter with the default settings.
     */
    static Formatter create() {
      return new TestableFormatter("|");
    }

    /**
     * Clears the content of this formatter, resetting it to an empty state.
     */
    void clear();

    void heading1(String value);

    /**
     * Formats the specified name and value as a field.
     *
     * <p>
     * Using the default field separator, the following invocation:
     *
     * <pre>{@code
     * writer.field("name", "Albert");
     * writer.field("age", "37");
     * }</pre>
     *
     * <p>
     * Produces the following output:
     *
     * <pre>
     * name: Albert
     * age: 37
     * </pre>
     *
     * @param name
     *        the field name
     * @param value
     *        the field value
     */
    void field(String name, String value);

    /**
     * Formats the specified name as a field name. More specifically, writes
     * out the specified name immediately following by the field separator.
     *
     * @param name
     *        the field name
     */
    void fieldName(String name);

    /**
     * Formats the specified value as a field value. More specifically, writes
     * out the value immediately following by a line separator.
     *
     * @param value
     *        the field value
     */
    void fieldValue(String value);

    /**
     * Adds a single cell value to the current row.
     * The cell content will be right-padded with spaces to match the
     * specified length.
     *
     * @param value the string value of the cell; may be {@code null}.
     * @param length the fixed width of the cell.
     *
     * @throws IllegalArgumentException if {@code length} is negative or if
     *         {@code value.length()} exceeds {@code length}.
     */
    void cell(String value, int length);

    /**
     * Moves the writer to the next row. Any subsequent column operations will
     * append to a new line.
     */
    void nextRow();

    /**
     * Adds multiple columns to a single row. Columns are specified as
     * alternating value and length pairs.
     *
     * @param values an array where even indices represent column values
     *        (strings)
     *        and odd indices represent their respective lengths (integers).
     *
     * @throws IllegalArgumentException if the values array is not structured
     *         as
     *         alternating string and integer pairs or contains unsupported
     *         types.
     */
    void row(Object... values);

  }

  /**
   * Formats this testable instance using the specified formatter instance.
   *
   * @param formatter
   *        the formatter to use
   */
  void formatTestable(Formatter formatter);

  /**
   * Returns the formatted string representation of this testable instance.
   *
   * @return the formatted string representation of this testable instance
   */
  default String toTestableText() {
    final Formatter f;
    f = Formatter.create();

    formatTestable(f);

    return f.toString();
  }

}