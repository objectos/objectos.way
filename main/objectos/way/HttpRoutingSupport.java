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
import java.util.Objects;

sealed abstract class HttpRoutingSupport permits HttpRouting, HttpRoutingPath {

  List<Http.Handler> many;

  HttpRoutingSupport() {}

  public abstract Http.Handler build();

  public final void handler(Http.Handler value) {
    addMany(
        Objects.requireNonNull(value, "value == null")
    );
  }

  final void addMany(Http.Handler handler) {
    if (many == null) {
      many = new UtilList<>();
    }

    many.add(handler);
  }

  final Http.Handler ofPath(HttpRequestMatcher matcher, Http.RoutingPath.Module module) {
    final HttpRoutingPath routing;
    routing = new HttpRoutingPath(matcher);

    module.configure(routing);

    return routing.build();
  }

}