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

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class SwitchStatement extends AbstractSimpleStatement {

  private final UnmodifiableList<SwitchElement> elements;
  private final Expression expression;

  private SwitchStatement(Expression expression, UnmodifiableList<SwitchElement> elements) {
    this.expression = expression;
    this.elements = elements;
  }

  public static SwitchStatement _switch(Expression expression, SwitchElement... elements) {
    Check.notNull(expression, "expression == null");
    return new SwitchStatement(expression, UnmodifiableList.copyOf(elements));
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._switch());
    w.writeCodeElement(space());
    w.writeCodeElement(parenthesized(expression));
    w.writeCodeElement(space());
    w.writeCodeElement(openBrace());

    w.beginSection(Section.BLOCK);

    for (SwitchElement element : elements) {
      w.writeCodeElement(nextLine());
      w.beginSection(Section.STATEMENT);
      w.writeCodeElement(element);
      w.endSection();
    }

    w.writeCodeElement(nextLine());
    w.endSection();
    w.writeCodeElement(closeBrace());

    return w;
  }

}
