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

import java.util.Iterator;
import java.util.List;
import objectos.http.Handler;
import objectos.http.HttpStatus;
import objectos.http.RequestMethod;
import objectox.http.req.RequestMethodY;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoutingAtMethodTest {

  @DataProvider
  public Iterator<RequestMethod> methodProvider() {
    return RequestMethodY.iterator();
  }

  @Test(dataProvider = "methodProvider", description = "empty")
  public void testCase01(RequestMethod method) {
    final RoutingAtMethod subject;
    subject = new RoutingAtMethod(method);

    assertEquals(
        subject.build(),

        HandlerNoop.INSTANCE
    );
  }

  @Test(dataProvider = "methodProvider", description = "result -> HandlerResult")
  public void testCase02(RequestMethod method) {
    final RoutingAtMethod subject;
    subject = new RoutingAtMethod(method);

    subject.result(HttpStatus.FORBIDDEN);

    assertEquals(
        subject.build(),

        new HandlerIfMethod(
            method,

            new HandlerResult(HttpStatus.FORBIDDEN)
        )
    );
  }

  @Test(dataProvider = "methodProvider", description = "disallow more than 1 result")
  public void testCase03(RequestMethod method) {
    final RoutingAtMethod subject;
    subject = new RoutingAtMethod(method);

    subject.result(HttpStatus.FORBIDDEN);

    try {
      subject.result(HttpStatus.BAD_REQUEST);

      Assert.fail("It should have thrown");
    } catch (IllegalStateException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "A result has already been set");
    }
  }

  @Test(dataProvider = "methodProvider", description = "1 handler -> handler itself")
  public void testCase04(RequestMethod method) {
    final RoutingAtMethod subject;
    subject = new RoutingAtMethod(method);

    final Handler handler;
    handler = new HandlerResult(HttpStatus.BAD_REQUEST);

    subject.handler(handler);

    assertEquals(
        subject.build(),

        new HandlerIfMethod(
            method,

            handler
        )
    );
  }

  @Test(dataProvider = "methodProvider", description = "Multiple handlers -> HandlerList")
  public void testCase05(RequestMethod method) {
    final RoutingAtMethod subject;
    subject = new RoutingAtMethod(method);

    final Handler h1;
    h1 = new HandlerResult(HttpStatus.BAD_REQUEST);

    subject.handler(h1);

    final Handler h2;
    h2 = new HandlerResult(HttpStatus.NOT_MODIFIED);

    subject.handler(h2);

    assertEquals(
        subject.build(),

        new HandlerIfMethod(
            method,

            new HandlerList(List.of(
                h1,

                h2
            ))
        )
    );
  }

}
