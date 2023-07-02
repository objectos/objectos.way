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

import static org.testng.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.Http.Exchange;
import org.testng.annotations.Test;

public class HttpExchangeHandleTest {

  @Test
  public void handleHttp001() {
    HttpExchange exchange;
    exchange = new HttpExchange();

    final byte[] bytes;
    bytes = Bytes.utf8("Hello world!\n");

    ZonedDateTime date;
    date = ZonedDateTime.of(
      LocalDate.of(2023, 6, 28),
      LocalTime.of(9, 8, 43),
      ZoneId.of("GMT-3")
    );

    exchange.handler = new Http.Handler() {
      @Override
      public void handle(Exchange exchange) {
        Http.Response response;
        response = exchange.response();

        response.status(Http.Status.OK_200);

        response.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

        response.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

        response.header(Http.Header.DATE, Http.formatDate(date));

        response.send(bytes);
      }
    };
    exchange.responseBytes = new byte[0];
    exchange.responseHeaders = null;
    exchange.state = HttpExchange._HANDLE;

    exchange.stepOne();

    assertEquals(exchange.responseBytes, bytes);
    assertEquals(
      exchange.responseHeaders.join("\n", "", "\n"),

      """
      Content-Type: text/plain; charset=utf-8
      Content-Length: 13
      Date: Wed, 28 Jun 2023 12:08:43 GMT
      """
    );
    assertEquals(exchange.state, HttpExchange._OUTPUT);
    assertEquals(exchange.status, HttpStatus.OK);
  }

}