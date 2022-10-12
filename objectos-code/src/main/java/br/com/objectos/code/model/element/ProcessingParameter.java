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

import br.com.objectos.code.java.declaration.ParameterModifier;
import br.com.objectos.code.processing.type.PTypeMirror;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.VariableElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableSet;

public class ProcessingParameter extends ProcessingVariableElement {

  private ProcessingParameter(ProcessingEnvironment processingEnv, VariableElement subject) {
    super(processingEnv, subject);
  }

  public static ProcessingParameter adapt(
      ProcessingEnvironment processingEnv, VariableElement element) {
    Check.notNull(processingEnv, "processingEnv == null");
    Check.notNull(element, "element == null");
    Check.argument(element.getKind() == ElementKind.PARAMETER, "Not ElementKind.PARAMETER");
    return adaptUnchecked(processingEnv, element);
  }

  static ProcessingParameter adaptUnchecked(
      ProcessingEnvironment processingEnv, VariableElement parameter) {
    return new ProcessingParameter(processingEnv, parameter);
  }

  @Override
  public final UnmodifiableSet<ParameterModifier> getModifiers() {
    return getModifiersImpl();
  }

  public final PTypeMirror getType() {
    return getTypeImpl();
  }

}
