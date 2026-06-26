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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import objectos.way.Y;
import objectos.y.HttpClientY;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerTest {

  private Server server;

  @BeforeClass
  public void beforeClass() throws Exception {
    server = Server.create(opts -> {
      opts.host(host -> {
        host.name("server.test.localhost");

        final ServerTestHost1 host1;
        host1 = new ServerTestHost1();

        final Handler handler;
        handler = Handler.create(host1);

        host.handler(handler);
      });

      opts.noteSink(Y.noteSink());
    });

    Y.shutdownHook(server);
  }

  @Test(description = """
  Unmatched host
  """)
  public void general01() throws IOException {
    final URI uri;
    uri = HttpClientY.uri(server, "/");

    final HttpRequest req;
    req = HttpRequest.newBuilder(uri).header("Host", "server.test.localhost").build();

    final HttpResponse<String> resp;
    resp = HttpClientY.send(req, BodyHandlers.ofString());

    assertEquals(resp.statusCode(), 400);
    assertEquals(resp.body(), "Invalid host: server.test.localhost");
  }

}