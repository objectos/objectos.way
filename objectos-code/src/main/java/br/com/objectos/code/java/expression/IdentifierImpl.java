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

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.EnumCode;
import br.com.objectos.code.java.declaration.EnumConstantCode;
import br.com.objectos.code.java.declaration.FieldCode;
import br.com.objectos.code.java.declaration.InterfaceCode;
import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.element.AbstractDefaultImmutableCodeElement;
import br.com.objectos.code.java.element.NewLine;
import javax.lang.model.SourceVersion;
import objectos.lang.Check;

final class IdentifierImpl
    extends AbstractDefaultImmutableCodeElement
    implements Identifier {

  private final String name;

  private IdentifierImpl(String name) {
    super(word(name));
    this.name = name;
  }

  private IdentifierImpl(String name, boolean newLine) {
    super(word(name), NewLine.nextLine());
    this.name = name;
  }

  static Identifier id0(String name) {
    Check.argument(
        SourceVersion.isIdentifier(name),
        name, " is not a valid identifier"
    );

    return new IdentifierImpl(name);
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.simpleName(name);
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.simpleName(this);
  }

  @Override
  public final void acceptEnumConstantCodeBuilder(EnumConstantCode.Builder builder) {
    builder.simpleName(this);
  }

  @Override
  public final FieldCode.Builder acceptFieldCodeBuilder(FieldCode.Builder builder) {
    return builder.addDeclarator(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.simpleName(name);
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.name(name);
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
  public final String name() {
    return name;
  }

  @Override
  public final Identifier nl() {
    return new IdentifierImpl(name, true);
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
