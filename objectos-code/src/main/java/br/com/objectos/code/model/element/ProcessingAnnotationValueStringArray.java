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
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

class ProcessingAnnotationValueStringArray extends ProcessingAnnotationValue {

  private final UnmodifiableList<String> value;

  ProcessingAnnotationValueStringArray(ProcessingAnnotation annotation,
                                       ExecutableElement element,
                                       AnnotationValue annotationValue,
                                       UnmodifiableList<String> value) {
    super(annotation, element, annotationValue);
    this.value = value;
  }

  static ProcessingAnnotationValue build(
      ProcessingAnnotation annotation,
      ExecutableElement element,
      AnnotationValue annotationValue,
      List<? extends AnnotationValue> values) {
    return new ProcessingAnnotationValueStringArray(
        annotation,
        element,
        annotationValue,
        toStringArray(values)
    );
  }

  private static UnmodifiableList<String> toStringArray(List<? extends AnnotationValue> array) {
    GrowableList<String> result = new GrowableList<>();

    for (int i = 0; i < array.size(); i++) {
      AnnotationValue annotationValue;
      annotationValue = array.get(i);

      String string;
      string = (String) annotationValue.getValue();

      result.add(string);
    }

    return result.toUnmodifiableList();
  }

  @Override
  public final UnmodifiableList<String> getStringArray() {
    return value;
  }

}
