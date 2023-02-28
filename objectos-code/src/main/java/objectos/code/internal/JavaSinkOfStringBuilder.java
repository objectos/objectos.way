/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import objectos.code.JavaSink;

public class JavaSinkOfStringBuilder extends JavaSink {

  private final StringBuilder out;

  public JavaSinkOfStringBuilder(StringBuilder out) {
    this.out = out;
  }

  @Override
  public final String toString() {
    return out.toString();
  }

  @Override
  protected final void writeComment(String value) {
    out.append("/* ");
    out.append(value);
    out.append(" */");
  }

  @Override
  protected void writeCompilationUnitEnd(String packageName, String fileName) {
    writenl();
  }

  @Override
  protected void writeCompilationUnitStart(String packageName, String fileName) {
    out.setLength(0);
  }

  @Override
  protected final void writeIdentifier(String name) { out.append(name); }

  @Override
  protected final void writeIndentation(Indentation value) {
    switch (value) {
      case CONTINUATION -> writeIndentation(level() + 2);

      case ENTER_BLOCK -> levelIncrease();

      case EXIT_BLOCK -> levelDecrease();

      case ENTER_PARENTHESIS -> levelIncrease();

      case EXIT_PARENTHESIS -> levelDecrease();

      default -> {}
    }
  }

  @Override
  protected final void writeLiteral(String value) { out.append(value); }

  @Override
  protected final void writeName(String name) { out.append(name); }

  @Override
  protected final void writeRaw(String value) { out.append(value); }

  @Override
  protected final void writeKeyword(Keyword value) { out.append(value); }

  @Override
  protected final void writeStringLiteral(String value) {
    out.append('"');
    out.append(value);
    out.append('"');
  }

  @Override
  protected final void writeSymbol(Symbol value) { out.append(value); }

  @Override
  protected final void writeWhitespace(Whitespace value) {
    switch (value) {
      case AFTER_ANNOTATION -> { writenl(); writeIndentation(level()); }

      case BEFORE_EMPTY_BLOCK_END -> {}

      case BEFORE_FIRST_LINE_CONTENT -> { writeIndentation(level()); }

      case BEFORE_FIRST_MEMBER -> { writenl(); writeIndentation(level()); }

      case BEFORE_NEXT_COMMA_SEPARATED_ITEM -> out.append(' ');

      case BEFORE_NEXT_MEMBER -> { writenl(); writenl(); writeIndentation(level()); }

      case BEFORE_NEXT_STATEMENT -> { writenl(); writeIndentation(level()); }

      case BEFORE_NON_EMPTY_BLOCK_END -> { writenl(); writeIndentation(level()); }

      case MANDATORY, OPTIONAL -> out.append(' ');

      case NEW_LINE -> writenl();
    }
  }

  private void writeIndentation(int length) {
    for (int i = 0; i < length; i++) {
      out.append("  ");
    }
  }

  private void writenl() {
    out.append(System.lineSeparator());
  }

}