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

import static br.com.objectos.code.java.expression.Expressions.id;

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public abstract class BreakStatement extends AbstractSimpleStatement {

  protected BreakStatement() {}

  public static BreakStatement _break(Identifier id) {
    Check.notNull(id, "id == null");
    return new WithLabelBreakStatement(id);
  }

  public static BreakStatement _break(String name) {
    return new WithLabelBreakStatement(id(name));
  }

  private static class WithLabelBreakStatement extends BreakStatement {

    private final Identifier label;

    WithLabelBreakStatement(Identifier label) {
      this.label = label;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writeCodeElement(Keywords._break());
      w.writeCodeElement(space());
      w.writeCodeElement(label);
      return w;
    }

  }

}
