/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import objectos.lang.object.Check;

abstract class HttpModule {

  protected sealed static abstract class Condition permits HttpModuleCondition {

    final String name;

    Condition(String name) {
      this.name = name;
    }

    final boolean test(HttpRequestLine path) {
      String value;
      value = path.pathParam(name);

      if (value == null) {
        return true;
      } else {
        return test(value);
      }
    }

    abstract boolean test(String value);

  }

  protected sealed static abstract class RouteOption permits HttpModuleRouteParameters {

    RouteOption() {}

  }

  private record Factory1Handler<T>(Function<T, Http.Handler> function, T value) implements Http.Handler {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = function.apply(value);

      handler.handle(http);
    }
  }

  private record MethodHandler(Http.Request.Method method, Http.Handler handler) implements Http.Handler {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Request.Method actual;
      actual = http.method();

      if (method.is(actual)) {
        handler.handle(http);
      }

      else if (method.is(Http.GET) && actual.is(Http.GET, Http.HEAD)) {
        handler.handle(http);
      }

      else {
        http.methodNotAllowed();
      }
    }
  }

  private record SupplierHandler(Supplier<Http.Handler> supplier) implements Http.Handler {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = supplier.get();

      handler.handle(http);
    }
  }

  private HttpModuleCompiler compiler;

  protected HttpModule() {}

  /**
   * Generates a handler instance based on the configuration of this module.
   *
   * @return a configured handler instance
   */
  public final Http.Handler compile() {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = new HttpModuleCompiler();

      configure();

      return compiler.compile();
    } finally {
      compiler = null;
    }
  }

  protected abstract void configure();

  protected final void install(Http.Module module) {
    Check.notNull(module, "module == null");

    module.acceptHttpModuleCompiler(compiler);
  }

  final void acceptHttpModuleCompiler(HttpModuleCompiler _compiler) {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = _compiler;

      configure();
    } finally {
      compiler = null;
    }
  }

  protected final void filter(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    compiler.filter(handler);
  }

  /**
   * If the {@code Host} request header value is equal to the specified name
   * then dispatches to the specified module.
   *
   * @param name
   *        the host name
   * @param module
   *        the module to dispatch to
   */
  protected final void host(String name, Http.Module module) {
    Check.notNull(name, "name == null");

    Http.Handler handler;
    handler = module.compile();

    compiler.host(name, handler);
  }

  /**
   * If the {@code Host} request header value is equal to the specified name
   * then dispatches to the specified handler.
   *
   * <p>
   * Please note that if the specified handler is not exhaustive then this
   * module might dispatch the request to any existing subsequent handler.
   *
   * @param name
   *        the host name
   * @param handler
   *        the handler to dispatch to
   */
  protected final void host(String name, Http.Handler handler) {
    Check.notNull(name, "name == null");

    compiler.host(name, handler);
  }

  /**
   * Intercepts all matched routes with the specified interceptor.
   *
   * @param interceptor
   *        the interceptor to use
   */
  protected final void interceptMatched(Http.Handler.Interceptor interceptor) {
    Check.notNull(interceptor, "interceptor == null");

    compiler.interceptor(interceptor);
  }

  // routes

  protected final void route(String pathExpression, Http.Handler handler, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");
    Check.notNull(handler, "handler == null");

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, handler, routeOptions);
  }

  protected final void route(String pathExpression, Http.Request.Method method, Http.Handler handler, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    MethodHandler methodHandler;
    methodHandler = new MethodHandler(method, handler);

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, methodHandler, routeOptions);
  }

  // handler suppliers

  protected final Http.Handler f(Supplier<Http.Handler> supplier) {
    Check.notNull(supplier, "supplier == null");

    return new SupplierHandler(supplier);
  }

  protected final <T> Http.Handler f(Function<T, Http.Handler> function, T value) {
    Check.notNull(function, "function == null");

    return new Factory1Handler<T>(function, value);
  }

  // route options

  protected final RouteOption params(Condition... conditions) {
    Condition[] copy;
    copy = new Condition[conditions.length];

    for (int i = 0; i < conditions.length; i++) {
      copy[i] = Check.notNull(conditions[i], "conditions[", i, "] == null");
    }

    return new HttpModuleRouteParameters(copy);
  }

  protected final Condition digits(String name) {
    Check.notNull(name, "name == null");

    return new HttpModuleCondition.Digits(name);
  }

  protected final Condition notEmpty(String name) {
    Check.notNull(name, "name == null");

    return new HttpModuleCondition.NotEmpty(name);
  }

  protected final Condition regex(String name, String regex) {
    Check.notNull(name, "name == null");
    Check.notNull(regex, "regex == null");

    Pattern pattern;
    pattern = Pattern.compile(regex);

    return new HttpModuleCondition.Regex(name, pattern);
  }

  // pre-made actions

  protected final Http.Handler movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return http -> http.movedPermanently(location);
  }

}