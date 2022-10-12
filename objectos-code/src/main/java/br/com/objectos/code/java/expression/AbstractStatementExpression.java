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

import br.com.objectos.code.java.statement.AbstractSimpleStatement;
import br.com.objectos.code.java.type.NamedReferenceType;

abstract class AbstractStatementExpression
    extends AbstractSimpleStatement
    implements StatementExpression {

  // AdditiveExpression

  public final AdditiveExpression add(MultiplicativeExpression rhs) {
    return Expressions.add(selfAdditiveExpression(), rhs);
  }

  public final AdditiveExpression subtract(MultiplicativeExpression rhs) {
    return Expressions.subtract(selfAdditiveExpression(), rhs);
  }

  protected AdditiveExpression selfAdditiveExpression() {
    return selfMultiplicativeExpression();
  }

  // ArrayAccess

  public final ArrayAccess aget(Expression e0) {
    return Expressions.aget(selfArrayReferenceExpression(), e0);
  }

  public final ArrayAccess aget(Expression e0, Expression e1) {
    return Expressions.aget(selfArrayReferenceExpression(), e0, e1);
  }

  public final ArrayAccess aget(Expression e0, Expression e1, Expression e2) {
    return Expressions.aget(selfArrayReferenceExpression(), e0, e1, e2);
  }

  public final ArrayAccess aget(
      Expression e0, Expression e1, Expression e2, Expression e3) {
    return Expressions.aget(selfArrayReferenceExpression(), e0, e1, e2, e3);
  }

  public final ArrayAccess aget(Iterable<? extends Expression> expressions) {
    return Expressions.aget(selfArrayReferenceExpression(), expressions);
  }

  public final ArrayAccess arrayAccess(Expression e0) {
    return Expressions.arrayAccess(selfArrayReferenceExpression(), e0);
  }

  public final ArrayAccess arrayAccess(Expression e0, Expression e1) {
    return Expressions.arrayAccess(selfArrayReferenceExpression(), e0, e1);
  }

  public final ArrayAccess arrayAccess(Expression e0, Expression e1,
      Expression e2) {
    return Expressions.arrayAccess(selfArrayReferenceExpression(), e0, e1, e2);
  }

  public final ArrayAccess arrayAccess(
      Expression e0, Expression e1, Expression e2, Expression e3) {
    return Expressions.arrayAccess(selfArrayReferenceExpression(), e0, e1, e2, e3);
  }

  public final ArrayAccess arrayAccess(Iterable<? extends Expression> expressions) {
    return Expressions.arrayAccess(selfArrayReferenceExpression(), expressions);
  }

  protected abstract ArrayReferenceExpression selfArrayReferenceExpression();

  // Assigment

  public final Assignment receive(Expression expression) {
    return Expressions.assign(selfLeftHandSide(), expression);
  }

  public final Assignment receive(AssignmentOperator operator, Expression expression) {
    return Expressions.assign(operator, selfLeftHandSide(), expression);
  }

  protected abstract LeftHandSide selfLeftHandSide();

  // ConditionalAndExpression

  public final ConditionalAndExpression and(InclusiveOrExpression rhs) {
    return Expressions.and(selfConditionalAndExpression(), rhs);
  }

  protected abstract ConditionalAndExpression selfConditionalAndExpression();

  // ConditionalExpression

  public final ConditionalExpression ternary(
      Expression trueExpression, ConditionalExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  public final ConditionalExpression ternary(
      Expression trueExpression, LambdaExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  // ConditionalOrExpression

  public final ConditionalOrExpression or(ConditionalAndExpression expression) {
    return Expressions.or(selfConditionalOrExpression(), expression);
  }

  protected ConditionalOrExpression selfConditionalOrExpression() {
    return selfConditionalAndExpression();
  }

  // EqualityExpression

  public final EqualityExpression eq(RelationalExpression rhs) {
    return Expressions.eq(selfEqualityExpression(), rhs);
  }

  public final EqualityExpression ne(RelationalExpression rhs) {
    return Expressions.ne(selfEqualityExpression(), rhs);
  }

  protected final EqualityExpression selfEqualityExpression() {
    return selfRelationalExpression();
  }

  // MethodInvocationExpression

  public final MethodInvocation invoke(String methodName) {
    return Expressions.invoke(selfCallee(), methodName);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(selfCallee(), methodName, a1);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1, 
      ArgumentsElement a2) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1, 
      ArgumentsElement a2, 
      ArgumentsElement a3) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1, 
      ArgumentsElement a2, 
      ArgumentsElement a3, 
      ArgumentsElement a4) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1, 
      ArgumentsElement a2, 
      ArgumentsElement a3, 
      ArgumentsElement a4, 
      ArgumentsElement a5) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1, 
      ArgumentsElement a2, 
      ArgumentsElement a3, 
      ArgumentsElement a4, 
      ArgumentsElement a5, 
      ArgumentsElement a6) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2, a3, a4, a5, a6);
  }

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

  public final MethodInvocation invoke(String methodName, ArgumentsElement... args) {
    return Expressions.invoke(selfCallee(), methodName, args);
  }

  public final MethodInvocation invoke(String methodName,
      Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(selfCallee(), methodName, args);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName) {
    return Expressions.invoke(selfCallee(), witness, methodName);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2, a3);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      ArgumentsElement a1, ArgumentsElement a2, ArgumentsElement a3, ArgumentsElement a4) {
    return Expressions.invoke(selfCallee(), witness, methodName, a1, a2, a3, a4);
  }

  public final MethodInvocation invoke(TypeWitness witness, String methodName,
      Iterable<? extends ArgumentsElement> args) {
    return Expressions.invoke(selfCallee(), witness, methodName, args);
  }

  protected abstract Callee selfCallee();

  // MethodReference

  public final MethodReference ref(String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), methodName);
  }

  public final MethodReference ref(TypeWitness witness, String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), witness, methodName);
  }

  protected abstract MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression();

  // MultiplicativeExpression

  public final MultiplicativeExpression divide(UnaryExpression rhs) {
    return Expressions.divide(selfMultiplicativeExpression(), rhs);
  }

  public final MultiplicativeExpression multiply(UnaryExpression rhs) {
    return Expressions.multiply(selfMultiplicativeExpression(), rhs);
  }

  public final MultiplicativeExpression remainder(UnaryExpression rhs) {
    return Expressions.remainder(selfMultiplicativeExpression(), rhs);
  }

  protected abstract MultiplicativeExpression selfMultiplicativeExpression();

  // PostDecrementExpression

  public final PostDecrementExpression postDec() {
    return Expressions.postDec(selfPostfixExpression());
  }

  public final PostIncrementExpression postInc() {
    return Expressions.postInc(selfPostfixExpression());
  }

  protected abstract PostfixExpression selfPostfixExpression();

  // RelationalExpression

  public final RelationalExpression lt(ShiftExpression rhs) {
    return Expressions.lt(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression gt(ShiftExpression rhs) {
    return Expressions.gt(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression le(ShiftExpression rhs) {
    return Expressions.le(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression ge(ShiftExpression rhs) {
    return Expressions.ge(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression instanceOf(NamedReferenceType typeName) {
    return Expressions.instanceOf(selfRelationalExpression(), typeName);
  }

  protected abstract RelationalExpression selfRelationalExpression();

  // ShiftExpression

  public final ShiftExpression leftShift(AdditiveExpression expression) {
    return Expressions.leftShift(selfShiftExpression(), expression);
  }

  public final ShiftExpression rightShift(AdditiveExpression expression) {
    return Expressions.rightShift(selfShiftExpression(), expression);
  }

  public final ShiftExpression unsignedRightShift(AdditiveExpression expression) {
    return Expressions.unsignedRightShift(selfShiftExpression(), expression);
  }

  protected ShiftExpression selfShiftExpression() {
    return selfAdditiveExpression();
  }

  protected UnsupportedOperationException newUoe(Class<?> typeName) {
    return new UnsupportedOperationException("Not supported for " + typeName.getSimpleName());
  }
  
}
