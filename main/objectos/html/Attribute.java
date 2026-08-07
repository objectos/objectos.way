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

import objectos.html.markup.RegularElementValue;
import objectos.html.markup.VoidElementValue;
import objectox.html.markup.AttributePojo;

/// Represents a non-boolean HTML attribute.
public non-sealed interface Attribute
    extends
    RegularElementValue,
    VoidElementValue {

  /// Returns a new attribute object with the specified name and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return a newly created object representing a non-boolean HTML attribute
  static Attribute of(AttributeName name, String value) {
    return AttributePojo.of(name, value);
  }

  /// Returns the attribute name.
  ///
  /// @return the attribute name
  AttributeName name();

  /// Returns the attribute value.
  ///
  /// @return the attribute value
  String value();

}