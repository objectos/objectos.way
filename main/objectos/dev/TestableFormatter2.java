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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import objectox.dev.TestableFormatter2Pojo;

/// Formats a string representation that is suitable for a testable object.
public sealed interface TestableFormatter2 permits TestableFormatter2Pojo {

  /// Formats the specified string a level 1 heading.
  ///
  /// @param value the heading text
  void h1(String value);

  /// Formats the specified string a level 2 heading.
  ///
  /// @param value the heading text
  void h2(String value);

  /// Formats a list of items.
  ///
  /// @param format allows for defining the format of the list
  void list(Consumer<? super TestableListFormatter> format);

  /// Formats the specified elements as a list of items.
  ///
  /// @param <T> the element type
  /// @param elements the elements to be formatted
  /// @param format allows for defining the format to be applied to each element
  <T> void list(Iterable<? extends T> elements, BiConsumer<? super TestableListFormatter, T> format);

  /// Formats the specified elements as a table.
  ///
  /// @param <T> the element type
  /// @param elements the elements to be formatted
  /// @param format allows for defining the format to be applied to each element
  <T> void table(Iterable<? extends T> elements, BiConsumer<? super TestableRowFormatter, T> format);

}
