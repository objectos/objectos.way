/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.specgen.css.mdn;

import static org.testng.Assert.assertEquals;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import objectos.specgen.css.Property;
import objectos.specgen.css.Spec;
import objectos.util.UnmodifiableList;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class MdnCrawlerTest {

  private HttpServer httpServer;

  @BeforeClass
  public void _beforeClass() throws IOException {
    byte[] ipBytes = {127, 0, 0, 1};
    var ip = InetAddress.getByAddress(ipBytes);
    int port = 9986;
    var socket = new InetSocketAddress(ip, port);

    httpServer = HttpServer.create(socket, 0);
    httpServer.createContext("/", new ThisHandler());
    httpServer.start();
  }

  @AfterClass(alwaysRun = true)
  public void tearDown() {
    if (httpServer != null) {
      httpServer.stop(0);
    }
  }

  @Test
  public void crawl() {
    Spec spec = new MdnCrawler("http://127.0.0.1:9986").crawl();

    UnmodifiableList<Property> props = spec.properties();
    assertEquals(props.size(), 2);

    Property p0 = props.get(0);
    assertEquals(p0.name(), "background");

    Property p1 = props.get(1);
    assertEquals(p1.name(), "bottom");
  }

  private static class ThisHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      var method = exchange.getRequestMethod();

      assert method.equals("GET");

      var uri = exchange.getRequestURI();

      var path = uri.getPath();

      var html = Resource.readString("html/MDN" + path);

      var response = html.getBytes(StandardCharsets.UTF_8);

      var header = exchange.getResponseHeaders();

      header.put("Content-Type", List.of("text/html; charset=utf-8"));

      exchange.sendResponseHeaders(200, response.length);

      try (var out = exchange.getResponseBody()) {
        out.write(response);
      }
    }
  }

}
