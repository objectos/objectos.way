/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html;

import objectos.html.internal.Interpreter;

public class CompiledTemplate {

  private final char[] buffer;

  private final int[] codes;

  public CompiledTemplate(char[] buffer, int[] codes) {
    this.buffer = buffer;
    this.codes = codes;
  }

  public final void acceptTemplateVisitor(CompiledTemplateVisitor visitor) {
    Interpreter interpreter;
    interpreter = new Interpreter(this, visitor);

    interpreter.execute();
  }

  public final int[] codes() {
    return codes;
  }

  public final String getBuffer(int index, int length) {
    return new String(buffer, index, length);
  }

  public final int getCode(int index) {
    return codes[index];
  }

  final int codesLength() {
    return codes.length;
  }

}
