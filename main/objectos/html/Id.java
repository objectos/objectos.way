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
import objectos.html.markup.RegularElementValue;
import objectos.html.markup.VoidElementValue;
import objectox.html.attr.AttributeObject;
import objectox.html.attr.AttributeName;

/// Represents an HTML `id` attribute and its value.
public final class Id
    extends
    AttributeObject
    implements
    RegularElementValue,
    VoidElementValue {

  private Id(String value) {
    super(AttributeName.ID, value);
  }

  /**
   * Creates a new {@code Id} instance with the specified value.
   *
   * @param value
   *        the value of this HTML {@code id} attribute.
   *
   * @return a newly constructed {@code Id} instance
   */
  public static Id of(String value) {
    Objects.requireNonNull(value, "value == null");

    return new Id(value);
  }

}