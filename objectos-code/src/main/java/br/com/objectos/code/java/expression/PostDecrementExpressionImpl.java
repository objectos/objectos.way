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

import br.com.objectos.code.java.element.CodeElement;
import objectos.util.UnmodifiableList;

final class PostDecrementExpressionImpl
    extends AbstractDefaultStatementExpression
    implements PostDecrementExpression {

  private PostDecrementExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private PostDecrementExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static PostDecrementExpression postDec0(PostfixExpression expression) {
    return new PostDecrementExpressionImpl(
        expression, minusMinus()
    );
  }

  @Override
  public final PostDecrementExpression nl() {
    return new PostDecrementExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(PostDecrementExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(PostDecrementExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(PostDecrementExpression.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    throw newUoe(PostDecrementExpression.class);
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    return this;
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

}
