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

import objectox.html.format.NewLineOption;

/// Configures when to emit new lines during formatting. New lines are typically
/// emitted *after* the event.
public sealed interface NewLineStyle
    permits
    NewLineOption {

  /// Returns a style that always emits a new line on an event.
  ///
  /// @return a style that always emits a new line on an event
  static NewLineStyle always() {
    return NewLineOption.ALWAYS;
  }

  /// Returns a style that emits a new line if the event is considered to be
  /// "not empty". The exact definition of "not empty" is specific to each event.
  ///
  /// @return a style that emits a new line if the event is "not empty"
  static NewLineStyle ifNotEmpty() {
    return NewLineOption.IF_NOT_EMPTY;
  }

  /// Returns a style that never emits a new line on an event.
  ///
  /// @return a style that never emits a new line on an event
  static NewLineStyle never() {
    return NewLineOption.NEVER;
  }

}
