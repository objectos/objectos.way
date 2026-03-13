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
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import objectos.way.Lang;
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
    extends HttpRequest
    permits HttpExchangeImpl {

  /**
   * Configures the creation of a stand-alone exchange instance.
   */
  sealed interface Options permits HttpExchangeBuilder {

    /**
     * Use the specified clock instance for generating time related values.
     *
     * @param value
     *        the clock instance to use
     */
    void clock(Clock value);

    /**
     * Adds the specified field name and value to the request body as if it
     * were sent by a HTML form. The name must be decoded.
     *
     * @param name
     *        the field name (decoded)
     * @param value
     *        the field value
     */
    void formParam(String name, int value);

    /**
     * Adds the specified field name and value to the request body as if it
     * were sent by a HTML form. The name must be decoded.
     *
     * @param name
     *        the field name (decoded)
     * @param value
     *        the field value
     */
    void formParam(String name, long value);

    /**
     * Adds the specified field name and value to the request body as if it
     * were sent by a HTML form. The name and value must be decoded.
     *
     * @param name
     *        the field name (decoded)
     * @param value
     *        the field value (decoded)
     */
    void formParam(String name, String value);

    /**
     * Adds the specified request header to the HTTP exchange.
     *
     * @param name
     *        the header field name
     * @param value
     *        the header field value
     */
    void header(HttpHeaderName name, String value);

    /**
     * Sets the request method to the specified value.
     *
     * @param value
     *        the HTTP method
     */
    void method(HttpMethod value);

    /**
     * Sets the path component of the request-target to the specified value.
     *
     * @param value
     *        the decoded path value
     */
    void path(String value);

    /// Sets the path parameter with the specified name to the specified value.
    ///
    /// @param name the name of the path parameter
    /// @param value the decoded value of the path parameter
    void pathParam(String name, String value);

    /**
     * Sets the request-target query parameter with the specified name to the
     * specified value.
     *
     * @param name
     *        the name of the query parameter
     * @param value
     *        the value of the query parameter
     */
    void queryParam(String name, int value);

    /**
     * Sets the request-target query parameter with the specified name to the
     * specified value.
     *
     * @param name
     *        the name of the query parameter
     * @param value
     *        the value of the query parameter
     */
    void queryParam(String name, long value);

    /**
     * Sets the request-target query parameter with the specified name to the
     * specified value.
     *
     * @param name
     *        the name of the query parameter
     * @param value
     *        the decoded value of the query parameter
     */
    void queryParam(String name, String value);

    /**
     * Stores the provided key-value pair in the resulting exchange.
     *
     * @param key
     *        the key to be stored
     * @param value
     *        the value to be stored
     * @param <T>
     *        the type of the value
     */
    <T> void set(Class<T> key, T value);

    /**
     * Listen to the response using the specified listener.
     *
     * @param value
     *        the listener to use
     */
    void responseListener(HttpResponseListener value);

    /**
     * Associate a session to the resulting exchange and store the provided
     * key-value pair in the session.
     *
     * @param <T>
     *        the type of the attribute
     * @param key
     *        the class object providing the attribute name
     * @param value
     *        the value to be stored
     */
    <T> void sessionAttr(Class<T> key, T value);

  }

  /**
   * Creates a stand-alone exchange instance typically to be used in test
   * cases.
   *
   * @param options
   *        allows for setting the options
   *
   * @return a newly created exchange instance with the configured options
   */
  static HttpExchange create(Consumer<? super Options> options) {
    return HttpExchangeImpl.create0(options);
  }

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
  // # BEGIN: Request Body
  // ##################################################################

  /**
   * Returns the names of all of the fields contained in this form data
   *
   * @return the names of all of the fields contained in this form data
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  Set<String> formParamNames();

  /**
   * Returns the first value of the specified form field name or {@code null}
   * if the field is not present.
   *
   * @param name
   *        the form field name
   *
   * @return the first value or {@code null}
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  String formParam(String name);

  /**
   * Returns the first value of the form field with the specified
   * {@code name}, converted to an {@code int} primitive. If the field is not
   * present in the form data or if its value cannot be converted to an
   * {@code int}, the specified {@code defaultValue} is returned.
   *
   * @param name
   *        the field name
   *
   * @param defaultValue
   *        the default value to return if the field is not present or cannot
   *        be converted to an {@code int}
   *
   * @return the value of the form field as an {@code int}, or
   *         {@code defaultValue} if the field is absent or cannot be
   *         converted
   */
  int formParamAsInt(String name, int defaultValue);

  /**
   * Returns the first value of the form field with the specified
   * {@code name}, converted to a {@code long} primitive. If the field is not
   * present in the form data or if its value cannot be converted to a
   * {@code long}, the specified {@code defaultValue} is returned.
   *
   * @param name
   *        the field name
   *
   * @param defaultValue
   *        the default value to return if the field is not present or cannot
   *        be converted to a {@code long}
   *
   * @return the value of the form field as an {@code long}, or
   *         {@code defaultValue} if the field is absent or cannot be
   *         converted
   */
  long formParamAsLong(String name, long defaultValue);

  /**
   * Returns a list containing all values associated to the specified form
   * field name. This method returns an empty list if the field name
   * is not present. In other words, this method never returns {@code null}.
   *
   * @param name
   *        the field name
   *
   * @return a list containing all of the decoded values in encounter order.
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  List<String> formParamAll(String name);

  /**
   * Returns an {@code IntStream} of all of the values, converted to
   * {@code int}, associated to the specified field name. Any value that
   * cannot be converted to an {@code int} is mapped to the specified
   * {@code defaultValue} instead.
   *
   * @param name
   *        the field name
   * @param defaultValue
   *        the default value to use if it cannot be converted to an
   *        {@code int}
   *
   * @return an {@code IntStream} of the values associated to the field name
   *
   * @throws IllegalStateException
   *         if the request body did not contain form data
   */
  IntStream formParamAllAsInt(String name, int defaultValue);

  /**
   * Returns a {@code LongStream} of all of the values, converted to
   * {@code long}, associated to the specified field name. Any value that
   * cannot be converted to an {@code long} is mapped to the specified
   * {@code defaultValue} instead.
   *
   * @param name
   *        the field name
   * @param defaultValue
   *        the default value to use if it cannot be converted to an
   *        {@code long}
   *
   * @return an {@code LongStream} of the values associated to the field name
   */
  LongStream formParamAllAsLong(String name, long defaultValue);

  // ##################################################################
  // # END: Request Body
  // ##################################################################

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
  <T> T sessionAttr(Lang.Key<T> key);

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
  <T> T sessionAttr(Lang.Key<T> key, T value);

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

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Bytes media);

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Stream media);

  /// Respond with a `200 OK` message with the specified media entity.
  ///
  /// @param media the media entity
  void ok(Media.Text media);

  // 3xx responses

  /**
   * Respond with a {@code 301 Moved Permanently} message with the specified
   * {@code Location} header.
   *
   * @param location
   *        the value of the {@code Location} header
   */
  void movedPermanently(String location);

  /**
   * Respond with a {@code 302 Found} message with the specified
   * {@code Location} header.
   *
   * @param location
   *        the value of the {@code Location} header
   */
  void found(String location);

  /**
   * Respond with a {@code 303 See Other} message with the specified
   * {@code Location} header.
   *
   * @param location
   *        the value of the {@code Location} header
   */
  void seeOther(String location);

  // 4xx responses

  /**
   * Respond with a {@code 400 Bad Request} message with the specified media
   * entity.
   *
   * @param media
   *        the media entity
   */
  void badRequest(Media media);

  /**
   * Respond with a {@code 403 Forbidden} message with the specified media
   * entity.
   *
   * @param media
   *        the media entity
   */
  void forbidden(Media media);

  /**
   * Respond with a {@code 404 Not Found} message with the specified media
   * entity.
   *
   * @param media
   *        the media entity
   */
  void notFound(Media media);

  /**
   * Respond with a {@code 405 Method Not Allowed} message with the specified
   * methods in the {@code Allow} response header.
   *
   * @param methods
   *        the allowed methods
   */
  void allow(HttpMethod... methods);

  // 5xx responses

  /**
   * Respond with a {@code 500 Internal Server Error} message with the
   * specified media entity. The specified {@code Throwable} will be noted.
   *
   * @param media
   *        the media entity
   * @param error
   *        the {@code Throwable} to be noted
   */
  void internalServerError(Media media, Throwable error);

  // response builder

  /**
   * Respond with the specified response message.
   *
   * @param response
   *        a handle to write the response message
   */
  void respond(Consumer<? super HttpResponse> response);

  /**
   * Return {@code true} if an HTTP response message has been written to this
   * exchange; {@code false} otherwise.
   *
   * @return {@code true} if an HTTP response message has been written to this
   *         exchange; {@code false} otherwise
   */
  boolean processed();

}