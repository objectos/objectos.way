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

import java.util.Objects;

sealed class ScriptJsObject
    implements JsObject
    permits
    ScriptJsArray,
    ScriptJsElement,
    ScriptJsString, ScriptJsNumber {

  static final ScriptJsObject GLOBAL = new ScriptJsObject("[\"GR\"]");

  private static final JsString IV = ScriptJsString.raw("IV");

  private static final JsString PR = ScriptJsString.raw("PR");

  private static final JsString PW = ScriptJsString.raw("PW");

  private final String value;

  ScriptJsObject(String value) {
    this.value = value;
  }

  private static ScriptJsObject of(JsObject v0, JsObject v1) {
    return new ScriptJsObject(
        v0.toString() + "," + v1.toString()
    );
  }

  @Override
  public final <T> T as(JsType<T> type) {
    final ScriptJsType<T> impl;
    impl = (ScriptJsType<T>) Objects.requireNonNull(type, "type == null");

    return impl.as(value);
  }

  @Override
  public final JsAction invoke(String type, String method, JsObject... args) {
    final JsArray action;
    action = invoke0(type, method, args);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final <T> T invoke(JsType<T> returnType, String type, String method, JsObject... args) {
    final ScriptJsType<T> impl;
    impl = (ScriptJsType<T>) Objects.requireNonNull(returnType, "returnType == null");

    final JsArray action;
    action = invoke0(type, method, args);

    return impl.invoke(this, action);
  }

  private JsArray invoke0(String type, String method, JsObject... args) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(args, "args == null");

    final JsString $type;
    $type = ScriptJsString.raw(type);

    final JsString $method;
    $method = ScriptJsString.raw(method);

    final JsArray $args;
    $args = ScriptJsArray.rawArgs(args);

    return ScriptJsArray.raw(IV, $type, $method, $args);
  }

  @Override
  public final JsObject prop(String type, String name) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");

    final JsString $type;
    $type = ScriptJsString.raw(type);

    final JsString $name;
    $name = ScriptJsString.raw(name);

    final JsArray action;
    action = ScriptJsArray.raw(PR, $type, $name);

    return of(this, action);
  }

  @Override
  public final JsAction prop(String type, String name, JsObject value) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final JsString $type;
    $type = ScriptJsString.raw(type);

    final JsString $name;
    $name = ScriptJsString.raw(name);

    final JsArray $value;
    $value = ScriptJsArray.raw(value);

    final JsArray action;
    action = ScriptJsArray.raw(PW, $type, $name, $value);

    return ScriptJsAction.of(this, action);
  }

  @Override
  public final String toString() {
    return value;
  }

}
