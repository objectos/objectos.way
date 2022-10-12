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

import javax.lang.model.type.UnionType;

abstract class SimpleTypeVisitorJava7<R, P> extends SimpleTypeVisitorJava6<R, P> {

  protected SimpleTypeVisitorJava7() {}

  protected SimpleTypeVisitorJava7(R defaultValue) {
    super(defaultValue);
  }

  @Override
  public R visitUnion(UnionType t, P p) {
    return defaultAction(t, p);
  }

}
