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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.List;
import objectos.http.Content;
import objectos.http.Handler;
import objectos.http.MediaType;
import objectos.http.Request;
import objectos.http.Result;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HandlerListTest {

  @Test(description = "reject empty list of handlers")
  public void copyOf01() {
    final List<Handler> list;
    list = List.of();

    try {
      HandlerList.copyOf(list);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Handler list must not be empty");
    }
  }

  @Test(description = "accept")
  public void copyOf02() {
    final List<Handler> list;
    list = List.of(HandlerNoop.INSTANCE);

    final HandlerList res;
    res = HandlerList.copyOf(list);

    assertEquals(res.handlers(), list);
  }

  private final Content c01 = Content.of(MediaType.TEXT_PLAIN, "C01");

  private final Content c02 = Content.of(MediaType.TEXT_PLAIN, "C02");

  private final Handler h01 = new HandlerResult(c01);

  private final Handler h02 = new HandlerResult(c02);

  private final Request req = Request.create(_ -> {});

  @Test(description = "single handler")
  public void handle01() {
    final List<Handler> list;
    list = List.of(h01);

    final HandlerList subject;
    subject = HandlerList.copyOf(list);

    final Result res;
    res = subject.handle(req);

    assertSame(res, c01);
  }

  @Test(description = "two handlers: result from first")
  public void handle02() {
    final List<Handler> list;
    list = List.of(h01, h02);

    final HandlerList subject;
    subject = HandlerList.copyOf(list);

    final Result res;
    res = subject.handle(req);

    assertSame(res, c01);
  }

  @Test(description = "two handlers: result from second")
  public void handle03() {
    final List<Handler> list;
    list = List.of(HandlerNoop.INSTANCE, h02);

    final HandlerList subject;
    subject = HandlerList.copyOf(list);

    final Result res;
    res = subject.handle(req);

    assertSame(res, c02);
  }

}
