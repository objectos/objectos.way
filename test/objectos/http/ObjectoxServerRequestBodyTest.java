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
import static org.testng.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

public class ObjectoxServerRequestBodyTest {

  @Test(description = """
  GET / HTTP/1.1
  Host: www.example.com
  Connection: close
  """)
  public void testCase001() throws IOException {
    ObjectoxServerRequestBody body;
    body = regularInput("""
    Host: www.example.com\r
    Connection: close\r
    \r
    """);

    body.parseHeaders();
    body.parseRequestBody();

    assertNull(body.badRequest);
    assertEquals(asString(body), "");
  }

  @Test(description = """
  Minimal POST request
  - happy path
  """)
  public void testCase008() throws IOException {
    ObjectoxServerRequestBody body;
    body = regularInput("""
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    body.parseHeaders();
    body.parseRequestBody();

    assertNull(body.badRequest);
    assertEquals(asString(body), "email=user%40example.com");
  }

  private ObjectoxServerRequestBody regularInput(Object... data) {
    ObjectoxServerRequestBody body;
    body = new ObjectoxServerRequestBody();

    body.bufferSize(64, 128);

    TestingInputStream inputStream;
    inputStream = TestingInputStream.of(data);

    body.initSocketInput(inputStream);

    return body;
  }

  private String asString(Body body) throws IOException {
    try (InputStream in = body.openStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      byte[] bytes;
      bytes = out.toByteArray();

      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

}