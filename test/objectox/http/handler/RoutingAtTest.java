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

import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.HttpStatus;
import objectos.http.Redirection;
import objectos.http.RequestMethod;
import objectos.http.Response;
import objectos.http.RouteMatcher;
import objectos.http.RouteMatcherExact;
import org.testng.annotations.Test;

public class RoutingAtTest {

  private final RouteMatcher pathMatcher = new RouteMatcherExact("/");

  private final Redirection redir = Redirection.movedPermanently("/");

  private final Response resp = Response.create(opts -> {
    opts.status(HttpStatus.OK);
  });

  private final Handler create(Consumer<? super RoutingAt> opts) {
    final RoutingAt at;
    at = RoutingAt.of("/");

    opts.accept(at);

    return at.build();
  }

  @Test(description = "at('/')")
  public void testCase01() {
    assertEquals(
        create(_ -> {}),

        HandlerNoop.INSTANCE
    );
  }

  @Test(description = "at('/', redir)")
  public void testCase02() {
    assertEquals(
        create(opts -> {
          opts.option(redir);
        }),

        new HandlerIfPath(
            pathMatcher,

            new HandlerResult(redir)
        )
    );
  }

  @Test(description = "at('/', POST, resp)")
  public void testCase03() {
    assertEquals(
        create(opts -> {
          opts.option(RequestMethod.POST);

          opts.option(resp);
        }),

        new HandlerIfPath(
            pathMatcher,

            new HandlerIfMethod(
                RequestMethod.POST,

                new HandlerResult(resp)
            )
        )
    );
  }

  @Test(description = "at('/', GET, handler)")
  public void testCase04() {
    final Handler handler;
    handler = new HandlerResult(resp);

    assertEquals(
        create(opts -> {
          opts.option(RequestMethod.GET);

          opts.option(handler);
        }),

        new HandlerIfPath(
            pathMatcher,

            new HandlerIfMethod(
                RequestMethod.GET,

                handler
            )
        )
    );
  }

}
