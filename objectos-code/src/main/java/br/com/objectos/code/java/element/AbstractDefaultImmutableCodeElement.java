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

import br.com.objectos.code.java.expression.AdditiveExpression;
import br.com.objectos.code.java.expression.ArgumentsElement;
import br.com.objectos.code.java.expression.ArrayAccess;
import br.com.objectos.code.java.expression.ArrayReferenceExpression;
import br.com.objectos.code.java.expression.Assignment;
import br.com.objectos.code.java.expression.AssignmentOperator;
import br.com.objectos.code.java.expression.Callee;
import br.com.objectos.code.java.expression.ConditionalAndExpression;
import br.com.objectos.code.java.expression.ConditionalExpression;
import br.com.objectos.code.java.expression.ConditionalOrExpression;
import br.com.objectos.code.java.expression.EqualityExpression;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.InclusiveOrExpression;
import br.com.objectos.code.java.expression.LambdaExpression;
import br.com.objectos.code.java.expression.LeftHandSide;
import br.com.objectos.code.java.expression.MethodInvocation;
import br.com.objectos.code.java.expression.MethodReference;
import br.com.objectos.code.java.expression.MethodReferenceReferenceExpression;
import br.com.objectos.code.java.expression.MultiplicativeExpression;
import br.com.objectos.code.java.expression.PostDecrementExpression;
import br.com.objectos.code.java.expression.PostIncrementExpression;
import br.com.objectos.code.java.expression.PostfixExpression;
import br.com.objectos.code.java.expression.RelationalExpression;
import br.com.objectos.code.java.expression.ShiftExpression;
import br.com.objectos.code.java.expression.TypeWitness;
import br.com.objectos.code.java.expression.UnaryExpression;
import br.com.objectos.code.java.type.NamedReferenceType;
import objectos.util.UnmodifiableList;

public abstract class AbstractDefaultImmutableCodeElement extends AbstractImmutableCodeElement {

  protected AbstractDefaultImmutableCodeElement(CodeElement... elements) {
    super(elements);
  }

