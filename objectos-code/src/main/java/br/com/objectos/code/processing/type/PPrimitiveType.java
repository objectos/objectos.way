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
import br.com.objectos.code.java.type.NamedPrimitive;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;

public final class PPrimitiveType extends PTypeMirror {

  private final PrimitiveType type;

  PPrimitiveType(ProcessingEnvironment processingEnv, PrimitiveType type) {
    super(processingEnv);
    this.type = type;
  }

  @Override
  public final NamedPrimitive getName() {
    TypeKind kind;
    kind = type.getKind();

    return NamedPrimitive.of(kind);
  }

  @Override
  public final boolean isPrimitiveType() {
    return true;
  }

  @Override
  public final PPrimitiveType toPrimitiveType() {
    return this;
  }

  @Override
  final PrimitiveType getType() {
    return type;
  }

  @Override
  final NamedArray toNamedArray() {
    return NamedArray.of(getName());
  }

}
