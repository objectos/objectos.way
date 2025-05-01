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

    PATH_SEGMENTS,

    PATH_WILDCARD,

    PATH_WITH_CONDITIONS,

    SUBPATH_EXACT,

    SUBPATH_SEGMENTS,

    SUBPATH_WILDCARD,

    SUBPATH_WITH_CONDITIONS;
  }

  private enum SegmentKind {

    EXACT,

    PARAM,

    PARAM_LAST,

    REGION;

  }

  private record Segment(SegmentKind kind, String value, char c) {

    final String paramName() {
      return switch (kind) {
        case PARAM, PARAM_LAST -> value;

        default -> null;
      };
    }

  }

  private record WithConditions(List<Segment> segments, HttpPathParam[] conditions) {}

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

  private enum Parser {
    START_PATH,

    EXACT,

    STARTS_WITH,

    PARAM_START,

    PARAM_PART,

    REGION;
  }

  public static HttpRequestMatcher parsePath(String pathExpression) {
    return parse(pathExpression, false);
  }

  public static HttpRequestMatcher parseSubpath(String pathExpression) {
    return parse(pathExpression, true);
  }

  private static HttpRequestMatcher parse(String pathExpression, boolean subpath) {
    Parser parser;
    parser = subpath ? Parser.EXACT : Parser.START_PATH;

    int aux;
    aux = 0;

    final int length;
    length = pathExpression.length(); // implicit null check

    UtilList<Segment> segments;
    segments = null;

    for (int idx = 0; idx < length; idx++) {
      final char c;
      c = pathExpression.charAt(idx);

      switch (parser) {
        case START_PATH -> {
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

            if (segments == null) {
              segments = new UtilList<>();

              beginIndex = 0;
            } else {
              beginIndex = aux;
            }

            final String value;
            value = pathExpression.substring(beginIndex, idx);

            final Segment segment;
            segment = segmentRegion(value);

            segments.add(segment);
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

            final Segment param;
            param = segmentParam(name, c);

            segments.add(param);

            // skip char terminator
            aux = idx + 1;
          }
        }

        case REGION -> {
          if (c == ':') {
            parser = Parser.PARAM_START;

            final String value;
            value = pathExpression.substring(aux, idx);

            final Segment segment;
            segment = segmentRegion(value);

            segments.add(segment);
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
      case START_PATH -> throw illegal(PATH_MUST_START_WITH_SOLIDUS, pathExpression);

      case EXACT -> subpath ? subpathExact(pathExpression) : pathExact(pathExpression);

      case STARTS_WITH -> {
        final int stripLast;
        stripLast = length - 1;

        final String value;
        value = pathExpression.substring(0, stripLast);

        yield subpath ? subpathWildcard(value) : pathWildcard(value);
      }

      case PARAM_START -> throw illegal(PARAM_EMPTY, pathExpression);

      case PARAM_PART -> {
        final String name;
        name = pathExpression.substring(aux, length);

        final Segment segment;
        segment = segmentParamLast(name);

        segments.add(segment);

        final List<Segment> segs;
        segs = segments.toUnmodifiableList();

        yield subpath ? subpathSegments(segs) : pathSegments(segs);
      }

      case REGION -> {
        final String value;
        value = pathExpression.substring(aux, length);

        final Segment segment;
        segment = segmentExact(value);

        segments.add(segment);

        final List<Segment> segs;
        segs = segments.toUnmodifiableList();

        yield subpath ? subpathSegments(segs) : pathSegments(segs);
      }
    };
  }

  static HttpRequestMatcher methodAllowed(Http.Method value) {
    return new HttpRequestMatcher(Kind.METHOD_ALLOWED, value);
  }

  static HttpRequestMatcher pathExact(final String value) {
    return new HttpRequestMatcher(Kind.PATH_EXACT, value);
  }

  static HttpRequestMatcher pathSegments(List<Segment> segments) {
    final Set<String> distinct;
    distinct = Util.createSet();

    for (Segment segment : segments) {
      final String name;
      name = segment.paramName();

      if (name == null) {
        continue;
      }

      if (distinct.add(name)) {
        continue;
      }

      throw new IllegalArgumentException(
          "The ':%s' path variable was declared more than once".formatted(name)
      );
    }

    return new HttpRequestMatcher(Kind.PATH_SEGMENTS, segments);
  }

  static HttpRequestMatcher pathWildcard(final String value) {
    return new HttpRequestMatcher(Kind.PATH_WILDCARD, value);
  }

  static Segment segmentExact(String value) {
    return new Segment(SegmentKind.EXACT, value, '\0');
  }

  static Segment segmentParam(String name, char c) {
    return new Segment(SegmentKind.PARAM, name, c);
  }

  static Segment segmentParamLast(String name) {
    return new Segment(SegmentKind.PARAM_LAST, name, '\0');
  }

  static Segment segmentRegion(String value) {
    return new Segment(SegmentKind.REGION, value, '\0');
  }

  static HttpRequestMatcher subpathExact(String value) {
    return new HttpRequestMatcher(Kind.SUBPATH_EXACT, value);
  }

  static HttpRequestMatcher subpathSegments(List<Segment> segments) {
    final Set<String> distinct;
    distinct = Util.createSet();

    for (Segment segment : segments) {
      final String name;
      name = segment.paramName();

      if (name == null) {
        continue;
      }

      if (distinct.add(name)) {
        continue;
      }

      throw new IllegalArgumentException(
          "The ':%s' path variable was declared more than once".formatted(name)
      );
    }

    return new HttpRequestMatcher(Kind.SUBPATH_SEGMENTS, segments);
  }

  static HttpRequestMatcher subpathWildcard(String value) {
    return new HttpRequestMatcher(Kind.SUBPATH_WILDCARD, value);
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
    final HttpExchange target;
    target = (HttpExchange) request;

    return switch (kind) {
      case METHOD_ALLOWED -> {
        final Http.Method method;
        method = target.method();

        yield method == state || (method == Http.Method.HEAD && state == Http.Method.GET);
      }

      // exact
      case PATH_EXACT -> { target.pathReset(); yield testPathExact(target, asString()); }

      case SUBPATH_EXACT -> { yield testPathExact(target, asString()); }

      // segments
      case PATH_SEGMENTS -> { target.pathReset(); yield testSegments(target, asSegments()); }

      case SUBPATH_SEGMENTS -> { yield testSegments(target, asSegments()); }

      // wildcard
      case PATH_WILDCARD -> { target.pathReset(); yield testPathRegion(target, asString()); }

      case SUBPATH_WILDCARD -> { yield testPathRegion(target, asString()); }

      // conditions
      case PATH_WITH_CONDITIONS -> { target.pathReset(); yield testPathWithConditions(target); }

      case SUBPATH_WITH_CONDITIONS -> { yield testPathWithConditions(target); }
    };
  }

  private boolean testPathExact(HttpExchange target, String exact) {
    final int pathIndex;
    pathIndex = target.pathIndex();

    final String path;
    path = target.pathUnchecked();

    final int thisLength;
    thisLength = path.length() - pathIndex;

    final int thatLength;
    thatLength = exact.length();

    if (thisLength == thatLength && path.regionMatches(pathIndex, exact, 0, thatLength)) {
      target.pathIndexAdd(thatLength);

      return true;
    } else {
      return false;
    }
  }

  private boolean testPathRegion(HttpExchange target, String region) {
    final int pathIndex;
    pathIndex = target.pathIndex();

    final String path;
    path = target.pathUnchecked();

    if (path.regionMatches(pathIndex, region, 0, region.length())) {
      target.pathIndexAdd(region.length());

      return true;
    } else {
      return false;
    }
  }

  private boolean testSegments(HttpExchange target, List<Segment> segments) {
    for (Segment segment : segments) {
      final boolean result;
      result = switch (segment.kind) {
        case EXACT -> testPathExact(target, segment.value);

        case PARAM -> testPathParam(target, segment.value, segment.c);

        case PARAM_LAST -> testPathParamLast(target, segment.value);

        case REGION -> testPathRegion(target, segment.value);
      };

      if (!result) {
        return false;
      }
    }

    return true;
  }

  private boolean testPathParam(HttpExchange target, String name, char terminator) {
    final int pathIndex;
    pathIndex = target.pathIndex();

    final String path;
    path = target.pathUnchecked();

    final int terminatorIndex;
    terminatorIndex = path.indexOf(terminator, pathIndex);

    if (terminatorIndex < 0) {
      return false;
    }

    final String varValue;
    varValue = path.substring(pathIndex, terminatorIndex);

    // immediately after the terminator
    target.pathIndex(terminatorIndex + 1);

    target.pathParamsPut(name, varValue);

    return true;
  }

  private boolean testPathParamLast(HttpExchange target, String name) {
    final int pathIndex;
    pathIndex = target.pathIndex();

    final String path;
    path = target.pathUnchecked();

    final int solidus;
    solidus = path.indexOf('/', pathIndex);

    if (solidus < 0) {

      final String varValue;
      varValue = path.substring(pathIndex);

      target.pathIndexAdd(varValue.length());

      target.pathParamsPut(name, varValue);

      return true;

    } else {

      return false;

    }
  }

  private boolean testPathWithConditions(HttpExchange target) {
    final WithConditions data;
    data = (WithConditions) state;

    final List<Segment> segments;
    segments = data.segments();

    final HttpPathParam[] conditions;
    conditions = data.conditions();

    return testSegments(target, segments) && testConditions(target, conditions);
  }

  private boolean testConditions(HttpExchange target, HttpPathParam[] conditions) {
    for (HttpPathParam condition : conditions) {
      if (!condition.test(target)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public final String toString() {
    return kind + "=" + state;
  }

  final boolean endsInWildcard() {
    return switch (kind) {
      case PATH_WILDCARD, SUBPATH_WILDCARD -> true;

      case PATH_SEGMENTS, SUBPATH_SEGMENTS -> {
        final List<Segment> segments;
        segments = asSegments();

        yield endsInWildcard(segments);
      }

      case PATH_WITH_CONDITIONS, SUBPATH_WITH_CONDITIONS -> {
        final WithConditions data;
        data = (WithConditions) state;

        final List<Segment> segments;
        segments = data.segments();

        yield endsInWildcard(segments);
      }

      default -> false;
    };
  }

  private boolean endsInWildcard(List<Segment> segments) {
    if (segments.isEmpty()) {
      return false;
    } else {
      final Segment last;
      last = segments.getLast();

      return last.kind == SegmentKind.REGION;
    }
  }

  @SuppressWarnings("unchecked")
  final HttpRequestMatcher with(HttpPathParam[] conditions) {
    if (kind != Kind.PATH_SEGMENTS && kind != Kind.SUBPATH_SEGMENTS) {
      throw new IllegalStateException("Cannot add path parameter conditions to matcher with kind=" + kind);
    }

    // caller, in theory, passed a safe array
    // no safe copy needed

    final Kind newKind;
    newKind = kind == Kind.PATH_SEGMENTS
        ? Kind.PATH_WITH_CONDITIONS
        : Kind.SUBPATH_WITH_CONDITIONS;

    final List<Segment> segments;
    segments = (List<Segment>) state;

    final WithConditions state;
    state = new WithConditions(segments, conditions);

    return new HttpRequestMatcher(newKind, state);
  }

  final boolean hasParam(String name) {
    if (kind == Kind.PATH_SEGMENTS || kind == Kind.SUBPATH_SEGMENTS) {
      final List<Segment> segments;
      segments = asSegments();

      for (Segment segment : segments) {
        final String paramName;
        paramName = segment.paramName();

        if (name.equals(paramName)) {
          return true;
        }
      }
    }

    return false;
  }

  @SuppressWarnings("unchecked")
  private List<Segment> asSegments() {
    return (List<Segment>) state;
  }

  private String asString() {
    return (String) state;
  }

}