  protected AbstractDefaultImmutableCodeElement(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  // AdditiveExpression

  public final AdditiveExpression add(MultiplicativeExpression rhs) {
    return Expressions.add(selfAdditiveExpression(), rhs);
  }

  public final ArrayAccess aget(Expression e0) {
    return Expressions.aget(selfArrayReferenceExpression(), e0);
  }

  public final ArrayAccess aget(Expression e0, Expression e1) {
    return Expressions.aget(selfArrayReferenceExpression(), e0, e1);
  }

  // ArrayAccess

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

  public final ConditionalAndExpression and(InclusiveOrExpression rhs) {
    return Expressions.and(selfConditionalAndExpression(), rhs);
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

  public final MultiplicativeExpression divide(UnaryExpression rhs) {
    return Expressions.divide(selfMultiplicativeExpression(), rhs);
  }

  public final EqualityExpression eq(RelationalExpression rhs) {
    return Expressions.eq(selfEqualityExpression(), rhs);
  }

  // Assigment

  public final RelationalExpression ge(ShiftExpression rhs) {
    return Expressions.ge(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression gt(ShiftExpression rhs) {
    return Expressions.gt(selfRelationalExpression(), rhs);
  }

  public final RelationalExpression instanceOf(NamedReferenceType typeName) {
    return Expressions.instanceOf(selfRelationalExpression(), typeName);
  }

  // ConditionalAndExpression

  public final MethodInvocation invoke(String methodName) {
    return Expressions.invoke(selfCallee(), methodName);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1) {
    return Expressions.invoke(selfCallee(), methodName, a1);
  }

  // ConditionalExpression

  public final MethodInvocation invoke(String methodName, ArgumentsElement... args) {
    return Expressions.invoke(selfCallee(), methodName, args);
  }

  public final MethodInvocation invoke(String methodName,
      ArgumentsElement a1,
      ArgumentsElement a2) {
    return Expressions.invoke(selfCallee(), methodName, a1, a2);
  }

  // ConditionalOrExpression

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

  // EqualityExpression

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

  // MethodInvocationExpression

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

  public final RelationalExpression le(ShiftExpression rhs) {
    return Expressions.le(selfRelationalExpression(), rhs);
  }

  public final ShiftExpression leftShift(AdditiveExpression expression) {
    return Expressions.leftShift(selfShiftExpression(), expression);
  }

  public final RelationalExpression lt(ShiftExpression rhs) {
    return Expressions.lt(selfRelationalExpression(), rhs);
  }

  public final MultiplicativeExpression multiply(UnaryExpression rhs) {
    return Expressions.multiply(selfMultiplicativeExpression(), rhs);
  }

  public final EqualityExpression ne(RelationalExpression rhs) {
    return Expressions.ne(selfEqualityExpression(), rhs);
  }

  public final ConditionalOrExpression or(ConditionalAndExpression expression) {
    return Expressions.or(selfConditionalOrExpression(), expression);
  }

  public final PostDecrementExpression postDec() {
    return Expressions.postDec(selfPostfixExpression());
  }

  public final PostIncrementExpression postInc() {
    return Expressions.postInc(selfPostfixExpression());
  }

  public final Assignment receive(AssignmentOperator operator, Expression expression) {
    return Expressions.assign(operator, selfLeftHandSide(), expression);
  }

  public final Assignment receive(Expression expression) {
    return Expressions.assign(selfLeftHandSide(), expression);
  }

  // MethodReference

  public final MethodReference ref(String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), methodName);
  }

  public final MethodReference ref(TypeWitness witness, String methodName) {
    return Expressions.ref(selfMethodReferenceReferenceExpression(), witness, methodName);
  }

  public final MultiplicativeExpression remainder(UnaryExpression rhs) {
    return Expressions.remainder(selfMultiplicativeExpression(), rhs);
  }

  // MultiplicativeExpression

  public final ShiftExpression rightShift(AdditiveExpression expression) {
    return Expressions.rightShift(selfShiftExpression(), expression);
  }

  public final AdditiveExpression subtract(MultiplicativeExpression rhs) {
    return Expressions.subtract(selfAdditiveExpression(), rhs);
  }

  public final ConditionalExpression ternary(
      Expression trueExpression, ConditionalExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  public final ConditionalExpression ternary(
      Expression trueExpression, LambdaExpression falseExpression) {
    return Expressions.ternary(selfConditionalOrExpression(), trueExpression, falseExpression);
  }

  // PostDecrementExpression

  public final ShiftExpression unsignedRightShift(AdditiveExpression expression) {
    return Expressions.unsignedRightShift(selfShiftExpression(), expression);
  }

  protected UnsupportedOperationException newUoe(Class<?> typeName) {
    return new UnsupportedOperationException("Not supported for " + typeName.getSimpleName());
  }

  protected AdditiveExpression selfAdditiveExpression() {
    return selfMultiplicativeExpression();
  }

  // RelationalExpression

  protected abstract ArrayReferenceExpression selfArrayReferenceExpression();

  protected abstract Callee selfCallee();

  protected abstract ConditionalAndExpression selfConditionalAndExpression();

  protected ConditionalOrExpression selfConditionalOrExpression() {
    return selfConditionalAndExpression();
  }

  protected final EqualityExpression selfEqualityExpression() {
    return selfRelationalExpression();
  }

  protected abstract LeftHandSide selfLeftHandSide();

  // ShiftExpression

  protected abstract MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression();

  protected abstract MultiplicativeExpression selfMultiplicativeExpression();

  protected abstract PostfixExpression selfPostfixExpression();

  protected abstract RelationalExpression selfRelationalExpression();

  protected ShiftExpression selfShiftExpression() {
    return selfAdditiveExpression();
  }

}
