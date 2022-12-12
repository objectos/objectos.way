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
package objectos.code;

class JavaSinkOfStringBuilder2 extends JavaSink2 {

  private final StringBuilder out;

  public JavaSinkOfStringBuilder2(StringBuilder out) { this.out = out; }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  protected void writeCompilationUnitEnd(String packageName, String fileName) {
    writenl();
  }

  @Override
  protected void writeCompilationUnitStart(String packageName, String fileName) {
    out.setLength(0);

    markIndex = out.length();
  }

  @Override
  protected final void writeIdentifier(String name) {
    out.append(name);
  }

  @Override
  protected final void writeLiteral(String value) {
    out.append(value);
  }

  @Override
  protected final void writeName(String name) {
    out.append(name);
  }

  @Override
  protected final void writeOperator(Operator2 operator) {
    out.append(operator.toString());
  }

  @Override
  protected final void writePseudoElement(PseudoElement value) {
    switch (value) {
      case BEFORE_NEXT_TOP_LEVEL_ITEM -> { writenl(); writenl(); }

      case BEFORE_NEXT_STATEMENT -> writenl();

      case BEFORE_NEXT_COMMA_SEPARATED_ITEM -> out.append(' ');

      case INDENTATION -> writeIndentation(level());

      case CONTINUATION_INDENTATION -> writeIndentation(level() + 2);

      default -> {}
    }
  }

  @Override
  protected final void writeReservedKeyword(Keyword value) {
    out.append(value);
  }

  @Override
  protected final void writeSeparator(Separator value) {
    switch (value) {
      case LEFT_PARENTHESIS -> { levelIncrease(); out.append(value); }

      case RIGHT_PARENTHESIS -> { levelDecrease(); out.append(value); }

      default -> out.append(value);
    }
  }

  @Override
  protected final void writeStringLiteral(String value) {
    out.append('"');

    out.append(value);

    out.append('"');
  }

  @Override
  protected final void writeWhitespace(Whitespace value) {
    switch (value) {
      case MANDATORY, OPTIONAL -> out.append(' ');

      case NEW_LINE -> writenl();
    }
  }

  private int level() {
    return protoIndex;
  }

  private void levelDecrease() {
    protoIndex--;
  }

  private void levelIncrease() {
    protoIndex++;
  }

  private void writeIndentation(int length) {
    for (int i = 0; i < length; i++) {
      out.append("  ");
    }
  }

  private void writenl() {
    out.append(System.lineSeparator());

    markIndex = out.length();
  }

}