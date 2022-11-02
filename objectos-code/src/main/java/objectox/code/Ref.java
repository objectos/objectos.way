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

import objectos.code.tmpl.AtRef;
import objectos.code.tmpl.ClassDeclarationRef;
import objectos.code.tmpl.ExpressionNameRef;
import objectos.code.tmpl.ExtendsRef;
import objectos.code.tmpl.FinalRef;
import objectos.code.tmpl.IdentifierRef;
import objectos.code.tmpl.LiteralRef;
import objectos.code.tmpl.LocalVariableDeclarationRef;
import objectos.code.tmpl.MarkerApi;
import objectos.code.tmpl.MethodInvocationRef;
import objectos.code.tmpl.MethodRef;
import objectos.code.tmpl.NewLineRef;
import objectos.code.tmpl.VoidRef;

public final class Ref
    implements
    AtRef,
    ClassDeclarationRef,
    ExtendsRef,
    FinalRef,
    IdentifierRef,
    LiteralRef,
    LocalVariableDeclarationRef,
    MethodInvocationRef,
    MethodRef,
    ExpressionNameRef,
    NewLineRef,
    VoidRef {

  public static final Ref INSTANCE = new Ref();

  private Ref() {}

  @Override
  public final void mark(MarkerApi api) {
    api.markReference();
  }

}