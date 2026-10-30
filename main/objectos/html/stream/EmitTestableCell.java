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
package objectos.html.stream;

import objectox.html.stream.EmitTestableCellEvent;

/// This event renders a testable cell node.
public sealed interface EmitTestableCell
    extends EmitTestable
    permits EmitTestableCellEvent {

  /// Returns the fixed width of the cell.
  ///
  /// @return the fixed width of the cell
  int width();

  /// Returns the cell value.
  ///
  /// @return the cell value
  String value();

}
