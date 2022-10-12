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

final class ShiftExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements ShiftExpression {

  private static final CodeElement LEFT = word("<<");
  private static final CodeElement RIGHT = word(">>");
  private static final CodeElement UNSIGNED_RIGHT = word(">>>");

  private ShiftExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private ShiftExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static ShiftExpression leftShift0(ShiftExpression lhs, AdditiveExpression rhs) {
    return ofOperator(LEFT, lhs, rhs);
  }

  static ShiftExpression rightShift0(ShiftExpression lhs, AdditiveExpression rhs) {
    return ofOperator(RIGHT, lhs, rhs);
  }

  static ShiftExpression unsignedRightShift0(ShiftExpression lhs, AdditiveExpression rhs) {
    return ofOperator(UNSIGNED_RIGHT, lhs, rhs);
  }

  private static ShiftExpression ofOperator(
      CodeElement operator, ShiftExpression lhs, AdditiveExpression rhs) {
    return new ShiftExpressionImpl(
        lhs, space(), operator, space(), rhs
    );
  }

  @Override
  public final ShiftExpression nl() {
    return new ShiftExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(ShiftExpression.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

  @Override
  protected final ShiftExpression selfShiftExpression() {
    return this;
  }

}
