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
import objectos.http.HttpStatus;
import objectos.http.Redirection;
import objectos.http.Response;
import org.testng.annotations.Test;

public class RouteBuilderTest {

  private final RouteMatcher matcher = new RouteMatcherExact("/");

  private final Redirection redir = Redirection.movedPermanently("/");

  private final Response resp = Response.create(opts -> {
    opts.status(HttpStatus.OK);
  });

  private final Route create(Consumer<? super RouteBuilder> opts) {
    final RouteBuilder builder;
    builder = new RouteBuilder(matcher);

    opts.accept(builder);

    return builder.build();
  }

  @Test(description = "add(redir)")
  public void addRedirect01() {
    final Route subject;
    subject = create(r -> {
      r.addRedirect(redir);
    });

    assertEquals(subject.matcher(), matcher);

    assertEquals(subject.handler(), new HandlerResult(redir));
  }

  @Test(description = "add(response)")
  public void addResponse01() {
    final Route subject;
    subject = create(r -> {
      r.addResponse(resp);
    });

    assertEquals(subject.matcher(), matcher);

    assertEquals(subject.handler(), new HandlerResult(resp));
  }

}
