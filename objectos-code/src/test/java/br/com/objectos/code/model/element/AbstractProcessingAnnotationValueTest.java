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

import br.com.objectos.code.util.AbstractCodeCoreTest;
import java.lang.annotation.Annotation;

public abstract class AbstractProcessingAnnotationValueTest extends AbstractCodeCoreTest {

  final ProcessingAnnotationValue getDeclaredOrDefaultValue(
      ProcessingAnnotation annotation, String name) {
    return annotation.getDeclaredOrDefaultValue(name);
  }

  final ProcessingAnnotationValue getDeclaredValue(ProcessingAnnotation annotation, String name) {
    return annotation.getDeclaredValue(name);
  }

  final ProcessingAnnotationValue getDefaultValue(ProcessingAnnotation annotation, String name) {
    return annotation.getDefaultValue(name);
  }

  final ProcessingAnnotation getDirectlyPresentAnnotation(
      Class<?> subjecType,
      Class<? extends Annotation> annotationType) {
    ProcessingType subject;
    subject = query(subjecType);

    return subject.getDirectlyPresentAnnotation(annotationType);
  }

}
