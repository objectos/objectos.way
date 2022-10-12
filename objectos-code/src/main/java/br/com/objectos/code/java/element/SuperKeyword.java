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
package br.com.objectos.code.java.element;

import br.com.objectos.code.java.declaration.ConstructorCode;
import br.com.objectos.code.java.declaration.ExplicitConstructorInvocation;
import br.com.objectos.code.java.declaration.SuperConstructorInvocation;
import br.com.objectos.code.java.expression.ArrayReferenceExpression;
import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.ConditionalAndExpression;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.FieldAccess;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.LeftHandSide;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MultiplicativeExpression;
import br.com.objectos.code.java.expression.PostfixExpression;
import br.com.objectos.code.java.expression.RelationalExpression;
import br.com.objectos.code.java.io.CodeWriter;

public final class SuperKeyword
    extends AbstractDefaultImmutableCodeElement
    implements
    Keyword,
    ExplicitConstructorInvocation,
    SuperKeywordDsl {

  private static final SuperKeyword INSTANCE = new SuperKeyword(word("super"));

  private SuperKeyword(CodeElement... elements) {
    super(elements);
  }

  public static SuperKeyword _super() {
    return INSTANCE;
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    builder.constructorInvocation(this);
  }

  @Override
  public final void acceptConstructorCodeWriter(CodeWriter w) {
    w.writeCodeElement(NewLine.nextLine());
    w.writeCodeElement(SuperConstructorInvocation._super());
    w.writeCodeElement(semicolon());
  }

  @Override
  public final FieldAccess id(Identifier id) {
    return Expressions.fieldAccess(this, id);
  }

  @Override
  public final FieldAccess id(String id) {
    return Expressions.fieldAccess(this, id);
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(ThisKeyword.class);
  }

  @Override
  protected final Callee selfCallee() {
    return this;
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    throw newUoe(SuperKeyword.class);
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(SuperKeyword.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    return this;
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(SuperKeyword.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(SuperKeyword.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    throw newUoe(SuperKeyword.class);
  }

}
