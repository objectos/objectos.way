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

import java.util.List;
import java.util.function.Function;
import objectos.lang.object.Check;
import objectos.way.Http.Handler;

abstract class HttpModule {

  protected sealed static abstract class Matcher permits HttpModulePathMatcher, HttpModuleSegments {

    Matcher() {}

    abstract boolean test(Http.Exchange http);

  }

  protected sealed static abstract class Condition permits HttpModuleCondition {

    Condition() {}

    abstract boolean test(List<Http.Request.Target.Path.Segment> segments, int index);

    final boolean hasIndex(List<Http.Request.Target.Path.Segment> segments, int index) {
      return index < segments.size();
    }

    boolean mustBeLast() {
      return false;
    }

  }

  protected sealed static abstract class MethodHandler permits HttpModuleMethodHandler {

    MethodHandler() {}

    abstract Handler compile();

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

  protected final void route(Matcher matcher, Http.Handler handler) {
    Check.notNull(matcher, "matcher == null");
    Check.notNull(handler, "handler == null");

    compiler.route(matcher, handler);
  }

  protected final void route(Matcher matcher, MethodHandler handler) {
    Check.notNull(matcher, "matcher == null");
    Check.notNull(handler, "handler == null");

    compiler.route(matcher, handler.compile());
  }

  protected final void route(Matcher matcher, Http.Module module) {
    Check.notNull(matcher, "matcher == null");

    Http.Handler handler;
    handler = module.compile(); // implicit null-check

    compiler.route(matcher, handler);
  }

  protected final <T> void route(Matcher matcher, Function<T, Http.Handler> factory, T value) {
    Check.notNull(matcher, "matcher == null");
    Check.notNull(factory, "factory == null");
    Check.notNull(value, "value == null");

    compiler.route(matcher, factory, value);
  }

  // matchers

  protected final Matcher path(String value) {
    Check.notNull(value, "value == null");

    return new HttpModulePathMatcher(value);
  }

  protected final Matcher segments(Condition condition) {
    Check.notNull(condition, "condition == null");

    return HttpModuleSegments.of(condition);
  }

  protected final Matcher segments(Condition c0, Condition c1) {
    checkMustBeLast(c0);
    Check.notNull(c1, "c1 == null");

    return HttpModuleSegments.of(c0, c1);
  }

  protected final Matcher segments(Condition c0, Condition c1, Condition c2) {
    checkMustBeLast(c0);
    checkMustBeLast(c1);
    Check.notNull(c2, "c2 == null");

    return HttpModuleSegments.of(c0, c1, c2);
  }

  private void checkMustBeLast(Condition condition) {
    if (condition.mustBeLast()) {
      Class<? extends Condition> type;
      type = condition.getClass();

      String name;
      name = type.getSimpleName();

      throw new IllegalArgumentException(name + " must only me used as the last condition");
    }
  }

  // conditions

  protected final Condition eq(String value) {
    Check.notNull(value, "value == null");

    return HttpModuleCondition.equalTo(value);
  }

  protected final Condition nonEmpty() {
    return HttpModuleCondition.nonEmpty();
  }

  protected final Condition zeroOrMore() {
    return HttpModuleCondition.zeroOrMore();
  }

  protected final Condition present() {
    return HttpModuleCondition.present();
  }

  protected final Condition oneOrMore() {
    return HttpModuleCondition.oneOrMore();
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