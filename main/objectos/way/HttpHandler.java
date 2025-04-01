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

import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

final class HttpHandler implements Http.Handler {

  private enum Kind {

    NOOP,

    // delegate

    SINGLE,

    MANY,

    FACTORY1,

    FILTER_MATCHED,

    // fixed content

    CONTENT,

    // pre-made responses

    METHOD_NOT_ALLOWED,

    MOVED_PERMANENTLY,

    NOT_FOUND;

  }

  private record Factory1<T>(Function<T, ? extends Http.Handler> factory, T value) {
    final Http.Handler create() {
      return factory.apply(value);
    }
  }

  private record Content(String contentType, byte[] bytes) {}

  private record FilterMatched(Http.Handler before, Kind actual, Object main) {}

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

  public static Http.Handler methodNotAllowed(HttpPathMatcher matcher, Set<Http.Method> allowedMethods) {
    final String allow;
    allow = allowedMethods.stream().map(Http.Method::name).collect(Collectors.joining(", "));

    return new HttpHandler(Kind.METHOD_NOT_ALLOWED, matcher, allow);
  }

  public static Http.Handler movedPermanently(String location) {
    return new HttpHandler(Kind.MOVED_PERMANENTLY, null, location);
  }

  public static Http.Handler methodAllowed(HttpPathMatcher matcher, Http.Method method, Http.Handler handler) {
    record MethodAllowedPredicate(Predicate<Http.Request> matcher, Http.Method method) implements Predicate<Http.Request> {
      @Override
      public final boolean test(Http.Request t) {
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

    handle0(http, kind, main);
  }

  @Override
  public final String toString() {
    return "HttpHandler[kind=" + kind + ",predicate=" + predicate + ",main=" + main + "]";
  }

  private void handle0(Http.Exchange http, Kind actualKind, Object data) {
    switch (actualKind) {
      case NOOP -> {}

      case SINGLE -> {
        final Http.Handler single;
        single = (Http.Handler) data;

        single.handle(http);
      }

      case MANY -> {
        final Http.Handler[] many;
        many = (Http.Handler[]) data;

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
        fac = (Factory1<?>) data;

        Http.Handler handler;
        handler = fac.create();

        if (handler == null) {
          throw new NullPointerException("Factory returned a null HTTP handler");
        }

        handler.handle(http);
      }

      case FILTER_MATCHED -> {
        final FilterMatched filter;
        filter = (FilterMatched) data;

        final Http.Handler before;
        before = filter.before();

        if (before != null) {
          before.handle(http);
        }

        if (http.processed()) {
          return;
        }

        handle0(http, filter.actual, filter.main);
      }

      case CONTENT -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        final Content content;
        content = (Content) data;

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

        support.header0(Http.HeaderName.CONTENT_LENGTH, 0L);

        support.header0(Http.HeaderName.ALLOW, (String) data);

        support.send0();
      }

      case MOVED_PERMANENTLY -> {
        final HttpSupport support;
        support = (HttpSupport) http;

        support.status0(Http.Status.MOVED_PERMANENTLY);

        support.dateNow();

        support.header0(Http.HeaderName.LOCATION, (String) data);

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

  final Http.Handler filterMatched(Http.Handler before) {
    if (before == main) {
      throw new IllegalArgumentException("Cycle detected");
    }

    final FilterMatched matched;
    matched = new FilterMatched(before, kind, main);

    return new HttpHandler(Kind.FILTER_MATCHED, predicate, matched);
  }

}