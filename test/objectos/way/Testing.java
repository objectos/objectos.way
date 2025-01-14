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

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.function.Consumer;

final class Testing {

  static final class HttpClient {

    public static final java.net.http.HttpClient INSTANCE;

    static {
      System.setProperty("jdk.httpclient.allowRestrictedHeaders", "connection,host");

      java.net.http.HttpClient httpClient;
      httpClient = java.net.http.HttpClient.newBuilder()
          .version(Version.HTTP_1_1)
          .build();

      TestingShutdownHook.register(httpClient);

      INSTANCE = httpClient;
    }

  }

  private Testing() {}

  public static HttpResponse<String> httpClient(String path, Consumer<HttpRequest.Builder> config) throws IOException, InterruptedException {
    // force early init
    java.net.http.HttpClient client;
    client = HttpClient.INSTANCE;

    HttpRequest.Builder builder;
    builder = HttpRequest.newBuilder();

    int port;
    port = TestingHttpServer.port();

    URI uri;
    uri = URI.create("http://localhost:" + port + path);

    builder.uri(uri);

    builder.timeout(Duration.ofMinutes(1));

    config.accept(builder);

    HttpRequest request;
    request = builder.build();

    return client.send(request, BodyHandlers.ofString(StandardCharsets.UTF_8));
  }

}
