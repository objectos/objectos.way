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
package objectox.http;

public interface TestingInput {

  void accept(HttpExchange exchange);

  public record RegularInput(String request) implements TestingInput {
    @Override
    public final void accept(HttpExchange exchange) {
      TestingInput.regularAccept(exchange);

      exchange.socket = TestableSocket.of(request);
    }

    public int requestLength() {
      return request.length();
    }
  }

  public record KeepAliveInput(String... requests) implements TestingInput {
    public KeepAliveInput(String... requests) {
      this.requests = requests.clone();
    }
    @Override
    public final void accept(HttpExchange exchange) {
      TestingInput.regularAccept(exchange);

      Object[] data;
      data = requests.clone();

      exchange.socket = TestableSocket.of(data);
    }
  }

  static HeaderValue hv(String string) {
    final byte[] bytes;
    bytes = Bytes.utf8(string);

    return new HeaderValue(bytes, 0, bytes.length);
  }

  private static void regularAccept(HttpExchange exchange) {
    exchange.buffer = new byte[512];
    exchange.bufferIndex = -1;
    exchange.bufferLimit = -1;
    exchange.keepAlive = true;
    exchange.error = null;
    exchange.handlerSupplier = TestingHandler.INSTANCE;
    exchange.method = null;
    exchange.requestHeaders = null;
    exchange.requestHeaderName = null;
    exchange.requestTarget = null;
    exchange.responseBody = null;
    exchange.responseHeaders = null;
    exchange.responseHeadersIndex = -1;
    exchange.socket = null;
    exchange.state = HttpExchange._SETUP;
    exchange.status = null;
    exchange.versionMajor = -1;
    exchange.versionMinor = -1;
  }

}