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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;
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

    // request

    /**
     * Returns the session associated with this request or {@code null} if no
     * session was found.
     *
     * @return the session associated with this request or {@code null}
     */
    Web.Session session();

    // response

    default void accept(Handler handler) {
      handler.handle(this);
    }

    void acceptSessionStore(SessionStore sessionStore);

    default void methodMatrix(Request.Method method, Handler handler) {
      Check.notNull(method, "method == null");
      Check.notNull(handler, "handler == null");

      Request.Method actual;
      actual = method();

      if (handles(method, actual)) {
        handler.handle(this);
      } else {
        methodNotAllowed();
      }
    }

    default void methodMatrix(Request.Method method1, Handler handler1,
        Request.Method method2, Handler handler2) {
      Check.notNull(method1, "method1 == null");
      Check.notNull(handler1, "handler1 == null");
      Check.notNull(method2, "method2 == null");
      Check.notNull(handler2, "handler2 == null");

      Request.Method actual;
      actual = method();

      if (handles(method1, actual)) {
        handler1.handle(this);
      } else if (handles(method2, actual)) {
        handler2.handle(this);
      } else {
        methodNotAllowed();
      }
    }

    default void methodMatrix(Request.Method method1, Handler handler1,
        Request.Method method2, Handler handler2,
        Request.Method method3, Handler handler3) {
      Check.notNull(method1, "method1 == null");
      Check.notNull(handler1, "handler1 == null");
      Check.notNull(method2, "method2 == null");
      Check.notNull(handler2, "handler2 == null");
      Check.notNull(method3, "method3 == null");
      Check.notNull(handler3, "handler3 == null");

      Request.Method actual;
      actual = method();

      if (handles(method1, actual)) {
        handler1.handle(this);
      } else if (handles(method2, actual)) {
        handler2.handle(this);
      } else if (handles(method3, actual)) {
        handler3.handle(this);
      } else {
        methodNotAllowed();
      }
    }

    private boolean handles(Request.Method method, Request.Method actual) {
      if (method.is(Http.GET)) {
        return actual.is(Http.GET, Http.HEAD);
      } else {
        return actual.is(method);
      }
    }

    void status(Response.Status status);

    void header(HeaderName name, long value);

    void header(HeaderName name, String value);

    // pre-made headers

    void dateNow();

    // response body

    void send();

    void send(byte[] body);

    void send(CharWritable body, Charset charset);

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
  public sealed interface Request {

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
    public sealed interface Method permits HttpRequestMethod {

      /**
       * Tests if this method is the same as the specified method.
       *
       * @param method
       *        the method to test against this method
       *
       * @return {@code true} if this method is the same as the specified
       *         method, {@code false} otherwise
       */
      boolean is(Method method);

      /**
       * Tests if this method is the same as one of the two specified methods.
       *
       * @param method1
       *        the first method to test against this method
       * @param method2
       *        the second method to test against this method
       *
       * @return {@code true} if this method is the same as first or the second
       *         specified methods, {@code false} otherwise
       */
      boolean is(Method method1, Method method2);

      /**
       * The index of this method.
       *
       * @return the index of this method.
       */
      int index();

      /**
       * The name of this method.
       *
       * @return the name of this method.
       */
      String text();

    }

    /**
     * The request-target of an HTTP request message.
     *
     * <p>
     * Unless otherwise specified the values returned by the methods of this
     * interface are decoded.
     */
    public sealed interface Target permits HttpRequestLine {

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
      int queryParamAsInt(String name, int defaultValue);

      /**
       * The raw (encoded) value of the query component. This method returns
       * {@code null} if this request-target does not have a query component.
       *
       * @return the raw (encoded) value of the query component or {@code null}
       */
      String rawQuery();

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
     * The method of this request message.
     *
     * @return the method of this request message
     */
    Method method();

    /**
     * The request-target of this HTTP request message.
     *
     * @return the request-target of this HTTP request message
     */
    Target target();

    // convenience methods

    /**
     * The value of the path component of the request-target.
     *
     * @return value of the path component of the request-target
     *
     * @see Http.Request.Target#path()
     */
    default String path() {
      return target().path();
    }

    /**
     * Returns the value of the path parameter with the specified name if it
     * exists or returns {@code null} otherwise.
     *
     * @param name
     *        the name of the path parameter
     *
     * @return the value if it exists or {@code null} if it does not
     *
     * @see Http.Request.Target#pathParam(String)
     */
    default String pathParam(String name) {
      return target().pathParam(name);
    }

    /**
     * Returns the first value of the query parameter with the specified name
     * or {@code null} if there are no values.
     *
     * @param name
     *        the name of the query parameter
     *
     * @return the first value if it exists or {@code null} if it does not
     *
     * @see Http.Request.Target#queryParam(String)
     */
    default String queryParam(String name) {
      return target().queryParam(name);
    }

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
  public sealed interface TestingExchange extends Exchange permits HttpTestingExchange {

    /**
     * Configures the creation of a exchange instance.
     */
    public sealed interface Option {}

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

  // non-public types

  non-sealed static abstract class HttpTestingExchangeOption implements Http.TestingExchange.Option {

    abstract void acceptHttpTestingExchange(HttpTestingExchange http);

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
   * The CONNECT method.
   */
  public static final Request.Method CONNECT = HttpRequestMethod.create("CONNECT");

  /**
   * The DELETE method.
   */
  public static final Request.Method DELETE = HttpRequestMethod.create("DELETE");

  /**
   * The GET method.
   */
  public static final Request.Method GET = HttpRequestMethod.create("GET");

  /**
   * The HEAD method.
   */
  public static final Request.Method HEAD = HttpRequestMethod.create("HEAD");

  /**
   * The OPTIONS method.
   */
  public static final Request.Method OPTIONS = HttpRequestMethod.create("OPTIONS");

  /**
   * The PATCH method.
   */
  public static final Request.Method PATCH = HttpRequestMethod.create("PATCH");

  /**
   * The POST method.
   */
  public static final Request.Method POST = HttpRequestMethod.create("POST");

  /**
   * The PUT method.
   */
  public static final Request.Method PUT = HttpRequestMethod.create("PUT");

  /**
   * The TRACE method.
   */
  public static final Request.Method TRACE = HttpRequestMethod.createLast("TRACE");

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

  public static Server.Option sessionStore(SessionStore sessionStore) {
    Check.notNull(sessionStore, "sessionStore == null");

    return new HttpServerOption() {
      @Override
      final void acceptHttpServerBuilder(Builder builder) {
        builder.sessionStore = sessionStore;
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
  public static Request.Target parseRequestTarget(String target) throws IllegalArgumentException {
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

  /**
   * Creates an exchange instance suitable for test cases.
   *
   * @param options
   *        configures the created exchange instance
   *
   * @return a newly created exchange instance with the provided options
   */
  public static TestingExchange testingExchange(TestingExchange.Option... options) {
    Check.notNull(options, "options == null");

    HttpTestingExchange result;
    result = new HttpTestingExchange();

    for (int idx = 0; idx < options.length; idx++) {
      TestingExchange.Option o;
      o = Check.notNull(options[idx], "options[", idx, "] == null");

      HttpTestingExchangeOption option;
      option = (HttpTestingExchangeOption) o;

      option.acceptHttpTestingExchange(result);
    }

    return result;
  }

  /**
   * Testing exchange option: sets the request-target to the result of parsing
   * the specified string.
   *
   * @param target
   *        the raw (undecoded) request-target value
   *
   * @return a new testing exchange option
   */
  public static TestingExchange.Option requestTarget(String target) {
    Request.Target requestTarget;
    requestTarget = parseRequestTarget(target);

    return new HttpTestingExchangeOption() {
      @Override
      final void acceptHttpTestingExchange(HttpTestingExchange http) {
        http.requestTarget = requestTarget;
      }
    };
  }

  /**
   * Testing exchange option: stores the provided key-value pair in the testing
   * exchange.
   *
   * @param key
   *        the key to be stored
   * @param value
   *        the value to be stored
   *
   * @return a new testing exchange option
   */
  public static <T> TestingExchange.Option set(Class<T> key, T value) {
    return new HttpTestingExchangeOption() {
      @Override
      final void acceptHttpTestingExchange(HttpTestingExchange http) {
        http.set(key, value);
      }
    };
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

}