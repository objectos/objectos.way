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
import objectox.html.attr.AttributeNamePojo;
import objectox.html.attr.IdPojo;

/**
 * Represents an HTML {@code id} attribute and its value.
 */
public sealed interface Id extends AttributeObject permits IdPojo {

  /**
   * Creates a new {@code Id} instance with the specified value.
   *
   * @param value
   *        the value of this HTML {@code id} attribute.
   *
   * @return a newly constructed {@code Id} instance
   */
  static Id of(String value) {
    Objects.requireNonNull(value, "value == null");

    return new IdPojo(value);
  }

  /**
   * The {@code id} attribute name.
   *
   * @return the {@code id} attribute name
   */
  @Override
  default AttributeName attrName() {
    return AttributeNamePojo.ID;
  }

  /**
   * The {@code id} value.
   *
   * @return the {@code id} value
   */
  @Override
  String attrValue();

}