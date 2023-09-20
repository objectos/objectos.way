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
package selfgen.css.internal;

import java.util.HashSet;
import java.util.Set;
import objectos.code.ClassName;
import objectos.code.TypeName;

public final class IntType implements ParameterType, Value {

  public final Set<ClassName> interfaces = new HashSet<>();

  @Override
  public final void addValueType(ValueType valueType) {
    interfaces.add(valueType.className);
  }

  @Override
  public final TypeName typeName() {
    return ThisTemplate.INT_LITERAL;
  }

}