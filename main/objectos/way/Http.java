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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import java.util.function.Supplier;
import java.util.regex.Pattern;
import objectos.notes.Note1;
import objectos.notes.NoteSink;
import objectos.way.HttpExchangeLoop.ParseStatus;
import objectos.way.HttpServer.Builder;

/**
 * The <strong>Objectos HTTP</strong> main class.
 */
public final class Http {

  // types

  /**
   * An HTTP request received by the server and its subsequent response to the
   * client.
   *
   * <p>
   * Unless otherwise specified, request-target related methods of this
   * interface return decoded values.
   */
  public sealed interface Exchange extends Request permits HttpExchangeLoop, TestingExchange {

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

    default void accept(Handler handler) {
      handler.handle(this);
    }

    void status(Response.Status status);

    void header(HeaderName name, long value);

    void header(HeaderName name, String value);

    // pre-made headers

    void dateNow();

    // response body

    void send();

    void send(byte[] body);

    void send(Lang.CharWritable body, Charset charset);

    void send(Path file);

    // pre-made responses

    // 200
    default void ok() {
      status(Http.OK);

      dateNow();

      send();
    }

    default void ok(Html.Template template) {
      Html.Compiler html;
      html = Html.createCompiler();

      template.accept(html);

      status(Http.OK);

      dateNow();

      header(Http.CONTENT_TYPE, "text/html; charset=utf-8");

      header(Http.TRANSFER_ENCODING, "chunked");

      send(html, StandardCharsets.UTF_8);
    }

    default void okText(String text, Charset charset) {
      byte[] bytes;
      bytes = text.getBytes(charset); // early implicit null-check

      status(Http.OK);

      dateNow();

      header(Http.CONTENT_TYPE, "text/plain; charset=" + charset.name().toLowerCase(Locale.US));

      header(Http.CONTENT_LENGTH, bytes.length);

      send(bytes);
    }

    // 301
    default void movedPermanently(String location) {
      Check.notNull(location, "location == null");

      status(Http.MOVED_PERMANENTLY);

      dateNow();

      header(Http.LOCATION, location);

      send();
    }

    // 302
    default void found(String location) {
      Check.notNull(location, "location == null");

      status(Http.FOUND);

      dateNow();

      header(Http.LOCATION, location);

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
      status(Http.UNSUPPORTED_MEDIA_TYPE);

      dateNow();

      header(Http.CONNECTION, "close");

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
      status(Http.UNPROCESSABLE_CONTENT);

      dateNow();

      header(Http.CONNECTION, "close");

      send();
    }

    // 500
    void internalServerError(Throwable t);

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
   * An HTTP request message.
   */
  public sealed interface Request extends RequestTarget {

    /**
     * The body of an HTTP request message.
     */
    public interface Body {

      /**
       * An input stream that reads the bytes of this request body.
       *
       * @return an input stream that reads the bytes of this request body.
       *
       * @throws IOException
       *         if an I/O error occurs
       */
      InputStream openStream() throws IOException;

    }

    /**
     * The cookies of an HTTP request message.
     */
    public sealed interface Cookies permits HttpRequestCookies, HttpRequestCookiesEmpty {

      /**
       * Returns the value of the cookie with the specified name; {@code null}
       * if a
       * cookie with the specified name is not present.
       *
       * @param name
       *        the cookie name
       *
       * @return the value or {@code null} if the cookie is not present
       */
      String get(String name);

    }

    /**
     * The header section of an HTTP request message.
     */
    public interface Headers {

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
      String first(Http.HeaderName name);

      /**
       * The number of field lines in this header section.
       *
       * @return the number of field lines in this header section.
       */
      int size();

    }

    /**
     * The method of an HTTP request message.
     */
    public sealed interface Method {

      /**
       * The name of this method.
       *
       * @return the name of this method.
       */
      String name();

    }

    /**
     * The body of this request message.
     *
     * @return the body of this request message
     */
    Body body();

    /**
     * The header section of this request message.
     *
     * @return the header section of this request message
     */
    Headers headers();

    /**
     * The code of the method of this request message.
     *
     * @return the code of the method of this request message
     */
    byte method();

  }

  /**
   * The request-target of an HTTP request message.
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

  }

  /**
   * An HTTP server.
   */
  public interface Server extends Closeable {

    /**
     * Configures the creation of an HTTP server.
     */
    public sealed interface Option permits HttpServerOption {}

    /**
     * Indicates that this server is ready to accept requests.
     */
    Note1<ServerSocket> LISTENING = Note1.info(Http.Server.class, "Listening");

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
   * An HTTP response message.
   */
  public interface Response {

    /**
     * The status of an HTTP response message.
     */
    public sealed interface Status permits HttpResponseStatus {

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
     * Sets the status of this response message.
     *
     * @param status
     *        the status to set
     */
    void status(Status status);

  }

