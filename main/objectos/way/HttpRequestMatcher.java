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

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

final class HttpRequestMatcher implements Predicate<Http.Request> {

  private enum Kind {
    METHOD_ALLOWED,

    PATH_EXACT,

    PATH_PARAMS,

    PATH_STARTS_WITH,

    PATH_WITH_CONDITIONS;
  }

  private record Param(String name) {}

  private record WithConditions(List<Object> params, HttpPathParam[] conditions) {}

  private static final String PARAM_EMPTY = "Path parameter name must not be empty";

  private static final String PARAM_SEPARATOR = "Cannot begin a path parameter immediately after the end of another parameter";

  private static final String PATH_MUST_START_WITH_SOLIDUS = "Path must start with a '/' character";

  private static final String WILDCARD_CHAR = "The '*' wildcard character can only be used once at the end of the path expression";

  private static final String WILDCARD_PARAM = "The '*' wildcard character cannot be used when path parameters are declared";

  private final Kind kind;

  private final Object state;

  private HttpRequestMatcher(Kind kind, Object state) {
    this.kind = kind;
    this.state = state;
  }

  public static HttpRequestMatcher parsePath(String pathExpression) {
    enum Parser {
      START,

      EXACT,

      STARTS_WITH,

      PARAM_START,

      PARAM_PART,

      REGION;
    }

    Parser parser;
    parser = Parser.START;

    int aux = 0;

    final int length;
    length = pathExpression.length(); // implicit null check

    List<Object> params;
    params = null;

    for (int idx = 0; idx < length; idx++) {
      final char c;
      c = pathExpression.charAt(idx);

      switch (parser) {
        case START -> {
          if (c == '/') {
            parser = Parser.EXACT;
          }

          else {
            throw illegal(PATH_MUST_START_WITH_SOLIDUS, pathExpression);
          }
        }

        case EXACT -> {
          if (c == ':') {
            parser = Parser.PARAM_START;

            final int beginIndex;

            if (params == null) {
              params = Util.createList();

              beginIndex = 0;
            } else {
              beginIndex = aux;
            }

            final String value;
            value = pathExpression.substring(beginIndex, idx);

            params.add(value);
          }

          else if (c == '*') {
            parser = Parser.STARTS_WITH;
          }

          else {
            parser = Parser.EXACT;
          }
        }

        case STARTS_WITH -> throw illegal(WILDCARD_CHAR, pathExpression);

        case PARAM_START -> {
          if (Character.isJavaIdentifierStart(c)) {
            parser = Parser.PARAM_PART;

            aux = idx;
          }

          else {
            throw illegal(PARAM_EMPTY, pathExpression);
          }
        }

        case PARAM_PART -> {
          if (Character.isJavaIdentifierPart(c)) {
            parser = Parser.PARAM_PART;
          }

          else if (c == ':') {
            throw illegal(PARAM_SEPARATOR, pathExpression);
          }

          else {
            parser = Parser.REGION;

            final String name;
            name = pathExpression.substring(aux, idx);

            final Param param;
            param = new Param(name);

            params.add(param);

            aux = idx;
          }
        }

        case REGION -> {
          if (c == ':') {
            parser = Parser.PARAM_START;

            final Object value;
            value = pathExpression.substring(aux, idx);

            params.add(value);
          }

          else if (c == '*') {
            throw illegal(WILDCARD_PARAM, pathExpression);
          }

          else {
            parser = Parser.REGION;
          }
        }
      }
    }

    return switch (parser) {
      case START -> throw illegal(PATH_MUST_START_WITH_SOLIDUS, pathExpression);

      case EXACT -> pathExact(pathExpression);

      case STARTS_WITH -> {
        final int stripLast;
        stripLast = length - 1;

        final String value;
        value = pathExpression.substring(0, stripLast);

        yield pathStartsWith(value);
      }

      case PARAM_START -> throw illegal(PARAM_EMPTY, pathExpression);

      case PARAM_PART -> {
        final String value;
        value = pathExpression.substring(aux, length);

        final Param param;
        param = new Param(value);

        params.add(param);

        yield pathParams(params);
      }

      case REGION -> {
        final String value;
        value = pathExpression.substring(aux, length);

        params.add(value);

        yield pathParams(params);
      }
    };
  }

