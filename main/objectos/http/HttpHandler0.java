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
package objectos.http;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import objectos.internal.Check;

final class HttpHandler0 implements HttpHandler {

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

  private record Factory1<T>(Function<T, ? extends HttpHandler> factory, T value) {
    final HttpHandler create() {
      return factory.apply(value);
    }
  }

  private record FilterHolder(HttpFilter filter, HttpHandler handler) {}

  static final HttpHandler NOOP = new HttpHandler0(Kind.NOOP, null, null);

  private static final HttpHandler NOT_FOUND = new HttpHandler0(Kind.NOT_FOUND, null, null);

  private final Kind kind;

  private final Predicate<? super HttpExchange> predicate;

  private final Object main;

  private HttpHandler0(Kind kind, Predicate<? super HttpExchange> predicate, Object main) {
    this.kind = kind;

    this.predicate = predicate;

    this.main = main;
  }

  public static HttpHandler single(Predicate<? super HttpRequest> predicate, HttpHandler handler) {
    return new HttpHandler0(Kind.SINGLE, predicate, handler);
  }

  public static HttpHandler many(Predicate<? super HttpRequest> predicate, HttpHandler[] handlers) {
    return new HttpHandler0(Kind.MANY, predicate, handlers);
  }

  public static <T> HttpHandler factory(Function<T, ? extends HttpHandler> factory, T value) {
    final Factory1<T> main;
    main = new Factory1<>(factory, value);

    return new HttpHandler0(Kind.FACTORY1, null, main);
  }

  public static HttpHandler methodNotAllowed(Set<HttpMethod> allowedMethods) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodNotAllowed(allowedMethods);

    final String allow;
    allow = allowedMethods.stream().map(HttpMethod::name).collect(Collectors.joining(", "));

    return new HttpHandler0(Kind.METHOD_NOT_ALLOWED, predicate, allow);
  }

  public static HttpHandler methodAllowed(HttpMethod method, HttpHandler handler) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodAllowed(method);

    return new HttpHandler0(Kind.METHOD_ALLOWED_SINGLE, predicate, handler);
  }

  public static HttpHandler methodAllowed(HttpMethod method, HttpHandler first, HttpHandler[] rest) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodAllowed(method);

    final HttpHandler[] array;
    array = new HttpHandler[rest.length + 1];

    array[0] = first;

    for (int idx = 0; idx < rest.length; idx++) {
      array[idx + 1] = Check.notNull(rest[idx], "rest[", idx, "] == null");
    }

    return new HttpHandler0(Kind.METHOD_ALLOWED_MANY, predicate, array);
  }

  public static HttpHandler notFound() {
    return NOT_FOUND;
  }

  public static HttpHandler of(Predicate<? super HttpExchange> condition, HttpFilter filter, List<HttpHandler> handlers) {
    if (handlers == null || handlers.isEmpty()) {
      return ofNoop(condition, Kind.FILTER, filter, Kind.SINGLE);
    }

    return switch (handlers.size()) {
      case 1 -> {
        final HttpHandler single;
        single = handlers.get(0);

        yield ofSingle(condition, Kind.FILTER, filter, Kind.SINGLE, single);
      }

      default -> {
        final HttpHandler[] copy;
        copy = handlers.toArray(HttpHandler[]::new);

        yield ofMany(condition, Kind.FILTER, filter, Kind.MANY, copy);
      }
    };
  }

  private static HttpHandler ofNoop(Predicate<? super HttpExchange> condition, Kind filterKind, HttpFilter filter, Kind singleKind) {
    if (condition == null) {

      if (filter == null) {
        return NOOP;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, NOOP);

      return new HttpHandler0(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler0(singleKind, condition, NOOP);
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, NOOP);

      return new HttpHandler0(filterKind, condition, holder);

    }
  }

  private static HttpHandler ofSingle(Predicate<? super HttpExchange> condition, Kind filterKind, HttpFilter filter, Kind singleKind, HttpHandler single) {
    if (condition == null) {

      if (filter == null) {
        return single;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, single);

      return new HttpHandler0(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler0(singleKind, condition, single);
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, single);

      return new HttpHandler0(filterKind, condition, holder);

    }
  }

  private static HttpHandler ofMany(Predicate<? super HttpExchange> condition, Kind filterKind, HttpFilter filter, Kind manyKind, HttpHandler[] many) {
    if (condition == null) {

      final HttpHandler0 handler;
      handler = new HttpHandler0(manyKind, null, many);

      if (filter == null) {
        return handler;
      }

      final FilterHolder holder;
      holder = new FilterHolder(filter, handler);

      return new HttpHandler0(filterKind, null, holder);

    }

    else {

      if (filter == null) {
        return new HttpHandler0(manyKind, condition, many);
      }

      final HttpHandler0 handler;
      handler = new HttpHandler0(manyKind, null, many);

      final FilterHolder holder;
      holder = new FilterHolder(filter, handler);

      return new HttpHandler0(filterKind, condition, holder);

    }
  }

  @Override
  public final void handle(HttpExchange xch) {
    if (xch.processed()) {
      return;
    }

    final HttpExchange0 http;
    http = (HttpExchange0) xch;

    if (predicate != null && !predicate.test(http)) {
      return;
    }

    switch (kind) {
      case NOOP -> {}

      case SINGLE -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final HttpHandler single;
        single = (HttpHandler) main;

        single.handle(http);

        if (!http.processed()) {
          http.pathIndex(pathIndex);
        }
      }

      case MANY -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final HttpHandler[] many;
        many = (HttpHandler[]) main;

        int index;
        index = 0;

        while (index < many.length) {
          final HttpHandler handler;
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

        final HttpFilter filter;
        filter = holder.filter;

        final HttpHandler handler;
        handler = holder.handler;

        filter.filter(http, handler);
      }

      case FACTORY1 -> {
        Factory1<?> fac;
        fac = (Factory1<?>) main;

        HttpHandler handler;
        handler = fac.create();

        if (handler == null) {
          throw new NullPointerException("Factory returned a null HTTP handler");
        }

        handler.handle(http);
      }

      case METHOD_ALLOWED_SINGLE -> {
        final HttpHandler single;
        single = (HttpHandler) main;

        single.handle(http);
      }

      case METHOD_ALLOWED_MANY -> {
        final int pathIndex;
        pathIndex = http.pathIndex();

        final HttpHandler[] many;
        many = (HttpHandler[]) main;

        int index;
        index = 0;

        while (index < many.length) {
          final HttpHandler handler;
          handler = many[index++];

          handler.handle(http);

          if (http.processed()) {
            break;
          }

          http.pathIndex(pathIndex);
        }
      }

      case METHOD_NOT_ALLOWED -> {
        http.status(HttpStatus.METHOD_NOT_ALLOWED);

        http.header(HttpHeaderName.DATE, http.now());

        http.header(HttpHeaderName.CONTENT_LENGTH, 0L);

        http.header(HttpHeaderName.ALLOW, (String) main);

        http.send();
      }

      case NOT_FOUND -> {
        http.status(HttpStatus.NOT_FOUND);

        http.header(HttpHeaderName.DATE, http.now());

        http.header(HttpHeaderName.CONTENT_LENGTH, 0L);

        http.header(HttpHeaderName.CONNECTION, "close");

        http.send();
      }
    }
  }

  @Override
  public final String toString() {
    return "HttpHandler[kind=" + kind + ",predicate=" + predicate + ",main=" + main + "]";
  }

}