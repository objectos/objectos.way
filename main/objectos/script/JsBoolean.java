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

/// A JS runtime `Boolean` instance.
public final class JsBoolean extends JsObject {

  /// Represents the `boolean` JS type.
  public static final JsType<JsBoolean> type = new JsType<>(
      JsString.raw("boolean"), JsBoolean::new
  );

  private JsBoolean(JsBase recv, JsOp op) {
    super(recv, op);
  }

  /// Performs an action equivalent to an `if-else` statement on this boolean
  /// value.
  ///
  /// @param onTrue the action to execute if this value is `true`
  /// @param onFalse the action to execute if this value is `false`
  ///
  /// @return an object representing this action
  public final JsAction test(JsAction onTrue, JsAction onFalse) {
    Objects.requireNonNull(onTrue, "onTrue == null");
    Objects.requireNonNull(onFalse, "onFalse == null");

    final JsOp op;
    op = JsOp.of(JsString.IF, onTrue, onFalse);

    return JsAction.one(this, op);
  }

}
