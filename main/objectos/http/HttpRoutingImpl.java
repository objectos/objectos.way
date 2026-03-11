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

import java.util.Objects;
import java.util.function.Predicate;

final class HttpRoutingImpl extends HttpRoutingSupport implements HttpRouting {

  private final Predicate<? super HttpExchange> condition;

  public HttpRoutingImpl() {
    this(null);
  }

  private HttpRoutingImpl(Predicate<? super HttpExchange> condition) {
    this.condition = condition;
  }

  @Override
  public final HttpHandler build() {
    return HttpHandlerImpl.of(condition, null, many);
  }

  @Override
  public final void install(HttpRouting.Module module) {
    module.configure(this);
  }

  @Override
  public final void path(String path, HttpRoutingPath.Module module) {
    Objects.requireNonNull(path, "path == null");

    final HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.parsePath(path);

    final HttpHandler handler;
    handler = ofPath(matcher, module);

    addMany(handler);
  }

  @Override
  public final void path(String path, HttpMethod method, HttpHandler handler) {
    path(path, matched -> {
      matched.allow(method, handler);
    });
  }

  @Override
  public final void when(Predicate<? super HttpExchange> condition, HttpRouting.Module module) {
    Objects.requireNonNull(condition, "condition == null");

    final HttpRoutingImpl builder;
    builder = new HttpRoutingImpl(condition);

    // implicit null-check
    module.configure(builder);

    final HttpHandler handler;
    handler = builder.build();

    addMany(handler);
  }

}