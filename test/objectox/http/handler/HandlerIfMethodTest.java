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
package objectox.http.handler;

import static org.testng.Assert.assertSame;

import objectos.http.Content;
import objectos.http.Handler;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.RequestMethod;
import objectos.http.Result;
import org.testng.annotations.Test;

public class HandlerIfMethodTest {

  private final Content content = Content.of(MediaType.TEXT_PLAIN, "Cond");

  private final Handler handler = new HandlerResult(content);

  @Test(description = "pass-through when condition => false")
  public void handle01() {
    final Request req;
    req = Request.create(opts -> opts.method(RequestMethod.DELETE));

    final Handler subject;
    subject = new HandlerIfMethod(RequestMethod.PATCH, handler);

    final Result res;
    res = subject.handle(req);

    assertSame(res, req);
  }

  @Test(description = "delegate when condition => true")
  public void handle02() {
    final Request req;
    req = Request.create(opts -> opts.method(RequestMethod.DELETE));

    final Handler subject;
    subject = new HandlerIfMethod(RequestMethod.DELETE, handler);

    final Result res;
    res = subject.handle(req);

    assertSame(res, content);
  }

  @Test(description = "accept HEAD when GET")
  public void handle03() {
    final Request req;
    req = Request.create(opts -> opts.method(RequestMethod.HEAD));

    final Handler subject;
    subject = new HandlerIfMethod(RequestMethod.GET, handler);

    final Result res;
    res = subject.handle(req);

    assertSame(res, content);
  }

}
