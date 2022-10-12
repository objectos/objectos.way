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
package br.com.objectos.code.java.statement;

import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.type.NamedType;
import objectos.lang.Check;

public class SimpleLocalVariableDeclaration extends LocalVariableDeclaration {

  private SimpleLocalVariableDeclaration(CodeElement... elements) {
    super(elements);
  }

  static SimpleLocalVariableDeclaration ofUnchecked(NamedType typeName, String name) {
    return new SimpleLocalVariableDeclaration(
        typeName, space(), identifier(name)
    );
  }

  public final WithInitLocalVariableDeclaration init(VariableInitializer init) {
    Check.notNull(init, "init == null");
    return WithInitLocalVariableDeclaration.ofUnchecked(this, init);
  }

}