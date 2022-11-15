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

class JavaSinkOfStringBuilder extends JavaSink {

  private final StringBuilder out;

  private int length;

  private int level;

  public JavaSinkOfStringBuilder(StringBuilder out) { this.out = out; }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  protected void write(char c) {
    writeIdentation();

    out.append(c);

    length += 1;
  }

  @Override
  protected void write(String s) {
    writeIdentation();

    out.append(s);

    length += s.length();
  }

  @Override
  protected void writeArgumentListEnd() {
    level--;

    write(')');
  }

  @Override
  protected void writeArgumentListStart() {
    write('(');

    level++;
  }

  @Override
  protected void writeBeforeBlockNextMember() {
    writenl();
    writenl();
  }

  @Override
  protected void writeBeforeFirstMember() {
    writenl();
  }

  @Override
  protected void writeBeforeFirstStatement() {
    writeBeforeFirstMember();
  }

  @Override
  protected void writeBeforeNextStatement() {
    writenl();
  }

  @Override
  protected void writeBlockEnd(boolean contents) {
    level--;

    if (contents) {
      writenl();
    }

    write('}');
  }

  @Override
  protected void writeBlockStart() {
    write(" {");

    level++;
  }

  @Override
  protected void writeComma() {
    write(", ");
  }

  @Override
  protected void writeCompilationUnitEnd(PackageName packageName, String fileName) {
    writenl();
  }

  @Override
  protected void writeCompilationUnitSeparator() {
    writenl();

    writenl();
  }

  @Override
  protected void writeCompilationUnitStart(PackageName packageName, String fileName) {
    length = 0;

    level = 0;

    out.setLength(0);
  }

  @Override
  protected void writeNewLine() {
    writenl();
  }

  @Override
  protected void writeOperator(Operator operator) {
    write(' ');

    write(operator.toString());

    write(' ');
  }

  @Override
  protected void writeSemicolon() {
    write(';');
  }

  @Override
  protected void writeSeparator(char c) {
    write(' ');
    write(c);
    write(' ');
  }

  @Override
  protected void writeSpace() {
    write(' ');
  }

  @Override
  protected void writeSpaceIf(boolean condition) {
    if (condition) {
      writeSpace();
    }
  }

  @Override
  protected void writeStringLiteral(String s) {
    write('"');
    write(s);
    write('"');
  }

  private void writeIdentation() {
    if (length == 0) {
      for (int i = 0; i < level; i++) {
        out.append("  ");

        length += 2;
      }
    }
  }

  private void writenl() {
    var builderLength = out.length();

    var newLength = builderLength;

    for (int i = builderLength - 1; i >= 0; i--) {
      var c = out.charAt(i);

      if (c == ' ') {
        newLength--;
      } else {
        break;
      }
    }

    out.setLength(newLength);

    out.append(System.lineSeparator());

    length = 0;
  }

}