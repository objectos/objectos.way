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

import static org.testng.Assert.assertSame;

import org.testng.annotations.Test;

public class HandlerTest {

  @Test
  public void create01() {
    final Handler h;
    h = Handler.create(_ -> {});

    final Request req;
    req = Request.create(_ -> {});

    final Result res;
    res = h.handle(req);

    assertSame(res, req);
  }

  @Test
  public void create02() {
    final Redirection redir;
    redir = Redirection.movedPermanently("/index.html");

    final Handler h;
    h = Handler.create(opts -> {
      opts.at("/", redir);
    });

    final Request req1;
    req1 = Request.create(opts -> {
      opts.path("/");
    });

    final Result res1;
    res1 = h.handle(req1);

    assertSame(res1, redir);

    final Request req2;
    req2 = Request.create(opts -> {
      opts.path("/other");
    });

    final Result res2;
    res2 = h.handle(req2);

    assertSame(res2, req2);
  }

  @Test
  public void of() {
    final Handler h;
    h = _ -> Status.NOT_FOUND;

    final Handler res;
    res = Handler.of(h);

    assertSame(res, h);
  }

}
