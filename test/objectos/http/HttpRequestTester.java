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

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;
import objectos.way.Y;

final class HttpRequestTester {

  private int bufferSizeInitial = 128;

  private int bufferSizeMax = 256;

  private final Socket socket;

  private HttpRequestTester(Socket socket) {
    this.socket = socket;
  }

  public final void bufferSize(int initial, int max) {
    bufferSizeInitial = initial;

    bufferSizeMax = max;
  }

  public static HttpRequest parse(Consumer<? super HttpRequestTester> test, Object... data) {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestTester tester;
    tester = new HttpRequestTester(socket);

    test.accept(tester);

    return tester.parse0();
  }

  private HttpRequest parse0() {
    try {
      final HttpSocket httpSocket;
      httpSocket = HttpSocket.of(bufferSizeInitial, bufferSizeMax, socket);

      final HttpRequestParser parser;
      parser = new HttpRequestParser(httpSocket);

      return parser.parse();
    } catch (IOException e) {
      throw new AssertionError(e);
    }
  }

}
