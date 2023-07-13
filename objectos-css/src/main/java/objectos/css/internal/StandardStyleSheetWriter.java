/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.internal;

import java.io.IOException;
import objectos.css.StyleSheet;
import objectos.css.StyleSheetWriter;

public final class StandardStyleSheetWriter implements StyleSheetWriter {

  private static final String INDENTATION = "  ";

  private static final String NL = System.lineSeparator();

  private final Appendable appendable;

  private int indentation;

  private int declarationCount;

  private int selectorCount;

  public StandardStyleSheetWriter(Appendable appendable) {
    this.appendable = appendable;
  }

  public final void declarationEnd() throws IOException {
    appendable.append(';');
    appendable.append(NL);
  }

  public final void declarationStart(String name) throws IOException {
    if (declarationCount == 0) {
      appendable.append(" {");
      appendable.append(NL);
      indentation++;
    }

    declarationCount++;

    writeIndentation();
    appendable.append(name);
    appendable.append(':');
  }

  public final void keyword(String name) throws IOException {
    appendable.append(' ');
    appendable.append(name);
  }

  public final void selector(String name) throws IOException {
    if (selectorCount == 0) {
      writeIndentation();
    }

    selectorCount++;

    appendable.append(name);
  }

  public final void styleRuleEnd() throws IOException {
    indentation--;

    writeIndentation();
    appendable.append('}');
    appendable.append(NL);
  }

  public final void styleRuleStart() {
    declarationCount = 0;

    selectorCount = 0;
  }

  @Override
  public final void write(StyleSheet sheet) throws IOException {
    CompiledStyleSheet compiled;
    compiled = (CompiledStyleSheet) sheet;

    compiled.accept(this);
  }

  private void writeIndentation() throws IOException {
    for (int i = 0; i < indentation; i++) {
      appendable.append(INDENTATION);
    }
  }

}