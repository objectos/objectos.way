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

/// Processes a `HttpExchange`.
@FunctionalInterface
public non-sealed interface HttpHandler extends HttpRoutes.Option {

  /// Returns a new handler for processing the specified routes.
  ///
  /// @param routing allows for defining the top-level routes
  ///
  /// @return a newly created handler for processing the routes
  static HttpHandler create(Consumer<? super HttpRouting> routing) {
    final HttpRouting0 builder;
    builder = new HttpRouting0();

    routing.accept(builder);

    return builder.build();
  }

  static HttpHandler of(Consumer<? super HttpRoutes> routes) {
    final HttpRoutes0 builder;
    builder = new HttpRoutes0();

    routes.accept(builder);

    return builder.build();
  }

  /// A handler that always responds with the configured `404 Not Found`
  /// message.
  ///
  /// @return the handler instance
  static HttpHandler notFound() {
    return HttpHandler2NotFound.INSTANCE;
  }

  /// Process the specified exchange. In other words, the method:
  ///
  /// - consumes the request; and
  /// - generates a response.
  ///
  /// @param http the exchange to be processed
  void handle(HttpExchange http);

}