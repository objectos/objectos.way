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

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import objectos.lang.object.Check;
import objectos.util.array.ObjectArrays;

abstract class HttpModule {

  protected sealed static abstract class Matcher {

    Matcher() {}

    abstract boolean test(Http.Exchange http);

  }

  private sealed static abstract class AbstractSegments extends Matcher {

    final Condition last;

    AbstractSegments(Condition last) {
      this.last = last;
    }

    @Override
    final boolean test(Http.Exchange http) {
      Http.Request.Target.Path path;
      path = http.path();

      List<Http.Request.Target.Path.Segment> segments;
      segments = path.segments();

      if (!last.mustBeLast() && segments.size() != count()) {
        return false;
      }

      return test(segments);
    }

    abstract int count();

    abstract boolean test(List<Http.Request.Target.Path.Segment> segments);

  }

  protected sealed static abstract class Condition {

    Condition() {}

    abstract boolean test(List<Http.Request.Target.Path.Segment> segments, int index);

    final boolean hasIndex(List<Http.Request.Target.Path.Segment> segments, int index) {
      return index < segments.size();
    }

    boolean mustBeLast() {
      return false;
    }

  }
  
  private static final class Compiler implements Http.Handler {

    private Action[] actions;

    private int actionsIndex;

    private Http.Handler.Interceptor interceptor;

    private SessionStore sessionStore;

    public final Http.Handler compile() {
      if (actions != null) {
        actions = Arrays.copyOf(actions, actionsIndex);
      } else {
        actions = new Action[0];
      }

      return this;
    }

    @Override
    public final void handle(Http.Exchange http) {
      if (sessionStore != null) {
        http.acceptSessionStore(sessionStore);
      }

      for (int index = 0, length = actions.length; index < length; index++) {
        Action action;
        action = actions[index];

        if (action.execute(http)) {
          return;
        }
      }

      http.notFound();
    }

    final void filter(Http.Handler handler) {
      handler = decorate(handler);
      
      int index;
      index = nextSlot();

      actions[index] = new Filter(handler);
    }

    final void interceptor(Http.Handler.Interceptor next) {
      if (interceptor == null) {
        interceptor = next;
      } else {
        interceptor = handler -> interceptor.intercept(next.intercept(handler));
      }
    }

    final void route(Matcher matcher, Http.Handler handler) {
      handler = decorate(handler);
      
      int index;
      index = nextSlot();

      actions[index] = new RouteHandler(matcher, handler);
    }

    final <T> void route(Matcher matcher, Function<T, Http.Handler> factory, T value) {
      Function<T, Http.Handler> function;
      function = interceptor == null ? factory : (T t) -> interceptor.intercept(factory.apply(t));

      int index;
      index = nextSlot();

      actions[index] = new RouteFactory1<T>(matcher, function, value);
    }

    final void sessionStore(SessionStore sessionStore) {
      Check.state(this.sessionStore == null, "A session store has already been configured");

      this.sessionStore = sessionStore;
    }
    
    private Http.Handler decorate(Http.Handler handler) {
      if (interceptor == null) {
        return handler;
      } else {
        return interceptor.intercept(handler);
      }
    }

    private int nextSlot() {
      int requiredIndex;
      requiredIndex = actionsIndex++;

      if (actions == null) {
        actions = new Action[10];
      } else {
        actions = ObjectArrays.growIfNecessary(actions, requiredIndex);
      }

      return requiredIndex;
    }

  }

  private sealed interface Action {
    boolean execute(Http.Exchange http);
  }

  private record Filter(Http.Handler handler) implements Action {
    @Override
    public final boolean execute(Http.Exchange http) {
      Http.Handler handler;
      handler = handler();

      handler.handle(http);

      return http.processed();
    }
  }

  private sealed static abstract class Route implements Action {

    private final Matcher matcher;

    public Route(Matcher matcher) {
      this.matcher = matcher;
    }

    @Override
    public final boolean execute(Http.Exchange http) {
      boolean result;
      result = false;

      if (matcher.test(http)) {
        Http.Handler handler;
        handler = handler();

        handler.handle(http);

        result = http.processed();
      }

      return result;
    }

    abstract Http.Handler handler();

  }

  private static final class RouteHandler extends Route {

    private final Http.Handler handler;

    public RouteHandler(Matcher matcher, Http.Handler handler) {
      super(matcher);
      this.handler = handler;
    }

    @Override
    final Http.Handler handler() { return handler; }

  }

  private static final class RouteFactory1<T> extends Route {

    private final Function<T, Http.Handler> factory;
    private final T value;

    public RouteFactory1(Matcher matcher, Function<T, Http.Handler> factory, T value) {
      super(matcher);
      this.factory = factory;
      this.value = value;
    }

