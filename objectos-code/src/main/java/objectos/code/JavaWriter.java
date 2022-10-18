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

  private boolean startOfLine;

  @Override
  public void compilationUnitEnd() {}

  @Override
  public void compilationUnitStart() {}

  @Override
  public final void keyword(Keyword keyword) {
    if (!startOfLine) {
      write(' ');
    }

    write(keyword.toString());
  }

  private void write(char c) {
    out.append(c);
  }

  private void write(String s) {
    out.append(s);
  }

}