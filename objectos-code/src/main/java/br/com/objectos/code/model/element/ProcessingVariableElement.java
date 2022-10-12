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
package br.com.objectos.code.model.element;

import br.com.objectos.code.java.declaration.ParameterCode;
import br.com.objectos.code.java.expression.Expressions;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.processing.type.PTypeMirror;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

abstract class ProcessingVariableElement extends ProcessingElement<VariableElement> {

  private Identifier identifier;
  private PTypeMirror type;

  ProcessingVariableElement(ProcessingEnvironment processingEnv, VariableElement element) {
    super(processingEnv, element);
  }

  public final String getName() {
    Name simpleName;
    simpleName = element.getSimpleName();

    return simpleName.toString();
  }

  public final boolean hasName(String name) {
    String thisName;
    thisName = getName();

    return thisName.equals(name);
  }

  public final Identifier toIdentifier() {
    if (identifier == null) {
      identifier = toIdentifier0();
    }

    return identifier;
  }

  public final ParameterCode toParameterCode() {
    return ParameterCode.of(element);
  }
  
  final PTypeMirror getTypeImpl() {
    if (type == null) {
      type = getType0();
    }
    
    return type;
  }

  private PTypeMirror getType0() {
    TypeMirror asType;
    asType = element.asType();

    return PTypeMirror.adapt(processingEnv, asType);
  }

  private Identifier toIdentifier0() {
    return Expressions.id(getName());
  }

}