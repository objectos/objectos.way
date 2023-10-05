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
package objectox.http;

import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectox.http.TestingInput.RegularInput;

/**
 * Unknown request headers
 */
public final class Http003 implements Handler {

  public static final RegularInput INPUT = new RegularInput(
    """
    GET / HTTP/1.1
    Host: www.example.com
    Connection: close
    Foo: Bar

    """.replace("\n", "\r\n")
  );

  public static final String OUTPUT = Http001.OUTPUT;

  public static void response(HttpExchange exchange) {
    Http001.response(exchange);
  }

  @Override
  public final void handle(Exchange exchange) {
    throw new UnsupportedOperationException();
  }

}