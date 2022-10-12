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
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.type.NamedArray;
import java.util.Arrays;
import java.util.Iterator;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

final class ArrayCreationExpressionImpl
    extends AbstractDefaultImmutableCodeElement
    implements ArrayCreationExpression {

  private ArrayCreationExpressionImpl(CodeElement... elements) {
    super(elements);
  }

  private ArrayCreationExpressionImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static ArrayCreationExpression _new0(NamedArray type, ArrayInitializer initializer) {
    return new ArrayCreationExpressionImpl(
        Keywords._new(), space(), type, space(), initializer
    );
  }

  static ArrayCreationExpression _new0(NamedArray type, Expression... dims) {
    return _new0(type, Arrays.asList(dims));
  }

  static ArrayCreationExpression _new0(
      NamedArray type, Iterable<? extends Expression> dims) {
    GrowableList<CodeElement> els = new GrowableList<>();

    els.add(Keywords._new());
    els.add(space());
    els.add(type.getArrayCreationExpressionName());

    Iterator<? extends Expression> it = dims.iterator();

    int dimension = type.dimension();
    for (int i = 0; i < dimension; i++) {
      Expression dim;
      if (it.hasNext()) {
        dim = it.next();
      } else {
        dim = EmptyExpression.empty();
      }

      els.add(openBracket());
      els.add(dim);
      els.add(closeBracket());
    }

    if (it.hasNext()) {
      throw new IllegalArgumentException("Too many dim expressions: array dimension=" + dimension);
    }

    return new ArrayCreationExpressionImpl(
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
  public final ArrayCreationExpression nl() {
    return new ArrayCreationExpressionImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(ArrayCreationExpression.class);
  }

  @Override
  protected final Callee selfCallee() {
    throw newUoe(ArrayCreationExpression.class);
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(ArrayCreationExpression.class);
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
