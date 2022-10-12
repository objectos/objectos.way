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

abstract class UnaryExpressionImpl extends AbstractDefaultImmutableCodeElement
    implements
    ConditionalAndExpression,
    MultiplicativeExpression,
    RelationalExpression {

  UnaryExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  UnaryExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(UnaryExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(UnaryExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(UnaryExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(UnaryExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(UnaryExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

  static class NotPlusOrMinus extends UnaryExpressionImpl implements UnaryExpressionNotPlusMinus {
    private NotPlusOrMinus(CodeElement... elements) {
      super(elements);
    }

    private NotPlusOrMinus(UnmodifiableList<CodeElement> elements) {
      super(elements);
    }

    static NotPlusOrMinus of0(UnaryOperator operator, UnaryExpression expression) {
      return new NotPlusOrMinus(
          operator, expression
      );
    }

    @Override
    public final UnaryExpressionNotPlusMinus nl() {
      return new NotPlusOrMinus(appendNextLine());
    }
  }

  static class Standard extends UnaryExpressionImpl implements UnaryExpression {
    private Standard(CodeElement... elements) {
      super(elements);
    }

    private Standard(UnmodifiableList<CodeElement> elements) {
      super(elements);
    }

    static Standard of0(UnaryOperator operator, UnaryExpression expression) {
      return new Standard(
          operator, expression
      );
    }

    @Override
    public final UnaryExpression nl() {
      return new Standard(appendNextLine());
    }
  }

}