    @Override
    final Http.Handler handler() {
      return factory.apply(value);
    }

  }

  private Compiler compiler;

  protected HttpModule() {}

  /**
   * Generates a handler instance based on the configuration of this module.
   * 
   * @return a configured handler instance
   */
  public final Http.Handler compile() {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = new Compiler();

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

  protected final void filter(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    compiler.filter(handler);
  }
  
  protected final void intercept(Http.Handler.Interceptor interceptor) {
    Check.notNull(interceptor, "interceptor == null");
    
    compiler.interceptor(interceptor);
  }

  protected final void route(Matcher matcher, Http.Handler handler) {
    Check.notNull(matcher, "matcher == null");
    Check.notNull(handler, "handler == null");

    compiler.route(matcher, handler);
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

  private static final class PathIs extends Matcher {

    private final String value;

    PathIs(String value) {
      this.value = value;
    }

    @Override
    final boolean test(Http.Exchange http) {
      Http.Request.Target.Path path;
      path = http.path();

      return path.is(value);
    }

  }

  protected final Matcher path(String value) {
    Check.notNull(value, "value == null");

    return new PathIs(value);
  }

  private static final class Segments1 extends AbstractSegments {

    Segments1(Condition condition) {
      super(condition);
    }

    @Override
    final int count() { return 1; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      return last.test(segments, 0);
    }

  }

  private static final class Segments2 extends AbstractSegments {

    private final Condition condition0;

    Segments2(Condition condition0, Condition condition1) {
      super(condition1);

      this.condition0 = condition0;
    }

    @Override
    final int count() { return 2; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      if (!condition0.test(segments, 0)) {
        return false;
      }

      return last.test(segments, 1);
    }

  }

  private static final class Segments3 extends AbstractSegments {

    private final Condition condition0;
    
    private final Condition condition1;

    Segments3(Condition condition0, Condition condition1, Condition condition2) {
      super(condition2);

      this.condition0 = condition0;
      this.condition1 = condition1;
    }

    @Override
    final int count() { return 3; }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments) {
      if (!condition0.test(segments, 0)) {
        return false;
      }

      if (!condition1.test(segments, 1)) {
        return false;
      }

      return last.test(segments, 2);
    }

  }

  protected final Matcher segments(Condition condition) {
    Check.notNull(condition, "condition == null");

    return new Segments1(condition);
  }

  protected final Matcher segments(Condition c0, Condition c1) {
    checkMustBeLast(c0);
    Check.notNull(c1, "c1 == null");

    return new Segments2(c0, c1);
  }

  protected final Matcher segments(Condition c0, Condition c1, Condition c2) {
    checkMustBeLast(c0);
    checkMustBeLast(c1);
    Check.notNull(c2, "c2 == null");

    return new Segments3(c0, c1, c2);
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

  private static final class EqualTo extends Condition {

    private final String value;

    EqualTo(String value) {
      this.value = value;
    }

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      if (!hasIndex(segments, index)) {
        return false;
      }

      Http.Request.Target.Path.Segment segment;
      segment = segments.get(index);

      return segment.is(value);
    }

  }

  protected final Condition eq(String value) {
    Check.notNull(value, "value == null");

    return new EqualTo(value);
  }

  private static final class NonEmpty extends Condition {

    static final NonEmpty INSTANCE = new NonEmpty();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      if (!hasIndex(segments, index)) {
        return false;
      }

      Http.Request.Target.Path.Segment segment;
      segment = segments.get(index);

      String value;
      value = segment.value();

      return !value.isEmpty();
    }

  }

  protected final Condition nonEmpty() {
    return NonEmpty.INSTANCE;
  }

  private static final class ZeroOrMore extends Condition {

    static final ZeroOrMore INSTANCE = new ZeroOrMore();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return segments.size() >= index;
    }

    @Override
    final boolean mustBeLast() { return true; }

  }

  protected final Condition zeroOrMore() {
    return ZeroOrMore.INSTANCE;
  }

  private static final class Present extends Condition {

    static final Present INSTANCE = new Present();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return hasIndex(segments, index);
    }

  }

  protected final Condition present() {
    return Present.INSTANCE;
  }
  
  private static final class OneOrMore extends Condition {
    
    static final OneOrMore INSTANCE = new OneOrMore();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return segments.size() > index;
    }


    @Override
    final boolean mustBeLast() { return true; }

  }
  
  protected final Condition oneOrMore() {
    return OneOrMore.INSTANCE;
  }

  // actions

  protected final Http.Handler matrix(Http.Request.Method method, Http.Handler handler) {
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    return http -> http.methodMatrix(method, handler);
  }

  // pre-made actions

  protected final Http.Handler movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return http -> http.movedPermanently(location);
  }

}