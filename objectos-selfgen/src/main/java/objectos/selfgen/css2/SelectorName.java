/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css2;

import java.util.Comparator;
import java.util.Objects;
import objectos.selfgen.util.JavaNames;

final class SelectorName {
  static final Comparator<? super SelectorName> ORDER_BY_FIELD_NAME
      = (self, that) -> self.fieldName.compareTo(that.fieldName);

  public final String fieldName;

  public final String selectorName;

  boolean disabled;

  public SelectorName(String fieldName, String selectorName) {
    this.fieldName = fieldName;
    this.selectorName = selectorName;
  }

  public static SelectorName of(String name) {
    Objects.requireNonNull(name, "name == null");

    var fieldName = name.replaceFirst("^:-", "_");

    fieldName = fieldName.replaceFirst("^::-", "__");

    fieldName = fieldName.replace(':', '_');

    fieldName = JavaNames.toValidMethodName(fieldName);

    return new SelectorName(fieldName, name);
  }

  final void disable() {
    disabled = true;
  }
}