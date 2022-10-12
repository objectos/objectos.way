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

class ArrayCommaSeparatedCodeElement implements CodeElement {

  private final CodeElement[] elements;

  ArrayCommaSeparatedCodeElement(CodeElement[] elements) {
    this.elements = elements;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    switch (elements.length) {
      case 0:
        return acceptCodeWriter0(w);
      case 1:
        return acceptCodeWriter1(w);
      default:
        return acceptCodeWriterN(w);
    }
  }

  private CodeWriter acceptCodeWriter0(CodeWriter w) {
    return w;
  }

  private CodeWriter acceptCodeWriter1(CodeWriter w) {
    w.writeCodeElement(elements[0]);
    return w;
  }

  private CodeWriter acceptCodeWriterN(CodeWriter w) {
    w.writeCodeElement(elements[0]);
    for (int i = 1; i < elements.length; i++) {
      writeSeparator(w);
      w.writeCodeElement(elements[i]);
    }
    return w;
  }

  private void writeSeparator(CodeWriter w) {
    w.writeCodeElement(CharCodeElement.COMMA);
    w.writeCodeElement(SpaceCodeElement.INSTANCE);
  }

}
