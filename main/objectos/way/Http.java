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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import objectos.html.Html;
import objectos.html.HtmlTemplate;
import objectos.http.UriPath;
import objectos.http.UriQuery;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;

/**
 * The Objectos HTTP main class.
 */
public final class Http {

  // types
  
  /**
   * An HTTP request received by the server and its subsequent response to the
   * client.
   */
  public interface Exchange {
    
    /**
     * Stores an object in this request. The object will be associated to the name
     * of the specified {@code Class} instance.
     * Stored objects are reset between requests.
     * 
     * <p>
     * If an object is already associated to the specified key it will be replaced
     * with the specified value.
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
     * Returns the request method.
     *
     * @return the request method
     */
    Request.Method method();

    /**
     * Returns the path component of the request target.
     *
     * @return the path component of the request target.
     */
    UriPath path();

    UriQuery query();

    Request.Headers headers();

    /**
     * Returns the request message body.
     *
     * @return the request message body.
     */
    Request.Body body();

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

    default void ok(HtmlTemplate template) {
      Html html;
      html = new Html();

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
   * Responsible for processing an HTTP {@linkplain Exchange exchange}.
   */
  @FunctionalInterface
  public interface Handler {

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
   * An HTTP request message.
   */
  public interface Request {
    
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
       * Returns the value of the cookie with the specified name; {@code null} if a
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
     */
    public interface Target {

      /**
       * The path component of a request-target.
       */
      public interface Path {

        /**
         * A segment of a path.
         */
        public interface Segment {

          boolean is(String other);

          String value();

        }

        boolean is(String path);

        boolean startsWith(String prefix);

        List<Segment> segments();

      }

      /**
       * The path component of this request-target.
       * 
       * @return the path component of this request-target.
       */
      Path path();

      /**
       * The query component of a request-target.
       */
      public interface Query {

        String encodedValue();
        
        String get(String name);

        default int getAsInt(String name, int defaultValue) {
          String maybe;
          maybe = get(name);

          if (maybe == null) {
            return defaultValue;
          }

          try {
            return Integer.parseInt(maybe);
          } catch (NumberFormatException expected) {
            return defaultValue;
          }
        }

        boolean isEmpty();

        Set<String> names();

        UriQuery set(String name, String value);
        
        String value();

      }

      /**
       * The query component of this request-target.
       * 
       * @return the query component of this request-target.
       */
      Query query();
      
    }
    
    /**
     * The body of this request message.
     * 
     * @return the body of this request message.
     */
    Body body();
    
    /**
     * The header section of this request message.
     * 
     * @return the header section of this request message.
     */
    Headers headers();

    /**
     * The method of this request message.
     * 
     * @return the method of this request message.
     */
    Method method();

    /**
     * The request-target of this HTTP request message.
     * 
     * @return the request-target of this HTTP request message.
     */
    Target target();

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

}