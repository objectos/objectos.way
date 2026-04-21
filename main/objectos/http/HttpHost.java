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

import java.util.function.Consumer;

/// Configures a name-based web site to be served by a `HttpServer` instance.
public sealed interface HttpHost permits HttpHost0Builder {

  /// Sets the name of this host. The specified name will be matched against the
  /// HTTP request `Host` header field value. Defaults to `localhost:port` when
  /// not specified, where `port` is the port number to which the server is
  /// listening to.
  ///
  /// @param value the host name
  void name(String value);

  /// Sets the `Handler` to the specified value. All requests to this host will
  /// be handled by this object.
  ///
  /// @param value a handler instance
  void handler(HttpHandler value);

  /// Uses the specified `HttpSessionStore` for HTTP session handling.
  ///
  /// @param value the `HttpSessionStore` instance to use
  void sessionStore(HttpSessionStore value);

  /// Configures the static files to be served by this host.
  ///
  /// @param opts allows for setting the options
  void staticFiles(Consumer<? super HttpStaticFiles> opts);

}
