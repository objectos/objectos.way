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
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.statement.VariableInitializer;

public class FieldCodeDeclarator extends AbstractImmutableCodeElement
    implements
    FieldCodeElement {

  private final String name;

  private FieldCodeDeclarator(String name, CodeElement... elements) {
    super(elements);
    this.name = name;
  }

  static FieldCodeDeclarator init0(Identifier name) {
    return new FieldCodeDeclarator(name.name(), name);
  }

  static FieldCodeDeclarator init0(Identifier name, VariableInitializer initializer) {
    return new FieldCodeDeclarator(
        name.name(),
        name, space(), equals(), space(), initializer
    );
  }

  @Override
  public final FieldCode.Builder acceptFieldCodeBuilder(FieldCode.Builder builder) {
    return builder.addDeclarator(this);
  }

  final String name() {
    return name;
  }

}
