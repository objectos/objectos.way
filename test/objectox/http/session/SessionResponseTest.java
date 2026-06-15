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

import java.util.HashMap;
import java.util.List;
import objectos.http.HttpHeaderName;
import objectos.http.HttpStatus;
import objectos.http.Request;
import objectos.way.Y;
import objectos.y.RandomGeneratorY;
import objectox.http.Header;
import objectox.http.HttpToken;
import objectox.http.resp.ResponsePojo;
import org.testng.annotations.Test;

public class SessionResponseTest {

  @Test(description = "request has no session -> noop")
  public void accept01() {
    final Request request;
    request = Request.create(_ -> {});

    assertEquals(request.sessionPresent(), false);

    final ResponsePojo response;
    response = ResponsePojo.create0(opts -> {
      opts.status(HttpStatus.OK);
    });

    final List<Header> headers;
    headers = response.headers();

    assertEquals(headers.size(), 0);

    final SessionResponse sr;
    sr = SessionResponseY.create(opts -> {
      opts.sessionSetCookie = SessionSetCookieY.create(set -> {
        set.name = "WAY";
      });
    });

    sr.accept(request, response);

    assertEquals(headers.size(), 0);
  }

  @Test(description = "request has session, response is error -> noop")
  public void accept02() {
    final Request request;
    request = Request.create(opts -> {
      opts.sessionAttr(String.class, "foo");
    });

    assertEquals(request.sessionPresent(), true);

    final ResponsePojo response;
    response = ResponsePojo.create0(opts -> {
      opts.status(HttpStatus.NOT_FOUND);
    });

    final List<Header> headers;
    headers = response.headers();

    assertEquals(headers.size(), 0);

    final SessionResponse sr;
    sr = SessionResponseY.create(opts -> {
      opts.sessionSetCookie = SessionSetCookieY.create(set -> {
        set.name = "WAY";
      });
    });

    sr.accept(request, response);

    assertEquals(headers.size(), 0);
  }

  @Test(description = "lazy session is not initialized -> noop")
  public void accept03() {
    final Request request;
    request = Request.create(_ -> {});

    request.attr(Session.KEY, new SessionLazy());

    assertEquals(request.sessionPresent(), false);

    final ResponsePojo response;
    response = ResponsePojo.create0(opts -> {
      opts.status(HttpStatus.OK);
    });

    final List<Header> headers;
    headers = response.headers();

    assertEquals(headers.size(), 0);

    final SessionResponse sr;
    sr = SessionResponseY.create(opts -> {
      opts.sessionSetCookie = SessionSetCookieY.create(set -> {
        set.name = "WAY";
      });
    });

    sr.accept(request, response);

    assertEquals(headers.size(), 0);
  }

  @Test(description = "lazy session initialized -> Set-Cookie")
  public void accept04() {
    final Request request;
    request = Request.create(_ -> {});

    final SessionLazy lazy;
    lazy = new SessionLazy();

    lazy.attr(String.class, "foo");

    request.attr(Session.KEY, lazy);

    assertEquals(request.sessionPresent(), true);
    assertEquals(request.sessionAttr(String.class), "foo");

    final ResponsePojo response;
    response = ResponsePojo.create0(opts -> {
      opts.status(HttpStatus.OK);
    });

    final List<Header> headers;
    headers = response.headers();

    assertEquals(headers.size(), 0);

    final SessionResponse sr;
    sr = SessionResponseY.create(opts -> {
      opts.sessionFactory = SessionFactoryY.create(y -> {
        y.instantSource = Y.clockFixed();

        y.randomGenerator = RandomGeneratorY.ofLongs(4, 5, 5, 6);

        y.sessions = new HashMap<>();
      });

      opts.sessionSetCookie = SessionSetCookieY.create(set -> {
        set.name = "WAY";
      });
    });

    sr.accept(request, response);

    assertEquals(headers.size(), 1);

    final Header header;
    header = headers.get(0);

    assertEquals(header.name(), HttpHeaderName.SET_COOKIE);
    assertEquals(header.value(), "WAY=" + HttpToken.of32(4, 5, 5, 6).toString());
  }

}
