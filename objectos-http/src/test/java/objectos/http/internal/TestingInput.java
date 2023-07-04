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
package objectos.http.internal;

interface TestingInput {

  void accept(HttpExchange exchange);

  public record RegularInput(String request) implements TestingInput {
    @Override
    public final void accept(HttpExchange exchange) {
      exchange.buffer = new byte[1024];
      exchange.bufferIndex = -1;
      exchange.bufferLimit = -1;
      exchange.keepAlive = false;
      exchange.error = null;
      exchange.handler = TestingHandler.INSTANCE;
      exchange.method = null;
      exchange.requestHeaders = null;
      exchange.requestHeaderName = null;
      exchange.requestTarget = null;
      exchange.responseBody = null;
      exchange.responseHeaders = null;
      exchange.responseHeadersIndex = -1;
      exchange.socket = TestableSocket.of(request);
      exchange.state = HttpExchange._SETUP;
      exchange.status = null;
      exchange.versionMajor = -1;
      exchange.versionMinor = -1;
    }

    public int requestLength() {
      return request.length();
    }
  }

  RegularInput HTTP_001 = new RegularInput(
    """
    GET / HTTP/1.1
    Host: www.example.com
    Connection: close

    """.replace("\n", "\r\n")
  );

  public static HeaderValue hv(String string) {
    final byte[] bytes;
    bytes = Bytes.utf8(string);

    return new HeaderValue(bytes, 0, bytes.length);
  }

}