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
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeTest4Create {

  @Test
  public void formParam01() {
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.formParam("p1", "abc");

      config.formParam("p2", "val1");
      config.formParam("p2", "val2");
    });

    Web.FormData data;
    data = Web.FormData.parse(http);

    assertEquals(data.get("p1"), "abc");
    assertEquals(data.getAll("p1"), List.of("abc"));
    assertEquals(data.get("p2"), "val1");
    assertEquals(data.getAll("p2"), List.of("val1", "val2"));
  }

  @Test
  public void formParam02() {
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.formParam("i0", Integer.MAX_VALUE);
      config.formParam("i1", Integer.MIN_VALUE);
      config.formParam("l0", Long.MAX_VALUE);
      config.formParam("l1", Long.MIN_VALUE);
    });

    Web.FormData data;
    data = Web.FormData.parse(http);

    assertEquals(data.get("i0"), Integer.toString(Integer.MAX_VALUE));
    assertEquals(data.get("i1"), Integer.toString(Integer.MIN_VALUE));
    assertEquals(data.get("l0"), Long.toString(Long.MAX_VALUE));
    assertEquals(data.get("l1"), Long.toString(Long.MIN_VALUE));
  }

  @Test
  public void header01() {
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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
    Http.Exchange.create(config -> {
      config.header(null, "application/x-www-form-urlencoded");
    });

    Assert.fail("it should have thrown");
  }

  @Test(description = "config.header should reject null values", expectedExceptions = NullPointerException.class)
  public void header03() {
    Http.Exchange.create(config -> {
      config.header(Http.HeaderName.CONTENT_TYPE, null);
    });

    Assert.fail("it should have thrown");
  }

  @Test
  public void header04() {
    Http.HeaderName foo = Http.HeaderName.of("Foo");
    Http.HeaderName name = Http.HeaderName.of("Name");

    Http.Exchange http;
    http = Http.Exchange.create(config -> {
      config.header(foo, "bar");
      config.header(foo, "another bar");
      config.header(name, "some value");
    });

    assertEquals(http.header(foo), "bar");
    assertEquals(http.header(name), "some value");
  }

  private final Http.Handler moduleInterop = Http.Handler.create(routing -> {
    routing.path("/tc01", path -> {
      path.handler(Http.Handler.ofText("TC01", StandardCharsets.UTF_8));
    });
  });

  @Test
  public void moduleInterop01() {
    Http.Exchange http;
    http = http(config -> {
      config.method(Http.Method.GET);

      config.path("/tc01");
    });

    moduleInterop.handle(http);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        TC01\
        """
    );
  }

  @Test
  public void queryParam01() {
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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

    Http.Exchange http;
    http = http(config -> {});

    http.ok(new Template());

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Content-Length: 16\r
        \r
        <div>tc02</div>
        """
    );
  }

  @Test(description = "respond method")
  public void testCase03() {
    class Template extends Html.Template {
      @Override
      protected void render() {
        div("tc02");
      }
    }

    Http.Exchange http;
    http = http(config -> {});

    http.badRequest(new Template());

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Content-Length: 16\r
        \r
        <div>tc02</div>
        """
    );
  }

  @Test(description = "rawPath")
  public void rawPath01() {
    assertEquals(rawPath("/"), "/");
    assertEquals(rawPath("/files"), "/files");
    assertEquals(rawPath("/files/"), "/files/");
    assertEquals(rawPath("/files/s%C3%A3o+paulo.pdf"), "/files/s%C3%A3o+paulo.pdf");
  }

  private String rawPath(String string) {
    Http.Exchange http;
    http = Http.Exchange.create(config -> config.path(string));

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
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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
    Http.Exchange http;
    http = Http.Exchange.create(config -> {
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

  @Test
  public void responseHeader01() {
    Http.Exchange http;
    http = http(config -> {});

    http.respond(Http.Status.OK, new TestingSingleParagraph("foo/bar"), resp -> {
      resp.header(Http.HeaderName.ALLOW, "foo/bar");

      resp.header(Http.HeaderName.USER_AGENT, "Agent 1");
      resp.header(Http.HeaderName.USER_AGENT, "Agent 2");
    });

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Content-Length: 30\r
        Allow: foo/bar\r
        User-Agent: Agent 1\r
        User-Agent: Agent 2\r
        \r
        <html>
        <p>foo/bar</p>
        </html>
        """
    );
  }

  private Http.Exchange http(Consumer<Http.Exchange.Config> more) {
    return Http.Exchange.create(config -> {
      config.clock(TestingClock.FIXED);

      more.accept(config);
    });
  }

}