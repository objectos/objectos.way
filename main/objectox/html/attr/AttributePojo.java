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
package objectox.html.attr;

import java.util.Objects;
import objectos.html.Attribute;

public record AttributePojo(AttributeName name, String value) implements Attribute {

  public static AttributePojo of(AttributeName name, String value) {
    final AttributeName n;
    n = Objects.requireNonNull(name, "name == null");

    return of0(n, value);
  }

  public static AttributePojo of0(AttributeName name, String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    return new AttributePojo(name, v);
  }

}