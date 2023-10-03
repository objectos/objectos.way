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
package objectos.http.util;

import static org.testng.Assert.assertEquals;

import objectox.http.Bytes;
import objectox.http.InBufferRequestBody;
import org.testng.annotations.Test;

public class UrlEncodedFormTest {

  @Test
  public void testCase01() {
    InBufferRequestBody body;
    body = body("email=user%40example.com");

    UrlEncodedForm form;
    form = UrlEncodedForm.parse(body);

    assertEquals(form.size(), 1);
    assertEquals(form.get("email"), "user@example.com");
  }

  @Test
  public void testCase02() {
    InBufferRequestBody body;
    body = body("login=foo&password=bar");

    UrlEncodedForm form;
    form = UrlEncodedForm.parse(body);

    assertEquals(form.size(), 2);
    assertEquals(form.get("login"), "foo");
    assertEquals(form.get("password"), "bar");
  }

  private InBufferRequestBody body(String s) {
    byte[] bytes;
    bytes = Bytes.utf8(s);

    return new InBufferRequestBody(bytes, 0, bytes.length);
  }

}