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
package objectos.html.format;

import objectox.html.format.BaseFormatter;
import objectox.html.format.StandardFormatter;
import objectox.html.format.TestableFormatter;

/// Formats the string representation of a compiled Objectos HTML document.
public sealed interface Formatter permits BaseFormatter {

  /// Returns the standard formatter instance.
  ///
  /// @return the standard formatter instance
  static Formatter standard() {
    return new StandardFormatter();
  }

  /// Returns a new testable formatter instance.
  ///
  /// @return a new testable formatter instance
  static Formatter testable() {
    return new TestableFormatter();
  }

}
