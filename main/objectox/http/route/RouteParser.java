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
package objectox.http.route;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import objectos.internal.VisibleForTesting;

public final class RouteParser {

  private int index;

  private Set<String> paramNames = Set.of();

  private final String pathExpression;

  private final List<RouteMatcher> segments = new ArrayList<>();

  private boolean stop;

  public RouteParser(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  @VisibleForTesting
  RouteParser(String pathExpression, int index) {
    this.pathExpression = pathExpression;

    this.index = index;
  }

  public final RouteMatcher parse() {
    final RouteParserStart start;
    start = new RouteParserStart(this);

    start.execute();

    do {
      final RouteParserLeft left;
      left = new RouteParserLeft(this);

      left.execute();

      if (stop) {
        break;
      }

      final RouteParserRight right;
      right = new RouteParserRight(this);

      right.execute();
    } while (!stop);

    return switch (segments.size()) {
      case 0 -> throw new IllegalStateException("segments is empty");

      case 1 -> segments.get(0);

      default -> {
        final List<RouteMatcher> copy;
        copy = List.copyOf(segments);

        yield new RouteMatcherList(copy);
      }
    };
  }

  public final Set<String> paramNames() {
    return paramNames;
  }

  final boolean add(String paramName) {
    if (paramNames.isEmpty()) {
      paramNames = new HashSet<>();
    }

    return paramNames.add(paramName);
  }

  final void add(RouteMatcher segment) {
    segments.add(segment);
  }

  final void end() {
    stop = true;
  }

  final boolean hasNext() {
    return index < pathExpression.length();
  }

  final int index() {
    return index;
  }

  final int indexOf(char c) {
    return pathExpression.indexOf(c, index);
  }

  final int length() {
    return pathExpression.length();
  }

  final char next() {
    return pathExpression.charAt(index++);
  }

  final char peek() {
    return pathExpression.charAt(index);
  }

  final Iterable<RouteMatcher> segments() {
    return segments;
  }

  final boolean stop() {
    return stop;
  }

  final String substring(int beginIndex) {
    final String result;
    result = pathExpression.substring(beginIndex);

    index = pathExpression.length();

    return result;
  }

  final String substring(int beginIndex, int endIndex) {
    final String result;
    result = pathExpression.substring(beginIndex, endIndex);

    index = endIndex + 1;

    return result;
  }

}