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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

final class PathExpression {

  private final Set<String> paramNames;

  private final List<Segment> segments;

  private Map<String, Predicate<String>> predicates = Map.of();

  PathExpression(Set<String> paramNames, List<Segment> segments) {
    this.paramNames = paramNames;

    this.segments = segments;
  }

  public final List<Segment> build() {
    return segments.stream().map(this::predicateIfNecessary).toList();
  }

  @Override
  public final int hashCode() {
    return Objects.hash(paramNames, segments);
  }

  @Override
  public final boolean equals(Object obj) {
    return obj instanceof PathExpression that
        && Objects.equals(paramNames, that.paramNames)
        && Objects.equals(segments, that.segments);
  }

  private Segment predicateIfNecessary(Segment original) {
    return switch (original) {
      case SegmentParam p -> p.with(predicates);

      case SegmentParamLast p -> p.with(predicates);

      default -> original;
    };
  }

  public final void pathParamNamed(PathParamNamed param) {
    final String name;
    name = param.name();

    if (!paramNames.contains(name)) {
      final String msg;
      msg = "Path expression does not declare a '%s' path parameter".formatted(name);

      throw new IllegalArgumentException(msg);
    }

    if (predicates.isEmpty()) {
      predicates = new HashMap<>();
    }

    final Predicate<String> predicate;
    predicate = param.predicate();

    final Predicate<String> existing;
    existing = predicates.put(name, predicate);

    if (existing != null) {
      final Predicate<String> combined;
      combined = existing.and(predicate);

      predicates.put(name, combined);
    }
  }

}
