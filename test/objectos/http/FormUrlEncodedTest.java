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
import org.testng.annotations.Test;

public class FormUrlEncodedTest {

  @Test
  public void testCase01() throws IOException {
    Body body;
    body = body("email=user%40example.com");

    FormUrlEncoded form;
    form = FormUrlEncoded.parse(body);

    assertEquals(form.size(), 1);
    assertEquals(form.get("email"), "user@example.com");
  }

  @Test
  public void testCase02() throws IOException {
    Body body;
    body = body("login=foo&password=bar");

    FormUrlEncoded form;
    form = FormUrlEncoded.parse(body);

    assertEquals(form.size(), 2);
    assertEquals(form.get("login"), "foo");
    assertEquals(form.get("password"), "bar");
  }

  private Body body(String s) {
    return new ThisBody(s);
  }

  private static class ThisBody implements Body {

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