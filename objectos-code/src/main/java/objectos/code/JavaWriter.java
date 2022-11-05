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

public class JavaWriter implements JavaTemplate.Renderer {

  private final StringBuilder out = new StringBuilder();

  private int length;

  private int level;

  public static JavaWriter of() {
    return new JavaWriter();
  }

  @Override
  public void writeArgumentListEnd() {
    level--;

    write(')');
  }

  @Override
  public void writeArgumentListStart() {
    write('(');

    level++;
  }

  @Override
  public void writeBeforeBlockNextItem() {
    writenl();
    writenl();
  }

  @Override
  public void writeBeforeClassFirstMember() {
    writenl();
  }

  @Override
  public void writeBeforeCompilationUnitBody() {
    writenl();
  }

  @Override
  public void writeCompilationUnitEnd() {
  }

  @Override
  public void writeCompilationUnitStart() {
    length = 0;

    level = 0;

    out.setLength(0);
  }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  public final void write(char c) {
    writeIdentation();

    out.append(c);

    length += 1;
  }

  @Override
  public final void write(String s) {
    writeIdentation();

    out.append(s);

    length += s.length();
  }

  @Override
  public void writeBlockEnd() {
    level--;

    write('}');
    writenl();
  }

  @Override
  public void writeBlockStart() {
    write(" {");

    level++;
  }

  @Override
  public void writeComma() {
    write(", ");
  }

  @Override
  public void writeNewLine() {
    writenl();
  }

  @Override
  public void writeSemicolon() {
    write(';');

    writenl();
  }

  @Override
  public void writeSeparator(char c) {
    write(' ');
    write(c);
    write(' ');
  }

  @Override
  public void writeSpace() {
    write(' ');
  }

  @Override
  public void writeSpaceIf(boolean condition) {
    if (condition) {
      writeSpace();
    }
  }

  @Override
  public void writeStringLiteral(String s) {
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