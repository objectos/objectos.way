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
import java.nio.charset.Charset;
import objectos.http.server.Body;
import objectos.lang.NoOpNoteSink;
import objectos.lang.NoteSink;
import objectox.lang.CharWritable;
import objectox.lang.Check;

/**
 * Represents the server-side view of an HTTP exchange. This class allows for
 * writing an HTTP server.
 */
public sealed interface HttpExchange extends AutoCloseable
    permits objectox.http.HttpExchange {

  static HttpExchange of(Socket socket) {
    return of(socket, 1024);
  }

  static HttpExchange of(Socket socket, int bufferSize) {
    Check.notNull(socket, "socket == null");
    Check.argument(bufferSize > 128, "buffer size must be > 128");

    NoteSink noteSink;
    noteSink = NoOpNoteSink.of();

    return new objectox.http.HttpExchange(bufferSize, noteSink, socket);
  }

  /**
   * Closes and ends this exchange by closing its underlying socket.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  @Override
  void close() throws IOException;

  boolean active();

  /**
   * Checks if the request method is equal to the specified method.
   *
   * @param method
   *        the method to be tested
   *
   * @return {@code true} if the request method is equal to the specified method
   *         and {@code false} otherwise
   */
  boolean is(Http.Method method);

  /**
   * Checks if the request method is equal to the one of the two specified
   * methods.
   *
   * @param method1
   *        a method to be tested
   * @param method2
   *        another method to be tested
   *
   * @return {@code true} if the request method is equal to one of the specified
   *         methods and {@code false} otherwise
   */
  boolean is(Http.Method method1, Http.Method method2);

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

  /**
   * Returns the decoded path component of the request target.
   *
   * @return the decoded path component of the request target.
   */
  String path();

  Http.Header.Value header(Http.Header.Name name);

  Body body();

  boolean hasResponse();

  Http.Status status();

  void status(Http.Status status);

  void header(Http.Header.Name name, String value);

  void body(byte[] data);

  void body(CharWritable entity, Charset charset);

}
