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

/// Configures the top-level routing of an HTTP server.
public sealed interface HttpRouting2 permits HttpRouting0Builder {

  /// Configures a path-specific routing of an HTTP server.
  ///
  /// If none of the configured handlers produce a response, then this
  /// configuration responds with a `204 No Content` message.
  ///
  /// If method-specific handlers are configured, then this configuration
  /// responds with a `405 Method Not Allowed` message when a request does not
  /// match any of the configured methods.
  sealed interface OfPath permits HttpRouting1Path {

    /// Use the specified handler for `GET` requests.
    ///
    /// @param value the HTTP handler
    void GET(HttpHandler value);

    /// Use the specified handler for `POST` requests.
    ///
    /// @param value the HTTP handler
    void POST(HttpHandler value);

    /// Adds the specified path-specific handler to this configuration.
    ///
    /// @param value the HTTP handler
    void handler(HttpHandler value);

  }

  /// Adds the specified routes to this configuration.
  ///
  /// @param routes the routes to be appended to this configuration
  default void add(Consumer<? super HttpRouting2> routes) {
    routes.accept(this);
  }

  /// Adds the specified top-level handler to this configuration.
  ///
  /// @param value the HTTP handler
  void handler(HttpHandler value);

  /// Adds a path-specific route to this configuration.
  ///
  /// @param path a path expression
  /// @param opts allows for configuring the path-specific route
  void path(String path, Consumer<? super OfPath> opts);

}