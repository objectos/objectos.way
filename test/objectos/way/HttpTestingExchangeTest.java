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

import java.nio.charset.StandardCharsets;
import org.testng.Assert;
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
  Invalid:
  - request method
  """)
  public void testCase02() {
    try {
      Http.TestingExchange.create(config -> {
        config.method((byte) (Http.CONNECT - 1));
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }

    try {
      Http.TestingExchange.create(config -> {
        config.method((byte) (Http.TRACE + 1));
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

  @Test(description = "Lang.CharWritable response")
  public void testCase03() {
    class Template extends Html.Template {
      @Override
      protected void render() {
        div("tc03");
      }
    }

    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {});

    http.ok(new Template());

    assertEquals(http.responseStatus(), Http.OK);

    Lang.CharWritable body;
    body = (Lang.CharWritable) http.responseBody();

    assertEquals(
        body.toString(),

        """
        <div>tc03</div>
        """
    );

    assertEquals(http.responseCharset(), StandardCharsets.UTF_8);
  }

}