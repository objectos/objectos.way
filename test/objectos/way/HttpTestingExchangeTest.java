/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.util.List;
import objectos.way.Web.FormData;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpTestingExchangeTest {

  @Test
  public void formParam01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.formParam("p1", "abc");

      config.formParam("p2", "val1");
      config.formParam("p2", "val2");
    });

    FormData data;
    data = Web.FormData.parse(http);

    assertEquals(data.get("p1"), "abc");
    assertEquals(data.getAll("p1"), List.of("abc"));
    assertEquals(data.get("p2"), "val1");
    assertEquals(data.getAll("p2"), List.of("val1", "val2"));
  }

  @Test
  public void formParam02() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.formParam("i0", Integer.MAX_VALUE);
      config.formParam("i1", Integer.MIN_VALUE);
      config.formParam("l0", Long.MAX_VALUE);
      config.formParam("l1", Long.MIN_VALUE);
    });

    FormData data;
    data = Web.FormData.parse(http);

    assertEquals(data.get("i0"), Integer.toString(Integer.MAX_VALUE));
    assertEquals(data.get("i1"), Integer.toString(Integer.MIN_VALUE));
    assertEquals(data.get("l0"), Long.toString(Long.MAX_VALUE));
    assertEquals(data.get("l1"), Long.toString(Long.MIN_VALUE));
  }

  @Test
  public void header01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.header(Http.HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded");

      config.header(Http.HeaderName.USER_AGENT, "first");
      config.header(Http.HeaderName.USER_AGENT, "second");
    });

    assertEquals(http.header(Http.HeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
    assertEquals(http.header(Http.HeaderName.CONTENT_LENGTH), null);
    assertEquals(http.header(Http.HeaderName.USER_AGENT), "first");
  }

  @Test(description = "config.header should reject null names", expectedExceptions = NullPointerException.class)
  public void header02() {
    Http.TestingExchange.create(config -> {
      config.header(null, "application/x-www-form-urlencoded");
    });

    Assert.fail("it should have thrown");
  }

  @Test(description = "config.header should reject null values", expectedExceptions = NullPointerException.class)
  public void header03() {
    Http.TestingExchange.create(config -> {
      config.header(Http.HeaderName.CONTENT_TYPE, null);
    });

    Assert.fail("it should have thrown");
  }

  private final Http.Handler moduleInterop = new Http.Module() {
    @Override
    protected final void configure() {
      route("/tc01", handler(this::tc01));
    }

    private void tc01(Http.Exchange http) {
      http.okText("TC01", StandardCharsets.UTF_8);
    }
  }.compile();

  @Test
  public void moduleInterop01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.method(Http.Method.GET);

      config.path("/tc01");
    });

    moduleInterop.handle(http);

    assertEquals(http.responseStatus(), Http.Status.OK);
  }

  @Test
  public void queryParam01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.queryParam("p1", "abc");

      config.queryParam("p2", "val1");
      config.queryParam("p2", "val2");
    });

    assertEquals(http.queryParam("p1"), "abc");
    assertEquals(http.queryParam("p2"), "val1");
    assertEquals(http.queryParam("x"), null);
    assertEquals(http.queryParamAll("p2"), List.of("val1", "val2"));
    assertEquals(http.queryParamAll("x"), List.of());
  }

  @Test
  public void queryParam02() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.queryParam("i0", Integer.MAX_VALUE);
      config.queryParam("i1", Integer.MIN_VALUE);
      config.queryParam("l0", Long.MAX_VALUE);
      config.queryParam("l1", Long.MIN_VALUE);
    });

    assertEquals(http.queryParamAsInt("i0", 0), Integer.MAX_VALUE);
    assertEquals(http.queryParamAsInt("i1", 0), Integer.MIN_VALUE);
    assertEquals(http.queryParamAsLong("l0", 0L), Long.MAX_VALUE);
    assertEquals(http.queryParamAsLong("l1", 0L), Long.MIN_VALUE);
  }

  @Test
  public void testCase01() {
    Http.TestingExchange http;
    http = Http.TestingExchange.create(config -> {
      config.method(Http.Method.GET);

      config.path("/foo");

      config.set(String.class, "Hello");
    });

    assertEquals(http.method(), Http.Method.GET);
    assertEquals(http.path(), "/foo");
    assertEquals(http.pathParam("path"), null);
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
  public void rawPath01() {
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
  public void rawQuery01() {
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