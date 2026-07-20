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
import objectos.html.rec.Instruction;
import objectox.html.attr.AttributeObjectPojo;

/// An object representing an instruction to render an HTML attribute. These
/// instructions may be reused, unlike the instructions represented by methods of
/// the `Markup` or `Template` classes.
public non-sealed interface AttributeObject extends Instruction.AsObject, Instruction.OfVoid {

  /// Creates an object representing an HTML boolean attribute with the
  /// specified name.
  ///
  /// @param name the boolean attribute name
  ///
  /// @return a newly created object representing an HTML boolean attribute
  static AttributeObject of(AttributeName name) {
    return new AttributeObjectPojo(
        Objects.requireNonNull(name, "name == null"),
        null
    );
  }

  /// Creates an object representing a HTML attribute with the specified name
  /// and value.
  ///
  /// @param name the attribute name
  /// @param value the attribute value
  ///
  /// @return a newly created object representing an HTML attribute
  static AttributeObject of(AttributeName name, String value) {
    return new AttributeObjectPojo(
        Objects.requireNonNull(name, "name == null"),
        Objects.requireNonNull(value, "value == null")
    );
  }

  /// The HTML attribute name.
  ///
  /// @return the HTML attribute name
  AttributeName attrName();

  /// The HTML attribute value, or `null` if this object represents a boolean
  /// HTML attribute.
  ///
  /// @return the HTML attribute value, or `null`
  String attrValue();

}