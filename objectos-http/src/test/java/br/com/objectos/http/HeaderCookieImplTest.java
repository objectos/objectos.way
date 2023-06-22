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
package br.com.objectos.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HeaderCookieImplTest {

  @Test
  public void testCase01() {
    HeaderCookieImpl result;
    result = parse("foo=bar\r\n");

    assertEquals(result.getCookieName(), "foo");

    assertEquals(result.getCookieValue(), "bar");
  }

  @Test
  public void testCase02() {
    HeaderCookieImpl result;
    result = parse("JSESSIONID=node017a67odnfx7ou1o1fc332v3q4a8.node0\r\n");

    assertEquals(result.getCookieName(), "JSESSIONID");

    assertEquals(result.getCookieValue(), "node017a67odnfx7ou1o1fc332v3q4a8.node0");
  }

  private HeaderCookieImpl parse(String source) {
    HeaderCookieImpl header;
    header = new HeaderCookieImpl();

    return HeaderTesting.parse(header, source);
  }

}