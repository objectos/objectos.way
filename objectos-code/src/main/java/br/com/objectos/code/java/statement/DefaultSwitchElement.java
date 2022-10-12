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

import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.java.element.Keywords;
import br.com.objectos.code.java.io.CodeWriter;
import objectos.util.UnmodifiableList;

public final class DefaultSwitchElement extends SwitchElement
    implements
    MethodCodeElement {

  private static final DefaultSwitchElement EMPTY = new DefaultSwitchElement(
      UnmodifiableList.<BlockStatement> of()
  );

  DefaultSwitchElement(UnmodifiableList<BlockStatement> statements) {
    super(statements);
  }

  public static DefaultSwitchElement _default() {
    return EMPTY;
  }

  public static DefaultSwitchElement _default(BlockStatement... statements) {
    return new DefaultSwitchElement(UnmodifiableList.copyOf(statements));
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.addModifier(Modifiers.DEFAULT);
  }

  @Override
  final void writePreColon(CodeWriter w) {
    w.writeCodeElement(Keywords._default());
  }

}
