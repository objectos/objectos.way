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

import br.com.objectos.code.processing.type.PTypeMirror;
import java.lang.annotation.Annotation;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;
import objectos.util.GrowableList;

public abstract class ProcessingAnnotationValue implements CanGenerateCompilationError {

  final ProcessingAnnotation annotation;
  final AnnotationValue annotationValue;
  final ExecutableElement element;

  ProcessingAnnotationValue(ProcessingAnnotation annotation,
                            ExecutableElement element,
                            AnnotationValue annotationValue) {
    this.annotation = annotation;
    this.element = element;
    this.annotationValue = annotationValue;
  }

  @Override
  public final void compilationError(String message) {
    annotation.compilationError(annotationValue, message);
  }

  @Override
  public final void compilationError(String template, Object... arguments) {
    String message = String.format(template, arguments);
    compilationError(message);
  }

  public ProcessingAnnotation getAnnotation() {
    throw new AnnotationValueTypeException(Annotation.class);
  }

  public UnmodifiableList<ProcessingAnnotation> getAnnotationArray() {
    throw new AnnotationValueTypeException(Annotation.class);
  }

  public char getChar() {
    throw new AnnotationValueTypeException(char.class);
  }

  public final <E extends Enum<E>> E getEnum(Class<E> enumType) {
    Check.notNull(enumType, "enumType == null");

    ProcessingEnumConstant enumConstant;
    enumConstant = getEnumConstant();

    return Enum.valueOf(enumType, enumConstant.getName());
  }

  public final <E extends Enum<E>> UnmodifiableList<E> getEnumArray(Class<E> enumType) {
    Check.notNull(enumType, "enumType == null");

    GrowableList<E> result;
    result = new GrowableList<>();

    UnmodifiableList<ProcessingEnumConstant> enumArray;
    enumArray = getEnumConstantArray();

    for (int i = 0; i < enumArray.size(); i++) {
      ProcessingEnumConstant constant;
      constant = enumArray.get(i);

      E value;
      value = constant.valueOf(enumType);

      result.add(value);
    }

    return result.toUnmodifiableList();
  }

  public ProcessingEnumConstant getEnumConstant() {
    throw new AnnotationValueTypeException(Enum.class);
  }

  public UnmodifiableList<ProcessingEnumConstant> getEnumConstantArray() {
    throw new AnnotationValueTypeException(Enum[].class);
  }

  public int getInt() {
    throw new AnnotationValueTypeException(int.class);
  }

  public String getString() {
    throw new AnnotationValueTypeException(String.class);
  }

  public UnmodifiableList<String> getStringArray() {
    throw new AnnotationValueTypeException(String[].class);
  }

  public PTypeMirror getType() {
    throw new AnnotationValueTypeException(Class.class);
  }

  public UnmodifiableList<PTypeMirror> getTypeArray() {
    throw new AnnotationValueTypeException(Class[].class);
  }

  public final String simpleName() {
    return element.getSimpleName().toString();
  }

}