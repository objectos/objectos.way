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

import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public abstract class ContinueStatement extends AbstractSimpleStatement {

  protected ContinueStatement() {}

  public static ContinueStatement _continue(Identifier id) {
    Check.notNull(id, "id == null");
    return new WithLabelContinueStatement(id);
  }

  public static ContinueStatement _continue(String id) {
    return new WithLabelContinueStatement(Expressions.id(id));
  }

  private static final class WithLabelContinueStatement extends ContinueStatement {

    private final Identifier label;

    WithLabelContinueStatement(Identifier label) {
      this.label = label;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.writeCodeElement(Keywords._continue());
      w.writeCodeElement(space());
      w.writeCodeElement(label);
      return w;
    }

  }

}
