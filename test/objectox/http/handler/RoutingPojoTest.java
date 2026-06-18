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
import java.util.List;
import objectos.http.Handler;
import objectos.http.Status;
import objectos.http.PathParam;
import objectos.http.Redirection;
import objectos.http.RequestMethod;
import org.testng.annotations.Test;

public class RoutingPojoTest {

  private final Handler handler = new HandlerResult(Status.BAD_REQUEST);

  private final Redirection redir = Redirection.movedPermanently("/index.html");

  @Test(description = "() => HandlerNoop")
  public void testCase01() {
    assertEquals(
        RoutingPojo.create0(_ -> {}),

        HandlerNoop.INSTANCE
    );
  }

  @Test(description = "('/', redir) => HandlerResult(redir)")
  public void testCase02() {
    assertEquals(
        RoutingPojo.create0(r -> {
          r.at("/", redir);
        }),

        new HandlerRoute(
            List.of(new SegmentExact("/")),

            new HandlerResult(redir)
        )
    );
  }

  @Test(description = "('/movie/{id}', digits('id'), GET, handler)")
  public void testCase03() {
    assertEquals(
        RoutingPojo.create0(r -> {
          r.at("/movie/{id}",
              PathParam.digits("id"),
              RequestMethod.GET, handler);
        }),

        new HandlerRoute(
            List.of(new SegmentRegion("/movie/"), new SegmentParamLast("id", PathParamPredicates.DIGITS)),

            new HandlerIfMethod(
                RequestMethod.GET,

                handler
            )
        )
    );
  }

}
