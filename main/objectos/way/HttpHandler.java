/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import java.util.function.Predicate;
import objectos.way.Http.Request;

final class HttpHandler implements Http.Handler {

  private enum Kind {

    NOOP,

    SINGLE_HANDLER,

    MANY_HANDLERS,

    PREDICATE_SINGLE_HANDLER,

    PREDICATE_MANY_HANDLERS;

  }

  static final Http.Handler NOOP = new HttpHandler(Kind.NOOP, null, null);

  private final Kind kind;

  private final Predicate<Http.Request> predicate;

  private final Object main;

  private HttpHandler(Kind kind, Predicate<Request> predicate, Object main) {
    this.kind = kind;
    this.predicate = predicate;
    this.main = main;
  }

  public static Http.Handler single(Predicate<Http.Request> predicate, Http.Handler handler) {
    Kind kind = predicate == null ? Kind.SINGLE_HANDLER : Kind.PREDICATE_SINGLE_HANDLER;

    return new HttpHandler(kind, predicate, handler);
  }

  public static Http.Handler many(Predicate<Http.Request> predicate, Http.Handler[] handlers) {
    Kind kind = predicate == null ? Kind.MANY_HANDLERS : Kind.PREDICATE_MANY_HANDLERS;

    return new HttpHandler(kind, predicate, handlers);
  }

  @Override
  public final void handle(Http.Exchange http) {
    switch (kind) {
      case NOOP -> {}

      case SINGLE_HANDLER -> single(http);

      case MANY_HANDLERS -> many(http);

      case PREDICATE_SINGLE_HANDLER -> {
        if (predicate.test(http)) {
          single(http);
        }
      }

      case PREDICATE_MANY_HANDLERS -> {
        if (predicate.test(http)) {
          many(http);
        }
      }
    }
  }

  private void many(Http.Exchange http) {
    final Http.Handler[] many;
    many = (Http.Handler[]) main;

    int index;
    index = 0;

    while (!http.processed() && index < many.length) {
      final Http.Handler handler;
      handler = many[index++];

      handler.handle(http);
    }
  }

  private void single(Http.Exchange http) {
    if (http.processed()) {
      return;
    }

    final Http.Handler single;
    single = (Http.Handler) main;

    single.handle(http);
  }

}