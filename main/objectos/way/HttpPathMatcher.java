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

final class HttpPathMatcher implements Predicate<Http.Request> {

  private enum Kind {
    EXACT,

    STARTS_WITH,

    PARAMS,

    WITH_CONDITIONS;
  }

  private record Param(String name) {
    public boolean test(HttpSupport target) {
      return target.namedVariable(name);
    }
  }

  private record WithConditions(List<Object> params, HttpPathParam[] conditions) {}

  private static final String PARAM_EMPTY = "Path parameter name must not be empty";

  private static final String PARAM_SEPARATOR = "Cannot begin a path parameter immediately after the end of another parameter";

  private static final String PATH_MUST_START_WITH_SOLIDUS = "Path does not start with a '/' character";

  private static final String WILDCARD_CHAR = "The '*' wildcard character can only be used once at the end of the path expression";

  private static final String WILDCARD_PARAM = "The '*' wildcard character cannot be used when path parameters are declared";

  private final Kind kind;

  private final Object state;

  private HttpPathMatcher(Kind kind, Object state) {
    this.kind = kind;
    this.state = state;
  }

  public static HttpPathMatcher parse(String pathExpression) {
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

      case EXACT -> exact(pathExpression);

      case STARTS_WITH -> {
        final int stripLast;
        stripLast = length - 1;

        final String value;
        value = pathExpression.substring(0, stripLast);

        yield startsWith(value);
      }

      case PARAM_START -> throw illegal(PARAM_EMPTY, pathExpression);

      case PARAM_PART -> {
        final String value;
        value = pathExpression.substring(aux, length);

        final Param param;
        param = new Param(value);

        params.add(param);

        yield params(params);
      }

      case REGION -> {
        final String value;
        value = pathExpression.substring(aux, length);

        params.add(value);

        yield params(params);
      }
    };
  }

  @Lang.VisibleForTesting
  static HttpPathMatcher exact(final String value) {
    return new HttpPathMatcher(Kind.EXACT, value);
  }

  @Lang.VisibleForTesting
  static HttpPathMatcher startsWith(final String value) {
    return new HttpPathMatcher(Kind.STARTS_WITH, value);
  }

  @Lang.VisibleForTesting
  static Object param(String name) {
    return new Param(name);
  }

  @Lang.VisibleForTesting
  static HttpPathMatcher params(List<Object> params) {
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

    return new HttpPathMatcher(Kind.PARAMS, params);
  }

  private static IllegalArgumentException illegal(String prefix, String path) {
    return new IllegalArgumentException(prefix + ": " + path);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof HttpPathMatcher that
        && kind == that.kind
        && state.equals(that.state);
  }

  @Override
  public final boolean test(Http.Request request) {
    final HttpSupport target;
    target = (HttpSupport) request;

    target.matcherReset();

    return test(target);
  }

  final HttpPathMatcher with(HttpPathParam[] conditions) {
    if (kind != Kind.PARAMS) {
      throw new IllegalStateException("Cannot add path parameter conditions to matcher with kind=" + kind);
    }

    // caller, in theory, passed a safe array
    // no safe copy needed

    List<Object> params;
    params = asParams();

    WithConditions state;
    state = new WithConditions(params, conditions);

    return new HttpPathMatcher(Kind.WITH_CONDITIONS, state);
  }

  final boolean hasParam(String name) {
    if (kind == Kind.PARAMS) {
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
      case EXACT -> target.exact(asString());

      case STARTS_WITH -> target.startsWithMatcher(asString());

      case PARAMS -> {
        List<Object> params;
        params = asParams();

        yield testParams(target, params);
      }

      case WITH_CONDITIONS -> {
        WithConditions data;
        data = (WithConditions) state;

        List<Object> params;
        params = data.params();

        HttpPathParam[] conditions;
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
        result = p.test(target);
      } else {
        final String s;
        s = (String) o;

        result = target.region(s);
      }

      if (!result) {
        return false;
      }
    }

    return target.atEnd();
  }

  private boolean testConditions(HttpSupport target, HttpPathParam[] conditions) {
    for (HttpPathParam condition : conditions) {
      if (!condition.test(target)) {
        return false;
      }
    }

    return true;
  }

  private String asString() {
    return (String) state;
  }

  @SuppressWarnings("unchecked")
  private List<Object> asParams() {
    return (List<Object>) state;
  }

}