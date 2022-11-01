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

  private boolean word;

  private boolean parameterList;

  private int parameterListLevel;

  public static JavaWriter of() {
    return new JavaWriter();
  }

  @Override
  public void annotationEnd() {
    nl();
  }

  @Override
  public void annotationStart() {
    write('@');
  }

  @Override
  public void beforeCompilationUnitBody() {
    nl();
  }

  @Override
  public void blockAfterLastItem() {
  }

  @Override
  public void blockBeforeFirstItem() {
    nl();
  }

  @Override
  public void blockBeforeNextItem() {
    nl();
    nl();
  }

  @Override
  public void blockEnd() {
    level--;

    identation();

    write('}');

    nl();
  }

  @Override
  public void blockStart() {
    write(' ');
    write('{');

    level++;
  }

  @Override
  public void comma() {
    write(',');

    word = true;
  }

  @Override
  public void compilationUnitEnd() {
  }

  @Override
  public void compilationUnitStart() {
    length = 0;

    level = 0;

    out.setLength(0);

    word = false;
  }

  @Override
  public void dot() {
    write('.');

    word = false;
  }

  @Override
  public void identifier(String name) {
    word(name);
  }

  @Override
  public final void keyword(String keyword) {
    word(keyword);
  }

  @Override
  public void methodEnd() {}

  @Override
  public void methodStart() {}

  @Override
  public void modifier(String name) {
    word(name);
  }

  @Override
  public void name(String name) {
    word(name);
  }

  @Override
  public void newLine() {
    nl();

    if (parameterList && level == parameterListLevel) {
      level++;
    }
  }

  @Override
  public void parameterListEnd() {
    write(')');

    parameterList = false;
    level = parameterListLevel;
  }

  @Override
  public void parameterListStart() {
    write('(');

    parameterList = true;
    parameterListLevel = level;
    word = false;
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
  }

  @Override
  public void statementEnd() {
    write(';');

    nl();
  }

  @Override
  public void statementStart() {}

  @Override
  public void stringLiteral(String s) {
    identation();

    word();

    write('"');
    write(s);
    write('"');
  }

  @Override
  public final String toString() { return out.toString(); }

  private void identation() {
    if (length == 0) {
      for (int i = 0; i < level; i++) {
        out.append("  ");

        length += 2;
      }
    }
  }

  private void nl() {
    out.append(System.lineSeparator());

    length = 0;

    word = false;
  }

  private void word() {
    if (word) {
      out.append(' ');
    }
  }

  private void word(String s) {
    identation();

    word();

    write(s);

    word = true;
  }

  private void write(char c) {
    out.append(c);

    length += 1;
  }

  private void write(String s) {
    out.append(s);

    length += s.length();
  }

}