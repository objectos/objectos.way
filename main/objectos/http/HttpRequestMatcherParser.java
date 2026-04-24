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

import java.util.ArrayList;
import java.util.List;

final class HttpRequestMatcherParser {

  private enum State {
    START_PATH,

    EXACT,

    PARAM_START,

    PARAM_PART,

    PARAM_END,

    REGION,

    WILDCARD;
  }

  private String paramName;

  private final String pathExpression;

  private final List<HttpRequestMatcher> segments = new ArrayList<>();

  private int startIndex;

  HttpRequestMatcherParser(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  public final HttpRequestMatcher parse() {
    State state;
    state = State.START_PATH;

    final int length;
    length = pathExpression.length();

    for (int idx = 0; idx < length; idx++) {
      final char c;
      c = pathExpression.charAt(idx);

      state = switch (state) {
        case START_PATH -> state0StartPath(c);

        case EXACT -> state1Exact(c);

        case PARAM_START -> state2ParamStart(idx, c);

        case PARAM_PART -> state3ParamPart(idx, c);

        case PARAM_END -> state4ParamEnd(idx, c);

        case REGION -> state5Region(c);

        case WILDCARD -> state6Wildcard();
      };
    }

    return switch (state) {
      case START_PATH -> throw illegal("Route path must start with a '/' character");

      case EXACT -> new HttpRequestMatcher2PathExact(pathExpression);

      case PARAM_START, PARAM_PART -> throw illegal("Route path with an unclosed path parameter definition");

      case PARAM_END -> {
        final HttpRequestMatcher param;
        param = new HttpRequestMatcher5PathParamLast(paramName);

        segments.add(param);

        yield new HttpRequestMatcher7List(segments);
      }

      case REGION -> {
        final String value;
        value = pathExpression.substring(startIndex, length);

        final HttpRequestMatcher segment;
        segment = new HttpRequestMatcher2PathExact(value);

        segments.add(segment);

        yield new HttpRequestMatcher7List(segments);
      }

      case WILDCARD -> new HttpRequestMatcher7List(segments);
    };
  }

  private State state0StartPath(char c) {
    if (c == '/') {
      return State.EXACT;
    }

    else {
      throw illegal("Route path must start with a '/' character");
    }
  }

  private State state1Exact(char c) {
    if (c == '{') {
      return State.PARAM_START;
    }

    else {
      return State.EXACT;
    }
  }

  private State state2ParamStart(int idx, char c) {
    if (c == '{') {
      throw illegal("Route path parameter names must not contain the '{' character");
    }

    else if (c == '}') {
      final String value;
      value = pathExpression.substring(startIndex, idx - 1);

      if (!value.isEmpty()) {
        final HttpRequestMatcher segment;
        segment = new HttpRequestMatcher3PathRegion(value);

        segments.add(segment);
      }

      segments.add(HttpRequestMatcher6Wildcard.INSTANCE);

      return State.WILDCARD;
    }

    else {
      final String value;
      value = pathExpression.substring(startIndex, idx - 1);

      final HttpRequestMatcher segment;
      segment = new HttpRequestMatcher3PathRegion(value);

      segments.add(segment);

      startIndex = idx;

      return State.PARAM_PART;
    }
  }

  private State state3ParamPart(int idx, char c) {
    if (c == '{') {
      throw illegal("Route path parameter names must not contain the '{' character");
    }

    else if (c == '}') {
      paramName = pathExpression.substring(startIndex, idx);

      return State.PARAM_END;
    }

    else {
      return State.PARAM_PART;
    }
  }

  private State state4ParamEnd(int idx, char c) {
    if (c == '{') {
      throw illegal("Route path parameter must not begin immediately after the end of another parameter");
    }

    else {
      // skip char terminator
      startIndex = idx + 1;

      final HttpRequestMatcher segment;
      segment = new HttpRequestMatcher4PathParam(paramName, c);

      segments.add(segment);

      paramName = null;

      return State.REGION;
    }
  }

  private State state5Region(char c) {
    if (c == '{') {
      return State.PARAM_START;
    }

    else {
      return State.REGION;
    }
  }

  private State state6Wildcard() {
    throw illegal(
        "Route path can only declare the '{}' wildcard path parameter once and at the end of the path"
    );
  }

  private IllegalArgumentException illegal(String prefix) {
    return new IllegalArgumentException(prefix + ": " + pathExpression);
  }

}