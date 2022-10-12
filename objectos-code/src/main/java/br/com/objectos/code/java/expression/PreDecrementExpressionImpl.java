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

final class PreDecrementExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements PreDecrementExpression {

  private PreDecrementExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private PreDecrementExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static PreDecrementExpression preDec0(UnaryExpression expression) {
    return new PreDecrementExpressionImpl(
        minusMinus(), expression
    );
  }

  @Override
  public final PreDecrementExpression nl() {
    return new PreDecrementExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(PreDecrementExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(PreDecrementExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(PreDecrementExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(PreDecrementExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(PreDecrementExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

}
