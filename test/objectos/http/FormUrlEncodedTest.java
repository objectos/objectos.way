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
package objectos.http;

import static org.testng.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import objectos.http.WayServerLoop.ParseStatus;
import objectos.way.Http.Exchange;
import objectos.way.Http;
import objectos.way.TestingClock;
import objectos.way.TestingNoteSink;
import org.testng.Assert;
import org.testng.annotations.Test;

public class FormUrlEncodedTest {

  @Test
  public void testCase01() throws IOException {
    Http.Request.Body body;
    body = body("email=user%40example.com");

    FormUrlEncoded form;
    form = FormUrlEncoded.parse(body);

    assertEquals(form.size(), 1);
    assertEquals(form.names(), Set.of("email"));
    assertEquals(form.get("email"), "user@example.com");
  }

  @Test
  public void testCase02() throws IOException {
    Http.Request.Body body;
    body = body("login=foo&password=bar");

    FormUrlEncoded form;
    form = FormUrlEncoded.parse(body);

    assertEquals(form.size(), 2);
    assertEquals(form.names(), Set.of("login", "password"));
    assertEquals(form.get("login"), "foo");
    assertEquals(form.get("password"), "bar");
  }

  @Test
  public void testCase03() throws UnsupportedMediaTypeException, IOException {
    FormUrlEncoded form;
    form = parse("""
    POST /login HTTP/1.1\r
    Host: www.example.com\r
    Content-Length: 24\r
    Content-Type: application/x-www-form-urlencoded\r
    \r
    email=user%40example.com""");

    assertEquals(form.size(), 1);
    assertEquals(form.names(), Set.of("email"));
    assertEquals(form.get("email"), "user@example.com");
  }

  @Test
  public void testCase04() throws IOException {
    try {
      parse("""
      POST /login HTTP/1.1\r
      Host: www.example.com\r
      Content-Length: 24\r
      Content-Type: multipart/form-data\r
      \r
      email=user%40example.com""");

      Assert.fail("Should have thrown");
    } catch (UnsupportedMediaTypeException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "multipart/form-data");
    }
  }

  private Http.Request.Body body(String s) {
    return new ThisBody(s);
  }

  private FormUrlEncoded parse(String request) throws UnsupportedMediaTypeException, IOException {
    TestableSocket socket;
    socket = TestableSocket.of(request);

    try (WayServerLoop http = new WayServerLoop(socket)) {
      http.bufferSize(512, 1024);
      http.clock(TestingClock.FIXED);
      http.noteSink(TestingNoteSink.INSTANCE);

      ParseStatus status;
      status = http.parse();

      assertEquals(status.isError(), false);

      Exchange exchange;
      exchange = http;

      return FormUrlEncoded.parse(exchange);
    }
  }

  private static class ThisBody implements Http.Request.Body {

    private final String value;

    public ThisBody(String value) {
      this.value = value;
    }

    @Override
    public final InputStream openStream() throws IOException {
      byte[] bytes;
      bytes = value.getBytes(StandardCharsets.UTF_8);

      return new ByteArrayInputStream(bytes);
    }

  }

}