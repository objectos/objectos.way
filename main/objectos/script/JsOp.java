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

final class JsOp {

  static final JsOp ET = of(JsString.ET);
  static final JsOp GR = of(JsString.GR);

  private final String value;

  private JsOp(String value) {
    this.value = value;
  }

  static JsOp of(JsString name) {
    return new JsOp("[" + name + "]");
  }

  static JsOp of(JsString name, Object v0) {
    return new JsOp("[" + name + "," + v0 + "]");
  }

  static JsOp of(JsString name, Object v0, Object v1) {
    return new JsOp("[" + name + "," + v0 + "," + v1 + "]");
  }

  static JsOp of(JsString name, Object v0, Object v1, Object v2) {
    return new JsOp("[" + name + "," + v0 + "," + v1 + "," + v2 + "]");
  }

  @Override
  public final String toString() {
    return value;
  }

}
