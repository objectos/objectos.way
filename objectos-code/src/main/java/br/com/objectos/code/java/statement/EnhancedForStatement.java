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

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.type.NamedType;

public class EnhancedForStatement extends ForStatement {

  private final NamedType typeName;
  private final Identifier id;
  private final Expression expression;
  private final Statement body;

  EnhancedForStatement(NamedType typeName,
                       Identifier id,
                       Expression expression,
                       Statement body) {
    this.typeName = typeName;
    this.id = id;
    this.expression = expression;
    this.body = body;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._for());
    w.writeCodeElement(space());
    w.writeCodeElement(openParens());
    w.writeCodeElement(typeName);
    w.writeCodeElement(space());
    w.writeCodeElement(id);
    w.writeCodeElement(space());
    w.writeCodeElement(colon());
    w.writeCodeElement(space());
    w.writeCodeElement(expression);
    w.writeCodeElement(closeParens());
    w.writeCodeElement(space());
    w.writeCodeElement(body);
    writeSemicolonIfNecessary(w, body);
    return w;
  }

}
