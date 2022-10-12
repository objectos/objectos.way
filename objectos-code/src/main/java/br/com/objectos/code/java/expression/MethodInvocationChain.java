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

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.NewLine;
import br.com.objectos.code.java.expression.MethodInvocation.Unqualified;
import br.com.objectos.code.java.expression.production.MethodInvocationExpression;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public class MethodInvocationChain extends AbstractStatementExpression
    implements
    MethodInvocationExpression {

  private final Callee caller;
  private final UnmodifiableList<CodeElement> elements;

  MethodInvocationChain(Callee caller, UnmodifiableList<CodeElement> elements) {
    this.caller = caller;
    this.elements = elements;
  }

  @Ignore
  public static Builder builder() {
    return new Builder();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement... elements) {
    Check.notNull(caller, "caller == null");
    Check.notNull(elements, "elements == null");
    Builder b = builder();
    b.setStart(caller);

    for (int i = 0; i < elements.length; i++) {
      MethodInvocationChainElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      element.acceptMethodInvocationChainBuilder(b);
    }

    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4,
      MethodInvocationChainElement e5) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    e5.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4,
      MethodInvocationChainElement e5,
      MethodInvocationChainElement e6) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    e5.acceptMethodInvocationChainBuilder(b);
    e6.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4,
      MethodInvocationChainElement e5,
      MethodInvocationChainElement e6,
      MethodInvocationChainElement e7) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    e5.acceptMethodInvocationChainBuilder(b);
    e6.acceptMethodInvocationChainBuilder(b);
    e7.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4,
      MethodInvocationChainElement e5,
      MethodInvocationChainElement e6,
      MethodInvocationChainElement e7,
      MethodInvocationChainElement e8) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    e5.acceptMethodInvocationChainBuilder(b);
    e6.acceptMethodInvocationChainBuilder(b);
    e7.acceptMethodInvocationChainBuilder(b);
    e8.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  public static MethodInvocationChain chain(
      Callee caller,
      MethodInvocationChainElement e1,
      MethodInvocationChainElement e2,
      MethodInvocationChainElement e3,
      MethodInvocationChainElement e4,
      MethodInvocationChainElement e5,
      MethodInvocationChainElement e6,
      MethodInvocationChainElement e7,
      MethodInvocationChainElement e8,
      MethodInvocationChainElement e9) {
    Check.notNull(caller, "caller == null");
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Check.notNull(e9, "e9 == null");
    Builder b = builder();
    b.setStart(caller);
    e1.acceptMethodInvocationChainBuilder(b);
    e2.acceptMethodInvocationChainBuilder(b);
    e3.acceptMethodInvocationChainBuilder(b);
    e4.acceptMethodInvocationChainBuilder(b);
    e5.acceptMethodInvocationChainBuilder(b);
    e6.acceptMethodInvocationChainBuilder(b);
    e7.acceptMethodInvocationChainBuilder(b);
    e8.acceptMethodInvocationChainBuilder(b);
    e9.acceptMethodInvocationChainBuilder(b);
    return b.build();
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    caller.acceptCodeWriter(w);

    for (int i = 0; i < elements.size(); i++) {
      CodeElement element;
      element = elements.get(i);

      element.acceptCodeWriter(w);
    }

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
  public final MethodInvocation nl() {
    throw new UnsupportedOperationException();
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

  public static class Builder {

    private final GrowableList<CodeElement> elements = new GrowableList<>();
    private Callee start;

    public final Builder addMethodInvocation(Unqualified invocation) {
      Check.notNull(invocation, "invocation == null");

      UnqualifiedWrapper wrapper;
      wrapper = new UnqualifiedWrapper(invocation);

      elements.add(wrapper);
      return this;
    }

    public final Builder addNewLine() {
      elements.add(NewLine.nextLine());
      return this;
    }

    public final MethodInvocationChain build() {
      return new MethodInvocationChain(
          start,
          elements.toUnmodifiableList()
      );
    }

    public final Builder setStart(Callee caller) {
      start = Check.notNull(caller, "caller == null");
      return this;
    }

  }

  private static class UnqualifiedWrapper implements CodeElement {

    private final Unqualified invocation;

    UnqualifiedWrapper(Unqualified invocation) {
      this.invocation = invocation;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writePreIndentation();
      w.write('.');
      invocation.acceptCodeWriter(w);
      return w;
    }

  }

}
