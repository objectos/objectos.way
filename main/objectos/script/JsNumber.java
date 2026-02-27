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

/// A JS runtime `Number` instance.
public final class JsNumber extends JsObject {

  /// Represents the `number` JS type.
  public static final JsType<JsNumber> type = new JsType<>("number") {
    @Override
    final JsNumber create(Object value) {
      return new JsNumber(value);
    }

    @Override
    final JsNumber create(JsObject recv, JsOp op) {
      return new JsNumber(recv, op);
    }
  };

  private JsNumber(Object value) {
    super(value);
  }

  private JsNumber(JsObject recv, JsOp op) {
    super(recv, op);
  }

  public static JsNumber of(int value) {
    final String n;
    n = Integer.toString(value);

    final JsOp op;
    op = JsOp.of(JsString.JS, n);

    return new JsNumber(op);
  }

  static JsNumber raw(int value) {
    return new JsNumber(
        Integer.toString(value)
    );
  }

}
