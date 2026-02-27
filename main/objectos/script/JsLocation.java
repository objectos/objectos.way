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

/// A JS runtime `Location` instance.
public final class JsLocation extends JsObject {

  /// Represents the `Location` JS type.
  public static final JsType<JsLocation> type = new JsType<>("Location") {
    @Override
    final JsLocation create(Object value) {
      return new JsLocation(value);
    }

    @Override
    final JsLocation create(JsObject recv, JsOp op) {
      return new JsLocation(recv, op);
    }
  };

  private JsLocation(Object value) {
    super(value);
  }

  private JsLocation(JsObject recv, JsOp op) {
    super(recv, op);
  }

  /// Returns the `hash` property of this location.
  ///
  /// @return the `hash` property of this location
  public final JsString hash() {
    return prop("Location", "hash").as(JsString.type);
  }

  /// Returns the `href` property of this location.
  ///
  /// @return the `href` property of this location
  public final JsString href() {
    return prop("Location", "href").as(JsString.type);
  }

}
