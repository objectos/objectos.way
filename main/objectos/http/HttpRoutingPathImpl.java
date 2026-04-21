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

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import objectos.internal.Util;

final class HttpRoutingPathImpl extends HttpRoutingSupport implements HttpRoutingPath {

  private Set<HttpMethod> allowedMethods;

  private final boolean allowSubpath;

  private final Predicate<? super HttpExchange> condition;

  private final HttpFilter filter;

  private final HttpRequestMatcher matcher;

  private HttpPathParam[] pathParams;

  private int pathParamsIndex;

  HttpRoutingPathImpl(HttpRequestMatcher matcher) {
    allowSubpath = matcher.endsInWildcard();

    condition = null;

    filter = null;

    this.matcher = matcher;
  }

  HttpRoutingPathImpl(boolean allowSubpath, HttpFilter filter) {
    this.allowSubpath = allowSubpath;

    condition = null;

    this.filter = filter;

    matcher = null;
  }

  HttpRoutingPathImpl(boolean allowSubpath, Predicate<? super HttpExchange> condition) {
    this.allowSubpath = allowSubpath;

    this.condition = condition;

    filter = null;

    matcher = null;
  }

  @Override
  public final HttpHandler build() {
    // define the condition, if necessary
    final Predicate<? super HttpExchange> cond;

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
      if (allowedMethods.contains(HttpMethod.GET)) {
        allowedMethods.add(HttpMethod.HEAD);
      }

      final HttpHandler notAllowed;
      notAllowed = HttpHandler0.methodNotAllowed(allowedMethods);

      addMany(notAllowed);
    }

    return HttpHandler0.of(cond, filter, many);
  }

  @Override
  public final void allow(HttpMethod method, HttpHandler handler) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(handler, "handler == null");

    if (allowedMethods == null) {
      allowedMethods = EnumSet.noneOf(HttpMethod.class);
    }

    if (allowedMethods.add(method)) {
      final HttpHandler allowed;
      allowed = HttpHandler0.methodAllowed(method, handler);

      addMany(allowed);
    } else {
      throw new IllegalArgumentException("A handler has already been defined for method " + method);
    }
  }

  @Override
  public final void allow(HttpMethod method, HttpHandler first, HttpHandler... rest) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(first, "first == null");
    Objects.requireNonNull(rest, "rest == null");

    if (allowedMethods == null) {
      allowedMethods = EnumSet.noneOf(HttpMethod.class);
    }

    if (allowedMethods.add(method)) {
      final HttpHandler allowed;
      allowed = HttpHandler0.methodAllowed(method, first, rest);

      addMany(allowed);
    } else {
      throw new IllegalArgumentException("A handler has already been defined for method " + method);
    }
  }

  @Override
  public final void filter(HttpFilter value, HttpRoutingPath.Module module) {
    Objects.requireNonNull(value, "value == null");

    final HttpRoutingPathImpl routing;
    routing = new HttpRoutingPathImpl(allowSubpath, value);

    module.configure(routing);

    final HttpHandler handler;
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
  public final void subpath(String subpath, HttpMethod method, HttpHandler handler) {
    subpath(subpath, matched -> {
      matched.allow(method, handler);
    });
  }

  @Override
  public final void subpath(String path, HttpRoutingPath.Module module) {
    if (!allowSubpath) {
      throw new IllegalStateException("A subpath can only be defined in a wildcard parent path");
    }

    Objects.requireNonNull(path, "path == null");

    final HttpRequestMatcher subpathMatcher;
    subpathMatcher = HttpRequestMatcher.parseSubpath(path);

    final HttpRoutingPathImpl routing;
    routing = new HttpRoutingPathImpl(subpathMatcher);

    module.configure(routing);

    final HttpHandler handler;
    handler = routing.build();

    addMany(handler);
  }

  @Override
  public final void when(Predicate<? super HttpExchange> condition, HttpRoutingPath.Module module) {
    Objects.requireNonNull(condition, "condition == null");

    final HttpRoutingPathImpl routing;
    routing = new HttpRoutingPathImpl(allowSubpath, condition);

    module.configure(routing);

    final HttpHandler handler;
    handler = routing.build();

    addMany(handler);
  }

}