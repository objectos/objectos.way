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

  private int level;

  private boolean word;

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
    write('}');
    nl();
  }

  @Override
  public void blockStart() {
    write(' ');
    write('{');
  }

  @Override
  public void classEnd() {
    level--;
  }

  @Override
  public void classStart() {
    level++;

    if (!out.isEmpty()) {
      nl();
    }
  }

  @Override
  public void comma() {
    write(',');
    write(' ');

    word = false;
  }

  @Override
  public void compilationUnitEnd() {
  }

  @Override
  public void compilationUnitStart() {
    level = 0;

    out.setLength(0);

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
  public void methodStart() {
    identation();
  }

  @Override
  public void modifier(String name) {
    word(name);
  }

  @Override
  public void name(String name) {
    word(name);
  }

  @Override
  public void packageEnd() {}

  @Override
  public void packageStart() {}

  @Override
  public void parameterListEnd() {
    out.append(')');
  }

  @Override
  public void parameterListStart() {
    out.append('(');
  }

  @Override
  public void semicolon() {
    out.append(';');
    nl();
  }

  @Override
  public void separator(char c) {
    out.append(' ');
    out.append(c);
    out.append(' ');
  }

  @Override
  public void statementEnd() {
    out.append(';');
    nl();
  }

  @Override
  public void statementStart() {}

  @Override
  public void stringLiteral(String s) {
    out.append('"');
    out.append(s);
    out.append('"');
  }

  @Override
  public final String toString() { return out.toString(); }

  private void identation() {
    for (int i = 0; i < level; i++) {
      out.append("  ");
    }
  }

  private void nl() {
    out.append(System.lineSeparator());

    word = false;
  }

  private void word(String s) {
    if (word) {
      out.append(' ');
    }

    out.append(s);

    word = true;
  }

  private void write(char c) {
    out.append(c);
  }

}