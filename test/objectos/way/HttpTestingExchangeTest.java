/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HttpTestingExchangeTest {

  @Test
  public void testCase01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.method(Http.GET);

      config.path("/foo");

      config.pathParam("id", "123");

      config.queryParam("page", "1");

      config.set(String.class, "Hello");
    });

    assertEquals(http.method(), Http.GET);
    assertEquals(http.path(), "/foo");
    assertEquals(http.pathParam("id"), "123");
    assertEquals(http.pathParam("path"), null);
    assertEquals(http.queryParam("page"), "1");
    assertEquals(http.queryParam("query"), null);
    assertEquals(http.get(String.class), "Hello");
  }

  @Test(description = """
  Sets:
  - request method
  """)
  public void testCase02() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.method(Http.GET);
    });

    assertEquals(http.method(), Http.GET);
  }

}