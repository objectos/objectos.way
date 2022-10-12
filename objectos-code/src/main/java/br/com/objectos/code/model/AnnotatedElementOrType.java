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
package br.com.objectos.code.model;

import br.com.objectos.code.model.element.ProcessingAnnotation;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public abstract class AnnotatedElementOrType {

  protected final ProcessingEnvironment processingEnv;

  private UnmodifiableList<ProcessingAnnotation> directlyPresentAnnotations;
  private UnmodifiableList<ProcessingAnnotation> directlyPresentOrInheritedAnnotations;

  protected AnnotatedElementOrType(ProcessingEnvironment processingEnv) {
    this.processingEnv = processingEnv;
  }

  @Override
  public final boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof AnnotatedElementOrType)) {
      return false;
    }
    AnnotatedElementOrType that = (AnnotatedElementOrType) obj;
    return getClass().equals(that.getClass())
        && equalsImpl(that);
  }

  public final ProcessingAnnotation getDirectlyPresentAnnotation(
      Class<? extends Annotation> annotationType)
      throws NoSuchElementException {
    Check.notNull(annotationType, "annotationType == null");
    return getDirectlyPresentAnnotation(annotationType.getCanonicalName());
  }

  public final ProcessingAnnotation getDirectlyPresentAnnotation(
      String canonicalName)
      throws NoSuchElementException {
    UnmodifiableList<ProcessingAnnotation> annotations;
    annotations = getDirectlyPresentAnnotations();

    return getProcessingAnnotationByCanonicalName(annotations, canonicalName);
  }

  public final UnmodifiableList<ProcessingAnnotation> getDirectlyPresentAnnotations() {
    if (directlyPresentAnnotations == null) {
      directlyPresentAnnotations = getDirectlyPresentAnnotations0();
    }

    return directlyPresentAnnotations;
  }

  @Override
  public abstract int hashCode();

  @Override
  public abstract String toString();

  protected final Elements elementUtils() {
    return processingEnv.getElementUtils();
  }

  protected abstract boolean equalsImpl(AnnotatedElementOrType obj);

  protected abstract List<? extends AnnotationMirror> getAnnotationMirrors();

  protected final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotationImpl(
      Element element, Class<? extends Annotation> annotationType)
      throws NoSuchElementException {
    Check.notNull(annotationType, "annotationType == null");
    return getDirectlyPresentOrInheritedAnnotationImpl(element, annotationType.getCanonicalName());
  }

  protected final ProcessingAnnotation getDirectlyPresentOrInheritedAnnotationImpl(
      Element element, String canonicalName)
      throws NoSuchElementException {
    UnmodifiableList<ProcessingAnnotation> annotations;
    annotations = getDirectlyPresentOrInheritedAnnotationsImpl(element);

    return getProcessingAnnotationByCanonicalName(annotations, canonicalName);
  }

  protected final UnmodifiableList<ProcessingAnnotation>
      getDirectlyPresentOrInheritedAnnotationsImpl(Element element) {
    if (directlyPresentOrInheritedAnnotations == null) {
      directlyPresentOrInheritedAnnotations = getDirectlyPresentOrInheritedAnnotations0(element);
    }

    return directlyPresentOrInheritedAnnotations;
  }

  protected final ProcessingAnnotation getProcessingAnnotationByCanonicalName(
      UnmodifiableList<ProcessingAnnotation> annotations, String canonicalName) {
    Check.notNull(canonicalName, "canonicalName == null");

    for (int i = 0; i < annotations.size(); i++) {
      ProcessingAnnotation annotation;
      annotation = annotations.get(i);

      if (annotation.hasQualifiedName(canonicalName)) {
        return annotation;
      }
    }

    throw new NoSuchElementException(canonicalName);
  }

  protected final boolean isSameType(TypeMirror type, String qualifiedName) {
    Types types = typeUtils();

    TypeMirror first = types.erasure(type);

    TypeElement typeElement = elementUtils().getTypeElement(qualifiedName);
    TypeMirror second = types.erasure(typeElement.asType());

    return types.isSameType(first, second);
  }

  protected abstract ProcessingAnnotation toProcessingAnnotation(AnnotationMirror mirror);

  protected final UnmodifiableList<ProcessingAnnotation> toProcessingAnnotationUnmodifiableList(
      List<? extends AnnotationMirror> mirrors) {
    GrowableList<ProcessingAnnotation> list;
    list = new GrowableList<>();

    for (int i = 0; i < mirrors.size(); i++) {
      AnnotationMirror mirror;
      mirror = mirrors.get(i);

      ProcessingAnnotation annotation;
      annotation = toProcessingAnnotation(mirror);

      list.add(annotation);
    }

    return list.toUnmodifiableList();
  }

  protected final Types typeUtils() {
    return processingEnv.getTypeUtils();
  }

  private UnmodifiableList<ProcessingAnnotation> getDirectlyPresentAnnotations0() {
    List<? extends AnnotationMirror> mirrors;
    mirrors = getAnnotationMirrors();

    return toProcessingAnnotationUnmodifiableList(mirrors);
  }

  private UnmodifiableList<ProcessingAnnotation> getDirectlyPresentOrInheritedAnnotations0(
      Element element) {
    Elements elements;
    elements = elementUtils();

    List<? extends AnnotationMirror> mirrors;
    mirrors = elements.getAllAnnotationMirrors(element);

    return toProcessingAnnotationUnmodifiableList(mirrors);
  }

}