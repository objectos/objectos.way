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

abstract class HttpModule {

  // Matcher
  
  protected sealed static abstract class Matcher {

    Matcher() {}

    abstract boolean test(Http.Exchange http);

  }

  // Segments < Matcher
  
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
  
  // Path < Matcher

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

  // Condition
  
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

  private static final class ZeroOrMore extends Condition {

    static final ZeroOrMore INSTANCE = new ZeroOrMore();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return segments.size() >= index;
    }

    @Override
    final boolean mustBeLast() { return true; }

  }

  private static final class Present extends Condition {

    static final Present INSTANCE = new Present();

    @Override
    final boolean test(List<Http.Request.Target.Path.Segment> segments, int index) {
      return hasIndex(segments, index);
    }

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

  protected final Matcher path(String value) {
    Check.notNull(value, "value == null");

    return new PathIs(value);
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

  protected final Condition eq(String value) {
    Check.notNull(value, "value == null");

    return new EqualTo(value);
  }

  protected final Condition nonEmpty() {
    return NonEmpty.INSTANCE;
  }

  protected final Condition zeroOrMore() {
    return ZeroOrMore.INSTANCE;
  }

  protected final Condition present() {
    return Present.INSTANCE;
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