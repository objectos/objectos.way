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

    SINGLE,

    MANY,

    METHOD_NOT_ALLOWED;

  }

  static final Http.Handler NOOP = new HttpHandler(Kind.NOOP, null, null);

  private final Kind kind;

  private final Predicate<Http.Request> predicate;

  private final Object main;

  private HttpHandler(Kind kind, Predicate<Http.Request> predicate, Object main) {
    this.kind = kind;
    this.predicate = predicate;
    this.main = main;
  }

  public static Http.Handler single(Predicate<Http.Request> predicate, Http.Handler handler) {
    return new HttpHandler(Kind.SINGLE, predicate, handler);
  }

  public static Http.Handler many(Http.Handler[] handlers) {
    return new HttpHandler(Kind.MANY, null, handlers);
  }

  public static Http.Handler many(Predicate<Http.Request> predicate, Http.Handler[] handlers) {
    return new HttpHandler(Kind.MANY, predicate, handlers);
  }

  public static Http.Handler methodNotAllowed(Predicate<Http.Request> predicate) {
    return new HttpHandler(Kind.METHOD_NOT_ALLOWED, predicate, null);
  }

  public static Http.Handler methodAllowed(Predicate<Http.Request> matcher, Http.Method method, Http.Handler handler) {
    record MethodAllowedPredicate(Predicate<Http.Request> matcher, Http.Method method) implements Predicate<Http.Request> {
      @Override
      public final boolean test(Request t) {
        return matcher.test(t)
            && (t.method() == method || (t.method() == Http.Method.HEAD && method == Http.Method.GET));
      }
    }

    final MethodAllowedPredicate predicate;
    predicate = new MethodAllowedPredicate(matcher, method);

    return new HttpHandler(Kind.SINGLE, predicate, handler);
  }

  @Override
  public final void handle(Http.Exchange http) {
    if (http.processed()) {
      return;
    }

    if (predicate != null && !predicate.test(http)) {
      return;
    }

    switch (kind) {
      case NOOP -> {}

      case SINGLE -> {
        final Http.Handler single;
        single = (Http.Handler) main;

        single.handle(http);
      }

      case MANY -> {
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

      case METHOD_NOT_ALLOWED -> {
        http.status(Http.Status.METHOD_NOT_ALLOWED);

        http.dateNow();

        http.header(Http.HeaderName.CONNECTION, "close");

        http.send();
      }
    }
  }

}