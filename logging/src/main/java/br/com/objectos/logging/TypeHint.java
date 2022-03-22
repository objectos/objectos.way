/*
 * Copyright (C) 2021-2022 Objectos Software LTDA.
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
package br.com.objectos.logging;

public final class TypeHint<T> {

  static final TypeHint<Object> OBJECT = new TypeHint<Object>();

  private TypeHint() {}

  @SuppressWarnings("unchecked")
  public static <T> TypeHint<T> get() {
    return (TypeHint<T>) TypeHint.OBJECT;
  }

  public static <T> TypeHint<T> of(Class<T> typeHint) {
    Checks.checkNotNull(typeHint, "typeHint == null");

    return get();
  }

  static <T> TypeHint<T> of0(Class<T> typeHint) {
    return get();
  }

}