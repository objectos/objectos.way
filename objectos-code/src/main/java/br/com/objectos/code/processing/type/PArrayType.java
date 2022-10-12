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
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeMirror;

public final class PArrayType extends PTypeMirror {

  private PTypeMirror componentType;

  private NamedArray name;

  private final ArrayType type;

  PArrayType(ProcessingEnvironment processingEnv, ArrayType type) {
    super(processingEnv);
    this.type = type;
  }

  public final PTypeMirror getComponentType() {
    if (componentType == null) {
      componentType = getComponentType0();
    }

    return componentType;
  }

  @Override
  public final NamedArray getName() {
    if (name == null) {
      name = getName0();
    }

    return name;
  }

  @Override
  public final boolean isArrayType() {
    return true;
  }

  @Override
  public final PArrayType toArrayType() {
    return this;
  }

  @Override
  final ArrayType getType() {
    return type;
  }

  @Override
  final NamedArray toNamedArray() {
    return NamedArray.of(getName());
  }

  private PTypeMirror getComponentType0() {
    TypeMirror componentType;
    componentType = type.getComponentType();

    return PTypeMirror.adapt(processingEnv, componentType);
  }

  private NamedArray getName0() {
    PTypeMirror componentType;
    componentType = getComponentType();

    return componentType.toNamedArray();
  }

}
