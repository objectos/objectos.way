/*
 * Copyright (C) 2025-2026 Objectos Software LTDA.
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

import objectos.http.HttpExchange;
import objectos.http.HttpHandler;
import objectos.http.HttpMethod;
import objectos.http.HttpRouting;

final class MarketingSite implements HttpRouting.Module {

  @Override
  public final void configure(HttpRouting routing) {
    routing.path("/", path -> {
      path.handler(http -> http.movedPermanently("/index.html"));
    });

    routing.path("/index.html", path -> {
      path.handler(this::indexHtml);
    });

    routing.handler(HttpHandler.notFound());
  }

  private void indexHtml(HttpExchange http) {
    switch (http.method()) {
      case GET, HEAD -> http.ok(new MarketingSiteHome());

      default -> http.allow(HttpMethod.GET, HttpMethod.HEAD);
    }
  }

  private static class MarketingSiteHome extends Html.Template {
    @Override
    protected void render() {
      doctype();
      h1("home");
    }
  }

}
