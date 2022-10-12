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

final class AdditiveExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements AdditiveExpression {

  private AdditiveExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private AdditiveExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static AdditiveExpression of0(
      AdditiveOperator operator, AdditiveExpression lhs, MultiplicativeExpression rhs) {
    return new AdditiveExpressionImpl(
        lhs, space(), operator, space(), rhs
    );
  }

  @Override
  public final AdditiveExpression nl() {
    return new AdditiveExpressionImpl(appendNextLine());
  }

  @Override
  protected final AdditiveExpression selfAdditiveExpression() {
    return this;
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(AdditiveExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

}
