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
package objectos.code.tmpl;

import objectos.code.ClassName;
import objectox.code.Pass0;

public sealed interface Api permits Pass0 {

  sealed interface AtRef
      extends ClassElement {}

  sealed interface ClassElement permits AtRef, ExtendsRef, FinalRef, IdentifierRef, MethodRef {}

  sealed interface ClassRef {}

  sealed interface ExpressionElement {}

  sealed interface ExtendsRef
      extends ClassElement {}

  sealed interface FinalRef
      extends ClassElement {}

  sealed interface IdentifierRef
      extends ClassElement, MethodElement {}

  sealed interface LiteralRef extends ExpressionElement {}

  sealed interface LocalVariableDeclarationRef {}

  sealed interface MethodElement permits IdentifierRef {}

  sealed interface MethodRef
      extends ClassElement {}

  static final class Ref
      implements
      AtRef,
      ClassRef,
      ExtendsRef,
      FinalRef,
      IdentifierRef,
      LiteralRef,
      LocalVariableDeclarationRef,
      MethodRef {

    private Ref() {}

  }

  Ref REF = new Ref();

  void _class(int length);

  void _extends(ClassName superclass);

  void _final();

  void _package(String packageName);

  void annotation(int length);

  void autoImports();

  void className(ClassName name);

  void id(String name);

  void localVariable(int length);

  void method(int length);

  void stringLiteral(String value);

}