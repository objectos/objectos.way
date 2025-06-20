/*
 * Copyright (C) 2016-2025 Objectos Software LTDA.
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
package objectos.way;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.Duration;
import java.time.InstantSource;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.random.RandomGenerator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * The <strong>Objectos HTTP</strong> main class.
 */
public final class Http {

  /**
   * The cookies of an HTTP request message.
   */
  public sealed interface Cookies permits HttpCookies, HttpCookiesEmpty {

    /**
     * Parses the specified string to a {@code Cookies} instance.
     *
     * @param s
     *        the string to be parsed
     *
     * @return a {@code Cookies} instance representation of the cookies string
     *         value
     */
    static Cookies parse(String s) {
      Objects.requireNonNull(s, "s == null");

      if (s.isBlank()) {
        return HttpCookiesEmpty.INSTANCE;
      }

      HttpCookiesParser parser;
      parser = new HttpCookiesParser(s);

      return parser.parse();
    }

    /**
     * Returns the value of the cookie with the specified name; {@code null}
     * if a cookie with the specified name is not present.
     *
     * @param name
     *        the cookie name
     *
     * @return the value or {@code null} if the cookie is not present
     */
    String get(String name);

  }

  /**
   * Represents a CSRF token.
   */
  public sealed interface CsrfToken permits HttpToken {}