  public static HttpRequestMatcher parseSubpath(String path) {
    throw new UnsupportedOperationException("Implement me");
  }

  static HttpRequestMatcher methodAllowed(Http.Method value) {
    return new HttpRequestMatcher(Kind.METHOD_ALLOWED, value);
  }

  @Lang.VisibleForTesting
  static HttpRequestMatcher pathExact(final String value) {
    return new HttpRequestMatcher(Kind.PATH_EXACT, value);
  }

  @Lang.VisibleForTesting
  static HttpRequestMatcher pathStartsWith(final String value) {
    return new HttpRequestMatcher(Kind.PATH_STARTS_WITH, value);
  }

  @Lang.VisibleForTesting
  static HttpRequestMatcher pathParams(List<Object> params) {
    final Set<String> distinct;
    distinct = Util.createSet();

    for (Object o : params) {
      if (!(o instanceof Param p)) {
        continue;
      }

      final String name;
      name = p.name;

      if (distinct.add(name)) {
        continue;
      }

      throw new IllegalArgumentException(
          "The ':%s' path variable was declared more than once".formatted(name)
      );
    }

    return new HttpRequestMatcher(Kind.PATH_PARAMS, params);
  }

  @Lang.VisibleForTesting
  static Object param(String name) {
    return new Param(name);
  }

  private static IllegalArgumentException illegal(String prefix, String path) {
    return new IllegalArgumentException(prefix + ": " + path);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof HttpRequestMatcher that
        && kind == that.kind
        && state.equals(that.state);
  }

  @Override
  public final boolean test(Http.Request request) {
    final HttpSupport target;
    target = (HttpSupport) request;

    return test(target);
  }

  @Override
  public final String toString() {
    return kind + "=" + state;
  }

  final HttpRequestMatcher with(HttpPathParam[] conditions) {
    if (kind != Kind.PATH_PARAMS) {
      throw new IllegalStateException("Cannot add path parameter conditions to matcher with kind=" + kind);
    }

    // caller, in theory, passed a safe array
    // no safe copy needed

    List<Object> params;
    params = asParams();

    WithConditions state;
    state = new WithConditions(params, conditions);

    return new HttpRequestMatcher(Kind.PATH_WITH_CONDITIONS, state);
  }

  final boolean hasParam(String name) {
    if (kind == Kind.PATH_PARAMS) {
      final List<Object> params;
      params = asParams();

      for (Object o : params) {
        if (o instanceof Param p && p.name().equals(name)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean test(HttpSupport target) {
    return switch (kind) {
      case METHOD_ALLOWED -> {
        final Http.Method method;
        method = target.method();

        yield method == state || (method == Http.Method.HEAD && state == Http.Method.GET);
      }

      case PATH_EXACT -> {
        target.pathReset();

        final String exact;
        exact = (String) state;

        yield target.testPathExact(exact);
      }

      case PATH_PARAMS -> {
        target.pathReset();

        final List<Object> params;
        params = asParams();

        yield testParams(target, params);
      }

      case PATH_STARTS_WITH -> {
        target.pathReset();

        final String prefix;
        prefix = (String) state;

        yield target.testPathRegion(prefix);
      }

      case PATH_WITH_CONDITIONS -> {
        target.pathReset();

        final WithConditions data;
        data = (WithConditions) state;

        final List<Object> params;
        params = data.params();

        final HttpPathParam[] conditions;
        conditions = data.conditions();

        yield testParams(target, params)
            && testConditions(target, conditions);
      }
    };
  }

  private boolean testParams(HttpSupport target, List<Object> params) {
    for (Object o : params) {
      boolean result;

      if (o instanceof Param p) {
        result = target.testPathParam(p.name);
      } else {
        final String region;
        region = (String) o;

        result = target.testPathRegion(region);
      }

      if (!result) {
        return false;
      }
    }

    return target.testPathEnd();
  }

  private boolean testConditions(HttpSupport target, HttpPathParam[] conditions) {
    for (HttpPathParam condition : conditions) {
      if (!condition.test(target)) {
        return false;
      }
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  private List<Object> asParams() {
    return (List<Object>) state;
  }

}