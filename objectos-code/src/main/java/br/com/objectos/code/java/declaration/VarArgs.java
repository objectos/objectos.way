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

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.type.NamedArray;
import objectos.lang.Check;

public class VarArgs extends AbstractImmutableCodeElement implements ParameterTypeName {

  private VarArgs(CodeElement... elements) {
    super(elements);
  }

  public static VarArgs of(NamedArray typeName) {
    Check.notNull(typeName, "typeName == null");
    return new VarArgs(
        typeName.getDeepComponent(),
        typeName.printVarArgsSymbol()
    );
  }

  @Override
  public final boolean isVarArgs() {
    return true;
  }

}
