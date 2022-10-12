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
import br.com.objectos.code.java.type.NamedClass;
import objectos.util.UnmodifiableList;

final class ExpressionNameImpl
    extends AbstractDefaultImmutableCodeElement
    implements ExpressionName {

  private ExpressionNameImpl(CodeElement... elements) {
    super(elements);
  }

  private ExpressionNameImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static ExpressionName expressionName0(ExpressionName name, Identifier id) {
    return new ExpressionNameImpl(
        name, dot(), id
    );
  }

  static ExpressionName expressionName0(NamedClass className, Identifier id) {
    return new ExpressionNameImpl(
        className, dot(), id
    );
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final ExpressionName id(Identifier id) {
    return Expressions.expressionName(this, id);
  }

  @Override
  public final ExpressionName id(String id) {
    return Expressions.expressionName(this, id);
  }

  @Override
  public final ExpressionName nl() {
    return new ExpressionNameImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    return this;
  }

  @Override
  protected final Callee selfCallee() {
    return this;
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(ExpressionName.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    return this;
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
