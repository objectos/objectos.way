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

import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.Section;
import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.java.type.NamedType;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

public class Statements {

  private Statements() {}

  public static SimpleLocalVariableDeclaration _var(Class<?> type, Identifier id) {
    Check.notNull(type, "type == null");
    Check.notNull(id, "id == null");
    return SimpleLocalVariableDeclaration.ofUnchecked(NamedClass.of(type), id.name());
  }

  public static WithInitLocalVariableDeclaration _var(Class<?> type, Identifier id,
      VariableInitializer init) {
    Check.notNull(init, "init == null");
    return _var(type, id).init(init);
  }

  public static SimpleLocalVariableDeclaration _var(Class<?> type, String name) {
    Check.notNull(type, "type == null");
    Check.notNull(name, "name == null");
    return SimpleLocalVariableDeclaration.ofUnchecked(NamedClass.of(type), name);
  }

  public static WithInitLocalVariableDeclaration _var(Class<?> type, String name,
      VariableInitializer init) {
    Check.notNull(init, "init == null");
    return _var(type, name).init(init);
  }

  public static SimpleLocalVariableDeclaration _var(NamedType typeName, Identifier id) {
    Check.notNull(typeName, "typeName == null");
    Check.notNull(id, "id == null");
    return SimpleLocalVariableDeclaration.ofUnchecked(typeName, id.name());
  }

  public static WithInitLocalVariableDeclaration _var(
      NamedType typeName, Identifier id, VariableInitializer init) {
    Check.notNull(init, "init == null");
    return _var(typeName, id).init(init);
  }

  public static SimpleLocalVariableDeclaration _var(NamedType typeName, String name) {
    Check.notNull(typeName, "typeName == null");
    Check.notNull(name, "name == null");
    return SimpleLocalVariableDeclaration.ofUnchecked(typeName, name);
  }

  public static WithInitLocalVariableDeclaration _var(
      NamedType typeName, String name, VariableInitializer init) {
    Check.notNull(init, "init == null");
    return _var(typeName, name).init(init);
  }

  // Misc.

  public static StatementsShorthand statements(Iterable<? extends BlockStatement> statements) {
    UnmodifiableList<? extends BlockStatement> copy = UnmodifiableList.copyOf(statements);
    return new StatementsShorthand(copy);
  }

  public static String toString(Statement statement) {
    return statement.acceptCodeWriter(
        CodeWriter.forToString().beginSection(Section.STATEMENT)
    ).endSection().toString();
  }

}
