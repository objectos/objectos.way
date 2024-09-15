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

  // user types

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

  protected sealed interface RouteOption {}

  // internal types

  private sealed interface ThisRouteOption extends RouteOption {
    void accept(HttpModuleCompiler compiler);
  }

  private record HandlerOption(Http.Handler instance) implements ThisRouteOption {
    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(instance);
    }
  }

  private record HandlerFactory0Option(Supplier<Http.Handler> factory) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = factory.get();

      handler.handle(http);
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record HandlerFactory1Option<T>(Function<T, Http.Handler> function, T value) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = function.apply(value);

      handler.handle(http);
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record MovedPermanentlyOption(String location) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      http.status(Http.MOVED_PERMANENTLY);

      http.dateNow();

      http.header(Http.LOCATION, location);

      http.send();
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record PathParametersOption(Condition[] conditions) implements ThisRouteOption {
    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.pathParams(conditions);
    }
  }

  // fields

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

  // route

  protected final void route(String pathExpression, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");

    compiler.routeStart(pathExpression);

    for (int idx = 0; idx < options.length; idx++) {
      RouteOption o;
      o = Check.notNull(options[idx], "options[", idx, "] == null");

      ThisRouteOption option;
      option = (ThisRouteOption) o;

      option.accept(compiler);
    }

    compiler.routeEnd();
  }

  // route options

  protected final RouteOption handler(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    return new HandlerOption(handler);
  }

  protected final RouteOption handlerFactory(Supplier<Http.Handler> factory) {
    Check.notNull(factory, "factory == null");

    return new HandlerFactory0Option(factory);
  }

  protected final <T> RouteOption handlerFactory(Function<T, Http.Handler> factory, T value) {
    Check.notNull(factory, "factory == null");

    return new HandlerFactory1Option<>(factory, value);
  }

  protected final RouteOption movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return new MovedPermanentlyOption(location);
  }

  protected final RouteOption pathParams(Condition... conditions) {
    Condition[] copy;
    copy = new Condition[conditions.length];

    for (int i = 0; i < conditions.length; i++) {
      copy[i] = Check.notNull(conditions[i], "conditions[", i, "] == null");
    }

    return new PathParametersOption(copy);
  }

  //

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

}