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
package objectox.http.handler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import objectos.internal.VisibleForTesting;

final class PathExpressionParser {

  private int index;

  private Set<String> paramNames = Set.of();

  private final String pathExpression;

  private final List<Segment> segments = new ArrayList<>();

  private boolean stop;

  public PathExpressionParser(String pathExpression) {
    this.pathExpression = pathExpression;
  }

  @VisibleForTesting
  PathExpressionParser(String pathExpression, int index) {
    this.pathExpression = pathExpression;

    this.index = index;
  }

  public final PathExpression parse() {
    final PathExpressionParserStart start;
    start = new PathExpressionParserStart(this);

    start.execute();

    do {
      final PathExpressionParserLeft left;
      left = new PathExpressionParserLeft(this);

      left.execute();

      if (stop) {
        break;
      }

      final PathExpressionParserRight right;
      right = new PathExpressionParserRight(this);

      right.execute();
    } while (!stop);

    if (segments.isEmpty()) {
      throw new IllegalStateException("segments is empty");
    }

    return new PathExpression(
        paramNames,

        segments
    );
  }

  final boolean add(String paramName) {
    if (paramNames.isEmpty()) {
      paramNames = new HashSet<>();
    }

    return paramNames.add(paramName);
  }

  final void add(Segment segment) {
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

  final Iterable<Segment> segments() {
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
