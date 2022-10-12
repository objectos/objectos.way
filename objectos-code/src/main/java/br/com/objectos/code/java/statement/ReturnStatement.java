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
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public abstract class ReturnStatement extends AbstractSimpleStatement {

  protected ReturnStatement() {}

  public static ReturnStatement _return(String id) {
    return new WithExpressionReturnStatement(Expressions.id(id));
  }

  public static ReturnStatement _return(Expression expression) {
    Check.notNull(expression, "expression == null");
    return new WithExpressionReturnStatement(expression);
  }
  
  private static final class WithExpressionReturnStatement extends ReturnStatement {

    private final Expression expression;

    WithExpressionReturnStatement(Expression expression) {
      this.expression = expression;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writeCodeElement(Keywords._return());
      w.writeCodeElement(space());
      w.writeCodeElement(expression);
      return w;
    }

  }

}
