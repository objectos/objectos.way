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

import objectox.html.attr.AbstractAttribute;
import objectox.html.attr.AttributeName;
import objectox.html.attr.RegularAttribute;

/// Represents a non-boolean HTML attribute.
public sealed interface Attribute
    permits
    AbstractAttribute {

  /// Returns a new attribute object with the specified name and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return a newly created object representing a non-boolean HTML attribute
  static Attribute of(String name, String value) {
    final AttributeName attributeName;
    attributeName = AttributeName.getOrCreate(name);

    return RegularAttribute.of(attributeName, value);
  }

  /// Returns the attribute name.
  ///
  /// @return the attribute name
  String name();

  /// Returns the attribute value.
  ///
  /// @return the attribute value
  String value();

}