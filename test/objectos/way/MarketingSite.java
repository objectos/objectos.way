/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

import java.util.function.Consumer;
import objectos.way.Http.Routing;

final class MarketingSite implements Consumer<Http.Routing> {

  @Override
  public final void accept(Routing routing) {
    routing.path("/", path -> {
      path.handler(Http.Handler.movedPermanently("/index.html"));
    });

    routing.path("/index.html", path -> {
      path.handler(this::indexHtml);
    });

    routing.handler(Http.Handler.notFound());
  }

  private void indexHtml(Http.Exchange http) {
    switch (http.method()) {
      case GET, HEAD -> http.ok(new MarketingSiteHome());

      default -> http.allow(Http.Method.GET, Http.Method.HEAD);
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
