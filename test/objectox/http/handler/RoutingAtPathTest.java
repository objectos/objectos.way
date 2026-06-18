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
import java.util.Set;
import objectos.http.Handler;
import objectos.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RoutingAtPathTest {

  @Test(description = "empty")
  public void testCase01() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

    assertEquals(
        subject.build(),

        HandlerNoop.INSTANCE
    );
  }

  @Test(description = "result -> HandlerResult")
  public void testCase02() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

    subject.result(HttpStatus.FORBIDDEN);

    assertEquals(
        subject.build(),

        new HandlerRoute(
            segments,

            new HandlerResult(HttpStatus.FORBIDDEN)
        )
    );
  }

  @Test(description = "disallow more than 1 result")
  public void testCase03() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

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

  @Test(description = "1 handler -> handler itself")
  public void testCase04() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

    final Handler handler;
    handler = new HandlerResult(HttpStatus.BAD_REQUEST);

    subject.handler(handler);

    assertEquals(
        subject.build(),

        new HandlerRoute(
            segments,

            handler
        )
    );
  }

  @Test(description = "Multiple handlers -> HandlerList")
  public void testCase05() {
    final Set<String> paramNames;
    paramNames = Set.of();

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

    final Handler h1;
    h1 = new HandlerResult(HttpStatus.BAD_REQUEST);

    subject.handler(h1);

    final Handler h2;
    h2 = new HandlerResult(HttpStatus.NOT_MODIFIED);

    subject.handler(h2);

    assertEquals(
        subject.build(),

        new HandlerRoute(
            segments,

            new HandlerList(List.of(
                h1,

                h2
            ))
        )
    );
  }

  @Test(description = "matched path param")
  public void testCase06() {
    final Set<String> paramNames;
    paramNames = Set.of("foo");

    final List<Segment> segments;
    segments = List.of(new SegmentExact("/test"), new SegmentParamLast("foo"));

    final PathExpression expression;
    expression = new PathExpression(paramNames, segments);

    final RoutingAtPath subject;
    subject = new RoutingAtPath(expression);

    subject.pathParamNamed(new PathParamNamed("foo", PathParamPredicates.DIGITS));

    subject.result(HttpStatus.BAD_REQUEST);

    assertEquals(
        subject.build(),

        new HandlerRoute(
            List.of(new SegmentExact("/test"), new SegmentParamLast("foo", PathParamPredicates.DIGITS)),

            new HandlerResult(HttpStatus.BAD_REQUEST)
        )
    );
  }

}
