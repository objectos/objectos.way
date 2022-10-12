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
import br.com.objectos.code.java.type.NamedPrimitive;
import br.com.objectos.code.java.type.NamedReferenceType;
import br.com.objectos.code.java.type.NamedType;
import objectos.util.UnmodifiableList;

final class CastExpressionImpl extends AbstractDefaultImmutableCodeElement
    implements CastExpression {

  private CastExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private CastExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static CastExpression cast0(NamedPrimitive type, UnaryExpression expression) {
    return cast1(type, expression);
  }

  static CastExpression cast0(
      NamedReferenceType type, UnaryExpressionNotPlusMinus expression) {
    return cast1(type, expression);
  }

  private static CastExpression cast1(NamedType type, Expression expression) {
    return new CastExpressionImpl(
        openParens(), type, closeParens(), space(), expression
    );
  }

  @Override
  public final CastExpression nl() {
    return new CastExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(CastExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(CastExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(CastExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(CastExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(CastExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

}
