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

import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import br.com.objectos.code.java.statement.Block;
import br.com.objectos.code.java.statement.BlockElement;
import br.com.objectos.code.java.statement.Semicolon;
import br.com.objectos.code.java.statement.Statement;

public abstract class AbstractCodeElement implements CodeElement {

  protected AbstractCodeElement() {}

  protected static CodeElement angled(CodeElement element) {
    return new AngledCodeElement(element);
  }

  protected static CodeElement arrow() {
    return RawCodeElement.ARROW;
  }

  protected static CodeElement beginSection(Section section) {
    return CodeElements.ofSection(section);
  }

  protected static CodeElement bitwiseAnd() {
    return CharCodeElement.BITWISE_AND;
  }

  protected static CodeElement bitwiseExclusiveOr() {
    return CharCodeElement.BITWISE_XOR;
  }

  protected static CodeElement bitwiseOr() {
    return CharCodeElement.BITWISE_OR;
  }

  protected static CodeElement closeAngle() {
    return CharCodeElement.CLOSE_ANGLE;
  }

  protected static CodeElement closeBrace() {
    return Symbols.closeBrace();
  }

  protected static CodeElement closeBracket() {
    return CharCodeElement.CLOSE_BRACKET;
  }

  protected static CodeElement closeParens() {
    return CharCodeElement.CLOSE_PARENS;
  }

  protected static CodeElement colon() {
    return CharCodeElement.COLON;
  }

  protected static CodeElement comma() {
    return CharCodeElement.COMMA;
  }

  protected static CodeElement commaSeparated(CodeElement[] elements) {
    return new ArrayCommaSeparatedCodeElement(elements);
  }

  protected static CodeElement commaSeparated(Iterable<? extends CodeElement> elements) {
    return new CommaSeparatedCodeElement(elements);
  }

  protected static CodeElement dot() {
    return CharCodeElement.DOT;
  }

  protected static CodeElement doubleColon() {
    return RawCodeElement.DOUBLE_COLON;
  }

  protected static CodeElement ellipsis() {
    return Symbols.ellipsis();
  }

  protected static CodeElement endSection() {
    return CodeElements.END_SECTION;
  }

  protected static CodeElement equals() {
    return Symbols.equals();
  }

  protected static CodeElement identifier(String name) {
    return Expressions.id(name);
  }

  protected static CodeElement indentIfNecessary() {
    return CodeElements.INDENT_IF_NECESSARY;
  }

  protected static CodeElement minusMinus() {
    return RawCodeElement.MINUS_MINUS;
  }

  protected static CodeElement newLineSeparated(Iterable<? extends CodeElement> elements) {
    return new NewLineSeparatedCodeElement(elements);
  }

  protected static NoopCodeElement noop() {
    return NoopCodeElement.noop();
  }

  protected static CodeElement openAngle() {
    return CharCodeElement.OPEN_ANGLE;
  }

  protected static CodeElement openBrace() {
    return CharCodeElement.OPEN_BRACE;
  }

  protected static CodeElement openBracket() {
    return CharCodeElement.OPEN_BRACKET;
  }

  protected static CodeElement openParens() {
    return CharCodeElement.OPEN_PARENS;
  }

  protected static CodeElement parenthesized(CodeElement element) {
    return new ParenthesizedCodeElement(element);
  }

  protected static CodeElement plusPlus() {
    return RawCodeElement.PLUS_PLUS;
  }

  protected static CodeElement questionMark() {
    return CharCodeElement.QUESTION_MARK;
  }

  protected static CodeElement quote() {
    return CharCodeElement.QUOTE;
  }

  protected static CodeElement raw(String string) {
    return new RawCodeElement(string);
  }

  protected static CodeElement semicolon() {
    return CharCodeElement.SEMICOLON;
  }

  protected static CodeElement simpleNameElement() {
    return SimpleNameCodeElement.INSTANCE;
  }

  protected static CodeElement space() {
    return SpaceCodeElement.INSTANCE;
  }

  protected static CodeElement spaceSeparated(Iterable<? extends CodeElement> elements) {
    return new SpaceSeparatedCodeElement(elements);
  }

  protected static CodeElement verticalBar() {
    return Symbols.verticalBar();
  }

  protected static CodeElement word(String string) {
    return WordCodeElement.ofString(string);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AbstractCodeElement)) {
      return false;
    }
    AbstractCodeElement that = (AbstractCodeElement) obj;
    return getClass().equals(that.getClass())
        && toString().equals(that.toString());
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  public CodeElement ifEmpty(CodeElement empty, CodeElement notEmpty) {
    return notEmpty;
  }

  @Override
  public String toString() {
    return acceptCodeWriter(CodeWriter.forToString()).toString();
  }

  protected final Statement wrapWithBlockIfNecessary(Statement statement) {
    Statement result = statement;

    if (!(statement instanceof Block)) {
      result = Block.block(statement);
    }

    return result;
  }

  protected final void writeSemicolonIfNecessary(final CodeWriter w, BlockElement element) {
    element.acceptSemicolon(new Semicolon() {
      @Override
      public final void write() {
        w.writeCodeElement(semicolon());
      }
    });
  }

}