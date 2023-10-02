/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
import java.util.function.Supplier;
import objectos.http.server.Handler;
import objectos.lang.Check;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;

/**
 * Represents the server-side view of an HTTP exchange. This class allows for
 * writing an HTTP server.
 */
public sealed interface HttpExchange extends AutoCloseable
    permits objectos.http.internal.HttpExchange {

  static HttpExchange of(Socket socket) {
    Check.notNull(socket, "socket == null");

    int bufferSize;
    bufferSize = 1024;

    Supplier<Handler> handlerSupplier;
    handlerSupplier = null;

    NoteSink noteSink;
    noteSink = NoOpNoteSink.getInstance();

    return new objectos.http.internal.HttpExchange(bufferSize, handlerSupplier, noteSink, socket);
  }

  /**
   * Closes this exchange (and its underlying socket).
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void close() throws IOException;

  boolean keepAlive();

  /**
   * Parses the HTTP request.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void executeRequestPhase() throws IOException;

  /**
   * Returns the request HTTP method.
   *
   * @return the request HTTP method
   *
   * @throws IllegalStateException
   *         if the request has not been parsed or if the response has already
   *         been sent to the client.
   */
  Http.Method method();

  boolean hasResponse();

  void status(Http.Status status);

  void header(Http.Header.Name name, String value);

  void body(byte[] data);

}
