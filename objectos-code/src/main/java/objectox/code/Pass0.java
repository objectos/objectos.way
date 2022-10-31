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
package objectox.code;

import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import objectos.code.ClassName;
import objectos.code.JavaTemplate;
import objectos.code.TypeName;
import objectos.code.tmpl.InternalApi;
import objectos.lang.Check;

public final class Pass0 implements InternalApi {

  public static final Ref REF = new Ref();

  private State state;

  @Override
  public final void _extends(ClassName superclass) {
    Check.notNull(superclass, "superclass == null");

    state.typenameadd(superclass);

    state.objectadd(ByteProto.EXTENDS, superclass);
  }

  @Override
  public final void _final() {
    state.objectadd(ByteProto.MODIFIER, Modifier.FINAL);
  }

  @Override
  public final void annotation(int length) {
    state.elementadd(ByteProto.ANNOTATION, length);
  }

  @Override
  public final void autoImports() {
    state.autoImports();
  }

  @Override
  public final void classDeclaration(int length) {
    state.elementadd(ByteProto.CLASS, length);
  }

  @Override
  public final void className(ClassName name) {
    Check.notNull(name, "name == null");

    state.typenameadd(name);

    state.objectadd(ByteProto.CLASS_NAME, name);
  }

  public final void execute(State state, JavaTemplate template) {
    this.state = state.reset();

    template.eval(this);

    state.pass0end();
  }

  @Override
  public final void expressionName(int length) {
    state.elementadd(ByteProto.EXPRESSION_NAME, length);
  }

  @Override
  public final void identifier(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    state.objectadd(ByteProto.IDENTIFIER, name);
  }

  @Override
  public final void localVariable(int length) {
    state.elementadd(ByteProto.LOCAL_VARIABLE, length);
  }

  @Override
  public final void methodDeclaration(int length) {
    state.elementadd(ByteProto.METHOD, length);
  }

  @Override
  public final void methodInvocation(int length) {
    state.elementadd(ByteProto.METHOD_INVOCATION, length);
  }

  @Override
  public final void newLine() {
    state.elementadd(ByteProto.NEW_LINE, 0);
  }

  @Override
  public final void packageDeclaration(String packageName) {
    Check.notNull(packageName, "packageName == null");
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    state.packagename(packageName);

    state.objectadd(ByteProto.PACKAGE_NAME, packageName);

    state.elementadd(ByteProto.PACKAGE, 1);
  }

  @Override
  public void stringLiteral(String value) {
    Check.notNull(value, "value == null");

    state.objectadd(ByteProto.STRING_LITERAL, value);
  }

  @Override
  public final void typeName(TypeName typeName) {
    Check.notNull(typeName, "typeName == null");

    state.typenameadd(typeName);

    state.objectadd(ByteProto.TYPE_NAME, typeName);
  }

}