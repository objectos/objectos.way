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
import objectox.http.TestingInput.KeepAliveInput;

/**
 * Keep-Alive request
 */
public final class Http004 {

  private static final ZonedDateTime DATE = ZonedDateTime.of(
    LocalDate.of(2023, 7, 7),
    LocalTime.of(11, 11, 45),
    ZoneId.of("GMT-3")
  );

  public static final String INPUT01 = """
    GET /login HTTP/1.1
    Host: www.example.com
    Connection: keep-alive

    """.replace("\n", "\r\n");

  public static final String INPUT02 = """
    GET /login.css HTTP/1.1
    Host: www.example.com
    Connection: close

    """.replace("\n", "\r\n");

  public static final KeepAliveInput INPUT = new KeepAliveInput(
    INPUT01, INPUT02);

  public static final String BODY01 = """
    <!doctype html>
    <html>
    <head>
    <title>Login page</title>
    <link rel="stylesheet" type="text/css" href="login.css" />
    </head>
    <body>
    <!-- the actual body -->
    </body>
    </html>
    """;

  public static final String BODY02 = """
    * {
      box-sizing: border-box;
    }
    """;

  public static final String OUTPUT01 = """
    HTTP/1.1 200 OK<CRLF>
    Content-Type: text/html; charset=utf-8<CRLF>
    Content-Length: 171<CRLF>
    Date: Fri, 07 Jul 2023 14:11:45 GMT<CRLF>
    <CRLF>
    %s""".formatted(BODY01).replace("<CRLF>\n", "\r\n");

  public static final String OUTPUT02 = """
    HTTP/1.1 200 OK<CRLF>
    Content-Type: text/css; charset=utf-8<CRLF>
    Content-Length: 32<CRLF>
    Date: Fri, 07 Jul 2023 14:11:45 GMT<CRLF>
    <CRLF>
    %s""".formatted(BODY02).replace("<CRLF>\n", "\r\n");

  public static final String OUTPUT = OUTPUT01 + OUTPUT02;

  public static final Http004 INSTANCE = new Http004();

  public static void response(objectos.http.HttpExchange exchange) {
    String path;
    path = exchange.path();

    switch (path) {
      case "/login" -> {
        final byte[] bytes;
        bytes = Bytes.utf8(BODY01);

        exchange.status(Http.Status.OK_200);

        exchange.header(Http.Header.CONTENT_TYPE, "text/html; charset=utf-8");

        exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

        exchange.header(Http.Header.DATE, Http.formatDate(DATE));

        exchange.body(bytes);
      }

      case "/login.css" -> {
        final byte[] bytes;
        bytes = Bytes.utf8(BODY02);

        exchange.status(Http.Status.OK_200);

        exchange.header(Http.Header.CONTENT_TYPE, "text/css; charset=utf-8");

        exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

        exchange.header(Http.Header.DATE, Http.formatDate(DATE));

        exchange.body(bytes);
      }
    }
  }

}