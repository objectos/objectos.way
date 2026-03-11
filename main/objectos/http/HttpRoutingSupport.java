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
import java.util.Objects;
import objectos.internal.UtilList;

sealed abstract class HttpRoutingSupport permits HttpRoutingImpl, HttpRoutingPathImpl {

  List<HttpHandler> many;

  HttpRoutingSupport() {}

  public abstract HttpHandler build();

  public final void handler(HttpHandler value) {
    addMany(
        Objects.requireNonNull(value, "value == null")
    );
  }

  final void addMany(HttpHandler handler) {
    if (many == null) {
      many = new UtilList<>();
    }

    many.add(handler);
  }

  final HttpHandler ofPath(HttpRequestMatcher matcher, HttpRoutingPath.Module module) {
    final HttpRoutingPathImpl routing;
    routing = new HttpRoutingPathImpl(matcher);

    module.configure(routing);

    return routing.build();
  }

}