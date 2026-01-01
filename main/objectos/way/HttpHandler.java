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
package objectos.way;

import java.util.List;
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

    FILTER,

    FACTORY1,

    METHOD_ALLOWED_SINGLE,

    METHOD_ALLOWED_MANY,

    // pre-made responses

    METHOD_NOT_ALLOWED,

    NOT_FOUND;

  }

  private record Factory1<T>(Function<T, ? extends Http.Handler> factory, T value) {
    final Http.Handler create() {
      return factory.apply(value);
    }
  }

  private record FilterHolder(Http.Filter filter, Http.Handler handler) {}

  static final Http.Handler NOOP = new HttpHandler(Kind.NOOP, null, null);

  private static final Http.Handler NOT_FOUND = new HttpHandler(Kind.NOT_FOUND, null, null);

  private final Kind kind;

  private final Predicate<? super Http.Exchange> predicate;

  private final Object main;

  private HttpHandler(Kind kind, Predicate<? super Http.Exchange> predicate, Object main) {
    this.kind = kind;
    this.predicate = predicate;
    this.main = main;
  }

  public static Http.Handler single(Predicate<? super Http.Request> predicate, Http.Handler handler) {
    return new HttpHandler(Kind.SINGLE, predicate, handler);
  }

  public static Http.Handler many(Predicate<? super Http.Request> predicate, Http.Handler[] handlers) {
    return new HttpHandler(Kind.MANY, predicate, handlers);
  }

  public static <T> Http.Handler factory(Function<T, ? extends Http.Handler> factory, T value) {
    final Factory1<T> main;
    main = new Factory1<>(factory, value);

    return new HttpHandler(Kind.FACTORY1, null, main);
  }

  public static Http.Handler methodNotAllowed(Set<Http.Method> allowedMethods) {
    final String allow;
    allow = allowedMethods.stream().map(Http.Method::name).collect(Collectors.joining(", "));

    return new HttpHandler(Kind.METHOD_NOT_ALLOWED, null, allow);
  }

  public static Http.Handler methodAllowed(Http.Method method, Http.Handler handler) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodAllowed(method);

    return new HttpHandler(Kind.METHOD_ALLOWED_SINGLE, predicate, handler);
  }

  public static Http.Handler methodAllowed(Http.Method method, Http.Handler first, Http.Handler[] rest) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodAllowed(method);

    final Http.Handler[] array;
    array = new Http.Handler[rest.length + 1];

    array[0] = first;

    for (int idx = 0; idx < rest.length; idx++) {
      array[idx + 1] = Check.notNull(rest[idx], "rest[", idx, "] == null");
    }

    return new HttpHandler(Kind.METHOD_ALLOWED_MANY, predicate, array);
  }

  public static Http.Handler notFound() {
    return NOT_FOUND;
  }

  public static Http.Handler of(Predicate<? super Http.Exchange> condition, Http.Filter filter, List<Http.Handler> handlers) {
    if (handlers == null || handlers.isEmpty()) {
      return ofNoop(condition, Kind.FILTER, filter, Kind.SINGLE);
    }

    return switch (handlers.size()) {
      case 1 -> {
        final Http.Handler single;
        single = handlers.get(0);

        yield ofSingle(condition, Kind.FILTER, filter, Kind.SINGLE, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = handlers.toArray(Http.Handler[]::new);

        yield ofMany(condition, Kind.FILTER, filter, Kind.MANY, copy);
      }
    };
  }

  private static Http.Handler ofNoop(Predicate<? super Http.Exchange> condition, Kind filterKind, Http.Filter filter, Kind singleKind) {
    if (condition == null) {

      if (filter == null) {
        return NOOP;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, NOOP);

      return new HttpHandler(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler(singleKind, condition, NOOP);
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, NOOP);

      return new HttpHandler(filterKind, condition, holder);

    }
  }

  private static Http.Handler ofSingle(Predicate<? super Http.Exchange> condition, Kind filterKind, Http.Filter filter, Kind singleKind, Http.Handler single) {
    if (condition == null) {

      if (filter == null) {
        return single;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, single);

      return new HttpHandler(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler(singleKind, condition, single);
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, single);

      return new HttpHandler(filterKind, condition, holder);

    }
  }

  private static Http.Handler ofMany(Predicate<? super Http.Exchange> condition, Kind filterKind, Http.Filter filter, Kind manyKind, Http.Handler[] many) {
    if (condition == null) {

      final HttpHandler handler;
      handler = new HttpHandler(manyKind, null, many);

      if (filter == null) {
        return handler;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, handler);

      return new HttpHandler(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler(manyKind, condition, many);
      }

      final HttpHandler handler;
      handler = new HttpHandler(manyKind, null, many);

      final FilterHolder holder;
      holder = new FilterHolder(filter, handler);

      return new HttpHandler(filterKind, condition, holder);

    }
  }

  @Override
  public final void handle(Http.Exchange xch) {
    if (xch.processed()) {
      return;
    }

    final HttpExchange http;
    http = (HttpExchange) xch;

    if (predicate != null && !predicate.test(http)) {
      return;
    }

    switch (kind) {
      case NOOP -> {}

      case SINGLE -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final Http.Handler single;
        single = (Http.Handler) main;

        single.handle(http);

        if (!http.processed()) {
          http.pathIndex(pathIndex);
        }
      }

      case MANY -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final Http.Handler[] many;
        many = (Http.Handler[]) main;

        int index;
        index = 0;

        while (index < many.length) {
          final Http.Handler handler;
          handler = many[index++];

          handler.handle(http);

          if (http.processed()) {
            break;
          }

          http.pathIndex(pathIndex);
        }
      }

      case FILTER -> {
        final FilterHolder holder;
        holder = (FilterHolder) main;

        final Http.Filter filter;
        filter = holder.filter;

        final Http.Handler handler;
        handler = holder.handler;

        filter.filter(http, handler);
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

      case METHOD_ALLOWED_SINGLE -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final Http.Handler single;
        single = (Http.Handler) main;

        single.handle(http);

        if (!http.processed()) {
          http.pathIndex(pathIndex);

          http.status(Http.Status.NO_CONTENT);

          http.header(Http.HeaderName.DATE, http.now());

          http.header(Http.HeaderName.CONTENT_LENGTH, 0L);

          http.send();
        }
      }

      case METHOD_ALLOWED_MANY -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final Http.Handler[] many;
        many = (Http.Handler[]) main;

        int index;
        index = 0;

        while (index < many.length) {
          final Http.Handler handler;
          handler = many[index++];

          handler.handle(http);

          if (http.processed()) {
            break;
          }

          http.pathIndex(pathIndex);
        }

        if (!http.processed()) {
          http.pathIndex(pathIndex);

          http.status(Http.Status.NO_CONTENT);

          http.header(Http.HeaderName.DATE, http.now());

          http.header(Http.HeaderName.CONTENT_LENGTH, 0L);

          http.send();
        }
      }

      case METHOD_NOT_ALLOWED -> {
        http.status(Http.Status.METHOD_NOT_ALLOWED);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.CONTENT_LENGTH, 0L);

        http.header(Http.HeaderName.ALLOW, (String) main);

        http.send();
      }

      case NOT_FOUND -> {
        http.status(Http.Status.NOT_FOUND);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.CONTENT_LENGTH, 0L);

        http.header(Http.HeaderName.CONNECTION, "close");

        http.send();
      }
    }
  }

  @Override
  public final String toString() {
    return "HttpHandler[kind=" + kind + ",predicate=" + predicate + ",main=" + main + "]";
  }

}