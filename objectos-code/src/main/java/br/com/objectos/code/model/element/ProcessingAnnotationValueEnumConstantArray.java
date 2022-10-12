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

import java.util.List;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

class ProcessingAnnotationValueEnumConstantArray extends ProcessingAnnotationValue {

  private final UnmodifiableList<ProcessingEnumConstant> value;

  ProcessingAnnotationValueEnumConstantArray(ProcessingAnnotation annotation,
                                             ExecutableElement element,
                                             AnnotationValue annotationValue,
                                             UnmodifiableList<ProcessingEnumConstant> value) {
    super(annotation, element, annotationValue);
    this.value = value;
  }

  static ProcessingAnnotationValueEnumConstantArray build(
      ProcessingAnnotation annotation,
      ExecutableElement element,
      AnnotationValue annotationValue,
      List<? extends AnnotationValue> values) {
    return new ProcessingAnnotationValueEnumConstantArray(
        annotation,
        element,
        annotationValue,
        toEnumConstant(annotation, values)
    );
  }

  private static UnmodifiableList<ProcessingEnumConstant> toEnumConstant(
      ProcessingAnnotation annotation, List<? extends AnnotationValue> array) {
    GrowableList<ProcessingEnumConstant> result;
    result = new GrowableList<>();

    for (int i = 0; i < array.size(); i++) {
      AnnotationValue annotationValue;
      annotationValue = array.get(i);

      VariableElement element;
      element = (VariableElement) annotationValue.getValue();

      ProcessingEnumConstant value;
      value = annotation.toEnumConstant(element);

      result.add(value);
    }

    return result.toUnmodifiableList();
  }

  @Override
  public final UnmodifiableList<ProcessingEnumConstant> getEnumConstantArray() {
    return value;
  }

}
