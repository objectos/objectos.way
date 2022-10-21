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

  private int lineStart;

  @Override
  public void blockEnd() {
    write('}');
  }

  @Override
  public void blockStart() {
    write(' ');
    write('{');
  }

  @Override
  public void classEnd() {}

  @Override
  public void classStart() {
    if (!out.isEmpty()) {
      nl();
    }
  }

  @Override
  public void compilationUnitEnd() {
    nl();
  }

  @Override
  public void compilationUnitStart() {
    out.setLength(0);

    lineStart = 0;
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
  public void semicolon() {
    out.append(';');
    nl();
  }

  @Override
  public final String toString() { return out.toString(); }

  private void nl() {
    out.append(System.lineSeparator());

    lineStart = out.length();
  }

  private void word(String s) {
    int length = out.length();

    if (length != lineStart) {
      out.append(' ');
    }

    out.append(s);
  }

  private void write(char c) {
    out.append(c);
  }

}