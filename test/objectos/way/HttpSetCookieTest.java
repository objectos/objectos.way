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

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HttpSetCookieTest {

  @Test(description = "Tests basic cookie with name and value only")
  public void testCase01() {
    test(
        config -> {
          config.name("session");
          config.value("abc123");
        },

        "session=abc123"
    );
  }

  @Test(description = "Tests cookie with HttpOnly flag")
  public void testCase02() {
    test(
        config -> {
          config.name("my-cookie");
          config.value("cookie-value");
          config.httpOnly();
        },

        "my-cookie=cookie-value; HttpOnly"
    );
  }

  @Test(description = "Tests cookie with multiple attributes including path and domain")
  public void testCase03() {
    test(
        config -> {
          config.name("user");
          config.value("john-doe");
          config.path("/api");
          config.domain("example.com");
          config.httpOnly();
        },

        "user=john-doe; Domain=example.com; HttpOnly; Path=/api"
    );
  }

  @Test(description = "Tests cookie with maxAge using Duration")
  public void testCase04() {
    test(
        config -> {
          config.name("temp");
          config.value("xyz789");
          config.maxAge(Duration.ofHours(1));
        },

        "temp=xyz789; Max-Age=3600"
    );
  }

  @Test(description = "Tests secure cookie with all common attributes")
  public void testCase05() {
    test(
        config -> {
          config.name("auth");
          config.value("token123");
          config.secure();
          config.httpOnly();
          config.path("/");
          config.domain(".app.example.com");
          config.maxAge(Duration.ofDays(1));
        },

        "auth=token123; Domain=.app.example.com; HttpOnly; Max-Age=86400; Path=/; Secure"
    );
  }

  @Test(description = "Tests cookie with expires using ZonedDateTime")
  public void testCase06() {
    test(
        config -> {
          config.name("persistent");
          config.value("val123");

          final ZonedDateTime expires;
          expires = ZonedDateTime.of(2025, 12, 31, 23, 59, 59, 0, ZoneOffset.UTC);

          config.expires(expires);
        },

        "persistent=val123; Expires=Wed, 31 Dec 2025 23:59:59 GMT"
    );
  }

  @Test(description = "Tests cookie with SameSite attribute")
  public void testCase07() {
    test(
        config -> {
          config.name("sso");
          config.value("token456");
          config.sameSite(Http.SetCookie.SameSite.STRICT);
        },

        "sso=token456; SameSite=Strict"
    );
  }

  @Test(description = "Tests cookie with multiple SameSite options")
  public void testCase08() {
    test(
        config -> {
          config.name("login");
          config.value("user789");
          config.sameSite(Http.SetCookie.SameSite.LAX);
          config.secure();
        },

        "login=user789; SameSite=Lax; Secure"
    );
  }

  @Test(description = "Tests failure when name is not provided")
  public void testCase09() {
    testIAE(
        config -> {
          config.value("orphan-value");
        },

        "Cookie name and value are required"
    );
  }

  @Test(description = "Tests failure when value is not provided")
  public void testCase10() {
    testIAE(
        config -> {
          config.name("orphan-name");
        },

        "Cookie name and value are required"
    );
  }

  @Test(description = "Tests failure when neither name nor value is provided")
  public void testCase11() {
    testIAE(
        config -> {
          config.httpOnly();
          config.secure();
        },

        "Cookie name and value are required"
    );
  }

  private void test(Consumer<Http.SetCookie.Config> config, String expected) {
    final Http.SetCookie setCookie;
    setCookie = Http.SetCookie.create(config);

    assertEquals(setCookie.toString(), expected);
  }

  private void testIAE(Consumer<Http.SetCookie.Config> config, String expectedMessage) {
    try {
      Http.SetCookie.create(config);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), expectedMessage);
    }
  }

}