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
import java.net.Socket;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;
import objectos.way.Y;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParserTest {

  private static final HttpRequestBody EMPTY_BODY = new HttpRequestBody0(
      HttpRequestBodyData.ofNull(),
      Map.of()
  );

  private static final class Cfg extends HttpRequestBodyOptions {
    Path bodyDirectory;

    int bodyMemoryMax = 512;

    long bodySizeMax = 1024;

    int bufferSize = 512;

    long id = 0;

    HttpMethod method = HttpMethod.GET;

    String path = "/";

    Map<String, Object> queryParams = Map.of();

    HttpVersion0 version = HttpVersion0.HTTP_1_1;

    Map<HttpHeaderName, Object> headers = Map.of(HttpHeaderName.HOST, "www.example.com");

    HttpRequestBody body = EMPTY_BODY;

    static Consumer<? super Cfg> of(Consumer<? super Cfg> config) {
      return config;
    }

    final HttpRequest parse(Object... data) throws IOException {
      final Socket socket;
      socket = Y.socket(data);

      final byte[] buffer;
      buffer = new byte[bufferSize];

      final HttpRequestParser parser;
      parser = new HttpRequestParser(this, buffer, id, socket.getInputStream());

      return parser.parse();
    }

    final HttpRequest build() {
      return new HttpRequest0(
          method,

          path,

          queryParams,

          version,

          new HttpRequestHeaders0(headers),

          body
      );
    }

    @Override
    final Path directory() { return bodyDirectory; }

    @Override
    final int memoryMax() { return bodyMemoryMax; }

    @Override
    final long sizeMax() { return bodySizeMax; }
  }

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            """
            GET / HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            Cfg.of(cfg -> {
              cfg.method = HttpMethod.GET;
            }),
            "method"
        },
        {
            """
            HEAD /index.html HTTP/1.1\r
            Host: www.example.com\r
            \r
            """,
            Cfg.of(cfg -> {
              cfg.method = HttpMethod.HEAD;
              cfg.path = "/index.html";
            }),
            "method, path"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(String data, Consumer<? super Cfg> cfg, String description) throws IOException {
    final Cfg c;
    c = new Cfg();

    cfg.accept(c);

    assertEquals(
        c.parse(data),

        c.build()
    );
  }

}
