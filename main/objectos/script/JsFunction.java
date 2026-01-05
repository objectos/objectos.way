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

final class JsFunction extends JsObject {

  JsFunction(JsOp value) {
    super(value);
  }

  static JsFunction of(JsAction value) {
    final JsOp op;
    op = JsOp.of(JsString.FN, value);

    return new JsFunction(op);
  }

  static JsFunction of(JsObject value) {
    final JsOp op;
    op = JsOp.of(JsString.FN, value);

    return new JsFunction(op);
  }

}
