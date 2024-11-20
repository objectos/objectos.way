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
      config.method(Http.Method.GET);

      config.path("/foo");

      config.pathParam("id", "123");

      config.queryParam("page", "1");

      config.set(String.class, "Hello");
    });

    assertEquals(http.method(), Http.Method.GET);
    assertEquals(http.path(), "/foo");
    assertEquals(http.pathParam("id"), "123");
    assertEquals(http.pathParam("path"), null);
    assertEquals(http.queryParam("page"), "1");
    assertEquals(http.queryParam("query"), null);
    assertEquals(http.get(String.class), "Hello");
  }

  @Test(description = "Html.Template response")
  public void testCase02() {
    class Template extends Html.Template {
      @Override
      protected void render() {
        div("tc02");
      }
    }

    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {});

    http.ok(new Template());

    assertEquals(http.responseStatus(), Http.Status.OK);

    Html.Template body;
    body = (Html.Template) http.responseBody();

    assertEquals(
        body.toString(),

        """
        <div>tc02</div>
        """
    );
  }

  @Test(description = "rawPath")
  public void rawPath() {
    assertEquals(rawPath("/"), "/");
    assertEquals(rawPath("/files"), "/files");
    assertEquals(rawPath("/files/"), "/files/");
    assertEquals(rawPath("/files/são paulo.pdf"), "/files/s%C3%A3o+paulo.pdf");
    assertEquals(rawPath("/files/são paulo.pdf/"), "/files/s%C3%A3o+paulo.pdf/");
  }

  private String rawPath(String string) {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> config.path(string));

    return http.rawPath();
  }

  @Test(description = "rawQuery")
  public void rawQuery() {
    assertEquals(rawQuery0(), null);
    assertEquals(rawQuery0("q", "a"), "q=a");
    assertEquals(rawQuery0("q", "a", "foo", "bar"), "q=a&foo=bar");
    assertEquals(rawQuery0("@", "a", "foo", "~"), "%40=a&foo=%7E");
  }

  private String rawQuery0(String... values) {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      for (int i = 0; i < values.length;) {
        String name;
        name = values[i++];

        String value;
        value = values[i++];

        config.queryParam(name, value);
      }
    });

    return http.rawQuery();
  }

  @Test(description = "rawQueryWith: happy paths")
  public void rawQueryWith01() {
    assertEquals(rawQueryWith("page", "123"), "page=123");
    assertEquals(rawQueryWith("page", "123", "q", "a"), "q=a&page=123");
    assertEquals(rawQueryWith("page", "123", "q", "a", "page", "foo"), "q=a&page=123");
    assertEquals(rawQueryWith("@", "123"), "%40=123");
    assertEquals(rawQueryWith("page", "@"), "page=%40");
    assertEquals(rawQueryWith("@", "~"), "%40=%7E");
  }

  @Test(description = "rawQueryWith: reject null name",
      expectedExceptions = NullPointerException.class)
  public void rawQueryWith02() {
    rawQueryWith(null, "123");
  }

  @Test(description = "rawQueryWith: reject empty name",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "name must not be blank")
  public void rawQueryWith03() {
    rawQueryWith("", "123");
  }

  @Test(description = "rawQueryWith: reject blank name",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "name must not be blank")
  public void rawQueryWith04() {
    rawQueryWith("   ", "123");
  }

  @Test(description = "rawQueryWith: reject null value",
      expectedExceptions = NullPointerException.class)
  public void rawQueryWith05() {
    rawQueryWith("page", null);
  }

  private String rawQueryWith(String newName, String newValue, String... values) {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      for (int i = 0; i < values.length;) {
        String name;
        name = values[i++];

        String value;
        value = values[i++];

        config.queryParam(name, value);
      }
    });

    return http.rawQueryWith(newName, newValue);
  }

}