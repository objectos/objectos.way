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
package br.com.objectos.code.util;

import java.util.List;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.UnknownAnnotationValueException;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class SimpleAnnotationValueVisitor<R, P> implements AnnotationValueVisitor<R, P> {

  private final R defaultValue;

  protected SimpleAnnotationValueVisitor() {
    defaultValue = null;
  }

  protected SimpleAnnotationValueVisitor(R defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public final R visit(AnnotationValue av) {
    return av.accept(this, null);
  }

  @Override
  public final R visit(AnnotationValue av, P p) {
    return av.accept(this, p);
  }

  @Override
  public R visitAnnotation(AnnotationMirror a, P p) {
    return defaultAction(a, p);
  }

  @Override
  public R visitArray(List<? extends AnnotationValue> vals, P p) {
    return defaultAction(vals, p);
  }

  @Override
  public R visitBoolean(boolean b, P p) {
    return defaultAction(b, p);
  }

  @Override
  public R visitByte(byte b, P p) {
    return defaultAction(b, p);
  }

  @Override
  public R visitChar(char c, P p) {
    return defaultAction(c, p);
  }

  @Override
  public R visitDouble(double d, P p) {
    return defaultAction(d, p);
  }

  @Override
  public R visitEnumConstant(VariableElement c, P p) {
    return defaultAction(c, p);
  }

  @Override
  public R visitFloat(float f, P p) {
    return defaultAction(f, p);
  }

  @Override
  public R visitInt(int i, P p) {
    return defaultAction(i, p);
  }

  @Override
  public R visitLong(long i, P p) {
    return defaultAction(i, p);
  }

  @Override
  public R visitShort(short s, P p) {
    return defaultAction(s, p);
  }

  @Override
  public R visitString(String s, P p) {
    return defaultAction(s, p);
  }

  @Override
  public R visitType(TypeMirror t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitUnknown(AnnotationValue av, P p) {
    throw new UnknownAnnotationValueException(av, p);
  }

  protected R defaultAction(Object o, P p) {
    return defaultValue;
  }

}
