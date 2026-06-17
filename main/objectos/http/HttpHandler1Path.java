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

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

final class HttpHandler1Path extends HttpHandler0Super {

  private final RouteMatcher matcher;

  private final Map<String, Predicate<String>> predicates;

  private final List<HttpHandler> handlers;

  HttpHandler1Path(RouteMatcher matcher, Map<String, Predicate<String>> predicates, List<HttpHandler> handlers) {
    this.matcher = matcher;

    this.predicates = predicates;

    this.handlers = handlers;
  }

  @Override
  final void handleImpl(HttpExchange http) {
    final String path;
    path = http.path();

    final RoutePath httpPath;
    httpPath = new RoutePath(path);

    if (!matcher.matches(httpPath)) {
      return;
    }

    final Map<String, String> pathParams;
    pathParams = httpPath.params;

    for (var entry : pathParams.entrySet()) {
      final String paramName;
      paramName = entry.getKey();

      final Predicate<String> predicate;
      predicate = predicates.get(paramName);

      if (predicate == null) {
        continue;
      }

      final String value;
      value = entry.getValue();

      if (predicate.test(value)) {
        continue;
      }

      return;
    }

    if (!pathParams.isEmpty()) {
      http.req(HttpExchange0.PATH_PARAMS, pathParams);
    }

    handleFirst(http, handlers);
  }

}
