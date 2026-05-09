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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class HttpPathMatcherParser {

  private enum State {
    START_PATH,

    EXACT,

    PARAM,

    REGION,

    RESULT_EXACT,

    RESULT_LIST;
  }

  private static final char[] DELIMS;

  static {
    final String pathDelim;
    pathDelim = Http.pathDelim();

    final char[] delims;
    delims = pathDelim.toCharArray();

    Arrays.sort(delims);

    DELIMS = delims;
  }

  private Set<String> paramNames = Set.of();

  private final String pathExpression;

  private int pathIndex;

  private final List<HttpPathMatcher> segments = new ArrayList<>();

  private int startIndex;

  HttpPathMatcherParser(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  public final HttpPathMatcher parse() {
    State state;
    state = State.START_PATH;

    while (true) {
      switch (state) {
        case START_PATH -> state = state0StartPath();

        case EXACT -> state = state1Exact();

        case PARAM -> state = state2Param();

        case REGION -> state = state3Region();

        case RESULT_EXACT -> {
          return new HttpPathMatcher0Exact(pathExpression);
        }

        case RESULT_LIST -> {
          return new HttpPathMatcher5List(segments);
        }
      }
    }
  }

  public final Set<String> paramNames() {
    return paramNames;
  }

  private State state0StartPath() {
    if (pathExpression.isEmpty()) {
      final String msg;
      msg = "Invalid path expression: it must not be empty";

      throw new IllegalArgumentException(msg);
    }

    final char first;
    first = pathExpression.charAt(0);

    if (first != '/') {
      final String msg;
      msg = "Invalid path expression: it must begin with the '/' character";

      throw new IllegalArgumentException(msg);
    }

    startIndex = 0;

    pathIndex = 1;

    return State.EXACT;
  }

  private State state1Exact() {
    final int bracket;
    bracket = pathExpression.indexOf('{', pathIndex);

    if (bracket >= 0) {
      return stateParamStart(bracket);
    }

    return State.RESULT_EXACT;
  }

  private State state2Param() {
    final int bracket;
    bracket = pathExpression.indexOf('}', pathIndex);

    if (bracket < 0) {
      final String msg;
      msg = "Invalid path expression: unclosed path parameter";

      throw new IllegalArgumentException(msg);
    }

    final String name;
    name = pathExpression.substring(pathIndex, bracket);

    final int lastIndex;
    lastIndex = pathExpression.length() - 1;

    final boolean last;
    last = bracket == lastIndex;

    if (name.isEmpty() && last) {
      segments.add(HttpPathMatcher4Wildcard.INSTANCE);

      return State.RESULT_LIST;
    }

    else if (name.isEmpty() && !last) {
      final String msg;
      msg = "Invalid path expression: the '{}' wildcard path parameter can only be declared at the end of the expression";

      throw new IllegalArgumentException(msg);
    }

    // validate path parameter name
    final boolean valid;
    valid = name.codePoints().skip(1).allMatch(Character::isJavaIdentifierPart);

    if (!valid) {
      final String msg;
      msg = "Invalid path expression: path parameter name must be a valid Java identifier";

      throw new IllegalArgumentException(msg);
    }

    final int first;
    first = name.codePointAt(0);

    if (!Character.isJavaIdentifierStart(first)) {
      final String msg;
      msg = "Invalid path expression: path parameter name must be a valid Java identifier";

      throw new IllegalArgumentException(msg);
    }

    if (paramNames.isEmpty()) {
      paramNames = new HashSet<>();
    }

    if (!paramNames.add(name)) {
      final String msg;
      msg = "Invalid path expression: duplicate path parameter name '%s'".formatted(name);

      throw new IllegalArgumentException(msg);
    }

    if (last) {
      final HttpPathMatcher matcher;
      matcher = new HttpPathMatcher3ParamLast(name);

      segments.add(matcher);

      return State.RESULT_LIST;
    }

    final int delimIndex;
    delimIndex = bracket + 1;

    final char delim;
    delim = pathExpression.charAt(delimIndex);

    final int validDelim;
    validDelim = Arrays.binarySearch(DELIMS, delim);

    if (validDelim < 0) {
      final String msg;
      msg = "Invalid path expression: path parameter can only be either at the end of the expression or immediately followed by one of " + Http.pathDelim();

      throw new IllegalArgumentException(msg);
    }

    final HttpPathMatcher matcher;
    matcher = new HttpPathMatcher2Param(name, delim);

    segments.add(matcher);

    pathIndex = startIndex = delimIndex + 1;

    return State.REGION;
  }

  private State state3Region() {
    final int bracket;
    bracket = pathExpression.indexOf('{', pathIndex);

    if (bracket >= 0) {
      return stateParamStart(bracket);
    }

    final String value;
    value = pathExpression.substring(startIndex);

    final HttpPathMatcher segment;
    segment = new HttpPathMatcher0Exact(value);

    segments.add(segment);

    return State.RESULT_LIST;
  }

  private State stateParamStart(int bracket) {
    final char delim;
    delim = pathExpression.charAt(bracket - 1);

    final int delimIdx;
    delimIdx = Arrays.binarySearch(DELIMS, delim);

    if (delimIdx < 0) {
      final String msg;
      msg = "Invalid path expression: path parameter can only be immediately preceeded by one of " + Http.pathDelim();

      throw new IllegalArgumentException(msg);
    }

    if (startIndex < bracket) {
      final String value;
      value = pathExpression.substring(startIndex, bracket);

      final HttpPathMatcher segment;
      segment = new HttpPathMatcher1Region(value);

      segments.add(segment);
    }

    startIndex = bracket;

    pathIndex = startIndex + 1;

    return State.PARAM;
  }

}