  /**
   * A test-only HTTP exchange.
   */
  public sealed interface TestingExchange extends Exchange {

    /**
     * Configures the creation of a testing exchange instance.
     */
    sealed interface Config {

      /**
       * Sets the request method to the specified value.
       *
       * @param value
       *        the byte value representing the HTTP method
       *
       * @return this config instance
       */
      Config method(byte value);

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

  // HeaderName constants

  /**
   * The {@code Accept-Encoding} header name.
   */
  public static final HeaderName ACCEPT_ENCODING = HttpHeaderName.create("Accept-Encoding", HttpHeaderType.REQUEST);

  /**
   * The {@code Connection} header name.
   */
  public static final HeaderName CONNECTION = HttpHeaderName.create("Connection", HttpHeaderType.BOTH);

  /**
   * The {@code Content-Length} header name.
   */
  public static final HeaderName CONTENT_LENGTH = HttpHeaderName.create("Content-Length", HttpHeaderType.BOTH);

  /**
   * The {@code Content-Type} header name.
   */
  public static final HeaderName CONTENT_TYPE = HttpHeaderName.create("Content-Type", HttpHeaderType.BOTH);

  /**
   * The {@code Cookie} header name.
   */
  public static final HeaderName COOKIE = HttpHeaderName.create("Cookie", HttpHeaderType.REQUEST);

  /**
   * The {@code Date} header name.
   */
  public static final HeaderName DATE = HttpHeaderName.create("Date", HttpHeaderType.BOTH);

  /**
   * The {@code ETag} header name.
   */
  public static final HeaderName ETAG = HttpHeaderName.create("ETag", HttpHeaderType.RESPONSE);

  /**
   * The {@code From} header name.
   */
  public static final HeaderName FROM = HttpHeaderName.create("From", HttpHeaderType.REQUEST);

  /**
   * The {@code Host} header name.
   */
  public static final HeaderName HOST = HttpHeaderName.create("Host", HttpHeaderType.REQUEST);

  /**
   * The {@code If-None-Match} header name.
   */
  public static final HeaderName IF_NONE_MATCH = HttpHeaderName.create("If-None-Match", HttpHeaderType.REQUEST);

  /**
   * The {@code Location} header name.
   */
  public static final HeaderName LOCATION = HttpHeaderName.create("Location", HttpHeaderType.RESPONSE);

  /**
   * The {@code Set-Cookie} header name.
   */
  public static final HeaderName SET_COOKIE = HttpHeaderName.create("Set-Cookie", HttpHeaderType.RESPONSE);

  /**
   * The {@code Transfer-Encoding} header name.
   */
  public static final HeaderName TRANSFER_ENCODING = HttpHeaderName.create("Transfer-Encoding", HttpHeaderType.BOTH);

  /**
   * The {@code User-Agent} header name.
   */
  public static final HeaderName USER_AGENT = HttpHeaderName.createLast("User-Agent", HttpHeaderType.REQUEST);

  // Request constants

  /**
   * The CONNECT method code.
   */
  public static final byte CONNECT = 1;

  /**
   * The DELETE method code.
   */
  public static final byte DELETE = 2;

  /**
   * The GET method code.
   */
  public static final byte GET = 3;

  /**
   * The HEAD method code.
   */
  public static final byte HEAD = 4;

  /**
   * The OPTIONS method code.
   */
  public static final byte OPTIONS = 5;

  /**
   * The PATCH method code.
   */
  public static final byte PATCH = 6;

  /**
   * The POST method code.
   */
  public static final byte POST = 7;

  /**
   * The PUT method code.
   */
  public static final byte PUT = 8;

  /**
   * The TRACE method code.
   */
  public static final byte TRACE = 9;

  static byte checkMethod(byte method) {
    if (method < CONNECT) {
      throw new IllegalArgumentException("The value " + method + " does not represent a valid HTTP method");
    }

    if (method > TRACE) {
      throw new IllegalArgumentException("The value " + method + " does not represent a valid HTTP method");
    }

    return method;
  }

  enum HttpRequestMethod implements Http.Request.Method {

    CONNECT,
    DELETE,
    GET,
    HEAD,
    OPTIONS,
    PATCH,
    POST,
    PUT,
    TRACE;

    static final HttpRequestMethod[] VALUES = values();

  }

