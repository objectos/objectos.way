/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectos.script;

import java.util.function.BiFunction;

/// Represents the type of a JS runtime value.
public final class JsType<T> {

  private final JsString typeName;

  private final BiFunction<JsBase, JsOp, T> constructor;

  JsType(JsString typeName, BiFunction<JsBase, JsOp, T> constructor) {
    this.typeName = typeName;

    this.constructor = constructor;
  }

  final T as(JsBase value) {
    final JsOp cast;
    cast = JsOp.of(JsString.TY, typeName);

    return constructor.apply(value, cast);
  }

  final T invoke(JsObject recv, JsOp op) {
    return constructor.apply(recv, op);
  }

}
