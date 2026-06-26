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
package objectos.y;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandler;
import objectos.http.Server;
import objectos.way.Y;

public final class HttpClientY {

  private static final java.net.http.HttpClient INSTANCE;

  static {
    System.setProperty("jdk.httpclient.allowRestrictedHeaders", "connection,host");

    java.net.http.HttpClient httpClient;
    httpClient = java.net.http.HttpClient.newBuilder()
        .version(Version.HTTP_1_1)
        .build();

    Y.shutdownHook(httpClient);

    INSTANCE = httpClient;
  }

  public static URI uri(Server server, String path) {
    try {
      final InetAddress address;
      address = server.address();

      final String host;
      host = address.getHostAddress();

      final int port;
      port = server.port();

      return new URI("http", null, host, port, path, null, null);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public static <T> HttpResponse<T> send(HttpRequest request, BodyHandler<T> bodyHandler) {
    try {
      return INSTANCE.send(request, bodyHandler);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();

      throw new RuntimeException(e);
    }
  }

}
