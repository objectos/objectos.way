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

import java.time.Clock;
import java.util.function.Consumer;
import objectos.lang.Key;
import objectos.way.Media;

/**
 * An HTTP request received by the server and its subsequent response to the
 * client.
 *
 * <p>
 * Unless otherwise specified, request-target related methods of this
 * interface return decoded values.
 */
public sealed interface HttpExchange
    extends HttpRequest, HttpResponse
    permits HttpExchange0 {

  /// Configures the creation of a stand-alone exchange instance.
  sealed interface Options permits HttpExchangeBuilder {

    /// Use the specified clock instance for generating time related values.
    ///
    /// @param value the clock instance to use
    void clock(Clock value);

    /// Adds the specified field name and value to the request body as if it
    /// were sent by a HTML form. The name must be decoded.
    ///
    /// @param name the field name (decoded)
    /// @param value the field value
    void formParam(String name, int value);

    /// Adds the specified field name and value to the request body as if it
    /// were sent by a HTML form. The name must be decoded.
    ///
    /// @param name the field name (decoded)
    /// @param value the field value
    void formParam(String name, long value);

    /// Adds the specified field name and value to the request body as if it
    /// were sent by a HTML form. The name and value must be decoded.
    ///
    /// @param name the field name (decoded)
    /// @param value the field value (decoded)
    void formParam(String name, String value);

    /// Adds the specified request header to the HTTP exchange.
    ///
    /// @param name the header field name
    /// @param value the header field value
    void header(HttpHeaderName name, String value);

    /// Sets the request method to the specified value.
    ///
    /// @param value the HTTP method
    void method(HttpMethod value);

    /// Sets the path component of the request-target to the specified value.
    ///
    /// @param value the decoded path value
    void path(String value);

    /// Sets the path parameter with the specified name to the specified value.
    ///
    /// @param name the name of the path parameter
    /// @param value the decoded value of the path parameter
    void pathParam(String name, String value);

    /// Sets the request-target query parameter with the specified name to the
    /// specified value.
    ///
    /// @param name the name of the query parameter
    /// @param value the value of the query parameter
    void queryParam(String name, int value);

    /// Sets the request-target query parameter with the specified name to the
    /// specified value.
    ///
    /// @param name the name of the query parameter
    /// @param value the value of the query parameter
    void queryParam(String name, long value);

    /// Sets the request-target query parameter with the specified name to the
    /// specified value.
    ///
    /// @param name the name of the query parameter
    /// @param value the decoded value of the query parameter
    void queryParam(String name, String value);

    /// Stores the provided key-value pair in the resulting exchange.
    ///
    /// @param key the key to be stored
    /// @param value the value to be stored
    /// @param <T> the type of the value
    <T> void set(Class<T> key, T value);

    /// Associate a session to the resulting exchange and store the provided
    /// key-value pair in the session.
    ///
    /// @param <T> the type of the attribute
    /// @param key the class object providing the attribute name
    /// @param value the value to be stored
    <T> void sessionAttr(Class<T> key, T value);

    /// Sets the resulting exchange to be `Testable` aware.
    void testable();

  }

  /// Creates a stand-alone exchange instance. It is typically used in test
  /// cases.
  ///
  /// @param options allows for setting the options
  ///
  /// @return a newly created exchange instance with the configured options
  static HttpExchange create(Consumer<? super Options> options) {
    final HttpExchangeBuilder builder;
    builder = new HttpExchangeBuilder();

    options.accept(builder);

    return builder.build();
  }

  // ##################################################################
  // # BEGIN: path parameters
  // ##################################################################

  /// Returns the value of the path parameter with the specified name if it
  /// exists or returns `null` otherwise.
  ///
  /// @param name the name of the path parameter
  ///
  /// @return the value if it exists or `null` if it does not
  String pathParam(String name);

  /// Returns, as an `int`, the value of the path parameter with the specified
  /// name. If the path parameter does not exist or if the value cannot be
  /// converted to an `int` value then the specified default value is returned
  /// instead.
  ///
  /// @param name the name of the path parameter
  /// @param defaultValue the value to be returned if the parameter does exist of
  ///        if its value cannot be converted to an `int` value
  ///
  /// @return the value converted to an `int` if it exists or the specified
  ///         default value otherwise
  default int pathParamAsInt(String name, int defaultValue) {
    String maybe;
    maybe = pathParam(name);

    if (maybe == null) {
      return defaultValue;
    }

    try {
      return Integer.parseInt(maybe);
    } catch (NumberFormatException expected) {
      return defaultValue;
    }
  }

  // ##################################################################
  // # END: path parameters
  // ##################################################################

  /**
   * Stores an object in this request. The object will be associated to the
   * name of the specified {@code Class} instance.
   * Stored objects are reset between requests.
   *
   * <p>
   * If an object is already associated to the specified key it will be
   * replaced with the specified value.
   *
   * <p>
   * Objects to be stored must not be {@code null}.
   *
   * @param <T> the type of the object
   * @param key
   *        the object will be associated to the name of this key
   * @param value
   *        the object to be stored in this request
   */
  <T> void set(Class<T> key, T value);

  /**
   * Retrieves the object stored in this request associated to the specified
   * key. Returns {@code null} if no object is found.
   *
   * @param <T> the type of the object
   * @param key
   *        the key to look for
   *
   * @return the object associated to the specified key or {@code null} if no
   *         object is found
   */
  <T> T get(Class<T> key);

  // ##################################################################
  // # BEGIN: Session Support
  // ##################################################################

  /**
   * If a session is not associated to this exchange, returns {@code true},
   * otherwise {@code false}.
   *
   * @return {@code true} if a session is not associated to this
   *         exchange, otherwise {@code false}
   */
  boolean sessionAbsent();

  /**
   * Returns the session value associated to the specified
   * class name, or {@code null} if no value is associated.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the class whose associated value is to be returned
   *
   * @return the session value associated to the specified class name, or
   *         {@code null} if no value is associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Class<T> key);

  /**
   * Returns the session value associated to the specified key, or
   * {@code null} if no value is associated.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the key object whose associated value is to be returned
   *
   * @return the session value, or {@code null} if no value is associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Key<T> key);

  /**
   * Using the name of the specified class as the key, associate the
   * specified value to this exchange's session.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the class object whose name will serve as the key
   * @param value
   *        the session value
   *
   * @return the previous session value, or {@code null} if no value was
   *         associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Class<T> key, T value);

  /**
   * Using the specified key, associates the specified value to this
   * exchange's session.
   *
   * @param <T>
   *        the type of the session value
   * @param key
   *        the key object
   * @param value
   *        the session value
   *
   * @return the previous session value, or {@code null} if no value was
   *         associated
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  <T> T sessionAttr(Key<T> key, T value);

  /**
   * Invalidates the session associated to this exchange.
   *
   * @throws IllegalStateException
   *         if no session is associated to this exchange
   */
  void sessionInvalidate();

  /**
   * If a session is associated to this exchange, returns {@code true},
   * otherwise {@code false}.
   *
   * @return {@code true} if a session is associated to this
   *         exchange, otherwise {@code false}
   */
  boolean sessionPresent();

  // ##################################################################
  // # END: Session Support
  // ##################################################################

  // ##################################################################
  // # BEGIN: StaticFiles Support
  // ##################################################################

  /// Saves the specified entity as a static file and responds with a `200 OK`
  /// message. Subsequent requests to this path are handled as a static file.
  ///
  /// @param media the media entity
  void staticFile(Media media);

  // ##################################################################
  // # END: StaticFiles Support
  // ##################################################################

}