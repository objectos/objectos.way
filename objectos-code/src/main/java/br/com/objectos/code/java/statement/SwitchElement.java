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
package br.com.objectos.code.java.statement;

import static br.com.objectos.code.java.element.NewLine.nextLine;

import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import objectos.util.UnmodifiableList;

public abstract class SwitchElement extends AbstractCodeElement {

  private final UnmodifiableList<BlockStatement> statements;

  SwitchElement(UnmodifiableList<BlockStatement> statements) {
    this.statements = statements;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    writePreColon(w);
    w.writeCodeElement(colon());

    w.beginSection(Section.BLOCK);

    for (BlockStatement statement : statements) {
      w.writeCodeElement(nextLine());
      w.writeCodeElement(statement);
      w.writeCodeElement(semicolon());
    }

    w.endSection();

    return w;
  }

  abstract void writePreColon(CodeWriter w);

}