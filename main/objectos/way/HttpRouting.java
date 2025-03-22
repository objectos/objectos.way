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
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import objectos.way.Http.Request;
import objectos.way.Http.Routing;

final class HttpRouting implements Http.Routing, Http.Routing.OfPath {

  private final Predicate<Http.Request> condition;

  private Http.Handler[] many;

  private int manyIndex;

  private Map<Http.Method, Http.Handler> pathMethods;

  private HttpPathParam[] pathParams;

  private int pathParamsIndex;

  private Http.Handler single;

  HttpRouting() {
    this(null);
  }

  HttpRouting(Predicate<Request> condition) {
    this.condition = condition;
  }

  @Override
  public final void handler(Http.Handler handler) {
    if (single != null) {
      throw new IllegalArgumentException("A handler has already been defined");
    }

    single = Objects.requireNonNull(handler, "handler == null");
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

    addMany(handler);
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

    addMany(handler);
  }

  private void addMany(Http.Handler handler) {
    final int requiredIndex;
    requiredIndex = manyIndex++;

    if (many == null) {
      many = new Http.Handler[10];
    } else {
      many = Util.growIfNecessary(many, requiredIndex);
    }

    many[requiredIndex] = handler;
  }

  @Override
  public final void allow(Http.Method method, Http.Handler handler) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(handler, "handler == null");

    if (pathMethods == null) {
      pathMethods = new EnumMap<>(Http.Method.class);
    }

    final Http.Handler maybeExisting;
    maybeExisting = pathMethods.put(method, handler);

    if (maybeExisting != null) {
      throw new IllegalArgumentException("A handler has already been defined for method " + method);
    }
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

    Http.Handler[] aux;
    aux = null;

    if (predicate instanceof HttpPathMatcher matcher) {

      if (pathParamsIndex > 0) {
        HttpPathParam[] params;
        params = Arrays.copyOf(pathParams, pathParamsIndex);

        predicate = matcher.with(params);
      }

      if (pathMethods != null) {
        int requiredLength;
        requiredLength = pathMethods.size();

        // add method not allowed;
        requiredLength += 1;

        aux = new Http.Handler[requiredLength];

        int auxIndex;
        auxIndex = 0;

        for (Map.Entry<Http.Method, Http.Handler> entry : pathMethods.entrySet()) {
          final Http.Method method;
          method = entry.getKey();

          final Http.Handler handler;
          handler = entry.getValue();

          aux[auxIndex++] = HttpHandler.methodAllowed(matcher, method, handler);
        }

        aux[auxIndex++] = HttpHandler.methodNotAllowed(matcher);
      }

    }

    if (single != null) {
      addMany(single);
    }

    return switch (manyIndex) {
      case 0 -> aux == null ? HttpHandler.NOOP : HttpHandler.many(aux);

      case 1 -> {
        final Http.Handler single;
        single = many[0];

        final Http.Handler handler;
        handler = HttpHandler.single(predicate, single);

        yield merge(aux, handler);
      }

      default -> {
        final Http.Handler[] copy;
        copy = Arrays.copyOf(many, manyIndex);

        final Http.Handler handler;
        handler = HttpHandler.many(predicate, copy);

        yield merge(aux, handler);
      }
    };
  }

  private Http.Handler merge(Http.Handler[] aux, Http.Handler handler) {
    if (aux == null) {
      return handler;
    }

    final Http.Handler[] copy;
    copy = Arrays.copyOf(aux, aux.length + 1);

    copy[copy.length - 1] = handler;

    return HttpHandler.many(copy);
  }

}