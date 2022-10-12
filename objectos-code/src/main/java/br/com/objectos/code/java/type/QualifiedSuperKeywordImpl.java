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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.element.AbstractDefaultImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Keywords;
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
import objectos.util.UnmodifiableList;

class QualifiedSuperKeywordImpl
    extends AbstractDefaultImmutableCodeElement
    implements QualifiedSuperKeyword {

  private QualifiedSuperKeywordImpl(CodeElement... elements) {
    super(elements);
  }

  private QualifiedSuperKeywordImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static QualifiedSuperKeyword ofUnchecked(NamedClass className) {
    return new QualifiedSuperKeywordImpl(
        className, dot(), Keywords._super()
    );
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
    throw newUoe(QualifiedSuperKeyword.class);
  }

  @Override
  protected final Callee selfCallee() {
    return this;
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    throw newUoe(QualifiedSuperKeyword.class);
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(QualifiedSuperKeyword.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    return this;
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    throw newUoe(QualifiedSuperKeyword.class);
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    throw newUoe(QualifiedSuperKeyword.class);
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    throw newUoe(QualifiedSuperKeyword.class);
  }

}
