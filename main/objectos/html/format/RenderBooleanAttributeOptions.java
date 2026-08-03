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

import objectos.html.stream.RenderBooleanAttribute;
import objectox.html.format.RenderBooleanAttributeFormatterBuilder;

/// Configures the formatting of the [RenderBooleanAttribute] event.
public sealed interface RenderBooleanAttributeOptions
    permits
    RenderBooleanAttributeFormatterBuilder {

  /// Sets the boolean attribute value style to the specified value. Defaults to
  /// [BooleanAttributeValueStyle#noValue] if not set.
  ///
  /// @param value the style value
  void valueStyle(BooleanAttributeValueStyle value);

  /// Sets the quotation mark style to the specified value. Defaults to
  /// [QuotationMarkStyle#double] if not set.
  ///
  /// The quotation mark style is used to quote the value of a boolean attribute
  /// when the [BooleanAttributeValueStyle] is not
  /// [BooleanAttributeValueStyle#noValue].
  ///
  /// @param value the style value
  void quotationMarkStyle(QuotationMarkStyle value);

}
