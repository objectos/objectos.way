/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.host;

import static org.testng.Assert.assertEquals;

import objectos.http.Content;
import objectos.http.Handler;
import objectos.http.HttpHeaderName;
import objectos.http.HttpStatus;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Response;
import objectos.way.Y;
import objectox.http.RequestMethodEnum;
import objectox.http.resp.ResponsePojo;
import org.testng.annotations.Test;

public class HostTest {

  private String handle(Handler handler) {
    final Host host;
    host = HostY.create(opts -> {
      opts.handler = handler;

      opts.name = "test.localhost";
    });

    final Request request;
    request = Request.create(opts -> {
      opts.method(RequestMethodEnum.GET);

      opts.header(HttpHeaderName.HOST, "test.localhost");
    });

    final ResponsePojo resp;
    resp = host.handle(request);

    return resp.toString(Y.clockFixed(), false);
  }

  @Test(description = "handler -> Response")
  public void handle01() {
    assertEquals(
        handle(_ -> Response.create(opts -> {
          opts.status(HttpStatus.OK);
          opts.date();
        })),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        \r
        """
    );
  }

  @Test(description = "handler -> Content")
  public void handle02() {
    assertEquals(
        handle(_ -> Content.of(MediaType.TEXT_PLAIN, "OK\n")),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

}
