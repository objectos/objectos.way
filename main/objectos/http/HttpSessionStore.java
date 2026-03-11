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

import java.time.Duration;
import java.time.InstantSource;
import java.util.function.Consumer;
import java.util.random.RandomGenerator;

/**
 * Creates, stores and manages session instances.
 */
public sealed interface HttpSessionStore permits HttpSessionStoreImpl {

  /**
   * Configures the creation of a {@code SessionStore} instance.
   */
  public sealed interface Options permits HttpSessionStoreBuilder {

    /**
     * Use the specified {@code name} when setting the client session cookie.
     *
     * @param name
     *        the cookie name to use
     */
    void cookieName(String name);

    /**
     * Sets the session cookie {@code Path} attribute to the specified value.
     *
     * @param path
     *        the session cookie {@code Path} attribute value
     */
    void cookiePath(String path);

    /**
     * Sets the session cookie {@code Max-Age} attribute to the specified
     * value.
     *
     * @param duration
     *        the session cookie {@code Max-Age} attribute value
     */
    void cookieMaxAge(Duration duration);

    /**
     * Sets the session cookie {@code Secure} attribute to the specified
     * value.
     *
     * @param value
     *        the session cookie {@code Secure} attribute value
     */
    void cookieSecure(boolean value);

    /**
     * Use the specified {@link RandomGenerator} instance for generating
     * CSRF token values.
     *
     * @param value
     *        the {@link RandomGenerator} instance to use
     */
    void csrfGenerator(RandomGenerator value);

    /**
     * Discards empty sessions, during a clean up operation, whose last access
     * time is greater than the specified duration.
     *
     * @param duration
     *        the duration value
     */
    void emptyMaxAge(Duration duration);

    /**
     * Use the specified {@link InstantSource} when setting session time
     * related values.
     *
     * @param value
     *        the {@link InstantSource} instance to use
     */
    void instantSource(InstantSource value);

    /**
     * Use the specified {@link RandomGenerator} instance for generating
     * session token values.
     *
     * @param value
     *        the {@link RandomGenerator} instance to use
     */
    void sessionGenerator(RandomGenerator value);

  }

  /**
   * Creates a new session store with the specified configuration.
   *
   * @param options
   *        the session store configuration
   *
   * @return a newly created session store with the specified
   *         configuration
   */
  static HttpSessionStore create(Consumer<? super Options> options) {
    HttpSessionStoreBuilder builder;
    builder = new HttpSessionStoreBuilder();

    options.accept(builder);

    return builder.build();
  }

  /// Loads the session associated to the specified exchange, or creates a new
  /// session if one does not exist.
  ///
  /// @param http the HTTP exchange
  void ensureSession(HttpExchange http);

  /// Loads the session associated to the specified exchange if one exists.
  ///
  /// @param http the HTTP exchange
  ///
  /// @return `true` if a session has been loaded; `false` otherwise
  boolean loadSession(HttpExchange http);

  /// Requires a POST, PUT, PATCH or DELETE request to contain a valid CSRF
  /// token. If the request does contain a CSRF token, or if the token value does
  /// not match the one from the session associated to the request, then a `403
  /// Forbidden` response is written to the specified exchange.
  ///
  /// @param http the HTTP exchange
  void requireCsrfToken(HttpExchange http);

}