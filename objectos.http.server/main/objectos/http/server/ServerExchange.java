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
import objectos.http.HeaderName;
import objectos.http.Method;
import objectos.http.Status;

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

  void status(Status status);

  void header(HeaderName name, long value);

  void header(HeaderName name, String value);

  void send();

  void send(byte[] body);

  void send(Path file);

}
