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

import module java.base;

/// The **Objectos Script** main class, part of Objectos HTML.
public final class Js {

  /// Sole constructor.
  private Js() {}

  public static JsArray array(String... values) {
    final JsArray.Builder builder;
    builder = JsArray.jsBuilder();

    builder.rawAll(values, "values");

    return builder.build();
  }

  public static JsRef args(int index) {
    return JsRef.args(index);
  }

  /// Returns a handle for the element with the specified ID attribute.
  ///
  /// @param value the ID value
  ///
  /// @return the element
  public static JsElement byId(JsString value) {
    return JsElement.byId(value);
  }

  /// Returns a handle for the element with the specified ID attribute.
  ///
  /// @param value the ID value
  ///
  /// @return the element
  public static JsElement byId(String value) {
    final String v;
    v = Objects.requireNonNull(value, "value == null");

    final JsString id;
    id = JsString.of(v);

    return JsElement.byId(id);
  }

  public static JsPromise fetch(JsString resource) {
    return global().invokeUnchecked(JsPromise.type, "fetch", resource);
  }

  /// The object which represents the global scope.
  ///
  /// @return the global scope object
  public static JsObject global() {
    return new JsObject(JsOp.GR);
  }

  /// Creates an action by concatenating all of the specified individual
  /// actions.
  ///
  /// @param first the first action
  /// @param second the second action
  /// @param more additional actions
  ///
  /// @return an object representing the concatenated action
  public static JsAction of(JsAction first, JsAction second, JsAction... more) {
    return JsAction.seq(first, second, more);
  }

  /// The no-op JS action.
  ///
  /// @return the no-op action
  public static JsAction noop() {
    return JsAction.NOOP;
  }

  /// The element which triggered the Objectos Script.
  ///
  /// @return the element
  public static JsElement target() {
    return JsElement.TARGET;
  }

  /// Throws a new JS `Error` instance with the specified message.
  ///
  /// @param msg the description of the error
  ///
  /// @return an object representing this action
  public static JsAction throwError(JsString msg) {
    return JsAction.throwError(msg);
  }

  /// Retrieves the named property from the event context.
  ///
  /// @param name the property name
  ///
  /// @return an object representing a JS reference
  public static JsRef var(String name) {
    return JsRef.var(name);
  }

  /// Stores the specified value to a named property in the event context.
  ///
  /// @param name the property name
  /// @param value the value to store
  ///
  /// @return an object representing this action
  public static JsAction var(String name, JsObject value) {
    return JsAction.var(name, value);
  }

}