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

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

abstract class SimpleElementVisitorJava6<R, P> extends AbstractSimpleElementVisitor<R, P> {

  protected SimpleElementVisitorJava6() {}

  protected SimpleElementVisitorJava6(R defaultValue) {
    super(defaultValue);
  }

  @Override
  public R visitExecutable(ExecutableElement e, P p) {
    return defaultAction(e, p);
  }

  @Override
  public R visitPackage(PackageElement e, P p) {
    return defaultAction(e, p);
  }

  @Override
  public R visitType(TypeElement e, P p) {
    return defaultAction(e, p);
  }

  @Override
  public R visitTypeParameter(TypeParameterElement e, P p) {
    return defaultAction(e, p);
  }

  @Override
  public R visitVariable(VariableElement e, P p) {
    return defaultAction(e, p);
  }

}
