/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
 *
 * This file is part of the ObjectosCode (testing) project.
 *
 * Confidential. Do not distribute.
 */
package br.com.objectos.code.model.element;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import br.com.objectos.code.testing.InheritedAnnotation;
import br.com.objectos.code.testing.NonInheritedAnnotation;
import br.com.objectos.code.util.AbstractCodeCoreTest;
import br.com.objectos.code.util.MethodAnnotation;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import org.testng.annotations.Test;

public class ExecutableElementTest extends AbstractCodeCoreTest {

  private abstract static class AnnotationsParent {
    @InheritedAnnotation
    @NonInheritedAnnotation
    abstract void subject();
  }

  private abstract static class AnnotationsSubject extends AnnotationsParent {
    @Override
    @MethodAnnotation
    final void subject() {}
  }

  @Test(description = ""
      + "Asserts that overriden methods do not inherit the @Inherited-annotated annotations. "
      + "Put another way, methods only have 'directly present' annotations."
  )
  public void getAllAnnotationMirrors() {
    TypeElement annotationsSubject;
    annotationsSubject = getTypeElement(AnnotationsSubject.class);

    List<? extends Element> annotationsSubjectElements;
    annotationsSubjectElements = annotationsSubject.getEnclosedElements();

    List<ExecutableElement> annotationsSubjectMethods;
    annotationsSubjectMethods = ElementFilter.methodsIn(annotationsSubjectElements);

    assertEquals(annotationsSubjectMethods.size(), 1);

    ExecutableElement subject;
    subject = annotationsSubjectMethods.get(0);

    Elements elements;
    elements = processingEnv.getElementUtils();

    List<? extends AnnotationMirror> allAnnotationMirrors;
    allAnnotationMirrors = elements.getAllAnnotationMirrors(subject);

    assertEquals(allAnnotationMirrors.size(), 1);

    AnnotationMirror annotationMirror;
    annotationMirror = allAnnotationMirrors.get(0);

    DeclaredType annotationType;
    annotationType = annotationMirror.getAnnotationType();

    Types types;
    types = processingEnv.getTypeUtils();

    TypeElement methodAnnotationTypeElement;
    methodAnnotationTypeElement = getTypeElement(MethodAnnotation.class);

    DeclaredType methodAnnotationType;
    methodAnnotationType = types.getDeclaredType(methodAnnotationTypeElement);

    assertTrue(types.isSameType(annotationType, methodAnnotationType));
  }
  
}
