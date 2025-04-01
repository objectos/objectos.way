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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

sealed abstract class HttpRouting {

  static final class Of extends HttpRouting implements Http.Routing {

    private final Predicate<Http.Request> condition;

    public Of() {
      this(null);
    }

    private Of(Predicate<Http.Request> condition) {
      this.condition = condition;
    }

    @Override
    public final Http.Handler build() {
      return buildFromMany(condition);
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
      final HttpPathMatcher matcher;
      matcher = HttpPathMatcher.parse(null, path);

      final Http.Handler handler;
      handler = ofPath(matcher, routes);

      addMany(handler);
    }

    @Override
    public final void prefix(String prefix, Consumer<Http.Routing.OfPrefix> routes) {
      final int len;
      len = prefix.length();

      if (len == 0) {
        throw new IllegalArgumentException("Path prefix must start with a '/' character");
      }

      final char first;
      first = prefix.charAt(0);

      if (first != '/') {
        throw new IllegalArgumentException("Path prefix must start with a '/' character");
      }

      final HttpRouting.OfPrefix routing;
      routing = new HttpRouting.OfPrefix(prefix);

      routes.accept(routing);

      final Http.Handler handler;
      handler = routing.build();

      addMany(handler);
    }

    @Override
    public final void when(Predicate<Http.Request> condition, Consumer<Http.Routing> routes) {
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

    private final HttpPathMatcher matcher;

    private Map<Http.Method, Http.Handler> pathMethods;

    private HttpPathParam[] pathParams;

    private int pathParamsIndex;

    private OfPath(HttpPathMatcher matcher) {
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

          addMany(HttpHandler.methodAllowed(matcher, method, handler));
        }

        if (allowedMethods.contains(Http.Method.GET)) {
          allowedMethods.add(Http.Method.HEAD);
        }

        addMany(HttpHandler.methodNotAllowed(matcher, allowedMethods));
      }

      return buildFromMany(condition);
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
    public final void handler(Http.Handler value) {
      single(value);
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

  }

  static final class OfPrefix extends HttpRouting implements Http.Routing.OfPrefix {

    private final String prefix;

    private Http.Handler beforeMatched;

    private OfPrefix(String prefix) {
      this.prefix = prefix;
    }

    @Override
    public final Http.Handler build() {
      final HttpPathMatcher matcher;
      matcher = HttpPathMatcher.startsWith(prefix);

      return buildFromMany(matcher);
    }

    @Override
    public final void beforeMatched(Http.Handler value) {
      if (beforeMatched != null) {
        throw new IllegalArgumentException("A handler has already been defined");
      }

      beforeMatched = Objects.requireNonNull(value, "value == null");
    }

    @Override
    public final void handler(Http.Handler value) {
      single(value);
    }

    @Override
    public final void path(String path, Consumer<Http.Routing.OfPath> routes) {
      final HttpPathMatcher matcher;
      matcher = HttpPathMatcher.parse(prefix, path);

      final Http.Handler handler;
      handler = ofPath(matcher, routes);

      final Http.Handler result;

      if (beforeMatched != null) {
        final HttpHandler impl;
        impl = (HttpHandler) handler;

        result = impl.filterMatched(beforeMatched);
      } else {
        result = handler;
      }

      addMany(result);
    }

  }

  Http.Handler[] many;

  int manyIndex;

  Http.Handler single;

  HttpRouting() {}

  public abstract Http.Handler build();

  final Http.Handler buildFromMany(Predicate<Http.Request> condition) {
    if (single != null) {
      addMany(single);
    }

    return switch (manyIndex) {
      case 0 -> HttpHandler.NOOP;

      case 1 -> {
        final Http.Handler single;
        single = many[0];

        if (condition == null) {
          yield single;
        }

        yield HttpHandler.single(condition, single);
      }

      default -> {
        final Http.Handler[] copy;
        copy = Arrays.copyOf(many, manyIndex);

        yield HttpHandler.many(condition, copy);
      }
    };
  }

  final void addMany(Http.Handler handler) {
    final int requiredIndex;
    requiredIndex = manyIndex++;

    if (many == null) {
      many = new Http.Handler[10];
    } else {
      many = Util.growIfNecessary(many, requiredIndex);
    }

    many[requiredIndex] = handler;
  }

  final Http.Handler ofPath(HttpPathMatcher matcher, Consumer<Http.Routing.OfPath> routes) {
    final HttpRouting.OfPath routing;
    routing = new HttpRouting.OfPath(matcher);

    routes.accept(routing);

    return routing.build();
  }

  final void single(Http.Handler value) {
    if (single != null) {
      throw new IllegalArgumentException("A handler has already been defined");
    }

    single = Objects.requireNonNull(value, "value == null");
  }

}