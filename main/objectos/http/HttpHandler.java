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
import java.util.function.Function;

/**
 * Responsible for processing an HTTP {@linkplain HttpExchange exchange}.
 */
@FunctionalInterface
public interface HttpHandler {

  /**
   * Returns a new handler for processing the routes defined in the specified
   * routing module.
   *
   * @param module
   *        the module defining top-level routes
   *
   * @return a newly created handler for processing the routes
   */
  static HttpHandler of(HttpRouting.Module module) {
    final HttpRoutingImpl routing;
    routing = new HttpRoutingImpl();

    module.configure(routing);

    return routing.build();
  }

  /**
   * Returns a handler which delegates the processing to the handler created
   * by the specified factory and value.
   *
   * <p>
   * The returned handler is equivalent to the following:
   *
   * <pre>{@code
   * static <T> Handler factory(Function<T, ? extends Handler> factory, T value) {
   *   return new Handler() {
   *     &#64;Override
   *     public void handle(Http.Exchange http) {
   *       factory.apply(value).handle(http);
   *     }
   *   };
   * }
   * }</pre>
   *
   * <p>
   * Except it is done in a null-safe way.
   *
   * @param factory
   *        a function that provides a handler based on the specified value
   * @param value
   *        the factory's argument
   *
   * @return a newly created handler
   */
  static <T> HttpHandler factory(Function<T, ? extends HttpHandler> factory, T value) {
    Objects.requireNonNull(factory, "factory == null");

    return HttpHandlerImpl.factory(factory, value);
  }

  /**
   * Returns a handler that does nothing.
   *
   * @return a handler that does nothing
   */
  static HttpHandler noop() {
    return HttpHandlerImpl.NOOP;
  }

  // 4xx responses

  /// A handler that always responds with an empty `404 Not Found` message.
  ///
  /// @return the handler instance
  static HttpHandler notFound() {
    return HttpHandlerImpl.notFound();
  }

  /**
   * Process the specified exchange i.e. consume the request and generate a
   * response.
   *
   * @param http
   *        the exchange to be processed
   */
  void handle(HttpExchange http);

}