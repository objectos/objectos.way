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

class InclusiveOrExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements InclusiveOrExpression {

  private InclusiveOrExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private InclusiveOrExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static InclusiveOrExpression bitwiseOr0(
      InclusiveOrExpression lhs, ExclusiveOrExpression rhs) {
    return new InclusiveOrExpressionImpl(
        lhs, space(), bitwiseOr(), space(), rhs
    );
  }

  @Override
  public final InclusiveOrExpression nl() {
    return new InclusiveOrExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(InclusiveOrExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    throw newUoe(InclusiveOrExpression.class);
  }

}
