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
  public void argumentListEnd() {
    level--;

    write(')');
  }

  @Override
  public void argumentListStart() {
    write('(');

    level++;
  }

  @Override
  public void beforeBlockNextItem() {
    nl();
    nl();
  }

  @Override
  public void beforeClassFirstMember() {
    nl();
  }

  @Override
  public void beforeCompilationUnitBody() {
    nl();
  }

  @Override
  public void blockEnd() {
    level--;

    write('}');
    nl();
  }

  @Override
  public void blockStart() {
    write(" {");

    level++;
  }

  @Override
  public void comma() {
    write(", ");
  }

  @Override
  public void compilationUnitEnd() {
  }

  @Override
  public void compilationUnitStart() {
    length = 0;

    level = 0;

    out.setLength(0);
  }

  @Override
  public void newLine() {
    nl();
  }

  @Override
  public void semicolon() {
    write(';');

    nl();
  }

  @Override
  public void separator(char c) {
    write(' ');
    write(c);
    write(' ');
  }

  @Override
  public void space() {
    write(' ');
  }

  @Override
  public void spaceIf(boolean condition) {
    if (condition) {
      space();
    }
  }

  @Override
  public void stringLiteral(String s) {
    write('"');
    write(s);
    write('"');
  }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  public final void write(char c) {
    identation();

    out.append(c);

    length += 1;
  }

  @Override
  public final void write(String s) {
    identation();

    out.append(s);

    length += s.length();
  }

  private void identation() {
    if (length == 0) {
      for (int i = 0; i < level; i++) {
        out.append("  ");

        length += 2;
      }
    }
  }

  private void nl() {
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