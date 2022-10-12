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
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public class DoStatement extends AbstractSimpleStatement {

  private final Statement body;
  private final WhileElement condition;

  private DoStatement(Statement body, WhileElement condition) {
    this.body = body;
    this.condition = condition;
  }

  public static DoStatement _do(Statement body, WhileElement condition) {
    Check.notNull(body, "body == null");
    Check.notNull(condition, "condition == null");
    return new DoStatement(body, condition);
  }
  
  public static WhileElement _while(Expression condition) {
    Check.notNull(condition, "condition == null");
    return new WhileElement(condition);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._do());
    w.writeCodeElement(space());
    w.writeCodeElement(body);
    writeSemicolonIfNecessary(w, body);
    w.writeCodeElement(space());
    w.writeCodeElement(condition);
    return w;
  }
  
  public static final class WhileElement implements CodeElement {

    private final Expression condition;

    private WhileElement(Expression condition) {
      this.condition = condition;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writeCodeElement(Keywords._while());
      w.writeCodeElement(space());
      w.writeCodeElement(parenthesized(condition));
      return w;
    }

  }
  
}