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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.io.JavaFileImportSet;

public class NamedVoid extends NamedType
    implements
    MethodCodeElement {

  static final NamedVoid VOID = new NamedVoid();

  private NamedVoid() {}

  public static NamedVoid _void() {
    return VOID;
  }

  @Override
  public final String acceptJavaFileImportSet(JavaFileImportSet set) {
    return toString();
  }

  @Override
  public final <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p) {
    return visitor.visitNamedVoid(this, p);
  }

  @Override
  public final boolean isVoid() {
    return true;
  }

  @Override
  public final String toString() {
    return "void";
  }

}