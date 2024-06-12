/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.html;

final class ActionArray extends Action {

  private final Action[] actions;

  public ActionArray(Action[] actions) {
    this.actions = actions.clone();
  }

  @Override
  final void writeTo(StringBuilder json) {
    if (actions.length > 0) {
      Action action;
      action = actions[0];

      action.writeTo(json);

      for (int idx = 1; idx < actions.length; idx++) {
        json.append(',');

        action = actions[idx];

        action.writeTo(json);
      }
    }
  }

}