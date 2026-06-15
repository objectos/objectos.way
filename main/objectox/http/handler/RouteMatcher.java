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

import java.util.function.Predicate;
import objectos.http.Request;

public sealed interface RouteMatcher
    extends Predicate<Request>
    permits
    RouteMatcherExact,
    RouteMatcherRegion,
    RouteMatcherParam,
    RouteMatcherParamLast,
    RouteMatcherWildcard,
    RouteMatcherList {

  boolean matches(RoutePath path);

  @Override
  default boolean test(Request request) {
    final String path;
    path = request.path();

    final RoutePath routePath;
    routePath = new RoutePath(path);

    return matches(routePath);
  }

}
