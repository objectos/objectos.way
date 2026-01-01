/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.function.Function;

final class ScriptJsType<T> implements Script.JsType<T> {

  private static final Script.JsString TY = ScriptJsString.raw("TY");

  private final ScriptJsString typeName;

  private final Function<String, T> constructor;

  ScriptJsType(ScriptJsString typeName, Function<String, T> constructor) {
    this.typeName = typeName;

    this.constructor = constructor;
  }

  final T as(String value) {
    final ScriptJsArray cast;
    cast = ScriptJsArray.raw(TY, typeName);

    final String computed;
    computed = value + "," + cast;

    return constructor.apply(computed);
  }

  T invoke(Script.JsObject v0, Script.JsObject v1) {
    final String value;
    value = v0 + "," + v1;

    return constructor.apply(value);
  }

}
