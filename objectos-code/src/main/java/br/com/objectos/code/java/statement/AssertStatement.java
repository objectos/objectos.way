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

public class AssertStatement extends AbstractSimpleStatement {

  private final Expression detailMessage;
  private final Expression expression;

  private AssertStatement(Expression expression, Expression detailMessage) {
    this.expression = expression;
    this.detailMessage = detailMessage;
  }

  public static AssertStatement _assert(Expression expression) {
    Check.notNull(expression, "expression == null");
    return new AssertStatement(expression, null);
  }

  public static AssertStatement _assert(Expression expression, Expression detailMessage) {
    Check.notNull(expression, "expression == null");
    Check.notNull(detailMessage, "detailMessage == null");
    return new AssertStatement(expression, detailMessage);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._assert());
    w.writeCodeElement(space());
    w.writeCodeElement(expression);

    if (detailMessage != null) {
      w.writeCodeElement(space());
      w.writeCodeElement(colon());
      w.writeCodeElement(space());
      w.writeCodeElement(detailMessage);
    }

    return w;
  }

}