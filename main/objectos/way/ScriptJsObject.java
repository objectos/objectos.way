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

import java.util.Objects;
import java.util.function.Function;

sealed class ScriptJsObject
    implements Script.JsObject
    permits
    ScriptJsArray,
    ScriptJsElement,
    ScriptJsString, ScriptJsNumber {

  static final ScriptJsObject GLOBAL = new ScriptJsObject("[\"GR\"]");

  private static final Script.JsString IV = ScriptJsString.raw("IV");

  private static final Script.JsString PR = ScriptJsString.raw("PR");

  private static final Script.JsString PW = ScriptJsString.raw("PW");

  private static final Script.JsString TY = ScriptJsString.raw("TY");

  private final String value;

  ScriptJsObject(String value) {
    this.value = value;
  }

  private static ScriptJsObject of(Script.JsObject v0, Script.JsObject v1) {
    return new ScriptJsObject(
        v0.toString() + "," + v1.toString()
    );
  }

  @Override
  public final Script.JsString asString() {
    return ScriptJsString.cast(this);
  }

  @Override
  public final Script.JsAction invoke(String type, String method, Script.JsObject... args) {
    final Script.JsArray action;
    action = invoke0(type, method, args);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final <T> T invoke(Script.JsType<T> returnType, String type, String method, Script.JsObject... args) {
    Objects.requireNonNull(returnType, "returnType == null");

    final Script.JsArray action;
    action = invoke0(type, method, args);

    return ScriptJsType.of(returnType, this, action);
  }

  private Script.JsArray invoke0(String type, String method, Script.JsObject... args) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(args, "args == null");

    final Script.JsString $type;
    $type = ScriptJsString.raw(type);

    final Script.JsString $method;
    $method = ScriptJsString.raw(method);

    final Script.JsArray $args;
    $args = ScriptJsArray.rawArgs(args);

    return ScriptJsArray.raw(IV, $type, $method, $args);
  }

  @Override
  public final Script.JsObject prop(String type, String name) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");

    final Script.JsString $type;
    $type = ScriptJsString.raw(type);

    final Script.JsString $name;
    $name = ScriptJsString.raw(name);

    final Script.JsArray action;
    action = ScriptJsArray.raw(PR, $type, $name);

    return of(this, action);
  }

  @Override
  public final Script.JsAction prop(String type, String name, Script.JsObject value) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final Script.JsString $type;
    $type = ScriptJsString.raw(type);

    final Script.JsString $name;
    $name = ScriptJsString.raw(name);

    final Script.JsArray $value;
    $value = ScriptJsArray.raw(value);

    final Script.JsArray action;
    action = ScriptJsArray.raw(PW, $type, $name, $value);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final String toString() {
    return value;
  }

  final <T> T cast(Function<String, T> constructor, Script.JsString typeName) {
    final ScriptJsArray cast;
    cast = ScriptJsArray.raw(TY, typeName);

    final String computed;
    computed = value + "," + cast;

    return constructor.apply(computed);
  }

}
