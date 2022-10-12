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

import br.com.objectos.code.java.declaration.ConstructorModifier;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableSet;

public class ProcessingConstructor extends ProcessingExecutableElement {

  private ProcessingConstructor(ProcessingEnvironment processingEnv,
                                ExecutableElement subject) {
    super(processingEnv, subject);
  }

  public static ProcessingConstructor adapt(
      ProcessingEnvironment processingEnv, ExecutableElement executableElement) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(executableElement, "executableElement == null");
    Check.argument(
        executableElement.getKind() == ElementKind.CONSTRUCTOR,
        "Not ElementKind.CONSTRUCTOR"
    );
    return ProcessingConstructor.adaptUnchecked(processingEnv, executableElement);
  }

  static ProcessingConstructor adaptUnchecked(
      ProcessingEnvironment processingEnv, ExecutableElement element) {
    return new ProcessingConstructor(
        processingEnv,
        element
    );
  }

  @Override
  public final UnmodifiableSet<ConstructorModifier> getModifiers() {
    return getModifiersImpl();
  }

  @Override
  public final String toString() {
    if (getModifiers().isEmpty()) {
      return element.toString();
    } else {
      return modifiersToString() + ' ' + element.toString();
    }
  }

}