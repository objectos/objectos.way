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

final class HttpRequestParserY {

  private HttpExchangeBodyFiles bodyFiles = HttpExchangeBodyFiles.standard();

  private int bodyMemoryMax = 256;

  private long bodySizeMax = 1024;

  private int bufferSizeInitial = 128;

  private int bufferSizeMax = 256;

  private final Socket socket;

  private HttpRequestParserY(Socket socket) {
    this.socket = socket;
  }

  public final void bodyFiles(HttpExchangeBodyFiles value) {
    bodyFiles = value;
  }

  public final void bodySizeMax(long value) {
    bodySizeMax = value;
  }

  public final void bufferSize(int initial, int max) {
    bufferSizeInitial = initial;

    bodyMemoryMax = bufferSizeMax = max;
  }

  public static HttpRequestParser0Input input(int initial, int max, Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    return HttpRequestParser0Input.of(initial, max, socket);
  }

  public static HttpRequest parse(Consumer<? super HttpRequestParserY> test, Object... data) throws IOException {
    final Socket socket;
    socket = Y.socket(data);

    final HttpRequestParserY tester;
    tester = new HttpRequestParserY(socket);

    test.accept(tester);

    return tester.parse0();
  }

  private HttpRequest parse0() throws IOException {
    final HttpRequestParser0Input input;
    input = HttpRequestParser0Input.of(bufferSizeInitial, bufferSizeMax, socket);

    final HttpRequestParser parser;
    parser = new HttpRequestParser(bodyFiles, bodyMemoryMax, bodySizeMax, 0, input);

    return parser.parse();
  }

}
