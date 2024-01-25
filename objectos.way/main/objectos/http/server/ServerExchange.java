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
package objectos.http.server;

import java.nio.file.Path;
import java.util.function.Consumer;
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.Status;
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

  // response

  default void accept(Handler handler) {
    handler.handle(this);
  }

  default void methodMatrix(Method method, Consumer<ServerExchange> handler) {
    Check.notNull(method, "method == null");
    Check.notNull(handler, "handler == null");

    Method actual;
    actual = method();

    if (actual.is(method)) {
      handler.accept(this);
    } else {
      methodNotAllowed();
    }
  }

  default void methodMatrix(Method method1, Consumer<ServerExchange> handler1,
                            Method method2, Consumer<ServerExchange> handler2) {
    Check.notNull(method1, "method1 == null");
    Check.notNull(handler1, "handler1 == null");
    Check.notNull(method2, "method2 == null");
    Check.notNull(handler2, "handler2 == null");

    Method actual;
    actual = method();

    if (actual.is(method1)) {
      handler1.accept(this);
    } else if (actual.is(method2)) {
      handler2.accept(this);
    } else {
      methodNotAllowed();
    }
  }

  default void methodMatrix(Method method1, Consumer<ServerExchange> handler1,
                            Method method2, Consumer<ServerExchange> handler2,
                            Method method3, Consumer<ServerExchange> handler3) {
    Check.notNull(method1, "method1 == null");
    Check.notNull(handler1, "handler1 == null");
    Check.notNull(method2, "method2 == null");
    Check.notNull(handler2, "handler2 == null");
    Check.notNull(method3, "method3 == null");
    Check.notNull(handler3, "handler3 == null");

    Method actual;
    actual = method();

    if (actual.is(method1)) {
      handler1.accept(this);
    } else if (actual.is(method2)) {
      handler2.accept(this);
    } else if (actual.is(method3)) {
      handler3.accept(this);
    } else {
      methodNotAllowed();
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

  // 404
  void notFound();

  // 405
  void methodNotAllowed();

  // 500
  void internalServerError(Throwable t);

}
