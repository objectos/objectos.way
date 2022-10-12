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

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.UnknownElementException;

abstract class AbstractSimpleElementVisitor<R, P> implements ElementVisitor<R, P> {

  private final R defaultValue;

  AbstractSimpleElementVisitor() {
    this.defaultValue = null;
  }

  AbstractSimpleElementVisitor(R defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public final R visit(Element e) {
    return e.accept(this, null);
  }

  @Override
  public final R visit(Element e, P p) {
    return e.accept(this, p);
  }

  @Override
  public R visitUnknown(Element e, P p) {
    throw new UnknownElementException(e, p);
  }

  protected R defaultAction(Element e, P p) {
    return defaultValue;
  }

}
