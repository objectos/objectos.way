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
import java.util.HexFormat;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

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

  static final class TextPlain implements Lang.MediaObject {

    private final String value;

    TextPlain(String value) {
      this.value = value;
    }

    @Override
    public final String contentType() {
      return "text/plain; charset=utf-8";
    }

    @Override
    public final byte[] mediaBytes() {
      return value.getBytes(StandardCharsets.UTF_8);
    }

  }

  private Testing() {}

  public static String cookie(String name, long high, long low) {
    final HexFormat format;
    format = HexFormat.of();

    final String h;
    h = format.toHexDigits(high);

    final String l;
    l = format.toHexDigits(low);

    return name + "=" + h + l;
  }

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

  public static RandomGenerator randomGeneratorOfLongs(long... longs) {
    return new RandomGenerator() {
      private final long[] values = longs.clone();

      private int index;

      @Override
      public final long nextLong() {
        return values[index++];
      }
    };
  }

}
