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
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.expression.production.MethodInvocationExpression;
import br.com.objectos.code.java.io.CodeWriter;

public abstract class MethodInvocation extends AbstractStatementExpression
    implements
    MethodInvocationExpression {

  final String methodName;
  final Arguments arguments;
  private final CodeElement newLine;

  MethodInvocation(String methodName, Arguments arguments, CodeElement newLine) {
    this.methodName = methodName;
    this.arguments = arguments;
    this.newLine = newLine;
  }

  static Qualified invoke0(
      Callee caller, String methodName, Arguments args) {
    return new Qualified(
        caller, noop(), methodName, args, noop()
    );
  }

  static Qualified invoke0(
      Callee caller, TypeWitness witness, String methodName, Arguments args) {
    StandardTypeWitness wrapper;
    wrapper = new StandardTypeWitness(witness);

    return new Qualified(
        caller, wrapper, methodName, args, noop()
    );
  }

  static Unqualified invoke0(String methodName, Arguments args) {
    return new Unqualified(
        methodName, args, noop()
    );
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    acceptCodeWriterImpl(w);
    w.write(methodName);
    arguments.acceptCodeWriter(w);
    newLine.acceptCodeWriter(w);
    return w;
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
    throw newUoe(MethodInvocation.class);
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

  abstract void acceptCodeWriterImpl(CodeWriter w);

  public static class Qualified extends MethodInvocation {

    private final Callee caller;
    private final CodeElement typeWitness;

    private Qualified(Callee caller,
                      CodeElement typeWitness,
                      String methodName,
                      Arguments arguments,
                      CodeElement newLine) {
      super(methodName, arguments, newLine);
      this.caller = caller;
      this.typeWitness = typeWitness;
    }

    @Override
    public final MethodInvocationExpression nl() {
      return new Qualified(caller, typeWitness, methodName, arguments, NewLine.nextLine());
    }

    @Override
    final void acceptCodeWriterImpl(CodeWriter w) {
      caller.acceptCodeWriter(w);
      w.writePreIndentation();
      w.write('.');
      typeWitness.acceptCodeWriter(w);
    }

  }

  public static class Unqualified extends MethodInvocation
      implements
      MethodInvocationChainElement {

    private Unqualified(String methodName, Arguments arguments, CodeElement newLine) {
      super(methodName, arguments, newLine);
    }

    @Override
    public final void acceptMethodInvocationChainBuilder(MethodInvocationChain.Builder builder) {
      builder.addMethodInvocation(this);
    }

    @Override
    public final MethodInvocationExpression nl() {
      return new Unqualified(methodName, arguments, NewLine.nextLine());
    }

    @Override
    final void acceptCodeWriterImpl(CodeWriter w) {
      w.writePreIndentation();
    }

  }

  private static class StandardTypeWitness implements CodeElement {

    private final TypeWitness typeWitness;

    StandardTypeWitness(TypeWitness typeWitness) {
      this.typeWitness = typeWitness;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      typeWitness.acceptCodeWriter(w);
      w.write(' ');
      return w;
    }

  }

}
