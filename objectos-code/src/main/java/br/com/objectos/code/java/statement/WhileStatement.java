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
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public class WhileStatement extends AbstractSimpleStatement {

  private final Expression condition;
  private final Statement body;

  private WhileStatement(Expression condition, Statement body) {
    this.condition = condition;
    this.body = body;
  }

  public static WhileStatement _while(Expression expression, Statement body) {
    Check.notNull(expression, "expression == null");
    Check.notNull(body, "body == null");
    return new WhileStatement(expression, body);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._while());
    w.writeCodeElement(space());
    w.writeCodeElement(parenthesized(condition));
    w.writeCodeElement(space());
    w.writeCodeElement(body);
    writeSemicolonIfNecessary(w, body);
    return w;
  }

}