  /**
   * An HTTP request received by the server and its subsequent response to the
   * client.
   *
   * <p>
   * Unless otherwise specified, request-target related methods of this
   * interface return decoded values.
   */
  public sealed interface Exchange
      extends Request
      permits HttpExchange {

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
      void header(HeaderName name, String value);

      /**
       * Sets the request method to the specified value.
       *
       * @param value
       *        the HTTP method
       */
      void method(Http.Method value);

      /**
       * Sets the path component of the request-target to the specified value.
       *
       * @param value
       *        the decoded path value
       */
      void path(String value);

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
      void responseListener(ResponseListener value);

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
    static Exchange create(Consumer<? super Options> options) {
      return HttpExchange.create0(options);
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
     * Returns the session attribute associated to the specified key, or
     * {@code null} if the attribute is not found.
     *
     * @param <T>
     *        the type of the attribute
     * @param key
     *        the class object providing the attribute name
     *
     * @return the attribute value, or {@code null} if the attribute is not
     *         found
     *
     * @throws IllegalStateException
     *         if no session is associated to this exchange
     */
    <T> T sessionAttr(Class<T> key);

    /**
     * If a value is not mapped to the attribute name provided by
     * the specified key, associate the one provided by the specified
     * supplier.
     *
     * @param <T>
     *        the type of the attribute
     * @param key
     *        the class object providing the attribute name
     * @param supplier
     *        provides the object to be stored
     *
     * @throws IllegalStateException
     *         if no session is associated to this exchange
     */
    <T> void sessionAttr(Class<T> key, Supplier<? extends T> supplier);

    // ##################################################################
    // # END: Session Support
    // ##################################################################

    // 2xx responses

    /**
     * Respond with a {@code 200 OK} message with the specified media entity.
     *
     * @param media
     *        the media entity
     */
    void ok(Media.Bytes media);

    /**
     * Respond with a {@code 200 OK} message with the specified media entity.
     *
     * @param media
     *        the media entity
     */
    void ok(Media.Stream media);

    /**
     * Respond with a {@code 200 OK} message with the specified media entity.
     *
     * @param media
     *        the media entity
     */
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
    void allow(Http.Method... methods);

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
    void respond(Consumer<? super Response> response);

    /**
     * Return {@code true} if an HTTP response message has been written to this
     * exchange; {@code false} otherwise.
     *
     * @return {@code true} if an HTTP response message has been written to this
     *         exchange; {@code false} otherwise
     */
    boolean processed();

  }

  /**
   * Executes tasks around the invocation of an HTTP handler.
   */
  @FunctionalInterface
  public interface Filter {

    void filter(Http.Exchange http, Http.Handler handler);

  }

  /**
   * Responsible for processing an HTTP {@linkplain Exchange exchange}.
   */
  @FunctionalInterface
  public interface Handler {

    static Handler create(Consumer<? super Routing> config) {
      final HttpRouting.Of routing;
      routing = new HttpRouting.Of();

      config.accept(routing);

      return routing.build();
    }

    static <T> Handler factory(Function<T, ? extends Handler> factory, T value) {
      Objects.requireNonNull(factory, "factory == null");

      return HttpHandler.factory(factory, value);
    }

    static Handler noop() {
      return HttpHandler.NOOP;
    }

    // 2xx responses

    static Handler ok(Media media) {
      Objects.requireNonNull(media, "media == null");

      return switch (media) {
        case Media.Bytes bytes -> HttpHandler.ok(bytes);

        case Media.Text text -> HttpHandler.ok(text);

        case Media.Stream stream -> HttpHandler.ok(stream);
      };
    }

    // 3xx responses

    static Handler movedPermanently(String location) {
      Objects.requireNonNull(location, "location == null");

      return HttpHandler.movedPermanently(location);
    }

    // 4xx responses

    static Handler notFound() {
      return HttpHandler.notFound();
    }

    /**
     * Process the specified exchange i.e. consume the request and generate a
     * response.
     *
     * @param http
     *        the exchange to be processed
     */
    void handle(Http.Exchange http);

  }

  /**
   * An HTTP header name.
   */
  public sealed interface HeaderName permits HttpHeaderName {

    static HeaderName of(String name) {
      Objects.requireNonNull(name, "name == null");

      return HttpHeaderName.of(name);
    }

    /**
     * The {@code Accept-Encoding} header name.
     */
    HeaderName ACCEPT_ENCODING = HttpHeaderName.ACCEPT_ENCODING;

    /**
     * The {@code Allow} header name.
     */
    HeaderName ALLOW = HttpHeaderName.ALLOW;

    /**
     * The {@code Connection} header name.
     */
    HeaderName CONNECTION = HttpHeaderName.CONNECTION;

    /**
     * The {@code Content-Disposition} header name.
     */
    HeaderName CONTENT_DISPOSITION = HttpHeaderName.CONTENT_DISPOSITION;

    /**
     * The {@code Content-Length} header name.
     */
    HeaderName CONTENT_LENGTH = HttpHeaderName.CONTENT_LENGTH;

    /**
     * The {@code Content-Type} header name.
     */
    HeaderName CONTENT_TYPE = HttpHeaderName.CONTENT_TYPE;

    /**
     * The {@code Cookie} header name.
     */
    HeaderName COOKIE = HttpHeaderName.COOKIE;

    /**
     * The {@code Date} header name.
     */
    HeaderName DATE = HttpHeaderName.DATE;

    /**
     * The {@code ETag} header name.
     */
    HeaderName ETAG = HttpHeaderName.ETAG;

    /**
     * The {@code From} header name.
     */
    HeaderName FROM = HttpHeaderName.FROM;

    /**
     * The {@code Host} header name.
     */
    HeaderName HOST = HttpHeaderName.HOST;

    /**
     * The {@code If-None-Match} header name.
     */
    HeaderName IF_NONE_MATCH = HttpHeaderName.IF_NONE_MATCH;

    /**
     * The {@code Location} header name.
     */
    HeaderName LOCATION = HttpHeaderName.LOCATION;

    /**
     * The {@code Referer} header name.
     */
    HeaderName REFERER = HttpHeaderName.REFERER;

    /**
     * The {@code Set-Cookie} header name.
     */
    HeaderName SET_COOKIE = HttpHeaderName.SET_COOKIE;

    /**
     * The {@code Transfer-Encoding} header name.
     */
    HeaderName TRANSFER_ENCODING = HttpHeaderName.TRANSFER_ENCODING;

    /**
     * The {@code User-Agent} header name.
     */
    HeaderName USER_AGENT = HttpHeaderName.USER_AGENT;

    /**
     * The {@code Way-CSRF-Token} header name.
     */
    HeaderName WAY_CSRF_TOKEN = HttpHeaderName.WAY_CSRF_TOKEN;

    /**
     * The {@code Way-Request} header name.
     */
    HeaderName WAY_REQUEST = HttpHeaderName.WAY_REQUEST;

    /**
     * Returns this name in header case, i.e., first letter of each word
     * capitalized.
     *
     * @return this name in header case.
     */
    String headerCase();

    /**
     * Returns this name in lower case.
     *
     * @return this name in lower case.
     */
    String lowerCase();

  }

  /**
   * Configures the creation of a header value.
   */
  public sealed interface HeaderValueBuilder permits HttpExchange.HttpHeaderValueBuilder {

    /**
     * Begins the next value.
     *
     * @param value
     *        the header value
     */
    void value(String value);

    /**
     * Appends a parameter to the current header value with the specified
     * name and value.
     *
     * @param name
     *        the parameter name
     * @param value
     *        the parameter value
     *
     * @throws IllegalArgumentException
     *         if either the name or the value contains invalid characters
     */
    void param(String name, String value);

    /**
     * Appends a parameter to the current header value with the specified
     * name, charset and value.
     *
     * @param name
     *        the parameter name
     * @param charset
     *        the charset to use when encoding the value
     * @param value
     *        the parameter value
     *
     * @throws IllegalArgumentException
     *         if the name contains invalid characters
     */
    void param(String name, Charset charset, String value);

  }

  /**
   * The method of an HTTP request message.
   */
  public enum Method {

    /**
     * The CONNECT method.
     */
    CONNECT(false),

    /**
     * The DELETE method.
     */
    DELETE(true),

    /**
     * The GET method.
     */
    GET(true),

    /**
     * The HEAD method.
     */
    HEAD(true),

    /**
     * The OPTIONS method.
     */
    OPTIONS(false),

    /**
     * The PATCH method.
     */
    PATCH(true),

    /**
     * The POST method.
     */
    POST(true),

    /**
     * The PUT method.
     */
    PUT(true),

    /**
     * The TRACE method.
     */
    TRACE(false);

    static final Method[] VALUES = values();

    final boolean implemented;

    private Method(boolean implemented) {
      this.implemented = implemented;
    }

  }

  /**
   * Provides methods for inspecting the request message of an HTTP exchange.
   */
  public sealed interface Request
      extends
      RequestLine,
      RequestTarget,
      RequestHeaders,
      RequestBody {

  }

  /**
   * Provides methods for reading the body of an HTTP request message.
   */
  public interface RequestBody {

    /**
     * Returns an input stream that reads the bytes of this request body.
     *
     * @return an input stream that reads the bytes of this request body.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    InputStream bodyInputStream() throws IOException;

  }

  /**
   * Provides methods for inspecting the headers of an HTTP request message.
   *
   * <p>
   * Unless otherwise specified the values returned by the methods of this
   * interface are decoded.
   */
  public sealed interface RequestHeaders {

    /**
     * Returns the value of the first field line having the specified name;
     * returns {@code null} if the field line is not present.
     *
     * @param name
     *        the name of the header field line
     *
     * @return the value of first field line or {@code null} if a field line
     *         with the specified name is not present.
     */
    String header(Http.HeaderName name);

  }

  /**
   * Provides methods for inspecting the request line of an HTTP request
   * message.
   *
   * <p>
   * Unless otherwise specified the values returned by the methods of this
   * interface are decoded.
   */
  public sealed interface RequestLine extends RequestTarget {

    /**
     * The code of the method of this request message.
     *
     * @return the code of the method of this request message
     */
    Method method();

    /**
     * The version of the HTTP protocol of this request message.
     *
     * @return the version of the HTTP protocol of this request message.
     */
    Version version();

  }

  /**
   * Provides methods for inspecting the request-target of an HTTP request
   * message.
   *
   * <p>
   * Unless otherwise specified the values returned by the methods of this
   * interface are decoded.
   */
  public sealed interface RequestTarget {

    /**
     * The value of the path component.
     *
     * @return the value of the path component
     */
    String path();

    /**
     * Returns the value of the path parameter with the specified name
     * if it exists or returns {@code null} otherwise.
     *
     * @param name
     *        the name of the path parameter
     *
     * @return the value if it exists or {@code null} if it does not
     */
    String pathParam(String name);

    /**
     * Returns the first value of the query parameter with the specified name
     * or {@code null} if there are no values.
     *
     * @param name
     *        the name of the query parameter
     *
     * @return the first value if it exists or {@code null} if it does not
     */
    String queryParam(String name);

    /**
     * Returns all values of the query parameter with the specified name
     * or an empty list if there are no values. The list contains the values in
     * encounter order.
     *
     * @param name
     *        the name of the query parameter
     *
     * @return a list containing all values in encounter order; or an empty list
     *         if the parameter was not present in the request query
     */
    List<String> queryParamAll(String name);

    /**
     * Returns, as an {@code int}, the first value of the query parameter with
     * the specified name or returns the specified default value.
     *
     * <p>
     * The specified default value will be returned if the query component
     * does not contain a parameter with the specified name or if the first
     * value of such parameter does not represent an {@code int} value.
     *
     * @param name
     *        the name of the query parameter
     * @param defaultValue
     *        the value to be returned if the parameter does not exist or if
     *        its first value cannot be converted to an {@code int} value
     *
     * @return the first value converted to {@code int} if it exists or the
     *         specified default value otherwise
     */
    default int queryParamAsInt(String name, int defaultValue) {
      String maybe;
      maybe = queryParam(name);

      if (maybe == null) {
        return defaultValue;
      }

      try {
        return Integer.parseInt(maybe);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    }

    /**
     * Returns, as a {@code long}, the first value of the query parameter with
     * the specified name or returns the specified default value.
     *
     * <p>
     * The specified default value will be returned if the query component
     * does not contain a parameter with the specified name or if the first
     * value of such parameter does not represent a {@code long} value.
     *
     * @param name
     *        the name of the query parameter
     * @param defaultValue
     *        the value to be returned if the parameter does not exist or if
     *        its first value cannot be converted to an {@code long} value
     *
     * @return the first value converted to {@code long} if it exists or the
     *         specified default value otherwise
     */
    default long queryParamAsLong(String name, long defaultValue) {
      String maybe;
      maybe = queryParam(name);

      if (maybe == null) {
        return defaultValue;
      }

      try {
        return Long.parseLong(maybe);
      } catch (NumberFormatException expected) {
        return defaultValue;
      }
    }

    /**
     * The names of all of the query parameters in this request-target.
     *
     * @return the names of all of the query parameters
     */
    Set<String> queryParamNames();

    /**
     * The raw (encoded) value of the path component.
     *
     * @return the raw (encoded) value of the path component
     */
    String rawPath();

    /**
     * The raw (encoded) value of the query component. This method returns
     * {@code null} if this request-target does not have a query component.
     *
     * @return the raw (encoded) value of the query component or {@code null}
     */
    String rawQuery();

    /**
     * Returns the raw (encoded) value of the query component with the specified
     * parameter added or replaced if it exists.
     *
     * <p>
     * If a parameter with the same name already exists in the query, its value
     * is replaced with the specified value. If no such parameter exists, a new
     * parameter is added.
     *
     * <p>
     * Usage example:
     *
     * <pre>{@code
     * // original query is "search=java&sort=asc";
     * Http.RequestTarget target = ...
     *
     * // returns "search=java&sort=desc"
     * target.rawQueryWith("sort", "desc");
     *
     * // returns "search=java&sort=asc&page=2"
     * target.rawQueryWith("page", "2");
     * }</pre>
     *
     * @param name
     *        the name of the parameter to be added or replaced
     * @param value
     *        the value of the parameter to be added or set
     *
     * @return the raw query string with the updated parameter
     *
     * @throws IllegalArgumentException
     *         if {@code name} is blank
     */
    String rawQueryWith(String name, String value);

  }

  /**
   * Represents an HTTP response message.
   */
  public sealed interface Response permits HttpExchange.ResponseHandle {

    /**
     * Begins this HTTP response message by writing out the status line.
     *
     * @param value
     *        the response status
     */
    void status(Http.Status value);

    /**
     * Writes an HTTP response header field with the specified name and
     * value.
     *
     * @param name
     *        the header name
     * @param value
     *        the header value
     */
    void header(HeaderName name, long value);

    /**
     * Writes an HTTP response header field with the specified name and
     * value.
     *
     * @param name
     *        the header name
     * @param value
     *        the header value
     */
    void header(HeaderName name, String value);

    /**
     * Writes an HTTP response header field with the specified name and
     * value.
     *
     * <p>
     * Example usage:
     * <pre>{@code
     * response.header(Http.HeaderName.CONTENT_DISPOSITION, builder -> {
     *   builder.value("attachment");
     *   builder.param("filename", "document.pdf");
     *   builder.param("filename*", StandardCharsets.UTF_8, "document.pdf");
     * });
     * }</pre>
     *
     * <p>
     * Which would result in the following header field written out to the
     * response:
     *
     * <pre>{@code
     * Content-Disposition: attachment; filename=document.pdf; filename*=UTF-8''document.pdf
     * }</pre>
     *
     * @param name
     *        the header name
     * @param builder
     *        a handle for creating the header field value
     */
    void header(HeaderName name, Consumer<? super HeaderValueBuilder> builder);

    /**
     * Returns the server's current time.
     *
     * @return the RFC-5322 formatted server time
     */
    String now();

    /**
     * Ends this HTTP response message with an empty body.
     */
    void body();

    /**
     * Ends this HTTP response message with the specified body.
     */
    void body(byte[] bytes, int offset, int length);

    /**
     * Writes the required response headers for the specified media and ends
     * this HTTP response message with the contents from the specified media.
     *
     * @param media
     *        the media entity
     */
    void media(Media media);

  }

  /**
   * Listens to the response defined in a HTTP exchange.
   */
  public interface ResponseListener {

    /**
     * Invoked when the response status line is set.
     *
     * @param version
     *        the HTTP version
     * @param status
     *        the response status
     */
    default void status(Version version, Status status) {}

    /**
     * Invoked when a response header is set.
     *
     * @param name
     *        the header name
     * @param value
     *        the header value
     */
    default void header(HeaderName name, String value) {}

    /**
     * Invoked when a response body is set; if the response has no body this
     * method is invoked with {@code null}.
     *
     * @param body
     *        the response body; or {@code null} if the response has no body
     */
    default void body(Object body) {}

  }

  /**
   * Configures the routing of an HTTP server.
   */
  public sealed interface Routing permits HttpRouting.Of {

    public sealed interface OfPath permits HttpRouting.OfPath {

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
       */
      void allow(Method method, Handler handler);

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
       */
      void allow(Method method, Handler first, Handler... rest);

      void filter(Filter value, Consumer<OfPath> routes);

      void handler(Handler value);

      void paramDigits(String name);

      void paramNotEmpty(String name);

      void paramRegex(String name, String value);

      void subpath(String path, Consumer<OfPath> routes);

      void when(Predicate<? super Exchange> condition, Consumer<OfPath> routes);

    }

    void handler(Handler value);

    void install(Consumer<Routing> routes);

    void path(String path, Consumer<OfPath> routes);

    void when(Predicate<? super Exchange> condition, Consumer<Routing> routes);

  }

  /**
   * An HTTP server.
   */
  public sealed interface Server extends Closeable permits HttpServer {

    /**
     * Configures the creation of an HTTP server.
     */
    public sealed interface Options permits HttpServerBuilder {

      /**
       * Sets the initial and maximum sizes in bytes for the exchange buffer.
       *
       * <p>
       * The exchange will use the buffer to store the whole request as a
       * best-case scenario. As a minimum, the request line and request headers
       * must fit entirely in the buffer. As a result, the maximum buffer size
       * also limits the maximum request size, minus the request body, the
       * server will accept.
       *
       * @param initial
       *        the initial size (in bytes) of the exchange buffer
       * @param max
       *        the maximum size (in bytes) of the exchange buffer
       */
      void bufferSize(int initial, int max);

      void clock(Clock value);

      void handler(Handler value);

      void noteSink(Note.Sink value);

      void port(int port);

      /**
       * Sets the maximum allowed size in bytes for the request body.
       *
       * <p>
       * If the server determines that the request body exceeds the limit, the
       * request processing ends, the server responds with a `413 Content Too
       * Large` message, and the server closes the connection.
       *
       * @param max
       *        the maximum size (in bytes) of an allowed request body
       */
      void requestBodySize(long max);

    }

    /**
     * References to the note instances emitted by an web server.
     */
    public sealed interface Notes permits HttpServer.Notes {

      /**
       * Creates a new {@code Notes} instance.
       *
       * @return a new {@code Notes} instance.
       */
      static Notes create() {
        return HttpServer.Notes.get();
      }

      /**
       * This server has started and is ready to accept requests.
       */
      Note.Ref1<Http.Server> started();

    }

    /**
     * Creates a new HTTP server instance with the specified configuration.
     *
     * @param options
     *        the HTTP server configuration
     *
     * @return a newly created HTTP server instance
     */
    static Server create(Consumer<Options> options) {
      HttpServerBuilder builder;
      builder = new HttpServerBuilder();

      options.accept(builder);

      return builder.build();
    }

    /**
     * Starts this HTTP server.
     *
     * @throws IOException
     *         if an I/O error occurs
     */
    void start() throws IOException;

    /**
     * Returns the IP address this server is listening to.
     *
     * @return the IP address this server is listening to.
     */
    InetAddress address();

    /**
     * Returns the port number this server is listening to.
     *
     * @return the port number this server is listening to.
     */
    int port();

  }

  /**
   * Creates, stores and manages session instances.
   */
  public sealed interface SessionStore permits HttpSessionStore {

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
    static SessionStore create(Consumer<? super Options> options) {
      HttpSessionStoreBuilder builder;
      builder = new HttpSessionStoreBuilder();

      options.accept(builder);

      return builder.build();
    }

    /**
     * Loads the session associated to the specified exchange, or creates a new
     * session if one does not exist.
     *
     * @param http
     *        the HTTP exchange
     */
    void ensureSession(Http.Exchange http);

    /**
     * Loads the session associated to the specified exchange if one exists.
     *
     * @param http
     *        the HTTP exchange
     */
    void loadSession(Http.Exchange http);

    /**
     * Requires a POST, PUT, PATCH or DELETE request to contain a valid CSRF
     * token. If the request does contain a CSRF token, or if the token value
     * does not match the one from the session associated to the request, then
     * a {@code 403 Forbidden} response is written to the specified exchange.
     *
     * @param http
     *        the HTTP exchange
     */
    void requireCsrfToken(Http.Exchange http);

  }

  /**
   * Represents an HTTP {@code Set-Cookie} response header as defined in RFC
   * 6265.
   *
   * <p>
   * Example usage:
   * <pre>{@code
   * Http.SetCookie cookie = Http.SetCookie.create(opts -> {
   *     opts.name("session");
   *     opts.value("abc123");
   *     opts.httpOnly();
   *     opts.sameSite(Http.SetCookie.SameSite.STRICT);
   * });
   * }</pre>
   *
   * @see <a href="https://tools.ietf.org/html/rfc6265">RFC 6265 - HTTP State
   *      Management Mechanism</a>
   */
  public sealed interface SetCookie permits HttpSetCookie {

    /**
     * Configuration interface for building a {@link SetCookie} instance.
     * Provides methods to set cookie attributes before creation.
     */
    public sealed interface Config permits HttpSetCookieConfig {

      /**
       * Sets the name of the cookie.
       *
       * @param value
       *        the cookie name (required)
       */
      void name(String value);

      /**
       * Sets the value of the cookie.
       *
       * @param value
       *        the cookie value (required)
       */
      void value(String value);

      /**
       * Sets the {@code HttpOnly} attribute.
       */
      void httpOnly();

      /**
       * Sets the {@code Secure} attribute.
       */
      void secure();

      /**
       * Sets the {@code Path} attribute to the specified value.
       *
       * @param value
       *        the path value
       */
      void path(String value);

      /**
       * Sets the {@code Domain} attribute to the specified value.
       *
       * @param value
       *        the domain value
       */
      void domain(String value);

      /**
       * Sets the {@code Max-Age} attribute to the number of seconds of the
       * specified duration.
       *
       * @param value
       *        the duration after which the cookie expires
       */
      void maxAge(Duration value);

      /**
       * Sets the {@code Expires} attribute to the specified value.
       *
       * @param value
       *        the date/time when the cookie expires
       */
      void expires(ZonedDateTime value);

      /**
       * Sets the {@code SameSite} attribute to the specified value.
       *
       * @param value
       *        the SameSite policy to apply
       */
      void sameSite(SameSite value);
    }

    /**
     * Enum representing possible values for the SameSite attribute.
     */
    enum SameSite {
      /** Prevents the cookie from being sent in cross-site requests. */
      STRICT("Strict"),

      /** Allows the cookie in top-level navigation cross-site requests. */
      LAX("Lax"),

      /** Allows the cookie in all cross-site requests (not recommended). */
      NONE("None");

      final String text;

      private SameSite(String text) { this.text = text; }

    }

    /**
     * Creates a new {@link SetCookie} instance using a configuration consumer.
     *
     * @param options a consumer that configures the cookie attributes
     *
     * @return a new SetCookie instance
     *
     * @throws IllegalArgumentException if required attributes (name and value)
     *         are not set
     */
    static SetCookie create(Consumer<Config> options) {
      final HttpSetCookieConfig builder;
      builder = new HttpSetCookieConfig();

      options.accept(builder);

      return builder.build();
    }

    /**
     * Returns the string representation of this {@code Set-Cookie} header
     * value. The format follows RFC 6265, with attributes separated by
     * semicolons.
     *
     * @return the {@code Set-Cookie} header value
     */
    @Override
    String toString();

  }

  /**
   * The status of an HTTP response message.
   */
  public sealed interface Status permits HttpStatus {

    // Response constants

    // 2.x.x

    /**
     * The {@code 200 OK} status.
     */
    Status OK = HttpStatus.OK;

    /**
     * The {@code 204 No Content} status.
     */
    Status NO_CONTENT = HttpStatus.NO_CONTENT;

    // 3.x.x

    /**
     * The {@code 301 Moved Permanently} status.
     */
    Status MOVED_PERMANENTLY = HttpStatus.MOVED_PERMANENTLY;

    /**
     * The {@code 302 Found} status.
     */
    Status FOUND = HttpStatus.FOUND;

    /**
     * The {@code 303 See Other} status.
     */
    Status SEE_OTHER = HttpStatus.SEE_OTHER;

    /**
     * The {@code 304 Not Modified} status.
     */
    Status NOT_MODIFIED = HttpStatus.NOT_MODIFIED;

    // 4.x.x

    /**
     * The {@code 400 Bad Request} status.
     */
    Status BAD_REQUEST = HttpStatus.BAD_REQUEST;

    /**
     * The {@code 403 Forbidden} status.
     */
    Status FORBIDDEN = HttpStatus.FORBIDDEN;

    /**
     * The {@code 404 Not Found} status.
     */
    Status NOT_FOUND = HttpStatus.NOT_FOUND;

    /**
     * The {@code 405 Method Not Allowed} status.
     */
    Status METHOD_NOT_ALLOWED = HttpStatus.METHOD_NOT_ALLOWED;

    /**
     * The {@code 411 Length Required} status.
     */
    Status LENGTH_REQUIRED = HttpStatus.LENGTH_REQUIRED;

    /**
     * The {@code 413 Content Too Large} status.
     */
    Status CONTENT_TOO_LARGE = HttpStatus.CONTENT_TOO_LARGE;

    /**
     * The {@code 414 URI Too Long} status.
     */
    Status URI_TOO_LONG = HttpStatus.URI_TOO_LONG;

    /**
     * The {@code 415 UNSUPPORTED MEDIA TYPE} status.
     */
    Status UNSUPPORTED_MEDIA_TYPE = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

    /**
     * The {@code 422 UNPROCESSABLE CONTENT} status.
     */
    Status UNPROCESSABLE_CONTENT = HttpStatus.UNPROCESSABLE_CONTENT;

    /**
     * The {@code 431 Request Header Fields Too Large} status.
     */
    Status REQUEST_HEADER_FIELDS_TOO_LARGE = HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE;

    // 5.x.x

    /**
     * The {@code 500 INTERNAL SERVER ERROR} status.
     */
    Status INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * The {@code 501 NOT IMPLEMENTED} status.
     */
    Status NOT_IMPLEMENTED = HttpStatus.NOT_IMPLEMENTED;

    /**
     * The {@code 505 HTTP VERSION NOT SUPPORTED} status.
     */
    Status HTTP_VERSION_NOT_SUPPORTED = HttpStatus.HTTP_VERSION_NOT_SUPPORTED;

    /**
     * The code of this status.
     *
     * @return the code of this status.
     */
    int code();

    /**
     * The reason-phrase of this status.
     *
     * @return the reason-phrase of this status.
     */
    String reasonPhrase();

  }

  // exception types

  public static abstract class AbstractHandlerException extends RuntimeException implements Handler {

    private static final long serialVersionUID = -8277337261280606415L;

  }

  /**
   * Thrown to indicate that a content type is not supported.
   */
  public static final class UnsupportedMediaTypeException extends RuntimeException {

    private static final long serialVersionUID = -6412173093510319276L;

    private final String unsupportedMediaType;

    private final List<String> supportedMediaTypes;

    /**
     * Creates a new {@code UnsupportedMediaTypeException}.
     *
     * @param unsupportedMediaType
     *        the name of the media type such as {@code application/pdf} or
     *        {@code image/gif} that caused this exception to be thrown
     *
     * @param supportedMediaTypes
     *        the names of the supported media types such as
     *        {@code application/json}
     */
    public UnsupportedMediaTypeException(String unsupportedMediaType, String... supportedMediaTypes) {
      super((String) null);

      this.unsupportedMediaType = unsupportedMediaType;

      if (supportedMediaTypes.length == 0) {
        throw new IllegalArgumentException("At least one media type is required");
      }

      this.supportedMediaTypes = List.of(supportedMediaTypes);
    }

    @Override
    public final String getMessage() {
      if (unsupportedMediaType != null) {
        return "Supports " + supportedMediaTypes.stream().collect(Collectors.joining(", ")) + " but got " + unsupportedMediaType;
      } else {
        return "Supports " + supportedMediaTypes.stream().collect(Collectors.joining(", ")) + " but Content-Type was not specified";
      }
    }

    /**
     * Returns the name of the unsupported media type.
     *
     * @return the name of the unsupported media type.
     */
    public final String unsupportedMediaType() {
      return unsupportedMediaType;
    }

    /**
     * Returns the names of the supported media types.
     *
     * @return the names of the supported media types.
     */
    public final List<String> supportedMediaTypes() {
      return supportedMediaTypes;
    }

  }

  /**
   * The version of the HTTP protocol.
   */
  public enum Version {

    HTTP_0_9("HTTP/0.9"),

    HTTP_1_0("HTTP/1.0"),

    HTTP_1_1("HTTP/1.1");

    final byte[] responseBytes;

    private Version(String signature) {
      String response;
      response = signature + " ";

      responseBytes = Http.utf8(response);
    }

    final void appendTo(StringBuilder out) {
      switch (this) {
        case HTTP_0_9 -> out.append("HTTP/0.9");

        case HTTP_1_0 -> out.append("HTTP/1.0");

        case HTTP_1_1 -> out.append("HTTP/1.1");
      }
    }

  }

  static final class NoopResponseListener implements ResponseListener {

    static final NoopResponseListener INSTANCE = new NoopResponseListener();

    @Override
    public final void status(Version version, Status status) { /* noop */ }

    @Override
    public final void header(HeaderName name, String value) { /* noop */ }

    @Override
    public final void body(Object body) { /* noop */ }

  }

  private Http() {}

  /**
   * Formats a date so it can be used as the value of a {@code Date} HTTP
   * header.
   *
   * @param date
   *        the date to be formatted
   *
   * @return the formatted date
   */
  public static String formatDate(ZonedDateTime date) {
    ZonedDateTime normalized;
    normalized = date.withZoneSameInstant(ZoneOffset.UTC);

    return IMF_FIXDATE.format(normalized);
  }

  // utils

  private static final DateTimeFormatter IMF_FIXDATE;

  static {
    DateTimeFormatterBuilder b;
    b = new DateTimeFormatterBuilder();

    Map<Long, String> dow;
    dow = new HashMap<>();

    dow.put(1L, "Mon");
    dow.put(2L, "Tue");
    dow.put(3L, "Wed");
    dow.put(4L, "Thu");
    dow.put(5L, "Fri");
    dow.put(6L, "Sat");
    dow.put(7L, "Sun");

    b.appendText(ChronoField.DAY_OF_WEEK, dow);

    b.appendLiteral(", ");

    b.appendValue(ChronoField.DAY_OF_MONTH, 2);

    b.appendLiteral(' ');

    Map<Long, String> moy;
    moy = new HashMap<>();

    moy.put(1L, "Jan");
    moy.put(2L, "Feb");
    moy.put(3L, "Mar");
    moy.put(4L, "Apr");
    moy.put(5L, "May");
    moy.put(6L, "Jun");
    moy.put(7L, "Jul");
    moy.put(8L, "Aug");
    moy.put(9L, "Sep");
    moy.put(10L, "Oct");
    moy.put(11L, "Nov");
    moy.put(12L, "Dec");

    b.appendText(ChronoField.MONTH_OF_YEAR, moy);

    b.appendLiteral(' ');

    b.appendValue(ChronoField.YEAR, 4);

    b.appendLiteral(' ');

    b.appendValue(ChronoField.HOUR_OF_DAY, 2);

    b.appendLiteral(':');

    b.appendValue(ChronoField.MINUTE_OF_HOUR, 2);

    b.appendLiteral(':');

    b.appendValue(ChronoField.SECOND_OF_MINUTE, 2);

    b.appendLiteral(' ');

    b.appendOffset("+HHMM", "GMT");

    IMF_FIXDATE = b.toFormatter(Locale.US);
  }

  private static final byte DIGIT_0 = '0';

  private static final byte DIGIT_9 = '9';

  static boolean isDigit(byte value) {
    return DIGIT_0 <= value && value <= DIGIT_9;
  }

  static int parseHexDigit(byte value) {
    return parseHexDigit(value);
  }

  static int parseHexDigit(int value) {
    return switch (value) {
      case '0' -> 0;
      case '1' -> 1;
      case '2' -> 2;
      case '3' -> 3;
      case '4' -> 4;
      case '5' -> 5;
      case '6' -> 6;
      case '7' -> 7;
      case '8' -> 8;
      case '9' -> 9;
      case 'a', 'A' -> 10;
      case 'b', 'B' -> 11;
      case 'c', 'C' -> 12;
      case 'd', 'D' -> 13;
      case 'e', 'E' -> 14;
      case 'f', 'F' -> 15;

      default -> throw new IllegalArgumentException(
          "Illegal hex char= " + (char) value
      );
    };
  }

  @SuppressWarnings("unchecked")
  static void queryParamsAdd(Map<String, Object> map, Function<String, String> decoder, String rawKey, String rawValue) {
    String key;
    key = decoder.apply(rawKey);

    String value;
    value = decoder.apply(rawValue);

    Object oldValue;
    oldValue = map.put(key, value);

    if (oldValue == null) {
      return;
    }

    if (oldValue instanceof String s) {

      List<String> list;
      list = Util.createList();

      list.add(s);

      list.add(value);

      map.put(key, list);

    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
    }
  }

  static <K> String queryParamsGet(Map<K, Object> params, K key) {
    Object maybe;
    maybe = params.get(key);

    return switch (maybe) {
      case null -> null;

      case String s -> s;

      case List<?> l -> (String) l.get(0);

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + maybe.getClass()
      );
    };
  }

  @SuppressWarnings("unchecked")
  static List<String> queryParamsGetAll(Map<String, Object> params, String name) {
    Object maybe;
    maybe = params.get(name);

    return switch (maybe) {
      case null -> List.of();

      case String s -> List.of(s);

      case List<?> l -> (List<String>) l;

      default -> throw new AssertionError(
          "Type should not have been put into the map: " + maybe.getClass()
      );
    };
  }

  @SuppressWarnings("unchecked")
  static String queryParamsToString(Map<String, Object> params, Function<String, String> processor) {
    StringBuilder builder;
    builder = new StringBuilder();

    int count;
    count = 0;

    for (String key : params.keySet()) {
      if (count++ > 0) {
        builder.append('&');
      }

      queryParamsToStringAppend(builder, processor, key);

      builder.append('=');

      Object existing;
      existing = params.get(key);

      if (existing instanceof String s) {
        queryParamsToStringAppend(builder, processor, s);
      }

      else {
        List<String> list;
        list = (List<String>) existing;

        String value;
        value = list.get(0);

        queryParamsToStringAppend(builder, processor, value);

        for (int i = 1; i < list.size(); i++) {
          builder.append('&');

          queryParamsToStringAppend(builder, processor, key);

          builder.append('=');

          value = list.get(i);

          queryParamsToStringAppend(builder, processor, value);
        }
      }
    }

    return builder.toString();
  }

  private static void queryParamsToStringAppend(StringBuilder builder, Function<String, String> processor, String value) {
    String processed;
    processed = processor.apply(value);

    builder.append(processed);
  }

  static byte[] utf8(String value) {
    return value.getBytes(StandardCharsets.UTF_8);
  }

  static void fillTable(byte[] table, String ascii, byte value) {
    final byte[] bytes;
    bytes = ascii.getBytes(StandardCharsets.US_ASCII);

    for (byte b : bytes) {
      table[b] = value;
    }
  }

  // URI RFC 3986

  static String raw(String input) {
    final int len;
    len = input.length();

    if (len == 0) {
      return input;
    }

    int firstToEncode;
    firstToEncode = -1;

    for (int i = 0; i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (c < 0x20) {
        // iso control
        firstToEncode = i;

        break;
      }

      if (c > 0x7F) {
        firstToEncode = i;

        break;
      }
    }

    if (firstToEncode == -1) {
      return input;
    }

    final int worstCaseChars;
    worstCaseChars = len - firstToEncode;

    final int initialBytesLength;
    initialBytesLength = (firstToEncode + 1) + (worstCaseChars * 3);

    byte[] bytes;
    bytes = new byte[initialBytesLength];

    int bytesIndex;
    bytesIndex = 0;

    for (int i = 0; i < firstToEncode; i++) {
      bytes[bytesIndex++] = (byte) input.charAt(i);
    }

    char highSurrogate;
    highSurrogate = 0;

    for (int i = firstToEncode; i < input.length(); i++) {
      final char c;
      c = input.charAt(i);

      if (c <= ' ') {
        highSurrogate = ensureZero(highSurrogate);

        bytes = ensureBytes(bytes, bytesIndex, 3);

        bytesIndex = raw(bytes, bytesIndex, c);
      }

      else if (c <= 0x7F) {
        highSurrogate = ensureZero(highSurrogate);

        bytes = ensureBytes(bytes, bytesIndex, 1);

        bytes[bytesIndex++] = (byte) c;
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 6);

        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 12);

        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);

        bytesIndex = raw(bytes, bytesIndex, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 9);

        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    return new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);
  }

