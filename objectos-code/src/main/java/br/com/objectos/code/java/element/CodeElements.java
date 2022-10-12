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
import br.com.objectos.code.java.io.Section;

final class CodeElements {

  static final CodeElement END_SECTION = new AbstractCodeElement() {
    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w.endSection();
    }
  };

  static final CodeElement INDENT_IF_NECESSARY = new AbstractCodeElement() {
    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w.writePreIndentation();
    }
  };

  private CodeElements() {}

  static CodeElement ofRaw(String string) {
    return new RawString(string);
  }

  static CodeElement ofSection(Section section) {
    return new BeginSection(section);
  }

  private static class BeginSection extends AbstractCodeElement {
    private final Section section;

    BeginSection(Section section) {
      this.section = section;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w.beginSection(section);
    }
  }

  private static class RawString extends AbstractCodeElement {
    private final String value;

    RawString(String value) {
      this.value = value;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      return w.write(value);
    }
  }

}
