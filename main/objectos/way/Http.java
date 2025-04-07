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
import java.util.stream.Collectors;

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
   * An HTTP request received by the server and its subsequent response to the
   * client.
   *
   * <p>
   * Unless otherwise specified, request-target related methods of this
   * interface return decoded values.
   */
  public sealed interface Exchange
      extends Request, Response
      permits HttpSupport, TestingExchange {

    /**
     * Configures the creation of a stand-alone exchange instance.
     */
    sealed interface Config permits HttpExchangeConfig {

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
       * Stores the provided key-value pair in the testing exchange.
       *
       * @param key
       *        the key to be stored
       * @param value
       *        the value to be stored
       * @param <T>
       *        the type of the value
       */
      <T> void set(Class<T> key, T value);

    }

    /**
     * Creates a stand-alone exchange instance typically to be used in test
     * cases.
     *
     * @param config
     *        configures the exchange instance creation
     *
     * @return a newly created exchange instance with the configured options
     */
    static Exchange create(Consumer<Config> config) {
      HttpExchangeConfig builder;
      builder = new HttpExchangeConfig();

      config.accept(builder);

      return builder.build();
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

    // 2XX responses

    /**
     * Respond with a {@code 200 OK} message with the specified media entity.
     *
     * @param media
     *        the media entity
     */
    void ok(Media.Bytes media);

    // 4XX responses

    /**
     * Respond with a {@code 400 Bad Request} message with the specified media
     * entity.
     *
     * @param media
     *        the media entity
     */
    void badRequest(Media.Bytes media);

    /**
     * Respond with a {@code 404 Not Found} message with the specified media
     * entity.
     *
     * @param media
     *        the media entity
     */
    void notFound(Media.Bytes media);

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

    /**
     * Augments a handler.
     */
    @FunctionalInterface
    public interface Interceptor {

      Http.Handler intercept(Http.Handler handler);

    }

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

    static Handler firstOf(Http.Handler h1, Http.Handler h2) {
      Objects.requireNonNull(h1, "h1 == null");
      Objects.requireNonNull(h2, "h2 == null");

      return HttpHandler.many(null, new Handler[] {h1, h2});
    }

    static Handler firstOf(Http.Handler h1, Http.Handler h2, Http.Handler h3) {
      Objects.requireNonNull(h1, "h1 == null");
      Objects.requireNonNull(h2, "h2 == null");
      Objects.requireNonNull(h3, "h3 == null");

      return HttpHandler.many(null, new Handler[] {h1, h2, h3});
    }

    static Handler movedPermanently(String location) {
      Objects.requireNonNull(location, "location == null");

      return HttpHandler.movedPermanently(location);
    }

    static Handler noop() {
      return HttpHandler.NOOP;
    }

    static Handler notFound() {
      return HttpHandler.notFound();
    }

    static Handler ofText(String text, Charset charset) {
      final String charsetName;
      charsetName = charset.name();

      final String contentType;
      contentType = "text/plain; charset=" + charsetName.toLowerCase(Locale.US);

      final byte[] bytes;
      bytes = text.getBytes(charset);

      return HttpHandler.ofContent(contentType, bytes);
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
  public sealed interface HeaderName permits HttpHeaderName, HttpHeaderNameUnknown {

    static HeaderName of(String name) {
      Objects.requireNonNull(name, "name == null");

      HeaderName headerName;
      headerName = HttpHeaderName.findByName(name);

      if (headerName == null) {
        headerName = new HttpHeaderNameUnknown(name);
      }

      return headerName;
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
     * The {@code Way-Requst} header name.
     */
    HeaderName WAY_REQUEST = HttpHeaderName.WAY_REQUEST;

    /**
     * The index of this header name.
     *
     * @return the index of this header name
     */
    int index();

    /**
     * Returns this name with the first letter of each word capitalized.
     *
     * @return this name with the first letter of each word capitalized.
     */
    String capitalized();

  }

  /**
   * The method of an HTTP request message.
   */
  public enum Method {

    /**
     * The CONNECT method.
     */
    CONNECT,

    /**
     * The DELETE method.
     */
    DELETE,

    /**
     * The GET method.
     */
    GET,

    /**
     * The HEAD method.
     */
    HEAD,

    /**
     * The OPTIONS method.
     */
    OPTIONS,

    /**
     * The PATCH method.
     */
    PATCH,

    /**
     * The POST method.
     */
    POST,

    /**
     * The PUT method.
     */
    PUT,

    /**
     * The TRACE method.
     */
    TRACE;

    static final Method[] VALUES = values();

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

    /**
     * Returns a formatted string representation of the headers of this HTTP
     * request message.
     *
     * @return a formatted string representation of the headers of this HTTP
     *         request message.
     */
    String toRequestHeadersText();

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
   * Provides methods for writing the response message of an HTTP exchange.
   */
  public sealed interface Response {

    /**
     * Writes a {@code 200 OK} response message with the contents of the
     * specified media object.
     *
     * @param writer
     *        the media writer
     */
    void respond(Lang.MediaWriter writer);

    /**
     * Writes the specified response message.
     *
     * @param message
     *        the response message
     */
    void respond(ResponseMessage message);

    /**
     * Writes a response message with the specified status and the contents of
     * the specified media object. The specified headers will be
     *
     * @param status
     *        the HTTP response status
     * @param object
     *        the media object
     * @param headers
     *        the additional headers to write
     */
    void respond(Http.Status status, Media.Bytes object, Consumer<ResponseHeaders> headers);

    /**
     * Writes a response message with the specified status and the
     * contents of the specified media writer.
     *
     * @param status
     *        the HTTP response status
     * @param writer
     *        the media writer
     */
    void respond(Http.Status status, Lang.MediaWriter writer);

    /**
     * Writes a response message with the specified status and the
     * contents of the specified media writer.
     *
     * @param status
     *        the HTTP response status
     * @param writer
     *        the media writer
     * @param headers
     *        the additional headers to write
     */
    void respond(Http.Status status, Lang.MediaWriter writer, Consumer<ResponseHeaders> headers);

  }

  /**
   * Provides messages for writing the headers of an HTTP response message.
   */
  public sealed interface ResponseHeaders permits HttpSupport {

    /**
     * Writes an HTTP response header field with the specified name and value.
     *
     * @param name
     *        the header name
     * @param value
     *        the header value
     */
    void header(HeaderName name, long value);

    /**
     * Writes an HTTP response header field with the specified name and value.
     *
     * @param name
     *        the header name
     * @param value
     *        the header value
     */
    void header(HeaderName name, String value);

    /**
     * Writes the {@link HeaderName#DATE} HTTP response header field with the
     * current date and time.
     */
    void dateNow();

  }

  /**
   * Represents an HTTP response message.
   */
  public sealed interface ResponseMessage permits HttpResponseMessage {

    static ResponseMessage found(String location) {
      Objects.requireNonNull(location, "location == null");

      return HttpResponseMessage.found(location);
    }

    static ResponseMessage methodNotAllowed(Method... methods) {
      for (int i = 0; i < methods.length; i++) {
        Check.notNull(methods[i], "methods[", i, "] == null");
      }

      return HttpResponseMessage.methodNotAllowed(methods);
    }

    static ResponseMessage okTextPlain(String text, Charset charset) {
      final Media.Bytes object;
      object = Media.Bytes.textPlain(text, charset);

      return HttpResponseMessage.ok(object);
    }

  }

  /**
   * Configures the routing of an HTTP server.
   */
  public sealed interface Routing permits HttpRouting.Of {

    public sealed interface OfPath permits HttpRouting.OfPath {

      void allow(Method method, Handler handler);

      void filter(Filter value);

      void handler(Handler value);

      void paramDigits(String name);

      void paramNotEmpty(String name);

      void paramRegex(String name, String value);

      void subpath(String path, Consumer<OfPath> routes);

    }

    void handler(Handler value);

    void install(Consumer<Routing> routes);

    void path(String path, Consumer<OfPath> routes);

    void when(Predicate<? super Request> condition, Consumer<Routing> routes);

  }

  /**
   * An HTTP server.
   */
  public sealed interface Server extends Closeable permits HttpServer {

    /**
     * Configures the creation of an HTTP server.
     */
    public sealed interface Config permits HttpServerConfig {

      void bufferSize(int initial, int max);

      void clock(Clock clock);

      void handler(Handler value);

      void noteSink(Note.Sink noteSink);

      void port(int port);

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
     * @param config
     *        configuration options of this new server instance
     *
     * @return a newly created HTTP server instance
     */
    static Server create(Consumer<Config> config) {
      HttpServerConfig builder;
      builder = new HttpServerConfig();

      config.accept(builder);

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
   * Represents an HTTP Set-Cookie response header as defined in RFC 6265.
   * Provides a builder pattern through the {@link Config} interface to
   * construct
   * cookie instances with various attributes.
   *
   * <p>
   * Example usage:
   * <pre>{@code
   * SetCookie cookie = SetCookie.create(config -> {
   *     config.name("session");
   *     config.value("abc123");
   *     config.httpOnly();
   *     config.sameSite(SetCookie.SameSite.STRICT);
   * });
   * }</pre>
   *
   * @see <a href="https://tools.ietf.org/html/rfc6265">RFC 6265 - HTTP State
   *      Management Mechanism</a>
   */
  public sealed interface SetCookie extends Consumer<ResponseHeaders> permits HttpSetCookie {

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
     * @param config a consumer that configures the cookie attributes
     *
     * @return a new SetCookie instance
     *
     * @throws IllegalArgumentException if required attributes (name and value)
     *         are not set
     */
    static SetCookie create(Consumer<Config> config) {
      final HttpSetCookieConfig builder;
      builder = new HttpSetCookieConfig();

      config.accept(builder);

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

    // 3.x.x

    /**
     * The {@code 301 MOVED PERMANENTLY} status.
     */
    Status MOVED_PERMANENTLY = HttpStatus.MOVED_PERMANENTLY;

    /**
     * The {@code 302 FOUND} status.
     */
    Status FOUND = HttpStatus.FOUND;

    /**
     * The {@code 303 SEE OTHER} status.
     */
    Status SEE_OTHER = HttpStatus.SEE_OTHER;

    /**
     * The {@code 304 NOT MODIFIED} status.
     */
    Status NOT_MODIFIED = HttpStatus.NOT_MODIFIED;

    // 4.x.x

    /**
     * The {@code 400 BAD REQUEST} status.
     */
    Status BAD_REQUEST = HttpStatus.BAD_REQUEST;

    /**
     * The {@code 404 Not Found} status.
     */
    Status NOT_FOUND = HttpStatus.NOT_FOUND;

    /**
     * The {@code 405 METHOD NOT ALLOWED} status.
     */
    Status METHOD_NOT_ALLOWED = HttpStatus.METHOD_NOT_ALLOWED;

    /**
     * The {@code 414 URI TOO LONG} status.
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

  /**
   * A test-only HTTP exchange.
   */
  public sealed interface TestingExchange extends Exchange permits HttpTestingExchange {

    /**
     * Configures the creation of a testing exchange instance.
     */
    sealed interface Config permits HttpTestingExchangeConfig {

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
       * Stores the provided key-value pair in the testing exchange.
       *
       * @param key
       *        the key to be stored
       * @param value
       *        the value to be stored
       * @param <T>
       *        the type of the value
       */
      <T> void set(Class<T> key, T value);

    }

    /**
     * Creates an exchange instance suitable for test cases.
     *
     * @param config
     *        configures the exchange instance creation
     *
     * @return a newly created exchange instance with the configured options
     */
    static TestingExchange create(Consumer<Config> config) {
      HttpTestingExchangeConfig builder;
      builder = new HttpTestingExchangeConfig();

      config.accept(builder);

      return builder.build();
    }

    Status responseStatus();

    /**
     * Returns the value of the first response header field line having the
     * specified name; returns {@code null} if the field line is not present.
     *
     * @param name
     *        the name of the response header field line
     *
     * @return the value of first field line or {@code null} if a field line
     *         with the specified name is not present.
     */
    String responseHeader(Http.HeaderName name);

    Object responseBody();

    Charset responseCharset();

    String responseToString();

  }

  // exception types

  public static abstract class AbstractHandlerException extends RuntimeException implements Handler {

    private static final long serialVersionUID = -8277337261280606415L;

  }

  public static final class InternalServerException extends RuntimeException {

    private static final long serialVersionUID = 4192238770922308870L;

    public InternalServerException(Throwable cause) {
      super(cause);
    }

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

  enum Version {

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
        case HTTP_1_0 -> out.append("HTTP/1.0");

        case HTTP_1_1 -> out.append("HTTP/1.1");
      }
    }

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

  static void init() {
    // noop: mostly for testing
  }

}
