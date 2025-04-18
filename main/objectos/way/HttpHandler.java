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

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import objectos.way.Http.Handler;
import objectos.way.Http.Request;

final class HttpHandler implements Http.Handler {

  private enum Kind {

    NOOP,

    // subpath

    SUBPATH_SINGLE,

    SUBPATH_MANY,

    // delegate

    SINGLE,

    MANY,

    FACTORY1,

    FILTER,

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

  private record FilterHolder(Http.Filter filter, Http.Handler handler) {}

  static final Http.Handler NOOP = new HttpHandler(Kind.NOOP, null, null);

  private static final Http.Handler NOT_FOUND = new HttpHandler(Kind.NOT_FOUND, null, null);

  private final Kind kind;

  private final Predicate<? super Http.Request> predicate;

  private final Object main;

  private HttpHandler(Kind kind, Predicate<? super Http.Request> predicate, Object main) {
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

  public static Http.Handler filter(Predicate<? super Http.Request> predicate, Http.Filter filter, Http.Handler handler) {
    final FilterHolder holder;
    holder = new FilterHolder(filter, handler);

    return new HttpHandler(Kind.FILTER, predicate, holder);
  }

  public static Http.Handler methodNotAllowed(Set<Http.Method> allowedMethods) {
    final String allow;
    allow = allowedMethods.stream().map(Http.Method::name).collect(Collectors.joining(", "));

    return new HttpHandler(Kind.METHOD_NOT_ALLOWED, null, allow);
  }

  public static Http.Handler movedPermanently(String location) {
    return new HttpHandler(Kind.MOVED_PERMANENTLY, null, location);
  }

  public static Http.Handler methodAllowed(Http.Method method, Http.Handler handler) {
    final HttpRequestMatcher predicate;
    predicate = HttpRequestMatcher.methodAllowed(method);

    return new HttpHandler(Kind.SINGLE, predicate, handler);
  }

  public static Http.Handler notFound() {
    return NOT_FOUND;
  }

  public static Http.Handler of(Predicate<? super Request> condition, List<Handler> handlers) {
    if (handlers == null) {
      return NOOP;
    }

    return switch (handlers.size()) {
      case 0 -> HttpHandler.NOOP;

      case 1 -> {
        final Http.Handler single;
        single = handlers.get(0);

        if (condition == null) {
          yield single;
        }

        yield new HttpHandler(Kind.SINGLE, condition, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = handlers.toArray(Http.Handler[]::new);

        yield new HttpHandler(Kind.MANY, condition, copy);
      }
    };
  }

  public static Http.Handler ofSubpath(Predicate<? super Request> condition, List<Handler> handlers) {
    if (handlers == null) {
      return NOOP;
    }

    return switch (handlers.size()) {
      case 0 -> HttpHandler.NOOP;

      case 1 -> {
        final Http.Handler single;
        single = handlers.get(0);

        if (condition == null) {
          yield single;
        }

        yield new HttpHandler(Kind.SUBPATH_SINGLE, condition, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = handlers.toArray(Http.Handler[]::new);

        yield new HttpHandler(Kind.SUBPATH_MANY, condition, copy);
      }
    };
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

    final HttpExchange impl;
    impl = (HttpExchange) http;

    switch (kind) {
      case SUBPATH_SINGLE, SUBPATH_MANY -> {}

      default -> impl.pathReset();
    }

    if (predicate != null && !predicate.test(impl)) {
      return;
    }

    handle0(impl, kind, main);
  }

  @Override
  public final String toString() {
    return "HttpHandler[kind=" + kind + ",predicate=" + predicate + ",main=" + main + "]";
  }

  private void handle0(HttpExchange http, Kind actualKind, Object data) {
    switch (actualKind) {
      case NOOP -> {}

      case SUBPATH_SINGLE, SINGLE -> {
        final int pathIndex;
        pathIndex = http.pathIndex;

        final Http.Handler single;
        single = (Http.Handler) data;

        single.handle(http);

        if (!http.processed()) {
          http.pathIndex = pathIndex;
        }
      }

      case SUBPATH_MANY, MANY -> {
        final int pathIndex;
        pathIndex = http.pathIndex;

        // TODO path parameters

        final Http.Handler[] many;
        many = (Http.Handler[]) data;

        int index;
        index = 0;

        while (index < many.length) {
          final Http.Handler handler;
          handler = many[index++];

          handler.handle(http);

          if (http.processed()) {
            break;
          }

          http.pathIndex = pathIndex;
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

      case FILTER -> {
        final FilterHolder holder;
        holder = (FilterHolder) data;

        final Http.Filter filter;
        filter = holder.filter;

        final Http.Handler handler;
        handler = holder.handler;

        filter.filter(http, handler);
      }

      case CONTENT -> {
        final Content content;
        content = (Content) data;

        http.status(Http.Status.OK);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.CONTENT_TYPE, content.contentType);

        final byte[] bytes;
        bytes = content.bytes;

        http.header(Http.HeaderName.CONTENT_LENGTH, bytes.length);

        http.send(bytes);
      }

      case METHOD_NOT_ALLOWED -> {
        http.status(Http.Status.METHOD_NOT_ALLOWED);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.CONTENT_LENGTH, 0L);

        http.header(Http.HeaderName.ALLOW, (String) data);

        http.send();
      }

      case MOVED_PERMANENTLY -> {
        http.status(Http.Status.MOVED_PERMANENTLY);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.LOCATION, (String) data);

        http.send();
      }

      case NOT_FOUND -> {
        http.status(Http.Status.NOT_FOUND);

        http.header(Http.HeaderName.DATE, http.now());

        http.header(Http.HeaderName.CONNECTION, "close");

        http.send();
      }
    }
  }

}