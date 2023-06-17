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
import objectos.code.ClassTypeName;
import objectos.util.UnmodifiableList;

public final class ValueType implements ParameterType {

  static final Comparator<? super ValueType> ORDER_BY_SIMPLE_NAME
      = (self, that) -> self.className.simpleName().compareTo(that.className.simpleName());

  public final ClassTypeName className;

  public final UnmodifiableList<Value> values;

  ValueType(ClassTypeName className, UnmodifiableList<Value> values) {
    this.className = className;
    this.values = values;
  }

}