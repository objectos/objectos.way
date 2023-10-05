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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectos.http.server.Response;
import objectox.http.TestingInput.RegularInput;

/**
 * Minimal GET request
 */
public final class Http001 implements Handler {

  public static final RegularInput INPUT = new RegularInput(
    """
    GET / HTTP/1.1
    Host: www.example.com
    Connection: close

    """.replace("\n", "\r\n")
  );

  public static final String OUTPUT = """
      HTTP/1.1 200 OK<CRLF>
      Content-Type: text/plain; charset=utf-8<CRLF>
      Content-Length: 13<CRLF>
      Date: Wed, 28 Jun 2023 12:08:43 GMT<CRLF>
      <CRLF>
      Hello World!
      """.replace("<CRLF>\n", "\r\n");

  public static final ZonedDateTime DATE = ZonedDateTime.of(
    LocalDate.of(2023, 6, 28),
    LocalTime.of(9, 8, 43),
    ZoneId.of("GMT-3")
  );

  static final Http001 INSTANCE = new Http001();

  public static void response(objectos.http.HttpExchange exchange) {
    final byte[] bytes;
    bytes = Bytes.utf8("Hello World!\n");

    exchange.status(Http.Status.OK_200);

    exchange.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

    exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

    exchange.header(Http.Header.DATE, Http.formatDate(DATE));

    exchange.body(bytes);
  }

  @Override
  public final void handle(Exchange exchange) {
    Response response;
    response = exchange.response();

    final byte[] bytes;
    bytes = Bytes.utf8("Hello World!\n");

    response.status(Http.Status.OK_200);

    response.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

    response.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

    response.header(Http.Header.DATE, Http.formatDate(DATE));

    response.send(bytes);
  }

}