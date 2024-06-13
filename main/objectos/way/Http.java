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
import objectos.http.Body;
import objectos.http.Handler;
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.ServerRequestHeaders;
import objectos.http.Session;
import objectos.http.SessionStore;
import objectos.http.Status;
import objectos.http.UriPath;
import objectos.http.UriQuery;
import objectos.lang.CharWritable;
import objectos.lang.object.Check;

/**
 * 
 */
public final class Http {

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

  private Http() {}

  /**
   * Formats a date so it can be used as the value of a {@code Date} HTTP
   * header.
   * 
   * @param date the date to be formatted
   * 
   * @return the formatted date
   */
  public static String formatDate(ZonedDateTime date) {
    ZonedDateTime normalized;
    normalized = date.withZoneSameInstant(ZoneOffset.UTC);

    return IMF_FIXDATE.format(normalized);
  }
  
  /**
   * An HTTP request received by the server and its subsequent response to the
   * client.
   */
  public interface ServerExchange {
    
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
    Method method();

    /**
     * Returns the path component of the request target.
     *
     * @return the path component of the request target.
     */
    UriPath path();

    UriQuery query();

    ServerRequestHeaders headers();

    /**
     * Returns the request message body.
     *
     * @return the request message body.
     */
    Body body();

    /**
     * Returns the session associated with this request or {@code null} if no
     * session was found.
     *
     * @return the session associated with this request or {@code null}
     */
    Session session();

    // response

    default void accept(Handler handler) {
      handler.handle(this);
    }

    void acceptSessionStore(SessionStore sessionStore);

    default void methodMatrix(Method method, Handler handler) {
      Check.notNull(method, "method == null");
      Check.notNull(handler, "handler == null");

      Method actual;
      actual = method();

      if (handles(method, actual)) {
        handler.handle(this);
      } else {
        methodNotAllowed();
      }
    }

    default void methodMatrix(Method method1, Handler handler1,
                              Method method2, Handler handler2) {
      Check.notNull(method1, "method1 == null");
      Check.notNull(handler1, "handler1 == null");
      Check.notNull(method2, "method2 == null");
      Check.notNull(handler2, "handler2 == null");

      Method actual;
      actual = method();

      if (handles(method1, actual)) {
        handler1.handle(this);
      } else if (handles(method2, actual)) {
        handler2.handle(this);
      } else {
        methodNotAllowed();
      }
    }

    default void methodMatrix(Method method1, Handler handler1,
                              Method method2, Handler handler2,
                              Method method3, Handler handler3) {
      Check.notNull(method1, "method1 == null");
      Check.notNull(handler1, "handler1 == null");
      Check.notNull(method2, "method2 == null");
      Check.notNull(handler2, "handler2 == null");
      Check.notNull(method3, "method3 == null");
      Check.notNull(handler3, "handler3 == null");

      Method actual;
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

    private boolean handles(Method method, Method actual) {
      if (method.is(Method.GET)) {
        return actual.is(Method.GET, Method.HEAD);
      } else {
        return actual.is(method);
      }
    }

    void status(Status status);

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
      status(Status.OK);

      dateNow();

      send();
    }

    default void ok(HtmlTemplate template) {
      Html html;
      html = new Html();

      template.accept(html);

      status(Status.OK);

      dateNow();

      header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");

      header(HeaderName.TRANSFER_ENCODING, "chunked");
      
      send(html, StandardCharsets.UTF_8);
    }

    default void okText(String text, Charset charset) {
      byte[] bytes;
      bytes = text.getBytes(charset); // early implicit null-check

      status(Status.OK);

      dateNow();

      header(HeaderName.CONTENT_TYPE, "text/plain; charset=" + charset.name().toLowerCase(Locale.US));

      header(HeaderName.CONTENT_LENGTH, bytes.length);

      send(bytes);
    }

    // 301
    default void movedPermanently(String location) {
      Check.notNull(location, "location == null");

      status(Status.MOVED_PERMANENTLY);

      dateNow();

      header(HeaderName.LOCATION, location);

      send();
    }

    // 302
    default void found(String location) {
      Check.notNull(location, "location == null");

      status(Status.FOUND);

      dateNow();

      header(HeaderName.LOCATION, location);

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
      status(Status.UNSUPPORTED_MEDIA_TYPE);

      dateNow();

      header(HeaderName.CONNECTION, "close");

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
      status(Status.UNPROCESSABLE_CONTENT);

      dateNow();

      header(HeaderName.CONNECTION, "close");

      send();
    }

    // 500
    void internalServerError(Throwable t);

    boolean processed();
    
  }

  /**
   * An HTTP request message.
   */
  public interface Request {
    
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
     * The request-target of this HTTP request message.
     * 
     * @return the request-target of this HTTP request message.
     */
    Target target();

  }

}