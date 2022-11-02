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
import objectos.code.TypeName;
import objectos.code.tmpl.TemplateApi;
import objectos.lang.Check;

public final class Pass0 extends Pass0Super implements TemplateApi {

  @Override
  public final void _extends(ClassName superclass) {
    superclass.acceptClassNameSet(importSet);

    object(ByteProto.EXTENDS, superclass);
  }

  @Override
  public final void _final() {
    object(ByteProto.MODIFIER, Modifier.FINAL);
  }

  @Override
  public final void annotation() {
    element(ByteProto.ANNOTATION);
  }

  @Override
  public final void autoImports() {
    importSet.enable();
  }

  @Override
  public final void classDeclaration() {
    element(ByteProto.CLASS_DECLARATION);
  }

  @Override
  public final void className(ClassName name) {
    name.acceptClassNameSet(importSet);

    object(ByteProto.CLASS_NAME, name);
  }

  public final void compilationUnitEnd() {
    markStart();

    for (int i = 0; i < elementIndex; i++) {
      markReference();
    }

    element(ByteProto.COMPILATION_UNIT);

    if (elementIndex != 1) {
      throw new UnsupportedOperationException("Implement me");
    }

    protoArray[1] = elementArray[0];
  }

  public final void compilationUnitStart() {
    elementIndex = 0;

    objectIndex = 0;

    lambdaIndex = -1;

    markIndex = -1;

    protoIndex = 0;

    protoAdd(ByteProto.JMP, ByteProto.NULL);
  }

  @Override
  public final void expressionName() {
    element(ByteProto.EXPRESSION_NAME);
  }

  @Override
  public final void identifier(String name) {
    Check.argument(
      SourceVersion.isIdentifier(name), // implicit null-check
      name, " is not a valid identifier"
    );

    object(ByteProto.IDENTIFIER, name);
  }

  @Override
  public final void lambdaEnd() {
    lambdaPop();
  }

  @Override
  public final void lambdaStart() {
    lambdaPush();
  }

  @Override
  public final void localVariable() {
    element(ByteProto.LOCAL_VARIABLE);
  }

  @Override
  public final void markLambda() {
    lambdaCount();
  }

  @Override
  public final void markReference() {
    markIncrement();
  }

  @Override
  public final void markStart() {
    markPush();
  }

  @Override
  public final void methodDeclaration() {
    element(ByteProto.METHOD_DECLARATION);
  }

  @Override
  public final void methodInvocation() {
    element(ByteProto.METHOD_INVOCATION);
  }

  @Override
  public final void newLine() {
    markStart();

    element(ByteProto.NEW_LINE);
  }

  @Override
  public final void packageDeclaration() {
    element(ByteProto.PACKAGE_DECLARATION);
  }

  @Override
  public final void packageName(String packageName) {
    Check.argument(
      SourceVersion.isName(packageName),
      packageName, " is not a valid package name"
    );

    object(ByteProto.PACKAGE_NAME, packageName);
  }

  @Override
  public void stringLiteral(String value) {
    Check.notNull(value, "value == null");

    object(ByteProto.STRING_LITERAL, value);
  }

  @Override
  public final void typeName(TypeName typeName) {
    typeName.acceptClassNameSet(importSet);

    object(ByteProto.TYPE_NAME, typeName);
  }

}