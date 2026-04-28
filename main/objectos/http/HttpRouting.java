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

/// Allows for creating a `HttpHandler` instance by declaring HTTP routes.
///
/// If none of the declared routes produce a response, then the resulting handler
/// responds with the default `404 Not Found` message.
///
/// If method-specific routes are configured, then the resulting handler responds
/// with a `405 Method Not Allowed` message when a request does not match any of
/// the configured methods.
public sealed interface HttpRouting permits HttpRouting0Builder {

  /// Use the specified handler for `GET` requests.
  ///
  /// @param value the HTTP handler
  void GET(HttpHandler value);

  /// Use the specified handler for `POST` requests.
  ///
  /// @param value the HTTP handler
  void POST(HttpHandler value);

  /// Adds a path-specific route to this handler.
  ///
  /// @param path a path expression
  /// @param routing allows for configuring the path-specific route
  void path(String path, Consumer<? super HttpRouting> routing);

}