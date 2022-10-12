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
import br.com.objectos.code.java.declaration.ThisConstructorInvocation;
import br.com.objectos.code.java.expression.AdditiveExpression;
import br.com.objectos.code.java.expression.Argument;
import br.com.objectos.code.java.expression.Arguments;
import br.com.objectos.code.java.expression.ArgumentsElement;
import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.ConditionalAndExpression;
import br.com.objectos.code.java.expression.ConditionalExpression;
import br.com.objectos.code.java.expression.ConditionalOrExpression;
import br.com.objectos.code.java.expression.EqualityExpression;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.FieldAccess;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.expression.InclusiveOrExpression;
import br.com.objectos.code.java.expression.LambdaExpression;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MultiplicativeExpression;
import br.com.objectos.code.java.expression.PostDecrementExpression;
import br.com.objectos.code.java.expression.PostIncrementExpression;
import br.com.objectos.code.java.expression.PostfixExpression;
import br.com.objectos.code.java.expression.PrimaryNoNewArray;
import br.com.objectos.code.java.expression.RelationalExpression;
import br.com.objectos.code.java.expression.ShiftExpression;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.expression.UnaryExpression;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.type.NamedReferenceType;

public final class ThisKeyword
    extends AbstractCodeElement
    implements
    Argument,
    Keyword,
    ExplicitConstructorInvocation,
    PrimaryNoNewArray {

  private static final ThisKeyword INSTANCE = new ThisKeyword();

  private ThisKeyword() {}

  public static ThisKeyword _this() {
    return INSTANCE;
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgument(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writePreIndentation();
    w.write("this");
    return w;
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    builder.constructorInvocation(this);
  }

  @Override
  public final void acceptConstructorCodeWriter(CodeWriter w) {
    w.writeCodeElement(NewLine.nextLine());
    w.writeCodeElement(ThisConstructorInvocation._this());
    w.writeCodeElement(semicolon());
  }

  @Override
  public final AdditiveExpression add(MultiplicativeExpression rhs) {
    return Expressions.add(selfAdditiveExpression(), rhs);
  }

  @Override
  public final ConditionalAndExpression and(InclusiveOrExpression rhs) {
    return Expressions.and(selfConditionalAndExpression(), rhs);
  }

  @Override
  public final MultiplicativeExpression divide(UnaryExpression rhs) {
    return Expressions.divide(selfMultiplicativeExpression(), rhs);
  }

  @Override
  public final EqualityExpression eq(RelationalExpression rhs) {
    return Expressions.eq(selfEqualityExpression(), rhs);
  }

  @Override
  public final RelationalExpression ge(ShiftExpression rhs) {
    return Expressions.ge(selfRelationalExpression(), rhs);
  }

  @Override
  public final RelationalExpression gt(ShiftExpression rhs) {
    return Expressions.gt(selfRelationalExpression(), rhs);
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
  public final RelationalExpression instanceOf(NamedReferenceType typeName) {
    return Expressions.instanceOf(selfRelationalExpression(), typeName);
  }

  @Override
  public final MethodInvocation invoke(String methodName) {
    return Expressions.invoke(selfCallee(), methodName);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(selfCallee(), methodName, a1);
  }

  @Override
  public final MethodInvocation invoke(String methodName, ArgumentsElement... args) {
    return Expressions.invoke(selfCallee(), methodName, args);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5, a6);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5, a6, a7);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5, a6, a7, a8);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2,
      ArgumentsElement a3,
      ArgumentsElement a4,
      ArgumentsElement a5,
      ArgumentsElement a6,
      ArgumentsElement a7,
      ArgumentsElement a8,
      ArgumentsElement a9) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5, a6, a7, a8, a9);
  }

  @Override
  public final MethodInvocation invoke(String methodName,
      Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(selfCallee(), methodName, args);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName) {
    return Expressions.invoke(selfCallee(), witness, methodName);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2, a3);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2, a3, a4);
  }

  @Override
  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(selfCallee(), witness, methodName, args);
  }

  @Override
  public final RelationalExpression le(ShiftExpression rhs) {
    return Expressions.le(selfRelationalExpression(), rhs);
  }

  // RelationalExpression

  @Override
  public final ShiftExpression leftShift(AdditiveExpression expression) {
    return Expressions.leftShift(selfShiftExpression(), expression);
  }

  @Override
  public final RelationalExpression lt(ShiftExpression rhs) {
    return Expressions.lt(selfRelationalExpression(), rhs);
  }

  @Override
  public final MultiplicativeExpression multiply(UnaryExpression rhs) {
    return Expressions.multiply(selfMultiplicativeExpression(), rhs);
  }

  @Override
  public final EqualityExpression ne(RelationalExpression rhs) {
    return Expressions.ne(selfEqualityExpression(), rhs);
  }

  @Override
  public final PostfixExpression nl() {
    throw new UnsupportedOperationException();
  }

  // ShiftExpression

  @Override
  public final ConditionalOrExpression or(ConditionalAndExpression expression) {
    return Expressions.or(selfConditionalOrExpression(), expression);
  }

  @Override
  public final PostDecrementExpression postDec() {
    return Expressions.postDec(selfPostfixExpression());
  }

  @Override
  public final PostIncrementExpression postInc() {
    return Expressions.postInc(selfPostfixExpression());
  }

  @Override
  public final MethodReference ref(String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), methodName);
  }

  @Override
  public final MethodReference ref(TypeWitness witness, String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), witness, methodName);
  }

  @Override
  public final MultiplicativeExpression remainder(UnaryExpression rhs) {
    return Expressions.remainder(selfMultiplicativeExpression(), rhs);
  }

  @Override
  public final ShiftExpression rightShift(AdditiveExpression expression) {
    return Expressions.rightShift(selfShiftExpression(), expression);
  }

  @Override
  public final AdditiveExpression subtract(MultiplicativeExpression rhs) {
    return Expressions.subtract(selfAdditiveExpression(), rhs);
  }

  @Override
  public final ConditionalExpression ternary(
      Expression trueExpression, ConditionalExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  @Override
  public final ConditionalExpression ternary(
      Expression trueExpression, LambdaExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  @Override
  public final ShiftExpression unsignedRightShift(AdditiveExpression expression) {
    return Expressions.unsignedRightShift(selfShiftExpression(), expression);
  }

  private AdditiveExpression selfAdditiveExpression() {
    return selfMultiplicativeExpression();
  }

  private Callee selfCallee() {
    return this;
  }

  private ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  private ConditionalOrExpression selfConditionalOrExpression() {
    return selfConditionalAndExpression();
  }

  private EqualityExpression selfEqualityExpression() {
    return selfRelationalExpression();
  }

  private MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    return this;
  }

  private MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  private PostfixExpression selfPostfixExpression() {
    return this;
  }

  private RelationalExpression selfRelationalExpression() {
    return this;
  }

  private ShiftExpression selfShiftExpression() {
    return selfAdditiveExpression();
  }

}