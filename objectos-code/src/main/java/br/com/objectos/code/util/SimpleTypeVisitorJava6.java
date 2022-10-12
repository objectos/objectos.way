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

import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

abstract class SimpleTypeVisitorJava6<R, P> extends AbstractSimpleTypeVisitor<R, P> {

  protected SimpleTypeVisitorJava6() {}

  protected SimpleTypeVisitorJava6(R defaultValue) {
    super(defaultValue);
  }

  @Override
  public R visitArray(ArrayType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitDeclared(DeclaredType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitError(ErrorType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitExecutable(ExecutableType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNoType(NoType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNull(NullType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitPrimitive(PrimitiveType t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitTypeVariable(TypeVariable t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitWildcard(WildcardType t, P p) {
    return defaultAction(t, p);
  }

}
