/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import objectos.lang.object.Check;

public final class ElementId implements Api.ExternalAttribute.Id {

  private final String value;

  ElementId(String value) {
    this.value = value;
  }

  public static ElementId of(String value) {
    Check.notNull(value, "value == null");

    return new ElementId(value);
  }

  @Override
  public final String id() {
    return value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Api.ExternalAttribute.Id that
        && value.equals(that.id());
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final String toString() {
    return value;
  }

}