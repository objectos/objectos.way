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
package objectox.http.server;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import objectos.http.Method;
import objectos.http.server.ServerExchangeResult;
import objectos.http.server.ServerRequest;
import objectos.http.server.ServerRequestBody;
import objectos.http.server.ServerRequestHeaders;
import objectos.http.server.UriPath;
import objectox.http.StandardHeaderName;
import org.testng.annotations.Test;

public class ObjectoxServerRequestTest {

  @Test(description = "Minimal GET request")
  public void testCase001() throws IOException {
    ObjectoxServerRequest req;
    req = regularInput("""
    GET / HTTP/1.1\r
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    ServerExchangeResult maybe;
    maybe = req.get();

    ServerRequest result;
    result = (ServerRequest) maybe;

    assertSame(result, req);

    // request line
    UriPath path;
    path = req.path();

    assertEquals(req.method(), Method.GET);
    assertEquals(path.is("/"), true);

    // headers
    ServerRequestHeaders headers;
    headers = req.headers();

    assertEquals(headers.size(), 2);
    assertEquals(headers.first(StandardHeaderName.HOST), "www.example.com");
    assertEquals(headers.first(StandardHeaderName.CONNECTION), "close");

    // body
    ServerRequestBody body;
    body = req.body();

    byte[] bytes;
    bytes = ObjectoxHttpServer.readAllBytes(body);

    assertEquals(bytes.length, 0);
  }

  private ObjectoxServerRequest regularInput(Object... data) {
    TestableInputStream inputStream;
    inputStream = TestableInputStream.of(data);

    SocketInput input;
    input = new SocketInput(64, inputStream);

    return new ObjectoxServerRequest(input);
  }

}