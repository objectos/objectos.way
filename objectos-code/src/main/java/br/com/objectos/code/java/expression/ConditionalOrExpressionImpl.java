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

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import objectos.util.UnmodifiableList;

final class ConditionalOrExpressionImpl
    extends AbstractImmutableCodeElement
    implements ConditionalOrExpression {

  private static final CodeElement OR = word("||");

  private ConditionalOrExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private ConditionalOrExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static ConditionalOrExpression or0(
      ConditionalOrExpression lhs, ConditionalAndExpression rhs) {
    return new ConditionalOrExpressionImpl(
        lhs, space(), OR, space(), rhs
    );
  }

  @Override
  public final ConditionalOrExpression nl() {
    return new ConditionalOrExpressionImpl(appendNextLine());
  }

  @Override
  public final ConditionalOrExpression or(ConditionalAndExpression expression) {
    return Expressions.or(this, expression);
  }

  @Override
  public final ConditionalExpression ternary(
      Expression trueExpression, ConditionalExpression falseExpression) {
    return Expressions.ternary(this, trueExpression, falseExpression);
  }

  @Override
  public final ConditionalExpression ternary(
      Expression trueExpression, LambdaExpression falseExpression) {
    return Expressions.ternary(this, trueExpression, falseExpression);
  }

}
