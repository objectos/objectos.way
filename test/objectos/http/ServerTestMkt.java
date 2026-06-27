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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import objectos.way.Y;
import objectos.y.HttpClientY;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ServerTestMkt {

  private Server server;

  private String hostName;

  @BeforeClass
  public void beforeClass() throws IOException {
    server = Server.create(opts -> {
      opts.host(new ServerTestMktHost()::host);

      opts.noteSink(Y.noteSink());
    });

    hostName = "mkt.server.test:" + server.port();

    Y.shutdownHook(server);
  }

  private URI uri(String path) {
    return HttpClientY.uri(server, path);
  }

  @Test(description = """
  it should redirect '/' to '/index.html'
  """)
  public void testCase01() throws IOException {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/"));
          req.header("Host", hostName);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 301);
          assertEquals(resp.headers().firstValue("Location").get(), "/index.html");
        }
    );
  }

  @Test(description = """
  GET /index.html should return 200 OK
  """)
  public void testCase02() throws IOException, InterruptedException {
    ServerY.resp(
        req -> {
          req.GET();
          req.uri(uri("/index.html"));
          req.header("Host", hostName);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 200);
          assertEquals(resp.body(), """
          <!DOCTYPE html>
          <h1>home</h1>
          """);
        }
    );
  }

  @Test(description = """
  HEAD /index.html should return 200 OK
  """)
  public void testCase03() throws IOException, InterruptedException {
    ServerY.resp(
        req -> {
          req.HEAD();
          req.uri(uri("/index.html"));
          req.header("Host", hostName);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 200);
          assertEquals(resp.body(), "");
        }
    );
  }

  @Test(description = """
  Other methods to /index.html should return 404
  """)
  public void testCase04() throws IOException {
    ServerY.resp(
        req -> {
          req.POST(BodyPublishers.noBody());
          req.uri(uri("/index.html"));
          req.header("Host", hostName);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 404);
        }
    );
  }

  @Test(description = """
  GET /i-do-not-exist should return 404
  """)
  public void testCase05() throws IOException {
    ServerY.resp(
        req -> {
          req.POST(BodyPublishers.noBody());
          req.uri(uri("/i-do-not-exist"));
          req.header("Host", hostName);
        },

        BodyHandlers.ofString(),

        resp -> {
          assertEquals(resp.statusCode(), 404);
        }
    );
  }

}