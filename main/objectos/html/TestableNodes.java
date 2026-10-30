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
package objectos.html;

import objectos.html.markup.TestableMarkup;
import objectox.html.markup.MarkupTestableNodes;

/// Declares the testable nodes of an HTML document.
///
/// Testable nodes contribute to the output of the
/// [Component#toTestableText()] method. During regular HTML formatting,
/// testable nodes are rendered as text nodes with the specified value.
public sealed interface TestableNodes permits MarkupTestableNodes {

  /// Encodes a testable cell node with the specified width and value.
  ///
  /// The value is right-padded with spaces to the specified width.
  ///
  /// @param width the fixed width of the cell, must be greater than zero
  /// @param value the testable node value, must not be longer than the width
  ///
  /// @return an object representing the encoded testable node
  ///
  /// @throws IllegalArgumentException if the width is not greater than zero
  /// or if the value is longer than the width
  /// @throws NullPointerException if the value is null
  TestableMarkup cell(int width, String value);

  /// Encodes a testable heading 1 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h1(String value);

  /// Encodes a testable heading 2 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h2(String value);

  /// Encodes a testable heading 3 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h3(String value);

  /// Encodes a testable heading 4 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h4(String value);

  /// Encodes a testable heading 5 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h5(String value);

  /// Encodes a testable heading 6 node with the specified value.
  ///
  /// @param value the testable node value
  ///
  /// @return an object representing the encoded testable node
  TestableMarkup h6(String value);

}
