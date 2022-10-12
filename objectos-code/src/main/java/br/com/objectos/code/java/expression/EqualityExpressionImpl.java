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

import br.com.objectos.code.java.element.AbstractDefaultImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import objectos.util.UnmodifiableList;

final class EqualityExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements EqualityExpression {

  private EqualityExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private EqualityExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static EqualityExpression eq0(EqualityExpression lhs, RelationalExpression rhs) {
    return ofOperator(EqualityOperator.EQUAL_TO, lhs, rhs);
  }

  static EqualityExpression ne0(EqualityExpression lhs, RelationalExpression rhs) {
    return ofOperator(EqualityOperator.NOT_EQUAL_TO, lhs, rhs);
  }

  private static EqualityExpressionImpl ofOperator(
      EqualityOperator operator, EqualityExpression lhs, RelationalExpression rhs) {
    return new EqualityExpressionImpl(
        lhs, space(), operator, space(), rhs
    );
  }

  @Override
  public final EqualityExpression nl() {
    return new EqualityExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(EqualityExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    throw newUoe(EqualityExpression.class);
  }

}
