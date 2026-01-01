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
package objectos.way;

import java.util.Objects;
import java.util.function.Predicate;

final class HttpRouting extends HttpRoutingSupport implements Http.Routing {

  private final Predicate<? super Http.Exchange> condition;

  public HttpRouting() {
    this(null);
  }

  private HttpRouting(Predicate<? super Http.Exchange> condition) {
    this.condition = condition;
  }

  @Override
  public final Http.Handler build() {
    return HttpHandler.of(condition, null, many);
  }

  @Override
  public final void install(Http.Routing.Module module) {
    module.configure(this);
  }

  @Override
  public final void path(String path, Http.RoutingPath.Module module) {
    Objects.requireNonNull(path, "path == null");

    final HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.parsePath(path);

    final Http.Handler handler;
    handler = ofPath(matcher, module);

    addMany(handler);
  }

  @Override
  public final void path(String path, Http.Method method, Http.Handler handler) {
    path(path, matched -> {
      matched.allow(method, handler);
    });
  }

  @Override
  public final void when(Predicate<? super Http.Exchange> condition, Http.Routing.Module module) {
    Objects.requireNonNull(condition, "condition == null");

    final HttpRouting builder;
    builder = new HttpRouting(condition);

    // implicit null-check
    module.configure(builder);

    final Http.Handler handler;
    handler = builder.build();

    addMany(handler);
  }

}