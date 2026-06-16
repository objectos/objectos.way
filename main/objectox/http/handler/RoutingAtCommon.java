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
package objectox.http.handler;

import java.util.ArrayList;
import java.util.List;
import objectos.http.Handler;
import objectos.http.Result;

sealed abstract class RoutingAtCommon permits RoutingAtMethod, RoutingAtPath {

  private List<Handler> handlers = List.of();

  private boolean result;

  public final Handler build() {
    return switch (handlers.size()) {
      case 0 -> HandlerNoop.INSTANCE;

      case 1 -> build(handlers.get(0));

      default -> build(HandlerList.copyOf(handlers));
    };
  }

  final void addHandler(Handler handler) {
    if (handlers.isEmpty()) {
      handlers = new ArrayList<>();
    }

    handlers.add(handler);
  }

  abstract Handler build(Handler handler);

  final void result(Result value) {
    if (result) {
      final String msg;
      msg = "A result has already been set";

      throw new IllegalStateException(msg);
    }

    final Handler handler;
    handler = new HandlerResult(value);

    addHandler(handler);

    result = true;
  }

}
