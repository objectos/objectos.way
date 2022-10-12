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
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.production.ClassInstanceCreationExpression;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.type.NamedClass;
import objectos.lang.Check;

public class NewClass extends AbstractStatementExpression
    implements
    ClassInstanceCreationExpression {

  private final Arguments arguments;
  private final NamedClass newClass;
  private final CodeElement typeWitness;

  private NewClass(NamedClass newClass,
                   CodeElement typeWitness,
                   Arguments arguments) {
    this.newClass = newClass;
    this.typeWitness = typeWitness;
    this.arguments = arguments;
  }

  public static NewClass _new(
      NamedClass className) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.empty());
  }

  public static NewClass _new(
      NamedClass className,
      Arguments args) {
    Check.notNull(className, "className == null");
    Check.notNull(args, "args == null");
    return new NewClass(className, noop(), args);
  }

  public static NewClass _new(
      NamedClass className,
      ArgumentsElement a1) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.args(a1));
  }

  public static NewClass _new(
      NamedClass className,
      ArgumentsElement a1,
      ArgumentsElement a2) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.args(a1, a2));
  }

  public static NewClass _new(
      NamedClass className,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.args(a1, a2, a3));
  }

  public static NewClass _new(
      NamedClass className,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.args(a1, a2, a3, a4));
  }

  public static NewClass _new(
      NamedClass className,
      Iterable<? extends ArgumentsElement> args) {
    Check.notNull(className, "className == null");
    return new NewClass(className, noop(), Arguments.args(args));
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.empty());
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      Arguments args) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    Check.notNull(args, "args == null");
    return new NewClass(className, witness, args);
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      ArgumentsElement a1) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.args(a1));
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      ArgumentsElement a1,
      ArgumentsElement a2) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.args(a1, a2));
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.args(a1, a2, a3));
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.args(a1, a2, a3, a4));
  }

  public static NewClass _new(
      NamedClass className, TypeWitness witness,
      Iterable<? extends ArgumentsElement> args) {
    Check.notNull(className, "className == null");
    Check.notNull(witness, "witness == null");
    return new NewClass(className, witness, Arguments.args(args));
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    Keywords._new().acceptCodeWriter(w);
    space().acceptCodeWriter(w);
    newClass.acceptCodeWriter(w);
    typeWitness.acceptCodeWriter(w);
    arguments.acceptCodeWriter(w);
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
  public final ClassInstanceCreationExpression nl() {
    throw new UnsupportedOperationException();
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(ClassInstanceCreationExpression.class);
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
    throw newUoe(ClassInstanceCreationExpression.class);
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
