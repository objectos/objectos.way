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
package objectox.http.server;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.Http.Status;
import objectos.http.server.Body;
import objectos.http.server.HttpExchange;
import objectos.http.server.UrlEncodedForm;
import objectox.http.server.TestingInput.RegularInput;

/**
 * Minimal POST request
 *
 * - application/x-www-form-urlencoded
 */
public final class Http006 {

  public static final RegularInput INPUT = new RegularInput(
    """
    POST /login HTTP/1.1
    Host: www.example.com
    Content-Length: 24
    Content-Type: application/x-www-form-urlencoded

    email=user%40example.com""".replace("\n", "\r\n")
  );

  public static final String OUTPUT = """
      HTTP/1.1 303 SEE OTHER<CRLF>
      Location: /app<CRLF>
      Content-Type: text/plain; charset=utf-8<CRLF>
      Content-Length: 52<CRLF>
      Date: Mon, 31 Jul 2023 10:54:43 GMT<CRLF>
      <CRLF>
      Hello user@example.com. Please enter your password.
      """.replace("<CRLF>\n", "\r\n");

  static final ZonedDateTime DATE = ZonedDateTime.of(
    LocalDate.of(2023, 7, 31),
    LocalTime.of(7, 54, 43),
    ZoneId.of("GMT-3")
  );

  static final Http006 INSTANCE = new Http006();

  public static void response(HttpExchange exchange) {
    Http.Method method;
    method = exchange.method();

    if (method != Http.Method.POST) {
      sendText(
        exchange,
        Http.Status.NOT_FOUND_404,
        "Not found on this server\n"
      );

      return;
    }

    Http.Header.Value contentType;
    contentType = exchange.header(Http.Header.CONTENT_TYPE);

    if (!contentType.contentEquals("application/x-www-form-urlencoded")) {
      sendText(
        exchange,
        Http.Status.UNSUPPORTED_MEDIA_TYPE_415,
        "Requested content-type is not supported\n"
      );

      return;
    }

    Body body;
    body = exchange.body();

    UrlEncodedForm form;
    form = UrlEncodedForm.parse(body);

    String email;
    email = form.get("email");

    if (email == null) {
      sendText(
        exchange,
        Http.Status.UNPROCESSABLE_CONTENT_422,
        "Email is required\n"
      );

      return;
    }

    exchange.header(Http.Header.LOCATION, "/app");

    sendText(
      exchange,
      Http.Status.SEE_OTHER_303,
      "Hello %s. Please enter your password.\n".formatted(email)
    );
  }

  private static void sendText(HttpExchange exchange, Status status, String message) {
    exchange.status(status);

    final byte[] bytes;
    bytes = Bytes.utf8(message);

    exchange.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

    exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

    exchange.header(Http.Header.DATE, Http.formatDate(DATE));

    exchange.body(bytes);
  }

}