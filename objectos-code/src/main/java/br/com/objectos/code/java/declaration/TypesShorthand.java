/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class TypesShorthand implements ClassCodeElement {

  private final UnmodifiableList<TypeCode> types;

  private TypesShorthand(UnmodifiableList<TypeCode> types) {
    this.types = types;
  }

  public static TypesShorthand types(Iterable<? extends TypeCode> types) {
    Check.notNull(types, "types == null");

    return new TypesShorthand(UnmodifiableList.copyOf(types));
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addTypes(types);
  }

}
