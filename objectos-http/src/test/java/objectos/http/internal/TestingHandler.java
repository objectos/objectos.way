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
package objectos.http.internal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Supplier;
import objectos.http.Http;
import objectos.http.Http.Exchange;
import objectos.http.Http.Handler;

public final class TestingHandler implements Http.Handler, Supplier<Http.Handler> {

  public static final String HTTP001 = """
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

  public static final TestingHandler INSTANCE = new TestingHandler();

  private TestingHandler() {}

  @Override
  public final void handle(Exchange exchange) {
    final byte[] bytes;
    bytes = Bytes.utf8("Hello World!\n");

    Http.Response response;
    response = exchange.response();

    response.status(Http.Status.OK_200);

    response.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

    response.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

    response.header(Http.Header.DATE, Http.formatDate(DATE));

    response.send(bytes);
  }

  @Override
  public final Handler get() { return this; }

}