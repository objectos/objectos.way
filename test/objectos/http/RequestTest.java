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
import objectox.http.RequestMethodEnum;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RequestTest {

  @Test
  public void formParam01() {
    Request http;
    http = Request.create(config -> {
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
    Request http;
    http = Request.create(config -> {
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
    Request http;
    http = Request.create(config -> {
      config.header(HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded");
      config.header(HeaderName.CONTENT_LENGTH, "0");

      config.header(HeaderName.USER_AGENT, "first");
      config.header(HeaderName.USER_AGENT, "second");
    });

    assertEquals(http.header(HeaderName.CONTENT_TYPE), "application/x-www-form-urlencoded");
    assertEquals(http.header(HeaderName.CONTENT_LENGTH), "0");
    assertEquals(http.header(HeaderName.USER_AGENT), "first");
  }

  @Test(description = "config.header should reject null names", expectedExceptions = NullPointerException.class)
  public void header02() {
    Request.create(config -> {
      config.header(null, "application/x-www-form-urlencoded");
    });

    Assert.fail("it should have thrown");
  }

  @Test(description = "config.header should reject null values", expectedExceptions = NullPointerException.class)
  public void header03() {
    Request.create(config -> {
      config.header(HeaderName.CONTENT_TYPE, null);
    });

    Assert.fail("it should have thrown");
  }

  @Test
  public void header04() {
    HeaderName foo = HeaderName.of("Foo");
    HeaderName name = HeaderName.of("Name");

    Request http;
    http = Request.create(config -> {
      config.header(foo, "bar");
      config.header(foo, "another bar");
      config.header(name, "some value");
    });

    assertEquals(http.header(foo), "bar");
    assertEquals(http.header(name), "some value");
  }

  private final Content content = Content.of(MediaType.TEXT_PLAIN, "TC01");

  private final Handler moduleInterop = Handler.create(r -> {
    r.at("/tc01", content);
  });

  @Test
  public void moduleInterop01() {
    final Request http;
    http = http(config -> {
      config.method(RequestMethodEnum.GET);

      config.path("/tc01");
    });

    final Result res;
    res = moduleInterop.handle(http);

    assertEquals(
        res,

        content
    );
  }

  @Test
  public void pathParam01() {
    Request http;
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
    Request http;
    http = http(config -> {
      config.path("/restricted01");

      config.sessionAttr(User.class, new User("foo"));
    });

    final Handler handler;
    handler = this::requireUser;

    final Result res;
    res = handler.handle(http);

    assertEquals(res, Content.of(MediaType.TEXT_PLAIN, "OK\n"));
  }

  private Result requireUser(Request http) {
    final User user;
    user = http.sessionAttr(User.class);

    if (user == null) {
      return Redirection.found("/login");
    } else {
      return Content.of(MediaType.TEXT_PLAIN, "OK\n");
    }
  }

  @Test
  public void queryParam01() {
    Request http;
    http = Request.create(config -> {
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
    Request http;
    http = Request.create(config -> {
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
    Request http;
    http = Request.create(config -> {
      config.method(RequestMethodEnum.GET);

      config.path("/foo");

      config.attr(String.class, "Hello");
    });

    assertEquals(http.method(), RequestMethodEnum.GET);
    assertEquals(http.path(), "/foo");
    assertEquals(http.pathParam("path"), null);
    assertEquals(http.attr(String.class), "Hello");
  }

  private Request http(Consumer<? super RequestOptions> more) {
    return Request.create(config -> {
      more.accept(config);
    });
  }

}