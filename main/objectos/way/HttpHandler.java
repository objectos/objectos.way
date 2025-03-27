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

import java.util.function.Function;
import java.util.function.Predicate;
import objectos.way.Http.Handler;
import objectos.way.Http.Request;

final class HttpHandler implements Http.Handler {

  private enum Kind {

    NOOP,

    // delegate

    SINGLE,

    MANY,

    FACTORY1,

    // fixed content

    CONTENT,

    // pre-made responses

    METHOD_NOT_ALLOWED,

    MOVED_PERMANENTLY,

    NOT_FOUND;

  }

  private record Factory1<T>(Function<T, ? extends Handler> factory, T value) {
    final Http.Handler create() {
      return factory.apply(value);
    }
  }

  private record Content(String contentType, byte[] bytes) {}

  static final Http.Handler NOOP = new HttpHandler(Kind.NOOP, null, null);

  private static final Http.Handler NOT_FOUND = new HttpHandler(Kind.NOT_FOUND, null, null);

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

  public static <T> Http.Handler factory(Function<T, ? extends Http.Handler> factory, T value) {
    final Factory1<T> main;
    main = new Factory1<>(factory, value);

    return new HttpHandler(Kind.FACTORY1, null, main);
  }

  public static Http.Handler methodNotAllowed(Predicate<Http.Request> predicate) {
    return new HttpHandler(Kind.METHOD_NOT_ALLOWED, predicate, null);
  }

  public static Http.Handler movedPermanently(String location) {
    return new HttpHandler(Kind.MOVED_PERMANENTLY, null, location);
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

  public static Http.Handler notFound() {
    return NOT_FOUND;
  }

  public static Http.Handler ofContent(String contentType, byte[] bytes) {
    final Content main;
    main = new Content(contentType, bytes);

    return new HttpHandler(Kind.CONTENT, null, main);
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

      case FACTORY1 -> {
        Factory1<?> fac;
        fac = (Factory1<?>) main;

        Http.Handler handler;
        handler = fac.create();

        if (handler == null) {
          throw new NullPointerException("Factory returned a null HTTP handler");
        }

        handler.handle(http);
      }

      case CONTENT -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        final Content content;
        content = (Content) main;

        support.status0(Http.Status.OK);

        support.dateNow();

        support.header0(Http.HeaderName.CONTENT_TYPE, content.contentType);

        final byte[] bytes;
        bytes = content.bytes;

        support.header0(Http.HeaderName.CONTENT_LENGTH, bytes.length);

        support.send0(bytes);
      }

      case METHOD_NOT_ALLOWED -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        support.status0(Http.Status.METHOD_NOT_ALLOWED);

        support.dateNow();

        support.header0(Http.HeaderName.CONNECTION, "close");

        support.send0();
      }

      case MOVED_PERMANENTLY -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        support.status0(Http.Status.MOVED_PERMANENTLY);

        support.dateNow();

        support.header0(Http.HeaderName.LOCATION, asString());

        support.send0();
      }

      case NOT_FOUND -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        support.status0(Http.Status.NOT_FOUND);

        support.dateNow();

        support.header0(Http.HeaderName.CONNECTION, "close");

        support.send0();
      }
    }
  }

  private String asString() {
    return (String) main;
  }

}