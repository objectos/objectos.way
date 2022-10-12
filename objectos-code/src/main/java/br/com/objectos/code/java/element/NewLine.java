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

import br.com.objectos.code.java.declaration.ClassBodyElement;
import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.InterfaceBodyElement;
import br.com.objectos.code.java.declaration.InterfaceCode;
import br.com.objectos.code.java.expression.Arguments;
import br.com.objectos.code.java.expression.ArgumentsElement;
import br.com.objectos.code.java.expression.MethodInvocationChain;
import br.com.objectos.code.java.expression.MethodInvocationChainElement;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.NewLineFormatting.NewLineFormattingAction;
import br.com.objectos.code.java.statement.BlockElement;
import br.com.objectos.code.java.statement.ForStatement;
import br.com.objectos.code.java.statement.IfStatement;
import br.com.objectos.code.java.statement.Semicolon;
import br.com.objectos.code.java.statement.StatementOrBlockBuilder;

public abstract class NewLine extends AbstractCodeElement
    implements
    ArgumentsElement,
    BlockElement,
    ClassBodyElement,
    InterfaceBodyElement,
    MethodInvocationChainElement {

  private static final NextLine NEXT_LINE = new NextLine();
  private static final Single SINGLE = new Single();

  NewLine() {}

  public static CodeElement nextLine() {
    return NEXT_LINE;
  }

  public static NewLine nl() {
    return SINGLE;
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addNewLine();
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addNewLine(this);
  }

  @Override
  public final void acceptForStatementBuilder(ForStatement.Builder builder) {
    builder.addNewLine(this);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.addNewLine(this);
  }
  
  @Override
  public final void acceptMethodInvocationChainBuilder(MethodInvocationChain.Builder builder) {
    builder.addNewLine();
  }

  @Override
  public final void acceptNewLineFormattingAction(NewLineFormattingAction action) {
    action.consumeNewLine(this);
  }

  @Override
  public final void acceptSemicolon(Semicolon semicolon) {
    // noop
  }

  @Override
  public final void acceptStatementOrBlockBuilder(StatementOrBlockBuilder builder) {
    builder.nl();
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof NewLine)) {
      return false;
    }
    NewLine that = (NewLine) obj;
    return getClass().equals(that.getClass());
  }

  @Override
  public abstract int hashCode();

  @Override
  public final Kind kind() {
    return Kind.NEW_LINE;
  }

  private static class NextLine extends AbstractCodeElement {
    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w.nextLine();
    }
  }

  private static class Single extends NewLine {
    private Single() {}

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      // this assumes (correctly?) that this will be only used
      // as a ClassBodyElement inside a w.writeClassBody()
      return w;
    }

    @Override
    public final void acceptIfStatementBuilder(IfStatement.Builder builder) {
      builder.addToCurrentBlock(this);
    }

    @Override
    public final int hashCode() {
      return 0;
    }
  }

}