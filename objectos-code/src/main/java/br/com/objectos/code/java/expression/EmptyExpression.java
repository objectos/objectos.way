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
package br.com.objectos.code.java.expression;

import br.com.objectos.code.java.declaration.ConstructorCode;
import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.statement.ForStatement;
import br.com.objectos.code.java.statement.IfStatement;
import br.com.objectos.code.java.statement.Semicolon;
import br.com.objectos.code.java.statement.StatementOrBlockBuilder;
import br.com.objectos.code.java.statement.TryStatement;

public final class EmptyExpression extends AbstractCodeElement
    implements
    MethodCodeElement,
    StatementExpression {

  static final EmptyExpression INSTANCE = new EmptyExpression();

  private EmptyExpression() {}

  static EmptyExpression empty() {
    return EmptyExpression.INSTANCE;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    return w;
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    // noop
  }

  @Override
  public final void acceptForStatementBuilder(ForStatement.Builder builder) {
    builder.addStatement(this);
  }

  @Override
  public final void acceptIfStatementBuilder(IfStatement.Builder builder) {
    builder.addToCurrentBlock(this);
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.emptyBody();
  }

  @Override
  public final void acceptSemicolon(Semicolon semicolon) {
    semicolon.write();
  }

  @Override
  public final void acceptStatementOrBlockBuilder(StatementOrBlockBuilder builder) {
    builder.withStatement(this);
  }

  @Override
  public final void acceptTryStatementBuilder(TryStatement.Builder builder) {
    builder.addStatement(this);
  }

  @Override
  public final CodeElement ifEmpty(CodeElement empty, CodeElement notEmpty) {
    return empty;
  }

}
