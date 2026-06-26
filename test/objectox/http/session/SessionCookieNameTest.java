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
package objectox.http.session;

import static org.testng.Assert.assertEquals;

import objectox.http.Rfc;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SessionCookieNameTest {

  @Test(description = "reject empty name")
  public void validate01() {
    try {
      final SessionCookieName subject;
      subject = new SessionCookieName("");

      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Cookie name must not be blank");
    }
  }

  @Test(description = "reject blank name")
  public void validate02() {
    try {
      final SessionCookieName subject;
      subject = new SessionCookieName(" \t ");

      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Cookie name must not be blank");
    }
  }

  @Test(description = "single invalid char")
  public void validate03() {
    try {
      final SessionCookieName subject;
      subject = new SessionCookieName("COOKIE{ID");

      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(
          msg,

          """
          Cookie name must only contain the following characters:
          \t"!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
          \tDIGIT (US-ASCII) / ALPHA (US-ASCII)
          """
      );
    }
  }

  @Test(description = "multiple invalid chars")
  public void validate04() {
    try {
      final SessionCookieName subject;
      subject = new SessionCookieName("{COOKIE}");

      subject.validate();

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(
          msg,

          """
          Cookie name must only contain the following characters:
          \t"!" / "#" / "$" / "%" / "&" / "'" / "*" / "+" / "-" / "." / "^" / "_" / "`" / "|" / "~"
          \tDIGIT (US-ASCII) / ALPHA (US-ASCII)
          """
      );
    }
  }

  @Test(description = "accept")
  public void validate05() {
    final String name;
    name = Rfc.tchar();

    final SessionCookieName subject;
    subject = new SessionCookieName(name);

    assertEquals(subject.validate(), name);
  }

}
