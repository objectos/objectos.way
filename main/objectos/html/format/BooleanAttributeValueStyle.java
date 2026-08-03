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

import objectox.html.format.BooleanAttributeValueOption;

/// Configures the rendering of a boolean attribute value.
public sealed interface BooleanAttributeValueStyle
    permits
    BooleanAttributeValueOption {

  /// Returns a style that does not emit any value.
  ///
  /// For example, a `checked` attribute in a checkbox is rendered like the
  /// following:
  ///
  /// ```html
  /// <input type="checkbox" checked>
  /// ```
  ///
  /// @return a style that does not emit any value
  static BooleanAttributeValueStyle noValue() {
    return BooleanAttributeValueOption.NO_VALUE;
  }

  /// Returns a style that emits an empty string as the value.
  ///
  /// For example, a `checked` attribute in a checkbox is rendered like the
  /// following:
  ///
  /// ```html
  /// <input type="checkbox" checked="">
  /// ```
  ///
  /// @return a style that emits an empty string as the value
  static BooleanAttributeValueStyle emptyString() {
    return BooleanAttributeValueOption.EMPTY_STRING;
  }

  /// Returns a style that emits the attribute name as the value.
  ///
  /// For example, a `checked` attribute in a checkbox is rendered like the
  /// following:
  ///
  /// ```html
  /// <input type="checkbox" checked="checked">
  /// ```
  ///
  /// @return a style that emits the attribute name as the value
  static BooleanAttributeValueStyle attributeName() {
    return BooleanAttributeValueOption.ATTRIBUTE_NAME;
  }

}
