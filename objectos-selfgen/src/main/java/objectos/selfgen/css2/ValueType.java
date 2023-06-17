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
import objectos.code.ClassTypeName;

public final class ValueType implements ParameterType {

  static final Comparator<? super ValueType> ORDER_BY_SIMPLE_NAME
      = (self, that) -> self.className.simpleName().compareTo(that.className.simpleName());

  public final ClassTypeName className;

  ValueType(ClassTypeName className) {
    this.className = className;
  }

  public static ValueType of(String simpleName) {
    Objects.requireNonNull(simpleName, "simpleName == null");

    var className = ClassTypeName.of(ThisTemplate.CSS_TMPL, simpleName);

    return new ValueType(className);
  }

  @Override
  public final ClassTypeName className() {
    return className;
  }

}