  private static char ensureZero(char highSurrogate) {
    if (highSurrogate != 0) {
      throw new IllegalArgumentException("High surrogate \\u" + Integer.toHexString(highSurrogate) + " must be followed by a low surrogate.");
    }

    return 0;
  }

  private static byte[] ensureBytes(byte[] bytes, int bytesIndex, int requiredLength) {
    final int requiredIndex;
    requiredIndex = bytesIndex + requiredLength - 1;

    return Util.growIfNecessary(bytes, requiredIndex);
  }

  private static int raw(byte[] bytes, int bytesIndex, int value) {
    // value is < 256
    bytes[bytesIndex++] = '%';
    bytes[bytesIndex++] = hexDigit(value >> 4);
    bytes[bytesIndex++] = hexDigit(value & 0b1111);

    return bytesIndex;
  }

  private static final class Rfc8187 {

    static final byte[] TABLE = table();

    static final byte INVALID = 0;

    static final byte VALID = 1;

    private static byte[] table() {
      final byte[] table;
      table = new byte[128];

      final String attrChars;
      attrChars = "!#$&+-.^_`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

      for (int idx = 0, len = attrChars.length(); idx < len; idx++) {
        final char c;
        c = attrChars.charAt(idx);

        table[c] = VALID;
      }

      return table;
    }

  }

