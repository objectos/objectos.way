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
 * Configures the top-level routing of an HTTP server.
 */
public sealed interface HttpRouting permits HttpRoutingImpl {

  /**
   * An object for configuring top-level routes of an HTTP server.
   */
  @FunctionalInterface
  interface Module {

    /**
     * Configures top-level routes of an HTTP server.
     *
     * @param routing
     *        allows for configuring the routes
     */
    void configure(HttpRouting routing);

  }

  /**
   * Appends to this configuration the specified top-level handler.
   *
   * @param value
   *        the HTTP handler
   */
  void handler(HttpHandler value);

  /**
   * Appends to this configuration the handlers defined by the specified
   * module.
   *
   * @param module
   *        the module defining top-level routes
   */
  void install(HttpRouting.Module module);

  /**
   * Appends to this configuration the handlers for the specified path defined
   * by the specified module.
   *
   * @param path
   *        a path expression
   * @param module
   *        the module defining path-specific routes
   */
  void path(String path, HttpRoutingPath.Module module);

  /// For a request that matches the specified path expression and method, use
  /// the specified handler.
  ///
  /// This method is a convenience to the following:
  ///
  /// ```java r.path(path, matched -> { matched.allow(method, handler); }); ```
  ///
  /// @param path a path expression
  /// @param method the only allowed method
  /// @param handler handles the requests for this path and method
  void path(String path, HttpMethod method, HttpHandler handler);

  /**
   * Appends to this configuration the handlers defined by the specified
   * module to be executed when the specified condition evaluates to
   * {@code true}.
   *
   * @param condition
   *        delegates to the specified module when this condition evaluates to
   *        {@code true}
   * @param module
   *        the module defining top-level routes
   */
  void when(Predicate<? super HttpExchange> condition, HttpRouting.Module module);

}