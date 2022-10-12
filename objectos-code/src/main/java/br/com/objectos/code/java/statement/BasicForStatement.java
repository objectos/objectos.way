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
import br.com.objectos.code.java.io.CodeWriter;

public class BasicForStatement extends ForStatement {

  private final ForInitElement init;
  private final ForConditionElement test;
  private final ForUpdateElement update;
  private final Statement body;

  BasicForStatement(ForInitElement init,
                    ForConditionElement test,
                    ForUpdateElement update,
                    Statement body) {
    this.init = init;
    this.test = test;
    this.update = update;
    this.body = body;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._for());
    w.writeCodeElement(space());
    w.writeCodeElement(openParens());
    w.writeCodeElement(init);
    w.writeCodeElement(semicolon());
    w.writeCodeElement(test.ifEmpty(noop(), space()));
    w.writeCodeElement(test);
    w.writeCodeElement(semicolon());
    w.writeCodeElement(update.ifEmpty(noop(), space()));
    w.writeCodeElement(update);
    w.writeCodeElement(closeParens());
    w.writeCodeElement(space());
    Statement wrappedBody = wrapWithBlockIfNecessary(body);
    w.writeCodeElement(wrappedBody);
    writeSemicolonIfNecessary(w, wrappedBody);

    return w;
  }

  @Override
  public final void acceptSemicolon(Semicolon semicolon) {
    // noop
  }

}
