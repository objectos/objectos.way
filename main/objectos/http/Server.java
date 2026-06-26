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

import module java.base;
import objectox.http.srv.ServerLoopBuilder;
import objectox.http.srv.ServerLoop;

/// An HTTP server.
public sealed interface Server extends Closeable permits ServerLoop {

  /// Creates a new HTTP server instance with the specified configuration. The
  /// server will be ready to accept connections after this method returns.
  ///
  /// @param opts the HTTP server configuration
  ///
  /// @return a newly created HTTP server instance
  ///
  /// @throws IOException if an I/O error occurs
  static Server create(Consumer<? super ServerOptions> opts) throws IOException {
    ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    opts.accept(builder);

    return builder.build();
  }

  /// Returns the IP address this server is listening to.
  ///
  /// @return the IP address this server is listening to.
  InetAddress address();

  /// Returns the port number this server is listening to.
  ///
  /// @return the port number this server is listening to.
  int port();

}