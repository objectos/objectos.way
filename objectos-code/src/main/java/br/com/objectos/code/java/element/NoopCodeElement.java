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
package br.com.objectos.code.java.element;

import br.com.objectos.code.java.declaration.ClassCode;
import br.com.objectos.code.java.declaration.ClassCodeElement;
import br.com.objectos.code.java.declaration.ConstructorCode;
import br.com.objectos.code.java.declaration.ConstructorCodeElement;
import br.com.objectos.code.java.declaration.EnumCode;
import br.com.objectos.code.java.declaration.EnumCodeElement;
import br.com.objectos.code.java.declaration.EnumConstantCode;
import br.com.objectos.code.java.declaration.EnumConstantCodeElement;
import br.com.objectos.code.java.declaration.FieldCode;
import br.com.objectos.code.java.declaration.FieldCodeElement;
import br.com.objectos.code.java.declaration.InterfaceCode;
import br.com.objectos.code.java.declaration.InterfaceCodeElement;
import br.com.objectos.code.java.declaration.MethodCode;
import br.com.objectos.code.java.declaration.MethodCodeElement;
import br.com.objectos.code.java.io.CodeWriter;

public final class NoopCodeElement
    implements
    ClassCodeElement,
    ConstructorCodeElement,
    EnumCodeElement,
    EnumConstantCodeElement,
    FieldCodeElement,
    InterfaceCodeElement,
    MethodCodeElement,
    CodeElement {

  private static final NoopCodeElement INSTANCE = new NoopCodeElement();

  private NoopCodeElement() {}

  public static NoopCodeElement noop() {
    return INSTANCE;
  }

  @Override
  public final CodeWriter acceptCodeWriter(CodeWriter w) {
    // noop
    return w;
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    // noop
  }

  @Override
  public final void acceptConstructorCodeBuilder(ConstructorCode.Builder builder) {
    // noop
  }

  @Override
  public final void acceptEnumCodeBuilder(EnumCode.Builder builder) {
    // noop
  }

  @Override
  public final void acceptEnumConstantCodeBuilder(EnumConstantCode.Builder builder) {
    // noop
  }

  @Override
  public final FieldCode.Builder acceptFieldCodeBuilder(FieldCode.Builder builder) {
    // noop
    return builder;
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    // noop
  }

  @Override
  public final void acceptMethodCodeBuilder(MethodCode.Builder builder) {
    // noop
  }

}
