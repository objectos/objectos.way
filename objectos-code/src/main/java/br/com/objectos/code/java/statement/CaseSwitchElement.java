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

import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class CaseSwitchElement extends SwitchElement {

  private final CodeElement value;

  private CaseSwitchElement(CodeElement value, UnmodifiableList<BlockStatement> statements) {
    super(statements);
    this.value = value;
  }

  public static CaseSwitchElement _case(Identifier value, BlockStatement... statements) {
    Check.notNull(value, "value == null");
    return new CaseSwitchElement(
        value,
        UnmodifiableList.copyOf(statements)
    );
  }

  public static CaseSwitchElement _case(int value, BlockStatement... statements) {
    return new CaseSwitchElement(
        Expressions.l(value),
        UnmodifiableList.copyOf(statements)
    );
  }

  public static CaseSwitchElement _case(String value, BlockStatement... statements) {
    Check.notNull(value, "value == null");
    return new CaseSwitchElement(
        Expressions.l(value),
        UnmodifiableList.copyOf(statements)
    );
  }

  @Override
  final void writePreColon(CodeWriter w) {
    w.writeCodeElement(Keywords._case());
    w.writeCodeElement(space());
    w.writeCodeElement(value);
  }

}
