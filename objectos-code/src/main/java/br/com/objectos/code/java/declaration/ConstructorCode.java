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
package br.com.objectos.code.java.declaration;

import static br.com.objectos.code.java.element.NewLine.nextLine;

import br.com.objectos.code.annotations.Ignore;
import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.NewLineFormatting.NewLineFormattingAction;
import br.com.objectos.code.java.io.Section;
import br.com.objectos.code.java.statement.BlockElement;
import br.com.objectos.code.java.statement.BlockStatement;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public final class ConstructorCode
    extends AbstractCodeElement
    implements
    ClassBodyElement,
    EnumBodyElement {

  private static final ConstructorCode DEFAULT = builder().build();
  private static final ConstructorCode PRIVATE = ofModifier(Modifiers.PRIVATE);
  private static final ConstructorCode PROTECTED = ofModifier(Modifiers.PROTECTED);
  private static final ConstructorCode PUBLIC = ofModifier(Modifiers.PUBLIC);

  private final UnmodifiableList<BlockElement> body;

  private final ExplicitConstructorInvocation constructorInvocation;
  private final ConstructorModifier modifier;
  private final UnmodifiableList<ParameterCode> parameters;

  private ConstructorCode(ConstructorModifier modifier,
                          UnmodifiableList<ParameterCode> parameters,
                          ExplicitConstructorInvocation constructorInvocation,
                          UnmodifiableList<BlockElement> body) {
    this.modifier = modifier;
    this.parameters = parameters;
    this.constructorInvocation = constructorInvocation;
    this.body = body;
  }

  @Ignore("AggregatorGenProcessor")
  public static Builder builder() {
    return new Builder();
  }

  public static ConstructorCode constructor() {
    return DEFAULT;
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1) {
    Check.notNull(e1, "e1 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(ConstructorCodeElement... elements) {
    Check.notNull(elements, "elements == null");
    Builder b = builder();

    for (int i = 0; i < elements.length; i++) {
      ConstructorCodeElement element;
      element = elements[i];

      if (element == null) {
        throw new NullPointerException("elements[" + i + "] == null");
      }

      element.acceptConstructorCodeBuilder(b);
    }

    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4,
      ConstructorCodeElement e5) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    e5.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4,
      ConstructorCodeElement e5,
      ConstructorCodeElement e6) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    e5.acceptConstructorCodeBuilder(b);
    e6.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4,
      ConstructorCodeElement e5,
      ConstructorCodeElement e6,
      ConstructorCodeElement e7) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    e5.acceptConstructorCodeBuilder(b);
    e6.acceptConstructorCodeBuilder(b);
    e7.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4,
      ConstructorCodeElement e5,
      ConstructorCodeElement e6,
      ConstructorCodeElement e7,
      ConstructorCodeElement e8) {
    Check.notNull(e1, "e1 == null");
    Check.notNull(e2, "e2 == null");
    Check.notNull(e3, "e3 == null");
    Check.notNull(e4, "e4 == null");
    Check.notNull(e5, "e5 == null");
    Check.notNull(e6, "e6 == null");
    Check.notNull(e7, "e7 == null");
    Check.notNull(e8, "e8 == null");
    Builder b = builder();
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    e5.acceptConstructorCodeBuilder(b);
    e6.acceptConstructorCodeBuilder(b);
    e7.acceptConstructorCodeBuilder(b);
    e8.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode constructor(
      ConstructorCodeElement e1,
      ConstructorCodeElement e2,
      ConstructorCodeElement e3,
      ConstructorCodeElement e4,
      ConstructorCodeElement e5,
      ConstructorCodeElement e6,
      ConstructorCodeElement e7,
      ConstructorCodeElement e8,
      ConstructorCodeElement e9) {
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
    e1.acceptConstructorCodeBuilder(b);
    e2.acceptConstructorCodeBuilder(b);
    e3.acceptConstructorCodeBuilder(b);
    e4.acceptConstructorCodeBuilder(b);
    e5.acceptConstructorCodeBuilder(b);
    e6.acceptConstructorCodeBuilder(b);
    e7.acceptConstructorCodeBuilder(b);
    e8.acceptConstructorCodeBuilder(b);
    e9.acceptConstructorCodeBuilder(b);
    return b.build();
  }

  public static ConstructorCode privateConstructor() {
    return PRIVATE;
  }

  public static ConstructorCode protectedConstructor() {
    return PROTECTED;
  }

  public static ConstructorCode publicConstructor() {
    return PUBLIC;
  }

  private static ConstructorCode ofModifier(ConstructorModifier modifier) {
    return builder().addModifier(modifier).build();
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addConstructor(this);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(modifier);
    w.writeCodeElement(space());
    w.writeCodeElement(simpleNameElement());
    w.writeCodeElement(parenthesized(commaSeparated(parameters)));
    w.writeCodeElement(space());
    writeBody(w);

    return w;
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    builder.addConstructor(this);
  }

  @Override
  public final void acceptNewLineFormattingAction(NewLineFormattingAction action) {
    action.propagateElement(this);
  }

  @Override
  public final Kind kind() {
    return Kind.CONSTRUCTOR;
  }

  @Override
  public final String toString() {
    return acceptCodeWriter(
        CodeWriter.forToString().pushSimpleName("Constructor")
    ).popSimpleName().toString();
  }

  private void writeBody(CodeWriter w) {
    w.writeCodeElement(openBrace());
    w.beginSection(Section.BLOCK);

    int count = w.charCount();

    constructorInvocation.acceptConstructorCodeWriter(w);

    for (BlockElement element : body) {
      w.writeCodeElement(nextLine());
      w.beginSection(Section.STATEMENT);
      w.writeCodeElement(element);
      w.endSection();
      writeSemicolonIfNecessary(w, element);
    }

    if (count != w.charCount()) {
      w.writeCodeElement(nextLine());
    }

    w.endSection();
    w.writeCodeElement(closeBrace());
  }

  public static class Builder {

    private ConstructorModifier accessModifier = Nothing.INSTANCE;
    private final GrowableList<BlockElement> body = new GrowableList<>();
    private ExplicitConstructorInvocation constructorInvocation = Nothing.INSTANCE;
    private final GrowableList<ParameterCode> parameters = new GrowableList<>();

    private Builder() {}

    public final Builder addModifier(ConstructorModifier modifier) {
      accessModifier = Check.notNull(modifier, "modifier == null");
      return this;
    }

    public final Builder addParameter(ParameterCode parameter) {
      Check.notNull(parameter, "parameter == null");
      return withParameterUnchecked(parameter);
    }

    public final Builder addParameters(Iterable<ParameterCode> parameters) {
      Check.notNull(parameters, "parameters == null");
      for (ParameterCode parameter : parameters) {
        addParameter(parameter);
      }
      return this;
    }

    public final Builder addStatement(BlockStatement statement) {
      body.addWithNullMessage(statement, "statement == null");
      return this;
    }

    public final Builder addStatements(Iterable<? extends BlockStatement> statements) {
      body.addAllIterable(statements);
      return this;
    }

    public final ConstructorCode build() {
      return new ConstructorCode(
          accessModifier,
          parameters.toUnmodifiableList(),
          constructorInvocation,
          body.toUnmodifiableList()
      );
    }

    public final Builder constructorInvocation(ExplicitConstructorInvocation invocation) {
      constructorInvocation = Check.notNull(invocation, "invocation == null");
      return this;
    }

    private Builder withParameterUnchecked(ParameterCode parameter) {
      parameters.add(parameter);
      return this;
    }

  }

  private enum Nothing
      implements
      ConstructorModifier,
      ExplicitConstructorInvocation {

    INSTANCE;

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      // noop
      return w;
    }

    @Override
    public final void acceptConstructorCodeBuilder(Builder builder) {
      throw new AssertionError();
    }

    @Override
    public final void acceptConstructorCodeWriter(CodeWriter w) {
      // noop
    }

  }

}