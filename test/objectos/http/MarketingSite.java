/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
import objectos.html.HtmlTemplate;
import objectos.http.UriPath.Segment;

final class MarketingSite implements Handler {

  @Override
  public final void handle(ServerExchange http) {
    UriPath path;
    path = http.path();

    List<Segment> segments;
    segments = path.segments();

    if (segments.size() == 1) {
      root(http, segments.get(0));
    } else {
      http.notFound();
    }
  }

  private void root(ServerExchange http, Segment first) {
    String fileName;
    fileName = first.value();

    switch (fileName) {
      case "" -> http.movedPermanently("/index.html");

      case "index.html" -> http.methodMatrix(Method.GET, this::indexHtml);

      default -> http.notFound();
    }
  }

  private void indexHtml(ServerExchange http) {
    http.ok(new MarketingSiteHome());
  }

}

final class MarketingSiteHome extends HtmlTemplate {
  @Override
  protected void definition() {
    doctype();
    h1("home");
  }
}