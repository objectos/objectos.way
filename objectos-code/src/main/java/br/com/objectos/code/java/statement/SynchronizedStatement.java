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
import br.com.objectos.code.java.expression.Expression;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;

public class SynchronizedStatement extends AbstractSimpleStatement {

  private final Expression lock;
  private final Block body;

  private SynchronizedStatement(Expression lock, Block body) {
    this.lock = lock;
    this.body = body;
  }

  public static SynchronizedStatement _synchronized(Expression lock, Block body) {
    Check.notNull(lock, "lock == null");
    Check.notNull(body, "body == null");
    return new SynchronizedStatement(lock, body);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._synchronized());
    w.writeCodeElement(space());
    w.writeCodeElement(parenthesized(lock));
    w.writeCodeElement(space());
    w.writeCodeElement(body);
    return w;
  }

}
