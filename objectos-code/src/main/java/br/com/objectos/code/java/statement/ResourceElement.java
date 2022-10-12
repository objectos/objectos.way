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

import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.statement.TryStatement.Builder;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import objectos.lang.Check;

public class ResourceElement extends AbstractCodeElement implements TryStatementElement {

  private final NamedType typeName;
  private final Identifier id;
  private final VariableInitializer init;

  ResourceElement(NamedType typeName, Identifier id, VariableInitializer init) {
    this.typeName = typeName;
    this.id = id;
    this.init = init;
  }

  public static ResourceElement resource(Class<?> type, Identifier id, VariableInitializer init) {
    Check.notNull(type, "type == null");
    Check.notNull(id, "id == null");
    Check.notNull(init, "init == null");
    return new ResourceElement(NamedClass.of(type), id, init);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(typeName);
    w.writeCodeElement(space());
    w.writeCodeElement(id);
    w.writeCodeElement(space());
    w.writeCodeElement(equals());
    w.writeCodeElement(space());
    w.writeCodeElement(init);

    return w;
  }

  @Override
  public final void acceptTryStatementBuilder(Builder builder) {
    builder.addResource(this);
  }

}