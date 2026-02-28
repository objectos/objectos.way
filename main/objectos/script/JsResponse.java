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

/// A JS runtime `Response` instance.
public final class JsResponse extends JsObject {

  /// Represents the `Response` JS type.
  public static final JsType<JsResponse> type = new JsType<>("Response") {
    @Override
    final JsResponse create(Object value) {
      return new JsResponse(value);
    }

    @Override
    final JsResponse create(JsObject recv, JsArray op) {
      return new JsResponse(recv, op);
    }
  };

  private JsResponse(Object value) {
    super(value);
  }

  private JsResponse(JsObject recv, JsArray op) {
    super(recv, op);
  }

  /// Returns the value of the `ok` property stating if the response status was
  /// in the range `200-299` or not.
  ///
  /// @return a boolean value
  public final JsBoolean ok() {
    return prop("Response", "ok").as(JsBoolean.type);
  }

  /// Reads the response stream to its completion.
  ///
  /// @return a promise that resolves with a `JsString`
  public final JsPromise text() {
    return invoke(JsPromise.type, "Response", "text");
  }

}
