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

  public sealed interface AtRef
      extends ClassElement {}

  public sealed interface ClassElement permits AtRef, ExtendsRef, FinalRef, IdentifierRef, MethodRef {}

  public sealed interface ClassRef {}

  public sealed interface ExtendsRef
      extends ClassElement {}

  public sealed interface FinalRef
      extends ClassElement {}

  public sealed interface IdentifierRef
      extends ClassElement, MethodElement {}

  public sealed interface MethodElement permits IdentifierRef {}

  public sealed interface MethodRef
      extends ClassElement {}

  public static final class Ref
      implements
      AtRef,
      ClassRef,
      ExtendsRef,
      FinalRef,
      IdentifierRef,
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

  void method(int length);

}