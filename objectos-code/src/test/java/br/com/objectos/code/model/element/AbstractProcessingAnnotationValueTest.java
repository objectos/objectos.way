/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
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
