/*
 * Copyright (C) 2023 Objectos Software LTDA.
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

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import objectos.html.HtmlTemplate;
import objectos.lang.object.Check;

public interface ServerExchange {

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

  void send(Path file);

  // pre-made responses

  // 200
  default void ok() {
    status(Status.OK);

    dateNow();

    send();
  }

  default void ok(HtmlTemplate html) {
    String s; // early implicit null-check
    s = html.toString();

    status(Status.OK);

    dateNow();

    header(HeaderName.CONTENT_TYPE, "text/html; charset=utf-8");

    byte[] bytes;
    bytes = s.getBytes(StandardCharsets.UTF_8);

    header(HeaderName.CONTENT_LENGTH, bytes.length);

    send(bytes);
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

  // 500
  void internalServerError(Throwable t);

}
