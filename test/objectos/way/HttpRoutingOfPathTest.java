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

import static org.testng.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class HttpRoutingOfPathTest {

  @Test
  public void subpath01() {
    final HttpRequestMatcher matcher;
    matcher = HttpRequestMatcher.pathWildcard("/app/");

    final HttpRouting.OfPath routing;
    routing = new HttpRouting.OfPath(matcher);

    final Lang.MediaObject object;
    object = Lang.MediaObject.textPlain("LOGIN", StandardCharsets.UTF_8);

    routing.subpath("login", path -> {
      path.handler(ok(object));
    });

    routing.handler(Http.Handler.notFound());

    final Http.Handler handler;
    handler = routing.build();

    final Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.path("/app/login");
    });

    handler.handle(http);

    assertEquals(http.responseBody(), object);
  }

  private Http.Handler ok(Lang.MediaObject object) {
    return http -> http.respond(object);
  }

}