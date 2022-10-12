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
import java.util.Arrays;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

final class ArrayAccessImpl
    extends AbstractDefaultImmutableCodeElement
    implements ArrayAccess {

  private ArrayAccessImpl(CodeElement... elements) {
    super(elements);
  }

  private ArrayAccessImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static ArrayAccess arrayAccess0(
      ArrayReferenceExpression ref, Expression... expressions) {
    return arrayAccess0(ref, Arrays.asList(expressions));
  }

  static ArrayAccess arrayAccess0(
      ArrayReferenceExpression ref, Iterable<? extends Expression> expressions) {
    GrowableList<CodeElement> els = new GrowableList<>();
    els.add(ref);

    for (Expression expression : expressions) {
      els.add(openBracket());
      els.add(expression);
      els.add(closeBracket());
    }

    return new ArrayAccessImpl(
        els.toUnmodifiableList()
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
  public final ArrayAccess nl() {
    return new ArrayAccessImpl(appendNextLine());
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
    return this;
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
