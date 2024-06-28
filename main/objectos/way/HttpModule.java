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
import objectos.lang.object.Check;

abstract class HttpModule {

  protected sealed static abstract class Condition permits HttpModuleCondition {

    final String name;

    Condition(String name) {
      this.name = name;
    }

    final boolean test(HttpRequestTargetPath path) {
      String value;
      value = path.get(name);

      if (value == null) {
        return true;
      } else {
        return test(value);
      }
    }

    abstract boolean test(String value);

  }

  protected sealed static abstract class MethodHandler permits HttpModuleMethodHandler {

    MethodHandler() {}

    abstract Http.Handler compile();

  }

  protected sealed static abstract class RouteOption permits HttpModuleRouteParameters {

    RouteOption() {}

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

  /**
   * Uses the specified session store for HTTP session handling.
   *
   * @param sessionStore
   *        the session store instance to use
   *
   * @throws IllegalStateException
   *         if a session store has already been configured
   */
  protected final void sessionStore(SessionStore sessionStore) {
    Check.notNull(sessionStore, "sessionStore == null");

    compiler.sessionStore(sessionStore);
  }

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
    Check.notNull(options, "options == null");

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, handler, routeOptions);
  }

  protected final void route(String pathExpression, Http.Module module, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");
    Check.notNull(options, "options == null");

    Http.Handler handler;
    handler = module.compile(); // implicit null-check

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, handler, routeOptions);
  }

  protected final void route(String pathExpression, MethodHandler handler, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");
    Check.notNull(options, "options == null");

    Http.Handler httpHandler; // implicit null-check
    httpHandler = handler.compile();

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, httpHandler, routeOptions);
  }

  protected final void route(String pathExpression, Supplier<Http.Handler> supplier, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");
    Check.notNull(supplier, "supplier == null");
    Check.notNull(options, "options == null");

    HttpModuleRouteOptions routeOptions;
    routeOptions = HttpModuleRouteOptions.of(options);

    compiler.route(pathExpression, supplier, routeOptions);
  }

  // handler suppliers

  protected final <T> Supplier<Http.Handler> factory(Function<T, Http.Handler> function, T value) {
    return () -> function.apply(value);
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

  protected final Condition notEmpty(String name) {
    Check.notNull(name, "name == null");

    return new HttpModuleCondition.NotEmpty(name);
  }

  // actions

  protected final MethodHandler GET(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    return HttpModuleMethodHandler.ofHandler(Http.GET, handler);
  }

  protected final MethodHandler method(Http.Request.Method method, Http.Handler handler) {
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    return HttpModuleMethodHandler.ofHandler(method, handler);
  }

  // pre-made actions

  protected final Http.Handler movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return http -> http.movedPermanently(location);
  }

}