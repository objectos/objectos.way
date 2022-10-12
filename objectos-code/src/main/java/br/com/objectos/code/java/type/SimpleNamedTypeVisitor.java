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
package br.com.objectos.code.java.type;

import br.com.objectos.code.java.type.NamedWildcard.Extends;
import br.com.objectos.code.java.type.NamedWildcard.Super;
import br.com.objectos.code.java.type.NamedWildcard.Unbound;

public class SimpleNamedTypeVisitor<R, P> implements NamedTypeVisitor<R, P> {

  protected final R defaultValue;

  protected SimpleNamedTypeVisitor() {
    this(null);
  }

  protected SimpleNamedTypeVisitor(R defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public R visitNamedArray(NamedArray t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedClass(NamedClass t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedVoid(NamedVoid t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedParameterized(NamedParameterized t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedPrimitive(NamedPrimitive t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedTypeVariable(NamedTypeVariable t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedWildcardExtends(Extends t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedWildcardSuper(Super t, P p) {
    return defaultAction(t, p);
  }

  @Override
  public R visitNamedWildcardUnbound(Unbound t, P p) {
    return defaultAction(t, p);
  }

  protected R defaultAction(NamedType t, P p) {
    return defaultValue;
  }

}