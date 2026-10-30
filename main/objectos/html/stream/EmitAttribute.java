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

import objectox.html.stream.EmitAttributeEvent;

/// This event renders a non-boolean attribute in an element start tag.
public sealed interface EmitAttribute
    extends Event
    permits EmitAttributeEvent {

  /// Returns the attribute name.
  ///
  /// @return the attribute name
  String name();

  /// Returns the attribute value.
  ///
  /// @return the attribute value
  String value();

}
