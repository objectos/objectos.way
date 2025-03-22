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

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import objectos.way.Http.Request;
import objectos.way.Http.Routing;

final class HttpRouting implements Http.Routing, Http.Routing.OfPath {

  private final Predicate<Http.Request> condition;

  private Http.Handler[] handlers;

  private int handlersIndex;

  private HttpPathParam[] pathParams;

  private int pathParamsIndex;

  HttpRouting() {
    this(null);
  }

  HttpRouting(Predicate<Request> condition) {
    this.condition = condition;
  }

  @Override
  public final void handler(Http.Handler handler) {
    Objects.requireNonNull(handler, "handler == null");

    add(handler);
  }

  @Override
  public final void install(Consumer<Http.Routing> routes) {
    routes.accept(this);
  }

  @Override
  public final void path(String path, Consumer<Http.Routing.OfPath> config) {
    final HttpPathMatcher matcher;
    matcher = HttpPathMatcher.parse(path);

    final HttpRouting routing;
    routing = new HttpRouting(matcher);

    config.accept(routing);

    final Http.Handler handler;
    handler = routing.build();

    add(handler);
  }

  @Override
  public final void when(Predicate<Http.Request> condition, Consumer<Routing> routes) {
    Objects.requireNonNull(condition, "condition == null");

    final HttpRouting builder;
    builder = new HttpRouting(condition);

    // implicit null-check
    routes.accept(builder);

    final Http.Handler handler;
    handler = builder.build();

    add(handler);
  }

  private void add(Http.Handler handler) {
    final int requiredIndex;
    requiredIndex = handlersIndex++;

    if (handlers == null) {
      handlers = new Http.Handler[10];
    } else {
      handlers = Util.growIfNecessary(handlers, requiredIndex);
    }

    handlers[requiredIndex] = handler;
  }

  @Override
  public final void allow(Http.Method method, Http.Handler handler) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public void param(String name, Predicate<String> condition) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void paramDigits(String name) {
    checkMatcherParam(name);

    final HttpPathParam.Digits param;
    param = new HttpPathParam.Digits(name);

    add(param);
  }

  @Override
  public final void paramNotEmpty(String name) {
    checkMatcherParam(name);

    final HttpPathParam.NotEmpty param;
    param = new HttpPathParam.NotEmpty(name);

    add(param);
  }

  @Override
  public final void paramRegex(String name, String value) {
    checkMatcherParam(name);

    final Pattern pattern;
    pattern = Pattern.compile(value);

    final HttpPathParam.Regex param;
    param = new HttpPathParam.Regex(name, pattern);

    add(param);
  }

  private void checkMatcherParam(String name) {
    if (!(condition instanceof HttpPathMatcher matcher)) {
      throw new IllegalStateException("Cannot set a path parameter condition as current route does not define path parameters");
    }

    if (!matcher.hasParam(name)) {
      throw new IllegalArgumentException("Current route does not define a path parameter named " + name);
    }
  }

  private void add(HttpPathParam param) {
    final int requiredIndex;
    requiredIndex = pathParamsIndex++;

    if (pathParams == null) {
      pathParams = new HttpPathParam[2];
    } else {
      pathParams = Util.growIfNecessary(pathParams, requiredIndex);
    }

    pathParams[requiredIndex] = param;
  }

  final Http.Handler build() {

    Predicate<Http.Request> predicate;
    predicate = condition;

    if (predicate instanceof HttpPathMatcher matcher) {

      if (pathParamsIndex > 0) {
        HttpPathParam[] params;
        params = Arrays.copyOf(pathParams, pathParamsIndex);

        predicate = matcher.with(params);
      }

    }

    return switch (handlersIndex) {
      case 0 -> HttpHandler.NOOP;

      case 1 -> {
        final Http.Handler single;
        single = handlers[0];

        yield HttpHandler.single(predicate, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = Arrays.copyOf(handlers, handlersIndex);

        yield HttpHandler.many(predicate, copy);
      }
    };
  }

}