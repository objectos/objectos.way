/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package testing.site.web;

import java.util.List;
import objectos.http.Handler;
import objectos.http.ServerExchange;
import objectos.http.UriPath;
import objectos.http.UriPath.Segment;
import objectos.web.WebResources;
import testing.zite.TestingSiteInjector;

public class UiHandler implements Handler {

  private final TestingSiteInjector injector;

  public UiHandler(TestingSiteInjector injector) {
    this.injector = injector;
  }

  @Override
  public final void handle(ServerExchange http) {
    UriPath path;
    path = http.path();

    switch (path.toString()) {
      // home and login are special cases so we treat them separately

      case "/" -> {
        Home home;
        home = new Home(injector);

        home.handle(http);
      }

      case "/login" -> {
        Login login;
        login = new Login(injector);

        login.handle(http);
      }

      default -> handle0(http, path);
    }
  }

  private void handle0(ServerExchange http, UriPath path) {
    List<Segment> segments;
    segments = path.segments();

    if (segments.size() == 1) {
      root(http, segments.getFirst());
    } else {
      http.notFound();
    }
  }

  private void root(ServerExchange http, Segment segment) {
    String fileName;
    fileName = segment.value();

    switch (fileName) {
      case "preflight.css",
           "way.js" -> {
        WebResources webResources;
        webResources = injector.webResources();

        webResources.handle(http);
      }

      case "styles.css" -> {
        Styles styles;
        styles = new Styles(injector);

        styles.handle(http);
      }

      default -> http.notFound();
    }
  }

}