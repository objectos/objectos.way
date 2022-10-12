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
import br.com.objectos.code.java.expression.ThrowableExpression;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public final class ThrowStatement extends AbstractSimpleStatement {

  private final ThrowableExpression expression;

  private ThrowStatement(ThrowableExpression expression) {
    this.expression = expression;
  }

  public static ThrowStatement _throw(ThrowableExpression expression) {
    Check.notNull(expression, "expression == null");
    return new ThrowStatement(expression);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._throw());
    w.writeCodeElement(space());
    w.writeCodeElement(expression);
    return w;
  }

}
