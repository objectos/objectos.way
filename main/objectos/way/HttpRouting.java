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
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import objectos.way.Http.Handler;

sealed abstract class HttpRouting {

  static final class Of extends HttpRouting implements Http.Routing {

    private final Predicate<? super Http.Request> condition;

    public Of() {
      this(null);
    }

    private Of(Predicate<? super Http.Request> condition) {
      this.condition = condition;
    }

    @Override
    public final Http.Handler build() {
      if (single != null) {
        addMany(single);
      }

      return HttpHandler.of(condition, null, many);
    }

    @Override
    public final void handler(Http.Handler value) {
      single(value);
    }

    @Override
    public final void install(Consumer<Http.Routing> routes) {
      routes.accept(this);
    }

    @Override
    public final void path(String path, Consumer<Http.Routing.OfPath> routes) {
      Objects.requireNonNull(path, "path == null");

      final HttpRequestMatcher matcher;
      matcher = HttpRequestMatcher.parsePath(path);

      final Http.Handler handler;
      handler = ofPath(matcher, routes);

      addMany(handler);
    }

    @Override
    public final void when(Predicate<? super Http.Request> condition, Consumer<Http.Routing> routes) {
      Objects.requireNonNull(condition, "condition == null");

      final HttpRouting.Of builder;
      builder = new HttpRouting.Of(condition);

      // implicit null-check
      routes.accept(builder);

      final Http.Handler handler;
      handler = builder.build();

      addMany(handler);
    }

  }

  static final class OfPath extends HttpRouting implements Http.Routing.OfPath {

    private Http.Filter filter;

    private final HttpRequestMatcher matcher;

    private Map<Http.Method, Http.Handler> pathMethods;

    private HttpPathParam[] pathParams;

    private int pathParamsIndex;

    @Lang.VisibleForTesting
    OfPath(HttpRequestMatcher matcher) {
      this.matcher = matcher;
    }

    @Override
    public final Http.Handler build() {
      // define the condition, if necessary
      final Predicate<Http.Request> condition;

      if (pathParamsIndex > 0) {
        HttpPathParam[] params;
        params = Arrays.copyOf(pathParams, pathParamsIndex);

        condition = matcher.with(params);
      } else {
        condition = matcher;
      }

      if (pathMethods != null) {
        final Set<Http.Method> allowedMethods;
        allowedMethods = EnumSet.noneOf(Http.Method.class);

        for (Map.Entry<Http.Method, Http.Handler> entry : pathMethods.entrySet()) {
          final Http.Method method;
          method = entry.getKey();

          allowedMethods.add(method);

          final Http.Handler handler;
          handler = entry.getValue();

          addMany(HttpHandler.methodAllowed(method, handler));
        }

        if (allowedMethods.contains(Http.Method.GET)) {
          allowedMethods.add(Http.Method.HEAD);
        }

        addMany(HttpHandler.methodNotAllowed(allowedMethods));
      }

      if (single != null) {
        addMany(single);
      }

      return HttpHandler.of(condition, filter, many);
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
    public final void filter(Http.Filter value) {
      if (filter != null) {
        throw new IllegalStateException("A filter has already been defined");
      }

      filter = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void handler(Http.Handler value) {
      single(value);
    }

    @Override
    public final void paramDigits(String name) {
      checkMatcherParam(name);

      final HttpPathParam param;
      param = HttpPathParam.digits(name);

      add(param);
    }

    @Override
    public final void paramNotEmpty(String name) {
      checkMatcherParam(name);

      final HttpPathParam param;
      param = HttpPathParam.notEmpty(name);

      add(param);
    }

    @Override
    public final void paramRegex(String name, String value) {
      checkMatcherParam(name);

      final Pattern pattern;
      pattern = Pattern.compile(value);

      final HttpPathParam param;
      param = HttpPathParam.regex(name, pattern);

      add(param);
    }

    private void checkMatcherParam(String name) {
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

    @Override
    public final void subpath(String path, Consumer<Http.Routing.OfPath> routes) {
      if (!matcher.endsInWildcard()) {
        throw new IllegalStateException("A subpath can only be defined in a wildcard parent path");
      }

      Objects.requireNonNull(path, "path == null");

      final HttpRequestMatcher subpathMatcher;
      subpathMatcher = HttpRequestMatcher.parseSubpath(path);

      final OfPath routing;
      routing = new OfPath(subpathMatcher);

      routes.accept(routing);

      final Handler handler;
      handler = routing.build();

      addMany(handler);
    }

  }

  List<Http.Handler> many;

  Http.Handler single;

  HttpRouting() {}

  public abstract Http.Handler build();

  final void addMany(Http.Handler handler) {
    if (many == null) {
      many = new UtilList<>();
    }

    many.add(handler);
  }

  final Http.Handler ofPath(HttpRequestMatcher matcher, Consumer<Http.Routing.OfPath> routes) {
    final HttpRouting.OfPath routing;
    routing = new HttpRouting.OfPath(matcher);

    routes.accept(routing);

    return routing.build();
  }

  final void single(Http.Handler value) {
    if (single != null) {
      throw new IllegalStateException("A handler has already been defined");
    }

    single = Objects.requireNonNull(value, "value == null");
  }

}