  /**
   * RFC-8187: encodes the UTF-8 value of a parameter of an HTTP header value.
   */
  static String rfc8187(String input) {
    final int len = input.length();

    if (input.isEmpty()) {
      return "UTF-8''";
    }

    int firstToEncode;
    firstToEncode = -1;

    for (int i = 0; i < len; i++) {
      final char c;
      c = input.charAt(i);

      if (c >= 128) {
        firstToEncode = i;

        break;
      }

      final byte flag;
      flag = Rfc8187.TABLE[c];

      if (flag == Rfc8187.INVALID) {
        firstToEncode = i;

        break;
      }
    }

    if (firstToEncode == -1) {
      return "UTF-8''" + input;
    }

    final int worstCaseBytes;
    worstCaseBytes = len - firstToEncode;

    final int initialBytesLength;
    initialBytesLength = (firstToEncode + 1) + (worstCaseBytes * 3);

    byte[] bytes;
    bytes = new byte[initialBytesLength];

    int bytesIndex;
    bytesIndex = 0;

    for (int i = 0; i < firstToEncode; i++) {
      bytes[bytesIndex++] = (byte) input.charAt(i);
    }

    char highSurrogate;
    highSurrogate = 0;

    for (int i = firstToEncode; i < input.length(); i++) {
      final char c;
      c = input.charAt(i);

      if (c <= 0x7F) {
        final byte flag;
        flag = Rfc8187.TABLE[c];

        if (flag == Rfc8187.INVALID) {
          highSurrogate = ensureZero(highSurrogate);

          bytes = ensureBytes(bytes, bytesIndex, 3);

          bytesIndex = raw(bytes, bytesIndex, c);
        } else {
          highSurrogate = ensureZero(highSurrogate);

          bytes = ensureBytes(bytes, bytesIndex, 1);

          bytes[bytesIndex++] = (byte) c;
        }
      }

      else if (c <= 0x7FF) {
        highSurrogate = ensureZero(highSurrogate);

        // 110xxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 6);

        final int byte0;
        byte0 = 0b1100_0000 | (c >> 6); // c <= 0x7FF, no higher bits set.

        final int byte1;
        byte1 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);
      }

      else if (Character.isHighSurrogate(c)) {
        ensureZero(highSurrogate);

        highSurrogate = c;
      }

      else if (Character.isLowSurrogate(c)) {
        if (highSurrogate == 0) {
          throw new IllegalArgumentException("Low surrogate \\u" + Integer.toHexString(c) + " must be preceeded by a high surrogate.");
        }

        int codePoint;
        codePoint = Character.toCodePoint(highSurrogate, c);

        highSurrogate = 0;

        // 11110uvv 10vvwwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 12);

        final int byte0;
        byte0 = 0b1111_0000 | (codePoint >> 18);

        final int byte1;
        byte1 = 0b1000_0000 | ((codePoint >> 12) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | ((codePoint >> 6) & 0b0011_1111);

        final int byte3;
        byte3 = 0b1000_0000 | (codePoint & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);

        bytesIndex = raw(bytes, bytesIndex, byte3);
      }

      else if (c <= 0xFFFF) {
        highSurrogate = ensureZero(highSurrogate);

        // 1110wwww 10xxxxyy 10yyzzzz
        bytes = ensureBytes(bytes, bytesIndex, 9);

        final int byte0;
        byte0 = 0b1110_0000 | (c >> 12);

        final int byte1;
        byte1 = 0b1000_0000 | ((c >> 6) & 0b0011_1111);

        final int byte2;
        byte2 = 0b1000_0000 | (c & 0b0011_1111);

        bytesIndex = raw(bytes, bytesIndex, byte0);

        bytesIndex = raw(bytes, bytesIndex, byte1);

        bytesIndex = raw(bytes, bytesIndex, byte2);
      }
    }

    if (highSurrogate != 0) {
      throw new IllegalArgumentException("Unmatched high surrogate at end of string");
    }

    final String value;
    value = new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);

    return "UTF-8''" + value;
  }

  static byte hexDigit(int nibble) {
    return (byte) (nibble < 10 ? '0' + nibble : 'A' + (nibble - 10));
  }

  static String subDelims() {
    return "!$&'()*+,;=";
  }

  static String tchar() {
    return "!#$%&'*+-.^`|~ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  }

  static String unreserved() {
    return Ascii.alphaUpper() + Ascii.alphaLower() + Ascii.digit() + "-._~";
  }

  static String vchar() {
    return Ascii.visible();
  }

  static int requiredHexDigits(int value) {
    final int leadingZeros;
    leadingZeros = Integer.numberOfLeadingZeros(value);

    final int magnitude;
    magnitude = Integer.SIZE - leadingZeros;

    final int chars;
    chars = ((magnitude + 3) / 4);

    return Math.max(chars, 1);
  }

}
