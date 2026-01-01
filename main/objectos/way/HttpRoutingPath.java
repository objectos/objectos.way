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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

final class HttpRoutingPath extends HttpRoutingSupport implements Http.RoutingPath {

  private Set<Http.Method> allowedMethods;

  private final boolean allowSubpath;

  private final Predicate<? super Http.Exchange> condition;

  private final Http.Filter filter;

  private final HttpRequestMatcher matcher;

  private HttpPathParam[] pathParams;

  private int pathParamsIndex;

  HttpRoutingPath(HttpRequestMatcher matcher) {
    allowSubpath = matcher.endsInWildcard();

    condition = null;

    filter = null;

    this.matcher = matcher;
  }

  HttpRoutingPath(boolean allowSubpath, Http.Filter filter) {
    this.allowSubpath = allowSubpath;

    condition = null;

    this.filter = filter;

    matcher = null;
  }

  HttpRoutingPath(boolean allowSubpath, Predicate<? super Http.Exchange> condition) {
    this.allowSubpath = allowSubpath;

    this.condition = condition;

    filter = null;

    matcher = null;
  }

  @Override
  public final Http.Handler build() {
    // define the condition, if necessary
    final Predicate<? super Http.Exchange> cond;

    if (condition != null) {
      cond = condition;
    }

    else if (pathParamsIndex > 0) {
      HttpPathParam[] params;
      params = Arrays.copyOf(pathParams, pathParamsIndex);

      cond = matcher.with(params);
    }

    else {
      cond = matcher;
    }

    if (allowedMethods != null) {
      if (allowedMethods.contains(Http.Method.GET)) {
        allowedMethods.add(Http.Method.HEAD);
      }

      final Http.Handler notAllowed;
      notAllowed = HttpHandler.methodNotAllowed(allowedMethods);

      addMany(notAllowed);
    }

    return HttpHandler.of(cond, filter, many);
  }

  @Override
  public final void allow(Http.Method method, Http.Handler handler) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(handler, "handler == null");

    if (allowedMethods == null) {
      allowedMethods = EnumSet.noneOf(Http.Method.class);
    }

    if (allowedMethods.add(method)) {
      final Http.Handler allowed;
      allowed = HttpHandler.methodAllowed(method, handler);

      addMany(allowed);
    } else {
      throw new IllegalArgumentException("A handler has already been defined for method " + method);
    }
  }

  @Override
  public final void allow(Http.Method method, Http.Handler first, Http.Handler... rest) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(first, "first == null");
    Objects.requireNonNull(rest, "rest == null");

    if (allowedMethods == null) {
      allowedMethods = EnumSet.noneOf(Http.Method.class);
    }

    if (allowedMethods.add(method)) {
      final Http.Handler allowed;
      allowed = HttpHandler.methodAllowed(method, first, rest);

      addMany(allowed);
    } else {
      throw new IllegalArgumentException("A handler has already been defined for method " + method);
    }
  }

  @Override
  public final void filter(Http.Filter value, Http.RoutingPath.Module module) {
    Objects.requireNonNull(value, "value == null");

    final HttpRoutingPath routing;
    routing = new HttpRoutingPath(allowSubpath, value);

    module.configure(routing);

    final Http.Handler handler;
    handler = routing.build();

    addMany(handler);
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
    if (matcher != null && !matcher.hasParam(name)) {
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
  public final void subpath(String subpath, Http.Method method, Http.Handler handler) {
    subpath(subpath, matched -> {
      matched.allow(method, handler);
    });
  }

  @Override
  public final void subpath(String path, Http.RoutingPath.Module module) {
    if (!allowSubpath) {
      throw new IllegalStateException("A subpath can only be defined in a wildcard parent path");
    }

    Objects.requireNonNull(path, "path == null");

    final HttpRequestMatcher subpathMatcher;
    subpathMatcher = HttpRequestMatcher.parseSubpath(path);

    final HttpRoutingPath routing;
    routing = new HttpRoutingPath(subpathMatcher);

    module.configure(routing);

    final Http.Handler handler;
    handler = routing.build();

    addMany(handler);
  }

  @Override
  public final void when(Predicate<? super Http.Exchange> condition, Http.RoutingPath.Module module) {
    Objects.requireNonNull(condition, "condition == null");

    final HttpRoutingPath routing;
    routing = new HttpRoutingPath(allowSubpath, condition);

    module.configure(routing);

    final Http.Handler handler;
    handler = routing.build();

    addMany(handler);
  }

}