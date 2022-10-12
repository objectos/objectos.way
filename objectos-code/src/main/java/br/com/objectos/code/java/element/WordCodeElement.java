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
package br.com.objectos.code.java.element;

import br.com.objectos.code.java.io.CodeWriter;

abstract class WordCodeElement extends AbstractCodeElement {

  WordCodeElement() {}

  static CodeElement ofChar(char c) {
    return new OfCharWordCodeElement(c);
  }

  static CodeElement ofString(String string) {
    return new OfStringWordCodeElement(string);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writePreIndentation();
    writeValue(w);
    return w;
  }

  abstract void writeValue(CodeWriter w);

  private static class OfCharWordCodeElement extends WordCodeElement {
    private final char value;

    OfCharWordCodeElement(char value) {
      this.value = value;
    }

    @Override
    final void writeValue(CodeWriter w) {
      w.write(value);
    }
  }

  private static class OfStringWordCodeElement extends WordCodeElement {
    private final String value;

    OfStringWordCodeElement(String value) {
      this.value = value;
    }

    @Override
    final void writeValue(CodeWriter w) {
      w.write(value);
    }
  }

}
