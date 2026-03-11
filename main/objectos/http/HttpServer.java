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

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Clock;
import java.util.function.Consumer;
import objectos.way.Note;

/**
 * An HTTP server.
 */
public sealed interface HttpServer extends Closeable permits HttpServerImpl {

  /**
   * Configures the creation of an HTTP server.
   */
  public sealed interface Options permits HttpServerBuilder {

    /// The IP address to which this server will listen to.
    ///
    /// @param value the IP address
    void address(InetAddress value);

    /**
     * Sets the initial and maximum sizes in bytes for the exchange buffer.
     *
     * <p>
     * The exchange will use the buffer to store the whole request as a
     * best-case scenario. As a minimum, the request line and request headers
     * must fit entirely in the buffer. As a result, the maximum buffer size
     * also limits the maximum request size, minus the request body, the
     * server will accept.
     *
     * @param initial
     *        the initial size (in bytes) of the exchange buffer
     * @param max
     *        the maximum size (in bytes) of the exchange buffer
     */
    void bufferSize(int initial, int max);

    /**
     * Sets the clock to the specified value.
     *
     * @param value
     *        a clock instance
     */
    void clock(Clock value);

    /**
     * Sets the {@code Handler} to the specified value. All requests
     * to the server will be handled by this object.
     *
     * @param value
     *        a handler instance
     */
    void handler(HttpHandler value);

    /**
     * Sets the note sink to the specified value.
     *
     * @param value
     *        a note sink instance
     */
    void noteSink(Note.Sink value);

    /**
     * Sets the server's port to the specified value.
     *
     * @param value
     *        the port to use
     */
    void port(int value);

    /**
     * Sets the maximum allowed size in bytes for the request body.
     *
     * <p>
     * If the server determines that the request body exceeds the limit, the
     * request processing ends, the server responds with a `413 Content Too
     * Large` message, and the server closes the connection.
     *
     * @param max
     *        the maximum size (in bytes) of an allowed request body
     */
    void requestBodySize(long max);

  }

  /**
   * References to the note instances emitted by an web server.
   */
  public sealed interface Notes permits HttpServerImpl.Notes {

    /**
     * Creates a new {@code Notes} instance.
     *
     * @return a new {@code Notes} instance.
     */
    static Notes create() {
      return HttpServerImpl.Notes.get();
    }

    /// This server has started and is ready to accept requests.
    ///
    /// @return the note instance
    Note.Ref1<HttpServer> started();

  }

  /**
   * Creates a new HTTP server instance with the specified configuration.
   *
   * @param options
   *        the HTTP server configuration
   *
   * @return a newly created HTTP server instance
   */
  static HttpServer create(Consumer<Options> options) {
    HttpServerBuilder builder;
    builder = new HttpServerBuilder();

    options.accept(builder);

    return builder.build();
  }

  /**
   * Starts this HTTP server.
   *
   * @throws IOException
   *         if an I/O error occurs
   */
  void start() throws IOException;

  /**
   * Returns the IP address this server is listening to.
   *
   * @return the IP address this server is listening to.
   */
  InetAddress address();

  /**
   * Returns the port number this server is listening to.
   *
   * @return the port number this server is listening to.
   */
  int port();

}