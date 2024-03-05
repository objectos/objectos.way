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

public final class ElementClass implements Api.ExternalAttribute.StyleClass {

  private final String value;

  ElementClass(String value) {
    this.value = value;
  }

  public static ElementClass of(String value) {
    Check.notNull(value, "value == null");
    return new ElementClass(value);
  }

  public static ElementClass of(String v0, String v1) {
    Check.notNull(v0, "v0 == null");
    Check.notNull(v1, "v1 == null");

    return new ElementClass(v0 + " " + v1);
  }

  public static ElementClass of(String v0, String v1, String v2) {
    Check.notNull(v0, "v0 == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");

    return new ElementClass(v0 + " " + v1 + " " + v2);
  }

  public static ElementClass of(String v0, String v1, String v2, String v3) {
    Check.notNull(v0, "v0 == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");

    return new ElementClass(v0 + " " + v1 + " " + v2 + " " + v3);
  }

  public static ElementClass of(String v0, String v1, String v2, String v3, String v4) {
    Check.notNull(v0, "v0 == null");
    Check.notNull(v1, "v1 == null");
    Check.notNull(v2, "v2 == null");
    Check.notNull(v3, "v3 == null");
    Check.notNull(v4, "v4 == null");

    return new ElementClass(v0 + " " + v1 + " " + v2 + " " + v3 + " " + v4);
  }

  public static ElementClass of(String... values) {
    StringBuilder sb;
    sb = new StringBuilder();

    for (int i = 0; i < values.length; i++) {
      if (i > 0) {
        sb.append(' ');
      }

      String value;
      value = Check.notNull(values[i], "values[", i, "] == null");

      sb.append(value);
    }

    String value;
    value = sb.toString();

    return new ElementClass(value);
  }

  @Override
  public final String className() {
    return value;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof Api.ExternalAttribute.StyleClass that
        && value.equals(that.className());
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