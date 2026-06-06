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
import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

public class SessionRequestTest {

  @Test(description = "load existing")
  public void accept01() {
    final HttpToken id;
    id = HttpToken.of32(3, 3, 3, 3);

    final SessionPojo pojo;
    pojo = SessionPojoY.of();

    final SessionRequest sr;
    sr = SessionRequestY.create(opts -> {
      opts.cookieName = "WAY";

      opts.session(id, pojo);
    });

    final Request req;
    req = Request.create(opts -> {
      opts.header(HttpHeaderName.COOKIE, "WAY=" + id.toString());
    });

    assertEquals(req.sessionPresent(), false);

    sr.accept(req);

    assertEquals(req.sessionPresent(), true);

    final SessionPojo session;
    session = (SessionPojo) req.attr(Session.KEY);

    assertSame(session, pojo);
  }

  @Test(description = "return lazy session")
  public void accept02() {
    final SessionRequest sr;
    sr = SessionRequestY.create(opts -> {
      opts.cookieName = "WAY";
    });

    final Request req;
    req = Request.create(_ -> {});

    assertEquals(req.sessionPresent(), false);

    sr.accept(req);

    assertEquals(req.sessionPresent(), false);

    final Session session;
    session = req.attr(Session.KEY);

    assertEquals(session instanceof SessionLazy, true);
  }

}
