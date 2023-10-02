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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.time.ZonedDateTime;
import objectos.http.internal.Bytes;
import objectos.http.internal.Http001;
import objectos.http.internal.TestableSocket;
import objectos.http.internal.TestingInput.RegularInput;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void http001() throws IOException {
    RegularInput input;
    input = Http001.INPUT;

    TestableSocket socket;
    socket = TestableSocket.of(input.request());

    try (HttpExchange exchange = HttpExchange.of(socket)) {
      assertTrue(exchange.keepAlive());

      exchange.executeRequestPhase();

      assertEquals(exchange.method(), Http.Method.GET);

      assertFalse(exchange.hasResponse());

      byte[] bytes;
      bytes = Bytes.utf8("Hello World!\n");

      exchange.status(Http.Status.OK_200);

      exchange.header(Http.Header.CONTENT_TYPE, "text/plain; charset=utf-8");

      exchange.header(Http.Header.CONTENT_LENGTH, Long.toString(bytes.length));

      ZonedDateTime date;
      date = Http001.DATE;

      exchange.header(Http.Header.DATE, Http.formatDate(date));

      exchange.body(bytes);

      assertTrue(exchange.hasResponse());

      exchange.executeResponsePhase();

      assertFalse(exchange.keepAlive());
    }

    assertEquals(socket.outputAsString(), Http001.OUTPUT);
  }

}