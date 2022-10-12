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

import br.com.objectos.code.java.element.AbstractCodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.type.NamedClass;
import java.util.Iterator;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class CatchElement extends AbstractCodeElement implements TryStatementElement {

  private final UnmodifiableList<NamedClass> exceptionTypes;
  private final Identifier id;

  private CatchElement(UnmodifiableList<NamedClass> exceptionTypes, Identifier id) {
    this.exceptionTypes = exceptionTypes;
    this.id = id;
  }

  public static CatchElement _catch(
      Class<? extends Throwable> type1,
      Class<? extends Throwable> type2,
      Identifier id) {
    return _catch0(
        UnmodifiableList.of(
            NamedClass.ofWithNullMessage(type1, "type1 == null"),
            NamedClass.ofWithNullMessage(type2, "type2 == null")
        ),
        id
    );
  }

  public static CatchElement _catch(Class<? extends Throwable> type, Identifier id) {
    return _catch0(
        UnmodifiableList.of(
            NamedClass.ofWithNullMessage(type, "type == null")
        ),
        id
    );
  }

  private static CatchElement _catch0(UnmodifiableList<NamedClass> exceptionTypes, Identifier id) {
    Check.notNull(id, "id == null");
    return new CatchElement(exceptionTypes, id);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writeCodeElement(Keywords._catch());
    w.writeCodeElement(space());
    w.writeCodeElement(openParens());

    Iterator<NamedClass> exIter = exceptionTypes.iterator();
    if (exIter.hasNext()) {
      w.writeCodeElement(exIter.next());
      while (exIter.hasNext()) {
        w.writeCodeElement(space());
        w.writeCodeElement(verticalBar());
        w.writeCodeElement(space());
        w.writeCodeElement(exIter.next());
      }
    }

    w.writeCodeElement(space());
    w.writeCodeElement(id);
    w.writeCodeElement(closeParens());

    return w;
  }

  @Override
  public final void acceptTryStatementBuilder(TryStatement.Builder builder) {
    builder.addCatchElement(this);
  }

}
