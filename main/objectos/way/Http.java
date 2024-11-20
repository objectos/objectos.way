/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Clock;
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
      extends RequestLine, RequestTarget, RequestHeaders, RequestBody
      permits HttpExchange, TestingExchange {

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

    // response

    /**
     * Begins the HTTP response by writing out the specified response status.
     *
     * @param status
     *        the HTTP response status
     */
    void status(Status status);

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

    // response body

    /**
     * Writes the end of this HTTP response message with an empty message body.
     */
    void send();

    /**
     * Writes the end of this HTTP response message with the specified message
     * body.
     *
     * @param body
     *        an array of bytes with the message body contents
     */
    void send(byte[] body);

    /**
     * Writes the end of this HTTP response message with the contents of the
     * specified file as message body.
     *
     * @param file
     *        the message body contents
     */
    void send(Path file);

    // pre-made responses

    /**
     * Sends an HTTP {@code 200 OK} response with no response body.
     */
    void ok();

    /**
     * Sends an HTTP {@code 200 OK} response with the specified media object as
     * the response body.
     *
     * @param object
     *        the object providing the raw data to be sent over the wire
     */
    void ok(Lang.MediaObject object);

    default void okText(String text, Charset charset) {
      byte[] bytes;
      bytes = text.getBytes(charset); // early implicit null-check

      status(Http.Status.OK);

      dateNow();

      header(Http.HeaderName.CONTENT_TYPE, "text/plain; charset=" + charset.name().toLowerCase(Locale.US));

      header(Http.HeaderName.CONTENT_LENGTH, bytes.length);

      send(bytes);
    }

    // 301
    default void movedPermanently(String location) {
      Check.notNull(location, "location == null");

      status(Http.Status.MOVED_PERMANENTLY);

      dateNow();

      header(Http.HeaderName.LOCATION, location);

      send();
    }

    // 302
    default void found(String location) {
      Check.notNull(location, "location == null");

      status(Http.Status.FOUND);

      dateNow();

      header(Http.HeaderName.LOCATION, location);

      send();
    }

    // 404
    void notFound();

    // 405
    void methodNotAllowed();

    /**
     * Sends a pre-made 415 Unsupported Media Type response.
     *
     * <p>
     * The response is equivalent to:
     *
     * <pre>
     * ServerExchange http = ...
     * http.status(Status.UNSUPPORTED_MEDIA_TYPE);
     * http.dateNow();
     * http.header(HeaderName.CONNECTION, "close");
     * http.send();</pre>
     */
    // 415
    default void unsupportedMediaType() {
      status(Http.Status.UNSUPPORTED_MEDIA_TYPE);

      dateNow();

      header(Http.HeaderName.CONNECTION, "close");

      send();
    }

    /**
     * Sends a pre-made 422 Unprocessable Content response.
     *
     * <p>
     * The response is equivalent to:
     *
     * <pre>
     * ServerExchange http = ...
     * http.status(Status.UNPROCESSABLE_CONTENT);
     * http.dateNow();
     * http.header(HeaderName.CONNECTION, "close");
     * http.send();</pre>
     */
    // 422
    default void unprocessableContent() {
      status(Http.Status.UNPROCESSABLE_CONTENT);

      dateNow();

      header(Http.HeaderName.CONNECTION, "close");

      send();
    }

    // 500
    void internalServerError(Throwable t);

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
   * The parsed and decoded body of a {@code application/x-www-form-urlencoded}
   * HTTP message.
   */
  public interface FormUrlEncoded {

    /**
     * Returns the keys.
     */
    Set<String> names();

    /**
     * Returns the first decoded value associated to the specified key or
     * {@code null} if the key is not present.
     *
     * @param key
     *        the key to search for
     *
     * @return the first decoded value or {@code null}
     */
    String get(String key);

    /**
     * Returns the first decoded value associated to the specified key or
     * the specified {@code defaultValue} if the key is not present.
     *
     * @param key
     *        the key to search for
     * @param defaultValue
     *        the value to return if the key is not present
     *
     * @return the first decoded value or the {@code defaultValue}
     */
    String getOrDefault(String key, String defaultValue);

    /**
     * Returns the number of distinct keys
     *
     * @return the number of distinct keys
     */
    int size();

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
   * The HTTP handler factory of an HTTP server.
   */
  @FunctionalInterface
  public interface HandlerFactory {

    Http.Handler create() throws Exception;

  }

  /**
   * An HTTP header name.
   */
  public sealed interface HeaderName permits HttpHeaderName {

    /**
     * The {@code Accept-Encoding} header name.
     */
    HeaderName ACCEPT_ENCODING = HttpHeaderName.ACCEPT_ENCODING;

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

    static HeaderName create(String name) {
      Objects.requireNonNull(name, "name == null");

      HeaderName headerName;
      headerName = HttpHeaderName.findByName(name);

      if (headerName == null) {
        headerName = new HttpHeaderName(name);
      }

      return headerName;
    }

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
   * A module configures the handlers a server instance will use to process its
   * requests.
   */
  public static abstract class Module extends HttpModule {

    /**
     * Sole constructor.
     */
    protected Module() {}

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
     * The names of all of the query parameters in this request-target.
     *
     * @return the names of all of the query parameters
     */
    Set<String> queryParamNames();

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
     * <h3>Usage Example</h3>
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
   * An HTTP server.
   */
  public sealed interface Server extends Closeable permits HttpServer {

    /**
     * Configures the creation of an HTTP server.
     */
    public sealed interface Config permits HttpServerConfig {

      Config bufferSize(int initial, int max);

      Config clock(Clock clock);

      Config handlerFactory(HandlerFactory factory);

      Config noteSink(Note.Sink noteSink);

      Config port(int port);

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
      Note.Ref1<ServerSocket> started();

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
     * The {@code 404 NOT FOUND} status.
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
       *
       * @return this config instance
       */
      Config clock(Clock value);

      /**
       * Sets the request method to the specified value.
       *
       * @param value
       *        the HTTP method
       *
       * @return this config instance
       */
      Config method(Http.Method value);

      /**
       * Sets the path component of the request-target to the specified value.
       *
       * @param value
       *        the decoded path value
       *
       * @return this config instance
       */
      Config path(String value);

      /**
       * Sets the request-path parameter with the specified name to the
       * specified value.
       *
       * @param name
       *        the name of the path parameter
       * @param value
       *        the decoded value of the path parameter
       *
       * @return this config instance
       */
      Config pathParam(String name, String value);

      /**
       * Sets the request-target query parameter with the specified name to the
       * specified value.
       *
       * @param name
       *        the name of the query parameter
       * @param value
       *        the decoded value of the query parameter
       *
       * @return this config instance
       */
      Config queryParam(String name, String value);

      /**
       * Stores the provided key-value pair in the testing exchange.
       *
       * @param key
       *        the key to be stored
       * @param value
       *        the value to be stored
       *
       * @return this config instance
       */
      <T> Config set(Class<T> key, T value);

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

    Object responseBody();

    Charset responseCharset();

  }

  // exception types

  public static abstract class AbstractHandlerException extends RuntimeException implements Handler {

    private static final long serialVersionUID = -8277337261280606415L;

  }

  /**
   * Thrown to indicate that a content type is not supported.
   */
  public static class UnsupportedMediaTypeException extends Exception {

    private static final long serialVersionUID = -6412173093510319276L;

    /**
     * Creates a new {@code UnsupportedMediaTypeException} with the specified
     * content type name.
     *
     * @param contentType
     *        the name of the content type such as {@code application/pdf} or
     *        {@code image/gif}.
     */
    public UnsupportedMediaTypeException(String contentType) {
      super(contentType);
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

  }

  private Http() {}

  private static final class SimpleHandlerFactory implements Http.HandlerFactory {

    private final Http.Handler handler;

    public SimpleHandlerFactory(Handler handler) {
      this.handler = handler;
    }

    @Override
    public final Handler create() {
      return handler;
    }

  }

  public static HandlerFactory createHandlerFactory(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    return new SimpleHandlerFactory(handler);
  }

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

  /**
   * Parse the specified body as if it is the body of a
   * {@code application/x-www-form-urlencoded} HTTP message.
   *
   * @param body
   *        the body of the HTTP message to parse
   *
   * @throws IOException
   *         if an I/O error occurs while reading the body
   */
  public static FormUrlEncoded parseFormUrlEncoded(Http.RequestBody body) throws IOException {
    return HttpFormUrlEncoded.parse(body);
  }

  /**
   * Parse the specified body as if it is the body of a
   * {@code application/x-www-form-urlencoded} HTTP message.
   *
   * @param http
   *        the HTTP exchange to parse
   *
   * @throws IOException
   *         if an I/O error occurs while reading the body
   */
  public static FormUrlEncoded parseFormUrlEncoded(Http.Exchange http) throws IOException, UnsupportedMediaTypeException {
    return HttpFormUrlEncoded.parse(http);
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

    if (oldValue.equals("")) {
      return;
    }

    if (oldValue instanceof String s) {

      if (value.equals("")) {
        map.put(key, s);
      } else {
        List<String> list;
        list = Util.createList();

        list.add(s);

        list.add(value);

        map.put(key, list);
      }

    }

    else {
      List<String> list;
      list = (List<String>) oldValue;

      list.add(value);

      map.put(key, list);
    }
  }

  static String queryParamsGet(Map<String, Object> params, String name) {
    Object maybe;
    maybe = params.get(name);

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

  static int headerNameSize() {
    return HttpHeaderName.standardNamesSize();
  }

  static void init() {
    // noop: mostly for testing
  }

}
