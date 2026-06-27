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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import objectox.http.handler.HandlerOfSupplier;
import objectox.http.handler.RoutingPojo;

/// Processes an HTTP request to produce a result, e.g., an HTTP response.
@FunctionalInterface
public non-sealed interface Handler extends RoutingOption {

  /// Returns a new handler for processing the specified routes.
  ///
  /// @param routes allows for defining the top-level routes
  ///
  /// @return a newly created handler for processing the routes
  static Handler create(Consumer<? super Routing> routes) {
    return RoutingPojo.create0(routes);
  }

  /// Helper method for specifying a handler in a route declaration. The method
  /// returns the specified handler as it is.
  ///
  /// @param h the handler instance to be returned
  ///
  /// @return always the specified handler
  static Handler of(Handler h) {
    return h;
  }

  /// Returns a new handler which provides the result from the specified
  /// supplier. As a result, the supplier is invoked for each request processed.
  ///
  /// An invocation of the form:
  ///
  /// ```java
  /// Handler h = Handler.ofSupplier(HomeTemplate::new);
  /// ```
  ///
  /// Returns a handler that is equivalent to:
  ///
  /// ```java
  /// Handler h = _ -> new HomeTemplate();
  /// ```
  ///
  /// @param supplier the result supplier
  ///
  /// @return a newly created handler
  static Handler ofSupplier(Supplier<? extends Result> supplier) {
    return new HandlerOfSupplier(
        Objects.requireNonNull(supplier, "supplier == null")
    );
  }

  /// Handles the specified HTTP request and produces a `Result`.
  ///
  /// @param request the incoming HTTP request
  ///
  /// @return a `Result` instance representing the outcome of the processing; it
  ///         must never be `null`
  Result handle(Request request);

}