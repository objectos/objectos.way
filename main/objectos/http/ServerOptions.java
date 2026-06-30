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

import java.time.Clock;
import java.util.function.Consumer;
import objectos.lang.Stage;
import objectos.way.Note;
import objectox.http.srv.ServerLoopBuilder;

/// Configures the creation of an HTTP server.
public sealed interface ServerOptions permits ServerLoopBuilder {

  /// Sets the size in bytes of the exchange buffer.
  ///
  /// The server will create a buffer of the specified size for each connection
  /// it accepts. Then it will use it for all request messages coming from that
  /// connection and all response messages sent to that connection.
  ///
  /// For each request message, the server will use the buffer to store the
  /// request line and request headers. It may also use it to store the request
  /// body. As a result, the buffer size limits the maximum request size, minus
  /// the request body, the server will accept.
  ///
  /// For each response message, the server will use the buffer to store the
  /// status line and the response headers. As a result, the buffer size limits
  /// the maximum response size, minus the response body, the server will emit.
  ///
  /// @param value the size (in bytes) of the exchange buffer
  void bufferSize(int value);

  /// Sets the server's clock to the specified value.
  ///
  /// @param value a clock instance
  void clock(Clock value);

  /// Adds a host with the specified configuration to this server.
  ///
  /// @param opts allows for setting the host options
  void host(Consumer<? super HostOptions> opts);

  /// Sets the note sink to the specified value.
  ///
  /// @param value a note sink instance
  void noteSink(Note.Sink value);

  /// Sets the server's port to the specified value.
  ///
  /// @param value the port to use
  void port(int value);

  /// Sets the request body options.
  ///
  /// @param opts allows for setting the options
  void requestBody(Consumer<? super RequestBodyOptions> opts);

  /// Sets the server's stage to the specified value.
  ///
  /// @param value the stage
  void stage(Stage value);

}
