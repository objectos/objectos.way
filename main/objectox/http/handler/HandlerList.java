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

import java.util.List;
import objectos.http.Handler;
import objectos.http.Request;
import objectos.http.Result;

public record HandlerList(List<Handler> handlers) implements Handler {

  public HandlerList {
    if (handlers.isEmpty()) {
      final String msg;
      msg = "Handler list must not be empty";

      throw new IllegalArgumentException(msg);
    }
  }

  public static HandlerList copyOf(List<Handler> handlers) {
    final List<Handler> copy;
    copy = List.copyOf(handlers);

    return new HandlerList(copy);
  }

  @Override
  public final Result handle(Request request) {
    for (Handler handler : handlers) {
      final Result result;
      result = handler.handle(request);

      if (result != request) {
        return result;
      }
    }

    return request;
  }

}
