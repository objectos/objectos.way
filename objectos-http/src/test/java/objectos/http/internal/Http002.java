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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import objectos.http.Http;
import objectos.http.internal.TestingInput.RegularInput;
import objectos.http.server.Exchange;
import objectos.http.server.Handler;
import objectos.http.server.Response;
import objectos.lang.CharWritable;

/**
 * Transfer-Encoding: Chunked
 */
public final class Http002 implements Handler {

  public static final RegularInput INPUT = new RegularInput(
    """
    GET /chunked.txt HTTP/1.1
    Host: www.example.com
    Connection: close

    """.replace("\n", "\r\n")
  );

  public static final String BODY = """
    200<CRLF>
    012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    012345678901<CRLF>
    58<CRLF>
    234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
    <CRLF>
    0<CRLF>
    <CRLF>
    """.replace("<CRLF>\n", "\r\n");

  public static final String OUTPUT = """
    HTTP/1.1 200 OK<CRLF>
    Content-Type: text/plain; charset=utf-8<CRLF>
    Date: Thu, 06 Jul 2023 13:13:25 GMT<CRLF>
    Transfer-Encoding: chunked<CRLF>
    <CRLF>
    """.replace("<CRLF>\n", "\r\n") + BODY;

  private static final ZonedDateTime DATE = ZonedDateTime.of(
    LocalDate.of(2023, 7, 6),
    LocalTime.of(10, 13, 25),
    ZoneId.of("GMT-3")
  );

  static final Http002 INSTANCE = new Http002();

  @Override
  public final void handle(Exchange exchange) {
    Response response;
    response = exchange.response();

    response.status(Http.Status.OK_200);

    response.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

    response.header(Http.Header.DATE, Http.formatDate(DATE));

    response.header(Http.Header.TRANSFER_ENCODING, "chunked");

    ThisEntity entity;
    entity = new ThisEntity();

    response.send(entity, StandardCharsets.UTF_8);
  }

  public static CharWritable newEntity() {
    return new ThisEntity();
  }

  private static class ThisEntity implements CharWritable {
    @Override
    public final void writeTo(Appendable appendable) throws IOException {
      String singleLine;
      singleLine = """
      012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678
      """;

      for (int i = 0; i < 6; i++) {
        appendable.append(singleLine);
      }
    }
  }

}