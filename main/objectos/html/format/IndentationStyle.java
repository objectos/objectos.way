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

import objectox.html.format.IndentationOption;

/// Configures when to emit indentation during formatting. Indentation is
/// emitted *before* the event.
public sealed interface IndentationStyle
    permits
    IndentationOption {

  /// Returns the style to never apply an indentation on an event.
  ///
  /// @return the style to never apply an indentation on an event
  static IndentationStyle never() {
    return IndentationOption.NEVER;
  }

  /// Returns a style that emits an indentation at the start of a line. For the
  /// purposes of indentation, the start of a line occurs when:
  ///
  /// - the output is currently empty.
  /// - the output currently ends in a `\n` (line feed) or `\r` (carriage return)
  ///   character
  ///
  /// @return a style that emits an indentation at the start of a line
  static IndentationStyle startOfLine() {
    return IndentationOption.START_OF_LINE;
  }

}
