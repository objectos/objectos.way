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

import java.util.function.Predicate;

/**
 * Configures a path-specific routing of an HTTP server.
 */
public sealed interface HttpRoutingPath permits HttpRoutingPathImpl {

  /**
   * An object for configuring path-specific routes of an HTTP server.
   */
  @FunctionalInterface
  interface Module {

    /**
     * Configures path-specific routes of an HTTP server.
     *
     * @param routing
     *        allows for configuring the routes
     */
    void configure(HttpRoutingPath routing);

  }

  /**
   * For a request with the specified method, use the specified
   * handler.
   *
   * <p>
   * If the registered handler produces no response, then the server
   * responds with a `204 No Content` message.
   *
   * <p>
   * If the request does not match any of the registered allowed methods for
   * this path, then the server responds with a `405 Method Not Allowed`
   * message.
   *
   * @param method
   *        the HTTP method to allow
   * @param handler
   *        the HTTP handler
   */
  void allow(HttpMethod method, HttpHandler handler);

  /**
   * For a request with the specified method, use the first handler that
   * produces a response. In other words, the server will iterate over the
   * specified handlers in order and, after a handler produces a response,
   * it will stop the processing.
   *
   * <p>
   * If none of the registered handlers produce a response, then the
   * server responds with a `204 No Content` message.
   *
   * <p>
   * If the request does not match any of the registered allowed methods for
   * this path, then the server responds with a `405 Method Not Allowed`
   * message.
   *
   * @param method
   *        the HTTP method to allow
   * @param first
   *        the first HTTP handler
   * @param rest
   *        the remaining HTTP handlers
   */
  void allow(HttpMethod method, HttpHandler first, HttpHandler... rest);

  void filter(HttpFilter value, HttpRoutingPath.Module module);

  /**
   * Appends to this configuration the specified path-specific handler.
   *
   * @param value
   *        the HTTP handler
   */
  void handler(HttpHandler value);

  void paramDigits(String name);

  void paramNotEmpty(String name);

  void paramRegex(String name, String value);

  /// Appends to this configuration the handler for the specified subpath and
  /// method. Subpaths can only be registered when this configuration
  /// represents a wildcard path.
  ///
  /// This method is a convenience to the following:
  ///
  /// ```java r.subpath(subpath, matched -> { matched.allow(method, handler);
  /// }); ```
  ///
  /// @param subpath a subpath expression
  /// @param method the only allowed method
  /// @param handler handles the requests for this subpath and method
  ///
  /// @throws IllegalStateException if this configuration does not represent a
  /// wildcard path
  void subpath(String subpath, HttpMethod method, HttpHandler handler);

  /**
   * Appends to this configuration the handlers for the specified subpath
   * defined by the specified module. Subpaths can only be registered when
   * this configuration represents a wildcard path.
   *
   * @param subpath
   *        a subpath expression
   * @param module
   *        the module defining path-specific routes
   *
   * @throws IllegalStateException
   *         if this configuration does not represent a wildcard path
   */
  void subpath(String subpath, HttpRoutingPath.Module module);

  /**
   * Appends to this configuration the handlers defined by the specified
   * module to be executed when the specified condition evaluates to
   * {@code true}.
   *
   * @param condition
   *        delegates to the specified module when this condition evaluates to
   *        {@code true}
   * @param module
   *        the module defining path-specific routes
   */
  void when(Predicate<? super HttpExchange> condition, HttpRoutingPath.Module module);

}