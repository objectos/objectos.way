/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.util.List;
import java.util.function.Consumer;
import objectos.way.Html;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpExchangeTest {

  @Test
  public void formParam01() {
    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.formParam("p1", "abc");

      config.formParam("p2", "val1");
      config.formParam("p2", "val2");
    });

    assertEquals(http.formParam("p1"), "abc");
    assertEquals(http.formParamAll("p1"), List.of("abc"));
    assertEquals(http.formParam("p2"), "val1");
    assertEquals(http.formParamAll("p2"), List.of("val1", "val2"));
  }

  @Test
  public void formParam02() {
    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.formParam("i0", Integer.MAX_VALUE);
      config.formParam("i1", Integer.MIN_VALUE);
      config.formParam("l0", Long.MAX_VALUE);
      config.formParam("l1", Long.MIN_VALUE);
    });

    assertEquals(http.formParam("i0"), Integer.toString(Integer.MAX_VALUE));
    assertEquals(http.formParam("i1"), Integer.toString(Integer.MIN_VALUE));
    assertEquals(http.formParam("l0"), Long.toString(Long.MAX_VALUE));
    assertEquals(http.formParam("l1"), Long.toString(Long.MIN_VALUE));
  }

  @Test
  public void header01() {
    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.header(HttpHeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded");
      config.header(HttpHeaderName.CONTENT_LENGTH, "0");

      config.header(HttpHeaderName.USER_AGENT, "first");
      config.header(HttpHeaderName.USER_AGENT, "second");
    });

    assertEquals(http.header(HttpHeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
    assertEquals(http.header(HttpHeaderName.CONTENT_LENGTH), "0");
    assertEquals(http.header(HttpHeaderName.USER_AGENT), "first");
  }

  @Test(description = "config.header should reject null names", expectedExceptions = NullPointerException.class)
  public void header02() {
    HttpExchange.create(config -> {
      config.header(null, "application/x-www-form-urlencoded");
    });

    Assert.fail("it should have thrown");
  }

  @Test(description = "config.header should reject null values", expectedExceptions = NullPointerException.class)
  public void header03() {
    HttpExchange.create(config -> {
      config.header(HttpHeaderName.CONTENT_TYPE, null);
    });

    Assert.fail("it should have thrown");
  }

  @Test
  public void header04() {
    HttpHeaderName foo = HttpHeaderName.of("Foo");
    HttpHeaderName name = HttpHeaderName.of("Name");

    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.header(foo, "bar");
      config.header(foo, "another bar");
      config.header(name, "some value");
    });

    assertEquals(http.header(foo), "bar");
    assertEquals(http.header(name), "some value");
  }

  private final HttpHandler moduleInterop = HttpHandler.of(routing -> {
    routing.path("/tc01", path -> {
      path.handler(http -> http.ok(Media.Bytes.textPlain("TC01")));
    });
  });

  @Test
  public void moduleInterop01() {
    HttpExchange http;
    http = http(config -> {
      config.method(HttpMethod.GET);

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
  public void pathParam01() {
    HttpExchange http;
    http = http(config -> {
      config.pathParam("id", "123");
    });

    assertEquals(http.pathParam("id"), "123");
    assertEquals(http.pathParamAsInt("id", 0), 123);
    assertEquals(http.pathParamAsInt("it should return default value", 256), 256);
  }

  @Test
  public void pathParam02() {
    try {
      http(config -> {
        config.pathParam("id", "123");

        config.pathParam("id", "it should throw");
      });

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Duplicate mapping for path parameter id");
    }
  }

  private record User(String login) {}

  @Test
  public void sessionAttr01() {
    HttpExchange http;
    http = http(config -> {
      config.path("/restricted01");

      config.sessionAttr(User.class, new User("foo"));
    });

    final HttpHandler handler;
    handler = this::requireUser;

    handler.handle(http);

    assertEquals(http.toString(), """
    HTTP/1.1 200 OK\r
    Date: Wed, 28 Jun 2023 12:08:43 GMT\r
    Content-Type: text/plain; charset=utf-8\r
    Content-Length: 3\r
    \r
    OK
    """);
  }

  private void requireUser(HttpExchange http) {
    final User user;
    user = http.sessionAttr(User.class);

    if (user == null) {
      http.found("/login");
    } else {
      http.ok(Media.Bytes.textPlain("OK\n"));
    }
  }

  @Test
  public void queryParam01() {
    HttpExchange http;
    http = HttpExchange.create(config -> {
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
    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.queryParam("i0", Integer.MAX_VALUE);
      config.queryParam("i1", Integer.MIN_VALUE);
      config.queryParam("l0", Long.MAX_VALUE);
      config.queryParam("l1", Long.MIN_VALUE);
      config.queryParam("s", "i'm a string");
    });

    assertEquals(http.queryParamAsInt("i0", 0), Integer.MAX_VALUE);
    assertEquals(http.queryParamAsInt("i1", 0), Integer.MIN_VALUE);
    assertEquals(http.queryParamAsInt("s", 123), 123);
    assertEquals(http.queryParamAsInt("x", 123), 123);
    assertEquals(http.queryParamAsInt("i0", () -> 0), Integer.MAX_VALUE);
    assertEquals(http.queryParamAsInt("s", () -> 123), 123);
    assertEquals(http.queryParamAsInt("x", () -> 123), 123);
    assertEquals(http.queryParamAsLong("l0", 0L), Long.MAX_VALUE);
    assertEquals(http.queryParamAsLong("l1", 0L), Long.MIN_VALUE);
    assertEquals(http.queryParamAsLong("s", 123L), 123L);
    assertEquals(http.queryParamAsLong("x", 123L), 123L);
    assertEquals(http.queryParamAsLong("l0", () -> 0L), Long.MAX_VALUE);
    assertEquals(http.queryParamAsLong("s", () -> 123L), 123L);
    assertEquals(http.queryParamAsLong("x", () -> 123L), 123L);
  }

  @Test
  public void testCase01() {
    HttpExchange http;
    http = HttpExchange.create(config -> {
      config.method(HttpMethod.GET);

      config.path("/foo");

      config.set(String.class, "Hello");
    });

    assertEquals(http.method(), HttpMethod.GET);
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

    HttpExchange http;
    http = http(_ -> {});

    http.ok(new Template());

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        010\r
        <div>tc02</div>
        \r
        0\r
        \r
        """
    );
  }

  @Test(description = "respond method")
  public void testCase03() {
    HttpExchange http;
    http = http(_ -> {});

    http.error(HttpStatus.BAD_REQUEST);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 400 Bad Request\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Connection: close\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 16\r
        \r
        400 Bad Request
        """
    );
  }

  @Test(enabled = false, description = "rawPath")
  public void rawPath01() {
    assertEquals(rawPath("/"), "/");
    assertEquals(rawPath("/files"), "/files");
    assertEquals(rawPath("/files/"), "/files/");
    assertEquals(rawPath("/files/s%C3%A3o+paulo.pdf"), "/files/s%C3%A3o+paulo.pdf");
  }

  private String rawPath(String string) {
    HttpExchange http;
    http = HttpExchange.create(config -> config.path(string));

    return http.rawPath();
  }

  @Test(enabled = false, description = "rawQuery")
  public void rawQuery01() {
    assertEquals(rawQuery0(), null);
    assertEquals(rawQuery0("q", "a"), "q=a");
    assertEquals(rawQuery0("q", "a", "foo", "bar"), "q=a&foo=bar");
    assertEquals(rawQuery0("\n", "a", "foo", "\r"), "%0A=a&foo=%0D");
  }

  private String rawQuery0(String... values) {
    HttpExchange http;
    http = HttpExchange.create(config -> {
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

  @Test(enabled = false, description = "rawQueryWith: happy paths")
  public void rawQueryWith01() {
    assertEquals(rawQueryWith("page", "123"), "page=123");
    assertEquals(rawQueryWith("page", "123", "q", "a"), "q=a&page=123");
    assertEquals(rawQueryWith("page", "123", "q", "a", "page", "foo"), "q=a&page=123");
    assertEquals(rawQueryWith("+", "123"), "%2B=123");
    assertEquals(rawQueryWith("page", "+"), "page=%2B");
    assertEquals(rawQueryWith("+", " "), "%2B=%20");
  }

  @Test(enabled = false, description = "rawQueryWith: reject null name",
      expectedExceptions = NullPointerException.class)
  public void rawQueryWith02() {
    rawQueryWith(null, "123");
  }

  @Test(enabled = false, description = "rawQueryWith: reject null value",
      expectedExceptions = NullPointerException.class)
  public void rawQueryWith03() {
    rawQueryWith("page", null);
  }

  @Test
  public void testable01() {
    final Html.Component html = m -> {
      m.testableH1("Testable");
      m.div("ignore me!");
      m.testableH2("Prints only testable");
    };

    final HttpExchange http;
    http = HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      config.testable();
    });

    final HttpHandler handler;
    handler = x -> x.ok(html);

    handler.handle(http);

    assertEquals(
        http.toString(),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/html; charset=utf-8\r
        Transfer-Encoding: chunked\r
        \r
        # Testable

        ## Prints only testable

        """
    );
  }

  private String rawQueryWith(String newName, String newValue, String... values) {
    HttpExchange http;
    http = HttpExchange.create(config -> {
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

  private HttpExchange http(Consumer<? super HttpExchange.Options> more) {
    return HttpExchange.create(config -> {
      config.clock(Y.clockFixed());

      more.accept(config);
    });
  }

}