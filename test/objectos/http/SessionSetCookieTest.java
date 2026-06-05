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

import java.util.function.Consumer;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SessionSetCookieTest {

  @DataProvider
  public Object[][] validProvider() {
    return new Object[][] {
        {
            c(opts -> {
              opts.name = "WAY";
            }),

            "WAY=abc"
        },
        {
            c(opts -> {
              opts.name = "WAY";

              opts.path = "/";
            }),

            "WAY=abc; Path=/"},
        {
            c(opts -> {
              opts.name = "WAY";

              opts.path = "/";

              opts.httpOnly = true;

              opts.secure = true;
            }),

            "WAY=abc; HttpOnly; Path=/; Secure"
        },
        {
            c(opts -> {
              opts.name = "WAY";

              opts.path = "/";

              opts.httpOnly = true;

              opts.sameSite = Http.SameSite.STRICT;

              opts.secure = true;
            }),

            "WAY=abc; HttpOnly; Path=/; SameSite=Strict; Secure"
        }

    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(Consumer<? super SessionSetCookieY> opts, String expected) {
    final SessionSetCookie setCookie;
    setCookie = SessionSetCookieY.create(opts);

    assertEquals(
        setCookie.forString("abc"),

        expected
    );
  }

  private Consumer<? super SessionSetCookieY> c(Consumer<? super SessionSetCookieY> opts) {
    return opts;
  }

}
