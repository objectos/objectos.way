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
import java.util.function.Consumer;
import objectox.http.host.HostBuilder;

/// Configures a name-based web site to be served by a `Server` instance.
public sealed interface HostOptions permits HostBuilder {

  /// Sets the name of this host.
  ///
  /// @param value the host name
  void name(String value);

  /// Sets the `Handler` to the specified value. All requests to this host will
  /// be handled by this object.
  ///
  /// @param value a handler instance
  void handler(Handler value);

  /// Enables for this host support for HTTP sessions with the specified
  /// options.
  ///
  /// @param opts allows for setting the session options
  void session(Consumer<? super SessionOptions> opts);

  /// Enables for this host support for static files with the specified options.
  ///
  /// @param opts allows for setting the static files options
  ///
  /// @throws IOException if an I/O error occurs
  void staticFiles(Consumer<? super StaticFilesOptions> opts) throws IOException;

}
