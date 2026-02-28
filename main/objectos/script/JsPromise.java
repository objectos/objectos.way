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

/// A JS runtime `Promise` instance.
public final class JsPromise extends JsObject {

  /// Represents the `Promise` JS type.
  public static final JsType<JsPromise> type = new JsType<>("Promise") {
    @Override
    final JsPromise create(Object value) {
      return new JsPromise(value);
    }

    @Override
    final JsPromise create(JsObject recv, JsArray op) {
      return new JsPromise(recv, op);
    }
  };

  private JsPromise(Object value) {
    super(value);
  }

  private JsPromise(JsObject recv, JsArray op) {
    super(recv, op);
  }

  /// Executes the specified action when this promise becomes fulfilled.
  ///
  /// @param value the action to execute
  ///
  /// @return an object representing this action
  public final JsAction then(JsAction value) {
    return invoke("Promise", "then", JsFunction.of(value));
  }

  /// Returns a promise to the specified value to be returned when this promise
  /// becomes fulfilled.
  ///
  /// @param value the value to be returned
  ///
  /// @return a promise to the specified value
  public final JsPromise then(JsObject value) {
    return invoke(JsPromise.type, "Promise", "then", JsFunction.of(value));
  }

}