  /**
   * Returns an HTTP request method object representing the specified method
   * code.
   *
   * @param code
   *        the method code
   *
   * @return an HTTP request method instance
   *
   * @throws IllegalArgumentException
   *         if the specified code does not represent a valid HTTP method
   */
  public static Http.Request.Method method(byte code) {
    return switch (code) {
      case Http.CONNECT -> HttpRequestMethod.CONNECT;
      case Http.DELETE -> HttpRequestMethod.DELETE;
      case Http.GET -> HttpRequestMethod.GET;
      case Http.HEAD -> HttpRequestMethod.HEAD;
      case Http.OPTIONS -> HttpRequestMethod.OPTIONS;
      case Http.PATCH -> HttpRequestMethod.PATCH;
      case Http.POST -> HttpRequestMethod.POST;
      case Http.PUT -> HttpRequestMethod.PUT;
      case Http.TRACE -> HttpRequestMethod.TRACE;
      default -> throw new IllegalArgumentException("The value " + code + " is not mapped to a Http.Request.Method value");
    };
  }

  // Response constants

  // 2.x.x

  /**
   * The {@code 200 OK} status.
   */
  public static final Response.Status OK = HttpResponseStatus.create(200, "OK");

  // 3.x.x

  /**
   * The {@code 301 MOVED PERMANENTLY} status.
   */
  public static final Response.Status MOVED_PERMANENTLY = HttpResponseStatus.create(301, "MOVED PERMANENTLY");

  /**
   * The {@code 302 FOUND} status.
   */
  public static final Response.Status FOUND = HttpResponseStatus.create(302, "FOUND");

  /**
   * The {@code 303 SEE OTHER} status.
   */
  public static final Response.Status SEE_OTHER = HttpResponseStatus.create(303, "SEE OTHER");

  /**
   * The {@code 304 NOT MODIFIED} status.
   */
  public static final Response.Status NOT_MODIFIED = HttpResponseStatus.create(304, "NOT MODIFIED");

  // 4.x.x

  /**
   * The {@code 400 BAD REQUEST} status.
   */
  public static final Response.Status BAD_REQUEST = HttpResponseStatus.create(400, "BAD REQUEST");

  /**
   * The {@code 404 NOT FOUND} status.
   */
  public static final Response.Status NOT_FOUND = HttpResponseStatus.create(404, "NOT FOUND");

  /**
   * The {@code 405 METHOD NOT ALLOWED} status.
   */
  public static final Response.Status METHOD_NOT_ALLOWED = HttpResponseStatus.create(405, "METHOD NOT ALLOWED");

  /**
   * The {@code 414 URI TOO LONG} status.
   */
  public static final Response.Status URI_TOO_LONG = HttpResponseStatus.create(414, "URI TOO LONG");

  /**
   * The {@code 415 UNSUPPORTED MEDIA TYPE} status.
   */
  public static final Response.Status UNSUPPORTED_MEDIA_TYPE = HttpResponseStatus.create(415, "UNSUPPORTED MEDIA TYPE");

  /**
   * The {@code 422 UNPROCESSABLE CONTENT} status.
   */
  public static final Response.Status UNPROCESSABLE_CONTENT = HttpResponseStatus.create(422, "UNPROCESSABLE CONTENT");

  // 5.x.x

  /**
   * The {@code 500 INTERNAL SERVER ERROR} status.
   */
  public static final Response.Status INTERNAL_SERVER_ERROR = HttpResponseStatus.create(500, "INTERNAL SERVER ERROR");

  /**
   * The {@code 501 NOT IMPLEMENTED} status.
   */
  public static final Response.Status NOT_IMPLEMENTED = HttpResponseStatus.create(501, "NOT IMPLEMENTED");

  /**
   * The {@code 505 HTTP VERSION NOT SUPPORTED} status.
   */
  public static final Response.Status HTTP_VERSION_NOT_SUPPORTED = HttpResponseStatus.createLast(505, "HTTP VERSION NOT SUPPORTED");

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

  public static HeaderName createHeaderName(String name) {
    Check.notNull(name, "name == null");

    HeaderName headerName;
    headerName = HttpHeaderName.findByName(name);

    if (headerName == null) {
      headerName = new HttpHeaderName(name);
    }

    return headerName;
  }

  /**
   * Creates a new HTTP server instance with the specified handler provider and
   * the specified options.
   *
   * @param handlerFactory
   *        the handler provider of this new server instance
   * @param options
   *        configuration options of this new server instance
   *
   * @return a newly created HTTP server instance
   */
  public static Server createServer(HandlerFactory handlerFactory, Server.Option... options) {
    Check.notNull(handlerFactory, "handlerFactory == null");
    Check.notNull(options, "options == null");

    HttpServer.Builder builder;
    builder = new HttpServer.Builder(handlerFactory);

    for (int i = 0; i < options.length; i++) {
      Server.Option option;
      option = Check.notNull(options[i], "options[", i, "] == null");

      // the cast is safe as Server.Option is sealed
      HttpServerOption actual;
      actual = (HttpServerOption) option;

      actual.acceptHttpServerBuilder(builder);
    }

    return builder.build();
  }

