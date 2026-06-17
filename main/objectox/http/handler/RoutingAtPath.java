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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import objectos.http.Handler;
import objectos.http.RouteMatcher;

final class RoutingAtPath extends RoutingAtCommon {

  private final RouteMatcher pathMatcher;

  private final Set<String> paramNames;

  private Map<String, Predicate<String>> predicates = Map.of();

  RoutingAtPath(RouteMatcher pathMatcher, Set<String> paramNames) {
    this.pathMatcher = pathMatcher;

    this.paramNames = paramNames;
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

  @Override
  final Handler build(Handler handler) {
    return new HandlerIfPath(pathMatcher, handler);
  }

}
