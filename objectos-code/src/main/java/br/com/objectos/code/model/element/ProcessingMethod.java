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

import br.com.objectos.code.java.declaration.MethodModifier;
import br.com.objectos.code.java.declaration.Modifiers;
import br.com.objectos.code.processing.type.PTypeMirror;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;
import objectos.lang.Check;
import objectos.util.UnmodifiableSet;

public final class ProcessingMethod extends ProcessingExecutableElement {

  private ProcessingType declaringType;

  private PTypeMirror returnType;

  private ProcessingMethod(ProcessingEnvironment processingEnv,
                           ExecutableElement subject) {
    super(processingEnv, subject);
  }

  public static ProcessingMethod adapt(
      ProcessingEnvironment processingEnv, ExecutableElement executableElement) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(executableElement, "executableElement == null");
    Check.argument(executableElement.getKind() == ElementKind.METHOD,
        "Not ElementKind.METHOD");

    return ProcessingMethod.adaptUnchecked(processingEnv, executableElement);
  }

  static ProcessingMethod adaptUnchecked(
      ProcessingEnvironment processingEnv, ExecutableElement element) {
    return new ProcessingMethod(
        processingEnv,
        element
    );
  }

  public final ProcessingType getDeclaringType() {
    if (declaringType == null) {
      declaringType = getDeclaringType0();
    }

    return declaringType;
  }

  @Override
  public final UnmodifiableSet<MethodModifier> getModifiers() {
    return getModifiersImpl();
  }

  public final String getName() {
    return element.getSimpleName().toString();
  }

  public final PTypeMirror getReturnType() {
    if (returnType == null) {
      returnType = getReturnType0();
    }

    return returnType;
  }

  public final boolean hasName(String name) {
    return getName().equals(name);
  }

  public final boolean isAbstract() {
    return hasModifier(Modifiers.ABSTRACT);
  }

  public final boolean isFinal() {
    return hasModifier(Modifiers.FINAL);
  }

  public final boolean isStatic() {
    return hasModifier(Modifiers.STATIC);
  }

  @Override
  public final String toString() {
    if (getModifiers().isEmpty()) {
      return toString0();
    } else {
      return modifiersToString() + ' ' + toString0();
    }
  }

  private PTypeMirror getReturnType0() {
    TypeMirror returnTypeMirror;
    returnTypeMirror = element.getReturnType();

    return PTypeMirror.adapt(processingEnv, returnTypeMirror);
  }

  private String toString0() {
    return getReturnType().toString() + ' ' + element.toString();
  }

}