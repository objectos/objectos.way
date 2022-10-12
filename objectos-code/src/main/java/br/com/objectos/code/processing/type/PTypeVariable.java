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
package br.com.objectos.code.processing.type;

import br.com.objectos.code.java.type.NamedArray;
import br.com.objectos.code.java.type.NamedTypeVariable;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;

public final class PTypeVariable extends PTypeMirror {

  private NamedTypeVariable name;

  private final TypeVariable type;

  PTypeVariable(ProcessingEnvironment processingEnv, TypeVariable type) {
    super(processingEnv);
    this.type = type;
  }

  @Override
  public final NamedTypeVariable getName() {
    if (name == null) {
      name = getName0();
    }

    return name;
  }

  @Override
  public final boolean isTypeVariable() {
    return true;
  }

  @Override
  public final PTypeVariable toTypeVariable() {
    return this;
  }

  @Override
  final TypeMirror getType() {
    return type;
  }

  @Override
  final NamedArray toNamedArray() {
    return NamedArray.of(getName());
  }

  private NamedTypeVariable getName0() {
    String name;
    name = type.toString();

    return NamedTypeVariable.of(name);
  }

}
