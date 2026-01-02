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

/// Represents a JS runtime `Object` instance.
public sealed class JsObject
    permits
    JsArray,
    JsFunction,
    JsNode,
    JsNumber,
    JsPromise,
    JsResponse,
    JsString {

  static final JsObject GLOBAL = new JsObject("[\"GR\"]");

  private static final JsString IU = JsString.raw("IU");

  private static final JsString IV = JsString.raw("IV");

  private static final JsString PR = JsString.raw("PR");

  private static final JsString PW = JsString.raw("PW");

  private final String value;

  JsObject(String value) {
    this.value = value;
  }

  private static JsObject of(JsObject v0, JsObject v1) {
    return new JsObject(
        v0.toString() + "," + v1.toString()
    );
  }

  /// Converts this reference to a JS reference of the specified type.
  ///
  /// @param <T> the JS runtime type
  ///
  /// @param type the handle representing the JS runtime type
  ///
  /// @return the converted reference
  public final <T> T as(JsType<T> type) {
    return type.as(value);
  }

  /// Invokes the specified method with the specified arguments, in order, if
  /// the JS object is an instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the method
  /// @param method the method name
  /// @param args the method arguments
  ///
  /// @return an object representing this action
  public final JsAction invoke(String type, String method, JsObject... args) {
    final JsArray action;
    action = invoke0(type, method, args);

    return JsAction.of(this, action);
  }

  /// Invokes the specified method with the specified arguments, in order, if
  /// the JS object is an instance of the specified type.
  ///
  /// @param <T> the JS runtime type
  /// @param returnType the return type of the invoked method
  /// @param type the name of the JS type that defines the method
  /// @param method the method name
  /// @param args the method arguments
  ///
  /// @return the result of the method invocation
  public final <T> T invoke(JsType<T> returnType, String type, String method, JsObject... args) {
    final JsArray action;
    action = invoke0(type, method, args);

    return returnType.invoke(this, action);
  }

  private JsArray invoke0(String type, String method, JsObject... args) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(args, "args == null");

    final JsString $type;
    $type = JsString.raw(type);

    final JsString $method;
    $method = JsString.raw(method);

    final JsArray $args;
    $args = JsArray.rawArgs(args);

    return JsArray.raw(IV, $type, $method, $args);
  }

  /// Invokes the specified method with the specified arguments in order.
  ///
  /// @param <T> the JS runtime type
  /// @param returnType the return type of the invoked method
  /// @param method the method name
  /// @param args the method arguments
  ///
  /// @return the result of the method invocation
  public final <T> T invokeUnchecked(JsType<T> returnType, String method, JsObject... args) {
    final JsString $method;
    $method = JsString.raw(method);

    final JsArray $args;
    $args = JsArray.rawArgs(args);

    final JsArray action;
    action = JsArray.raw(IU, $method, $args);

    return returnType.invoke(this, action);
  }

  /// Returns the property of the specified name, if the JS object is an
  /// instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the property
  /// @param name the property name
  ///
  /// @return the property
  public final JsObject prop(String type, String name) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");

    final JsString $type;
    $type = JsString.raw(type);

    final JsString $name;
    $name = JsString.raw(name);

    final JsArray action;
    action = JsArray.raw(PR, $type, $name);

    return of(this, action);
  }

  /// Sets the property of the specified name to the specified value, if the JS
  /// object is an instance of the specified type.
  ///
  /// @param type the name of the JS type that defines the property
  /// @param name the property name
  /// @param value the property value
  ///
  /// @return an object representing this action
  public final JsAction prop(String type, String name, JsObject value) {
    Objects.requireNonNull(type, "type == null");
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    final JsString $type;
    $type = JsString.raw(type);

    final JsString $name;
    $name = JsString.raw(name);

    final JsArray $value;
    $value = JsArray.raw(value);

    final JsArray action;
    action = JsArray.raw(PW, $type, $name, $value);

    return JsAction.of(this, action);
  }

  @Override
  public final String toString() {
    return value;
  }

}
