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

import java.util.Objects;
import objectos.html.markup.RegularElementContent;
import objectos.html.markup.VoidElementContent;
import objectox.html.markup.BooleanAttributePojo;

/// Represents a boolean HTML attribute.
public sealed interface BooleanAttribute
    extends
    RegularElementContent,
    VoidElementContent
    permits
    BooleanAttributePojo {

  /// Creates an object representing a boolean HTML attribute with the specified
  /// name.
  ///
  /// @param name the boolean attribute name
  ///
  /// @return a newly created object representing a boolean HTML attribute
  static BooleanAttribute of(AttributeName name) {
    return new BooleanAttributePojo(
        Objects.requireNonNull(name, "name == null")
    );
  }

  /// Returns the attribute name.
  ///
  /// @return the attribute name
  AttributeName name();

}