  public static Server.Option bufferSize(int initial, int max) {
    Check.argument(initial >= 128, "initial size must be >= 128");
    Check.argument(max >= 128, "max size must be >= 128");
    Check.argument(max >= initial, "max size must be >= initial size");

    return new HttpServerOption() {
      @Override
      final void acceptHttpServerBuilder(Builder builder) {
        builder.bufferSizeInitial = initial;

        builder.bufferSizeMax = max;
      }
    };
  }

  public static Server.Option clock(Clock clock) {
    Check.notNull(clock, "clock == null");

    return new HttpServerOption() {
      @Override
      final void acceptHttpServerBuilder(Builder builder) {
        builder.clock = clock;
      }
    };
  }

  public static Server.Option noteSink(NoteSink noteSink) {
    Check.notNull(noteSink, "noteSink == null");

    return new HttpServerOption() {
      @Override
      final void acceptHttpServerBuilder(Builder builder) {
        builder.noteSink = noteSink;
      }
    };
  }

  public static Server.Option port(int port) {
    if (port < 0 || port > 0xFFFF) {
      throw new IllegalArgumentException("port out of range:" + port);
    }

    return new HttpServerOption() {
      @Override
      final void acceptHttpServerBuilder(Builder builder) {
        builder.port = port;
      }
    };
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

  public static Request.Cookies parseCookies(String s) {
    Check.notNull(s, "s == null");

    if (s.isBlank()) {
      return HttpRequestCookiesEmpty.INSTANCE;
    }

    HttpRequestCookiesParser parser;
    parser = new HttpRequestCookiesParser(s);

    return parser.parse();
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
  public static FormUrlEncoded parseFormUrlEncoded(Http.Request.Body body) throws IOException {
    return HttpFormUrlEncoded.parse(body);
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
  public static FormUrlEncoded parseFormUrlEncoded(Http.Exchange http) throws IOException, UnsupportedMediaTypeException {
    return HttpFormUrlEncoded.parse(http);
  }

  /**
   * Parses the specified string into a new request-target instance.
   *
   * @param target
   *        the raw (undecoded) request-target value
   *
   * @return a new request target instance
   *
   * @throws IllegalArgumentException
   *         if the string represents an invalid request-target value
   */
  static RequestTarget parseRequestTarget(String target) throws IllegalArgumentException {
    Check.notNull(target, "target == null");

    HttpRequestLine requestLine;
    requestLine = new HttpRequestLine();

    // append a line terminator
    target = target + " \r\n";

    byte[] bytes;
    bytes = target.getBytes(StandardCharsets.UTF_8);

    // in-memory stream... no closing needed...
    InputStream inputStream;
    inputStream = new ByteArrayInputStream(bytes);

    requestLine.initSocketInput(inputStream);

    try {
      requestLine.parseLine();

      requestLine.parseRequestTarget();
    } catch (IOException e) {
      throw new AssertionError("In-memory stream does not throw IOException", e);
    }

    ParseStatus parseStatus;
    parseStatus = requestLine.parseStatus;

    if (parseStatus.isError()) {
      throw new IllegalArgumentException(parseStatus.name());
    }

    return requestLine;
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

abstract class HttpModule {

  // user types

  protected sealed static abstract class Condition permits HttpModuleCondition {

    final String name;

    Condition(String name) {
      this.name = name;
    }

    final boolean test(HttpRequestLine path) {
      String value;
      value = path.pathParam(name);

      if (value == null) {
        return true;
      } else {
        return test(value);
      }
    }

    abstract boolean test(String value);

  }

  protected sealed interface RouteOption {}

  // internal types

  private sealed interface ThisRouteOption extends RouteOption {
    void accept(HttpModuleCompiler compiler);
  }

  private record HandlerOption(Http.Handler instance) implements ThisRouteOption {
    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(instance);
    }
  }

  private record HandlerFactory0Option(Supplier<Http.Handler> factory) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = factory.get();

      handler.handle(http);
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record HandlerFactory1Option<T>(Function<T, Http.Handler> function, T value) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      Http.Handler handler;
      handler = function.apply(value);

      handler.handle(http);
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record MovedPermanentlyOption(String location) implements Http.Handler, ThisRouteOption {
    @Override
    public final void handle(Http.Exchange http) {
      http.status(Http.MOVED_PERMANENTLY);

      http.dateNow();

      http.header(Http.LOCATION, location);

      http.send();
    }

    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.handleWith(this);
    }
  }

  private record PathParametersOption(Condition[] conditions) implements ThisRouteOption {
    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.pathParams(conditions);
    }
  }

  // fields

  private HttpModuleCompiler compiler;

  protected HttpModule() {}

  /**
   * Generates a handler instance based on the configuration of this module.
   *
   * @return a configured handler instance
   */
  public final Http.Handler compile() {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = new HttpModuleCompiler();

      configure();

      return compiler.compile();
    } finally {
      compiler = null;
    }
  }

  protected abstract void configure();

  protected final void install(Http.Module module) {
    Check.notNull(module, "module == null");

    module.acceptHttpModuleCompiler(compiler);
  }

  final void acceptHttpModuleCompiler(HttpModuleCompiler _compiler) {
    Check.state(compiler == null, "Another compilation is already in progress");

    try {
      compiler = _compiler;

      configure();
    } finally {
      compiler = null;
    }
  }

  protected final void filter(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    compiler.filter(handler);
  }

  /**
   * If the {@code Host} request header value is equal to the specified name
   * then dispatches to the specified module.
   *
   * @param name
   *        the host name
   * @param module
   *        the module to dispatch to
   */
  protected final void host(String name, Http.Module module) {
    Check.notNull(name, "name == null");

    Http.Handler handler;
    handler = module.compile();

    compiler.host(name, handler);
  }

  /**
   * If the {@code Host} request header value is equal to the specified name
   * then dispatches to the specified handler.
   *
   * <p>
   * Please note that if the specified handler is not exhaustive then this
   * module might dispatch the request to any existing subsequent handler.
   *
   * @param name
   *        the host name
   * @param handler
   *        the handler to dispatch to
   */
  protected final void host(String name, Http.Handler handler) {
    Check.notNull(name, "name == null");

    compiler.host(name, handler);
  }

  /**
   * Intercepts all matched routes with the specified interceptor.
   *
   * @param interceptor
   *        the interceptor to use
   */
  protected final void interceptMatched(Http.Handler.Interceptor interceptor) {
    Check.notNull(interceptor, "interceptor == null");

    compiler.interceptor(interceptor);
  }

  // route

  protected final void route(String pathExpression, RouteOption... options) {
    Check.notNull(pathExpression, "pathExpression == null");

    compiler.routeStart(pathExpression);

    for (int idx = 0; idx < options.length; idx++) {
      RouteOption o;
      o = Check.notNull(options[idx], "options[", idx, "] == null");

      ThisRouteOption option;
      option = (ThisRouteOption) o;

      option.accept(compiler);
    }

    compiler.routeEnd();
  }

  // route options

  protected final RouteOption handler(Http.Handler handler) {
    Check.notNull(handler, "handler == null");

    return new HandlerOption(handler);
  }

  protected final RouteOption handlerFactory(Supplier<Http.Handler> factory) {
    Check.notNull(factory, "factory == null");

    return new HandlerFactory0Option(factory);
  }

  protected final <T> RouteOption handlerFactory(Function<T, Http.Handler> factory, T value) {
    Check.notNull(factory, "factory == null");

    return new HandlerFactory1Option<>(factory, value);
  }

  private record InterceptorOption(Http.Handler.Interceptor interceptor) implements ThisRouteOption {
    @Override
    public final void accept(HttpModuleCompiler compiler) {
      compiler.interceptWith(interceptor);
    }
  }

  protected final RouteOption interceptor(Http.Handler.Interceptor interceptor) {
    Objects.requireNonNull(interceptor, "interceptor == null");

    return new InterceptorOption(interceptor);
  }

  protected final RouteOption movedPermanently(String location) {
    Check.notNull(location, "location == null");

    return new MovedPermanentlyOption(location);
  }

  protected final RouteOption pathParams(Condition... conditions) {
    Condition[] copy;
    copy = new Condition[conditions.length];

    for (int i = 0; i < conditions.length; i++) {
      copy[i] = Check.notNull(conditions[i], "conditions[", i, "] == null");
    }

    return new PathParametersOption(copy);
  }

  //

  protected final Condition digits(String name) {
    Check.notNull(name, "name == null");

    return new HttpModuleCondition.Digits(name);
  }

  protected final Condition notEmpty(String name) {
    Check.notNull(name, "name == null");

    return new HttpModuleCondition.NotEmpty(name);
  }

  protected final Condition regex(String name, String regex) {
    Check.notNull(name, "name == null");
    Check.notNull(regex, "regex == null");

    Pattern pattern;
    pattern = Pattern.compile(regex);

    return new HttpModuleCondition.Regex(name, pattern);
  }

}

non-sealed class HttpRequestLine extends HttpSocketInput implements Http.RequestTarget {

  // force Http class init
  static final byte HTTP_REQUEST_LINE = Http.GET;

  byte method;

  private String path;

  private int pathLimit;

  Map<String, String> pathParams;

  private Map<String, Object> queryParams;

  private boolean queryParamsReady;

  private int queryStart;

  private String rawValue;

  byte versionMajor;

  byte versionMinor;

  HttpRequestLine() {}

  @Override
  public final String path() {
    if (path == null) {
      String raw;
      raw = rawPath();

      path = decode(raw);
    }

    return path;
  }

  private String decode(String raw) {
    return URLDecoder.decode(raw, StandardCharsets.UTF_8);
  }

  @Override
  public final String pathParam(String name) {
    Check.notNull(name, "name == null");

    String result;
    result = null;

    if (pathParams != null) {
      result = pathParams.get(name);
    }

    return result;
  }

  @Override
  public final String queryParam(String name) {
    Check.notNull(name, "name == null");

    Map<String, Object> params;
    params = $queryParams();

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

  @Override
  public final Set<String> queryParamNames() {
    Map<String, Object> params;
    params = $queryParams();

    return params.keySet();
  }

  private Map<String, Object> $queryParams() {
    if (!queryParamsReady) {
      if (queryParams == null) {
        queryParams = Util.createMap();
      }

      makeQueryParams(queryParams, this::decode);

      queryParamsReady = true;
    }

    return queryParams;
  }

  @Override
  public final String rawPath() {
    return rawValue.substring(0, pathLimit);
  }

  @Override
  public final String rawQuery() {
    return queryStart == pathLimit ? null : rawValue.substring(queryStart);
  }

  private Map<String, Object> $rawQueryParams() {
    Map<String, Object> map;
    map = Util.createMap();

    makeQueryParams(map, Function.identity());

    return map;
  }

  public final String rawValue() {
    return rawValue;
  }

  public final String rawValue(String queryParamName, String queryParamValue) {
    Check.notNull(queryParamName, "queryParamName == null");
    Check.notNull(queryParamValue, "queryParamValue == null");

    StringBuilder builder;
    builder = new StringBuilder(rawPath());

    builder.append('?');

    Map<String, Object> params;
    params = $rawQueryParams();

    String rawName;
    rawName = encode(queryParamName);

    String rawValue;
    rawValue = encode(queryParamValue);

    params.put(rawName, rawValue);

    int count;
    count = 0;

    for (String key : params.keySet()) {
      if (count++ > 0) {
        builder.append('&');
      }

      builder.append(key);

      builder.append('=');

      Object value;
      value = params.get(key);

      if (value instanceof String s) {
        builder.append(s);
      }

      else {
        throw new UnsupportedOperationException("Implement me");
      }
    }

    return builder.toString();
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

  final void resetRequestLine() {
    method = 0;

    pathLimit = 0;

    if (pathParams != null) {
      pathParams.clear();
    }

    path = null;

    if (queryParams != null) {
      queryParams.clear();
    }

    queryParamsReady = false;

    queryStart = 0;

    rawValue = null;

    versionMajor = versionMinor = 0;
  }

  final void parseRequestLine() throws IOException {
    parseLine();

    if (parseStatus == ParseStatus.UNEXPECTED_EOF) {
      if (bufferLimit == 0) {
        // buffer is empty, this is an expected EOF
        parseStatus = ParseStatus.EOF;
      }

      return;
    }

    parseMethod();

    if (method == 0) {
      // parse method failed -> bad request
      parseStatus = ParseStatus.INVALID_METHOD;

      return;
    }

    parseRequestTarget();

    parseVersion();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    if (!consumeIfEndOfLine()) {
      parseStatus = ParseStatus.INVALID_REQUEST_LINE_TERMINATOR;

      return;
    }
  }

  final void parseRequestTarget() throws IOException {
    int startIndex;
    startIndex = parsePathStart();

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }

    parsePathRest(startIndex);

    if (parseStatus.isError()) {
      // bad request -> fail
      return;
    }
  }

  private static final byte[] _CONNECT = "CONNECT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _DELETE = "DELETE ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _GET = "GET ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _HEAD = "HEAD ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _OPTIONS = "OPTIONS ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _POST = "POST ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PUT = "PUT ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _PATCH = "PATCH ".getBytes(StandardCharsets.UTF_8);

  private static final byte[] _TRACE = "TRACE ".getBytes(StandardCharsets.UTF_8);

  private void parseMethod() throws IOException {
    if (bufferIndex >= lineLimit) {
      // empty line... nothing to do
      return;
    }

    byte first;
    first = buffer[bufferIndex];

    // based on the first char, we select out method candidate

    switch (first) {
      case 'C' -> parseMethod0(Http.CONNECT, _CONNECT);

      case 'D' -> parseMethod0(Http.DELETE, _DELETE);

      case 'G' -> parseMethod0(Http.GET, _GET);

      case 'H' -> parseMethod0(Http.HEAD, _HEAD);

      case 'O' -> parseMethod0(Http.OPTIONS, _OPTIONS);

      case 'P' -> parseMethodP();

      case 'T' -> parseMethod0(Http.TRACE, _TRACE);
    }
  }

  private void parseMethod0(byte candidate, byte[] candidateBytes) throws IOException {
    if (matches(candidateBytes)) {
      method = candidate;
    }
  }

  private void parseMethodP() throws IOException {
    // method starts with a P. It might be:
    // - POST
    // - PUT
    // - PATCH

    // we'll try them in sequence

    parseMethod0(Http.POST, _POST);

    if (method != 0) {
      return;
    }

    parseMethod0(Http.PUT, _PUT);

    if (method != 0) {
      return;
    }

    parseMethod0(Http.PATCH, _PATCH);

    if (method != 0) {
      return;
    }
  }

  private int parsePathStart() throws IOException {
    // we will check if the request target starts with a '/' char

    int targetStart;
    targetStart = bufferIndex;

    if (bufferIndex >= lineLimit) {
      // reached EOL -> bad request
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    byte b;
    b = buffer[bufferIndex++];

    if (b != Bytes.SOLIDUS) {
      // first char IS NOT '/' => BAD_REQUEST
      parseStatus = ParseStatus.INVALID_TARGET;

      return 0;
    }

    // mark request path start

    return targetStart;
  }

  private void parsePathRest(int startIndex) throws IOException {
    // we will look for the first:
    // - ? char
    // - SP char
    int index;
    index = indexOf(Bytes.QUESTION_MARK, Bytes.SP);

    if (index < 0) {
      // trailing char was not found
      parseStatus = ParseStatus.URI_TOO_LONG;

      return;
    }

    // index where path ends
    int pathEndIndex;
    pathEndIndex = index;

    // as of now target ends at the path
    int targetEndIndex;
    targetEndIndex = pathEndIndex;

    // as of now query starts and ends at path i.e. len = 0
    int queryStartIndex;
    queryStartIndex = pathEndIndex;

    // we'll continue at the '?' or SP char
    bufferIndex = index;

    byte b;
    b = buffer[bufferIndex++];

    if (b == Bytes.QUESTION_MARK) {
      queryStartIndex = bufferIndex;

      targetEndIndex = indexOf(Bytes.SP);

      if (targetEndIndex < 0) {
        // trailing char was not found
        parseStatus = ParseStatus.URI_TOO_LONG;

        return;
      }

      // we'll continue immediately after the SP
      bufferIndex = targetEndIndex + 1;
    }

    rawValue = bufferToString(startIndex, targetEndIndex);

    pathLimit = pathEndIndex - startIndex;

    queryStart = queryStartIndex - startIndex;
  }

  static final byte[] HTTP_VERSION_PREFIX = {'H', 'T', 'T', 'P', '/'};

  private void parseVersion() {
    // 'H' 'T' 'T' 'P' '/' '1' '.' '1' = 8 bytes

    if (!matches(HTTP_VERSION_PREFIX)) {
      // buffer does not start with 'HTTP/'
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    // check if we  have '1' '.' '1' = 3 bytes

    int requiredIndex;
    requiredIndex = bufferIndex + 3 - 1;

    if (requiredIndex >= lineLimit) {
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMajor;
    maybeMajor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMajor)) {
      // major version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeDot;
    maybeDot = buffer[bufferIndex++];

    if (maybeDot != '.') {
      // major version not followed by a DOT => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    byte maybeMinor;
    maybeMinor = buffer[bufferIndex++];

    if (!Http.isDigit(maybeMinor)) {
      // minor version is not a digit => bad request
      parseStatus = ParseStatus.INVALID_PROTOCOL;

      return;
    }

    versionMajor = (byte) (maybeMajor - 0x30);

    versionMinor = (byte) (maybeMinor - 0x30);
  }

  // matcher methods

  private int matcherIndex;

  final void matcherReset() {
    matcherIndex = 0;

    if (pathParams != null) {
      pathParams.clear();
    }
  }

  final boolean atEnd() {
    return matcherIndex == pathLimit;
  }

  final boolean exact(String other) {
    String value;
    value = path();

    boolean result;
    result = value.equals(other);

    matcherIndex += value.length();

    return result;
  }

  final boolean namedVariable(String name) {
    String value;
    value = path();

    int solidus;
    solidus = value.indexOf('/', matcherIndex);

    String varValue;

    if (solidus < 0) {
      varValue = value.substring(matcherIndex);
    } else {
      varValue = value.substring(matcherIndex, solidus);
    }

    matcherIndex += varValue.length();

    variable(name, varValue);

    return true;
  }

  final boolean region(String region) {
    String value;
    value = path();

    boolean result;
    result = value.regionMatches(matcherIndex, region, 0, region.length());

    matcherIndex += region.length();

    return result;
  }

  final boolean startsWithMatcher(String prefix) {
    String value;
    value = path();

    boolean result;
    result = value.startsWith(prefix);

    matcherIndex += prefix.length();

    return result;
  }

  private void variable(String name, String value) {
    if (pathParams == null) {
      pathParams = Util.createMap();
    }

    pathParams.put(name, value);
  }

  // query param stuff

  private void makeQueryParams(Map<String, Object> map, Function<String, String> decoder) {
    int queryLength;
    queryLength = rawValue.length() - queryStart;

    if (queryLength < 2) {
      // query is empty: either "" or "?"
      return;
    }

    String source;
    source = rawQuery();

    StringBuilder sb;
    sb = new StringBuilder();

    String key;
    key = null;

    for (int i = 0, len = source.length(); i < len; i++) {
      char c;
      c = source.charAt(i);

      switch (c) {
        case '=' -> {
          key = sb.toString();

          sb.setLength(0);

          putQueryParams(map, decoder, key, "");
        }

        case '&' -> {
          String value;
          value = sb.toString();

          sb.setLength(0);

          if (key == null) {
            putQueryParams(map, decoder, value, "");

            continue;
          }

          putQueryParams(map, decoder, key, value);

          key = null;
        }

        default -> sb.append(c);
      }
    }

    String value;
    value = sb.toString();

    if (key != null) {
      putQueryParams(map, decoder, key, value);
    } else {
      putQueryParams(map, decoder, value, "");
    }
  }

  @SuppressWarnings("unchecked")
  private void putQueryParams(Map<String, Object> map, Function<String, String> decoder, String rawKey, String rawValue) {
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

}

final class HttpTestingExchange implements Http.TestingExchange {

  private Map<Object, Object> objectStore;

  private final byte method;

  private final String path;

  private final Map<String, String> queryParams;

  HttpTestingExchange(HttpTestingExchangeConfig config) {
    objectStore = config.objectStore;

    method = config.method;

    path = config.path;

    queryParams = config.queryParams;
  }

  // request methods

  @Override
  public final Http.Request.Body body() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Http.Request.Headers headers() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final byte method() {
    return Http.checkMethod(method);
  }

  @Override
  public final String path() {
    Check.state(path != null, "path was not set");

    return path;
  }

  @Override
  public final String pathParam(String name) {
    if (objectStore == null) {
      return null;
    } else {
      return (String) objectStore.get(name);
    }
  }

  @Override
  public final String queryParam(String name) {
    if (queryParams == null) {
      return null;
    } else {
      return queryParams.get(name);
    }
  }

  @Override
  public final Set<String> queryParamNames() {
    if (queryParams == null) {
      return Set.of();
    } else {
      return queryParams.keySet();
    }
  }

  @Override
  public final String rawPath() {
    return URLEncoder.encode(path(), StandardCharsets.UTF_8);
  }

  @Override
  public final String rawQuery() {
    throw new IllegalStateException("query was not set");
  }

  @Override
  public final <T> void set(Class<T> key, T value) {
    Check.notNull(key, "key == null");
    Check.notNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(key, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final <T> T get(Class<T> key) {
    if (objectStore == null) {
      return null;
    } else {
      return (T) objectStore.get(key);
    }
  }

  @Override
  public void header(Http.HeaderName name, long value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void header(Http.HeaderName name, String value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void dateNow() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(byte[] body) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(Lang.CharWritable body, Charset charset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void send(Path file) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void notFound() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void methodNotAllowed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void internalServerError(Throwable t) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean processed() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void status(Http.Response.Status value) {
    throw new UnsupportedOperationException();
  }

}

final class HttpTestingExchangeConfig implements Http.TestingExchange.Config {

  Map<Object, Object> objectStore;

  byte method;

  String path;

  Map<String, String> queryParams;

  public final Http.TestingExchange build() {
    return new HttpTestingExchange(this);
  }

  @Override
  public final Http.TestingExchange.Config method(byte value) {
    method = Http.checkMethod(value);

    return this;
  }

  @Override
  public final Http.TestingExchange.Config path(String value) {
    path = Objects.requireNonNull(value, "value == null");

    return this;
  }

  @Override
  public final Http.TestingExchange.Config pathParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(name, value);

    return this;
  }

  @Override
  public final Http.TestingExchange.Config queryParam(String name, String value) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(value, "value == null");

    if (queryParams == null) {
      queryParams = Util.createMap();
    }

    queryParams.put(name, value);

    return this;
  }

  @Override
  public final <T> Http.TestingExchange.Config set(Class<T> key, T value) {
    Objects.requireNonNull(key, "key == null");
    Objects.requireNonNull(value, "value == null");

    if (objectStore == null) {
      objectStore = Util.createMap();
    }

    objectStore.put(key, value);

    return this;
  }

}