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

import br.com.objectos.code.java.declaration.FieldCode;
import br.com.objectos.code.java.declaration.FieldCodeElement;
import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.declaration.ParameterTypeName;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.io.CanGenerateImportDeclaration;
import br.com.objectos.code.java.io.CodeWriter;
import javax.lang.model.type.TypeMirror;

public abstract class NamedType
    implements
    CanGenerateImportDeclaration,
    CodeElement,
    FieldCodeElement,
    MethodCodeElement,
    ParameterTypeName {

  protected NamedType() {}

  public static NamedType of(TypeMirror type) {
    return NamedTypeFactory.of(type);
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    w.writePreIndentation();
    w.writeNamedType(this);
    return w;
  }

  @Override
  public final FieldCode.Builder acceptFieldCodeBuilder(FieldCode.Builder builder) {
    return builder.type(this);
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    builder.returnType(this);
  }

  public abstract <R, P> R acceptTypeNameVisitor(NamedTypeVisitor<R, P> visitor, P p);

  public boolean isJavaLangObject() {
    return false;
  }

  @Override
  public final boolean isVarArgs() {
    return false;
  }

  public boolean isVoid() {
    return false;
  }

  public NamedClass toClassNameUnchecked() {
    throw new UnsupportedOperationException(getClass() + " is not a ClassName instance.");
  }

}
