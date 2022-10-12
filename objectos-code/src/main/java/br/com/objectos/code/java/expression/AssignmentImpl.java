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
import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.statement.ForStatement;
import br.com.objectos.code.java.statement.IfStatement;
import br.com.objectos.code.java.statement.Semicolon;
import br.com.objectos.code.java.statement.StatementOrBlockBuilder;
import br.com.objectos.code.java.statement.Statements;
import br.com.objectos.code.java.statement.TryStatement;
import objectos.util.UnmodifiableList;

final class AssignmentImpl
    // extends AbstractImmutableCodeElement instead of
    // AbstractDefaultCodeElement since AssignmentExpression is on the top of
    // the Expression grammar
    extends AbstractImmutableCodeElement
    implements Assignment {

  private AssignmentImpl(CodeElement... elements) {
    super(elements);
  }

  private AssignmentImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static Assignment assign0(
      AssignmentOperator operator, LeftHandSide lhs, Expression rhs) {
    return new AssignmentImpl(
        lhs, space(), operator, space(), rhs
    );
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    builder.addStatement(this);
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
    builder.addStatement(this);
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
  public final AssignmentExpression nl() {
    return new AssignmentImpl(appendNextLine());
  }

  @Override
  public final String toString() {
    return Statements.toString(this);
  }

}
